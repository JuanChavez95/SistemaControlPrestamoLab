/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PanelesMateriales;

import Clases.Insumo;
import Clases.Laboratorio;
import Controles.ControladorInsumo;
import Controles.ControladorLaboratorio;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
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
        
        setLayout(new BorderLayout());
        setBackground(new Color(81, 0, 255));

        // Panel del formulario
        JPanel panelForm = new JPanel(new GridLayout(6, 2, 10, 10));
        panelForm.setBorder(BorderFactory.createTitledBorder("GESTOR DE INSUMOS"));
        panelForm.setBackground(new Color(81, 0, 255));

        JLabel lblInsumo = new JLabel("Insumo:");
        JLabel lblCantidad = new JLabel("Cantidad:");
        JLabel lblCategoria = new JLabel("Categoría:");
        JLabel lblLab = new JLabel("Laboratorio:");
        JLabel lblDisponibilidad = new JLabel("Disponibilidad:");

        for (JLabel label : new JLabel[]{lblInsumo, lblCantidad, lblCategoria, lblLab, lblDisponibilidad}) {
            label.setForeground(Color.WHITE);
        }

        cajaInsumo = new JTextField();
        cajaCantidad = new JTextField();
        
        // ComboBox para la categoría
        comboCategoria = new JComboBox<>(new String[]{"Unidad", "Gramos", "Mililitros"});
        
        // ComboBox para disponibilidad
        comboDisponibilidad = new JComboBox<>(new String[]{"Disponible", "No Disponible", "De Baja"});
        
        // Creación del ComboBox para laboratorios
        comboLaboratorio = new JComboBox<>();
        comboLaboratorio.addItem("Ninguno");
        mapLaboratorios.put("Ninguno", SIN_LABORATORIO);
        cargarLaboratorios();

        panelForm.add(lblInsumo); panelForm.add(cajaInsumo);
        panelForm.add(lblCantidad); panelForm.add(cajaCantidad);
        panelForm.add(lblCategoria); panelForm.add(comboCategoria);
        panelForm.add(lblLab); panelForm.add(comboLaboratorio);
        panelForm.add(lblDisponibilidad); panelForm.add(comboDisponibilidad);

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

        // Tabla
        modelo = new DefaultTableModel(new String[]{
                "ID", "Insumo", "Cantidad", "Categoría", "Disponibilidad", "ID Lab"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer que la tabla no sea editable
            }
        };
        tablaInsumos = new JTable(modelo);
        tablaInsumos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(tablaInsumos);

        add(panelSuperior, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        // Eventos
        btnAgregar.addActionListener(e -> {
            try {
                // Validar campos antes de continuar
                validarCampos();
                
                String labSeleccionado = comboLaboratorio.getSelectedItem().toString();
                Integer idLab = mapLaboratorios.get(labSeleccionado);
                
                // Convertir cantidad a entero
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
                
                // Validar campos antes de continuar
                validarCampos();
                
                String labSeleccionado = comboLaboratorio.getSelectedItem().toString();
                Integer idLab = mapLaboratorios.get(labSeleccionado);
                
                // Convertir cantidad a entero
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
                    
                    // Seleccionar el laboratorio correspondiente
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

    // Método para cargar laboratorios en el ComboBox
    private void cargarLaboratorios() {
        try {
            // Ya hemos añadido "Ninguno" antes
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
        
        // Si no se encuentra, seleccionar "Ninguno"
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