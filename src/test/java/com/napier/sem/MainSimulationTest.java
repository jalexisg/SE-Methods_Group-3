package com.napier.sem;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.*;
import java.util.*;

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
        // deregister our test driver
        try {
            Enumeration<Driver> drivers = DriverManager.getDrivers();
            List<Driver> toRemove = new ArrayList<>();
            while (drivers.hasMoreElements()) {
                Driver d = drivers.nextElement();
                if (d instanceof TestDriver) {
                    toRemove.add(d);
                }
            }
            for (Driver d : toRemove) {
                try {
                    DriverManager.deregisterDriver(d);
                } catch (SQLException ignored) {
                }
            }
        } catch (Exception ignored) {}
    }

    @Test
    void testMainRunsWithFakeDriver() throws Exception {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));

    // Register the test driver that will provide an H2 in-memory DB
    DriverManager.registerDriver(new TestDriver());

        // Run main with a topN argument to exercise parsing
        Main.main(new String[]{"3"});

        String out = outContent.toString();
        String err = errContent.toString();

        // Ensure it printed global report header and didn't fail to connect
        assertTrue(out.contains("Global Country Population Report") || err.contains("Failed to connect to database") == false,
                "Main should produce a global report when fake driver is present");

        // Check that at least one formatted country line appears
        assertTrue(out.contains(" | "), "Output should contain formatted country lines");
    }

    // TestDriver creates an H2 in-memory database, populates a `country` table and returns the real H2 Connection.
    public static class TestDriver implements Driver {
        @Override
        public Connection connect(String url, Properties info) throws SQLException {
            // Create an H2 in-memory DB and populate it
            Connection c = DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
            try (Statement s = c.createStatement()) {
                s.execute("CREATE TABLE country (Code VARCHAR(3), Name VARCHAR(100), Continent VARCHAR(50), Region VARCHAR(50), Population BIGINT, Capital VARCHAR(100));");
                s.execute("INSERT INTO country VALUES ('AAA','CountryA','Europe','RegionA',1000,'CapA');");
                s.execute("INSERT INTO country VALUES ('BBB','CountryB','Europe','RegionB',500,'CapB');");
                s.execute("INSERT INTO country VALUES ('CCC','CountryC','Asia','RegionC',2000,'CapC');");
            }
            return c;
        }

        @Override public boolean acceptsURL(String url) { return url != null && url.startsWith("jdbc:mysql:"); }
        @Override public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) { return new DriverPropertyInfo[0]; }
        @Override public int getMajorVersion() { return 1; }
        @Override public int getMinorVersion() { return 0; }
        @Override public boolean jdbcCompliant() { return false; }
        @Override public java.util.logging.Logger getParentLogger() { return java.util.logging.Logger.getGlobal(); }
    }
}
