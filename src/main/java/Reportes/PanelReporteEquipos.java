/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package Reportes;

import Clases.Equipos;
import Controles.ControladorEquipo;
import Controles.ControladorHistorialEquipos;
import com.spire.doc.*;
import com.spire.doc.documents.*;
import com.spire.doc.fields.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * Panel para generar reportes de equipos en formato Word.
 * Autor: Equipo Soldados Caídos (mejorado)
 */
public class PanelReporteEquipos extends JPanel {

    private JTextField txtIdEquipo;
    private JComboBox<String> cboTipoReporte;
    private JButton btnGenerarWord;
    private JTextField txtFecha;
    private ControladorEquipo controlEquipo;
    private ControladorHistorialEquipos controlHistorial;

    public PanelReporteEquipos() {
        // Inicializar controladores
        controlEquipo = new ControladorEquipo();
        controlHistorial = new ControladorHistorialEquipos();
        
        // Configurar panel
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Panel de título
        JPanel panelTitulo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel lblTitulo = new JLabel("Generación de Reportes de Equipos");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        panelTitulo.add(lblTitulo);
        add(panelTitulo, BorderLayout.NORTH);
        
        // Panel de opciones
        JPanel panelOpciones = new JPanel();
        panelOpciones.setLayout(new GridBagLayout());
        panelOpciones.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), 
                "Opciones de Reporte", 
                TitledBorder.LEFT, 
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14)));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Tipo de reporte
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel lblTipoReporte = new JLabel("Tipo de Reporte:");
        lblTipoReporte.setFont(new Font("Arial", Font.PLAIN, 12));
        panelOpciones.add(lblTipoReporte, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        String[] opciones = {"Reporte por ID de Equipo", "Reporte de Todos los Equipos"};
        cboTipoReporte = new JComboBox<>(opciones);
        cboTipoReporte.setPreferredSize(new Dimension(250, 25));
        panelOpciones.add(cboTipoReporte, gbc);
        
        // ID Equipo
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        JLabel lblIdEquipo = new JLabel("ID Equipo:");
        lblIdEquipo.setFont(new Font("Arial", Font.PLAIN, 12));
        panelOpciones.add(lblIdEquipo, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        txtIdEquipo = new JTextField(15);
        txtIdEquipo.setPreferredSize(new Dimension(250, 25));
        panelOpciones.add(txtIdEquipo, gbc);
        
        // Fecha
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        JLabel lblFecha = new JLabel("Fecha:");
        lblFecha.setFont(new Font("Arial", Font.PLAIN, 12));
        panelOpciones.add(lblFecha, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        txtFecha = new JTextField(15);
        txtFecha.setPreferredSize(new Dimension(150, 25));
        // Establecer fecha actual
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        txtFecha.setText(dateFormat.format(new Date()));
        panelOpciones.add(txtFecha, gbc);
        
        gbc.gridx = 2;
        gbc.gridy = 2;
        JButton btnFechaActual = new JButton("Hoy");
        btnFechaActual.setPreferredSize(new Dimension(80, 25));
        btnFechaActual.addActionListener(e -> txtFecha.setText(dateFormat.format(new Date())));
        panelOpciones.add(btnFechaActual, gbc);
        
        // Botón generar
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        btnGenerarWord = new JButton("Generar Reporte Word");
        btnGenerarWord.setFont(new Font("Arial", Font.BOLD, 12));
        btnGenerarWord.setPreferredSize(new Dimension(200, 30));
        btnGenerarWord.setBackground(new Color(70, 130, 180));
        btnGenerarWord.setForeground(Color.WHITE);
        btnGenerarWord.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGenerarWord.setFocusPainted(false);
        panelOpciones.add(btnGenerarWord, gbc);
        
        // Oyente para tipo de reporte
        cboTipoReporte.addActionListener(e -> {
            boolean esReportePorId = cboTipoReporte.getSelectedIndex() == 0;
            txtIdEquipo.setEnabled(esReportePorId);
            lblIdEquipo.setEnabled(esReportePorId);
        });
        
        // Oyente para generar reporte
        btnGenerarWord.addActionListener(e -> generarReporte());
        
        // Añadir panel de opciones
        add(panelOpciones, BorderLayout.CENTER);
        
        // Panel de instrucciones
        JPanel panelInstrucciones = new JPanel();
        panelInstrucciones.setLayout(new BoxLayout(panelInstrucciones, BoxLayout.Y_AXIS));
        panelInstrucciones.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), 
                "Instrucciones", 
                TitledBorder.LEFT, 
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14)));
        
        JTextArea txtInstrucciones = new JTextArea(
                "1. Seleccione el tipo de reporte a generar.\n" +
                "2. Para reportes individuales, ingrese el ID del equipo.\n" +
                "3. Verifique la fecha del reporte.\n" +
                "4. Haga clic en 'Generar Reporte Word' para crear el documento.");
        txtInstrucciones.setEditable(false);
        txtInstrucciones.setBackground(new Color(240, 240, 240));
        txtInstrucciones.setFont(new Font("Arial", Font.PLAIN, 12));
        txtInstrucciones.setBorder(new EmptyBorder(10, 10, 10, 10));
        panelInstrucciones.add(txtInstrucciones);
        
        add(panelInstrucciones, BorderLayout.SOUTH);
    }
    
    /**
     * Determina qué tipo de reporte generar según la selección del usuario
     */
    private void generarReporte() {
        try {
            if (txtFecha.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor ingrese una fecha válida", 
                        "Fecha Requerida", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (cboTipoReporte.getSelectedIndex() == 0) {
                // Reporte por ID
                String id = txtIdEquipo.getText().trim();
                if (id.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Por favor ingrese un ID de equipo", 
                            "ID Requerido", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                generarReportePorID(id);
            } else {
                // Reporte de todos los equipos
                generarReporteTodos();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al generar el reporte: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Genera un reporte para un equipo específico por ID
     * @param id ID del equipo
     */
    private void generarReportePorID(String id) {
        try {
            Equipos equipo = controlEquipo.buscarPorId(id);
            
            if (equipo == null) {
                JOptionPane.showMessageDialog(this, "No se encontró el equipo con ID: " + id,
                        "Equipo no encontrado", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Crear documento
            Document doc = new Document();
            Section section = doc.addSection();
            
            // Título central: Universidad Salesiana de Bolivia
            Paragraph parUniversidad = section.addParagraph();
            TextRange trUniversidad = parUniversidad.appendText("Universidad Salesiana de Bolivia");
            trUniversidad.getCharacterFormat().setBold(true);
            trUniversidad.getCharacterFormat().setFontSize(16f);
            parUniversidad.getFormat().setHorizontalAlignment(HorizontalAlignment.Center);
            
            // Título central: Reporte de equipo 'id equipo'
            Paragraph parReporteEquipo = section.addParagraph();
            TextRange trReporteEquipo = parReporteEquipo.appendText("Reporte de equipo " + id);
            trReporteEquipo.getCharacterFormat().setBold(true);
            trReporteEquipo.getCharacterFormat().setFontSize(14f);
            parReporteEquipo.getFormat().setHorizontalAlignment(HorizontalAlignment.Center);
            
            // Título central: Fecha
            Paragraph parFecha = section.addParagraph();
            TextRange trFecha = parFecha.appendText("Fecha: " + txtFecha.getText());
            trFecha.getCharacterFormat().setBold(true);
            trFecha.getCharacterFormat().setFontSize(12f);
            parFecha.getFormat().setHorizontalAlignment(HorizontalAlignment.Center);
            
            // Salto de línea
            section.addParagraph();
            
            // Subtítulo: Información del Equipo
            Paragraph parInfoEquipo = section.addParagraph();
            TextRange trInfoEquipo = parInfoEquipo.appendText("Información del Equipo:");
            trInfoEquipo.getCharacterFormat().setBold(true);
            trInfoEquipo.getCharacterFormat().setFontSize(12f);
            
            // Detalles del equipo
            addDetalleBold(section, "Procesador", equipo.getProcesador());
            addDetalleBold(section, "RAM", equipo.getRam());
            addDetalleBold(section, "Dispositivo", equipo.getDispositivo());
            addDetalleBold(section, "Monitor", equipo.getMonitor());
            addDetalleBold(section, "Teclado", equipo.getTeclado());
            addDetalleBold(section, "Mouse", equipo.getMouse());
            addDetalleBold(section, "Estado", equipo.getEstado());
            addDetalleBold(section, "Laboratorio", String.valueOf(equipo.getIdLaboratorio()));
            
            // Salto de línea
            section.addParagraph();
            
            // Subtítulo: Historial del Equipo
            Paragraph parHistorial = section.addParagraph();
            TextRange trHistorial = parHistorial.appendText("Historial del Equipo:");
            trHistorial.getCharacterFormat().setBold(true);
            trHistorial.getCharacterFormat().setFontSize(12f);
            
            // Tabla de historial
            List<Object[]> historial = controlHistorial.buscarHistorialPorEquipo(id);
            if (historial.isEmpty()) {
                section.addParagraph().appendText("No hay registros de historial para este equipo.");
            } else {
                // Crear tabla para historial
                Table table = section.addTable(true);
                table.resetCells(historial.size() + 1, 4); // +1 para encabezado
                
                // Encabezados
                String[] encabezados = {"RU", "Fecha", "Categoría", "Descripción"};
                for (int i = 0; i < encabezados.length; i++) {
                    TableCell cell = table.get(0, i);
                    Paragraph p = cell.addParagraph();
                    p.appendText(encabezados[i]);
                    p.getFormat().setHorizontalAlignment(HorizontalAlignment.Center);
                    cell.getCellFormat().setBackColor(new Color(220, 220, 220));
                }
                
                // Datos
                for (int i = 0; i < historial.size(); i++) {
                    Object[] registro = historial.get(i);
                    
                    table.get(i + 1, 0).addParagraph().appendText(String.valueOf(registro[1])); // RU
                    
                    // Formatear fecha
                    Date fecha = (Date)registro[2];
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    String fechaFormateada = fecha != null ? sdf.format(fecha) : "";
                    table.get(i + 1, 1).addParagraph().appendText(fechaFormateada); // Fecha
                    
                    table.get(i + 1, 2).addParagraph().appendText(String.valueOf(registro[3])); // Categoría
                    table.get(i + 1, 3).addParagraph().appendText(String.valueOf(registro[4])); // Descripción
                }
                
                // Formatear tabla
                table.autoFit(AutoFitBehaviorType.Auto_Fit_To_Contents);
            }
            
            // Guardar documento
            String ruta = "Reporte_Equipo_" + id + ".docx";
            doc.saveToFile(ruta, FileFormat.Docx_2013);
            
            // Abrir documento
            Desktop.getDesktop().open(new java.io.File(ruta));
            JOptionPane.showMessageDialog(this, "Reporte generado exitosamente: " + ruta,
                    "Reporte Generado", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al generar el reporte: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Genera un reporte con todos los equipos registrados
     */
    private void generarReporteTodos() {
        try {
            List<Equipos> listaEquipos = controlEquipo.listar();
            
            if (listaEquipos.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No hay equipos registrados en el sistema.",
                        "Sin datos", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            // Crear documento
            Document doc = new Document();
            Section section = doc.addSection();
            
            // Título central: Universidad Salesiana de Bolivia
            Paragraph parUniversidad = section.addParagraph();
            TextRange trUniversidad = parUniversidad.appendText("Universidad Salesiana de Bolivia");
            trUniversidad.getCharacterFormat().setBold(true);
            trUniversidad.getCharacterFormat().setFontSize(16f);
            parUniversidad.getFormat().setHorizontalAlignment(HorizontalAlignment.Center);
            
            // Título central: Reporte de todos los equipos
            Paragraph parReporteEquipo = section.addParagraph();
            TextRange trReporteEquipo = parReporteEquipo.appendText("Reporte de todos los equipos");
            trReporteEquipo.getCharacterFormat().setBold(true);
            trReporteEquipo.getCharacterFormat().setFontSize(14f);
            parReporteEquipo.getFormat().setHorizontalAlignment(HorizontalAlignment.Center);
            
            // Título central: Fecha
            Paragraph parFecha = section.addParagraph();
            TextRange trFecha = parFecha.appendText("Fecha: " + txtFecha.getText());
            trFecha.getCharacterFormat().setBold(true);
            trFecha.getCharacterFormat().setFontSize(12f);
            parFecha.getFormat().setHorizontalAlignment(HorizontalAlignment.Center);
            
            // Procesar cada equipo
            for (Equipos equipo : listaEquipos) {
                String id = equipo.getIdEquipos();
                
                // Salto de línea
                section.addParagraph();
                
                // Subtítulo: Información del Equipo 'Id del equipo'
                Paragraph parInfoEquipo = section.addParagraph();
                TextRange trInfoEquipo = parInfoEquipo.appendText("Información del Equipo '" + id + "':");
                trInfoEquipo.getCharacterFormat().setBold(true);
                trInfoEquipo.getCharacterFormat().setFontSize(12f);
                
                // Tabla con información del equipo
                Table tableInfo = section.addTable(true);
                tableInfo.resetCells(2, 8); // 2 filas: encabezado y datos
                
                // Encabezados
                String[] encabezados = {
                    "Procesador", "RAM", "Dispositivo", "Monitor", 
                    "Teclado", "Mouse", "Estado", "Laboratorio"
                };
                
                for (int i = 0; i < encabezados.length; i++) {
                    TableCell cell = tableInfo.get(0, i);
                    Paragraph p = cell.addParagraph();
                    p.appendText(encabezados[i]);
                    p.getFormat().setHorizontalAlignment(HorizontalAlignment.Center);
                    cell.getCellFormat().setBackColor(new Color(220, 220, 220));
                }
                
                // Datos del equipo
                tableInfo.get(1, 0).addParagraph().appendText(equipo.getProcesador());
                tableInfo.get(1, 1).addParagraph().appendText(equipo.getRam());
                tableInfo.get(1, 2).addParagraph().appendText(equipo.getDispositivo());
                tableInfo.get(1, 3).addParagraph().appendText(equipo.getMonitor());
                tableInfo.get(1, 4).addParagraph().appendText(equipo.getTeclado());
                tableInfo.get(1, 5).addParagraph().appendText(equipo.getMouse());
                tableInfo.get(1, 6).addParagraph().appendText(equipo.getEstado());
                tableInfo.get(1, 7).addParagraph().appendText(String.valueOf(equipo.getIdLaboratorio()));
                
                // Formatear tabla
                tableInfo.autoFit(AutoFitBehaviorType.Auto_Fit_To_Contents);
                
                // Salto de línea
                section.addParagraph();
                
                // Subtítulo: Historial del Equipo 'Id del equipo'
                Paragraph parHistorial = section.addParagraph();
                TextRange trHistorial = parHistorial.appendText("Historial del Equipo '" + id + "':");
                trHistorial.getCharacterFormat().setBold(true);
                trHistorial.getCharacterFormat().setFontSize(12f);
                
                // Tabla de historial
                List<Object[]> historial = controlHistorial.buscarHistorialPorEquipo(id);
                if (historial.isEmpty()) {
                    section.addParagraph().appendText("No hay registros de historial para este equipo.");
                } else {
                    // Crear tabla para historial
                    Table tableHistorial = section.addTable(true);
                    tableHistorial.resetCells(historial.size() + 1, 4); // +1 para encabezado
                    
                    // Encabezados historial
                    String[] encabezadosHistorial = {"RU", "Fecha", "Categoría", "Descripción"};
                    for (int i = 0; i < encabezadosHistorial.length; i++) {
                        TableCell cell = tableHistorial.get(0, i);
                        Paragraph p = cell.addParagraph();
                        p.appendText(encabezadosHistorial[i]);
                        p.getFormat().setHorizontalAlignment(HorizontalAlignment.Center);
                        cell.getCellFormat().setBackColor(new Color(220, 220, 220));
                    }
                    
                    // Datos historial
                    for (int i = 0; i < historial.size(); i++) {
                        Object[] registro = historial.get(i);
                        
                        tableHistorial.get(i + 1, 0).addParagraph().appendText(String.valueOf(registro[1])); // RU
                        
                        // Formatear fecha
                        Date fecha = (Date)registro[2];
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        String fechaFormateada = fecha != null ? sdf.format(fecha) : "";
                        tableHistorial.get(i + 1, 1).addParagraph().appendText(fechaFormateada); // Fecha
                        
                        tableHistorial.get(i + 1, 2).addParagraph().appendText(String.valueOf(registro[3])); // Categoría
                        tableHistorial.get(i + 1, 3).addParagraph().appendText(String.valueOf(registro[4])); // Descripción
                    }
                    
                    // Formatear tabla
                    tableHistorial.autoFit(AutoFitBehaviorType.Auto_Fit_To_Contents);
                }
                
                // Salto de línea entre equipos (excepto el último)
                /*
                if (listaEquipos.indexOf(equipo) < listaEquipos.size() - 1) {
                    section.addParagraph().appendBreak(BreakType.Page_Break);
                }
                */
                section.addParagraph();
            }
            
            // Guardar documento
            String ruta = "Reporte_Todos_Equipos.docx";
            doc.saveToFile(ruta, FileFormat.Docx_2013);
            
            // Abrir documento
            Desktop.getDesktop().open(new java.io.File(ruta));
            JOptionPane.showMessageDialog(this, "Reporte generado exitosamente: " + ruta,
                    "Reporte Generado", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al generar el reporte: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Agrega un detalle al documento con etiqueta en negrita
     * @param section Sección del documento
     * @param etiqueta Etiqueta del detalle (en negrita)
     * @param valor Valor del detalle
     */
    private void addDetalleBold(Section section, String etiqueta, String valor) {
        Paragraph p = section.addParagraph();
        TextRange trEtiqueta = p.appendText(etiqueta + ": ");
        trEtiqueta.getCharacterFormat().setBold(true);
        p.appendText(valor);
    }
}