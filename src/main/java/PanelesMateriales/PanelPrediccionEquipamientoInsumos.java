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

/**
 * Panel de Predicción para Equipamientos e Insumos
 * Proporciona estimaciones y recomendaciones basadas en análisis de datos históricos
 */
public class PanelPrediccionEquipamientoInsumos extends JPanel {
    
    // Controladores
    private ControladorEquipamiento controladorEquipamiento;
    private ControladorInsumo controladorInsumo;
    private ControladorPrestamo controladorPrestamo;
    private ControladorHistorialGeneral controladorHistorial;
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
    
    // Panel de Análisis
    private JTextArea areaAnalisisGeneral;
    private JProgressBar progressBar;
    
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
        controladorHistorial = new ControladorHistorialGeneral();
        controladorHistorialEquipamiento = new ControladorHistorialEquipamiento();
    }
    
    private void inicializarComponentes() {
        setLayout(new BorderLayout());
        
        // Panel principal con pestañas
        tabbedPane = new JTabbedPane();
        
        // Pestaña de Equipamientos
        JPanel panelEquipamientos = crearPanelEquipamientos();
        tabbedPane.addTab("Predicción Equipamientos", panelEquipamientos);
        
        // Pestaña de Insumos
        JPanel panelInsumos = crearPanelInsumos();
        tabbedPane.addTab("Predicción Insumos", panelInsumos);
        
        // Pestaña de Análisis General
        JPanel panelAnalisis = crearPanelAnalisis();
        tabbedPane.addTab("Análisis General", panelAnalisis);
        
        add(tabbedPane, BorderLayout.CENTER);
        
        // Barra de progreso
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setString("Listo");
        add(progressBar, BorderLayout.SOUTH);
    }
    
    private JPanel crearPanelEquipamientos() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Panel superior con controles
        JPanel panelControles = new JPanel(new FlowLayout());
        JButton btnActualizar = new JButton("Actualizar Predicciones");
        JButton btnExportar = new JButton("Exportar Reporte");
        
        btnActualizar.addActionListener(e -> actualizarPrediccionesEquipamientos());
        btnExportar.addActionListener(e -> exportarReporteEquipamientos());
        
        panelControles.add(btnActualizar);
        panelControles.add(btnExportar);
        
        // Tabla de predicciones
        String[] columnas = {"ID", "Equipamiento", "Estado Actual", "Días Desde Último Mantenimiento", 
                           "Predicción Mantenimiento", "Prioridad", "Recomendación"};
        modeloEquipamientos = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaPrediccionEquipamientos = new JTable(modeloEquipamientos);
        tablaPrediccionEquipamientos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaPrediccionEquipamientos.getTableHeader().setReorderingAllowed(false);
        
        // Configurar colores según prioridad
        tablaPrediccionEquipamientos.setDefaultRenderer(Object.class, new PriorityTableCellRenderer());
        
        JScrollPane scrollTabla = new JScrollPane(tablaPrediccionEquipamientos);
        scrollTabla.setPreferredSize(new Dimension(800, 300));
        
        // Área de recomendaciones
        areaRecomendacionesEquipamientos = new JTextArea(8, 50);
        areaRecomendacionesEquipamientos.setEditable(false);
        areaRecomendacionesEquipamientos.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        areaRecomendacionesEquipamientos.setBorder(new TitledBorder("Recomendaciones Detalladas"));
        
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
        JButton btnExportar = new JButton("Exportar Reporte");
        
        btnActualizar.addActionListener(e -> actualizarPrediccionesInsumos());
        btnExportar.addActionListener(e -> exportarReporteInsumos());
        
        panelControles.add(btnActualizar);
        panelControles.add(btnExportar);
        
        // Tabla de predicciones
        String[] columnas = {"ID", "Insumo", "Cantidad Actual", "Consumo Promedio/Día", 
                           "Días Restantes", "Fecha Agotamiento", "Prioridad", "Recomendación"};
        modeloInsumos = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaPrediccionInsumos = new JTable(modeloInsumos);
        tablaPrediccionInsumos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaPrediccionInsumos.getTableHeader().setReorderingAllowed(false);
        
        // Configurar colores según prioridad
        tablaPrediccionInsumos.setDefaultRenderer(Object.class, new PriorityTableCellRenderer());
        
        JScrollPane scrollTabla = new JScrollPane(tablaPrediccionInsumos);
        scrollTabla.setPreferredSize(new Dimension(800, 300));
        
        // Área de recomendaciones
        areaRecomendacionesInsumos = new JTextArea(8, 50);
        areaRecomendacionesInsumos.setEditable(false);
        areaRecomendacionesInsumos.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        areaRecomendacionesInsumos.setBorder(new TitledBorder("Recomendaciones de Compra"));
        
        JScrollPane scrollRecomendaciones = new JScrollPane(areaRecomendacionesInsumos);
        
        // Layout del panel
        panel.add(panelControles, BorderLayout.NORTH);
        panel.add(scrollTabla, BorderLayout.CENTER);
        panel.add(scrollRecomendaciones, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel crearPanelAnalisis() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Panel superior con controles
        JPanel panelControles = new JPanel(new FlowLayout());
        JButton btnGenerar = new JButton("Generar Análisis Completo");
        JButton btnExportar = new JButton("Exportar Análisis");
        
        btnGenerar.addActionListener(e -> generarAnalisisCompleto());
        btnExportar.addActionListener(e -> exportarAnalisisCompleto());
        
        panelControles.add(btnGenerar);
        panelControles.add(btnExportar);
        
        // Área de análisis
        areaAnalisisGeneral = new JTextArea(25, 80);
        areaAnalisisGeneral.setEditable(false);
        areaAnalisisGeneral.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        
        JScrollPane scrollAnalisis = new JScrollPane(areaAnalisisGeneral);
        
        panel.add(panelControles, BorderLayout.NORTH);
        panel.add(scrollAnalisis, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void configurarLayout() {
        setBorder(BorderFactory.createTitledBorder("Sistema de Predicción y Recomendaciones"));
    }
    
    private void cargarDatosIniciales() {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                actualizarPrediccionesEquipamientos();
                actualizarPrediccionesInsumos();
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
                    List<PrediccionEquipamiento> predicciones = new ArrayList<>();
                    
                    for (Equipamiento equipo : equipamientos) {
                        PrediccionEquipamiento prediccion = analizarEquipamiento(equipo);
                        predicciones.add(prediccion);
                    }
                    
                    // Ordenar por prioridad
                    predicciones.sort((p1, p2) -> p2.prioridad.compareTo(p1.prioridad));
                    
                    // Agregar a la tabla
                    for (PrediccionEquipamiento p : predicciones) {
                        modeloEquipamientos.addRow(new Object[]{
                            p.equipamiento.getIdEquipamiento(),
                            p.equipamiento.getNombreEquipamiento() + " (" + p.equipamiento.getMarca() + ")",
                            p.equipamiento.getEstado(),
                            p.diasSinMantenimiento,
                            p.prediccionMantenimiento,
                            p.prioridad,
                            p.recomendacion
                        });
                    }
                    
                    // Generar recomendaciones detalladas
                    recomendaciones.append("=== RECOMENDACIONES DE MANTENIMIENTO ===\n\n");
                    
                    long equiposUrgentes = predicciones.stream()
                        .filter(p -> p.prioridad.equals("URGENTE"))
                        .count();
                    
                    long equiposAlta = predicciones.stream()
                        .filter(p -> p.prioridad.equals("ALTA"))
                        .count();
                    
                    recomendaciones.append(String.format("• Equipos que requieren atención URGENTE: %d\n", equiposUrgentes));
                    recomendaciones.append(String.format("• Equipos con prioridad ALTA: %d\n\n", equiposAlta));
                    
                    if (equiposUrgentes > 0) {
                        recomendaciones.append("ACCIONES INMEDIATAS REQUERIDAS:\n");
                        predicciones.stream()
                            .filter(p -> p.prioridad.equals("URGENTE"))
                            .forEach(p -> recomendaciones.append(String.format("- %s: %s\n", 
                                p.equipamiento.getNombreEquipamiento(), p.recomendacion)));
                        recomendaciones.append("\n");
                    }
                    
                    // Estadísticas generales
                    Map<String, Long> estadoStats = equipamientos.stream()
                        .collect(java.util.stream.Collectors.groupingBy(
                            Equipamiento::getEstado, 
                            java.util.stream.Collectors.counting()));
                    
                    recomendaciones.append("ESTADÍSTICAS GENERALES:\n");
                    estadoStats.forEach((estado, count) -> 
                        recomendaciones.append(String.format("- %s: %d equipos\n", estado, count)));
                    
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
                    List<PrediccionInsumo> predicciones = new ArrayList<>();
                    
                    for (Insumo insumo : insumos) {
                        PrediccionInsumo prediccion = analizarInsumo(insumo);
                        predicciones.add(prediccion);
                    }
                    
                    // Ordenar por prioridad
                    predicciones.sort((p1, p2) -> p2.prioridad.compareTo(p1.prioridad));
                    
                    // Agregar a la tabla
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    for (PrediccionInsumo p : predicciones) {
                        String fechaAgotamiento = p.fechaAgotamiento != null ? 
                            sdf.format(p.fechaAgotamiento) : "N/A";
                        
                        modeloInsumos.addRow(new Object[]{
                            p.insumo.getIdInsumo(),
                            p.insumo.getNombreInsumo(),
                            p.insumo.getCantidad(),
                            String.format("%.2f", p.consumoPromedioDiario),
                            p.diasRestantes,
                            fechaAgotamiento,
                            p.prioridad,
                            p.recomendacion
                        });
                    }
                    
                    // Generar recomendaciones de compra
                    recomendaciones.append("=== RECOMENDACIONES DE COMPRA ===\n\n");
                    
                    long insumosUrgentes = predicciones.stream()
                        .filter(p -> p.prioridad.equals("URGENTE"))
                        .count();
                    
                    long insumosAlta = predicciones.stream()
                        .filter(p -> p.prioridad.equals("ALTA"))
                        .count();
                    
                    recomendaciones.append(String.format("• Insumos que requieren compra URGENTE: %d\n", insumosUrgentes));
                    recomendaciones.append(String.format("• Insumos con prioridad ALTA: %d\n\n", insumosAlta));
                    
                    if (insumosUrgentes > 0) {
                        recomendaciones.append("COMPRAS INMEDIATAS REQUERIDAS:\n");
                        predicciones.stream()
                            .filter(p -> p.prioridad.equals("URGENTE"))
                            .forEach(p -> recomendaciones.append(String.format("- %s: %s\n", 
                                p.insumo.getNombreInsumo(), p.recomendacion)));
                        recomendaciones.append("\n");
                    }
                    
                    // Presupuesto estimado
                    double presupuestoEstimado = calcularPresupuestoEstimado(predicciones);
                    recomendaciones.append(String.format("PRESUPUESTO ESTIMADO MENSUAL: $%.2f\n\n", presupuestoEstimado));
                    
                    // Análisis por categoría
                    Map<String, List<PrediccionInsumo>> categorias = predicciones.stream()
                        .collect(java.util.stream.Collectors.groupingBy(p -> p.insumo.getCategoria()));
                    
                    recomendaciones.append("ANÁLISIS POR CATEGORÍA:\n");
                    categorias.forEach((categoria, lista) -> {
                        long urgentes = lista.stream().filter(p -> p.prioridad.equals("URGENTE")).count();
                        recomendaciones.append(String.format("- %s: %d insumos (%d urgentes)\n", 
                            categoria, lista.size(), urgentes));
                    });
                    
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
    
    private PrediccionEquipamiento analizarEquipamiento(Equipamiento equipo) {
        PrediccionEquipamiento prediccion = new PrediccionEquipamiento();
        prediccion.equipamiento = equipo;
        
        try {
            // Analizar historial del equipamiento
            List<Object[]> historial = controladorHistorialEquipamiento
                .buscarHistorialCompletoEquipamiento(equipo.getIdEquipamiento());
            
            // Calcular días desde último mantenimiento
            prediccion.diasSinMantenimiento = calcularDiasSinMantenimiento(historial);
            
            // Determinar prioridad y recomendación basada en estado y historial
            if (equipo.getEstado().equals("Dañado") || equipo.getEstado().equals("En reparación")) {
                prediccion.prioridad = "URGENTE";
                prediccion.prediccionMantenimiento = "Inmediato";
                prediccion.recomendacion = "Reparación urgente requerida";
            } else if (prediccion.diasSinMantenimiento > 180) { // 6 meses
                prediccion.prioridad = "URGENTE";
                prediccion.prediccionMantenimiento = "Inmediato";
                prediccion.recomendacion = "Mantenimiento preventivo urgente";
            } else if (prediccion.diasSinMantenimiento > 120) { // 4 meses
                prediccion.prioridad = "ALTA";
                prediccion.prediccionMantenimiento = "1-2 semanas";
                prediccion.recomendacion = "Programar mantenimiento preventivo";
            } else if (prediccion.diasSinMantenimiento > 90) { // 3 meses
                prediccion.prioridad = "MEDIA";
                prediccion.prediccionMantenimiento = "1 mes";
                prediccion.recomendacion = "Monitorear estado y programar revisión";
            } else {
                prediccion.prioridad = "BAJA";
                prediccion.prediccionMantenimiento = "2-3 meses";
                prediccion.recomendacion = "Estado óptimo, continuar monitoreo";
            }
            
            // Ajustar según disponibilidad
            if (equipo.getDisponibilidad().equals("No Disponible")) {
                prediccion.prioridad = "URGENTE";
                prediccion.recomendacion += " - Equipo fuera de servicio";
            }
            
        } catch (SQLException e) {
            prediccion.diasSinMantenimiento = 0;
            prediccion.prioridad = "DESCONOCIDO";
            prediccion.prediccionMantenimiento = "Sin datos";
            prediccion.recomendacion = "Error al analizar historial";
        }
        
        return prediccion;
    }
    
    private PrediccionInsumo analizarInsumo(Insumo insumo) {
        PrediccionInsumo prediccion = new PrediccionInsumo();
        prediccion.insumo = insumo;
        
        try {
            // Calcular consumo promedio basado en préstamos
            prediccion.consumoPromedioDiario = calcularConsumoPromedio(insumo.getIdInsumo());
            
            // Calcular días restantes
            if (prediccion.consumoPromedioDiario > 0) {
                prediccion.diasRestantes = (int) (insumo.getCantidad() / prediccion.consumoPromedioDiario);
                
                // Calcular fecha de agotamiento
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DAY_OF_MONTH, prediccion.diasRestantes);
                prediccion.fechaAgotamiento = cal.getTime();
            } else {
                prediccion.diasRestantes = Integer.MAX_VALUE;
                prediccion.fechaAgotamiento = null;
            }
            
            // Determinar prioridad
            if (insumo.getCantidad() == 0) {
                prediccion.prioridad = "URGENTE";
                prediccion.recomendacion = "AGOTADO - Compra inmediata";
            } else if (prediccion.diasRestantes <= 7) {
                prediccion.prioridad = "URGENTE";
                prediccion.recomendacion = "Comprar inmediatamente - Se agota en menos de 1 semana";
            } else if (prediccion.diasRestantes <= 15) {
                prediccion.prioridad = "ALTA";
                prediccion.recomendacion = "Programar compra urgente - Se agota en 2 semanas";
            } else if (prediccion.diasRestantes <= 30) {
                prediccion.prioridad = "MEDIA";
                prediccion.recomendacion = "Planificar compra - Se agota en 1 mes";
            } else if (prediccion.diasRestantes <= 60) {
                prediccion.prioridad = "BAJA";
                prediccion.recomendacion = "Monitorear consumo - Se agota en 2 meses";
            } else {
                prediccion.prioridad = "BAJA";
                prediccion.recomendacion = "Stock suficiente - Monitoreo rutinario";
            }
            
            // Ajustar cantidad recomendada de compra
            if (prediccion.consumoPromedioDiario > 0) {
                int cantidadMensual = (int) (prediccion.consumoPromedioDiario * 30);
                int cantidadRecomendada = Math.max(cantidadMensual * 2, 50); // Mínimo 2 meses de stock
                
                if (!prediccion.prioridad.equals("BAJA")) {
                    prediccion.recomendacion += String.format(" (Cantidad sugerida: %d unidades)", cantidadRecomendada);
                }
            }
            
        } catch (Exception e) {
            prediccion.consumoPromedioDiario = 0;
            prediccion.diasRestantes = 0;
            prediccion.prioridad = "DESCONOCIDO";
            prediccion.recomendacion = "Error al analizar consumo";
        }
        
        return prediccion;
    }
    
    private int calcularDiasSinMantenimiento(List<Object[]> historial) {
        if (historial.isEmpty()) {
            return 365; // Si no hay historial, asumir que necesita mantenimiento
        }
        
        // Buscar el último mantenimiento
        Date ultimoMantenimiento = null;
        for (Object[] registro : historial) {
            String categoria = (String) registro[3];
            Date fecha = (Date) registro[2];
            
            if (categoria != null && categoria.toLowerCase().contains("mantenimiento")) {
                if (ultimoMantenimiento == null || fecha.after(ultimoMantenimiento)) {
                    ultimoMantenimiento = fecha;
                }
            }
        }
        
        if (ultimoMantenimiento == null) {
            return 365; // No hay registro de mantenimiento
        }
        
        long diffMs = System.currentTimeMillis() - ultimoMantenimiento.getTime();
        return (int) TimeUnit.MILLISECONDS.toDays(diffMs);
    }
    
    private double calcularConsumoPromedio(int idInsumo) throws SQLException {
        // Obtener préstamos de los últimos 30 días
        List<Prestamo> prestamos = controladorPrestamo.listar();
        
        // Filtrar préstamos recientes y calcular consumo
        Calendar hace30Dias = Calendar.getInstance();
        hace30Dias.add(Calendar.DAY_OF_MONTH, -30);
        
        double consumoTotal = 0;
        int diasConConsumo = 0;
        
        Map<java.sql.Date, Integer> consumoPorDia = new HashMap<>();
        
        for (Prestamo prestamo : prestamos) {
            if (prestamo.getFechaPrestamo().after(hace30Dias.getTime())) {
                try {
                    Map<Integer, Integer> insumos = controladorPrestamo
                        .obtenerInsumosPrestamoConCantidades(prestamo.getIdPrestamo());
                    
                    if (insumos.containsKey(idInsumo)) {
                        int cantidad = insumos.get(idInsumo);
                        consumoPorDia.merge(prestamo.getFechaPrestamo(), cantidad, Integer::sum);
                    }
                } catch (SQLException e) {
                    // Continuar con el siguiente préstamo
                }
            }
        }
        
        if (consumoPorDia.isEmpty()) {
            return 0; // No hay consumo registrado
        }
        
        // Calcular promedio
        int totalConsumo = consumoPorDia.values().stream().mapToInt(Integer::intValue).sum();
        return (double) totalConsumo / 30.0; // Promedio en 30 días
    }
    
    private double calcularPresupuestoEstimado(List<PrediccionInsumo> predicciones) {
        // Estimación básica de presupuesto mensual
        double presupuesto = 0;
        
        for (PrediccionInsumo p : predicciones) {
            if (p.prioridad.equals("URGENTE") || p.prioridad.equals("ALTA")) {
                // Precio estimado por categoría (valores de ejemplo)
                double precioEstimado = obtenerPrecioEstimado(p.insumo.getCategoria());
                double cantidadMensual = p.consumoPromedioDiario * 30;
                presupuesto += precioEstimado * cantidadMensual;
            }
        }
        
        return presupuesto;
    }
    
    private double obtenerPrecioEstimado(String categoria) {
        // Precios estimados por categoría (valores de ejemplo)
        switch (categoria.toLowerCase()) {
            case "químicos": return 15.0;
            case "vidriería": return 8.0;
            case "reactivos": return 25.0;
            case "consumibles": return 5.0;
            case "materiales": return 10.0;
            default: return 12.0;
        }
    }
    
    private void generarAnalisisCompleto() {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                progressBar.setString("Generando análisis completo...");
                progressBar.setIndeterminate(true);
                
                StringBuilder analisis = new StringBuilder();
                
                try {
                    // Título del análisis
                    analisis.append("=".repeat(80)).append("\n");
                    analisis.append("           ANÁLISIS COMPLETO DEL SISTEMA DE LABORATORIO\n");
                    analisis.append("=".repeat(80)).append("\n\n");
                    
                    // Fecha del análisis
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    analisis.append("Fecha de análisis: ").append(sdf.format(new Date())).append("\n\n");
                    
                    // 1. RESUMEN EJECUTIVO
                    analisis.append("1. RESUMEN EJECUTIVO\n");
                    analisis.append("-".repeat(50)).append("\n");
                    
                    List<Equipamiento> equipamientos = controladorEquipamiento.listar();
                    List<Insumo> insumos = controladorInsumo.listar();
                    List<Prestamo> prestamos = controladorPrestamo.listar();
                    
                    // Estadísticas generales
                    analisis.append(String.format("• Total de equipamientos: %d\n", equipamientos.size()));
                    analisis.append(String.format("• Total de insumos: %d\n", insumos.size()));
                    analisis.append(String.format("• Total de préstamos registrados: %d\n", prestamos.size()));
                    
                    // Estados de equipamientos
                    Map<String, Long> estadosEquipos = equipamientos.stream()
                        .collect(java.util.stream.Collectors.groupingBy(
                            Equipamiento::getEstado, 
                            java.util.stream.Collectors.counting()));
                    
                    analisis.append("\nEstado de equipamientos:\n");
                    estadosEquipos.forEach((estado, count) -> 
                        analisis.append(String.format("  - %s: %d (%.1f%%)\n", 
                            estado, count, (count * 100.0) / equipamientos.size())));
                    
                    // 2. ANÁLISIS DE EQUIPAMIENTOS
                    analisis.append("\n\n2. ANÁLISIS DETALLADO DE EQUIPAMIENTOS\n");
                    analisis.append("-".repeat(50)).append("\n");
                    
                    // Equipos que requieren atención urgente
                    List<Equipamiento> equiposUrgentes = equipamientos.stream()
                        .filter(e -> e.getEstado().equals("Dañado") || 
                                   e.getEstado().equals("En reparación") ||
                                   e.getDisponibilidad().equals("No Disponible"))
                        .collect(java.util.stream.Collectors.toList());
                    
                    analisis.append(String.format("Equipos que requieren atención urgente: %d\n", equiposUrgentes.size()));
                    if (!equiposUrgentes.isEmpty()) {
                        analisis.append("Lista de equipos urgentes:\n");
                        for (Equipamiento e : equiposUrgentes) {
                            analisis.append(String.format("  • %s (%s) - Estado: %s, Disponibilidad: %s\n",
                                e.getNombreEquipamiento(), e.getMarca(), e.getEstado(), e.getDisponibilidad()));
                        }
                    }
                    
                    // Análisis por ubicación
                    Map<Integer, Long> equiposPorUbicacion = equipamientos.stream()
                        .collect(java.util.stream.Collectors.groupingBy(
                            Equipamiento::getIdLaboratorio, 
                            java.util.stream.Collectors.counting()));

                    
                    analisis.append("\nDistribución por ubicación:\n");
                    equiposPorUbicacion.forEach((ubicacion, count) -> 
                        analisis.append(String.format("  - %s: %d equipos\n", ubicacion, count)));
                    
                    // 3. ANÁLISIS DE INSUMOS
                    analisis.append("\n\n3. ANÁLISIS DETALLADO DE INSUMOS\n");
                    analisis.append("-".repeat(50)).append("\n");
                    
                    // Insumos con stock crítico
                    List<Insumo> insumosAgotados = insumos.stream()
                        .filter(i -> i.getCantidad() == 0)
                        .collect(java.util.stream.Collectors.toList());
                    
                    List<Insumo> insumosBajos = insumos.stream()
                        .filter(i -> i.getCantidad() > 0 && i.getCantidad() <= 10)
                        .collect(java.util.stream.Collectors.toList());
                    
                    analisis.append(String.format("Insumos agotados: %d\n", insumosAgotados.size()));
                    analisis.append(String.format("Insumos con stock bajo (≤10): %d\n", insumosBajos.size()));
                    
                    if (!insumosAgotados.isEmpty()) {
                        analisis.append("\nInsumos AGOTADOS (requieren compra inmediata):\n");
                        for (Insumo i : insumosAgotados) {
                            analisis.append(String.format("  • %s (Categoría: %s)\n",
                                i.getNombreInsumo(), i.getCategoria()));
                        }
                    }
                    
                    if (!insumosBajos.isEmpty()) {
                        analisis.append("\nInsumos con STOCK BAJO:\n");
                        for (Insumo i : insumosBajos) {
                            analisis.append(String.format("  • %s - Cantidad: %d (Categoría: %s)\n",
                                i.getNombreInsumo(), i.getCantidad(), i.getCategoria()));
                        }
                    }
                    
                    // Análisis por categoría
                    Map<String, List<Insumo>> insumosPorCategoria = insumos.stream()
                        .collect(java.util.stream.Collectors.groupingBy(Insumo::getCategoria));
                    
                    analisis.append("\nAnálisis por categoría:\n");
                    insumosPorCategoria.forEach((categoria, lista) -> {
                        int totalCantidad = lista.stream().mapToInt(Insumo::getCantidad).sum();
                        long agotados = lista.stream().filter(i -> i.getCantidad() == 0).count();
                        analisis.append(String.format("  - %s: %d insumos, Stock total: %d, Agotados: %d\n",
                            categoria, lista.size(), totalCantidad, agotados));
                    });
                    
                    // 4. ANÁLISIS DE ACTIVIDAD (PRÉSTAMOS)
                    analisis.append("\n\n4. ANÁLISIS DE ACTIVIDAD DEL LABORATORIO\n");
                    analisis.append("-".repeat(50)).append("\n");
                    
                    // Préstamos por mes (últimos 6 meses)
                    Calendar hace6Meses = Calendar.getInstance();
                    hace6Meses.add(Calendar.MONTH, -6);
                    
                    List<Prestamo> prestamosRecientes = prestamos.stream()
                        .filter(p -> p.getFechaPrestamo().after(hace6Meses.getTime()))
                        .collect(java.util.stream.Collectors.toList());
                    
                    analisis.append(String.format("Préstamos en los últimos 6 meses: %d\n", prestamosRecientes.size()));
                    analisis.append(String.format("Promedio mensual: %.1f préstamos\n", prestamosRecientes.size() / 6.0));
                    
                    // Actividad por estado
                    Map<String, Long> prestamosPorEstado = prestamosRecientes.stream()
                        .collect(java.util.stream.Collectors.groupingBy(
                            Prestamo::getEstadoPrestamo, 
                            java.util.stream.Collectors.counting()));
                    
                    analisis.append("\nEstado de préstamos recientes:\n");
                    prestamosPorEstado.forEach((estado, count) -> 
                        analisis.append(String.format("  - %s: %d\n", estado, count)));
                    
                    // 5. RECOMENDACIONES PRIORITARIAS
                    analisis.append("\n\n5. RECOMENDACIONES PRIORITARIAS\n");
                    analisis.append("-".repeat(50)).append("\n");
                    
                    int prioridad = 1;
                    
                    if (!equiposUrgentes.isEmpty()) {
                        analisis.append(String.format("%d. EQUIPAMIENTOS - ACCIÓN INMEDIATA\n", prioridad++));
                        analisis.append("   Revisar y reparar equipos en estado crítico.\n");
                        analisis.append("   Tiempo estimado: 1-2 semanas\n\n");
                    }
                    
                    if (!insumosAgotados.isEmpty()) {
                        analisis.append(String.format("%d. INSUMOS - COMPRA URGENTE\n", prioridad++));
                        analisis.append("   Adquirir insumos agotados para mantener operatividad.\n");
                        analisis.append("   Tiempo estimado: 1 semana\n\n");
                    }
                    
                    if (!insumosBajos.isEmpty()) {
                        analisis.append(String.format("%d. INVENTARIO - PLANIFICACIÓN\n", prioridad++));
                        analisis.append("   Establecer puntos de reorden para evitar agotamientos.\n");
                        analisis.append("   Implementar sistema de alertas automáticas.\n\n");
                    }
                    
                    analisis.append(String.format("%d. MANTENIMIENTO PREVENTIVO\n", prioridad++));
                    analisis.append("   Establecer calendario de mantenimiento regular.\n");
                    analisis.append("   Frecuencia recomendada: cada 3 meses\n\n");
                    
                    analisis.append(String.format("%d. OPTIMIZACIÓN DE PROCESOS\n", prioridad++));
                    analisis.append("   Implementar sistema de predicción automática.\n");
                    analisis.append("   Capacitar personal en gestión preventiva.\n\n");
                    
                    // 6. PROYECCIONES Y TENDENCIAS
                    analisis.append("\n6. PROYECCIONES Y TENDENCIAS\n");
                    analisis.append("-".repeat(50)).append("\n");
                    
                    // Tendencia de uso
                    if (prestamosRecientes.size() > 0) {
                        double tendenciaMensual = prestamosRecientes.size() / 6.0;
                        analisis.append(String.format("Proyección de préstamos próximos 3 meses: %.0f\n", tendenciaMensual * 3));
                        
                        // Cálculo de eficiencia
                        long prestamosDevueltos = prestamosRecientes.stream()
                            .filter(p -> p.getEstadoPrestamo().equals("Devuelto"))
                            .count();
                        
                        if (prestamosRecientes.size() > 0) {
                            double eficiencia = (prestamosDevueltos * 100.0) / prestamosRecientes.size();
                            analisis.append(String.format("Eficiencia de devoluciones: %.1f%%\n", eficiencia));
                            
                            if (eficiencia < 80) {
                                analisis.append("   ⚠ ALERTA: Eficiencia de devoluciones baja\n");
                            }
                        }
                    }
                    
                    // 7. INDICADORES CLAVE
                    analisis.append("\n\n7. INDICADORES CLAVE DE RENDIMIENTO (KPI)\n");
                    analisis.append("-".repeat(50)).append("\n");
                    
                    double disponibilidadEquipos = equipamientos.stream()
                        .mapToDouble(e -> e.getDisponibilidad().equals("Disponible") ? 1.0 : 0.0)
                        .average().orElse(0.0) * 100;
                    
                    double stockAdecuado = insumos.stream()
                        .mapToDouble(i -> i.getCantidad() > 10 ? 1.0 : 0.0)
                        .average().orElse(0.0) * 100;
                    
                    analisis.append(String.format("• Disponibilidad de equipos: %.1f%%\n", disponibilidadEquipos));
                    analisis.append(String.format("• Nivel de stock adecuado: %.1f%%\n", stockAdecuado));
                    analisis.append(String.format("• Equipos operativos: %d de %d\n", 
                        (int)equipamientos.stream().filter(e -> !e.getEstado().equals("Dañado")).count(),
                        equipamientos.size()));
                    
                    // Estado general del laboratorio
                    analisis.append("\n\n8. EVALUACIÓN GENERAL DEL LABORATORIO\n");
                    analisis.append("-".repeat(50)).append("\n");
                    
                    double puntuacionGeneral = (disponibilidadEquipos + stockAdecuado) / 2;
                    String estadoGeneral;
                    
                    if (puntuacionGeneral >= 80) {
                        estadoGeneral = "EXCELENTE";
                    } else if (puntuacionGeneral >= 60) {
                        estadoGeneral = "BUENO";
                    } else if (puntuacionGeneral >= 40) {
                        estadoGeneral = "REGULAR";
                    } else {
                        estadoGeneral = "CRÍTICO";
                    }
                    
                    analisis.append(String.format("Estado general: %s (%.1f/100)\n", estadoGeneral, puntuacionGeneral));
                    
                    if (puntuacionGeneral < 60) {
                        analisis.append("\n⚠ RECOMENDACIÓN: Se requiere atención inmediata para mejorar\n");
                        analisis.append("   la operatividad del laboratorio.\n");
                    }
                    
                    analisis.append("\n").append("=".repeat(80)).append("\n");
                    analisis.append("                    FIN DEL ANÁLISIS\n");
                    analisis.append("=".repeat(80));
                    
                } catch (SQLException e) {
                    analisis.append("Error al generar análisis completo: ").append(e.getMessage());
                }
                
                SwingUtilities.invokeLater(() -> {
                    areaAnalisisGeneral.setText(analisis.toString());
                    areaAnalisisGeneral.setCaretPosition(0);
                });
                
                return null;
            }
            
            @Override
            protected void done() {
                progressBar.setIndeterminate(false);
                progressBar.setString("Análisis completo generado");
            }
        };
        worker.execute();
    }
    
    private void exportarReporteEquipamientos() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar Reporte de Equipamientos");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Archivos de texto", "txt"));
        
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                java.io.File file = fileChooser.getSelectedFile();
                if (!file.getName().endsWith(".txt")) {
                    file = new java.io.File(file.getAbsolutePath() + ".txt");
                }
                
                StringBuilder reporte = new StringBuilder();
                reporte.append("REPORTE DE PREDICCIONES - EQUIPAMIENTOS\n");
                reporte.append("Fecha: ").append(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date())).append("\n");
                reporte.append("="+ "=".repeat(60)).append("\n\n");
                
                // Datos de la tabla
                for (int i = 0; i < modeloEquipamientos.getRowCount(); i++) {
                    reporte.append(String.format("ID: %s\n", modeloEquipamientos.getValueAt(i, 0)));
                    reporte.append(String.format("Equipamiento: %s\n", modeloEquipamientos.getValueAt(i, 1)));
                    reporte.append(String.format("Estado: %s\n", modeloEquipamientos.getValueAt(i, 2)));
                    reporte.append(String.format("Días sin mantenimiento: %s\n", modeloEquipamientos.getValueAt(i, 3)));
                    reporte.append(String.format("Predicción: %s\n", modeloEquipamientos.getValueAt(i, 4)));
                    reporte.append(String.format("Prioridad: %s\n", modeloEquipamientos.getValueAt(i, 5)));
                    reporte.append(String.format("Recomendación: %s\n", modeloEquipamientos.getValueAt(i, 6)));
                    reporte.append("-".repeat(50)).append("\n");
                }
                
                reporte.append("\n").append(areaRecomendacionesEquipamientos.getText());
                
                java.nio.file.Files.write(file.toPath(), reporte.toString().getBytes());
                JOptionPane.showMessageDialog(this, "Reporte exportado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al exportar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void exportarReporteInsumos() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar Reporte de Insumos");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Archivos de texto", "txt"));
        
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                java.io.File file = fileChooser.getSelectedFile();
                if (!file.getName().endsWith(".txt")) {
                    file = new java.io.File(file.getAbsolutePath() + ".txt");
                }
                
                StringBuilder reporte = new StringBuilder();
                reporte.append("REPORTE DE PREDICCIONES - INSUMOS\n");
                reporte.append("Fecha: ").append(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date())).append("\n");
                reporte.append("="+ "=".repeat(60)).append("\n\n");
                
                // Datos de la tabla
                for (int i = 0; i < modeloInsumos.getRowCount(); i++) {
                    reporte.append(String.format("ID: %s\n", modeloInsumos.getValueAt(i, 0)));
                    reporte.append(String.format("Insumo: %s\n", modeloInsumos.getValueAt(i, 1)));
                    reporte.append(String.format("Cantidad actual: %s\n", modeloInsumos.getValueAt(i, 2)));
                    reporte.append(String.format("Consumo promedio/día: %s\n", modeloInsumos.getValueAt(i, 3)));
                    reporte.append(String.format("Días restantes: %s\n", modeloInsumos.getValueAt(i, 4)));
                    reporte.append(String.format("Fecha agotamiento: %s\n", modeloInsumos.getValueAt(i, 5)));
                    reporte.append(String.format("Prioridad: %s\n", modeloInsumos.getValueAt(i, 6)));
                    reporte.append(String.format("Recomendación: %s\n", modeloInsumos.getValueAt(i, 7)));
                    reporte.append("-".repeat(50)).append("\n");
                }
                
                reporte.append("\n").append(areaRecomendacionesInsumos.getText());
                
                java.nio.file.Files.write(file.toPath(), reporte.toString().getBytes());
                JOptionPane.showMessageDialog(this, "Reporte exportado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al exportar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void exportarAnalisisCompleto() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar Análisis Completo");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Archivos de texto", "txt"));
        
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                java.io.File file = fileChooser.getSelectedFile();
                if (!file.getName().endsWith(".txt")) {
                    file = new java.io.File(file.getAbsolutePath() + ".txt");
                }
                
                java.nio.file.Files.write(file.toPath(), areaAnalisisGeneral.getText().getBytes());
                JOptionPane.showMessageDialog(this, "Análisis exportado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al exportar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    // Clases auxiliares para predicciones
    private static class PrediccionEquipamiento {
        Equipamiento equipamiento;
        int diasSinMantenimiento;
        String prediccionMantenimiento;
        String prioridad;
        String recomendacion;
    }
    
    private static class PrediccionInsumo {
        Insumo insumo;
        double consumoPromedioDiario;
        int diasRestantes;
        Date fechaAgotamiento;
        String prioridad;
        String recomendacion;
    }
    
    // Renderer personalizado para colorear filas según prioridad
    private static class PriorityTableCellRenderer extends javax.swing.table.DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, 
                boolean hasFocus, int row, int column) {
            
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            if (!isSelected) {
                String prioridad = "";
                
                // Obtener la prioridad de la fila
                if (table.getColumnCount() > 5) { // Para equipamientos
                    prioridad = (String) table.getValueAt(row, 5);
                } else if (table.getColumnCount() > 6) { // Para insumos
                    prioridad = (String) table.getValueAt(row, 6);
                }
                
                switch (prioridad) {
                    case "URGENTE":
                        c.setBackground(new Color(255, 200, 200)); // Rojo claro
                        break;
                    case "ALTA":
                        c.setBackground(new Color(255, 230, 200)); // Naranja claro
                        break;
                    case "MEDIA":
                        c.setBackground(new Color(255, 255, 200)); // Amarillo claro
                        break;
                    case "BAJA":
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
}