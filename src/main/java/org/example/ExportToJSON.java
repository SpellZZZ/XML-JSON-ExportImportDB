package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExportToJSON {
    ExportToJSON(String entity){
        String dbUrl="jdbc:mysql://localhost/integracjaprojektdb?user=root&password=";

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try {

            Connection conn = DriverManager.getConnection(dbUrl);

            String sql = "SELECT * FROM " + entity;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            List<Map<String, Object>> list = new ArrayList<>();

            ResultSetMetaData rsmd = rs.getMetaData();

            while(rs.next()) {

                Map<String, Object> record = new HashMap<>();
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    String columnName = rsmd.getColumnName(i);
                    Object columnValue = rs.getObject(i);

                    record.put(columnName, columnValue);
                }

                list.add(record);
            }

            String json = gson.toJson(list);

            try (FileWriter writer = new FileWriter(entity + "JSON.json")) {
                writer.write(json);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
