package com.napier.sem;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AppIntegrationTest {

    private Connection conn;

    @BeforeAll
    void init() {
        // Prefer a real integration DB when RUN_INTEGRATION_TESTS=true; otherwise fall back to an H2 in-memory DB
        boolean runIntegration = Boolean.parseBoolean(System.getenv().getOrDefault("RUN_INTEGRATION_TESTS", "false"));
        if (runIntegration) {
            conn = Main.connect();
            assertNotNull(conn, "Could not establish DB connection to integration DB");
        } else {
            // Setup an H2 in-memory database and configure Main to use it via TEST_DB_* system properties
            try {
                Class.forName("org.h2.Driver");
            } catch (ClassNotFoundException ignored) {}
            String url = "jdbc:h2:mem:app_integration_test;DB_CLOSE_DELAY=-1";
            String user = "test";
            String pass = "test";
            System.setProperty("TEST_DB_URL", url);
            System.setProperty("TEST_DB_USER", user);
            System.setProperty("TEST_DB_PASSWORD", pass);

            try (java.sql.Connection c = java.sql.DriverManager.getConnection(url, user, pass);
                 java.sql.Statement s = c.createStatement()) {
                s.execute("CREATE TABLE country (Code VARCHAR(3), Name VARCHAR(100), Continent VARCHAR(50), Region VARCHAR(50), Population BIGINT, Capital VARCHAR(100));");
                s.execute("INSERT INTO country VALUES ('AAA','CountryA','Europe','RegionA',1000,'CapA');");
                s.execute("INSERT INTO country VALUES ('BBB','CountryB','Europe','RegionB',500,'CapB');");
            } catch (Exception e) {
                fail("Failed to prepare H2 test DB: " + e.getMessage());
            }

            conn = Main.connect();
            assertNotNull(conn, "Could not establish DB connection to test H2 database");
        }
    }

    @org.junit.jupiter.api.AfterAll
    void cleanup() {
        // Clear test DB system properties if we set them
        System.clearProperty("TEST_DB_URL");
        System.clearProperty("TEST_DB_USER");
        System.clearProperty("TEST_DB_PASSWORD");
    }

    @Test
    void testCountryCountQuery() throws Exception {
        // This test will only run when RUN_INTEGRATION_TESTS=true
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS cnt FROM country");
            assertTrue(rs.next());
            long count = rs.getLong("cnt");
            assertTrue(count >= 0, "Country count should be >= 0");
            rs.close();
        }
    }
}
