package org.example;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Format {
    void FormatFromTXT(String cruiseName,String cruiseDescription,String cruiseDates,String purchaseLink,String firstDay,String lastDay,BufferedWriter writer) throws IOException {
        String paddedText1 = String.format("%-" + 40 + "s",cruiseName);
        //замена
        String originalText = "09.05.2025 – 20.05.2025, 12 дней/11 ночей";

        // Удаляем все после запятой
        String[] parts = cruiseDates.split(",");
        String dateRange = parts[0].trim();

        // Разделяем даты на две строки
        String[] dates = dateRange.split("–");
        String startDate = dates[0].trim();
        String endDate = dates[1].trim();

        String paddedText2 = String.format("%-" + 20 + "s",startDate);
        String paddedText3 = String.format("%-" + 20 + "s",firstDay);

        String paddedText4 = String.format("%-" + 20 + "s",endDate);
        String paddedText5 = String.format("%-" + 20 + "s",lastDay);

        String replacedDescription = cruiseDescription.replace("-", "_");
        String paddedText6 = String.format("%-" + 100 + "s",replacedDescription);
        writer.write(String.format("\"%s\"\t,\"%s\"\t,\"%s\"\t,\"%s\"\t,\"%s\"\t,\"%s\"\t,\"%s\"\n", paddedText1, paddedText2, paddedText3, paddedText4, paddedText5, paddedText6,lastDay));
    }
}
