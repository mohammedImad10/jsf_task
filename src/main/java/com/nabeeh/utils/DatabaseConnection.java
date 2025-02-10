package com.nabeeh.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {

    private static String URL;
    private static String USER;
    private static String PASSWORD;
    private static String DRIVER;

    public static Connection getConnection() throws SQLException {
        try (InputStream inputStream = DatabaseConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (inputStream == null) {
                throw new FileNotFoundException("Property file 'db.properties' not found in the classpath");
            }

            Properties properties = new Properties();
            properties.load(inputStream);

            URL = properties.getProperty("db.url");
            USER = properties.getProperty("db.user");
            PASSWORD = properties.getProperty("db.password");
            DRIVER = properties.getProperty("db.driver");


            if (URL == null || USER == null || PASSWORD == null || DRIVER == null) {
                throw new IllegalArgumentException("Missing database properties in db.properties file.");
            }

            Class.forName(DRIVER);  // Load the database driver

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException("PostgreSQL JDBC Driver not found.");
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Property file 'db.properties' not found", e);
        } catch (IOException e) {
            throw new RuntimeException("Error reading property file", e);
        }

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
