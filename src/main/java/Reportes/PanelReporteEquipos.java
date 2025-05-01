/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package Reportes;

import javax.swing.*;
import java.awt.*;

public class PanelReporteEquipos extends JPanel {
    
    public PanelReporteEquipos() {
        // Configuración del panel
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        
        // Aquí agregarías todos los componentes específicos
        // para la gestión de docentes (tablas, formularios, botones, etc.)
        JLabel titleLabel = new JLabel("Reporte de Equipos");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel central con la tabla o formulario principal
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(Color.WHITE);
        // Aquí agregarías los componentes específicos
        contentPanel.add(new JLabel("Contenido para generar reportes de equipos"));
        
        // Agregar componentes al panel principal
        add(titleLabel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }
}


/*
package Reportes;

import javax.swing.*;
import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.List;

public class PanelReporteEquipos extends JPanel {

    public PanelReporteEquipos() {
        // Configuración del panel
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        // Título
        JLabel titleLabel = new JLabel("Reporte de Equipos");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel de contenido
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(Color.WHITE);

        // Campo para ingresar el ID del equipo
        JTextField idEquipoField = new JTextField(20);
        contentPanel.add(new JLabel("ID del equipo:"));
        contentPanel.add(idEquipoField);

        // Botón para generar el reporte de un solo equipo
        JButton btnGenerarReporte = new JButton("Generar Reporte");
        btnGenerarReporte.addActionListener(e -> {
            String idEquipo = idEquipoField.getText();
            if (!idEquipo.isEmpty()) {
                generarReporteEquipo(idEquipo); // Método para generar el reporte
            }
        });
        contentPanel.add(btnGenerarReporte);

        // Botón para generar el reporte de todos los equipos
        JButton btnGenerarTodos = new JButton("Generar Reporte de Todos los Equipos");
        btnGenerarTodos.addActionListener(e -> {
            generarReporteTodosEquipos(); // Método para generar el reporte de todos los equipos
        });
        contentPanel.add(btnGenerarTodos);

        // Agregar componentes al panel principal
        add(titleLabel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }

    // Método para generar el reporte en Word de un solo equipo
    private void generarReporteEquipo(String idEquipo) {
        // Aquí puedes realizar la consulta para obtener los detalles del equipo a partir de su ID.
        // Esto es solo un ejemplo con información estática.
        String nombreEquipo = "Equipo Ejemplo"; // Ejemplo de nombre
        String detallesEquipo = "Detalles completos del equipo: Características, estado, etc."; // Ejemplo de detalles

        // Crear el documento Word
        XWPFDocument doc = new XWPFDocument();

        try {
            // Agregar título al reporte
            XWPFParagraph title = doc.createParagraph();
            XWPFRun titleRun = title.createRun();
            titleRun.setText("Reporte de Equipo - ID: " + idEquipo);
            titleRun.setBold(true);
            titleRun.setFontSize(16);

            // Agregar detalles del equipo
            XWPFParagraph paragraph = doc.createParagraph();
            XWPFRun run = paragraph.createRun();
            run.setText("Nombre del equipo: " + nombreEquipo);
            run.addBreak();
            run.setText("Detalles: " + detallesEquipo);

            // Guardar el documento
            try (FileOutputStream out = new FileOutputStream("Reporte_Equipo_" + idEquipo + ".docx")) {
                doc.write(out);
            }

            JOptionPane.showMessageDialog(this, "Reporte generado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al generar el reporte", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para generar el reporte de todos los equipos en Word
    private void generarReporteTodosEquipos() {
        // Aquí deberías consultar todos los equipos registrados en el sistema
        List<String> equipos = List.of("Equipo A", "Equipo B", "Equipo C"); // Ejemplo de equipos
        List<String> detalles = List.of("Detalles A", "Detalles B", "Detalles C"); // Ejemplo de detalles

        // Crear el documento Word
        XWPFDocument doc = new XWPFDocument();

        try {
            // Título del reporte
            XWPFParagraph title = doc.createParagraph();
            XWPFRun titleRun = title.createRun();
            titleRun.setText("Reporte de Todos los Equipos");
            titleRun.setBold(true);
            titleRun.setFontSize(16);

            // Agregar tabla con los equipos
            XWPFTable table = doc.createTable();
            XWPFTableRow row = table.getRow(0);
            row.getCell(0).setText("Nombre del Equipo");
            row.addNewTableCell().setText("Detalles");

            // Agregar cada equipo a la tabla
            for (int i = 0; i < equipos.size(); i++) {
                row = table.createRow();
                row.getCell(0).setText(equipos.get(i));
                row.getCell(1).setText(detalles.get(i));
            }

            // Guardar el documento
            try (FileOutputStream out = new FileOutputStream("Reporte_Todos_Los_Equipos.docx")) {
                doc.write(out);
            }

            JOptionPane.showMessageDialog(this, "Reporte de todos los equipos generado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al generar el reporte", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
*/
