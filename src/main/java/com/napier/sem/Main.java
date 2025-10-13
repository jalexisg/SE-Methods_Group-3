
package com.napier.sem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/world?allowPublicKeyRetrieval=true&useSSL=false";
        String user = "root";
        String password = "example";
        try {
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stmt = con.createStatement();

            // Global Country Population Report
            System.out.println("\nGlobal Country Population Report\n");
            System.out.println("Code | Name | Continent | Region | Population | Capital");
            String sqlGlobal = "SELECT Code, Name, Continent, Region, Population, Capital FROM country ORDER BY Population DESC";
            ResultSet rsGlobal = stmt.executeQuery(sqlGlobal);
            while (rsGlobal.next()) {
                System.out.println(
                    rsGlobal.getString("Code") + " | " +
                    rsGlobal.getString("Name") + " | " +
                    rsGlobal.getString("Continent") + " | " +
                    rsGlobal.getString("Region") + " | " +
                    String.format("%,d", rsGlobal.getLong("Population")) + " | " +
                    rsGlobal.getString("Capital")
                );
            }
            rsGlobal.close();

            // Continental Country Population Reports
            String[] continents = {"Asia", "Europe", "Africa", "North America", "South America", "Oceania", "Antarctica"};
            for (String continent : continents) {
                System.out.println("\nCountries in " + continent + " by Population\n");
                System.out.println("Code | Name | Continent | Region | Population | Capital");
                String sqlContinent = "SELECT Code, Name, Continent, Region, Population, Capital FROM country WHERE Continent='" + continent + "' ORDER BY Population DESC";
                ResultSet rsContinent = stmt.executeQuery(sqlContinent);
                while (rsContinent.next()) {
                    System.out.println(
                        rsContinent.getString("Code") + " | " +
                        rsContinent.getString("Name") + " | " +
                        rsContinent.getString("Continent") + " | " +
                        rsContinent.getString("Region") + " | " +
                        String.format("%,d", rsContinent.getLong("Population")) + " | " +
                        rsContinent.getString("Capital")
                    );
                }
                rsContinent.close();
            }

            stmt.close();
            con.close();
        } catch (SQLException e) {
            System.out.println("Error connecting or querying the database: " + e.getMessage());
        }
    }
}