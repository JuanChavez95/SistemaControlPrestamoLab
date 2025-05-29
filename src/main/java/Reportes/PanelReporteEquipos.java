/*
 * Click https://www.netbeans.org/dtds/license-default.txt to change this license
 * Click https://www.netbeans.org/dtds/class-template.java to edit this template
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

// Importaciones para JFreeChart
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
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

    // Definición de fuentes para PDF con iText
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

        // Etiquetas y campos
        JLabel lblTipoReporte = new JLabel("Tipo de Reporte:");
        lblTipoReporte.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panelOpciones.add(lblTipoReporte, gbc);

        gbc.gridx = 1;
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
                // Reporte estadístico
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
            String nombreArchivo = "Reporte_Equipo_" + id + "_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".pdf";
            String rutaCompleta = DIRECTORIO_REPORTES + nombreArchivo;

            try (FileOutputStream outputStream = new FileOutputStream(rutaCompleta)) {
                // Crear documento PDF
                Document document = new Document();
                PdfWriter.getInstance(document, outputStream);
                document.open();

                // Agregar encabezado completo
                agregarEncabezadoCompleto(document, "REPORTE DE EQUIPO " + id);

                // Agregar línea divisoria
                document.add(new Paragraph(" "));
                Paragraph linea = new Paragraph("_".repeat(100));
                linea.setAlignment(Element.ALIGN_CENTER);
                linea.getFont().setColor(BaseColor.GRAY);
                document.add(linea);
                document.add(new Paragraph(" "));

                // Agregar información del equipo
                agregarInformacionEquipo(document, equipo);

                // Agregar historial del equipo
                agregarHistorialEquipo(document, id);

                // Agregar pie de página
                agregarPiePagina(document, 1);

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
            String nombreArchivo = "Reporte_Todos_Equipos_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".pdf";
            String rutaCompleta = DIRECTORIO_REPORTES + nombreArchivo;

            try (FileOutputStream outputStream = new FileOutputStream(rutaCompleta)) {
                // Crear documento PDF
                Document document = new Document();
                PdfWriter.getInstance(document, outputStream);
                document.open();

                // Agregar encabezado completo
                agregarEncabezadoCompleto(document, "REPORTE DE TODOS LOS EQUIPOS");

                // Agregar línea divisoria
                document.add(new Paragraph(" "));
                Paragraph linea = new Paragraph("_".repeat(100));
                linea.setAlignment(Element.ALIGN_CENTER);
                linea.getFont().setColor(BaseColor.GRAY);
                document.add(linea);
                document.add(new Paragraph(" "));

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

                    // Título del equipo
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

                // Agregar pie de página
                agregarPiePagina(document, listaEquipos.size());

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
     * Agrega el encabezado completo a un documento PDF
     * @param document Documento PDF
     * @param subtitulo Subtítulo específico para el reporte
     */
    private void agregarEncabezadoCompleto(Document document, String subtitulo) throws Exception {
        // Crear tabla para el encabezado
        PdfPTable tablaEncabezado = new PdfPTable(3);
        tablaEncabezado.setWidthPercentage(100);
        float[] anchosEncabezado = {1f, 3f, 1f};
        tablaEncabezado.setWidths(anchosEncabezado);

        // Logo izquierdo (simulado, ajusta la ruta según tu sistema)
        try {
            com.itextpdf.text.Image logo = com.itextpdf.text.Image.getInstance("C:\\Users\\Windows\\Documents\\NetBeansProjects\\SistemaControlPrestamoLab-main\\logo.png");
            logo.scaleToFit(60, 60);
            PdfPCell celdaLogo = new PdfPCell(logo);
            celdaLogo.setBorder(Rectangle.NO_BORDER);
            celdaLogo.setHorizontalAlignment(Element.ALIGN_CENTER);
            celdaLogo.setVerticalAlignment(Element.ALIGN_MIDDLE);
            tablaEncabezado.addCell(celdaLogo);
        } catch (Exception e) {
            PdfPCell celdaVacia = new PdfPCell();
            celdaVacia.setBorder(Rectangle.NO_BORDER);
            tablaEncabezado.addCell(celdaVacia);
        }

        // Información central
        com.itextpdf.text.Font fontGestion = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 10, com.itextpdf.text.Font.BOLD, BaseColor.DARK_GRAY);
        Paragraph gestion = new Paragraph("GESTIÓN: 1 - 2025", fontGestion);
        gestion.setAlignment(Element.ALIGN_CENTER);

        com.itextpdf.text.Font fontUniversidad = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 14, com.itextpdf.text.Font.BOLD, new BaseColor(52, 73, 94));
        Paragraph universidad = new Paragraph("UNIVERSIDAD SALESIANA DE BOLIVIA", fontUniversidad);
        universidad.setAlignment(Element.ALIGN_CENTER);
        universidad.setSpacingAfter(3);

        com.itextpdf.text.Font fontTitulo = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 12, com.itextpdf.text.Font.BOLD, BaseColor.BLACK);
        Paragraph titulo = new Paragraph(subtitulo, fontTitulo);
        titulo.setAlignment(Element.ALIGN_CENTER);
        titulo.setSpacingAfter(8);

        com.itextpdf.text.Font fontInfo = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 9, com.itextpdf.text.Font.NORMAL, BaseColor.GRAY);
        Paragraph direccion = new Paragraph("Av. Chacaltaya Nro. 1258, Zona Achachicala\nEl Alto, La Paz, Bolivia", fontInfo);
        direccion.setAlignment(Element.ALIGN_CENTER);

        Paragraph contacto = new Paragraph("Teléfono: +591 2305210\nCorreo: informaciones@usalesiana.edu.bo", fontInfo);
        contacto.setAlignment(Element.ALIGN_CENTER);

        PdfPCell celdaCentral = new PdfPCell();
        celdaCentral.setBorder(Rectangle.NO_BORDER);
        celdaCentral.addElement(gestion);
        celdaCentral.addElement(universidad);
        celdaCentral.addElement(titulo);
        celdaCentral.addElement(direccion);
        celdaCentral.addElement(contacto);
        tablaEncabezado.addCell(celdaCentral);

        // Celda derecha vacía para balance
        PdfPCell celdaDerecha = new PdfPCell();
        celdaDerecha.setBorder(Rectangle.NO_BORDER);
        tablaEncabezado.addCell(celdaDerecha);

        document.add(tablaEncabezado);

        // Fecha de generación
        com.itextpdf.text.Font fontFechaGen = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 10, com.itextpdf.text.Font.NORMAL, BaseColor.GRAY);
        Paragraph fechaGen = new Paragraph(
            "Fecha de Generación: " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()),
            fontFechaGen
        );
        fechaGen.setAlignment(Element.ALIGN_CENTER);
        fechaGen.setSpacingAfter(15);
        document.add(fechaGen);
    }

    /**
     * Agrega la información detallada de un equipo al documento
     * @param document Documento PDF
     * @param equipo Objeto con la información del equipo
     */
    private void agregarInformacionEquipo(Document document, Equipos equipo) throws Exception {
        // Título de información
        Paragraph tituloInfo = new Paragraph("INFORMACIÓN DEL EQUIPO", FONT_SUBTITULO);
        tituloInfo.setAlignment(Element.ALIGN_CENTER);
        document.add(tituloInfo);
        document.add(new Paragraph(" ")); // Espacio

        // Tabla para detalles del equipo
        PdfPTable tablaDetalles = new PdfPTable(2);
        tablaDetalles.setWidthPercentage(100);
        tablaDetalles.setWidths(new float[]{30, 70});

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
        float[] anchosInfo = {14, 12, 14, 14, 14, 12, 10, 10};
        tablaInfoEquipo.setWidths(anchosInfo);

        // Encabezados de la tabla
        String[] encabezados = {"Procesador", "RAM", "Dispositivo", "Monitor", "Teclado", "Mouse", "Estado", "Laboratorio"};
        for (String encabezado : encabezados) {
            PdfPCell cell = new PdfPCell(new Phrase(encabezado, FONT_TABLA_ENCABEZADO));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(new BaseColor(52, 73, 94));
            cell.setPadding(8);
            cell.setBorderColor(BaseColor.WHITE);
            cell.setBorderWidth(1);
            tablaInfoEquipo.addCell(cell);
        }

        // Datos del equipo
        PdfPCell cellProcesador = new PdfPCell(new Phrase(equipo.getProcesador() != null ? equipo.getProcesador() : "", FONT_TABLA_CELDA));
        configurarCelda(cellProcesador, BaseColor.WHITE, Element.ALIGN_CENTER);
        tablaInfoEquipo.addCell(cellProcesador);

        PdfPCell cellRam = new PdfPCell(new Phrase(equipo.getRam() != null ? equipo.getRam() : "", FONT_TABLA_CELDA));
        configurarCelda(cellRam, BaseColor.WHITE, Element.ALIGN_CENTER);
        tablaInfoEquipo.addCell(cellRam);

        PdfPCell cellDispositivo = new PdfPCell(new Phrase(equipo.getDispositivo() != null ? equipo.getDispositivo() : "", FONT_TABLA_CELDA));
        configurarCelda(cellDispositivo, BaseColor.WHITE, Element.ALIGN_CENTER);
        tablaInfoEquipo.addCell(cellDispositivo);

        PdfPCell cellMonitor = new PdfPCell(new Phrase(equipo.getMonitor() != null ? equipo.getMonitor() : "", FONT_TABLA_CELDA));
        configurarCelda(cellMonitor, BaseColor.WHITE, Element.ALIGN_CENTER);
        tablaInfoEquipo.addCell(cellMonitor);

        PdfPCell cellTeclado = new PdfPCell(new Phrase(equipo.getTeclado() != null ? equipo.getTeclado() : "", FONT_TABLA_CELDA));
        configurarCelda(cellTeclado, BaseColor.WHITE, Element.ALIGN_CENTER);
        tablaInfoEquipo.addCell(cellTeclado);

        PdfPCell cellMouse = new PdfPCell(new Phrase(equipo.getMouse() != null ? equipo.getMouse() : "", FONT_TABLA_CELDA));
        configurarCelda(cellMouse, BaseColor.WHITE, Element.ALIGN_CENTER);
        tablaInfoEquipo.addCell(cellMouse);

        PdfPCell cellEstado = new PdfPCell(new Phrase(equipo.getEstado() != null ? equipo.getEstado() : "", FONT_TABLA_CELDA));
        configurarCelda(cellEstado, BaseColor.WHITE, Element.ALIGN_CENTER);
        tablaInfoEquipo.addCell(cellEstado);

        PdfPCell cellLaboratorio = new PdfPCell(new Phrase(String.valueOf(equipo.getIdLaboratorio()), FONT_TABLA_CELDA));
        configurarCelda(cellLaboratorio, BaseColor.WHITE, Element.ALIGN_CENTER);
        tablaInfoEquipo.addCell(cellLaboratorio);

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
        Paragraph tituloHistorial = new Paragraph("HISTORIAL DEL EQUIPO", FONT_SUBTITULO);
        tituloHistorial.setAlignment(Element.ALIGN_CENTER);
        document.add(tituloHistorial);
        document.add(new Paragraph(" ")); // Espacio

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
            paragraphNoHistorial.setAlignment(Element.ALIGN_CENTER);
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
                cell.setBackgroundColor(new BaseColor(52, 73, 94));
                cell.setPadding(8);
                cell.setBorderColor(BaseColor.WHITE);
                cell.setBorderWidth(1);
                tablaHistorial.addCell(cell);
            }

            // Datos del historial
            for (int i = 0; i < historial.size(); i++) {
                Object[] registro = historial.get(i);
                BaseColor colorFondo = (i % 2 == 0) ? BaseColor.WHITE : new BaseColor(248, 249, 250);

                PdfPCell cellRU = new PdfPCell(new Phrase(String.valueOf(registro[1]), FONT_TABLA_CELDA));
                configurarCelda(cellRU, colorFondo, Element.ALIGN_CENTER);
                tablaHistorial.addCell(cellRU);

                Date fecha = (Date) registro[2];
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String fechaFormateada = fecha != null ? sdf.format(fecha) : "";
                PdfPCell cellFechaReg = new PdfPCell(new Phrase(fechaFormateada, FONT_TABLA_CELDA));
                configurarCelda(cellFechaReg, colorFondo, Element.ALIGN_CENTER);
                tablaHistorial.addCell(cellFechaReg);

                PdfPCell cellCategoria = new PdfPCell(new Phrase(String.valueOf(registro[3]), FONT_TABLA_CELDA));
                configurarCelda(cellCategoria, colorFondo, Element.ALIGN_CENTER);
                tablaHistorial.addCell(cellCategoria);

                PdfPCell cellDescripcion = new PdfPCell(new Phrase(String.valueOf(registro[4]), FONT_TABLA_CELDA));
                configurarCelda(cellDescripcion, colorFondo, Element.ALIGN_LEFT);
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
        cellEtiqueta.setPadding(6);
        tabla.addCell(cellEtiqueta);

        PdfPCell cellValor = new PdfPCell(new Phrase(valor != null ? valor : "", FONT_NORMAL));
        cellValor.setHorizontalAlignment(Element.ALIGN_LEFT);
        cellValor.setPadding(6);
        tabla.addCell(cellValor);
    }

    /**
     * Genera un reporte estadístico de equipos
     */
    private void generarReporteEstadistico() {
        try {
            // Ruta del archivo PDF
            String nombreArchivo = "Reporte_Estadistico_Equipos_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".pdf";
            String rutaCompleta = DIRECTORIO_REPORTES + nombreArchivo;

            try (FileOutputStream outputStream = new FileOutputStream(rutaCompleta)) {
                // Crear documento PDF
                Document document = new Document();
                PdfWriter.getInstance(document, outputStream);
                document.open();

                // Agregar encabezado completo
                agregarEncabezadoCompleto(document, "REPORTE ESTADÍSTICO DE EQUIPOS");

                // Agregar línea divisoria
                document.add(new Paragraph(" "));
                Paragraph linea = new Paragraph("_".repeat(100));
                linea.setAlignment(Element.ALIGN_CENTER);
                linea.getFont().setColor(BaseColor.GRAY);
                document.add(linea);
                document.add(new Paragraph(" "));

                // Agregar subtítulo para los gráficos de estado
                Paragraph subtituloEstado = new Paragraph("ESTADO DE LOS EQUIPOS", FONT_SUBTITULO);
                subtituloEstado.setAlignment(Element.ALIGN_CENTER);
                document.add(subtituloEstado);
                document.add(new Paragraph(" ")); // Espacio

                // Crear y agregar gráficos de torta
                agregarGraficoEstadoEquipos(document);
                document.add(new Paragraph(" ")); // Espacio

                // Agregar gráfico de equipos por laboratorio
                Paragraph subtituloLaboratorio = new Paragraph("DISTRIBUCIÓN DE EQUIPOS POR LABORATORIO", FONT_SUBTITULO);
                subtituloLaboratorio.setAlignment(Element.ALIGN_CENTER);
                document.add(subtituloLaboratorio);
                document.add(new Paragraph(" ")); // Espacio
                agregarGraficoEquiposPorLaboratorio(document);

                // Nueva página para el historial
                document.newPage();

                // Agregar subtítulo para el historial
                Paragraph subtituloHistorial = new Paragraph("HISTORIAL DE LOS EQUIPOS", FONT_SUBTITULO);
                subtituloHistorial.setAlignment(Element.ALIGN_CENTER);
                document.add(subtituloHistorial);
                document.add(new Paragraph(" ")); // Espacio

                // Agregar gráfico de barras para categorías de historial
                agregarGraficoHistorialCategorias(document);

                // Agregar pie de página
                agregarPiePagina(document, 1); // Ajustar según necesidad

                document.close();

                // Abrir el documento PDF
                abrirArchivoPDF(rutaCompleta);
            }
        } catch (Exception ex) {
            manejarError("Error al generar el reporte estadístico", ex);
        }
    }

    /**
     * Agrega un gráfico de torta para estados de equipos
     */
    private void agregarGraficoEstadoEquipos(Document document) throws Exception {
        // Obtener datos de los equipos por estado
        Map<String, Integer> estadosEquipos = contarEquiposPorEstado();

        if (estadosEquipos.isEmpty()) {
            Paragraph noData = new Paragraph("No hay datos de equipos disponibles para generar el gráfico.", FONT_NORMAL);
            noData.setAlignment(Element.ALIGN_CENTER);
            document.add(noData);
            return;
        }

        // Crear dataset para el gráfico de torta
        DefaultPieDataset dataset = new DefaultPieDataset();
        for (Map.Entry<String, Integer> entry : estadosEquipos.entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }

        // Crear gráfico
        JFreeChart chart = ChartFactory.createPieChart(
            "Distribución de Equipos por Estado",
            dataset,
            true,
            true,
            false
        );

        // Personalizar gráfico
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLabelFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12));
        plot.setNoDataMessage("No hay datos disponibles");
        plot.setCircular(true);
        plot.setLabelGap(0.02);

        // Convertir a imagen
        int width = 500;
        int height = 300;
        ByteArrayOutputStream imgBytes = new ByteArrayOutputStream();
        BufferedImage bufferedImage = chart.createBufferedImage(width, height);
        ImageIO.write(bufferedImage, "png", imgBytes);

        com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance(imgBytes.toByteArray());
        image.setAlignment(Element.ALIGN_CENTER);
        document.add(image);
        document.add(new Paragraph(" "));

        // Tabla de porcentajes
        PdfPTable tablaInfo = new PdfPTable(2);
        tablaInfo.setWidthPercentage(70);
        tablaInfo.setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell cellEstado = new PdfPCell(new Phrase("Estado", FONT_TABLA_ENCABEZADO));
        cellEstado.setBackgroundColor(new BaseColor(52, 73, 94));
        cellEstado.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellEstado.setPadding(8);
        cellEstado.setBorderColor(BaseColor.WHITE);
        cellEstado.setBorderWidth(1);
        tablaInfo.addCell(cellEstado);

        PdfPCell cellPorcentaje = new PdfPCell(new Phrase("Porcentaje", FONT_TABLA_ENCABEZADO));
        cellPorcentaje.setBackgroundColor(new BaseColor(52, 73, 94));
        cellPorcentaje.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellPorcentaje.setPadding(8);
        cellPorcentaje.setBorderColor(BaseColor.WHITE);
        cellPorcentaje.setBorderWidth(1);
        tablaInfo.addCell(cellPorcentaje);

        int total = 0;
        for (Integer cantidad : estadosEquipos.values()) {
            total += cantidad;
        }

        for (Map.Entry<String, Integer> entry : estadosEquipos.entrySet()) {
            PdfPCell celdaEstado = new PdfPCell(new Phrase(entry.getKey(), FONT_TABLA_CELDA));
            configurarCelda(celdaEstado, BaseColor.WHITE, Element.ALIGN_CENTER);
            tablaInfo.addCell(celdaEstado);

            double porcentaje = (double) entry.getValue() / total * 100;
            PdfPCell celdaPorcentaje = new PdfPCell(new Phrase(String.format("%.2f%%", porcentaje), FONT_TABLA_CELDA));
            configurarCelda(celdaPorcentaje, BaseColor.WHITE, Element.ALIGN_CENTER);
            tablaInfo.addCell(celdaPorcentaje);
        }

        document.add(tablaInfo);
    }

    /**
     * Agrega un gráfico de torta para equipos por laboratorio
     */
    private void agregarGraficoEquiposPorLaboratorio(Document document) throws Exception {
        // Obtener datos de los equipos por laboratorio
        Map<Integer, Integer> equiposPorLab = contarEquiposPorLaboratorio();

        if (equiposPorLab.isEmpty()) {
            Paragraph noData = new Paragraph("No hay datos de equipos por laboratorio disponibles para generar el gráfico.", FONT_NORMAL);
            noData.setAlignment(Element.ALIGN_CENTER);
            document.add(noData);
            return;
        }

        // Crear dataset para el gráfico de torta
        DefaultPieDataset dataset = new DefaultPieDataset();
        for (Map.Entry<Integer, Integer> entry : equiposPorLab.entrySet()) {
            dataset.setValue("Laboratorio " + entry.getKey(), entry.getValue());
        }

        // Crear gráfico
        JFreeChart chart = ChartFactory.createPieChart(
            "Distribución de Equipos por Laboratorio",
            dataset,
            true,
            true,
            false
        );

        // Personalizar gráfico
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLabelFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12));
        plot.setNoDataMessage("No hay datos disponibles");
        plot.setCircular(true);
        plot.setLabelGap(0.02);

        // Convertir a imagen
        int width = 500;
        int height = 300;
        ByteArrayOutputStream imgBytes = new ByteArrayOutputStream();
        BufferedImage bufferedImage = chart.createBufferedImage(width, height);
        ImageIO.write(bufferedImage, "png", imgBytes);

        com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance(imgBytes.toByteArray());
        image.setAlignment(Element.ALIGN_CENTER);
        document.add(image);
        document.add(new Paragraph(" "));

        // Tabla de porcentajes
        PdfPTable tablaInfo = new PdfPTable(2);
        tablaInfo.setWidthPercentage(70);
        tablaInfo.setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell cellLab = new PdfPCell(new Phrase("Laboratorio", FONT_TABLA_ENCABEZADO));
        cellLab.setBackgroundColor(new BaseColor(52, 73, 94));
        cellLab.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellLab.setPadding(8);
        cellLab.setBorderColor(BaseColor.WHITE);
        cellLab.setBorderWidth(1);
        tablaInfo.addCell(cellLab);

        PdfPCell cellPorcentaje = new PdfPCell(new Phrase("Porcentaje", FONT_TABLA_ENCABEZADO));
        cellPorcentaje.setBackgroundColor(new BaseColor(52, 73, 94));
        cellPorcentaje.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellPorcentaje.setPadding(8);
        cellPorcentaje.setBorderColor(BaseColor.WHITE);
        cellPorcentaje.setBorderWidth(1);
        tablaInfo.addCell(cellPorcentaje);

        int total = 0;
        for (Integer cantidad : equiposPorLab.values()) {
            total += cantidad;
        }

        for (Map.Entry<Integer, Integer> entry : equiposPorLab.entrySet()) {
            PdfPCell celdaLab = new PdfPCell(new Phrase("Laboratorio " + entry.getKey(), FONT_TABLA_CELDA));
            configurarCelda(celdaLab, BaseColor.WHITE, Element.ALIGN_CENTER);
            tablaInfo.addCell(celdaLab);

            double porcentaje = (double) entry.getValue() / total * 100;
            PdfPCell celdaPorcentaje = new PdfPCell(new Phrase(String.format("%.2f%%", porcentaje), FONT_TABLA_CELDA));
            configurarCelda(celdaPorcentaje, BaseColor.WHITE, Element.ALIGN_CENTER);
            tablaInfo.addCell(celdaPorcentaje);
        }

        document.add(tablaInfo);
    }

    /**
     * Agrega un gráfico de barras para categorías de historial
     */
    private void agregarGraficoHistorialCategorias(Document document) throws Exception {
        // Obtener datos del historial por categoría
        Map<String, Integer> historialPorCategoria = contarHistorialPorCategoria();

        if (historialPorCategoria.isEmpty()) {
            Paragraph noData = new Paragraph("No hay datos de historial disponibles para generar el gráfico.", FONT_NORMAL);
            noData.setAlignment(Element.ALIGN_CENTER);
            document.add(noData);
            return;
        }

        // Crear dataset para el gráfico de barras
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Map.Entry<String, Integer> entry : historialPorCategoria.entrySet()) {
            dataset.setValue(entry.getValue(), "Cantidad", entry.getKey());
        }

        // Crear gráfico
        JFreeChart chart = ChartFactory.createBarChart(
            "Eventos por Categoría de Historial",
            "Categoría",
            "Cantidad",
            dataset,
            PlotOrientation.VERTICAL,
            true,
            true,
            false
        );

        // Convertir a imagen
        int width = 550;
        int height = 400;
        ByteArrayOutputStream imgBytes = new ByteArrayOutputStream();
        BufferedImage bufferedImage = chart.createBufferedImage(width, height);
        ImageIO.write(bufferedImage, "png", imgBytes);

        com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance(imgBytes.toByteArray());
        image.setAlignment(Element.ALIGN_CENTER);
        document.add(image);
        document.add(new Paragraph(" "));

        // Tabla con los datos
        PdfPTable tablaInfo = new PdfPTable(2);
        tablaInfo.setWidthPercentage(70);
        tablaInfo.setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell cellCategoria = new PdfPCell(new Phrase("Categoría", FONT_TABLA_ENCABEZADO));
        cellCategoria.setBackgroundColor(new BaseColor(52, 73, 94));
        cellCategoria.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellCategoria.setPadding(8);
        cellCategoria.setBorderColor(BaseColor.WHITE);
        cellCategoria.setBorderWidth(1);
        tablaInfo.addCell(cellCategoria);

        PdfPCell cellCantidad = new PdfPCell(new Phrase("Cantidad", FONT_TABLA_ENCABEZADO));
        cellCantidad.setBackgroundColor(new BaseColor(52, 73, 94));
        cellCantidad.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellCantidad.setPadding(8);
        cellCantidad.setBorderColor(BaseColor.WHITE);
        cellCantidad.setBorderWidth(1);
        tablaInfo.addCell(cellCantidad);

        for (Map.Entry<String, Integer> entry : historialPorCategoria.entrySet()) {
            PdfPCell celdaCategoria = new PdfPCell(new Phrase(entry.getKey(), FONT_TABLA_CELDA));
            configurarCelda(celdaCategoria, BaseColor.WHITE, Element.ALIGN_CENTER);
            tablaInfo.addCell(celdaCategoria);

            PdfPCell celdaCantidad = new PdfPCell(new Phrase(String.valueOf(entry.getValue()), FONT_TABLA_CELDA));
            configurarCelda(celdaCantidad, BaseColor.WHITE, Element.ALIGN_CENTER);
            tablaInfo.addCell(celdaCantidad);
        }

        document.add(tablaInfo);
    }

    /**
     * Método auxiliar para contar equipos por estado
     */
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

    /**
     * Método auxiliar para contar equipos por laboratorio
     */
    private Map<Integer, Integer> contarEquiposPorLaboratorio() throws SQLException {
        Map<Integer, Integer> resultado = new HashMap<>();

        List<Equipos> listaEquipos = controlEquipo.listar();
        for (Equipos equipo : listaEquipos) {
            int idLab = equipo.getIdLaboratorio();
            resultado.put(idLab, resultado.getOrDefault(idLab, 0) + 1);
        }

        return resultado;
    }

    /**
     * Método auxiliar para contar historial por categoría
     */
    private Map<String, Integer> contarHistorialPorCategoria() throws SQLException {
        Map<String, Integer> resultado = new HashMap<>();

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
     * Configura una celda de tabla con estilo consistente
     */
    private void configurarCelda(PdfPCell celda, BaseColor colorFondo, int alineacion) {
        celda.setBackgroundColor(colorFondo);
        celda.setHorizontalAlignment(alineacion);
        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
        celda.setPadding(6);
        celda.setBorderColor(new BaseColor(220, 220, 220));
        celda.setBorderWidth(0.5f);
    }

    /**
     * Agrega pie de página con resumen y aviso legal
     */
    private void agregarPiePagina(Document document, int totalItems) throws Exception {
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));

        // Resumen
        com.itextpdf.text.Font fontResumen = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 11, com.itextpdf.text.Font.BOLD, BaseColor.DARK_GRAY);
        Paragraph resumen = new Paragraph("Total de registros: " + totalItems, fontResumen);
        resumen.setAlignment(Element.ALIGN_RIGHT);
        resumen.setSpacingAfter(15);
        document.add(resumen);

        // Aviso legal
        com.itextpdf.text.Font fontAviso = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 8, com.itextpdf.text.Font.NORMAL, BaseColor.GRAY);
        Paragraph aviso = new Paragraph(
            "Este documento debe ser utilizado de manera responsable. Cualquier uso indebido o alteración de su " +
            "contenido será motivo de sanciones conforme a las normativas institucionales y podrá ser reportado a las " +
            "autoridades competentes de la universidad.",
            fontAviso
        );
        aviso.setAlignment(Element.ALIGN_JUSTIFIED);
        aviso.setSpacingBefore(20);

        // Agregar marco al aviso
        PdfPTable tablaAviso = new PdfPTable(1);
        tablaAviso.setWidthPercentage(100);
        PdfPCell celdaAviso = new PdfPCell(aviso);
        celdaAviso.setPadding(10);
        celdaAviso.setBorderColor(BaseColor.LIGHT_GRAY);
        celdaAviso.setBorderWidth(1);
        celdaAviso.setBackgroundColor(new BaseColor(250, 250, 250));
        tablaAviso.addCell(celdaAviso);

        document.add(tablaAviso);
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
        JOptionPane.showMessageDialog(this,
            mensaje + ": " + ex.getMessage(),
            "Error",
            JOptionPane.ERROR_MESSAGE);
        System.err.println(mensaje);
        ex.printStackTrace();
    }
}