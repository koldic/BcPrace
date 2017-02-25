/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bcprace;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

/**
 *
 * @author David
 */
public class XmlToJson {
    
    private final String fileName;
    private JSONObject jsonObject = null;
    
    public XmlToJson(String fileName){
        this.fileName = fileName;
    }
    
    public JSONObject convertFile(){
    InputStream inputStream;
       
       BufferedReader bufferedReader;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(this.fileName), "UTF-8"));
            StringBuilder responseStrBuilder = new StringBuilder();

            String inputStr;
            while ((inputStr = bufferedReader.readLine()) != null) {
                responseStrBuilder.append(inputStr);
            }

            jsonObject = XML.toJSONObject(responseStrBuilder.toString());
            
        }catch(IOException | JSONException Ex){
       
       }
        return this.jsonObject;
    }
}
