/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Paneles;

/**
 *
 * @author DOC
 */

import Clases.Horario;
import Clases.Laboratorio;
import Controles.ControladorHorario;
import Controles.ControladorLaboratorio;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PanelHorario extends JPanel {
    private JTextField cajaMateria;
    private JComboBox<String> comboParalelo, comboSemestre, comboCarrera, comboHora, comboDia, comboEstado, comboLaboratorio;
    private JTable tablaHorario;
    private DefaultTableModel modelo;
    private JButton btnAgregar, btnActualizar, btnEliminar, btnLimpiar;

    private ControladorHorario controlador;
    private ControladorLaboratorio controladorLab;
    private Map<String, Integer> mapLaboratorios;
    private Integer idSeleccionado = null;

    public PanelHorario() {
        controlador = new ControladorHorario();
        controladorLab = new ControladorLaboratorio();
        mapLaboratorios = new HashMap<>();

        setLayout(new BorderLayout());
        setBackground(new Color(81, 0, 255));

        JPanel panelForm = new JPanel(new GridLayout(9, 2, 10, 10));
        panelForm.setBorder(BorderFactory.createTitledBorder("GESTOR DE HORARIOS"));
        panelForm.setBackground(new Color(81, 0, 255));

        JLabel lblMateria = new JLabel("Materia:");
        JLabel lblParalelo = new JLabel("Paralelo:");
        JLabel lblSemestre = new JLabel("Semestre:");
        JLabel lblCarrera = new JLabel("Carrera:");
        JLabel lblHora = new JLabel("Hora:");
        JLabel lblDia = new JLabel("Día:");
        JLabel lblEstado = new JLabel("Estado:");
        JLabel lblLab = new JLabel("Laboratorio:");

        for (JLabel label : new JLabel[]{lblMateria, lblParalelo, lblSemestre, lblCarrera, lblHora, lblDia, lblEstado, lblLab}) {
            label.setForeground(Color.WHITE);
        }

        cajaMateria = new JTextField();
        // Cambiado de String[] a Integer[] para paralelo
        comboParalelo = new JComboBox<>(new String[]{"111", "112", "113"});
        comboSemestre = new JComboBox<>(new String[]{"I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"});
        comboCarrera = new JComboBox<>(new String[]{"Ingeniería de Sistemas", "Derecho", "Contaduría", "Gastronomía", "Psicomotricidad", "Ingeniería Comercial", "Parvularía"});
        comboHora = new JComboBox<>(new String[]{"7:30 A 9:00", "9:15 A 10:45", "11:00 A 12:30", "12:30 A 13:30"});
        comboDia = new JComboBox<>(new String[]{"Lunes", "Martes", "Miércoles", "Jueves", "Viernes"});
        comboEstado = new JComboBox<>(new String[]{"Asignado", "Disponible", "No Disponible", "Préstamo"});

        comboLaboratorio = new JComboBox<>();
        cargarLaboratorios();

        panelForm.add(lblMateria); panelForm.add(cajaMateria);
        panelForm.add(lblParalelo); panelForm.add(comboParalelo);
        panelForm.add(lblSemestre); panelForm.add(comboSemestre);
        panelForm.add(lblCarrera); panelForm.add(comboCarrera);
        panelForm.add(lblHora); panelForm.add(comboHora);
        panelForm.add(lblDia); panelForm.add(comboDia);
        panelForm.add(lblEstado); panelForm.add(comboEstado);
        panelForm.add(lblLab); panelForm.add(comboLaboratorio);

        JPanel panelBotones = new JPanel(new GridLayout(1, 4, 10, 10));
        btnAgregar = new JButton("AGREGAR");
        btnActualizar = new JButton("ACTUALIZAR");
        btnEliminar = new JButton("ELIMINAR");
        btnLimpiar = new JButton("LIMPIAR");

        btnAgregar.setBackground(new Color(25, 209, 49));
        btnActualizar.setBackground(new Color(210, 79, 9));
        btnEliminar.setBackground(new Color(220, 20, 60));
        btnLimpiar.setBackground(new Color(0, 63, 135));

        for (JButton btn : new JButton[]{btnAgregar, btnActualizar, btnEliminar, btnLimpiar}) {
            btn.setForeground(Color.WHITE);
            panelBotones.add(btn);
        }

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(panelForm, BorderLayout.NORTH);
        panelSuperior.add(panelBotones, BorderLayout.SOUTH);

        modelo = new DefaultTableModel(new String[]{
                "ID", "Materia", "Paralelo", "Semestre", "Carrera", "Hora", "Día", "Laboratorio", "Estado"
        }, 0);
        tablaHorario = new JTable(modelo);
        tablaHorario.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(tablaHorario);

        add(panelSuperior, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        btnAgregar.addActionListener(e -> {
            try {
                Horario horario = obtenerHorarioDesdeFormulario();
                controlador.insertar(horario);
                cargarDatos();
                limpiarFormulario();
            } catch (Exception ex) {
                mostrarError(ex);
            }
        });

        btnActualizar.addActionListener(e -> {
            try {
                if (idSeleccionado == null) throw new IllegalArgumentException("Seleccione un horario.");
                Horario horario = obtenerHorarioDesdeFormulario();
                // Crear nuevo objeto con ID incluido
                Horario horarioActualizar = new Horario(
                    idSeleccionado,
                    horario.getMateria(),
                    horario.getParalelo(),
                    horario.getSemestre(),
                    horario.getCarrera(),
                    horario.getHora(),
                    horario.getDia(),
                    horario.getIdLaboratorio(),
                    horario.getEstado()
                );
                controlador.actualizar(horarioActualizar);
                cargarDatos();
                limpiarFormulario();
            } catch (Exception ex) {
                mostrarError(ex);
            }
        });

        btnEliminar.addActionListener(e -> {
            try {
                if (idSeleccionado == null) throw new IllegalArgumentException("Seleccione un horario.");
                int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar este horario?", "Confirmación", JOptionPane.YES_NO_OPTION);
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

        tablaHorario.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            int fila = tablaHorario.getSelectedRow();
            if (fila >= 0) {
                idSeleccionado = Integer.parseInt(modelo.getValueAt(fila, 0).toString());
                cajaMateria.setText(modelo.getValueAt(fila, 1).toString());
                comboParalelo.setSelectedItem(modelo.getValueAt(fila, 2).toString());
                comboSemestre.setSelectedItem(modelo.getValueAt(fila, 3).toString());
                comboCarrera.setSelectedItem(modelo.getValueAt(fila, 4).toString());
                comboHora.setSelectedItem(modelo.getValueAt(fila, 5).toString());
                comboDia.setSelectedItem(modelo.getValueAt(fila, 6).toString());
                seleccionarLaboratorio(Integer.parseInt(modelo.getValueAt(fila, 7).toString()));
                comboEstado.setSelectedItem(modelo.getValueAt(fila, 8).toString());
            }
        });

        cargarDatos();
    }

    private void cargarLaboratorios() {
        try {
            comboLaboratorio.removeAllItems();
            mapLaboratorios.clear();
            List<Laboratorio> laboratorios = controladorLab.listar();
            for (Laboratorio lab : laboratorios) {
                String info = lab.getIdLaboratorio() + " - " + lab.getUbicacion();
                comboLaboratorio.addItem(info);
                mapLaboratorios.put(info, lab.getIdLaboratorio());
            }
        } catch (SQLException e) {
            mostrarError(e);
        }
    }

    private void seleccionarLaboratorio(int idLab) {
        for (Map.Entry<String, Integer> entry : mapLaboratorios.entrySet()) {
            if (entry.getValue() == idLab) {
                comboLaboratorio.setSelectedItem(entry.getKey());
                return;
            }
        }
    }

    private Horario obtenerHorarioDesdeFormulario() {
        String materia = cajaMateria.getText();
        int paralelo = Integer.parseInt(comboParalelo.getSelectedItem().toString());
        String semestre = comboSemestre.getSelectedItem().toString();
        String carrera = comboCarrera.getSelectedItem().toString();
        String hora = comboHora.getSelectedItem().toString();
        String dia = comboDia.getSelectedItem().toString();
        String estado = comboEstado.getSelectedItem().toString();
        int idLab = mapLaboratorios.get(comboLaboratorio.getSelectedItem().toString());

        // Correcto orden de parámetros según el constructor
        return new Horario(materia, paralelo, semestre, carrera, hora, dia, idLab, estado);
    }

    private void cargarDatos() {
        try {
            modelo.setRowCount(0);
            List<Horario> lista = controlador.listar();
            for (Horario h : lista) {
                modelo.addRow(new Object[]{
                        h.getIdHorario(), h.getMateria(), h.getParalelo(), h.getSemestre(),
                        h.getCarrera(), h.getHora(), h.getDia(), h.getIdLaboratorio(), h.getEstado()
                });
            }
        } catch (SQLException e) {
            mostrarError(e);
        }
    }

    private void limpiarFormulario() {
        idSeleccionado = null;
        cajaMateria.setText("");
        comboParalelo.setSelectedIndex(0);
        comboSemestre.setSelectedIndex(0);
        comboCarrera.setSelectedIndex(0);
        comboHora.setSelectedIndex(0);
        comboDia.setSelectedIndex(0);
        comboEstado.setSelectedIndex(0);
        comboLaboratorio.setSelectedIndex(0);
        tablaHorario.clearSelection();
    }

    private void mostrarError(Exception ex) {
        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}