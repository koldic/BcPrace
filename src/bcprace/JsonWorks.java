/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bcprace;

import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 *
 * @author David
 */
public class JsonWorks {
            
    public static void saveJsonFile(JSONObject jsonObject, String path, String fileName){
        String fileNamePath = path+fileName;
        try {
            try (FileWriter file = new FileWriter(fileNamePath)) {
                jsonObject.write(file);
                file.flush();
            }
        } catch (IOException | JSONException ex) {
            Logger.getLogger(BcPrace.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    
    public static void saveJsonFileArray(String fileName, JSONArray jsonArray){
        try {
            try (FileWriter file = new FileWriter(fileName)) {
                JSONObject jsonObject = null;
                System.out.println(jsonArray);
                for(int i=0; i < jsonArray.length();i++){
                    file.write((jsonArray.getJSONObject(i).toString()));
                    file.write('\n');
                    jsonObject = new JSONObject((jsonArray.getJSONObject(i)));
                }
                file.flush();
            }
       } catch (IOException | JSONException ex) {
           Logger.getLogger(BcPrace.class.getName()).log(Level.SEVERE, null, ex);
       }
    }
    
    public static JSONObject objectToJSONObject(Object object){
        Object json = null;
        JSONObject jsonObject = null;
        
        try {
            json = new JSONTokener(object.toString()).nextValue();
        } catch (JSONException e) {
            
        }
        
        if (json instanceof JSONObject) {
            jsonObject = (JSONObject) json;
        }
        else {
            jsonObject = new JSONObject(json);
        }
        System.out.println(json);
        return jsonObject;
    }

    public static JSONArray objectToJSONArray(Object object){
        Object json = null;
        JSONArray jsonArray = null;
        try {
            json = new JSONTokener(object.toString()).nextValue();
        } catch (JSONException e) {
        }
        if (json instanceof JSONArray) {
            jsonArray = (JSONArray) json;
        }
        return jsonArray;
    }
    
}
