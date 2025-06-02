/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package PanelesMateriales;

import Controles.*;
import Clases.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.*;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

//Mejoras para las predicciones del algoritmo genético
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.Properties;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.File;

/**
 * Panel de Predicción Simplificado para Equipamientos e Insumos
 * Proporciona recomendaciones básicas para mantenimiento y reposición
 */
public class PanelPrediccionEquipamientoInsumos extends JPanel {
    
    // Controladores
    private ControladorEquipamiento controladorEquipamiento;
    private ControladorInsumo controladorInsumo;
    private ControladorPrestamo controladorPrestamo;
    private ControladorHistorialEquipamiento controladorHistorialEquipamiento;
    
    // Componentes de la interfaz
    private JTabbedPane tabbedPane;
    
    // Panel de Equipamientos
    private JTable tablaPrediccionEquipamientos;
    private DefaultTableModel modeloEquipamientos;
    private JTextArea areaRecomendacionesEquipamientos;
    
    // Panel de Insumos
    private JTable tablaPrediccionInsumos;
    private DefaultTableModel modeloInsumos;
    private JTextArea areaRecomendacionesInsumos;
    
    // Barra de progreso
    private JProgressBar progressBar;
    
    private AlgoritmoGenetico.Individuo mejorIndividuo;
    private List<ResultadoHistorico> historicoResultados;
    private JLabel labelPrediccionIA;
    private JButton btnEntrenarIA;

    public PanelPrediccionEquipamientoInsumos() {
        inicializarControladores();
        inicializarComponentes();
        configurarLayout();
        cargarDatosIniciales();
    }
    
    private void inicializarControladores() {
        controladorEquipamiento = new ControladorEquipamiento();
        controladorInsumo = new ControladorInsumo();
        controladorPrestamo = new ControladorPrestamo();
        controladorHistorialEquipamiento = new ControladorHistorialEquipamiento();
    }
    
    private void inicializarComponentes() {
        setLayout(new BorderLayout());

        // Inicializar componentes de IA
        historicoResultados = new ArrayList<>();
        cargarHistoricoResultados(); // Cargar datos históricos si existen

        // Panel principal con pestañas
        tabbedPane = new JTabbedPane();

        // Pestaña de Equipamientos (modificada)
        JPanel panelEquipamientos = crearPanelEquipamientos();
        tabbedPane.addTab("Predicción Equipamientos", panelEquipamientos);

        // Pestaña de Insumos (modificada)
        JPanel panelInsumos = crearPanelInsumos();
        tabbedPane.addTab("Predicción Insumos", panelInsumos);

        // Nueva pestaña de IA
        JPanel panelIA = crearPanelIA();
        tabbedPane.addTab("Inteligencia Artificial", panelIA);

        add(tabbedPane, BorderLayout.CENTER);

        // Panel inferior con info de IA y barra de progreso
        JPanel panelInferior = new JPanel(new BorderLayout());

        labelPrediccionIA = new JLabel("IA: No entrenada");
        labelPrediccionIA.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        panelInferior.add(labelPrediccionIA, BorderLayout.WEST);

        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setString("Listo");
        panelInferior.add(progressBar, BorderLayout.CENTER);

        add(panelInferior, BorderLayout.SOUTH);
    }
    
    private JPanel crearPanelIA() {
        JPanel panel = new JPanel(new BorderLayout());

        // Panel de controles de IA
        JPanel panelControles = new JPanel(new FlowLayout());

        btnEntrenarIA = new JButton("Entrenar IA");
        btnEntrenarIA.addActionListener(e -> entrenarAlgoritmoGenetico());

        JButton btnEvaluarIA = new JButton("Evaluar Rendimiento");
        btnEvaluarIA.addActionListener(e -> evaluarRendimientoIA());

        JButton btnResetIA = new JButton("Resetear IA");
        btnResetIA.addActionListener(e -> resetearIA());

        panelControles.add(btnEntrenarIA);
        panelControles.add(btnEvaluarIA);
        panelControles.add(btnResetIA);

        // Área de información de la IA
        JTextArea infoIA = new JTextArea(20, 60);
        infoIA.setEditable(false);
        infoIA.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        infoIA.setBorder(new TitledBorder("Información del Algoritmo Genético"));

        if (mejorIndividuo != null) {
            StringBuilder info = new StringBuilder();
            info.append("=== CONFIGURACIÓN ACTUAL DE LA IA ===\n\n");
            info.append(String.format("Peso Estado: %.3f\n", mejorIndividuo.pesoEstado));
            info.append(String.format("Peso Días sin Actividad: %.3f\n", mejorIndividuo.pesoDiasSinActividad));
            info.append(String.format("Peso Préstamos: %.3f\n", mejorIndividuo.pesoPrestamos));
            info.append(String.format("Peso Historial Mantenimiento: %.3f\n", mejorIndividuo.pesoHistorialMant));
            info.append(String.format("Peso Consumo: %.3f\n", mejorIndividuo.pesoConsumo));
            info.append(String.format("Peso Cantidad: %.3f\n\n", mejorIndividuo.pesoCantidad));

            info.append("=== UMBRALES OPTIMIZADOS ===\n");
            info.append(String.format("Días para Reparación: %d\n", mejorIndividuo.umbralDiasReparacion));
            info.append(String.format("Préstamos Alto: %d\n", mejorIndividuo.umbralPrestamosAlto));
            info.append(String.format("Cantidad Crítica: %d\n", mejorIndividuo.umbralCantidadCritica));
            info.append(String.format("Consumo Diario Crítico: %.2f\n\n", mejorIndividuo.umbralConsumoDiario));

            info.append(String.format("Fitness Score: %.2f\n", mejorIndividuo.fitness));
            info.append(String.format("Datos de Entrenamiento: %d registros\n", historicoResultados.size()));

            infoIA.setText(info.toString());
        } else {
            infoIA.setText("La IA no ha sido entrenada aún.\n\nHaga clic en 'Entrenar IA' para comenzar el proceso de optimización.");
        }

        JScrollPane scrollInfo = new JScrollPane(infoIA);

        panel.add(panelControles, BorderLayout.NORTH);
        panel.add(scrollInfo, BorderLayout.CENTER);

        return panel;
    }
    
    
    
    private void cargarHistoricoResultados() {
    // Aquí cargarías datos históricos de una base de datos o archivo
    // Por ahora, generar algunos datos de ejemplo
    historicoResultados = new ArrayList<>();
    
    // Simular algunos resultados históricos
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.MONTH, -6);
    
    for (int i = 1; i <= 20; i++) {
        ResultadoHistorico resultado = new ResultadoHistorico(
            i, 
            i % 2 == 0 ? "equipamiento" : "insumo",
            "Normal",
            i % 4 == 0 ? "Reparación" : "Normal",
            cal.getTime()
        );
        historicoResultados.add(resultado);
        cal.add(Calendar.DAY_OF_MONTH, 7);
    }
}

private void evaluarRendimientoIA() {
    if (mejorIndividuo == null) {
        JOptionPane.showMessageDialog(this, 
            "Primero debe entrenar la IA antes de evaluar su rendimiento.",
            "IA No Entrenada", JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    StringBuilder evaluacion = new StringBuilder();
    evaluacion.append("=== EVALUACIÓN DE RENDIMIENTO DE LA IA ===\n\n");
    evaluacion.append(String.format("Fitness Score: %.2f\n", mejorIndividuo.fitness));
    evaluacion.append(String.format("Datos de Entrenamiento: %d registros\n\n", historicoResultados.size()));
    
    // Calcular estadísticas de predicción
    int prediccionesCorrectas = 0;
    int totalPredicciones = historicoResultados.size();
    
    for (ResultadoHistorico hist : historicoResultados) {
        if (compararPredicciones(hist.prediccionOriginal, hist.resultadoReal)) {
            prediccionesCorrectas++;
        }
    }
    
    double tasaAcierto = totalPredicciones > 0 ? (double) prediccionesCorrectas / totalPredicciones : 0;
    evaluacion.append(String.format("Tasa de Acierto: %.1f%%\n", tasaAcierto * 100));
    evaluacion.append(String.format("Predicciones Correctas: %d de %d\n\n", prediccionesCorrectas, totalPredicciones));
    
    evaluacion.append("=== PARÁMETROS OPTIMIZADOS ===\n");
    evaluacion.append(String.format("Umbral Días Reparación: %d días\n", mejorIndividuo.umbralDiasReparacion));
    evaluacion.append(String.format("Umbral Préstamos Alto: %d\n", mejorIndividuo.umbralPrestamosAlto));
    evaluacion.append(String.format("Umbral Cantidad Crítica: %d unidades\n", mejorIndividuo.umbralCantidadCritica));
    
    JOptionPane.showMessageDialog(this, evaluacion.toString(), 
        "Evaluación de Rendimiento IA", JOptionPane.INFORMATION_MESSAGE);
}

private void entrenarAlgoritmoGenetico() {
    SwingWorker<Void, String> worker = new SwingWorker<Void, String>() {
        @Override
        protected Void doInBackground() throws Exception {
            publish("Iniciando entrenamiento de IA...");
            progressBar.setIndeterminate(true);
            btnEntrenarIA.setEnabled(false);
            
            try {
                // Cargar datos actuales para entrenamiento
                List<Equipamiento> equipamientos = controladorEquipamiento.listar();
                List<AnalisisEquipamiento> analisisEquipos = new ArrayList<>();
                
                publish("Analizando equipamientos...");
                for (Equipamiento equipo : equipamientos) {
                    analisisEquipos.add(analizarEquipamiento(equipo));
                    Thread.sleep(10); // Simular procesamiento
                }
                
                List<Insumo> insumos = controladorInsumo.listar();
                List<AnalisisInsumo> analisisInsumos = new ArrayList<>();
                
                publish("Analizando insumos...");
                for (Insumo insumo : insumos) {
                    analisisInsumos.add(analizarInsumo(insumo));
                    Thread.sleep(10);
                }
                
                publish("Ejecutando algoritmo genético...");
                mejorIndividuo = AlgoritmoGenetico.evolucionar(analisisEquipos, analisisInsumos, historicoResultados);
                
                publish("IA entrenada exitosamente");
                
            } catch (SQLException e) {
                publish("Error durante el entrenamiento: " + e.getMessage());
            }
            
            return null;
        }
        
        @Override
        protected void process(List<String> chunks) {
            for (String mensaje : chunks) {
                progressBar.setString(mensaje);
            }
        }
        
        @Override
        protected void done() {
            progressBar.setIndeterminate(false);
            btnEntrenarIA.setEnabled(true);
            
            if (mejorIndividuo != null) {
                labelPrediccionIA.setText(String.format("IA: Entrenada (Fitness: %.2f)", mejorIndividuo.fitness));
                progressBar.setString("IA entrenada y lista para usar");
                
                // Actualizar panel de información
                actualizarInfoIA();
                
                JOptionPane.showMessageDialog(PanelPrediccionEquipamientoInsumos.this,
                    "¡Entrenamiento completado!\n\nLa IA ha sido optimizada y está lista para mejorar las predicciones.",
                    "Entrenamiento Exitoso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                progressBar.setString("Error en el entrenamiento");
            }
        }
    };
    worker.execute();
}


private AnalisisEquipamiento analizarEquipamientoConIA(Equipamiento equipo) {
    AnalisisEquipamiento analisis = analizarEquipamiento(equipo); // Análisis original
    
    if (mejorIndividuo != null) {
        // Aplicar predicción mejorada con IA
        String prediccionIA = AlgoritmoGenetico.predecirEquipamiento(mejorIndividuo, analisis);
        
        // Combinar predicción original con IA (dar más peso a la IA si está entrenada)
        if (!prediccionIA.equals(analisis.recomendacion)) {
            analisis.recomendacion = prediccionIA + " (IA)";
        }
    }
    
    return analisis;
}

private AnalisisInsumo analizarInsumoConIA(Insumo insumo) {
    AnalisisInsumo analisis = analizarInsumo(insumo); // Análisis original
    
    if (mejorIndividuo != null) {
        // Aplicar predicción mejorada con IA
        String prediccionIA = AlgoritmoGenetico.predecirInsumo(mejorIndividuo, analisis);
        
        if (!prediccionIA.equals(analisis.recomendacion)) {
            analisis.recomendacion = prediccionIA + " (IA)";
        }
    }
    
    return analisis;
}

private void actualizarPrediccionesEquipamientosConIA() {
    SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
        @Override
        protected Void doInBackground() throws Exception {
            progressBar.setString("Analizando equipamientos con IA...");
            progressBar.setIndeterminate(true);
            
            modeloEquipamientos.setRowCount(0);
            StringBuilder recomendaciones = new StringBuilder();
            
            try {
                List<Equipamiento> equipamientos = controladorEquipamiento.listar();
                List<AnalisisEquipamiento> analisis = new ArrayList<>();
                
                for (Equipamiento equipo : equipamientos) {
                    // Usar análisis con IA si está disponible
                    AnalisisEquipamiento analisisEquipo = mejorIndividuo != null ? 
                        analizarEquipamientoConIA(equipo) : analizarEquipamiento(equipo);
                    analisis.add(analisisEquipo);
                }
                
                // Ordenar por prioridad de recomendación
                analisis.sort((a1, a2) -> obtenerPrioridadNumerica(a2.recomendacion) - obtenerPrioridadNumerica(a1.recomendacion));
                
                // Agregar a la tabla
                for (AnalisisEquipamiento a : analisis) {
                    modeloEquipamientos.addRow(new Object[]{
                        a.equipamiento.getIdEquipamiento(),
                        a.equipamiento.getNombreEquipamiento() + " (" + a.equipamiento.getMarca() + ")",
                        a.equipamiento.getEstado(),
                        a.ultimaActividad,
                        a.totalPrestamos,
                        a.recomendacion
                    });
                }
                
                // Generar recomendaciones detalladas
                generarRecomendacionesEquipamientos(analisis, recomendaciones);
                
                // Agregar información de IA si está activa
                if (mejorIndividuo != null) {
                    recomendaciones.append("\n=== ANÁLISIS CON INTELIGENCIA ARTIFICIAL ===\n");
                    recomendaciones.append(String.format("Fitness Score: %.2f\n", mejorIndividuo.fitness));
                    
                    long recomendacionesIA = analisis.stream()
                        .filter(a -> a.recomendacion.contains("(IA)"))
                        .count();
                    
                    recomendaciones.append(String.format("Recomendaciones optimizadas por IA: %d de %d\n", 
                        recomendacionesIA, analisis.size()));
                }
                
                SwingUtilities.invokeLater(() -> {
                    areaRecomendacionesEquipamientos.setText(recomendaciones.toString());
                    areaRecomendacionesEquipamientos.setCaretPosition(0);
                });
                
            } catch (SQLException e) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(PanelPrediccionEquipamientoInsumos.this,
                        "Error al cargar predicciones de equipamientos: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                });
            }
            
            return null;
        }
        
        @Override
        protected void done() {
            progressBar.setIndeterminate(false);
            String mensaje = mejorIndividuo != null ? 
                "Predicciones actualizadas con IA" : "Predicciones actualizadas";
            progressBar.setString(mensaje);
        }
    };
    worker.execute();
}

private void actualizarPrediccionesInsumosConIA() {
    SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
        @Override
        protected Void doInBackground() throws Exception {
            progressBar.setString("Analizando insumos con IA...");
            progressBar.setIndeterminate(true);
            
            modeloInsumos.setRowCount(0);
            StringBuilder recomendaciones = new StringBuilder();
            
            try {
                List<Insumo> insumos = controladorInsumo.listar();
                List<AnalisisInsumo> analisis = new ArrayList<>();
                
                for (Insumo insumo : insumos) {
                    // Usar análisis con IA si está disponible
                    AnalisisInsumo analisisInsumo = mejorIndividuo != null ? 
                        analizarInsumoConIA(insumo) : analizarInsumo(insumo);
                    analisis.add(analisisInsumo);
                }
                
                // Ordenar por prioridad de reposición
                analisis.sort((a1, a2) -> obtenerPrioridadNumerica(a2.recomendacion) - obtenerPrioridadNumerica(a1.recomendacion));
                
                // Agregar a la tabla
                for (AnalisisInsumo a : analisis) {
                    modeloInsumos.addRow(new Object[]{
                        a.insumo.getIdInsumo(),
                        a.insumo.getNombreInsumo(),
                        a.insumo.getCantidad(),
                        String.format("%.2f", a.consumoDiario),
                        a.estado,
                        a.recomendacion
                    });
                }
                
                // Generar recomendaciones detalladas
                generarRecomendacionesInsumos(analisis, recomendaciones);
                
                // Agregar información de IA si está activa
                if (mejorIndividuo != null) {
                    recomendaciones.append("\n=== ANÁLISIS CON INTELIGENCIA ARTIFICIAL ===\n");
                    recomendaciones.append(String.format("Fitness Score: %.2f\n", mejorIndividuo.fitness));
                    
                    long recomendacionesIA = analisis.stream()
                        .filter(a -> a.recomendacion.contains("(IA)"))
                        .count();
                    
                    recomendaciones.append(String.format("Recomendaciones optimizadas por IA: %d de %d\n", 
                        recomendacionesIA, analisis.size()));
                    
                    // Estadísticas adicionales de insumos
                    Map<String, Long> estadisticasIA = analisis.stream()
                        .filter(a -> a.recomendacion.contains("(IA)"))
                        .collect(java.util.stream.Collectors.groupingBy(
                            a -> a.recomendacion.replace(" (IA)", ""),
                            java.util.stream.Collectors.counting()
                        ));
                    
                    recomendaciones.append("\nDistribución de recomendaciones IA:\n");
                    estadisticasIA.forEach((rec, count) -> 
                        recomendaciones.append(String.format("- %s: %d\n", rec, count)));
                }
                
                SwingUtilities.invokeLater(() -> {
                    areaRecomendacionesInsumos.setText(recomendaciones.toString());
                    areaRecomendacionesInsumos.setCaretPosition(0);
                });
                
            } catch (SQLException e) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(PanelPrediccionEquipamientoInsumos.this,
                        "Error al cargar predicciones de insumos: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                });
            }
            
            return null;
        }
        
        @Override
        protected void done() {
            progressBar.setIndeterminate(false);
            String mensaje = mejorIndividuo != null ? 
                "Predicciones de insumos actualizadas con IA" : "Predicciones de insumos actualizadas";
            progressBar.setString(mensaje);
        }
    };
    worker.execute();
}

// 10. MÉTODO PARA GUARDAR Y CARGAR CONFIGURACIÓN DE IA
private void guardarConfiguracionIA() {
    if (mejorIndividuo == null) return;
    
    try {
        // Aquí podrías guardar la configuración en un archivo o base de datos
        Properties config = new Properties();
        config.setProperty("pesoEstado", String.valueOf(mejorIndividuo.pesoEstado));
        config.setProperty("pesoDiasSinActividad", String.valueOf(mejorIndividuo.pesoDiasSinActividad));
        config.setProperty("pesoPrestamos", String.valueOf(mejorIndividuo.pesoPrestamos));
        config.setProperty("pesoHistorialMant", String.valueOf(mejorIndividuo.pesoHistorialMant));
        config.setProperty("pesoConsumo", String.valueOf(mejorIndividuo.pesoConsumo));
        config.setProperty("pesoCantidad", String.valueOf(mejorIndividuo.pesoCantidad));
        config.setProperty("umbralDiasReparacion", String.valueOf(mejorIndividuo.umbralDiasReparacion));
        config.setProperty("umbralPrestamosAlto", String.valueOf(mejorIndividuo.umbralPrestamosAlto));
        config.setProperty("umbralCantidadCritica", String.valueOf(mejorIndividuo.umbralCantidadCritica));
        config.setProperty("umbralConsumoDiario", String.valueOf(mejorIndividuo.umbralConsumoDiario));
        config.setProperty("fitness", String.valueOf(mejorIndividuo.fitness));
        
        // Guardar en archivo
        java.io.FileOutputStream fos = new java.io.FileOutputStream("ia_config.properties");
        config.store(fos, "Configuración de IA - Algoritmo Genético");
        fos.close();
        
    } catch (Exception e) {
        System.err.println("Error al guardar configuración de IA: " + e.getMessage());
    }
}

private void cargarConfiguracionIA() {
    try {
        java.io.File configFile = new java.io.File("ia_config.properties");
        if (!configFile.exists()) return;
        
        Properties config = new Properties();
        java.io.FileInputStream fis = new java.io.FileInputStream(configFile);
        config.load(fis);
        fis.close();
        
        // Recrear individuo desde configuración guardada
        mejorIndividuo = new AlgoritmoGenetico.Individuo();
        mejorIndividuo.pesoEstado = Double.parseDouble(config.getProperty("pesoEstado", "0.5"));
        mejorIndividuo.pesoDiasSinActividad = Double.parseDouble(config.getProperty("pesoDiasSinActividad", "0.5"));
        mejorIndividuo.pesoPrestamos = Double.parseDouble(config.getProperty("pesoPrestamos", "0.5"));
        mejorIndividuo.pesoHistorialMant = Double.parseDouble(config.getProperty("pesoHistorialMant", "0.5"));
        mejorIndividuo.pesoConsumo = Double.parseDouble(config.getProperty("pesoConsumo", "0.5"));
        mejorIndividuo.pesoCantidad = Double.parseDouble(config.getProperty("pesoCantidad", "0.5"));
        mejorIndividuo.umbralDiasReparacion = Integer.parseInt(config.getProperty("umbralDiasReparacion", "90"));
        mejorIndividuo.umbralPrestamosAlto = Integer.parseInt(config.getProperty("umbralPrestamosAlto", "35"));
        mejorIndividuo.umbralCantidadCritica = Integer.parseInt(config.getProperty("umbralCantidadCritica", "10"));
        mejorIndividuo.umbralConsumoDiario = Double.parseDouble(config.getProperty("umbralConsumoDiario", "1.0"));
        mejorIndividuo.fitness = Double.parseDouble(config.getProperty("fitness", "0.0"));
        
        labelPrediccionIA.setText(String.format("IA: Cargada (Fitness: %.2f)", mejorIndividuo.fitness));
        
    } catch (Exception e) {
        System.err.println("Error al cargar configuración de IA: " + e.getMessage());
        mejorIndividuo = null;
    }
}

// 11. MÉTODOS PARA EXPORTAR REPORTES CON IA
private void exportarReporteIA() {
    if (mejorIndividuo == null) {
        JOptionPane.showMessageDialog(this,
            "No hay datos de IA para exportar. Entrene la IA primero.",
            "Sin Datos de IA", JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    try {
        StringBuilder reporte = new StringBuilder();
        reporte.append("=== REPORTE DE INTELIGENCIA ARTIFICIAL ===\n");
        reporte.append("Fecha de generación: ").append(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())).append("\n\n");
        
        reporte.append("=== CONFIGURACIÓN OPTIMIZADA ===\n");
        reporte.append(String.format("Peso Estado: %.4f\n", mejorIndividuo.pesoEstado));
        reporte.append(String.format("Peso Días sin Actividad: %.4f\n", mejorIndividuo.pesoDiasSinActividad));
        reporte.append(String.format("Peso Préstamos: %.4f\n", mejorIndividuo.pesoPrestamos));
        reporte.append(String.format("Peso Historial Mantenimiento: %.4f\n", mejorIndividuo.pesoHistorialMant));
        reporte.append(String.format("Peso Consumo: %.4f\n", mejorIndividuo.pesoConsumo));
        reporte.append(String.format("Peso Cantidad: %.4f\n\n", mejorIndividuo.pesoCantidad));
        
        reporte.append("=== UMBRALES DINÁMICOS ===\n");
        reporte.append(String.format("Días para Reparación: %d días\n", mejorIndividuo.umbralDiasReparacion));
        reporte.append(String.format("Préstamos considerados Alto: %d\n", mejorIndividuo.umbralPrestamosAlto));
        reporte.append(String.format("Cantidad Crítica de Insumos: %d unidades\n", mejorIndividuo.umbralCantidadCritica));
        reporte.append(String.format("Consumo Diario Crítico: %.2f unidades/día\n\n", mejorIndividuo.umbralConsumoDiario));
        
        reporte.append("=== MÉTRICAS DE RENDIMIENTO ===\n");
        reporte.append(String.format("Fitness Score: %.4f\n", mejorIndividuo.fitness));
        reporte.append(String.format("Tasa de Acierto: %.1f%%\n", calcularTasaAcierto() * 100));
        reporte.append(String.format("Datos de Entrenamiento: %d registros\n", historicoResultados.size()));
        reporte.append(String.format("Generaciones Evolutivas: %d\n", AlgoritmoGenetico.GENERACIONES));
        reporte.append(String.format("Tamaño de Población: %d\n\n", AlgoritmoGenetico.TAMAÑO_POBLACION));
        
        reporte.append("=== PARÁMETROS DEL ALGORITMO ===\n");
        reporte.append(String.format("Tasa de Mutación: %.1f%%\n", AlgoritmoGenetico.TASA_MUTACION * 100));
        reporte.append(String.format("Tasa de Cruce: %.1f%%\n", AlgoritmoGenetico.TASA_CRUCE * 100));
        
        // Guardar reporte en archivo
        javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();
        fileChooser.setDialogTitle("Exportar Reporte de IA");
        fileChooser.setSelectedFile(new java.io.File("reporte_ia_" + 
            new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".txt"));
        
        if (fileChooser.showSaveDialog(this) == javax.swing.JFileChooser.APPROVE_OPTION) {
            java.io.FileWriter writer = new java.io.FileWriter(fileChooser.getSelectedFile());
            writer.write(reporte.toString());
            writer.close();
            
            JOptionPane.showMessageDialog(this,
                "Reporte de IA exportado exitosamente.",
                "Exportación Exitosa", JOptionPane.INFORMATION_MESSAGE);
        }
        
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
            "Error al exportar reporte: " + e.getMessage(),
            "Error de Exportación", JOptionPane.ERROR_MESSAGE);
    }
}

// 12. OVERRIDE DEL CONSTRUCTOR PRINCIPAL PARA INCLUIR INICIALIZACIÓN COMPLETA
public void inicializar() {
    // Cargar configuración de IA si existe
    cargarConfiguracionIA();
    
    // Actualizar interfaz según estado de IA
    if (mejorIndividuo != null) {
        labelPrediccionIA.setText(String.format("IA: Cargada (Fitness: %.2f)", mejorIndividuo.fitness));
        progressBar.setString("IA lista para usar");
    }
}




// ====== CORRECCIÓN 4: Implementar métodos auxiliares faltantes ======
// AGREGAR ESTOS MÉTODOS:

private int contarPrestamosEquipamiento(int idEquipamiento) {
    try {
        // Implementar lógica para contar préstamos
        return controladorPrestamo.contarPrestamosPorEquipamiento(idEquipamiento);
    } catch (Exception e) {
        return 0;
    }
}

private String obtenerUltimaActividad(List<Object[]> historial) {
    if (historial == null || historial.isEmpty()) {
        return "Sin actividad";
    }
    
    try {
        // Asumir que la fecha está en la primera posición del array
        Object fecha = historial.get(0)[0];
        if (fecha instanceof Date) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            return sdf.format((Date) fecha);
        }
        return fecha.toString();
    } catch (Exception e) {
        return "Sin fecha";
    }
}

private String obtenerUltimaCategoriaMantenimiento(List<Object[]> historial) {
    if (historial == null || historial.isEmpty()) {
        return null;
    }
    
    try {
        // Asumir que la categoría está en una posición específica del array
        Object categoria = historial.get(0)[2]; // Ajustar índice según estructura
        return categoria != null ? categoria.toString() : null;
    } catch (Exception e) {
        return null;
    }
}

private int calcularDiasSinActividad(List<Object[]> historial) {
    if (historial == null || historial.isEmpty()) {
        return 365;
    }
    
    try {
        Object fecha = historial.get(0)[0];
        if (fecha instanceof Date) {
            long diffMs = System.currentTimeMillis() - ((Date) fecha).getTime();
            return (int) TimeUnit.MILLISECONDS.toDays(diffMs);
        }
    } catch (Exception e) {
        // Error al calcular
    }
    
    return 365;
}

private double calcularConsumoPromedio(int idInsumo) {
    try {
        // Implementar lógica de cálculo de consumo promedio
        // Por ahora retornar un valor por defecto
        return 1.0;
    } catch (Exception e) {
        return 0.0;
    }
}

private int obtenerPrioridadNumerica(String recomendacion) {
    if (recomendacion == null) return 0;
    
    String rec = recomendacion.toLowerCase();
    if (rec.contains("urgente") || rec.contains("baja")) return 4;
    if (rec.contains("reparacion") || rec.contains("necesaria")) return 3;
    if (rec.contains("monitorear")) return 2;
    return 1;
}

private void generarRecomendacionesEquipamientos(List<AnalisisEquipamiento> analisis, StringBuilder recomendaciones) {
    recomendaciones.append("=== RECOMENDACIONES DE MANTENIMIENTO ===\n\n");
    
    Map<String, List<AnalisisEquipamiento>> porRecomendacion = analisis.stream()
        .collect(java.util.stream.Collectors.groupingBy(a -> a.recomendacion));
    
    porRecomendacion.forEach((rec, lista) -> {
        recomendaciones.append(String.format("%s (%d equipamientos):\n", rec, lista.size()));
        lista.forEach(a -> recomendaciones.append(String.format("- %s\n", 
            a.equipamiento.getNombreEquipamiento())));
        recomendaciones.append("\n");
    });
}

private void generarRecomendacionesInsumos(List<AnalisisInsumo> analisis, StringBuilder recomendaciones) {
    recomendaciones.append("=== RECOMENDACIONES DE REPOSICIÓN ===\n\n");
    
    Map<String, List<AnalisisInsumo>> porRecomendacion = analisis.stream()
        .collect(java.util.stream.Collectors.groupingBy(a -> a.recomendacion));
    
    porRecomendacion.forEach((rec, lista) -> {
        recomendaciones.append(String.format("%s (%d insumos):\n", rec, lista.size()));
        lista.forEach(a -> recomendaciones.append(String.format("- %s (Cantidad: %d)\n", 
            a.insumo.getNombreInsumo(), a.insumo.getCantidad())));
        recomendaciones.append("\n");
    });
}


// 13. MÉTODO PARA LIMPIAR RECURSOS AL CERRAR
public void limpiarRecursos() {
    // Guardar configuración de IA antes de cerrar
    if (mejorIndividuo != null) {
        guardarConfiguracionIA();
    }
    
    // Limpiar listas
    if (historicoResultados != null) {
        historicoResultados.clear();
    }
}

// 14. GETTER PARA ACCESO A LA CONFIGURACIÓN DE IA DESDE OTRAS CLASES
public AlgoritmoGenetico.Individuo getConfiguracionIA() {
    return mejorIndividuo;
}

public boolean isIAEntrenada() {
    return mejorIndividuo != null;
}

public double getFitnessIA() {
    return mejorIndividuo != null ? mejorIndividuo.fitness : 0.0;
}

// 15. MÉTODO PARA AGREGAR RESULTADOS HISTÓRICOS DINÁMICAMENTE
public void agregarResultadoHistorico(int idItem, String tipo, String prediccion, String resultado) {
    ResultadoHistorico nuevo = new ResultadoHistorico(idItem, tipo, prediccion, resultado, new Date());
    historicoResultados.add(nuevo);
    
    // Mantener solo los últimos 100 registros para evitar memoria excesiva
    if (historicoResultados.size() > 100) {
        historicoResultados.remove(0);
    }
    
    // Si la IA está entrenada, actualizar label con nueva información
    if (mejorIndividuo != null) {
        labelPrediccionIA.setText(String.format("IA: Entrenada (Fitness: %.2f, Datos: %d)", 
            mejorIndividuo.fitness, historicoResultados.size()));
    }
}



private void resetearIA() {
    int confirmacion = JOptionPane.showConfirmDialog(this,
        "¿Está seguro de que desea resetear la IA?\nSe perderá todo el entrenamiento actual.",
        "Confirmar Reset", JOptionPane.YES_NO_OPTION);
    
    if (confirmacion == JOptionPane.YES_OPTION){
        mejorIndividuo = null;
        historicoResultados.clear();
        labelPrediccionIA.setText("IA: No entrenada");
        progressBar.setString("IA reseteada");
        
        // Actualizar panel de información
        actualizarInfoIA();
        
        JOptionPane.showMessageDialog(this,
            "La IA ha sido reseteada exitosamente.",
            "Reset Completado", JOptionPane.INFORMATION_MESSAGE);
    }
}

private void actualizarInfoIA() {
    // Buscar el área de texto de información de IA y actualizarla
    JTabbedPane tabbedPane = (JTabbedPane) getComponent(0);
    JPanel panelIA = (JPanel) tabbedPane.getComponentAt(2); // Pestaña de IA
    JScrollPane scrollPane = (JScrollPane) panelIA.getComponent(1);
    JTextArea infoIA = (JTextArea) scrollPane.getViewport().getView();
    
    if (mejorIndividuo != null) {
        StringBuilder info = new StringBuilder();
        info.append("=== CONFIGURACIÓN ACTUAL DE LA IA ===\n\n");
        info.append(String.format("Peso Estado: %.3f\n", mejorIndividuo.pesoEstado));
        info.append(String.format("Peso Días sin Actividad: %.3f\n", mejorIndividuo.pesoDiasSinActividad));
        info.append(String.format("Peso Préstamos: %.3f\n", mejorIndividuo.pesoPrestamos));
        info.append(String.format("Peso Historial Mantenimiento: %.3f\n", mejorIndividuo.pesoHistorialMant));
        info.append(String.format("Peso Consumo: %.3f\n", mejorIndividuo.pesoConsumo));
        info.append(String.format("Peso Cantidad: %.3f\n\n", mejorIndividuo.pesoCantidad));
        
        info.append("=== UMBRALES OPTIMIZADOS ===\n");
        info.append(String.format("Días para Reparación: %d\n", mejorIndividuo.umbralDiasReparacion));
        info.append(String.format("Préstamos Alto: %d\n", mejorIndividuo.umbralPrestamosAlto));
        info.append(String.format("Cantidad Crítica: %d\n", mejorIndividuo.umbralCantidadCritica));
        info.append(String.format("Consumo Diario Crítico: %.2f\n\n", mejorIndividuo.umbralConsumoDiario));
        
        info.append(String.format("Fitness Score: %.2f\n", mejorIndividuo.fitness));
        info.append(String.format("Datos de Entrenamiento: %d registros\n\n", historicoResultados.size()));
        
        // Agregar estadísticas de rendimiento
        info.append("=== ESTADÍSTICAS DE RENDIMIENTO ===\n");
        double tasaAcierto = calcularTasaAcierto();
        info.append(String.format("Tasa de Acierto: %.1f%%\n", tasaAcierto * 100));
        info.append(String.format("Generaciones Evolutivas: %d\n", AlgoritmoGenetico.GENERACIONES));
        info.append(String.format("Tamaño de Población: %d\n", AlgoritmoGenetico.TAMAÑO_POBLACION));
        
        infoIA.setText(info.toString());
    } else {
        infoIA.setText("La IA no ha sido entrenada aún.\n\nHaga clic en 'Entrenar IA' para comenzar el proceso de optimización.");
    }
    
    infoIA.setCaretPosition(0);
}

private double calcularTasaAcierto() {
    if (historicoResultados.isEmpty()) return 0.0;
    
    int aciertos = 0;
    for (ResultadoHistorico hist : historicoResultados) {
        if (compararPredicciones(hist.prediccionOriginal, hist.resultadoReal)) {
            aciertos++;
        }
    }
    
    return (double) aciertos / historicoResultados.size();
}

private static boolean compararPredicciones(String prediccion, String resultado) {
    if (prediccion == null || resultado == null) return false;
    
    // Mapear predicciones a categorías generales para comparación
    String catPrediccion = mapearCategoria(prediccion);
    String catResultado = mapearCategoria(resultado);
    
    return catPrediccion.equals(catResultado);
}

private static String mapearCategoria(String recomendacion) {
    if (recomendacion == null) return "desconocido";
    
    String rec = recomendacion.toLowerCase().replace(" (ia)", "");
    
    if (rec.contains("urgente") || rec.contains("baja")) return "critico";
    if (rec.contains("reparacion") || rec.contains("necesaria")) return "atencion";
    if (rec.contains("monitorear")) return "precaucion";
    return "normal";
}

private static String buscarResultadoReal(int idEquipamiento, List<ResultadoHistorico> historial) {
    return historial.stream()
        .filter(h -> h.idItem == idEquipamiento && h.tipo.equals("equipamiento"))
        .map(h -> h.resultadoReal)
        .findFirst()
        .orElse(null);
}

private static String buscarResultadoRealInsumo(int idInsumo, List<ResultadoHistorico> historial) {
    return historial.stream()
        .filter(h -> h.idItem == idInsumo && h.tipo.equals("insumo"))
        .map(h -> h.resultadoReal)
        .findFirst()
        .orElse(null);
}

private static int calcularDiasDesdeUltimaActividad(String ultimaActividad) {
    if (ultimaActividad == null || ultimaActividad.equals("Sin actividad") || ultimaActividad.equals("Sin fecha")) {
        return 365; // Asumir un año si no hay datos
    }
    
    try {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date fechaActividad = sdf.parse(ultimaActividad);
        long diffMs = System.currentTimeMillis() - fechaActividad.getTime();
        return (int) TimeUnit.MILLISECONDS.toDays(diffMs);
    } catch (Exception e) {
        return 365;
    }
}


    
    private JPanel crearPanelEquipamientos() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Panel superior con controles
        JPanel panelControles = new JPanel(new FlowLayout());
        JButton btnActualizar = new JButton("Actualizar Predicciones");
        
        btnActualizar.addActionListener(e -> actualizarPrediccionesEquipamientos());
        panelControles.add(btnActualizar);
        
        // Tabla de predicciones simplificada
        String[] columnas = {"ID", "Equipamiento", "Estado Actual", "Última Actividad", 
                           "Préstamos Realizados", "Recomendación"};
        modeloEquipamientos = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaPrediccionEquipamientos = new JTable(modeloEquipamientos);
        tablaPrediccionEquipamientos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaPrediccionEquipamientos.getTableHeader().setReorderingAllowed(false);
        
        // Configurar colores según recomendación
        tablaPrediccionEquipamientos.setDefaultRenderer(Object.class, new RecommendationTableCellRenderer());
        
        JScrollPane scrollTabla = new JScrollPane(tablaPrediccionEquipamientos);
        scrollTabla.setPreferredSize(new Dimension(800, 300));
        
        // Área de recomendaciones
        areaRecomendacionesEquipamientos = new JTextArea(8, 50);
        areaRecomendacionesEquipamientos.setEditable(false);
        areaRecomendacionesEquipamientos.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        areaRecomendacionesEquipamientos.setBorder(new TitledBorder("Recomendaciones de Mantenimiento"));
        
        JScrollPane scrollRecomendaciones = new JScrollPane(areaRecomendacionesEquipamientos);
        
        // Layout del panel
        panel.add(panelControles, BorderLayout.NORTH);
        panel.add(scrollTabla, BorderLayout.CENTER);
        panel.add(scrollRecomendaciones, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel crearPanelInsumos() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Panel superior con controles
        JPanel panelControles = new JPanel(new FlowLayout());
        JButton btnActualizar = new JButton("Actualizar Predicciones");
        
        btnActualizar.addActionListener(e -> actualizarPrediccionesInsumos());
        panelControles.add(btnActualizar);
        
        // Tabla de predicciones simplificada
        String[] columnas = {"ID", "Insumo", "Cantidad Actual", "Consumo Diario", 
                           "Estado", "Recomendación"};
        modeloInsumos = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaPrediccionInsumos = new JTable(modeloInsumos);
        tablaPrediccionInsumos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaPrediccionInsumos.getTableHeader().setReorderingAllowed(false);
        
        // Configurar colores según estado
        tablaPrediccionInsumos.setDefaultRenderer(Object.class, new RecommendationTableCellRenderer());
        
        JScrollPane scrollTabla = new JScrollPane(tablaPrediccionInsumos);
        scrollTabla.setPreferredSize(new Dimension(800, 300));
        
        // Área de recomendaciones
        areaRecomendacionesInsumos = new JTextArea(8, 50);
        areaRecomendacionesInsumos.setEditable(false);
        areaRecomendacionesInsumos.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        areaRecomendacionesInsumos.setBorder(new TitledBorder("Recomendaciones de Reposición"));
        
        JScrollPane scrollRecomendaciones = new JScrollPane(areaRecomendacionesInsumos);
        
        // Layout del panel
        panel.add(panelControles, BorderLayout.NORTH);
        panel.add(scrollTabla, BorderLayout.CENTER);
        panel.add(scrollRecomendaciones, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void configurarLayout() {
        setBorder(BorderFactory.createTitledBorder("Sistema de Predicción - Equipamientos e Insumos"));
    }
    
    private void cargarDatosIniciales() {
    SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
        @Override
        protected Void doInBackground() throws Exception {
            actualizarPrediccionesEquipamientos();
            actualizarPrediccionesInsumos();
            inicializar(); // AGREGAR ESTA LÍNEA
            return null;
        }
        
        @Override
        protected void done() {
            progressBar.setString("Datos cargados correctamente");
        }
    };
    worker.execute();
}
    
    private void actualizarPrediccionesEquipamientos() {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                progressBar.setString("Analizando equipamientos...");
                progressBar.setIndeterminate(true);
                
                modeloEquipamientos.setRowCount(0);
                StringBuilder recomendaciones = new StringBuilder();
                
                try {
                    List<Equipamiento> equipamientos = controladorEquipamiento.listar();
                    List<AnalisisEquipamiento> analisis = new ArrayList<>();
                    
                    for (Equipamiento equipo : equipamientos) {
                        AnalisisEquipamiento analisisEquipo = analizarEquipamiento(equipo);
                        analisis.add(analisisEquipo);
                    }
                    
                    // Ordenar por prioridad de recomendación
                    analisis.sort((a1, a2) -> obtenerPrioridadNumerica(a2.recomendacion) - obtenerPrioridadNumerica(a1.recomendacion));
                    
                    // Agregar a la tabla
                    for (AnalisisEquipamiento a : analisis) {
                        modeloEquipamientos.addRow(new Object[]{
                            a.equipamiento.getIdEquipamiento(),
                            a.equipamiento.getNombreEquipamiento() + " (" + a.equipamiento.getMarca() + ")",
                            a.equipamiento.getEstado(),
                            a.ultimaActividad,
                            a.totalPrestamos,
                            a.recomendacion
                        });
                    }
                    
                    // Generar recomendaciones detalladas
                    generarRecomendacionesEquipamientos(analisis, recomendaciones);
                    
                    SwingUtilities.invokeLater(() -> {
                        areaRecomendacionesEquipamientos.setText(recomendaciones.toString());
                        areaRecomendacionesEquipamientos.setCaretPosition(0);
                    });
                    
                } catch (SQLException e) {
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(PanelPrediccionEquipamientoInsumos.this,
                            "Error al cargar predicciones de equipamientos: " + e.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                    });
                }
                
                return null;
            }
            
            @Override
            protected void done() {
                progressBar.setIndeterminate(false);
                progressBar.setString("Predicciones de equipamientos actualizadas");
            }
        };
        worker.execute();
    }
    
    private void actualizarPrediccionesInsumos() {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                progressBar.setString("Analizando insumos...");
                progressBar.setIndeterminate(true);
                
                modeloInsumos.setRowCount(0);
                StringBuilder recomendaciones = new StringBuilder();
                
                try {
                    List<Insumo> insumos = controladorInsumo.listar();
                    List<AnalisisInsumo> analisis = new ArrayList<>();
                    
                    for (Insumo insumo : insumos) {
                        AnalisisInsumo analisisInsumo = analizarInsumo(insumo);
                        analisis.add(analisisInsumo);
                    }
                    
                    // Ordenar por prioridad de reposición
                    analisis.sort((a1, a2) -> obtenerPrioridadNumerica(a2.recomendacion) - obtenerPrioridadNumerica(a1.recomendacion));
                    
                    // Agregar a la tabla
                    for (AnalisisInsumo a : analisis) {
                        modeloInsumos.addRow(new Object[]{
                            a.insumo.getIdInsumo(),
                            a.insumo.getNombreInsumo(),
                            a.insumo.getCantidad(),
                            String.format("%.2f", a.consumoDiario),
                            a.estado,
                            a.recomendacion
                        });
                    }
                    
                    // Generar recomendaciones detalladas
                    generarRecomendacionesInsumos(analisis, recomendaciones);
                    
                    SwingUtilities.invokeLater(() -> {
                        areaRecomendacionesInsumos.setText(recomendaciones.toString());
                        areaRecomendacionesInsumos.setCaretPosition(0);
                    });
                    
                } catch (SQLException e) {
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(PanelPrediccionEquipamientoInsumos.this,
                            "Error al cargar predicciones de insumos: " + e.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                    });
                }
                
                return null;
            }
            
            @Override
            protected void done() {
                progressBar.setIndeterminate(false);
                progressBar.setString("Predicciones de insumos actualizadas");
            }
        };
        worker.execute();
    }
    
    private AnalisisEquipamiento analizarEquipamiento(Equipamiento equipo) {
        AnalisisEquipamiento analisis = new AnalisisEquipamiento();
        analisis.equipamiento = equipo;
        
        try {
            // Obtener historial del equipamiento
            List<Object[]> historial = controladorHistorialEquipamiento
                .buscarHistorialCompletoEquipamiento(equipo.getIdEquipamiento());
            
            // Contar préstamos realizados
            analisis.totalPrestamos = contarPrestamosEquipamiento(equipo.getIdEquipamiento());
            
            // Determinar última actividad
            analisis.ultimaActividad = obtenerUltimaActividad(historial);
            
            // Analizar estado del equipamiento y historial de mantenimiento
            String estado = equipo.getEstado();
            String ultimaCategoria = obtenerUltimaCategoriaMantenimiento(historial);
            int diasSinActividad = calcularDiasSinActividad(historial);
            
            // Determinar recomendación basada en estado, historial y préstamos
            if (estado.equals("De Baja")) {
                analisis.recomendacion = "Dar Baja";
            } else if (estado.equals("No disponible")) {
                analisis.recomendacion = "Reparación";
            } else if (estado.equals("Uso Medio")) {
                if (ultimaCategoria != null && ultimaCategoria.equals("Reparación") && diasSinActividad > 60) {
                    analisis.recomendacion = "Reparación";
                } else if (diasSinActividad > 180 || analisis.totalPrestamos > 50) {
                    analisis.recomendacion = "Reparación";
                } else {
                    analisis.recomendacion = "Normal";
                }
            } else if (estado.equals("Nuevo")) {
                if (analisis.totalPrestamos > 20) {
                    analisis.recomendacion = "Normal";
                } else {
                    analisis.recomendacion = "Normal";
                }
            } else {
                analisis.recomendacion = "Normal";
            }
            
            // Ajustar recomendación basada en historial de mantenimiento
            if (ultimaCategoria != null) {
                switch (ultimaCategoria) {
                    case "Restauración":
                        if (diasSinActividad > 45) {
                            analisis.recomendacion = "Reparación";
                        }
                        break;
                    case "Actualización":
                        if (diasSinActividad > 90 && !analisis.recomendacion.equals("Reparación")) {
                            analisis.recomendacion = "Normal";
                        }
                        break;
                }
            }
            
        } catch (SQLException e) {
            analisis.ultimaActividad = "Sin datos";
            analisis.totalPrestamos = 0;
            analisis.recomendacion = "Normal";
        }
        
        return analisis;
    }
    
    private AnalisisInsumo analizarInsumo(Insumo insumo) {
        AnalisisInsumo analisis = new AnalisisInsumo();
        analisis.insumo = insumo;
        
        try {
            // Calcular consumo diario promedio
            analisis.consumoDiario = calcularConsumoPromedio(insumo.getIdInsumo());
            
            int cantidadActual = insumo.getCantidad();
            
            // Determinar estado y recomendación
            if (cantidadActual == 0) {
                analisis.estado = "Agotado";
                analisis.recomendacion = "Reposición Urgente";
            } else if (cantidadActual < 10) {
                analisis.estado = "Crítico";
                analisis.recomendacion = "Reposición Urgente";
            } else if (cantidadActual < 20) {
                analisis.estado = "Bajo";
                analisis.recomendacion = "Reposición Necesaria";
            } else if (cantidadActual < 50) {
                analisis.estado = "Aceptable";
                analisis.recomendacion = "Monitorear";
            } else {
                analisis.estado = "Suficiente";
                analisis.recomendacion = "Normal";
            }
            
        } catch (Exception e) {
            analisis.consumoDiario = 0;
            analisis.estado = "Sin datos";
            analisis.recomendacion = "Normal";
        }
        
        return analisis;
    }
    
   
    
    // Clases auxiliares para análisis
    private static class AnalisisEquipamiento {
        Equipamiento equipamiento;
        String ultimaActividad;
        int totalPrestamos;
        String recomendacion;
    }
    
    private static class AnalisisInsumo {
        Insumo insumo;
        double consumoDiario;
        String estado;
        String recomendacion;
    }
    
    // Renderer personalizado para colorear filas según recomendación
    private static class RecommendationTableCellRenderer extends javax.swing.table.DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, 
                boolean hasFocus, int row, int column) {
            
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            if (!isSelected) {
                String recomendacion = "";
                
                // Obtener la recomendación de la última columna
                int ultimaColumna = table.getColumnCount() - 1;
                recomendacion = (String) table.getValueAt(row, ultimaColumna);
                
                switch (recomendacion) {
                    case "Reposición Urgente":
                    case "Dar Baja":
                        c.setBackground(new Color(255, 200, 200)); // Rojo claro
                        break;
                    case "Reparación":
                    case "Reposición Necesaria":
                        c.setBackground(new Color(255, 230, 200)); // Naranja claro
                        break;
                    case "Monitorear":
                        c.setBackground(new Color(255, 255, 200)); // Amarillo claro
                        break;
                    case "Normal":
                        c.setBackground(new Color(200, 255, 200)); // Verde claro
                        break;
                    default:
                        c.setBackground(Color.WHITE);
                        break;
                }
            }
            
            return c;
        }
    }
    
    /**
 * Algoritmo Genético para optimización de predicciones
 */
private static class AlgoritmoGenetico {
    private static final int TAMAÑO_POBLACION = 50;
    private static final int GENERACIONES = 100;
    private static final double TASA_MUTACION = 0.1;
    private static final double TASA_CRUCE = 0.8;
    
    public static class Individuo {
        // Pesos para diferentes factores de decisión
        double pesoEstado;           // 0-1
        double pesoDiasSinActividad; // 0-1
        double pesoPrestamos;        // 0-1
        double pesoHistorialMant;    // 0-1
        double pesoConsumo;          // 0-1 (para insumos)
        double pesoCantidad;         // 0-1 (para insumos)
        
        // Umbrales dinámicos
        int umbralDiasReparacion;    // días
        int umbralPrestamosAlto;     // número de préstamos
        int umbralCantidadCritica;   // cantidad mínima
        double umbralConsumoDiario;  // consumo diario crítico
        
        double fitness = 0.0;
        
        public Individuo() {
            Random rand = new Random();
            pesoEstado = rand.nextDouble();
            pesoDiasSinActividad = rand.nextDouble();
            pesoPrestamos = rand.nextDouble();
            pesoHistorialMant = rand.nextDouble();
            pesoConsumo = rand.nextDouble();
            pesoCantidad = rand.nextDouble();
            
            umbralDiasReparacion = 60 + rand.nextInt(120); // 60-180 días
            umbralPrestamosAlto = 20 + rand.nextInt(30);   // 20-50 préstamos
            umbralCantidadCritica = 5 + rand.nextInt(15);  // 5-20 unidades
            umbralConsumoDiario = 0.5 + rand.nextDouble() * 2; // 0.5-2.5
        }
        
        public Individuo cruzar(Individuo otro) {
            Individuo hijo = new Individuo();
            Random rand = new Random();
            
            // Cruce uniforme
            hijo.pesoEstado = rand.nextBoolean() ? this.pesoEstado : otro.pesoEstado;
            hijo.pesoDiasSinActividad = rand.nextBoolean() ? this.pesoDiasSinActividad : otro.pesoDiasSinActividad;
            hijo.pesoPrestamos = rand.nextBoolean() ? this.pesoPrestamos : otro.pesoPrestamos;
            hijo.pesoHistorialMant = rand.nextBoolean() ? this.pesoHistorialMant : otro.pesoHistorialMant;
            hijo.pesoConsumo = rand.nextBoolean() ? this.pesoConsumo : otro.pesoConsumo;
            hijo.pesoCantidad = rand.nextBoolean() ? this.pesoCantidad : otro.pesoCantidad;
            
            hijo.umbralDiasReparacion = rand.nextBoolean() ? this.umbralDiasReparacion : otro.umbralDiasReparacion;
            hijo.umbralPrestamosAlto = rand.nextBoolean() ? this.umbralPrestamosAlto : otro.umbralPrestamosAlto;
            hijo.umbralCantidadCritica = rand.nextBoolean() ? this.umbralCantidadCritica : otro.umbralCantidadCritica;
            hijo.umbralConsumoDiario = rand.nextBoolean() ? this.umbralConsumoDiario : otro.umbralConsumoDiario;
            
            return hijo;
        }
        
        public void mutar() {
            Random rand = new Random();
            
            if (rand.nextDouble() < TASA_MUTACION) {
                switch (rand.nextInt(10)) {
                    case 0: pesoEstado += (rand.nextDouble() - 0.5) * 0.2; break;
                    case 1: pesoDiasSinActividad += (rand.nextDouble() - 0.5) * 0.2; break;
                    case 2: pesoPrestamos += (rand.nextDouble() - 0.5) * 0.2; break;
                    case 3: pesoHistorialMant += (rand.nextDouble() - 0.5) * 0.2; break;
                    case 4: pesoConsumo += (rand.nextDouble() - 0.5) * 0.2; break;
                    case 5: pesoCantidad += (rand.nextDouble() - 0.5) * 0.2; break;
                    case 6: umbralDiasReparacion += rand.nextInt(21) - 10; break;
                    case 7: umbralPrestamosAlto += rand.nextInt(11) - 5; break;
                    case 8: umbralCantidadCritica += rand.nextInt(11) - 5; break;
                    case 9: umbralConsumoDiario += (rand.nextDouble() - 0.5) * 0.5; break;
                }
            }
            
            // Mantener valores en rangos válidos
            pesoEstado = Math.max(0, Math.min(1, pesoEstado));
            pesoDiasSinActividad = Math.max(0, Math.min(1, pesoDiasSinActividad));
            pesoPrestamos = Math.max(0, Math.min(1, pesoPrestamos));
            pesoHistorialMant = Math.max(0, Math.min(1, pesoHistorialMant));
            pesoConsumo = Math.max(0, Math.min(1, pesoConsumo));
            pesoCantidad = Math.max(0, Math.min(1, pesoCantidad));
            
            umbralDiasReparacion = Math.max(30, Math.min(365, umbralDiasReparacion));
            umbralPrestamosAlto = Math.max(10, Math.min(100, umbralPrestamosAlto));
            umbralCantidadCritica = Math.max(1, Math.min(50, umbralCantidadCritica));
            umbralConsumoDiario = Math.max(0.1, Math.min(5.0, umbralConsumoDiario));
        }
    }
    
    public static Individuo evolucionar(List<AnalisisEquipamiento> equipamientos, 
                                      List<AnalisisInsumo> insumos,
                                      List<ResultadoHistorico> historial) {
        // Crear población inicial
        List<Individuo> poblacion = IntStream.range(0, TAMAÑO_POBLACION)
            .mapToObj(i -> new Individuo())
            .collect(Collectors.toList());
        
        // Evolucionar durante las generaciones especificadas
        for (int generacion = 0; generacion < GENERACIONES; generacion++) {
            // Evaluar fitness de cada individuo
            for (Individuo individuo : poblacion) {
                individuo.fitness = evaluarFitness(individuo, equipamientos, insumos, historial);
            }
            
            // Ordenar por fitness (descendente)
            poblacion.sort((a, b) -> Double.compare(b.fitness, a.fitness));
            
            // Crear nueva generación
            List<Individuo> nuevaPoblacion = new ArrayList<>();
            
            // Elitismo: mantener los mejores 10%
            int elites = TAMAÑO_POBLACION / 10;
            for (int i = 0; i < elites; i++) {
                nuevaPoblacion.add(poblacion.get(i));
            }
            
            // Generar el resto por cruce y mutación
            Random rand = new Random();
            while (nuevaPoblacion.size() < TAMAÑO_POBLACION) {
                if (rand.nextDouble() < TASA_CRUCE) {
                    // Selección por torneo
                    Individuo padre1 = seleccionTorneo(poblacion);
                    Individuo padre2 = seleccionTorneo(poblacion);
                    Individuo hijo = padre1.cruzar(padre2);
                    hijo.mutar();
                    nuevaPoblacion.add(hijo);
                } else {
                    // Mutación de un individuo existente
                    Individuo mutante = new Individuo();
                    mutante = poblacion.get(rand.nextInt(poblacion.size() / 2)); // De la mitad superior
                    mutante.mutar();
                    nuevaPoblacion.add(mutante);
                }
            }
            
            poblacion = nuevaPoblacion;
        }
        
        // Evaluar fitness final y retornar el mejor
        for (Individuo individuo : poblacion) {
            individuo.fitness = evaluarFitness(individuo, equipamientos, insumos, historial);
        }
        
        return poblacion.stream().max((a, b) -> Double.compare(a.fitness, b.fitness)).orElse(null);
    }
    
    private static Individuo seleccionTorneo(List<Individuo> poblacion) {
        Random rand = new Random();
        int tamañoTorneo = 5;
        Individuo mejor = poblacion.get(rand.nextInt(poblacion.size()));
        
        for (int i = 1; i < tamañoTorneo; i++) {
            Individuo competidor = poblacion.get(rand.nextInt(poblacion.size()));
            if (competidor.fitness > mejor.fitness) {
                mejor = competidor;
            }
        }
        return mejor;
    }
    
    private static double evaluarFitness(Individuo individuo, 
                                       List<AnalisisEquipamiento> equipamientos,
                                       List<AnalisisInsumo> insumos,
                                       List<ResultadoHistorico> historial) {
        double fitness = 0.0;
        int prediccionesCorrectas = 0;
        int totalPredicciones = 0;
        
        // Evaluar predicciones de equipamientos
        for (AnalisisEquipamiento analisis : equipamientos) {
            String prediccionOriginal = analisis.recomendacion;
            String prediccionGenetica = predecirEquipamiento(individuo, analisis);
            
            // Buscar resultado histórico real
            String resultadoReal = buscarResultadoReal(analisis.equipamiento.getIdEquipamiento(), historial);
            
            if (resultadoReal != null) {
                // Comparar predicción genética vs resultado real
                if (compararPredicciones(prediccionGenetica, resultadoReal)) {
                    prediccionesCorrectas++;
                }
                totalPredicciones++;
                
                // Bonificar si la predicción genética es mejor que la original
                if (compararPredicciones(prediccionGenetica, resultadoReal) && 
                    !compararPredicciones(prediccionOriginal, resultadoReal)) {
                    fitness += 2.0; // Bonificación por mejora
                }
            }
        }
        
        // Evaluar predicciones de insumos
        for (AnalisisInsumo analisis : insumos) {
            String prediccionOriginal = analisis.recomendacion;
            String prediccionGenetica = predecirInsumo(individuo, analisis);
            
            String resultadoReal = buscarResultadoRealInsumo(analisis.insumo.getIdInsumo(), historial);
            
            if (resultadoReal != null) {
                if (compararPredicciones(prediccionGenetica, resultadoReal)) {
                    prediccionesCorrectas++;
                }
                totalPredicciones++;
                
                if (compararPredicciones(prediccionGenetica, resultadoReal) && 
                    !compararPredicciones(prediccionOriginal, resultadoReal)) {
                    fitness += 2.0;
                }
            }
        }
        
        // Calcular fitness final
        if (totalPredicciones > 0) {
            double tasaAcierto = (double) prediccionesCorrectas / totalPredicciones;
            fitness += tasaAcierto * 100; // Base fitness por tasa de acierto
        }
        
        // Penalizar configuraciones extremas
        double penalizacion = 0.0;
        if (individuo.umbralDiasReparacion < 30 || individuo.umbralDiasReparacion > 300) penalizacion += 5;
        if (individuo.umbralPrestamosAlto < 5 || individuo.umbralPrestamosAlto > 80) penalizacion += 5;
        
        return Math.max(0, fitness - penalizacion);
    }
    
    private static String predecirEquipamiento(Individuo individuo, AnalisisEquipamiento analisis) {
        double score = 0.0;
        
        // Evaluar estado
        switch (analisis.equipamiento.getEstado()) {
            case "De Baja": score += individuo.pesoEstado * 4.0; break;
            case "No Disponible": score += individuo.pesoEstado * 3.0; break;
            case "Uso Medio": score += individuo.pesoEstado * 2.0; break;
            case "Nuevo": score += individuo.pesoEstado * 1.0; break;
        }
        
        // Evaluar días sin actividad
        int diasSinActividad = calcularDiasDesdeUltimaActividad(analisis.ultimaActividad);
        if (diasSinActividad > individuo.umbralDiasReparacion) {
            score += individuo.pesoDiasSinActividad * 2.0;
        }
        
        // Evaluar préstamos
        if (analisis.totalPrestamos > individuo.umbralPrestamosAlto) {
            score += individuo.pesoPrestamos * 1.5;
        }
        
        // Determinar recomendación basada en score
        if (score > 3.5) return "Dar Baja";
        else if (score > 2.5) return "Reparación";
        else if (score > 1.5) return "Monitorear";
        else return "Normal";
    }
    
    private static String predecirInsumo(Individuo individuo, AnalisisInsumo analisis) {
        double score = 0.0;
        
        // Evaluar cantidad actual
        int cantidad = analisis.insumo.getCantidad();
        if (cantidad <= individuo.umbralCantidadCritica) {
            score += individuo.pesoCantidad * 3.0;
        } else if (cantidad <= individuo.umbralCantidadCritica * 2) {
            score += individuo.pesoCantidad * 2.0;
        }
        
        // Evaluar consumo diario
        if (analisis.consumoDiario > individuo.umbralConsumoDiario) {
            score += individuo.pesoConsumo * 2.0;
        }
        
        // Evaluar tendencia de agotamiento
        if (cantidad > 0 && analisis.consumoDiario > 0) {
            double diasRestantes = cantidad / analisis.consumoDiario;
            if (diasRestantes < 7) score += 3.0;
            else if (diasRestantes < 14) score += 2.0;
            else if (diasRestantes < 30) score += 1.0;
        }
        
        // Determinar recomendación
        if (score > 3.0) return "Reposición Urgente";
            else if (score > 2.0) return "Reposición Necesaria";
            else if (score > 1.0) return "Monitorear";
            else return "Normal";
        }
    }

    /**
     * Clase para almacenar resultados históricos para entrenamiento
     */
    private static class ResultadoHistorico {
        int idItem;
        String tipo; // "equipamiento" o "insumo"
        String prediccionOriginal;
        String resultadoReal;
        Date fecha;

        public ResultadoHistorico(int idItem, String tipo, String prediccionOriginal, 
                                String resultadoReal, Date fecha) {
            this.idItem = idItem;
            this.tipo = tipo;
            this.prediccionOriginal = prediccionOriginal;
            this.resultadoReal = resultadoReal;
            this.fecha = fecha;
        }
    }

}