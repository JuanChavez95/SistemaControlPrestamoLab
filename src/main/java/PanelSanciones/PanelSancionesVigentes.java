/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PanelSanciones;

import Clases.Sancion;
import Controles.ControladorSancion;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.*;
import javax.swing.*;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 * Panel para mostrar las sanciones vigentes de un usuario.
 * Autor: Equipo Soldados Caídos
 */
public class PanelSancionesVigentes extends JPanel {
    
    private JTextField txtRU;
    private JButton btnBuscar;
    private JTable tablaSanciones;
    private DefaultTableModel modeloTabla;
    private ControladorSancion controlador;
    private SimpleDateFormat formatoFecha;
    
    /**
     * Constructor del panel de sanciones vigentes.
     */
    public PanelSancionesVigentes() {
        inicializarComponentes();
        configurarEventos();
    }
    
    /**
     * Inicializa y configura los componentes del panel.
     */
    private void inicializarComponentes() {
        // Configuración del panel
        setBackground(new Color(240, 248, 255)); // Fondo azul claro (AliceBlue)
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setOpaque(false);
        
        // Inicializar el controlador y el formato de fecha
        controlador = new ControladorSancion();
        formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        
        // Panel superior para búsqueda
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panelBusqueda.setBackground(new Color(135, 206, 250)); // Fondo azul cielo
        panelBusqueda.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 130, 180), 1), // Borde azul acero
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        
        JLabel lblTitulo = new JLabel("Sanciones Vigentes");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(25, 25, 112)); // Azul oscuro (MidnightBlue)
        
        JLabel lblRU = new JLabel("RU Usuario:");
        lblRU.setFont(new Font("Arial", Font.PLAIN, 14));
        lblRU.setForeground(new Color(25, 25, 112));
        
        txtRU = new JTextField(10);
        txtRU.setFont(new Font("Arial", Font.PLAIN, 14));
        txtRU.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 130, 180)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        
        btnBuscar = new JButton("Buscar");
        btnBuscar.setFont(new Font("Arial", Font.BOLD, 14));
        btnBuscar.setBackground(new Color(70, 130, 180)); // Azul acero
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setFocusPainted(false);
        btnBuscar.setBorder(BorderFactory.createLineBorder(new Color(25, 25, 112), 1));
        
        panelBusqueda.add(lblTitulo);
        panelBusqueda.add(lblRU);
        panelBusqueda.add(txtRU);
        panelBusqueda.add(btnBuscar);
        
        add(panelBusqueda, BorderLayout.NORTH);
        
        // Configuración de la tabla
        String[] columnas = {
            "ID Sanción", 
            "Tipo de Sanción", 
            "Descripción", 
            "Fecha Sanción", 
            "Estado", 
            "Fecha Inicio", 
            "Fecha Fin", 
            "Días Suspensión", 
            "ID Préstamo"
        };
        
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // No permitir edición de celdas
            }
        };
        
        tablaSanciones = new JTable(modeloTabla);
        tablaSanciones.setRowHeight(28);
        tablaSanciones.setFont(new Font("Arial", Font.PLAIN, 14));
        tablaSanciones.setBackground(new Color(245, 245, 255)); // Fondo blanco azulado
        tablaSanciones.setGridColor(new Color(173, 216, 230)); // Líneas de cuadrícula azul claro
        tablaSanciones.setSelectionBackground(new Color(100, 149, 237)); // Selección azul
        tablaSanciones.setSelectionForeground(Color.WHITE);
        tablaSanciones.getTableHeader().setReorderingAllowed(false);
        
        // Personalizar el encabezado de la tabla
        JTableHeader header = tablaSanciones.getTableHeader();
        header.setBackground(new Color(70, 130, 180)); // Azul acero
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setBorder(BorderFactory.createLineBorder(new Color(25, 25, 112)));
        
        // Agregar scroll a la tabla
        JScrollPane scrollPane = new JScrollPane(tablaSanciones);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(5, 5, 5, 5),
                BorderFactory.createLineBorder(new Color(135, 206, 250), 2, true))); // Borde redondeado
        scrollPane.getViewport().setBackground(new Color(240, 248, 255));
        
        add(scrollPane, BorderLayout.CENTER);
        
        // Hacer que el panel tenga bordes redondeados
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 130, 180), 2, true),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
    }
    
    // Método para pintar el fondo con bordes redondeados
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
        g2.dispose();
        super.paintComponent(g);
    }
    
    /**
     * Configura los eventos de los componentes.
     */
    private void configurarEventos() {
        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarSancionesVigentes();
            }
        });
    }
    
    /**
     * Busca las sanciones vigentes del usuario según el RU introducido.
     */
    private void buscarSancionesVigentes() {
        try {
            // Obtener el RU del usuario
            String ruTexto = txtRU.getText().trim();
            
            if (ruTexto.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                        "Por favor, introduzca el RU del usuario", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int ru;
            try {
                ru = Integer.parseInt(ruTexto);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, 
                        "El RU debe ser un número válido", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Limpiar la tabla
            modeloTabla.setRowCount(0);
            
            // Buscar sanciones del usuario
            List<Sancion> sanciones = controlador.buscarPorUsuario(ru);
            
            // Filtrar solo las sanciones vigentes (estado = "Vigente")
            List<Sancion> sancionesVigentes = new ArrayList<>();
            for (Sancion sancion : sanciones) {
                if ("ACTIVA".equalsIgnoreCase(sancion.getEstadoSancion())) {
                    sancionesVigentes.add(sancion);
                }
            }
            
            // Verificar si hay sanciones vigentes
            if (sancionesVigentes.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                        "El usuario no tiene sanciones vigentes", 
                        "Información", 
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            // Llenar la tabla con las sanciones vigentes
            for (Sancion sancion : sancionesVigentes) {
                Object[] fila = new Object[9];
                fila[0] = sancion.getIdSancion();
                fila[1] = sancion.getTipoSancion();
                fila[2] = sancion.getDescripcion();
                fila[3] = formatoFecha.format(sancion.getFechaSancion());
                fila[4] = sancion.getEstadoSancion();
                fila[5] = formatoFecha.format(sancion.getFechaInicio());
                fila[6] = sancion.getFechaFin() != null ? 
                        formatoFecha.format(sancion.getFechaFin()) : "N/A";
                fila[7] = sancion.getDiasSuspension();
                fila[8] = sancion.getIdPrestamo() != null ? 
                        sancion.getIdPrestamo() : "N/A";
                
                modeloTabla.addRow(fila);
            }
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, 
                    "Error al buscar sanciones: " + ex.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}