
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
     * Get database connection string
     * @return the database URL
     */
    public static String getDatabaseURL() {
        // Allow tests to override the database URL via system property TEST_DB_URL
        String testUrl = System.getProperty("TEST_DB_URL");
        if (testUrl != null && !testUrl.isEmpty()) return testUrl;
        String envUrl = System.getenv("TEST_DB_URL");
        if (envUrl != null && !envUrl.isEmpty()) return envUrl;
        return DB_URL;
    }

    

    private static String safe(String s) {
        return s == null ? "null" : s;
    }

    /**
     * Get a connection to the database
     * @return the connection object, or null if connection fails
     */
    public static Connection connect() {
        try {
            return DriverManager.getConnection(getDatabaseURL(), getDatabaseUser(), getDatabasePassword());
        } catch (SQLException e) {
            System.err.println("Error connecting to database: " + e.getMessage());
            return null;
        }
    }

    /**
     * Allow tests to override DB user via system property or env var
     */
    public static String getDatabaseUser() {
        String u = System.getProperty("TEST_DB_USER");
        if (u != null && !u.isEmpty()) return u;
        String eu = System.getenv("TEST_DB_USER");
        if (eu != null && !eu.isEmpty()) return eu;
        return DB_USER;
    }

    /**
     * Allow tests to override DB password via system property or env var
     */
    public static String getDatabasePassword() {
        String p = System.getProperty("TEST_DB_PASSWORD");
        if (p != null && !p.isEmpty()) return p;
        String ep = System.getenv("TEST_DB_PASSWORD");
        if (ep != null && !ep.isEmpty()) return ep;
        return DB_PASSWORD;
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

    /**
     * Get list of all continents
     * @return array of continent names
     */
    public static String[] getContinents() {
        return new String[]{"Asia", "Europe", "Africa", "North America", "South America", "Oceania", "Antarctica"};
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

            //us 3.10 as a local urban planner I want to see top N populated cities of a specific district

            String districtName = "California"; // Hardcoded again

            String sqlTopDistrict =
                    "SELECT Name AS CityName, CountryCode, District, Population " +
                            "FROM city " +
                            "WHERE District='" + districtName + "' " +
                            "ORDER BY Population DESC LIMIT " + topN;

            ResultSet rsTopDistrict = stmt.executeQuery(sqlTopDistrict);
            System.out.println("\nTop N Cities by Population by District\n");
            System.out.println("\nTop " + topN + " Cities by Population in " + districtName + "\n");
            System.out.println("Rank | City Name | Country Code | District | Population");

            int districtRank = 1;
            while (rsTopDistrict.next()) {
                System.out.println(
                        districtRank + " | " +
                                rsTopDistrict.getString("CityName") + " | " +
                                rsTopDistrict.getString("CountryCode") + " | " +
                                rsTopDistrict.getString("District") + " | " +
                                String.format("%,d", rsTopDistrict.getLong("Population"))
                );
                districtRank++;
            }
            rsTopDistrict.close();


            


            // Top N query (global)
            String sqlTop = "SELECT Code, Name, Continent, Region, Population, Capital FROM country ORDER BY Population DESC LIMIT " + topN;
            ResultSet rsTop = stmt.executeQuery(sqlTop);
            System.out.println("\nTop " + topN + " Countries by Population (Global)\n");
            System.out.println("Rank | Code | Name | Continent | Region | Population | Capital");
            int continentRank = 1;
            while (rsTop.next()) {
                System.out.println(
                    continentRank + " | " +
                    rsTop.getString("Code") + " | " +
                    rsTop.getString("Name") + " | " +
                    rsTop.getString("Continent") + " | " +
                    rsTop.getString("Region") + " | " +
                    String.format("%,d", rsTop.getLong("Population")) + " | " +
                    rsTop.getString("Capital")
                );
                continentRank++;
            }



            //3.2 cities in specific continents ranked by population

            String continentName2 = "Asia";  // Hardcoded for now
            System.out.println("\nCities in " + continentName + " ranked by Population\n");
            System.out.println("Rank | City | Country | Continent | Population");

            String sqlCitiesByContinent =
                    "SELECT city.Name AS City, country.Name AS Country, country.Continent, city.Population " +
                            "FROM city " +
                            "JOIN country ON city.CountryCode = country.Code " +
                            "WHERE country.Continent = '" + continentName2 + "' " +
                            "ORDER BY city.Population DESC " +
                            "LIMIT 10"; //change 10 to n to determine top n cities by pop in a continent (only put this here due to there being thousands of cities in asia, and it's a pain to scroll past!)


            ResultSet rsCities = stmt.executeQuery(sqlCitiesByContinent);
            int rank2 = 1;
            while (rsCities.next()) {
                System.out.println(
                        rank2 + " | " +
                                rsCities.getString("City") + " | " +
                                rsCities.getString("Country") + " | " +
                                rsCities.getString("Continent") + " | " +
                                String.format("%,d", rsCities.getLong("Population"))
                );
                rank2++;
































            }

            rsCities.close();
            rsTop.close();

                // --- Global City Population Report (User Story 3.1) ---
                // Print all cities ordered by population (descending). Inline implementation here.
                String sqlCities = "SELECT city.ID, city.Name, country.Name AS CountryName, city.District, city.Population "
                    + "FROM city JOIN country ON city.CountryCode = country.Code "
                    + "ORDER BY city.Population DESC";
                // no limit (0) means all
                System.out.println();
                System.out.println("Global City Population Report");
                System.out.println();
                System.out.println("ID | Name | Country | District | Population");
                ResultSet rsCitiesAll = stmt.executeQuery(sqlCities);
                while (rsCitiesAll.next()) {
                System.out.printf("%d | %s | %s | %s | %,d%n",
                    rsCitiesAll.getInt("ID"),
                    safe(rsCitiesAll.getString("Name")),
                    safe(rsCitiesAll.getString("CountryName")),
                    safe(rsCitiesAll.getString("District")),
                    rsCitiesAll.getLong("Population")
                );
                }
                rsCitiesAll.close();

//Top N cities by Region
        // The `city` table does not have a `Region` column. Join with `country` and filter by
        // `country.Region` instead.
        String sqlTopCity = "SELECT city.ID, city.Name, city.CountryCode, city.District, city.Population "
            + "FROM city JOIN country ON city.CountryCode = country.Code "
            + "WHERE country.Region='" + regionName.replace("'", "''") + "' "
            + "ORDER BY city.Population DESC LIMIT " + topN;
        ResultSet rsTopCity = stmt.executeQuery(sqlTopCity);
        System.out.println("\nTop N Cities by Population by Region\n");
        System.out.println("\nTop " + topN + " Cities by Population in " + regionName + "\n");
        System.out.println("Rank | ID | Name | CountryCode | District | Population");
        int cityRank = 1;
        while (rsTopCity.next()) {
        System.out.println( cityRank + " | " +
            rsTopCity.getString("ID") + " | " +
            rsTopCity.getString("Name") + " | " +
            rsTopCity.getString("CountryCode") + " | " +
            rsTopCity.getString("District") + " | " +
            String.format("%,d", rsTopCity.getLong("Population"))
        );
        cityRank++; }

        rsTopCity.close();


                // --- Top N Cities by Country (User Story 3.9) ---
                // Show the top N most populated cities for a given country name.
                String countryName = "Poland"; // Hardcoded for now; could be parameterized
                String sqlTopCityCountry = "SELECT city.ID, city.Name, country.Name AS CountryName, city.District, city.Population "
                    + "FROM city JOIN country ON city.CountryCode = country.Code "
                    + "WHERE country.Name='" + countryName.replace("'", "''") + "' "
                    + "ORDER BY city.Population DESC LIMIT " + topN;

                ResultSet rsTopCityCountry = stmt.executeQuery(sqlTopCityCountry);
                System.out.println("\nTop N Cities by Population by Country\n");
                System.out.println("\nTop " + topN + " Cities by Population in " + countryName + "\n");
                System.out.println("Rank | ID | Name | Country | District | Population");
                int cityRankC = 1;
                while (rsTopCityCountry.next()) {
                    System.out.println(
                        cityRankC + " | " +
                        rsTopCityCountry.getInt("ID") + " | " +
                        safe(rsTopCityCountry.getString("Name")) + " | " +
                        safe(rsTopCityCountry.getString("CountryName")) + " | " +
                        safe(rsTopCityCountry.getString("District")) + " | " +
                        String.format("%,d", rsTopCityCountry.getLong("Population"))
                    );
                    cityRankC++;
                }
                rsTopCityCountry.close();


            // Close resources after all reports are generated
            stmt.close();
            con.close();
        } catch (SQLException e) {
            // Handle and report any SQL/database errors
            System.out.println("Error connecting or querying the database: " + e.getMessage());
        }
    }

    
}