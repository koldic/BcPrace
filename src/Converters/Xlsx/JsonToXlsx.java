/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Converters.Xlsx;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author David
 */
public class JsonToXlsx {
    private final Map<String, Map<String, List<String>>> values;
    private final Boolean oneFile;
    private final String fileName;
    private final String outputPath;
    
    public JsonToXlsx(String outputPath, String fileName, Map<String, Map<String, List<String>>> valuesMap, Boolean intoOneFile) {
        this.outputPath = outputPath;
        this.fileName = fileName;
        this.values = valuesMap;
        this.oneFile = intoOneFile;
    }
    
    public void convertToXlsx(){
        
        try {
            Workbook workBook = new XSSFWorkbook();
            Sheet sheet = workBook.createSheet("Sample sheet");
            List<String> turnedValues = new ArrayList<>();
            
            if (this.oneFile){
                int rownum = 0;
                Cell cell;
                for(String outKey : this.values.keySet()){
                Boolean firstLine = true;
                    
                    for (String arKey : this.values.get(outKey).keySet()){
                        Row row = sheet.createRow(rownum++);
                        if (firstLine){
                            row = sheet.createRow(rownum++);
                            int keyCellNumber = 0;
                            for (String key : this.values.get(outKey).keySet()){
                                cell = row.createCell(keyCellNumber++);
                                cell.setCellValue(key);
                            }
                            firstLine = false;
                        }
                        for (int j=0;j<this.values.get(outKey).get(arKey).size();j++){
                            row = sheet.createRow(rownum++);

                            turnedValues.clear();
                            int cellnum = 0;
                            for(String keys : this.values.get(outKey).keySet()){
                                if (j<this.values.get(outKey).get(keys).size()){
                                    cell = row.createCell(cellnum++);
                                    cell.setCellValue(this.values.get(outKey).get(keys).get(j));
                                    //turnedValues.add(this.values.get(outKey).get(keys).get(j));
                                    //System.out.println("key: " + keys + " value: " + this.values.get(outKey).get(keys).get(j));
                                    //this.addValue(value.get(keys).get(j), this.firstColumnValue);
                                }
                            }
                            //System.out.println(" values: " + row);
                            //csvWriter.writeNext(turnedValues.toArray(new String[turnedValues.size()]));
                        }
                            break;
                    }
                }
            }
            else{
            
            }
            
            FileOutputStream out = new FileOutputStream(new File(this.outputPath+this.fileName));
            workBook.write(out);
            out.close();
            workBook.close();
            
        } catch (EncryptedDocumentException ex) {
            Logger.getLogger(JsonToXlsx.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(JsonToXlsx.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(JsonToXlsx.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
