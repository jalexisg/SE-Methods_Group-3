package com.napier.sem;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.Connection;

class MyTest
{

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
        // Test that we can connect to the database
        Connection conn = Main.connect();
        assertNotNull(conn, "Database connection should be established");
    }


}

