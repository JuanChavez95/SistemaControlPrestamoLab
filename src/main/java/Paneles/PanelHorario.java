/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package Paneles;

// Importación de clases necesarias
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
    // Campos del formulario
    private JTextField cajaMateria;
    private JComboBox<String> comboParalelo, comboSemestre, comboCarrera, comboHora, comboDia, comboEstado, comboLaboratorio;
    private JTable tablaHorario;
    private DefaultTableModel modelo;
    private JButton btnAgregar, btnActualizar, btnEliminar, btnLimpiar;

    // Controladores y mapeo de laboratorios
    private ControladorHorario controlador;
    private ControladorLaboratorio controladorLab;
    private Map<String, Integer> mapLaboratorios;
    private Integer idSeleccionado = null; // ID del horario actualmente seleccionado en la tabla

    public PanelHorario() {
        // Inicialización de controladores
        controlador = new ControladorHorario();
        controladorLab = new ControladorLaboratorio();
        mapLaboratorios = new HashMap<>();

        // Establece layout general y color de fondo del panel principal
        setLayout(new BorderLayout());
        setBackground(new Color(81, 0, 255));

        // Panel de formulario (formulario de ingreso/edición de datos)
        JPanel panelForm = new JPanel(new GridLayout(9, 2, 10, 10));
        panelForm.setBorder(BorderFactory.createTitledBorder("GESTOR DE HORARIOS"));
        panelForm.setBackground(new Color(81, 0, 255));

        // Creación de etiquetas
        JLabel lblMateria = new JLabel("Materia:");
        JLabel lblParalelo = new JLabel("Paralelo:");
        JLabel lblSemestre = new JLabel("Semestre:");
        JLabel lblCarrera = new JLabel("Carrera:");
        JLabel lblHora = new JLabel("Hora:");
        JLabel lblDia = new JLabel("Día:");
        JLabel lblEstado = new JLabel("Estado:");
        JLabel lblLab = new JLabel("Laboratorio:");

        // Establece el color blanco para las etiquetas
        for (JLabel label : new JLabel[]{lblMateria, lblParalelo, lblSemestre, lblCarrera, lblHora, lblDia, lblEstado, lblLab}) {
            label.setForeground(Color.WHITE);
        }

        // Inicializa campos del formulario
        cajaMateria = new JTextField();
        comboParalelo = new JComboBox<>(new String[]{"111", "112", "113"});
        comboSemestre = new JComboBox<>(new String[]{"I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"});
        comboCarrera = new JComboBox<>(new String[]{"Ingeniería de Sistemas", "Derecho", "Contaduría", "Gastronomía", "Psicomotricidad", "Ingeniería Comercial", "Parvularía"});
        comboHora = new JComboBox<>(new String[]{"7:30 A 9:00", "9:15 A 10:45", "11:00 A 12:30", "12:30 A 13:30"});
        comboDia = new JComboBox<>(new String[]{"Lunes", "Martes", "Miércoles", "Jueves", "Viernes"});
        comboEstado = new JComboBox<>(new String[]{"Asignado", "Disponible", "No Disponible", "Préstamo"});

        // ComboBox de laboratorios (se carga dinámicamente)
        comboLaboratorio = new JComboBox<>();
        cargarLaboratorios(); // Llama a función que carga laboratorios desde la base de datos

        // Añade componentes al formulario
        panelForm.add(lblMateria); panelForm.add(cajaMateria);
        panelForm.add(lblParalelo); panelForm.add(comboParalelo);
        panelForm.add(lblSemestre); panelForm.add(comboSemestre);
        panelForm.add(lblCarrera); panelForm.add(comboCarrera);
        panelForm.add(lblHora); panelForm.add(comboHora);
        panelForm.add(lblDia); panelForm.add(comboDia);
        panelForm.add(lblEstado); panelForm.add(comboEstado);
        panelForm.add(lblLab); panelForm.add(comboLaboratorio);

        // Panel de botones
        JPanel panelBotones = new JPanel(new GridLayout(1, 4, 10, 10));
        btnAgregar = new JButton("AGREGAR");
        btnActualizar = new JButton("ACTUALIZAR");
        btnEliminar = new JButton("ELIMINAR");
        btnLimpiar = new JButton("LIMPIAR");

        // Estilo y color para botones
        btnAgregar.setBackground(new Color(25, 209, 49));
        btnActualizar.setBackground(new Color(210, 79, 9));
        btnEliminar.setBackground(new Color(220, 20, 60));
        btnLimpiar.setBackground(new Color(0, 63, 135));
        for (JButton btn : new JButton[]{btnAgregar, btnActualizar, btnEliminar, btnLimpiar}) {
            btn.setForeground(Color.WHITE);
            panelBotones.add(btn);
        }

        // Panel que agrupa formulario y botones
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(panelForm, BorderLayout.NORTH);
        panelSuperior.add(panelBotones, BorderLayout.SOUTH);

        // Tabla para mostrar horarios
        modelo = new DefaultTableModel(new String[]{
                "ID", "Materia", "Paralelo", "Semestre", "Carrera", "Hora", "Día", "Laboratorio", "Estado"
        }, 0);
        tablaHorario = new JTable(modelo);
        tablaHorario.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Solo permite seleccionar una fila
        JScrollPane scroll = new JScrollPane(tablaHorario); // Añade scroll a la tabla

        // Añade paneles al contenedor principal
        add(panelSuperior, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        // Evento al presionar botón AGREGAR
        btnAgregar.addActionListener(e -> {
            try {
                Horario horario = obtenerHorarioDesdeFormulario(); // Obtiene los datos ingresados
                controlador.insertar(horario); // Inserta en BD
                cargarDatos(); // Recarga tabla
                limpiarFormulario(); // Limpia formulario
            } catch (Exception ex) {
                mostrarError(ex); // Muestra error si ocurre
            }
        });

        // Evento al presionar botón ACTUALIZAR
        btnActualizar.addActionListener(e -> {
            try {
                if (idSeleccionado == null) throw new IllegalArgumentException("Seleccione un horario.");
                Horario horario = obtenerHorarioDesdeFormulario();
                Horario horarioActualizar = new Horario(
                    idSeleccionado,
                    horario.getMateria(), horario.getParalelo(), horario.getSemestre(),
                    horario.getCarrera(), horario.getHora(), horario.getDia(),
                    horario.getIdLaboratorio(), horario.getEstado()
                );
                controlador.actualizar(horarioActualizar); // Actualiza en BD
                cargarDatos();
                limpiarFormulario();
            } catch (Exception ex) {
                mostrarError(ex);
            }
        });

        // Evento al presionar botón ELIMINAR
        btnEliminar.addActionListener(e -> {
            try {
                if (idSeleccionado == null) throw new IllegalArgumentException("Seleccione un horario.");
                int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar este horario?", "Confirmación", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    controlador.eliminar(idSeleccionado); // Elimina de BD
                    cargarDatos();
                    limpiarFormulario();
                }
            } catch (Exception ex) {
                mostrarError(ex);
            }
        });

        // Evento al presionar botón LIMPIAR
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        // Evento al seleccionar una fila en la tabla
        tablaHorario.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            int fila = tablaHorario.getSelectedRow();
            if (fila >= 0) {
                // Carga datos de la fila seleccionada en el formulario
                idSeleccionado = Integer.parseInt(modelo.getValueAt(fila, 0).toString());
                cajaMateria.setText(modelo.getValueAt(fila, 1).toString());
                comboParalelo.setSelectedItem(modelo.getValueAt(fila, 2).toString());
                comboSemestre.setSelectedItem(modelo.getValueAt(fila, 3).toString());
                comboCarrera.setSelectedItem(modelo.getValueAt(fila, 4).toString());
                comboHora.setSelectedItem(modelo.getValueAt(fila, 5).toString());
                comboDia.setSelectedItem(modelo.getValueAt(fila, 6).toString());
                seleccionarLaboratorio(Integer.parseInt(modelo.getValueAt(fila, 7).toString())); // Selecciona laboratorio en combo
                comboEstado.setSelectedItem(modelo.getValueAt(fila, 8).toString());
            }
        });

        cargarDatos(); // Carga inicial de datos en la tabla
    }

    // Carga laboratorios desde la base de datos al combo
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

    // Selecciona un laboratorio en el combo a partir de su ID
    private void seleccionarLaboratorio(int idLab) {
        for (Map.Entry<String, Integer> entry : mapLaboratorios.entrySet()) {
            if (entry.getValue() == idLab) {
                comboLaboratorio.setSelectedItem(entry.getKey());
                return;
            }
        }
    }

    // Extrae los datos del formulario y crea un objeto Horario
    private Horario obtenerHorarioDesdeFormulario() {
        String materia = cajaMateria.getText();
        int paralelo = Integer.parseInt(comboParalelo.getSelectedItem().toString());
        String semestre = comboSemestre.getSelectedItem().toString();
        String carrera = comboCarrera.getSelectedItem().toString();
        String hora = comboHora.getSelectedItem().toString();
        String dia = comboDia.getSelectedItem().toString();
        String estado = comboEstado.getSelectedItem().toString();
        int idLab = mapLaboratorios.get(comboLaboratorio.getSelectedItem().toString());

        return new Horario(materia, paralelo, semestre, carrera, hora, dia, idLab, estado);
    }

    // Carga todos los horarios desde la base de datos a la tabla
    private void cargarDatos() {
        try {
            modelo.setRowCount(0); // Limpia la tabla
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

    // Limpia todos los campos del formulario
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

    // Muestra un mensaje de error emergente
    private void mostrarError(Exception ex) {
        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}

