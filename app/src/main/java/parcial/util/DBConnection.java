package parcial.util;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import parcial.exception.DatabaseException;

public class DBConnection {
    private static final String SEPARATOR = File.separator;
    private static final String LOCATION = System.getProperty("user.dir") + SEPARATOR;
    private static final String DB_FILE = LOCATION + "";
    private static final String URL = "jdbc:h2:" + DB_FILE;
    private static final String USER = "";
    private static final String PASSWORD = "";

    public static Connection getConnection() {
        Connection c = null;
        try {
            c = DriverManager.getConnection(URL, USER, PASSWORD);
            Class.forName("org.h2.Driver");
            if (c == null) {
                throw new DatabaseException("No se pudo conectar a la base de datos");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Controlador de base de datos no encontrado: " + e.getMessage());
        } catch (DatabaseException e) {
            System.err.println(e.getMessage());
        } catch (SQLException e) {
            System.err.println("Error con la base de datos: " + e);
        }
        return c;
    }
}