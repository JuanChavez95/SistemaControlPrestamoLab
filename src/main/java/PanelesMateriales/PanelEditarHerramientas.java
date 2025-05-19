/*
 * Click nbsp://netbeans/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbsp://netbeans/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PanelesMateriales;

import Clases.Equipamiento;
import Clases.Laboratorio;
import Controles.ControladorEquipamiento;
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
 * Panel para editar equipamientos de laboratorio
 * Autor: Equipo Soldados Caídos
 */
public class PanelEditarHerramientas extends JPanel {

    private JTextField cajaNombre, cajaMarca, cajaModelo, cajaNumeroSerie;
    private JComboBox<String> comboEstado, comboLaboratorio, comboDisponibilidad;
    private JTable tablaEquipamientos;
    private DefaultTableModel modelo;
    private JButton btnAgregar, btnActualizar, btnEliminar, btnLimpiar;

    private ControladorEquipamiento controlador;
    private ControladorLaboratorio controladorLab;
    
    // Mapa para relacionar nombres de laboratorios con sus IDs
    private Map<String, Integer> mapLaboratorios;
    
    // Para la opción "Ninguno" en el laboratorio
    private final Integer SIN_LABORATORIO = null;

    private Integer idSeleccionado = null;

    public PanelEditarHerramientas() {
        controlador = new ControladorEquipamiento();
        controladorLab = new ControladorLaboratorio();
        mapLaboratorios = new HashMap<>();
        
        // Establece layout general y color de fondo del panel principal
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(15, 15, 15, 15));

        // Título
        JLabel lblTitulo = new JLabel("Gestión de Equipamiento de Laboratorio", SwingConstants.CENTER);
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
                "Datos del Equipamiento",
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
        JLabel lblNombre = new JLabel("Nombre del Equipo:");
        JLabel lblMarca = new JLabel("Marca:");
        JLabel lblModelo = new JLabel("Modelo:");
        JLabel lblNumeroSerie = new JLabel("Número de Serie:");
        JLabel lblEstado = new JLabel("Estado:");
        JLabel lblLab = new JLabel("Laboratorio:");
        JLabel lblDisponibilidad = new JLabel("Disponibilidad:");

        // Estilo para etiquetas
        for (JLabel label : new JLabel[]{lblNombre, lblMarca, lblModelo, lblNumeroSerie, lblEstado, lblLab, lblDisponibilidad}) {
            label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            label.setForeground(new Color(25, 25, 112));
        }

        // Inicializa campos del formulario
        cajaNombre = new JTextField();
        cajaNombre.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cajaNombre.setPreferredSize(new Dimension(180, 25));
        cajaMarca = new JTextField();
        cajaMarca.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cajaMarca.setPreferredSize(new Dimension(180, 25));
        cajaModelo = new JTextField();
        cajaModelo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cajaModelo.setPreferredSize(new Dimension(180, 25));
        cajaNumeroSerie = new JTextField();
        cajaNumeroSerie.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cajaNumeroSerie.setPreferredSize(new Dimension(180, 25));
        
        // ComboBox para el estado
        comboEstado = new JComboBox<>(new String[]{"Nuevo", "Uso Medio", "De Baja", "No disponible"});
        comboEstado.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboEstado.setBackground(Color.WHITE);
        comboEstado.setPreferredSize(new Dimension(180, 25));
        
        // ComboBox para disponibilidad
        comboDisponibilidad = new JComboBox<>(new String[]{"Disponible", "En Uso", "En Mantenimiento", "No Disponible"});
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
        panelIzquierda.add(lblNombre, gbcIzq);
        gbcIzq.gridx = 1;
        panelIzquierda.add(cajaNombre, gbcIzq);

        gbcIzq.gridx = 0; gbcIzq.gridy = 1;
        panelIzquierda.add(lblMarca, gbcIzq);
        gbcIzq.gridx = 1;
        panelIzquierda.add(cajaMarca, gbcIzq);

        gbcIzq.gridx = 0; gbcIzq.gridy = 2;
        panelIzquierda.add(lblModelo, gbcIzq);
        gbcIzq.gridx = 1;
        panelIzquierda.add(cajaModelo, gbcIzq);

        gbcIzq.gridx = 0; gbcIzq.gridy = 3;
        panelIzquierda.add(lblNumeroSerie, gbcIzq);
        gbcIzq.gridx = 1;
        panelIzquierda.add(cajaNumeroSerie, gbcIzq);

        // Añade componentes al panel derecho
        gbcDer.gridx = 0; gbcDer.gridy = 0;
        panelDerecha.add(lblEstado, gbcDer);
        gbcDer.gridx = 1;
        panelDerecha.add(comboEstado, gbcDer);

        gbcDer.gridx = 0; gbcDer.gridy = 1;
        panelDerecha.add(lblDisponibilidad, gbcDer);
        gbcDer.gridx = 1;
        panelDerecha.add(comboDisponibilidad, gbcDer);

        gbcDer.gridx = 0; gbcDer.gridy = 2;
        panelDerecha.add(lblLab, gbcDer);
        gbcDer.gridx = 1;
        panelDerecha.add(comboLaboratorio, gbcDer);

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

        // Tabla para mostrar equipamientos
        modelo = new DefaultTableModel(new String[]{
                "ID", "Nombre", "Marca", "Modelo", "Número Serie", "Estado", "Disponibilidad", "ID Lab"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaEquipamientos = new JTable(modelo);
        tablaEquipamientos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaEquipamientos.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tablaEquipamientos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tablaEquipamientos.setRowHeight(25);
        tablaEquipamientos.setOpaque(false);
        tablaEquipamientos.setShowGrid(true);
        tablaEquipamientos.setGridColor(new Color(100, 149, 237));

        // Estilo para la tabla
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                // Fondo por defecto para filas (blanco y plomo claro)
                Color defaultBackground = row % 2 == 0 ? Color.WHITE : new Color(220, 220, 220);
                c.setBackground(defaultBackground);

                // Colorear la celda de "Disponibilidad" (columna 6)
                if (column == 6 && value != null) {
                    String disponibilidad = value.toString();
                    switch (disponibilidad) {
                        case "Disponible":
                            c.setBackground(new Color(200, 255, 200, 70)); // Verde muy claro, transparente
                            break;
                        case "En Uso":
                            c.setBackground(new Color(200, 230, 255, 70)); // Azul muy claro, transparente
                            break;
                        case "En Mantenimiento":
                            c.setBackground(new Color(255, 230, 200, 70)); // Naranja muy claro, transparente
                            break;
                        case "No Disponible":
                            c.setBackground(new Color(255, 200, 200, 70)); // Rojo muy claro, transparente
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
        for (int i = 0; i < tablaEquipamientos.getColumnCount(); i++) {
            tablaEquipamientos.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        // Ajustar anchos de columnas
        tablaEquipamientos.getColumnModel().getColumn(0).setPreferredWidth(50); // ID
        tablaEquipamientos.getColumnModel().getColumn(1).setPreferredWidth(120); // Nombre
        tablaEquipamientos.getColumnModel().getColumn(2).setPreferredWidth(80); // Marca
        tablaEquipamientos.getColumnModel().getColumn(3).setPreferredWidth(80); // Modelo
        tablaEquipamientos.getColumnModel().getColumn(4).setPreferredWidth(100); // Número Serie
        tablaEquipamientos.getColumnModel().getColumn(5).setPreferredWidth(80); // Estado
        tablaEquipamientos.getColumnModel().getColumn(6).setPreferredWidth(100); // Disponibilidad
        tablaEquipamientos.getColumnModel().getColumn(7).setPreferredWidth(50); // ID Lab

        // ScrollPane con fondo semitransparente
        JScrollPane scroll = new JScrollPane(tablaEquipamientos);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBackground(new Color(100, 149, 237, 50)); // Azul semitransparente
        scroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(100, 149, 237), 2),
                "Lista de Equipamientos",
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
                
                Equipamiento equipo = new Equipamiento(
                        cajaNombre.getText(),
                        cajaMarca.getText(),
                        cajaModelo.getText(),
                        cajaNumeroSerie.getText(),
                        comboEstado.getSelectedItem().toString(),
                        idLab,
                        comboDisponibilidad.getSelectedItem().toString()
                );
                controlador.insertar(equipo);
                cargarDatos();
                limpiarFormulario();
                JOptionPane.showMessageDialog(this, "Equipamiento agregado con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                mostrarError(ex);
            }
        });

        btnActualizar.addActionListener(e -> {
            try {
                if (idSeleccionado == null) throw new IllegalArgumentException("Seleccione un equipamiento.");
                
                validarCampos();
                
                String labSeleccionado = comboLaboratorio.getSelectedItem().toString();
                Integer idLab = mapLaboratorios.get(labSeleccionado);
                
                Equipamiento equipo = new Equipamiento(
                        idSeleccionado,
                        cajaNombre.getText(),
                        cajaMarca.getText(),
                        cajaModelo.getText(),
                        cajaNumeroSerie.getText(),
                        comboEstado.getSelectedItem().toString(),
                        idLab,
                        comboDisponibilidad.getSelectedItem().toString()
                );
                controlador.actualizar(equipo);
                cargarDatos();
                limpiarFormulario();
                JOptionPane.showMessageDialog(this, "Equipamiento actualizado con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                mostrarError(ex);
            }
        });

        btnEliminar.addActionListener(e -> {
            try {
                if (idSeleccionado == null) throw new IllegalArgumentException("Seleccione un equipamiento.");
                int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar este equipamiento?", "Confirmación", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    controlador.eliminar(idSeleccionado);
                    cargarDatos();
                    limpiarFormulario();
                    JOptionPane.showMessageDialog(this, "Equipamiento eliminado con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception ex) {
                mostrarError(ex);
            }
        });

        btnLimpiar.addActionListener(e -> limpiarFormulario());

        tablaEquipamientos.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            if (!e.getValueIsAdjusting()) {
                int fila = tablaEquipamientos.getSelectedRow();
                if (fila >= 0) {
                    idSeleccionado = Integer.parseInt(modelo.getValueAt(fila, 0).toString());
                    cajaNombre.setText(modelo.getValueAt(fila, 1).toString());
                    cajaMarca.setText(modelo.getValueAt(fila, 2).toString());
                    cajaModelo.setText(modelo.getValueAt(fila, 3).toString());
                    cajaNumeroSerie.setText(modelo.getValueAt(fila, 4).toString());
                    comboEstado.setSelectedItem(modelo.getValueAt(fila, 5).toString());
                    comboDisponibilidad.setSelectedItem(modelo.getValueAt(fila, 6).toString());
                    
                    Object idLabObj = modelo.getValueAt(fila, 7);
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
        if (cajaNombre.getText().trim().isEmpty() || 
            cajaMarca.getText().trim().isEmpty() || 
            cajaModelo.getText().trim().isEmpty() || 
            cajaNumeroSerie.getText().trim().isEmpty()) {
            throw new IllegalArgumentException("Todos los campos son obligatorios");
        }
    }

    private void cargarDatos() {
        try {
            modelo.setRowCount(0);
            List<Equipamiento> lista = controlador.listar();
            for (Equipamiento eq : lista) {
                modelo.addRow(new Object[]{
                        eq.getIdEquipamiento(),
                        eq.getNombreEquipamiento(),
                        eq.getMarca(),
                        eq.getModelo(),
                        eq.getNumeroSerie(),
                        eq.getEstado(),
                        eq.getDisponibilidad(),
                        eq.getIdLaboratorio()
                });
            }
        } catch (SQLException e) {
            mostrarError(e);
        }
    }

    private void limpiarFormulario() {
        idSeleccionado = null;
        cajaNombre.setText("");
        cajaMarca.setText("");
        cajaModelo.setText("");
        cajaNumeroSerie.setText("");
        comboEstado.setSelectedIndex(0);
        comboDisponibilidad.setSelectedIndex(0);
        comboLaboratorio.setSelectedIndex(0);
        tablaEquipamientos.clearSelection();
    }

    private void mostrarError(Exception ex) {
        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}