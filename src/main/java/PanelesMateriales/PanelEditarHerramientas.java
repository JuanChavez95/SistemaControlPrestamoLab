/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PanelesMateriales;

import Clases.Equipamiento;
import Clases.Laboratorio;
import Controles.ControladorEquipamiento;
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
        
        setLayout(new BorderLayout());
        setBackground(new Color(81, 0, 255));

        // Panel del formulario
        JPanel panelForm = new JPanel(new GridLayout(8, 2, 10, 10));
        panelForm.setBorder(BorderFactory.createTitledBorder("GESTOR DE EQUIPAMIENTO"));
        panelForm.setBackground(new Color(81, 0, 255));

        JLabel lblNombre = new JLabel("Nombre del Equipo:");
        JLabel lblMarca = new JLabel("Marca:");
        JLabel lblModelo = new JLabel("Modelo:");
        JLabel lblNumeroSerie = new JLabel("Número de Serie:");
        JLabel lblEstado = new JLabel("Estado:");
        JLabel lblLab = new JLabel("Laboratorio:");
        JLabel lblDisponibilidad = new JLabel("Disponibilidad:");

        for (JLabel label : new JLabel[]{lblNombre, lblMarca, lblModelo, lblNumeroSerie, lblEstado, lblLab, lblDisponibilidad}) {
            label.setForeground(Color.WHITE);
        }

        cajaNombre = new JTextField();
        cajaMarca = new JTextField();
        cajaModelo = new JTextField();
        cajaNumeroSerie = new JTextField();
        
        // ComboBox para el estado
        comboEstado = new JComboBox<>(new String[]{"Nuevo", "Uso Medio", "De Baja", "No disponible"});
        
        // ComboBox para disponibilidad
        comboDisponibilidad = new JComboBox<>(new String[]{"Disponible", "En Uso", "En Mantenimiento", "No Disponible"});
        
        // Creación del ComboBox para laboratorios
        comboLaboratorio = new JComboBox<>();
        comboLaboratorio.addItem("Ninguno");
        mapLaboratorios.put("Ninguno", SIN_LABORATORIO);
        cargarLaboratorios();

        panelForm.add(lblNombre); panelForm.add(cajaNombre);
        panelForm.add(lblMarca); panelForm.add(cajaMarca);
        panelForm.add(lblModelo); panelForm.add(cajaModelo);
        panelForm.add(lblNumeroSerie); panelForm.add(cajaNumeroSerie);
        panelForm.add(lblEstado); panelForm.add(comboEstado);
        panelForm.add(lblDisponibilidad); panelForm.add(comboDisponibilidad);
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

        // Tabla
        modelo = new DefaultTableModel(new String[]{
                "ID", "Nombre", "Marca", "Modelo", "Número Serie", "Estado", "Disponibilidad", "ID Lab"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer que la tabla no sea editable
            }
        };
        tablaEquipamientos = new JTable(modelo);
        tablaEquipamientos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(tablaEquipamientos);

        add(panelSuperior, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        // Eventos
        btnAgregar.addActionListener(e -> {
            try {
                // Validar campos antes de continuar
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
                
                // Validar campos antes de continuar
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
                    
                    // Seleccionar el laboratorio correspondiente
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