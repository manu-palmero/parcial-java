package parcial;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import parcial.dao.DAO;
import parcial.util.TUI;

public class Main {
    protected static final Logger logger = LogManager.getLogger();

    /**
     * Construye una nueva instancia de Main.
     * <p>
     * Este constructor inicializa el objeto de acceso a datos (DAO) de la aplicación,
     * configura los datos iniciales y lanza la interfaz de usuario basada en texto (TUI).
     * </p>
     * <ul>
     *   <li>Crea e inicializa el DAO llamando a {@code dao.inicializar()}.</li>
     *   <li>Instancia la TUI con el DAO y comienza la interfaz de usuario llamando a {@code tui.iniciar()}.</li>
     * </ul>
     */
    public Main() {
        DAO dao = new DAO();
        dao.inicializar();

        TUI tui = new TUI(dao);
        tui.iniciar();
    }

    private void terminar() {
        logger.info("Fin del programa.");
        System.exit(0);
    }

    public static void main(String[] args) {
        logger.info("Inicio del programa.");
        Main main = new Main();
        main.terminar();
    }
}
