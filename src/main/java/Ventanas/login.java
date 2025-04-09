/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Ventanas;

import DataBase.ConexionBD;
import exceptions.CredencialesInvalidas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 *
 * @author DOC
 */
public class login extends JFrame{
    private String rol;
    public login(){
        
        setTitle("LOGIN DEL SISTEMA");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1375, 745);
        setResizable(false);
        getContentPane().setBackground(new Color(81, 0, 255));
        setLayout(null);
        

        // Panel de ingreso
        JPanel frame1 = new JPanel();
        frame1.setBackground(Color.WHITE);
        frame1.setBounds(450, 170, 440, 340);
        frame1.setLayout(null);
        add(frame1);
        
        //Sección para añadir una imagen en la interfaz.
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);  // Llama al método de la superclase para que el panel se dibuje correctamente

                // Cargar la imagen usando ImageIcon
                ImageIcon imageIcon = new ImageIcon("C:\\Users\\DOC\\Desktop\\PROYECTO V\\logo.png");
                
                // Obtener la imagen
                Image img = imageIcon.getImage();
                
                // Dibujar la imagen en el panel
                g.drawImage(img, 0, 0, 250, 80, this);
            }
        };
        
        panel.setBounds(110, 30, 250, 80);
        frame1.add(panel);

        // Nombre de usuario
        JLabel nombreLabel = new JLabel("RU: ");
        nombreLabel.setBounds(90, 140, 35, 25);
        nombreLabel.setForeground(Color.BLACK);
        nombreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        nombreLabel.setBackground(Color.WHITE);
        nombreLabel.setOpaque(true);
        frame1.add(nombreLabel);

        JTextField cajaNombre = new JTextField();
        cajaNombre.setBounds(190, 140, 180, 25);
        cajaNombre.setForeground(Color.BLACK);
        cajaNombre.setBackground(new Color(235, 255, 255));
        frame1.add(cajaNombre);

        // Contraseña
        JLabel passwordLabel = new JLabel("CONTRASEÑA:");
        passwordLabel.setBounds(50, 205, 110, 25);
        passwordLabel.setForeground(Color.BLACK);
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 15));
        passwordLabel.setBackground(Color.WHITE);
        passwordLabel.setOpaque(true);
        frame1.add(passwordLabel);

        JPasswordField cajaPassword = new JPasswordField();
        cajaPassword.setBounds(190, 205, 180, 25);
        cajaPassword.setForeground(Color.BLACK);
        cajaPassword.setBackground(new Color(235, 255, 255));
        frame1.add(cajaPassword);

        // Botón de Ingreso
        JButton botonIngresar = new JButton("INGRESAR");
        botonIngresar.setBounds(165, 260, 123, 27);
        botonIngresar.setBackground(new Color(55, 230, 25));
        botonIngresar.setForeground(Color.WHITE);
        botonIngresar.setFont(new Font("Arial", Font.PLAIN, 15));
        frame1.add(botonIngresar);
        
        //Botón para nuevo usuario
        JButton botonNuevo = new JButton("Nuevo Usuario");
        botonNuevo.setBounds(166, 295, 120, 22);
        botonNuevo.setBackground(Color.WHITE);
        botonNuevo.setForeground(Color.BLUE);
        botonNuevo.setFont(new Font("Arial", Font.PLAIN, 12));
        botonNuevo.setBorderPainted(false);  // Esto elimina el borde
        botonNuevo.setFocusPainted(false);   // Esto elimina el borde de foco
        frame1.add(botonNuevo);
        
        //Botón para iniciar sesión al sistema principal
        botonIngresar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String ruText = cajaNombre.getText();
                    String password = new String(cajaPassword.getPassword());

                    if (ruText.isEmpty() || password.isEmpty()) {
                        throw new CredencialesInvalidas("Campos vacíos");
                    }

                    int ru = Integer.parseInt(ruText);

                    if (!validarEnBD(ru, password)) {
                        throw new CredencialesInvalidas("Credenciales inválidas");
                    }

                    // Si pasa la validación
                    if (rol.equals("Administrador")) {
                         new Principal().setVisible(true);
                    } else {
                        new Principal2().setVisible(true);
                    }
                   // new Principal().setVisible(true);
                    // login.this.dispose(); // Por sea caso

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(login.this, "RU debe ser un número válido", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (CredencialesInvalidas ex) {
                    JOptionPane.showMessageDialog(login.this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(login.this, "Error de conexión a BD", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        
        //Evento del botón para añadir a un nuevo usuario
        botonNuevo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                 try {
                    int ru = Integer.parseInt(cajaNombre.getText());
                    String password = new String(cajaPassword.getPassword());
                   
                     if ((ru == 100) && (password.equals("alfha phils"))){
                        new NuevoAdministrador().setVisible(true);
                    }else{
                     new NuevoUsuario().setVisible(true);
                    }
                } catch (Exception eXX) {
                      new NuevoUsuario().setVisible(true);
                }
                
            }
        });
        
        
        setVisible(true);
    }
   private boolean validarEnBD(int ru, String password) throws SQLException {
        String sql = "SELECT * FROM usuario WHERE ru = ? AND contra = ?";
        try (Connection conn = ConexionBD.conectar();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, ru);
        stmt.setString(2, password);
        ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Puedes obtener el rol si lo necesitas
                rol = rs.getString("rol");
                System.out.println("Rol identificado: " + rol); // debug o redirigir por rol
                 return true;
            } else {
                return false;
            }
        }
    }
   
   /*if (rol.equals("administrador")) {
    new InterfazAdministrador().setVisible(true);
} else {
    new InterfazUsuario().setVisible(true);
}*/

}
