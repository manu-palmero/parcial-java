package parcial.util;

import java.io.IOException;
import java.util.ArrayList;

import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;

public class TUI {
    public TUI() {

    }

    public void iniciar() {
        limpiarConsola();
        // menuInicial();
        inicio();
    }

    private void limpiarConsola() {
        try {
            final String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (IOException | InterruptedException e) {
            // Ignorar errores al limpiar la consola
        }
    }

    /**
     * Inicia la interfaz de usuario basada en texto utilizando la biblioteca
     * Lanterna.
     * <p>
     * Este método crea una terminal y una pantalla ({@link Screen}), luego
     * inicializa una interfaz gráfica de usuario
     * de múltiples ventanas ({@link MultiWindowTextGUI}) y una ventana básica
     * ({@link BasicWindow}) centrada en la pantalla.
     * Dentro de la ventana, se crea un panel ({@link Panel}) con un diseño vertical
     * ({@link LinearLayout} con {@link Direction#VERTICAL}),
     * donde se agregan varios botones ({@link Button}) correspondientes a las
     * opciones disponibles.
     * Cada botón, al ser presionado, muestra un cuadro de diálogo de mensaje
     * ({@link MessageDialog}) con el nombre de la opción seleccionada.
     * Finalmente, el panel se agrega a la ventana y la ventana se muestra hasta que
     * el usuario la cierre.
     * <p>
     * Clases utilizadas:
     * <ul>
     * <li>{@link Screen}: Representa la pantalla de la terminal.</li>
     * <li>{@link DefaultTerminalFactory}: Fábrica para crear terminales y
     * pantallas.</li>
     * <li>{@link MultiWindowTextGUI}: Administra la interfaz gráfica de usuario
     * basada en texto con soporte para múltiples ventanas.</li>
     * <li>{@link BasicWindow}: Ventana básica que puede contener componentes.</li>
     * <li>{@link Panel}: Contenedor de componentes con soporte para diferentes
     * diseños.</li>
     * <li>{@link LinearLayout}: Gestor de diseño que organiza los componentes en
     * forma lineal (vertical u horizontal).</li>
     * <li>{@link Button}: Componente interactivo que ejecuta una acción al ser
     * presionado.</li>
     * <li>{@link MessageDialog}: Cuadro de diálogo para mostrar mensajes al
     * usuario.</li>
     * </ul>
     * <p>
     * Si ocurre un error de entrada/salida al crear la pantalla, se imprime el
     * mensaje de error en la salida estándar de error.
     */
    public void inicio() {
        try {
            // Crea la terminal y la pantalla
            Screen screen = new DefaultTerminalFactory().createScreen();
            screen.startScreen();

            // Crear interfaz gráfica basada en texto
            MultiWindowTextGUI gui = new MultiWindowTextGUI(screen);
            BasicWindow ventana = new BasicWindow("Inicio");
            ventana.setHints(java.util.Arrays.asList(BasicWindow.Hint.CENTERED));

            // PANELES

            // Crear lista de opciones
            Panel panel = new Panel();
            panel.setLayoutManager(new LinearLayout(Direction.VERTICAL));
            // Panel para los botones inferiores
            Panel panelInferior = new Panel();
            panelInferior.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
            panelInferior.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
            // Espaciador
            Panel espaciador = new Panel();
            espaciador.setLayoutManager(new LinearLayout());
            EmptySpace espacio = new EmptySpace();
            espaciador.addComponent(espacio);

            ArrayList<String> opciones = new ArrayList<>();
            opciones.add("Opción 1");
            opciones.add("Opción 2");
            opciones.add("Opción 3");
            opciones.add("Opción 4");
            opciones.add("Opción 5");

            // Botones
            Button salir = new Button("Salir", () -> {
                ventana.close();
            });

            for (String opcion : opciones) {
                panel.addComponent(new Button(opcion, () -> {
                    MessageDialog.showMessageDialog(gui, "Opción seleccionada", "Se seleccionó: " + opcion);
                }));

            }

            panelInferior.addComponent(salir);

            panel.addComponent(espaciador);
            panel.addComponent(panelInferior);

            // Agregar panel a la ventana
            ventana.setComponent(panel);
            gui.addWindowAndWait(ventana);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
