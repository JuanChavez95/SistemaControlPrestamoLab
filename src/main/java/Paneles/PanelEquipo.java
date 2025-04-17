/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Paneles;

import Clases.Equipos;
import Clases.Laboratorio;
import Controles.ControladorEquipo;
import Controles.ControladorLaboratorio;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
        
        setLayout(new BorderLayout());
        setBackground(new Color(81, 0, 255));

        // Panel del formulario
        JPanel panelForm = new JPanel(new GridLayout(9, 2, 10, 10)); // Aumentado a 9 filas para incluir estado
        panelForm.setBorder(BorderFactory.createTitledBorder("GESTOR DE EQUIPOS"));
        panelForm.setBackground(new Color(81, 0, 255));

        JLabel lblId = new JLabel("ID del Equipo:");
        JLabel lblProcesador = new JLabel("Procesador:");
        JLabel lblRam = new JLabel("RAM:");
        JLabel lblDispositivo = new JLabel("Disco / Almacenamiento:");
        JLabel lblMonitor = new JLabel("Monitor:");
        JLabel lblTeclado = new JLabel("Teclado:");
        JLabel lblMouse = new JLabel("Mouse:");
        JLabel lblEstado = new JLabel("Estado:"); // Nueva etiqueta para estado
        JLabel lblLab = new JLabel("Laboratorio:");

        for (JLabel label : new JLabel[]{lblId, lblProcesador, lblRam, lblDispositivo, lblMonitor, lblTeclado, lblMouse, lblEstado, lblLab}) {
            label.setForeground(Color.WHITE);
        }

        // Configuración del campo ID con prefijo PC-
        cajaId = new JTextField();
        cajaId.setText("PC-");
        cajaId.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                // Si solo contiene el prefijo, posiciona el cursor al final
                if (cajaId.getText().equals("PC-")) {
                    cajaId.setCaretPosition(cajaId.getText().length());
                }
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                // Asegurarse que siempre tenga el prefijo
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
                    e.consume(); // Ignora caracteres que no sean números
                }
                
                // Asegurar que el prefijo siempre esté presente
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
        comboRam = new JComboBox<>(new String[]{"4GB", "8GB", "16GB", "32GB"});
        comboDispositivo = new JComboBox<>(new String[]{"128GB", "256GB", "512GB", "1TB", "2TB"});
        cajaMonitor = new JTextField();
        cajaTeclado = new JTextField();
        cajaMouse = new JTextField();
        
        // Nuevo ComboBox para el estado
        comboEstado = new JComboBox<>(new String[]{"Disponible", "No Disponible", "De baja"});
        
        // Creación del ComboBox para laboratorios
        comboLaboratorio = new JComboBox<>();
        cargarLaboratorios();

        panelForm.add(lblId); panelForm.add(cajaId);
        panelForm.add(lblProcesador); panelForm.add(cajaProcesador);
        panelForm.add(lblRam); panelForm.add(comboRam);
        panelForm.add(lblDispositivo); panelForm.add(comboDispositivo);
        panelForm.add(lblMonitor); panelForm.add(cajaMonitor);
        panelForm.add(lblTeclado); panelForm.add(cajaTeclado);
        panelForm.add(lblMouse); panelForm.add(cajaMouse);
        panelForm.add(lblEstado); panelForm.add(comboEstado); // Añadir el ComboBox de estado
        panelForm.add(lblLab); panelForm.add(comboLaboratorio);

        // Botones
        JPanel panelBotones = new JPanel(new GridLayout(1, 4, 10, 10));
        btnAgregar = new JButton("AGREGAR");
        btnActualizar = new JButton("ACTUALIZAR");
        btnEliminar = new JButton("ELIMINAR");
        btnLimpiar = new JButton("LIMPIAR");

        btnAgregar.setBackground(new Color(25, 209, 49));
        btnAgregar.setForeground(Color.WHITE);
        btnActualizar.setBackground(new Color(210, 79, 9));
        btnActualizar.setForeground(Color.WHITE);
        btnEliminar.setBackground(new Color(220, 20, 60));
        btnEliminar.setForeground(Color.WHITE);
        btnLimpiar.setBackground(new Color(0, 63, 135));
        btnLimpiar.setForeground(Color.WHITE);

        panelBotones.add(btnAgregar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(panelForm, BorderLayout.NORTH);
        panelSuperior.add(panelBotones, BorderLayout.SOUTH);

        // Tabla (actualizada para incluir el estado)
        modelo = new DefaultTableModel(new String[]{
                "ID", "Procesador", "RAM", "Disco", "Monitor", "Teclado", "Mouse", "Estado", "ID Lab"
        }, 0);
        tablaEquipos = new JTable(modelo);
        tablaEquipos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(tablaEquipos);

        add(panelSuperior, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        // Eventos
        btnAgregar.addActionListener(e -> {
            try {
                // Validar campos antes de continuar
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
                        comboEstado.getSelectedItem().toString(), // Añadir el estado
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
                
                // Validar campos antes de continuar
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
                        comboEstado.getSelectedItem().toString(), // Añadir el estado
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
                comboEstado.setSelectedItem(modelo.getValueAt(fila, 7).toString()); // Seleccionar el estado
                
                // Seleccionar el laboratorio correspondiente
                int idLabSeleccionado = Integer.parseInt(modelo.getValueAt(fila, 8).toString());
                seleccionarLaboratorio(idLabSeleccionado);
            }
        });

        cargarDatos();
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
                        eq.getEstado(), // Añadir el estado
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
        comboEstado.setSelectedIndex(0); // Resetear el estado
        comboLaboratorio.setSelectedIndex(0);
        tablaEquipos.clearSelection();
    }

    private void mostrarError(Exception ex) {
        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}