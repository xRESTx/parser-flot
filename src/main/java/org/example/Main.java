package org.example;


public class Main {
    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "chromedriver-win64\\chromedriver.exe");
        Parser parser = new Parser();
        parser.Course1("https://doninturflot.com/catalog/date-from-01.01.2025-to-31.12.2025/ship-shevthenko_aleksandra/");
    }
}