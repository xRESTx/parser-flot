package org.example;

import java.util.*;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.firefox.FirefoxDriver;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;

public class Parser {
    public void Course1(String url) {
        System.out.println("Начинаем парсинг данных...");
        Format format = new Format();
        WebDriver webDriver = new FirefoxDriver();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("cruises.txt",true))) {

            webDriver.get(url);
            List<WebElement> cruiseItems = webDriver.findElements(By.cssSelector(".d-flex.flex-wrap.gap-3.justify-content-center"));

            for (WebElement item : cruiseItems) {
                try {
                    WebElement description = item.findElement(By.className("catalog-section-item-description-preview"));
                    String cruiseDescription = description.getText();

                    WebElement dates = item.findElement(By.className("cruise-properties-dates"));
                    String cruiseDates = dates.getText();

                    WebElement purchaseButton = item.findElement(By.cssSelector(".catalog-section-item-name-wrapper"));
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
                    Thread.sleep(5000); // Задержка для полной загрузки страницы

                    List<WebElement> teplohodItem = webDriver.findElements(By.className("catalog-element-information-teplohod"));
                    String cruiseName = "";
                    for (WebElement item2 : teplohodItem){
                        WebElement name = item2.findElement(By.cssSelector(".catalog-element-information-teplohod a"));
                        cruiseName = name.getText();
                    }


                    List<WebElement> routeDays = webDriver.findElements(By.className("catalog-element-information-route-day"));
                    String firstDay = "", lastDay = "";

                    if (!routeDays.isEmpty()) {
                        try {
                            WebElement firstDayElement = routeDays.get(0).findElement(By.cssSelector(".time-table span"));
                            firstDay = firstDayElement.getText();

                            // Кликаем для раскрытия данных
                            WebElement lastToggleLink = routeDays.get(routeDays.size() - 1).findElement(By.className("toggle-link"));

                            //Скролим
                            ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView({block: 'center', behavior: 'smooth'});", lastToggleLink);

                            // Ожидание загрузки данных о последнем дне после клика
                            Thread.sleep(2500);
                            lastToggleLink.click();
                            List<WebElement> routeDay = webDriver.findElements(By.className("catalog-element-information-route-day"));
                            WebElement lastDayElement = routeDay.get(routeDays.size() - 1).findElement(By.cssSelector(".time-table span"));
                            ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", lastDayElement);
                            lastDay = lastDayElement.getText();

                        } catch (Exception e) {
                            System.out.println("Ошибка при извлечении данных: " + e.getMessage());
                        }
                    }

                    // Записываем данные в файл
                    format.FormatFromTXT(cruiseName, cruiseDescription, cruiseDates, purchaseLink, firstDay, lastDay, writer);


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
            System.out.println("Данные успешно записаны в файл cruises.txt");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            webDriver.quit();
        }
    }

    public void Course2(String url) {
        System.out.println("Начинаем парсинг данных...");
        Format format = new Format();
        WebDriver webDriver = new FirefoxDriver();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("cruises.txt",true))) {

            webDriver.get(url);
            List<WebElement> cruiseItems = webDriver.findElements(By.cssSelector(".d-flex.flex-wrap.gap-3.justify-content-center"));

            for (WebElement item : cruiseItems) {
                try {
                    WebElement description = item.findElement(By.className("catalog-section-item-description-preview"));
                    String cruiseDescription = description.getText();
//                    НАХУЙ НЕ НАДО
//                    WebElement dates = item.findElement(By.className("cruise-properties-dates"));
//                    String cruiseDates = dates.getText();

                    WebElement purchaseButton = item.findElement(By.cssSelector(".catalog-section-item-name-wrapper"));
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
                    Thread.sleep(5000); // Задержка для полной загрузки страницы

                    WebElement teplohodItem = webDriver.findElement(By.className("catalog-element-information-teplohod"));
                    String cruiseName = teplohodItem.getText();

                    ArrayList<String> city = new ArrayList<>();
                    ArrayList<String> timeIn = new ArrayList<>();
                    ArrayList<String> timeOut = new ArrayList<>();
                    List<WebElement> routeDays = webDriver.findElements(By.cssSelector(".catalog-element-information-route-day"));
                    String firstDay = "", lastDay = "";
                    if (!routeDays.isEmpty()) {
                        try {
                            WebElement firstDayElement = routeDays.get(0).findElement(By.cssSelector(".toggle-link"));
                            firstDay = firstDayElement.getText();

                            firstDayElement = routeDays.get(0).findElement(By.cssSelector(".time-table span"));
                            firstDay = firstDayElement.getText();
                            for(int numberDay = 1; numberDay<routeDays.size(); numberDay++){
                                // Указатель на клик
                                WebElement ToggleLink = routeDays.get(numberDay).findElement(By.className("toggle-link"));
                                //Скролим
                                ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView({block: 'center', behavior: 'smooth'});", ToggleLink);
                                // Ожидание загрузки данных о последнем дне после клик
                                Thread.sleep(500);
                                // Кликаем для раскрытия данных
                                ToggleLink.click();
                                ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", ToggleLink);
                            }

                            List<WebElement> FirstElement = webDriver.findElements(By.cssSelector(".catalog-element-information-route-day"));
                            for(WebElement second : FirstElement){
                                List<WebElement> elements = second.findElements(By.cssSelector(".toggle-link"));
                                for (WebElement element : elements) {
                                    WebElement check = second.findElement(By.cssSelector(".time-table"));
                                    if(check.getText()==""){
                                        System.out.println("Всё хуйня");
                                        continue;
                                    }
                                    String temp = element.getText();

                                    String[] parts = temp.split("\n");
                                    String[] part = parts[0].split(": ");
                                    String par = part[1];

                                    String dateStr = parts[1].split(",")[0];
                                    String formattedResult = String.format("%s", dateStr);
                                    List<WebElement> timeSpan = second.findElements(By.cssSelector(".time-table p"));
                                    List<WebElement> grid = second.findElements(By.cssSelector(".intec-grid.intec-grid-wrap.intec-grid-i-10"));
                                    if(grid.size()>1){
                                        List<WebElement> intec = second.findElements(By.cssSelector(".intec-grid-item-1.city-name"));
                                        for(WebElement inte : intec){
                                            city.add(inte.getText());
                                            System.out.println("Город: " + inte.getText());
                                            }
                                        for(WebElement spanElement : grid){
                                            WebElement span = spanElement.findElement(By.cssSelector(".time-table"));
                                            List<WebElement> pspan = span.findElements(By.tagName("p"));
                                            for(WebElement pspa : pspan){
                                                String temp1 = pspa.getText();
                                                System.out.println(temp1+"ЭТО 1");
                                                String temp2 = temp1.split("\n")[0];
                                                String temp3 = temp1.split("\n")[1];
                                                System.out.println(temp2.length());
                                                if (temp2.length() == 13) {
                                                    System.out.println("Начало круиза: " + temp3);
                                                    timeOut.add(formattedResult + " " + temp3);
                                                    System.out.println(formattedResult + " " + temp3);
                                                    timeIn.add(" ");
                                                }
                                                if (temp2.length() == 8) {
                                                    System.out.println("Прибытие: " + temp3);
                                                    timeIn.add(formattedResult + " " + temp3);
                                                    System.out.println(formattedResult + " " + temp3);
                                                }
                                                if (temp2.length() == 11) {
                                                    if(temp3.length()==17) {
                                                        city.remove(city.size()-1);
                                                        continue;
                                                    }
                                                    System.out.println("Отправление: " + temp3);
                                                    timeOut.add(formattedResult + " " + temp3);
                                                    System.out.println(formattedResult + " " + temp3);
                                                }
                                                if (temp2.length() == 17) {
                                                    System.out.println("Завершение круиза: " + temp3);
                                                    timeIn.add(formattedResult + " " + temp3);
                                                    System.out.println(formattedResult + " " + temp3);
                                                    timeOut.add(" ");
                                                }
                                            }
                                        }
                                    } else {
                                        city.add(par);
                                        System.out.println("Город: " + par);
                                        for(WebElement spanElement : timeSpan){
                                            String temp1 = spanElement.getText();
                                            String temp2 = temp1.split("\n")[0];
                                            String temp3 = temp1.split("\n")[1];
                                            if (temp2.length() == 13) {
                                                System.out.println("Начало круиза: " + temp3);
                                                timeOut.add(formattedResult + " " + temp3);
                                                timeIn.add(" ");
                                                System.out.println(formattedResult + " " + temp3);
                                            }
                                            if (temp2.length() == 8) {
                                                System.out.println("Прибытие: " + temp3);
                                                timeIn.add(formattedResult + " " + temp3);
                                                System.out.println(formattedResult + " " + temp3);
                                            }
                                            if (temp2.length() == 11) {
                                                if(temp3.length()==17) {
                                                    city.remove(city.size()-1);
                                                    continue;
                                                }
                                                System.out.println("Отправление: " + temp3);
                                                timeOut.add(formattedResult + " " + temp3);
                                                System.out.println(formattedResult + " " + temp3);
                                            }
                                            if (temp2.length() == 17) {
                                                System.out.println("Завершение круиза: " + temp3);
                                                timeIn.add(formattedResult + " " + temp3);
                                                System.out.println(formattedResult + " " + temp3);
                                                timeOut.add(" ");
                                            }
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {
                            System.out.println("Ошибка при извлечении данных: " + e.getMessage());
                        }
                    }

                    // Записываем данные в файл
                    format.FormatDonInturStopFromTXT(cruiseName, purchaseLink, city, timeIn , timeOut,  writer);

                    webDriver.close();
                    webDriver.switchTo().window(originalTab);

                } catch (NoSuchElementException e) {
                    System.err.println("Некоторые элементы не найдены для одного из круизов. Пропускаем...");
                }
            }
            System.out.println("Данные успешно записаны в файл cruises.txt");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            webDriver.quit();
        }
    }

}