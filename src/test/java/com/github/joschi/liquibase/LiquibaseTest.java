package com.github.joschi.liquibase;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseConnection;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.FileSystemResourceAccessor;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.Assert.assertTrue;

public class LiquibaseTest {
    @Test
    public void test() throws Exception {
        final URL resource = LiquibaseTest.class.getResource("/migrations.xml");
        final File file = new File(resource.toURI());
        try (Connection conn = DriverManager.getConnection("jdbc:h2:~/test", "sa", "")) {
            final Database database = createDatabase(conn);
            final Liquibase liquibase = new Liquibase(file.getAbsolutePath(), new FileSystemResourceAccessor(), database);

            assertTrue(liquibase.getDatabaseChangeLog().getChangeSets().isEmpty());
        }
    }

    private Database createDatabase(Connection conn) throws SQLException, LiquibaseException {
        final DatabaseConnection databaseConnection = new JdbcConnection(conn);
        return DatabaseFactory.getInstance().findCorrectDatabaseImplementation(databaseConnection);
    }
}
