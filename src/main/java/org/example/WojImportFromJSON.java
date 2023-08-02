package org.example;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class WojImportFromJSON {
    public WojImportFromJSON() {
        String dbUrl = "jdbc:mysql://localhost/integracjaprojektdb?user=root&password=";
        String jsonFilePath = "woj_wyniki_maturJSON.json";

        String query = "INSERT INTO woj_wyniki_matur(id, maxs, mediana, mins, odchylenie, odsetek, poziom, przedmiot, rok, srednia, wojewodztwo, zdajacych) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

        JSONParser parser = new JSONParser();

        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            JSONArray jsonArray = (JSONArray) parser.parse(new FileReader(jsonFilePath));

            for (Object obj : jsonArray) {
                JSONObject jsonObject = (JSONObject) obj;

                stmt.setInt(1, ((Long) jsonObject.get("id")).intValue());
                stmt.setString(2, (String) jsonObject.get("maxs"));
                stmt.setString(3, (String) jsonObject.get("mediana"));
                stmt.setString(4, (String) jsonObject.get("mins"));
                stmt.setString(5, (String) jsonObject.get("odchylenie"));
                stmt.setString(6, (String) jsonObject.get("odsetek"));
                stmt.setString(7, (String) jsonObject.get("poziom"));
                stmt.setString(8, (String) jsonObject.get("przedmiot"));
                stmt.setString(9, (String) jsonObject.get("rok"));
                stmt.setString(10, (String) jsonObject.get("srednia"));
                stmt.setString(11, (String) jsonObject.get("wojewodztwo"));
                stmt.setString(12, (String) jsonObject.get("zdajacych"));

                stmt.execute();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
