/*
 * Panel para generar reportes de préstamos en formato PDF.
 * Autor: Equipo Soldados Caídos (mejorado)
 */
package Reportes;

import Clases.Horario;
import Clases.Laboratorio;
import Clases.Prestamo;
import Controles.ControladorHorario;
import Controles.ControladorLaboratorio;
import Controles.ControladorPrestamo;
import DataBase.ConexionBD;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Desktop;

// Para gráficos estadísticos
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;

// Para equipamiento e insumos
import Clases.Equipamiento;
import Controles.ControladorEquipamiento;
import Controles.ControladorDetallePrestamoEquipamiento;

/**
 * Panel para generar reportes de préstamos en formato PDF.
 */
public class PanelReportePrestamo extends JPanel {
    private JComboBox<String> comboLaboratorio;
    private JComboBox<String> comboTipoReporte;
    private JTextField txtFechaInicial;
    private JTextField txtFechaFinal;
    private JButton btnGenerarPDF;
    private ControladorLaboratorio controladorLaboratorio;
    private ControladorPrestamo controladorPrestamo;
    private ControladorHorario controladorHorario;
    private static final String DIRECTORIO_REPORTES = "./reportes/";

    // Definición de fuentes para PDF
    private static final com.itextpdf.text.Font FONT_TITULO =
        new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 16, com.itextpdf.text.Font.BOLD);
    private static final com.itextpdf.text.Font FONT_SUBTITULO =
        new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 14, com.itextpdf.text.Font.BOLD);
    private static final com.itextpdf.text.Font FONT_FECHA =
        new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 12, com.itextpdf.text.Font.BOLD);
    private static final com.itextpdf.text.Font FONT_NORMAL =
        new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 12, com.itextpdf.text.Font.NORMAL);
    private static final com.itextpdf.text.Font FONT_TABLA_ENCABEZADO =
        new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 12, com.itextpdf.text.Font.BOLD);
    private static final com.itextpdf.text.Font FONT_TABLA_CELDA =
        new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 12, com.itextpdf.text.Font.NORMAL);
    private static final com.itextpdf.text.Font FONT_TABLA_ENCABEZADO_SUB =
        new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 10, com.itextpdf.text.Font.BOLD);
    private static final com.itextpdf.text.Font FONT_TABLA_CELDA_SUB =
        new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 10, com.itextpdf.text.Font.NORMAL);

    public PanelReportePrestamo() {
        controladorLaboratorio = new ControladorLaboratorio();
        controladorPrestamo = new ControladorPrestamo();
        controladorHorario = new ControladorHorario();
        crearDirectorioReportes();

        setLayout(new BorderLayout(20, 20));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(25, 25, 25, 25));

        // Título
        JLabel lblTitulo = new JLabel("Generación de Reportes de Préstamos (PDF)", SwingConstants.CENTER);
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
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel lblTipoReporte = new JLabel("Tipo de Reporte:");
        lblTipoReporte.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panelOpciones.add(lblTipoReporte, gbc);

        gbc.gridx = 1;
        String[] opciones = {
            "Reporte de Préstamos por Laboratorio",
            "Reporte General de Préstamos",
            "Reporte Estadístico de Préstamos"
        };
        comboTipoReporte = new JComboBox<>(opciones);
        comboTipoReporte.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        comboTipoReporte.setBackground(Color.WHITE);
        comboTipoReporte.setPreferredSize(new Dimension(240, 30));
        panelOpciones.add(comboTipoReporte, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel lblLaboratorio = new JLabel("Laboratorio:");
        lblLaboratorio.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panelOpciones.add(lblLaboratorio, gbc);

        gbc.gridx = 1;
        comboLaboratorio = new JComboBox<>();
        comboLaboratorio.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        comboLaboratorio.setBackground(Color.WHITE);
        comboLaboratorio.setPreferredSize(new Dimension(240, 30));
        cargarLaboratorios();
        panelOpciones.add(comboLaboratorio, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel lblFechaInicio = new JLabel("Fecha Inicial:");
        lblFechaInicio.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panelOpciones.add(lblFechaInicio, gbc);

        gbc.gridx = 1;
        txtFechaInicial = new JTextField();
        txtFechaInicial.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtFechaInicial.setPreferredSize(new Dimension(240, 30));
        txtFechaInicial.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        panelOpciones.add(txtFechaInicial, gbc);

        gbc.gridx = 2;
        JButton btnFechaInicioHoy = new JButton("Hoy");
        btnFechaInicioHoy.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnFechaInicioHoy.setBackground(new Color(100, 149, 237));
        btnFechaInicioHoy.setForeground(Color.WHITE);
        btnFechaInicioHoy.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnFechaInicioHoy.setFocusPainted(false);
        btnFechaInicioHoy.addActionListener(e -> txtFechaInicial.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date())));
        panelOpciones.add(btnFechaInicioHoy, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel lblFechaFinal = new JLabel("Fecha Final:");
        lblFechaFinal.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panelOpciones.add(lblFechaFinal, gbc);

        gbc.gridx = 1;
        txtFechaFinal = new JTextField();
        txtFechaFinal.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtFechaFinal.setPreferredSize(new Dimension(240, 30));
        txtFechaFinal.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        panelOpciones.add(txtFechaFinal, gbc);

        gbc.gridx = 2;
        JButton btnFechaFinalHoy = new JButton("Hoy");
        btnFechaFinalHoy.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnFechaFinalHoy.setBackground(new Color(100, 149, 237));
        btnFechaFinalHoy.setForeground(Color.WHITE);
        btnFechaFinalHoy.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnFechaFinalHoy.setFocusPainted(false);
        btnFechaFinalHoy.addActionListener(e -> txtFechaFinal.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date())));
        panelOpciones.add(btnFechaFinalHoy, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
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
                "✔ Seleccione un laboratorio (excepto para reporte estadístico).\n" +
                "✔ Ingrese las fechas inicial y final.\n" +
                "✔ Presione 'Generar Reporte PDF' para continuar.");
        txtInstrucciones.setEditable(false);
        txtInstrucciones.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtInstrucciones.setBackground(new Color(245, 245, 255));
        txtInstrucciones.setBorder(new EmptyBorder(10, 10, 10, 10));
        panelInstrucciones.add(txtInstrucciones);

        add(panelInstrucciones, BorderLayout.SOUTH);

        // Comportamiento dinámico
        comboTipoReporte.addActionListener(e -> {
            int selectedIndex = comboTipoReporte.getSelectedIndex();
            boolean esReporteEstadistico = selectedIndex == 2;
            comboLaboratorio.setEnabled(!esReporteEstadistico);
            lblLaboratorio.setEnabled(!esReporteEstadistico);
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
     * Carga los laboratorios en el JComboBox
     */
    private void cargarLaboratorios() {
        try {
            List<Laboratorio> laboratorios = controladorLaboratorio.listar();
            for (Laboratorio lab : laboratorios) {
                comboLaboratorio.addItem(lab.getIdLaboratorio() + " - " + lab.getUbicacion());
            }

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

    /**
     * Inicia el proceso de generación del reporte
     */
    private void generarReporte() {
        // Verificar que se hayan ingresado las fechas
        String fechaInicialStr = txtFechaInicial.getText().trim();
        String fechaFinalStr = txtFechaFinal.getText().trim();
        if (fechaInicialStr.isEmpty() || fechaFinalStr.isEmpty() ||
            !validarFecha(fechaInicialStr) || !validarFecha(fechaFinalStr)) {
            JOptionPane.showMessageDialog(this,
                "Por favor ingrese fechas válidas en formato dd/MM/yyyy",
                "Fecha Inválida",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Convertir las fechas de String a java.sql.Date
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            java.util.Date parsedDateInicial = dateFormat.parse(fechaInicialStr);
            java.util.Date parsedDateFinal = dateFormat.parse(fechaFinalStr);

            java.sql.Date fechaInicial = new java.sql.Date(parsedDateInicial.getTime());
            java.sql.Date fechaFinal = new java.sql.Date(parsedDateFinal.getTime());

            int selectedIndex = comboTipoReporte.getSelectedIndex();
            if (selectedIndex == 2) {
                // Reporte estadístico no requiere laboratorio
                generarReporteEstadistico(fechaInicial, fechaFinal);
            } else {
                // Verificar que se haya seleccionado un laboratorio
                if (comboLaboratorio.getSelectedIndex() == -1) {
                    JOptionPane.showMessageDialog(this,
                        "Debe seleccionar un laboratorio para generar el reporte",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Obtener el ID del laboratorio seleccionado
                String selectedLab = (String) comboLaboratorio.getSelectedItem();
                int idLaboratorio = Integer.parseInt(selectedLab.split(" - ")[0]);

                if (selectedIndex == 0) {
                    // Generar reporte de préstamos por laboratorio
                    List<ReportePrestamo> prestamos = obtenerPrestamosLaboratorio(idLaboratorio, fechaInicial, fechaFinal);

                    if (prestamos.isEmpty()) {
                        JOptionPane.showMessageDialog(this,
                            "No se encontraron préstamos para el laboratorio seleccionado en el rango de fechas indicado",
                            "Información",
                            JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }

                    generarDocumentoPDF(prestamos, idLaboratorio, fechaInicial, fechaFinal);
                } else {
                    // Generar reporte general de préstamos
                    generarReporteGeneralPrestamosPDF(idLaboratorio, fechaInicial, fechaFinal);
                }
            }
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this,
                "Error en el formato de las fechas. Utilice el formato dd/mm/yyyy",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            manejarError("Error al generar el reporte", e);
        }
    }

    // Clase interna para manejar los datos del reporte
    private class ReportePrestamo {
        private java.sql.Date fecha;
        private String hora;
        private int ruUsuario;
        private Integer ruAdministrador;
        private Integer idHorario;
        private String observaciones;
        public ReportePrestamo(java.sql.Date fecha, String hora, int ruUsuario, Integer ruAdministrador, Integer idHorario, String observaciones) {
            this.fecha = fecha;
            this.hora = hora;
            this.ruUsuario = ruUsuario;
            this.ruAdministrador = ruAdministrador;
            this.idHorario = idHorario;
            this.observaciones = observaciones;
        }

        public java.sql.Date getFecha() {
            return fecha;
        }

        public String getHora() {
            return hora;
        }

        public int getRuUsuario() {
            return ruUsuario;
        }

        public Integer getRuAdministrador() {
            return ruAdministrador;
        }

        public Integer getIdHorario() {
            return idHorario;
        }

        public String getObservaciones() {
            return observaciones;
        }
    }

    /**
     * Genera un reporte estadístico de préstamos
     */
    private void generarReporteEstadistico(java.sql.Date fechaInicial, java.sql.Date fechaFinal) {
        try {
            String nombreArchivo = DIRECTORIO_REPORTES + "Reporte_Estadistico_Prestamos_" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".pdf";

            try (FileOutputStream outputStream = new FileOutputStream(nombreArchivo)) {
                Document document = new Document(PageSize.A4);
                PdfWriter.getInstance(document, outputStream);
                document.open();

                // Agregar encabezado
                agregarEncabezadoPDF(document, "REPORTE ESTADÍSTICO DE PRÉSTAMOS", fechaInicial, fechaFinal);
                document.add(new Paragraph(" "));

                // Gráfico de estado de préstamos
                Paragraph subtituloEstado = new Paragraph("ESTADO DE LOS PRÉSTAMOS:", FONT_SUBTITULO);
                subtituloEstado.setAlignment(Element.ALIGN_CENTER);
                document.add(subtituloEstado);
                document.add(new Paragraph(" "));
                agregarGraficoEstadoPrestamos(document, fechaInicial, fechaFinal);
                document.add(new Paragraph(" "));

                // Gráfico de préstamos por laboratorio
                Paragraph subtituloLaboratorio = new Paragraph("PRÉSTAMOS POR LABORATORIO:", FONT_SUBTITULO);
                subtituloLaboratorio.setAlignment(Element.ALIGN_CENTER);
                document.add(subtituloLaboratorio);
                document.add(new Paragraph(" "));
                agregarGraficoPrestamosPorLaboratorio(document, fechaInicial, fechaFinal);

                // Nueva página para gráfico de barras
                document.newPage();

                // Gráfico de préstamos por mes
                Paragraph subtituloMes = new Paragraph("PRÉSTAMOS POR MES:", FONT_SUBTITULO);
                subtituloMes.setAlignment(Element.ALIGN_CENTER);
                document.add(subtituloMes);
                document.add(new Paragraph(" "));
                agregarGraficoPrestamosPorMes(document, fechaInicial, fechaFinal);

                document.close();
                abrirArchivoPDF(nombreArchivo);
            }
        } catch (Exception e) {
            manejarError("Error al generar el reporte estadístico", e);
        }
    }

    /**
     * Agrega un gráfico de torta para estados de préstamos
     */
    private void agregarGraficoEstadoPrestamos(Document document, java.sql.Date fechaInicial, java.sql.Date fechaFinal) throws Exception {
        Map<String, Integer> estadosPrestamos = contarPrestamosPorEstado(fechaInicial, fechaFinal);

        if (estadosPrestamos.isEmpty()) {
            document.add(new Paragraph("No hay datos de préstamos disponibles para generar el gráfico.", FONT_NORMAL));
            return;
        }

        // Crear dataset
        DefaultPieDataset dataset = new DefaultPieDataset();
        for (Map.Entry<String, Integer> entry : estadosPrestamos.entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }

        // Crear gráfico
        JFreeChart chart = ChartFactory.createPieChart(
            "Distribución de Préstamos por Estado",
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
        cellEstado.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tablaInfo.addCell(cellEstado);

        PdfPCell cellPorcentaje = new PdfPCell(new Phrase("Porcentaje", FONT_TABLA_ENCABEZADO));
        cellPorcentaje.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tablaInfo.addCell(cellPorcentaje);

        int total = 0;
        for (Integer cantidad : estadosPrestamos.values()) {
            total += cantidad;
        }

        for (Map.Entry<String, Integer> entry : estadosPrestamos.entrySet()) {
            tablaInfo.addCell(new PdfPCell(new Phrase(entry.getKey(), FONT_TABLA_CELDA)));
            double porcentaje = (double) entry.getValue() / total * 100;
            tablaInfo.addCell(new PdfPCell(new Phrase(String.format("%.2f%%", porcentaje), FONT_TABLA_CELDA)));
        }

        document.add(tablaInfo);
    }

    /**
     * Agrega un gráfico de torta para préstamos por laboratorio
     */
    private void agregarGraficoPrestamosPorLaboratorio(Document document, java.sql.Date fechaInicial, java.sql.Date fechaFinal) throws Exception {
        Map<Integer, Integer> prestamosPorLab = contarPrestamosPorLaboratorio(fechaInicial, fechaFinal);

        if (prestamosPorLab.isEmpty()) {
            document.add(new Paragraph("No hay datos de préstamos por laboratorio disponibles para generar el gráfico.", FONT_NORMAL));
            return;
        }

        // Crear dataset
        DefaultPieDataset dataset = new DefaultPieDataset();
        for (Map.Entry<Integer, Integer> entry : prestamosPorLab.entrySet()) {
            dataset.setValue("Laboratorio " + entry.getKey(), entry.getValue());
        }

        // Crear gráfico
        JFreeChart chart = ChartFactory.createPieChart(
            "Distribución de Préstamos por Laboratorio",
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
        cellLab.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tablaInfo.addCell(cellLab);

        PdfPCell cellPorcentaje = new PdfPCell(new Phrase("Porcentaje", FONT_TABLA_ENCABEZADO));
        cellPorcentaje.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tablaInfo.addCell(cellPorcentaje);

        int total = 0;
        for (Integer cantidad : prestamosPorLab.values()) {
            total += cantidad;
        }

        for (Map.Entry<Integer, Integer> entry : prestamosPorLab.entrySet()) {
            tablaInfo.addCell(new PdfPCell(new Phrase("Laboratorio " + entry.getKey(), FONT_TABLA_CELDA)));
            double porcentaje = (double) entry.getValue() / total * 100;
            tablaInfo.addCell(new PdfPCell(new Phrase(String.format("%.2f%%", porcentaje), FONT_TABLA_CELDA)));
        }

        document.add(tablaInfo);
    }

    /**
     * Agrega un gráfico de barras para préstamos por mes
     */
    private void agregarGraficoPrestamosPorMes(Document document, java.sql.Date fechaInicial, java.sql.Date fechaFinal) throws Exception {
        Map<String, Integer> prestamosPorMes = contarPrestamosPorMes(fechaInicial, fechaFinal);

        if (prestamosPorMes.isEmpty()) {
            document.add(new Paragraph("No hay datos de préstamos por mes disponibles para generar el gráfico.", FONT_NORMAL));
            return;
        }

        // Crear dataset
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Map.Entry<String, Integer> entry : prestamosPorMes.entrySet()) {
            dataset.setValue(entry.getValue(), "Cantidad", entry.getKey());
        }

        // Crear gráfico
        JFreeChart chart = ChartFactory.createBarChart(
            "Préstamos por Mes",
            "Mes",
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

        // Tabla de datos
        PdfPTable tablaInfo = new PdfPTable(2);
        tablaInfo.setWidthPercentage(70);
        tablaInfo.setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell cellMes = new PdfPCell(new Phrase("Mes", FONT_TABLA_ENCABEZADO));
        cellMes.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tablaInfo.addCell(cellMes);

        PdfPCell cellCantidad = new PdfPCell(new Phrase("Cantidad", FONT_TABLA_ENCABEZADO));
        cellCantidad.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tablaInfo.addCell(cellCantidad);

        for (Map.Entry<String, Integer> entry : prestamosPorMes.entrySet()) {
            tablaInfo.addCell(new PdfPCell(new Phrase(entry.getKey(), FONT_TABLA_CELDA)));
            tablaInfo.addCell(new PdfPCell(new Phrase(String.valueOf(entry.getValue()), FONT_TABLA_CELDA)));
        }

        document.add(tablaInfo);
    }

    /**
     * Conta préstamos por estado
     */
    private Map<String, Integer> contarPrestamosPorEstado(java.sql.Date fechaInicial, java.sql.Date fechaFinal) throws SQLException {
        Map<String, Integer> resultado = new HashMap<>();
        List<Prestamo> listaPrestamos = controladorPrestamo.listar();

        for (Prestamo prestamo : listaPrestamos) {
            java.sql.Date fechaPrestamo = prestamo.getFechaPrestamo();
            if (fechaPrestamo != null && !fechaPrestamo.before(fechaInicial) && !fechaPrestamo.after(fechaFinal)) {
                String estado = prestamo.getEstadoPrestamo();
                if (estado != null && !estado.isEmpty()) {
                    resultado.put(estado, resultado.getOrDefault(estado, 0) + 1);
                }
            }
        }

        return resultado;
    }

    /**
     * Conta préstamos por laboratorio
     */
    private Map<Integer, Integer> contarPrestamosPorLaboratorio(java.sql.Date fechaInicial, java.sql.Date fechaFinal) throws SQLException {
        Map<Integer, Integer> resultado = new HashMap<>();

        try (Connection conn = ConexionBD.conectar()) {
            String sql = "SELECT h.id_laboratorio " +
                        "FROM prestamo p " +
                        "JOIN horario h ON p.id_horario = h.id_horario " +
                        "WHERE p.fecha_prestamo BETWEEN ? AND ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setDate(1, fechaInicial);
                stmt.setDate(2, fechaFinal);

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        int idLab = rs.getInt("id_laboratorio");
                        resultado.put(idLab, resultado.getOrDefault(idLab, 0) + 1);
                    }
                }
            }
        }

        return resultado;
    }

    /**
     * Conta préstamos por mes
     */
    private Map<String, Integer> contarPrestamosPorMes(java.sql.Date fechaInicial, java.sql.Date fechaFinal) throws SQLException {
        Map<String, Integer> resultado = new HashMap<>();
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMM yyyy");
        List<Prestamo> listaPrestamos = controladorPrestamo.listar();

        for (Prestamo prestamo : listaPrestamos) {
            java.sql.Date fechaPrestamo = prestamo.getFechaPrestamo();
            if (fechaPrestamo != null && !fechaPrestamo.before(fechaInicial) && !fechaPrestamo.after(fechaFinal)) {
                String mes = monthFormat.format(fechaPrestamo);
                resultado.put(mes, resultado.getOrDefault(mes, 0) + 1);
            }
        }

        return resultado;
    }

    /**
     * Obtiene los préstamos de un laboratorio en un rango de fechas
     */
    private List<ReportePrestamo> obtenerPrestamosLaboratorio(int idLaboratorio, java.sql.Date fechaInicial, java.sql.Date fechaFinal) {
        List<ReportePrestamo> prestamos = new ArrayList<>();

        try {
            String sql = "SELECT p.fecha_prestamo, p.hora_prestamo, p.ru_usuario, p.ru_administrador, p.id_horario, p.observaciones " +
                         "FROM prestamo p " +
                         "JOIN horario h ON p.id_horario = h.id_horario " +
                         "WHERE h.id_laboratorio = ? " +
                         "AND p.fecha_prestamo BETWEEN ? AND ? " +
                         "ORDER BY p.fecha_prestamo, p.hora_prestamo";

            try (Connection conn = ConexionBD.conectar();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, idLaboratorio);
                stmt.setDate(2, fechaInicial);
                stmt.setDate(3, fechaFinal);

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        prestamos.add(new ReportePrestamo(
                            rs.getDate("fecha_prestamo"),
                            rs.getString("hora_prestamo"),
                            rs.getInt("ru_usuario"),
                            rs.getObject("ru_administrador") != null ? rs.getInt("ru_administrador") : null,
                            rs.getObject("id_horario") != null ? rs.getInt("id_horario") : null,
                            rs.getString("observaciones")
                        ));
                    }
                }
            }
        } catch (SQLException e) {
            manejarError("Error al obtener los préstamos", e);
        }

        return prestamos;
    }

    /**
     * Genera un reporte en formato PDF con los datos de préstamos del laboratorio
     */
    private void generarDocumentoPDF(List<ReportePrestamo> prestamos, int idLaboratorio, java.sql.Date fechaInicial, java.sql.Date fechaFinal) {
        try {
            String nombreArchivo = DIRECTORIO_REPORTES + "Reporte_Prestamos_Lab" + idLaboratorio + "_" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".pdf";

            Document documento = new Document(PageSize.A4);
            try (FileOutputStream outputStream = new FileOutputStream(nombreArchivo)) {
                PdfWriter.getInstance(documento, outputStream);
                documento.open();

                // Agregar encabezado
                agregarEncabezadoPDF(documento, "REPORTE DE PRÉSTAMOS DE LABORATORIO " + idLaboratorio, fechaInicial, fechaFinal);

                // Subtítulo
                Paragraph subtitulo = new Paragraph("PRÉSTAMOS REALIZADOS", FONT_SUBTITULO);
                subtitulo.setAlignment(Element.ALIGN_CENTER);
                documento.add(subtitulo);
                documento.add(new Paragraph(" "));

                // Crear tabla
                PdfPTable tabla = new PdfPTable(6);
                tabla.setWidthPercentage(100);
                float[] anchos = {1.5f, 1f, 1.5f, 1.5f, 1.5f, 2.5f};
                tabla.setWidths(anchos);

                // Encabezados
                String[] titulos = {"Fecha", "Hora", "RU Usuario", "RU Administrador", "ID Horario", "Observaciones"};
                for (String titulo : titulos) {
                    PdfPCell celda = new PdfPCell(new Phrase(titulo, FONT_TABLA_ENCABEZADO));
                    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    celda.setPadding(5);
                    tabla.addCell(celda);
                }

                // Llenar tabla
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                for (int i = 0; i < prestamos.size(); i++) {
                    ReportePrestamo prestamo = prestamos.get(i);
                    BaseColor colorFondo = (i % 2 == 1) ? new BaseColor(240, 240, 255) : BaseColor.WHITE;

                    PdfPCell celdaFecha = new PdfPCell(new Phrase(sdf.format(prestamo.getFecha()), FONT_TABLA_CELDA));
                    celdaFecha.setBackgroundColor(colorFondo);
                    celdaFecha.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celdaFecha.setPadding(5);
                    tabla.addCell(celdaFecha);

                    PdfPCell celdaHora = new PdfPCell(new Phrase(prestamo.getHora(), FONT_TABLA_CELDA));
                    celdaHora.setBackgroundColor(colorFondo);
                    celdaHora.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celdaHora.setPadding(5);
                    tabla.addCell(celdaHora);

                    PdfPCell celdaUsuario = new PdfPCell(new Phrase(String.valueOf(prestamo.getRuUsuario()), FONT_TABLA_CELDA));
                    celdaUsuario.setBackgroundColor(colorFondo);
                    celdaUsuario.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celdaUsuario.setPadding(5);
                    tabla.addCell(celdaUsuario);

                    String valorAdmin = prestamo.getRuAdministrador() != null ? String.valueOf(prestamo.getRuAdministrador()) : "N/A";
                    PdfPCell celdaAdmin = new PdfPCell(new Phrase(valorAdmin, FONT_TABLA_CELDA));
                    celdaAdmin.setBackgroundColor(colorFondo);
                    celdaAdmin.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celdaAdmin.setPadding(5);
                    tabla.addCell(celdaAdmin);

                    String valorHorario = prestamo.getIdHorario() != null ? String.valueOf(prestamo.getIdHorario()) : "N/A";
                    PdfPCell celdaHorario = new PdfPCell(new Phrase(valorHorario, FONT_TABLA_CELDA));
                    celdaHorario.setBackgroundColor(colorFondo);
                    celdaHorario.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celdaHorario.setPadding(5);
                    tabla.addCell(celdaHorario);

                    String valorObs = prestamo.getObservaciones() != null && !prestamo.getObservaciones().isEmpty() ? prestamo.getObservaciones() : "Ninguna";
                    PdfPCell celdaObs = new PdfPCell(new Phrase(valorObs, FONT_TABLA_CELDA));
                    celdaObs.setBackgroundColor(colorFondo);
                    celdaObs.setPadding(5);
                    tabla.addCell(celdaObs);
                }

                if (prestamos.isEmpty()) {
                    PdfPCell celdaNoData = new PdfPCell(new Phrase("No hay préstamos registrados en este período", FONT_TABLA_CELDA));
                    celdaNoData.setColspan(6);
                    celdaNoData.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celdaNoData.setPadding(10);
                    tabla.addCell(celdaNoData);
                }

                documento.add(tabla);
                documento.close();
                abrirArchivoPDF(nombreArchivo);
            }
        } catch (Exception e) {
            manejarError("Error al generar el documento PDF", e);
        }
    }

    // Clases para detalles de equipamiento e insumos
    private class DetalleEquipamiento {
        private int idEquipamiento;
        private String nombreEquipamiento;
        private String estado;

        public DetalleEquipamiento(int idEquipamiento, String nombreEquipamiento, String estado) {
            this.idEquipamiento = idEquipamiento;
            this.nombreEquipamiento = nombreEquipamiento;
            this.estado = estado;
        }

        public int getIdEquipamiento() {
            return idEquipamiento;
        }

        public String getNombreEquipamiento() {
            return nombreEquipamiento;
        }

        public String getEstado() {
            return estado;
        }
    }

    private class DetalleInsumo {
        private int idInsumo;
        private String nombreInsumo;
        private int cantidadInicial;
        private Integer cantidadFinal;

        public DetalleInsumo(int idInsumo, String nombreInsumo, int cantidadInicial, Integer cantidadFinal) {
            this.idInsumo = idInsumo;
            this.nombreInsumo = nombreInsumo;
            this.cantidadInicial = cantidadInicial;
            this.cantidadFinal = cantidadFinal;
        }

        public int getIdInsumo() {
            return idInsumo;
        }

        public String getNombreInsumo() {
            return nombreInsumo;
        }

        public int getCantidadInicial() {
            return cantidadInicial;
        }

        public Integer getCantidadFinal() {
            return cantidadFinal;
        }
    }

    private class PrestamoCompleto {
        private int idPrestamo;
        private java.sql.Date fecha;
        private String hora;
        private int ruUsuario;
        private Integer ruAdministrador;
        private Integer idHorario;
        private String observaciones;
        private List<DetalleEquipamiento> equipamientos;
        private List<DetalleInsumo> insumos;

        public PrestamoCompleto(int idPrestamo, java.sql.Date fecha, String hora, int ruUsuario,
                               Integer ruAdministrador, Integer idHorario, String observaciones) {
            this.idPrestamo = idPrestamo;
            this.fecha = fecha;
            this.hora = hora;
            this.ruUsuario = ruUsuario;
            this.ruAdministrador = ruAdministrador;
            this.idHorario = idHorario;
            this.observaciones = observaciones;
            this.equipamientos = new ArrayList<>();
            this.insumos = new ArrayList<>();
        }

        public int getIdPrestamo() {
            return idPrestamo;
        }

        public java.sql.Date getFecha() {
            return fecha;
        }

        public String getHora() {
            return hora;
        }

        public int getRuUsuario() {
            return ruUsuario;
        }

        public Integer getRuAdministrador() {
            return ruAdministrador;
        }

        public Integer getIdHorario() {
            return idHorario;
        }

        public String getObservaciones() {
            return observaciones;
        }

        public List<DetalleEquipamiento> getEquipamientos() {
            return equipamientos;
        }

        public List<DetalleInsumo> getInsumos() {
            return insumos;
        }

        public void addEquipamiento(DetalleEquipamiento equipamiento) {
            this.equipamientos.add(equipamiento);
        }

        public void addInsumo(DetalleInsumo insumo) {
            this.insumos.add(insumo);
        }
    }

    /**
     * Obtiene los datos completos de los préstamos, incluyendo equipamiento e insumos
     */
    private List<PrestamoCompleto> obtenerPrestamosCompletos(int idLaboratorio, java.sql.Date fechaInicial, java.sql.Date fechaFinal) {
        Map<Integer, PrestamoCompleto> prestamosMap = new HashMap<>();

        try (Connection conn = ConexionBD.conectar()) {
            // Obtener préstamos básicos
            String sqlPrestamos = "SELECT p.id_prestamo, p.fecha_prestamo, p.hora_prestamo, p.ru_usuario, " +
                                 "p.ru_administrador, p.id_horario, p.observaciones " +
                                 "FROM prestamo p " +
                                 "JOIN horario h ON p.id_horario = h.id_horario " +
                                 "WHERE h.id_laboratorio = ? " +
                                 "AND p.fecha_prestamo BETWEEN ? AND ? " +
                                 "ORDER BY p.fecha_prestamo, p.hora_prestamo";

            try (PreparedStatement stmt = conn.prepareStatement(sqlPrestamos)) {
                stmt.setInt(1, idLaboratorio);
                stmt.setDate(2, fechaInicial);
                stmt.setDate(3, fechaFinal);

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        int idPrestamo = rs.getInt("id_prestamo");
                        PrestamoCompleto prestamo = new PrestamoCompleto(
                            idPrestamo,
                            rs.getDate("fecha_prestamo"),
                            rs.getString("hora_prestamo"),
                            rs.getInt("ru_usuario"),
                            rs.getObject("ru_administrador") != null ? rs.getInt("ru_administrador") : null,
                            rs.getObject("id_horario") != null ? rs.getInt("id_horario") : null,
                            rs.getString("observaciones")
                        );
                        prestamosMap.put(idPrestamo, prestamo);
                    }
                }
            }

            // Obtener equipamiento para cada préstamo
            for (PrestamoCompleto prestamo : prestamosMap.values()) {
                String sqlEquipamiento = "SELECT e.id_equipamiento, e.nombre_equipamiento, e.estado " +
                                        "FROM detalle_prestamo_equipamiento dpe " +
                                        "JOIN equipamiento e ON dpe.id_equipamiento = e.id_equipamiento " +
                                        "WHERE dpe.id_prestamo = ?";

                try (PreparedStatement stmt = conn.prepareStatement(sqlEquipamiento)) {
                    stmt.setInt(1, prestamo.getIdPrestamo());

                    try (ResultSet rs = stmt.executeQuery()) {
                        while (rs.next()) {
                            DetalleEquipamiento equipamiento = new DetalleEquipamiento(
                                rs.getInt("id_equipamiento"),
                                rs.getString("nombre_equipamiento"),
                                rs.getString("estado")
                            );
                            prestamo.addEquipamiento(equipamiento);
                        }
                    }
                }
            }

            // Obtener insumos para cada préstamo
            for (PrestamoCompleto prestamo : prestamosMap.values()) {
                String sqlInsumos = "SELECT i.id_insumo, i.nombre_insumo, dpi.cantidad_inicial, dpi.cantidad_final " +
                                   "FROM detalle_prestamo_insumo dpi " +
                                   "JOIN insumos i ON dpi.id_insumo = i.id_insumo " +
                                   "WHERE dpi.id_prestamo = ?";

                try (PreparedStatement stmt = conn.prepareStatement(sqlInsumos)) {
                    stmt.setInt(1, prestamo.getIdPrestamo());

                    try (ResultSet rs = stmt.executeQuery()) {
                        while (rs.next()) {
                            DetalleInsumo insumo = new DetalleInsumo(
                                rs.getInt("id_insumo"),
                                rs.getString("nombre_insumo"),
                                rs.getInt("cantidad_inicial"),
                                rs.getObject("cantidad_final") != null ? rs.getInt("cantidad_final") : null
                            );
                            prestamo.addInsumo(insumo);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            manejarError("Error al obtener los datos de préstamos", e);
        }

        // Convertir el mapa a una lista ordenada por fecha y hora
        List<PrestamoCompleto> prestamos = new ArrayList<>(prestamosMap.values());
        prestamos.sort((p1, p2) -> {
            int compareFecha = p1.getFecha().compareTo(p2.getFecha());
            if (compareFecha == 0) {
                return p1.getHora().compareTo(p2.getHora());
            }
            return compareFecha;
        });

        return prestamos;
    }

    /**
     * Genera un reporte general en formato PDF con los datos completos de préstamos
     */
    private void generarReporteGeneralPrestamosPDF(int idLaboratorio, java.sql.Date fechaInicial, java.sql.Date fechaFinal) {
        try {
            List<PrestamoCompleto> prestamos = obtenerPrestamosCompletos(idLaboratorio, fechaInicial, fechaFinal);

            if (prestamos.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "No se encontraron préstamos para el laboratorio seleccionado en el rango de fechas indicado",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            String nombreArchivo = DIRECTORIO_REPORTES + "Reporte_General_Prestamos_Lab" + idLaboratorio + "_" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".pdf";

            Document documento = new Document(PageSize.A4);
            try (FileOutputStream outputStream = new FileOutputStream(nombreArchivo)) {
                PdfWriter.getInstance(documento, outputStream);
                documento.open();

                // Agregar encabezado
                agregarEncabezadoPDF(documento, "REPORTE GENERAL DE PRÉSTAMOS DEL LABORATORIO " + idLaboratorio, fechaInicial, fechaFinal);

                for (int i = 0; i < prestamos.size(); i++) {
                    PrestamoCompleto prestamo = prestamos.get(i);

                    if (i > 0) {
                        documento.newPage();
                    }

                    // Título del préstamo
                    Paragraph tituloPrestamo = new Paragraph("PRÉSTAMO " + (i + 1), FONT_SUBTITULO);
                    documento.add(tituloPrestamo);
                    documento.add(new Paragraph(" "));

                    // Tabla de datos básicos del préstamo
                    PdfPTable tablaPrestamo = new PdfPTable(6);
                    tablaPrestamo.setWidthPercentage(100);
                    float[] anchosPrestamo = {1.5f, 1f, 1.5f, 1.5f, 1.5f, 2.5f};
                    tablaPrestamo.setWidths(anchosPrestamo);

                    String[] titulosPrestamo = {"Fecha", "Hora", "RU Usuario", "RU Administrador", "ID Horario", "Observaciones"};
                    for (String titulo : titulosPrestamo) {
                        PdfPCell celda = new PdfPCell(new Phrase(titulo, FONT_TABLA_ENCABEZADO));
                        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                        celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        celda.setPadding(5);
                        tablaPrestamo.addCell(celda);
                    }

                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    PdfPCell celdaFecha = new PdfPCell(new Phrase(sdf.format(prestamo.getFecha()), FONT_TABLA_CELDA));
                    celdaFecha.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celdaFecha.setPadding(5);
                    tablaPrestamo.addCell(celdaFecha);

                    PdfPCell celdaHora = new PdfPCell(new Phrase(prestamo.getHora(), FONT_TABLA_CELDA));
                    celdaHora.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celdaHora.setPadding(5);
                    tablaPrestamo.addCell(celdaHora);

                    PdfPCell celdaUsuario = new PdfPCell(new Phrase(String.valueOf(prestamo.getRuUsuario()), FONT_TABLA_CELDA));
                    celdaUsuario.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celdaUsuario.setPadding(5);
                    tablaPrestamo.addCell(celdaUsuario);

                    String valorAdmin = prestamo.getRuAdministrador() != null ? String.valueOf(prestamo.getRuAdministrador()) : "N/A";
                    PdfPCell celdaAdmin = new PdfPCell(new Phrase(valorAdmin, FONT_TABLA_CELDA));
                    celdaAdmin.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celdaAdmin.setPadding(5);
                    tablaPrestamo.addCell(celdaAdmin);

                    String valorHorario = prestamo.getIdHorario() != null ? String.valueOf(prestamo.getIdHorario()) : "N/A";
                    PdfPCell celdaHorario = new PdfPCell(new Phrase(valorHorario, FONT_TABLA_CELDA));
                    celdaHorario.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celdaHorario.setPadding(5);
                    tablaPrestamo.addCell(celdaHorario);

                    String valorObs = prestamo.getObservaciones() != null && !prestamo.getObservaciones().isEmpty() ? prestamo.getObservaciones() : "Ninguna";
                    PdfPCell celdaObs = new PdfPCell(new Phrase(valorObs, FONT_TABLA_CELDA));
                    celdaObs.setPadding(5);
                    tablaPrestamo.addCell(celdaObs);

                    documento.add(tablaPrestamo);
                    documento.add(new Paragraph(" "));

                    // Equipamiento
                    if (!prestamo.getEquipamientos().isEmpty()) {
                        Paragraph tituloEquipamiento = new Paragraph("EQUIPAMIENTO", FONT_SUBTITULO);
                        documento.add(tituloEquipamiento);
                        documento.add(new Paragraph(" "));
                        PdfPTable tablaEquipamiento = new PdfPTable(3);
                        tablaEquipamiento.setWidthPercentage(100);
                        float[] anchosEquipamiento = {1f, 2f, 1f};
                        tablaEquipamiento.setWidths(anchosEquipamiento);

                        String[] titulosEquipamiento = {"ID Equipamiento", "Nombre", "Estado"};
                        for (String titulo : titulosEquipamiento) {
                            PdfPCell celda = new PdfPCell(new Phrase(titulo, FONT_TABLA_ENCABEZADO_SUB));
                            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                            celda.setBackgroundColor(new BaseColor(153, 204, 255));
                            celda.setPadding(4);
                            tablaEquipamiento.addCell(celda);
                        }

                        for (int j = 0; j < prestamo.getEquipamientos().size(); j++) {
                            DetalleEquipamiento equipamiento = prestamo.getEquipamientos().get(j);
                            BaseColor colorFondo = (j % 2 == 1) ? new BaseColor(240, 240, 255) : BaseColor.WHITE;

                            PdfPCell celdaIdEquip = new PdfPCell(new Phrase(String.valueOf(equipamiento.getIdEquipamiento()), FONT_TABLA_CELDA_SUB));
                            celdaIdEquip.setBackgroundColor(colorFondo);
                            celdaIdEquip.setHorizontalAlignment(Element.ALIGN_CENTER);
                            celdaIdEquip.setPadding(4);
                            tablaEquipamiento.addCell(celdaIdEquip);

                            PdfPCell celdaNombreEquip = new PdfPCell(new Phrase(equipamiento.getNombreEquipamiento(), FONT_TABLA_CELDA_SUB));
                            celdaNombreEquip.setBackgroundColor(colorFondo);
                            celdaNombreEquip.setPadding(4);
                            tablaEquipamiento.addCell(celdaNombreEquip);

                            PdfPCell celdaEstadoEquip = new PdfPCell(new Phrase(equipamiento.getEstado(), FONT_TABLA_CELDA_SUB));
                            celdaEstadoEquip.setBackgroundColor(colorFondo);
                            celdaEstadoEquip.setPadding(4);
                            tablaEquipamiento.addCell(celdaEstadoEquip);
                        }

                        documento.add(tablaEquipamiento);
                        documento.add(new Paragraph(" "));
                    }

                    // Insumos
                    if (!prestamo.getInsumos().isEmpty()) {
                        Paragraph tituloInsumo = new Paragraph("INSUMOS", FONT_SUBTITULO);
                        documento.add(tituloInsumo);
                        documento.add(new Paragraph(" "));

                        PdfPTable tablaInsumo = new PdfPTable(4);
                        tablaInsumo.setWidthPercentage(100);
                        float[] anchosInsumo = {1f, 2f, 1f, 1f};
                        tablaInsumo.setWidths(anchosInsumo);

                        String[] titulosInsumo = {"ID Insumo", "Nombre", "Cantidad Inicial", "Cantidad Final"};
                        for (String titulo : titulosInsumo) {
                            PdfPCell celda = new PdfPCell(new Phrase(titulo, FONT_TABLA_ENCABEZADO_SUB));
                            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                            celda.setBackgroundColor(new BaseColor(204, 255, 204));
                            celda.setPadding(4);
                            tablaInsumo.addCell(celda);
                        }

                        for (int j = 0; j < prestamo.getInsumos().size(); j++) {
                            DetalleInsumo insumo = prestamo.getInsumos().get(j);
                            BaseColor colorFondo = (j % 2 == 1) ? new BaseColor(240, 255, 240) : BaseColor.WHITE;

                            PdfPCell celdaIdInsumo = new PdfPCell(new Phrase(String.valueOf(insumo.getIdInsumo()), FONT_TABLA_CELDA_SUB));
                            celdaIdInsumo.setBackgroundColor(colorFondo);
                            celdaIdInsumo.setHorizontalAlignment(Element.ALIGN_CENTER);
                            celdaIdInsumo.setPadding(4);
                            tablaInsumo.addCell(celdaIdInsumo);

                            PdfPCell celdaNombreInsumo = new PdfPCell(new Phrase(insumo.getNombreInsumo(), FONT_TABLA_CELDA_SUB));
                            celdaNombreInsumo.setBackgroundColor(colorFondo);
                            celdaNombreInsumo.setPadding(4);
                            tablaInsumo.addCell(celdaNombreInsumo);

                            PdfPCell celdaCantInicial = new PdfPCell(new Phrase(String.valueOf(insumo.getCantidadInicial()), FONT_TABLA_CELDA_SUB));
                            celdaCantInicial.setBackgroundColor(colorFondo);
                            celdaCantInicial.setHorizontalAlignment(Element.ALIGN_CENTER);
                            celdaCantInicial.setPadding(4);
                            tablaInsumo.addCell(celdaCantInicial);

                            String valorCantFinal = insumo.getCantidadFinal() != null ? String.valueOf(insumo.getCantidadFinal()) : "No devuelto";
                            PdfPCell celdaCantFinal = new PdfPCell(new Phrase(valorCantFinal, FONT_TABLA_CELDA_SUB));
                            celdaCantFinal.setBackgroundColor(colorFondo);
                            celdaCantFinal.setHorizontalAlignment(Element.ALIGN_CENTER);
                            celdaCantFinal.setPadding(4);
                            tablaInsumo.addCell(celdaCantFinal);
                        }

                        documento.add(tablaInsumo);
                    }
                }

                documento.close();
                abrirArchivoPDF(nombreArchivo);
            }
        } catch (Exception e) {
            manejarError("Error al generar el reporte general de préstamos", e);
        }
    }

    /**
     * Agrega el encabezado estándar a un documento PDF
     */
    private void agregarEncabezadoPDF(Document document, String subtitulo, java.sql.Date fechaInicial, java.sql.Date fechaFinal) throws Exception {
        PdfPTable tablaEncabezado = new PdfPTable(1);
        tablaEncabezado.setWidthPercentage(100);

        PdfPCell cellUniversidad = new PdfPCell(new Phrase("UNIVERSIDAD SALESIANA DE BOLIVIA", FONT_TITULO));
        cellUniversidad.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellUniversidad.setBorder(Rectangle.BOX);
        tablaEncabezado.addCell(cellUniversidad);

        PdfPCell cellReporte = new PdfPCell(new Phrase(subtitulo, FONT_SUBTITULO));
        cellReporte.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellReporte.setBorder(Rectangle.BOX);
        tablaEncabezado.addCell(cellReporte);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String periodo = "PERÍODO: " + sdf.format(fechaInicial) + " - " + sdf.format(fechaFinal);
        PdfPCell cellFecha = new PdfPCell(new Phrase(periodo, FONT_FECHA));
        cellFecha.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFecha.setBorder(Rectangle.BOX);
        tablaEncabezado.addCell(cellFecha);

        document.add(tablaEncabezado);
        document.add(new Paragraph(" "));
    }

    /**
     * Abre un archivo PDF
     */
    private void abrirArchivoPDF(String rutaArchivo) {
        try {
            File archivo = new File(rutaArchivo);
            if (archivo.exists()) {
                Desktop.getDesktop().open(archivo);
                JOptionPane.showMessageDialog(this,
                    "Reporte PDF generado exitosamente: " + rutaArchivo,
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    "El archivo PDF no se encontró: " + rutaArchivo,
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            manejarError("Error al abrir el archivo PDF", e);
        }
    }

    /**
     * Maneja errores mostrando un mensaje al usuario
     */
    private void manejarError(String mensaje, Exception e) {
        JOptionPane.showMessageDialog(this,
            mensaje + ": " + e.getMessage(),
            "Error",
            JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}