
package com.napier.sem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.net.URLEncoder;
import java.util.stream.Stream;

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
            List<List<String>> rowsGlobalCountries = new ArrayList<>();
            List<String> headersGlobalCountries = List.of("Code", "Name", "Continent", "Region", "Population", "Capital");
            while (rsGlobal.next()) {
                // Print each country's details in a formatted row
                String code = safe(rsGlobal.getString("Code"));
                String name = safe(rsGlobal.getString("Name"));
                String continent = safe(rsGlobal.getString("Continent"));
                String region = safe(rsGlobal.getString("Region"));
                String population = String.format("%,d", rsGlobal.getLong("Population"));
                String capital = safe(rsGlobal.getString("Capital"));

                System.out.println(code + " | " + name + " | " + continent + " | " + region + " | " + population + " | " + capital);
                rowsGlobalCountries.add(List.of(code, name, continent, region, population, capital));
            }
            rsGlobal.close();
            // Write global countries report
            outputTable(headersGlobalCountries, rowsGlobalCountries, "Countries by population (global) — User Story 2.1.md");

            // --- Continental Country Population Reports (User Story 2.2) ---
            // For each continent, print countries ordered by population
            String[] continents = {"Asia", "Europe", "Africa", "North America", "South America", "Oceania", "Antarctica"};
            // Aggregate all continent reports into a single file
            List<List<String>> rowsCountriesAllContinents = new ArrayList<>();
            List<String> headersContinent = List.of("Code", "Name", "Continent", "Region", "Population", "Capital");
            for (String continent : continents) {
                // Print header for the current continent
                System.out.println("\nCountries in " + continent + " by Population\n");
                System.out.println("Code | Name | Continent | Region | Population | Capital");

                // Query countries in the current continent, ordered by population
                String sqlContinent = "SELECT Code, Name, Continent, Region, Population, Capital FROM country WHERE Continent='" + continent + "' ORDER BY Population DESC";
                ResultSet rsContinent = stmt.executeQuery(sqlContinent);

                while (rsContinent.next()) {
                    String code = safe(rsContinent.getString("Code"));
                    String name = safe(rsContinent.getString("Name"));
                    String cont = safe(rsContinent.getString("Continent"));
                    String reg = safe(rsContinent.getString("Region"));
                    String pop = String.format("%,d", rsContinent.getLong("Population"));
                    String cap = safe(rsContinent.getString("Capital"));

                    // Print each country's details for the current continent
                    System.out.println(code + " | " + name + " | " + cont + " | " + reg + " | " + pop + " | " + cap);
                    rowsCountriesAllContinents.add(List.of(code, name, cont, reg, pop, cap));
                }
                rsContinent.close();
            }
            // Write a single consolidated continent report
            outputTable(headersContinent, rowsCountriesAllContinents, "Countries by population (by continent) — User Story 2.2.md");

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

            // Now, for each region, query and accumulate countries
            List<List<String>> rowsCountriesAllRegions = new ArrayList<>();
            List<String> headersRegion = List.of("Code", "Name", "Continent", "Region", "Population", "Capital");
            for (String region : regions) {
                System.out.println("\nCountries in region: " + region + " by Population\n");
                System.out.println("Code | Name | Continent | Region | Population | Capital");
                // Query for countries in the current region
                String sqlRegionReport = "SELECT Code, Name, Continent, Region, Population, Capital FROM country WHERE Region='" + region.replace("'", "''") + "' ORDER BY Population DESC";
                ResultSet rsRegion = stmt.executeQuery(sqlRegionReport);
                while (rsRegion.next()) {
                    String code = safe(rsRegion.getString("Code"));
                    String name = safe(rsRegion.getString("Name"));
                    String cont = safe(rsRegion.getString("Continent"));
                    String reg = safe(rsRegion.getString("Region"));
                    String pop = String.format("%,d", rsRegion.getLong("Population"));
                    String cap = safe(rsRegion.getString("Capital"));

                    // Print country data for the current region
                    System.out.println(code + " | " + name + " | " + cont + " | " + reg + " | " + pop + " | " + cap);
                    rowsCountriesAllRegions.add(List.of(code, name, cont, reg, pop, cap));
                }
                rsRegion.close();
            }
            // Write a single consolidated region report
            outputTable(headersRegion, rowsCountriesAllRegions, "Regional country population reports — User Story 2.3.md");

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
            List<List<String>> rowsTopContinent = new ArrayList<>();
            List<String> headersTopContinent = List.of("Rank", "Code", "Name", "Continent", "Region", "Population", "Capital");
            while (rsTopContinent.next()) {
                String code = safe(rsTopContinent.getString("Code"));
                String name = safe(rsTopContinent.getString("Name"));
                String cont = safe(rsTopContinent.getString("Continent"));
                String reg = safe(rsTopContinent.getString("Region"));
                String pop = String.format("%,d", rsTopContinent.getLong("Population"));
                String cap = safe(rsTopContinent.getString("Capital"));

                System.out.println(rank + " | " + code + " | " + name + " | " + cont + " | " + reg + " | " + pop + " | " + cap);
                rowsTopContinent.add(List.of(String.valueOf(rank), code, name, cont, reg, pop, cap));
                rank++;
            }
            rsTopContinent.close();
            outputTable(headersTopContinent, rowsTopContinent, "Top N countries by continent — User Story 2.5.md");

            // Top N Countries by Region (2.6)


            String regionName = "Western Europe"; //Hardcoded again

            String sqlTopRegion = "SELECT Code, Name, Continent, Region, Population, Capital FROM country WHERE Region='" + regionName + "' ORDER BY Population DESC LIMIT " + topN;
            ResultSet rsTopRegion = stmt.executeQuery(sqlTopRegion);
            System.out.println("\nTop N Countries by Population by Region\n");
            System.out.println("\nTop " + topN + " Countries by Population in " + regionName + "\n");
            System.out.println("Rank | Code | Name | Continent | Region | Population | Capital");
            int regionRank = 1;
            List<List<String>> rowsTopRegion = new ArrayList<>();
            List<String> headersTopRegion = List.of("Rank", "Code", "Name", "Continent", "Region", "Population", "Capital");
            while (rsTopRegion.next()) {
                String code = safe(rsTopRegion.getString("Code"));
                String name = safe(rsTopRegion.getString("Name"));
                String cont = safe(rsTopRegion.getString("Continent"));
                String reg = safe(rsTopRegion.getString("Region"));
                String pop = String.format("%,d", rsTopRegion.getLong("Population"));
                String cap = safe(rsTopRegion.getString("Capital"));

                System.out.println(regionRank + " | " + code + " | " + name + " | " + cont + " | " + reg + " | " + pop + " | " + cap);
                rowsTopRegion.add(List.of(String.valueOf(regionRank), code, name, cont, reg, pop, cap));
                regionRank++;
            }
            rsTopRegion.close();
            outputTable(headersTopRegion, rowsTopRegion, "Top N countries by region — User Story 2.6.md");

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
            List<List<String>> rowsTopDistrict = new ArrayList<>();
            List<String> headersTopDistrict = List.of("Rank", "City Name", "Country Code", "District", "Population");
            while (rsTopDistrict.next()) {
                String cityName = safe(rsTopDistrict.getString("CityName"));
                String countryCode = safe(rsTopDistrict.getString("CountryCode"));
                String district = safe(rsTopDistrict.getString("District"));
                String pop = String.format("%,d", rsTopDistrict.getLong("Population"));

                System.out.println(districtRank + " | " + cityName + " | " + countryCode + " | " + district + " | " + pop);
                rowsTopDistrict.add(List.of(String.valueOf(districtRank), cityName, countryCode, district, pop));
                districtRank++;
            }
            rsTopDistrict.close();
            outputTable(headersTopDistrict, rowsTopDistrict, "Top N cities by district — User Story 3.10.md");


            


            // Top N query (global)
            String sqlTop = "SELECT Code, Name, Continent, Region, Population, Capital FROM country ORDER BY Population DESC LIMIT " + topN;
            ResultSet rsTop = stmt.executeQuery(sqlTop);
            System.out.println("\nTop " + topN + " Countries by Population (Global)\n");
            System.out.println("Rank | Code | Name | Continent | Region | Population | Capital");
            int continentRank = 1;
            List<List<String>> rowsTopGlobal = new ArrayList<>();
            List<String> headersTopGlobal = List.of("Rank", "Code", "Name", "Continent", "Region", "Population", "Capital");
            while (rsTop.next()) {
                String code = safe(rsTop.getString("Code"));
                String name = safe(rsTop.getString("Name"));
                String cont = safe(rsTop.getString("Continent"));
                String reg = safe(rsTop.getString("Region"));
                String pop = String.format("%,d", rsTop.getLong("Population"));
                String cap = safe(rsTop.getString("Capital"));

                System.out.println(continentRank + " | " + code + " | " + name + " | " + cont + " | " + reg + " | " + pop + " | " + cap);
                rowsTopGlobal.add(List.of(String.valueOf(continentRank), code, name, cont, reg, pop, cap));
                continentRank++;
            }
            outputTable(headersTopGlobal, rowsTopGlobal, "Top N countries globally — User Story 2.4.md");



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
            List<List<String>> rowsCitiesByContinent = new ArrayList<>();
            List<String> headersCitiesByContinent = List.of("Rank", "City", "Country", "Continent", "Population");
            while (rsCities.next()) {
                String city = safe(rsCities.getString("City"));
                String country = safe(rsCities.getString("Country"));
                String cont = safe(rsCities.getString("Continent"));
                String pop = String.format("%,d", rsCities.getLong("Population"));

                System.out.println(rank2 + " | " + city + " | " + country + " | " + cont + " | " + pop);
                rowsCitiesByContinent.add(List.of(String.valueOf(rank2), city, country, cont, pop));
                rank2++;
            }































            rsCities.close();
            // Write continent city report
            outputTable(headersCitiesByContinent, rowsCitiesByContinent, "Cities in a continent (sample/top N) — User Story 3.2.md");
            rsTop.close();

            // --- District City Population Reports (User Story 3.5) ---
            // For each district, list cities ordered by population (descending)
            String sqlDistinctDistricts = "SELECT DISTINCT District FROM city ORDER BY District";
            ArrayList<String> districts = new ArrayList<>();
            ResultSet rsDistinctDistricts = stmt.executeQuery(sqlDistinctDistricts);
            while (rsDistinctDistricts.next()) {
                districts.add(rsDistinctDistricts.getString("District"));
            }
            rsDistinctDistricts.close();

            // Consolidate all district city reports into a single file
            List<List<String>> rowsCitiesAllDistricts = new ArrayList<>();
            List<String> headersCitiesInDistrictAll = List.of("District", "Name", "CountryCode", "Population");

            for (String district : districts) {
                System.out.println("\nCities in district: " + district + " by Population\n");
                System.out.println("Name | CountryCode | Population");
                String sqlCitiesInDistrict = "SELECT Name, CountryCode, Population FROM city WHERE District='" + district.replace("'", "''") + "' ORDER BY Population DESC";
                ResultSet rsCitiesInDistrict = stmt.executeQuery(sqlCitiesInDistrict);
                while (rsCitiesInDistrict.next()) {
                    String name = safe(rsCitiesInDistrict.getString("Name"));
                    String cc = safe(rsCitiesInDistrict.getString("CountryCode"));
                    String pop = String.format("%,d", rsCitiesInDistrict.getLong("Population"));

                    System.out.println(name + " | " + cc + " | " + pop);
                    rowsCitiesAllDistricts.add(List.of(district, name, cc, pop));
                }
                rsCitiesInDistrict.close();
            }
            // Write a single consolidated district report
            outputTable(headersCitiesInDistrictAll, rowsCitiesAllDistricts, "District city population reports (all districts) — User Story 3.5.md");

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
                List<List<String>> rowsGlobalCities = new ArrayList<>();
                List<String> headersCities = List.of("ID", "Name", "Country", "District", "Population");
                while (rsCitiesAll.next()) {
                int id = rsCitiesAll.getInt("ID");
                String name = safe(rsCitiesAll.getString("Name"));
                String countryNameAll = safe(rsCitiesAll.getString("CountryName"));
                String district = safe(rsCitiesAll.getString("District"));
                String pop = String.format("%,d", rsCitiesAll.getLong("Population"));

                System.out.printf("%d | %s | %s | %s | %,d%n", id, name, countryNameAll, district, rsCitiesAll.getLong("Population"));
                rowsGlobalCities.add(List.of(String.valueOf(id), name, countryNameAll, district, pop));
                }
                rsCitiesAll.close();
                // Write global cities report (may be large)
                outputTable(headersCities, rowsGlobalCities, "Global city population report (all cities) — User Story 3.1.md");

                // --- Top N Cities Globally (User Story 3.6) ---
                // Show the top N most populated cities in the world.
                String sqlTopCitiesGlobal = "SELECT city.ID, city.Name, country.Name AS CountryName, city.District, city.Population "
                    + "FROM city JOIN country ON city.CountryCode = country.Code "
                    + "ORDER BY city.Population DESC LIMIT " + topN;
                ResultSet rsTopCitiesGlobal = stmt.executeQuery(sqlTopCitiesGlobal);
                System.out.println("\nTop " + topN + " Cities by Population (Global)\n");
                System.out.println("Rank | ID | Name | Country | District | Population");
                int topCityRank = 1;
                List<List<String>> rowsTopCities = new ArrayList<>();
                List<String> headersTopCities = List.of("Rank", "ID", "Name", "Country", "District", "Population");
                while (rsTopCitiesGlobal.next()) {
                    int id = rsTopCitiesGlobal.getInt("ID");
                    String name = safe(rsTopCitiesGlobal.getString("Name"));
                    String countryNameTop = safe(rsTopCitiesGlobal.getString("CountryName"));
                    String districtTop = safe(rsTopCitiesGlobal.getString("District"));
                    String pop = String.format("%,d", rsTopCitiesGlobal.getLong("Population"));

                    System.out.println(topCityRank + " | " + id + " | " + name + " | " + countryNameTop + " | " + districtTop + " | " + pop);
                    rowsTopCities.add(List.of(String.valueOf(topCityRank), String.valueOf(id), name, countryNameTop, districtTop, pop));
                    topCityRank++;
                }
                rsTopCitiesGlobal.close();
                outputTable(headersTopCities, rowsTopCities, "Top N cities globally — User Story 3.6.md");

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
        List<List<String>> rowsTopCitiesRegion = new ArrayList<>();
        List<String> headersTopCitiesRegion = List.of("Rank", "ID", "Name", "CountryCode", "District", "Population");
        while (rsTopCity.next()) {
            String id = safe(rsTopCity.getString("ID"));
            String name = safe(rsTopCity.getString("Name"));
            String cc = safe(rsTopCity.getString("CountryCode"));
            String dist = safe(rsTopCity.getString("District"));
            String pop = String.format("%,d", rsTopCity.getLong("Population"));

            System.out.println(cityRank + " | " + id + " | " + name + " | " + cc + " | " + dist + " | " + pop);
            rowsTopCitiesRegion.add(List.of(String.valueOf(cityRank), id, name, cc, dist, pop));
            cityRank++;
        }

        rsTopCity.close();
        outputTable(headersTopCitiesRegion, rowsTopCitiesRegion, "TopCities_Region_" + regionName.replaceAll("\\s+", "_") + ".md");


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
                List<List<String>> rowsTopCitiesCountry = new ArrayList<>();
                List<String> headersTopCitiesCountry = List.of("Rank", "ID", "Name", "Country", "District", "Population");
                while (rsTopCityCountry.next()) {
                    String id = String.valueOf(rsTopCityCountry.getInt("ID"));
                    String name = safe(rsTopCityCountry.getString("Name"));
                    String cname = safe(rsTopCityCountry.getString("CountryName"));
                    String dist = safe(rsTopCityCountry.getString("District"));
                    String pop = String.format("%,d", rsTopCityCountry.getLong("Population"));

                    System.out.println(cityRankC + " | " + id + " | " + name + " | " + cname + " | " + dist + " | " + pop);
                    rowsTopCitiesCountry.add(List.of(String.valueOf(cityRankC), id, name, cname, dist, pop));
                    cityRankC++;
                }
                rsTopCityCountry.close();
                outputTable(headersTopCitiesCountry, rowsTopCitiesCountry, "Top N cities by country — User Story 3.9.md");


            // Regenerate the human-friendly index in `./reports` so the web UI shows current files
            try {
                regenerateReportsIndex();
            } catch (IOException e) {
                System.err.println("Failed to regenerate reports index: " + e.getMessage());
            }

            // Close resources after all reports are generated
            stmt.close();
            con.close();
        } catch (SQLException e) {
            // Handle and report any SQL/database errors
            System.out.println("Error connecting or querying the database: " + e.getMessage());
        }
    }

    /**
     * Write raw Markdown content to files under `./tmp/reports/` and `./reports/`.
     * Both directories will be created if they do not exist. Writing to both
     * locations keeps compatibility with the previous container-based flow
     * (which used `/tmp/reports`) while ensuring the repository `./reports/`
     * directory contains the generated reports for CI and manual inspection.
     * @param content markdown content
     * @param filename file name (e.g. "GlobalCities.md")
     */
    public static void outputMarkdown(String content, String filename) {
        if (content == null || filename == null || filename.isEmpty()) return;

        Path dirTmp = Paths.get("./tmp/reports");
        Path dirReports = Paths.get("./reports");

        try {
            Files.createDirectories(dirTmp);
        } catch (IOException e) {
            System.err.println("Failed to create tmp reports dir: " + e.getMessage());
        }

        try {
            Files.createDirectories(dirReports);
        } catch (IOException e) {
            System.err.println("Failed to create reports dir: " + e.getMessage());
        }

        // Sanitize filename to avoid path separators or illegal filename characters
        String safeFilename = filename.replaceAll("[\\\\/:*?\"<>|]", "_");
        if (!safeFilename.equals(filename)) {
            System.out.println("Sanitized filename: '" + filename + "' -> '" + safeFilename + "'");
        }

        Path fileTmp = dirTmp.resolve(safeFilename);
        Path fileReports = dirReports.resolve(safeFilename);

        try {
            Files.createDirectories(fileTmp.getParent());
            Files.write(fileTmp, content.getBytes(StandardCharsets.UTF_8));
            System.out.println("Wrote report: " + fileTmp.toString());
        } catch (IOException e) {
            System.err.println("Failed to write report to " + fileTmp + ": " + e.getMessage());
        }

        try {
            Files.createDirectories(fileReports.getParent());
            Files.write(fileReports, content.getBytes(StandardCharsets.UTF_8));
            System.out.println("Wrote report: " + fileReports.toString());
        } catch (IOException e) {
            System.err.println("Failed to write report to " + fileReports + ": " + e.getMessage());
        }
    }

    /**
     * Helper to output a simple Markdown table from headers and rows.
     * This writes the resulting `.md` into `./reports/` and `./tmp/reports/`.
     * @param headers column headers
     * @param rows list of rows, each row is a list of cell strings
     * @param filename output filename (e.g. "TopCities.md")
     */
    public static void outputTable(List<String> headers, List<List<String>> rows, String filename) {
        if (headers == null || filename == null) return;
        StringBuilder sb = new StringBuilder();
        // header
        sb.append("| ");
        sb.append(String.join(" | ", headers));
        sb.append(" |\n");
        // separator
        sb.append("| ");
        sb.append(headers.stream().map(h -> "---").collect(Collectors.joining(" | ")));
        sb.append(" |\n");
        if (rows != null) {
            for (List<String> row : rows) {
                if (row == null) continue;
                sb.append("| ");
                sb.append(row.stream().map(s -> s == null ? "" : s).collect(Collectors.joining(" | ")));
                sb.append(" |\n");
            }
        }
        outputMarkdown(sb.toString(), filename);
    }

    /**
     * Regenerate `reports/index.html` dynamically from the current .md files
     * in the `./reports` directory. The index links each file into `viewer.html`.
     */
    public static void regenerateReportsIndex() throws IOException {
        Path dirReports = Paths.get("./reports");
        Files.createDirectories(dirReports);

        List<Path> mdFiles;
        try (Stream<Path> s = Files.walk(dirReports)) {
            mdFiles = s.filter(p -> Files.isRegularFile(p) && p.getFileName().toString().toLowerCase().endsWith(".md"))
                .sorted()
                .collect(Collectors.toList());
        }

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm");
        String generated = LocalDateTime.now().format(fmt);

        StringBuilder html = new StringBuilder();
        html.append("<!doctype html>\n<html lang=\"en\">\n<head>\n  <meta charset=\"utf-8\">\n  <meta name=\"viewport\" content=\"width=device-width,initial-scale=1\">\n  <title>Population Information System — Reports</title>\n  <style>body { font-family: system-ui, -apple-system, 'Segoe UI', Roboto, 'Helvetica Neue', Arial; margin: 2rem; } h1 { margin-bottom: .5rem } .list { margin-top: 1rem; } a { color: #0366d6; text-decoration: none; } a:hover { text-decoration: underline; } .meta { color: #666; font-size: .9rem }</style>\n</head>\n<body>\n  <h1>Population Information System — Reports</h1>\n  <p class=\"meta\">Last generated: ");
        html.append(generated).append(".</p>\n  <div class=\"list\">\n    <ul>\n");

        for (Path p : mdFiles) {
            Path rel = dirReports.relativize(p);
            // Build display name from relative path and remove .md for display
            String display = rel.toString().replaceAll("\\.md$", "").replace(File.separatorChar, '/');

            // Encode each path segment separately so '/' remains a separator in the URL
            StringBuilder encoded = new StringBuilder();
            for (int i = 0; i < rel.getNameCount(); i++) {
                if (i > 0) encoded.append('/');
                String seg = rel.getName(i).toString();
                String encSeg = URLEncoder.encode(seg, StandardCharsets.UTF_8.toString()).replace("+", "%20");
                encoded.append(encSeg);
            }

            html.append("      <li><a href=\"viewer.html?file=").append(encoded.toString()).append("\">")
                    .append(escapeHtml(display)).append("</a></li>\n");
        }

        html.append("    </ul>\n  </div>\n</body>\n</html>\n");

        Path indexFile = dirReports.resolve("index.html");
        Files.write(indexFile, html.toString().getBytes(StandardCharsets.UTF_8));
        System.out.println("Regenerated reports index: " + indexFile.toString());
    }

    private static String escapeHtml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }

}