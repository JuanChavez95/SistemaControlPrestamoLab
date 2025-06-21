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
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.plaf.basic.BasicComboBoxUI;

// Custom RoundedBorder class for rounded corners
class RoundedBorder implements javax.swing.border.Border {
    private int radius;

    public RoundedBorder(int radius) {
        this.radius = radius;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(c.getForeground());
        g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        g2.dispose();
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(radius / 2, radius / 2, radius / 2, radius / 2);
    }

    @Override
    public boolean isBorderOpaque() {
        return false;
    }
}

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
    private JList<String> listaEquipamientosSeleccionados;
    private JList<String> listaInsumosSeleccionados;
    private DefaultListModel<String> modeloListaEquipamientos;
    private DefaultListModel<String> modeloListaInsumos;
    private ControladorLaboratorio controladorLaboratorio;
    private ControladorHorario controladorHorario;
    private ControladorPrestamo controladorPrestamo;
    private ControladorEquipamiento controladorEquipamiento;
    private ControladorInsumo controladorInsumo;
    private int ruUsuario;
    private Map<Integer, Integer> insumoCantidades;

    // Updated Color Palette
    private static final Color PRIMARY_COLOR = new Color(2, 136, 209); // Vibrant Blue
    private static final Color SECONDARY_COLOR = new Color(38, 166, 154); // Vibrant Teal
    private static final Color BACKGROUND_COLOR = Color.WHITE; // Pure White
    private static final Color CARD_COLOR = Color.WHITE; // Pure White
    private static final Color TEXT_COLOR = new Color(33, 33, 33); // Dark Gray
    private static final Color BORDER_COLOR = new Color(189, 189, 189); // Lighter Gray
    private static final Color ACCENT_COLOR = new Color(79, 195, 247); // Vivid Light Blue
    private static final Color AVAILABLE_COLOR = new Color(76, 175, 80); // Vibrant Green
    private static final Color UNAVAILABLE_COLOR = new Color(244, 67, 54); // Vibrant Red
    private static final Color REMOVE_BUTTON_COLOR = new Color(211, 47, 47); // Brighter Red

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
        setBackground(BACKGROUND_COLOR);
        setBorder(new EmptyBorder(10, 10, 10, 10)); // Reduced padding

        // Título
        JLabel titleLabel = new JLabel("Solicitar Préstamo", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 24)); // Smaller font
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setBorder(new EmptyBorder(10, 0, 15, 0)); // Reduced padding
        add(titleLabel, BorderLayout.NORTH);

        // Panel principal
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(BACKGROUND_COLOR);
        formPanel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Reduced padding
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Reduced insets
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;

        // Sección: Laboratorio y Horario
        JPanel sectionLabHorario = createCardPanel("Selección de Laboratorio y Horario");
        sectionLabHorario.setLayout(new GridBagLayout());
        GridBagConstraints gbcSection = new GridBagConstraints();
        gbcSection.insets = new Insets(5, 5, 5, 5); // Reduced insets
        gbcSection.fill = GridBagConstraints.HORIZONTAL;
        gbcSection.weightx = 1.0;

        gbcSection.gridx = 0;
        gbcSection.gridy = 0;
        JLabel lblLaboratorio = new JLabel("Laboratorio:");
        lblLaboratorio.setFont(new Font("Roboto", Font.PLAIN, 14)); // Smaller font
        lblLaboratorio.setForeground(TEXT_COLOR);
        sectionLabHorario.add(lblLaboratorio, gbcSection);

        gbcSection.gridx = 1;
        comboLaboratorios = new JComboBox<>();
        styleComboBox(comboLaboratorios);
        sectionLabHorario.add(comboLaboratorios, gbcSection);

        gbcSection.gridx = 0;
        gbcSection.gridy = 1;
        JLabel lblHorario = new JLabel("Horario:");
        lblHorario.setFont(new Font("Roboto", Font.PLAIN, 14)); // Smaller font
        lblHorario.setForeground(TEXT_COLOR);
        sectionLabHorario.add(lblHorario, gbcSection);

        gbcSection.gridx = 1;
        comboHorarios = new JComboBox<>();
        styleComboBox(comboHorarios);
        sectionLabHorario.add(comboHorarios, gbcSection);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 0.1;
        formPanel.add(sectionLabHorario, gbc);

        // Sección: Equipamientos
        modeloEquipamientos = new DefaultTableModel(new String[]{"ID", "Nombre", "Modelo", "Disponibilidad"}, 0);
        tablaEquipamientos = createStyledTable(modeloEquipamientos);
        JScrollPane scrollEquipamientos = createStyledScrollPane(tablaEquipamientos);
        scrollEquipamientos.setPreferredSize(new Dimension(350, 100)); // Smaller size

        modeloListaEquipamientos = new DefaultListModel<>();
        listaEquipamientosSeleccionados = new JList<>(modeloListaEquipamientos);
        setupListRenderer(listaEquipamientosSeleccionados, true);
        JScrollPane scrollListaEquipamientos = createStyledScrollPane(listaEquipamientosSeleccionados);
        scrollListaEquipamientos.setPreferredSize(new Dimension(200, 100)); // Smaller size

        JPanel equipamientoPanel = new JPanel(new BorderLayout(5, 5)); // Reduced spacing
        equipamientoPanel.setOpaque(false);
        equipamientoPanel.add(scrollEquipamientos, BorderLayout.CENTER);
        equipamientoPanel.add(scrollListaEquipamientos, BorderLayout.EAST);

        gbc.gridy = 1;
        gbc.weighty = 0.3;
        JPanel sectionEquipamientos = createCardPanel("Equipamientos Disponibles");
        sectionEquipamientos.setLayout(new BorderLayout(5, 5)); // Reduced spacing
        sectionEquipamientos.add(equipamientoPanel, BorderLayout.CENTER);

        btnAgregarEquipamiento = createStyledButton("Agregar Equipamiento", SECONDARY_COLOR);
        sectionEquipamientos.add(btnAgregarEquipamiento, BorderLayout.SOUTH);
        formPanel.add(sectionEquipamientos, gbc);

        // Sección: Insumos
        modeloInsumos = new DefaultTableModel(new String[]{"ID", "Insumo", "Cantidad Disponible", "Categoría", "Disponibilidad"}, 0);
        tablaInsumos = createStyledTable(modeloInsumos);
        JScrollPane scrollInsumos = createStyledScrollPane(tablaInsumos);
        scrollInsumos.setPreferredSize(new Dimension(350, 100)); // Smaller size

        modeloListaInsumos = new DefaultListModel<>();
        listaInsumosSeleccionados = new JList<>(modeloListaInsumos);
        setupListRenderer(listaInsumosSeleccionados, false);
        JScrollPane scrollListaInsumos = createStyledScrollPane(listaInsumosSeleccionados);
        scrollListaInsumos.setPreferredSize(new Dimension(200, 100)); // Smaller size

        JPanel insumoPanel = new JPanel(new BorderLayout(5, 5)); // Reduced spacing
        insumoPanel.setOpaque(false);
        insumoPanel.add(scrollInsumos, BorderLayout.CENTER);
        insumoPanel.add(scrollListaInsumos, BorderLayout.EAST);

        gbc.gridy = 2;
        JPanel sectionInsumos = createCardPanel("Insumos Disponibles");
        sectionInsumos.setLayout(new BorderLayout(5, 5)); // Reduced spacing
        sectionInsumos.add(insumoPanel, BorderLayout.CENTER);

        JPanel insumoInputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5)); // Reduced spacing
        insumoInputPanel.setBackground(CARD_COLOR);
        JLabel lblCantidad = new JLabel("Cantidad:");
        lblCantidad.setFont(new Font("Roboto", Font.PLAIN, 14)); // Smaller font
        lblCantidad.setForeground(TEXT_COLOR);
        insumoInputPanel.add(lblCantidad);

        txtCantidadInsumo = new JTextField(5);
        txtCantidadInsumo.setFont(new Font("Roboto", Font.PLAIN, 14)); // Smaller font
        txtCantidadInsumo.setBorder(new RoundedBorder(10)); // Rounded border
        txtCantidadInsumo.setPreferredSize(new Dimension(80, 30)); // Smaller size
        txtCantidadInsumo.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { validarCantidad(); }
            public void removeUpdate(DocumentEvent e) { validarCantidad(); }
            public void changedUpdate(DocumentEvent e) { validarCantidad(); }
            private void validarCantidad() {
                String text = txtCantidadInsumo.getText().trim();
                try {
                    int cantidad = Integer.parseInt(text);
                    txtCantidadInsumo.setBorder(cantidad <= 0 ? new LineBorder(UNAVAILABLE_COLOR, 1, true) : new RoundedBorder(10));
                } catch (NumberFormatException ex) {
                    txtCantidadInsumo.setBorder(new LineBorder(UNAVAILABLE_COLOR, 1, true));
                }
            }
        });
        insumoInputPanel.add(txtCantidadInsumo);

        btnAgregarInsumo = createStyledButton("Agregar Insumo", SECONDARY_COLOR);
        insumoInputPanel.add(btnAgregarInsumo);
        sectionInsumos.add(insumoInputPanel, BorderLayout.SOUTH);
        formPanel.add(sectionInsumos, gbc);

        // Sección: Observaciones
        gbc.gridy = 3;
        gbc.weighty = 0.2;
        JPanel sectionObservaciones = createCardPanel("Observaciones");
        sectionObservaciones.setLayout(new BorderLayout(5, 5)); // Reduced spacing
        txtObservaciones = new JTextArea(3, 20); // Smaller rows
        txtObservaciones.setFont(new Font("Roboto", Font.PLAIN, 14)); // Smaller font
        txtObservaciones.setLineWrap(true);
        txtObservaciones.setWrapStyleWord(true);
        txtObservaciones.setBorder(new RoundedBorder(10)); // Rounded border
        JScrollPane scrollObservaciones = createStyledScrollPane(txtObservaciones);
        sectionObservaciones.add(scrollObservaciones, BorderLayout.CENTER);
        formPanel.add(sectionObservaciones, gbc);

        // Botón Solicitar
        btnSolicitar = createStyledButton("Solicitar Préstamo", PRIMARY_COLOR);
        btnSolicitar.setFont(new Font("Roboto", Font.BOLD, 16)); // Smaller font
        gbc.gridy = 4;
        gbc.weighty = 0.1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(btnSolicitar, gbc);

        // Envolver en JScrollPane
        JScrollPane mainScroll = createStyledScrollPane(formPanel);
        mainScroll.setBorder(null);
        add(mainScroll, BorderLayout.CENTER);

        // Cargar datos iniciales
        try {
            cargarLaboratorios();
        } catch (Exception ex) {
            LOGGER.severe("Error al cargar laboratorios iniciales: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, "No se pudieron cargar los laboratorios: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Listeners
        comboLaboratorios.addActionListener(e -> {
            if (comboLaboratorios.getItemCount() > 0 && comboLaboratorios.getSelectedItem() != null) {
                try {
                    cargarHorarios();
                    modeloListaEquipamientos.clear();
                    modeloListaInsumos.clear();
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

    // Styling Helper Methods
    private JPanel createCardPanel(String title) {
        JPanel panel = new JPanel();
        panel.setBackground(CARD_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(10), // Rounded border
            new EmptyBorder(5, 5, 5, 5) // Reduced padding
        ));
        panel.setBorder(BorderFactory.createTitledBorder(
            new RoundedBorder(10),
            title,
            javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
            javax.swing.border.TitledBorder.DEFAULT_POSITION,
            new Font("Roboto", Font.BOLD, 14), // Smaller font
            PRIMARY_COLOR
        ));
        // Add subtle shadow
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 1, new Color(0, 0, 0, 20)),
            panel.getBorder()
        ));
        return panel;
    }

    private JButton createStyledButton(String text, Color baseColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Roboto", Font.PLAIN, 12)); // Smaller font
        button.setForeground(Color.WHITE);
        button.setBackground(baseColor);
        button.setBorder(new RoundedBorder(10)); // Rounded border
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(8, 15, 8, 15)); // Reduced padding
        return button;
    }

    private JTable createStyledTable(DefaultTableModel model) {
        JTable table = new JTable(model) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setRowHeight(25); // Reduced row height
        table.setFont(new Font("Roboto", Font.PLAIN, 12)); // Smaller font
        table.setSelectionBackground(ACCENT_COLOR);
        table.setGridColor(BORDER_COLOR);
        table.setShowGrid(true);
        table.getTableHeader().setFont(new Font("Roboto", Font.BOLD, 12)); // Smaller font
        table.getTableHeader().setBackground(PRIMARY_COLOR);
        table.getTableHeader().setForeground(Color.WHITE);
        table.setOpaque(false);
        table.setBackground(new Color(255, 255, 255, 200)); // Semi-transparent
        table.setBorder(new RoundedBorder(10)); // Rounded border

        // Custom renderer for Disponibilidad column
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setFont(new Font("Roboto", Font.PLAIN, 12)); // Smaller font
                if (model.getColumnName(column).equals("Disponibilidad")) {
                    if ("Disponible".equals(value)) {
                        c.setForeground(AVAILABLE_COLOR);
                    } else if ("No Disponible".equals(value)) {
                        c.setForeground(UNAVAILABLE_COLOR);
                    } else if ("Prestado".equals(value)) {
                        c.setForeground(UNAVAILABLE_COLOR);
                    }
                } else {
                    c.setForeground(TEXT_COLOR);
                }
                if (isSelected) {
                    c.setBackground(ACCENT_COLOR);
                } else {
                    c.setBackground(new Color(255, 255, 255, 200)); // Semi-transparent
                }
                return c;
            }
        });

        // Adjust column widths for Equipamientos
        if (model.getColumnCount() == 4) {
            table.getColumnModel().getColumn(0).setPreferredWidth(30);
            table.getColumnModel().getColumn(1).setPreferredWidth(140);
            table.getColumnModel().getColumn(2).setPreferredWidth(90);
            table.getColumnModel().getColumn(3).setPreferredWidth(80);
        }
        // Adjust column widths for Insumos
        if (model.getColumnCount() == 5) {
            table.getColumnModel().getColumn(0).setPreferredWidth(30);
            table.getColumnModel().getColumn(1).setPreferredWidth(120);
            table.getColumnModel().getColumn(2).setPreferredWidth(80);
            table.getColumnModel().getColumn(3).setPreferredWidth(80);
            table.getColumnModel().getColumn(4).setPreferredWidth(80);
        }
        return table;
    }

    private void setupListRenderer(JList<String> list, boolean isEquipamiento) {
        list.setFont(new Font("Roboto", Font.PLAIN, 12)); // Smaller font
        list.setSelectionBackground(ACCENT_COLOR);
        list.setBackground(new Color(255, 255, 255, 200)); // Semi-transparent
        list.setForeground(TEXT_COLOR);
        list.setBorder(new RoundedBorder(10)); // Rounded border

        list.setCellRenderer(new ListCellRenderer<String>() {
            @Override
            public Component getListCellRendererComponent(JList<? extends String> list, String value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                JPanel panel = new JPanel(new BorderLayout(5, 0));
                panel.setOpaque(true);
                panel.setBackground(isSelected ? ACCENT_COLOR : new Color(255, 255, 255, 200)); // Semi-transparent

                JLabel label = new JLabel(value);
                label.setFont(new Font("Roboto", Font.PLAIN, 12)); // Smaller font
                label.setForeground(TEXT_COLOR);
                panel.add(label, BorderLayout.CENTER);

                JButton removeButton = new JButton("X");
                removeButton.setFont(new Font("Roboto", Font.BOLD, 8)); // Smaller font
                removeButton.setForeground(Color.WHITE);
                removeButton.setBackground(REMOVE_BUTTON_COLOR);
                removeButton.setBorder(new EmptyBorder(2, 4, 2, 4));
                removeButton.setFocusPainted(false);
                removeButton.setPreferredSize(new Dimension(15, 15)); // Smaller button
                panel.add(removeButton, BorderLayout.EAST);

                removeButton.addActionListener(e -> {
                    if (index >= 0 && index < list.getModel().getSize()) {
                        try {
                            removeListItem(index, isEquipamiento);
                        } catch (Exception ex) {
                            LOGGER.severe("Error al remover item de la lista: " + ex.getMessage());
                            JOptionPane.showMessageDialog(PanelSolicitarPrestamo.this, "Error al remover item: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });

                return panel;
            }
        });

        // Add mouse listener to handle button clicks
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = list.locationToIndex(e.getPoint());
                if (index >= 0 && index < list.getModel().getSize()) {
                    Rectangle rect = list.getCellBounds(index, index);
                    int x = e.getX() - rect.x;
                    // Check if click is in the button area
                    if (x > rect.width - 20 && x <= rect.width) {
                        try {
                            removeListItem(index, isEquipamiento);
                        } catch (Exception ex) {
                            LOGGER.severe("Error al remover item de la lista: " + ex.getMessage());
                            JOptionPane.showMessageDialog(PanelSolicitarPrestamo.this, "Error al remover item: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });
    }

    private void removeListItem(int index, boolean isEquipamiento) {
        DefaultListModel<String> model = isEquipamiento ? modeloListaEquipamientos : modeloListaInsumos;
        if (index < 0 || index >= model.getSize()) {
            JOptionPane.showMessageDialog(this, "Índice inválido al intentar remover item.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String item = model.getElementAt(index);
        model.remove(index);

        // Parse item to extract ID and other details
        String[] parts = item.split(" - ");
        int id = Integer.parseInt(parts[0].replace("ID: ", ""));
        String nombre = parts[1].contains(" | ") ? parts[1].substring(0, parts[1].indexOf(" | ")) : parts[1];

        if (isEquipamiento) {
            // Add back to equipamientos table
            modeloEquipamientos.addRow(new Object[]{id, nombre, "", "Disponible"});
            insumoCantidades.remove(id);
        } else {
            // Parse additional insumo details
            String[] details = parts[1].split(" \\| ");
            String nombreInsumo = details[0];
            String categoria = details[1];
            int cantidad = Integer.parseInt(parts[2].replace("Cantidad: ", "").replace(")", ""));

            // Find the insumo in the table and update its quantity
            boolean found = false;
            for (int i = 0; i < modeloInsumos.getRowCount(); i++) {
                if ((int) modeloInsumos.getValueAt(i, 0) == id) {
                    int currentCantidad = (int) modeloInsumos.getValueAt(i, 2);
                    modeloInsumos.setValueAt(currentCantidad + cantidad, i, 2);
                    // Update disponibilidad based on new quantity
                    modeloInsumos.setValueAt((currentCantidad + cantidad) > 0 ? "Disponible" : "No Disponible", i, 4);
                    found = true;
                    break;
                }
            }
            if (!found) {
                // If not found, add it back as a new row
                modeloInsumos.addRow(new Object[]{id, nombreInsumo, cantidad, categoria, cantidad > 0 ? "Disponible" : "No Disponible"});
            }
            insumoCantidades.remove(id);
        }
        System.out.println((isEquipamiento ? "Equipamiento" : "Insumo") + " removido: ID " + id);
    }

    private JScrollPane createStyledScrollPane(Component view) {
        JScrollPane scrollPane = new JScrollPane(view);
        scrollPane.setBorder(new RoundedBorder(10)); // Rounded border
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setUnitIncrement(15);
        JScrollBar horizontalScrollBar = scrollPane.getHorizontalScrollBar();
        horizontalScrollBar.setUnitIncrement(15);
        return scrollPane;
    }

    private void styleComboBox(JComboBox<String> comboBox) {
        comboBox.setFont(new Font("Roboto", Font.PLAIN, 14)); // Smaller font
        comboBox.setBackground(CARD_COLOR);
        comboBox.setForeground(TEXT_COLOR);
        comboBox.setBorder(new RoundedBorder(10)); // Rounded border
        comboBox.setPreferredSize(new Dimension(300, 35)); // Smaller size
        comboBox.setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton button = new JButton("▼");
                button.setFont(new Font("Roboto", Font.PLAIN, 10)); // Smaller font
                button.setBackground(CARD_COLOR);
                button.setForeground(PRIMARY_COLOR);
                button.setBorder(new RoundedBorder(10)); // Rounded border
                button.setFocusPainted(false);
                return button;
            }
        });
    }

    // Updated Methods to Handle JList
    private void agregarEquipamiento() {
        int selectedRow = tablaEquipamientos.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un equipamiento.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String disponibilidad = (String) modeloEquipamientos.getValueAt(selectedRow, 3);
        if ("No Disponible".equals(disponibilidad)) {
            JOptionPane.showMessageDialog(this, "No puede elegir este equipamiento porque está ocupado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int idEquipamiento = (int) modeloEquipamientos.getValueAt(selectedRow, 0);
        String nombre = (String) modeloEquipamientos.getValueAt(selectedRow, 1);
        modeloListaEquipamientos.addElement("ID: " + idEquipamiento + " - " + nombre);
        modeloEquipamientos.removeRow(selectedRow);
        insumoCantidades.computeIfAbsent(idEquipamiento, k -> 0);
        System.out.println("Equipamiento agregado: ID " + idEquipamiento);
    }

    private void agregarInsumo() {
        int selectedRow = tablaInsumos.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un insumo.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String disponibilidad = (String) modeloInsumos.getValueAt(selectedRow, 4);
        if ("No Disponible".equals(disponibilidad)) {
            JOptionPane.showMessageDialog(this, "No puede elegir este insumo porque está ocupado.", "Error", JOptionPane.ERROR_MESSAGE);
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
        String nombre = (String) modeloInsumos.getValueAt(selectedRow, 1);
        int cantidadDisponible = (int) modeloInsumos.getValueAt(selectedRow, 2);
        String categoria = (String) modeloInsumos.getValueAt(selectedRow, 3);
        if (cantidad > cantidadDisponible) {
            JOptionPane.showMessageDialog(this, "Cantidad solicitada excede la disponible.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        modeloListaInsumos.addElement("ID: " + idInsumo + " - " + nombre + " | " + categoria + " (Cantidad: " + cantidad + ")");
        insumoCantidades.put(idInsumo, cantidad);
        modeloInsumos.setValueAt(cantidadDisponible - cantidad, selectedRow, 2);
        modeloInsumos.setValueAt((cantidadDisponible - cantidad) > 0 ? "Disponible" : "No Disponible", selectedRow, 4);
        txtCantidadInsumo.setText("");
        System.out.println("Insumo agregado: ID " + idInsumo + ", Cantidad: " + cantidad);
    }

    private void solicitarPrestamo() {
        System.out.println("Iniciando solicitud de préstamo");

        // NUEVA VALIDACIÓN: Verificar si el usuario puede solicitar un préstamo
        try {
            if (!controladorPrestamo.puedesolicitarPrestamo(ruUsuario)) {
                // Obtener información detallada de préstamos activos
                List<Clases.Prestamo> prestamosActivos = controladorPrestamo.obtenerPrestamosActivos(ruUsuario);

                StringBuilder mensaje = new StringBuilder();
                mensaje.append("No puede solicitar un préstamo porque ya tiene préstamo(s) activo(s):\n\n");

                for (Clases.Prestamo p : prestamosActivos) {
                    mensaje.append("• ID Préstamo: ").append(p.getIdPrestamo())
                           .append(" - Estado: ").append(p.getEstadoPrestamo())
                           .append(" - Fecha: ").append(p.getFechaPrestamo())
                           .append("\n");
                }

                mensaje.append("\nSolo puede solicitar un nuevo préstamo cuando sus préstamos actuales estén 'Terminados' o 'Rechazados'.");

                JOptionPane.showMessageDialog(this, mensaje.toString(), "Préstamo No Permitido", JOptionPane.WARNING_MESSAGE);
                System.out.println("Solicitud rechazada: Usuario RU " + ruUsuario + " tiene préstamos activos");
                return;
            }
        } catch (SQLException ex) {
            LOGGER.severe("Error al verificar préstamos activos: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, "Error al verificar préstamos activos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Continuar con las validaciones existentes
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
            System.out.println("Validando ruUsuario: " + ruUsuario);
            if (ruUsuario <= 0) {
                throw new IllegalArgumentException("RU de usuario inválido: " + ruUsuario);
            }
            if (!controladorPrestamo.existeUsuario(ruUsuario)) {
                throw new SQLException("El usuario con RU " + ruUsuario + " no está registrado en la base de datos.");
            }
            System.out.println("Usuario validado: RU " + ruUsuario);

            System.out.println("Horario seleccionado: " + selectedHorario);
            int idHorario;
            try {
                idHorario = Integer.parseInt(selectedHorario.split(" - ")[0]);
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException ex) {
                throw new IllegalArgumentException("ID de horario inválido: " + selectedHorario, ex);
            }
            System.out.println("ID de horario parseado: " + idHorario);

            String observaciones = txtObservaciones != null ? txtObservaciones.getText() : "";
            System.out.println("Observaciones: " + observaciones);

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

            System.out.println("Insertando préstamo en la base de datos");
            int idPrestamo = controladorPrestamo.insertar(prestamo);
            if (idPrestamo == -1) {
                throw new SQLException("No se pudo registrar el préstamo en la base de datos.");
            }
            System.out.println("Préstamo registrado con ID: " + idPrestamo);

            System.out.println("Procesando equipamientos e insumos");
            for (Map.Entry<Integer, Integer> entry : insumoCantidades.entrySet()) {
                int id = entry.getKey();
                int cantidad = entry.getValue();
                if (cantidad == 0) {
                    System.out.println("Insertando detalle de equipamiento: ID " + id);
                    controladorPrestamo.insertarDetalleEquipamiento(idPrestamo, id);
                } else {
                    System.out.println("Insertando detalle de insumo: ID " + id + ", Cantidad: " + cantidad);
                    controladorPrestamo.insertarDetalleInsumo(idPrestamo, id, cantidad);
                }
            }

            System.out.println("Mostrando mensaje de éxito");
            JOptionPane.showMessageDialog(this, "Préstamo solicitado con éxito. ID: " + idPrestamo, "Éxito", JOptionPane.INFORMATION_MESSAGE);
            System.out.println("Préstamo solicitado con éxito.");

            // Limpiar formulario
            insumoCantidades.clear();
            txtObservaciones.setText("");
            comboHorarios.setSelectedIndex(-1);
            modeloListaEquipamientos.clear();
            modeloListaInsumos.clear();
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
        
       // Filtrar solo horarios disponibles
        List<Horario> horariosDisponibles = new ArrayList<>();
        for (Horario h : horarios) {
            if ("Disponible".equalsIgnoreCase(h.getEstado()) || "Asignado".equalsIgnoreCase(h.getEstado())) {
                horariosDisponibles.add(h);
            }
        }        
        if (horariosDisponibles.isEmpty()) {
            System.out.println("No hay horarios disponibles para el laboratorio ID: " + idLaboratorio);
            JOptionPane.showMessageDialog(this, "No hay horarios disponibles para este laboratorio.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        for (Horario h : horariosDisponibles) {
            comboHorarios.addItem(h.getIdHorario() + " - " + h.getDia() + " " + h.getHora());
        }
        System.out.println("Horarios disponibles cargados: " + horariosDisponibles.size());
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
                String disponibilidad = "Disponible".equalsIgnoreCase(e.getDisponibilidad()) ? "Disponible" : "No Disponible";
                modeloEquipamientos.addRow(new Object[]{
                    e.getIdEquipamiento(),
                    e.getNombreEquipamiento(),
                    e.getModelo(),
                    disponibilidad
                });
                if ("Disponible".equalsIgnoreCase(e.getDisponibilidad())) {
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
                String disponibilidad = "Disponible".equalsIgnoreCase(i.getDisponibilidad()) && i.getCantidad() > 0 ? "Disponible" : "No Disponible";
                modeloInsumos.addRow(new Object[]{
                    i.getIdInsumo(),
                    i.getNombreInsumo(),
                    i.getCantidad(),
                    i.getCategoria(),
                    disponibilidad
                });
                if ("Disponible".equalsIgnoreCase(i.getDisponibilidad()) && i.getCantidad() > 0) {
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
}