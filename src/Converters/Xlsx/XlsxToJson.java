/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Converters.Xlsx;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.json.JSONException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;


/**
 *
 * @author David
 */
public class XlsxToJson {
    private final String fileName;
    private JSONObject json = null;
    private int numberOfSheets;
    //private List<Cell> keys;
    private Map<Integer, String> keysMap;
    private int firstRowNumber;
    private StringBuilder strBuilder;

    public XlsxToJson(String fileName) {
        this.fileName = fileName;
        //this.keys = new ArrayList<Cell>();
        this.keysMap = new HashMap<>();
        this.strBuilder = new StringBuilder();
        this.json = new JSONObject();
    }
    
    public JSONObject ConvertColumns(boolean isXlsx){
        File inp = null;
        
        try {
            inp = new File( this.fileName);
            Workbook workbook = WorkbookFactory.create( inp );
            DataFormatter objDefaultFormat = new DataFormatter();
            FormulaEvaluator objFormulaEvaluator = null;
            
            if (isXlsx) {
                objFormulaEvaluator = new XSSFFormulaEvaluator((XSSFWorkbook) workbook);
            }
            else{
                objFormulaEvaluator = new HSSFFormulaEvaluator((HSSFWorkbook) workbook);
            }
            
            this.numberOfSheets = workbook.getNumberOfSheets();
            //System.out.println(this.numberOfSheets);
            //Sheet sheet = workbook.getSheetAt( workbook.getFirstVisibleTab() );
            
            //int columnCount = 0;
            //List<Cell> listOfRows = new ArrayList<Cell>();
            JSONObject jRow = new JSONObject();
            //JSONArray rowsArray = new JSONArray();
            
            for(Iterator<Sheet> sheetsIterator = workbook.sheetIterator(); sheetsIterator.hasNext();){
                Sheet sheet = sheetsIterator.next();
                this.firstRowNumber = sheet.getFirstRowNum();
                Row firstRow = sheet.getRow(firstRowNumber);
                //System.out.println(sheet.getSheetName());
                
                for(Iterator<Row> rowsIterator = sheet.rowIterator(); rowsIterator.hasNext();){
                    Row row = rowsIterator.next();
                    //System.out.println("test1" + row.getCell(firstRowNumber) + row.getFirstCellNum());

                    for ( Iterator<Cell> cellsIT = row.cellIterator(); cellsIT.hasNext(); ) {
                        Cell cell = cellsIT.next();
                        objFormulaEvaluator.evaluate(cell);
                        if(row.getRowNum() == this.firstRowNumber ){
                            switch(cell.getCellType()){
                                case Cell.CELL_TYPE_STRING:
                                    if (cell.getStringCellValue().isEmpty()){
                                        cell.setCellValue("MissingKey"+cell.getColumnIndex());
                                        //System.out.println("cellContent: " + cell.getStringCellValue() + " index: " + cell.getColumnIndex());
                                        this.keysMap.put(cell.getColumnIndex(), cell.getStringCellValue());
                                        //this.keys.add(cell);
                                    }
                                    else{
                                        this.keysMap.put(cell.getColumnIndex(), cell.getStringCellValue());
                                        //this.keys.add(cell);
                                        //System.out.println("cellContent: " + cell.getStringCellValue() + " index: " + cell.getColumnIndex());
                                    }
                                break;
                                case Cell.CELL_TYPE_BLANK:
                                    cell.setCellValue("MissingKey"+cell.getColumnIndex());
                                    System.out.println("cellContent: " + cell.getStringCellValue() + " index: " + cell.getColumnIndex());
                                    this.keysMap.put(cell.getColumnIndex(), cell.getStringCellValue());
                                default:
                                    cell.setCellType(Cell.CELL_TYPE_STRING);
                                    this.keysMap.put(cell.getColumnIndex(), cell.getStringCellValue());
                                break;
                            }
                        }
                        else{
                            // cellType 1 = string, cellType 0 = number
                            if(cell.getCellType() == Cell.CELL_TYPE_STRING ){
                                if ((this.keysMap.get(cell.getColumnIndex())) != null){
                                    //System.out.println((this.keysMap.get(cell.getColumnIndex())) + " : " + cell.toString() + " typ: "+ cell.getColumnIndex());
                                    //this.json.append((this.keys.get(cell.getColumnIndex())).toString(), cell.toString());
                                    this.json.append(this.keysMap.get(cell.getColumnIndex()), cell.getStringCellValue());

                                }
                                else{
                                    //System.out.println((this.keysMap.get(cell.getColumnIndex())) + " : " + cell.toString() + " typ: "+ cell.getColumnIndex());
                                    keysMap.put(cell.getColumnIndex(), "MissingKey"+cell.getColumnIndex());
                                    this.json.append(this.keysMap.get(cell.getColumnIndex()), cell.getStringCellValue());
                                }
                            }
                            else if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
                                
                                if ((this.keysMap.get(cell.getColumnIndex())) != null){
                                    //if (DateUtil.isCellDateFormatted(cell)) {
                                        //String date = DataFormatter.formatCellValue(cell);
                                        //DataFormatter df = new DataFormatter();
                                        
 
                                        //System.out.println(cell.getDateCellValue() + " Format: " + new DataFormatter().formatCellValue(cell) );
                                    //}
                                    //System.out.println((this.keys.get(cell.getColumnIndex())).toString() + " : " + objDefaultFormat.formatCellValue(cell,objFormulaEvaluator) + " typ: "+ cell.getCellType());
                                    this.json.append(this.keysMap.get(cell.getColumnIndex()), objDefaultFormat.formatCellValue(cell,objFormulaEvaluator));
                                    //this.json.append((this.keys.get(cell.getColumnIndex())).toString(), objDefaultFormat.formatCellValue(cell,objFormulaEvaluator));
                                }
                                else{
                                    //System.out.println("key" + " : " + cell.toString());
                                    keysMap.put(cell.getColumnIndex(), "MissingKey"+cell.getColumnIndex());
                                    this.json.append(this.keysMap.get(cell.getColumnIndex()), objDefaultFormat.formatCellValue(cell,objFormulaEvaluator));
                                }
                            }
                        }
                    }

                    //json.put( "document", rowsArray );
                }
            }
            //System.out.println(this.json.toString());
            return this.json;
        
        
        } catch (FileNotFoundException ex) {
            Logger.getLogger(XlsxToJson.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XlsxToJson.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidFormatException | EncryptedDocumentException | JSONException ex) {
            Logger.getLogger(XlsxToJson.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
        }
        return null;
    }
    
    /*
    public JSONObject ConvertFile(){
        try {
           File inp = new File( this.fileName);
            //FileInputStream inp = new FileInputStream( this.fileName);
           Workbook workbook = WorkbookFactory.create( inp );

// Get the first Sheet.

           Sheet sheet = workbook.getSheetAt( 0 );
           this.firstRowNumber = sheet.getFirstRowNum();

            // Start constructing JSON.
            json = new JSONObject();

            // Iterate through the rows.
            JSONArray rowsArray = new JSONArray();
            for ( Iterator<Row> rowsIT = sheet.rowIterator(); rowsIT.hasNext(); ) {
                Row row = rowsIT.next();
                //System.out.println(row.getRowNum());

                JSONObject jRow = new JSONObject();

                // Iterate through the cells.
                JSONArray cells = new JSONArray();

                for ( Iterator<Cell> cellsIT = row.cellIterator(); cellsIT.hasNext(); ) {
                    Cell cell = cellsIT.next();
                    if(row.getRowNum() == this.firstRowNumber && !(cell.getStringCellValue().isEmpty())){
                        //System.out.print(cell.getColumnIndex() + "  ");
                        this.keys.add(cell);
                    }
                    else{
                        if(!(cell.getStringCellValue().isEmpty())){
                            cells.put( cell.getStringCellValue() ); 
                        }
                    }
                }
                //System.out.println(cells.toString());
                jRow.put( "cell", cells );
                rowsArray.put( jRow );
            }

            // Create the JSON.
            json.put( "rows", rowsArray );
            return json;

       } catch (JSONException | InvalidFormatException | EncryptedDocumentException ex) {
           Logger.getLogger(BcPrace.class.getName()).log(Level.SEVERE, null, ex);
       } catch (FileNotFoundException ex) {
            Logger.getLogger(XlsxToJson.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XlsxToJson.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    */
}
