/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PanelEstadistica;


import javax.swing.*;
import java.awt.*;

public class PanelEstadisticasEquipos extends JPanel {

    public PanelEstadisticasEquipos() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel titulo = new JLabel("Panel de Estadísticas de Equipos", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titulo.setForeground(new Color(33, 97, 140));
        add(titulo, BorderLayout.NORTH);

        JTextArea descripcion = new JTextArea(
            "Aquí se mostrarán estadísticas relacionadas con el uso, disponibilidad, y estado de los equipos en los laboratorios."
        );
        descripcion.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        descripcion.setLineWrap(true);
        descripcion.setWrapStyleWord(true);
        descripcion.setEditable(false);
        descripcion.setBackground(Color.WHITE);
        descripcion.setMargin(new Insets(20, 40, 20, 40));

        add(descripcion, BorderLayout.CENTER);
    }
}
