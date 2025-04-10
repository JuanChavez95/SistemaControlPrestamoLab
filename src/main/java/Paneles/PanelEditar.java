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

public class PanelEditar extends JPanel {
    
    public PanelEditar() {
        // Configuración del panel
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        
       
        JLabel titleLabel = new JLabel("Ajustes de Usuario");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel central con la tabla 
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(Color.WHITE);
        // Aquí agregarías los componentes específicos
        contentPanel.add(new JLabel("Contenido del panel de edición de usuario"));
        
        // Agregar componentes al panel principal
        add(titleLabel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }
}
