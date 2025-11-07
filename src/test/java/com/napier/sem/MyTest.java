package com.napier.sem;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

class MyTest
{
    @Test
    void testMainDatabaseURL()
    {
        // Test the database URL is correctly formed
        String dbUrl = Main.getDatabaseURL();
        assertTrue(dbUrl.startsWith("jdbc:mysql://"), "Database URL should start with jdbc:mysql://");
        assertTrue(dbUrl.contains("world"), "Database URL should contain the database name 'world'");
        assertTrue(dbUrl.contains("allowPublicKeyRetrieval=true"), "Database URL should include public key retrieval setting");
        assertTrue(dbUrl.contains("useSSL=false"), "Database URL should include SSL setting");
    }

    @Test
    void testGetContinents()
    {
        //Test that the getContinents method array returns the expected 7 continents
        String[] expected = {"Asia","Europe","Africa","North America","South America","Oceania","Antarctica"};
        assertArrayEquals(expected, Main.getContinents(),"Continents should be Asia, Europe, Africa, North America, South America, Oceania & Antarctica ");
    }

    @Test
    public void testFormatCountryLine()
    {
        //Tests that the formatCountryLine method formats the line properly
        String result = Main.formatCountryLine(
                "USA", "United States", "North America", "Northern America", 331002651L, "Washington D.C.");
        String expected = "USA | United States | North America | Northern America | 331,002,651 | Washington D.C.";
        assertEquals(expected, result);
    }




    @Test
    void testCountryWithZeroPopulation()
    {
        // Test a country with zero population (like some territories might have)
        Country country = new Country("ATA", "Antarctica", "Antarctica", "Antarctic", 0, "None");
        assertEquals(0, country.population, "Population should be zero");
    }

    @Test
    void testCountryWithNullValues()
    {
        // Test that we can create a country with null values
        Country country = new Country(null, null, null, null, 0, null);
        assertNull(country.code, "Code can be null");
        assertNull(country.name, "Name can be null");
        assertNull(country.continent, "Continent can be null");
        assertNull(country.region, "Region can be null");
        assertNull(country.capital, "Capital can be null");
    }

    @Test
    void testDatabaseConnection()
    {
        // Create an H2 in-memory DB and configure Main to use it so this test can run in CI
        System.setProperty("TEST_DB_URL", "jdbc:h2:mem:mytestdb;DB_CLOSE_DELAY=-1");
        System.setProperty("TEST_DB_USER", "test");
        System.setProperty("TEST_DB_PASSWORD", "test");
        try (Connection c = DriverManager.getConnection(System.getProperty("TEST_DB_URL"), System.getProperty("TEST_DB_USER"), System.getProperty("TEST_DB_PASSWORD"))) {
            // DB started
        } catch (SQLException e) {
            fail("Failed to set up H2 in-memory DB: " + e.getMessage());
        }

        Connection conn = Main.connect();
        assertNotNull(conn, "Database connection should be established");
        try { conn.close(); } catch (SQLException ignored) {}

        System.clearProperty("TEST_DB_URL");
        System.clearProperty("TEST_DB_USER");
        System.clearProperty("TEST_DB_PASSWORD");
    }


}

