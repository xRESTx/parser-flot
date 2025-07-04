package org.example;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ExcelSaver {

    public static void FormatMosturflotStopFromExcel(String cruiseName, String purchaseLink, List<String> city, List<String> timeIn, List<String> timeOut, String filePath) throws IOException {
        File file = new File(filePath);
        Workbook workbook;
        Sheet sheet;
        int rowNum;

        if (file.exists()) {
            try (FileInputStream fis = new FileInputStream(file)) {
                workbook = new XSSFWorkbook(fis);
                sheet = workbook.getSheetAt(0);
                rowNum = sheet.getLastRowNum() + 1;
            }
        } else {
            workbook = new XSSFWorkbook();
            sheet = workbook.createSheet("Маршрут");
            rowNum = 0;
        }

        Row header = sheet.createRow(rowNum++);
        header.createCell(0).setCellValue(cruiseName);
        header.createCell(1).setCellValue(purchaseLink);
        header.createCell(2).setCellValue(city.getFirst());
        header.createCell(3).setCellValue("");
        header.createCell(4).setCellValue(timeOut.getFirst());
        for (int i = 1; i < city.size() && i < timeIn.size() && i < timeOut.size(); i++) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(2).setCellValue(city.get(i));
            row.createCell(3).setCellValue(timeIn.get(i-1));
            row.createCell(4).setCellValue(timeOut.get(i));
        }
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            workbook.write(fos);
        }
        workbook.close();
    }
    static void FormatSputnikGermesFromTXT(String cruiseName, String purchaseLink, ArrayList<String> timeDay, ArrayList<String> city, ArrayList<String> timeIn, ArrayList<String> timeOut, String filePath) throws IOException {
        File file = new File(filePath);
        Workbook workbook;
        Sheet sheet;
        int rowNum;

        if (file.exists()) {
            try (FileInputStream fis = new FileInputStream(file)) {
                workbook = new XSSFWorkbook(fis);
                sheet = workbook.getSheetAt(0);
                rowNum = sheet.getLastRowNum() + 1;
            }
        } else {
            workbook = new XSSFWorkbook();
            sheet = workbook.createSheet("Маршрут");
            rowNum = 0;
        }

        Row header = sheet.createRow(rowNum++);
        header.createCell(0).setCellValue(cruiseName);
        header.createCell(1).setCellValue(purchaseLink);
        header.createCell(2).setCellValue(city.getFirst());
        if(timeIn.getFirst().isEmpty()){
            header.createCell(3).setCellValue("");
        }else {
            header.createCell(3).setCellValue(timeDay.getFirst() + " " + timeIn.getFirst());
        }
        if(timeOut.getFirst().isEmpty()){
            header.createCell(4).setCellValue("");
        }else {
            header.createCell(4).setCellValue(timeDay.getFirst() + " " + timeOut.getFirst());
        }

        for(int numberDay = 1; numberDay < city.size() && numberDay < timeIn.size() && numberDay < timeOut.size(); numberDay++){
            boolean flag = false;
            if(timeDay.get(numberDay).equals("")){
                flag = true;
            }
            if(timeIn.get(numberDay).equals("") && !flag) {
                timeIn.set(numberDay,"00:00");
            }
            if(timeOut.get(numberDay).equals("") && numberDay!= city.size()-1 && !flag){
                timeOut.set(numberDay,"00:00");
            }
            Row row = sheet.createRow(rowNum++);
            if(numberDay== city.size()-1) {
                row.createCell(2).setCellValue(city.get(numberDay));
                row.createCell(3).setCellValue(timeDay.get(numberDay) + " " + timeIn.get(numberDay));
                if(!timeOut.get(numberDay).isEmpty()){
                    row.createCell(4).setCellValue(timeDay.get(numberDay) + " " + timeOut.get(numberDay));
                }
                break;
            }
            row.createCell(2).setCellValue(city.get(numberDay));
            row.createCell(3).setCellValue(timeDay.get(numberDay) + " " + timeIn.get(numberDay));
            row.createCell(4).setCellValue(timeDay.get(numberDay) + " " + timeOut.get(numberDay));
        }
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            workbook.write(fos);
        }
        workbook.close();
    }



    public static void saveIngredientsToExcel(List<String> ingredients, String dishName, String filePath) {
        Workbook workbook;
        Sheet sheet;

        File file = new File(filePath);
        if (file.exists()) {
            try (FileInputStream fis = new FileInputStream(file)) {
                workbook = new XSSFWorkbook(fis);
                sheet = workbook.getSheetAt(0);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        } else {
            workbook = new XSSFWorkbook();
            sheet = workbook.createSheet("Ингредиенты");
        }

        int lastRowNum = sheet.getLastRowNum();
        // Если файл только создан, `getLastRowNum()` вернёт 0, но строки ещё нет — нужно проверить
        if (lastRowNum == 0 && sheet.getRow(0) == null) {
            lastRowNum = -1;
        }

        for (String ingredient : ingredients) {
            Row row = sheet.createRow(++lastRowNum);
            row.createCell(0).setCellValue(ingredient);
            row.createCell(1).setCellValue(dishName);
        }

        // Автоширина
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);

        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            workbook.write(fos);
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
