package parcial.util;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import parcial.exception.DatabaseException;

public class DBConnection {
    private static final String SEPARATOR = File.separator;
    private static final String LOCATION = System.getProperty("user.dir") + SEPARATOR;
    private static final String DB_FILE = LOCATION + "database";
    private static final String URL = "jdbc:h2:" + DB_FILE;
    private static final String USER = "";
    private static final String PASSWORD = "";
    protected static final Logger logger = LogManager.getLogger();

    public static Connection getConnection() {

        Connection c = null;
        try {
            c = DriverManager.getConnection(URL, USER, PASSWORD);
            Class.forName("org.h2.Driver");
            if (c == null) {
                throw new DatabaseException("No se pudo conectar a la base de datos");
            }
        } catch (ClassNotFoundException e) {
            logger.error("Controlador de base de datos no encontrado: " + e.getMessage());
        } catch (DatabaseException e) {
            logger.error(e.getMessage());
        } catch (SQLException e) {
            logger.error("Error con la base de datos: " + e);
        }
        return c;
    }
}