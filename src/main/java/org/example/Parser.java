package org.example;

import java.util.List;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.FileWriter;
import java.io.IOException;
import java.util.NoSuchElementException;

public class Parser {
    public void Course1(String args){
        System.out.println("ну чтож, начнем");
        WebDriver webDriver = new FirefoxDriver();
        webDriver.get(args);

        List<WebElement> elements = webDriver.findElements(By.className("catalog-section-item-image-look"));
        for (WebElement element : elements) {
            String hrefValue = element.getAttribute("href");
            System.out.println("Значение href: " + hrefValue);
            webDriver.get(hrefValue);
            WebElement element = webDriver.findElements(By.className("catalog-element-description catalog-element-description-preview mt-4"));
        }

        webDriver.quit();
    }
}
