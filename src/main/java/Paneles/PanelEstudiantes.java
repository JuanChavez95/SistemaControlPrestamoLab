/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Paneles;

/**
 *
 * @author DOC
 */
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class PanelEstudiantes extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;

    public PanelEstudiantes() {
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("LISTA ESTUDIANTES");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(new Color(50, 50, 150));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));

        // Columnas que se mostrarán en tabla
        String[] columnNames = {"RU", "Nombre", "Apellido Paterno", "Apellido Materno", "Email", "Rol"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(24);
        table.setFont(new Font("Arial", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(table);

        add(titleLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        cargarDatosDesdeBD();
    }

    private void cargarDatosDesdeBD() {
        String url = "jdbc:mysql://localhost:3306/prestamos_controles_laboratorio";
        String usuario = "root";
        String contraseña = "grand batle124"; 

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