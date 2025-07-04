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

public class Parser_SputnikGermes {
    public void Course(String url, String fileName) throws RuntimeException {
        FirefoxOptions options = new FirefoxOptions();
//        options.addArguments("--headless");
        WebDriver webDriver = new FirefoxDriver(options);
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
        try {
            webDriver.get(url);
            System.out.println("Okay, let's go");
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("blockfind")));
            List<WebElement> hferWebEL = webDriver.findElements(By.className("blockfind"));
            String nameTeplohod = webDriver.findElements(By.className("name_teplohod")).getFirst().getText();
            String lastHref = hferWebEL.getLast().getAttribute("idtur");
            while(true){
                ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", hferWebEL.getLast());
                Thread.sleep(1000);
                hferWebEL =  webDriver.findElements(By.className("blockfind"));
                if(lastHref.equals(hferWebEL.getLast().getAttribute("idtur"))) {
                    break;
                }
                lastHref = hferWebEL.getLast().getAttribute("idtur");
            }

            List<String> hrefs = new ArrayList<>();

            for(WebElement hferWeb : hferWebEL){
                String idtreValue = hferWeb.getAttribute("idtur");
                hrefs.add(idtreValue);
            }

            for(String href : hrefs){
                href = "https://river.sputnik-germes.ru/1lib/ExcelReport/get_program_tur.php?id_tur=" + href;
                System.out.println(href);

                ArrayList<String> city = new ArrayList<>();
                ArrayList<String> timeIn = new ArrayList<>();
                ArrayList<String> timeOut = new ArrayList<>();
                ArrayList<String> timeDay = new ArrayList<>();


                ((JavascriptExecutor) webDriver).executeScript("window.open('" + href + "', '_blank');");
                String originalTab = webDriver.getWindowHandle();
                Set<String> allTabs = webDriver.getWindowHandles();
                for (String tab : allTabs) {
                    if (!tab.equals(originalTab)) {
                        webDriver.switchTo().window(tab);
                        break;
                    }
                }
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("tbody")));

                WebElement tbody = webDriver.findElement(By.id("Tway"));
                List<WebElement> cells = tbody.findElements(By.tagName("tr"));
                boolean first = true;
                for(WebElement cell : cells) {
                    if(first){
                        first = false;
                        continue;
                    }
                    List<WebElement> tds = cell.findElements(By.tagName("td"));
                    city.add(tds.get(0).getText());

                    timeIn.add(tds.get(2).getText());
                    String timeOrDay = tds.get(4).getText();

                    Pattern pattern = Pattern.compile("(\\d{2}:\\d{2})\\s*\\((\\d{2}\\.\\d{2})\\)");
                    Matcher matcher = pattern.matcher(timeOrDay);
                    if(matcher.find()){
                        String time = matcher.group(1);
                        String date = matcher.group(2) + ".2025";

                        timeOut.add(time);
                        timeDay.add(date);
                    }
                    else{
                        timeOut.add(tds.get(4).getText());
                        if(!tds.get(1).getText().equals("")){
                            timeDay.add(tds.get(1).getText()+".2025");
                        }else{
                            timeDay.add("");
                        }
                    }
                }
                System.out.println(timeDay.size() + "" + timeIn.size()+ "" + timeOut.size()+"" + city.size());
                for (String time : timeIn){
                    System.out.println(time);
                }
                ExcelSaver.FormatSputnikGermesFromTXT(nameTeplohod, href, timeDay, city, timeIn, timeOut, fileName);
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
