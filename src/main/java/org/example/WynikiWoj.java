package org.example;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.sql.*;
import java.util.Arrays;
import java.util.List;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.FileWriter;

public class WynikiWoj {








    static private String getTextContent(Node parentNode, String childName) {
        NodeList nlist = parentNode.getChildNodes();
        for (int i = 0 ; i < nlist.getLength() ; i++) {
            Node n = nlist.item(i);
            String name = n.getNodeName();
            if ( name != null && name.equals(childName) )
                return n.getTextContent();
        }
        return "";
    }



    static private String getAttrValue(Node node,String attrName) {
        if ( ! node.hasAttributes() ) return "";
        NamedNodeMap nmap = node.getAttributes();
        if ( nmap == null ) return "";
        Node n = nmap.getNamedItem(attrName);
        if ( n == null ) return "";
        return n.getNodeValue();
    }



    public WynikiWoj() {

        String dbUrl="jdbc:mysql://localhost/integracjaprojektdb?user=root&password=";
        try{


            Connection conn = DriverManager.getConnection(dbUrl);

            File file = new File("maturyWoj.xml");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document xmlDoc = (Document) builder.parse(file);






            XPath xpath = XPathFactory.newInstance().newXPath();
            NodeList nlist = (NodeList) xpath.evaluate("/root/row", xmlDoc, XPathConstants.NODESET);




            PreparedStatement stmt = conn
                    .prepareStatement("INSERT INTO woj_wyniki_matur(id, maxs, mediana, mins, odchylenie, odsetek, poziom, przedmiot, rok, srednia, wojewodztwo, zdajacych) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)");



            for (int i = 0 ; i < nlist.getLength() ; i++) {
                Node node = nlist.item(i);
                List<String> columns = Arrays
                        .asList(Integer.toString(i+1),
                                getTextContent(node, "Maksimum"),
                                getTextContent(node, "Mediana"),
                                getTextContent(node, "Minimum"),
                                getTextContent(node, "Odchylenie"),
                                getTextContent(node, "Odsetek"),
                                getTextContent(node, "Poziom"),
                                getTextContent(node, "Przedmiot"),
                                getTextContent(node, "Rok"),
                                getTextContent(node, "Srednia"),
                                getTextContent(node, "Wojewodztwo"),
                                getTextContent(node, "Zdajacych").replace(",",""));
                for (int n = 0 ; n < columns.size() ; n++) {
                    stmt.setString(n+1, columns.get(n));
                }
                stmt.execute();
            }

        } catch (Exception e){
            System.out.println(e);
        }


    }




}
