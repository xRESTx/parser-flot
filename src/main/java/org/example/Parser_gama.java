package org.example;

import java.util.List;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.firefox.FirefoxDriver;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.NoSuchElementException;

public class Parser_gama {
    public void Course(String url) {
        System.out.println("Начинаем парсинг данных...");
        Format format = new Format();
        WebDriver webDriver = new FirefoxDriver();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("cruises.txt",true))) {

            webDriver.get(url);
            List<WebElement> cruiseItems = webDriver.findElements(By.cssSelector(".in-actual"));

            for (WebElement item : cruiseItems) {
                try {

                    WebElement description = item.findElement(By.className("in-way-text"));
                    String cruiseDescription = description.getText();

                    WebElement dates = item.findElement(By.cssSelector(".in-dt span"));
                    String cruiseDates = dates.getText();

                    List<WebElement> dateDivs = item.findElements(By.cssSelector(".in-dt div"));
                    String startDate = "";
                    String endDate = "";
                    if (dateDivs.size() >= 2) {
                        startDate = dateDivs.get(0).getText().replace("с ", "");
                        endDate = dateDivs.get(1).getText();
                    }
                    WebElement purchaseButton = item.findElement(By.cssSelector(".btn.btn-sm.btn-gama-action"));
                    String purchaseLink = purchaseButton.getAttribute("href");

                    WebElement name = item.findElement(By.cssSelector(".in-ship-txt a"));
                    String cruiseName = name.getText();

                    // Записываем данные в файл
                    Format.FormatGamaFromTXT(cruiseName, cruiseDescription, purchaseLink, startDate, endDate, writer);

                    System.out.println("Название судна: " + cruiseName);
                    System.out.println("Описание круиза: " + cruiseDescription);
                    System.out.println("Даты круиза: " + cruiseDates);
                    System.out.println("Ссылка на покупку: " + purchaseLink);
                    System.out.println("Первый день: " + startDate);
                    System.out.println("Последний день: " + endDate);
                    System.out.println("------------------------------------");

                } catch (NoSuchElementException e) {
                    System.err.println("Некоторые элементы не найдены для одного из круизов. Пропускаем...");
                }
            }
            System.out.println("Данные успешно записаны в файл cruises.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            webDriver.quit();
        }
    }
}
