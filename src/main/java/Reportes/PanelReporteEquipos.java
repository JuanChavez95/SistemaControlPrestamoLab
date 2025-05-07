/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package Reportes;

import Clases.Equipos;
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
        // Inicializar controladores
        controlEquipo = new ControladorEquipo();
        controlHistorial = new ControladorHistorialEquipos();
        
        // Crear directorio de reportes si no existe
        crearDirectorioReportes();
        
        // Configurar panel
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Panel de título
        JPanel panelTitulo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel lblTitulo = new JLabel("Generación de Reportes de Equipos (PDF)");
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
        btnGenerarPDF = new JButton("Generar Reporte PDF");
        btnGenerarPDF.setFont(new Font("Arial", Font.BOLD, 12));
        btnGenerarPDF.setPreferredSize(new Dimension(200, 30));
        btnGenerarPDF.setBackground(new Color(70, 130, 180));
        btnGenerarPDF.setForeground(Color.WHITE);
        btnGenerarPDF.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGenerarPDF.setFocusPainted(false);
        panelOpciones.add(btnGenerarPDF, gbc);
        
        // Oyente para tipo de reporte
        cboTipoReporte.addActionListener(e -> {
            boolean esReportePorId = cboTipoReporte.getSelectedIndex() == 0;
            txtIdEquipo.setEnabled(esReportePorId);
            lblIdEquipo.setEnabled(esReportePorId);
        });
        
        // Oyente para generar reporte
        btnGenerarPDF.addActionListener(e -> generarReporte());
        
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
                "3. Verifique la fecha del reporte (formato: dd/MM/yyyy).\n" +
                "4. Haga clic en 'Generar Reporte PDF' para crear el documento.");
        txtInstrucciones.setEditable(false);
        txtInstrucciones.setBackground(new Color(240, 240, 240));
        txtInstrucciones.setFont(new Font("Arial", Font.PLAIN, 12));
        txtInstrucciones.setBorder(new EmptyBorder(10, 10, 10, 10));
        panelInstrucciones.add(txtInstrucciones);
        
        add(panelInstrucciones, BorderLayout.SOUTH);
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
        
        // Opcionalmente, se podría implementar un registro en archivo de log
        // Ejemplo:
        // try {
        //     FileWriter fw = new FileWriter("error_log.txt", true);
        //     fw.write(new Date() + " - " + mensaje + ": " + ex.getMessage() + "\n");
        //     fw.close();
        // } catch (IOException e) {
        //     System.err.println("Error al escribir en el archivo de log: " + e.getMessage());
        // }
    }
}