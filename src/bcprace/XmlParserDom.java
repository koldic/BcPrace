/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bcprace;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.NamedNodeMap;
import org.xml.sax.SAXException;

/**
 *
 * @author David
 */
public class XmlParserDom {
    private Document doc;
    public XmlParserDom(String fileName){
        try {
            File fXmlFile = new File(fileName);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            this.doc = dBuilder.parse(fXmlFile);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(XmlParserDom.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(XmlParserDom.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XmlParserDom.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void xmlParse(){
        //System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

	if (doc.hasChildNodes()) {
            printNote(doc.getChildNodes());
            //Node root = doc.getFirstChild();
            //printNodeRows(root.getFirstChild());
	}
    }
    
    public static String getText(Node node) {
    if (! node.hasChildNodes()) return "";
    StringBuffer result = new StringBuffer();
    
    NodeList list = node.getChildNodes();
    for (int i=0; i < list.getLength(); i++) {
        Node subnode = list.item(i);
        if (subnode.getNodeType() == Node.TEXT_NODE) {
            result.append(subnode.getNodeValue());
        }
        else if (subnode.getNodeType() == Node.CDATA_SECTION_NODE) {
            result.append(subnode.getNodeValue());
        }
        else if (subnode.getNodeType() == Node.ENTITY_REFERENCE_NODE) {
            // Recurse into the subtree for text
            // (and ignore comments)
           result.append(getText(subnode));
        }
        else return "";
    }

    return result.toString();
}
    
    private static void printNote(NodeList nodeList) {

    for (int count = 0; count < nodeList.getLength(); count++) {

	Node tempNode = nodeList.item(count);

	// make sure it's element node.
	if (tempNode.getNodeType() == Node.ELEMENT_NODE) {

		// get node name and value
		
                System.out.println("\nNode Name = " + tempNode.getNodeName() + " :");
		if(getText(tempNode) !="" && getText(tempNode) !="\n") System.out.print(getText(tempNode));
		if (tempNode.hasAttributes()) {
			// get attributes names and values
			NamedNodeMap nodeMap = tempNode.getAttributes();

			for (int i = 0; i < nodeMap.getLength(); i++) {
				Node node = nodeMap.item(i);
				//System.out.println();
                                System.out.print(" " + node.getNodeName());
				System.out.print(" = " + node.getNodeValue());
                                
			}

		}

		if (tempNode.hasChildNodes()) {

			// loop again if has child nodes
			printNote(tempNode.getChildNodes());

		}

		//System.out.println("\nNode Name =" + tempNode.getNodeName() + " [CLOSE]");

	}

    }

  }
private static void printNodeRows(Node node) {
        Node tempNode = node;

	// make sure it's element node.
	if (tempNode.getNodeType() == Node.ELEMENT_NODE) {

            // get node name and value

            System.out.print("Node Name = " + tempNode.getNodeName() + "      ");
            //if(getText(tempNode) !="" && getText(tempNode) !="\n") System.out.print(getText(tempNode));
		//System.out.println("\nNode Name =" + tempNode.getNodeName() + " [CLOSE]");
	}
        if (tempNode.getNextSibling() != null) {
                    // loop again if has siling nodes
                    printNodeRows(tempNode.getNextSibling());
            }
        else System.out.println();
        
        tempNode = node;
        
        if (tempNode.hasChildNodes())
            
            printNodeRows(tempNode.getFirstChild());
        //else System.out.println(" false Element ");

    }

  }

    
    
