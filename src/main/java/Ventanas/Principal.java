package Ventanas;

import Clases.Insumo;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.awt.Paint;

// Importaciones de todos los paneles necesarios
import Paneles.PanelDocentes;
import Paneles.PanelAdministradores;
import Paneles.PanelEditar;
import Paneles.PanelEstudiantes;
import Paneles.PanelLaboratorio;
import Paneles.PanelEquipo;
import Paneles.PanelHorario;
import Paneles.PanelVisualizarHorario;
import Paneles.PanelHistorialEquipo;
import Paneles.PanelVisualizarEquipo;
import Prestamos.PanelVisualizarPrestamos;
import PanelesMateriales.PanelEditarHerramientas;
import PanelesMateriales.PanelEditarInsumos;
import PanelesMateriales.PanelDetalleHerramientas;
import PanelesMateriales.PanelHerramientas;
import PanelesMateriales.PanelInsumos;
import PanelSanciones.PanelListaSanciones;
import PanelSanciones.PanelSancionar;
import Reportes.PanelReporteEquipos;
import Reportes.PanelReportePrestamo;

import PanelEstadistica.PanelEstadisticasEquipos;
import PanelEstadistica.PanelEstadisticasPrestamos;

// IMPORTACIONES ADICIONALES PARA JFREECHART
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

// IMPORTACIONES DE CONTROLADORES
import Controles.ControladorEquipo;
import Controles.ControladorEquipamiento;
import Controles.ControladorInsumo;
import Controles.ControladorPrestamo;
import Controles.ControladorLaboratorio;
import PanelesMateriales.PanelPrediccionEquipamientoInsumos;
import java.util.ArrayList;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.renderer.category.StandardBarPainter;


import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;



/**
 * Ventana principal del Sistema de Control y Préstamo de Laboratorios para administradores.
 * Ofrece una interfaz moderna, elegante y fácil de usar para gestionar laboratorios, usuarios, equipos y préstamos.
 * El administrador tiene control total: agregar, editar, eliminar y gestionar horarios, usuarios y más.
 *
 * @author DOC
 */
public class Principal extends JFrame {

    private JPanel contentPanel;
    private JLabel usuarioLabel;
    private int ruUsuario; // Añadido para almacenar el RU del usuario
    private static final Color PRIMARY_COLOR = new Color(33, 97, 140);
    private static final Color SECONDARY_COLOR = new Color(235, 245, 255);
    private static final Color ACCENT_COLOR = new Color(52, 152, 219);
    private static final Color TEXT_COLOR = Color.WHITE;
    private static final Color SHADOW_COLOR = new Color(0, 0, 0, 80);
    private static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 20);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    public Principal(int ruUsuario) {
        this.ruUsuario = ruUsuario; // Inicializar ruUsuario
        // Configuración de la ventana
        setTitle("Sistema de Control y Préstamo de Laboratorios - Administrador");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1000, 600));
        setLayout(new BorderLayout());

        // Panel de fondo con degradado
        JPanel backgroundPanel = createBackgroundPanel();
        setContentPane(backgroundPanel);

        // Panel de encabezado
        JPanel headerPanel = createHeaderPanel();
        backgroundPanel.add(headerPanel, BorderLayout.NORTH);

        // Panel de menú principal
        JPanel menuPanel = createMenuPanel();
        backgroundPanel.add(menuPanel, BorderLayout.CENTER);

        // Centrar la ventana
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel createBackgroundPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gradient = new GradientPaint(0, 0, SECONDARY_COLOR, 0, getHeight(), new Color(180, 220, 255));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        panel.setLayout(new BorderLayout());
        return panel;
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(getWidth(), 60));
        headerPanel.setBorder(new EmptyBorder(0, 20, 0, 20));

        JLabel titleLabel = new JLabel("Universidad Salesiana de Bolivia");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(HEADER_FONT);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        userPanel.setOpaque(false);

        usuarioLabel = new JLabel("Administrador ▼");
        usuarioLabel.setForeground(Color.WHITE);
        usuarioLabel.setFont(BUTTON_FONT);
        usuarioLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        userPanel.add(usuarioLabel);

        JButton logoutButton = new JButton("Cerrar Sesión");
        logoutButton.setBackground(new Color(231, 76, 60));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFont(BUTTON_FONT);
        logoutButton.setFocusPainted(false);
        logoutButton.setBorder(new RoundedBorder(12));
        logoutButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logoutButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                logoutButton.setBackground(new Color(250, 100, 80));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                logoutButton.setBackground(new Color(231, 76, 60));
            }
        });
        logoutButton.addActionListener(e -> dispose());
        userPanel.add(logoutButton);

        headerPanel.add(userPanel, BorderLayout.EAST);
        return headerPanel;
    }

    private JPanel createMenuPanel() {
    	JPanel panel = new JPanel(new BorderLayout());
    	panel.setOpaque(false);
    	panel.setBorder(new EmptyBorder(20, 20, 20, 20));

    	JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
    	buttonPanel.setOpaque(false);

    	contentPanel = new JPanel(new BorderLayout());
    	contentPanel.setBackground(Color.WHITE);
    	contentPanel.setBorder(new RoundedBorder(10));

    	// Crear panel principal con gráficos estadísticos
    	JPanel mainContentPanel = createDashboardPanel();
    	contentPanel.add(mainContentPanel, BorderLayout.CENTER);

    	buttonPanel.add(createMenuButton("Laboratorios", new String[]{"Horarios", "Editar Horario", "Editar Laboratorio"}));
    	buttonPanel.add(createMenuButton("Usuarios", new String[]{"Docentes", "Estudiantes", "Administradores", "Editar Usuarios"}));
    	buttonPanel.add(createMenuButton("Equipos", new String[]{"Máquinas", "Editar Equipos", "Detalle Equipos", "Generar Reportes Equipos", "Estadística de Equipos"}));
    	buttonPanel.add(createMenuButton("Préstamos", new String[]{"Visualizar Préstamos", "Generar Reportes", "Estadística de Préstamos"}));
    	buttonPanel.add(createMenuButton("Materiales", new String[]{"Herramientas", "Insumos", "Editar Herramientas", "Editar Insumos", "Detalle Herramientas", "Predicciones"}));
    	buttonPanel.add(createMenuButton("Sanciones", new String[]{"Lista de Sanciones", "Sancionar"}));

    	panel.add(buttonPanel, BorderLayout.NORTH);
    	panel.add(contentPanel, BorderLayout.CENTER);
    	return panel;
    }

    private JButton createMenuButton(String title, String[] subOptions) {
        JButton button = new JButton(title) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(SHADOW_COLOR);
                g2d.fillRoundRect(3, 3, getWidth() - 4, getHeight() - 4, 20, 20);
                GradientPaint gradient = new GradientPaint(0, 0, ACCENT_COLOR, 0, getHeight(), new Color(41, 128, 185));
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                g2d.dispose();

                g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2d.setColor(TEXT_COLOR);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int textWidth = fm.stringWidth(getText());
                int textHeight = fm.getAscent();
                int x = (getWidth() - textWidth) / 2;
                int y = (getHeight() + textHeight) / 2 - 2;
                g2d.drawString(getText(), x, y);
                g2d.dispose();
            }

            @Override
            protected void paintBorder(Graphics g) {
            }
        };
        button.setForeground(TEXT_COLOR);
        button.setFont(BUTTON_FONT);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorder(new RoundedBorder(20));
        button.setPreferredSize(new Dimension(200, 45));
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(52, 170, 220));
                button.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(ACCENT_COLOR);
                button.repaint();
            }
        });

        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.setBackground(SECONDARY_COLOR);
        popupMenu.setBorder(new RoundedBorder(10));

        for (String subOption : subOptions) {
            JMenuItem menuItem = new JMenuItem(subOption);
            menuItem.setFont(LABEL_FONT);
            menuItem.setBackground(SECONDARY_COLOR);
            menuItem.setForeground(new Color(44, 62, 80));
            menuItem.setBorder(new EmptyBorder(8, 15, 8, 15));
            menuItem.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    menuItem.setBackground(new Color(200, 230, 255));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    menuItem.setBackground(SECONDARY_COLOR);
                }
            });
            menuItem.addActionListener(e -> mostrarContenido(title, subOption));
            popupMenu.add(menuItem);
        }

        button.addActionListener(e -> popupMenu.show(button, 0, button.getHeight()));
        return button;
    }

    private void mostrarContenido(String categoria, String subOpcion) {
        contentPanel.removeAll();

        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setOpaque(false);
        contentWrapper.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = new JLabel(categoria.toUpperCase() + " > " + subOpcion);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(new Color(44, 62, 80));
        titleLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentWrapper.add(titleLabel, BorderLayout.NORTH);

        JPanel contenidoEspecifico = new JPanel();
        contenidoEspecifico.setOpaque(false);

        switch (categoria) {
            case "Laboratorios":
                if (subOpcion.equals("Horarios")) {
                    contenidoEspecifico = crearPanelHorarios();
                } else if (subOpcion.equals("Editar Horario")) {
                    contenidoEspecifico = crearPanelEditarHorario();
                } else if (subOpcion.equals("Editar Laboratorio")) {
                    contenidoEspecifico = crearPanelEditarLaboratorio();
                }
                break;
            case "Usuarios":
                if (subOpcion.equals("Docentes")) {
                    contenidoEspecifico = crearPanelDocentes();
                } else if (subOpcion.equals("Estudiantes")) {
                    contenidoEspecifico = crearPanelEstudiantes();
                } else if (subOpcion.equals("Administradores")) {
                    contenidoEspecifico = crearPanelAdministradores();
                } else if (subOpcion.equals("Editar Usuarios")) {
                    contenidoEspecifico = crearPanelEditarUsuarios();
                }
                break;
            case "Equipos":
                if (subOpcion.equals("Máquinas")) {
                    contenidoEspecifico = crearPanelMaquinas();
                } else if (subOpcion.equals("Editar Equipos")) {
                    contenidoEspecifico = crearPanelEditarEquipos();
                } else if (subOpcion.equals("Detalle Equipos")) {
                    contenidoEspecifico = crearPanelDetalleEquipos();
                } else if (subOpcion.equals("Generar Reportes Equipos")) {
                    contenidoEspecifico = crearPanelReportesEquipos();
                }else if (subOpcion.equals("Estadística de Equipos")) {
                    contenidoEspecifico = crearPanelEstadisticaEquipos();
                }
                break;
            case "Préstamos":
                if (subOpcion.equals("Visualizar Préstamos")) {
                    contenidoEspecifico = crearPanelVisualizarPrestamos();
                } else if (subOpcion.equals("Generar Reportes")) {
                    contenidoEspecifico = crearPanelGenerarReportePrestamos();
                }else if (subOpcion.equals("Estadística de Préstamos")) {
                    contenidoEspecifico = crearPanelEstadisticaPrestamos();
                }

                break;
            case "Materiales":
                if (subOpcion.equals("Herramientas")) {
                    contenidoEspecifico = crearPanelHerramientas();
                } else if (subOpcion.equals("Insumos")) {
                    contenidoEspecifico = crearPanelInsumos();
                } else if (subOpcion.equals("Editar Herramientas")) {
                    contenidoEspecifico = crearPanelEditarHerramientas();
                } else if (subOpcion.equals("Editar Insumos")) {
                    contenidoEspecifico = crearPanelEditarInsumos();
                } else if (subOpcion.equals("Detalle Herramientas")) {
                    contenidoEspecifico = crearPanelDetalleHerramientas();
                } else if (subOpcion.equals("Predicciones")) {
                    contenidoEspecifico = crearPanelPrediccionEquipamientoInsumos();
                }
                break;
            case "Sanciones":
                if (subOpcion.equals("Lista de Sanciones")) {
                    contenidoEspecifico = crearPanelListaSanciones();
                } else if (subOpcion.equals("Sancionar")) {
                    contenidoEspecifico = crearPanelSancionar();
                }
                break;
        }

        contentWrapper.add(contenidoEspecifico, BorderLayout.CENTER);
        contentPanel.add(contentWrapper, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private JPanel crearPanelEstadisticaEquipos() {
        return new PanelEstadisticasEquipos();
    }

    private JPanel crearPanelEstadisticaPrestamos() {
        return new PanelEstadisticasPrestamos();
    }


    private JPanel crearPanelHorarios() {
        return new PanelVisualizarHorario();
    }

    private JPanel crearPanelEditarHorario() {
        return new PanelHorario();
    }

    private JPanel crearPanelEditarLaboratorio() {
        return new PanelLaboratorio();
    }

    private JPanel crearPanelDocentes() {
        return new PanelDocentes();
    }

    private JPanel crearPanelEstudiantes() {
        return new PanelEstudiantes();
    }

    private JPanel crearPanelAdministradores() {
        return new PanelAdministradores();
    }

    private JPanel crearPanelEditarUsuarios() {
        return new PanelEditar();
    }

    private JPanel crearPanelMaquinas() {
        return new PanelVisualizarEquipo();
    }

    private JPanel crearPanelEditarEquipos() {
        return new PanelEquipo();
    }

    private JPanel crearPanelDetalleEquipos() {
        return new PanelHistorialEquipo();
    }

    private JPanel crearPanelReportesEquipos() {
        return new PanelReporteEquipos();
    }

    private JPanel crearPanelVisualizarPrestamos() {
        return new PanelVisualizarPrestamos(ruUsuario);
    }

    private JPanel crearPanelGenerarReportePrestamos() {
        return new PanelReportePrestamo();
    }

    private JPanel crearPanelHerramientas() {
        return new PanelHerramientas();
    }

    private JPanel crearPanelInsumos() {
        return new PanelInsumos();
    }

    private JPanel crearPanelEditarHerramientas() {
        return new PanelEditarHerramientas();
    }

    private JPanel crearPanelEditarInsumos() {
        return new PanelEditarInsumos();
    }

    private JPanel crearPanelDetalleHerramientas() {
        return new PanelDetalleHerramientas();
    }
    
    private JPanel crearPanelPrediccionEquipamientoInsumos() {
        return new PanelPrediccionEquipamientoInsumos();
    }

    private JPanel crearPanelListaSanciones() {
        return new PanelListaSanciones();
    }

    private JPanel crearPanelSancionar() {
        return new PanelSancionar();
    }

    private static class RoundedBorder implements Border {
        private final int radius;

        public RoundedBorder(int radius) {
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(new Color(150, 150, 150));
            g2d.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2d.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(radius / 2, radius / 2, radius / 2, radius / 2);
        }

        @Override
        public boolean isBorderOpaque() {
            return false;
        }
    }

    // AGREGAR ESTOS MÉTODOS NUEVOS A LA CLASE Principal:
 
    private JPanel createDashboardPanel() {
        JPanel dashboardPanel = new JPanel(new BorderLayout());
        dashboardPanel.setBackground(Color.WHITE);
        dashboardPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Título principal
        JLabel titleLabel = new JLabel("Panel de Control - Estadísticas del Sistema", SwingConstants.CENTER);
    	titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
    	titleLabel.setForeground(new Color(44, 62, 80));
    	titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
    	dashboardPanel.add(titleLabel, BorderLayout.NORTH);

    	// Panel principal con scroll
    	JScrollPane scrollPane = new JScrollPane();
    	scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    	scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    	scrollPane.setBorder(null);

    	JPanel chartsContainer = new JPanel();
    	chartsContainer.setLayout(new BoxLayout(chartsContainer, BoxLayout.Y_AXIS));
    	chartsContainer.setBackground(Color.WHITE);

    	// Agregar secciones de gráficos
    	chartsContainer.add(createEquiposSection());
    	chartsContainer.add(Box.createVerticalStrut(20));
    	chartsContainer.add(createEquipamientosSection());
    	chartsContainer.add(Box.createVerticalStrut(20));
    	chartsContainer.add(createInsumosSection());
    	chartsContainer.add(Box.createVerticalStrut(20));
    	chartsContainer.add(createPrestamosSection());

    	scrollPane.setViewportView(chartsContainer);
    	dashboardPanel.add(scrollPane, BorderLayout.CENTER);

    	return dashboardPanel;
    }

   private JPanel createEquiposSection() {
    JPanel section = new JPanel(new BorderLayout());
    section.setBackground(Color.WHITE);
    section.setBorder(BorderFactory.createTitledBorder(
        BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
        "GRÁFICO DE EQUIPOS",
        0, 0, new Font("Segoe UI", Font.BOLD, 16), PRIMARY_COLOR
    ));

    JPanel chartsPanel = new JPanel(new GridLayout(1, 2, 10, 10));
    chartsPanel.setBackground(Color.WHITE);
    chartsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

    // Variables para almacenar totales para la descripción
    int totalEquipos = 0;
    int disponibles = 0;
    int noDisponibles = 0;
    int deBaja = 0;

    // Gráfico de estados de equipos
    try {
        DefaultPieDataset datasetEstados = new DefaultPieDataset();
        ControladorEquipo controladorEquipo = new ControladorEquipo();
        
        // Obtener datos reales de los controladores
        disponibles = controladorEquipo.contarEquiposPorEstado("Disponible");
        noDisponibles = controladorEquipo.contarEquiposPorEstado("No Disponible");
        deBaja = controladorEquipo.contarEquiposPorEstado("De Baja");
        totalEquipos = disponibles + noDisponibles + deBaja;

        datasetEstados.setValue("Disponible", disponibles);
        datasetEstados.setValue("No Disponible", noDisponibles);
        datasetEstados.setValue("De Baja", deBaja);

        JFreeChart chartEstados = ChartFactory.createPieChart(
            "Estados de Equipos",
            datasetEstados,
            true, true, false
        );
        
        // Personalización del gráfico de pie
        PiePlot plot = (PiePlot) chartEstados.getPlot();
        plot.setSectionPaint("Disponible", new Color(76, 175, 80));  // Verde
        plot.setSectionPaint("No Disponible", new Color(255, 152, 0));  // Naranja
        plot.setSectionPaint("De Baja", new Color(244, 67, 54));  // Rojo

        ChartPanel chartPanelEstados = new ChartPanel(chartEstados);
        chartPanelEstados.setPreferredSize(new Dimension(400, 300));
        chartsPanel.add(chartPanelEstados);

    } catch (Exception e) {
        JLabel errorLabel = new JLabel("Error al cargar datos de equipos", SwingConstants.CENTER);
        errorLabel.setPreferredSize(new Dimension(400, 300));
        chartsPanel.add(errorLabel);
    }

    // Gráfico de distribución por laboratorios
    try {
        DefaultCategoryDataset datasetLaboratorios = new DefaultCategoryDataset();
        ControladorEquipo controladorEquipo = new ControladorEquipo();
        ControladorLaboratorio controladorLaboratorio = new ControladorLaboratorio();
        
        // Obtener lista de laboratorios y contar equipos por cada uno
        var laboratorios = controladorLaboratorio.listar();
        
        // Crear una lista de colores aleatorios para las barras
        final List<Color> colorList = new ArrayList<>();
        for (int i = 0; i < laboratorios.size(); i++) {
            colorList.add(new Color(
                20 + (int)(Math.random() * 220), 
                20 + (int)(Math.random() * 220), 
                20 + (int)(Math.random() * 220)
            ));
        }
        
        for (var laboratorio : laboratorios) {
            int cantidad = controladorEquipo.contarEquiposPorLaboratorio(laboratorio.getIdLaboratorio());
            datasetLaboratorios.addValue(cantidad, "Equipamientos", String.valueOf(laboratorio.getIdLaboratorio()));
        }

        JFreeChart chartLaboratorios = ChartFactory.createBarChart(
            "Distribución de Equipos por Laboratorio",
            "Laboratorio",
            "Cantidad de Equipos",
            datasetLaboratorios,
            PlotOrientation.VERTICAL,
            true, true, false
        );
        
        // Personalizar gráfico
        CategoryPlot plot = chartLaboratorios.getCategoryPlot();
        
        // Crear un renderer personalizado que asigne un color diferente a cada barra
        BarRenderer renderer = new BarRenderer() {
            @Override
            public Paint getItemPaint(int row, int column) {
                // Usar el índice de columna para seleccionar un color específico
                return colorList.get(column % colorList.size());
            }
        };
        
        // Establecer el renderer personalizado
        plot.setRenderer(renderer);
        
        // Configuración adicional del renderer
        renderer.setDrawBarOutline(true);
        renderer.setShadowVisible(false);
        renderer.setBarPainter(new StandardBarPainter()); // Barras sólidas sin gradiente
        
        // Ajustar fuentes y etiquetas
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0));

        ChartPanel chartPanelLaboratorios = new ChartPanel(chartLaboratorios);
        chartPanelLaboratorios.setPreferredSize(new Dimension(400, 300));
        chartsPanel.add(chartPanelLaboratorios);

    } catch (Exception e) {
        JLabel errorLabel = new JLabel("Error al cargar distribución por laboratorios", SwingConstants.CENTER);
        errorLabel.setPreferredSize(new Dimension(400, 300));
        chartsPanel.add(errorLabel);
    }

    section.add(chartsPanel, BorderLayout.CENTER);
    
    // Añadir panel de descripción en la parte inferior
    JPanel descripcionPanel = new JPanel();
    descripcionPanel.setLayout(new BoxLayout(descripcionPanel, BoxLayout.Y_AXIS));
    descripcionPanel.setBackground(Color.WHITE);
    descripcionPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    
    JLabel lblTotalEquipos = new JLabel("Total de Equipos: " + totalEquipos, SwingConstants.CENTER);
    JLabel lblEquiposDisponibles = new JLabel("Equipos Disponibles: " + disponibles + 
            " (" + calcularPorcentaje(disponibles, totalEquipos) + "%)", SwingConstants.CENTER);
    JLabel lblEquiposNoDisponibles = new JLabel("Equipos No Disponibles: " + noDisponibles + 
            " (" + calcularPorcentaje(noDisponibles, totalEquipos) + "%)", SwingConstants.CENTER);
    JLabel lblEquiposBaja = new JLabel("Equipos de Baja: " + deBaja + 
            " (" + calcularPorcentaje(deBaja, totalEquipos) + "%)", SwingConstants.CENTER);
    
    // Usar fuente en negrita y colores para las etiquetas
    Font boldFont = new Font("Segoe UI", Font.BOLD, 14);
    lblTotalEquipos.setFont(boldFont);
    lblEquiposDisponibles.setFont(boldFont);
    lblEquiposDisponibles.setForeground(new Color(76, 175, 80)); // Verde
    lblEquiposNoDisponibles.setFont(boldFont);
    lblEquiposNoDisponibles.setForeground(new Color(255, 152, 0)); // Naranja
    lblEquiposBaja.setFont(boldFont);
    lblEquiposBaja.setForeground(new Color(244, 67, 54)); // Rojo
    
    // Asegurar que los JLabel estén centrados
    lblTotalEquipos.setAlignmentX(Component.CENTER_ALIGNMENT);
    lblEquiposDisponibles.setAlignmentX(Component.CENTER_ALIGNMENT);
    lblEquiposNoDisponibles.setAlignmentX(Component.CENTER_ALIGNMENT);
    lblEquiposBaja.setAlignmentX(Component.CENTER_ALIGNMENT);
    
    descripcionPanel.add(lblTotalEquipos);
    descripcionPanel.add(Box.createRigidArea(new Dimension(0, 8)));
    descripcionPanel.add(lblEquiposDisponibles);
    descripcionPanel.add(Box.createRigidArea(new Dimension(0, 8)));
    descripcionPanel.add(lblEquiposNoDisponibles);
    descripcionPanel.add(Box.createRigidArea(new Dimension(0, 8)));
    descripcionPanel.add(lblEquiposBaja);
    
    section.add(descripcionPanel, BorderLayout.SOUTH);
    
    return section;
}

private JPanel createEquipamientosSection() {
    JPanel section = new JPanel(new BorderLayout());
    section.setBackground(Color.WHITE);
    section.setBorder(BorderFactory.createTitledBorder(
        BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
        "GRÁFICO DE EQUIPAMIENTOS",
        0, 0, new Font("Segoe UI", Font.BOLD, 16), PRIMARY_COLOR
    ));

    JPanel chartsPanel = new JPanel(new GridLayout(1, 2, 10, 10));
    chartsPanel.setBackground(Color.WHITE);
    chartsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
    
    // Variables para almacenar totales para la descripción
    int totalEquipamientos = 0;
    int disponibles = 0;
    int enUso = 0;
    int enMantenimiento = 0;
    int noDisponibles = 0;

    // Gráfico de disponibilidad de equipamientos
    try {
        DefaultPieDataset datasetDisponibilidad = new DefaultPieDataset();
        ControladorEquipamiento controladorEquipamiento = new ControladorEquipamiento();
        
        disponibles = controladorEquipamiento.contarEquipamientosPorDisponibilidad("Disponible");
        enUso = controladorEquipamiento.contarEquipamientosPorDisponibilidad("En Uso");
        enMantenimiento = controladorEquipamiento.contarEquipamientosPorDisponibilidad("En Mantenimiento");
        noDisponibles = controladorEquipamiento.contarEquipamientosPorDisponibilidad("No Disponible");
        totalEquipamientos = disponibles + enUso + enMantenimiento + noDisponibles;

        datasetDisponibilidad.setValue("Disponible", disponibles);
        datasetDisponibilidad.setValue("En Uso", enUso);
        datasetDisponibilidad.setValue("En Mantenimiento", enMantenimiento);
        datasetDisponibilidad.setValue("No Disponible", noDisponibles);

        JFreeChart chartDisponibilidad = ChartFactory.createPieChart(
            "Disponibilidad de Equipamientos",
            datasetDisponibilidad,
            true, true, false
        );
        
        // Personalización del gráfico de pie
        PiePlot plot = (PiePlot) chartDisponibilidad.getPlot();
        plot.setSectionPaint("Disponible", new Color(76, 175, 80));  // Verde
        plot.setSectionPaint("En Uso", new Color(33, 150, 243));  // Azul
        plot.setSectionPaint("En Mantenimiento", new Color(255, 152, 0));  // Naranja
        plot.setSectionPaint("No Disponible", new Color(244, 67, 54));  // Rojo

        ChartPanel chartPanelDisponibilidad = new ChartPanel(chartDisponibilidad);
        chartPanelDisponibilidad.setPreferredSize(new Dimension(400, 300));
        chartsPanel.add(chartPanelDisponibilidad);

    } catch (Exception e) {
        JLabel errorLabel = new JLabel("Error al cargar datos de equipamientos", SwingConstants.CENTER);
        errorLabel.setPreferredSize(new Dimension(400, 300));
        chartsPanel.add(errorLabel);
    }

    // Gráfico de distribución de equipamientos por laboratorios
    try {
        DefaultCategoryDataset datasetLaboratorios = new DefaultCategoryDataset();
        ControladorEquipamiento controladorEquipamiento = new ControladorEquipamiento();
        ControladorLaboratorio controladorLaboratorio = new ControladorLaboratorio();
        
        var laboratorios = controladorLaboratorio.listar();
        
        // Crear una lista de colores aleatorios para las barras
        final List<Color> colorList = new ArrayList<>();
        for (int i = 0; i < laboratorios.size(); i++) {
            colorList.add(new Color(
                20 + (int)(Math.random() * 220), 
                20 + (int)(Math.random() * 220), 
                20 + (int)(Math.random() * 220)
            ));
        }
        
        for (var laboratorio : laboratorios) {
            int cantidad = controladorEquipamiento.contarEquipamientosPorLaboratorio(laboratorio.getIdLaboratorio());
            datasetLaboratorios.addValue(cantidad, "Equipamientos", String.valueOf(laboratorio.getIdLaboratorio()));
        }

        JFreeChart chartLaboratorios = ChartFactory.createBarChart(
            "Distribución de Equipamientos por Laboratorio",
            "Laboratorio",
            "Cantidad de Equipamientos",
            datasetLaboratorios,
            PlotOrientation.VERTICAL,
            true, true, false
        );
        
        // Personalizar gráfico
        CategoryPlot plot = chartLaboratorios.getCategoryPlot();
        
        // Crear un renderer personalizado que asigne un color diferente a cada barra
        BarRenderer renderer = new BarRenderer() {
            @Override
            public Paint getItemPaint(int row, int column) {
                // Usar el índice de columna para seleccionar un color específico
                return colorList.get(column % colorList.size());
            }
        };
        
        // Establecer el renderer personalizado
        plot.setRenderer(renderer);
        
        // Configuración adicional del renderer
        renderer.setDrawBarOutline(true);
        renderer.setShadowVisible(false);
        renderer.setBarPainter(new StandardBarPainter()); // Barras sólidas sin gradiente
        
        // Ajustar fuentes y etiquetas
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0));

        ChartPanel chartPanelLaboratorios = new ChartPanel(chartLaboratorios);
        chartPanelLaboratorios.setPreferredSize(new Dimension(400, 300));
        chartsPanel.add(chartPanelLaboratorios);

    } catch (Exception e) {
        JLabel errorLabel = new JLabel("Error al cargar distribución de equipamientos", SwingConstants.CENTER);
        errorLabel.setPreferredSize(new Dimension(400, 300));
        chartsPanel.add(errorLabel);
    }

    section.add(chartsPanel, BorderLayout.CENTER);
    
    // Añadir panel de descripción en la parte inferior
    JPanel descripcionPanel = new JPanel();
    descripcionPanel.setLayout(new BoxLayout(descripcionPanel, BoxLayout.Y_AXIS));
    descripcionPanel.setBackground(Color.WHITE);
    descripcionPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    
    JLabel lblTotalEquipamientos = new JLabel("Total de Equipamientos: " + totalEquipamientos, SwingConstants.CENTER);
    JLabel lblEquipDisponibles = new JLabel("Equipamientos Disponibles: " + disponibles + 
            " (" + calcularPorcentaje(disponibles, totalEquipamientos) + "%)", SwingConstants.CENTER);
    JLabel lblEquipEnUso = new JLabel("Equipamientos En Uso: " + enUso + 
            " (" + calcularPorcentaje(enUso, totalEquipamientos) + "%)", SwingConstants.CENTER);
    JLabel lblEquipMantenimiento = new JLabel("Equipamientos En Mantenimiento: " + enMantenimiento + 
            " (" + calcularPorcentaje(enMantenimiento, totalEquipamientos) + "%)", SwingConstants.CENTER);
    JLabel lblEquipNoDisponibles = new JLabel("Equipamientos No Disponibles: " + noDisponibles + 
            " (" + calcularPorcentaje(noDisponibles, totalEquipamientos) + "%)", SwingConstants.CENTER);
    
    // Usar fuente en negrita y colores para las etiquetas
    Font boldFont = new Font("Segoe UI", Font.BOLD, 14);
    lblTotalEquipamientos.setFont(boldFont);
    lblEquipDisponibles.setFont(boldFont);
    lblEquipDisponibles.setForeground(new Color(76, 175, 80)); // Verde
    lblEquipEnUso.setFont(boldFont);
    lblEquipEnUso.setForeground(new Color(33, 150, 243)); // Azul
    lblEquipMantenimiento.setFont(boldFont);
    lblEquipMantenimiento.setForeground(new Color(255, 152, 0)); // Naranja
    lblEquipNoDisponibles.setFont(boldFont);
    lblEquipNoDisponibles.setForeground(new Color(244, 67, 54)); // Rojo
    
    // Asegurar que las etiquetas se alineen correctamente en un layout BoxLayout
    lblTotalEquipamientos.setAlignmentX(Component.CENTER_ALIGNMENT);
    lblEquipDisponibles.setAlignmentX(Component.CENTER_ALIGNMENT);
    lblEquipEnUso.setAlignmentX(Component.CENTER_ALIGNMENT);
    lblEquipMantenimiento.setAlignmentX(Component.CENTER_ALIGNMENT);
    lblEquipNoDisponibles.setAlignmentX(Component.CENTER_ALIGNMENT);
    
    descripcionPanel.add(lblTotalEquipamientos);
    descripcionPanel.add(Box.createRigidArea(new Dimension(0, 8)));
    descripcionPanel.add(lblEquipDisponibles);
    descripcionPanel.add(Box.createRigidArea(new Dimension(0, 8)));
    descripcionPanel.add(lblEquipEnUso);
    descripcionPanel.add(Box.createRigidArea(new Dimension(0, 8)));
    descripcionPanel.add(lblEquipMantenimiento);
    descripcionPanel.add(Box.createRigidArea(new Dimension(0, 8)));
    descripcionPanel.add(lblEquipNoDisponibles);
    
    section.add(descripcionPanel, BorderLayout.SOUTH);
    
    return section;
}

// Función auxiliar para calcular porcentajes
private int calcularPorcentaje(int valor, int total) {
    if (total == 0) return 0;
    return (int) Math.round((double) valor / total * 100);
}

private JPanel createInsumosSection() {
    JPanel section = new JPanel(new BorderLayout());
    section.setBackground(Color.WHITE);
    section.setBorder(BorderFactory.createTitledBorder(
        BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
        "GRÁFICO DE INSUMOS",
        0, 0, new Font("Segoe UI", Font.BOLD, 16), PRIMARY_COLOR
    ));
    JPanel chartPanel = new JPanel(new BorderLayout());
    chartPanel.setBackground(Color.WHITE);
    chartPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
    try {
        // Crear dataset para gráfico de barras
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
         // Obtener lista de todos los insumos
        ControladorInsumo controladorInsumos = new ControladorInsumo();
        List<Insumo> listaInsumos = controladorInsumos.listar();
        
        // Crear una lista de colores aleatorios para las barras
        final List<Color> colorList = new ArrayList<>();
        for (int i = 0; i < listaInsumos.size(); i++) {
            colorList.add(new Color(
                20 + (int)(Math.random() * 220), 
                20 + (int)(Math.random() * 220), 
                20 + (int)(Math.random() * 220)
            ));
        }
        
        // Agregar cada insumo al dataset con su cantidad correspondiente
        for (Insumo insumo : listaInsumos) {
            dataset.addValue(insumo.getCantidad(), "Cantidad", insumo.getNombreInsumo());
        }
        
        // Crear gráfico de barras
        JFreeChart chart = ChartFactory.createBarChart(
            "Cantidades de Insumos",     // título del gráfico
            "Insumos",                   // etiqueta del eje X
            "Cantidad",                  // etiqueta del eje Y
            dataset,                     // dataset
            PlotOrientation.VERTICAL,    // orientación
            true,                        // incluir leyenda
            true,                        // tooltips
            false                        // URLs
        );
        
        // Personalizar gráfico
        CategoryPlot plot = chart.getCategoryPlot();
        
        // Crear un renderer personalizado que asigne un color diferente a cada barra
        BarRenderer renderer = new BarRenderer() {
            @Override
            public Paint getItemPaint(int row, int column) {
                // Usar el índice de columna para seleccionar un color específico
                return colorList.get(column % colorList.size());
            }
        };
        
        // Establecer el renderer personalizado
        plot.setRenderer(renderer);
        
        // Configuración adicional del renderer
        renderer.setDrawBarOutline(true);
        renderer.setShadowVisible(false);
        renderer.setBarPainter(new StandardBarPainter()); // Barras sólidas sin gradiente
        
        // Ajustar fuentes y etiquetas
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0));
        
        // Si hay muchos insumos, ajustar para mejor visualización
        if (listaInsumos.size() > 10) {
            domainAxis.setMaximumCategoryLabelLines(2);
            domainAxis.setMaximumCategoryLabelWidthRatio(0.5f);
        }
        
        ChartPanel chartPanelInsumos = new ChartPanel(chart);
        chartPanelInsumos.setPreferredSize(new Dimension(600, 400));
        chartPanel.add(chartPanelInsumos, BorderLayout.CENTER);
    } catch (Exception e) {
        JLabel errorLabel = new JLabel("Error al cargar datos de insumos: " + e.getMessage(), SwingConstants.CENTER);
        errorLabel.setPreferredSize(new Dimension(600, 400));
        chartPanel.add(errorLabel, BorderLayout.CENTER);
    }
    section.add(chartPanel, BorderLayout.CENTER);
    return section;
}

private JPanel createPrestamosSection() {
    JPanel section = new JPanel(new BorderLayout());
    section.setBackground(Color.WHITE);
    section.setBorder(BorderFactory.createTitledBorder(
        BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
        "GRÁFICO DE PRÉSTAMOS",
        0, 0, new Font("Segoe UI", Font.BOLD, 16), PRIMARY_COLOR
    ));

    JPanel chartPanel = new JPanel(new BorderLayout());
    chartPanel.setBackground(Color.WHITE);
    chartPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

    try {
        DefaultPieDataset datasetPrestamos = new DefaultPieDataset();
        ControladorPrestamo controladorPrestamo = new ControladorPrestamo();
        
        int pendientes = controladorPrestamo.contarPrestamosPorEstado("Pendiente");
        int aceptados = controladorPrestamo.contarPrestamosPorEstado("Aceptado");
        int rechazados = controladorPrestamo.contarPrestamosPorEstado("Rechazado");
        int terminados = controladorPrestamo.contarPrestamosPorEstado("Terminado");

        datasetPrestamos.setValue("Pendiente", pendientes);
        datasetPrestamos.setValue("Aceptado", aceptados);
        datasetPrestamos.setValue("Rechazado", rechazados);
        datasetPrestamos.setValue("Terminado", terminados);

        JFreeChart chartPrestamos = ChartFactory.createPieChart(
            "Estado de Préstamos",
            datasetPrestamos,
            true, true, false
        );

        ChartPanel chartPanelPrestamos = new ChartPanel(chartPrestamos);
        chartPanelPrestamos.setPreferredSize(new Dimension(600, 400));
        chartPanel.add(chartPanelPrestamos, BorderLayout.CENTER);

    } catch (Exception e) {
        JLabel errorLabel = new JLabel("Error al cargar datos de préstamos", SwingConstants.CENTER);
        errorLabel.setPreferredSize(new Dimension(600, 400));
        chartPanel.add(errorLabel, BorderLayout.CENTER);
    }

    section.add(chartPanel, BorderLayout.CENTER);
    return section;
}
}
