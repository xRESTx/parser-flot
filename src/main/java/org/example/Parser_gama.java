package org.example;

import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.formula.functions.T;
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
        try {
            File directory = new File("./Gama");
            if(!directory.exists()) {
                if (directory.mkdirs()) {
                    System.out.println("Папка создана");
                }
            }
            webDriver.get(url);
            System.out.println("Okay, let's go");

            WebElement mainPage = webDriver.findElement(By.className("thmx_page__w_ship"));

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ship_town_selector")));
            WebElement cityPage = mainPage.findElement(By.id("ship_town_selector"));

            ((JavascriptExecutor) webDriver).executeScript(
                    "window.scrollTo(0, arguments[0].getBoundingClientRect().top + window.pageYOffset - 150);",
                    cityPage
            );
            Thread.sleep(1000);

            List<WebElement> cityList = cityPage.findElements(By.cssSelector(".m-1 button"));

            List<String> cruiseHref = new ArrayList<>();

            for (WebElement city : cityList) {
                city.click();
                Thread.sleep(1000);

                WebElement monthPage = mainPage.findElement(By.className("_monthly"));
                List<WebElement> monthButtons = monthPage.findElements(By.cssSelector(".m-1 button"));
                int monthCount = monthButtons.size();

                for (int i = 0; i < monthCount; i++) {

                    monthPage = mainPage.findElement(By.className("_monthly"));
                    monthButtons = monthPage.findElements(By.cssSelector(".m-1 button"));

                    WebElement month = monthButtons.get(i);
                    month.click();
                    Thread.sleep(200);
                    List<WebElement> actualMonth = mainPage.findElements(By.className("thmx_cruise_list_wide"));
                    List<WebElement> actualCruises = actualMonth.get(i).findElements(By.className("thmx_cruise_item"));
                    for (WebElement cruise : actualCruises){
                        cruiseHref.add(cruise.findElement(By.cssSelector("a.btn-gradient-warning")).getAttribute("href"));
                    }
                }
            }

            String cruiseName = webDriver.findElement(By.cssSelector("h1.b")).getText();

            String originalWindow = webDriver.getWindowHandle();

            for (String cruise : cruiseHref) {
                ((JavascriptExecutor) webDriver).executeScript("window.open()");
                Set<String> allWindows = webDriver.getWindowHandles();

                for (String window : allWindows) {
                    if (!window.equals(originalWindow)) {
                        webDriver.switchTo().window(window);
                        webDriver.get(cruise);
                        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("_header")));
                        WebElement thmx_tabs = webDriver.findElement(By.className("thmx_tabs"));
                        WebElement header = thmx_tabs.findElement(By.className("_header"));
                        List<WebElement> tab = header.findElements(By.className("_tab"));
                        tab.get(1).click();

                        List<String> city = new ArrayList<>();
                        List<String> timeIn = new ArrayList<>();
                        List<String> timeOut = new ArrayList<>();

                        thmx_tabs = webDriver.findElement(By.className("thmx_tabs"));
                        WebElement content = thmx_tabs.findElement(By.className("_content"));
                        List<WebElement> frame = content.findElements(By.className("_frame"));

                        List<WebElement> mb_3 = frame.get(1).findElements(By.className("mb-3"));
                        boolean first = true;
                        for (int i = 0; i < mb_3.size(); i++) {
                            WebElement d_flex = mb_3.get(i).findElement(By.className("d-flex"));
                            if (i == 0 || i == mb_3.size()-1) {
                                city.add(d_flex.findElement(By.cssSelector("h3.mb-0")).getText());

                                List<WebElement> innerDivs = d_flex.findElements(By.xpath(".//div[3]/div[2]/div"));
                                Pattern pattern = Pattern.compile("\\d{2}\\.\\d{2}\\.\\d{4} \\d{2}:\\d{2}");

                                for (WebElement div : innerDivs) {
                                    String text = div.getText();
                                    Matcher matcher = pattern.matcher(text);

                                    if (matcher.find()) {
                                        String dateTime = matcher.group();
                                        if (first) {
                                            timeOut.add(dateTime);
                                            first = false;
                                        } else {
                                            timeIn.add(dateTime);
                                        }
                                    }
                                }
                            } else {
                                city.add(d_flex.findElement(By.cssSelector("h3.mb-0")).getText());
                                List<WebElement> dateDivs = d_flex.findElements(By.cssSelector("div > div > div"));

                                List<WebElement> timeCandidates = new ArrayList<>();

                                for (WebElement div : dateDivs) {
                                    String text = div.getText();
                                    if (text.matches("\\d{2}\\.\\d{2}\\.\\d{4} \\d{2}:\\d{2}")) {
                                        timeCandidates.add(div);
                                    }
                                }

                                int size = timeCandidates.size();
                                if (size >= 2) {
                                    String arrivalTime = timeCandidates.get(size - 2).getText();
                                    String departureTime = timeCandidates.get(size - 1).getText();
                                    timeIn.add(arrivalTime);
                                    timeOut.add(departureTime);
                                }
                            }
                        }
                        timeOut.add("");
                        timeIn.add("");
                        ExcelSaver.FormatMosturflotStopFromExcel(cruiseName, cruise, city, timeIn, timeOut, fileName);
                        System.out.println(cruiseName + " " + city.size() + " " + timeOut.size()+ " " + timeIn.size() +" " + cruise);
                        webDriver.close();
                        webDriver.switchTo().window(originalWindow);
                    }
                }
            }

        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        } finally {
            webDriver.manage().deleteAllCookies();
            webDriver.quit();
//            System.out.println("Strange, My Lord");
        }
    }
}
