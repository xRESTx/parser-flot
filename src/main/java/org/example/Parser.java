package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;


public class Parser {


    public void Course1(String args){
        Scanner in = new Scanner(System.in);
        WebDriver driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
        driver.manage().window().maximize();
        try {
            // Переход на страницу с морскими рейсами
            driver.get(args); // Укажите локальный путь к файлу или URL сайта

            // Установка времени ожидания
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            // Находим элементы, представляющие каждый морской рейс
            List<WebElement> cruises = driver.findElements(By.cssSelector(".catalog-section-item-purchase-button catalog-section-item-purchase-button-detail intec-button")); // Измените на актуальный селектор


                String name = driver.findElements(By.cssSelector(".catalog-section-item-purchase-button catalog-section-item-purchase-button-detail intec-button")).toString(); // Название рейса


                System.out.println("Название рейса: " + name);
                System.out.println("-----------------------");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            int n = in.nextInt();
            driver.quit();

        }
    }
}
