package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Parser_Vodohod {
    public void Course(String url, String fileName) {
        WebDriver webDriver = new FirefoxDriver();
        Format format = new Format();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName,true))) {
            webDriver.get(url);
            System.out.println("Okay, let's go");
            List<WebElement> elemets = webDriver.findElements(By.cssSelector(".p-content__inner__wrapper a"));
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
                Thread.sleep(5000);
                WebElement button = webDriver.findElement(By.className("b-rc__view-all-btn"));
                ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView({block: 'center', behavior: 'smooth'});", button);
                ((JavascriptExecutor)webDriver).executeScript("arguments[0].click();", button);
                List<WebElement> check = webDriver.findElements(By.cssSelector(".b-rc__view-all-btn.b-rc__view-all-btn--hide"));
                while(check.isEmpty()){
                    System.out.println("Tvar', 30 minut iz-za taby vebal");
                    ((JavascriptExecutor)webDriver).executeScript("arguments[0].click();", button);
                    check = webDriver.findElements(By.cssSelector(".b-rc__view-all-btn.b-rc__view-all-btn--hide"));
                }
                Thread.sleep(1000);
                WebElement teplohod = webDriver.findElement(By.cssSelector(".booking__left-item.booking__left-ship"));
                WebElement pteplohod = teplohod.findElement(By.cssSelector(".booking__i-value"));
                String nameTeplohod = pteplohod.getText();

                List<WebElement> days = webDriver.findElements(By.cssSelector(".b-spoiler__content"));
                for (WebElement day : days){
                    List<WebElement> bdays = day.findElements(By.cssSelector(".b-day"));
                    int condition = 0;
                    for(WebElement bday : bdays){
                        timeIn.add("00:00");
                        timeOut.add("00:00");
                        WebElement acity = bday.findElement(By.cssSelector(".body-content__text-title-main.link"));
                        city.add(acity.getText());
                        List<WebElement> dates = bday.findElements(By.cssSelector(".b-day__grid-item"));

                        for(WebElement date : dates){
                            String temp1 = date.getText();
                            System.out.println(temp1);

                            String[] parts = temp1.split("\n");
                            String temp2 = parts[0];
                            String temp3 = parts.length > 1 ? parts[1] : "00:00";

                            if (temp2.length() == 5) {
                                temp3 = temp3.split(" ")[0];
                                timeDay.add(temp3);
                                System.out.println("Date: " + timeDay.get(condition));
                            }
                            if (temp2.length() == 9) {
                                timeIn.removeLast();
                                timeIn.addLast(temp3);
                                System.out.println("timeIn: " + timeIn.get(condition));
                            }
                            if (temp2.length() == 12) {

                                timeOut.removeLast();
                                timeOut.addLast(temp3);
                                System.out.println("timeOut: " + timeOut.get(condition));
                            }
                        }
                        condition++;
                    }
                }
                Thread.sleep(3000);
                format.FormatVodohodInturStopFromTXT(nameTeplohod, strhref, timeDay, city, timeIn, timeOut, writer);
                webDriver.close();
                webDriver.switchTo().window(originalTab);
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            webDriver.manage().deleteAllCookies();
            webDriver.quit();
            System.out.println("Strange huinya, My Lord");
        }
    }
}
