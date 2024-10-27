package org.example;


import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        System.setProperty("webdriver.chrome.driver", "geckodriver-v0.35.0-win64\\geckodriver.exe");
        Parser parser = new Parser();
        FileWriter fileWriter = new FileWriter("cruises.txt");
        //запуск одного resulta
        //parser.Course1("https://doninturflot.com/catalog/cruises-from-rostov_on_don/to-rostov_on_don/date-from-01.01.2025-to-31.12.2025/ship-shevthenko_aleksandra/");
        //прогон всех рейсов 2025 года
        for (int numberPage = 1; numberPage<23; numberPage++){
            parser.Course1("https://doninturflot.com/catalog/date-from-01.01.2025-to-31.12.2025/?PAGEN_1="+numberPage);
        }
    }
}
