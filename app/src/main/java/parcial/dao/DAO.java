package parcial.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import parcial.model.Cliente;

public class DAO {
    private final Connection connection;

    public DAO(Connection connection) {
        this.connection = connection;
    }

    public void inicializar() {
        String sql = "CREATE TABLE IF NOT EXISTS ejemplo (dni INT PRIMARY KEY, nombre VARCHAR(255));";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error SQL: " + e);
        }
    }

    public void insertarEjemplo(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO ejemplo (id, nombre) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, cliente.getId());
            pstmt.setString(2, cliente.getNombre());
            pstmt.executeUpdate();
        }
    }

    public Cliente obtenerEjemploPorId(int id) throws SQLException {
        String sql = "SELECT * FROM ejemplo WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Cliente(rs.getInt("id"), rs.getString("nombre"));
            }
        }
        return null;
    }

    public List<Cliente> listarEjemplos() throws SQLException {
        List<Cliente> ejemplos = new ArrayList<>();
        String sql = "SELECT * FROM ejemplo";
        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                ejemplos.add(new Cliente(rs.getInt("id"), rs.getString("nombre")));
            }
        }
        return ejemplos;
    }

    public void eliminarEjemplo(int id) throws SQLException {
        String sql = "DELETE FROM ejemplo WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }
}
