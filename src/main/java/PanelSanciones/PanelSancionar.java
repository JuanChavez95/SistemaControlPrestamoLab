/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
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
import DataBase.ConexionBD;

import com.toedter.calendar.JDateChooser;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Date;
import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PanelSancionar extends JPanel {

    private JTextField txtRuUsuario;
    private JTextField txtIdPrestamo;
    private JComboBox<String> cmbTipoSancion;
    private JTextArea txtDescripcion;
    private JDateChooser fechaSancion;
    private JComboBox<String> cmbEstadoSancion;
    private JDateChooser fechaInicio;
    private JDateChooser fechaFin;
    private JSpinner spinnerDiasSuspension;

    private JPanel panelAfectado;
    private JComboBox<String> cmbTipoAfectado;
    private JTextField txtIdAfectado;
    private JSpinner spinnerCantidadAfectada;
    private JButton btnAgregarAfectado;
    private JButton btnGuardar;
    
    // Variables para almacenar los IDs de elementos afectados
    private Integer idEquipamientoAfectado = null;
    private Integer idInsumoAfectado = null;
    private String idEquipoAfectado = null;
    private Integer cantidadInsumoAfectada = 0;
    
    // Flag para comprobar si se ha agregado un elemento afectado
    private boolean elementoAfectadoAgregado = false;

    // Controladores
    private ControladorSancion controladorSancion;
    private ControladorSancionEquipamiento controladorSancionEquipamiento;
    private ControladorSancionInsumo controladorSancionInsumo;
    private ControladorSancionEquipo controladorSancionEquipo;
    private ControladorEquipamiento controladorEquipamiento;
    private ControladorInsumo controladorInsumo;
    private ControladorEquipo controladorEquipo;

    public PanelSancionar() {
        inicializarControladores();
        inicializarUI();
        configurarEventos();
    }
    
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
            JOptionPane.showMessageDialog(this, 
                "Error al inicializar controladores: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void inicializarUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);

        // Título
        JLabel titleLabel = new JLabel("Sancionar Usuarios");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(titleLabel, BorderLayout.NORTH);

        // Panel principal
        JPanel panelPrincipal = new JPanel(new GridBagLayout());
        panelPrincipal.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        int y = 0;

        // RU Usuario
        gbc.gridx = 0; gbc.gridy = y; panelPrincipal.add(new JLabel("RU del Usuario:"), gbc);
        gbc.gridx = 1; txtRuUsuario = new JTextField(10); panelPrincipal.add(txtRuUsuario, gbc); y++;

        // ID Préstamo
        gbc.gridx = 0; gbc.gridy = y; panelPrincipal.add(new JLabel("ID Préstamo (opcional):"), gbc);
        gbc.gridx = 1; txtIdPrestamo = new JTextField(10); panelPrincipal.add(txtIdPrestamo, gbc); y++;

        // Tipo Sanción
        gbc.gridx = 0; gbc.gridy = y; panelPrincipal.add(new JLabel("Tipo de Sanción:"), gbc);
        gbc.gridx = 1; cmbTipoSancion = new JComboBox<>(new String[]{"Retraso", "Daños", "Exceso de insumos", "Pérdida", "Otro"});
        panelPrincipal.add(cmbTipoSancion, gbc); y++;

        // Descripción
        gbc.gridx = 0; gbc.gridy = y; panelPrincipal.add(new JLabel("Descripción:"), gbc);
        gbc.gridx = 1;
        txtDescripcion = new JTextArea(4, 20);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        JScrollPane scrollDescripcion = new JScrollPane(txtDescripcion);
        panelPrincipal.add(scrollDescripcion, gbc); y++;

        // Fecha Sanción
        gbc.gridx = 0; gbc.gridy = y; panelPrincipal.add(new JLabel("Fecha de la Falta:"), gbc);
        gbc.gridx = 1; fechaSancion = new JDateChooser(); fechaSancion.setDate(new Date()); panelPrincipal.add(fechaSancion, gbc); y++;

        // Estado Sanción
        gbc.gridx = 0; gbc.gridy = y; panelPrincipal.add(new JLabel("Estado de Sanción:"), gbc);
        gbc.gridx = 1; cmbEstadoSancion = new JComboBox<>(new String[]{"ACTIVA", "CUMPLIDA", "NO ACTIVA"});
        panelPrincipal.add(cmbEstadoSancion, gbc); y++;

        // Fecha Inicio
        gbc.gridx = 0; gbc.gridy = y; panelPrincipal.add(new JLabel("Fecha de Inicio:"), gbc);
        gbc.gridx = 1; fechaInicio = new JDateChooser(); fechaInicio.setDate(new Date()); panelPrincipal.add(fechaInicio, gbc); y++;

        // Fecha Fin
        gbc.gridx = 0; gbc.gridy = y; panelPrincipal.add(new JLabel("Fecha de Fin:"), gbc);
        gbc.gridx = 1; fechaFin = new JDateChooser(); panelPrincipal.add(fechaFin, gbc); y++;

        // Días Suspensión
        gbc.gridx = 0; gbc.gridy = y; panelPrincipal.add(new JLabel("Días de Suspensión:"), gbc);
        gbc.gridx = 1; spinnerDiasSuspension = new JSpinner(new SpinnerNumberModel(0, 0, 365, 1));
        panelPrincipal.add(spinnerDiasSuspension, gbc); y++;

        // Panel elementos afectados
        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.BOTH;
        crearPanelElementosAfectados();
        panelPrincipal.add(panelAfectado, gbc); y++;

        // Botón guardar
        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        btnGuardar = new JButton("Guardar Sanción");
        panelPrincipal.add(btnGuardar, gbc);

        JScrollPane scrollPane = new JScrollPane(panelPrincipal);
        add(scrollPane, BorderLayout.CENTER);
        
        // Calcular fecha fin automáticamente
        spinnerDiasSuspension.addChangeListener(e -> calcularFechaFin());
    }

    private void crearPanelElementosAfectados() {
        panelAfectado = new JPanel(new GridBagLayout());
        panelAfectado.setBorder(BorderFactory.createTitledBorder("Elementos Afectados"));
        panelAfectado.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0; panelAfectado.add(new JLabel("Tipo:"), gbc);
        gbc.gridx = 1;
        cmbTipoAfectado = new JComboBox<>(new String[]{"Equipo", "Equipamiento", "Insumo"});
        panelAfectado.add(cmbTipoAfectado, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panelAfectado.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1; txtIdAfectado = new JTextField(10); panelAfectado.add(txtIdAfectado, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panelAfectado.add(new JLabel("Cantidad:"), gbc);
        gbc.gridx = 1; spinnerCantidadAfectada = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));
        spinnerCantidadAfectada.setEnabled(false); panelAfectado.add(spinnerCantidadAfectada, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        btnAgregarAfectado = new JButton("Agregar a la Sanción");
        panelAfectado.add(btnAgregarAfectado, gbc);
    }
    
    private void configurarEventos() {
        // Activar/desactivar spinner cantidad según tipo afectado
        cmbTipoAfectado.addActionListener(e -> 
            spinnerCantidadAfectada.setEnabled(cmbTipoAfectado.getSelectedItem().equals("Insumo"))
        );
        
        // Evento para agregar elemento afectado
        btnAgregarAfectado.addActionListener(e -> agregarElementoAfectado());
        
        // Evento para guardar la sanción
        btnGuardar.addActionListener(e -> guardarSancion());
    }
    
    private void calcularFechaFin() {
        int dias = (int) spinnerDiasSuspension.getValue();
        if (dias > 0 && fechaInicio.getDate() != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(fechaInicio.getDate());
            cal.add(Calendar.DAY_OF_MONTH, dias);
            fechaFin.setDate(cal.getTime());
        }
    }
    
    private void agregarElementoAfectado() {
        String tipoSeleccionado = (String) cmbTipoAfectado.getSelectedItem();
        String idIngresado = txtIdAfectado.getText().trim();
        
        if (idIngresado.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar un ID para el elemento afectado", 
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
                        JOptionPane.showMessageDialog(this, "Equipamiento agregado correctamente", 
                                "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        btnAgregarAfectado.setEnabled(false);
                    } else {
                        JOptionPane.showMessageDialog(this, "El ID de equipamiento no existe", 
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
                        JOptionPane.showMessageDialog(this, "Insumo agregado correctamente", 
                                "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        btnAgregarAfectado.setEnabled(false);
                    } else {
                        JOptionPane.showMessageDialog(this, "El ID de insumo no existe", 
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    break;
                    
                case "Equipo":
                    if (verificarExistenciaEquipo(idIngresado)) {
                        idEquipoAfectado = idIngresado;
                        idEquipamientoAfectado = null;
                        idInsumoAfectado = null;
                        elementoAfectadoAgregado = true;
                        JOptionPane.showMessageDialog(this, "Equipo agregado correctamente", 
                                "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        btnAgregarAfectado.setEnabled(false);
                    } else {
                        JOptionPane.showMessageDialog(this, "El ID de equipo no existe", 
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    break;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El ID debe ser un número válido para equipamiento e insumo", 
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al verificar elemento: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private boolean verificarExistenciaEquipamiento(int idEquipamiento) throws SQLException {
        return controladorEquipamiento.buscarPorId(idEquipamiento) != null;
    }
    
    private boolean verificarExistenciaInsumo(int idInsumo) throws SQLException {
        return controladorInsumo.buscarPorId(idInsumo) != null;
    }
    
    private boolean verificarExistenciaEquipo(String idEquipo) throws SQLException {
        return controladorEquipo.buscarPorId(idEquipo) != null;
    }
    
    private void guardarSancion() {
        // Validar datos básicos
        if (!validarDatosBasicos()) {
            return;
        }
        
        try {
            // Crear objeto sanción
            Sancion sancion = crearObjetoSancion();
            
            // Insertar la sanción principal y obtener el ID generado
            int idSancionGenerado = controladorSancion.insertar(sancion);
            
            if (idSancionGenerado > 0) {
                // Si hay elementos afectados, registrarlos
                if (elementoAfectadoAgregado) {
                    registrarElementosAfectados(idSancionGenerado);
                }
                
                JOptionPane.showMessageDialog(this, "Sanción registrada correctamente con ID: " + idSancionGenerado, 
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarFormulario();
            } else {
                JOptionPane.showMessageDialog(this, "Error al registrar la sanción", 
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error en la base de datos: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error inesperado: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private boolean validarDatosBasicos() {
        // Validar RU Usuario
        if (txtRuUsuario.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar el RU del usuario", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        try {
            Integer.parseInt(txtRuUsuario.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El RU del usuario debe ser un número", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Validar ID Préstamo si se ingresa
        if (!txtIdPrestamo.getText().trim().isEmpty()) {
            try {
                Integer.parseInt(txtIdPrestamo.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "El ID de préstamo debe ser un número", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        
        // Validar descripción
        if (txtDescripcion.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar una descripción", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Validar fechas
        if (fechaSancion.getDate() == null || fechaInicio.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar las fechas requeridas", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Si el tipo de sanción es "Daños" y no hay elementos afectados
        if (cmbTipoSancion.getSelectedItem().equals("Daños") && !elementoAfectadoAgregado) {
            JOptionPane.showMessageDialog(this, "Para sanciones por daños debe agregar un elemento afectado", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    private Sancion crearObjetoSancion() {
        // Crear objeto sanción con los datos del formulario
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
        
        // Crear el objeto con ID 0 (será asignado por la BD)
        return new Sancion(
            0, // ID temporal, será asignado por la base de datos
            ruUsuario,
            idPrestamo,
            tipoSancion,
            descripcion,
            fechaSancionDate,
            estadoSancion,
            fechaInicioDate,
            fechaFinDate,
            diasSuspension
        );
    }
    
    private void registrarElementosAfectados(int idSancion) throws SQLException {
        // Registrar elementos afectados según tipo
        if (idEquipamientoAfectado != null) {
            // Registrar sanción de equipamiento
            SancionEquipamiento sancionEquip = new SancionEquipamiento(idSancion, idEquipamientoAfectado);
            controladorSancionEquipamiento.insertar(sancionEquip);
            
            // Actualizar disponibilidad del equipamiento a "No Disponible"
            actualizarDisponibilidadEquipamiento(idEquipamientoAfectado);
        }
        else if (idInsumoAfectado != null) {
            // Registrar sanción de insumo
            SancionInsumo sancionInsumo = new SancionInsumo(idSancion, idInsumoAfectado, cantidadInsumoAfectada);
            controladorSancionInsumo.insertar(sancionInsumo);
            
            // Actualizar cantidad de insumo
            actualizarCantidadInsumo(idInsumoAfectado, cantidadInsumoAfectada);
        }
        else if (idEquipoAfectado != null) {
            // Registrar sanción de equipo
            SancionEquipo sancionEquipo = new SancionEquipo(idSancion, idEquipoAfectado);
            controladorSancionEquipo.insertar(sancionEquipo);
            
            // Actualizar estado del equipo a "No Disponible"
            actualizarEstadoEquipo(idEquipoAfectado);
        }
    }
    
    private void actualizarDisponibilidadEquipamiento(int idEquipamiento) throws SQLException {
        controladorEquipamiento.actualizarDisponibilidad(idEquipamiento, "No Disponible");
    }
    
    private void actualizarCantidadInsumo(int idInsumo, int cantidadAfectada) throws SQLException {
        // Obtener insumo actual
        Clases.Insumo insumo = controladorInsumo.buscarPorId(idInsumo);
        if (insumo != null) {
            // Calcular nueva cantidad (restar la cantidad afectada)
            int nuevaCantidad = Math.max(0, insumo.getCantidad() - cantidadAfectada);
            controladorInsumo.actualizarCantidad(idInsumo, nuevaCantidad);
            
            // Si la cantidad llega a 0, marcar como no disponible
            if (nuevaCantidad == 0) {
                controladorInsumo.actualizarDisponibilidad(idInsumo, "No Disponible");
            }
        }
    }
    
    private void actualizarEstadoEquipo(String idEquipo) throws SQLException {
        // Obtener equipo actual
        Clases.Equipos equipo = controladorEquipo.buscarPorId(idEquipo);
        if (equipo != null) {
            equipo.setEstado("No Disponible");
            controladorEquipo.actualizar(equipo);
        }
    }
    
    private void limpiarFormulario() {
        // Limpiar campos después de guardar
        txtRuUsuario.setText("");
        txtIdPrestamo.setText("");
        cmbTipoSancion.setSelectedIndex(0);
        txtDescripcion.setText("");
        fechaSancion.setDate(new Date());
        cmbEstadoSancion.setSelectedIndex(0);
        fechaInicio.setDate(new Date());
        fechaFin.setDate(null);
        spinnerDiasSuspension.setValue(0);
        
        // Limpiar elementos afectados
        cmbTipoAfectado.setSelectedIndex(0);
        txtIdAfectado.setText("");
        spinnerCantidadAfectada.setValue(1);
        spinnerCantidadAfectada.setEnabled(false);
        btnAgregarAfectado.setEnabled(true);
        
        // Reiniciar variables
        idEquipamientoAfectado = null;
        idInsumoAfectado = null;
        idEquipoAfectado = null;
        cantidadInsumoAfectada = 0;
        elementoAfectadoAgregado = false;
    }
}