package fr.forty_two.repositories;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

public class EmbeddedDataSourceTest {
    private DataSource ds;

    @BeforeEach
    public void init() {
        ds = new EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.HSQL)
            .addScript("schema.sql")
            .addScript("data.sql")
            .build();
    }

    @Test
    @DisplayName("Ensure that database is successfully integrated by checking if DataSource returns a proper connection")
    void shouldReturnConnection() throws SQLException {
        Connection conn = ds.getConnection();
        assertNotNull(conn);
        conn.close();
    }
}