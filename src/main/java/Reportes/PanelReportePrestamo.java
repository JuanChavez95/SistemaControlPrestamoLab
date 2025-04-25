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
        comboTipoReporte.addItem("Reporte de insumos");
        comboTipoReporte.addItem("Reporte de equipamiento");
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
            generarDocumentoWord(prestamos, idLaboratorio);

        } else if ("Reporte de equipamiento".equals(tipoReporte)) {
            // Generar reporte de equipamiento
            List<ReporteEquipamiento> equipamientos = obtenerEquipamientosPrestados(fechaInicial, fechaFinal);

            if (equipamientos.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "No se encontraron préstamos de equipamiento en el rango de fechas indicado",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Generar el documento Word para reporte de equipamiento
            generarDocumentoWordEquipamiento(equipamientos, fechaInicial, fechaFinal);

        } else {
            JOptionPane.showMessageDialog(this,
                "Esta funcionalidad está en desarrollo. Por favor seleccione 'Reporte de préstamos por laboratorio' o 'Reporte de equipamiento'",
                "Información",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this,
            "Reporte generado exitosamente",
            "Éxito",
            JOptionPane.INFORMATION_MESSAGE);

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
    //TODO LO REFERENTE A EQUIPAMIENTO INICIO
    // Clase interna para manejar los datos del reporte de equipamiento
private class ReporteEquipamiento {
    private int idEquipamiento;
    private String nombreEquipamiento;
    private int idPrestamo;
    private int idDetalle;
    private String estado;
    private Integer idLaboratorio;

    public ReporteEquipamiento(int idEquipamiento, String nombreEquipamiento, int idPrestamo, 
                              int idDetalle, String estado, Integer idLaboratorio) {
        this.idEquipamiento = idEquipamiento;
        this.nombreEquipamiento = nombreEquipamiento;
        this.idPrestamo = idPrestamo;
        this.idDetalle = idDetalle;
        this.estado = estado;
        this.idLaboratorio = idLaboratorio;
    }

    public int getIdEquipamiento() {
        return idEquipamiento;
    }

    public String getNombreEquipamiento() {
        return nombreEquipamiento;
    }

    public int getIdPrestamo() {
        return idPrestamo;
    }

    public int getIdDetalle() {
        return idDetalle;
    }

    public String getEstado() {
        return estado;
    }

    public Integer getIdLaboratorio() {
        return idLaboratorio;
    }
}

// Método para obtener los equipamientos prestados en un rango de fechas
private List<ReporteEquipamiento> obtenerEquipamientosPrestados(java.sql.Date fechaInicial, java.sql.Date fechaFinal) {
    List<ReporteEquipamiento> reporteEquipamientos = new ArrayList<>();
    
    try {
        // Consulta SQL para obtener los detalles de préstamos de equipamiento en el rango de fechas
        String sql = "SELECT dpe.id_detalle, dpe.id_prestamo, dpe.id_equipamiento, " +
                     "e.nombre_equipamiento, e.estado, p.id_horario " +
                     "FROM detalle_prestamo_equipamiento dpe " +
                     "JOIN prestamo p ON dpe.id_prestamo = p.id_prestamo " +
                     "JOIN equipamiento e ON dpe.id_equipamiento = e.id_equipamiento " +
                     "WHERE p.fecha_prestamo BETWEEN ? AND ? " +
                     "ORDER BY e.id_equipamiento, p.fecha_prestamo";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, fechaInicial);
            stmt.setDate(2, fechaFinal);

            try (ResultSet rs = stmt.executeQuery()) {
                // Mapa para almacenar los ID de horario y su laboratorio asociado
                Map<Integer, Integer> horarioLaboratorioMap = new HashMap<>();
                
                while (rs.next()) {
                    int idPrestamo = rs.getInt("id_prestamo");
                    int idEquipamiento = rs.getInt("id_equipamiento");
                    int idDetalle = rs.getInt("id_detalle");
                    String nombreEquipamiento = rs.getString("nombre_equipamiento");
                    String estado = rs.getString("estado");
                    Integer idHorario = rs.getObject("id_horario") != null ? rs.getInt("id_horario") : null;
                    
                    // Obtener el ID del laboratorio asociado al horario
                    Integer idLaboratorio = null;
                    if (idHorario != null) {
                        // Verificar si ya tenemos el ID del laboratorio para este horario
                        if (horarioLaboratorioMap.containsKey(idHorario)) {
                            idLaboratorio = horarioLaboratorioMap.get(idHorario);
                        } else {
                            // Consultar el ID del laboratorio para este horario
                            try {
                                String sqlHorario = "SELECT id_laboratorio FROM horario WHERE id_horario = ?";
                                try (PreparedStatement stmtHorario = conn.prepareStatement(sqlHorario)) {
                                    stmtHorario.setInt(1, idHorario);
                                    try (ResultSet rsHorario = stmtHorario.executeQuery()) {
                                        if (rsHorario.next()) {
                                            idLaboratorio = rsHorario.getInt("id_laboratorio");
                                            // Guardar en el mapa para futuras referencias
                                            horarioLaboratorioMap.put(idHorario, idLaboratorio);
                                        }
                                    }
                                }
                            } catch (SQLException e) {
                                System.err.println("Error al obtener el laboratorio del horario: " + e.getMessage());
                            }
                        }
                    }
                    
                    reporteEquipamientos.add(new ReporteEquipamiento(
                        idEquipamiento,
                        nombreEquipamiento,
                        idPrestamo,
                        idDetalle,
                        estado,
                        idLaboratorio
                    ));
                }
            }
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this,
            "Error al obtener los datos de equipamiento: " + e.getMessage(),
            "Error",
            JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
    
    return reporteEquipamientos;
}

// Método para generar el documento Word con el reporte de equipamiento
private void generarDocumentoWordEquipamiento(List<ReporteEquipamiento> equipamientos, 
                                               java.sql.Date fechaInicial, 
                                               java.sql.Date fechaFinal) {
    try {
        // Crear un nuevo documento
        Document documento = new Document();
        
        // Obtener la sección y el formato de la página
        Section seccion = documento.addSection();
        seccion.getPageSetup().setMargins(new MarginsF(72f, 72f, 72f, 72f)); // Márgenes de 1 pulgada
        
        // Crear el título principal con formato centrado
        Paragraph titulo1 = seccion.addParagraph();
        titulo1.getFormat().setHorizontalAlignment(HorizontalAlignment.Center);
        TextRange textoTitulo1 = titulo1.appendText("Universidad Salesiana de Bolivia");
        textoTitulo1.getCharacterFormat().setFontName("Arial");
        textoTitulo1.getCharacterFormat().setFontSize(18);
        textoTitulo1.getCharacterFormat().setBold(true);
        
        // Agregar el segundo título
        Paragraph titulo2 = seccion.addParagraph();
        titulo2.getFormat().setHorizontalAlignment(HorizontalAlignment.Center);
        TextRange textoTitulo2 = titulo2.appendText("Reporte de Préstamos de Equipamiento");
        textoTitulo2.getCharacterFormat().setFontName("Arial");
        textoTitulo2.getCharacterFormat().setFontSize(16);
        textoTitulo2.getCharacterFormat().setBold(true);
        
        // Agregar el rango de fechas
        Paragraph titulo3 = seccion.addParagraph();
        titulo3.getFormat().setHorizontalAlignment(HorizontalAlignment.Center);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String rangoFechas = "Del " + dateFormat.format(fechaInicial) + " al " + dateFormat.format(fechaFinal);
        TextRange textoTitulo3 = titulo3.appendText(rangoFechas);
        textoTitulo3.getCharacterFormat().setFontName("Arial");
        textoTitulo3.getCharacterFormat().setFontSize(14);
        textoTitulo3.getCharacterFormat().setBold(true);
        
        // Agregar espacio
        seccion.addParagraph();
        
        // Agregar subtítulo
        Paragraph subtitulo = seccion.addParagraph();
        subtitulo.getFormat().setHorizontalAlignment(HorizontalAlignment.Left);
        TextRange textoSubtitulo = subtitulo.appendText("PRÉSTAMOS DE EQUIPAMIENTO:");
        textoSubtitulo.getCharacterFormat().setFontName("Arial");
        textoSubtitulo.getCharacterFormat().setFontSize(14);
        textoSubtitulo.getCharacterFormat().setBold(true);
        
        // Crear la tabla de equipamientos
        Table tabla = seccion.addTable(true);
        tabla.resetCells(equipamientos.size() + 1, 6); // Filas + 1 para el encabezado, 6 columnas
        
        // Establecer el ancho de las columnas
        tabla.autoFit(AutoFitBehaviorType.Auto_Fit_To_Contents);
        
        // Estilos para la tabla
        TableRow encabezado = tabla.getRows().get(0);
        String[] titulos = {"ID Equipamiento", "Nombre", "ID Préstamo", "ID Detalle", "Estado", "ID Laboratorio"};
        
        for (int i = 0; i < titulos.length; i++) {
            encabezado.getCells().get(i).getCellFormat().setBackColor(new Color(0, 63, 135));
            TextRange textoEncabezado = encabezado.getCells().get(i).addParagraph().appendText(titulos[i]);
            textoEncabezado.getCharacterFormat().setFontName("Arial");
            textoEncabezado.getCharacterFormat().setFontSize(12);
            textoEncabezado.getCharacterFormat().setBold(true);
            textoEncabezado.getCharacterFormat().setTextColor(Color.WHITE);
        }
        
        // Llenar la tabla con los datos
        for (int i = 0; i < equipamientos.size(); i++) {
            ReporteEquipamiento equipamiento = equipamientos.get(i);
            TableRow fila = tabla.getRows().get(i + 1);
            
            // Agregar datos a cada celda
            fila.getCells().get(0).addParagraph().appendText(String.valueOf(equipamiento.getIdEquipamiento()));
            fila.getCells().get(1).addParagraph().appendText(equipamiento.getNombreEquipamiento());
            fila.getCells().get(2).addParagraph().appendText(String.valueOf(equipamiento.getIdPrestamo()));
            fila.getCells().get(3).addParagraph().appendText(String.valueOf(equipamiento.getIdDetalle()));
            fila.getCells().get(4).addParagraph().appendText(equipamiento.getEstado());
            fila.getCells().get(5).addParagraph().appendText(equipamiento.getIdLaboratorio() != null ? 
                String.valueOf(equipamiento.getIdLaboratorio()) : "N/A");
            
            // Alternar colores de fondo para las filas (para mejor legibilidad)
            if (i % 2 == 1) {
                for (int j = 0; j < 6; j++) {
                    fila.getCells().get(j).getCellFormat().setBackColor(new Color(240, 240, 255));
                }
            }
        }
        
        // Guardar el documento
        // Obtener la ruta del directorio actual
        String directorioActual = System.getProperty("user.dir");
        String fechaStr = new SimpleDateFormat("yyyyMMdd").format(new java.util.Date());
        String nombreArchivo = directorioActual + "/ReporteEquipamiento_" + fechaStr + ".docx";
        documento.saveToFile(nombreArchivo, FileFormat.Docx);
        
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
        
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
            "Error al generar el documento Word: " + e.getMessage(),
            "Error",
            JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}
    //TODO LO REFERENTE A EQUIPAMIENTO FINAL

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

    private void generarDocumentoWord(List<ReportePrestamo> prestamos, int idLaboratorio) {
        try {
            // Crear un nuevo documento
            Document documento = new Document();
            
            // Obtener la sección y el formato de la página
            Section seccion = documento.addSection();
            seccion.getPageSetup().setMargins(new MarginsF(72f, 72f, 72f, 72f)); // Márgenes de 1 pulgada
            
            // Crear el título principal con formato centrado
            Paragraph titulo1 = seccion.addParagraph();
            titulo1.getFormat().setHorizontalAlignment(HorizontalAlignment.Center);
            TextRange textoTitulo1 = titulo1.appendText("Universidad Salesiana de Bolivia");
            textoTitulo1.getCharacterFormat().setFontName("Arial");
            textoTitulo1.getCharacterFormat().setFontSize(18);
            textoTitulo1.getCharacterFormat().setBold(true);
            
            // Agregar el segundo título
            Paragraph titulo2 = seccion.addParagraph();
            titulo2.getFormat().setHorizontalAlignment(HorizontalAlignment.Center);
            TextRange textoTitulo2 = titulo2.appendText("Reporte de Préstamos de Laboratorio \"" + idLaboratorio + "\"");
            textoTitulo2.getCharacterFormat().setFontName("Arial");
            textoTitulo2.getCharacterFormat().setFontSize(16);
            textoTitulo2.getCharacterFormat().setBold(true);
            
            // Agregar espacio
            seccion.addParagraph();
            
            // Agregar subtítulo
            Paragraph subtitulo = seccion.addParagraph();
            subtitulo.getFormat().setHorizontalAlignment(HorizontalAlignment.Left);
            TextRange textoSubtitulo = subtitulo.appendText("PRÉSTAMOS:");
            textoSubtitulo.getCharacterFormat().setFontName("Arial");
            textoSubtitulo.getCharacterFormat().setFontSize(14);
            textoSubtitulo.getCharacterFormat().setBold(true);
            
            // Crear la tabla de préstamos
            Table tabla = seccion.addTable(true);
            tabla.resetCells(prestamos.size() + 1, 6); // Filas + 1 para el encabezado, 6 columnas (añadida columna de observaciones)
            
            // Establecer el ancho de las columnas
            tabla.autoFit(AutoFitBehaviorType.Auto_Fit_To_Contents);
            
            // Estilos para la tabla
            TableRow encabezado = tabla.getRows().get(0);
            String[] titulos = {"Fecha", "Hora", "RU Usuario", "RU Administrador", "ID Horario", "Observaciones"};
            
            for (int i = 0; i < titulos.length; i++) {
                encabezado.getCells().get(i).getCellFormat().setBackColor(new Color(0, 63, 135));
                TextRange textoEncabezado = encabezado.getCells().get(i).addParagraph().appendText(titulos[i]);
                textoEncabezado.getCharacterFormat().setFontName("Arial");
                textoEncabezado.getCharacterFormat().setFontSize(12);
                textoEncabezado.getCharacterFormat().setBold(true);
                textoEncabezado.getCharacterFormat().setTextColor(Color.WHITE);
            }
            
            // Llenar la tabla con los datos
            for (int i = 0; i < prestamos.size(); i++) {
                ReportePrestamo prestamo = prestamos.get(i);
                TableRow fila = tabla.getRows().get(i + 1);
                
                // Agregar datos a cada celda
                fila.getCells().get(0).addParagraph().appendText(prestamo.getFecha().toString());
                fila.getCells().get(1).addParagraph().appendText(prestamo.getHora());
                fila.getCells().get(2).addParagraph().appendText(String.valueOf(prestamo.getRuUsuario()));
                fila.getCells().get(3).addParagraph().appendText(prestamo.getRuAdministrador() != null ? 
                    String.valueOf(prestamo.getRuAdministrador()) : "N/A");
                fila.getCells().get(4).addParagraph().appendText(prestamo.getIdHorario() != null ? 
                    String.valueOf(prestamo.getIdHorario()) : "N/A");
                fila.getCells().get(5).addParagraph().appendText(prestamo.getObservaciones() != null ? 
                    prestamo.getObservaciones() : "Sin observaciones");
                
                // Alternar colores de fondo para las filas (para mejor legibilidad)
                if (i % 2 == 1) {
                    for (int j = 0; j < 6; j++) {
                        fila.getCells().get(j).getCellFormat().setBackColor(new Color(240, 240, 255));
                    }
                }
            }
            
            // Guardar el documento
            // Obtener la ruta del directorio actual
            String directorioActual = System.getProperty("user.dir");
            String nombreArchivo = directorioActual + "/ReportePrestamos_Lab" + idLaboratorio + ".docx";
            documento.saveToFile(nombreArchivo, FileFormat.Docx);
            
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
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al generar el documento Word: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}