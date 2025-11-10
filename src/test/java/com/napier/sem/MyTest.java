package com.napier.sem;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MyTest {

    @Test
    void testFormatCountryLine() {
        String formatted = Main.formatCountryLine("ESP", "Spain", "Europe", "Southern Europe", 47615034, "Madrid");
        assertEquals("ESP | Spain | Europe | Southern Europe | 47,615,034 | Madrid", formatted);

        String formattedNull = Main.formatCountryLine(null, null, null, null, 0, null);
        assertEquals("null | null | null | null | 0 | null", formattedNull);
    }

    @Test
    void testGetContinents() {
        String[] continents = Main.getContinents();
        assertNotNull(continents);
        assertEquals(7, continents.length);
        assertEquals("Asia", continents[0]);
        assertEquals("Antarctica", continents[6]);
    }

    @Test
    void testGetDatabaseURLDefault() {
        String orig = System.getProperty("TEST_DB_URL");
        try {
            System.clearProperty("TEST_DB_URL");
            String url = Main.getDatabaseURL();
            assertNotNull(url);
            assertTrue(url.startsWith("jdbc:mysql://"));
            assertTrue(url.contains("world"));
        } finally {
            if (orig != null) System.setProperty("TEST_DB_URL", orig);
        }
    }

    @Test
    void testCountryWithZeroPopulation() {
        Country country = new Country("ATA", "Antarctica", "Antarctica", "Antarctic", 0, "None");
        assertEquals(0, country.population, "Population should be zero");
    }

    @Test
    void testCountryWithNullValues() {
        Country country = new Country(null, null, null, null, 0, null);
        assertNull(country.code, "Code can be null");
        assertNull(country.name, "Name can be null");
        assertNull(country.continent, "Continent can be null");
        assertNull(country.region, "Region can be null");
        assertNull(country.capital, "Capital can be null");
    }

}

