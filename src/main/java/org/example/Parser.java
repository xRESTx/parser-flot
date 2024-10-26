package org.example;

import java.util.List;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.By;
import java.io.FileWriter;
import java.io.IOException;



public class Parser {
    public void Course1(String args){
        System.out.println("ну чтож, начнем");
        WebDriver webDriver = new ChromeDriver();
        webDriver.get(args);
        List<WebElement> elements = webDriver.findElements(By.xpath("//*"));
        StringBuilder allText = new StringBuilder();
        for (WebElement element : elements) {
            allText.append(element.getText()).append("\n");
        }

        // Записываем текст в файл
        try (FileWriter writer = new FileWriter("text.txt")) {
            writer.write(allText.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Закрываем браузер
        webDriver.quit();

    }
}
