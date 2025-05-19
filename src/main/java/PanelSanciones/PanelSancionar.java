/*
 * Panel compacto para registrar sanciones a usuarios por problemas con préstamos.
 * Diseño dividido en dos columnas: izquierda para detalles de la sanción, derecha para elementos afectados.
 * Optimizado para caber en la ventana sin necesidad de scroll.
 */
package PanelSanciones;

import Clases.Sancion;
import Clases.SancionEquipamiento;
import Clases.SancionInsumo;
import Clases.SancionEquipo;
import Controles.ControladorSancion;
import Controles.ControladorSancionEquipamiento;
import Controles.ControladorSancionInsumo;
import Controles.ControladorSancionEquipo;
import Controles.ControladorEquipamiento;
import Controles.ControladorInsumo;
import Controles.ControladorEquipo;
import com.toedter.calendar.JDateChooser;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.*;
import java.util.Calendar;
import java.util.logging.Logger;
import java.util.logging.Level;
import javax.swing.plaf.basic.BasicComboBoxUI;

/**
 * Clase que representa un panel compacto para registrar sanciones, dividido en dos columnas.
 */
public class PanelSancionar extends JPanel {

    private static final Logger LOGGER = Logger.getLogger(PanelSancionar.class.getName());
    
    // Componentes de la interfaz gráfica
    private JTextField txtRuUsuario;
    private JTextField txtIdPrestamo;
    private JComboBox<String> cmbTipoSancion;
    private JTextArea txtDescripcion;
    private JDateChooser fechaSancion;
    private JComboBox<String> cmbEstadoSancion;
    private JDateChooser fechaInicio;
    private JDateChooser fechaFin;
    private JSpinner spinnerDiasSuspension;
    private JComboBox<String> cmbTipoAfectado;
    private JTextField txtIdAfectado;
    private JSpinner spinnerCantidadAfectada;
    private JButton btnAgregarAfectado;
    private JButton btnGuardar;
    
    // Variables para almacenar IDs de elementos afectados
    private Integer idEquipamientoAfectado = null;
    private Integer idInsumoAfectado = null;
    private String idEquipoAfectado = null;
    private Integer cantidadInsumoAfectada = 0;
    
    // Bandera para verificar si se agregó un elemento afectado
    private boolean elementoAfectadoAgregado = false;

    // Controladores
    private ControladorSancion controladorSancion;
    private ControladorSancionEquipamiento controladorSancionEquipamiento;
    private ControladorSancionInsumo controladorSancionInsumo;
    private ControladorSancionEquipo controladorSancionEquipo;
    private ControladorEquipamiento controladorEquipamiento;
    private ControladorInsumo controladorInsumo;
    private ControladorEquipo controladorEquipo;

    // Paleta de colores (igual que PanelVisualizarPrestamos)
    private static final Color PRIMARY_COLOR = new Color(21, 101, 192); // Azul oscuro
    private static final Color BACKGROUND_COLOR = new Color(238, 238, 238); // Gris claro
    private static final Color CARD_COLOR = new Color(250, 250, 250); // Blanco suave
    private static final Color TEXT_COLOR = new Color(33, 33, 33); // Gris oscuro
    private static final Color BORDER_COLOR = new Color(189, 189, 189); // Gris claro
    private static final Color SUCCESS_COLOR = new Color(46, 125, 50); // Verde

    /**
     * Constructor del panel.
     */
    public PanelSancionar() {
        inicializarControladores();
        inicializarUI();
        configurarEventos();
    }

    /**
     * Inicializa los controladores para interactuar con la lógica de negocio.
     */
    private void inicializarControladores() {
        try {
            controladorSancion = new ControladorSancion();
            controladorSancionEquipamiento = new ControladorSancionEquipamiento();
            controladorSancionInsumo = new ControladorSancionInsumo();
            controladorSancionEquipo = new ControladorSancionEquipo();
            controladorEquipamiento = new ControladorEquipamiento();
            controladorInsumo = new ControladorInsumo();
            controladorEquipo = new ControladorEquipo();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al inicializar controladores", e);
            JOptionPane.showMessageDialog(this, 
                "Error al inicializar controladores: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Inicializa los componentes gráficos del panel en un diseño compacto.
     */
    private void inicializarUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(BACKGROUND_COLOR);
        setBorder(new EmptyBorder(15, 15, 15, 15));

        // Título
        JLabel titleLabel = new JLabel("Sancionar Usuarios", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 24));
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setBorder(new EmptyBorder(10, 0, 15, 0));
        add(titleLabel, BorderLayout.NORTH);

        // Panel central con dos columnas
        JPanel panelCentral = new JPanel(new GridLayout(1, 2, 10, 0));
        panelCentral.setBackground(BACKGROUND_COLOR);

        // Panel izquierdo: Detalles de la sanción
        JPanel panelIzquierdo = createCardPanel("Datos de la Sanción");
        panelIzquierdo.setLayout(new GridBagLayout());
        GridBagConstraints gbcIzq = new GridBagConstraints();
        gbcIzq.fill = GridBagConstraints.HORIZONTAL;
        gbcIzq.insets = new Insets(5, 5, 5, 5);
        int yIzq = 0;

        // RU Usuario
        gbcIzq.gridx = 0; gbcIzq.gridy = yIzq; 
        panelIzquierdo.add(createStyledLabel("RU:"), gbcIzq);
        gbcIzq.gridx = 1; 
        txtRuUsuario = new JTextField(8);
        txtRuUsuario.setFont(new Font("Roboto", Font.PLAIN, 14));
        txtRuUsuario.setBorder(createRoundedBorder(BORDER_COLOR, 8));
        panelIzquierdo.add(txtRuUsuario, gbcIzq); 
        yIzq++;

        // ID Préstamo
        gbcIzq.gridx = 0; gbcIzq.gridy = yIzq; 
        panelIzquierdo.add(createStyledLabel("ID Préstamo:"), gbcIzq);
        gbcIzq.gridx = 1; 
        txtIdPrestamo = new JTextField(8);
        txtIdPrestamo.setFont(new Font("Roboto", Font.PLAIN, 14));
        txtIdPrestamo.setBorder(createRoundedBorder(BORDER_COLOR, 8));
        panelIzquierdo.add(txtIdPrestamo, gbcIzq); 
        yIzq++;

        // Tipo Sanción
        gbcIzq.gridx = 0; gbcIzq.gridy = yIzq; 
        panelIzquierdo.add(createStyledLabel("Tipo:"), gbcIzq);
        gbcIzq.gridx = 1; 
        cmbTipoSancion = new JComboBox<>(new String[]{"Retraso", "Daños", "Exceso", "Pérdida", "Otro"});
        styleComboBox(cmbTipoSancion);
        panelIzquierdo.add(cmbTipoSancion, gbcIzq); 
        yIzq++;

        // Descripción
        gbcIzq.gridx = 0; gbcIzq.gridy = yIzq; 
        panelIzquierdo.add(createStyledLabel("Descripción:"), gbcIzq);
        gbcIzq.gridx = 1; 
        txtDescripcion = new JTextArea(3, 15);
        txtDescripcion.setFont(new Font("Roboto", Font.PLAIN, 12));
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        txtDescripcion.setBorder(createRoundedBorder(BORDER_COLOR, 8));
        txtDescripcion.setBackground(CARD_COLOR);
        panelIzquierdo.add(txtDescripcion, gbcIzq); 
        yIzq++;

        // Fecha Sanción
        gbcIzq.gridx = 0; gbcIzq.gridy = yIzq; 
        panelIzquierdo.add(createStyledLabel("Falta:"), gbcIzq);
        gbcIzq.gridx = 1; 
        fechaSancion = new JDateChooser();
        fechaSancion.setDate(new Date());
        fechaSancion.setFont(new Font("Roboto", Font.PLAIN, 14));
        panelIzquierdo.add(fechaSancion, gbcIzq); 
        yIzq++;

        // Estado Sanción
        gbcIzq.gridx = 0; gbcIzq.gridy = yIzq; 
        panelIzquierdo.add(createStyledLabel("Estado:"), gbcIzq);
        gbcIzq.gridx = 1; 
        cmbEstadoSancion = new JComboBox<>(new String[]{"ACTIVA", "CUMPLIDA", "NO ACTIVA"});
        styleComboBox(cmbEstadoSancion);
        panelIzquierdo.add(cmbEstadoSancion, gbcIzq); 
        yIzq++;

        // Fecha Inicio
        gbcIzq.gridx = 0; gbcIzq.gridy = yIzq; 
        panelIzquierdo.add(createStyledLabel("Inicio:"), gbcIzq);
        gbcIzq.gridx = 1; 
        fechaInicio = new JDateChooser();
        fechaInicio.setDate(new Date());
        fechaInicio.setFont(new Font("Roboto", Font.PLAIN, 14));
        panelIzquierdo.add(fechaInicio, gbcIzq); 
        yIzq++;

        // Fecha Fin
        gbcIzq.gridx = 0; gbcIzq.gridy = yIzq; 
        panelIzquierdo.add(createStyledLabel("Fin:"), gbcIzq);
        gbcIzq.gridx = 1; 
        fechaFin = new JDateChooser();
        fechaFin.setFont(new Font("Roboto", Font.PLAIN, 14));
        panelIzquierdo.add(fechaFin, gbcIzq); 
        yIzq++;

        // Días Suspensión
        gbcIzq.gridx = 0; gbcIzq.gridy = yIzq; 
        panelIzquierdo.add(createStyledLabel("Días:"), gbcIzq);
        gbcIzq.gridx = 1; 
        spinnerDiasSuspension = new JSpinner(new SpinnerNumberModel(0, 0, 365, 1));
        spinnerDiasSuspension.setFont(new Font("Roboto", Font.PLAIN, 14));
        panelIzquierdo.add(spinnerDiasSuspension, gbcIzq); 
        yIzq++;

        // Panel derecho: Elementos afectados
        JPanel panelDerecho = createCardPanel("Elementos Afectados");
        panelDerecho.setLayout(new GridBagLayout());
        GridBagConstraints gbcDer = new GridBagConstraints();
        gbcDer.fill = GridBagConstraints.HORIZONTAL;
        gbcDer.insets = new Insets(5, 5, 5, 5);
        int yDer = 0;

        // Tipo de elemento afectado
        gbcDer.gridx = 0; gbcDer.gridy = yDer; 
        panelDerecho.add(createStyledLabel("Tipo:"), gbcDer);
        gbcDer.gridx = 1; 
        cmbTipoAfectado = new JComboBox<>(new String[]{"Equipo", "Equipamiento", "Insumo"});
        styleComboBox(cmbTipoAfectado);
        panelDerecho.add(cmbTipoAfectado, gbcDer);
        yDer++;

        // ID del elemento afectado
        gbcDer.gridx = 0; gbcDer.gridy = yDer; 
        panelDerecho.add(createStyledLabel("ID:"), gbcDer);
        gbcDer.gridx = 1; 
        txtIdAfectado = new JTextField(8);
        txtIdAfectado.setFont(new Font("Roboto", Font.PLAIN, 14));
        txtIdAfectado.setBorder(createRoundedBorder(BORDER_COLOR, 8));
        panelDerecho.add(txtIdAfectado, gbcDer);
        yDer++;

        // Cantidad afectada
        gbcDer.gridx = 0; gbcDer.gridy = yDer; 
        panelDerecho.add(createStyledLabel("Cantidad:"), gbcDer);
        gbcDer.gridx = 1; 
        spinnerCantidadAfectada = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));
        spinnerCantidadAfectada.setFont(new Font("Roboto", Font.PLAIN, 14));
        spinnerCantidadAfectada.setEnabled(false);
        panelDerecho.add(spinnerCantidadAfectada, gbcDer);
        yDer++;

        // Botón para agregar elemento afectado
        gbcDer.gridx = 0; gbcDer.gridy = yDer; gbcDer.gridwidth = 2; gbcDer.anchor = GridBagConstraints.CENTER;
        btnAgregarAfectado = createStyledButton("Agregar", PRIMARY_COLOR);
        panelDerecho.add(btnAgregarAfectado, gbcDer);
        yDer++;

        // Añadir paneles al panel central
        panelCentral.add(panelIzquierdo);
        panelCentral.add(panelDerecho);

        // Panel inferior para el botón Guardar
        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBoton.setBackground(BACKGROUND_COLOR);
        btnGuardar = createStyledButton("Guardar Sanción", SUCCESS_COLOR);
        panelBoton.add(btnGuardar);

        // Añadir componentes al panel principal
        add(panelCentral, BorderLayout.CENTER);
        add(panelBoton, BorderLayout.SOUTH);
    }

    /**
     * Configura los eventos de los componentes.
     */
    private void configurarEventos() {
        cmbTipoAfectado.addActionListener(e -> 
            spinnerCantidadAfectada.setEnabled(cmbTipoAfectado.getSelectedItem().equals("Insumo"))
        );
        btnAgregarAfectado.addActionListener(e -> agregarElementoAfectado());
        btnGuardar.addActionListener(e -> guardarSancion());
        spinnerDiasSuspension.addChangeListener(e -> calcularFechaFin());
    }

    /**
     * Calcula la fecha de fin según los días de suspensión.
     */
    private void calcularFechaFin() {
        int dias = (int) spinnerDiasSuspension.getValue();
        if (dias > 0 && fechaInicio.getDate() != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(fechaInicio.getDate());
            cal.add(Calendar.DAY_OF_MONTH, dias);
            fechaFin.setDate(cal.getTime());
        }
    }

    /**
     * Agrega un elemento afectado a la sanción.
     */
    private void agregarElementoAfectado() {
        String tipoSeleccionado = (String) cmbTipoAfectado.getSelectedItem();
        String idIngresado = txtIdAfectado.getText().trim();
        
        if (idIngresado.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar un ID", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            switch (tipoSeleccionado) {
                case "Equipamiento":
                    int idEquip = Integer.parseInt(idIngresado);
                    if (verificarExistenciaEquipamiento(idEquip)) {
                        idEquipamientoAfectado = idEquip;
                        idInsumoAfectado = null;
                        idEquipoAfectado = null;
                        elementoAfectadoAgregado = true;
                        JOptionPane.showMessageDialog(this, "Equipamiento agregado", 
                                "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        btnAgregarAfectado.setEnabled(false);
                    } else {
                        JOptionPane.showMessageDialog(this, "ID de equipamiento no existe", 
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    break;
                    
                case "Insumo":
                    int idInsumo = Integer.parseInt(idIngresado);
                    if (verificarExistenciaInsumo(idInsumo)) {
                        idInsumoAfectado = idInsumo;
                        idEquipamientoAfectado = null;
                        idEquipoAfectado = null;
                        cantidadInsumoAfectada = (int) spinnerCantidadAfectada.getValue();
                        elementoAfectadoAgregado = true;
                        JOptionPane.showMessageDialog(this, "Insumo agregado", 
                                "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        btnAgregarAfectado.setEnabled(false);
                    } else {
                        JOptionPane.showMessageDialog(this, "ID de insumo no existe", 
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    break;
                    
                case "Equipo":
                    if (verificarExistenciaEquipo(idIngresado)) {
                        idEquipoAfectado = idIngresado;
                        idEquipamientoAfectado = null;
                        idInsumoAfectado = null;
                        elementoAfectadoAgregado = true;
                        JOptionPane.showMessageDialog(this, "Equipo agregado", 
                                "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        btnAgregarAfectado.setEnabled(false);
                    } else {
                        JOptionPane.showMessageDialog(this, "ID de equipo no existe", 
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    break;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID debe ser numérico para equipamiento e insumo", 
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al verificar elemento", ex);
            JOptionPane.showMessageDialog(this, "Error al verificar: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Verifica si un equipamiento existe.
     */
    private boolean verificarExistenciaEquipamiento(int idEquipamiento) throws SQLException {
        return controladorEquipamiento.buscarPorId(idEquipamiento) != null;
    }

    /**
     * Verifica si un insumo existe.
     */
    private boolean verificarExistenciaInsumo(int idInsumo) throws SQLException {
        return controladorInsumo.buscarPorId(idInsumo) != null;
    }

    /**
     * Verifica si un equipo existe.
     */
    private boolean verificarExistenciaEquipo(String idEquipo) throws SQLException {
        return controladorEquipo.buscarPorId(idEquipo) != null;
    }

    /**
     * Guarda la sanción y los elementos afectados.
     */
    private void guardarSancion() {
        if (!validarDatosBasicos()) {
            return;
        }
        
        try {
            Sancion sancion = crearObjetoSancion();
            int idSancionGenerado = controladorSancion.insertar(sancion);
            
            if (idSancionGenerado > 0) {
                if (elementoAfectadoAgregado) {
                    registrarElementosAfectados(idSancionGenerado);
                }
                
                JOptionPane.showMessageDialog(this, "Sanción registrada con ID: " + idSancionGenerado, 
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarFormulario();
            } else {
                JOptionPane.showMessageDialog(this, "Error al registrar sanción", 
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error en la base de datos", ex);
            JOptionPane.showMessageDialog(this, "Error en la base de datos: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error inesperado", ex);
            JOptionPane.showMessageDialog(this, "Error inesperado: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Valida los datos básicos del formulario.
     */
    private boolean validarDatosBasicos() {
        if (txtRuUsuario.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el RU del usuario", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        try {
            Integer.parseInt(txtRuUsuario.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "RU debe ser un número", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (!txtIdPrestamo.getText().trim().isEmpty()) {
            try {
                Integer.parseInt(txtIdPrestamo.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "ID de préstamo debe ser un número", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        
        if (txtDescripcion.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese una descripción", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (fechaSancion.getDate() == null || fechaInicio.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Seleccione las fechas requeridas", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (cmbTipoSancion.getSelectedItem().equals("Daños") && !elementoAfectadoAgregado) {
            JOptionPane.showMessageDialog(this, "Agregue un elemento afectado para daños", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }

    /**
     * Crea un objeto Sancion con los datos del formulario.
     */
    private Sancion crearObjetoSancion() {
        int ruUsuario = Integer.parseInt(txtRuUsuario.getText().trim());
        Integer idPrestamo = null;
        if (!txtIdPrestamo.getText().trim().isEmpty()) {
            idPrestamo = Integer.parseInt(txtIdPrestamo.getText().trim());
        }
        
        String tipoSancion = (String) cmbTipoSancion.getSelectedItem();
        String descripcion = txtDescripcion.getText().trim();
        Date fechaSancionDate = fechaSancion.getDate();
        String estadoSancion = (String) cmbEstadoSancion.getSelectedItem();
        Date fechaInicioDate = fechaInicio.getDate();
        Date fechaFinDate = fechaFin.getDate();
        int diasSuspension = (int) spinnerDiasSuspension.getValue();
        
        return new Sancion(
            0, ruUsuario, idPrestamo, tipoSancion, descripcion,
            fechaSancionDate, estadoSancion, fechaInicioDate, fechaFinDate, diasSuspension
        );
    }

    /**
     * Registra los elementos afectados asociados a la sanción.
     */
    private void registrarElementosAfectados(int idSancion) throws SQLException {
        if (idEquipamientoAfectado != null) {
            SancionEquipamiento sancionEquip = new SancionEquipamiento(idSancion, idEquipamientoAfectado);
            controladorSancionEquipamiento.insertar(sancionEquip);
            actualizarDisponibilidadEquipamiento(idEquipamientoAfectado);
        }
        else if (idInsumoAfectado != null) {
            SancionInsumo sancionInsumo = new SancionInsumo(idSancion, idInsumoAfectado, cantidadInsumoAfectada);
            controladorSancionInsumo.insertar(sancionInsumo);
            actualizarCantidadInsumo(idInsumoAfectado, cantidadInsumoAfectada);
        }
        else if (idEquipoAfectado != null) {
            SancionEquipo sancionEquipo = new SancionEquipo(idSancion, idEquipoAfectado);
            controladorSancionEquipo.insertar(sancionEquipo);
            actualizarEstadoEquipo(idEquipoAfectado);
        }
    }

    /**
     * Actualiza la disponibilidad de un equipamiento.
     */
    private void actualizarDisponibilidadEquipamiento(int idEquipamiento) throws SQLException {
        controladorEquipamiento.actualizarDisponibilidad(idEquipamiento, "No Disponible");
    }

    /**
     * Actualiza la cantidad de un insumo.
     */
    private void actualizarCantidadInsumo(int idInsumo, int cantidadAfectada) throws SQLException {
        Clases.Insumo insumo = controladorInsumo.buscarPorId(idInsumo);
        if (insumo != null) {
            int nuevaCantidad = Math.max(0, insumo.getCantidad() - cantidadAfectada);
            controladorInsumo.actualizarCantidad(idInsumo, nuevaCantidad);
            if (nuevaCantidad == 0) {
                controladorInsumo.actualizarDisponibilidad(idInsumo, "No Disponible");
            }
        }
    }

    /**
     * Actualiza el estado de un equipo.
     */
    private void actualizarEstadoEquipo(String idEquipo) throws SQLException {
        Clases.Equipos equipo = controladorEquipo.buscarPorId(idEquipo);
        if (equipo != null) {
            equipo.setEstado("No Disponible");
            controladorEquipo.actualizar(equipo);
        }
    }

    /**
     * Limpia el formulario tras guardar una sanción.
     */
    private void limpiarFormulario() {
        txtRuUsuario.setText("");
        txtIdPrestamo.setText("");
        cmbTipoSancion.setSelectedIndex(0);
        txtDescripcion.setText("");
        fechaSancion.setDate(new Date());
        cmbEstadoSancion.setSelectedIndex(0);
        fechaInicio.setDate(new Date());
        fechaFin.setDate(null);
        spinnerDiasSuspension.setValue(0);
        cmbTipoAfectado.setSelectedIndex(0);
        txtIdAfectado.setText("");
        spinnerCantidadAfectada.setValue(1);
        spinnerCantidadAfectada.setEnabled(false);
        btnAgregarAfectado.setEnabled(true);
        idEquipamientoAfectado = null;
        idInsumoAfectado = null;
        idEquipoAfectado = null;
        cantidadInsumoAfectada = 0;
        elementoAfectadoAgregado = false;
    }

    // Métodos de estilo

    /**
     * Crea un panel con estilo de tarjeta compacto.
     */
    private JPanel createCardPanel(String title) {
        JPanel panel = new JPanel();
        panel.setBackground(CARD_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
            createRoundedBorder(BORDER_COLOR, 10),
            new EmptyBorder(10, 10, 10, 10)
        ));
        panel.setBorder(BorderFactory.createTitledBorder(
            panel.getBorder(), title,
            javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
            javax.swing.border.TitledBorder.DEFAULT_POSITION,
            new Font("Roboto", Font.BOLD, 14),
            PRIMARY_COLOR
        ));
        return panel;
    }

    /**
     * Crea un botón estilizado compacto.
     */
    private JButton createStyledButton(String text, Color baseColor) {
        RoundedButton button = new RoundedButton(text, baseColor);
        button.setFont(new Font("Roboto", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(baseColor);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(120, 30));
        button.setMaximumSize(new Dimension(120, 30));
        button.setMinimumSize(new Dimension(120, 30));
        button.setBorder(new EmptyBorder(5, 10, 5, 10));
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(baseColor.brighter());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(baseColor);
            }
        });
        return button;
    }

    /**
     * Crea un JLabel estilizado compacto.
     */
    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Roboto", Font.PLAIN, 14));
        label.setForeground(TEXT_COLOR);
        return label;
    }

    /**
     * Aplica estilo a un JComboBox compacto.
     */
    private void styleComboBox(JComboBox<String> comboBox) {
        comboBox.setFont(new Font("Roboto", Font.PLAIN, 14));
        comboBox.setBackground(CARD_COLOR);
        comboBox.setForeground(TEXT_COLOR);
        comboBox.setBorder(createRoundedBorder(BORDER_COLOR, 8));
        comboBox.setPreferredSize(new Dimension(120, 30));
        comboBox.setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton button = new JButton("▼");
                button.setFont(new Font("Roboto", Font.PLAIN, 10));
                button.setBackground(CARD_COLOR);
                button.setForeground(PRIMARY_COLOR);
                button.setBorder(createRoundedBorder(BORDER_COLOR, 8));
                button.setFocusPainted(false);
                return button;
            }
        });
    }

    /**
     * Crea un borde redondeado compacto.
     */
    private Border createRoundedBorder(Color color, int radius) {
        return new Border() {
            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
                g2.dispose();
            }

            @Override
            public Insets getBorderInsets(Component c) {
                return new Insets(2, 4, 2, 4);
            }

            @Override
            public boolean isBorderOpaque() {
                return false;
            }
        };
    }

    /**
     * Clase interna para botones redondeados compactos.
     */
    private class RoundedButton extends JButton {
        private Color backgroundColor;
        private int radius;

        public RoundedButton(String text, Color backgroundColor) {
            super(text);
            this.backgroundColor = backgroundColor;
            this.radius = 20;
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorder(BorderFactory.createEmptyBorder(3, 10, 3, 10));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(backgroundColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            super.paintComponent(g2);
            g2.dispose();
        }

        @Override
        protected void paintBorder(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getForeground());
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            g2.dispose();
        }
    }
}