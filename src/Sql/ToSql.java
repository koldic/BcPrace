/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sql;

import Analyse.Item;
import Analyse.ItemType;
import bcprace.BcPrace;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author David
 */
public class ToSql {
    
    private final String databaseName;
    private StringBuilder createStructure;
    private StringBuilder createInsert;
    private StringBuilder insertValues;
    private final List<Item> items;
    //private Map<String, StringBuilder> tables;
    private Map<String, Map<String, List<String>>> values;
    private Map<String, String> rows;
    private Map<String, String> fKeys;
    private final DatabaseType dbType;
    private int tableNumber;
    private Boolean firstRow;
    private Boolean firstColumnValue;
    
    public ToSql(String dbName, List<Item> items, DatabaseType dbType){
        this.databaseName = dbName;
        this.items = items;
        this.dbType = dbType;
        this.createStructure = new StringBuilder();
        this.createInsert = new StringBuilder();
        this.insertValues = new StringBuilder();
        //this.tables = new LinkedHashMap<>();
        this.rows = new LinkedHashMap<>();
        this.values = new LinkedHashMap<>();
        this.fKeys = new LinkedHashMap<>();
        this.tableNumber = 0;
    }
    
    private void getArray(Object object2, String key) throws JSONException  {
        //System.out.println("in array");
        JSONArray jsonArr = (JSONArray) object2;
        //outerloop:
        
        for (int k = 0; k < jsonArr.length(); k++) {
            //System.out.println("array: " + jsonArr.toString());
            if (jsonArr.get(k) instanceof JSONObject) {
                JSONObject obj = jsonArr.getJSONObject(k);
                //this.fKeys.put(jsonArr.getJSONObject(k).toString(), key);
                if (!this.values.containsKey(key)){
                    //this.tables.put(key, new StringBuilder());
                    this.values.put(key, new LinkedHashMap<>());
                }
                //this.values.get(key).put("id_"+key, new ArrayList<>());
                //System.out.println("array table without values: "+ key);
                //this.tables.get(obj.toString()).append(this.createTable2(obj.toString())).append(addId2("id_"+obj.toString(), 10));
                //this.createTable(key, key);
                //this.addId("id_"+key, 10, key);
                ///*


                Iterator<Object> it = obj.keys();
                //System.out.println("keys: "+it.);
                int index = 0;
                while(it.hasNext()){
                    Object fKey = it.next();

                    if(!(obj.get(fKey.toString()) instanceof JSONObject) && !(obj.get(fKey.toString()) instanceof JSONArray)){
                        if (!this.values.get(key).containsKey(fKey)){
                            if ((this.values.get(key).size() > 0)){
                                    //System.out.println("Aparent: " + key + " key: " + fKey);
                                    this.addEmptyValues(key, fKey.toString(), this.arraySize(this.values.get(key)));     // když nezačínají všechny prvky v jObjektu od začátku                               
                                }
                                else{
                                    this.values.get(key).put(fKey.toString(), new ArrayList<>());
                                }
                        }
                        //System.out.println("Fkey: "+fKey + " val: " + obj.get(fKey.toString()));
                        this.fKeys.put(fKey.toString(), key);
                        //System.out.println("addding value:" + obj.get(fKey.toString()).toString() + " for key: " +fKey + " parent" + key);
                        this.values.get(key).get(fKey).add(obj.get(fKey.toString()).toString());
                        //System.out.println("obj: " + obj.toString() + " fkey: " + fKey);
                        //this.addRow(fKey.toString(), key);
                    }

                }
                if (this.differentArraySize(this.values.get(key))){
                    //System.out.println("1parent " + key + " values " + this.values.get(key) );
                    this.addEmptyValue(key, this.values.get(key));
                }
                //System.out.println("1parent " + key + " values " + this.values.get(key) );

                //this.addPrimaryKey("id_"+key, key);
                    //*/
                parseJson((JSONObject) jsonArr.get(k));
            } 

            else {
                //for (int i=0;i<jsonArr.length();i++){
                    
                //System.out.println("key: "+ key +" value: " + jsonArr.get(k));
                    //System.out.println(keyData.get(count).getKey()+" - "+key+" : "+jsonArr.get(k)+"  "+ (count+1));
                //}
            }
        }
        //this.layerChange = false;
    }
    
    //Map<String,Item> values = new HashMap<String,Item>();
    
    
    private void parseJson(JSONObject jsonObject) throws JSONException  {
        this.increaseTableNumber();
        String tmpTable = (this.databaseName.concat(Integer.toString(this.getTableNumber())));
        Iterator<Object> iterator = jsonObject.keys();
        
        while (iterator.hasNext()) {
            Object obj = iterator.next();
            //System.out.println("key: " + obj.toString());

            if (jsonObject.get(obj.toString()) instanceof JSONArray) {
                /*
                if (!this.tables.containsKey(obj.toString())){            
                    this.tables.put(obj.toString(), new StringBuilder());
                    System.out.println("new table: "+ obj.toString());
                    //this.tables.get(obj.toString()).append(createTable2(obj.toString()));
                    this.createTable(obj.toString());
                    
                }*/
                getArray(jsonObject.get(obj.toString()), obj.toString());
            } 
            
            else {
                
                if (jsonObject.get(obj.toString()) instanceof JSONObject) {
                    JSONObject obj2 = (JSONObject) jsonObject.get(obj.toString());
                    //Creating table with foreign keys
                    if (!this.values.containsKey(obj.toString())){
                        //this.tables.put(obj.toString(), new StringBuilder());
                        this.values.put(obj.toString(), new LinkedHashMap<>());
                    }
                    //System.out.println("table without values: "+ obj.toString());
                    //this.tables.get(obj.toString()).append(this.createTable2(obj.toString())).append(addId2("id_"+obj.toString(), 10));
                    //this.createTable(obj.toString(), obj.toString());
                    //this.addId("id_"+obj.toString(), 10, obj.toString());
                    ///*
                    Iterator<Object> it = (jsonObject.getJSONObject(obj.toString())).keys();
                    //System.out.println("keys: "+it.);
                    while(it.hasNext()){
                        String fKey = it.next().toString();
                        //System.out.println("obj: " + obj.toString() + " fkey: " + fKey);
                        //if((jsonObject.getJSONObject(obj.toString())).get(fKey) instanceof JSONObject  || (jsonObject.getJSONObject(obj.toString())).get(fKey) instanceof JSONArray){
                        //System.out.println("foreign keys: " + fKey);
                        if(!(obj2.get(fKey.toString()) instanceof JSONObject) && !(obj2.get(fKey.toString()) instanceof JSONArray)){
                            if (!this.values.get(obj.toString()).containsKey(fKey)){
                                if ((this.values.get(obj.toString()).size() > 0)){
                                    //System.out.println("parent: " + obj.toString() + " key: " + fKey + " value: " + jsonObject.getJSONObject(obj.toString()).get(fKey).toString());
                                    this.addEmptyValues(obj.toString(), fKey, this.arraySize(this.values.get(obj.toString())));     // když nezačínají všechny prvky v jObjektu od začátku                               
                                }
                                else{
                                    this.values.get(obj.toString()).put(fKey, new ArrayList<>());
                                }
                            }
                            this.values.get(obj.toString()).get(fKey).add(jsonObject.getJSONObject(obj.toString()).get(fKey).toString());
                        }    
                        this.fKeys.put(fKey, obj.toString());
                          //  this.addForeignKey(fKey, obj.toString(), obj.toString());
                            //this.tables.get(obj.toString()).append(this.addForeignKey2(fKey, obj.toString()));
                        //}
                        //else{



                            //this.addRow(fKey, obj.toString());
                            //System.out.println("tt " + fKey);
                            //this.tables.get(obj.toString()).append(this.addForeignKey2(fKey, obj.toString()));
                        //}
                        //this.addForeignKey(fKey, obj.toString());
                    }
                    if (this.differentArraySize(this.values.get(obj.toString()))){
                        //System.out.println("2parent " + obj.toString() + " values " + this.values.get(obj.toString()) );
                        this.addEmptyValue(obj.toString(), this.values.get(obj.toString()));
                    }
                    //this.addPrimaryKey("id_"+obj.toString(), obj.toString());
                        //*/
                    parseJson((JSONObject) jsonObject.get(obj.toString()));
                    
                    //this.tables.get(obj.toString()).append(this.addPrimaryKey2("id_"+obj.toString()));
                } 
                
                else {
                    
                    if (!this.rows.containsKey(obj.toString())){  
                        this.rows.put(obj.toString(), null);
                        //System.out.println("new row: "+ obj.toString());
                        //this.addRow(obj.toString());
                    }
                    if (!this.values.containsKey(obj.toString()) && !this.fKeys.containsKey(obj) ){
                        
                        if (!this.values.containsKey(tmpTable)){
                            this.values.put(tmpTable, new LinkedHashMap<>());
                            //System.out.println("new table with objects: "+ tmpTable);
                            //this.tables.get(obj.toString()).append(this.createTable2(obj.toString())).append(addId2("id_"+obj.toString(), 10));
                            //this.createTable(tmpTable, tmpTable);
                            //this.addId("id_"+tmpTable, 10, tmpTable);
                        }
                        if (!this.values.get(tmpTable).containsKey(obj.toString())){
                            if ((this.values.get(tmpTable).size() > 0)){
                                    //System.out.println("parent: " + tmpTable + " key: " + obj.toString());
                                    this.addEmptyValues(obj.toString(), obj.toString(), this.arraySize(this.values.get(tmpTable)));     // když nezačínají všechny prvky v jObjektu od začátku                               
                                }
                            else{
                                this.values.get(tmpTable).put(obj.toString(), new ArrayList<>());
                            }
                            
                            //this.values.get(tmpTable).put(obj.toString(), new ArrayList<>());
                        }
                        
                        //this.addRow(obj.toString(), tmpTable);
                    }
                }
                
            } 
        }
        if (!this.values.isEmpty() && this.values.containsKey(tmpTable)) {
            //System.out.println("123" + tmpTable);
            //this.endOfTable(tmpTable);
        }
        this.decreaseTableNumber();
    }
    
    private String getDate(Item dateItem){
        
        DateFormat oFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH);
        DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        Date date = null;
        try {
           date = oFormat.parse("August 21, 2012");
        } catch (ParseException ex) {
           Logger.getLogger(BcPrace.class.getName()).log(Level.SEVERE, null, ex);
        }
        String formattedDate = targetFormat.format(date);
        System.out.println(formattedDate); // prints 2010-01-01 14:30:59
        
        return null;
    }
    
    private void createTable(String tableName, String key){
        this.createStructure.append(String.format("-- Table structure for table `%s`", tableName)).append("\n");
        this.createStructure.append(String.format(SqlStatement.CreateTable.getStatement(dbType), tableName)).append("\n");
        //this.createStructure.append(String.format("-- Table structure for table `%s`", tableName)).append("\n");
        //this.createStructure.append(String.format(SqlStatement.CreateTable.getStatement(dbType), tableName)).append("\n");
    }
    
    private String createTable2(String tableName){
        return ((String.format("-- Table structure for table `%s`", tableName))+"\n"+(String.format(SqlStatement.CreateTable.getStatement(dbType), tableName))+"\n");
    }
    
    private void endOfTable(String key){
        this.createStructure.append(SqlStatement.EndCreateTable.getStatement(dbType)).append("\n").append("\n");
    }
    
    private String endOfTable2(){
        return (SqlStatement.EndCreateTable.getStatement(dbType))+"\n";
    }
    
    private void addRow(String rowName, String key, Boolean firstRow){
        ItemType analyzedType;
        for (Item item : this.items) {
            if (item.getParent().equals(rowName)){
                Map<ItemType, Boolean> resultField = item.provideResultField();
                String analysedType = null;
                Iterator<ItemType> iterator = resultField.keySet().iterator();
                while(iterator.hasNext()){
                    analyzedType = iterator.next();
                    if (resultField.get(ItemType.IsNull)){
                        //System.out.println("DatTy: " + item.isNotNull() + " item: " + item.getValue() + " key: " + item.getParent());
                        // to get right data type
                        if(resultField.get(analyzedType)){
                            //System.out.println(" item + a" );
                            if (firstRow){
                                this.createStructure.append(String.format(SqlStatement.addRow.getStatement(dbType), rowName)).append(" ").append(String.format(SqlStatement.valueOf(analyzedType.toString()).getStatement(dbType), item.getLenght()));
                            }
                            else{
                                this.createStructure.append(", \n").append(String.format(SqlStatement.addRow.getStatement(dbType), rowName)).append(" ").append(String.format(SqlStatement.valueOf(analyzedType.toString()).getStatement(dbType), item.getLenght()));
                            }
                            //System.out.println("ano not null" + analyzedType);
                            break;
                        }
                    }
                    else{
                        if(resultField.get(analyzedType)){
                            //System.out.println(" item + " + analyzedType);
                            if (item.isIsDate()){
                                // Select date format for database type
                                //System.out.println(" item + date " + item.getValue() + " Format: " + item.getDateFormatCount().get(0).getDateFormat());
                                if (firstRow){
                                    this.createStructure.append(String.format(SqlStatement.addRow.getStatement(dbType), rowName)).append(" ").append(String.format(SqlStatement.valueOf(analyzedType.toString()).getStatement(dbType), item.getDateFormatCount().get(0).getDateFormat()))
                                    .append(" ").append(SqlStatement.NotNull.getStatement(dbType));
                                }
                                else{
                                    this.createStructure.append(", \n").append(String.format(SqlStatement.addRow.getStatement(dbType), rowName)).append(" ").append(String.format(SqlStatement.valueOf(analyzedType.toString()).getStatement(dbType), item.getDateFormatCount().get(0).getDateFormat()))
                                    .append(" ").append(SqlStatement.NotNull.getStatement(dbType));
                                }
                                break;
                            }
                            else{
                                //System.out.println(" item + nodate " + item.getValue());
                                if (firstRow){
                                    this.createStructure.append(String.format(SqlStatement.addRow.getStatement(dbType), rowName)).append(" ").append(String.format(SqlStatement.valueOf(analyzedType.toString()).getStatement(dbType), item.getLenght()))
                                    .append(" ").append(SqlStatement.NotNull.getStatement(dbType));
                                }
                                else{
                                    this.createStructure.append(", \n").append(String.format(SqlStatement.addRow.getStatement(dbType), rowName)).append(" ").append(String.format(SqlStatement.valueOf(analyzedType.toString()).getStatement(dbType), item.getLenght()))
                                    .append(" ").append(SqlStatement.NotNull.getStatement(dbType));
                                }
                                break;
                            }
                            //System.out.println("ano not null" + analyzedType);
                            //break;
                        }
                    }
                }
                
                break;
            }
        }
        
        //this.createStructure.append(", \n").append(String.format(SqlStatement.addRow.getStatement(dbType), rowName));
    }
    
    private String addRow2(String rowName){
        return (String.format(SqlStatement.addRow.getStatement(dbType), rowName));
    }
    
    private void addId(String idName, int size, String key){
        this.createStructure.append(String.format(SqlStatement.addRow.getStatement(dbType), idName)).append(" ").append(String.format(SqlStatement.UnsignedIntNumber.getStatement(dbType), size))
            .append(" ").append(SqlStatement.NotNull.getStatement(dbType)).append(" ").append(SqlStatement.AutoIncrement.getStatement(dbType));
    }
    
    private String addId2(String idName, int size){
        return ((String.format(SqlStatement.addRow.getStatement(dbType), idName))+" "+(String.format(SqlStatement.UnsignedIntNumber.getStatement(dbType), size))
            +" "+(SqlStatement.NotNull.getStatement(dbType))+" "+(SqlStatement.AutoIncrement.getStatement(dbType)));
    }
    
    private void addPrimaryKey(String pKey, String key){
        this.createStructure.append(", \n").append(SqlStatement.PrimaryKey.getStatement(dbType)).append(" (`").append(pKey).append("`)\n);\n\n");
    }
    
    private String addPrimaryKey2(String pKey){
        return (", \n")+(SqlStatement.PrimaryKey.getStatement(dbType))+(" (`")+(pKey)+("`)\n);\n\n");
    }
    
    private void addForeignKey(String fKey, String parent, String key){
        this.createStructure.append(",\n").append(String.format(SqlStatement.ForeignKey.getStatement(dbType), fKey, parent, fKey));
    }
    
    private String addForeignKey2(String fKey, String parent){
        return (",\n")+(String.format(SqlStatement.ForeignKey.getStatement(dbType), fKey, parent, fKey));
    }
    
    private void insertTable(String tableName){
        this.createInsert.append(String.format(SqlStatement.Insert.getStatement(dbType), tableName));
    }
    
    private void insertColumn(String columnName, Boolean firstColumn){
        if (firstRow){
            this.createInsert.append(String.format(SqlStatement.InsertColumn.getStatement(dbType), columnName));
        }
        else{
            this.createInsert.append(", ").append(String.format(SqlStatement.InsertColumn.getStatement(dbType), columnName));
        }
    }
    
    private void endOfInserColumn(){
        this.createInsert.append(SqlStatement.EndInsert.getStatement(dbType)).append("\n");
    }
    
    private void insertValues(){
        this.createInsert.append(String.format(SqlStatement.InsertValues.getStatement(dbType)));
    }
    
    private void endOfInserValues(){
        this.createInsert.append(SqlStatement.EndofInsertValues.getStatement(dbType)).append("\n").append("\n");
    }
    
    private void addValue(String value, Boolean firstValue){
        if (firstValue){
            this.createInsert.append("\n").append("( ").append(String.format(SqlStatement.InsertValue.getStatement(dbType), value));
        }
        else{
            this.createInsert.append(", ").append(String.format(SqlStatement.InsertValue.getStatement(dbType), value));
        }
    }
    
    private void endOfAddValue(Boolean lastLine){
        if (lastLine){
            this.createInsert.append(SqlStatement.EndofInsertValues.getStatement(dbType)).append("\n");
        }
        else{
            this.createInsert.append(SqlStatement.EndofInsertValue.getStatement(dbType));
        }
    }
    
    public String buildSql(JSONObject jsonObject){
        this.createStructure.append("-- Database ").append(this.databaseName).append("\n");
        this.createStructure.append(String.format(SqlStatement.CreateDatabase.getStatement(dbType), this.databaseName)).append("\n").append("\n");
        
        try {
            /*for (Item item : items){
            this.createStructure.append(String.format("-- Table structure for table `%s`", item.getParent())).append("\n");
            this.createStructure.append(String.format(SqlStatement.CreateTable.getStatement(), item.getParent())).append("\n");
            }
            */
            
            this.parseJson(jsonObject);
        } catch (JSONException ex) {
            Logger.getLogger(ToSql.class.getName()).log(Level.SEVERE, null, ex);
        }
        //dopln

        this.values.forEach((key, value)->{
            //System.out.println("key: " + key + " array: " + value);
            this.firstRow = true;
            this.createTable(key, key);
            if (!this.values.get(key).isEmpty()){
                this.insertTable(key);
            }
            
            value.forEach((key2, value2)->{
                this.addRow(key2, key2, firstRow);
                if (!this.values.get(key).isEmpty()){
                    this.insertColumn(key2, firstRow);
                }

                this.firstRow = false;
                
            });
            this.endOfTable(key);
            if (!this.values.get(key).isEmpty()){
            
                this.endOfInserColumn();
                this.insertValues();

                for (String arKey : value.keySet()){
                    for (int j=0;j<value.get(arKey).size();j++){
                        this.firstColumnValue = true;
                        for(String keys : value.keySet()){
                            if (j<value.get(keys).size()){
                                //System.out.println("key: " + keys + " value: " + value.get(keys).get(j));
                                this.addValue(value.get(keys).get(j), this.firstColumnValue);
                            }
                            this.firstColumnValue = false;
                        }
                        if(value.get(arKey).size()-1 == j){
                            this.endOfAddValue(true);
                        }
                        else{
                            this.endOfAddValue(false);
                        }
                    }
                    break;
                }
            }
            
            //this.endOfInserValues();
            //this.createStructure.append(value);
            //System.out.println("key: "+ key + value.toString());
        });
        
        
        return (this.createStructure.toString()+this.createInsert.toString());
    }
    
    public void increaseTableNumber (){
        this.tableNumber++;
    }
    
    public void decreaseTableNumber (){
        this.tableNumber--;
    }
    
    public int getTableNumber (){
        return this.tableNumber;
    }
    
    private int arraySize(Map <String, List<String>> arrays){
        
        IntegerProperty maxSize = new SimpleIntegerProperty();
        maxSize.set(0);
        arrays.forEach((key,value) -> {
            if (maxSize.get() < value.size()){
                maxSize.set(value.size());
                //System.out.println("1key: " + key + " value: " + value + " mSize" + maxSize);
            }
        });
        return maxSize.get();
                
        //return this.values.size();
    }
    
    private void addEmptyValues(String parent, String key, int size){
        List<String> arrList = new ArrayList<>();
        for (int i=1; i < size; i++){
            arrList.add("");
        }
        this.values.get(parent).put(key, arrList);
        //System.out.println("Adding key: " + key +" array: "+ arrList.toString()+ arrList.size());
    }
    
    private boolean differentArraySize(Map <String, List<String>> arrays){    
        IntegerProperty size = new SimpleIntegerProperty();
        BooleanProperty differentSize = new SimpleBooleanProperty();
        differentSize.set(false);
        size.set(-1);
        
        arrays.forEach((key,value) -> {
            if (size.get() == -1){
                size.set(value.size());
            }
            else{
                if (size.get() != value.size()){
                differentSize.set(true);
                }
            }
        });
        return differentSize.get();
    }
    
    private void addEmptyValue(String parrent, Map <String, List<String>> arrays){
        int maxSize = this.arraySize(arrays);
        
        arrays.forEach((key,value) -> {
            if (maxSize > value.size()){
                this.values.get(parrent).get(key).add("");
                int index = this.getItemIndex(key);
                if (index !=-1){
                    this.items.get(index).setNotNull(false);
                }
            }
        });
    }
    
    private int getItemIndex(String key){
        for (int i=0; i<this.items.size(); i++){
            if(items.get(i).getParent() == null ? key == null : items.get(i).getParent().equals(key)){
                return i;
            }
        }
        return -1;
    }
}
