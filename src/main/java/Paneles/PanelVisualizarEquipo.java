/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Paneles;

import Clases.Equipos;
import Controles.ControladorEquipo;
import Controles.ControladorHistorialEquipos;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class PanelVisualizarEquipo extends JPanel {
    private JTextField txtIdEquipo;
    private JButton btnBuscar;
    private JLabel lblId, lblProcesador, lblRam, lblDispositivo, lblMonitor, lblTeclado, lblMouse, lblEstado, lblLaboratorio;
    private JTable tablaHistorial;
    private ControladorEquipo controladorEquipo;
    private ControladorHistorialEquipos controladorHistorial;

    public PanelVisualizarEquipo() {
        controladorEquipo = new ControladorEquipo();
        controladorHistorial = new ControladorHistorialEquipos();
        setBackground(new Color(240, 242, 245)); // Fondo gris azulado claro
        setLayout(new BorderLayout());
        initComponents();
    }

    private void initComponents() {
        // Panel contenedor para todo el contenido
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.setBackground(new Color(240, 242, 245));
        containerPanel.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

        // Título
        JLabel titleLabel = new JLabel("Visualizar el Equipo", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        titleLabel.setForeground(new Color(44, 62, 80)); // Azul oscuro
        titleLabel.setBorder(BorderFactory.createEmptyBorder(4, 4, 6, 4));
        containerPanel.add(titleLabel, BorderLayout.NORTH);

        // Panel central (búsqueda e información)
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(new Color(255, 255, 255));
        contentPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
            BorderFactory.createEmptyBorder(6, 6, 6, 6)
        ));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 3, 3, 3);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Búsqueda
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        panelBusqueda.setBackground(new Color(255, 255, 255));
        JLabel lblIdEquipo = new JLabel("ID Equipo:");
        lblIdEquipo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblIdEquipo.setForeground(new Color(44, 62, 80));
        txtIdEquipo = new JTextField(6);
        txtIdEquipo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        txtIdEquipo.setForeground(new Color(44, 62, 80));
        txtIdEquipo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(150, 160, 170), 1, true),
            BorderFactory.createEmptyBorder(3, 4, 3, 4)
        ));
        txtIdEquipo.setBackground(new Color(245, 247, 250)); // Fondo claro
        btnBuscar = new RoundedButton("Buscar");
        btnBuscar.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnBuscar.setBackground(new Color(41, 128, 185)); // Azul medio
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setFocusPainted(false);
        btnBuscar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBuscar.setPreferredSize(new Dimension(70, 24)); // Tamaño mínimo para click
        panelBusqueda.add(lblIdEquipo);
        panelBusqueda.add(txtIdEquipo);
        panelBusqueda.add(btnBuscar);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        contentPanel.add(panelBusqueda, gbc);

        // Información del equipo
        JPanel panelInfo = new JPanel(new GridLayout(9, 2, 5, 5));
        panelInfo.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(150, 160, 170), 1, true),
            "Información del Equipo",
            javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
            javax.swing.border.TitledBorder.DEFAULT_POSITION,
            new Font("Segoe UI", Font.BOLD, 11),
            new Color(44, 62, 80)
        ));
        panelInfo.setBackground(new Color(255, 255, 255));
        panelInfo.setBorder(BorderFactory.createCompoundBorder(
            panelInfo.getBorder(),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        JLabel[] labels = new JLabel[]{
            new JLabel("ID:"), new JLabel("Procesador:"), new JLabel("RAM:"),
            new JLabel("Dispositivo:"), new JLabel("Monitor:"), new JLabel("Teclado:"),
            new JLabel("Mouse:"), new JLabel("Estado:"), new JLabel("Laboratorio:")
        };
        for (JLabel label : labels) {
            label.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            label.setForeground(new Color(44, 62, 80));
        }

        lblId = new JLabel("");
        lblProcesador = new JLabel("");
        lblRam = new JLabel("");
        lblDispositivo = new JLabel("");
        lblMonitor = new JLabel("");
        lblTeclado = new JLabel("");
        lblMouse = new JLabel("");
        lblEstado = new JLabel("");
        lblLaboratorio = new JLabel("");

        JLabel[] valueLabels = new JLabel[]{lblId, lblProcesador, lblRam, lblDispositivo, lblMonitor, lblTeclado, lblMouse, lblEstado, lblLaboratorio};
        for (JLabel label : valueLabels) {
            label.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            label.setForeground(new Color(93, 109, 126)); // Azul grisáceo
            label.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 232, 235), 1, true),
                BorderFactory.createEmptyBorder(2, 4, 2, 4)
            ));
            label.setBackground(new Color(250, 251, 252));
            label.setOpaque(true);
        }

        panelInfo.add(labels[0]); panelInfo.add(lblId);
        panelInfo.add(labels[1]); panelInfo.add(lblProcesador);
        panelInfo.add(labels[2]); panelInfo.add(lblRam);
        panelInfo.add(labels[3]); panelInfo.add(lblDispositivo);
        panelInfo.add(labels[4]); panelInfo.add(lblMonitor);
        panelInfo.add(labels[5]); panelInfo.add(lblTeclado);
        panelInfo.add(labels[6]); panelInfo.add(lblMouse);
        panelInfo.add(labels[7]); panelInfo.add(lblEstado);
        panelInfo.add(labels[8]); panelInfo.add(lblLaboratorio);
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(6, 3, 6, 3);
        contentPanel.add(panelInfo, gbc);

        // Tabla de historial
        tablaHistorial = new JTable();
        tablaHistorial.setModel(new DefaultTableModel(
            new Object[]{"ID", "Fecha", "Categoría", "Descripción"}, 0
        ));
        tablaHistorial.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        tablaHistorial.setRowHeight(20);
        tablaHistorial.setGridColor(new Color(200, 200, 200));
        tablaHistorial.setShowGrid(true);
        tablaHistorial.getTableHeader().setBackground(new Color(44, 62, 80)); // Azul oscuro
        tablaHistorial.getTableHeader().setForeground(Color.WHITE);
        tablaHistorial.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 11));
        tablaHistorial.getTableHeader().setBorder(BorderFactory.createLineBorder(new Color(44, 62, 80)));
        JScrollPane tableScrollPane = new JScrollPane(tablaHistorial);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(150, 160, 170), 1, true),
            "Historial",
            javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
            javax.swing.border.TitledBorder.DEFAULT_POSITION,
            new Font("Segoe UI", Font.BOLD, 11),
            new Color(44, 62, 80)
        ));
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(3, 3, 3, 3);
        contentPanel.add(tableScrollPane, gbc);

        containerPanel.add(contentPanel, BorderLayout.CENTER);

        // Envolver todo el contenido en un JScrollPane
        JScrollPane mainScrollPane = new JScrollPane(containerPanel);
        mainScrollPane.setBorder(BorderFactory.createEmptyBorder());
        mainScrollPane.getVerticalScrollBar().setUnitIncrement(16); // Velocidad de desplazamiento
        add(mainScrollPane, BorderLayout.CENTER);

        // Acción del botón buscar con depuración
        btnBuscar.addActionListener(e -> {
            System.out.println("Botón Buscar clickeado, ID ingresado: " + txtIdEquipo.getText());
            buscarEquipo();
        });
    }

    private void buscarEquipo() {
        String idEquipo = txtIdEquipo.getText().trim();
        if (idEquipo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un ID de equipo.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            System.out.println("Buscando equipo con ID: " + idEquipo);
            Equipos equipo = controladorEquipo.listar().stream()
                .filter(e -> e.getIdEquipos().equals(idEquipo))
                .findFirst()
                .orElse(null);
            if (equipo != null) {
                System.out.println("Equipo encontrado: " + equipo.getIdEquipos());
                lblId.setText(equipo.getIdEquipos());
                lblProcesador.setText(equipo.getProcesador());
                lblRam.setText(equipo.getRam());
                lblDispositivo.setText(equipo.getDispositivo());
                lblMonitor.setText(equipo.getMonitor());
                lblTeclado.setText(equipo.getTeclado());
                lblMouse.setText(equipo.getMouse());
                lblEstado.setText(equipo.getEstado());
                lblLaboratorio.setText(String.valueOf(equipo.getIdLaboratorio()));
                cargarHistorial(idEquipo);
            } else {
                System.out.println("Equipo no encontrado para ID: " + idEquipo);
                JOptionPane.showMessageDialog(this, "Equipo no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                limpiarCampos();
            }
        } catch (SQLException e) {
            System.err.println("Error SQL al buscar equipo: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Error al buscar equipo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            System.err.println("Error inesperado al buscar equipo: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Error inesperado: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarHistorial(String idEquipo) {
        DefaultTableModel modelo = (DefaultTableModel) tablaHistorial.getModel();
        modelo.setRowCount(0);
        try {
            System.out.println("Cargando historial para equipo ID: " + idEquipo);
            List<Object[]> registros = controladorHistorial.buscarHistorialPorEquipo(idEquipo);
            for (Object[] registro : registros) {
                modelo.addRow(new Object[]{
                    registro[0], // id_historial
                    registro[2], // fecha
                    registro[3], // categoria
                    registro[4]  // descripcion
                });
            }
            System.out.println("Historial cargado, filas: " + modelo.getRowCount());
        } catch (SQLException e) {
            System.err.println("Error SQL al cargar historial: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Error al cargar historial: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            System.err.println("Error inesperado al cargar historial: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Error inesperado: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        System.out.println("Limpiando campos");
        lblId.setText("");
        lblProcesador.setText("");
        lblRam.setText("");
        lblDispositivo.setText("");
        lblMonitor.setText("");
        lblTeclado.setText("");
        lblMouse.setText("");
        lblEstado.setText("");
        lblLaboratorio.setText("");
        DefaultTableModel modelo = (DefaultTableModel) tablaHistorial.getModel();
        modelo.setRowCount(0);
    }

    // Clase para botones redondeados
    private class RoundedButton extends JButton {
        public RoundedButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setBorder(new RoundedBorder(14));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (getModel().isPressed()) {
                g2.setColor(getBackground().darker());
            } else if (getModel().isRollover()) {
                g2.setColor(new Color(52, 152, 219)); // Azul más claro al pasar el mouse
            } else {
                g2.setColor(getBackground());
            }
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
            super.paintComponent(g2);
            g2.dispose();
        }
    }

    // Borde redondeado personalizado
    private class RoundedBorder implements Border {
        private int radius;

        public RoundedBorder(int radius) {
            this.radius = radius;
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(5, 8, 5, 8);
        }

        @Override
        public boolean isBorderOpaque() {
            return false;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(150, 160, 170));
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2.dispose();
        }
    }
}