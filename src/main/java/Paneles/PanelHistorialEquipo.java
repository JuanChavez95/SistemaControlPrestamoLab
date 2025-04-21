/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Paneles;

import Clases.Equipos;
import Clases.HistorialEquipos;
import Clases.HistorialGeneral;
import Controles.ControladorHistorialEquipos;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.util.List;
import com.toedter.calendar.JDateChooser;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class PanelHistorialEquipo extends JPanel {
    private JTextField txtId; // Se mantiene como variable pero no se mostrará en la UI
    private JTextField txtRUAdministrador;
    private JComboBox<Equipos> cbEquipo;
    private JDateChooser dateChooser;
    private JComboBox<String> cbCategoria;
    private JComboBox<String> cbEstado;
    private JTextArea txtDescripcion;
    private JButton btnAgregar, btnModificar, btnEliminar, btnLimpiar;
    private JTable tablaHistorial;
    private ControladorHistorialEquipos controlador;

    public PanelHistorialEquipo() {
        controlador = new ControladorHistorialEquipos();
        setBackground(new Color(245, 245, 245));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Título
        JLabel titleLabel = new JLabel("Historial Equipo");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(33, 37, 41));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(titleLabel, BorderLayout.NORTH);

        // Panel central (formulario y tabla)
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(new Color(245, 245, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Formulario
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Inicializar txtId pero no agregarlo a la UI
        txtId = new JTextField(10);
        txtId.setEditable(false);

        // Campo para RU Administrador (ahora este es el primer campo visible)
        JLabel lblRUAdmin = new JLabel("RU Administrador:");
        lblRUAdmin.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtRUAdministrador = new JTextField(10);
        txtRUAdministrador.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(lblRUAdmin, gbc);
        gbc.gridx = 1;
        formPanel.add(txtRUAdministrador, gbc);

        JLabel lblEquipo = new JLabel("Equipo:");
        lblEquipo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cbEquipo = new JComboBox<>();
        cbEquipo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cbEquipo.setRenderer(new ListCellRenderer<Equipos>() {
            @Override
            public Component getListCellRendererComponent(JList<? extends Equipos> list, Equipos value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = new JLabel();
                if (value != null) {
                    label.setText(value.getIdEquipos() + " - " + value.getProcesador());
                }
                label.setOpaque(true);
                label.setBackground(isSelected ? new Color(0, 123, 255) : Color.WHITE);
                label.setForeground(isSelected ? Color.WHITE : Color.BLACK);
                label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                label.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
                return label;
            }
        });
        cargarEquipos();
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(lblEquipo, gbc);
        gbc.gridx = 1;
        formPanel.add(cbEquipo, gbc);

        // Campo para Estado del Equipo
        JLabel lblEstado = new JLabel("Estado Equipo:");
        lblEstado.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cbEstado = new JComboBox<>(new String[]{"Disponible", "No Disponible", "De baja"});
        cbEstado.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(lblEstado, gbc);
        gbc.gridx = 1;
        formPanel.add(cbEstado, gbc);

        JLabel lblFecha = new JLabel("Fecha:");
        lblFecha.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dateChooser = new JDateChooser();
        dateChooser.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(lblFecha, gbc);
        gbc.gridx = 1;
        formPanel.add(dateChooser, gbc);

        JLabel lblCategoria = new JLabel("Categoría:");
        lblCategoria.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cbCategoria = new JComboBox<>(new String[]{"Mantenimiento", "Reparación", "Actualización", "Reemplazo", "Inspección"});
        cbCategoria.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(lblCategoria, gbc);
        gbc.gridx = 1;
        formPanel.add(cbCategoria, gbc);

        JLabel lblDescripcion = new JLabel("Descripción:");
        lblDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtDescripcion = new JTextArea(5, 20);
        txtDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(txtDescripcion);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(lblDescripcion, gbc);
        gbc.gridx = 1; gbc.weighty = 1.0;
        formPanel.add(scrollPane, gbc);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        panelBotones.setBackground(Color.WHITE);
        btnAgregar = createStyledButton("Agregar", new Color(0, 123, 255));
        btnModificar = createStyledButton("Modificar", new Color(0, 123, 255));
        btnEliminar = createStyledButton("Eliminar", new Color(220, 53, 69));
        btnLimpiar = createStyledButton("Limpiar", new Color(108, 117, 125));
        panelBotones.add(btnAgregar);
        panelBotones.add(btnModificar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2; gbc.weighty = 0;
        formPanel.add(panelBotones, gbc);

        // Tabla de historial - Eliminada la columna "Estado"
        tablaHistorial = new JTable() {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (row % 2 == 0) {
                    c.setBackground(new Color(240, 240, 240));
                } else {
                    c.setBackground(Color.WHITE);
                }
                return c;
            }
        };
        tablaHistorial.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tablaHistorial.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tablaHistorial.getTableHeader().setBackground(new Color(0, 123, 255));
        tablaHistorial.getTableHeader().setForeground(Color.WHITE);
        tablaHistorial.setRowHeight(30);
        tablaHistorial.setGridColor(new Color(200, 200, 200));
        // Modelo de tabla actualizado - Eliminada la columna "Estado"
        tablaHistorial.setModel(new DefaultTableModel(
            new Object[]{"ID", "RU Admin", "Fecha", "Categoría", "Descripción", "Equipo"}, 0
        ));
        JScrollPane tablaScrollPane = new JScrollPane(tablaHistorial);
        tablaScrollPane.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            "Historial",
            javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
            javax.swing.border.TitledBorder.DEFAULT_POSITION,
            new Font("Segoe UI", Font.BOLD, 14)
        ));

        // Agregar formulario y tabla al panel central
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 1; gbc.weightx = 0.4; gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        contentPanel.add(formPanel, gbc);
        gbc.gridx = 1; gbc.weightx = 0.6;
        contentPanel.add(tablaScrollPane, gbc);

        add(contentPanel, BorderLayout.CENTER);

        // Acciones
        btnAgregar.addActionListener(e -> agregarHistorial());
        btnModificar.addActionListener(e -> modificarHistorial());
        btnEliminar.addActionListener(e -> eliminarHistorial());
        btnLimpiar.addActionListener(e -> limpiarCampos());
        tablaHistorial.getSelectionModel().addListSelectionListener(e -> seleccionarHistorial());

        // Cargar historial inicial
        cargarHistorial();
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.brighter());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        return button;
    }

    private void cargarEquipos() {
        cbEquipo.removeAllItems();
        try {
            for (Equipos equipo : controlador.controlEquipo.listar()) {
                cbEquipo.addItem(equipo);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar equipos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarHistorial() {
        DefaultTableModel model = (DefaultTableModel) tablaHistorial.getModel();
        model.setRowCount(0);
        try {
            for (Object[] registro : controlador.listarHistorialConDetalle()) {
                // Ya no necesitamos obtener el estado del equipo para la tabla
                model.addRow(new Object[]{
                    registro[0],          // id_historial
                    registro[1],          // ru_administrador
                    registro[2],          // fecha
                    registro[3],          // categoria
                    registro[4],          // descripcion
                    registro[5]           // id_equipos
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar historial: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void agregarHistorial() {
        if (cbEquipo.getSelectedItem() == null || dateChooser.getDate() == null || 
            txtDescripcion.getText().trim().isEmpty() || txtRUAdministrador.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            // Obtener RU del administrador desde el campo de texto
            int ruAdministrador;
            try {
                ruAdministrador = Integer.parseInt(txtRUAdministrador.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "El RU debe ser un número entero.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Date fecha = new Date(dateChooser.getDate().getTime());
            String categoria = (String) cbCategoria.getSelectedItem();
            String descripcion = txtDescripcion.getText();
            String idEquipos = ((Equipos) cbEquipo.getSelectedItem()).getIdEquipos();
            String estado = (String) cbEstado.getSelectedItem();

            // Registrar el historial primero
            controlador.registrarHistorialEquipo(ruAdministrador, fecha, categoria, descripcion, idEquipos);
            
            // Actualizar el estado solo del equipo seleccionado
            try {
                Equipos equipo = controlador.controlEquipo.buscarPorId(idEquipos);
                if (equipo != null) {
                    equipo.setEstado(estado);
                    controlador.controlEquipo.actualizar(equipo);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al actualizar estado del equipo: " + ex.getMessage(), 
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
                // Continuamos aunque falle la actualización del estado
            }
            
            JOptionPane.showMessageDialog(this, "Historial agregado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarCampos();
            cargarHistorial();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al agregar historial: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modificarHistorial() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un historial para modificar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (cbEquipo.getSelectedItem() == null || dateChooser.getDate() == null || 
            txtDescripcion.getText().trim().isEmpty() || txtRUAdministrador.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            int idHistorial = Integer.parseInt(txtId.getText());
            String idEquipos = ((Equipos) cbEquipo.getSelectedItem()).getIdEquipos();
            
            // Obtener RU del administrador
            int ruAdministrador;
            try {
                ruAdministrador = Integer.parseInt(txtRUAdministrador.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "El RU debe ser un número entero.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Primero, guardamos el ID del equipo anterior para verificar si cambió
            String idEquipoAnterior = "";
            HistorialEquipos historialActual = controlador.buscarPorIdHistorial(idHistorial);
            if (historialActual != null) {
                idEquipoAnterior = historialActual.getIdEquipos();
            }
            
            // Actualizar la relación equipo-historial
            HistorialEquipos historialEquipos = new HistorialEquipos(idHistorial, idEquipos);
            controlador.actualizar(historialEquipos);

            // Actualizar el historial general
            Date fecha = new Date(dateChooser.getDate().getTime());
            String categoria = (String) cbCategoria.getSelectedItem();
            String descripcion = txtDescripcion.getText();
            HistorialGeneral historialGeneral = new HistorialGeneral(idHistorial, ruAdministrador, fecha, categoria, descripcion);
            controlador.controlHistorialGeneral.actualizar(historialGeneral);
            
            // Actualizar el estado solo del equipo actualmente seleccionado
            String estado = (String) cbEstado.getSelectedItem();
            try {
                Equipos equipo = controlador.controlEquipo.buscarPorId(idEquipos);
                if (equipo != null) {
                    equipo.setEstado(estado);
                    controlador.controlEquipo.actualizar(equipo);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al actualizar estado del equipo: " + ex.getMessage(), 
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
                // Continuamos aunque falle la actualización del estado
            }

            JOptionPane.showMessageDialog(this, "Historial modificado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarCampos();
            cargarHistorial();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al modificar historial: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarHistorial() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un historial para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            int idHistorial = Integer.parseInt(txtId.getText());
            controlador.eliminar(idHistorial);
            JOptionPane.showMessageDialog(this, "Historial eliminado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarCampos();
            cargarHistorial();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al eliminar historial: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        txtId.setText("");
        txtRUAdministrador.setText("");
        cbEquipo.setSelectedIndex(cbEquipo.getItemCount() > 0 ? 0 : -1);
        dateChooser.setDate(null);
        cbCategoria.setSelectedIndex(0);
        cbEstado.setSelectedIndex(0);
        txtDescripcion.setText("");
    }

    private void seleccionarHistorial() {
        int fila = tablaHistorial.getSelectedRow();
        if (fila >= 0) {
            try {
                int idHistorial = (Integer) tablaHistorial.getValueAt(fila, 0);
                int ruAdmin = (Integer) tablaHistorial.getValueAt(fila, 1);
                
                // Establecer el RU del administrador en el campo de texto
                txtRUAdministrador.setText(String.valueOf(ruAdmin));
                
                HistorialEquipos historial = controlador.buscarPorIdHistorial(idHistorial);
                if (historial != null) {
                    txtId.setText(String.valueOf(idHistorial));
                    
                    // Seleccionar el equipo en el combo
                    for (int i = 0; i < cbEquipo.getItemCount(); i++) {
                        if (cbEquipo.getItemAt(i).getIdEquipos().equals(historial.getIdEquipos())) {
                            cbEquipo.setSelectedIndex(i);
                            
                            // Establecer el estado del equipo seleccionado
                            try {
                                Equipos equipo = controlador.controlEquipo.buscarPorId(historial.getIdEquipos());
                                if (equipo != null && equipo.getEstado() != null) {
                                    for (int j = 0; j < cbEstado.getItemCount(); j++) {
                                        if (cbEstado.getItemAt(j).equals(equipo.getEstado())) {
                                            cbEstado.setSelectedIndex(j);
                                            break;
                                        }
                                    }
                                }
                            } catch (Exception ex) {
                                System.err.println("Error al obtener estado del equipo: " + ex.getMessage());
                            }
                            break;
                        }
                    }
                    
                    Object[] registro = controlador.listarHistorialConDetalle().stream()
                        .filter(r -> r[0].equals(idHistorial))
                        .findFirst()
                        .orElse(null);
                    if (registro != null) {
                        dateChooser.setDate((java.util.Date) registro[2]);
                        cbCategoria.setSelectedItem(registro[3]);
                        txtDescripcion.setText((String) registro[4]);
                    }
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al cargar historial: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}