/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
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
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.spire.doc.*;
import com.spire.doc.documents.*;
import com.spire.doc.fields.TextRange;

//Para la sección de equipamiento
import Clases.Equipamiento;
import Controles.ControladorEquipamiento;
import Controles.ControladorDetallePrestamoEquipamiento;
import java.util.HashMap;
import java.util.Map;

//Librerías para los pdf
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.awt.Desktop;

public class PanelReportePrestamo extends JPanel {
    private JComboBox<String> comboLaboratorio;
    private JComboBox<String> comboTipoReporte;
    private JTextField txtFechaInicial;
    private JTextField txtFechaFinal;
    private ControladorLaboratorio controladorLaboratorio;
    private ControladorPrestamo controladorPrestamo;
    private ControladorHorario controladorHorario;
    private JLabel lblTitulo;
    private JPanel panelCentral;

    public PanelReportePrestamo() {
        controladorLaboratorio = new ControladorLaboratorio();
        controladorPrestamo = new ControladorPrestamo();
        controladorHorario = new ControladorHorario();
        
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Panel de título
        JPanel panelTitulo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelTitulo.setBackground(new Color(51, 0, 153));
        lblTitulo = new JLabel("GENERACIÓN DE REPORTES DE PRÉSTAMOS");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(Color.WHITE);
        panelTitulo.add(lblTitulo);
        
        // Panel de formulario
        panelCentral = new JPanel();
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelCentral.setBackground(new Color(240, 240, 255));
        
        // Panel para el formulario con GridBagLayout para mejor control
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBackground(new Color(240, 240, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Crear componentes con estilo mejorado
        JLabel lblLaboratorio = crearEtiqueta("Seleccione Laboratorio:");
        comboLaboratorio = crearComboBox();
        cargarLaboratorios();

        JLabel lblTipoReporte = crearEtiqueta("Tipo de Reporte:");
        comboTipoReporte = crearComboBox();
        comboTipoReporte.addItem("Reporte de préstamos por laboratorio");
        comboTipoReporte.addItem("Reporte general de préstamos");

        JLabel lblFechaInicio = crearEtiqueta("Fecha Inicial (dd/mm/yyyy):");
        txtFechaInicial = crearCampoTexto();

        JLabel lblFechaFinal = crearEtiqueta("Fecha Final (dd/mm/yyyy):");
        txtFechaFinal = crearCampoTexto();

        JButton btnGenerar = new JButton("Generar Reporte");
        btnGenerar.setFont(new Font("Arial", Font.BOLD, 14));
        btnGenerar.setBackground(new Color(0, 63, 135));
        btnGenerar.setForeground(Color.WHITE);
        btnGenerar.setFocusPainted(false);
        btnGenerar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGenerar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generarReporte();
            }
        });

        // Añadir componentes al panel de formulario con GridBagLayout
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        panelFormulario.add(lblLaboratorio, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        panelFormulario.add(comboLaboratorio, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        panelFormulario.add(lblTipoReporte, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        panelFormulario.add(comboTipoReporte, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        panelFormulario.add(lblFechaInicio, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        panelFormulario.add(txtFechaInicial, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        panelFormulario.add(lblFechaFinal, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        panelFormulario.add(txtFechaFinal, gbc);

        // Panel para el botón
        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBoton.setBackground(new Color(240, 240, 255));
        panelBoton.add(btnGenerar);

        // Añadir paneles al panel central
        panelCentral.add(panelFormulario);
        panelCentral.add(Box.createVerticalStrut(15));
        panelCentral.add(panelBoton);

        // Añadir componentes al panel principal
        add(panelTitulo, BorderLayout.NORTH);
        add(panelCentral, BorderLayout.CENTER);
    }

    private JLabel crearEtiqueta(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(new Color(0, 0, 102));
        return label;
    }

    private JTextField crearCampoTexto() {
        JTextField textField = new JTextField(15);
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 0, 102)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        return textField;
    }

    private JComboBox<String> crearComboBox() {
        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        comboBox.setBackground(Color.WHITE);
        comboBox.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 102)));
        return comboBox;
    }

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

    private void generarReporte() {
    // Verificar que se hayan ingresado las fechas
    if (txtFechaInicial.getText().trim().isEmpty() || txtFechaFinal.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this,
            "Debe ingresar fechas inicial y final para el reporte",
            "Error",
            JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Obtener el tipo de reporte seleccionado
    String tipoReporte = (String) comboTipoReporte.getSelectedItem();

    try {
        // Convertir las fechas de String a java.sql.Date
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        java.util.Date parsedDateInicial = dateFormat.parse(txtFechaInicial.getText());
        java.util.Date parsedDateFinal = dateFormat.parse(txtFechaFinal.getText());

        java.sql.Date fechaInicial = new java.sql.Date(parsedDateInicial.getTime());
        java.sql.Date fechaFinal = new java.sql.Date(parsedDateFinal.getTime());

        // Manejar el tipo de reporte seleccionado
        if ("Reporte de préstamos por laboratorio".equals(tipoReporte)) {
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

            // Generar reporte de préstamos por laboratorio
            List<ReportePrestamo> prestamos = obtenerPrestamosLaboratorio(idLaboratorio, fechaInicial, fechaFinal);

            if (prestamos.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "No se encontraron préstamos para el laboratorio seleccionado en el rango de fechas indicado",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Generar el documento Word para préstamos por laboratorio
            generarDocumentoPDF(prestamos, idLaboratorio);

        }else if ("Reporte general de préstamos".equals(tipoReporte)) {
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

            // Generar reporte general de préstamos
            generarReporteGeneralPrestamosPDF(idLaboratorio, fechaInicial, fechaFinal);
        }

        

    } catch (ParseException e) {
        JOptionPane.showMessageDialog(this,
            "Error en el formato de las fechas. Utilice el formato dd/mm/yyyy",
            "Error",
            JOptionPane.ERROR_MESSAGE);
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
            "Error al generar el reporte: " + e.getMessage(),
            "Error",
            JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}
    
    //SECCIÓN REFERENTE A REPORTE GENERAL DE PRÉSTAMOS (INICIO)
    // Primero, añadiremos clases para los detalles de equipamiento e insumos por préstamo
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

// Clase para almacenar toda la información de un préstamo, incluyendo equipamiento e insumos
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

// Método para obtener todos los préstamos completos para un laboratorio específico
private List<PrestamoCompleto> obtenerPrestamosCompletos(int idLaboratorio, java.sql.Date fechaInicial, java.sql.Date fechaFinal) {
    Map<Integer, PrestamoCompleto> prestamosMap = new HashMap<>();
    
    try (Connection conn = ConexionBD.conectar()) {
        // 1. Obtener préstamos básicos
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
        
        // 2. Obtener equipamiento para cada préstamo
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
        
        // 3. Obtener insumos para cada préstamo
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
        JOptionPane.showMessageDialog(this,
            "Error al obtener los datos de préstamos: " + e.getMessage(),
            "Error",
            JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
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

// Método para generar el reporte general de préstamos
/**
 * Genera un reporte general en formato PDF con los datos completos de préstamos del laboratorio
 * Muestra dos reportes por página para optimizar el espacio
 * @param idLaboratorio Identificador del laboratorio
 * @param fechaInicial Fecha de inicio del período a reportar
 * @param fechaFinal Fecha de fin del período a reportar
 */
private void generarReporteGeneralPrestamosPDF(int idLaboratorio, java.sql.Date fechaInicial, java.sql.Date fechaFinal) {
    try {
        // Obtener todos los préstamos completos
        List<PrestamoCompleto> prestamos = obtenerPrestamosCompletos(idLaboratorio, fechaInicial, fechaFinal);
        
        if (prestamos.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "No se encontraron préstamos para el laboratorio seleccionado en el rango de fechas indicado",
                "Información",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Obtener la ruta del directorio actual
        String directorioActual = System.getProperty("user.dir");
        String fechaStr = new SimpleDateFormat("yyyyMMdd").format(new java.util.Date());
        String nombreArchivo = directorioActual + "/ReporteGeneralPrestamos_Lab" + idLaboratorio + "_" + fechaStr + ".pdf";
        
        // Crear el documento PDF
        Document documento = new Document(PageSize.A4);
        PdfWriter writer = PdfWriter.getInstance(documento, new FileOutputStream(nombreArchivo));
        
        // Abrir el documento para escribir
        documento.open();
        
        // Configurar fuentes (com.itextpdf.text.)
        com.itextpdf.text.Font fontTituloPrincipal = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 16, com.itextpdf.text.Font.BOLD);
        com.itextpdf.text.Font fontTituloSecundario = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 14, com.itextpdf.text.Font.BOLD);
        com.itextpdf.text.Font fontSubtitulo = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 12, com.itextpdf.text.Font.BOLD);
        com.itextpdf.text.Font fontTablaEncabezado = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 10, com.itextpdf.text.Font.BOLD, BaseColor.WHITE);
        com.itextpdf.text.Font fontTablaEncabezadoEquip = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 9, com.itextpdf.text.Font.BOLD);
        com.itextpdf.text.Font fontTablaEncabezadoInsumo = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 9, com.itextpdf.text.Font.BOLD);
        com.itextpdf.text.Font fontTablaNormal = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 8);
        
        // Formatear fechas para el título
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String periodoFecha = "Del " + sdf.format(fechaInicial) + " al " + sdf.format(fechaFinal);
        
        int prestamosPerPage = 2; // Número de préstamos por página
        int prestamosCount = 0;
        
        for (int i = 0; i < prestamos.size(); i++) {
            PrestamoCompleto prestamo = prestamos.get(i);
            prestamosCount++;
            
            // Si es el primer préstamo de la página o el primer préstamo en general, agregar el encabezado
            if (prestamosCount == 1 || (prestamosCount - 1) % prestamosPerPage == 0) {
                // Título principal - Universidad
                Paragraph tituloPrincipal = new Paragraph("Universidad Salesiana de Bolivia", fontTituloPrincipal);
                tituloPrincipal.setAlignment(Element.ALIGN_CENTER);
                documento.add(tituloPrincipal);
                
                // Título secundario - Reporte
                Paragraph tituloSecundario = new Paragraph("Reporte General de Préstamos del Laboratorio \"" + idLaboratorio + "\"", fontTituloSecundario);
                tituloSecundario.setAlignment(Element.ALIGN_CENTER);
                documento.add(tituloSecundario);
                
                // Periodo de fechas
                Paragraph periodoFechas = new Paragraph(periodoFecha, fontTituloSecundario);
                periodoFechas.setAlignment(Element.ALIGN_CENTER);
                documento.add(periodoFechas);
                
                documento.add(new Paragraph(" "));
            }
            
            // Título del préstamo
            Paragraph tituloPrestamo = new Paragraph("Préstamo " + (i + 1) + ":", fontSubtitulo);
            documento.add(tituloPrestamo);
            
            // Tabla de datos básicos del préstamo
            PdfPTable tablaPrestamo = new PdfPTable(6);
            tablaPrestamo.setWidthPercentage(100);
            
            // Establecer anchos relativos de columnas
            float[] anchosPrestamo = {1.3f, 1f, 1.2f, 1.5f, 1.2f, 2f};
            tablaPrestamo.setWidths(anchosPrestamo);
            
            // Encabezados de la tabla de préstamo
            String[] titulosPrestamo = {"Fecha", "Hora", "RU Usuario", "RU Administrador", "ID Horario", "Observaciones"};
            BaseColor colorEncabezadoPrestamo = new BaseColor(0, 63, 135);
            
            for (String titulo : titulosPrestamo) {
                PdfPCell celda = new PdfPCell(new Phrase(titulo, fontTablaEncabezado));
                celda.setBackgroundColor(colorEncabezadoPrestamo);
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setPadding(4);
                tablaPrestamo.addCell(celda);
            }
            
            // Datos del préstamo
            PdfPCell celdaFecha = new PdfPCell(new Phrase(sdf.format(prestamo.getFecha()), fontTablaNormal));
            celdaFecha.setHorizontalAlignment(Element.ALIGN_CENTER);
            celdaFecha.setPadding(4);
            tablaPrestamo.addCell(celdaFecha);
            
            PdfPCell celdaHora = new PdfPCell(new Phrase(prestamo.getHora(), fontTablaNormal));
            celdaHora.setHorizontalAlignment(Element.ALIGN_CENTER);
            celdaHora.setPadding(4);
            tablaPrestamo.addCell(celdaHora);
            
            PdfPCell celdaUsuario = new PdfPCell(new Phrase(String.valueOf(prestamo.getRuUsuario()), fontTablaNormal));
            celdaUsuario.setHorizontalAlignment(Element.ALIGN_CENTER);
            celdaUsuario.setPadding(4);
            tablaPrestamo.addCell(celdaUsuario);
            
            String valorAdmin = prestamo.getRuAdministrador() != null ? 
                String.valueOf(prestamo.getRuAdministrador()) : "N/A";
            PdfPCell celdaAdmin = new PdfPCell(new Phrase(valorAdmin, fontTablaNormal));
            celdaAdmin.setHorizontalAlignment(Element.ALIGN_CENTER);
            celdaAdmin.setPadding(4);
            tablaPrestamo.addCell(celdaAdmin);
            
            String valorHorario = prestamo.getIdHorario() != null ? 
                String.valueOf(prestamo.getIdHorario()) : "N/A";
            PdfPCell celdaHorario = new PdfPCell(new Phrase(valorHorario, fontTablaNormal));
            celdaHorario.setHorizontalAlignment(Element.ALIGN_CENTER);
            celdaHorario.setPadding(4);
            tablaPrestamo.addCell(celdaHorario);
            
            String valorObs = prestamo.getObservaciones() != null && !prestamo.getObservaciones().isEmpty() ? 
                prestamo.getObservaciones() : "Ninguna observación";
            PdfPCell celdaObs = new PdfPCell(new Phrase(valorObs, fontTablaNormal));
            celdaObs.setPadding(4);
            tablaPrestamo.addCell(celdaObs);
            
            documento.add(tablaPrestamo);
            documento.add(new com.itextpdf.text.Paragraph(" ", new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 6)));

            
            // Equipamiento
            if (!prestamo.getEquipamientos().isEmpty()) {
                Paragraph tituloEquipamiento = new Paragraph("Equipamiento:", fontSubtitulo);
                documento.add(tituloEquipamiento);
                
                PdfPTable tablaEquipamiento = new PdfPTable(3);
                tablaEquipamiento.setWidthPercentage(100);
                
                // Establecer anchos relativos de columnas para equipamiento
                float[] anchosEquipamiento = {1f, 2f, 1f};
                tablaEquipamiento.setWidths(anchosEquipamiento);
                
                // Encabezados para tabla de equipamiento
                String[] titulosEquipamiento = {"ID Equipamiento", "Nombre", "Estado"};
                BaseColor colorEncabezadoEquip = new BaseColor(153, 204, 255);
                
                for (String titulo : titulosEquipamiento) {
                    PdfPCell celda = new PdfPCell(new Phrase(titulo, fontTablaEncabezadoEquip));
                    celda.setBackgroundColor(colorEncabezadoEquip);
                    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celda.setPadding(3);
                    tablaEquipamiento.addCell(celda);
                }
                
                // Datos de equipamiento
                BaseColor colorFilaAlternaEquip = new BaseColor(240, 240, 255);
                
                for (int j = 0; j < prestamo.getEquipamientos().size(); j++) {
                    DetalleEquipamiento equipamiento = prestamo.getEquipamientos().get(j);
                    BaseColor colorFondo = (j % 2 == 1) ? colorFilaAlternaEquip : BaseColor.WHITE;
                    
                    PdfPCell celdaIdEquip = new PdfPCell(new Phrase(String.valueOf(equipamiento.getIdEquipamiento()), fontTablaNormal));
                    celdaIdEquip.setBackgroundColor(colorFondo);
                    celdaIdEquip.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celdaIdEquip.setPadding(3);
                    tablaEquipamiento.addCell(celdaIdEquip);
                    
                    PdfPCell celdaNombreEquip = new PdfPCell(new Phrase(equipamiento.getNombreEquipamiento(), fontTablaNormal));
                    celdaNombreEquip.setBackgroundColor(colorFondo);
                    celdaNombreEquip.setPadding(3);
                    tablaEquipamiento.addCell(celdaNombreEquip);
                    
                    PdfPCell celdaEstadoEquip = new PdfPCell(new Phrase(equipamiento.getEstado(), fontTablaNormal));
                    celdaEstadoEquip.setBackgroundColor(colorFondo);
                    celdaEstadoEquip.setPadding(3);
                    tablaEquipamiento.addCell(celdaEstadoEquip);
                }
                
                documento.add(tablaEquipamiento);
                documento.add(new com.itextpdf.text.Paragraph(" ", new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 6)));

            }
            
            // Insumos
            if (!prestamo.getInsumos().isEmpty()) {
                Paragraph tituloInsumo = new Paragraph("Insumo:", fontSubtitulo);
                documento.add(tituloInsumo);
                
                PdfPTable tablaInsumo = new PdfPTable(4);
                tablaInsumo.setWidthPercentage(100);
                
                // Establecer anchos relativos de columnas para insumos
                float[] anchosInsumo = {1f, 2f, 1f, 1f};
                tablaInsumo.setWidths(anchosInsumo);
                
                // Encabezados para tabla de insumos
                String[] titulosInsumo = {"ID Insumo", "Nombre", "Cantidad Inicial", "Cantidad Final"};
                BaseColor colorEncabezadoInsumo = new BaseColor(204, 255, 204);
                
                for (String titulo : titulosInsumo) {
                    PdfPCell celda = new PdfPCell(new Phrase(titulo, fontTablaEncabezadoInsumo));
                    celda.setBackgroundColor(colorEncabezadoInsumo);
                    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celda.setPadding(3);
                    tablaInsumo.addCell(celda);
                }
                
                // Datos de insumos
                BaseColor colorFilaAlternaInsumo = new BaseColor(240, 255, 240);
                
                for (int j = 0; j < prestamo.getInsumos().size(); j++) {
                    DetalleInsumo insumo = prestamo.getInsumos().get(j);
                    BaseColor colorFondo = (j % 2 == 1) ? colorFilaAlternaInsumo : BaseColor.WHITE;
                    
                    PdfPCell celdaIdInsumo = new PdfPCell(new Phrase(String.valueOf(insumo.getIdInsumo()), fontTablaNormal));
                    celdaIdInsumo.setBackgroundColor(colorFondo);
                    celdaIdInsumo.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celdaIdInsumo.setPadding(3);
                    tablaInsumo.addCell(celdaIdInsumo);
                    
                    PdfPCell celdaNombreInsumo = new PdfPCell(new Phrase(insumo.getNombreInsumo(), fontTablaNormal));
                    celdaNombreInsumo.setBackgroundColor(colorFondo);
                    celdaNombreInsumo.setPadding(3);
                    tablaInsumo.addCell(celdaNombreInsumo);
                    
                    PdfPCell celdaCantInicial = new PdfPCell(new Phrase(String.valueOf(insumo.getCantidadInicial()), fontTablaNormal));
                    celdaCantInicial.setBackgroundColor(colorFondo);
                    celdaCantInicial.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celdaCantInicial.setPadding(3);
                    tablaInsumo.addCell(celdaCantInicial);
                    
                    String valorCantFinal = insumo.getCantidadFinal() != null ? 
                        String.valueOf(insumo.getCantidadFinal()) : "No devuelto";
                    PdfPCell celdaCantFinal = new PdfPCell(new Phrase(valorCantFinal, fontTablaNormal));
                    celdaCantFinal.setBackgroundColor(colorFondo);
                    celdaCantFinal.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celdaCantFinal.setPadding(3);
                    tablaInsumo.addCell(celdaCantFinal);
                }
                
                documento.add(tablaInsumo);
            }
            
            // Espacio entre préstamos en la misma página
            documento.add(new Paragraph(" "));
            documento.add(new Paragraph(" "));
            
            // Si hemos alcanzado el límite de préstamos por página y no es el último préstamo, añadir una nueva página
            if (prestamosCount % prestamosPerPage == 0 && i < prestamos.size() - 1) {
                documento.newPage();
                prestamosCount = 0;
            }
        }
        
        // Cerrar el documento
        documento.close();
        
        // Abrir el documento con la aplicación predeterminada
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(new java.io.File(nombreArchivo));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "El reporte se guardó correctamente pero no se pudo abrir automáticamente. Ubicación: " + nombreArchivo,
                "Información",
                JOptionPane.INFORMATION_MESSAGE);
        }
        
        JOptionPane.showMessageDialog(this,
            "Reporte general PDF generado correctamente.",
            "Éxito",
            JOptionPane.INFORMATION_MESSAGE);
        
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
            "Error al generar el reporte general de préstamos: " + e.getMessage(),
            "Error",
            JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}

    //SECCIÓN REFERENTE A REPORTE GENERAL DE PRÉSTAMOS (FINAL)

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

    private List<ReportePrestamo> obtenerPrestamosLaboratorio(int idLaboratorio, java.sql.Date fechaInicial, java.sql.Date fechaFinal) {
        List<ReportePrestamo> prestamos = new ArrayList<>();

        try {
            // Consulta SQL para obtener préstamos asociados a horarios del laboratorio seleccionado
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
            JOptionPane.showMessageDialog(this,
                "Error al obtener los préstamos: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        return prestamos;
    }

    /**
 * Genera un reporte en formato PDF con los datos de préstamos del laboratorio
 * @param prestamos Lista de préstamos a incluir en el reporte
 * @param idLaboratorio Identificador del laboratorio
 */
private void generarDocumentoPDF(List<ReportePrestamo> prestamos, int idLaboratorio) {
    try {
        // Obtener la ruta del directorio actual
        String directorioActual = System.getProperty("user.dir");
        String nombreArchivo = directorioActual + "/ReportePrestamos_Lab" + idLaboratorio + ".pdf";
        
        // Crear el documento PDF
        Document documento = new Document(PageSize.A4);
        documento.setMargins(72, 72, 72, 72); // Márgenes de 1 pulgada (72 puntos)
        
        // Crear el escritor PDF
        PdfWriter writer = PdfWriter.getInstance(documento, new FileOutputStream(nombreArchivo));
        
        // Abrir el documento para escribir
        documento.open();
        
        // Configurar fuentes
        com.itextpdf.text.Font fontTituloPrincipal = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 18, com.itextpdf.text.Font.BOLD);
        com.itextpdf.text.Font fontTituloSecundario = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 16, com.itextpdf.text.Font.BOLD);
        com.itextpdf.text.Font fontSubtitulo = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 14, com.itextpdf.text.Font.BOLD);
        com.itextpdf.text.Font fontTablaEncabezado = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 12, com.itextpdf.text.Font.BOLD, BaseColor.WHITE);
        com.itextpdf.text.Font fontTablaNormal = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 10);
        
        // Obtener fechas para el título
        java.sql.Date fechaInicial = null;
        java.sql.Date fechaFinal = null;
        
        if (!prestamos.isEmpty()) {
            // Si hay préstamos, usar la primera fecha como referencia inicial
            fechaInicial = prestamos.get(0).getFecha();
            fechaFinal = prestamos.get(prestamos.size() - 1).getFecha();
        } else {
            // Si no hay préstamos, usar la fecha actual
            fechaInicial = new java.sql.Date(System.currentTimeMillis());
            fechaFinal = new java.sql.Date(System.currentTimeMillis());
        }
        
        // Formatear fechas para el título
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String periodoFecha = "DEL " + sdf.format(fechaInicial) + " AL " + sdf.format(fechaFinal);
        
        // Título principal - Universidad
        Paragraph tituloPrincipal = new Paragraph("UNIVERSIDAD SALESIANA DE BOLIVIA", fontTituloPrincipal);
        tituloPrincipal.setAlignment(Element.ALIGN_CENTER);
        documento.add(tituloPrincipal);
        
        // Título secundario - Reporte
        Paragraph tituloSecundario = new Paragraph("REPORTE DE PRÉSTAMOS DE LABORATORIO " + idLaboratorio, fontTituloSecundario);
        tituloSecundario.setAlignment(Element.ALIGN_CENTER);
        documento.add(tituloSecundario);
        
        // Periodo de fechas
        Paragraph periodoFechas = new Paragraph(periodoFecha, fontTituloSecundario);
        periodoFechas.setAlignment(Element.ALIGN_CENTER);
        documento.add(periodoFechas);
        
        // Espacio después de los títulos
        documento.add(new Paragraph(" "));
        
        // Subtítulo - Préstamos
        Paragraph subtitulo = new Paragraph("PRÉSTAMOS REALIZADOS", fontSubtitulo);
        subtitulo.setAlignment(Element.ALIGN_CENTER);
        documento.add(subtitulo);
        
        documento.add(new Paragraph(" "));
        
        // Crear tabla
        PdfPTable tabla = new PdfPTable(6); // 6 columnas
        tabla.setWidthPercentage(100); // Ancho completo de la página
        
        // Establecer el ancho relativo de las columnas
        float[] anchos = {1.5f, 1f, 1.5f, 2f, 1.5f, 2.5f};
        tabla.setWidths(anchos);
        
        // Añadir encabezados de tabla
        String[] titulos = {"Fecha", "Hora", "RU Usuario", "RU Administrador", "ID Horario", "Observaciones"};
        
        // Color de fondo del encabezado (azul oscuro)
        BaseColor colorEncabezado = new BaseColor(0, 63, 135);
        
        for (String titulo : titulos) {
            PdfPCell celda = new PdfPCell(new Phrase(titulo, fontTablaEncabezado));
            celda.setBackgroundColor(colorEncabezado);
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setPadding(5);
            tabla.addCell(celda);
        }
        
        // Color para filas alternas
        BaseColor colorFilaAlterna = new BaseColor(240, 240, 255);
        
        // Llenar la tabla con los datos
        for (int i = 0; i < prestamos.size(); i++) {
            ReportePrestamo prestamo = prestamos.get(i);
            
            // Definir el color de fondo para filas alternas
            BaseColor colorFondo = (i % 2 == 1) ? colorFilaAlterna : BaseColor.WHITE;
            
            // Fecha
            PdfPCell celdaFecha = new PdfPCell(new Phrase(sdf.format(prestamo.getFecha()), fontTablaNormal));
            celdaFecha.setBackgroundColor(colorFondo);
            celdaFecha.setHorizontalAlignment(Element.ALIGN_CENTER);
            celdaFecha.setPadding(5);
            tabla.addCell(celdaFecha);
            
            // Hora
            PdfPCell celdaHora = new PdfPCell(new Phrase(prestamo.getHora(), fontTablaNormal));
            celdaHora.setBackgroundColor(colorFondo);
            celdaHora.setHorizontalAlignment(Element.ALIGN_CENTER);
            celdaHora.setPadding(5);
            tabla.addCell(celdaHora);
            
            // RU Usuario
            PdfPCell celdaUsuario = new PdfPCell(new Phrase(String.valueOf(prestamo.getRuUsuario()), fontTablaNormal));
            celdaUsuario.setBackgroundColor(colorFondo);
            celdaUsuario.setHorizontalAlignment(Element.ALIGN_CENTER);
            celdaUsuario.setPadding(5);
            tabla.addCell(celdaUsuario);
            
            // RU Administrador
            String valorAdmin = prestamo.getRuAdministrador() != null ? 
                String.valueOf(prestamo.getRuAdministrador()) : "N/A";
            PdfPCell celdaAdmin = new PdfPCell(new Phrase(valorAdmin, fontTablaNormal));
            celdaAdmin.setBackgroundColor(colorFondo);
            celdaAdmin.setHorizontalAlignment(Element.ALIGN_CENTER);
            celdaAdmin.setPadding(5);
            tabla.addCell(celdaAdmin);
            
            // ID Horario
            String valorHorario = prestamo.getIdHorario() != null ? 
                String.valueOf(prestamo.getIdHorario()) : "N/A";
            PdfPCell celdaHorario = new PdfPCell(new Phrase(valorHorario, fontTablaNormal));
            celdaHorario.setBackgroundColor(colorFondo);
            celdaHorario.setHorizontalAlignment(Element.ALIGN_CENTER);
            celdaHorario.setPadding(5);
            tabla.addCell(celdaHorario);
            
            // Observaciones
            String valorObs = prestamo.getObservaciones() != null && !prestamo.getObservaciones().isEmpty() ? 
                prestamo.getObservaciones() : "Ninguna";
            PdfPCell celdaObs = new PdfPCell(new Phrase(valorObs, fontTablaNormal));
            celdaObs.setBackgroundColor(colorFondo);
            celdaObs.setPadding(5);
            tabla.addCell(celdaObs);
        }
        
        // Si no hay datos, agregar una fila indicándolo
        if (prestamos.isEmpty()) {
            PdfPCell celdaNoData = new PdfPCell(new Phrase("No hay préstamos registrados en este período", fontTablaNormal));
            celdaNoData.setColspan(6);
            celdaNoData.setHorizontalAlignment(Element.ALIGN_CENTER);
            celdaNoData.setPadding(10);
            tabla.addCell(celdaNoData);
        }
        
        // Añadir la tabla al documento
        documento.add(tabla);
        
        // Cerrar el documento
        documento.close();
        
        // Abrir el documento con la aplicación predeterminada
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(new java.io.File(nombreArchivo));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "El reporte se guardó correctamente pero no se pudo abrir automáticamente. Ubicación: " + nombreArchivo,
                "Información",
                JOptionPane.INFORMATION_MESSAGE);
        }
        
        JOptionPane.showMessageDialog(this,
            "Reporte PDF generado correctamente.",
            "Éxito",
            JOptionPane.INFORMATION_MESSAGE);
        
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
            "Error al generar el documento PDF: " + e.getMessage(),
            "Error",
            JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}
}