package com.napier.sem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Population Information System
 * Main application class for generating population reports
 */
public class Main
{
    /**
     * Connection to MySQL database.
     */
    private Connection con = null;

    /**
     * Connect to the MySQL database.
     */
    public void connect(String location, int delay)
    {
        try
        {
            // Load Database driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch (ClassNotFoundException e)
        {
            System.out.println("Could not load SQL driver");
            System.exit(-1);
        }

        int retries = 10;
        for (int i = 0; i < retries; ++i)
        {
            System.out.println("Connecting to database...");
            try
            {
                // Wait a bit for db to start
                Thread.sleep(delay);
                // Connect to database
                con = DriverManager.getConnection("jdbc:mysql://" + location
                        + "/world?allowPublicKeyRetrieval=true&useSSL=false",
                        "root", "example");
                System.out.println("Successfully connected");
                break;
            }
            catch (SQLException sqle)
            {
                System.out.println("Failed to connect to database attempt " + Integer.toString(i));
                System.out.println(sqle.getMessage());
            }
            catch (InterruptedException ie)
            {
                System.out.println("Thread interrupted? Should not happen.");
            }
        }
    }

    /**
     * Disconnect from the MySQL database.
     */
    public void disconnect()
    {
        if (con != null)
        {
            try
            {
                // Close connection
                con.close();
            }
            catch (Exception e)
            {
                System.out.println("Error closing connection to database");
            }
        }
    }

    /**
     * Gets  all countries in the world organised by largest population to smallest.
     * @return A list of all countries sorted by population, or null if there is an error.
     */
    public List<Country> getAllCountriesByPopulation()
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT country.Code, country.Name, country.Continent, country.Region, " +
                    "country.Population, city.Name as Capital " +
                    "FROM country " +
                    "LEFT JOIN city ON country.Capital = city.ID " +
                    "ORDER BY country.Population DESC";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract country information
            List<Country> countries = new ArrayList<>();
            while (rset.next())
            {
                Country country = new Country(
                    rset.getString("Code"),
                    rset.getString("Name"),
                    rset.getString("Continent"),
                    rset.getString("Region"),
                    rset.getLong("Population"),
                    rset.getString("Capital")
                );
                countries.add(country);
            }
            return countries;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details");
            return null;
        }
    }

    /**
     * Gets all countries in a continent organised by largest population to smallest.
     * @param continent The continent to filter by
     * @return A list of countries in the continent sorted by population, or null if there is an error.
     */
    public List<Country> getCountriesByPopulationInContinent(String continent)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT country.Code, country.Name, country.Continent, country.Region, " +
                    "country.Population, city.Name as Capital " +
                    "FROM country " +
                    "LEFT JOIN city ON country.Capital = city.ID " +
                    "WHERE country.Continent = '" + continent + "' " +
                    "ORDER BY country.Population DESC";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract country information
            List<Country> countries = new ArrayList<>();
            while (rset.next())
            {
                Country country = new Country(
                    rset.getString("Code"),
                    rset.getString("Name"),
                    rset.getString("Continent"),
                    rset.getString("Region"),
                    rset.getLong("Population"),
                    rset.getString("Capital")
                );
                countries.add(country);
            }
            return countries;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details for continent: " + continent);
            return null;
        }
    }

    /**
     * Prints a list of countries in a formatted table.
     * @param countries The list of countries to display
     * @param title The title for the report
     */
    public void displayCountries(List<Country> countries, String title)
    {
        // Check countries is not null
        if (countries == null)
        {
            System.out.println("No countries found");
            return;
        }

        // Print header
        System.out.println("\n=== " + title.toUpperCase() + " ===\n");
        
        // Print table header
        System.out.println("Code Name                                         Continent       Region                    Population Capital");
        System.out.println("==================================================================================================================");
        
        // Loop over all countries in the list
        for (Country country : countries)
        {
            if (country == null)
                continue;
            
            System.out.println(country.toString());
        }
        
        // Print summary
        System.out.println("\nTotal countries: " + countries.size());
    }

    /**
     * Main method - Entry point of the application
     */
    public static void main(String[] args)
    {
        // Create new Application
        Main app = new Main();

        // Check if running in Docker environment (database hostname)
        String dbHost = "localhost";
        if (args.length > 0) {
            dbHost = args[0];
        } else {
            // Try to detect if we're in Docker by checking environment
            String dockerEnv = System.getenv("DOCKER_ENV");
            if (dockerEnv != null || System.getenv("HOSTNAME") != null) {
                dbHost = "localhost"; // Will try localhost first, then docker network
            }
        }

        // Connect to database with retry mechanism
        System.out.println("Population Information System");
        System.out.println("============================");
        
        app.connect(dbHost + ":3306", 1000);

        // Check if connection was successful
        if (app.con == null) {
            System.out.println("Failed to connect to database. Exiting...");
            System.exit(-1);
        }

        try {
            // Generate reports
            System.out.println("Generating population reports...\n");

            // Report 1: All countries by population
            List<Country> allCountries = app.getAllCountriesByPopulation();
            app.displayCountries(allCountries, "All Countries by Population (Largest to Smallest)");

            // Report 2: Countries in Asia by population
            List<Country> asianCountries = app.getCountriesByPopulationInContinent("Asia");
            app.displayCountries(asianCountries, "Countries in Asia by Population (Largest to Smallest)");

            // Additional continental reports
            String[] continents = {"North America", "South America", "Europe", "Africa", "Oceania", "Antarctica"};
            for (String continent : continents) {
                List<Country> countries = app.getCountriesByPopulationInContinent(continent);
                if (countries != null && !countries.isEmpty()) {
                    app.displayCountries(countries, "Countries in " + continent + " by Population (Largest to Smallest)");
                }
            }

            System.out.println("\n=== REPORT GENERATION COMPLETED ===");
            System.out.println("Implemented: 2/32 reports (6.25% complete)");
            System.out.println("- Global country population ranking ✅");
            System.out.println("- Continental country population rankings ✅");

        } catch (Exception e) {
            System.out.println("Error generating reports: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Disconnect from database
            app.disconnect();
        }
    }
}