
package com.napier.sem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Main {
    // Database connection parameters
    private static final String DB_URL = "jdbc:mysql://localhost:3306/world?allowPublicKeyRetrieval=true&useSSL=false";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "example";

    /**
     * Get a connection to the database
     * @return the connection object, or null if connection fails
     */
    public static Connection connect() {
        try {
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            System.err.println("Error connecting to database: " + e.getMessage());
            return null;
        }
    }

    /**
     * Format a line of country data for display
     * @param code country code
     * @param name country name
     * @param continent continent name
     * @param region region name
     * @param population population count
     * @param capital capital city name
     * @return formatted string
     */
    public static String formatCountryLine(String code, String name, String continent, String region, long population, String capital) {
        return String.format("%s | %s | %s | %s | %,d | %s",
            code, name, continent, region, population, capital);
    }

    public static void main(String[] args) {
        Connection con = connect();
        if (con == null) {
            System.err.println("Failed to connect to database");
            return;
        }
        
        try {
            Statement stmt = con.createStatement();

            // --- Global Country Population Report (User Story 2.1) ---
            // Prints all countries ordered by population (descending)
            System.out.println("\nGlobal Country Population Report\n");
            System.out.println("Code | Name | Continent | Region | Population | Capital");
            String sqlGlobal = "SELECT Code, Name, Continent, Region, Population, Capital FROM country ORDER BY Population DESC";
            ResultSet rsGlobal = stmt.executeQuery(sqlGlobal);
            while (rsGlobal.next()) {
                // Print each country's details in a formatted row
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

            // --- Continental Country Population Reports (User Story 2.2) ---
            // For each continent, print countries ordered by population
            String[] continents = {"Asia", "Europe", "Africa", "North America", "South America", "Oceania", "Antarctica"};
            for (String continent : continents) {
                // Print header for the current continent
                System.out.println("\nCountries in " + continent + " by Population\n");
                System.out.println("Code | Name | Continent | Region | Population | Capital");

                // Query countries in the current continent, ordered by population
                String sqlContinent = "SELECT Code, Name, Continent, Region, Population, Capital FROM country WHERE Continent='" + continent + "' ORDER BY Population DESC";
                ResultSet rsContinent = stmt.executeQuery(sqlContinent);

                while (rsContinent.next()) {
                    // Print each country's details for the current continent
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

            // --- Regional Country Population Reports (User Story 2.3) ---
            // For each region, print countries ordered by population (descending)
            System.out.println("\nRegional Country Population Reports\n");
            // Get all distinct regions from the country table and store in a list
            String sqlRegions = "SELECT DISTINCT Region FROM country ORDER BY Region";
            ArrayList<String> regions = new ArrayList<>();
            ResultSet rsRegions = stmt.executeQuery(sqlRegions);
            while (rsRegions.next()) {
                regions.add(rsRegions.getString("Region"));
            }
            rsRegions.close();

            // Now, for each region, query and print the countries
            for (String region : regions) {
                System.out.println("\nCountries in region: " + region + " by Population\n");
                System.out.println("Code | Name | Continent | Region | Population | Capital");
                // Query for countries in the current region
                String sqlRegionReport = "SELECT Code, Name, Continent, Region, Population, Capital FROM country WHERE Region='" + region.replace("'", "''") + "' ORDER BY Population DESC";
                ResultSet rsRegion = stmt.executeQuery(sqlRegionReport);
                while (rsRegion.next()) {
                    // Print country data for the current region
                    System.out.println(
                        rsRegion.getString("Code") + " | " +
                        rsRegion.getString("Name") + " | " +
                        rsRegion.getString("Continent") + " | " +
                        rsRegion.getString("Region") + " | " +
                        String.format("%,d", rsRegion.getLong("Population")) + " | " +
                        rsRegion.getString("Capital")
                    );
                }
                rsRegion.close();
            }

            // --- Top N Countries Globally (User Story 2.4) ---
            // Use first command-line argument as N if provided, otherwise default to 10
            int topN = 10;
            if (args.length > 0) {
                try {
                    topN = Integer.parseInt(args[0]);
                    if (topN <= 0) {
                        System.out.println("Invalid N provided, using default 10");
                        topN = 10;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number format for top N, using default 10");
                    topN = 10;
                }
            }

            // Top N Countries by Continent (2.5) (modified Top N global)


            String continentName = "Asia"; //Hard coded, unsure how to accept user input for this...

            String sqlTopContinent = "SELECT Code, Name, Continent, Region, Population, Capital FROM country WHERE Continent='" + continentName + "' ORDER BY Population DESC LIMIT " + topN;
            ResultSet rsTopContinent = stmt.executeQuery(sqlTopContinent);
            System.out.println("\nTop N Countries by Population by Continent\n");
            System.out.println("\nTop " + topN + " Countries by Population in " + continentName + "\n");
            System.out.println("Rank | Code | Name | Continent | Region | Population | Capital");
            int rank = 1;
            while (rsTopContinent.next()) {
                System.out.println(
                        rank + " | " +
                                rsTopContinent.getString("Code") + " | " +
                                rsTopContinent.getString("Name") + " | " +
                                rsTopContinent.getString("Continent") + " | " +
                                rsTopContinent.getString("Region") + " | " +
                                String.format("%,d", rsTopContinent.getLong("Population")) + " | " +
                                rsTopContinent.getString("Capital")
                );
                rank++;
            }
            rsTopContinent.close();

            // Top N Countries by Region (2.6)


            String regionName = "Western Europe"; //Hardcoded again

            String sqlTopRegion = "SELECT Code, Name, Continent, Region, Population, Capital FROM country WHERE Region='" + regionName + "' ORDER BY Population DESC LIMIT " + topN;
            ResultSet rsTopRegion = stmt.executeQuery(sqlTopRegion);
            System.out.println("\nTop N Countries by Population by Region\n");
            System.out.println("\nTop " + topN + " Countries by Population in " + regionName + "\n");
            System.out.println("Rank | Code | Name | Continent | Region | Population | Capital");
            int regionRank = 1;
            while (rsTopRegion.next()) {
                System.out.println(
                        regionRank + " | " +
                                rsTopRegion.getString("Code") + " | " +
                                rsTopRegion.getString("Name") + " | " +
                                rsTopRegion.getString("Continent") + " | " +
                                rsTopRegion.getString("Region") + " | " +
                                String.format("%,d", rsTopRegion.getLong("Population")) + " | " +
                                rsTopRegion.getString("Capital")
                );
                regionRank++;
            }
            rsTopRegion.close();



            // Top N query (global)
            String sqlTop = "SELECT Code, Name, Continent, Region, Population, Capital FROM country ORDER BY Population DESC LIMIT " + topN;
            ResultSet rsTop = stmt.executeQuery(sqlTop);
            System.out.println("\nTop " + topN + " Countries by Population (Global)\n");
            System.out.println("Rank | Code | Name | Continent | Region | Population | Capital");
            int continentRank = 1;
            while (rsTop.next()) {
                System.out.println(
                    rank + " | " +
                    rsTop.getString("Code") + " | " +
                    rsTop.getString("Name") + " | " +
                    rsTop.getString("Continent") + " | " +
                    rsTop.getString("Region") + " | " +
                    String.format("%,d", rsTop.getLong("Population")) + " | " +
                    rsTop.getString("Capital")
                );
                continentRank++;
            }
            rsTop.close();

            // Close resources after all reports are generated
            stmt.close();
            con.close();
        } catch (SQLException e) {
            // Handle and report any SQL/database errors
            System.out.println("Error connecting or querying the database: " + e.getMessage());
        }
    }

    
}