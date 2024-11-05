package org.example;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Format {
    void FormatFromTXT(String cruiseName,String cruiseDescription,String cruiseDates,String purchaseLink,String firstDay,String lastDay,BufferedWriter writer) throws IOException {
        String paddedText1 = String.format("%-" + 30 + "s",cruiseName);

        // Удаляем все после запятой
        String[] parts = cruiseDates.split(",");
        String dateRange = parts[0].trim();
        // Разделяем даты на две строки
        String[] dates = dateRange.split("\\s*[-\u2013\u2014]\\s*");

        String startDate = "";
        String endDate ="";
        if (dates.length == 2) {
            startDate = dates[0].trim();
            endDate = dates[1].trim();

            System.out.println("Start Date: " + startDate);
            System.out.println("End Date: " + endDate);
        } else {
            System.out.println("Date format is incorrect.");
        }


        String paddedText2 = String.format("%-" + 10 + "s",startDate);
        String paddedText3 = String.format("%-" + 5 + "s",firstDay);

        String paddedText4 = String.format("%-" + 10 + "s",endDate);
        String paddedText5 = String.format("%-" + 5 + "s",lastDay);

        String replacedDescription = cruiseDescription.replace(" – ", "_");
        String paddedText6 = String.format("%-" + 130 + "s",replacedDescription);
        writer.write(String.format("%s\t%s %s\t%s %s\t%s\t%s\n", paddedText1, paddedText2, paddedText3, paddedText4, paddedText5, paddedText6,purchaseLink));
    }

    static void FormatGamaFromTXT(String cruiseName, String cruiseDescription, String purchaseLink, String firstDay, String lastDay, BufferedWriter writer) throws IOException {
        String replacedDescription = cruiseDescription.replace(" — ", "_");
        String replacedFirstDay = firstDay.replace("c ", "").replaceAll(" \\(.*?\\)", "");
        String replacedLastDay = lastDay.replace("по ", "").replaceAll(" \\(.*?\\)", "");
        writer.write(String.format("%s\t%s\t%s\t%s\t%s\n", cruiseName, replacedFirstDay, replacedLastDay, replacedDescription,purchaseLink));
    }
    static void FormatDonInturStopFromTXT(String cruiseName, String purchaseLink, ArrayList<String> city, ArrayList<String> timeIn, ArrayList<String> timeOut, BufferedWriter writer) throws IOException {
//        String replacedFirstDay = firstDay.replace("c ", "").replaceAll(" \\(.*?\\)", "");
//        String replacedLastDay = lastDay.replace("по ", "").replaceAll(" \\(.*?\\)", "");
        writer.write(String.format("%s\t%s\t%s\t%s\t%s\n", cruiseName, purchaseLink, city.get(0), timeIn.get(0), timeOut.get(0)));
        for(int numberDay = 1; numberDay < city.size() && numberDay < timeIn.size() && numberDay < timeOut.size(); numberDay++){
            writer.write(String.format("\t\t%s\t%s\t%s\n", city.get(numberDay), timeIn.get(numberDay),timeOut.get(numberDay)));
        }
    }

    static void FormatVodohodInturStopFromTXT(String cruiseName, String purchaseLink,ArrayList<String> timeDay, ArrayList<String> city, ArrayList<String> timeIn, ArrayList<String> timeOut, BufferedWriter writer) throws IOException {
        if(timeIn.size()==0 || timeOut.size()==0){
            String out = "";
            writer.write(String.format("%s\t%s\t%s\t%s %s\t%s %s\n", cruiseName, purchaseLink, city.get(0), timeDay.get(0),out, timeDay.get(0),out));
            for(int numberDay = 1; numberDay < city.size() && numberDay < timeIn.size() && numberDay < timeOut.size(); numberDay++){
                writer.write(String.format("\t\t%s\t%s %s\t%s %s\n", city.get(numberDay), timeDay.get(numberDay),out, timeDay.get(numberDay),out));
            }
            return;
        }
        writer.write(String.format("%s\t%s\t%s\t%s %s\t%s %s\n", cruiseName, purchaseLink, city.get(0), timeDay.get(0),timeIn.get(0), timeDay.get(0),timeOut.get(0)));
        for(int numberDay = 1; numberDay < city.size() && numberDay < timeIn.size() && numberDay < timeOut.size(); numberDay++){
            writer.write(String.format("\t\t%s\t%s %s\t%s %s\n", city.get(numberDay), timeDay.get(numberDay),timeIn.get(numberDay), timeDay.get(numberDay),timeOut.get(numberDay)));
        }
    }
}
