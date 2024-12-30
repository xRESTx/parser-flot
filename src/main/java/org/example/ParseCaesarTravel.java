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

public class ParseCaesarTravel {
    public void Course(String url, String fileName) throws RuntimeException {
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--headless");
        WebDriver webDriver = new FirefoxDriver();
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
        Format format = new Format();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName,true))) {
            webDriver.get(url);
            System.out.println("Okay, let's go");
            WebElement raspship = webDriver.findElement(By.id("cruises"));
            List<WebElement> rows = raspship.findElements(By.className("block_criuse_right"));

            WebElement teplohod = webDriver.findElement(By.tagName("h1"));
            String nameTeplohod = teplohod.getText();

            List<String> hrefs = new ArrayList<>();
            for (WebElement row : rows) {
                // Извлечение ссылок из строки
                List<WebElement> links = row.findElements(By.tagName("a"));
                for (WebElement link : links) {
                    String href = link.getAttribute("href");
                    if(!hrefs.contains(href)){
                        hrefs.add(href);
                        continue;
                    }
                }
            }

            for(String href : hrefs){

                System.out.println(href);
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
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("nav-tabs")));

                WebElement navigation = webDriver.findElement(By.className("nav-tabs"));
                List<WebElement> click = navigation.findElements(By.tagName("li"));

                click.getLast().click();

                WebElement main = webDriver.findElement(By.className("tab-content"));
                List<WebElement> container = main.findElements(By.id("excursion"));
                if(container.isEmpty()){
                    Thread.sleep(500);
                    container = main.findElements(By.id("excursion"));
                }
                WebElement tbody = container.getFirst().findElement(By.tagName("tbody"));
                List<WebElement> cells = tbody.findElements(By.tagName("tr"));
                boolean firstDay = true;
                boolean first = true;

                for(WebElement cell : cells) {
                    boolean xh = true;
                    if(first){
                        first=false;
                        continue;
                    }
                    boolean second = false;
                    int tdSize = 0;

                    List<WebElement> cellsq = cell.findElements(By.tagName("td"));
                    boolean date = true;
                    boolean firsttd = true;
                    for(WebElement p : cellsq){

                        if(tdSize==1) {
                            break;
                        }
                        if(!date){
                            date = true;
                            continue;
                        }
                        List<WebElement> pIDs = p.findElements(By.tagName("p"));
                        if(pIDs.isEmpty()) continue;

                        for(WebElement pId: pIDs){
                            List<WebElement> strong = pId.findElements(By.tagName("strong"));
                            System.out.println(strong.size());
                            if(strong.isEmpty()) continue;
                            if(firsttd){
                                firsttd=false;
                                continue;
                            }
                            if((pIDs.size() == 2) && (xh)) {
                                xh = false;
                                city.add(strong.getFirst().getText());
                                date = false;
                            }
                            if(date){
                                timeDay.add(strong.getLast().getText());
                            }
                        }

                        if(!second){
                            second = true;
                            continue;
                        }
                        String timeRangePattern = "(\\d{2}:\\d{2})-(\\d{2}:\\d{2})";

                        Pattern pattern = Pattern.compile(timeRangePattern);
                        Matcher matcher = pattern.matcher(pIDs.getLast().getText());
                        System.out.println(pIDs.getLast().getText());
                        if (matcher.find()) {
                            String startTime = matcher.group(1);
                            String endTime = matcher.group(2);
                            timeIn.add(startTime);
                            timeOut.add(endTime);
                        } else {
                            String timePattern = "\\d{2}:\\d{2}";

                            Pattern patter = Pattern.compile(timePattern);
                            Matcher matche = patter.matcher(pIDs.getLast().getText());

                            if (matche.find()) {
                                String time = matche.group();
                                if(firstDay){
                                    firstDay = false;
                                    timeOut.add(time);
                                }
                                else timeIn.add(time);
                            }
                        }
                        tdSize++;
                    }
                }
                timeOut.add("");
                System.out.println(timeDay.size() + "" + timeIn.size()+ "" + timeOut.size()+"" + city.size());
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

