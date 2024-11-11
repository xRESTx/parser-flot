package org.example;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Format {
    void FormatFromTXT(String cruiseName,String cruiseDescription,String cruiseDates,String purchaseLink,String firstDay,String lastDay,BufferedWriter writer) throws IOException {
        String[] parts = cruiseDates.split(",");
        String dateRange = parts[0].trim();
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

        String replacedDescription = cruiseDescription.replace(" – ", "_");
        writer.write(String.format("%s\t%s %s\t%s %s\t%s\t%s\n", cruiseName, startDate, firstDay, endDate, lastDay, replacedDescription, purchaseLink));
    }

    static void FormatGamaFromTXT(String cruiseName, String cruiseDescription, String purchaseLink, String firstDay, String lastDay, BufferedWriter writer) throws IOException {
        String replacedDescription = cruiseDescription.replace(" — ", "_");
        String replacedFirstDay = firstDay.replace("c ", "").replaceAll(" \\(.*?\\)", "");
        String replacedLastDay = lastDay.replace("по ", "").replaceAll(" \\(.*?\\)", "");
        writer.write(String.format("%s\t%s\t%s\t%s\t%s\n", cruiseName, replacedFirstDay, replacedLastDay, replacedDescription,purchaseLink));
    }
    static public void FormatDonInturStopFromTXT(String cruiseName, String purchaseLink, ArrayList<String> city, ArrayList<String> timeIn, ArrayList<String> timeOut, BufferedWriter writer) throws IOException {
        writer.write(String.format("%s\t%s\t%s\t%s\t%s\n", cruiseName, purchaseLink, city.get(0), timeIn.get(0), timeOut.get(0)));
        for(int numberDay = 1; numberDay < city.size() && numberDay < timeIn.size() && numberDay < timeOut.size(); numberDay++){
            writer.write(String.format("\t\t%s\t%s\t%s\n", city.get(numberDay), timeIn.get(numberDay),timeOut.get(numberDay)));
        }
    }

    static void FormatVodohodStopFromTXT(String cruiseName, String purchaseLink,ArrayList<String> timeDay, ArrayList<String> city, ArrayList<String> timeIn, ArrayList<String> timeOut, BufferedWriter writer) throws IOException {
        boolean output= false;

        writer.write(String.format("%s\t%s\t%s\t\t%s %s\n", cruiseName, purchaseLink, city.get(0), timeDay.get(0),timeOut.get(0)));
        for(int numberDay = 1; numberDay < city.size() && numberDay < timeIn.size() && numberDay < timeOut.size(); numberDay++){

            if(output){
                output = false;
                continue;
            }
            if(numberDay == city.size()-1){
                writer.write(String.format("\t\t%s\t%s %s\t\n", city.get(numberDay), timeDay.get(numberDay),timeIn.get(numberDay)));
                System.out.println("Save, My Lord");
                return;
            }
            if(city.get(numberDay).equals(city.get(numberDay + 1))){
                writer.write(String.format("\t\t%s\t%s %s\t%s %s\n", city.get(numberDay), timeDay.get(numberDay),timeIn.get(numberDay), timeDay.get(numberDay+1),timeOut.get(numberDay+1)));
                output = true;
                continue;
            }
            writer.write(String.format("\t\t%s\t%s %s\t%s %s\n", city.get(numberDay), timeDay.get(numberDay),timeIn.get(numberDay), timeDay.get(numberDay),timeOut.get(numberDay)));
            writer.flush();
        }
    }
    static void FormatMosturflotStopFromTXT(String cruiseName, String purchaseLink,ArrayList<String> timeDay, ArrayList<String> city, ArrayList<String> timeIn, ArrayList<String> timeOut, BufferedWriter writer) throws IOException {
        int check = 0;

        writer.write(String.format("%s\t%s\t%s\t\t%s %s\n", cruiseName, purchaseLink, city.get(0), timeDay.get(0),timeOut.get(0)));
        for(int numberDay = 1; numberDay < city.size() && numberDay < timeIn.size() && numberDay < timeOut.size(); numberDay++){
            if(check == 1 || check == 2){
                check--;
                continue;
            }
            if(numberDay == city.size()-2){
                writer.write(String.format("\t\t%s\t%s %s\t%s %s\n", city.get(numberDay), timeDay.get(numberDay),timeIn.get(numberDay), timeDay.get(numberDay),timeOut.get(numberDay)));
                continue;
            }
            if(numberDay == city.size()-1){
                writer.write(String.format("\t\t%s\t%s %s\t\n", city.get(numberDay), timeDay.get(numberDay),timeIn.get(numberDay)));
                return;
            }
            if(city.get(numberDay).equals(city.get(numberDay+1)) && city.get(numberDay).equals(city.get(numberDay+2)) && check+2<city.size()){
                writer.write(String.format("\t\t%s\t%s %s\t%s %s\n", city.get(numberDay), timeDay.get(numberDay),timeIn.get(numberDay), timeDay.get(numberDay+2),timeOut.get(numberDay+2)));
                check=2;
                continue;
            }
            if(city.get(numberDay).equals(city.get(numberDay + 1))){
                writer.write(String.format("\t\t%s\t%s %s\t%s %s\n", city.get(numberDay), timeDay.get(numberDay),timeIn.get(numberDay), timeDay.get(numberDay+1),timeOut.get(numberDay+1)));
                check=1;
                continue;
            }
            writer.write(String.format("\t\t%s\t%s %s\t%s %s\n", city.get(numberDay), timeDay.get(numberDay),timeIn.get(numberDay), timeDay.get(numberDay),timeOut.get(numberDay)));
            writer.flush();
        }
    }
}