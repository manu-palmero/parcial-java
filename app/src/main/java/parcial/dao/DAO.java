package parcial.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import parcial.model.Cliente;
import parcial.util.DBConnection;

public class DAO implements DAOInterface {
    protected static final Logger logger = LogManager.getLogger();
    /**
     * La conexión a la base de datos utilizada por esta instancia de DAO.
     * Se inicializa usando la clase singleton DBConnection para asegurar que se use una única conexión en toda la aplicación.
     */
    private final Connection connection = DBConnection.getConnection();

    @Override
    public void inicializar() {
        logger.info("Inicializando la base de datos...");
        String sql = "CREATE TABLE IF NOT EXISTS clientes (dni INT PRIMARY KEY, nombre VARCHAR(255));";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas > 0) {
                logger.info("Tabla clientes creada.");
            } else {
                logger.debug("La tabla clientes ya existe, no se creará de nuevo.");
            }
        } catch (SQLException e) {
            logger.error(
                    "Error SQL: No se pudo inicializar la tabla clientes.\n" +
                            e.getMessage());
        }
    }

    @Override
    public ArrayList<Cliente> obtenerClientes() {
        ArrayList<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT dni, nombre FROM clientes";
        logger.debug("Obteniendo la lista de clientes...");
        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int dni = rs.getInt("dni");
                String nombre = rs.getString("nombre");
                clientes.add(new Cliente(dni, nombre));
            }
            logger.debug("Lista de clientes obtenida.");
        } catch (SQLException e) {
            logger.error(
                    "Error SQL: No se pudo obtener la lista de clientes.\n" +
                            e.getMessage());
        }
        return clientes;
    }

    @Override
    public boolean agregarCliente(Cliente cliente) {
        logger.debug("Agregando al cliente " + cliente.getNombre() +
                " con DNI " +
                cliente.getDni() + ".");
        String sql = "INSERT INTO clientes (dni, nombre) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, cliente.getDni());
            pstmt.setString(2, cliente.getNombre());
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            logger.error(
                    "Error SQL: No se pudo agregar el cliente " + cliente.getNombre() +
                            " con DNI " +
                            cliente.getDni() + ".\n" +
                            e.getMessage());
            return false;
        }
    }

    @Override
    public Cliente buscarCliente(int dni) {
        String sql = "SELECT dni, nombre FROM clientes WHERE dni = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, dni);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String nombre = rs.getString("nombre");
                    return new Cliente(dni, nombre);
                }
            }
        } catch (SQLException e) {
            logger.error(
                    "Error SQL: " +
                            e.getMessage());
        }
        return null;
    }

    @Override
    public boolean eliminarCliente(int dni) {
        String sql = "DELETE FROM clientes WHERE dni = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, dni);
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            logger.error(
                    "Error SQL: " +
                            e.getMessage());
            return false;
        }
    }
}
