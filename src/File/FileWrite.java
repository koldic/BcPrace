/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package File;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author David
 */
public class FileWrite {
    public FileWrite(){
    
    }
    
    public static void writeStringtoFile(String toWrite, String path, String fileName){
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter( new FileWriter( path+fileName));
            writer.write(toWrite);
        } catch (IOException ex) {
            Logger.getLogger(FileWrite.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(FileWrite.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
