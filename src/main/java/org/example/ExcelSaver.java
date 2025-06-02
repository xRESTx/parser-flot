package org.example;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.List;

public class ExcelSaver {

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
