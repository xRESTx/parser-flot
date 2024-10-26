package org.example;

import java.util.List;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.By;

public class Parser {
    public void Course1(String args){
        WebDriver webDriver = new ChromeDriver();
        webDriver.get(args);
        System.out.println(webDriver.getTitle());


        webDriver.quit();
    }
}
