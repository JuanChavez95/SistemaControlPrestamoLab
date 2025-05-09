/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Paneles;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*;

public class PanelEstudiantes extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;

    public PanelEstudiantes() {
        // Configurar el panel con bordes redondeados
        setBackground(new Color(240, 248, 255)); // Fondo azul claro (AliceBlue)
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Título
        JLabel titleLabel = new JLabel("LISTA ESTUDIANTES");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(new Color(25, 25, 112)); // Azul oscuro (MidnightBlue)
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        titleLabel.setBackground(new Color(135, 206, 250)); // Fondo azul cielo
        titleLabel.setOpaque(true);
        titleLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 130, 180), 1), // Borde azul acero
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        // Configuración de la tabla
        String[] columnNames = {"RU", "Nombre", "Apellido Paterno", "Apellido Materno", "Email", "Rol"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(28);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setBackground(new Color(245, 245, 255)); // Fondo blanco azulado
        table.setGridColor(new Color(173, 216, 230)); // Líneas de cuadrícula azul claro
        table.setSelectionBackground(new Color(100, 149, 237)); // Selección azul
        table.setSelectionForeground(Color.WHITE);

        // Personalizar el encabezado de la tabla
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(70, 130, 180)); // Azul acero
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setBorder(BorderFactory.createLineBorder(new Color(25, 25, 112)));

        // ScrollPane con bordes redondeados
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(5, 5, 5, 5),
                BorderFactory.createLineBorder(new Color(135, 206, 250), 2, true))); // Borde redondeado
        scrollPane.getViewport().setBackground(new Color(240, 248, 255));

        // Añadir componentes al panel
        add(titleLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Hacer que el panel tenga bordes redondeados
        setOpaque(false);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 130, 180), 2, true),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        cargarDatosDesdeBD();
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

    private void cargarDatosDesdeBD() {
        String url = "jdbc:mysql://localhost:3306/prestamo_controles_lab1111";
        String usuario = "root";
        String contraseña = "root";

        try (Connection conn = DriverManager.getConnection(url, usuario, contraseña)) {
            String sql = "SELECT ru, nombre, apellido_paterno, apellido_materno, email, rol FROM usuario WHERE rol = 'ESTUDIANTE'";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Object[] fila = {
                    rs.getInt("ru"),
                    rs.getString("nombre"),
                    rs.getString("apellido_paterno"),
                    rs.getString("apellido_materno"),
                    rs.getString("email"),
                    rs.getString("rol")
                };
                tableModel.addRow(fila);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos: " + e.getMessage());
            e.printStackTrace();
        }
    }
}