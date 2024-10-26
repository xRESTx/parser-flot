package org.example;

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

public class Parser {
    public void Course1(String url) {
        System.out.println("Начинаем парсинг данных...");


        WebDriver webDriver = new FirefoxDriver();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("cruises.txt"))) {

            writer.write("Название круиза,Описание круиза,Даты круиза,Ссылка на покупку,Первый день,Последний день\n");

            webDriver.get(url);

            List<WebElement> cruiseItems = webDriver.findElements(By.cssSelector(".d-flex.flex-wrap.gap-3.justify-content-center"));

            for (WebElement item : cruiseItems) {
                try {
                    WebElement name = item.findElement(By.className("catalog-section-item-name-wrapper"));
                    String cruiseName = name.getText();

                    WebElement description = item.findElement(By.className("catalog-section-item-description-preview"));
                    String cruiseDescription = description.getText();

                    WebElement dates = item.findElement(By.className("cruise-properties-dates"));
                    String cruiseDates = dates.getText();

                    WebElement purchaseButton = item.findElement(By.cssSelector(".catalog-section-item-purchase-button.catalog-section-item-purchase-button-detail.intec-button"));
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
                    try {
                        Thread.sleep(5000);  // Задержка в 5 секунд
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    List<WebElement> routeDays = webDriver.findElements(By.className("catalog-element-information-route-day"));

                    String firstDay = "", lastDay = "";

                    if (!routeDays.isEmpty()) {
                        WebElement firstDayElement = routeDays.get(0).findElement(By.cssSelector(".time-table span"));
                        firstDay = firstDayElement.getText();

                        WebElement lastDayElement = routeDays.get(routeDays.size() - 1).findElement(By.cssSelector(".time-table span"));
                        lastDay = lastDayElement.getText();
                    }


                    writer.write(String.format("\"%s\"\t,\"%s\"\t,\"%s\"\t,\"%s\"\t,\"%s\"\t,\"%s\"\n", cruiseName, cruiseDescription, cruiseDates, purchaseLink, firstDay, lastDay));

                    System.out.println("Название круиза: " + cruiseName);
                    System.out.println("Описание круиза: " + cruiseDescription);
                    System.out.println("Даты круиза: " + cruiseDates);
                    System.out.println("Ссылка на покупку: " + purchaseLink);
                    System.out.println("Первый день: " + firstDay);
                    System.out.println("Последний день: " + lastDay);
                    System.out.println("------------------------------------");

                    webDriver.close();
                    webDriver.switchTo().window(originalTab);


                } catch (NoSuchElementException e) {
                    System.err.println("Некоторые элементы не найдены для одного из круизов. Пропускаем...");
                }
            }
            System.out.println("Данные успешно записаны в файл cruises.csv");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            webDriver.quit();
        }
    }
}
