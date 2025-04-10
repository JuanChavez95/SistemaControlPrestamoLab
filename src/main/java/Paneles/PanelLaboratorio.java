/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package Paneles;

import Clases.Laboratorio;
import Controles.ControladorLaboratorio;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.List;

public class PanelLaboratorio extends JPanel {

    private JComboBox<String> cajaUbicacion;
    private JTextField cajaDescripcion, cajaCapacidad;
    private JTable tablaLaboratorios;
    private DefaultTableModel modelo;
    private JButton btnAgregar, btnActualizar, btnEliminar, btnLimpiar;
    private int idSeleccionado = -1;

    private ControladorLaboratorio controlador;

    public PanelLaboratorio() {
        controlador = new ControladorLaboratorio();
        setLayout(new BorderLayout());
        
        setBackground(new Color(81, 0, 255));

        // Panel de formulario
        JPanel panelForm = new JPanel(new GridLayout(4, 2, 10, 10));
        panelForm.setBorder(BorderFactory.createTitledBorder("GESTOR DE LABORATORIO"));
        panelForm.setBackground(new Color(81, 0, 255)); // Cambiar el fondo del panel
        
        JLabel lblUbicacion = new JLabel("Ubicación:");
        lblUbicacion.setForeground(Color.WHITE);  // Texto blanco
        panelForm.add(lblUbicacion);
        
        
        // Usar JComboBox para la selección de datos fijos
        cajaUbicacion = new JComboBox<>(new String[]{"Bloque A", "Bloque B", "Bloque C"});
        panelForm.add(cajaUbicacion);

        JLabel lblDescripcion = new JLabel("Descripción:");
        lblDescripcion.setForeground(Color.WHITE);  // Texto blanco
        panelForm.add(lblDescripcion);
    
        cajaDescripcion = new JTextField();
        panelForm.add(cajaDescripcion);

        JLabel lblCapacidad = new JLabel("Capacidad:");
        lblCapacidad.setForeground(Color.WHITE);  // Texto blanco
        panelForm.add(lblCapacidad);
    
        cajaCapacidad = new JTextField();
        panelForm.add(cajaCapacidad);

        // Botones
        JPanel panelBotones = new JPanel(new GridLayout(1, 4, 10, 10));
        btnAgregar = new JButton("AGREGAR");
        btnActualizar = new JButton("ACTUALIZAR");
        btnEliminar = new JButton("ELIMINAR");
        btnLimpiar = new JButton("LIMPIAR");
        
        //Ajustar el color de los botones.
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
        modelo = new DefaultTableModel(new String[]{"ID", "Ubicación", "Descripción", "Capacidad"}, 0);
        tablaLaboratorios = new JTable(modelo);
        tablaLaboratorios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(tablaLaboratorios);

        // Eventos
        btnAgregar.addActionListener(this::agregarLaboratorio);
        btnActualizar.addActionListener(this::actualizarLaboratorio);
        btnEliminar.addActionListener(this::eliminarLaboratorio);
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        tablaLaboratorios.getSelectionModel().addListSelectionListener(this::cargarSeleccion);

        add(panelSuperior, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        cargarDatos();
    }

    private void cargarDatos() {
        try {
            modelo.setRowCount(0);
            List<Laboratorio> lista = controlador.listar();
            for (Laboratorio lab : lista) {
                modelo.addRow(new Object[]{
                        lab.getIdLaboratorio(),
                        lab.getUbicacion(),
                        lab.getDescripcion(),
                        lab.getCapacidad()
                });
            }
        } catch (SQLException e) {
            mostrarError(e);
        }
    }

    private void agregarLaboratorio(ActionEvent e) {
        try {
            validarCampos();
            Laboratorio lab = new Laboratorio(
                    (String) cajaUbicacion.getSelectedItem(), // Obtener el valor seleccionado
                    cajaDescripcion.getText(),
                    Integer.parseInt(cajaCapacidad.getText())
            );
            controlador.insertar(lab);
            cargarDatos();
            limpiarFormulario();
        } catch (Exception ex) {
            mostrarError(ex);
        }
    }

    private void actualizarLaboratorio(ActionEvent e) {
        try {
            if (idSeleccionado == -1) throw new IllegalArgumentException("Seleccione un laboratorio para actualizar.");
            validarCampos();
            Laboratorio lab = new Laboratorio(
                    idSeleccionado,
                    (String) cajaUbicacion.getSelectedItem(), // Obtener el valor seleccionado
                    cajaDescripcion.getText(),
                    Integer.parseInt(cajaCapacidad.getText())
            );
            controlador.actualizar(lab);
            cargarDatos();
            limpiarFormulario();
        } catch (Exception ex) {
            mostrarError(ex);
        }
    }

    private void eliminarLaboratorio(ActionEvent e) {
        try {
            if (idSeleccionado == -1) throw new IllegalArgumentException("Seleccione un laboratorio para eliminar.");
            int confirmacion = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar este laboratorio?", "Confirmación", JOptionPane.YES_NO_OPTION);
            if (confirmacion == JOptionPane.YES_OPTION) {
                controlador.eliminar(idSeleccionado);
                cargarDatos();
                limpiarFormulario();
            }
        } catch (Exception ex) {
            mostrarError(ex);
        }
    }

    private void cargarSeleccion(ListSelectionEvent e) {
        int fila = tablaLaboratorios.getSelectedRow();
        if (fila >= 0) {
            idSeleccionado = (int) modelo.getValueAt(fila, 0);
            cajaUbicacion.setSelectedItem(modelo.getValueAt(fila, 1).toString()); // Establecer el valor seleccionado
            cajaDescripcion.setText(modelo.getValueAt(fila, 2).toString());
            cajaCapacidad.setText(modelo.getValueAt(fila, 3).toString());
        }
    }

    private void limpiarFormulario() {
        idSeleccionado = -1;
        cajaUbicacion.setSelectedIndex(0); // Restablecer al primer elemento
        cajaDescripcion.setText("");
        cajaCapacidad.setText("");
        tablaLaboratorios.clearSelection();
    }

    private void validarCampos() {
        if (cajaUbicacion.getSelectedItem() == null || cajaDescripcion.getText().isEmpty() || cajaCapacidad.getText().isEmpty()) {
            throw new IllegalArgumentException("Campos obligatorios.");
        }
        try {
            int capacidad = Integer.parseInt(cajaCapacidad.getText());
            if (capacidad <= 0) throw new IllegalArgumentException("Capacidad debe ser mayor a 0.");
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Capacidad no válida.");
        }
    }

    private void mostrarError(Exception ex) {
        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
