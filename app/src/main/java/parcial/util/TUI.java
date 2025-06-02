package parcial.util;

import java.io.IOException;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.graphics.Theme;
import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

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
     * Este método limpia la consola y luego crea una terminal y una pantalla
     * ({@link TerminalScreen}),
     * inicializando una interfaz gráfica de usuario de múltiples ventanas
     * ({@link MultiWindowTextGUI}).
     * Se crea una ventana principal ({@link BasicWindow}) centrada, que contiene un
     * panel vertical
     * ({@link Panel} con {@link LinearLayout} y {@link Direction#VERTICAL}) donde
     * se agregan botones
     * para listar clientes, agregar clientes y salir.
     * <p>
     * Cada botón ejecuta una acción específica al ser presionado:
     * <ul>
     * <li>"Listar clientes": Llama al método
     * {@link #listarClientes(Terminal, TerminalScreen, MultiWindowTextGUI, BasicWindow)}.</li>
     * <li>"Agregar cliente": Llama al método
     * {@link #agregarClientes(Terminal, TerminalScreen, MultiWindowTextGUI, BasicWindow)}.</li>
     * <li>"Salir": Cierra la ventana principal.</li>
     * </ul>
     * <p>
     * El panel inferior contiene el botón de salida y se centra horizontalmente.
     * Si ocurre un error de entrada/salida al crear la pantalla, se imprime el
     * mensaje de error en la salida estándar de error.
     * <p>
     * Clases utilizadas:
     * <ul>
     * <li>{@link Terminal}: Representa la terminal física o virtual.</li>
     * <li>{@link TerminalScreen}: Maneja la pantalla sobre la terminal.</li>
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
     * <li>{@link EmptySpace}: Espaciador visual dentro de los paneles.</li>
     * </ul>
     */
    public void inicio() {
        try {
            // Crea la terminal y la pantalla
            Terminal terminal = new DefaultTerminalFactory().createTerminal();
            TerminalScreen screen = new TerminalScreen(terminal);
            screen.startScreen();
            terminal.clearScreen();
            screen.clear();

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

            // Crear interfaz gráfica basada en texto
            BasicWindow ventana = new BasicWindow("Administración de Clientes");
            MultiWindowTextGUI gui = new MultiWindowTextGUI(screen);

            ventana.setHints(java.util.Arrays.asList(BasicWindow.Hint.CENTERED));
            ventana.setTheme(new SimpleTheme(
                    TextColor.ANSI.BLACK, // foreground
                    TextColor.ANSI.WHITE, // background
                    SGR.REVERSE));
            gui.setTheme(new SimpleTheme(
                    TextColor.ANSI.WHITE,
                    TextColor.ANSI.BLACK));
            // ventana.setTheme(ventanaTheme);

            // BOTONES

            Button listarClientes = new Button("Listar clientes", () -> {
                listarClientes(gui, ventana);
            });
            Button agregarCliente = new Button("Agregar cliente", () -> {
                agregarClientes(gui, ventana);
            });

            Button salir = new Button("Salir", () -> {
                ventana.close();
            });

            Button errorButton = new Button("Error", () -> {
                error(gui, ventana, "Este es un mensaje de error de prueba.");
            });
            panel.addComponent(errorButton);

            // Agregar botones al panel
            panel.addComponent(listarClientes);
            panel.addComponent(agregarCliente);
            // Agregar botones al panel inferior
            panelInferior.addComponent(salir);
            // Agregar componentes al panel
            panel.addComponent(espaciador);
            panel.addComponent(panelInferior);

            // Agregar panel a la ventana
            ventana.setComponent(panel);
            gui.addWindowAndWait(ventana);

        } catch (IOException e) {
            System.err.println(e.getMessage());

        }
    }

    public void listarClientes(MultiWindowTextGUI gui, BasicWindow ventana) {
        // TODO Implementar lógica para listar clientes
        System.out.println("Listando clientes...");
    }

    public void agregarClientes(MultiWindowTextGUI gui, BasicWindow ventana) {
        // TODO Implementar lógica para agregar un cliente
        System.out.println("Agregando cliente...");
    }

    public void error(MultiWindowTextGUI gui, BasicWindow ventana, String mensaje) {
        Theme defaultTheme = gui.getTheme();
        Theme ventanaTheme = ventana.getTheme();
        gui.setTheme(new SimpleTheme(
                com.googlecode.lanterna.TextColor.ANSI.BLACK, // foreground
                com.googlecode.lanterna.TextColor.ANSI.RED // background
        ));
        ventana.setTheme(new SimpleTheme(TextColor.ANSI.BLACK, TextColor.ANSI.BLACK));

        // Muestra el mensaje de error en un cuadro de diálogo
        MessageDialog.showMessageDialog(gui, "Error", mensaje);
        gui.setTheme(defaultTheme); // Reinicia la ventana al tema original
        ventana.setTheme(ventanaTheme);
        // TODO Hacer que se ejecute en una pantalla a parte para que no se vea encima de lo otro
    }
}