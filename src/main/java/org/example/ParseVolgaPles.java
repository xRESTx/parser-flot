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

public class ParseVolgaPles {
    public void Course(String url, String fileName) {
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--headless");
        WebDriver webDriver = new FirefoxDriver(options);
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
        Format format = new Format();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName,true))) {
            webDriver.get(url);
            System.out.println("Okay, let's go");
            List<WebElement> elemets = webDriver.findElements(By.cssSelector(".route-towns"));
            WebElement teplohod = webDriver.findElement(By.tagName("h1"));
            String nameTeplohod = teplohod.getText();
            for(WebElement elemet : elemets){

                ArrayList<String> city = new ArrayList<>();
                ArrayList<String> timeIn = new ArrayList<>();
                ArrayList<String> timeOut = new ArrayList<>();
                ArrayList<String> timeDay = new ArrayList<>();

                String strhref  = elemet.getAttribute("href");
                ((JavascriptExecutor) webDriver).executeScript("window.open('" + strhref + "', '_blank');");
                String originalTab = webDriver.getWindowHandle();
                Set<String> allTabs = webDriver.getWindowHandles();
                for (String tab : allTabs) {
                    if (!tab.equals(originalTab)) {
                        webDriver.switchTo().window(tab);
                        break;
                    }
                }
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("content")));

                WebElement main = webDriver.findElement(By.id("content"));
                List<WebElement> firstRow = main.findElements(By.cssSelector("tbody"));
                List<WebElement> cells = firstRow.getFirst().findElements(By.tagName("tr"));
                for(WebElement cell : cells){
                    List<WebElement> tds = cell.findElements(By.tagName("td"));
                    if(tds.isEmpty()){
                        continue;
                    }
                    else {
                        int check = 0;
                        for(WebElement td : tds){
                            String element = td.getText();
                            switch (check){
                                case 0:
                                    city.add(element);
                                    check++;
                                    break;
                                case 1:
                                    element = element.replaceAll("\\s*\\[.*?\\]", "");
                                    timeDay.add(element);
                                    check++;
                                    break;
                                case 2:
                                    timeIn.add(element);
                                    check++;
                                    break;
                                case 3:
                                    check++;
                                    break;
                                case 4:
                                    timeOut.add(element);
                                    check++;
                                    break;
                            }
                        }
                    }
                }
                for(String str : city){
                    System.out.println(str);
                }

                format.FormatVodohodStopFromTXT(nameTeplohod, strhref, timeDay, city, timeIn, timeOut, writer);
                webDriver.close();
                webDriver.switchTo().window(originalTab);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            webDriver.manage().deleteAllCookies();
            webDriver.quit();
            System.out.println("Strange, My Lord");
        }
    }

}
