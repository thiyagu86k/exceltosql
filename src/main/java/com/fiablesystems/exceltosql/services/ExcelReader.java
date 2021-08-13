package com.fiablesystems.exceltosql.services;

import com.fiablesystems.exceltosql.model.ExcelData;
import com.fiablesystems.exceltosql.model.ExcelSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.nio.file.Paths;
import java.util.*;

public class ExcelReader {

    public ExcelData loadExcelFile(String fileName) {
        try {
            XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(Paths.get(fileName).toFile()));
            List<String> sheetNames=getSheetNames(wb);
            List<ExcelSheet> excelSheetList=new ArrayList<>();
            for(String sheet:sheetNames){
                excelSheetList.add(collectDataFromSheet(wb.getSheet(sheet)));
            }
            ExcelData excelData = new ExcelData(fileName, excelSheetList);
            return excelData;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map<String, List<Object>> extractHeaders(Row row, Map<Integer, String> cellIndexMap) {
        Map<String, List<Object>> map = new HashMap<>();
        Iterator<Cell> cells = row.cellIterator();
        int i = 0;
        while (cells.hasNext()) {
            Cell cell = cells.next();
            map.put(cell.toString(), new ArrayList<Object>());
            cellIndexMap.put(i, cell.toString());
            i++;
        }
        return map;
    }

    public List<String> getSheetNames(XSSFWorkbook xssfWorkbook){
        List<String> sheetNames=new ArrayList<>();
        int noOfSheet=xssfWorkbook.getNumberOfSheets();
        for(int i=0;i<noOfSheet;i++){
            sheetNames.add(xssfWorkbook.getSheetName(i));
        }
        return sheetNames;

    }

    public ExcelSheet collectDataFromSheet(Sheet sheet){
        Map<Integer, String> cellIndexMap = new HashMap<>();
        Map<String, List<Object>> rowColumnData = extractHeaders(sheet.getRow(0), cellIndexMap);
        int lastRow=sheet.getLastRowNum();

        Iterator<Row> rows = sheet.iterator();
        long i = 0;
        while (rows.hasNext()) {
            Row cellRow = rows.next();
            if (i != 0) {
                Iterator<Cell> cells = cellRow.cellIterator();
                int j = 0;
                while (cells.hasNext()) {
                    Cell cell1 = cells.next();
                    Object data = cell1;
                    String key = cellIndexMap.get(j);
                    rowColumnData.get(key).add(data);
                    j++;
                }
            }
            i++;

        }
        return new ExcelSheet(sheet.getSheetName(),rowColumnData, lastRow);
    }

}
