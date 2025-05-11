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

// Clase que representa el panel de gestión de laboratorios en la interfaz gráfica
public class PanelLaboratorio extends JPanel {

    // Componentes de la interfaz
    private JComboBox<String> cajaUbicacion;
    private JTextField cajaDescripcion, cajaCapacidad;
    private JTable tablaLaboratorios;
    private DefaultTableModel modelo;
    private JButton btnAgregar, btnActualizar, btnEliminar, btnLimpiar;
    private int idSeleccionado = -1; // ID del laboratorio seleccionado

    private ControladorLaboratorio controlador; // Controlador para manejar operaciones con base de datos

    public PanelLaboratorio() {
        controlador = new ControladorLaboratorio(); // Inicializa el controlador
        setLayout(new BorderLayout()); // Establece el layout del panel principal
        setBackground(new Color(81, 0, 255)); // Color de fondo del panel

        // Panel para el formulario de entrada
        JPanel panelForm = new JPanel(new GridLayout(4, 2, 10, 10));
        panelForm.setBorder(BorderFactory.createTitledBorder("GESTOR DE LABORATORIO"));
        panelForm.setBackground(new Color(81, 0, 255));

        // Etiquetas y campos del formulario
        JLabel lblUbicacion = new JLabel("Ubicación:");
        lblUbicacion.setForeground(Color.WHITE);
        panelForm.add(lblUbicacion);

        cajaUbicacion = new JComboBox<>(new String[]{"Bloque A", "Bloque B", "Bloque C"}); // ComboBox con ubicaciones predefinidas
        panelForm.add(cajaUbicacion);

        JLabel lblDescripcion = new JLabel("Descripción:");
        lblDescripcion.setForeground(Color.WHITE);
        panelForm.add(lblDescripcion);

        cajaDescripcion = new JTextField();
        panelForm.add(cajaDescripcion);

        JLabel lblCapacidad = new JLabel("Capacidad:");
        lblCapacidad.setForeground(Color.WHITE);
        panelForm.add(lblCapacidad);

        cajaCapacidad = new JTextField();
        panelForm.add(cajaCapacidad);

        // Panel de botones con acciones
        JPanel panelBotones = new JPanel(new GridLayout(1, 4, 10, 10));
        btnAgregar = new JButton("AGREGAR");
        btnActualizar = new JButton("ACTUALIZAR");
        btnEliminar = new JButton("ELIMINAR");
        btnLimpiar = new JButton("LIMPIAR");

        // Colores personalizados para los botones
        btnAgregar.setBackground(new Color(25, 209, 49));
        btnAgregar.setForeground(Color.WHITE);
        btnActualizar.setBackground(new Color(210, 79, 9));
        btnActualizar.setForeground(Color.WHITE);
        btnEliminar.setBackground(new Color(220, 20, 60));
        btnEliminar.setForeground(Color.WHITE);
        btnLimpiar.setBackground(new Color(0, 63, 135));
        btnLimpiar.setForeground(Color.WHITE);

        // Agregar los botones al panel
        panelBotones.add(btnAgregar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        // Panel que combina el formulario y los botones
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(panelForm, BorderLayout.NORTH);
        panelSuperior.add(panelBotones, BorderLayout.SOUTH);

        // Tabla para mostrar los laboratorios
        modelo = new DefaultTableModel(new String[]{"ID", "Ubicación", "Descripción", "Capacidad"}, 0);
        tablaLaboratorios = new JTable(modelo);
        tablaLaboratorios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(tablaLaboratorios);

        // Asignar eventos a los botones y tabla
        btnAgregar.addActionListener(this::agregarLaboratorio);
        btnActualizar.addActionListener(this::actualizarLaboratorio);
        btnEliminar.addActionListener(this::eliminarLaboratorio);
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        tablaLaboratorios.getSelectionModel().addListSelectionListener(this::cargarSeleccion);

        // Agregar componentes al panel principal
        add(panelSuperior, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        // Cargar los datos existentes en la tabla
        cargarDatos();
    }

    // Carga los laboratorios desde la base de datos a la tabla
    private void cargarDatos() {
        try {
            modelo.setRowCount(0); // Limpia la tabla
            List<Laboratorio> lista = controlador.listar(); // Obtiene lista desde el controlador
            for (Laboratorio lab : lista) {
                modelo.addRow(new Object[]{
                        lab.getIdLaboratorio(),
                        lab.getUbicacion(),
                        lab.getDescripcion(),
                        lab.getCapacidad()
                });
            }
        } catch (SQLException e) {
            mostrarError(e); // Muestra mensaje si ocurre un error
        }
    }

    // Agrega un nuevo laboratorio a la base de datos
    private void agregarLaboratorio(ActionEvent e) {
        try {
            validarCampos(); // Valida que los campos no estén vacíos o inválidos
            Laboratorio lab = new Laboratorio(
                    (String) cajaUbicacion.getSelectedItem(),
                    cajaDescripcion.getText(),
                    Integer.parseInt(cajaCapacidad.getText())
            );
            controlador.insertar(lab); // Llama al controlador para insertar
            cargarDatos(); // Recarga la tabla
            limpiarFormulario(); // Limpia los campos
        } catch (Exception ex) {
            mostrarError(ex); // Muestra error si ocurre
        }
    }

    // Actualiza un laboratorio existente
    private void actualizarLaboratorio(ActionEvent e) {
        try {
            if (idSeleccionado == -1)
                throw new IllegalArgumentException("Seleccione un laboratorio para actualizar.");
            validarCampos();
            Laboratorio lab = new Laboratorio(
                    idSeleccionado,
                    (String) cajaUbicacion.getSelectedItem(),
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

    // Elimina el laboratorio seleccionado
    private void eliminarLaboratorio(ActionEvent e) {
        try {
            if (idSeleccionado == -1)
                throw new IllegalArgumentException("Seleccione un laboratorio para eliminar.");
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

    // Carga los datos del laboratorio seleccionado en los campos
    private void cargarSeleccion(ListSelectionEvent e) {
        int fila = tablaLaboratorios.getSelectedRow();
        if (fila >= 0) {
            idSeleccionado = (int) modelo.getValueAt(fila, 0);
            cajaUbicacion.setSelectedItem(modelo.getValueAt(fila, 1).toString());
            cajaDescripcion.setText(modelo.getValueAt(fila, 2).toString());
            cajaCapacidad.setText(modelo.getValueAt(fila, 3).toString());
        }
    }

    // Limpia todos los campos del formulario y resetea selección
    private void limpiarFormulario() {
        idSeleccionado = -1;
        cajaUbicacion.setSelectedIndex(0);
        cajaDescripcion.setText("");
        cajaCapacidad.setText("");
        tablaLaboratorios.clearSelection();
    }

    // Verifica que los campos tengan datos válidos
    private void validarCampos() {
        if (cajaUbicacion.getSelectedItem() == null || cajaDescripcion.getText().isEmpty() || cajaCapacidad.getText().isEmpty()) {
            throw new IllegalArgumentException("Campos obligatorios.");
        }
        try {
            int capacidad = Integer.parseInt(cajaCapacidad.getText());
            if (capacidad <= 0)
                throw new IllegalArgumentException("Capacidad debe ser mayor a 0.");
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Capacidad no válida.");
        }
    }

    // Muestra un mensaje de error al usuario
    private void mostrarError(Exception ex) {
        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
