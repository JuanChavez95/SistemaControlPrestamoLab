/*
 * Panel para mostrar estadísticas de préstamos con gráficos de barras y torta 3D usando JFreeChart.
 */
package PanelEstadistica;

import Clases.Prestamo;
import Controles.ControladorPrestamo;
import java.awt.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.logging.Level;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.util.Rotation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

public class PanelEstadisticasPrestamos extends JPanel {
    private static final Logger LOGGER = Logger.getLogger(PanelEstadisticasPrestamos.class.getName());
    private final ControladorPrestamo controladorPrestamo;

    // Componentes de la interfaz
    private JPanel panelGraficos;
    private JPanel panelResumen;
    private JLabel lblTotalPrestamos;
    private JLabel lblPrestamosPendientes;
    private JLabel lblPrestamosAceptados;
    private JLabel lblPrestamosRechazados;
    private JLabel lblPrestamosTerminados;

    // Color Palette
    private static final Color PRIMARY_COLOR = new Color(0, 128, 0); // Verde
    private static final Color SECONDARY_COLOR = new Color(255, 165, 0); // Naranja
    private static final Color ACCENT_COLOR = new Color(178, 34, 34); // Rojo oscuro
    private static final Color BACKGROUND_COLOR = Color.WHITE;
    private static final Color CARD_COLOR = new Color(240, 242, 245); // Fondo claro
    private static final Color TEXT_COLOR = new Color(33, 33, 33);
    private static final Color SHADOW_COLOR = new Color(100, 100, 100, 100); // Sombra

    public PanelEstadisticasPrestamos() {
        controladorPrestamo = new ControladorPrestamo();
        initComponents();
        cargarEstadisticas();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Title
        JLabel titleLabel = new JLabel("Estadísticas de Préstamos", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setBorder(new EmptyBorder(10, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        // Panel de gráficos
        panelGraficos = new JPanel(new GridLayout(1, 2, 15, 15));
        panelGraficos.setBackground(CARD_COLOR);
        panelGraficos.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // Panel de resumen
        panelResumen = new JPanel();
        panelResumen.setLayout(new BoxLayout(panelResumen, BoxLayout.Y_AXIS));
        panelResumen.setBackground(CARD_COLOR);
        panelResumen.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Resumen de Préstamos",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14)
        ));

        // Etiquetas para el resumen
        lblTotalPrestamos = crearLabelEstadistica("Total de Préstamos: ");
        lblPrestamosPendientes = crearLabelEstadistica("Préstamos Pendientes: ");
        lblPrestamosAceptados = crearLabelEstadistica("Préstamos Aceptados: ");
        lblPrestamosRechazados = crearLabelEstadistica("Préstamos Rechazados: ");
        lblPrestamosTerminados = crearLabelEstadistica("Préstamos Terminados: ");

        // Agregar etiquetas al panel de resumen
        panelResumen.add(Box.createVerticalStrut(15));
        panelResumen.add(lblTotalPrestamos);
        panelResumen.add(Box.createVerticalStrut(10));
        panelResumen.add(lblPrestamosPendientes);
        panelResumen.add(Box.createVerticalStrut(10));
        panelResumen.add(lblPrestamosAceptados);
        panelResumen.add(Box.createVerticalStrut(10));
        panelResumen.add(lblPrestamosRechazados);
        panelResumen.add(Box.createVerticalStrut(10));
        panelResumen.add(lblPrestamosTerminados);
        panelResumen.add(Box.createVerticalStrut(15));

        // Agregar componentes al panel principal
        add(panelGraficos, BorderLayout.CENTER);
        add(panelResumen, BorderLayout.SOUTH);
    }

    private JLabel crearLabelEstadistica(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    public void cargarEstadisticas() {
        try {
            // Obtener la lista de préstamos
            java.util.List<Prestamo> prestamos = controladorPrestamo.listar();

            // Contadores para las estadísticas
            int totalPrestamos = prestamos.size();
            int prestamosPendientes = 0;
            int prestamosAceptados = 0;
            int prestamosRechazados = 0;
            int prestamosTerminados = 0;

            // Procesar cada préstamo
            for (Prestamo p : prestamos) {
                switch (p.getEstadoPrestamo().toLowerCase()) {
                    case "pendiente":
                        prestamosPendientes++;
                        break;
                    case "aceptado":
                        prestamosAceptados++;
                        break;
                    case "rechazado":
                        prestamosRechazados++;
                        break;
                    case "terminado":
                        prestamosTerminados++;
                        break;
                }
            }

            // Actualizar las etiquetas de resumen
            lblTotalPrestamos.setText("Total de Préstamos: " + totalPrestamos);
            lblPrestamosPendientes.setText("Préstamos Pendientes: " + prestamosPendientes +
                    " (" + calcularPorcentaje(prestamosPendientes, totalPrestamos) + "%)");
            lblPrestamosAceptados.setText("Préstamos Aceptados: " + prestamosAceptados +
                    " (" + calcularPorcentaje(prestamosAceptados, totalPrestamos) + "%)");
            lblPrestamosRechazados.setText("Préstamos Rechazados: " + prestamosRechazados +
                    " (" + calcularPorcentaje(prestamosRechazados, totalPrestamos) + "%)");
            lblPrestamosTerminados.setText("Préstamos Terminados: " + prestamosTerminados +
                    " (" + calcularPorcentaje(prestamosTerminados, totalPrestamos) + "%)");

            // Limpiar el panel de gráficos
            panelGraficos.removeAll();

            // Crear y añadir gráficos
            JFreeChart barChart = createBarChart();
            JFreeChart pieChart = createPieChart();

            ChartPanel barChartPanel = new ChartPanel(barChart);
            ChartPanel pieChartPanel = new ChartPanel(pieChart);
            barChartPanel.setPreferredSize(new Dimension(400, 300));
            pieChartPanel.setPreferredSize(new Dimension(400, 300));
            barChartPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            pieChartPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            panelGraficos.add(barChartPanel);
            panelGraficos.add(pieChartPanel);

            // Refrescar la interfaz
            revalidate();
            repaint();

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al cargar estadísticas de préstamos", ex);
            JOptionPane.showMessageDialog(this, "Error al cargar estadísticas: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private double calcularPorcentaje(int parte, int total) {
        if (total == 0) return 0;
        return Math.round((double) parte / total * 1000) / 10.0;
    }

    private JFreeChart createBarChart() throws SQLException {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Map<String, Integer> loansPerMonth = new HashMap<>();

        // Initialize months
        String[] months = {"Ene", "Feb", "Mar", "Abr", "May", "Jun",
                           "Jul", "Ago", "Sep", "Oct", "Nov", "Dic"};
        for (String month : months) {
            loansPerMonth.put(month, 0);
        }

        // Count loans per month
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMM");
        for (Prestamo p : controladorPrestamo.listar()) {
            String month = monthFormat.format(p.getFechaPrestamo());
            loansPerMonth.put(month, loansPerMonth.getOrDefault(month, 0) + 1);
        }

        // Add to dataset
        for (String month : months) {
            dataset.addValue(loansPerMonth.get(month), "Préstamos", month);
        }

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
        chart.setBackgroundPaint(CARD_COLOR);
        CategoryPlot plot = chart.getCategoryPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, PRIMARY_COLOR); // Verde
        renderer.setSeriesPaint(1, SECONDARY_COLOR); // Naranja
        renderer.setSeriesPaint(2, ACCENT_COLOR); // Rojo oscuro
        renderer.setDrawBarOutline(false);
        renderer.setMaximumBarWidth(0.15); // Barras anchas
        renderer.setShadowVisible(true);
        renderer.setShadowPaint(SHADOW_COLOR);

        plot.setRenderer(0, renderer, true);
        plot.setRangeGridlinePaint(new Color(200, 200, 200));
        plot.setDomainGridlinesVisible(true);
        plot.setBackgroundPaint(new Color(245, 245, 245));
        return chart;
    }

    private JFreeChart createPieChart() throws SQLException {
        DefaultPieDataset dataset = new DefaultPieDataset();
        Map<String, Integer> statusCounts = new HashMap<>();
        String[] statuses = {"Pendiente", "Aceptado", "Rechazado", "Terminado"};
        for (String status : statuses) {
            statusCounts.put(status, 0);
        }

        for (Prestamo p : controladorPrestamo.listar()) {
            String status = p.getEstadoPrestamo();
            statusCounts.put(status, statusCounts.getOrDefault(status, 0) + 1);
        }

        statusCounts.forEach(dataset::setValue);

        JFreeChart chart = ChartFactory.createPieChart3D(
            "Estados de Préstamos",
            dataset,
            true,
            true,
            false
        );
        chart.setBackgroundPaint(CARD_COLOR);
        PiePlot3D plot = (PiePlot3D) chart.getPlot();
        plot.setSectionPaint("Pendiente", new Color(255, 215, 0)); // Amarillo vibrante
        plot.setSectionPaint("Aceptado", new Color(46, 125, 50)); // Verde
        plot.setSectionPaint("Rechazado", new Color(211, 47, 47)); // Rojo
        plot.setSectionPaint("Terminado", new Color(120, 144, 156)); // Gris azulado
        plot.setStartAngle(290);
        plot.setDirection(Rotation.CLOCKWISE);
        plot.setForegroundAlpha(0.8f);
        plot.setShadowPaint(SHADOW_COLOR);
        plot.setLabelFont(new Font("Arial", Font.PLAIN, 11));
        plot.setLabelPaint(TEXT_COLOR);
        plot.setBackgroundPaint(new Color(245, 245, 245));
        PieSectionLabelGenerator labelGenerator = new StandardPieSectionLabelGenerator("{0}: {1}");
        plot.setLabelGenerator(labelGenerator);
        return chart;
    }

    public void actualizarEstadisticas() {
        cargarEstadisticas();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        int w = getWidth(), h = getHeight();
        Color color1 = BACKGROUND_COLOR;
        Color color2 = new Color(230, 230, 230);
        GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, w, h);
    }
}