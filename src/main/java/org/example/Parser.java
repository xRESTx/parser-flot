package org.example;

import java.util.List;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.By;

public class Parser {
    public void Course1(){
        System.setProperty("webdriver.chrome.driver", "E:\\ProjectJava\\parser-flot\\chromedriver-win64\\chromedriver.exe");
        WebDriver webDriver = new ChromeDriver();
        webDriver.get("https://doninturflot.com/catalog/date-from-01.01.2025-to-31.12.2025/ship-shevthenko_aleksandra/");
        System.out.println(webDriver.getTitle());


        webDriver.quit();
    }
}
