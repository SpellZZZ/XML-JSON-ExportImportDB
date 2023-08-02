package org.example;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
public class OgolneImportFromJSON {

    public OgolneImportFromJSON(){
        String dbUrl="jdbc:mysql://localhost/integracjaprojektdb?user=root&password=";
       // String username = "root";
        //String password = "";
        String jsonFilePath = "ogolne_wyniki_maturJSON.json";

        String query = "INSERT INTO ogolne_wyniki_matur(id, flaga, nazwa_zmiennej, plec, poziom_egzaminu, przedmiot, rodzaj_egzaminu, rok, typ_informacji_z_jednostka_miary, wartosc) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        JSONParser parser = new JSONParser();

        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            JSONArray jsonArray = (JSONArray) parser.parse(new FileReader(jsonFilePath));

            for (Object obj : jsonArray) {
                JSONObject jsonObject = (JSONObject) obj;

                stmt.setInt(1, ((Long) jsonObject.get("id")).intValue());
                stmt.setString(2, (String) jsonObject.get("flaga"));
                stmt.setString(3, (String) jsonObject.get("nazwa_zmiennej"));
                stmt.setString(4, (String) jsonObject.get("plec"));
                stmt.setString(5, (String) jsonObject.get("poziom_egzaminu"));
                stmt.setString(6, (String) jsonObject.get("przedmiot"));
                stmt.setString(7, (String) jsonObject.get("rodzaj_egzaminu"));
                stmt.setString(8, (String) jsonObject.get("rok"));
                stmt.setString(9, (String) jsonObject.get("typ_informacji_z_jednostka_miary"));
                stmt.setDouble(10, (Double) jsonObject.get("wartosc"));

                stmt.execute();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    }

