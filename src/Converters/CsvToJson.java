/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Converters;

import bcprace.BcPrace;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author David
 */
public class CsvToJson {
    private final String fileName;
    private JSONArray array = null;
    

    public CsvToJson(String fileName){
        this.fileName = fileName;
    }
    
    public JSONArray ConvertFile(){
    try {   
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
            StringBuilder responseStrBuilder = new StringBuilder();
            
            JSONObject obj = null;
            String inputStr;
            while ((inputStr = bufferedReader.readLine()) != null) {
                responseStrBuilder.append(inputStr);
                responseStrBuilder.append('\n');
                //System.out.println(inputStr);
            }
            array = null;
            array = CDL.toJSONArray(responseStrBuilder.toString());
            
            return array;
        } catch (JSONException ex) {
            Logger.getLogger(BcPrace.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(CsvToJson.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CsvToJson.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CsvToJson.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    
}
