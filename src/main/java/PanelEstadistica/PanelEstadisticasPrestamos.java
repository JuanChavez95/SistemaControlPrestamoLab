/*
 * Panel para mostrar estadísticas de préstamos con gráficos de barras y torta usando JFreeChart.
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
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

public class PanelEstadisticasPrestamos extends JPanel {
    private static final Logger LOGGER = Logger.getLogger(PanelEstadisticasPrestamos.class.getName());
    private final ControladorPrestamo controladorPrestamo;

    // Color Palette
    private static final Color PRIMARY_COLOR = new Color(25, 118, 210); // Azul vibrante
    private static final Color SECONDARY_COLOR = new Color(56, 142, 60); // Verde
    private static final Color ACCENT_COLOR = new Color(142, 36, 170); // Morado
    private static final Color BACKGROUND_COLOR = Color.WHITE;
    private static final Color CARD_COLOR = new Color(240, 242, 245); // Fondo claro
    private static final Color TEXT_COLOR = new Color(33, 33, 33);
    private static final Color SHADOW_COLOR = new Color(100, 100, 100, 100); // Sombra

    public PanelEstadisticasPrestamos() {
        controladorPrestamo = new ControladorPrestamo();
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(BACKGROUND_COLOR);
        setBorder(new EmptyBorder(15, 15, 15, 15));

        // Title
        JLabel titleLabel = new JLabel("Estadísticas de Préstamos", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setBorder(new EmptyBorder(10, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        // Chart Panel
        JPanel chartPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        chartPanel.setBackground(CARD_COLOR);
        chartPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
            new EmptyBorder(10, 10, 10, 10)
        ));

        try {
            // Create charts
            JFreeChart barChart = createBarChart();
            JFreeChart pieChart = createPieChart();

            // Add charts to panel
            ChartPanel barChartPanel = new ChartPanel(barChart);
            ChartPanel pieChartPanel = new ChartPanel(pieChart);
            barChartPanel.setPreferredSize(new Dimension(400, 300));
            pieChartPanel.setPreferredSize(new Dimension(400, 300));
            chartPanel.add(barChartPanel);
            chartPanel.add(pieChartPanel);

            add(chartPanel, BorderLayout.CENTER);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al cargar estadísticas de préstamos", ex);
            JOptionPane.showMessageDialog(this, "Error al cargar estadísticas: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
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
        renderer.setSeriesPaint(0, PRIMARY_COLOR);
        renderer.setSeriesPaint(1, SECONDARY_COLOR);
        renderer.setSeriesPaint(2, ACCENT_COLOR);
        renderer.setDrawBarOutline(false);
        renderer.setMaximumBarWidth(0.15); // Barras más anchas
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

        JFreeChart chart = ChartFactory.createPieChart(
            "Estados de Préstamos",
            dataset,
            true,
            true,
            false
        );
        chart.setBackgroundPaint(CARD_COLOR);
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setSectionPaint("Pendiente", new Color(255, 215, 0)); // Amarillo vibrante
        plot.setSectionPaint("Aceptado", new Color(46, 125, 50)); // Verde
        plot.setSectionPaint("Rechazado", new Color(211, 47, 47)); // Rojo
        plot.setSectionPaint("Terminado", new Color(120, 144, 156)); // Gris azulado
        plot.setShadowPaint(SHADOW_COLOR);
        plot.setShadowXOffset(4);
        plot.setShadowYOffset(4);
        plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 11));
        plot.setLabelPaint(TEXT_COLOR);
        plot.setBackgroundPaint(new Color(245, 245, 245));
        PieSectionLabelGenerator labelGenerator = new StandardPieSectionLabelGenerator("{0}: {1}");
        plot.setLabelGenerator(labelGenerator);
        return chart;
    }
}