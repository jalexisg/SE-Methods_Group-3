package com.napier.sem;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.Connection;

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
    @Disabled("Disabled until database is properly configured in CI environment")
    void testDatabaseConnection()
    {
        // Test that we can connect to the database - disabled in CI environment
        Connection conn = Main.connect();
        assertNotNull(conn, "Database connection should be established");
    }


}

