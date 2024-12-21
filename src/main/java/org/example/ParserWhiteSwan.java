package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParserWhiteSwan {

    public void Course(String url, String fileName) throws RuntimeException {
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--headless");
        WebDriver webDriver = new FirefoxDriver(options);
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
        Format format = new Format();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName,true))) {
            webDriver.get(url);
            System.out.println("Okay, let's go");
            WebElement raspship = webDriver.findElement(By.className("raspship"));
            List<WebElement> rows = raspship.findElements(By.tagName("tr"));
            WebElement teplohod = webDriver.findElement(By.tagName("h1"));
            String nameTeplohod = teplohod.getText();
            List<String> hrefs = new ArrayList<>();
            for (WebElement row : rows) {

                // Извлечение ссылок из строки
                List<WebElement> links = row.findElements(By.tagName("a"));
                for (WebElement link : links) {
                    String href = link.getAttribute("href") + "#tabs-2";
                    if(!hrefs.contains(href)){
                        hrefs.add(href);
                        continue;
                    }
                }
            }
            for(String href : hrefs){

                ArrayList<String> city = new ArrayList<>();
                ArrayList<String> timeIn = new ArrayList<>();
                ArrayList<String> timeOut = new ArrayList<>();
                ArrayList<String> timeDay = new ArrayList<>();

                timeIn.add("");

                ((JavascriptExecutor) webDriver).executeScript("window.open('" + href + "', '_blank');");
                String originalTab = webDriver.getWindowHandle();
                Set<String> allTabs = webDriver.getWindowHandles();
                for (String tab : allTabs) {
                    if (!tab.equals(originalTab)) {
                        webDriver.switchTo().window(tab);
                        break;
                    }
                }
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("tabs-2")));
                Thread.sleep(300);

                WebElement main = webDriver.findElement(By.id("tabs-2"));
                WebElement firstRow = main.findElement(By.className("eks"));
                WebElement tbody = firstRow.findElement(By.tagName("tbody"));
                List<WebElement> cells = tbody.findElements(By.tagName("tr"));
                int ch=0;
                boolean bool = false;
                System.out.println(cells.size());
                for(WebElement cell : cells) {
                    List<WebElement> cellsq = cell.findElements(By.tagName("td"));

                    // Дата (из первой ячейки)
                    int intCell = 0;
                    String rawDate;
                    if(cellsq.size()==3){
                        rawDate = cellsq.get(intCell).getText();
                        timeDay.add(rawDate.split("\n")[1]+".2025");
                        intCell++;
                    } else {
                        timeDay.add(timeDay.getLast());
                    }


                    // Город и время (из второй ячейки)
                    String rawCityAndTime = cellsq.get(intCell).getText();
                    String[] cityAndTimeParts = rawCityAndTime.split("\n");
                    if("День на борту".equals(cityAndTimeParts[0])){
                        continue;
                    }
                    city.add(cityAndTimeParts[0]);
                    String timeRange = cityAndTimeParts[1].replace("отправление ", "").replace("прибытие ","");

                    // Вывод данных
                    System.out.println("Дата: " + timeDay.get(ch));
                    Pattern pattern = Pattern.compile("(\\d{2}:\\d{2})-(\\d{2}:\\d{2})");
                    Matcher matcher = pattern.matcher(timeRange);

                    if (matcher.matches()) {
                        String startTime = matcher.group(1);
                        String endTime = matcher.group(2);

                        // Вывод результатов
                        timeIn.add(startTime);
                        timeOut.add(endTime);
                    } else {
                        if(!bool){
                            timeOut.add(timeRange);
                        } else{
                            timeIn.add(timeRange);
                        }
                        bool = true;
                    }
                ch++;
                }
                timeOut.add("");
                format.FormatVodohodStopFromTXT(nameTeplohod, href, timeDay, city, timeIn, timeOut, writer);
                webDriver.close();
                webDriver.switchTo().window(originalTab);
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            webDriver.manage().deleteAllCookies();
            webDriver.quit();
            System.out.println("Strange, My Lord");
        }
    }

}