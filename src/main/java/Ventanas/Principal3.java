/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Ventanas;


/**
 *
 * @author DOC
 */
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

import Paneles.PanelDocentes;
import Paneles.PanelAdministradores;
import Paneles.PanelEstudiantes;
import Paneles.PanelHorario;
import Paneles.PanelVisualizarEquipo;

public class Principal3 extends JFrame {
    
    private JPanel menuPanel;
    private JPanel contentPanel;
    private JLabel usuarioLabel;
    
    public Principal3() {
        setTitle("Sistema de Control y Préstamo de Laboratorios");
        setSize(1375, 745);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setBackground(new Color(235, 255, 255));
        
        // Panel superior (header)
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // Panel de menú lateral
        menuPanel = createMenuPanel();
        add(menuPanel, BorderLayout.WEST);
        
        // Panel de contenido principal
        contentPanel = new JPanel();
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setLayout(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);
        
        // Mensaje por defecto en el panel de contenido
        JLabel defaultMessage = new JLabel("¡BIENVENIDO AL SISTEMA DE CONTROL Y PRÉSTAMO DE LABORATORIOS!", SwingConstants.CENTER);
        defaultMessage.setFont(new Font("Arial", Font.PLAIN, 18));
        contentPanel.add(defaultMessage, BorderLayout.CENTER);
        
        setLocationRelativeTo(null);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(81, 0, 255));
        headerPanel.setPreferredSize(new Dimension(getWidth(), 60));
        
        // Espacio para texto en lugar del logo
        JLabel textoLabel = new JLabel("  UNIVERSIDAD SALESIANA DE BOLIVIA");
        textoLabel.setForeground(Color.WHITE);
        textoLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerPanel.add(textoLabel, BorderLayout.WEST);
        
        // Información de usuario
        usuarioLabel = new JLabel(" ▼ ESTUDIANTE");
        usuarioLabel.setForeground(Color.WHITE);
        usuarioLabel.setFont(new Font("Arial", Font.BOLD, 14));
        usuarioLabel.setBorder(new EmptyBorder(0, 0, 0, 20));
        headerPanel.add(usuarioLabel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createMenuPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(127, 192, 231)); //MODIFICACIÓN
        panel.setPreferredSize(new Dimension(250, getHeight()));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        // Título del menú
        JLabel menuTitle = new JLabel("MENÚ PRINCIPAL");
        menuTitle.setForeground(Color.BLACK);
        menuTitle.setFont(new Font("Arial", Font.BOLD, 16));
        menuTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        menuTitle.setBorder(new EmptyBorder(20, 15, 20, 0));
        panel.add(menuTitle);
        
        // Opciones del menú actualizadas sin la opción de PRÉSTAMOS
        panel.add(createMenuOption("LABORATORIOS", new String[]{"Horarios"}));
        panel.add(createMenuOption("USUARIOS", new String[]{"Docentes", "Estudiantes"}));
        panel.add(createMenuOption("EQUIPOS", new String[]{"Máquinas"}));
        // La sección de PRÉSTAMOS ha sido eliminada
        
        // Agregar espacio en blanco al final
        panel.add(Box.createVerticalGlue());
        
        return panel;
    }
    
    private JPanel createMenuOption(String title, String[] subOptions) {
        JPanel optionPanel = new JPanel();
        optionPanel.setLayout(new BoxLayout(optionPanel, BoxLayout.Y_AXIS));
        optionPanel.setBackground(new Color(127, 192, 231)); 
        optionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Panel para el título y flecha
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(new Color(81, 0, 255)); 
        titlePanel.setMaximumSize(new Dimension(250, 40));
        
        // Icono de círculo
        JLabel circleLabel = new JLabel("●");
        circleLabel.setForeground(Color.WHITE);
        circleLabel.setBorder(new EmptyBorder(0, 15, 0, 10));
        titlePanel.add(circleLabel, BorderLayout.WEST);
        
        // Título de la opción
        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        
        // Flecha desplegable
        JLabel arrowLabel = new JLabel("▼");
        arrowLabel.setForeground(Color.WHITE);
        arrowLabel.setBorder(new EmptyBorder(0, 0, 0, 15));
        titlePanel.add(arrowLabel, BorderLayout.EAST);
        
        optionPanel.add(titlePanel);
        
        // Panel para subopciones (inicialmente oculto)
        JPanel subOptionsPanel = new JPanel();
        subOptionsPanel.setLayout(new BoxLayout(subOptionsPanel, BoxLayout.Y_AXIS));
        subOptionsPanel.setBackground(new Color(127, 192, 231)); 
        subOptionsPanel.setVisible(false);
        
        // Agregar subopciones
        for (String subOption : subOptions) {
            JLabel subOptionLabel = new JLabel("   • " + subOption);
            subOptionLabel.setForeground(Color.BLACK);
            subOptionLabel.setFont(new Font("Arial", Font.PLAIN, 13));
            subOptionLabel.setBorder(new EmptyBorder(5, 30, 5, 0));
            subOptionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            // Agregar funcionalidad al hacer clic en la subopción
            subOptionLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    mostrarContenido(title, subOption);
                }
                
                @Override
                public void mouseEntered(MouseEvent e) {
                    subOptionLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
                }
            });
            
            subOptionsPanel.add(subOptionLabel);
        }
        
        optionPanel.add(subOptionsPanel);
        
        // Agregar funcionalidad de despliegue
        titlePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                subOptionsPanel.setVisible(!subOptionsPanel.isVisible());
                arrowLabel.setText(subOptionsPanel.isVisible() ? "▼" : "►");
                optionPanel.revalidate();
                optionPanel.repaint();
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                titlePanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        });
        
        return optionPanel;
    }
    
    private void mostrarContenido(String categoria, String subopcion) {
        // Limpiar el panel de contenido
        contentPanel.removeAll();
        
        // Crear nuevo panel con el contenido específico
        JPanel nuevoContenido = new JPanel();
        nuevoContenido.setBackground(Color.WHITE);
        nuevoContenido.setLayout(new BorderLayout());
        
        // Título del contenido
        JLabel tituloContenido = new JLabel(categoria + " > " + subopcion);
        tituloContenido.setFont(new Font("Arial", Font.BOLD, 18));
        tituloContenido.setBorder(new EmptyBorder(20, 20, 20, 20));
        nuevoContenido.add(tituloContenido, BorderLayout.NORTH);
        
        // Contenido específico según la categoría y subopción
        JPanel contenidoEspecifico = new JPanel();
        contenidoEspecifico.setBackground(Color.WHITE);
        
        switch (categoria) {
            case "LABORATORIOS":
                if (subopcion.equals("Horarios")) {
                    contenidoEspecifico = crearPanelHorarios();
                }
                break;
            case "USUARIOS":
                if (subopcion.equals("Docentes")) {
                    contenidoEspecifico = crearPanelDocentes();
                } else if (subopcion.equals("Estudiantes")) {
                    contenidoEspecifico = crearPanelEstudiantes();
                }
                break;
            case "EQUIPOS":
                if (subopcion.equals("Máquinas")) {
                    contenidoEspecifico = crearPanelMaquinas();
                }
                break;
            // Se ha eliminado el case para PRÉSTAMOS
        }
        
        nuevoContenido.add(contenidoEspecifico, BorderLayout.CENTER);
        contentPanel.add(nuevoContenido);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    // Métodos para crear paneles específicos - Implementaciones vacías
    private JPanel crearPanelHorarios() {
        PanelHorario panelHorario = new PanelHorario();
        return panelHorario;
    }
    
    private JPanel crearPanelDocentes() {
        PanelDocentes panelDocentes = new PanelDocentes();
        return panelDocentes;
    }
    
    private JPanel crearPanelEstudiantes() {
        PanelEstudiantes panelEstudiantes = new PanelEstudiantes();
        return panelEstudiantes;
    }
    
    private JPanel crearPanelMaquinas() {
        PanelVisualizarEquipo panelVisualizarEquipo = new PanelVisualizarEquipo();
        return panelVisualizarEquipo;
    }
    
    // Se han eliminado los métodos:
    // - crearPanelSolicitarPrestamo()
    // - crearPanelNotificaciones()
}