package org.example;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class Main {
    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\REST\\Downloads\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
        Parser parser = new Parser();
        parser.Course1("https://doninturflot.com/catalog/date-from-01.01.2025-to-31.12.2025/ship-shevthenko_aleksandra/");
    }
}