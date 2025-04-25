package Ventanas;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

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
// Importaciones para los nuevos paneles (asumiendo que se crearán en el futuro)
import PanelesMateriales.PanelEditarHerramientas;
import PanelesMateriales.PanelEditarInsumos;
import PanelesMateriales.PanelDetalleHerramientas;
import PanelesMateriales.PanelHerramientas;
import PanelesMateriales.PanelInsumos;
import PanelSanciones.PanelListaSanciones;
import PanelSanciones.PanelSancionar;
import PanelesMateriales.PanelDetalleHerramientas;
import PanelesMateriales.PanelEditarHerramientas;
import PanelesMateriales.PanelEditarInsumos;
import PanelesMateriales.PanelHerramientas;
import PanelesMateriales.PanelInsumos;
import PanelSanciones.PanelListaSanciones;
import PanelSanciones.PanelSancionar;
import Reportes.PanelReporteEquipos;
import Reportes.PanelReportePrestamo;

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
    private static final Color PRIMARY_COLOR = new Color(33, 97, 140); // Azul profundo para encabezados
    private static final Color SECONDARY_COLOR = new Color(235, 245, 255); // Azul claro para fondos
    private static final Color ACCENT_COLOR = new Color(52, 152, 219); // Azul vibrante para botones
    private static final Color TEXT_COLOR = Color.WHITE; // Blanco para texto en botones
    private static final Color SHADOW_COLOR = new Color(0, 0, 0, 80); // Sombra sutil
    private static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 20);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    public Principal() {
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

    /**
     * Crea un panel de fondo con un degradado suave.
     */
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

    /**
     * Crea el panel de encabezado con la marca de la universidad y controles de usuario.
     */
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(getWidth(), 60));
        headerPanel.setBorder(new EmptyBorder(0, 20, 0, 20));

        // Título de la universidad
        JLabel titleLabel = new JLabel("Universidad Salesiana de Bolivia");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(HEADER_FONT);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        // Controles de usuario
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

    /**
     * Crea el panel de menú principal con botones y área de contenido.
     */
    private JPanel createMenuPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Panel de botones de menú
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        buttonPanel.setOpaque(false);

        // Panel de contenido
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(new RoundedBorder(10));

        // Mensaje de bienvenida
        JLabel welcomeLabel = new JLabel("Bienvenido al Sistema de Control y Préstamo de Laboratorios", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        welcomeLabel.setForeground(new Color(44, 62, 80));
        contentPanel.add(welcomeLabel, BorderLayout.CENTER);

        // Añadir botones de menú con las mismas categorías y submenús que en el código original
        buttonPanel.add(createMenuButton("Laboratorios", new String[]{"Horarios", "Editar Horario", "Editar Laboratorio"}));
        buttonPanel.add(createMenuButton("Usuarios", new String[]{"Docentes", "Estudiantes", "Administradores", "Editar Usuarios"}));
        buttonPanel.add(createMenuButton("Equipos", new String[]{"Máquinas", "Editar Equipos", "Detalle Equipos", "Generar Reportes Equipos"}));
        buttonPanel.add(createMenuButton("Préstamos", new String[]{"Visualizar Préstamos", "Generar Reportes"}));
        
        // Nuevos botones para las categorías solicitadas
        buttonPanel.add(createMenuButton("Materiales", new String[]{"Herramientas", "Insumos", "Editar Herramientas", "Editar Insumos", "Detalle Herramientas"}));
        buttonPanel.add(createMenuButton("Sanciones", new String[]{"Lista de Sanciones", "Sancionar"}));

        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(contentPanel, BorderLayout.CENTER);
        return panel;
    }

    /**
     * Crea un botón de menú estilizado con un menú desplegable.
     */
    private JButton createMenuButton(String title, String[] subOptions) {
        JButton button = new JButton(title) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Sombra
                g2d.setColor(SHADOW_COLOR);
                g2d.fillRoundRect(3, 3, getWidth() - 4, getHeight() - 4, 20, 20);
                // Fondo del botón
                GradientPaint gradient = new GradientPaint(0, 0, ACCENT_COLOR, 0, getHeight(), new Color(41, 128, 185));
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                g2d.dispose();

                // Asegurar que el texto se dibuje correctamente
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
                // No dibujar borde adicional para mantener el estilo limpio
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

        // Menú desplegable
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

    /**
     * Muestra el contenido seleccionado en el panel principal.
     * Adaptado del código original para mantener compatibilidad con los paneles existentes.
     */
    private void mostrarContenido(String categoria, String subOpcion) {
        // Limpiar el panel de contenido
        contentPanel.removeAll();

        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setOpaque(false);
        contentWrapper.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Título del contenido
        JLabel titleLabel = new JLabel(categoria.toUpperCase() + " > " + subOpcion);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(new Color(44, 62, 80));
        titleLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentWrapper.add(titleLabel, BorderLayout.NORTH);

        // Panel de contenido específico
        JPanel contenidoEspecifico = new JPanel();
        contenidoEspecifico.setOpaque(false);

        // Mapeo de las opciones de menú con los métodos correspondientes del código original
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
            }
                break;
            case "Préstamos":
            if (subOpcion.equals("Visualizar Préstamos")) {
                contenidoEspecifico = crearPanelVisualizarPrestamos();
            } else if (subOpcion.equals("Generar Reportes")) {
                contenidoEspecifico = crearPanelGenerarReportePrestamos();
            }
            break;
            // Nuevos casos para Materiales y Sanciones
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

    /**
     * Crea el panel para mostrar los horarios de los laboratorios.
     * Integración del panel de visualización de horarios del código original.
     */
    private JPanel crearPanelHorarios() {
        return new PanelVisualizarHorario();
    }

    /**
     * Crea el panel para editar los horarios de los laboratorios.
     * Integración del panel de horarios del código original.
     */
    private JPanel crearPanelEditarHorario() {
        return new PanelHorario();
    }

    /**
     * Crea el panel para editar los laboratorios.
     * Integración del panel de laboratorio del código original.
     */
    private JPanel crearPanelEditarLaboratorio() {
        return new PanelLaboratorio();
    }

    /**
     * Crea el panel para gestionar docentes.
     * Integración del panel de docentes del código original.
     */
    private JPanel crearPanelDocentes() {
        return new PanelDocentes();
    }

    /**
     * Crea el panel para gestionar estudiantes.
     * Integración del panel de estudiantes del código original.
     */
    private JPanel crearPanelEstudiantes() {
        return new PanelEstudiantes();
    }

    /**
     * Crea el panel para gestionar administradores.
     * Integración del panel de administradores del código original.
     */
    private JPanel crearPanelAdministradores() {
        return new PanelAdministradores();
    }

    /**
     * Crea el panel para editar usuarios.
     * Integración del panel de edición de usuarios del código original.
     */
    private JPanel crearPanelEditarUsuarios() {
        return new PanelEditar();
    }

    /**
     * Crea el panel para visualizar máquinas/equipos.
     * Integración del panel de visualización de equipos del código original.
     */
    private JPanel crearPanelMaquinas() {
        return new PanelVisualizarEquipo();
    }

    /**
     * Crea el panel para editar equipos.
     * Integración del panel de equipo del código original.
     */
    private JPanel crearPanelEditarEquipos() {
        return new PanelEquipo();
    }

    /**
     * Crea el panel para ver detalles de equipos.
     * Integración del panel de historial de equipos del código original.
     */
    private JPanel crearPanelDetalleEquipos() {
        return new PanelHistorialEquipo();
    }
    
    private JPanel crearPanelReportesEquipos() {
        return new PanelReporteEquipos();
    }

    /**
     * Crea el panel para visualizar préstamos.
     * Integración del panel de visualización de préstamos del código original.
     */
    private JPanel crearPanelVisualizarPrestamos() {
        return new PanelVisualizarPrestamos();
    }
    
    //Panel para los reportes:
    private JPanel crearPanelGenerarReportePrestamos() {
        return new PanelReportePrestamo(); 
    }

    // Nuevos métodos para los paneles de Materiales

    /**
     * Crea el panel para visualizar herramientas.
     */
    private JPanel crearPanelHerramientas() {
        return new PanelHerramientas();
    }

    /**
     * Crea el panel para visualizar insumos.
     */
    private JPanel crearPanelInsumos() {
        return new PanelInsumos();
    }

    /**
     * Crea el panel para editar herramientas.
     */
    private JPanel crearPanelEditarHerramientas() {
        return new PanelEditarHerramientas();
    }

    /**
     * Crea el panel para editar insumos.
     */
    private JPanel crearPanelEditarInsumos() {
        return new PanelEditarInsumos();
    }

    /**
     * Crea el panel para ver detalles de herramientas.
     */
    private JPanel crearPanelDetalleHerramientas() {
        return new PanelDetalleHerramientas();
    }

    // Nuevos métodos para los paneles de Sanciones

    /**
     * Crea el panel para listar sanciones.
     */
    private JPanel crearPanelListaSanciones() {
        return new PanelListaSanciones();
    }

    /**
     * Crea el panel para aplicar sanciones.
     */
    private JPanel crearPanelSancionar() {
        return new PanelSancionar();
    }

    /**
     * Borde personalizado con esquinas redondeadas.
     */
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
}