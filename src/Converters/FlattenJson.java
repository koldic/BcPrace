/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Converters;

import Analyse.Item;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
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
public class FlattenJson {
    
    private final String dataName;
    private Map<String, Map<String, List<String>>> values;
    private Map<String, String> rows;
    private Map<String, String> fKeys;
    private final JSONObject jsonObject;
    private final JSONArray jsonArray;
    private final List<Item> items;
    private int tableNumber;
    private final Boolean jsonObjectStart;
    
    public FlattenJson(JSONObject jsonObject, String dataName, List<Item> items){
        this.jsonObject = jsonObject;
        this.jsonArray = null;
        this.tableNumber = 0;
        this.dataName = dataName;
        this.jsonObjectStart = true;
        this.items = items;
        
        this.rows = new LinkedHashMap<>();
        this.values = new LinkedHashMap<>();
        this.fKeys = new LinkedHashMap<>();
    }
    
    public FlattenJson(JSONArray jsonArray, String dataName, List<Item> items){
        this.jsonObject = null;
        this.jsonArray = jsonArray;
        this.tableNumber = 0;
        this.dataName = dataName;
        this.jsonObjectStart = false;
        this.items = items;
        
        this.rows = new LinkedHashMap<>();
        this.values = new LinkedHashMap<>();
        this.fKeys = new LinkedHashMap<>();
    }
    
    public Map<String, Map<String, List<String>>> makeJsonFlat() {
        if(this.jsonObjectStart){
            try {
                this.parseJson(this.jsonObject);
            } catch (JSONException ex) {
                Logger.getLogger(FlattenJson.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else{
            try {
                this.getArray(jsonArray, dataName);
            } catch (JSONException ex) {
                Logger.getLogger(FlattenJson.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        /*
        this.values.forEach((key, value)->{
        
            System.out.println("fattenJson: " + value);
        
        });
        */
        return this.values;
        
        
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
        String tmpTable = (this.dataName.concat(Integer.toString(this.getTableNumber())));
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
                                    this.addEmptyValues(tmpTable, obj.toString(), this.arraySize(this.values.get(tmpTable)));     // když nezačínají všechny prvky v jObjektu od začátku                               
                                }
                            else{
                                this.values.get(tmpTable).put(obj.toString(), new ArrayList<>());
                            }
                            this.values.get(tmpTable).get(obj.toString()).add(jsonObject.get(obj.toString()).toString());
                            
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
        //System.out.println("Adding key: " + key +" parent: "+ parent);
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
