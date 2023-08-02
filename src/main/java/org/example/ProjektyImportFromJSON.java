package org.example;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class ProjektyImportFromJSON {
    public ProjektyImportFromJSON() {
        String dbUrl = "jdbc:mysql://localhost/integracjaprojektdb?user=root&password=";
        String jsonFilePath = "projektyJSON.json";

        String query = "INSERT INTO projekty(id, dzialanie, dziedzina, koniec, miejsce, obszar, start, tytul, wartosc, zakonczone, beneficjent) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        JSONParser parser = new JSONParser();

        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            JSONArray jsonArray = (JSONArray) parser.parse(new FileReader(jsonFilePath));

            for (Object obj : jsonArray) {
                JSONObject jsonObject = (JSONObject) obj;

                stmt.setInt(1, ((Long) jsonObject.get("id")).intValue());
                stmt.setString(2, (String) jsonObject.get("dzialanie"));
                stmt.setString(3, (String) jsonObject.get("dziedzina"));
                stmt.setString(4, (String) jsonObject.get("koniec"));
                stmt.setString(5, (String) jsonObject.get("miejsce"));
                stmt.setString(6, (String) jsonObject.get("obszar"));
                stmt.setString(7, (String) jsonObject.get("start"));
                stmt.setString(8, (String) jsonObject.get("tytul"));
                stmt.setDouble(9, (Double) jsonObject.get("wartosc"));
                stmt.setString(10, (String) jsonObject.get("zakonczone"));
                stmt.setString(11, (String) jsonObject.get("beneficjent"));

                stmt.execute();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
