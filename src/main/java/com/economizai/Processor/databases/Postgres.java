package com.economizai.Processor;


import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
public class Postgres {

    private static final String jdbcUrl = "jdbc:postgresql://localhost:5432/economizai";
    private static final String username = "postgres";
    private static final String password = "dv@_7469";

    public static Connection connect() throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");

        return (Connection) DriverManager.getConnection(jdbcUrl, username, password);
    }
}
