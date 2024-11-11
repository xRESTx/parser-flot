package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Parser_Mosturflot {
    public void Course(String url, String fileName) throws RuntimeException {
        WebDriver webDriver = new FirefoxDriver();
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            webDriver.get(url);
            System.out.println("Okay, let's go");
            List<WebElement> allElements = webDriver.findElements(By.cssSelector(".col-12.p-0.col-sm-4.position-relative.h-100 a"));
            for (WebElement element : allElements) {
                ArrayList<String> date = new ArrayList<>();
                ArrayList<String> start = new ArrayList<>();
                ArrayList<String> end = new ArrayList<>();
                ArrayList<String> city = new ArrayList<>();
                String href = element.getAttribute("href");
                ((JavascriptExecutor) webDriver).executeScript("window.open('" + href + "', '_blank');");
                String originalTab = webDriver.getWindowHandle();
                Set<String> allTabs = webDriver.getWindowHandles();
                for (String tab : allTabs) {
                    if (!tab.equals(originalTab)) {
                        webDriver.switchTo().window(tab);
                        break;
                    }
                }
                WebElement linkElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a.nav__link[data-id='2']")));
                ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView({block: 'center', behavior: 'smooth'});", linkElement);
                Thread.sleep(200);
                linkElement.click();

                WebElement cruiseNameWeb = webDriver.findElement(By.cssSelector(".detail__card.d-block.d-md-flex.flex-column.align-items-start"));
                WebElement cruiseNameWebs = cruiseNameWeb.findElement(By.cssSelector(".card-title"));
                String cruiseName = cruiseNameWebs.getText();

                List<WebElement> informations = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector(".col-sm-12.col-xl-2.p-4.p-xl-0.pr-xl-0.order-2.order-xl-1")));
                List<WebElement> cityWebEls = webDriver.findElements(By.cssSelector(".col-sm-12.col-xl-6.pl-4.order-3.order-xl-3"));
                int check = 0;
                end.add("");
                for (WebElement information : informations) {
                    WebElement cityWebEl = cityWebEls.get(check).findElement(By.cssSelector(".fs-sm-5.fw-600.pb-2"));
                    city.add(cityWebEl.getText());
                    List<WebElement> allDivs = information.findElements(By.cssSelector("div"));
                    if(allDivs.size()==4){
                        date.add(allDivs.get(1).getText().trim()+ ".2025");
                        String timeValue = allDivs.get(2).getText().trim();
                        String[] parts = timeValue.split("\n");
                        if(parts[0].length()==18){
                            start.add(parts[1].trim());
                        }
                        if(parts[0].length()==15){
                            end.add(parts[1].trim());
                        }
                    }
                    if(allDivs.size()==6){
                        date.add(allDivs.get(1).getText().trim() + ".2025");
                        String timeValue = allDivs.get(2).getText().trim();
                        String[] parts = timeValue.split("\n");
                        end.add(parts[1].trim());
                        timeValue = allDivs.get(4).getText().trim();
                        parts = timeValue.split("\n");
                        start.add(parts[1].trim());
                    }
                    check++;
                }
                start.add("");
                Format.FormatMosturflotStopFromTXT(cruiseName, href,date, city,end, start,writer);

                webDriver.close();
                webDriver.switchTo().window(originalTab);
            }

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            System.out.println("All ok");
            webDriver.quit();
        }
    }
}