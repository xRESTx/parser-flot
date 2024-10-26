package org.example;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class Main {
    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "E:\\ProjectJava\\parser-flot\\chromedriver-win64\\chromedriver.exe");
        Parser parser = new Parser();
        parser.Course1();
    }
}