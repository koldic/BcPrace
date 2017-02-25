/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Converters.Sql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author David
 */
public class SqlTable {
    private final String tableName;
    private List<String> keys;
    private Map<String, List<String>> columns;

    public SqlTable(String tableName) {
        this.tableName = tableName;
        this.keys = new ArrayList<>();
        this.columns = new LinkedHashMap<>();
        
        //System.out.println("Creating table: " + this.tableName);
    }
    
    private void insertKey(String key){
        if (keys.isEmpty()){
            this.keys.add(key);
        }
        else{
            boolean missingKey = true;
            for (String keyTmp : keys) {
                if (keyTmp.equals(key)) {
                    missingKey = false;
                }
            }
            if (missingKey){
                this.keys.add(key);
            }
        }
    }

    public String getTableName() {
        return tableName;
    }

    public void insertColumn (String key, String value){
        if(!this.columns.containsKey(key)){
            ArrayList<String> valueList = new ArrayList<>();
            valueList.add(value);
            this.columns.put(key, valueList);
            this.insertKey(key);
            //System.out.println("Inserting key: " + key);
        }
        else{
            //if (!this.columns.isEmpty()){
                this.columns.get(key).add(value);
            //}
        }
    }
    
    public Set getKeys (){
        return this.columns.keySet();
    }
    
    public String getKey (int index){
        return this.keys.get(index);
    }
    
    public JSONObject converTableToJson(){
        JSONObject jObject = new JSONObject();
        //JSONArray jArray = new JSONArray();
        
        try {
            //this.columns.forEach((key, value) -> System.out.println("key: "+key+" value: "+value));
            //JSONObject tmpObject = new JSONObject();
            //this.columns.forEach((key, value) -> tObject.put(key, value));
            
            jObject.putOnce(tableName, this.columns);
            //jObject.put(this.tableName, this.columns );
            //jArray.put(this.columns);
            System.out.println(jObject);
        } catch (JSONException ex) {
            Logger.getLogger(SqlTable.class.getName()).log(Level.SEVERE, null, ex);
        }
        //System.out.println(this.columns.entrySet());
        //System.out.println(jObject.toString());
        return jObject;
    }
    
}
