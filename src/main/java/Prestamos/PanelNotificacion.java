/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Prestamos;

import Clases.Prestamo;
import Controles.ControladorPrestamo;
import DataBase.ConexionBD;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

public class PanelNotificacion extends JPanel {
    private static final Logger LOGGER = Logger.getLogger(PanelNotificacion.class.getName());
    private JTable tablaNotificaciones;
    private DefaultTableModel modeloNotificaciones;
    private ControladorPrestamo controladorPrestamo;
    private Timer timer;
    private int ruUsuario;
    private volatile boolean isActive;
    private ConexionBD conexionbd;

    public PanelNotificacion() {
        this.ruUsuario = -1;
        controladorPrestamo = new ControladorPrestamo();
        conexionbd = new ConexionBD();
        initComponents();
    }

    public PanelNotificacion(int ruUsuario) {
        this.ruUsuario = ruUsuario;
        controladorPrestamo = new ControladorPrestamo();
        conexionbd = new ConexionBD();
        initComponents();
    }

    public void setRuUsuario(int ruUsuario) {
        this.ruUsuario = ruUsuario;
        buscarNotificaciones(ruUsuario);
    }

    private void initComponents() {
        isActive = true;
        setLayout(new BorderLayout());
        setBackground(new Color(241, 243, 249)); // Fondo más limpio y moderno
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Título con diseño minimalista y elegante
        JLabel titleLabel = new JLabel("Notificaciones de Préstamos", SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0, 0, new Color(25, 118, 210), getWidth(), 0, new Color(33, 150, 243)));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setOpaque(false);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(titleLabel, BorderLayout.NORTH);

        modeloNotificaciones = new DefaultTableModel(new String[]{
                "ID Préstamo", "Fecha Préstamo", "Hora Préstamo", "Fecha Devolución", "Hora Devolución", "Estado", "Tiempo Restante", "Equipamientos", "Insumos",
                "Fecha Devolución Esperada", "Hora Devolución Esperada"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaNotificaciones = new JTable(modeloNotificaciones) {
            @Override
            public TableCellRenderer getCellRenderer(int row, int column) {
                return new CustomTableCellRenderer();
            }
        };

        // Ocultar columnas adicionales
        tablaNotificaciones.getColumnModel().getColumn(9).setMinWidth(0);
        tablaNotificaciones.getColumnModel().getColumn(9).setMaxWidth(0);
        tablaNotificaciones.getColumnModel().getColumn(9).setWidth(0);
        tablaNotificaciones.getColumnModel().getColumn(10).setMinWidth(0);
        tablaNotificaciones.getColumnModel().getColumn(10).setMaxWidth(0);
        tablaNotificaciones.getColumnModel().getColumn(10).setWidth(0);

        // Estilo mejorado de la tabla
        tablaNotificaciones.setRowHeight(40);
        tablaNotificaciones.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tablaNotificaciones.setGridColor(new Color(230, 234, 240));
        tablaNotificaciones.setShowGrid(true);
        tablaNotificaciones.setSelectionBackground(new Color(187, 222, 251));
        tablaNotificaciones.setSelectionForeground(Color.BLACK);
        tablaNotificaciones.setIntercellSpacing(new Dimension(10, 10)); // Espaciado interno

        // Estilo del encabezado
        tablaNotificaciones.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        tablaNotificaciones.getTableHeader().setBackground(new Color(25, 118, 210));
        tablaNotificaciones.getTableHeader().setForeground(Color.WHITE);
        tablaNotificaciones.getTableHeader().setReorderingAllowed(false);
        tablaNotificaciones.getTableHeader().setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // JScrollPane con diseño moderno
        JScrollPane scrollPane = new JScrollPane(tablaNotificaciones) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        scrollPane.setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10),
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true)
        ));
        scrollPane.setOpaque(true);
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        if (ruUsuario != -1) {
            buscarNotificaciones(ruUsuario);
        }

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (isActive) {
                    SwingUtilities.invokeLater(() -> actualizarTiempoRestante());
                }
            }
        }, 0, 1000);
    }

    private String[] obtenerHorario(Integer idHorario) throws SQLException {
        if (idHorario == null) {
            return new String[]{null, null};
        }

        String sql = "SELECT dia, hora FROM horario WHERE id_horario = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String dia = null;
        String hora = null;

        try {
            conn = conexionbd.conectar();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idHorario);
            rs = stmt.executeQuery();

            if (rs.next()) {
                dia = rs.getString("dia");
                hora = rs.getString("hora");
            }
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    LOGGER.warning("Error al cerrar ResultSet: " + e.getMessage());
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    LOGGER.warning("Error al cerrar PreparedStatement: " + e.getMessage());
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    LOGGER.warning("Error al cerrar Connection: " + e.getMessage());
                }
            }
        }
        return new String[]{dia, hora};
    }

    private void buscarNotificaciones(int ru) {
        try {
            List<Prestamo> prestamos = controladorPrestamo.listarPorUsuario(ru);
            modeloNotificaciones.setRowCount(0);
            for (Prestamo p : prestamos) {
                List<Integer> equipamientoIds = controladorPrestamo.obtenerEquipamientosPrestamo(p.getIdPrestamo());
                Map<Integer, Integer> insumoCantidades = controladorPrestamo.obtenerInsumosPrestamoConCantidades(p.getIdPrestamo());
                String equipamientosStr = equipamientoIds.isEmpty() ? "Ninguno" : equipamientoIds.toString();
                String insumosStr = insumoCantidades.isEmpty() ? "Ninguno" : insumoCantidades.entrySet().stream()
                        .map(entry -> "ID " + entry.getKey() + ": " + entry.getValue())
                        .reduce((a, b) -> a + ", " + b)
                        .orElse("Ninguno");

                String[] horarioInfo = obtenerHorario(p.getIdHorario());
                String fechaDevolucionEsperada = horarioInfo[0] != null ? horarioInfo[0] : new SimpleDateFormat("dd/MM/yyyy").format(p.getFechaPrestamo());
                String horaDevolucionEsperada = horarioInfo[1] != null ? horarioInfo[1] : "Desconocido";

                modeloNotificaciones.addRow(new Object[]{
                        p.getIdPrestamo(),
                        new SimpleDateFormat("dd/MM/yyyy").format(p.getFechaPrestamo()),
                        p.getHoraPrestamo(),
                        fechaDevolucionEsperada,
                        horaDevolucionEsperada,
                        p.getEstadoPrestamo(),
                        "",
                        equipamientosStr,
                        insumosStr,
                        fechaDevolucionEsperada,
                        horaDevolucionEsperada
                });
            }
            if (prestamos.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No se encontraron notificaciones para el usuario con RU " + ru + ".", "Información", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            LOGGER.severe("Error al cargar notificaciones: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, "Error al cargar notificaciones: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarTiempoRestante() {
        for (int row = 0; row < modeloNotificaciones.getRowCount(); row++) {
            String fechaDevolucionStr = (String) modeloNotificaciones.getValueAt(row, 9);
            String horaDevolucion = (String) modeloNotificaciones.getValueAt(row, 10);
            String estado = (String) modeloNotificaciones.getValueAt(row, 5);

            if ("TERMINADO".equalsIgnoreCase(estado) || "RECHAZADO".equalsIgnoreCase(estado)) {
                modeloNotificaciones.setValueAt("N/A", row, 6);
                continue;
            }

            try {
                if (horaDevolucion == null || !horaDevolucion.matches("\\d{1,2}:\\d{2}\\sA\\s\\d{1,2}:\\d{2}")) {
                    modeloNotificaciones.setValueAt("Formato de hora inválido", row, 6);
                    continue;
                }

                String horaFinDevolucion = horaDevolucion.split(" A ")[1];
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                String fechaHoraDevolucionStr = fechaDevolucionStr + " " + horaFinDevolucion;
                LocalDateTime fechaDevolucion = LocalDateTime.parse(fechaHoraDevolucionStr, formatter);
                LocalDateTime ahora = LocalDateTime.now();

                Duration duracion = Duration.between(ahora, fechaDevolucion);
                if (duracion.isNegative()) {
                    modeloNotificaciones.setValueAt("Vencido", row, 6);
                } else {
                    long dias = duracion.toDays();
                    long horas = duracion.toHoursPart();
                    long minutos = duracion.toMinutesPart();
                    long segundos = duracion.toSecondsPart();
                    String tiempoRestante = dias > 0
                            ? String.format("%dd %dh %dm %ds", dias, horas, minutos, segundos)
                            : String.format("%dh %dm %ds", horas, minutos, segundos);
                    modeloNotificaciones.setValueAt(tiempoRestante, row, 6);
                }
            } catch (Exception ex) {
                LOGGER.warning("Error al calcular tiempo restante para la fila " + row + ": " + ex.getMessage());
                modeloNotificaciones.setValueAt("Error", row, 6);
            }
        }
    }

    class CustomTableCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            String tiempoRestante = (String) table.getValueAt(row, 6);
            String estado = (String) table.getValueAt(row, 5);

            setHorizontalAlignment(SwingConstants.CENTER);
            setFont(new Font("Segoe UI", Font.PLAIN, 14));

            // Fondo alternado para filas con transición suave
            if (!isSelected) {
                c.setBackground(row % 2 == 0 ? new Color(250, 251, 255) : Color.WHITE);
            }

            // Estilo para la columna "Estado" con colores más vibrantes
            if (column == 5) {
                switch (estado.toUpperCase()) {
                    case "PENDIENTE":
                        c.setBackground(new Color(255, 245, 157)); // Amarillo suave
                        c.setForeground(new Color(33, 33, 33));
                        break;
                    case "ACEPTADO":
                        c.setBackground(new Color(165, 214, 167)); // Verde fresco
                        c.setForeground(new Color(33, 33, 33));
                        break;
                    case "RECHAZADO":
                        c.setBackground(new Color(239, 154, 154)); // Rojo suave
                        c.setForeground(new Color(33, 33, 33));
                        break;
                    case "TERMINADO":
                        c.setBackground(new Color(189, 195, 199)); // Gris elegante
                        c.setForeground(new Color(33, 33, 33));
                        break;
                    default:
                        c.setBackground(Color.WHITE);
                        c.setForeground(new Color(33, 33, 33));
                        break;
                }
            } else {
                // Resaltar "Tiempo Restante" si está vencido
                if ("Vencido".equals(tiempoRestante)) {
                    c.setBackground(new Color(239, 154, 154));
                    c.setForeground(new Color(33, 33, 33));
                } else {
                    c.setForeground(new Color(33, 33, 33));
                }
            }

            // Bordes suaves para celdas
            setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            return c;
        }
    }

    @Override
    public void removeNotify() {
        super.removeNotify();
        isActive = false;
        timer.cancel();
    }
}