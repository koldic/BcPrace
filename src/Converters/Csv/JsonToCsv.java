/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Converters.Csv;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author David
 */
public class JsonToCsv {
    private final String outputFileName;
    private List<String> keys;
    private Map<String, List<String>> keyData;

    public JsonToCsv(String outputFileName) {
        this.outputFileName = outputFileName;
        this.keys = new ArrayList<>();
        this.keyData = new LinkedHashMap<>();
    }
    
    private boolean newKey(String key){
        boolean missingKey = true;
        for (String value : this.keys) {
            if (value.equals(key)) {
                missingKey = false;
            }
        }
        return missingKey;
    }
    
    private void addKey(String key){
        if(this.newKey(key)){
            this.keys.add(key);
        }
    }
    /*
    public void getArray(Object object2, int limit, String key) throws JSONException  {

        JSONArray jsonArr = (JSONArray) object2;
        //System.out.println(jsonArr.toString());
        outerloop:
        for (int k = 0; k < jsonArr.length(); k++) {
            
            if (jsonArr.get(k) instanceof JSONObject) {
                //System.out.println("Inside JsonObje " + (JSONObject) jsonArr.get(k));
                parseJson(jsonArr.getJSONObject(k), limit);
            } 

            else {
                boolean missingKey = false;

                for (int i=0;i<count;i++){

                    if (keyData.get(i).getKey() == null ? key == null : keyData.get(i).getKey().equals(key)) {
                        missingKey = false;

                        if ((keyData.get(i).isLimitReached())) {
                            break outerloop;
                        }

                        else{
                            keyData.get(i).AddToStorage(jsonArr.get(k).toString());
                            //System.out.println(keyData.get(i).getKey()+" - "+key+" : "+jsonArr.get(k)+"  "+ count);
                            break;
                        }
                    }

                    else {
                        missingKey = true;
                    }
                }

                if(missingKey){
                    this.keyData.add(new AnalyzerStorage(key, limit));
                    keyData.get(count).AddToStorage(jsonArr.get(k).toString());
                    //System.out.println(keyData.get(count).getKey()+" - "+key+" : "+jsonArr.get(k)+"  "+ (count+1));
                    count++;
                    //????missingKey = false;
                }
            }
        }
    }
    
    //Map<String,Item> values = new HashMap<String,Item>();
    
    
    public void parseJson(JSONObject jsonObject, int limit) throws JSONException  {
        //System.out.println(jsonObject);
        Iterator<Object> iterator = jsonObject.keys();
        while (iterator.hasNext()) {
            Object obj = iterator.next();
            //System.out.println("key: " + obj.toString());
            if(this.keyData.isEmpty()){
                this.keyData.add(new AnalyzerStorage(obj.toString(), limit));
                count++;
            }

            if (jsonObject.get(obj.toString()) instanceof JSONArray) {
                //System.out.println("inside JArray");
                //System.out.println(obj.toString());
                getArray(jsonObject.getJSONArray(obj.toString()), limit, obj.toString());
                //getArray(jsonObject.get(obj.toString()), limit, obj.toString());
            } 
            
            else {
                    //System.out.println("not inside JObject " + "key :" + obj.toString() + jsonObject.toString());
                if (jsonObject.get(obj.toString()) instanceof JSONObject) {
                    //System.out.println("inside JObject");
                    parseJson((JSONObject) jsonObject.get(obj.toString()), limit);
                }
                else if(jsonObject.get(obj.toString()) instanceof LinkedHashMap){
                    //System.out.println("inside LinkedHashMap");
                    LinkedHashMap lMap = ((LinkedHashMap)(jsonObject.get(obj.toString())));
                    Iterator entries = lMap.keySet().iterator();
                    //Iterator entries = lMap.entrySet().iterator();
                    while(entries.hasNext()) {
                        String key = entries.next().toString();
                        ArrayList values;
                        
                        if (this.newKey(key)){
                            this.keyData.add(new AnalyzerStorage(key, limit));
                        }
                        for (int i=0; i<=limit;i++){
                            //System.out.println("eaq");
                            for (int j=0; j<this.keyData.size();j++){
                                //System.out.println("1 " + thisEntry.getKey().toString() + "   " + this.keyData.get(j));
                                if (this.keyData.get(j).getKey().equals(key)){
                                    if (lMap.get(key) instanceof ArrayList){
                                        values = (ArrayList) lMap.get(key);
                                        if (i < values.size() && values.get(i) != null){
                                            //System.out.println("2 " + key + " value: " + values.toString());
                                            this.keyData.get(j).AddToStorage(values.get(i).toString());
                                        }
                                    }
                                }
                            }
                        }
                        //System.out.println(thisEntry.getKey());
                    }
                    
                }
                else {
                    boolean missingKey = false;
                    
                    for (int i=0;i<count;i++){
                        //System.out.println("to add: " + obj.toString() + " has key: "+ keyData.get(i).getKey());
                        if (keyData.get(i).getKey().equals(obj.toString())) {
                            missingKey = false;
                            
                            if ((keyData.get(i).isLimitReached())) {
                                break;
                            }
                            else{
                                keyData.get(i).AddToStorage(jsonObject.get(obj.toString()).toString());
                                //System.out.println(obj.toString() + "\t"+ jsonObject.get(obj.toString())+"  "+ count);
                                break;
                            }
                        }
                        else {
                            missingKey = true;
                        }
                    }
                    if(missingKey){

                        this.keyData.add(new AnalyzerStorage(obj.toString(), limit));
                        keyData.get(count).AddToStorage(jsonObject.get(obj.toString()).toString());
                        //System.out.println(keyData.get(count).getKey()+" - "+obj.toString()+" : "+jsonObject.get(obj.toString()).toString()+"  "+ (count+1));
                        count++;
                        //??????missingKey = false;
                    }
                }
            }
        }
    }
    */
    
    
    public void convertToCsv(Object inputFile) throws JSONException{
        
        if (inputFile instanceof JSONObject){
            System.out.println("is jsonobject");
            JSONObject inputJson = (JSONObject) inputFile;
            Iterator<Object> iterator = inputJson.keys();
            while (iterator.hasNext()) {
                Object obj = iterator.next();
                System.out.println("Key: " + obj.toString());
                this.addKey(obj.toString());
                if (inputJson.get(obj.toString()) instanceof JSONArray) {
                    this.convertToCsv(inputJson.getJSONArray(obj.toString()));
                }
                else if(inputJson.get(obj.toString()) instanceof JSONObject){
                    this.convertToCsv(inputJson.getJSONObject(obj.toString()));
                }
                else if (inputJson.get(obj.toString()) instanceof LinkedHashMap) {
                    System.out.println("hashmap");
                    this.convertToCsv(inputJson.get(obj.toString()));
                }
            }
        }
        else if(inputFile instanceof JSONArray){
            System.out.println("is jsonarray" + inputFile.toString());
            JSONArray inputJsonArray = (JSONArray) inputFile;
            
            for (int k = 0; k < inputJsonArray.length(); k++) {
            
                if (inputJsonArray.get(k) instanceof JSONObject) {
                    System.out.println("Inside JsonObje " + (JSONObject) inputJsonArray.get(k));
                    this.convertToCsv(inputJsonArray.getJSONObject(k));
                }
                else{
                    System.out.println("2still in array" + inputJsonArray.toString());
                }
            }
            
        }
    }
}
