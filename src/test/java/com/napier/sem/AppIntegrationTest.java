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
        // Only run integration test when explicitly enabled via env var
        boolean runIntegration = Boolean.parseBoolean(System.getenv().getOrDefault("RUN_INTEGRATION_TESTS", "false"));
        assumeTrue(runIntegration, "Integration tests disabled - set RUN_INTEGRATION_TESTS=true to enable");

        conn = Main.connect();
        assertNotNull(conn, "Could not establish DB connection");
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
