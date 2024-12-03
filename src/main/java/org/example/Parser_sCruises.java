package org.example;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.firefox.FirefoxDriver;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Set;

public class Parser_sCruises {
    public void Course(String url){
        System.out.println("Okay, let's go...");
        WebDriver webDriver = new FirefoxDriver();

        try(BufferedWriter writer = new BufferedWriter(new FileWriter("sozvezdie.txt", true))) {
            webDriver.get(url);

            List<WebElement> cruiseItems = webDriver.findElements(By.cssSelector(".schedule-item mb_2rem "));
            for (WebElement item : cruiseItems){
                try{
                    ArrayList<String> city = new ArrayList<>();
                    ArrayList<String> timeIn = new ArrayList<>();
                    ArrayList<String> timeOut = new ArrayList<>();
                    ArrayList<String> timeDay = new ArrayList<>();

                    WebElement purchaseButton = item.findElement(By.cssSelector(".mt-0 mb-4 lh_1-2 fz_xl .a"));
                    String purchaseLink = purchaseButton.getAttribute("href");
                    ((JavascriptExecutor) webDriver).executeScript("window.open('" + purchaseLink + "', '_blank');");
                    String originalTab = webDriver.getWindowHandle();
                    Set<String> allTabs = webDriver.getWindowHandles();
                    for (String tab : allTabs) {
                        if (!tab.equals(originalTab)) {
                            webDriver.switchTo().window(tab);
                            break;
                        }
                    }
                    Thread.sleep(5000);

                } catch (NoSuchElementException e){
                    System.err.println("Any elements not search for cruise. Skip...");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("Data successful update in cruises.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            webDriver.quit();
        }
    }

}
