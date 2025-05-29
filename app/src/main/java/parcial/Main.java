package parcial;

import java.sql.Connection;

import parcial.dao.DAO;
import parcial.util.DBConnection;
import parcial.util.TUI;

public class Main {
        public static void main(String[] args) {
        Connection connection = DBConnection.getConnection();
        DAO dao = new DAO(connection);
        
        dao.inicializar();
        
        TUI tui = new TUI();
        tui.iniciar();
    }
}
