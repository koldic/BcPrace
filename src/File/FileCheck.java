/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package File;

import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author David
 */
public class FileCheck {
    
    private final String fileName;
    private FileTypes fileType;
    private File file;
    
    public FileCheck (String fileName) {
        this.fileName = fileName;
        this.file = new File(this.fileName);
    }
    
    public FileTypes checkFile(){
        
        if(this.checkName()){
            //System.out.println("empty filename!");
            return FileTypes.isNull;
        }

        if (this.file.isFile() && this.file.length() >  0){
            String[] split = this.fileName.split("\\.");
            String type = split[split.length - 1];
            //System.out.println(type);
            switch(type){
                case "xlsx":
                    this.fileType = FileTypes.xlsx;
                    break;
                case "xls":
                    this.fileType = FileTypes.xls;
                    break;
                case "xml":
                    this.fileType = FileTypes.xml;
                    break;
                case "json":
                    this.fileType = FileTypes.json;
                    break;
                case "csv":
                    this.fileType = FileTypes.csv;
                    break;
                case "sql":
                    this.fileType = FileTypes.sql;
                    break;
                default:
                    this.fileType = FileTypes.notSupported;
            }
        }
        else{
            this.fileType = FileTypes.isNull;
        }
        
        return this.fileType;
    }
    
    private boolean checkName (){
        return this.fileName.isEmpty();
    }
    
    public String checkCoding(){
        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(this.file));
            
            CharsetDetector cDetector = new CharsetDetector();
            cDetector.setText(bis);
            
            CharsetMatch cMatch = cDetector.detect();
            String charSet = null;
            
            if (cMatch != null) {
                charSet = cMatch.getName();
            }
            
            bis.close();
            
            return charSet;
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileCheck.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FileCheck.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
}
