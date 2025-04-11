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
import java.awt.*;

public class PanelHistorialEquipo extends JPanel {
    
    public PanelHistorialEquipo() {
        // Configuración del panel
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        
        // Aquí agregarías todos los componentes específicos
        // para la gestión de docentes (tablas, formularios, botones, etc.)
        JLabel titleLabel = new JLabel("Historial Equipo");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel central con la tabla o formulario principal
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(Color.WHITE);
        // Aquí agregarías los componentes específicos
        contentPanel.add(new JLabel("Contenido del panel CRUD historial equipo"));
        
        // Agregar componentes al panel principal
        add(titleLabel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }
}