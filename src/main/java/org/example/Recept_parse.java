package org.example;

import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.example.ExcelSaver.saveIngredientsToExcel;

public class Recept_parse {

    public static void main(String[] args){
        FirefoxOptions options = new FirefoxOptions();
//        options.addArguments("--headless");
        WebDriver webDriver = new FirefoxDriver();
        try {
//            webDriver.get("https://www.russianfood.com/recipes/bytype/?fid=131");
//            webDriver.get("https://www.russianfood.com/recipes/bytype/?fid=131&page=2#rcp_list");


            webDriver.get("https://www.russianfood.com/recipes/bytype/?fid=142");
//            webDriver.get("https://www.russianfood.com/recipes/bytype/?fid=143&page=2#rcp_list");
            System.out.println("Lets GO");
            List<WebElement> elements = webDriver.findElements(By.className("in_seen"));
            System.out.println(elements.size());
            for(WebElement element :elements){
                WebElement hrefWeb = element.findElement(By.tagName("a"));
                String href = hrefWeb.getAttribute("href");


                ((JavascriptExecutor) webDriver).executeScript("window.open('" + href + "', '_blank');");
                String originalTab = webDriver.getWindowHandle();
                Set<String> allTabs = webDriver.getWindowHandles();
                for (String tab : allTabs) {
                    if (!tab.equals(originalTab)) {
                        webDriver.switchTo().window(tab);
                        break;
                    }
                }
                WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("h1")));
                String h1 = webDriver.findElement(By.tagName("h1")).getText();

                List<String> ingredients = new ArrayList<>();

                WebElement block = webDriver.findElement(By.className("ingr_block"));
                List<WebElement> ings = block.findElements(By.cssSelector(".ingr span"));
                boolean next = true;
                Pattern pattern = Pattern.compile("^(.*?)\\s*–");
                Pattern pattern2 = Pattern.compile("^(.*?)\\s*-");
                Pattern pattern3 = Pattern.compile("^(.*?)\\s*—");
                for(WebElement ing :ings){
                    if(next){
                        next = false;
                        continue;
                    }
                    String text = ing.getText().trim();
                    if (text.isEmpty()) {
                        continue;
                    }
                    Matcher matcher = pattern.matcher(text);

                    if (matcher.find()) {
                        ingredients.add(matcher.group(1));
                    } else {
                        Matcher matcher1 = pattern2.matcher(text);
                        if (matcher1.find()) {
                            ingredients.add(matcher1.group(1));
                        }else {
                            Matcher matcher2 = pattern3.matcher(text);
                            if (matcher2.find()) {
                                ingredients.add(matcher2.group(1));
                            }
                        }
                    }
                }
                for (int i = 0; i < ingredients.size(); i++) {
                    String line = ingredients.get(i);
                    line = line.replaceAll("\\s*\\(.*?\\)", "").trim();
                    ingredients.set(i, line); // сохраняем обратно в список
                }
                System.out.println(ingredients.size());
                saveIngredientsToExcel(ingredients,h1,"data.xlsx");
                webDriver.close();
                webDriver.switchTo().window(originalTab);
            }
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
