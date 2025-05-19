/*
 * Click nbsp://netbeans/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbsp://netbeans/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PanelesMateriales;

import Clases.Insumo;
import Clases.Laboratorio;
import Controles.ControladorInsumo;
import Controles.ControladorLaboratorio;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * Panel para editar insumos de laboratorio
 * Autor: Equipo Soldados Caídos
 */
public class PanelEditarInsumos extends JPanel {

    private JTextField cajaInsumo, cajaCantidad;
    private JComboBox<String> comboCategoria, comboLaboratorio, comboDisponibilidad;
    private JTable tablaInsumos;
    private DefaultTableModel modelo;
    private JButton btnAgregar, btnActualizar, btnEliminar, btnLimpiar;

    private ControladorInsumo controlador;
    private ControladorLaboratorio controladorLab;
    
    // Mapa para relacionar nombres de laboratorios con sus IDs
    private Map<String, Integer> mapLaboratorios;
    
    // Para la opción "Ninguno" en el laboratorio
    private final Integer SIN_LABORATORIO = null;

    private Integer idSeleccionado = null;

    public PanelEditarInsumos() {
        controlador = new ControladorInsumo();
        controladorLab = new ControladorLaboratorio();
        mapLaboratorios = new HashMap<>();
        
        // Establece layout general y color de fondo del panel principal
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(15, 15, 15, 15));

        // Título
        JLabel lblTitulo = new JLabel("Gestión de Insumos de Laboratorio", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(45, 62, 80));
        add(lblTitulo, BorderLayout.NORTH);

        // Panel superior (formulario y botones)
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(Color.WHITE);

        // Panel de formulario dividido en dos columnas
        JPanel panelForm = new JPanel(new GridLayout(1, 2, 5, 0));
        panelForm.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(100, 149, 237), 2),
                "Datos del Insumo",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 13),
                new Color(25, 25, 112)));
        panelForm.setBackground(new Color(250, 250, 255));

        // Panel izquierda
        JPanel panelIzquierda = new JPanel(new GridBagLayout());
        panelIzquierda.setBackground(new Color(250, 250, 255));
        GridBagConstraints gbcIzq = new GridBagConstraints();
        gbcIzq.insets = new Insets(5, 5, 5, 5);
        gbcIzq.fill = GridBagConstraints.HORIZONTAL;

        // Panel derecha
        JPanel panelDerecha = new JPanel(new GridBagLayout());
        panelDerecha.setBackground(new Color(250, 250, 255));
        GridBagConstraints gbcDer = new GridBagConstraints();
        gbcDer.insets = new Insets(5, 5, 5, 5);
        gbcDer.fill = GridBagConstraints.HORIZONTAL;

        // Creación de etiquetas
        JLabel lblInsumo = new JLabel("Insumo:");
        JLabel lblCantidad = new JLabel("Cantidad:");
        JLabel lblCategoria = new JLabel("Categoría:");
        JLabel lblLab = new JLabel("Laboratorio:");
        JLabel lblDisponibilidad = new JLabel("Disponibilidad:");

        // Estilo para etiquetas
        for (JLabel label : new JLabel[]{lblInsumo, lblCantidad, lblCategoria, lblLab, lblDisponibilidad}) {
            label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            label.setForeground(new Color(25, 25, 112));
        }

        // Inicializa campos del formulario
        cajaInsumo = new JTextField();
        cajaInsumo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cajaInsumo.setPreferredSize(new Dimension(180, 25));
        cajaCantidad = new JTextField();
        cajaCantidad.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cajaCantidad.setPreferredSize(new Dimension(180, 25));
        
        // ComboBox para la categoría
        comboCategoria = new JComboBox<>(new String[]{"Unidad", "Gramos", "Mililitros"});
        comboCategoria.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboCategoria.setBackground(Color.WHITE);
        comboCategoria.setPreferredSize(new Dimension(180, 25));
        
        // ComboBox para disponibilidad
        comboDisponibilidad = new JComboBox<>(new String[]{"Disponible", "No Disponible", "De Baja"});
        comboDisponibilidad.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboDisponibilidad.setBackground(Color.WHITE);
        comboDisponibilidad.setPreferredSize(new Dimension(180, 25));
        
        // Creación del ComboBox para laboratorios
        comboLaboratorio = new JComboBox<>();
        comboLaboratorio.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboLaboratorio.setBackground(Color.WHITE);
        comboLaboratorio.setPreferredSize(new Dimension(180, 25));
        comboLaboratorio.addItem("Ninguno");
        mapLaboratorios.put("Ninguno", SIN_LABORATORIO);
        cargarLaboratorios();

        // Añade componentes al panel izquierdo
        gbcIzq.gridx = 0; gbcIzq.gridy = 0;
        panelIzquierda.add(lblInsumo, gbcIzq);
        gbcIzq.gridx = 1;
        panelIzquierda.add(cajaInsumo, gbcIzq);

        gbcIzq.gridx = 0; gbcIzq.gridy = 1;
        panelIzquierda.add(lblCantidad, gbcIzq);
        gbcIzq.gridx = 1;
        panelIzquierda.add(cajaCantidad, gbcIzq);

        gbcIzq.gridx = 0; gbcIzq.gridy = 2;
        panelIzquierda.add(lblCategoria, gbcIzq);
        gbcIzq.gridx = 1;
        panelIzquierda.add(comboCategoria, gbcIzq);

        // Añade componentes al panel derecho
        gbcDer.gridx = 0; gbcDer.gridy = 0;
        panelDerecha.add(lblLab, gbcDer);
        gbcDer.gridx = 1;
        panelDerecha.add(comboLaboratorio, gbcDer);

        gbcDer.gridx = 0; gbcDer.gridy = 1;
        panelDerecha.add(lblDisponibilidad, gbcDer);
        gbcDer.gridx = 1;
        panelDerecha.add(comboDisponibilidad, gbcDer);

        // Añade paneles al formulario
        panelForm.add(panelIzquierda);
        panelForm.add(panelDerecha);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        panelBotones.setBackground(new Color(250, 250, 255));

        btnAgregar = new JButton("Agregar");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");
        btnLimpiar = new JButton("Limpiar");

        // Estilo para botones
        btnAgregar.setBackground(new Color(60, 179, 113));
        btnActualizar.setBackground(new Color(100, 149, 237));
        btnEliminar.setBackground(new Color(220, 20, 60));
        btnLimpiar.setBackground(new Color(150, 150, 150));

        for (JButton btn : new JButton[]{btnAgregar, btnActualizar, btnEliminar, btnLimpiar}) {
            btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
            btn.setForeground(Color.WHITE);
            btn.setPreferredSize(new Dimension(100, 30));
            btn.setFocusPainted(false);
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addHoverEffect(btn);
            panelBotones.add(btn);
        }

        // Añade formulario y botones al panel superior
        panelSuperior.add(panelForm, BorderLayout.CENTER);
        panelSuperior.add(panelBotones, BorderLayout.SOUTH);

        // Tabla para mostrar insumos
        modelo = new DefaultTableModel(new String[]{
                "ID", "Insumo", "Cantidad", "Categoría", "Disponibilidad", "ID Lab"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaInsumos = new JTable(modelo);
        tablaInsumos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaInsumos.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tablaInsumos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tablaInsumos.setRowHeight(25);
        tablaInsumos.setOpaque(false);
        tablaInsumos.setShowGrid(true);
        tablaInsumos.setGridColor(new Color(100, 149, 237));

        // Estilo para la tabla
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                // Fondo por defecto para filas (blanco y plomo claro)
                Color defaultBackground = row % 2 == 0 ? Color.WHITE : new Color(220, 220, 220);
                c.setBackground(defaultBackground);

                // Colorear la celda de "Disponibilidad" (columna 4)
                if (column == 4 && value != null) {
                    String disponibilidad = value.toString();
                    switch (disponibilidad) {
                        case "Disponible":
                            c.setBackground(new Color(200, 255, 200, 70)); // Verde muy claro, transparente
                            break;
                        case "No Disponible":
                            c.setBackground(new Color(255, 200, 200, 70)); // Rojo muy claro, transparente
                            break;
                        case "De Baja":
                            c.setBackground(new Color(255, 230, 200, 70)); // Naranja muy claro, transparente
                            break;
                        default:
                            c.setBackground(defaultBackground);
                            break;
                    }
                }

                // Estilo para selección
                if (isSelected) {
                    c.setBackground(new Color(135, 206, 250)); // Azul suave para selección
                    c.setForeground(Color.WHITE);
                } else {
                    c.setForeground(Color.BLACK);
                }
                ((JComponent) c).setOpaque(true); // Celdas opacas para colores
                return c;
            }
        };
        for (int i = 0; i < tablaInsumos.getColumnCount(); i++) {
            tablaInsumos.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        // Ajustar anchos de columnas
        tablaInsumos.getColumnModel().getColumn(0).setPreferredWidth(50); // ID
        tablaInsumos.getColumnModel().getColumn(1).setPreferredWidth(120); // Insumo
        tablaInsumos.getColumnModel().getColumn(2).setPreferredWidth(60); // Cantidad
        tablaInsumos.getColumnModel().getColumn(3).setPreferredWidth(80); // Categoría
        tablaInsumos.getColumnModel().getColumn(4).setPreferredWidth(100); // Disponibilidad
        tablaInsumos.getColumnModel().getColumn(5).setPreferredWidth(50); // ID Lab

        // ScrollPane con fondo semitransparente
        JScrollPane scroll = new JScrollPane(tablaInsumos);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBackground(new Color(100, 149, 237, 50)); // Azul semitransparente
        scroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(100, 149, 237), 2),
                "Lista de Insumos",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 13),
                new Color(25, 25, 112)));

        // Añade paneles al contenedor principal
        add(panelSuperior, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        // Eventos
        btnAgregar.addActionListener(e -> {
            try {
                validarCampos();
                
                String labSeleccionado = comboLaboratorio.getSelectedItem().toString();
                Integer idLab = mapLaboratorios.get(labSeleccionado);
                
                int cantidad = Integer.parseInt(cajaCantidad.getText().trim());
                
                Insumo insumo = new Insumo(
                        cajaInsumo.getText(),
                        cantidad,
                        comboCategoria.getSelectedItem().toString(),
                        idLab,
                        comboDisponibilidad.getSelectedItem().toString()
                );
                controlador.insertar(insumo);
                cargarDatos();
                limpiarFormulario();
                JOptionPane.showMessageDialog(this, "Insumo agregado con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "La cantidad debe ser un número entero", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                mostrarError(ex);
            }
        });

        btnActualizar.addActionListener(e -> {
            try {
                if (idSeleccionado == null) throw new IllegalArgumentException("Seleccione un insumo.");
                
                validarCampos();
                
                String labSeleccionado = comboLaboratorio.getSelectedItem().toString();
                Integer idLab = mapLaboratorios.get(labSeleccionado);
                
                int cantidad = Integer.parseInt(cajaCantidad.getText().trim());
                
                Insumo insumo = new Insumo(
                        idSeleccionado,
                        cajaInsumo.getText(),
                        cantidad,
                        comboCategoria.getSelectedItem().toString(),
                        idLab,
                        comboDisponibilidad.getSelectedItem().toString()
                );
                controlador.actualizar(insumo);
                cargarDatos();
                limpiarFormulario();
                JOptionPane.showMessageDialog(this, "Insumo actualizado con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "La cantidad debe ser un número entero", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                mostrarError(ex);
            }
        });

        btnEliminar.addActionListener(e -> {
            try {
                if (idSeleccionado == null) throw new IllegalArgumentException("Seleccione un insumo.");
                int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar este insumo?", "Confirmación", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    controlador.eliminar(idSeleccionado);
                    cargarDatos();
                    limpiarFormulario();
                    JOptionPane.showMessageDialog(this, "Insumo eliminado con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception ex) {
                mostrarError(ex);
            }
        });

        btnLimpiar.addActionListener(e -> limpiarFormulario());

        tablaInsumos.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            if (!e.getValueIsAdjusting()) {
                int fila = tablaInsumos.getSelectedRow();
                if (fila >= 0) {
                    idSeleccionado = Integer.parseInt(modelo.getValueAt(fila, 0).toString());
                    cajaInsumo.setText(modelo.getValueAt(fila, 1).toString());
                    cajaCantidad.setText(modelo.getValueAt(fila, 2).toString());
                    comboCategoria.setSelectedItem(modelo.getValueAt(fila, 3).toString());
                    comboDisponibilidad.setSelectedItem(modelo.getValueAt(fila, 4).toString());
                    
                    Object idLabObj = modelo.getValueAt(fila, 5);
                    if (idLabObj != null) {
                        Integer idLabSeleccionado = Integer.parseInt(idLabObj.toString());
                        seleccionarLaboratorio(idLabSeleccionado);
                    } else {
                        comboLaboratorio.setSelectedItem("Ninguno");
                    }
                }
            }
        });

        cargarDatos();
    }

    // Agrega efecto hover a los botones
    private void addHoverEffect(JButton button) {
        Color originalColor = button.getBackground();
        Color hoverColor = originalColor.brighter();
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(originalColor);
            }
        });
    }

    // Método para cargar laboratorios en el ComboBox
    private void cargarLaboratorios() {
        try {
            List<Laboratorio> laboratorios = controladorLab.listar();
            for (Laboratorio lab : laboratorios) {
                String infoLab = lab.getIdLaboratorio() + " - " + lab.getUbicacion();
                comboLaboratorio.addItem(infoLab);
                mapLaboratorios.put(infoLab, lab.getIdLaboratorio());
            }
        } catch (SQLException e) {
            mostrarError(e);
        }
    }
    
    // Método para seleccionar un laboratorio específico en el ComboBox
    private void seleccionarLaboratorio(Integer idLab) {
        if (idLab == null) {
            comboLaboratorio.setSelectedItem("Ninguno");
            return;
        }
        
        for (Map.Entry<String, Integer> entry : mapLaboratorios.entrySet()) {
            if (entry.getValue() != null && entry.getValue().equals(idLab)) {
                comboLaboratorio.setSelectedItem(entry.getKey());
                return;
            }
        }
        
        comboLaboratorio.setSelectedItem("Ninguno");
    }

    // Método para validar campos
    private void validarCampos() {
        if (cajaInsumo.getText().trim().isEmpty() || cajaCantidad.getText().trim().isEmpty()) {
            throw new IllegalArgumentException("Todos los campos son obligatorios");
        }
        
        try {
            Integer.parseInt(cajaCantidad.getText().trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("La cantidad debe ser un número entero");
        }
    }

    private void cargarDatos() {
        try {
            modelo.setRowCount(0);
            List<Insumo> lista = controlador.listar();
            for (Insumo insumo : lista) {
                modelo.addRow(new Object[]{
                        insumo.getIdInsumo(),
                        insumo.getNombreInsumo(),
                        insumo.getCantidad(),
                        insumo.getCategoria(),
                        insumo.getDisponibilidad(),
                        insumo.getIdLaboratorio()
                });
            }
        } catch (SQLException e) {
            mostrarError(e);
        }
    }

    private void limpiarFormulario() {
        idSeleccionado = null;
        cajaInsumo.setText("");
        cajaCantidad.setText("");
        comboCategoria.setSelectedIndex(0);
        comboDisponibilidad.setSelectedIndex(0);
        comboLaboratorio.setSelectedIndex(0);
        tablaInsumos.clearSelection();
    }

    private void mostrarError(Exception ex) {
        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}