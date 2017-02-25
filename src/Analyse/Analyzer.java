/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Analyse;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author David
 */
public class Analyzer {
    //private List<Item> listOfItems;
    //private List<Item> resultList;
    private int number;
    List <AnalyzerStorage> keyData;
    //private Map<String, Map<String, List<String>>> structure;
    List <Item> finalItems;
    static int count=0;
    //Map<String, ArrayList<Item>> myKeyValues;
    // Used for constructing the path to the key in the json object
    //Stack<String> key_path = new Stack<String>();

    public Analyzer() {
        //this.myKeyValues = new HashMap<String, ArrayList<Item>>();
        this.keyData = new ArrayList<>();
        //this.structure = new LinkedHashMap<>();
    }
    
    private boolean newKey(String key){
        boolean missingKey = true;
        for (AnalyzerStorage as : this.keyData) {
            if (as.getKey().equals(key)) {
                missingKey = false;
            }
        }
        return missingKey;
    }
    
    public List<Item> Analyse(){
        Item finalItem;
        finalItems = new ArrayList<>();
        
        for (AnalyzerStorage item : this.keyData){
            //System.out.println("testit: " + item.getKey() + " : " + item.storageNotEmpty());
            if(item.storageNotEmpty()){
                item.checkTypes();
                finalItem = item.sumResults();
                finalItems.add(finalItem);
                //finalItem.provideResultField().forEach((type, value)->System.out.println("Type: " + type + " Value: " + value + " Item - " + item.getKey()));
                /*
                if((finalItem.getDateFormatCount() != null)){
                    for(DateFormatCount formatItem : finalItem.getDateFormatCount()){
                        //System.out.println("Format: " + formatItem.getDateFormat() + " Count: " + formatItem.getDateFormatCount() + " - Key: " + item.getKey() + " Locale: " + formatItem.getLocale());
                    }
                }*/
                
            }
                        
        }
        return finalItems;
    }
    
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
                    //this.structure.get(key).put(jsonArr.get(k).toString(), new ArrayList<>());
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
                //this.structure.put(obj.toString(), new LinkedHashMap<>());
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
                        //this.structure.put(obj.toString(), new LinkedHashMap<>());
                        keyData.get(count).AddToStorage(jsonObject.get(obj.toString()).toString());
                        //this.structure.get(obj.toString()).put(jsonObject.get(obj.toString()).toString(), new ArrayList<>());
                        //System.out.println(keyData.get(count).getKey()+" - "+obj.toString()+" : "+jsonObject.get(obj.toString()).toString()+"  "+ (count+1));
                        count++;
                        //??????missingKey = false;
                    }
                }
            }
        }
    }
    
    public void getArrayTest(Object object2, int limit, String key, String parent) throws JSONException  {

        JSONArray jsonArr = (JSONArray) object2;
        outerloop:
        for (int k = 0; k < jsonArr.length(); k++) {
            
            if (jsonArr.get(k) instanceof JSONObject) {
                //System.out.println("json array out: " + parent);
                parseJsonTest((JSONObject) jsonArr.get(k), limit, parent);
            } 

            else {
                boolean missingKey = false;
                for (int i=0;i<count;i++){

                    if (keyData.get(i).getKey() == key) {
                        missingKey = false;

                        if ((keyData.get(i).isLimitReached())) {
                            break outerloop;
                        }

                        else{
                            if (parent.isEmpty()){
                                keyData.get(i).AddToStorage(jsonArr.get(k).toString());
                                //System.out.println(keyData.get(i).getKey()+" - "+key+" : "+jsonArr.get(k)+"  "+ count + " parent: " + parent);
                                break;
                            }
                            else{
                                keyData.get(i).AddToStorage(parent, jsonArr.get(k).toString());
                                //System.out.println(keyData.get(i).getKey()+" - "+key+" : "+jsonArr.get(k)+"  "+ count + " parent: " + parent);
                                break;
                            }
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
                    //??????missingKey = false;
                }
            }
        }
    }
    
    //Map<String,Item> values = new HashMap<String,Item>();
    
    
    public void parseJsonTest(JSONObject jsonObject, int limit, String parent) throws JSONException  {
        Iterator<Object> iterator = jsonObject.keys();
        
        while (iterator.hasNext()) {
            Object obj = iterator.next();
            //System.out.println("key: " + obj.toString());
            //parent = obj.toString();
            
            if(this.keyData.isEmpty()){
                parent = obj.toString();
                //System.out.println("first: " + obj.toString() + " key: " + parent);
                this.keyData.add(new AnalyzerStorage(obj.toString(), limit));
                this.keyData.get(0).AddToStorage(parent,null);
                count++;
            }
            /*
            boolean missingFrontKey = false;
            for (int i=0; i<count;i++){
                if((keyData.get(i).getKey() != obj.toString())){
                    missingFrontKey = true;
                }
                else {
                    missingFrontKey = false;
                }
            }
            if (missingFrontKey){
                System.out.println("neq " + obj.toString());
                this.keyData.add(new AnalyzerStorage(obj.toString(), limit));
                count++;
                missingFrontKey = false;
            }
            */
            if (jsonObject.get(obj.toString()) instanceof JSONArray) {
                System.out.println(obj.toString());
                boolean tmp = false;
                for(AnalyzerStorage item : this.keyData){
                    tmp|= item.compareKey(obj.toString());
                }
                
                if (!tmp){
                    this.keyData.add(new AnalyzerStorage(obj.toString(), limit));
                    this.keyData.get(this.keyData.size()-1).AddToStorage(parent,null);
                    count++;
                }
                //System.out.println("array out: " + parent +" result " + tmp);
                parent= obj.toString();
                getArrayTest(jsonObject.get(obj.toString()), limit, obj.toString(), parent);
            } 
            
            else {
                
                if (jsonObject.get(obj.toString()) instanceof JSONObject) {
                    System.out.println("obj out");
                    parent = obj.toString();
                    parseJsonTest((JSONObject) jsonObject.get(obj.toString()), limit, parent);
                }
                else if(jsonObject.get(obj.toString()) instanceof LinkedHashMap){
                    System.out.println("hashmap" );
                }
                
                else {
                    boolean missingKey = false;
                    
                    for (int i=0;i<count;i++){
                        System.out.println("to add: " + obj.toString() + " has key: "+ keyData.get(i).getKey());
                        if (keyData.get(i).getKey().equals(obj.toString())) {
                            missingKey = false;
                            
                            if ((keyData.get(i).isLimitReached())) {
                                break;
                            }
                            
                            else{
                                if (parent == null){
                                    keyData.get(i).AddToStorage(jsonObject.get(obj.toString()).toString());
                                    //System.out.println(obj.toString() + "\t"+ jsonObject.get(obj.toString())+"  "+ count + parent);
                                    break;
                                }
                                else {
                                    keyData.get(i).AddToStorage(parent, jsonObject.get(obj.toString()).toString());
                                    //System.out.println(obj.toString() + "\t"+ jsonObject.get(obj.toString())+"  "+ count + parent);
                                    break;
                                }
                            }
                        }
                        
                        else {
                            missingKey = true;
                        }
                    }
                    if(missingKey){
                        if (parent == null){
                            this.keyData.add(new AnalyzerStorage(obj.toString(), limit));
                            keyData.get(count).AddToStorage(jsonObject.get(obj.toString()).toString());
                        }
                        else{
                            this.keyData.add(new AnalyzerStorage(obj.toString(), limit));
                            keyData.get(count).AddToStorage(parent, jsonObject.get(obj.toString()).toString());
                        }
                        
                        //System.out.println(keyData.get(count).getKey()+" - "+obj.toString()+" : "+jsonObject.get(obj.toString()).toString()+"  "+ (count+1));
                        count++;
                        //??????missingKey = false;
                    }
                }
            }
        }
    }
    
    
    public void PrintStorage(){
        for (int i=0;i<this.keyData.size();i++){
            System.out.println("\n keyData: " + keyData.get(i).getKey());
            for (int j=0;j<keyData.get(i).getCount();j++) {
                Item item = keyData.get(i).getItemInPosition(j);
                System.out.println("Key: " + item.getParent() + " Value: " + item.getValue());
            }
            System.out.println();
        }
        
        //this.structure.forEach((key, value)->{
        //    System.out.println("\n key: " + key + " value: " + value);
        //});
    }
    
    
    
    
    /*
    private ArrayList<String> GetKeys(JSONObject jObject, ArrayList<String> listOfKeys){
        Iterator<String> keys = jObject.keys();
        
        while (keys.hasNext()){
            String key = keys.next();
            listOfKeys.add(key);
            //System.out.println(key);
            try {
                
                JSONObject innerKey = jObject.getJSONObject(key);
                //System.out.println(innerKey);
                this.GetKeys(innerKey, listOfKeys);
            } catch (JSONException ex) {
                
            }
            
        }
        
        return listOfKeys;
    }
    
    
    public void GetValues(JSONObject toAnalyse, int analyseLimit){
        //Map<String,ArrayList<Item>> out = new HashMap<String, ArrayList<Item>>();
        
        ArrayList<String> keys; 
        //this.GetKeys(toAnalyse, keys);
        //System.out.println(keys.toString());
        
        //this.myKeyValues = getKeysValues(toAnalyse, analyseLimit);
        Map<String,ArrayList<Item>> mapOfValues = new HashMap<String,ArrayList<Item>>();
        this.GetValuesWithKey(toAnalyse, mapOfValues, analyseLimit, 0);
        keys = new ArrayList(mapOfValues.keySet());
        this.myKeyValues = mapOfValues;
        this.number = toAnalyse.length();
        System.out.println(mapOfValues.size());
        //System.out.println(mapOfValues.toString());
        
        //System.out.println(this.myKeyValues.toString());
        
        //System.out.println(this.number + "aaaaa");
        for (int i=0; i<mapOfValues.size();i++){
            //System.out.println(keys.get(i));
            if (this.myKeyValues.containsKey(keys.get(i))){
                for (Item item : this.myKeyValues.get(keys.get(i))) {
                    
                    System.out.println("Key: " + item.getParent() + "; value: " + item.getValue() );
                }
            }
        }
        
    }
    
    private Map<String,ArrayList<Item>> getKeysValues(JSONObject jsonObj, int analyseLimit){
        Iterator<String> keys = jsonObj.keys();
        Map<String,ArrayList<Item>> out = new HashMap<String, ArrayList<Item>>();
        
        //List <String> resultKeys = new ArrayList<String>();
        
        while(keys.hasNext()) {
            String key = keys.next();
            ArrayList<Item> items = new ArrayList<Item>();
            
            for(int i=0;i<analyseLimit;i++){
                
                try {
                    if (analyseLimit > jsonObj.getJSONArray(key).length() && i >= jsonObj.getJSONArray(key).length()){
                        //System.out.println(analyseLimit + " -limit-> " + jsonObj.getJSONArray(key).length() + " count-> " + i);
                        break;
                    }
                    // konstruktor Item(), vstup je hodnota, klic (rodic);
                    items.add(new Item(jsonObj.getJSONArray(key).getString(i), key));
                    System.out.println(key + " : " +jsonObj.getJSONArray(key).getString(i));
                
                } catch (JSONException ex) {
                    try {
                        //Logger.getLogger(Analyzer.class.getName()).log(Level.SEVERE, null, ex);
                        JSONObject innerKey = jsonObj.getJSONObject(key);
                        
                        //System.out.println(jsonObj.getJSONObject(key).toString());
                        break;

                    } catch (JSONException ex1) {
                        try {
                            //Logger.getLogger(Analyzer.class.getName()).log(Level.SEVERE, null, ex1);
                            System.out.println(jsonObj.get(key));
                            items.add(new Item((jsonObj.get(key)).toString(), key));
                        } catch (JSONException ex2) {
                            //Logger.getLogger(Analyzer.class.getName()).log(Level.SEVERE, null, ex2);
                        }
                        break;
                    }
                }
            }
            
            out.put(key, items);
        }
        
        return out;
    }
    
    
    ArrayList<Item> itemsOut = new ArrayList<Item>();
    
    private Map<String,ArrayList<Item>> GetValuesWithKey(JSONObject jObject, Map<String, ArrayList<Item>> mapOfItems, int analyseLimit, int index){
        Iterator<String> keys = jObject.keys();

        while(keys.hasNext()) {
            ArrayList<Item> items = new ArrayList<Item>();
            String key = keys.next();
            
            for(int i=0;i<analyseLimit;i++){
                
                try {
                    if (index >= jObject.getJSONArray(key).length() && analyseLimit > jObject.getJSONArray(key).length()){
                        System.out.println(analyseLimit + " -limit-> " + jObject.getJSONArray(key).length() + " count-> " + index);
                        break;
                    }
                    // konstruktor Item(), vstup je hodnota, klic (rodic);
                    //System.out.println(jObject.getJSONArray(key));
                    jObject.getJSONArray(key).getString(i);
                    //itemsOut.add(new Item(jObject.getJSONArray(key).getString(i), key));
                    //System.out.println(" array out " + jObject.getJSONArray(key).getString(i) + " key: " + key);
                    //System.out.println(key + " :: " +jObject.getJSONArray(key).getString(i));
                
                } catch (JSONException ex) {
                    //Logger.getLogger(Analyzer.class.getName()).log(Level.SEVERE, null, ex);
                    try {
                        //Logger.getLogger(Analyzer.class.getName()).log(Level.SEVERE, null, ex);
                        JSONObject innerKey = jObject.getJSONObject(key);
                        //System.out.println("key: " + key + " : " + innerKey.toString());
                        Iterator<String> innerKeys = innerKey.keys();
                        //System.out.println(" object out " + jObject.toString());
                        this.GetValuesWithKey(innerKey, mapOfItems, analyseLimit, index++);
                        
                        //System.out.println(jsonObj.getJSONObject(key).toString());
                        break;

                    } catch (JSONException ex1) {
   
                        try{
                            jObject.getJSONArray(key);
                            //System.out.println("index:  " + index);
                            this.GetValuesWithKey(jObject.getJSONArray(key).getJSONObject(index), mapOfItems, analyseLimit, index++);

                        }catch(JSONException ex2){
                            try {
                                //System.out.println("test: " + key + " : " + jObject.get(key).toString());
                                items.add(new Item((jObject.get(key)).toString(), key));
                            } catch (JSONException ex3) {
                                Logger.getLogger(Analyzer.class.getName()).log(Level.SEVERE, null, ex3);
                            }
                            break;
                        }
                    }
                
                }
            }

            if (!items.isEmpty()){
                itemsOut.add(items.get(0));
                System.out.println("kkkey: " + key + "items: " + items.get(0).getValue());
                //mapOfItems.put(key, items);
            }
        }
        mapOfItems.putIfAbsent("key", itemsOut);
        //mapOfItems.put("key", itemsOut);
        return mapOfItems;
    }
    */
}
