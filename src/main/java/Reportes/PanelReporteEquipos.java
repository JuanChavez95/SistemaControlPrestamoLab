/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package Reportes;

import Clases.Equipos;
import Clases.HistorialGeneral;
import Controles.ControladorEquipo;
import Controles.ControladorHistorialEquipos;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.awt.Desktop;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

// Importaciones para iText PDF
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

//Modificar las importaciones para incluir JFreeChart
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Panel para generar reportes de equipos en formato PDF.
 * Autor: Equipo Soldados Caídos (mejorado)
 */
public class PanelReporteEquipos extends JPanel {

    private JTextField txtIdEquipo;
    private JComboBox<String> cboTipoReporte;
    private JButton btnGenerarPDF;
    private JTextField txtFecha;
    private ControladorEquipo controlEquipo;
    private ControladorHistorialEquipos controlHistorial;
    private static final String DIRECTORIO_REPORTES = "./reportes/";

    // Definición de fuentes para reutilización
   // Fuentes para PDF con iText (usando nombres totalmente calificados para evitar conflictos con java.awt.Font)
private static final com.itextpdf.text.Font FONT_TITULO = 
    new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 16, com.itextpdf.text.Font.BOLD);

private static final com.itextpdf.text.Font FONT_SUBTITULO = 
    new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 14, com.itextpdf.text.Font.BOLD);

private static final com.itextpdf.text.Font FONT_FECHA = 
    new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 12, com.itextpdf.text.Font.BOLD);

private static final com.itextpdf.text.Font FONT_NORMAL = 
    new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 12, com.itextpdf.text.Font.NORMAL);

private static final com.itextpdf.text.Font FONT_NEGRITA = 
    new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 12, com.itextpdf.text.Font.BOLD);

private static final com.itextpdf.text.Font FONT_TABLA_ENCABEZADO = 
    new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 12, com.itextpdf.text.Font.BOLD);

private static final com.itextpdf.text.Font FONT_TABLA_CELDA = 
    new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 12, com.itextpdf.text.Font.NORMAL);


    public PanelReporteEquipos() {
        controlEquipo = new ControladorEquipo();
        controlHistorial = new ControladorHistorialEquipos();
        crearDirectorioReportes();

        setLayout(new BorderLayout(20, 20));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(25, 25, 25, 25));

        // Título
        JLabel lblTitulo = new JLabel("Generación de Reportes de Equipos (PDF)", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(45, 62, 80));
        add(lblTitulo, BorderLayout.NORTH);

        // Panel de opciones
        JPanel panelOpciones = new JPanel(new GridBagLayout());
        panelOpciones.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(100, 149, 237), 2),
                "Opciones de Reporte",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14),
                new Color(25, 25, 112)));
        panelOpciones.setBackground(new Color(250, 250, 255));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Etiquetas y campos con nueva estética
        JLabel lblTipoReporte = new JLabel("Tipo de Reporte:");
        lblTipoReporte.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panelOpciones.add(lblTipoReporte, gbc);

        gbc.gridx = 1;
        // Añadir la nueva opción "Reporte Estadística de Equipos"
        String[] opciones = {"Reporte por ID de Equipo", "Reporte de Todos los Equipos", "Reporte Estadística de Equipos"};
        cboTipoReporte = new JComboBox<>(opciones);
        cboTipoReporte.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cboTipoReporte.setBackground(Color.WHITE);
        cboTipoReporte.setPreferredSize(new Dimension(240, 30));
        panelOpciones.add(cboTipoReporte, gbc);


        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel lblIdEquipo = new JLabel("ID Equipo:");
        lblIdEquipo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panelOpciones.add(lblIdEquipo, gbc);

        gbc.gridx = 1;
        txtIdEquipo = new JTextField();
        txtIdEquipo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtIdEquipo.setPreferredSize(new Dimension(240, 30));
        panelOpciones.add(txtIdEquipo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel lblFecha = new JLabel("Fecha:");
        lblFecha.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panelOpciones.add(lblFecha, gbc);

        gbc.gridx = 1;
        txtFecha = new JTextField();
        txtFecha.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtFecha.setPreferredSize(new Dimension(240, 30));
        txtFecha.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        panelOpciones.add(txtFecha, gbc);

        gbc.gridx = 2;
        JButton btnFechaActual = new JButton("Hoy");
        btnFechaActual.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnFechaActual.setBackground(new Color(100, 149, 237));
        btnFechaActual.setForeground(Color.WHITE);
        btnFechaActual.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnFechaActual.setFocusPainted(false);
        btnFechaActual.addActionListener(e -> txtFecha.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date())));
        panelOpciones.add(btnFechaActual, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        btnGenerarPDF = new JButton("Generar Reporte PDF");
        btnGenerarPDF.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnGenerarPDF.setBackground(new Color(60, 179, 113));
        btnGenerarPDF.setForeground(Color.WHITE);
        btnGenerarPDF.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnGenerarPDF.setPreferredSize(new Dimension(240, 35));
        btnGenerarPDF.setFocusPainted(false);
        panelOpciones.add(btnGenerarPDF, gbc);

        add(panelOpciones, BorderLayout.CENTER);

        // Instrucciones
        JPanel panelInstrucciones = new JPanel();
        panelInstrucciones.setLayout(new BorderLayout());
        panelInstrucciones.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(100, 149, 237), 2),
                "Instrucciones",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14),
                new Color(25, 25, 112)));
        panelInstrucciones.setBackground(new Color(245, 245, 255));

        JTextArea txtInstrucciones = new JTextArea(
                "✔ Seleccione el tipo de reporte a generar.\n" +
                "✔ Ingrese el ID del equipo si aplica.\n" +
                "✔ Verifique la fecha del reporte.\n" +
                "✔ Presione 'Generar Reporte PDF' para continuar.");
        txtInstrucciones.setEditable(false);
        txtInstrucciones.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtInstrucciones.setBackground(new Color(245, 245, 255));
        txtInstrucciones.setBorder(new EmptyBorder(10, 10, 10, 10));
        panelInstrucciones.add(txtInstrucciones);

        add(panelInstrucciones, BorderLayout.SOUTH);

        // Comportamiento dinámico
        cboTipoReporte.addActionListener(e -> {
            int selectedIndex = cboTipoReporte.getSelectedIndex();
            boolean esReportePorId = selectedIndex == 0;
            txtIdEquipo.setEnabled(esReportePorId);
            lblIdEquipo.setEnabled(esReportePorId);
        });

        btnGenerarPDF.addActionListener(e -> generarReporte());
    }
    
    /**
     * Crea el directorio para guardar los reportes si no existe
     */
    private void crearDirectorioReportes() {
        File directorio = new File(DIRECTORIO_REPORTES);
        if (!directorio.exists()) {
            if (directorio.mkdirs()) {
                System.out.println("Directorio de reportes creado: " + DIRECTORIO_REPORTES);
            } else {
                System.err.println("No se pudo crear el directorio de reportes: " + DIRECTORIO_REPORTES);
            }
        }
    }
    
    /**
     * Valida el formato de la fecha ingresada
     * @param fechaStr Fecha en formato string
     * @return true si la fecha es válida, false en caso contrario
     */
    private boolean validarFecha(String fechaStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);
        try {
            sdf.parse(fechaStr);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
    
    /**
     * Determina qué tipo de reporte generar según la selección del usuario
     */
    private void generarReporte() {
    try {
        String fechaStr = txtFecha.getText().trim();
        if (fechaStr.isEmpty() || !validarFecha(fechaStr)) {
            JOptionPane.showMessageDialog(this, 
                "Por favor ingrese una fecha válida en formato dd/MM/yyyy", 
                "Fecha Inválida", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int selectedIndex = cboTipoReporte.getSelectedIndex();
        
        if (selectedIndex == 0) {
            // Reporte por ID
            String id = txtIdEquipo.getText().trim();
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor ingrese un ID de equipo", 
                        "ID Requerido", JOptionPane.WARNING_MESSAGE);
                return;
            }
            generarReportePorID(id);
        } else if (selectedIndex == 1) {
            // Reporte de todos los equipos
            generarReporteTodos();
        } else if (selectedIndex == 2) {
            // Nuevo reporte estadístico
            generarReporteEstadistico();
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
            // Comprobar si el equipo existe
            Equipos equipo = controlEquipo.buscarPorId(id);
            
            if (equipo == null) {
                JOptionPane.showMessageDialog(this, "No se encontró el equipo con ID: " + id,
                        "Equipo no encontrado", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Ruta del archivo PDF
            String nombreArchivo = "Reporte_Equipo_" + id + ".pdf";
            String rutaCompleta = DIRECTORIO_REPORTES + nombreArchivo;
            
            try (FileOutputStream outputStream = new FileOutputStream(rutaCompleta)) {
                // Crear documento PDF
                Document document = new Document();
                PdfWriter.getInstance(document, outputStream);
                document.open();
                
                // Agregar encabezado
                agregarEncabezadoPDF(document, "REPORTE DE EQUIPO " + id);
                
                // Agregar información del equipo
                agregarInformacionEquipo(document, equipo);
                
                // Agregar historial del equipo
                agregarHistorialEquipo(document, id);
                
                document.close();
                
                // Abrir el documento PDF
                abrirArchivoPDF(rutaCompleta);
            }
        } catch (SQLException ex) {
            manejarError("Error al buscar el equipo en la base de datos", ex);
        } catch (Exception ex) {
            manejarError("Error al generar el reporte PDF", ex);
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
            
            // Ruta del archivo PDF
            String nombreArchivo = "Reporte_Todos_Equipos.pdf";
            String rutaCompleta = DIRECTORIO_REPORTES + nombreArchivo;
            
            try (FileOutputStream outputStream = new FileOutputStream(rutaCompleta)) {
                // Crear documento PDF
                Document document = new Document();
                PdfWriter.getInstance(document, outputStream);
                document.open();
                
                // Agregar encabezado general
                agregarEncabezadoPDF(document, "REPORTE DE TODOS LOS EQUIPOS");
                
                // Procesar cada equipo
                boolean isFirstEquipo = true;
                for (Equipos equipo : listaEquipos) {
                    String idEquipo = equipo.getIdEquipos();
                    
                    // Agregar salto de página excepto para el primer equipo
                    if (!isFirstEquipo) {
                        document.newPage();
                    } else {
                        isFirstEquipo = false;
                    }
                    
                    // Titulo del equipo
                    Paragraph tituloEquipo = new Paragraph("INFORMACIÓN DEL EQUIPO '" + idEquipo + "'", FONT_NEGRITA);
                    document.add(tituloEquipo);
                    document.add(new Paragraph(" ")); // Espacio
                    
                    // Agregar información del equipo (tabla resumida)
                    agregarTablaInfoEquipo(document, equipo);
                    
                    // Agregar historial del equipo
                    Paragraph tituloHistorial = new Paragraph("HISTORIAL DEL EQUIPO '" + idEquipo + "'", FONT_NEGRITA);
                    document.add(tituloHistorial);
                    document.add(new Paragraph(" ")); // Espacio
                    
                    agregarTablaHistorial(document, idEquipo);
                }
                
                document.close();
                
                // Abrir el documento PDF
                abrirArchivoPDF(rutaCompleta);
            }
        } catch (SQLException ex) {
            manejarError("Error al obtener la lista de equipos de la base de datos", ex);
        } catch (Exception ex) {
            manejarError("Error al generar el reporte PDF de todos los equipos", ex);
        }
    }
    
    /**
     * Agrega el encabezado estándar a un documento PDF
     * @param document Documento PDF
     * @param subtitulo Subtítulo específico para el reporte
     */
    private void agregarEncabezadoPDF(Document document, String subtitulo) throws Exception {
        // Crear tabla para encabezado principal
        PdfPTable tablaEncabezado = new PdfPTable(1);
        tablaEncabezado.setWidthPercentage(100);
        
        // Título: Universidad Salesiana de Bolivia
        PdfPCell cellUniversidad = new PdfPCell(new Phrase("UNIVERSIDAD SALESIANA DE BOLIVIA", FONT_TITULO));
        cellUniversidad.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellUniversidad.setBorder(Rectangle.BOX);
        tablaEncabezado.addCell(cellUniversidad);
        
        // Subtítulo: Reporte específico
        PdfPCell cellReporte = new PdfPCell(new Phrase(subtitulo, FONT_SUBTITULO));
        cellReporte.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellReporte.setBorder(Rectangle.BOX);
        tablaEncabezado.addCell(cellReporte);
        
        // Fecha
        PdfPCell cellFecha = new PdfPCell(new Phrase("FECHA: " + txtFecha.getText(), FONT_FECHA));
        cellFecha.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFecha.setBorder(Rectangle.BOX);
        tablaEncabezado.addCell(cellFecha);
        
        document.add(tablaEncabezado);
        document.add(new Paragraph(" ")); // Espacio
    }
    
    /**
     * Agrega la información detallada de un equipo al documento
     * @param document Documento PDF
     * @param equipo Objeto con la información del equipo
     */
    private void agregarInformacionEquipo(Document document, Equipos equipo) throws Exception {
        // Título de información
        PdfPTable tablaInfo = new PdfPTable(1);
        tablaInfo.setWidthPercentage(100);
        
        PdfPCell cellInfoTitulo = new PdfPCell(new Phrase("INFORMACIÓN DEL EQUIPO", FONT_NEGRITA));
        cellInfoTitulo.setBorder(Rectangle.BOX);
        tablaInfo.addCell(cellInfoTitulo);
        document.add(tablaInfo);
        
        // Tabla para detalles del equipo
        PdfPTable tablaDetalles = new PdfPTable(2);
        tablaDetalles.setWidthPercentage(100);
        tablaDetalles.setWidths(new float[]{30, 70});
        
        // Añadir detalles del equipo
        agregarDetallePDF(tablaDetalles, "Procesador:", equipo.getProcesador());
        agregarDetallePDF(tablaDetalles, "RAM:", equipo.getRam());
        agregarDetallePDF(tablaDetalles, "Dispositivo:", equipo.getDispositivo());
        agregarDetallePDF(tablaDetalles, "Monitor:", equipo.getMonitor());
        agregarDetallePDF(tablaDetalles, "Teclado:", equipo.getTeclado());
        agregarDetallePDF(tablaDetalles, "Mouse:", equipo.getMouse());
        agregarDetallePDF(tablaDetalles, "Estado:", equipo.getEstado());
        agregarDetallePDF(tablaDetalles, "Laboratorio:", String.valueOf(equipo.getIdLaboratorio()));
        
        document.add(tablaDetalles);
        document.add(new Paragraph(" ")); // Espacio
    }
    
    /**
     * Agrega una tabla resumen con la información del equipo
     * @param document Documento PDF
     * @param equipo Objeto con la información del equipo
     */
    private void agregarTablaInfoEquipo(Document document, Equipos equipo) throws Exception {
        // Tabla para información del equipo
        PdfPTable tablaInfoEquipo = new PdfPTable(8);
        tablaInfoEquipo.setWidthPercentage(100);
        
        // Definir anchos relativos de las columnas
        float[] anchosInfo = {14, 12, 14, 14, 14, 12, 10, 10};
        tablaInfoEquipo.setWidths(anchosInfo);
        
        // Encabezados de la tabla de información
        String[] encabezados = {
            "Procesador", "RAM", "Dispositivo", "Monitor", 
            "Teclado", "Mouse", "Estado", "Laboratorio"
        };
        
        for (String encabezado : encabezados) {
            PdfPCell cell = new PdfPCell(new Phrase(encabezado, FONT_TABLA_ENCABEZADO));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            tablaInfoEquipo.addCell(cell);
        }
        
        // Datos del equipo
        tablaInfoEquipo.addCell(new PdfPCell(new Phrase(equipo.getProcesador(), FONT_TABLA_CELDA)));
        tablaInfoEquipo.addCell(new PdfPCell(new Phrase(equipo.getRam(), FONT_TABLA_CELDA)));
        tablaInfoEquipo.addCell(new PdfPCell(new Phrase(equipo.getDispositivo(), FONT_TABLA_CELDA)));
        tablaInfoEquipo.addCell(new PdfPCell(new Phrase(equipo.getMonitor(), FONT_TABLA_CELDA)));
        tablaInfoEquipo.addCell(new PdfPCell(new Phrase(equipo.getTeclado(), FONT_TABLA_CELDA)));
        tablaInfoEquipo.addCell(new PdfPCell(new Phrase(equipo.getMouse(), FONT_TABLA_CELDA)));
        tablaInfoEquipo.addCell(new PdfPCell(new Phrase(equipo.getEstado(), FONT_TABLA_CELDA)));
        tablaInfoEquipo.addCell(new PdfPCell(new Phrase(String.valueOf(equipo.getIdLaboratorio()), FONT_TABLA_CELDA)));
        
        document.add(tablaInfoEquipo);
        document.add(new Paragraph(" ")); // Espacio
    }
    
    /**
     * Agrega la sección de historial del equipo al documento
     * @param document Documento PDF
     * @param idEquipo ID del equipo
     */
    private void agregarHistorialEquipo(Document document, String idEquipo) throws Exception {
        // Sección de historial
        PdfPTable tablaHistorialTitulo = new PdfPTable(1);
        tablaHistorialTitulo.setWidthPercentage(100);
        
        PdfPCell cellHistorialTitulo = new PdfPCell(new Phrase("HISTORIAL DEL EQUIPO", FONT_NEGRITA));
        cellHistorialTitulo.setBorder(Rectangle.BOX);
        tablaHistorialTitulo.addCell(cellHistorialTitulo);
        document.add(tablaHistorialTitulo);
        
        // Agregar tabla de historial
        agregarTablaHistorial(document, idEquipo);
    }
    
    /**
     * Agrega una tabla con el historial del equipo al documento
     * @param document Documento PDF
     * @param idEquipo ID del equipo
     */
    private void agregarTablaHistorial(Document document, String idEquipo) throws Exception {
        // Obtener historial
        List<Object[]> historial = controlHistorial.buscarHistorialPorEquipo(idEquipo);
        
        if (historial.isEmpty()) {
            Paragraph paragraphNoHistorial = new Paragraph("No hay registros de historial para este equipo.", FONT_NORMAL);
            document.add(paragraphNoHistorial);
        } else {
            // Crear tabla para historial
            PdfPTable tablaHistorial = new PdfPTable(4);
            tablaHistorial.setWidthPercentage(100);
            tablaHistorial.setWidths(new float[]{20, 25, 25, 30});
            
            // Encabezados
            String[] encabezados = {"RU", "Fecha", "Categoría", "Descripción"};
            for (String encabezado : encabezados) {
                PdfPCell cell = new PdfPCell(new Phrase(encabezado, FONT_TABLA_ENCABEZADO));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                tablaHistorial.addCell(cell);
            }
            
            // Datos del historial
            for (Object[] registro : historial) {
                // RU
                PdfPCell cellRU = new PdfPCell(new Phrase(String.valueOf(registro[1]), FONT_TABLA_CELDA));
                tablaHistorial.addCell(cellRU);
                
                // Formatear fecha
                Date fecha = (Date)registro[2];
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String fechaFormateada = fecha != null ? sdf.format(fecha) : "";
                PdfPCell cellFechaReg = new PdfPCell(new Phrase(fechaFormateada, FONT_TABLA_CELDA));
                tablaHistorial.addCell(cellFechaReg);
                
                // Categoría
                PdfPCell cellCategoria = new PdfPCell(new Phrase(String.valueOf(registro[3]), FONT_TABLA_CELDA));
                tablaHistorial.addCell(cellCategoria);
                
                // Descripción
                PdfPCell cellDescripcion = new PdfPCell(new Phrase(String.valueOf(registro[4]), FONT_TABLA_CELDA));
                tablaHistorial.addCell(cellDescripcion);
            }
            
            document.add(tablaHistorial);
        }
    }

    /**
     * Método auxiliar para agregar detalles al PDF
     * @param tabla Tabla donde se agregará el detalle
     * @param etiqueta Etiqueta del detalle
     * @param valor Valor del detalle
     */
    private void agregarDetallePDF(PdfPTable tabla, String etiqueta, String valor) {
        PdfPCell cellEtiqueta = new PdfPCell(new Phrase(etiqueta, FONT_NEGRITA));
        cellEtiqueta.setHorizontalAlignment(Element.ALIGN_LEFT);
        tabla.addCell(cellEtiqueta);
        
        PdfPCell cellValor = new PdfPCell(new Phrase(valor != null ? valor : "", FONT_NORMAL));
        cellValor.setHorizontalAlignment(Element.ALIGN_LEFT);
        tabla.addCell(cellValor);
    }
    
    /**
     * Abre un archivo PDF
     * @param rutaArchivo Ruta del archivo PDF
     */
    private void abrirArchivoPDF(String rutaArchivo) {
        try {
            File archivo = new File(rutaArchivo);
            if (archivo.exists()) {
                Desktop.getDesktop().open(archivo);
                JOptionPane.showMessageDialog(this, "Reporte PDF generado exitosamente: " + rutaArchivo,
                        "Reporte Generado", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo encontrar el archivo generado: " + rutaArchivo,
                        "Archivo no encontrado", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            manejarError("Error al abrir el archivo PDF", ex);
        }
    }
    
    
    // 4. Añadir el nuevo método para generar el reporte estadístico
private void generarReporteEstadistico() {
    try {
        // Ruta del archivo PDF
        String nombreArchivo = "Reporte_Estadistico_Equipos.pdf";
        String rutaCompleta = DIRECTORIO_REPORTES + nombreArchivo;
        
        try (FileOutputStream outputStream = new FileOutputStream(rutaCompleta)) {
            // Crear documento PDF
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);
            document.open();
            
            // Agregar encabezado
            agregarEncabezadoPDF(document, "REPORTE ESTADÍSTICO DE EQUIPOS");
            document.add(new Paragraph(" ")); // Espacio
            
            // Agregar subtítulo para los gráficos de estado
            Paragraph subtituloEstado = new Paragraph("ESTADO DE LOS EQUIPOS:", FONT_SUBTITULO);
            subtituloEstado.setAlignment(Element.ALIGN_CENTER);
            document.add(subtituloEstado);
            document.add(new Paragraph(" ")); // Espacio
            
            // Crear y agregar gráficos de torta
            agregarGraficoEstadoEquipos(document);
            document.add(new Paragraph(" ")); // Espacio
            
            agregarGraficoEquiposPorLaboratorio(document);
            
            // Nueva página para el historial
            document.newPage();
            
            // Agregar subtítulo para el historial
            Paragraph subtituloHistorial = new Paragraph("HISTORIAL DE LOS EQUIPOS:", FONT_SUBTITULO);
            subtituloHistorial.setAlignment(Element.ALIGN_CENTER);
            document.add(subtituloHistorial);
            document.add(new Paragraph(" ")); // Espacio
            
            // Agregar gráfico de barras para categorías de historial
            agregarGraficoHistorialCategorias(document);
            
            document.close();
            
            // Abrir el documento PDF
            abrirArchivoPDF(rutaCompleta);
        }
    } catch (Exception ex) {
        manejarError("Error al generar el reporte estadístico", ex);
    }
}

// 5. Método para crear y agregar el gráfico de estado de equipos
private void agregarGraficoEstadoEquipos(Document document) throws Exception {
    // Obtener datos de los equipos por estado
    Map<String, Integer> estadosEquipos = contarEquiposPorEstado();
    
    if (estadosEquipos.isEmpty()) {
        document.add(new Paragraph("No hay datos de equipos disponibles para generar el gráfico.", FONT_NORMAL));
        return;
    }
    
    // Crear dataset para el gráfico de torta
    DefaultPieDataset dataset = new DefaultPieDataset();
    for (Map.Entry<String, Integer> entry : estadosEquipos.entrySet()) {
        dataset.setValue(entry.getKey(), entry.getValue());
    }
    
    // Crear gráfico
    JFreeChart chart = ChartFactory.createPieChart(
            "Distribución de Equipos por Estado",  // título
            dataset,                               // datos
            true,                                  // incluir leyenda
            true,                                  // usar tooltips
            false                                  // generar URLs
    );
    
    // Personalizar gráfico
    PiePlot plot = (PiePlot) chart.getPlot();
    plot.setLabelFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12));
    plot.setNoDataMessage("No hay datos disponibles");
    plot.setCircular(true);
    plot.setLabelGap(0.02);
    
    // Convertir gráfico a imagen y agregarlo al PDF
    int width = 500;
    int height = 300;
    
    ByteArrayOutputStream imgBytes = new ByteArrayOutputStream();
    BufferedImage bufferedImage = chart.createBufferedImage(width, height);
    ImageIO.write(bufferedImage, "png", imgBytes);
    
    com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance(imgBytes.toByteArray());
    image.setAlignment(Element.ALIGN_CENTER);
    
    document.add(image);
    document.add(new Paragraph(" ")); // Espacio
    
    // Agregar información de los porcentajes
    PdfPTable tablaInfo = new PdfPTable(2);
    tablaInfo.setWidthPercentage(70);
    tablaInfo.setHorizontalAlignment(Element.ALIGN_CENTER);
    
    // Encabezados
    PdfPCell cellEstado = new PdfPCell(new Phrase("Estado", FONT_TABLA_ENCABEZADO));
    cellEstado.setBackgroundColor(BaseColor.LIGHT_GRAY);
    tablaInfo.addCell(cellEstado);
    
    PdfPCell cellPorcentaje = new PdfPCell(new Phrase("Porcentaje", FONT_TABLA_ENCABEZADO));
    cellPorcentaje.setBackgroundColor(BaseColor.LIGHT_GRAY);
    tablaInfo.addCell(cellPorcentaje);
    
    // Calcular total
    int total = 0;
    for (Integer cantidad : estadosEquipos.values()) {
        total += cantidad;
    }
    
    // Agregar filas con porcentajes
    for (Map.Entry<String, Integer> entry : estadosEquipos.entrySet()) {
        tablaInfo.addCell(new PdfPCell(new Phrase(entry.getKey(), FONT_TABLA_CELDA)));
        
        double porcentaje = (double) entry.getValue() / total * 100;
        tablaInfo.addCell(new PdfPCell(new Phrase(String.format("%.2f%%", porcentaje), FONT_TABLA_CELDA)));
    }
    
    document.add(tablaInfo);
}

// 6. Método para crear y agregar el gráfico de equipos por laboratorio
private void agregarGraficoEquiposPorLaboratorio(Document document) throws Exception {
    // Obtener datos de los equipos por laboratorio
    Map<Integer, Integer> equiposPorLab = contarEquiposPorLaboratorio();
    
    if (equiposPorLab.isEmpty()) {
        document.add(new Paragraph("No hay datos de equipos por laboratorio disponibles para generar el gráfico.", FONT_NORMAL));
        return;
    }
    
    // Crear dataset para el gráfico de torta
    DefaultPieDataset dataset = new DefaultPieDataset();
    for (Map.Entry<Integer, Integer> entry : equiposPorLab.entrySet()) {
        dataset.setValue("Laboratorio " + entry.getKey(), entry.getValue());
    }
    
    // Crear gráfico
    JFreeChart chart = ChartFactory.createPieChart(
            "Distribución de Equipos por Laboratorio",  // título
            dataset,                                    // datos
            true,                                       // incluir leyenda
            true,                                       // usar tooltips
            false                                       // generar URLs
    );
    
    // Personalizar gráfico
    PiePlot plot = (PiePlot) chart.getPlot();
    plot.setLabelFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12));
    plot.setNoDataMessage("No hay datos disponibles");
    plot.setCircular(true);
    plot.setLabelGap(0.02);
    
    // Convertir gráfico a imagen y agregarlo al PDF
    int width = 500;
    int height = 300;
    
    ByteArrayOutputStream imgBytes = new ByteArrayOutputStream();
    BufferedImage bufferedImage = chart.createBufferedImage(width, height);
    ImageIO.write(bufferedImage, "png", imgBytes);
    
    com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance(imgBytes.toByteArray());
    image.setAlignment(Element.ALIGN_CENTER);
    
    document.add(image);
    document.add(new Paragraph(" ")); // Espacio
    
    // Agregar información de los porcentajes
    PdfPTable tablaInfo = new PdfPTable(2);
    tablaInfo.setWidthPercentage(70);
    tablaInfo.setHorizontalAlignment(Element.ALIGN_CENTER);
    
    // Encabezados
    PdfPCell cellLab = new PdfPCell(new Phrase("Laboratorio", FONT_TABLA_ENCABEZADO));
    cellLab.setBackgroundColor(BaseColor.LIGHT_GRAY);
    tablaInfo.addCell(cellLab);
    
    PdfPCell cellPorcentaje = new PdfPCell(new Phrase("Porcentaje", FONT_TABLA_ENCABEZADO));
    cellPorcentaje.setBackgroundColor(BaseColor.LIGHT_GRAY);
    tablaInfo.addCell(cellPorcentaje);
    
    // Calcular total
    int total = 0;
    for (Integer cantidad : equiposPorLab.values()) {
        total += cantidad;
    }
    
    // Agregar filas con porcentajes
    for (Map.Entry<Integer, Integer> entry : equiposPorLab.entrySet()) {
        tablaInfo.addCell(new PdfPCell(new Phrase("Laboratorio " + entry.getKey(), FONT_TABLA_CELDA)));
        
        double porcentaje = (double) entry.getValue() / total * 100;
        tablaInfo.addCell(new PdfPCell(new Phrase(String.format("%.2f%%", porcentaje), FONT_TABLA_CELDA)));
    }
    
    document.add(tablaInfo);
}

// 7. Método para crear y agregar el gráfico de historial por categorías
private void agregarGraficoHistorialCategorias(Document document) throws Exception {
    // Obtener datos del historial por categoría
    Map<String, Integer> historialPorCategoria = contarHistorialPorCategoria();
    
    if (historialPorCategoria.isEmpty()) {
        document.add(new Paragraph("No hay datos de historial disponibles para generar el gráfico.", FONT_NORMAL));
        return;
    }
    
    // Crear dataset para el gráfico de barras
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    for (Map.Entry<String, Integer> entry : historialPorCategoria.entrySet()) {
        dataset.setValue(entry.getValue(), "Cantidad", entry.getKey());
    }
    
    // Crear gráfico
    JFreeChart chart = ChartFactory.createBarChart(
            "Eventos por Categoría de Historial",  // título
            "Categoría",                          // etiqueta eje x
            "Cantidad",                           // etiqueta eje y
            dataset,                              // datos
            PlotOrientation.VERTICAL,             // orientación
            true,                                 // incluir leyenda
            true,                                 // usar tooltips
            false                                 // generar URLs
    );
    
    // Convertir gráfico a imagen y agregarlo al PDF
    int width = 550;
    int height = 400;
    
    ByteArrayOutputStream imgBytes = new ByteArrayOutputStream();
    BufferedImage bufferedImage = chart.createBufferedImage(width, height);
    ImageIO.write(bufferedImage, "png", imgBytes);
    
    com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance(imgBytes.toByteArray());
    image.setAlignment(Element.ALIGN_CENTER);
    
    document.add(image);
    document.add(new Paragraph(" ")); // Espacio
    
    // Agregar tabla con los datos
    PdfPTable tablaInfo = new PdfPTable(2);
    tablaInfo.setWidthPercentage(70);
    tablaInfo.setHorizontalAlignment(Element.ALIGN_CENTER);
    
    // Encabezados
    PdfPCell cellCategoria = new PdfPCell(new Phrase("Categoría", FONT_TABLA_ENCABEZADO));
    cellCategoria.setBackgroundColor(BaseColor.LIGHT_GRAY);
    tablaInfo.addCell(cellCategoria);
    
    PdfPCell cellCantidad = new PdfPCell(new Phrase("Cantidad", FONT_TABLA_ENCABEZADO));
    cellCantidad.setBackgroundColor(BaseColor.LIGHT_GRAY);
    tablaInfo.addCell(cellCantidad);
    
    // Agregar filas
    for (Map.Entry<String, Integer> entry : historialPorCategoria.entrySet()) {
        tablaInfo.addCell(new PdfPCell(new Phrase(entry.getKey(), FONT_TABLA_CELDA)));
        tablaInfo.addCell(new PdfPCell(new Phrase(String.valueOf(entry.getValue()), FONT_TABLA_CELDA)));
    }
    
    document.add(tablaInfo);
}

// 8. Método auxiliar para contar equipos por estado
private Map<String, Integer> contarEquiposPorEstado() throws SQLException {
    Map<String, Integer> resultado = new HashMap<>();
    
    List<Equipos> listaEquipos = controlEquipo.listar();
    for (Equipos equipo : listaEquipos) {
        String estado = equipo.getEstado();
        if (estado != null && !estado.isEmpty()) {
            resultado.put(estado, resultado.getOrDefault(estado, 0) + 1);
        }
    }
    
    return resultado;
}

// 9. Método auxiliar para contar equipos por laboratorio
private Map<Integer, Integer> contarEquiposPorLaboratorio() throws SQLException {
    Map<Integer, Integer> resultado = new HashMap<>();
    
    List<Equipos> listaEquipos = controlEquipo.listar();
    for (Equipos equipo : listaEquipos) {
        int idLab = equipo.getIdLaboratorio();
        resultado.put(idLab, resultado.getOrDefault(idLab, 0) + 1);
    }
    
    return resultado;
}

// 10. Método auxiliar para contar historial por categoría
private Map<String, Integer> contarHistorialPorCategoria() throws SQLException {
    Map<String, Integer> resultado = new HashMap<>();
    
    // Obtener todos los registros de historial
    List<HistorialGeneral> listaHistorial = controlHistorial.controlHistorialGeneral.listar();
    for (HistorialGeneral historial : listaHistorial) {
        String categoria = historial.getCategoria();
        if (categoria != null && !categoria.isEmpty()) {
            resultado.put(categoria, resultado.getOrDefault(categoria, 0) + 1);
        }
    }
    
    return resultado;
}
    
    /**
     * Maneja los errores mostrando un mensaje y registrando la excepción
     * @param mensaje Mensaje de error
     * @param ex Excepción producida
     */

 private void manejarError(String mensaje, Exception ex) {
        // Mostrar mensaje de error al usuario
        JOptionPane.showMessageDialog(this, 
                mensaje + ": " + ex.getMessage(),
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        
        // Registrar el error en la consola para depuración
        System.err.println(mensaje);
        ex.printStackTrace();
        
        
    }
}