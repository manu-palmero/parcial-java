package parcial.util;

import java.io.IOException;
import java.util.Arrays;

import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

public class TUI {
    Panel espaciador;
    EmptySpace espacio;
    Terminal terminal;
    TerminalScreen screen;

    public TUI() {
        // Crea la terminal y la pantalla
        try {
            terminal = new DefaultTerminalFactory().createTerminal();
            screen = new TerminalScreen(terminal);
            screen.startScreen();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        espaciador = new Panel();
        espaciador.setLayoutManager(new LinearLayout());
        espacio = new EmptySpace();
        espaciador.addComponent(espacio);
    }

    public void iniciar() {
        int seleccion = 1;
        while (seleccion != 0) {
            switch (seleccion) {
                case 0 -> {return;}
                case 1 -> seleccion = inicio();
                case 2 -> seleccion = agregarClientes();
                case 3 -> seleccion = listarClientes();
                default -> throw new AssertionError();
            }
            limpiarConsola();
        }
    }

    private void limpiarConsola() {
        try {
            terminal.clearScreen();
        } catch (IOException e) {
            // Ignorar errores al limpiar la consola
        }
    }

    public int inicio() {
        /**
         * Nota sobre el uso de 'final int[] sel = new int[1];':
         * Se utiliza un arreglo de un solo elemento en lugar de una variable entera
         * normal porque las variables locales deben ser efectivamente finales
         * para ser accedidas dentro de las expresiones lambda (como las acciones de los
         * botones). Al usar un arreglo, se puede modificar su contenido
         * dentro de las lambdas, permitiendo así comunicar la selección del usuario
         * fuera del ámbito de la lambda.
         */
        final int[] sel = new int[1];

        /*
         * PANELES
         */

        // Crear lista de opciones
        Panel panel = new Panel();
        panel.setLayoutManager(new LinearLayout(Direction.VERTICAL));
        // Panel para los botones inferiores
        Panel panelInferior = new Panel();
        panelInferior.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
        panelInferior.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
        // Espaciador

        // Crear interfaz gráfica basada en texto
        MultiWindowTextGUI gui = new MultiWindowTextGUI(screen);

        BasicWindow ventana = new BasicWindow(" Administración de Clientes ");
        ventana.setHints(Arrays.asList(BasicWindow.Hint.CENTERED));

        /*
         * BOTONES
         */

        Button listarClientes = new Button("Listar clientes", () -> {
            ventana.close();
            sel[0] = 3;
        });
        Button agregarCliente = new Button("Agregar cliente", () -> {
            ventana.close();
            sel[0] = 2;
        });

        Button salir = new Button("Salir", () -> {
            ventana.close();
            sel[0] = 0;
        });

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

        return sel[0];
    }

    public int listarClientes() {
        // TODO Implementar lógica para listar clientes
        try {
            limpiarConsola();
            screen.clear();
            screen.newTextGraphics().putString(1, 1, "hola");
            screen.refresh();
            Thread.sleep(1000); // Espera para que el usuario vea el mensaje
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return 1;

        
    }

    public int agregarClientes() {
        // TODO Implementar lógica para agregar un cliente
        System.out.println("Agregando cliente...");
        return 0;
    }

    public void error(MultiWindowTextGUI gui, String mensaje) {
        Panel errorPanel = new Panel();
        errorPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));
        BasicWindow ventana = new BasicWindow();
        ventana.setTitle(" ERROR ");
        ventana.setHints(java.util.Arrays.asList(BasicWindow.Hint.CENTERED));

        // Muestra el mensaje de error en un cuadro de diálogo
        Label textoError = new Label(mensaje);
        Button volver = new Button("Volver", () -> {
            ventana.close();
        });

        errorPanel.addComponent(textoError);
        errorPanel.addComponent(volver);
        ventana.setComponent(errorPanel);
        gui.addWindowAndWait(ventana);

    }
}