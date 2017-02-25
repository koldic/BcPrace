/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Converters.Sql;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author David
 */
public class SqlToJson {
    private final String fileName;
    private ResultSet resultSet;

    public SqlToJson(String fileName) {
        this.fileName = fileName;
    }
    
    public JSONObject convertFile(){
        Map <String, SqlTable> tables = this.readSqlFile();
        JSONArray resultArray = new JSONArray();
        
        Iterator mapIterator = tables.entrySet().iterator();
        JSONObject tmpObject = new JSONObject();
        for (String key : tables.keySet()) {
            JSONObject jResult = tables.get(key).converTableToJson();
            resultArray.put(jResult);
            //System.out.println(jResult);
        }
        JSONObject resultObject = new JSONObject();
        try {
            resultObject.putOnce("database", resultArray);
        } catch (JSONException ex) {
            Logger.getLogger(SqlToJson.class.getName()).log(Level.SEVERE, null, ex);
        }
        //System.out.println(resultObject);
        return resultObject;
    }
    
    
    private Map <String, SqlTable> readSqlFile (){
        long startTime = System.currentTimeMillis();
        FileInputStream inputStream = null;
        Scanner scanner = null;
        Map <String, SqlTable> tables = new LinkedHashMap<>();
        
        try {
            inputStream = new FileInputStream(this.fileName);
            scanner = new Scanner(inputStream, "UTF-8");
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                /*
                if (line.matches(".*Table structure for table.*")){
                    Pattern pattern = Pattern.compile("`(.*?)`");
                    Matcher match = pattern.matcher(line);
                    match.find();
                    System.out.println(match.group(1));
                    SqlTable table = new SqlTable(match.group(1));
                    tables.add(table);
                    //while(m.find()) {
                        //System.out.println(m.group(1));
                    //}
                    //String tableName = line.split("[``]").;
                    //System.out.println("mateches " + tableName);
                }*/
                if (line.matches(".*INSERT INTO.*|.*insert  into.*")){
                    Pattern pattern = Pattern.compile("`(.*?)`");
                    Matcher match = pattern.matcher(line);
                    String tableName;
                    
                    if(match.find()){
                        //System.out.println(match.group(1));
                        tableName = match.group(1);
                        if(!tables.containsKey(tableName)){
                            tables.put(tableName, new SqlTable(tableName));
                            //System.out.println(tables.keySet() + " adding:" + tableName);
                        }
                        
                        //SqlTable table = new SqlTable(match.group(1));
                        
                        
                        pattern = Pattern.compile("\\((.*?)\\)");
                        match = pattern.matcher(line);

                        if(match.find()){
                            //System.out.println(match.group(1));
                            
                            pattern = Pattern.compile("`(.*?)`");
                            match = pattern.matcher(line);
                            while(match.find()){
                                
                                if (!match.group(1).equals(tableName)){
                                    //System.out.println(match.group(1));
                                    tables.get(tableName).insertColumn(match.group(1), null);
                                    //System.out.println("Keys in table: " + tables.get(tableName).getKeys());
                                }
                            }
                            
                            if (line.matches(".*values.*|.*VALUES.*|.*Values.*")){
                                while(true){
                                    line = scanner.nextLine();
                                    if (line.matches("\\((.*?)\\).*")){
                                        line = line.replace("\\'", "\\´");
                                        //System.out.println(line);
                                        pattern = Pattern.compile("\\d{1,100}|'(.*?)'|NULL");
                                        match = pattern.matcher(line);
                                        
                                        //System.out.println(" count: " + match.groupCount());
                                        Pattern patternString = Pattern.compile("'(.*?)'");
                                        Matcher matchString;
                                        int i=0;
                                        while(match.find()){
                                            String result = match.group().replace("'", "");
                                            //System.out.println(result + " result ");
                                            tables.get(tableName).insertColumn(tables.get(tableName).getKey(i), result);
                                            i++;
                                        }
                                    }
                                    
                                    if (line.matches(".*\\)\\;.*")){
                                        //System.out.println("Semicolon Found" + line);
                                        break;
                                    }
                                }
                                
                            }
                        }
                        else{
                            System.out.println("No col. names found!");
                        }
                    }
                    else {
                    System.out.println("No table found!");
                    }
                }
                
                
        // System.out.println(line);
            }
            //System.out.println("tables: " + tables.keySet() + " columns: " + tables.get("user_details").getKeys());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SqlToJson.class.getName()).log(Level.SEVERE, null, ex);
        }
        //long estimatedTime = System.currentTimeMillis()- startTime;
        //System.out.println("time: " + estimatedTime);
        //System.out.println(tables);
        return tables;
    }
}


