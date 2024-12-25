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

public class ParseAzurit {
    public void Course(String url, String fileName) throws RuntimeException {
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--headless");
        WebDriver webDriver = new FirefoxDriver(options);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName,true))) {
            webDriver.get(url);
            System.out.println("Okay, let's go");
            List<WebElement> rows = webDriver.findElements(By.cssSelector(".ex-card__price-wr"));

            WebElement teplohod = webDriver.findElement(By.tagName("h1"));
            String nameTeplohod = teplohod.getText();

            List<String> hrefs = new ArrayList<>();
            for (WebElement row : rows) {
                // Извлечение ссылок из строки
                List<WebElement> links = row.findElements(By.tagName("a"));
                for (WebElement link : links) {
                    String href = link.getAttribute("href");
                    if(!hrefs.contains(href)){
                        hrefs.add(href);
                        continue;
                    }
                }
            }

            for(String href : hrefs){

                boolean checkFirst = true;
                String regex = "tourid=(\\d+)";

                // Компилируем регулярное выражение
                Pattern patternHref = Pattern.compile(regex);
                Matcher matcherHref = patternHref.matcher(href);
                if (matcherHref.find()) {
                    // Выводим первую группу захвата
                    href = href.replaceAll("\\?tourid=\\d+", "");
                    href += "programma-kruiza-bunin?tourid="  + matcherHref.group(1);
                } else {
                    System.out.println("Ссылка не найдена");
                    continue;
                }
                System.out.println(href);

                ArrayList<String> city = new ArrayList<>();
                ArrayList<String> timeIn = new ArrayList<>();
                ArrayList<String> timeOut = new ArrayList<>();
                ArrayList<String> timeDayOut = new ArrayList<>();
                ArrayList<String> timeDayIn = new ArrayList<>();


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
                try {
                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("pcp__program")));
                } catch (TimeoutException e) {
                    webDriver.close();
                    webDriver.switchTo().window(originalTab);
                    continue;
                }


                List<WebElement> cells = webDriver.findElements(By.xpath("//tbody/tr"));

                // Проходимся по каждой строке
                for (WebElement row : cells) {
                    // Извлекаем текст из <td>
                    WebElement cell = row.findElement(By.tagName("td"));
                    String cellText = cell.getText();

                    // Разбиваем данные по строкам
                    String[] parts = cellText.split("\n");
                    if (parts.length >= 2) {
                        String location = parts[1]; // Место
                        city.add(location);
                    }
                    String regexTime = "(\\d{1,2} \\p{L}+ \\d{4}), (\\d{2}:\\d{2}) / (\\d{1,2} \\p{L}+ \\d{4}), (\\d{2}:\\d{2})";

                    Pattern pattern = Pattern.compile(regexTime);
                    Matcher matcher = pattern.matcher(parts[0]);

                    if (matcher.find()) {
                        timeDayIn.add(matcher.group(1));
                        timeIn.add(matcher.group(2));
                        timeDayOut.add(matcher.group(3));
                        timeOut.add(matcher.group(4));
                    } else {
                        String regexTimeOne = "(\\d{1,2} \\p{L}+ \\d{4}), (\\d{2}:\\d{2})";
                        Pattern patternOne = Pattern.compile(regexTimeOne);
                        Matcher matcherOne = patternOne.matcher(parts[0]);

                        if (matcherOne.find()) {
                            if(checkFirst){
                                timeDayIn.add("");
                                timeIn.add("");
                                timeDayOut.add(matcherOne.group(1));
                                timeOut.add(matcherOne.group(2));
                                checkFirst = false;
                            }
                            else{
                                timeDayOut.add("");
                                timeOut.add("");
                                timeDayIn.add(matcherOne.group(1));
                                timeIn.add(matcherOne.group(2));
                            }
                        } else {
                            System.out.println("Strange");
                        }
                    }
                }
                System.out.println(timeDayIn.size() + "" + timeDayOut.size() + "" + timeIn.size()+ "" + timeOut.size()+ "" + city.size());
                Format.FormatAzuritFromTXT(nameTeplohod, href, city, timeIn, timeOut, timeDayOut,timeDayIn ,writer);
                webDriver.close();
                webDriver.switchTo().window(originalTab);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            webDriver.manage().deleteAllCookies();
            webDriver.quit();
            System.out.println("Strange, My Lord");
        }
    }

}
