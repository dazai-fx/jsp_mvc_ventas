package org.iesvdm.jsp_mvc_ventas.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {

    private static String url;
    private static String username;
    private static String password;

    static {
        try {
            Properties props = new Properties();
            InputStream input = DBConnection.class.getClassLoader().getResourceAsStream("db.properties");

            if (input == null) {
                throw new IOException("No se encontró el archivo db.properties en el classpath.");
            }

            props.load(input);

            String driver = props.getProperty("jdbc.driverClassName");
            String baseUrl = props.getProperty("jdbc.url");
            String schema = props.getProperty("jdbc.schema");
            username = props.getProperty("jdbc.username");
            password = props.getProperty("jdbc.password");

            if (driver == null || driver.isEmpty())
                throw new IllegalArgumentException("La propiedad jdbc.driverClassName no está definida en db.properties");
            if (baseUrl == null || baseUrl.isEmpty())
                throw new IllegalArgumentException("La propiedad jdbc.url no está definida en db.properties");
            if (schema == null || schema.isEmpty())
                throw new IllegalArgumentException("La propiedad jdbc.schema no está definida en db.properties");
            if (username == null || username.isEmpty())
                throw new IllegalArgumentException("La propiedad jdbc.username no está definida en db.properties");
            if (password == null)
                throw new IllegalArgumentException("La propiedad jdbc.password no está definida en db.properties");

            url = baseUrl + schema;

            //System.out.println("Configuración DB cargada:");
            //System.out.println("Driver: " + driver);
            //System.out.println("URL: " + url);
            //System.out.println("Usuario: " + username);

            Class.forName(driver);

        } catch (Exception e) {
            System.err.println("Error al cargar configuración de la base de datos:");
            e.printStackTrace();
            throw new RuntimeException(e); // Fallar rápido para detectar problema
        }
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            System.err.println("Error al conectar con la base de datos.");
            e.printStackTrace();
            return null;
        }

    }

}
