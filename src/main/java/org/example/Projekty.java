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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.List;

public class Projekty {








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








    public Projekty() {

        String dbUrl="jdbc:mysql://localhost/integracjaprojektdb?user=root&password=";






        try{


            Connection conn = DriverManager.getConnection(dbUrl);

            File file = new File("projektyU.xml");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document xmlDoc = (Document) builder.parse(file);






            XPath xpath = XPathFactory.newInstance().newXPath();
            NodeList nlist = (NodeList) xpath.evaluate("/root/row", xmlDoc, XPathConstants.NODESET);




            PreparedStatement stmt = conn
                    .prepareStatement("INSERT INTO projekty(id, dzialanie, dziedzina, koniec, miejsce, obszar, start, tytul, wartosc, zakonczone, beneficjent) VALUES (?,?,?,?,?,?,?,?,?,?,?)");



            for (int i = 0 ; i < nlist.getLength() ; i++) {
                Node node = nlist.item(i);
                List<String> columns = Arrays
                        .asList(Integer.toString(i+1),
                                getTextContent(node, "Dzialanie"),
                                getTextContent(node, "Dziedzina"),
                                getTextContent(node, "Koniec"),
                                getTextContent(node, "Miejsce"),
                                getTextContent(node, "Obszar"),
                                getTextContent(node, "Start"),
                                getTextContent(node, "Tytul"),
                                getTextContent(node, "Wartosc").replace("\u00a0","").replace(",", ".").replaceAll("\\s+",""),
                                getTextContent(node, "Zakonczone"),
                                getTextContent(node, "beneficjent"));
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
