package parcial.util;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.TextBox;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import parcial.dao.DAO;
import parcial.model.Cliente;

/**
 * La clase {@code TUI} implementa una interfaz de usuario basada en texto (TUI)
 * para la administración de clientes,
 * utilizando la biblioteca Lanterna para la creación de interfaces gráficas en
 * terminal.
 * Permite listar, agregar, eliminar, buscar y mostrar información de clientes
 * almacenados en un DAO.
 * 
 * <p>
 * Características principales:
 * </p>
 * <ul>
 * <li>Menú principal con opciones para gestionar clientes.</li>
 * <li>Ventanas modales para agregar, eliminar, buscar y mostrar información de
 * clientes.</li>
 * <li>Manejo de errores y mensajes de confirmación mediante cuadros de
 * diálogo.</li>
 * <li>Uso de lambdas para manejar acciones de botones y navegación entre
 * ventanas.</li>
 * </ul>
 * 
 * <p>
 * Dependencias:
 * </p>
 * <ul>
 * <li>Lanterna (para la interfaz de usuario en terminal).</li>
 * <li>DAO para la gestión de datos de clientes.</li>
 * <li>Logger para el registro de eventos y errores.</li>
 * </ul>
 * 
 * <p>
 * Uso típico:
 * </p>
 * 
 * <pre>
 * DAO dao = new DAO();
 * TUI tui = new TUI(dao);
 * tui.iniciar();
 * </pre>
 * 
 * <p>
 * Nota:
 * </p>
 * <ul>
 * <li>La clase asume que las implementaciones de {@code DAO} y {@code Cliente}
 * están correctamente definidas.</li>
 * <li>El flujo de navegación se controla mediante un arreglo entero {@code sel}
 * para permitir la modificación dentro de lambdas.</li>
 * </ul>
 * 
 * @author manu
 */
public class TUI {
    protected static final Logger logger = LogManager.getLogger();
    private final int[] sel = new int[1];
    private Terminal terminal;
    private TerminalScreen screen;
    private MultiWindowTextGUI gui;
    private DAO dao;
    private Cliente clienteSeleccionado;

    public TUI(DAO dao) {
        logger.debug("Iniciando interfaz de usuario...");
        this.dao = dao;

        try {
            // Crea la terminal y la pantalla
            terminal = new DefaultTerminalFactory().createTerminal();
            screen = new TerminalScreen(terminal);
            screen.startScreen();
            // Crear interfaz gráfica basada en texto
            gui = new MultiWindowTextGUI(screen);
        } catch (IOException e) {
            logger.error("Error al iniciar la interfaz de usuario: " + e.getMessage());
        }
        logger.info("Interfaz de usuario iniciada.");
    }

    /**
     * Inicia el bucle principal de la interfaz de usuario por consola.
     * Permite al usuario seleccionar diferentes opciones del menú hasta que elija
     * salir (selección 0).
     * Según la opción seleccionada, ejecuta las acciones correspondientes:
     * - 1: Muestra el menú de inicio.
     * - 2: Lista los clientes.
     * - 3: Agrega un nuevo cliente.
     * - 4: Elimina un cliente seleccionado.
     * - 5: Busca un cliente seleccionado.
     * - 6: Muestra información de un cliente seleccionado.
     * Después de cada acción, limpia la consola antes de mostrar el menú
     * nuevamente.
     * Sale del bucle y termina la ejecución cuando la selección es 0.
     */
    public void iniciar() {
        int seleccion = 1;
        while (seleccion != 0) {
            switch (seleccion) {
                case 0 -> {
                    return;
                }
                case 1 -> seleccion = inicio();
                case 2 -> seleccion = listarClientes();
                case 3 -> seleccion = agregarCliente();
                case 4 -> seleccion = eliminarCliente(clienteSeleccionado);
                case 5 -> seleccion = buscarCliente(clienteSeleccionado);
                case 6 -> seleccion = infoCliente(clienteSeleccionado);
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

    private Panel espaciador() {
        Panel espaciador = new Panel();
        espaciador.setLayoutManager(new LinearLayout());
        espaciador.addComponent(new EmptySpace());
        return espaciador;
    }

    private Panel crearPanelVertical() {
        Panel panel = new Panel();
        panel.setLayoutManager(new LinearLayout(Direction.VERTICAL));
        return panel;
    }

    private Panel crearPanelHorizontal() {
        Panel panel = new Panel();
        panel.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
        panel.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
        return panel;
    }

    private BasicWindow crearVentana(String titulo) {
        BasicWindow ventana = new BasicWindow();
        ventana.setTitle(" " + titulo + " ");
        ventana.setHints(java.util.Arrays.asList(BasicWindow.Hint.CENTERED));
        return ventana;
    }

    /**
     * Muestra una ventana de diálogo de error con el mensaje especificado.
     *
     * @param mensaje el mensaje de error a mostrar en el diálogo
     */
    private void error(String mensaje) {
        String titulo = "ERROR";
        Panel errorPanel = crearPanelVertical();
        BasicWindow ventana = crearVentana(titulo);

        // Muestra el mensaje de error en un cuadro de diálogo
        Label textoError = new Label(mensaje);
        Button volver = new Button("Volver", () -> {
            ventana.close();
        });

        errorPanel.addComponent(textoError);
        errorPanel.addComponent(espaciador());
        errorPanel.addComponent(volver);
        ventana.setComponent(errorPanel);
        gui.addWindowAndWait(ventana);
    }


    /**
     * Muestra una ventana principal de administración de clientes con opciones para listar,
     * agregar clientes o salir de la aplicación. Utiliza un arreglo de un solo elemento para
     * permitir la modificación de la selección del usuario dentro de expresiones lambda.
     *
     * @return un entero que representa la opción seleccionada por el usuario:
     *         2 para "Listar clientes",
     *         3 para "Agregar cliente",
     *         0 para "Salir".
     */
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

        /*
         * PANELES
         */

        // Crear lista de opciones
        Panel panel = crearPanelVertical();
        // Panel para los botones inferiores
        Panel panelInferior = crearPanelHorizontal();

        BasicWindow ventana = crearVentana("Administración de Clientes");

        /*
         * BOTONES
         */

        Button listarClientes = new Button("Listar clientes", () -> {
            ventana.close();
            sel[0] = 2;
        });
        Button agregarCliente = new Button("Agregar cliente", () -> {
            ventana.close();
            sel[0] = 3;
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
        panel.addComponent(espaciador());
        panel.addComponent(panelInferior);

        // Agregar panel a la ventana
        ventana.setComponent(panel);
        gui.addWindowAndWait(ventana);

        return sel[0];
    }

    /**
     * Muestra una ventana con la lista de clientes obtenidos desde la base de datos.
     * Permite al usuario seleccionar un cliente, buscar un cliente específico o volver al menú principal.
     * Si no hay clientes registrados, muestra un mensaje indicándolo.
     *
     * @return Un valor entero que indica la acción seleccionada por el usuario:
     *         6 si se selecciona un cliente,
     *         5 si se elige buscar un cliente,
     *         1 si se vuelve al menú principal.
     */
    public int listarClientes() {
        Panel panel = crearPanelVertical();
        Panel scrollPanel = crearPanelVertical();
        BasicWindow ventana = crearVentana("Lista de clientes");

        ArrayList<Cliente> listaClientes = dao.obtenerClientes();

        if (!listaClientes.isEmpty()) {
            for (Cliente clienteDeLista : listaClientes) {
                Button clienteBtn = new Button(clienteDeLista.getNombre(), () -> {
                    sel[0] = 6;
                    ventana.close();
                    this.clienteSeleccionado = clienteDeLista;
                });
                scrollPanel.addComponent(clienteBtn);
            }
        } else {
            scrollPanel.addComponent(new Label("No hay clientes en la base de datos."));
        }

        // Agrega el panel de scroll a la ventana principal

        // Botón para volver al menú principal
        Button volverBtn = new Button("Volver", () -> {
            sel[0] = 1;
            ventana.close();
        });
        Button buscarBtn = new Button("Buscar cliente", () -> {
            sel[0] = 5;
            ventana.close();
        });

        panel.addComponent(volverBtn);
        panel.addComponent(buscarBtn);
        panel.addComponent(espaciador());
        panel.addComponent(scrollPanel);

        ventana.setComponent(panel);
        gui.addWindowAndWait(ventana);

        return sel[0];

    }

    /**
     * Muestra una ventana con la información detallada de un cliente seleccionado,
     * permitiendo al usuario eliminar el cliente o volver a la pantalla anterior.
     *
     * @param cliente El cliente cuya información se va a mostrar.
     * @return Un valor entero que indica la acción seleccionada por el usuario:
     *         2 para volver, 4 para eliminar el cliente.
     */
    private int infoCliente(Cliente cliente) {
        // Información del cliente seleccionado
        Panel infoPanel = crearPanelVertical();
        BasicWindow infoVentana = crearVentana("Información del Cliente");

        /*
         * BOTONES
         */
        // Volver
        Button volverBtn = new Button("Volver", () -> {
            sel[0] = 2;
            infoVentana.close();
        });
        // Eliminar

        Button eliminarBtn = new Button("Eliminar cliente", () -> {
            this.clienteSeleccionado = cliente;
            sel[0] = 4;
            infoVentana.close();
        });

        /*
         * AGREGAR COMPONENTES
         */
        // Agregar Información
        infoPanel.addComponent(new Label("Nombre: " + cliente.getNombre()));
        infoPanel.addComponent(new Label("DNI: " + cliente.getDni()));

        infoPanel.addComponent(espaciador());
        // Agregar botones
        infoPanel.addComponent(eliminarBtn);
        infoPanel.addComponent(volverBtn);
        infoVentana.setComponent(infoPanel);
        gui.addWindowAndWait(infoVentana);

        return sel[0];
    }

    /**
     * Muestra una ventana para agregar un nuevo cliente solicitando su nombre y DNI.
     * Valida que ambos campos estén completos y que el DNI sea un número válido.
     * Si el cliente se agrega correctamente, muestra una ventana de confirmación con los datos ingresados.
     * Si ocurre un error (campos vacíos, DNI inválido o DNI duplicado), muestra un mensaje de error correspondiente.
     * 
     * @return 1 siempre, indicando que la operación fue ejecutada (éxito o error).
     */
    private int agregarCliente() {
        Panel panel = crearPanelVertical();
        BasicWindow ventana = crearVentana("Agregar un cliente");

        TextBox nombreBox = new TextBox();
        TextBox dniBox = new TextBox();

        panel.addComponent(new Label("Nombre:"));
        panel.addComponent(nombreBox);
        panel.addComponent(new Label("DNI:"));
        panel.addComponent(dniBox);

        Button guardarBtn = new Button("Guardar", () -> {
            String nombre = nombreBox.getText().trim();
            String dniString = dniBox.getText().trim();
            if (nombre.isEmpty() || dniString.isEmpty()) {
                error("Debe completar ambos campos.");
                return;
            }
            try {
                int dni = Integer.parseInt(dniString);
                Cliente nuevo = new Cliente(dni, nombre);
                if (dao.agregarCliente(nuevo)) {

                    // Ventana de confirmación
                    BasicWindow confirmacionVentana = crearVentana("Cliente agregado");
                    Panel confirmacionPanel = crearPanelVertical();
                    confirmacionPanel.addComponent(new Label("Cliente agregado correctamente:"));
                    confirmacionPanel.addComponent(new Label("Nombre: " + nuevo.getNombre()));
                    confirmacionPanel.addComponent(new Label("DNI: " + nuevo.getDni()));
                    Button volverBtn = new Button("Volver", confirmacionVentana::close);
                    confirmacionPanel.addComponent(volverBtn);
                    confirmacionVentana.setComponent(confirmacionPanel);
                    gui.addWindowAndWait(confirmacionVentana);
                    logger.info("Cliente agregado: " + nuevo.getNombre() + " con DNI " + nuevo.getDni());
                } else {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Error en la creación del cliente,\n");
                    stringBuilder.append("es probable que haya ingresado un DNI que ya existe.\n\n");
                    stringBuilder.append("Puede revisar el registro para más información.");
                    throw new Exception(
                            stringBuilder.toString());
                }
                // Cerrar la ventana de agregar cliente
                ventana.close();
            } catch (NumberFormatException e) {
                error("Error al agregar cliente: El DNI no es un número válido.\n" + e.getMessage());
            } catch (Exception e) {
                error(e.getMessage());
            }
        });

        Button cancelarBtn = new Button("Cancelar", ventana::close);

        Panel botones = crearPanelHorizontal();
        botones.addComponent(guardarBtn);
        botones.addComponent(cancelarBtn);

        panel.addComponent(espaciador());
        panel.addComponent(botones);

        ventana.setComponent(panel);
        gui.addWindowAndWait(ventana);

        return 1;
    }

    /**
     * Muestra una ventana de confirmación para eliminar un cliente específico.
     * Si el usuario confirma, intenta eliminar el cliente utilizando el DAO y muestra
     * una ventana de confirmación adicional si la eliminación fue exitosa.
     *
     * @param cliente El cliente que se desea eliminar.
     * @return Siempre retorna 2 tras cerrar la ventana.
     */
    private int eliminarCliente(Cliente cliente) {
        Panel panel = crearPanelVertical();
        Panel botoneraPanel = crearPanelHorizontal();
        BasicWindow ventana = crearVentana("Borrar cliente");

        Label nombreLabel = new Label("Nombre: " + cliente.getNombre());
        Label dniLabel = new Label("DNI: " + String.valueOf(cliente.getDni()));

        Button confirmarButton = new Button("Confirmar", () -> {
            if (dao.eliminarCliente(cliente.getDni())) {
                Panel confirmacionPanel = crearPanelVertical();
                BasicWindow confirmacionVentana = crearVentana("Cliente eliminado");

                confirmacionPanel.addComponent(new Label("Cliente eliminado."));
                confirmacionPanel.addComponent(new Button("OK", () -> {
                    confirmacionVentana.close();
                    ventana.close();
                }));
                confirmacionVentana.setComponent(confirmacionPanel);
                gui.addWindowAndWait(confirmacionVentana);
            }

        });
        Button cancelarButton = new Button("Cancelar", ventana::close);

        botoneraPanel.addComponent(confirmarButton);
        botoneraPanel.addComponent(cancelarButton);
        panel.addComponent(new Label("¿Confirmar eliminación del cliente?"));
        panel.addComponent(nombreLabel);
        panel.addComponent(dniLabel);
        panel.addComponent(espaciador());
        panel.addComponent(botoneraPanel);
        ventana.setComponent(panel);
        gui.addWindowAndWait(ventana);

        return 2;
    }

    /**
     * Muestra una ventana para buscar un cliente por su DNI.
     * Permite al usuario ingresar el DNI de un cliente y buscarlo en la base de datos.
     * Si el cliente es encontrado, se selecciona y se navega a la información del cliente.
     * Si no se encuentra el cliente o el DNI ingresado no es válido, muestra un mensaje de error.
     *
     * @param cliente Objeto Cliente (no utilizado directamente en este método).
     * @return Un entero que indica la siguiente acción a realizar según la selección del usuario.
     */
    public int buscarCliente(Cliente cliente) {
        sel[0] = 2;
        Panel panel = crearPanelVertical();
        BasicWindow ventana = crearVentana("Buscar cliente");

        TextBox dniBox = new TextBox();

        panel.addComponent(new Label("Ingrese el DNI del cliente:"));
        panel.addComponent(dniBox);

        Button buscarBtn = new Button("Buscar", () -> {
            String dniString = dniBox.getText().trim();
            if (dniString.isEmpty()) {
                error("Debe ingresar un DNI.");
                return;
            }
            try {
                int dni = Integer.parseInt(dniString);
                Cliente encontrado = dao.buscarCliente(dni);
                if (encontrado != null) {
                    ventana.close();
                    this.clienteSeleccionado = encontrado;
                    sel[0] = 6; // Ir a infoCliente
                } else {
                    error("No se encontró un cliente con ese DNI.");
                }
            } catch (NumberFormatException e) {
                error("El DNI debe ser un número válido.");
            }
        });

        Button cancelarBtn = new Button("Cancelar", ventana::close);

        Panel botones = crearPanelHorizontal();
        botones.addComponent(buscarBtn);
        botones.addComponent(cancelarBtn);

        panel.addComponent(espaciador());
        panel.addComponent(botones);

        ventana.setComponent(panel);
        gui.addWindowAndWait(ventana);

        return sel[0];
    }
}