/*
 * Click nbsp://netbeans/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbsp://netbeans/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Paneles;

import Clases.Equipos;
import Clases.Laboratorio;
import Controles.ControladorEquipo;
import Controles.ControladorLaboratorio;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class PanelEquipo extends JPanel {

    private JTextField cajaProcesador, cajaMonitor, cajaTeclado, cajaMouse, cajaId;
    private JComboBox<String> comboRam, comboDispositivo, comboLaboratorio, comboEstado;
    private JTable tablaEquipos;
    private DefaultTableModel modelo;
    private JButton btnAgregar, btnActualizar, btnEliminar, btnLimpiar;

    private ControladorEquipo controlador;
    private ControladorLaboratorio controladorLab;
    
    // Mapa para relacionar nombres de laboratorios con sus IDs
    private Map<String, Integer> mapLaboratorios;

    private String idSeleccionado = null;

    public PanelEquipo() {
        controlador = new ControladorEquipo();
        controladorLab = new ControladorLaboratorio();
        mapLaboratorios = new HashMap<>();

        // Establece layout general y color de fondo del panel principal
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(15, 15, 15, 15));

        // Título
        JLabel lblTitulo = new JLabel("Gestión de Equipos de Laboratorio", SwingConstants.CENTER);
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
                "Datos del Equipo",
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
        JLabel lblId = new JLabel("ID del Equipo:");
        JLabel lblProcesador = new JLabel("Procesador:");
        JLabel lblRam = new JLabel("RAM:");
        JLabel lblDispositivo = new JLabel("Disco / Almacenamiento:");
        JLabel lblMonitor = new JLabel("Monitor:");
        JLabel lblTeclado = new JLabel("Teclado:");
        JLabel lblMouse = new JLabel("Mouse:");
        JLabel lblEstado = new JLabel("Estado:");
        JLabel lblLab = new JLabel("Laboratorio:");

        // Estilo para etiquetas
        for (JLabel label : new JLabel[]{lblId, lblProcesador, lblRam, lblDispositivo, lblMonitor, lblTeclado, lblMouse, lblEstado, lblLab}) {
            label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            label.setForeground(new Color(25, 25, 112));
        }

        // Configuración del campo ID con prefijo PC-
        cajaId = new JTextField();
        cajaId.setText("PC-");
        cajaId.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cajaId.setPreferredSize(new Dimension(180, 25));
        cajaId.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (cajaId.getText().equals("PC-")) {
                    cajaId.setCaretPosition(cajaId.getText().length());
                }
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                if (!cajaId.getText().startsWith("PC-")) {
                    cajaId.setText("PC-" + cajaId.getText());
                }
            }
        });
        
        // Solo permitir números después del prefijo
        cajaId.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE && c != KeyEvent.VK_DELETE) {
                    e.consume();
                }
                
                SwingUtilities.invokeLater(() -> {
                    String text = cajaId.getText();
                    if (!text.startsWith("PC-")) {
                        cajaId.setText("PC-" + text);
                        cajaId.setCaretPosition(cajaId.getText().length());
                    }
                });
            }
        });
        
        cajaProcesador = new JTextField();
        cajaProcesador.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cajaProcesador.setPreferredSize(new Dimension(180, 25));
        comboRam = new JComboBox<>(new String[]{"4GB", "8GB", "16GB", "32GB"});
        comboRam.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboRam.setBackground(Color.WHITE);
        comboRam.setPreferredSize(new Dimension(180, 25));
        comboDispositivo = new JComboBox<>(new String[]{"128GB", "256GB", "512GB", "1TB", "2TB"});
        comboDispositivo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboDispositivo.setBackground(Color.WHITE);
        comboDispositivo.setPreferredSize(new Dimension(180, 25));
        cajaMonitor = new JTextField();
        cajaMonitor.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cajaMonitor.setPreferredSize(new Dimension(180, 25));
        cajaTeclado = new JTextField();
        cajaTeclado.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cajaTeclado.setPreferredSize(new Dimension(180, 25));
        cajaMouse = new JTextField();
        cajaMouse.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cajaMouse.setPreferredSize(new Dimension(180, 25));
        
        // Nuevo ComboBox para el estado
        comboEstado = new JComboBox<>(new String[]{"Disponible", "No Disponible", "De baja"});
        comboEstado.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboEstado.setBackground(Color.WHITE);
        comboEstado.setPreferredSize(new Dimension(180, 25));
        
        // Creación del ComboBox para laboratorios
        comboLaboratorio = new JComboBox<>();
        comboLaboratorio.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboLaboratorio.setBackground(Color.WHITE);
        comboLaboratorio.setPreferredSize(new Dimension(180, 25));
        cargarLaboratorios();

        // Añade componentes al panel izquierdo
        gbcIzq.gridx = 0; gbcIzq.gridy = 0;
        panelIzquierda.add(lblId, gbcIzq);
        gbcIzq.gridx = 1;
        panelIzquierda.add(cajaId, gbcIzq);

        gbcIzq.gridx = 0; gbcIzq.gridy = 1;
        panelIzquierda.add(lblProcesador, gbcIzq);
        gbcIzq.gridx = 1;
        panelIzquierda.add(cajaProcesador, gbcIzq);

        gbcIzq.gridx = 0; gbcIzq.gridy = 2;
        panelIzquierda.add(lblRam, gbcIzq);
        gbcIzq.gridx = 1;
        panelIzquierda.add(comboRam, gbcIzq);

        gbcIzq.gridx = 0; gbcIzq.gridy = 3;
        panelIzquierda.add(lblDispositivo, gbcIzq);
        gbcIzq.gridx = 1;
        panelIzquierda.add(comboDispositivo, gbcIzq);

        // Añade componentes al panel derecho
        gbcDer.gridx = 0; gbcDer.gridy = 0;
        panelDerecha.add(lblMonitor, gbcDer);
        gbcDer.gridx = 1;
        panelDerecha.add(cajaMonitor, gbcDer);

        gbcDer.gridx = 0; gbcDer.gridy = 1;
        panelDerecha.add(lblTeclado, gbcDer);
        gbcDer.gridx = 1;
        panelDerecha.add(cajaTeclado, gbcDer);

        gbcDer.gridx = 0; gbcDer.gridy = 2;
        panelDerecha.add(lblMouse, gbcDer);
        gbcDer.gridx = 1;
        panelDerecha.add(cajaMouse, gbcDer);

        gbcDer.gridx = 0; gbcDer.gridy = 3;
        panelDerecha.add(lblEstado, gbcDer);
        gbcDer.gridx = 1;
        panelDerecha.add(comboEstado, gbcDer);

        gbcDer.gridx = 0; gbcDer.gridy = 4;
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

        // Tabla para mostrar equipos
        modelo = new DefaultTableModel(new String[]{
                "ID", "Procesador", "RAM", "Disco", "Monitor", "Teclado", "Mouse", "Estado", "ID Lab"
        }, 0);
        tablaEquipos = new JTable(modelo);
        tablaEquipos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaEquipos.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tablaEquipos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tablaEquipos.setRowHeight(25);
        tablaEquipos.setOpaque(false);
        tablaEquipos.setShowGrid(true);
        tablaEquipos.setGridColor(new Color(100, 149, 237));

        // Estilo para la tabla
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                // Fondo por defecto para filas (blanco y plomo claro)
                Color defaultBackground = row % 2 == 0 ? Color.WHITE : new Color(220, 220, 220);
                c.setBackground(defaultBackground);

                // Colorear la celda de "Estado" (columna 7)
                if (column == 7 && value != null) {
                    String estado = value.toString();
                    switch (estado) {
                        case "Disponible":
                            c.setBackground(new Color(200, 255, 200, 70)); // Verde muy claro, transparente
                            break;
                        case "No Disponible":
                            c.setBackground(new Color(255, 200, 200, 70)); // Rojo muy claro, transparente
                            break;
                        case "De baja":
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
        for (int i = 0; i < tablaEquipos.getColumnCount(); i++) {
            tablaEquipos.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        // Ajustar anchos de columnas
        tablaEquipos.getColumnModel().getColumn(0).setPreferredWidth(50); // ID
        tablaEquipos.getColumnModel().getColumn(1).setPreferredWidth(120); // Procesador
        tablaEquipos.getColumnModel().getColumn(2).setPreferredWidth(50); // RAM
        tablaEquipos.getColumnModel().getColumn(3).setPreferredWidth(60); // Disco
        tablaEquipos.getColumnModel().getColumn(4).setPreferredWidth(80); // Monitor
        tablaEquipos.getColumnModel().getColumn(5).setPreferredWidth(80); // Teclado
        tablaEquipos.getColumnModel().getColumn(6).setPreferredWidth(80); // Mouse
        tablaEquipos.getColumnModel().getColumn(7).setPreferredWidth(80); // Estado
        tablaEquipos.getColumnModel().getColumn(8).setPreferredWidth(50); // ID Lab

        // ScrollPane con fondo semitransparente
        JScrollPane scroll = new JScrollPane(tablaEquipos);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBackground(new Color(100, 149, 237, 50)); // Azul semitransparente
        scroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(100, 149, 237), 2),
                "Lista de Equipos",
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
                int idLab = mapLaboratorios.get(labSeleccionado);
                
                Equipos equipo = new Equipos(
                        cajaId.getText(),
                        cajaProcesador.getText(),
                        comboRam.getSelectedItem().toString(),
                        comboDispositivo.getSelectedItem().toString(),
                        cajaMonitor.getText(),
                        cajaTeclado.getText(),
                        cajaMouse.getText(),
                        comboEstado.getSelectedItem().toString(),
                        idLab
                );
                controlador.insertar(equipo);
                cargarDatos();
                limpiarFormulario();
            } catch (Exception ex) {
                mostrarError(ex);
            }
        });

        btnActualizar.addActionListener(e -> {
            try {
                if (idSeleccionado == null) throw new IllegalArgumentException("Seleccione un equipo.");
                
                validarCampos();
                
                String labSeleccionado = comboLaboratorio.getSelectedItem().toString();
                int idLab = mapLaboratorios.get(labSeleccionado);
                
                Equipos equipo = new Equipos(
                        cajaId.getText(),
                        cajaProcesador.getText(),
                        comboRam.getSelectedItem().toString(),
                        comboDispositivo.getSelectedItem().toString(),
                        cajaMonitor.getText(),
                        cajaTeclado.getText(),
                        cajaMouse.getText(),
                        comboEstado.getSelectedItem().toString(),
                        idLab
                );
                controlador.actualizar(equipo);
                cargarDatos();
                limpiarFormulario();
            } catch (Exception ex) {
                mostrarError(ex);
            }
        });

        btnEliminar.addActionListener(e -> {
            try {
                if (idSeleccionado == null) throw new IllegalArgumentException("Seleccione un equipo.");
                int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar este equipo?", "Confirmación", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    controlador.eliminar(idSeleccionado);
                    cargarDatos();
                    limpiarFormulario();
                }
            } catch (Exception ex) {
                mostrarError(ex);
            }
        });

        btnLimpiar.addActionListener(e -> limpiarFormulario());

        tablaEquipos.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            int fila = tablaEquipos.getSelectedRow();
            if (fila >= 0) {
                idSeleccionado = modelo.getValueAt(fila, 0).toString();
                cajaId.setText(idSeleccionado);
                cajaProcesador.setText(modelo.getValueAt(fila, 1).toString());
                comboRam.setSelectedItem(modelo.getValueAt(fila, 2).toString());
                comboDispositivo.setSelectedItem(modelo.getValueAt(fila, 3).toString());
                cajaMonitor.setText(modelo.getValueAt(fila, 4).toString());
                cajaTeclado.setText(modelo.getValueAt(fila, 5).toString());
                cajaMouse.setText(modelo.getValueAt(fila, 6).toString());
                comboEstado.setSelectedItem(modelo.getValueAt(fila, 7).toString());
                
                int idLabSeleccionado = Integer.parseInt(modelo.getValueAt(fila, 8).toString());
                seleccionarLaboratorio(idLabSeleccionado);
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
            comboLaboratorio.removeAllItems();
            mapLaboratorios.clear();
            
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
    private void seleccionarLaboratorio(int idLab) {
        for (Map.Entry<String, Integer> entry : mapLaboratorios.entrySet()) {
            if (entry.getValue() == idLab) {
                comboLaboratorio.setSelectedItem(entry.getKey());
                return;
            }
        }
    }

    // Método para validar campos
    private void validarCampos() {
        if (cajaProcesador.getText() == null || cajaProcesador.getText().isEmpty() || 
            cajaMonitor.getText() == null || cajaMonitor.getText().isEmpty() || 
            cajaTeclado.getText() == null || cajaTeclado.getText().isEmpty() || 
            cajaMouse.getText() == null || cajaMouse.getText().isEmpty()) {
            throw new IllegalArgumentException("Campos obligatorios.");
        }
    }

    private void cargarDatos() {
        try {
            modelo.setRowCount(0);
            List<Equipos> lista = controlador.listar();
            for (Equipos eq : lista) {
                modelo.addRow(new Object[]{
                        eq.getIdEquipos(),
                        eq.getProcesador(),
                        eq.getRam(),
                        eq.getDispositivo(),
                        eq.getMonitor(),
                        eq.getTeclado(),
                        eq.getMouse(),
                        eq.getEstado(),
                        eq.getIdLaboratorio()
                });
            }
        } catch (SQLException e) {
            mostrarError(e);
        }
    }

    private void limpiarFormulario() {
        idSeleccionado = null;
        cajaId.setText("PC-");
        cajaProcesador.setText("");
        comboRam.setSelectedIndex(0);
        comboDispositivo.setSelectedIndex(0);
        cajaMonitor.setText("");
        cajaTeclado.setText("");
        cajaMouse.setText("");
        comboEstado.setSelectedIndex(0);
        comboLaboratorio.setSelectedIndex(0);
        tablaEquipos.clearSelection();
    }

    private void mostrarError(Exception ex) {
        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}