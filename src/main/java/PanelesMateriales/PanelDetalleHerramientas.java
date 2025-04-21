/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PanelesMateriales;


import Clases.Equipamiento;
import Clases.HistorialEquipamiento;
import Clases.HistorialGeneral;
import Controles.ControladorEquipamiento;
import Controles.ControladorHistorialEquipamiento;
import Controles.ControladorHistorialGeneral;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.Date;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import com.toedter.calendar.JDateChooser;

public class PanelDetalleHerramientas extends JPanel {
    private JTextField txtId;
    private JTextField txtRUAdministrador;
    private JComboBox<Equipamiento> cbEquipamiento;
    private JComboBox<String> cbDisponibilidad;
    private JDateChooser dateChooser;
    private JComboBox<String> cbCategoria;
    private JTextArea txtDescripcion;
    private JButton btnAgregar, btnModificar, btnLimpiar;
    private JTable tablaHistorial;
    private ControladorEquipamiento controlEquipamiento;
    private ControladorHistorialGeneral controlHistorialGeneral;
    private ControladorHistorialEquipamiento controlHistorialEquipamiento;

    public PanelDetalleHerramientas() {
        controlEquipamiento = new ControladorEquipamiento();
        controlHistorialGeneral = new ControladorHistorialGeneral();
        controlHistorialEquipamiento = new ControladorHistorialEquipamiento();
        setBackground(new Color(245, 245, 245));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Título
        JLabel titleLabel = new JLabel("Detalle de Herramientas");
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

        // Inicializar txtId pero no agregarlo a la UI (se generará automáticamente)
        txtId = new JTextField(10);
        txtId.setEditable(false);

        // Campo para RU Administrador
        JLabel lblRUAdmin = new JLabel("RU Administrador:");
        lblRUAdmin.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtRUAdministrador = new JTextField(10);
        txtRUAdministrador.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(lblRUAdmin, gbc);
        gbc.gridx = 1;
        formPanel.add(txtRUAdministrador, gbc);

        // Campo para seleccionar equipamiento
        JLabel lblEquipamiento = new JLabel("Equipamiento:");
        lblEquipamiento.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cbEquipamiento = new JComboBox<>();
        cbEquipamiento.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cbEquipamiento.setRenderer(new ListCellRenderer<Equipamiento>() {
            @Override
            public Component getListCellRendererComponent(JList<? extends Equipamiento> list, Equipamiento value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = new JLabel();
                if (value != null) {
                    label.setText(value.getIdEquipamiento() + " - " + value.getNombreEquipamiento());
                }
                label.setOpaque(true);
                label.setBackground(isSelected ? new Color(0, 123, 255) : Color.WHITE);
                label.setForeground(isSelected ? Color.WHITE : Color.BLACK);
                label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                label.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
                return label;
            }
        });
        cargarEquipamientos();
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(lblEquipamiento, gbc);
        gbc.gridx = 1;
        formPanel.add(cbEquipamiento, gbc);

        // Campo para seleccionar disponibilidad
        JLabel lblDisponibilidad = new JLabel("Disponibilidad:");
        lblDisponibilidad.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cbDisponibilidad = new JComboBox<>(new String[]{"Disponible", "No Disponible", "De Baja"});
        cbDisponibilidad.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(lblDisponibilidad, gbc);
        gbc.gridx = 1;
        formPanel.add(cbDisponibilidad, gbc);

        // Campo para seleccionar fecha
        JLabel lblFecha = new JLabel("Fecha:");
        lblFecha.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dateChooser = new JDateChooser();
        dateChooser.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dateChooser.setDate(new Date()); // Establecer fecha actual por defecto
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(lblFecha, gbc);
        gbc.gridx = 1;
        formPanel.add(dateChooser, gbc);

        // Campo para seleccionar categoría
        JLabel lblCategoria = new JLabel("Categoría:");
        lblCategoria.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cbCategoria = new JComboBox<>(new String[]{"Reparación", "Actualización", "Restaurada"});
        cbCategoria.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(lblCategoria, gbc);
        gbc.gridx = 1;
        formPanel.add(cbCategoria, gbc);

        // Campo para descripción
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

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        panelBotones.setBackground(Color.WHITE);
        btnAgregar = createStyledButton("Agregar", new Color(0, 123, 255));
        btnModificar = createStyledButton("Modificar", new Color(40, 167, 69));
        btnLimpiar = createStyledButton("Limpiar", new Color(108, 117, 125));
        panelBotones.add(btnAgregar);
        panelBotones.add(btnModificar);
        panelBotones.add(btnLimpiar);
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2; gbc.weighty = 0;
        formPanel.add(panelBotones, gbc);

        // Tabla de historial de equipamiento
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
        
        tablaHistorial.setModel(new DefaultTableModel(
            new Object[]{"ID", "RU Admin", "Fecha", "Categoría", "Descripción", "Equipamiento", "Disponibilidad"}, 0
        ));
        JScrollPane tablaScrollPane = new JScrollPane(tablaHistorial);
        tablaScrollPane.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            "Historial de Herramientas",
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

        // Configurar listeners para los botones
        btnAgregar.addActionListener(e -> agregarHistorial());
        btnModificar.addActionListener(e -> modificarHistorial());
        btnLimpiar.addActionListener(e -> limpiarCampos());
        
        // Agregar listener para la selección de equipamiento
        cbEquipamiento.addActionListener(e -> actualizarDisponibilidadSeleccionada());
        
        // Listener para la tabla
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
        
        // Efecto hover
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

    private void cargarEquipamientos() {
        cbEquipamiento.removeAllItems();
        try {
            for (Equipamiento equipo : controlEquipamiento.listar()) {
                cbEquipamiento.addItem(equipo);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar equipamientos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void actualizarDisponibilidadSeleccionada() {
        Equipamiento equipoSeleccionado = (Equipamiento) cbEquipamiento.getSelectedItem();
        if (equipoSeleccionado != null) {
            String disponibilidad = equipoSeleccionado.getDisponibilidad();
            for (int i = 0; i < cbDisponibilidad.getItemCount(); i++) {
                if (cbDisponibilidad.getItemAt(i).equals(disponibilidad)) {
                    cbDisponibilidad.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    private void cargarHistorial() {
        DefaultTableModel model = (DefaultTableModel) tablaHistorial.getModel();
        model.setRowCount(0);
        try {
            // Listar el historial de todos los equipamientos
            for (Object[] registro : controlHistorialEquipamiento.buscarHistorialCompletoEquipamiento(
                    cbEquipamiento.getSelectedItem() != null ? 
                    ((Equipamiento)cbEquipamiento.getSelectedItem()).getIdEquipamiento() : 0)) {
                
                // Obtener el equipamiento para mostrar el nombre
                int idEquipamiento = (int) registro[5];
                Equipamiento equipo = controlEquipamiento.buscarPorId(idEquipamiento);
                String nombreEquipo = equipo != null ? equipo.getNombreEquipamiento() : "Desconocido";
                
                model.addRow(new Object[]{
                    registro[0],          // id_historial
                    registro[1],          // ru_administrador
                    registro[2],          // fecha
                    registro[3],          // categoria
                    registro[4],          // descripcion
                    nombreEquipo,         // nombre_equipamiento
                    equipo != null ? equipo.getDisponibilidad() : "Desconocido" // disponibilidad
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar historial: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void agregarHistorial() {
        if (cbEquipamiento.getSelectedItem() == null || dateChooser.getDate() == null || 
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
            
            java.sql.Date fecha = new java.sql.Date(dateChooser.getDate().getTime());
            String categoria = (String) cbCategoria.getSelectedItem();
            String descripcion = txtDescripcion.getText();
            int idEquipamiento = ((Equipamiento) cbEquipamiento.getSelectedItem()).getIdEquipamiento();
            String disponibilidad = (String) cbDisponibilidad.getSelectedItem();

            // Primero, crear historial general
            HistorialGeneral historialGeneral = new HistorialGeneral(ruAdministrador, dateChooser.getDate(), categoria, descripcion);
            int idHistorial = controlHistorialGeneral.insertar(historialGeneral);
            
            // Luego, crear historial de equipamiento
            HistorialEquipamiento historialEquipamiento = new HistorialEquipamiento(idHistorial, idEquipamiento);
            controlHistorialEquipamiento.insertar(historialEquipamiento);
            
            // Actualizar la disponibilidad del equipamiento
            controlEquipamiento.actualizarDisponibilidad(idEquipamiento, disponibilidad);
            
            JOptionPane.showMessageDialog(this, "Historial agregado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarCampos();
            cargarHistorial();
            cargarEquipamientos(); // Recargar para reflejar los cambios
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al agregar historial: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modificarHistorial() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un historial para modificar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (cbEquipamiento.getSelectedItem() == null || dateChooser.getDate() == null || 
            txtDescripcion.getText().trim().isEmpty() || txtRUAdministrador.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            int idHistorial = Integer.parseInt(txtId.getText());
            int idEquipamiento = ((Equipamiento) cbEquipamiento.getSelectedItem()).getIdEquipamiento();
            
            // Obtener RU del administrador
            int ruAdministrador;
            try {
                ruAdministrador = Integer.parseInt(txtRUAdministrador.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "El RU debe ser un número entero.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Actualizar historial general
            java.sql.Date fecha = new java.sql.Date(dateChooser.getDate().getTime());
            String categoria = (String) cbCategoria.getSelectedItem();
            String descripcion = txtDescripcion.getText();
            HistorialGeneral historialGeneral = new HistorialGeneral(idHistorial, ruAdministrador, dateChooser.getDate(), categoria, descripcion);
            controlHistorialGeneral.actualizar(historialGeneral);
            
            // Actualizar relación con equipamiento
            HistorialEquipamiento historialEquipamiento = new HistorialEquipamiento(idHistorial, idEquipamiento);
            controlHistorialEquipamiento.actualizar(historialEquipamiento);
            
            // Actualizar disponibilidad del equipamiento
            String disponibilidad = (String) cbDisponibilidad.getSelectedItem();
            controlEquipamiento.actualizarDisponibilidad(idEquipamiento, disponibilidad);
            
            JOptionPane.showMessageDialog(this, "Historial modificado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarCampos();
            cargarHistorial();
            cargarEquipamientos(); // Recargar para reflejar los cambios
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al modificar historial: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        txtId.setText("");
        txtRUAdministrador.setText("");
        dateChooser.setDate(new Date()); // Establecer fecha actual
        cbCategoria.setSelectedIndex(0);
        cbDisponibilidad.setSelectedIndex(0);
        txtDescripcion.setText("");
        if (cbEquipamiento.getItemCount() > 0) {
            cbEquipamiento.setSelectedIndex(0);
            actualizarDisponibilidadSeleccionada();
        }
    }

    private void seleccionarHistorial() {
        int fila = tablaHistorial.getSelectedRow();
        if (fila >= 0) {
            try {
                int idHistorial = (Integer) tablaHistorial.getValueAt(fila, 0);
                int ruAdmin = (Integer) tablaHistorial.getValueAt(fila, 1);
                
                // Establecer el RU del administrador en el campo de texto
                txtRUAdministrador.setText(String.valueOf(ruAdmin));
                txtId.setText(String.valueOf(idHistorial));
                
                // Cargar el historial seleccionado
                HistorialGeneral historialGeneral = controlHistorialGeneral.buscarPorId(idHistorial);
                HistorialEquipamiento historialEquipamiento = controlHistorialEquipamiento.buscarPorIdHistorial(idHistorial);
                
                if (historialGeneral != null && historialEquipamiento != null) {
                    dateChooser.setDate(historialGeneral.getFecha());
                    cbCategoria.setSelectedItem(historialGeneral.getCategoria());
                    txtDescripcion.setText(historialGeneral.getDescripcion());
                    
                    // Seleccionar el equipamiento en el combo
                    for (int i = 0; i < cbEquipamiento.getItemCount(); i++) {
                        if (cbEquipamiento.getItemAt(i).getIdEquipamiento() == historialEquipamiento.getIdEquipamiento()) {
                            cbEquipamiento.setSelectedIndex(i);
                            break;
                        }
                    }
                    
                    // La disponibilidad se actualiza automáticamente al seleccionar el equipamiento
                    actualizarDisponibilidadSeleccionada();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al cargar historial: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}