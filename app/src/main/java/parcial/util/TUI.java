package parcial.util;

import java.io.IOException;
import java.util.ArrayList;

import org.fusesource.jansi.AnsiConsole;

import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Direction;
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
        AnsiConsole.systemInstall();
        limpiarConsola();
        menuInicial();
    }

    private void limpiarConsola() {
        try {
            final String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                // System.out.print("\033[H\033[2J");
                // System.out.flush();
                // System.out.println("pantalla por borrar");
                new ProcessBuilder("clear").inheritIO().start().waitFor();
                // System.out.println("pantalla borrada");
            }
        } catch (Exception e) {
            // Ignorar errores al limpiar la consola
        }
    }

    public void inicio() {
        try {
            // Crea la terminal y la pantalla
            Screen screen = new DefaultTerminalFactory().createScreen();
            screen.startScreen();

            // Crear interfaz gráfica basada en texto
            MultiWindowTextGUI gui = new MultiWindowTextGUI(screen);
            BasicWindow ventana = new BasicWindow("Inicio");

            // Crear lista de opciones
            Panel panel = new Panel();
            panel.setLayoutManager(new LinearLayout(Direction.VERTICAL));

            ArrayList<String> opciones = new ArrayList<>();
            opciones.add("Opción 1");
            opciones.add("Opción 2");
            opciones.add("Opción 3");
            opciones.add("Opción 4");
            opciones.add("Opción 5");

            for (String opcion : opciones) {
                panel.addComponent(new Button(opcion, () -> {
                    MessageDialog.showMessageDialog(gui, opcion, opcion);}));
            }

            // Agregar panel a la ventana
            ventana.setComponent(panel);
            gui.addWindowAndWait(ventana);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public void menuInicial() {
        String[] opciones = {
                "Opción 1",
                "Opción 2",
                "Opción 3",
                "Salir"
        };
        int seleccion = 0;
        java.util.Scanner scanner = new java.util.Scanner(System.in);

        while (true) {
            limpiarConsola();
            System.out.println("\u001B[44m"); // Fondo celeste
            System.out.println("╔════════════════════════════════════════════╗");
            System.out.println("║           \u001B[47m\u001B[30m  MENÚ PRINCIPAL  \u001B[0m\u001B[44m           ║");
            System.out.println("╠════════════════════════════════════════════╣");
            for (int i = 0; i < opciones.length; i++) {
                // if (i == seleccion) {
                // System.out.printf("║ \u001B[47m\u001B[30m> %d.
                // %s\u001B[0m\u001B[44m%"+(36-opciones[i].length())+"s║\n", i+1, opciones[i],
                // "");
                // } else {
                // System.out.printf("║ %d. %s%"+(39-opciones[i].length())+"s║\n", i+1,
                // opciones[i], "");
                // }
                // TODO Ver qué hace %d y %s para agragar cantidad de espacios
                System.out.printf("║    %d. %s%" + (39 - opciones[i].length()) + "s║\n", i + 1, opciones[i], "");
            }
            System.out.println("╚════════════════════════════════════════════╝");
            System.out.print("\u001B[0m"); // Reset
            System.out.println("Use ↑/↓ (w/s) para mover, Enter para seleccionar.");

            String input = scanner.nextLine();

            switch (input) {
                case "1" -> System.out.println("");
            }

            if (input.equalsIgnoreCase("w") || input.equals("[[A")) {
                seleccion = (seleccion - 1 + opciones.length) % opciones.length;
            } else if (input.equalsIgnoreCase("s") || input.equals("[[B")) {
                seleccion = (seleccion + 1) % opciones.length;
            } else if (input.isEmpty()) {
                // Enter presionado
                if (seleccion == opciones.length - 1) {
                    // Salir
                    break;
                } else {
                    System.out.println("Seleccionaste: " + opciones[seleccion]);
                    System.out.println("Presiona Enter para continuar...");
                    scanner.nextLine();
                }
            }
        }
        scanner.close();
    }
}
