package org.example;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

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
}
