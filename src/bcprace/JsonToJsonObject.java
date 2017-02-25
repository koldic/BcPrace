/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bcprace;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;



/**
 *
 * @author David
 */
public class JsonToJsonObject {
    private final String fileName;
    private JSONObject jsonObject = null;

    public JsonToJsonObject(String fileName) {
        this.fileName = fileName;
    }
    
    public JSONObject ConvertFile(){
        //JSONParser parser = new JSONParser();
        
        File f = new File(this.fileName);
        try {
            if (f.exists()){
                InputStream is = new FileInputStream(this.fileName);
                StringBuilder jsonString = new StringBuilder();
                jsonString.append(IOUtils.toString(is));
                //JSONObject test = (JSONObject) inputValues;
                //System.out.println(inputValues + "aa");
                
                try {
                    jsonObject = new JSONObject(jsonString.toString());
                    System.out.println(jsonString);
                } catch (JSONException ex) {
                    
                    jsonString.insert(0, "{\""+f.getName()+"\": ");
                    jsonString.append("}");
                    
                    try {
                        jsonObject = new JSONObject(jsonString.toString());
                    } catch (JSONException ex1) {
                        Logger.getLogger(JsonToJsonObject.class.getName()).log(Level.SEVERE, null, ex1);
                    }
                    //Logger.getLogger(JsonToJsonObject.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
            else{
                try {
                    jsonObject = new JSONObject("File Not Found!");
                } catch (JSONException ex) {
                    Logger.getLogger(JsonToJsonObject.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(JsonToJsonObject.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            System.out.println("eror");
            Logger.getLogger(JsonToJsonObject.class.getName()).log(Level.SEVERE, null, ex);
        }   
        //System.out.println(jsonObject);
        return jsonObject;
    }
    
}
