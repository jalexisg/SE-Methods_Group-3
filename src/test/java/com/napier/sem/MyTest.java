package com.napier.sem;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

public class MyTest {

    @AfterEach
    void clearTestProperties() {
        System.clearProperty("TEST_DB_URL");
        System.clearProperty("TEST_DB_USER");
        System.clearProperty("TEST_DB_PASSWORD");
    }

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
    void testGetDatabaseURLSystemOverride() {
        System.setProperty("TEST_DB_URL", "jdbc:h2:mem:override;DB_CLOSE_DELAY=-1");
        assertEquals("jdbc:h2:mem:override;DB_CLOSE_DELAY=-1", Main.getDatabaseURL());
    }

    @Test
    void testGetDatabaseUserAndPasswordOverride() {
        String origU = System.getProperty("TEST_DB_USER");
        String origP = System.getProperty("TEST_DB_PASSWORD");
        try {
            System.setProperty("TEST_DB_USER", "u1");
            System.setProperty("TEST_DB_PASSWORD", "p1");
            assertEquals("u1", Main.getDatabaseUser());
            assertEquals("p1", Main.getDatabasePassword());
        } finally {
            if (origU != null) System.setProperty("TEST_DB_USER", origU);
            if (origP != null) System.setProperty("TEST_DB_PASSWORD", origP);
        }
    }

    @Test
    void testConnectWithH2InMemory() throws Exception {
        String url = "jdbc:h2:mem:connecttest;DB_CLOSE_DELAY=-1";
        System.setProperty("TEST_DB_URL", url);
        System.setProperty("TEST_DB_USER", "sa");
        // Main.getDatabasePassword ignores empty strings, so use a non-empty test password
        System.setProperty("TEST_DB_PASSWORD", "example");

        // H2 creates the DB on connect
        Connection c = Main.connect();
        assertNotNull(c, "Should be able to connect to H2 in-memory DB");
        c.close();
    }

    @Test
    void testMainInvalidTopNUsesDefault() throws Exception {
        // Prepare H2 DB with a single country so Main can print something
        String url = "jdbc:h2:mem:maindeftest;DB_CLOSE_DELAY=-1";
        System.setProperty("TEST_DB_URL", url);
        System.setProperty("TEST_DB_USER", "sa");
        // match Main's default-password handling by using a non-empty password
        System.setProperty("TEST_DB_PASSWORD", "example");

        try (Connection c = DriverManager.getConnection(url, "sa", "example")) {
            try (Statement s = c.createStatement()) {
                s.execute("CREATE TABLE country (Code VARCHAR(3), Name VARCHAR(100), Continent VARCHAR(50), Region VARCHAR(50), Population BIGINT, Capital VARCHAR(100));");
                s.execute("INSERT INTO country VALUES ('TST','Testland','Europe','TestRegion',12345,'Tcap');");
            }
        }

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));
        try {
            Main.main(new String[]{"not-a-number"});
            String out = outContent.toString();
            assertTrue(out.contains("Invalid number format for top N, using default 10") || out.contains("Global Country Population Report"));
        } finally {
            System.setOut(originalOut);
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

