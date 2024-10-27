package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.List;

public class Parser_gama {
    public void Course(String url) {
        System.out.println("Начинаем парсинг данных...");
        WebDriver webDriver = new FirefoxDriver();
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));  // Используем Duration для таймаута
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("cruises.txt", true))) {
            webDriver.get(url);
            Thread.sleep(2000);

            // Считываем информацию с текущей страницы
            extractInfoAndCheckButtons(writer, webDriver, wait);

            // Проверяем наличие ссылок на города и кликаем на них, если они есть
            List<WebElement> cityLinks = webDriver.findElements(By.cssSelector("div.alert.alert-warning.alert-icon-block.alert-dismissible a"));
            if (!cityLinks.isEmpty()) {
                System.out.println("Количество кнопок городов: " + cityLinks.size());
                for (WebElement cityLink : cityLinks) {
                    System.out.println("Кликаем на город: " + cityLink.getText());
                    cityLink.click();
                    Thread.sleep(2500);

                    // Считываем информацию после клика на город
                    extractInfoAndCheckButtons(writer, webDriver, wait);

                    // Возвращаемся назад, чтобы кликнуть следующую ссылку
                    webDriver.navigate().back();
                    Thread.sleep(2000);
                    // Обновляем список ссылок, так как после навигации они могут измениться
                    cityLinks = webDriver.findElements(By.cssSelector("div.alert.alert-warning.alert-icon-block.alert-dismissible a"));
                }
            } else {
                System.out.println("Ссылки на города не найдены. Информация считана с текущей страницы.");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            webDriver.quit();
        }
    }

    private void extractInfoAndCheckButtons(BufferedWriter writer, WebDriver webDriver, WebDriverWait wait) throws IOException, InterruptedException {
        // Считываем информацию с текущей страницы
        extractInfoAndWriteToFile(writer, webDriver);

        // Проверяем наличие кнопок с датами и кликаем на них, если они есть
        List<WebElement> monthButtons = webDriver.findElements(By.cssSelector("div.in-li"));
        if (!monthButtons.isEmpty()) {
            System.out.println("Найдено " + monthButtons.size() + " кнопок.");
            for (WebElement button : monthButtons) {
                String buttonText = button.getText();
                if (buttonText.matches(".*\\d{4}.*")) {  // Проверяем, содержит ли текст дату (год)
                    System.out.println("Кликаем на: " + buttonText);
                    button.click();

                    // Явное ожидание загрузки информации о маршруте
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.in-actual")));

                    // Считываем информацию после клика на кнопку
                    extractInfoAndWriteToFile(writer, webDriver);

                    // Возвращаемся назад, чтобы кликнуть следующую кнопку
                    webDriver.navigate().back();
                    Thread.sleep(2000);
                }
            }
        } else {
            System.out.println("Кнопки с датами не найдены.");
        }
    }

    private void extractInfoAndWriteToFile(BufferedWriter writer, WebDriver webDriver) throws IOException {
        List<WebElement> infoBlocks = webDriver.findElements(By.cssSelector("div.in-actual"));
        if (!infoBlocks.isEmpty()) {
            for (WebElement block : infoBlocks) {
                try {
                    // Извлекаем текст даты
                    WebElement dateElement = block.findElement(By.cssSelector("div.in-dt"));
                    String dateText = dateElement.getText();
                    System.out.println("Дата: " + dateText);

                    // Извлекаем текст маршрута
                    WebElement wayTextElement = block.findElement(By.cssSelector("div.in-way-text"));
                    String wayText = wayTextElement.getText();
                    System.out.println("Маршрут: " + wayText);

                    // Извлекаем ссылку на бронирование
                    WebElement bookingLinkElement = block.findElement(By.cssSelector("a[href*='/booking/river/']"));
                    String bookingLink = bookingLinkElement.getAttribute("href");
                    System.out.println("Ссылка на бронирование: " + bookingLink);

                    // Записываем информацию в файл
                    writer.write("Дата: " + dateText + "\n");
                    writer.write("Маршрут: " + wayText + "\n");
                    writer.write("Ссылка на бронирование: " + bookingLink + "\n");
                    writer.write("-----------\n");
                } catch (Exception e) {
                    System.out.println("Не удалось найти некоторые элементы в блоке.");
                }
            }
        } else {
            System.out.println("Блоки с информацией не найдены.");
        }
    }
}