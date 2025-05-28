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
        
        // Panel principal con pestañas
        tabbedPane = new JTabbedPane();
        
        // Pestaña de Equipamientos
        JPanel panelEquipamientos = crearPanelEquipamientos();
        tabbedPane.addTab("Predicción Equipamientos", panelEquipamientos);
        
        // Pestaña de Insumos
        JPanel panelInsumos = crearPanelInsumos();
        tabbedPane.addTab("Predicción Insumos", panelInsumos);
        
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
    
    private void generarRecomendacionesEquipamientos(List<AnalisisEquipamiento> analisis, StringBuilder recomendaciones) {
        recomendaciones.append("=== RECOMENDACIONES DE EQUIPAMIENTOS ===\n\n");
        
        long equiposReparacion = analisis.stream().filter(a -> a.recomendacion.equals("Reparación")).count();
        long equiposBaja = analisis.stream().filter(a -> a.recomendacion.equals("Dar Baja")).count();
        long equiposNormal = analisis.stream().filter(a -> a.recomendacion.equals("Normal")).count();
        
        recomendaciones.append(String.format("• Equipos que necesitan REPARACIÓN: %d\n", equiposReparacion));
        recomendaciones.append(String.format("• Equipos para DAR DE BAJA: %d\n", equiposBaja));
        recomendaciones.append(String.format("• Equipos en estado NORMAL: %d\n\n", equiposNormal));
        
        if (equiposReparacion > 0) {
            recomendaciones.append("EQUIPOS QUE REQUIEREN REPARACIÓN:\n");
            analisis.stream()
                .filter(a -> a.recomendacion.equals("Reparación"))
                .forEach(a -> recomendaciones.append(String.format("- %s (Estado: %s, Préstamos: %d)\n", 
                    a.equipamiento.getNombreEquipamiento(), a.equipamiento.getEstado(), a.totalPrestamos)));
            recomendaciones.append("\n");
        }
        
        if (equiposBaja > 0) {
            recomendaciones.append("EQUIPOS PARA DAR DE BAJA:\n");
            analisis.stream()
                .filter(a -> a.recomendacion.equals("Dar Baja"))
                .forEach(a -> recomendaciones.append(String.format("- %s (Estado: %s)\n", 
                    a.equipamiento.getNombreEquipamiento(), a.equipamiento.getEstado())));
            recomendaciones.append("\n");
        }
        
        recomendaciones.append("RESUMEN GENERAL:\n");
        recomendaciones.append(String.format("Total de equipamientos analizados: %d\n", analisis.size()));
        recomendaciones.append(String.format("Equipos que requieren atención: %d\n", equiposReparacion + equiposBaja));
    }
    
    private void generarRecomendacionesInsumos(List<AnalisisInsumo> analisis, StringBuilder recomendaciones) {
        recomendaciones.append("=== RECOMENDACIONES DE INSUMOS ===\n\n");
        
        long insumosUrgente = analisis.stream().filter(a -> a.recomendacion.equals("Reposición Urgente")).count();
        long insumosNecesaria = analisis.stream().filter(a -> a.recomendacion.equals("Reposición Necesaria")).count();
        
        recomendaciones.append(String.format("• Insumos con REPOSICIÓN URGENTE: %d\n", insumosUrgente));
        recomendaciones.append(String.format("• Insumos con REPOSICIÓN NECESARIA: %d\n\n", insumosNecesaria));
        
        if (insumosUrgente > 0) {
            recomendaciones.append("INSUMOS PARA REPOSICIÓN URGENTE:\n");
            analisis.stream()
                .filter(a -> a.recomendacion.equals("Reposición Urgente"))
                .forEach(a -> recomendaciones.append(String.format("- %s (Cantidad: %d, Consumo diario: %.2f)\n", 
                    a.insumo.getNombreInsumo(), a.insumo.getCantidad(), a.consumoDiario)));
            recomendaciones.append("\n");
        }
        
        if (insumosNecesaria > 0) {
            recomendaciones.append("INSUMOS PARA REPOSICIÓN NECESARIA:\n");
            analisis.stream()
                .filter(a -> a.recomendacion.equals("Reposición Necesaria"))
                .forEach(a -> recomendaciones.append(String.format("- %s (Cantidad: %d, Consumo diario: %.2f)\n", 
                    a.insumo.getNombreInsumo(), a.insumo.getCantidad(), a.consumoDiario)));
            recomendaciones.append("\n");
        }
        
        recomendaciones.append("ANÁLISIS POR CATEGORÍA:\n");
        Map<String, List<AnalisisInsumo>> categorias = analisis.stream()
            .collect(java.util.stream.Collectors.groupingBy(a -> a.insumo.getCategoria()));
        
        categorias.forEach((categoria, lista) -> {
            long criticos = lista.stream().filter(a -> a.estado.equals("Crítico") || a.estado.equals("Agotado")).count();
            recomendaciones.append(String.format("- %s: %d insumos (%d críticos)\n", categoria, lista.size(), criticos));
        });
    }
    
    // Métodos auxiliares
    private int contarPrestamosEquipamiento(int idEquipamiento) {
    try {
        List<Prestamo> prestamos = controladorPrestamo.listar();
        int contador = 0;

        for (Prestamo prestamo : prestamos) {
            List<Map<String, Object>> equipos = controladorPrestamo
                .obtenerEquipamientosPrestamoConCantidades(prestamo.getIdPrestamo());

            for (Map<String, Object> equipo : equipos) {
                int id = (int) equipo.get("id_equipamiento");
                if (id == idEquipamiento) {
                    contador++;
                    break; // No es necesario revisar más si ya lo contiene
                }
            }
        }
        return contador;
    } catch (SQLException e) {
        System.err.println("Error al contar préstamos para equipamiento " + idEquipamiento + ": " + e.getMessage());
        return 0;
    }
}

    
    private String obtenerUltimaActividad(List<Object[]> historial) {
        if (historial.isEmpty()) {
            return "Sin actividad";
        }
        
        Date ultimaFecha = null;
        for (Object[] registro : historial) {
            Date fecha = (Date) registro[2];
            if (ultimaFecha == null || fecha.after(ultimaFecha)) {
                ultimaFecha = fecha;
            }
        }
        
        if (ultimaFecha != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            return sdf.format(ultimaFecha);
        }
        
        return "Sin fecha";
    }
    
    private String obtenerUltimaCategoriaMantenimiento(List<Object[]> historial) {
        String ultimaCategoria = null;
        Date ultimaFecha = null;
        
        for (Object[] registro : historial) {
            String categoria = (String) registro[3];
            Date fecha = (Date) registro[2];
            
            if (categoria != null && (categoria.equals("Reparación") || 
                                     categoria.equals("Actualización") || 
                                     categoria.equals("Restauración"))) {
                if (ultimaFecha == null || fecha.after(ultimaFecha)) {
                    ultimaFecha = fecha;
                    ultimaCategoria = categoria;
                }
            }
        }
        
        return ultimaCategoria;
    }
    
    private int calcularDiasSinActividad(List<Object[]> historial) {
        if (historial.isEmpty()) {
            return 365;
        }
        
        Date ultimaActividad = null;
        for (Object[] registro : historial) {
            Date fecha = (Date) registro[2];
            if (ultimaActividad == null || fecha.after(ultimaActividad)) {
                ultimaActividad = fecha;
            }
        }
        
        if (ultimaActividad == null) {
            return 365;
        }
        
        long diffMs = System.currentTimeMillis() - ultimaActividad.getTime();
        return (int) TimeUnit.MILLISECONDS.toDays(diffMs);
    }
    
    private double calcularConsumoPromedio(int idInsumo) throws SQLException {
        List<Prestamo> prestamos = controladorPrestamo.listar();
        
        Calendar hace30Dias = Calendar.getInstance();
        hace30Dias.add(Calendar.DAY_OF_MONTH, -30);
        
        int totalConsumo = 0;
        int diasConConsumo = 0;
        
        for (Prestamo prestamo : prestamos) {
            if (prestamo.getFechaPrestamo().after(hace30Dias.getTime())) {
                try {
                    Map<Integer, Integer> insumos = controladorPrestamo
                        .obtenerInsumosPrestamoConCantidades(prestamo.getIdPrestamo());
                    
                    if (insumos.containsKey(idInsumo)) {
                        totalConsumo += insumos.get(idInsumo);
                        diasConConsumo++;
                    }
                } catch (SQLException e) {
                    // Continuar con el siguiente préstamo
                }
            }
        }
        
        if (diasConConsumo == 0) {
            return 0;
        }
        
        return (double) totalConsumo / 30.0; // Promedio diario en 30 días
    }
    
    private int obtenerPrioridadNumerica(String recomendacion) {
        switch (recomendacion) {
            case "Reposición Urgente":
            case "Dar Baja":
                return 4;
            case "Reparación":
            case "Reposición Necesaria":
                return 3;
            case "Monitorear":
                return 2;
            case "Normal":
                return 1;
            default:
                return 0;
        }
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
}