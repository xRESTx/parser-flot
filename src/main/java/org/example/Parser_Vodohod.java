package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public class Parser_Vodohod {
    public void Course(String url) {
        WebDriver webDriver = new FirefoxDriver();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("vodohod.txt",true))) {
            webDriver.get(url);
            Thread.sleep(2000);
            System.out.println("Okay, let's go");
            List<WebElement> elemets = webDriver.findElements(By.cssSelector(".p-content__inner__wrapper a"));
            for(WebElement elemet : elemets){
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



                webDriver.close();
                webDriver.switchTo().window(originalTab);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            System.out.println("Strange huinya, My Lord");
        }
    }
}
