/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Prestamos;

import Clases.Laboratorio;
import Clases.Horario;
import Clases.Equipamiento;
import Clases.Insumo;
import Controles.ControladorLaboratorio;
import Controles.ControladorHorario;
import Controles.ControladorPrestamo;
import Controles.ControladorEquipamiento;
import Controles.ControladorInsumo;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

public class PanelSolicitarPrestamo extends JPanel {
    private static final Logger LOGGER = Logger.getLogger(PanelSolicitarPrestamo.class.getName());
    private JComboBox<String> comboLaboratorios;
    private JComboBox<String> comboHorarios;
    private JTable tablaEquipamientos;
    private JTable tablaInsumos;
    private DefaultTableModel modeloEquipamientos;
    private DefaultTableModel modeloInsumos;
    private JTextField txtCantidadInsumo;
    private JButton btnAgregarEquipamiento;
    private JButton btnAgregarInsumo;
    private JButton btnSolicitar;
    private JTextArea txtObservaciones;
    private ControladorLaboratorio controladorLaboratorio;
    private ControladorHorario controladorHorario;
    private ControladorPrestamo controladorPrestamo;
    private ControladorEquipamiento controladorEquipamiento;
    private ControladorInsumo controladorInsumo;
    private int ruUsuario;
    private Map<Integer, Integer> insumoCantidades;

    public PanelSolicitarPrestamo(int ruUsuario) {
        this.ruUsuario = ruUsuario;
        controladorLaboratorio = new ControladorLaboratorio();
        controladorHorario = new ControladorHorario();
        controladorPrestamo = new ControladorPrestamo();
        controladorEquipamiento = new ControladorEquipamiento();
        controladorInsumo = new ControladorInsumo();
        insumoCantidades = new HashMap<>();
        try {
            initComponents();
        } catch (Exception ex) {
            LOGGER.severe("Error al inicializar PanelSolicitarPrestamo: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, "Error al cargar el panel: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 242, 245));

        // Título
        JLabel titleLabel = new JLabel("Solicitar Préstamo", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(81, 0, 255));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        // Panel principal con secciones
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(240, 242, 245));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Sección: Laboratorio y Horario
        JPanel sectionLabHorario = new JPanel(new GridBagLayout());
        sectionLabHorario.setBackground(Color.WHITE);
        sectionLabHorario.setBorder(BorderFactory.createTitledBorder("Selección de Laboratorio y Horario"));
        GridBagConstraints gbcSection = new GridBagConstraints();
        gbcSection.insets = new Insets(10, 10, 10, 10);
        gbcSection.fill = GridBagConstraints.HORIZONTAL;

        // Laboratorio
        gbcSection.gridx = 0;
        gbcSection.gridy = 0;
        JLabel lblLaboratorio = new JLabel("Laboratorio:");
        lblLaboratorio.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        sectionLabHorario.add(lblLaboratorio, gbcSection);

        gbcSection.gridx = 1;
        comboLaboratorios = new JComboBox<>();
        comboLaboratorios.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        comboLaboratorios.setPreferredSize(new Dimension(350, 35));
        sectionLabHorario.add(comboLaboratorios, gbcSection);

        // Horario
        gbcSection.gridx = 0;
        gbcSection.gridy = 1;
        JLabel lblHorario = new JLabel("Horario:");
        lblHorario.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        sectionLabHorario.add(lblHorario, gbcSection);

        gbcSection.gridx = 1;
        comboHorarios = new JComboBox<>();
        comboHorarios.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        comboHorarios.setPreferredSize(new Dimension(350, 35));
        sectionLabHorario.add(comboHorarios, gbcSection);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(sectionLabHorario, gbc);

        // Sección: Equipamientos
        modeloEquipamientos = new DefaultTableModel(new String[]{"ID", "Nombre", "Modelo", "Disponibilidad"}, 0);
        tablaEquipamientos = new JTable(modeloEquipamientos) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaEquipamientos.setRowHeight(30);
        tablaEquipamientos.setSelectionBackground(new Color(200, 220, 255));
        tablaEquipamientos.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tablaEquipamientos.setShowGrid(true);
        tablaEquipamientos.setGridColor(new Color(230, 230, 230));
        tablaEquipamientos.getColumnModel().getColumn(0).setPreferredWidth(50);
        tablaEquipamientos.getColumnModel().getColumn(1).setPreferredWidth(200);
        tablaEquipamientos.getColumnModel().getColumn(2).setPreferredWidth(150);
        tablaEquipamientos.getColumnModel().getColumn(3).setPreferredWidth(100);

        JScrollPane scrollEquipamientos = new JScrollPane(tablaEquipamientos);
        scrollEquipamientos.setPreferredSize(new Dimension(700, 250));
        scrollEquipamientos.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollEquipamientos.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        gbc.gridy = 1;
        JPanel sectionEquipamientos = new JPanel(new BorderLayout());
        sectionEquipamientos.setBackground(Color.WHITE);
        sectionEquipamientos.setBorder(BorderFactory.createTitledBorder("Equipamientos Disponibles"));
        sectionEquipamientos.add(scrollEquipamientos, BorderLayout.CENTER);

        btnAgregarEquipamiento = new JButton("Agregar Equipamiento");
        btnAgregarEquipamiento.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnAgregarEquipamiento.setBackground(new Color(25, 209, 49));
        btnAgregarEquipamiento.setForeground(Color.WHITE);
        btnAgregarEquipamiento.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnAgregarEquipamiento.setFocusPainted(false);
        btnAgregarEquipamiento.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAgregarEquipamiento.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnAgregarEquipamiento.setBackground(new Color(50, 230, 70));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnAgregarEquipamiento.setBackground(new Color(25, 209, 49));
            }
        });

        sectionEquipamientos.add(btnAgregarEquipamiento, BorderLayout.SOUTH);
        formPanel.add(sectionEquipamientos, gbc);

        // Sección: Insumos
        modeloInsumos = new DefaultTableModel(new String[]{"ID", "Insumo", "Cantidad Disponible", "Categoría"}, 0);
        tablaInsumos = new JTable(modeloInsumos) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaInsumos.setRowHeight(30);
        tablaInsumos.setSelectionBackground(new Color(200, 220, 255));
        tablaInsumos.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tablaInsumos.setShowGrid(true);
        tablaInsumos.setGridColor(new Color(230, 230, 230));
        tablaInsumos.getColumnModel().getColumn(0).setPreferredWidth(50);
        tablaInsumos.getColumnModel().getColumn(1).setPreferredWidth(200);
        tablaInsumos.getColumnModel().getColumn(2).setPreferredWidth(100);
        tablaInsumos.getColumnModel().getColumn(3).setPreferredWidth(100);

        JScrollPane scrollInsumos = new JScrollPane(tablaInsumos);
        scrollInsumos.setPreferredSize(new Dimension(700, 250));
        scrollInsumos.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollInsumos.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        gbc.gridy = 2;
        JPanel sectionInsumos = new JPanel(new BorderLayout());
        sectionInsumos.setBackground(Color.WHITE);
        sectionInsumos.setBorder(BorderFactory.createTitledBorder("Insumos Disponibles"));
        sectionInsumos.add(scrollInsumos, BorderLayout.CENTER);

        JPanel insumoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        insumoPanel.setBackground(Color.WHITE);
        JLabel lblCantidad = new JLabel("Cantidad:");
        lblCantidad.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        insumoPanel.add(lblCantidad);
        txtCantidadInsumo = new JTextField(6);
        txtCantidadInsumo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtCantidadInsumo.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { validarCantidad(); }
            public void removeUpdate(DocumentEvent e) { validarCantidad(); }
            public void changedUpdate(DocumentEvent e) { validarCantidad(); }
            private void validarCantidad() {
                String text = txtCantidadInsumo.getText().trim();
                try {
                    int cantidad = Integer.parseInt(text);
                    if (cantidad <= 0) {
                        txtCantidadInsumo.setBorder(BorderFactory.createLineBorder(Color.RED));
                    } else {
                        txtCantidadInsumo.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                    }
                } catch (NumberFormatException ex) {
                    txtCantidadInsumo.setBorder(BorderFactory.createLineBorder(Color.RED));
                }
            }
        });
        insumoPanel.add(txtCantidadInsumo);
        btnAgregarInsumo = new JButton("Agregar Insumo");
        btnAgregarInsumo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnAgregarInsumo.setBackground(new Color(25, 209, 49));
        btnAgregarInsumo.setForeground(Color.WHITE);
        btnAgregarInsumo.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnAgregarInsumo.setFocusPainted(false);
        btnAgregarInsumo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAgregarInsumo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnAgregarInsumo.setBackground(new Color(50, 230, 70));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnAgregarInsumo.setBackground(new Color(25, 209, 49));
            }
        });
        insumoPanel.add(btnAgregarInsumo);

        sectionInsumos.add(insumoPanel, BorderLayout.SOUTH);
        formPanel.add(sectionInsumos, gbc);

        // Sección: Observaciones
        gbc.gridy = 3;
        JPanel sectionObservaciones = new JPanel(new BorderLayout());
        sectionObservaciones.setBackground(Color.WHITE);
        sectionObservaciones.setBorder(BorderFactory.createTitledBorder("Observaciones"));
        txtObservaciones = new JTextArea(5, 20);
        txtObservaciones.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtObservaciones.setLineWrap(true);
        txtObservaciones.setWrapStyleWord(true);
        JScrollPane scrollObservaciones = new JScrollPane(txtObservaciones);
        sectionObservaciones.add(scrollObservaciones, BorderLayout.CENTER);
        formPanel.add(sectionObservaciones, gbc);

        // Botón Solicitar
        btnSolicitar = new JButton("Solicitar Préstamo");
        btnSolicitar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnSolicitar.setBackground(new Color(81, 0, 255));
        btnSolicitar.setForeground(Color.WHITE);
        btnSolicitar.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
        btnSolicitar.setFocusPainted(false);
        btnSolicitar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSolicitar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSolicitar.setBackground(new Color(100, 20, 255));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSolicitar.setBackground(new Color(81, 0, 255));
            }
        });

        gbc.gridy = 4;
        formPanel.add(btnSolicitar, gbc);

        // Envolver el formPanel en un JScrollPane para pantallas pequeñas
        JScrollPane mainScroll = new JScrollPane(formPanel);
        mainScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        mainScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        mainScroll.setBorder(null);
        add(mainScroll, BorderLayout.CENTER);

        // Cargar datos iniciales con manejo de errores
        try {
            cargarLaboratorios();
        } catch (Exception ex) {
            LOGGER.severe("Error al cargar laboratorios iniciales: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, "No se pudieron cargar los laboratorios: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Listeners para los botones
        comboLaboratorios.addActionListener(e -> {
            if (comboLaboratorios.getItemCount() > 0 && comboLaboratorios.getSelectedItem() != null) {
                try {
                    cargarHorarios();
                } catch (Exception ex) {
                    LOGGER.severe("Error al cargar horarios: " + ex.getMessage());
                    JOptionPane.showMessageDialog(this, "Error al cargar horarios: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnAgregarEquipamiento.addActionListener(e -> {
            try {
                agregarEquipamiento();
            } catch (Exception ex) {
                LOGGER.severe("Error al agregar equipamiento: " + ex.getMessage());
                JOptionPane.showMessageDialog(this, "Error al agregar equipamiento: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnAgregarInsumo.addActionListener(e -> {
            try {
                agregarInsumo();
            } catch (Exception ex) {
                LOGGER.severe("Error al agregar insumo: " + ex.getMessage());
                JOptionPane.showMessageDialog(this, "Error al agregar insumo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnSolicitar.addActionListener(e -> {
            try {
                solicitarPrestamo();
            } catch (Exception ex) {
                LOGGER.severe("Error al solicitar préstamo: " + ex.getMessage());
                JOptionPane.showMessageDialog(this, "Error al solicitar préstamo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void cargarLaboratorios() {
        try {
            List<Laboratorio> laboratorios = controladorLaboratorio.listar();
            comboLaboratorios.removeAllItems();
            if (laboratorios.isEmpty()) {
                System.out.println("No hay laboratorios registrados en la base de datos.");
                JOptionPane.showMessageDialog(this, "No hay laboratorios registrados.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
            for (Laboratorio lab : laboratorios) {
                comboLaboratorios.addItem(lab.getIdLaboratorio() + " - " + lab.getUbicacion());
            }
            System.out.println("Laboratorios cargados: " + laboratorios.size());
        } catch (SQLException ex) {
            LOGGER.severe("Error al cargar laboratorios: " + ex.getMessage());
            throw new RuntimeException("Error al cargar laboratorios", ex);
        }
    }

    private void cargarHorarios() {
        try {
            String selectedLab = (String) comboLaboratorios.getSelectedItem();
            if (selectedLab == null) {
                System.out.println("No se seleccionó ningún laboratorio.");
                JOptionPane.showMessageDialog(this, "Seleccione un laboratorio primero.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int idLaboratorio = Integer.parseInt(selectedLab.split(" - ")[0]);
            List<Horario> horarios = controladorHorario.listarPorLaboratorio(idLaboratorio);
            comboHorarios.removeAllItems();
            if (horarios.isEmpty()) {
                System.out.println("No hay horarios disponibles para el laboratorio ID: " + idLaboratorio);
                JOptionPane.showMessageDialog(this, "No hay horarios disponibles para este laboratorio.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
            for (Horario h : horarios) {
                comboHorarios.addItem(h.getIdHorario() + " - " + h.getDia() + " " + h.getHora());
            }
            System.out.println("Horarios cargados: " + horarios.size());
            cargarEquipamientos(idLaboratorio);
            cargarInsumos(idLaboratorio);
        } catch (SQLException ex) {
            LOGGER.severe("Error al cargar horarios: " + ex.getMessage());
            throw new RuntimeException("Error al cargar horarios", ex);
        }
    }

    private void cargarEquipamientos(int idLaboratorio) {
        try {
            List<Equipamiento> equipamientos = controladorEquipamiento.listarPorLaboratorio(idLaboratorio);
            modeloEquipamientos.setRowCount(0);
            if (equipamientos.isEmpty()) {
                System.out.println("No hay equipamientos registrados para el laboratorio ID: " + idLaboratorio);
                JOptionPane.showMessageDialog(this, "No hay equipamientos registrados para este laboratorio.", "Información", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            int disponibles = 0;
            for (Equipamiento e : equipamientos) {
                if ("Disponible".equalsIgnoreCase(e.getDisponibilidad())) {
                    modeloEquipamientos.addRow(new Object[]{
                        e.getIdEquipamiento(),
                        e.getNombreEquipamiento(),
                        e.getModelo(),
                        e.getDisponibilidad()
                    });
                    disponibles++;
                }
            }
            System.out.println("Equipamientos disponibles cargados: " + disponibles);
            if (disponibles == 0) {
                JOptionPane.showMessageDialog(this, "No hay equipamientos disponibles para este laboratorio.", "Información", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            LOGGER.severe("Error al cargar equipamientos: " + ex.getMessage());
            throw new RuntimeException("Error al cargar equipamientos", ex);
        }
    }

    private void cargarInsumos(int idLaboratorio) {
        try {
            List<Insumo> insumos = controladorInsumo.listarPorLaboratorio(idLaboratorio);
            modeloInsumos.setRowCount(0);
            if (insumos.isEmpty()) {
                System.out.println("No hay insumos registrados para el laboratorio ID: " + idLaboratorio);
                JOptionPane.showMessageDialog(this, "No hay insumos registrados para este laboratorio.", "Información", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            int disponibles = 0;
            for (Insumo i : insumos) {
                if ("Disponible".equalsIgnoreCase(i.getDisponibilidad()) && i.getCantidad() > 0) {
                    modeloInsumos.addRow(new Object[]{
                        i.getIdInsumo(),
                        i.getNombreInsumo(),
                        i.getCantidad(),
                        i.getCategoria()
                    });
                    disponibles++;
                }
            }
            System.out.println("Insumos disponibles cargados: " + disponibles);
            if (disponibles == 0) {
                JOptionPane.showMessageDialog(this, "No hay insumos disponibles (cantidad > 0 y estado 'Disponible') para este laboratorio.", "Información", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            LOGGER.severe("Error al cargar insumos: " + ex.getMessage());
            throw new RuntimeException("Error al cargar insumos", ex);
        }
    }

    private void agregarEquipamiento() {
        int selectedRow = tablaEquipamientos.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un equipamiento.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int idEquipamiento = (int) modeloEquipamientos.getValueAt(selectedRow, 0);
        modeloEquipamientos.removeRow(selectedRow);
        insumoCantidades.computeIfAbsent(idEquipamiento, k -> 0);
        System.out.println(" Equipamiento agregado: ID " + idEquipamiento);
    }

    private void agregarInsumo() {
        int selectedRow = tablaInsumos.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un insumo.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String cantidadStr = txtCantidadInsumo.getText().trim();
        int cantidad;
        try {
            cantidad = Integer.parseInt(cantidadStr);
            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a 0.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese una cantidad válida.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int idInsumo = (int) modeloInsumos.getValueAt(selectedRow, 0);
        int cantidadDisponible = (int) modeloInsumos.getValueAt(selectedRow, 2);
        if (cantidad > cantidadDisponible) {
            JOptionPane.showMessageDialog(this, "Cantidad solicitada excede la disponible.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        insumoCantidades.put(idInsumo, cantidad);
        modeloInsumos.setValueAt(cantidadDisponible - cantidad, selectedRow, 2);
        txtCantidadInsumo.setText("");
        System.out.println("Insumo agregado: ID " + idInsumo + ", Cantidad: " + cantidad);
    }

    private void solicitarPrestamo() {
        System.out.println("Iniciando solicitud de préstamo");
        String selectedHorario = (String) comboHorarios.getSelectedItem();
        if (selectedHorario == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un horario.", "Error", JOptionPane.ERROR_MESSAGE);
            System.out.println("Error: No se seleccionó un horario.");
            return;
        }
        if (insumoCantidades.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Agregue al menos un equipamiento o insumo.", "Error", JOptionPane.ERROR_MESSAGE);
            System.out.println("Error: No se agregaron equipamientos ni insumos.");
            return;
        }

        try {
            // Validar ruUsuario
            System.out.println("Validando ruUsuario: " + ruUsuario);
            if (ruUsuario <= 0) {
                throw new IllegalArgumentException("RU de usuario inválido: " + ruUsuario);
            }
            // Verificar si el usuario existe en la base de datos
            if (!controladorPrestamo.existeUsuario(ruUsuario)) {
                throw new SQLException("El usuario con RU " + ruUsuario + " no está registrado en la base de datos.");
            }
            System.out.println("Usuario validado: RU " + ruUsuario);

            // Parsear idHorario
            System.out.println("Horario seleccionado: " + selectedHorario);
            int idHorario;
            try {
                idHorario = Integer.parseInt(selectedHorario.split(" - ")[0]);
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException ex) {
                throw new IllegalArgumentException("ID de horario inválido: " + selectedHorario, ex);
            }
            System.out.println("ID de horario parseado: " + idHorario);

            // Validar txtObservaciones
            String observaciones = txtObservaciones != null ? txtObservaciones.getText() : "";
            System.out.println("Observaciones: " + observaciones);

            // Crear objeto Prestamo
            System.out.println("Creando objeto Prestamo");
            java.util.Date currentDate = new java.util.Date();
            String horaPrestamo = new SimpleDateFormat("HH:mm").format(currentDate);
            System.out.println("Fecha de préstamo: " + new java.sql.Date(currentDate.getTime()));
            System.out.println("Hora de préstamo: " + horaPrestamo);
            Clases.Prestamo prestamo = new Clases.Prestamo(
                0, // idPrestamo
                ruUsuario,
                null, // ruAdministrador
                idHorario,
                new java.sql.Date(currentDate.getTime()),
                horaPrestamo,
                "Pendiente",
                observaciones
            );
            System.out.println("Objeto Prestamo creado: " + prestamo);

            // Insertar préstamo
            System.out.println("Insertando préstamo en la base de datos");
            int idPrestamo = controladorPrestamo.insertar(prestamo);
            if (idPrestamo == -1) {
                throw new SQLException("No se pudo registrar el préstamo en la base de datos.");
            }
            System.out.println("Préstamo registrado con ID: " + idPrestamo);

            // Procesar equipamientos e insumos
            System.out.println("Procesando equipamientos e insumos");
            for (Map.Entry<Integer, Integer> entry : insumoCantidades.entrySet()) {
                int id = entry.getKey();
                int cantidad = entry.getValue();
                if (cantidad == 0) {
                    // Es un equipamiento
                    System.out.println("Insertando detalle de equipamiento: ID " + id);
                    controladorPrestamo.insertarDetalleEquipamiento(idPrestamo, id);
                    // No actualizamos la disponibilidad aquí; se hará al aceptar el préstamo
                } else {
                    // Es un insumo
                    System.out.println("Insertando detalle de insumo: ID " + id + ", Cantidad: " + cantidad);
                    controladorPrestamo.insertarDetalleInsumo(idPrestamo, id, cantidad);
                    // No actualizamos la cantidad aquí; se hará al aceptar el préstamo
                }
            }

            System.out.println("Mostrando mensaje de éxito");
            JOptionPane.showMessageDialog(this, "Préstamo solicitado con éxito. ID: " + idPrestamo, "Éxito", JOptionPane.INFORMATION_MESSAGE);
            System.out.println("Préstamo solicitado con éxito.");

            // Limpiar el formulario
            insumoCantidades.clear();
            txtObservaciones.setText("");
            comboHorarios.setSelectedIndex(-1);
            cargarInsumos(Integer.parseInt(((String) comboLaboratorios.getSelectedItem()).split(" - ")[0]));
            cargarEquipamientos(Integer.parseInt(((String) comboLaboratorios.getSelectedItem()).split(" - ")[0]));
        } catch (SQLException ex) {
            LOGGER.severe("Error SQL al solicitar préstamo: " + ex.getMessage());
            System.err.println("Error SQL: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, "Error en la base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            LOGGER.severe("Error de validación al solicitar préstamo: " + ex.getMessage());
            System.err.println("Error de validación: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, "Error de validación: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            LOGGER.severe("Error inesperado al solicitar préstamo: " + ex.getMessage());
            System.err.println("Error inesperado: " + ex.getMessage());
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error inesperado: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}