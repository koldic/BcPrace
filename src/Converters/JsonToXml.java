/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Converters;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

/**
 *
 * @author David
 */
public class JsonToXml {
    private String xml;
    private StringBuilder strBuilder;

    public JsonToXml() {
        this.xml = null;
        this.strBuilder = new StringBuilder();
    }
    
    public String convertJson (JSONObject jObject){
        this.strBuilder.append("<xmlDocument>");
        try {
            this.strBuilder.append(XML.toString(jObject));
            System.out.println(xml);
        } catch (JSONException ex) {
            Logger.getLogger(JsonToXml.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.strBuilder.append("</xmlDocument>");
        return this.strBuilder.toString();
    }
    
}
