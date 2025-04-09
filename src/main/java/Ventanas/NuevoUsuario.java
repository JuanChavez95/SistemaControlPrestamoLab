/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Ventanas;

import exceptions.CredencialesInvalidas;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import Clases.Usuario;
import Controles.ControlUsuario;
import java.sql.SQLException;

/**
 *
 * @author DOC
 */
public class NuevoUsuario extends JFrame{
    public NuevoUsuario(){
        setTitle("NUEVO USUARIO");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1375, 745);
        setResizable(false);
        getContentPane().setBackground(new Color(235, 255, 255));
        setLayout(null);
        
        //Titulo general del sistema
        JLabel titulo2 = new JLabel("INGRESAR NUEVO USUARIO");
        titulo2.setBounds(50, 50, 360, 26);
        titulo2.setForeground(Color.BLUE);
        titulo2.setFont(new Font("Arial", Font.BOLD, 25));
        titulo2.setBackground(new Color(235, 255, 255));
        titulo2.setOpaque(true);
        add(titulo2);
        setVisible(true);
        
        //Frame principal
        JPanel frame1 = new JPanel();
        frame1.setBackground(new Color(81, 0, 255));
        frame1.setBounds(70, 100, 1200, 580);
        frame1.setLayout(null);
        add(frame1);
        
        //Frames decorativos
        JPanel frame2 = new JPanel();
        frame2.setBackground(new Color(122, 95, 126));
        frame2.setBounds(50, 100, 20, 580);
        frame2.setLayout(null);
        add(frame2);
        
        JPanel frame3 = new JPanel();
        frame3.setBackground(new Color(122, 95, 126));
        frame3.setBounds(1270, 100, 20, 580);
        frame3.setLayout(null);
        add(frame3);
        
        //PANEL DE INGRESO DE TEXTO
        
        JLabel nombre = new JLabel("Nombre: ");
        nombre.setBounds(40, 80, 75, 30);
        nombre.setForeground(Color.WHITE);
        nombre.setFont(new Font("Arial", Font.BOLD, 16));
        nombre.setBackground(new Color(127, 192, 231));
        nombre.setHorizontalAlignment(SwingConstants.CENTER);
        nombre.setOpaque(true);
        frame1.add(nombre);

        JTextField cajaNombre = new JTextField();
        cajaNombre.setBounds(270, 80, 280, 25);
        frame1.add(cajaNombre);
        
        JLabel app = new JLabel("Apellido Paterno: ");
        app.setBounds(40, 170, 140, 30);
        app.setForeground(Color.WHITE);
        app.setFont(new Font("Arial", Font.BOLD, 16));
        app.setBackground(new Color(127, 192, 231));
        app.setHorizontalAlignment(SwingConstants.CENTER);
        app.setOpaque(true);
        frame1.add(app);

        JTextField cajaAPP = new JTextField();
        cajaAPP.setBounds(270, 170, 280, 25);
        frame1.add(cajaAPP);
        
        JLabel apm = new JLabel("Apellido Materno: ");
        apm.setBounds(40, 260, 140, 30);
        apm.setForeground(Color.WHITE);
        apm.setFont(new Font("Arial", Font.BOLD, 16));
        apm.setBackground(new Color(127, 192, 231));
        apm.setHorizontalAlignment(SwingConstants.CENTER);
        apm.setOpaque(true);
        frame1.add(apm);

        JTextField cajaAPM = new JTextField();
        cajaAPM.setBounds(270, 260, 280, 25);
        frame1.add(cajaAPM);
        
        JLabel correo = new JLabel("Correo Email: ");
        correo.setBounds(40, 350, 150, 30);
        correo.setForeground(Color.WHITE);
        correo.setFont(new Font("Arial", Font.BOLD, 16));
        correo.setBackground(new Color(127, 192, 231));
        correo.setHorizontalAlignment(SwingConstants.CENTER);
        correo.setOpaque(true);
        frame1.add(correo);

        JTextField cajacorreo = new JTextField();
        cajacorreo.setBounds(270, 350, 280, 25);
        frame1.add(cajacorreo);
        
        JLabel contra = new JLabel("Contraseña: ");
        contra.setBounds(680, 80, 105, 30);
        contra.setForeground(Color.WHITE);
        contra.setFont(new Font("Arial", Font.BOLD, 16));
        contra.setBackground(new Color(127, 192, 231));
        contra.setOpaque(true);
        frame1.add(contra);

        JTextField cajaContra = new JTextField();
        cajaContra.setBounds(875, 80, 280, 25);
        frame1.add(cajaContra);
        
        JLabel ru = new JLabel("R.U: ");
        ru.setBounds(680, 170, 50, 30);
        ru.setForeground(Color.WHITE);
        ru.setFont(new Font("Arial", Font.BOLD, 16));
        ru.setBackground(new Color(127, 192, 231));
        ru.setHorizontalAlignment(SwingConstants.CENTER);
        ru.setOpaque(true);
        frame1.add(ru);

        JTextField cajaRU = new JTextField();
        cajaRU.setBounds(875, 170, 280, 25);
        frame1.add(cajaRU);
        
        JLabel rol = new JLabel("Rol: ");
        rol.setBounds(680, 260, 50, 30);
        rol.setForeground(Color.WHITE);
        rol.setFont(new Font("Arial", Font.BOLD, 16));
        rol.setBackground(new Color(127, 192, 231));
        rol.setHorizontalAlignment(SwingConstants.CENTER);
        rol.setOpaque(true);
        frame1.add(rol);

        JComboBox<String> cajarol = new JComboBox<>(new String[]{"Docente", "Estudiante"});
        cajarol.setBounds(875, 260, 280, 25);
        frame1.add(cajarol);
        
        JLabel ci = new JLabel("C.I: ");
        ci.setBounds(680, 350, 50, 30);
        ci.setForeground(Color.WHITE);
        ci.setFont(new Font("Arial", Font.BOLD, 16));
        ci.setBackground(new Color(127, 192, 231));
        ci.setHorizontalAlignment(SwingConstants.CENTER);
        ci.setOpaque(true);
        frame1.add(ci);

        JTextField cajaCI = new JTextField();
        cajaCI.setBounds(875, 350, 280, 25);
        frame1.add(cajaCI);
        
        //Botón para GUARDAR nuevo usuario
        JButton botonNuevo = new JButton("GUARDAR");
        botonNuevo.setBounds(560, 505, 130, 26);
        botonNuevo.setBackground(new Color(82, 169, 41));
        botonNuevo.setForeground(Color.WHITE);
        botonNuevo.setFont(new Font("Arial", Font.BOLD, 15));
        frame1.add(botonNuevo);
        
        
        // Botón para guardar al nuevo usuario
        botonNuevo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Recoger los datos de los campos de texto de las cajas
                    String nombre = cajaNombre.getText();
                    String APP = cajaAPP.getText();
                    String APM = cajaAPM.getText();
                    String password = cajaContra.getText();
                    int ru = Integer.parseInt(cajaRU.getText());
                    int ci = Integer.parseInt(cajaCI.getText());
                    String correo = cajacorreo.getText();
                    String role = (String) cajarol.getSelectedItem();

                    // Verificar que no haya campos vacíos
                    if (nombre.isEmpty() || APP.isEmpty() || APM.isEmpty() || password.isEmpty() ||
                        correo.isEmpty() || cajaRU.getText().isEmpty() || cajaCI.getText().isEmpty()) {
                        throw new CredencialesInvalidas("Campos vacíos");
                    }

                    // Crear una nueva instancia de Usuario
                    Usuario nuevoUsuario = new Usuario(ru, nombre, APP, APM, password, ci, role, correo);
                    ControlUsuario controlUsuario = new ControlUsuario();
                    controlUsuario.insertar(nuevoUsuario);

                    // Mostrar un mensaje de éxito
                    JOptionPane.showMessageDialog(null, "Usuario guardado exitosamente");
                    //Cerrar esta interfaz
                    dispose();
                    

                } catch (NumberFormatException nfe) {
                    // Manejo de error si no se puede convertir el RU o CI a número
                    JOptionPane.showMessageDialog(null, "RU o CI deben ser números válidos");
                } catch (CredencialesInvalidas ex) {
                    // Manejo de error si algún campo está vacío
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                } catch (SQLException sqle) {
                    String msg = sqle.getMessage();
                    if (msg.contains("Duplicate entry")) {
                        JOptionPane.showMessageDialog(null, "El RU, CI o correo ya está registrado.");
                    } else {
                        JOptionPane.showMessageDialog(null, "Error al guardar el usuario.:\n" + msg);
                    }
                } catch (Exception ex) {
                    // Capturar cualquier otro error no manejado
                    JOptionPane.showMessageDialog(null, "Error del sistema");
                }
            }
        });

    }
}
