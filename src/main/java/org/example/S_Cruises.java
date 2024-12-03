package org.example;

import org.example.Main;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static org.example.Format.*;

public class S_Cruises {
    public static void main(String[] args) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("s-cruises.txt"))){
            List<String> item = new ArrayList<>();
            int page = 1;
            String url = "https://s-cruises.com/cruises/filter/date_cruise_start-from-01.01.2025-to-10.11.2025/date_cruise_end-from-15.11.2024-to-13.11.2025/numbers_of_days-from-3-to-31/ships-is-rossia_/count_persont-from-0/apply/?Pagen_1=2&PAGEN_1=" + String.valueOf(page);
            boolean bool;
            do {
                Document doc = Jsoup.connect(url).get();
                Elements cruiseElements = doc.select("div.mt-0.mb-4.lh_1-2.fz_xl");
                bool = false;
                int c = 0;
                for (Element cruise : cruiseElements){
                    Element linlEl = cruise.selectFirst("a.mb-3");
                    if(linlEl!=null){
                        item.add(linlEl.attr("href"));
                    }
                    c++;
                }
                System.out.println(c);
                Element paginationItem = doc.selectFirst(".pagination__item.next.disabled");
                if(paginationItem == null){
                    page ++;
                    url = "https://s-cruises.com/cruises/filter/date_cruise_start-from-01.01.2025-to-10.11.2025/date_cruise_end-from-15.11.2024-to-13.11.2025/numbers_of_days-from-3-to-31/ships-is-rossia_/count_persont-from-0/apply/?Pagen_1=2&PAGEN_1=" + String.valueOf(page);
                    bool = true;
                }
            }while (bool);
            String regexCity = "(.*?)\\s*(\\d{1,2}:\\d{2})\\s*-\\s*(\\d{1,2}:\\d{2})";
            String regexDate = "(\\d{2} \\p{IsAlphabetic}+)";
            String regexStart = "(\\p{L}+(?:\\s\\p{L}+)*)(\\s+\\p{L}+)\\s((\\d{1,2}:\\d{2}))";
            Pattern patternStart = Pattern.compile(regexStart);
            Pattern patternDate = Pattern.compile(regexDate);
            Pattern patternCity = Pattern.compile(regexCity);
            for(String href : item){
                ArrayList<String> date = new ArrayList<>();
                ArrayList<String> start = new ArrayList<>();
                ArrayList<String> end = new ArrayList<>();
                ArrayList<String> city = new ArrayList<>();
                end.add("");
                href = "https://s-cruises.com" + href;
                Document doc = Jsoup.connect(href).get();

                Element container = doc.select(".col-auto.pl-2").first();
                String cruiseName = null;
                if (container != null) {
                    Element linkName = container.select("a").first();
                    cruiseName = linkName.text();
                }
                Element cruiseInfo = doc.selectFirst(".schedule-points-nav.mb-5.d-none.d-lg-block");
                Elements scheduleItems = cruiseInfo.children();
                boolean checkDouble = true;
                boolean starts = true;
                for(int i = 0;i<scheduleItems.size();i++){
                    Element it = scheduleItems.get(i);
                    Matcher matcherStart = patternStart.matcher(it.text());
                    Matcher matcherCity = patternCity.matcher(it.text());
                    if(starts){
                        if(matcherStart.find()){
                            city.add(matcherStart.group(1));
//                            System.out.println(matcherStart.group(3));
                            start.add(matcherStart.group(3));
                            checkDouble = true;
                            starts = false;
                            continue;
                        }
                    }else {
                        matcherStart = patternStart.matcher(it.text());
                        if(matcherStart.find() && !matcherCity.find()){
                            if(checkDouble){
                                date.add(date.get(date.size()-1));
                            }
                            city.add(matcherStart.group(1));
                            System.out.println(matcherStart.group(0));
                            end.add(matcherStart.group(3));
                            break;
                        }
                    }
                    matcherCity = patternCity.matcher(it.text());
                    Matcher matcherDate = patternDate.matcher(it.text());
                    if(matcherDate.find()){
                        date.add(matcherDate.group(0));
//                            System.out.println(matcherDate.group(1));
                        checkDouble = false;
                    }else if(matcherCity.find()){
                        if(checkDouble){
                            date.add(date.get(date.size()-1));
                        }
//                        System.out.println(matcherCity.group(1));
                        city.add(matcherCity.group(1));
//                        System.out.println(matcherCity.group(2));
                        end.add(matcherCity.group(2));
                        System.out.println(start.get(0));
//                        System.out.println(matcherCity.group(3));
                        start.add(matcherCity.group(3));

                        checkDouble = true;
                    }
                }
                start.add("");
                FormatMosturflotStopFromTXT(cruiseName,  href,date,  city, end, start, writer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
