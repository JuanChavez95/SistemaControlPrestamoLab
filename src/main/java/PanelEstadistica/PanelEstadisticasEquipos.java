/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PanelEstadistica;

import Clases.Equipos;
import Controles.ControladorEquipo;
import Controles.ControladorHistorialEquipos;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.util.Rotation;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Panel para mostrar estadísticas de equipos con gráficos en forma de torta
 * Autor: Equipo Soldados Caídos
 */
public class PanelEstadisticasEquipos extends JPanel {

    private ControladorEquipo controlEquipo;
    private ControladorHistorialEquipos controlHistorial;
    
    // Componentes de la interfaz
    private JPanel panelGraficos;
    private JPanel panelResumen;
    private JLabel lblTotalEquipos;
    private JLabel lblEquiposOperativos;
    private JLabel lblEquiposMantenimiento;
    private JLabel lblEquiposBaja;

    /**
     * Constructor del panel de estadísticas
     */
    public PanelEstadisticasEquipos() {
        controlEquipo = new ControladorEquipo();
        controlHistorial = new ControladorHistorialEquipos();
        
        // Configurar el panel
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Inicializar componentes
        inicializarComponentes();
        
        // Cargar datos estadísticos
        cargarEstadisticas();
    }
    
    /**
     * Inicializa los componentes de la interfaz
     */
    private void inicializarComponentes() {
        // Panel de gráficos
        panelGraficos = new JPanel(new GridLayout(1, 2, 15, 15));
        panelGraficos.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        // Panel de resumen
        panelResumen = new JPanel();
        panelResumen.setLayout(new BoxLayout(panelResumen, BoxLayout.Y_AXIS));
        panelResumen.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), 
                "Resumen de Equipos",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14)
        ));
        
        // Etiquetas para el resumen
        lblTotalEquipos = crearLabelEstadistica("Total de Equipos: ");
        lblEquiposOperativos = crearLabelEstadistica("Equipos Operativos: ");
        lblEquiposMantenimiento = crearLabelEstadistica("Equipos en Mantenimiento: ");
        lblEquiposBaja = crearLabelEstadistica("Equipos de Baja: ");
        
        // Agregar etiquetas al panel de resumen
        panelResumen.add(Box.createVerticalStrut(15));
        panelResumen.add(lblTotalEquipos);
        panelResumen.add(Box.createVerticalStrut(10));
        panelResumen.add(lblEquiposOperativos);
        panelResumen.add(Box.createVerticalStrut(10));
        panelResumen.add(lblEquiposMantenimiento);
        panelResumen.add(Box.createVerticalStrut(10));
        panelResumen.add(lblEquiposBaja);
        panelResumen.add(Box.createVerticalStrut(15));
        
        // Agregar componentes al panel principal
        add(panelGraficos, BorderLayout.CENTER);
        add(panelResumen, BorderLayout.SOUTH);
    }
    
    /**
     * Crea una etiqueta con estilo para mostrar estadísticas
     */
    private JLabel crearLabelEstadistica(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }
    
    /**
     * Carga las estadísticas y actualiza la interfaz
     */
    public void cargarEstadisticas() {
        try {
            // Obtener la lista de equipos
            List<Equipos> listaEquipos = controlEquipo.listar();
            
            // Contadores para las estadísticas
            int totalEquipos = listaEquipos.size();
            int equiposOperativos = 0;
            int equiposMantenimiento = 0;
            int equiposBaja = 0;
            
            // Mapas para estadísticas adicionales
            Map<String, Integer> estadisticasPorLaboratorio = new HashMap<>();
            Map<String, Integer> estadisticasPorProcesador = new HashMap<>();
            
            // Procesar cada equipo
            for (Equipos equipo : listaEquipos) {
                // Contar por estado
                switch (equipo.getEstado().toLowerCase()) {
                    case "disponible":
                        equiposOperativos++;
                        break;
                    case "no disponible":
                        equiposMantenimiento++;
                        break;
                    case "de baja":
                        equiposBaja++;
                        break;
                }
                
                // Contar por laboratorio
                int idLab = equipo.getIdLaboratorio();
                String labKey = "Lab " + idLab;
                estadisticasPorLaboratorio.put(labKey, 
                    estadisticasPorLaboratorio.getOrDefault(labKey, 0) + 1);
                
                // Contar por procesador
                String procesador = equipo.getProcesador();
                if (procesador != null && !procesador.isEmpty()) {
                    // Simplificar el nombre del procesador para estadísticas
                    String procSimple = simplificarNombreProcesador(procesador);
                    estadisticasPorProcesador.put(procSimple, 
                        estadisticasPorProcesador.getOrDefault(procSimple, 0) + 1);
                }
            }
            
            // Actualizar las etiquetas de resumen
            lblTotalEquipos.setText("Total de Equipos: " + totalEquipos);
            lblEquiposOperativos.setText("Equipos Operativos: " + equiposOperativos + 
                    " (" + calcularPorcentaje(equiposOperativos, totalEquipos) + "%)");
            lblEquiposMantenimiento.setText("Equipos en Mantenimiento: " + equiposMantenimiento + 
                    " (" + calcularPorcentaje(equiposMantenimiento, totalEquipos) + "%)");
            lblEquiposBaja.setText("Equipos de Baja: " + equiposBaja + 
                    " (" + calcularPorcentaje(equiposBaja, totalEquipos) + "%)");
            
            // Limpiar el panel de gráficos
            panelGraficos.removeAll();
            
            // Crear y añadir gráficos de torta
            ChartPanel graficoPorEstado = crearGraficoEstadoEquipos(equiposOperativos, equiposMantenimiento, equiposBaja);
            ChartPanel graficoPorLaboratorio = crearGraficoDistribucionPorLaboratorio(estadisticasPorLaboratorio);
            
            panelGraficos.add(graficoPorEstado);
            panelGraficos.add(graficoPorLaboratorio);
            
            // Refrescar la interfaz
            revalidate();
            repaint();
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                    "Error al cargar las estadísticas: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    /**
     * Simplifica el nombre del procesador para estadísticas más claras
     */
    private String simplificarNombreProcesador(String nombreCompleto) {
        nombreCompleto = nombreCompleto.toUpperCase();
        
        if (nombreCompleto.contains("INTEL")) {
            if (nombreCompleto.contains("I3")) return "Intel i3";
            if (nombreCompleto.contains("I5")) return "Intel i5";
            if (nombreCompleto.contains("I7")) return "Intel i7";
            if (nombreCompleto.contains("I9")) return "Intel i9";
            if (nombreCompleto.contains("PENTIUM")) return "Intel Pentium";
            if (nombreCompleto.contains("CELERON")) return "Intel Celeron";
            return "Intel Otro";
        }
        else if (nombreCompleto.contains("AMD")) {
            if (nombreCompleto.contains("RYZEN")) return "AMD Ryzen";
            if (nombreCompleto.contains("ATHLON")) return "AMD Athlon";
            return "AMD Otro";
        }
        
        return "Otro";
    }
    
    /**
     * Calcula el porcentaje con un decimal
     */
    private double calcularPorcentaje(int parte, int total) {
        if (total == 0) return 0;
        return Math.round((double) parte / total * 1000) / 10.0;
    }
    
    /**
     * Crea un gráfico de torta 3D para la distribución por estado de los equipos
     */
    private ChartPanel crearGraficoEstadoEquipos(int operativos, int mantenimiento, int baja) {
        // Crear el dataset
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        dataset.setValue("Disponible", operativos);
        dataset.setValue("No Disponible", mantenimiento);
        dataset.setValue("Baja", baja);
        
        // Crear el gráfico
        JFreeChart chart = ChartFactory.createPieChart3D(
                "Estado de los Equipos",  // título
                dataset,                   // datos
                true,                      // incluir leyenda
                true,                      // tooltips
                false                      // URLs
        );
        
        // Personalizar el gráfico
        PiePlot3D plot = (PiePlot3D) chart.getPlot();
        plot.setStartAngle(290);
        plot.setDirection(Rotation.CLOCKWISE);
        plot.setForegroundAlpha(0.8f);
        plot.setNoDataMessage("No hay datos disponibles");
        
        // Establecer colores específicos
        plot.setSectionPaint("Dsiponible", new Color(0, 128, 0));     // Verde
        plot.setSectionPaint("No Disponible", new Color(255, 165, 0)); // Naranja
        plot.setSectionPaint("Baja", new Color(178, 34, 34));         // Rojo oscuro
        
        // Crear el panel para el gráfico
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        chartPanel.setPreferredSize(new Dimension(400, 300));
        
        return chartPanel;
    }
    
    /**
     * Crea un gráfico de torta para la distribución de equipos por laboratorio
     */
    private ChartPanel crearGraficoDistribucionPorLaboratorio(Map<String, Integer> datosPorLaboratorio) {
        // Crear el dataset
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        
        // Agregar datos al dataset
        for (Map.Entry<String, Integer> entry : datosPorLaboratorio.entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }
        
        // Crear el gráfico
        JFreeChart chart = ChartFactory.createPieChart(
                "Distribución por Laboratorio",  // título
                dataset,                         // datos
                true,                            // incluir leyenda
                true,                            // tooltips
                false                            // URLs
        );
        
        // Personalizar el gráfico
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setNoDataMessage("No hay datos disponibles");
        plot.setCircular(true);
        
        // Crear el panel para el gráfico
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        chartPanel.setPreferredSize(new Dimension(400, 300));
        
        return chartPanel;
    }
    
    /**
     * Actualiza los gráficos y estadísticas
     */
    public void actualizarEstadisticas() {
        cargarEstadisticas();
    }
}