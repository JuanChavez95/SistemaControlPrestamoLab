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
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(15, 15, 15, 15));

        // Título
        JLabel lblTitulo = new JLabel("Gestión de Horarios de Laboratorio", SwingConstants.CENTER);
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
                "Datos del Horario",
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
        JLabel lblMateria = new JLabel("Materia:");
        JLabel lblParalelo = new JLabel("Paralelo:");
        JLabel lblSemestre = new JLabel("Semestre:");
        JLabel lblCarrera = new JLabel("Carrera:");
        JLabel lblHora = new JLabel("Hora:");
        JLabel lblDia = new JLabel("Día:");
        JLabel lblEstado = new JLabel("Estado:");
        JLabel lblLab = new JLabel("Laboratorio:");

        // Estilo para etiquetas
        for (JLabel label : new JLabel[]{lblMateria, lblParalelo, lblSemestre, lblCarrera, lblHora, lblDia, lblEstado, lblLab}) {
            label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            label.setForeground(new Color(25, 25, 112));
        }

        // Inicializa campos del formulario
        cajaMateria = new JTextField();
        cajaMateria.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cajaMateria.setPreferredSize(new Dimension(180, 25));

        comboParalelo = new JComboBox<>(new String[]{"111", "112", "113", "000"});
        comboSemestre = new JComboBox<>(new String[]{"I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X", "Ninguno",});
        comboCarrera = new JComboBox<>(new String[]{"Ingeniería de Sistemas", "Derecho", "Contaduría", "Gastronomía", "Psicomotricidad", "Ingeniería Comercial", "Parvularía", "Ninguno"});
        comboHora = new JComboBox<>(new String[]{"7:30 A 9:00", "9:15 A 10:45", "11:00 A 12:30", "12:30 A 13:30"});
        comboDia = new JComboBox<>(new String[]{"Lunes", "Martes", "Miércoles", "Jueves", "Viernes"});
        comboEstado = new JComboBox<>(new String[]{"Asignado", "Disponible", "No Disponible", "Préstamo"});
        comboLaboratorio = new JComboBox<>();

        // Estilo para combos
        for (JComboBox<?> combo : new JComboBox<?>[]{comboParalelo, comboSemestre, comboCarrera, comboHora, comboDia, comboEstado, comboLaboratorio}) {
            combo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            combo.setBackground(Color.WHITE);
            combo.setPreferredSize(new Dimension(180, 25));
        }

        // Cargar laboratorios
        cargarLaboratorios();

        // Añade componentes al panel izquierdo
        gbcIzq.gridx = 0; gbcIzq.gridy = 0;
        panelIzquierda.add(lblMateria, gbcIzq);
        gbcIzq.gridx = 1;
        panelIzquierda.add(cajaMateria, gbcIzq);

        gbcIzq.gridx = 0; gbcIzq.gridy = 1;
        panelIzquierda.add(lblParalelo, gbcIzq);
        gbcIzq.gridx = 1;
        panelIzquierda.add(comboParalelo, gbcIzq);

        gbcIzq.gridx = 0; gbcIzq.gridy = 2;
        panelIzquierda.add(lblSemestre, gbcIzq);
        gbcIzq.gridx = 1;
        panelIzquierda.add(comboSemestre, gbcIzq);

        gbcIzq.gridx = 0; gbcIzq.gridy = 3;
        panelIzquierda.add(lblCarrera, gbcIzq);
        gbcIzq.gridx = 1;
        panelIzquierda.add(comboCarrera, gbcIzq);

        // Añade componentes al panel derecho
        gbcDer.gridx = 0; gbcDer.gridy = 0;
        panelDerecha.add(lblHora, gbcDer);
        gbcDer.gridx = 1;
        panelDerecha.add(comboHora, gbcDer);

        gbcDer.gridx = 0; gbcDer.gridy = 1;
        panelDerecha.add(lblDia, gbcDer);
        gbcDer.gridx = 1;
        panelDerecha.add(comboDia, gbcDer);

        gbcDer.gridx = 0; gbcDer.gridy = 2;
        panelDerecha.add(lblEstado, gbcDer);
        gbcDer.gridx = 1;
        panelDerecha.add(comboEstado, gbcDer);

        gbcDer.gridx = 0; gbcDer.gridy = 3;
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

        // Tabla para mostrar horarios
        modelo = new DefaultTableModel(new String[]{
                "ID", "Materia", "Paralelo", "Semestre", "Carrera", "Hora", "Día", "Laboratorio", "Estado"
        }, 0);
        tablaHorario = new JTable(modelo);
        tablaHorario.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaHorario.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tablaHorario.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tablaHorario.setRowHeight(25);
        tablaHorario.setOpaque(false); // Hacer la tabla transparente
        tablaHorario.setShowGrid(true);
        tablaHorario.setGridColor(new Color(100, 149, 237));

        // Estilo para la tabla
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                // Fondo por defecto para filas (blanco y plomo claro)
                Color defaultBackground = row % 2 == 0 ? Color.WHITE : new Color(220, 220, 220);
                c.setBackground(defaultBackground);

                // Colorear la celda de "Estado" (columna 8)
                if (column == 8 && value != null) {
                    String estado = value.toString();
                    switch (estado) {
                        case "Disponible":
                            c.setBackground(new Color(200, 255, 200, 70)); // Verde muy claro, transparente
                            break;
                        case "Asignado":
                            c.setBackground(new Color(200, 230, 255, 70)); // Azul muy claro, transparente
                            break;
                        case "No Disponible":
                            c.setBackground(new Color(255, 200, 200, 70)); // Rojo muy claro, transparente
                            break;
                        case "Préstamo":
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
        for (int i = 0; i < tablaHorario.getColumnCount(); i++) {
            tablaHorario.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        // Ajustar anchos de columnas
        tablaHorario.getColumnModel().getColumn(0).setPreferredWidth(50); // ID
        tablaHorario.getColumnModel().getColumn(1).setPreferredWidth(150); // Materia
        tablaHorario.getColumnModel().getColumn(2).setPreferredWidth(80); // Paralelo
        tablaHorario.getColumnModel().getColumn(3).setPreferredWidth(80); // Semestre
        tablaHorario.getColumnModel().getColumn(4).setPreferredWidth(150); // Carrera
        tablaHorario.getColumnModel().getColumn(5).setPreferredWidth(100); // Hora
        tablaHorario.getColumnModel().getColumn(6).setPreferredWidth(80); // Día
        tablaHorario.getColumnModel().getColumn(7).setPreferredWidth(80); // Laboratorio
        tablaHorario.getColumnModel().getColumn(8).setPreferredWidth(100); // Estado

        // ScrollPane con fondo semitransparente
        JScrollPane scroll = new JScrollPane(tablaHorario);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBackground(new Color(100, 149, 237, 50)); // Azul semitransparente
        scroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(100, 149, 237), 2),
                "Lista de Horarios",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 13),
                new Color(25, 25, 112)));

        // Añade paneles al contenedor principal
        add(panelSuperior, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        // Evento al presionar botón AGREGAR
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
                controlador.actualizar(horarioActualizar);
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
                    controlador.eliminar(idSeleccionado);
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