package org.example;

import java.util.List;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.FileWriter;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Parser {
    public void Course1(String args){
        Scanner in = new Scanner(System.in);
        System.out.println("ну чтож, начнем");
        WebDriver webDriver = new FirefoxDriver();


        try {
            webDriver.get(args);

            // Находим все элементы с классом d-flex flex-wrap gap-3 justify-content-center
            List<WebElement> cruiseItems = webDriver.findElements(By.cssSelector(".d-flex.flex-wrap.gap-3.justify-content-center"));

            for (WebElement item : cruiseItems) {
                // Находим название круиза
                WebElement name = item.findElement(By.className("catalog-section-item-name-wrapper"));
                String cruiseName = name.getText();

                // Описание круиза
                WebElement description = item.findElement(By.className("catalog-section-item-description-preview"));
                String cruiseDescription = description.getText();

                // Даты круиза
                WebElement dates = item.findElement(By.className("cruise-properties-dates"));
                String cruiseDates = dates.getText();

                // Ссылка на покупку
                WebElement purchaseButton = item.findElement(By.cssSelector(".catalog-section-item-purchase-button.catalog-section-item-purchase-button-detail.intec-button"));
                String purchaseLink = purchaseButton.getAttribute("href");

                // Выводим собранные данные
                System.out.println("Название круиза: " + cruiseName);
                System.out.println("Описание круиза: " + cruiseDescription);
                System.out.println("Даты круиза: " + cruiseDates);
                System.out.println("Ссылка на покупку: " + purchaseLink);
                System.out.println("------------------------------------");
            }
        } finally {
            // Закрываем браузер
            webDriver.quit();
        }
        webDriver.quit();
    }
}
