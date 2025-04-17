/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PanelSanciones;

import javax.swing.*;
import java.awt.*;

public class PanelSancionar extends JPanel {
    
    public PanelSancionar() {
        // Configuración del panel
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        
        // Aquí agregarías todos los componentes específicos
        // para la gestión de docentes (tablas, formularios, botones, etc.)
        JLabel titleLabel = new JLabel("Sancionar Usuarios");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel central con la tabla o formulario principal
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(Color.WHITE);
        // Aquí agregarías los componentes específicos
        contentPanel.add(new JLabel("Contenido del panel para sancionar usuarios"));
        
        // Agregar componentes al panel principal
        add(titleLabel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }
}