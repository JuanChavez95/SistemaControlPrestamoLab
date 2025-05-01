/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PanelSanciones;

import Clases.Sancion;
import Controles.ControladorSancion;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
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
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Inicializar el controlador y el formato de fecha
        controlador = new ControladorSancion();
        formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        
        // Panel superior para búsqueda
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        
        JLabel lblTitulo = new JLabel("Sanciones Vigentes");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        
        JLabel lblRU = new JLabel("RU Usuario:");
        txtRU = new JTextField(10);
        btnBuscar = new JButton("Buscar");
        
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
        tablaSanciones.getTableHeader().setReorderingAllowed(false);
        
        // Agregar scroll a la tabla
        JScrollPane scrollPane = new JScrollPane(tablaSanciones);
        add(scrollPane, BorderLayout.CENTER);
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