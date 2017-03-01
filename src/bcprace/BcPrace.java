/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//http://codebeautify.org/

package bcprace;

import Converters.Xlsx.XlsxToJson;
import Converters.CsvToJson;
import Converters.Sql.SqlToJson;
import Analyse.*;
import Converters.Csv.JsonToCsv;
import Converters.FlattenJson;
import Converters.JsonToXml;
import Converters.Xlsx.JsonToXlsx;
import Sql.*;
import File.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;


/**
 *
 * @author David
 */
public class BcPrace {

   public static void main(String argv[]) {
   
        String xmlDocument = "C:\\Users\\David\\Desktop\\bPrace\\dd\\adresy.xml";
        String xmlDocument2 = "C:\\Users\\David\\Desktop\\bPrace\\dd\\test.xml";
        String xmlDocument3 = "C:\\Users\\David\\Desktop\\bPrace\\dd\\Heureka.xml";
        String jsonDocument = "C:\\Users\\David\\Desktop\\bPrace\\dd\\adresy.json";
        String jsonDocument2 = "C:\\Users\\David\\Desktop\\bPrace\\dd\\allstars.json";
        String jsonDocument3 = "C:\\Users\\David\\Desktop\\bPrace\\dd\\crocs.json";
        String csvDocument = "C:\\Users\\David\\Desktop\\bPrace\\dd\\AllstarFull.csv";
        String xlsxDocument = "C:\\Users\\David\\Desktop\\bPrace\\dd\\crocsZmenaTechnickehoPopisu.xlsx";
        String xlsxDocument2 = "C:\\Users\\David\\Desktop\\bPrace\\dd\\Crocs_Sklad2.xlsx";
        String xlsDocument = "C:\\Users\\David\\Desktop\\bPrace\\dd\\crocsZmenaTechnickehoPopisu.xls";
        String jsonDocumentOrg = "C:\\Users\\David\\Desktop\\bPrace\\dd\\sample.json";
        String jsonDocumentOrg2 = "C:\\Users\\David\\Desktop\\bPrace\\dd\\sample2.json";
        String jsonDocumentOrg3 = "C:\\Users\\David\\Desktop\\bPrace\\dd\\sample3.json";
        String jsonDocumentOrg4 = "C:\\Users\\David\\Desktop\\bPrace\\dd\\sample4.json";
        String jsonDocumentOrg5 = "C:\\Users\\David\\Desktop\\bPrace\\dd\\sample_2.json";        
        String jsonSql = "C:\\Users\\David\\Desktop\\bPrace\\dd\\jsonToSql.json";
        String sqlDocument1 = "C:\\Users\\David\\Desktop\\bPrace\\dd\\Sample-SQL-File-1000-Rows.sql";
        String sqlDocument2 = "C:\\Users\\David\\Desktop\\bPrace\\dd\\mysqlsampledatabase.sql";
        //XmlParser xmlParser = new XmlParser(document2);
        //xmlParser.Parse();
        //XmlParserDom xmlParserDom = new XmlParserDom(document);
        //xmlParserDom.xmlParse();
        
        String outputPath = "C:\\Users\\David\\Desktop\\bPrace\\dd\\output\\";
        String sqlOutputFileName = "sqlOut.sql";
        String xmlOutputFileName = "xmlOut.xml";
        String jsonOutputFileName = "jsonOut.json";
        String csvOutputFileName = "csvOut.csv";
        String xlsxOutputFileName = "xlsxOut.xlsx";
        String xlsOutputFileName = "xlsxOut.xls";
        
        String inputFileName = xmlDocument3;
        
        FileCheck fileChecker = new FileCheck(inputFileName);
        FileTypes fileType = fileChecker.checkFile();
        String fileCoding = fileChecker.checkCoding();
        
        JSONObject jObject = null;
        JSONArray jArray = null;
        
        switch (fileType){
            case csv:
                CsvToJson csvConvertor = new CsvToJson(inputFileName);
                jArray = csvConvertor.ConvertFile();
                //JsonWorks.saveJsonFileArray(jsonDocument2, arrayCsv);
                break;
            case xml:
                XmlToJson xmlConvertor = new XmlToJson(inputFileName);
                jObject = xmlConvertor.convertFile();
                //JsonWorks.saveJsonFile(jsonDocument, jsonObjectXml);
                break;
            case xlsx:
                XlsxToJson xlsxConvertor = new XlsxToJson(inputFileName);
                jObject = xlsxConvertor.ConvertColumns(true);
                //JsonWorks.saveJsonFile(jsonDocument3, jsonObjectXlsx);
                break;
            case xls:
                XlsxToJson xlsConvertor = new XlsxToJson(inputFileName);
                jObject = xlsConvertor.ConvertColumns(false);
                //JsonWorks.saveJsonFile(jsonDocument3, jsonObjectXlsx);
                break;
            case json:
                JsonToJsonObject converToJsonObject = new JsonToJsonObject(inputFileName); //jsonDocumnetOrg
                jObject = converToJsonObject.ConvertFile();
                //System.out.println(jsonObjectJson.toString());
                break;
            case sql:
                SqlToJson sqlConventor = new SqlToJson(inputFileName);
                jObject = sqlConventor.convertFile();
                //JsonWorks.saveJsonFile("C:\\Users\\David\\Desktop\\bPrace\\dd\\mysqlsampledatabase.json", jObject);
                //System.out.println(jArray);
        }
        
        Analyzer analyzer = new Analyzer();
        List<Item> analyzedItems = null;
        try {
            analyzer.parseJson(jObject, 10);
            //analyzer.parseJson(jsonObjectJson, 10);
            //analyzer.getArray(jArray, 10, "key");
            //analyzer.parseJson(jsonObjectXlsx, 10);
            analyzedItems = analyzer.Analyse();
            analyzer.PrintStorage(); 
            /*
            for (Item it : analyzedItems){
                System.out.println("Key: " + it.getParent() + " values" + it.provideResultField());
            }*/
            
        } catch (JSONException ex) {
            Logger.getLogger(BcPrace.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //JsonToCsv jsonCsv = new JsonToCsv("C:\\Users\\David\\Desktop\\bPrace\\dd\\test.csv");
        //JsonToXml jsonXml = new JsonToXml("C:\\Users\\David\\Desktop\\bPrace\\dd\\test.xml");
        //jsonXml.convertJson(jObject);
        
        //50° 4' 54.5505674" N 14° 26' 29.5019531" E
        //Item testItem = new Item("key","50° 4' 54.5505674\" N");
        //Item testItem2 = new Item("key","Pondělí, června 30, 2014"); //Monday, June 30, 2014        Po, Čer 30, 2014
        
        //testItem.ChecTypes();
        //System.out.println("SEcond: ");
        //testItem2.ChecTypes();
        FlattenJson flat = new FlattenJson(jObject, "test", analyzedItems);
        
        //ToSql sqlConvertor = new ToSql("test", flat.makeJsonFlat(), DatabaseType.MySql, analyzedItems);
        //String sqlOutput = sqlConvertor.buildSql(jObject);
        //FileWrite.writeStringtoFile(sqlOutput, outputPath, sqlOutputFileName);
        //sqlOutput = null;
        
        //JsonToCsv toCsv = new JsonToCsv(outputPath, csvOutputFileName, flat.makeJsonFlat(), false);
        //toCsv.convertToCsv();
        
        // Path to save file; FileName (xls or xlsx); values; true - everything in one sheet;
        JsonToXlsx toXlsx = new JsonToXlsx(outputPath, xlsxOutputFileName, flat.makeJsonFlat(), true);
        toXlsx.convertToXlsx();
        
        JsonWorks.saveJsonFile(jObject, outputPath, jsonOutputFileName);
        
        JsonToXml convertToXml = new JsonToXml();
        String xmlOutput = convertToXml.convertJson(jObject);
        FileWrite.writeStringtoFile(xmlOutput, outputPath, xmlOutputFileName);
        xmlOutput = null;
        //System.out.println(sqlConvertor.buildSql(jObject));
        //System.out.println(sqlConvertor.buildSql(jsonObjectXlsx));
        
        //DatabaseType.MySql.getDateTypes().forEach((item)->System.out.println("item + " + String.format(item, "4")));
        
   }
}
