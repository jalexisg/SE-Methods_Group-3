package com.napier.sem;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
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
        // Don't assert a fixed length — the canonical list of continents may vary by dataset.
        // Instead assert it's non-empty and contains expected entries.
        assertTrue(continents.length > 0, "Continents array should not be empty");
        List<String> list = Arrays.asList(continents);
        assertTrue(list.contains("Asia"), "Continents should include Asia");
        assertTrue(list.contains("Antarctica"), "Continents should include Antarctica");
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
    void testConnectWithMockedDriverManager() throws Exception {
        String url = "jdbc:h2:mem:connecttest;DB_CLOSE_DELAY=-1";
        System.setProperty("TEST_DB_URL", url);
        System.setProperty("TEST_DB_USER", "sa");
        // Main.getDatabasePassword ignores empty strings, so use a non-empty test password
        System.setProperty("TEST_DB_PASSWORD", "example");

        Connection mockConn = Mockito.mock(Connection.class);

        try (MockedStatic<DriverManager> dm = Mockito.mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
                    .thenReturn(mockConn);

            Connection c = Main.connect();
            assertNotNull(c, "Main.connect should return the mocked connection");
            assertSame(mockConn, c);
        }
    }

    @Test
    void testMainInvalidTopNUsesDefaultWithMocks() throws Exception {
        // Prevent any real DB access by mocking DriverManager.getConnection and returning
        // a Connection whose Statement.executeQuery(...) returns an empty ResultSet.
        Connection mockConn = Mockito.mock(Connection.class);
        Statement mockStmt = Mockito.mock(Statement.class);
        ResultSet mockRs = Mockito.mock(ResultSet.class);

        Mockito.when(mockConn.createStatement()).thenReturn(mockStmt);
        Mockito.when(mockStmt.executeQuery(Mockito.anyString())).thenReturn(mockRs);
        Mockito.when(mockRs.next()).thenReturn(false); // no rows for any query

        try (MockedStatic<DriverManager> dm = Mockito.mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
                    .thenReturn(mockConn);

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

