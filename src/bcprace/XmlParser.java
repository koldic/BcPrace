/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bcprace;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author David
 */
public class XmlParser {
    final private String fileName;
    
    public XmlParser(String fileName){
        this.fileName = fileName;
    }
    
    public void Parse(){
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            DefaultHandler handler = new DefaultHandler() {

            @Override
            public void startElement(String uri, String localName,String qName, Attributes attributes) throws SAXException {
                if (attributes.getLength() >0) {
                    System.out.print("<" + qName );
                    
                    for (int i=0; i < attributes.getLength(); i++){
                        System.out.print(" " + attributes.getLocalName(i) + "=\"");
                        System.out.print(attributes.getValue(i) + "\""); 
                    }
                }
                else {                   
                    System.out.println("<" + qName + ">");
                }
            }
            
            @Override
            public void characters(char[] ch, int start, int length) throws SAXException {
		for (int i=0; i< length;i++){
                    if(ch[start] != '\n') System.out.print(ch[start]);
                    start++;
                }
            }
            
            @Override
            public void endElement(String uri, String localName, String qName) throws SAXException {
		System.out.print("<\\" + qName + ">");
                System.out.println();
            }

        };
        saxParser.parse(fileName, handler);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }
    
    
}
