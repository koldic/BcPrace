/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Converters.Csv;

import au.com.bytecode.opencsv.CSVWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author David
 */
public class JsonToCsv {
    private final Map<String, Map<String, List<String>>> values;
    private final Boolean oneFile;
    private final String fileName;
    private final String outputPath;
    private StringBuilder csvString;

    public JsonToCsv(String outputPath, String fileName, Map<String, Map<String, List<String>>> valuesMap, Boolean intoOneFile) {
        this.outputPath = outputPath;
        this.fileName = fileName;
        this.values = valuesMap;
        this.oneFile = intoOneFile;
        this.csvString = new StringBuilder();
    }
        
    public void convertToCsv(){
       
        if (this.oneFile){
            CSVWriter csvWriter = null;
            List<String> turnedValues = new ArrayList<>();

            try {
                csvWriter = new CSVWriter(new FileWriter(this.outputPath+this.fileName));
            } catch (IOException ex) {
                Logger.getLogger(JsonToCsv.class.getName()).log(Level.SEVERE, null, ex);
            }
//            for (String outKey : this.values.keySet()){
//                //csvWriter.writeNext();
//                for (String inKey : this.values.get(outKey).keySet()){
//                    System.out.println("adding csv: " + this.values.get(outKey).get(inKey).toArray(new String[this.values.get(outKey).get(inKey).size()]));
//                    csvWriter.writeNext(this.values.get(outKey).get(inKey).toArray(new String[this.values.get(outKey).get(inKey).size()]));
//                }
//            }
            for(String outKey : this.values.keySet()){
                Boolean firstLine = true;
                
                for (String arKey : this.values.get(outKey).keySet()){
                        for (int j=0;j<this.values.get(outKey).get(arKey).size();j++){
                            if(firstLine){
                                csvWriter.writeNext(this.values.get(outKey).keySet().toArray(new String[this.values.keySet().size()]));
                                firstLine = false;
                            }
                            turnedValues.clear();
                            for(String keys : this.values.get(outKey).keySet()){
                                if (j<this.values.get(outKey).get(keys).size()){
                                    turnedValues.add(this.values.get(outKey).get(keys).get(j));
                                    //System.out.println("key: " + keys + " value: " + this.values.get(outKey).get(keys).get(j));
                                    //this.addValue(value.get(keys).get(j), this.firstColumnValue);
                                }
                            }
                            //System.out.println(" values: " + turnedValues);
                            csvWriter.writeNext(turnedValues.toArray(new String[turnedValues.size()]));
                        }
                        break;
                }
            }
            
            try {
                csvWriter.flush();
                csvWriter.close();
            } catch (IOException ex) {
                Logger.getLogger(JsonToCsv.class.getName()).log(Level.SEVERE, null, ex);
            }
            turnedValues = null;
        }
        else{
            CSVWriter csvWriter = null;
            int writerIndex = 0;
            List<String> turnedValues = new ArrayList<>();

            
            for(String outKey : this.values.keySet()){
                Boolean firstLine = true;
                
                try {
                    if (!this.values.get(outKey).isEmpty()){
                        csvWriter = new CSVWriter(new FileWriter(this.outputPath+writerIndex+this.fileName));
                    }
                } catch (IOException ex) {
                    Logger.getLogger(JsonToCsv.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                for (String arKey : this.values.get(outKey).keySet()){
                        for (int j=0;j<this.values.get(outKey).get(arKey).size();j++){
                            if(firstLine){
                                csvWriter.writeNext(this.values.get(outKey).keySet().toArray(new String[this.values.keySet().size()]));
                                firstLine = false;
                            }
                            turnedValues.clear();
                            for(String keys : this.values.get(outKey).keySet()){
                                if (j<this.values.get(outKey).get(keys).size()){
                                    turnedValues.add(this.values.get(outKey).get(keys).get(j));
                                    //System.out.println("key: " + keys + " value: " + this.values.get(outKey).get(keys).get(j));
                                    //this.addValue(value.get(keys).get(j), this.firstColumnValue);
                                }
                            }
                            //System.out.println(" values: " + turnedValues);
                            csvWriter.writeNext(turnedValues.toArray(new String[turnedValues.size()]));
                        }
                        break;
                }
                writerIndex++;
                try {
                    csvWriter.flush();
                    csvWriter.close();
                } catch (IOException ex) {
                    Logger.getLogger(JsonToCsv.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            turnedValues = null;
        }
    }
}
