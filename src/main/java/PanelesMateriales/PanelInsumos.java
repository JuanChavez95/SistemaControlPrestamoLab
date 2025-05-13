/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PanelesMateriales;
/**
 *
 * @author DOC
 */
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*;
import DataBase.ConexionBD; // Importando la clase ConexionBD

public class PanelInsumos extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;

    public PanelInsumos() {
        setLayout(new BorderLayout());
        setBackground(new Color(230, 240, 255)); // Fondo general claro

        // PANEL DE TÍTULO AZUL
        JPanel panelTitulo = new JPanel(new BorderLayout());
        panelTitulo.setBackground(new Color(153, 204, 255)); // Azul claro
        panelTitulo.setBorder(BorderFactory.createLineBorder(new Color(100, 150, 220), 1));

        JLabel titleLabel = new JLabel("LISTA INSUMOS", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(33, 60, 130));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panelTitulo.add(titleLabel, BorderLayout.CENTER);

        // CONFIGURACIÓN DE LA TABLA
        String[] columnNames = {"Id. Insumo", "Nombre", "Cantidad", "Categoría", "Id. Laboratorio", "Disponibilidad"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(25);
        table.setGridColor(Color.LIGHT_GRAY);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(30, 80, 160));
        header.setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(new Color(245, 250, 255));
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(180, 200, 240), 2));

        // PANEL CONTENEDOR GENERAL
        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.setBackground(new Color(200, 225, 250));
        contenedor.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        contenedor.add(panelTitulo, BorderLayout.NORTH);
        contenedor.add(scrollPane, BorderLayout.CENTER);

        add(contenedor, BorderLayout.CENTER);

        cargarDatosDesdeBD(); // Funcionalidad intacta
    }

    private void cargarDatosDesdeBD() {
        try (Connection conn = ConexionBD.conectar()) { 
            String sql = "SELECT id_insumo, nombre_insumo, cantidad, categoria, id_laboratorio, disponibilidad FROM insumos";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Object[] fila = {
                    rs.getInt("id_insumo"),
                    rs.getString("nombre_insumo"),
                    rs.getString("cantidad"),
                    rs.getString("categoria"),
                    rs.getInt("id_laboratorio"),
                    rs.getString("disponibilidad")
                };
                tableModel.addRow(fila);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
