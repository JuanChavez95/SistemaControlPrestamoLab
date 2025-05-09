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

/**
 * Panel para mostrar una tabla de estudiantes desde la base de datos.
 * Diseñado con estilo moderno en Java Swing.
 */
public class PanelEstudiantes extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;

    // Constructor del panel
    public PanelEstudiantes() {
        // Configuración del fondo y borde del panel
        setBackground(new Color(240, 248, 255)); // Fondo azul claro (AliceBlue)
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Etiqueta de título
        JLabel titleLabel = new JLabel("LISTA ESTUDIANTES");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(new Color(25, 25, 112)); // Azul oscuro (MidnightBlue)
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(135, 206, 250)); // Fondo azul cielo
        titleLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 130, 180), 1), // Borde azul acero
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        // Definir las columnas de la tabla
        String[] columnNames = {"RU", "Nombre", "Apellido Paterno", "Apellido Materno", "Email", "Rol"};
        tableModel = new DefaultTableModel(columnNames, 0); // Sin filas iniciales

        // Crear la tabla con el modelo definido
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(28);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setBackground(new Color(245, 245, 255)); // Blanco azulado
        table.setGridColor(new Color(173, 216, 230)); // Líneas de cuadrícula azul claro
        table.setSelectionBackground(new Color(100, 149, 237)); // Color de selección azul
        table.setSelectionForeground(Color.WHITE);

        // Personalizar el encabezado de la tabla
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(70, 130, 180)); // Azul acero
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setBorder(BorderFactory.createLineBorder(new Color(25, 25, 112)));

        // Colocar la tabla dentro de un JScrollPane con bordes personalizados
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(5, 5, 5, 5),
                BorderFactory.createLineBorder(new Color(135, 206, 250), 2, true))); // Borde azul claro redondeado
        scrollPane.getViewport().setBackground(new Color(240, 248, 255)); // Fondo del viewport

        // Añadir componentes al panel principal
        add(titleLabel, BorderLayout.NORTH); // Título en la parte superior
        add(scrollPane, BorderLayout.CENTER); // Tabla al centro

        // Configurar el borde externo del panel con bordes redondeados
        setOpaque(false);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 130, 180), 2, true),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        // Cargar los datos desde la base de datos al iniciar el panel
        cargarDatosDesdeBD();
    }

    /**
     * Sobrescribe paintComponent para dibujar un fondo con esquinas redondeadas
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20); // Dibuja fondo con esquinas redondeadas
        g2.dispose();
        super.paintComponent(g);
    }

    /**
     * Método para conectar con la base de datos y cargar la lista de estudiantes en la tabla.
     */
    private void cargarDatosDesdeBD() {
        String url = "jdbc:mysql://localhost:3306/prestamo_controles_lab1111";
        String usuario = "root";
        String contraseña = "root";

        try (Connection conn = DriverManager.getConnection(url, usuario, contraseña)) {
            // Consulta SQL para obtener estudiantes
            String sql = "SELECT ru, nombre, apellido_paterno, apellido_materno, email, rol FROM usuario WHERE rol = 'ESTUDIANTE'";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            // Recorrer los resultados y añadirlos a la tabla
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
            // Mostrar error en un cuadro de diálogo en caso de fallo de conexión o consulta
            JOptionPane.showMessageDialog(this, "Error al cargar datos: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
