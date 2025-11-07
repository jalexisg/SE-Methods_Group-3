package com.napier.sem;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

public class MainSimulationTest {

    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
        // cleanup test DB system properties
        System.clearProperty("TEST_DB_URL");
        System.clearProperty("TEST_DB_USER");
        System.clearProperty("TEST_DB_PASSWORD");
    }

    @Test
    void testMainRunsWithFakeDriver() throws Exception {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));

        // Prepare an H2 in-memory database and tell Main to use it via system property
        System.setProperty("TEST_DB_URL", "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        // Use an explicit test user/password to avoid H2 authentication quirks
        System.setProperty("TEST_DB_USER", "test");
        System.setProperty("TEST_DB_PASSWORD", "test");
        try (Connection c = DriverManager.getConnection(System.getProperty("TEST_DB_URL"), System.getProperty("TEST_DB_USER"), System.getProperty("TEST_DB_PASSWORD"))) {
            try (Statement s = c.createStatement()) {
                s.execute("CREATE TABLE country (Code VARCHAR(3), Name VARCHAR(100), Continent VARCHAR(50), Region VARCHAR(50), Population BIGINT, Capital VARCHAR(100));");
                s.execute("INSERT INTO country VALUES ('AAA','CountryA','Europe','RegionA',1000,'CapA');");
                s.execute("INSERT INTO country VALUES ('BBB','CountryB','Europe','RegionB',500,'CapB');");
                s.execute("INSERT INTO country VALUES ('CCC','CountryC','Asia','RegionC',2000,'CapC');");
            }
        }

        // Ensure H2 driver is loaded and verify that Main.connect() can connect using the same properties
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException ignored) {}

        // Verify that Main.connect() can connect using the same properties
        Connection c2 = Main.connect();
        assertNotNull(c2, () -> "Main.connect() failed - getDatabaseURL=" + Main.getDatabaseURL() + ", user=" + Main.getDatabaseUser() + ", stderr=" + errContent.toString());
    try { c2.close(); } catch (SQLException ignored) {}

    // Run main with a topN argument to exercise parsing
    Main.main(new String[]{"3"});

    String out = outContent.toString();
    String err = errContent.toString();

    // Ensure it printed global report header
    assertTrue(out.contains("Global Country Population Report"), "Main should produce a global report when test DB is present; stderr=" + err);

        // Check that at least one formatted country line appears
        assertTrue(out.contains(" | "), "Output should contain formatted country lines");
    }

    // No custom driver; tests use an H2 in-memory DB and set TEST_DB_URL system property
}
