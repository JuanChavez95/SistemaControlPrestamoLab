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
        setBackground(new Color(245, 248, 255)); // Fondo más suave y profesional
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Título con fondo degradado y bordes redondeados
        JLabel titleLabel = new JLabel("Notificaciones de Préstamos", SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0, 0, new Color(33, 97, 140), getWidth(), 0, new Color(66, 146, 198)));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setOpaque(false);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(titleLabel, BorderLayout.NORTH);

        modeloNotificaciones = new DefaultTableModel(new String[]{
                "ID Préstamo", "Fecha Préstamo", "Hora Préstamo", "Fecha Devolución", "Hora Devolución", "Estado", "Tiempo Restante", "Equipamientos", "Insumos",
                "Fecha Devolución Esperada", "Hora Devolución Esperada" // Columnas ocultas para el cálculo
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

        // Ocultar las columnas adicionales
        tablaNotificaciones.getColumnModel().getColumn(9).setMinWidth(0);
        tablaNotificaciones.getColumnModel().getColumn(9).setMaxWidth(0);
        tablaNotificaciones.getColumnModel().getColumn(9).setWidth(0);
        tablaNotificaciones.getColumnModel().getColumn(10).setMinWidth(0);
        tablaNotificaciones.getColumnModel().getColumn(10).setMaxWidth(0);
        tablaNotificaciones.getColumnModel().getColumn(10).setWidth(0);

        // Estilo de la tabla
        tablaNotificaciones.setRowHeight(35);
        tablaNotificaciones.setFont(new Font("Roboto", Font.PLAIN, 14));
        tablaNotificaciones.setGridColor(new Color(220, 220, 220));
        tablaNotificaciones.setShowGrid(true);
        tablaNotificaciones.setSelectionBackground(new Color(200, 220, 255));
        tablaNotificaciones.setSelectionForeground(Color.BLACK);

        // Estilo del encabezado
        tablaNotificaciones.getTableHeader().setFont(new Font("Roboto", Font.BOLD, 14));
        tablaNotificaciones.getTableHeader().setBackground(new Color(33, 97, 140));
        tablaNotificaciones.getTableHeader().setForeground(Color.WHITE);
        tablaNotificaciones.getTableHeader().setReorderingAllowed(false);
        tablaNotificaciones.getTableHeader().setBorder(BorderFactory.createEmptyBorder());

        // JScrollPane con bordes redondeados y sombreado
        JScrollPane scrollPane = new JScrollPane(tablaNotificaciones) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        scrollPane.setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10),
                BorderFactory.createLineBorder(new Color(150, 150, 150), 1, true) // Borde redondeado
        ));
        scrollPane.setOpaque(false);
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

                // Obtener el horario asociado al préstamo
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
                        "", // Tiempo Restante (se calculará dinámicamente)
                        equipamientosStr,
                        insumosStr,
                        fechaDevolucionEsperada, // Columna oculta para cálculo
                        horaDevolucionEsperada    // Columna oculta para cálculo
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
            String fechaDevolucionStr = (String) modeloNotificaciones.getValueAt(row, 9); // Columna oculta
            String horaDevolucion = (String) modeloNotificaciones.getValueAt(row, 10);   // Columna oculta
            String estado = (String) modeloNotificaciones.getValueAt(row, 5);

            if ("TERMINADO".equalsIgnoreCase(estado) || "RECHAZADO".equalsIgnoreCase(estado)) {
                modeloNotificaciones.setValueAt("N/A", row, 6);
                continue;
            }

            try {
                // Validar el formato de horaDevolucion (esperado: "7:30 A 9:00")
                if (horaDevolucion == null || !horaDevolucion.matches("\\d{1,2}:\\d{2}\\sA\\s\\d{1,2}:\\d{2}")) {
                    modeloNotificaciones.setValueAt("Formato de hora inválido", row, 6);
                    continue;
                }

                // Usar la hora de fin del rango para el cálculo (por ejemplo, "9:00" en "7:30 A 9:00")
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
            setFont(new Font("Roboto", Font.PLAIN, 14));

            // Fondo alternado para filas
            if (!isSelected) {
                if (row % 2 == 0) {
                    c.setBackground(new Color(245, 245, 245));
                } else {
                    c.setBackground(Color.WHITE);
                }
            }

            // Colores para la columna "Estado" (columna 5)
            if (column == 5) {
                switch (estado.toUpperCase()) {
                    case "PENDIENTE":
                        c.setBackground(new Color(255, 243, 176)); // Amarillo claro
                        c.setForeground(Color.BLACK);
                        break;
                    case "ACEPTADO":
                        c.setBackground(new Color(198, 246, 213)); // Verde claro
                        c.setForeground(Color.BLACK);
                        break;
                    case "RECHAZADO":
                        c.setBackground(new Color(255, 204, 204)); // Rojo claro
                        c.setForeground(Color.BLACK);
                        break;
                    case "TERMINADO":
                        c.setBackground(new Color(220, 220, 220)); // Gris claro
                        c.setForeground(Color.BLACK);
                        break;
                    default:
                        c.setBackground(Color.WHITE);
                        c.setForeground(Color.BLACK);
                        break;
                }
            } else {
                // Resaltar "Tiempo Restante" si está vencido
                if ("Vencido".equals(tiempoRestante)) {
                    c.setBackground(new Color(255, 204, 204));
                    c.setForeground(Color.BLACK);
                } else {
                    c.setForeground(Color.BLACK);
                }
            }

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