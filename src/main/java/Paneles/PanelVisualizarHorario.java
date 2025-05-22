/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nb://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Paneles;

import Clases.Horario;
import Clases.Laboratorio;
import Controles.ControladorHorario;
import Controles.ControladorLaboratorio;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class PanelVisualizarHorario extends JPanel {
    // Constantes para los colores de estado
    private static final Color COLOR_ASIGNADO = new Color(100, 149, 237); // Azul
    private static final Color COLOR_DISPONIBLE = new Color(144, 238, 144); // Verde claro
    private static final Color COLOR_NO_DISPONIBLE = new Color(255, 102, 102); // Rojo claro
    private static final Color COLOR_PRESTAMO = new Color(255, 165, 0); // Naranja
    
    // Controladores
    private ControladorHorario controladorHorario;
    private ControladorLaboratorio controladorLaboratorio;
    
    // Panel principal del horario
    private JPanel panelHorario;
    
    // ComboBox para seleccionar laboratorio
    private JComboBox<String> comboLaboratorio;
    
    // Mapa para guardar las celdas del horario
    private JPanel[][] celdas;
    
    // Arreglos para días y horas
    private final String[] dias = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes"};
    private final String[] horas = {"7:30 A 9:00", "9:15 A 10:45", "11:00 A 12:30", "12:30 A 13:30"};
    
    public PanelVisualizarHorario() {
        controladorHorario = new ControladorHorario();
        controladorLaboratorio = new ControladorLaboratorio();
        
        setLayout(new BorderLayout());
        
        // Panel superior para la selección de laboratorio
        JPanel panelSeleccion = new JPanel();
        panelSeleccion.setBackground(new Color(81, 0, 255));
        JLabel lblLaboratorio = new JLabel("Seleccione Laboratorio:");
        lblLaboratorio.setForeground(Color.WHITE);
        
        // ComboBox para laboratorios
        comboLaboratorio = new JComboBox<>();
        cargarLaboratorios();
        
        JButton btnVer = new JButton("Ver Horario");
        btnVer.setBackground(new Color(0, 63, 135));
        btnVer.setForeground(Color.WHITE);
        
        panelSeleccion.add(lblLaboratorio);
        panelSeleccion.add(comboLaboratorio);
        panelSeleccion.add(btnVer);
        
        // Panel para el horario
        panelHorario = new JPanel(new BorderLayout());
        panelHorario.setBorder(BorderFactory.createTitledBorder("Horario del Laboratorio"));
        
        // Agregar panels al panel principal
        add(panelSeleccion, BorderLayout.NORTH);
        add(panelHorario, BorderLayout.CENTER);
        
        // Acción para el botón Ver
        btnVer.addActionListener(e -> {
            if (comboLaboratorio.getSelectedItem() != null) {
                String selectedItem = comboLaboratorio.getSelectedItem().toString();
                int idLaboratorio = Integer.parseInt(selectedItem.split(" - ")[0]);
                mostrarHorario(idLaboratorio);
            } else {
                JOptionPane.showMessageDialog(this, "Por favor seleccione un laboratorio", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
    
    private void cargarLaboratorios() {
        try {
            // Usar el controlador de laboratorio para obtener la lista
            List<Laboratorio> laboratorios = controladorLaboratorio.listar();
            for (Laboratorio lab : laboratorios) {
                comboLaboratorio.addItem(lab.getIdLaboratorio() + " - " + lab.getUbicacion());
            }
            
            // Si no hay laboratorios, mostrar mensaje
            if (laboratorios.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "No hay laboratorios registrados en la base de datos", 
                    "Advertencia", 
                    JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error al cargar la lista de laboratorios: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
private void mostrarHorario(int idLaboratorio) {
    // Limpiar el panel de horario
    panelHorario.removeAll();
    
    // Panel para los días (encabezados)
    JPanel panelDias = new RoundedPanel(10);
    panelDias.setLayout(new GridLayout(1, 6));
    panelDias.setBackground(new Color(220, 220, 220));
    
    // Celda vacía en la esquina superior izquierda
    JLabel emptyLabel = new JLabel("");
    emptyLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    emptyLabel.setBackground(new Color(220, 220, 220));
    emptyLabel.setOpaque(true);
    emptyLabel.setPreferredSize(new Dimension(40, 40)); // Further reduced width for the empty corner
    panelDias.add(emptyLabel);
    
    // Encabezados de días (centered)
    for (String dia : dias) {
        JLabel label = new JLabel(dia, SwingConstants.CENTER);
        label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setBackground(new Color(220, 220, 220));
        label.setOpaque(true);
        label.setPreferredSize(new Dimension(120, 40)); // Consistent height
        panelDias.add(label);
    }
    
    // Panel de la cuadrícula principal
    JPanel gridPanel = new RoundedPanel(10);
    gridPanel.setLayout(new GridLayout(4, 6));
    celdas = new JPanel[4][5];
    
    // Agregar filas de horas y celdas de horario
    for (int hora = 0; hora < 4; hora++) {
        // Celda de hora (centered and further reduced width)
        JLabel labelHora = new JLabel(horas[hora], SwingConstants.CENTER);
        labelHora.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        labelHora.setFont(new Font("Arial", Font.BOLD, 12));
        labelHora.setBackground(new Color(220, 220, 220));
        labelHora.setOpaque(true);
        labelHora.setPreferredSize(new Dimension(30, 80)); // Further reduced width for hour column
        emptyLabel.setMinimumSize(new Dimension(30, 40));
        gridPanel.add(labelHora);
        
        for (int dia = 0; dia < 5; dia++) {
            celdas[hora][dia] = crearCeldaHorario("Disponible", "", 0, "", "", COLOR_DISPONIBLE);
            gridPanel.add(celdas[hora][dia]);
        }
    }
    
    try {
        // Obtener los horarios del laboratorio seleccionado
        List<Horario> horarios = controladorHorario.listar();
        Map<String, Map<String, Horario>> horariosPorDiaYHora = new HashMap<>();
        
        // Filtrar horarios por id de laboratorio y organizarlos por día y hora
        for (Horario h : horarios) {
            if (h.getIdLaboratorio() == idLaboratorio) {
                if (!horariosPorDiaYHora.containsKey(h.getDia())) {
                    horariosPorDiaYHora.put(h.getDia(), new HashMap<>());
                }
                horariosPorDiaYHora.get(h.getDia()).put(h.getHora(), h);
            }
        }
        
        // Actualizar celdas con la información de los horarios
        for (int hora = 0; hora < horas.length; hora++) {
            for (int dia = 0; dia < dias.length; dia++) {
                // Verificar si hay un horario para este día y hora
                if (horariosPorDiaYHora.containsKey(dias[dia]) && 
                    horariosPorDiaYHora.get(dias[dia]).containsKey(horas[hora])) {
                    
                    Horario h = horariosPorDiaYHora.get(dias[dia]).get(horas[hora]);
                    Color color;
                    
                    // Determinar el color según el estado
                    switch (h.getEstado()) {
                        case "Asignado":
                            color = COLOR_ASIGNADO;
                            break;
                        case "Disponible":
                            color = COLOR_DISPONIBLE;
                            break;
                        case "No Disponible":
                            color = COLOR_NO_DISPONIBLE;
                            break;
                        case "Préstamo":
                            color = COLOR_PRESTAMO;
                            break;
                        default:
                            color = COLOR_DISPONIBLE;
                    }
                    
                    // Actualizar la celda con la información del horario
                    celdas[hora][dia] = crearCeldaHorario(
                        h.getEstado() != null ? h.getEstado() : "Disponible",
                        h.getMateria() != null ? h.getMateria() : "",
                        h.getParalelo(),
                        h.getSemestre() != null ? h.getSemestre() : "",
                        h.getCarrera() != null ? h.getCarrera() : "",
                        color
                    );
                    
                    // Reemplazar la celda en el panel
                    gridPanel.remove(hora * 6 + dia + 1); // +1 para saltar la celda de hora
                    gridPanel.add(celdas[hora][dia], hora * 6 + dia + 1);
                }
            }
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error al cargar horarios: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    // Crear el panel completo del horario
    JPanel completoPanel = new JPanel(new BorderLayout());
    completoPanel.setBackground(Color.WHITE);
    completoPanel.add(panelDias, BorderLayout.NORTH);
    completoPanel.add(gridPanel, BorderLayout.CENTER);
    
    // Centrar el horario en el panel
    JPanel centeredPanel = new JPanel();
    centeredPanel.setLayout(new BoxLayout(centeredPanel, BoxLayout.X_AXIS));
    centeredPanel.setBackground(Color.WHITE);
    centeredPanel.add(Box.createHorizontalGlue());
    centeredPanel.add(completoPanel);
    centeredPanel.add(Box.createHorizontalGlue());
    
    // Agregar el panel centrado al panel de horario
    panelHorario.add(centeredPanel, BorderLayout.CENTER);
    
    // Leyenda de colores
    JPanel leyendaPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    leyendaPanel.setBackground(Color.WHITE);
    
    agregarLeyendaColor(leyendaPanel, "Asignado", COLOR_ASIGNADO);
    agregarLeyendaColor(leyendaPanel, "Disponible", COLOR_DISPONIBLE);
    agregarLeyendaColor(leyendaPanel, "No Disponible", COLOR_NO_DISPONIBLE);
    agregarLeyendaColor(leyendaPanel, "Préstamo", COLOR_PRESTAMO);
    
    panelHorario.add(leyendaPanel, BorderLayout.SOUTH);
    
    // Actualizar la interfaz 
    panelHorario.revalidate();
    panelHorario.repaint();
}

    
    private JPanel crearCeldaHorario(String estado, String materia, int paralelo, String semestre, String carrera, Color color) {
        RoundedPanel celda = new RoundedPanel(10);
        celda.setLayout(new BoxLayout(celda, BoxLayout.Y_AXIS));
        celda.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        celda.setBackground(color);
        celda.setPreferredSize(new Dimension(120, 80));
        
        // Si hay materia, mostrar los detalles
        if (!materia.isEmpty()) {
            JLabel lblMateria = new JLabel("Materia: " + materia, SwingConstants.CENTER);
            JLabel lblParalelo = new JLabel("Paralelo: " + paralelo, SwingConstants.CENTER);
            JLabel lblSemestre = new JLabel("Semestre: " + semestre, SwingConstants.CENTER);
            JLabel lblCarrera = new JLabel("Carrera: " + carrera, SwingConstants.CENTER);
            JLabel lblEstado = new JLabel("Estado: " + estado, SwingConstants.CENTER);
            
            for (JLabel lbl : new JLabel[]{lblMateria, lblParalelo, lblSemestre, lblCarrera, lblEstado}) {
                lbl.setFont(new Font("Arial", Font.PLAIN, 10));
                lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
                celda.add(lbl);
            }
        } else {
            // Si no hay materia, mostrar el estado
            JLabel lblEstado = new JLabel(estado, SwingConstants.CENTER);
            lblEstado.setFont(new Font("Arial", Font.BOLD, 12));
            lblEstado.setAlignmentX(Component.CENTER_ALIGNMENT);
            celda.add(Box.createVerticalGlue());
            celda.add(lblEstado);
            celda.add(Box.createVerticalGlue());
        }
        
        return celda;
    }
    
    private void agregarLeyendaColor(JPanel panel, String texto, Color color) {
        JPanel colorBox = new RoundedPanel(10);
        colorBox.setBackground(color);
        colorBox.setPreferredSize(new Dimension(20, 20));
        colorBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        panel.add(colorBox);
        panel.add(new JLabel(texto));
        // Añadir un poco de espacio entre cada elemento de la leyenda
        panel.add(Box.createHorizontalStrut(15));
    }
    
    // Clase interna para paneles con esquinas redondeadas
    private class RoundedPanel extends JPanel {
        private int cornerRadius;
        
        public RoundedPanel(int radius) {
            this.cornerRadius = radius;
            setOpaque(false);
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);
            g2.setColor(Color.BLACK);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);
            g2.dispose();
        }
    }
}