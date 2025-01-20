package org.example;

import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser_gama {
    public void Course(String url) {

        System.out.println("Okay, let's go...");
        WebDriver webDriver = new FirefoxDriver();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("cruises-gama.txt",true))) {

            webDriver.get(url);
            List<WebElement> cruiseItems = webDriver.findElements(By.cssSelector(".in-actual"));

            for (WebElement item : cruiseItems) {
                try {
                    WebElement description = item.findElement(By.className("in-way-text"));
                    String cruiseDescription = description.getText();

                    WebElement dates = item.findElement(By.cssSelector(".in-dt span"));
//                    String cruiseDates = dates.getText();

                    List<WebElement> dateDivs = item.findElements(By.cssSelector(".in-dt div"));
                    String startDate = "";
                    String endDate = "";
                    if (dateDivs.size() >= 2) {
                        startDate = dateDivs.get(0).getText().replace("с ", "");
                        endDate = dateDivs.get(1).getText();
                    }
                    WebElement purchaseButton = item.findElement(By.cssSelector(".btn.btn-sm.btn-gama-action"));
                    String purchaseLink = purchaseButton.getAttribute("href");

                    WebElement name = item.findElement(By.cssSelector(".in-ship-txt a"));
                    String cruiseName = name.getText();

                    Format.FormatGamaFromTXT(cruiseName, cruiseDescription, purchaseLink, startDate, endDate, writer);

//                    System.out.println("Название судна: " + cruiseName);
//                    System.out.println("Описание круиза: " + cruiseDescription);
//                    System.out.println("Даты круиза: " + cruiseDates);
//                    System.out.println("Ссылка на покупку: " + purchaseLink);
//                    System.out.println("Первый день: " + startDate);
//                    System.out.println("Последний день: " + endDate);
//                    System.out.println("------------------------------------");

                } catch (NoSuchElementException e) {
                    System.err.println("Any elements not search for cruise. Skip...");
                }
            }
            System.out.println("Data successful update in cruises.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            webDriver.quit();
        }
    }
    public void CourseInfo(String url, String fileName){
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--headless");
        WebDriver webDriver = new FirefoxDriver(options);
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
        Format format = new Format();
        try {
            File directory = new File("./gama");
            if(!directory.exists()) {
                if (directory.mkdirs()) {
                    System.out.println("Папка создана");
                }
            }
            webDriver.get(url);
            System.out.println("Okay, let's go");
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("pj-search")));
            List<WebElement> cruises = webDriver.findElements(By.className("in-actual"));

            String regex = "(\\d{2}\\.\\d{2}\\.\\d{4})\\sв\\s(\\d{2}:\\d{2})";
            for(WebElement cruise : cruises){
                String nameTeplohod = cruise.findElement(By.className("in-ship-txt")).getText();
                ArrayList<String> city = new ArrayList<>();
                ArrayList<String> timeIn = new ArrayList<>();
                timeIn.add("");
                ArrayList<String> timeOut = new ArrayList<>();
                ArrayList<String> timeDayIn = new ArrayList<>();
                timeDayIn.add("");
                ArrayList<String> timeDayOut = new ArrayList<>();

                List<WebElement> info = cruise.findElements(By.cssSelector(".in-btn-stock.mt-1"));
                if(info.isEmpty()){
                    continue;
                }
                WebElement hrefInfo = cruise.findElement(By.cssSelector(".in-btn-stock a"));
                String hrefValue = hrefInfo.getAttribute("href");

                WebElement click = info.getFirst().findElement(By.cssSelector(".btn.btn-sm.btn-gama-action"));
                click.click();

                WebElement mainBlank = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("pj-tour")));
                List<WebElement> blanks = mainBlank.findElements(By.cssSelector(".mt-2.mb-4"));
                boolean start = true;
                for(WebElement blank : blanks){
                    WebElement cityInfoWebEl = blank.findElement(By.cssSelector(".mt-1.mb-1.in-town"));
                    city.add(cityInfoWebEl.getText());
                    List<WebElement> times = blank.findElements(By.cssSelector(".in-date"));
                    boolean departure = true;
                    for(WebElement time : times){
                        if(start && times.size()==1){
                            start = false;
                            String dayInfo = time.getText();

                            // Компилируем шаблон
                            Pattern pattern = Pattern.compile(regex);
                            Matcher matcher = pattern.matcher(dayInfo);

                            // Проверяем совпадения
                            if (matcher.find()) {
                                // Извлекаем дату и время
                                timeDayOut.add(matcher.group(1));
                                timeOut.add(matcher.group(2));
                            } else {
                                System.out.println("ERROR");
                            }
                            break;
                        } else if(!start && times.size()==1){
                            String dayInfo = time.getText();

                            // Компилируем шаблон
                            Pattern pattern = Pattern.compile(regex);
                            Matcher matcher = pattern.matcher(dayInfo);

                            // Проверяем совпадения
                            if (matcher.find()) {
                                // Извлекаем дату и время
                                timeDayIn.add(matcher.group(1));
                                timeIn.add(matcher.group(2));
                            } else {
                                System.out.println("ERROR");
                            }
                            break;
                        }
                        if(departure){
                            departure = false;
                            String dayInfo = time.getText();
                            // Компилируем шаблон
                            Pattern pattern = Pattern.compile(regex);
                            Matcher matcher = pattern.matcher(dayInfo);

                            // Проверяем совпадения
                            if (matcher.find()) {
                                // Извлекаем дату и время
                                timeDayIn.add(matcher.group(1));
                                timeIn.add(matcher.group(2));
                            } else {
                                System.out.println("Error");
                            }
                        } else {
                            String dayInfo = time.getText();
                            // Компилируем шаблон
                            Pattern pattern = Pattern.compile(regex);
                            Matcher matcher = pattern.matcher(dayInfo);

                            // Проверяем совпадения
                            if (matcher.find()) {
                                // Извлекаем дату и время
                                timeDayOut.add(matcher.group(1));
                                timeOut.add(matcher.group(2));
                            } else {
                                System.out.println("Error");
                            }
                        }
                    }

                }

                WebElement closeWindow = webDriver.findElement(By.cssSelector(".modal-dialog.modal-lg.modal-dialog-centered"));
                List<WebElement> closeButton = closeWindow.findElements(By.className("close"));
                while (closeButton.isEmpty()){
                    closeButton = closeWindow.findElements(By.className("close"));
                    System.out.println(closeButton.size());
                    Thread.sleep(200);
                }
                closeButton.getFirst().click();
                timeOut.add("");
                timeDayOut.add("");

                System.out.println(timeDayOut.size() + "" + timeDayIn.size() + "" + timeIn.size()+ "" + timeOut.size()+"" + city.size());
                format.FormatGammaInfoFromTXT(nameTeplohod,hrefValue,city,timeIn,timeOut,timeDayOut,timeDayIn);
                Thread.sleep(100);
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            webDriver.manage().deleteAllCookies();
            webDriver.quit();
            System.out.println("Strange, My Lord");
        }
    }
}
