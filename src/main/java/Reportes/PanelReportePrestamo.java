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
            
        } else if ("Reporte de insumos".equals(tipoReporte)) {
            // Generar reporte de insumos
            List<ReporteInsumo> insumos = obtenerInsumosPrestados(fechaInicial, fechaFinal);

            if (insumos.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "No se encontraron préstamos de insumos en el rango de fechas indicado",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Generar el documento Word para reporte de insumos
            generarDocumentoWordInsumos(insumos, fechaInicial, fechaFinal);

        } else if ("Reporte general de préstamos".equals(tipoReporte)) {
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
            generarReporteGeneralPrestamos(idLaboratorio, fechaInicial, fechaFinal);
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
private void generarReporteGeneralPrestamos(int idLaboratorio, java.sql.Date fechaInicial, java.sql.Date fechaFinal) {
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
        TextRange textoTitulo2 = titulo2.appendText("Reporte General de Préstamos del Laboratorio \"" + idLaboratorio + "\"");
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
        
        // Procesar cada préstamo
        int contador = 1;
        for (PrestamoCompleto prestamo : prestamos) {
            // Agregar título del préstamo
            Paragraph tituloPrestamo = seccion.addParagraph();
            tituloPrestamo.getFormat().setHorizontalAlignment(HorizontalAlignment.Left);
            TextRange textoTituloPrestamo = tituloPrestamo.appendText("Préstamo " + contador + ":");
            textoTituloPrestamo.getCharacterFormat().setFontName("Arial");
            textoTituloPrestamo.getCharacterFormat().setFontSize(14);
            textoTituloPrestamo.getCharacterFormat().setBold(true);
            
            // Crear tabla de detalles del préstamo
            Table tablaPrestamo = seccion.addTable(true);
            tablaPrestamo.resetCells(2, 6); // 2 filas (encabezado y datos), 6 columnas
            
            // Encabezado de la tabla de préstamo
            TableRow encabezadoPrestamo = tablaPrestamo.getRows().get(0);
            String[] titulosPrestamo = {"Fecha", "Hora", "RU Usuario", "RU Administrador", "ID Horario", "Observaciones"};
            
            for (int i = 0; i < titulosPrestamo.length; i++) {
                encabezadoPrestamo.getCells().get(i).getCellFormat().setBackColor(new Color(0, 63, 135));
                TextRange textoEncabezado = encabezadoPrestamo.getCells().get(i).addParagraph().appendText(titulosPrestamo[i]);
                textoEncabezado.getCharacterFormat().setFontName("Arial");
                textoEncabezado.getCharacterFormat().setFontSize(12);
                textoEncabezado.getCharacterFormat().setBold(true);
                textoEncabezado.getCharacterFormat().setTextColor(Color.WHITE);
            }
            
            // Datos del préstamo
            TableRow filaPrestamo = tablaPrestamo.getRows().get(1);
            filaPrestamo.getCells().get(0).addParagraph().appendText(dateFormat.format(prestamo.getFecha()));
            filaPrestamo.getCells().get(1).addParagraph().appendText(prestamo.getHora());
            filaPrestamo.getCells().get(2).addParagraph().appendText(String.valueOf(prestamo.getRuUsuario()));
            filaPrestamo.getCells().get(3).addParagraph().appendText(prestamo.getRuAdministrador() != null ? 
                String.valueOf(prestamo.getRuAdministrador()) : "N/A");
            filaPrestamo.getCells().get(4).addParagraph().appendText(prestamo.getIdHorario() != null ? 
                String.valueOf(prestamo.getIdHorario()) : "N/A");
            filaPrestamo.getCells().get(5).addParagraph().appendText(prestamo.getObservaciones() != null ? 
                prestamo.getObservaciones() : "Sin observaciones");
            
            // Espacio después de la tabla de préstamo
            seccion.addParagraph();
            
            // Agregar sección de equipamiento si hay equipamiento en este préstamo
            if (!prestamo.getEquipamientos().isEmpty()) {
                Paragraph tituloEquipamiento = seccion.addParagraph();
                tituloEquipamiento.getFormat().setHorizontalAlignment(HorizontalAlignment.Left);
                TextRange textoTituloEquipamiento = tituloEquipamiento.appendText("Equipamiento:");
                textoTituloEquipamiento.getCharacterFormat().setFontName("Arial");
                textoTituloEquipamiento.getCharacterFormat().setFontSize(12);
                textoTituloEquipamiento.getCharacterFormat().setBold(true);
                
                // Crear tabla de equipamiento
                Table tablaEquipamiento = seccion.addTable(true);
                tablaEquipamiento.resetCells(prestamo.getEquipamientos().size() + 1, 3); // filas + 1 para encabezado, 3 columnas
                
                // Encabezado de la tabla de equipamiento
                TableRow encabezadoEquipamiento = tablaEquipamiento.getRows().get(0);
                String[] titulosEquipamiento = {"ID Equipamiento", "Nombre", "Estado"};
                
                for (int i = 0; i < titulosEquipamiento.length; i++) {
                    encabezadoEquipamiento.getCells().get(i).getCellFormat().setBackColor(new Color(153, 204, 255));
                    TextRange textoEncabezado = encabezadoEquipamiento.getCells().get(i).addParagraph().appendText(titulosEquipamiento[i]);
                    textoEncabezado.getCharacterFormat().setFontName("Arial");
                    textoEncabezado.getCharacterFormat().setFontSize(11);
                    textoEncabezado.getCharacterFormat().setBold(true);
                }
                
                // Datos de equipamiento
                for (int i = 0; i < prestamo.getEquipamientos().size(); i++) {
                    DetalleEquipamiento equipamiento = prestamo.getEquipamientos().get(i);
                    TableRow filaEquipamiento = tablaEquipamiento.getRows().get(i + 1);
                    
                    filaEquipamiento.getCells().get(0).addParagraph().appendText(String.valueOf(equipamiento.getIdEquipamiento()));
                    filaEquipamiento.getCells().get(1).addParagraph().appendText(equipamiento.getNombreEquipamiento());
                    filaEquipamiento.getCells().get(2).addParagraph().appendText(equipamiento.getEstado());
                    
                    // Alternar colores de fondo para las filas
                    if (i % 2 == 1) {
                        for (int j = 0; j < 3; j++) {
                            filaEquipamiento.getCells().get(j).getCellFormat().setBackColor(new Color(240, 240, 255));
                        }
                    }
                }
                
                // Espacio después de la tabla de equipamiento
                seccion.addParagraph();
            }
            
            // Agregar sección de insumos si hay insumos en este préstamo
            if (!prestamo.getInsumos().isEmpty()) {
                Paragraph tituloInsumo = seccion.addParagraph();
                tituloInsumo.getFormat().setHorizontalAlignment(HorizontalAlignment.Left);
                TextRange textoTituloInsumo = tituloInsumo.appendText("Insumo:");
                textoTituloInsumo.getCharacterFormat().setFontName("Arial");
                textoTituloInsumo.getCharacterFormat().setFontSize(12);
                textoTituloInsumo.getCharacterFormat().setBold(true);
                
                // Crear tabla de insumos
                Table tablaInsumo = seccion.addTable(true);
                tablaInsumo.resetCells(prestamo.getInsumos().size() + 1, 4); // filas + 1 para encabezado, 4 columnas
                
                // Encabezado de la tabla de insumos
                TableRow encabezadoInsumo = tablaInsumo.getRows().get(0);
                String[] titulosInsumo = {"ID Insumo", "Nombre", "Cantidad Inicial", "Cantidad Final"};
                
                for (int i = 0; i < titulosInsumo.length; i++) {
                    encabezadoInsumo.getCells().get(i).getCellFormat().setBackColor(new Color(204, 255, 204));
                    TextRange textoEncabezado = encabezadoInsumo.getCells().get(i).addParagraph().appendText(titulosInsumo[i]);
                    textoEncabezado.getCharacterFormat().setFontName("Arial");
                    textoEncabezado.getCharacterFormat().setFontSize(11);
                    textoEncabezado.getCharacterFormat().setBold(true);
                }
                
                // Datos de insumos
                for (int i = 0; i < prestamo.getInsumos().size(); i++) {
                    DetalleInsumo insumo = prestamo.getInsumos().get(i);
                    TableRow filaInsumo = tablaInsumo.getRows().get(i + 1);
                    
                    filaInsumo.getCells().get(0).addParagraph().appendText(String.valueOf(insumo.getIdInsumo()));
                    filaInsumo.getCells().get(1).addParagraph().appendText(insumo.getNombreInsumo());
                    filaInsumo.getCells().get(2).addParagraph().appendText(String.valueOf(insumo.getCantidadInicial()));
                    filaInsumo.getCells().get(3).addParagraph().appendText(insumo.getCantidadFinal() != null ? 
                        String.valueOf(insumo.getCantidadFinal()) : "No devuelto");
                    
                    // Alternar colores de fondo para las filas
                    if (i % 2 == 1) {
                        for (int j = 0; j < 4; j++) {
                            filaInsumo.getCells().get(j).getCellFormat().setBackColor(new Color(240, 255, 240));
                        }
                    }
                }
            }
            
            // Espacio entre préstamos
            seccion.addParagraph();
            seccion.addParagraph();
            
            contador++;
        }
        
        // Guardar el documento
        String directorioActual = System.getProperty("user.dir");
        String fechaStr = new SimpleDateFormat("yyyyMMdd").format(new java.util.Date());
        String nombreArchivo = directorioActual + "/ReporteGeneralPrestamos_Lab" + idLaboratorio + "_" + fechaStr + ".docx";
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
            "Error al generar el reporte general de préstamos: " + e.getMessage(),
            "Error",
            JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}

    //SECCIÓN REFERENTE A REPORTE GENERAL DE PRÉSTAMOS (FINAL)
    
    //SECCIÓN REFERENTE A LOS INSUMOS INICIO
    // Clase interna para manejar los datos del reporte de insumos
private class ReporteInsumo {
    private int idInsumo;
    private String nombreInsumo;
    private int idPrestamo;
    private java.sql.Date fechaPrestamo;
    private int cantidadInicial;
    private Integer cantidadFinal;
    private Integer idLaboratorio;

    public ReporteInsumo(int idInsumo, String nombreInsumo, int idPrestamo, 
                         java.sql.Date fechaPrestamo, int cantidadInicial, 
                         Integer cantidadFinal, Integer idLaboratorio) {
        this.idInsumo = idInsumo;
        this.nombreInsumo = nombreInsumo;
        this.idPrestamo = idPrestamo;
        this.fechaPrestamo = fechaPrestamo;
        this.cantidadInicial = cantidadInicial;
        this.cantidadFinal = cantidadFinal;
        this.idLaboratorio = idLaboratorio;
    }

    public int getIdInsumo() {
        return idInsumo;
    }

    public String getNombreInsumo() {
        return nombreInsumo;
    }

    public int getIdPrestamo() {
        return idPrestamo;
    }

    public java.sql.Date getFechaPrestamo() {
        return fechaPrestamo;
    }

    public int getCantidadInicial() {
        return cantidadInicial;
    }

    public Integer getCantidadFinal() {
        return cantidadFinal;
    }

    public Integer getIdLaboratorio() {
        return idLaboratorio;
    }
}

// Método para obtener los insumos prestados en un rango de fechas
private List<ReporteInsumo> obtenerInsumosPrestados(java.sql.Date fechaInicial, java.sql.Date fechaFinal) {
    List<ReporteInsumo> reporteInsumos = new ArrayList<>();
    
    try {
        // Consulta SQL para obtener los detalles de préstamos de insumos en el rango de fechas
        String sql = "SELECT dpi.id_detalle_insumo, dpi.id_prestamo, dpi.id_insumo, " +
                     "i.nombre_insumo, p.fecha_prestamo, dpi.cantidad_inicial, dpi.cantidad_final, " +
                     "h.id_laboratorio " +
                     "FROM detalle_prestamo_insumo dpi " +
                     "JOIN prestamo p ON dpi.id_prestamo = p.id_prestamo " +
                     "JOIN insumos i ON dpi.id_insumo = i.id_insumo " +
                     "LEFT JOIN horario h ON p.id_horario = h.id_horario " +
                     "WHERE p.fecha_prestamo BETWEEN ? AND ? " +
                     "ORDER BY i.id_insumo, p.fecha_prestamo";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, fechaInicial);
            stmt.setDate(2, fechaFinal);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int idInsumo = rs.getInt("id_insumo");
                    String nombreInsumo = rs.getString("nombre_insumo");
                    int idPrestamo = rs.getInt("id_prestamo");
                    java.sql.Date fechaPrestamo = rs.getDate("fecha_prestamo");
                    int cantidadInicial = rs.getInt("cantidad_inicial");
                    Integer cantidadFinal = rs.getObject("cantidad_final") != null ? 
                                          rs.getInt("cantidad_final") : null;
                    Integer idLaboratorio = rs.getObject("id_laboratorio") != null ? 
                                          rs.getInt("id_laboratorio") : null;
                    
                    reporteInsumos.add(new ReporteInsumo(
                        idInsumo,
                        nombreInsumo,
                        idPrestamo,
                        fechaPrestamo,
                        cantidadInicial,
                        cantidadFinal,
                        idLaboratorio
                    ));
                }
            }
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this,
            "Error al obtener los datos de insumos: " + e.getMessage(),
            "Error",
            JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
    
    return reporteInsumos;
}

// Método para generar el documento Word con el reporte de insumos
private void generarDocumentoWordInsumos(List<ReporteInsumo> insumos, 
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
        TextRange textoTitulo2 = titulo2.appendText("Reporte de Préstamos de Insumos");
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
        TextRange textoSubtitulo = subtitulo.appendText("PRÉSTAMOS DE INSUMOS:");
        textoSubtitulo.getCharacterFormat().setFontName("Arial");
        textoSubtitulo.getCharacterFormat().setFontSize(14);
        textoSubtitulo.getCharacterFormat().setBold(true);
        
        // Crear la tabla de insumos
        Table tabla = seccion.addTable(true);
        tabla.resetCells(insumos.size() + 1, 7); // Filas + 1 para el encabezado, 7 columnas
        
        // Establecer el ancho de las columnas
        tabla.autoFit(AutoFitBehaviorType.Auto_Fit_To_Contents);
        
        // Estilos para la tabla
        TableRow encabezado = tabla.getRows().get(0);
        String[] titulos = {"ID Insumo", "Nombre", "ID Préstamo", "Fecha", "Cantidad Inicial", "Cantidad Final", "ID Laboratorio"};
        
        for (int i = 0; i < titulos.length; i++) {
            encabezado.getCells().get(i).getCellFormat().setBackColor(new Color(0, 63, 135));
            TextRange textoEncabezado = encabezado.getCells().get(i).addParagraph().appendText(titulos[i]);
            textoEncabezado.getCharacterFormat().setFontName("Arial");
            textoEncabezado.getCharacterFormat().setFontSize(12);
            textoEncabezado.getCharacterFormat().setBold(true);
            textoEncabezado.getCharacterFormat().setTextColor(Color.WHITE);
        }
        
        // Llenar la tabla con los datos
        for (int i = 0; i < insumos.size(); i++) {
            ReporteInsumo insumo = insumos.get(i);
            TableRow fila = tabla.getRows().get(i + 1);
            
            // Agregar datos a cada celda
            fila.getCells().get(0).addParagraph().appendText(String.valueOf(insumo.getIdInsumo()));
            fila.getCells().get(1).addParagraph().appendText(insumo.getNombreInsumo());
            fila.getCells().get(2).addParagraph().appendText(String.valueOf(insumo.getIdPrestamo()));
            fila.getCells().get(3).addParagraph().appendText(dateFormat.format(insumo.getFechaPrestamo()));
            fila.getCells().get(4).addParagraph().appendText(String.valueOf(insumo.getCantidadInicial()));
            fila.getCells().get(5).addParagraph().appendText(insumo.getCantidadFinal() != null ? 
                String.valueOf(insumo.getCantidadFinal()) : "No devuelto");
            fila.getCells().get(6).addParagraph().appendText(insumo.getIdLaboratorio() != null ? 
                String.valueOf(insumo.getIdLaboratorio()) : "N/A");
            
            // Alternar colores de fondo para las filas (para mejor legibilidad)
            if (i % 2 == 1) {
                for (int j = 0; j < 7; j++) {
                    fila.getCells().get(j).getCellFormat().setBackColor(new Color(240, 240, 255));
                }
            }
        }
        
        // Guardar el documento
        // Obtener la ruta del directorio actual
        String directorioActual = System.getProperty("user.dir");
        String fechaStr = new SimpleDateFormat("yyyyMMdd").format(new java.util.Date());
        String nombreArchivo = directorioActual + "/ReporteInsumos_" + fechaStr + ".docx";
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


    //SECCIÓN REFERENTE A LOS INSUMOS FINAL
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