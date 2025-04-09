/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Ventanas;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import Controles.ControlAdministrador;
import Clases.Administrador;
import exceptions.CredencialesInvalidas;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author DOC
 */
public class NuevoAdministrador extends JFrame{
    public NuevoAdministrador(){
    setTitle("NUEVO ADMNISTRADOR");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1375, 745);
        setResizable(false);
        getContentPane().setBackground(new Color(235, 255, 255));
        setLayout(null);
        
        //Titulo general del sistema
        JLabel titulo2 = new JLabel("INGRESAR NUEVO ADMINISTRADOR");
        titulo2.setBounds(50, 50, 405, 26);
        titulo2.setForeground(Color.BLUE);
        titulo2.setFont(new Font("Arial", Font.BOLD, 22));
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
        app.setBounds(40, 150, 140, 30);
        app.setForeground(Color.WHITE);
        app.setFont(new Font("Arial", Font.BOLD, 16));
        app.setBackground(new Color(127, 192, 231));
        app.setHorizontalAlignment(SwingConstants.CENTER);
        app.setOpaque(true);
        frame1.add(app);

        JTextField cajaAPP = new JTextField();
        cajaAPP.setBounds(270, 150, 280, 25);
        frame1.add(cajaAPP);
        
        JLabel apm = new JLabel("Apellido Materno: ");
        apm.setBounds(40, 220, 140, 30);
        apm.setForeground(Color.WHITE);
        apm.setFont(new Font("Arial", Font.BOLD, 16));
        apm.setBackground(new Color(127, 192, 231));
        apm.setHorizontalAlignment(SwingConstants.CENTER);
        apm.setOpaque(true);
        frame1.add(apm);

        JTextField cajaAPM = new JTextField();
        cajaAPM.setBounds(270, 220, 280, 25);
        frame1.add(cajaAPM);
        
        JLabel correo = new JLabel("Correo Email: ");
        correo.setBounds(40, 290, 150, 30);
        correo.setForeground(Color.WHITE);
        correo.setFont(new Font("Arial", Font.BOLD, 16));
        correo.setBackground(new Color(127, 192, 231));
        correo.setHorizontalAlignment(SwingConstants.CENTER);
        correo.setOpaque(true);
        frame1.add(correo);

        JTextField cajacorreo = new JTextField();
        cajacorreo.setBounds(270, 290, 280, 25);
        frame1.add(cajacorreo);
        
        JLabel FechaInicio = new JLabel("Fecha de inicio: ");
        FechaInicio.setBounds(40, 360, 145, 30);
        FechaInicio.setForeground(Color.WHITE);
        FechaInicio.setFont(new Font("Arial", Font.BOLD, 16));
        FechaInicio.setBackground(new Color(127, 192, 231));
        FechaInicio.setHorizontalAlignment(SwingConstants.CENTER);
        FechaInicio .setOpaque(true);
        frame1.add(FechaInicio);

        JTextField cajaFI = new JTextField();
        cajaFI.setBounds(270, 360, 280, 25);
        frame1.add(cajaFI);
        
        JLabel nit = new JLabel("Nro. Título: ");
        nit.setBounds(40, 430, 100, 30);
        nit.setForeground(Color.WHITE);
        nit.setFont(new Font("Arial", Font.BOLD, 16));
        nit.setBackground(new Color(127, 192, 231));
        nit.setHorizontalAlignment(SwingConstants.CENTER);
        nit.setOpaque(true);
        frame1.add(nit);

        JTextField cajaNIT = new JTextField();
        cajaNIT.setBounds(270, 430, 280, 25);
        frame1.add(cajaNIT);
        
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

        JComboBox<String> cajarol = new JComboBox<>(new String[]{"Administrador"});
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
        
        JLabel salario = new JLabel("Salario: ");
        salario.setBounds(680, 440, 80, 30);
        salario.setForeground(Color.WHITE);
        salario.setFont(new Font("Arial", Font.BOLD, 16));
        salario.setBackground(new Color(127, 192, 231));
        salario.setHorizontalAlignment(SwingConstants.CENTER);
        salario.setOpaque(true);
        frame1.add(salario);

        JTextField cajasalario = new JTextField();
        cajasalario.setBounds(875, 440, 280, 25);
        frame1.add(cajasalario);
        
        //Botón para GUARDAR nuevo usuario
        JButton botonNuevo = new JButton("GUARDAR");
        botonNuevo.setBounds(560, 505, 130, 26);
        botonNuevo.setBackground(new Color(82, 169, 41));
        botonNuevo.setForeground(Color.WHITE);
        botonNuevo.setFont(new Font("Arial", Font.BOLD, 15));
        frame1.add(botonNuevo);
        
        // Botón para guardar al nuevo administrador
        botonNuevo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Todos los valores a cargar en la tabla administrador
                    String nombre = cajaNombre.getText();
                    String APP = cajaAPP.getText();
                    String APM = cajaAPM.getText();
                    String password = cajaContra.getText();
                    String ruText = cajaRU.getText();
                    String ciText = cajaCI.getText();
                    String correo = cajacorreo.getText();
                    String role = (String) cajarol.getSelectedItem();
                    String fechainicio = cajaFI.getText(); // formato esperado: yyyy-MM-dd
                    String nit = cajaNIT.getText();
                    String salarioText = cajasalario.getText();

                    // Verificar si los se encuentran vacíos
                    if (nombre.isEmpty() || APP.isEmpty() || APM.isEmpty() || password.isEmpty() ||
                        correo.isEmpty() || ruText.isEmpty() || ciText.isEmpty() ||
                        fechainicio.isEmpty() || salarioText.isEmpty() || nit.isEmpty()) {
                        throw new CredencialesInvalidas("Campos vacíos");
                    }

                    // Conversión de tipos de datos
                    int ru = Integer.parseInt(ruText);
                    int ci = Integer.parseInt(ciText);
                    Date fechaInicioDate = Date.valueOf(fechainicio); 
                    BigDecimal salarioDecimal = new BigDecimal(salarioText);

                    // Crear el objeto Administrador
                    Administrador admin = new Administrador(
                        ru, nombre, APP, APM, password, ci, role, correo,
                        salarioDecimal.doubleValue(), fechaInicioDate, nit
                    );

                    // Insertar en la base de datos
                    ControlAdministrador controlador = new ControlAdministrador();
                    controlador.insertarAdministrador(admin);

                    // Mostrar mensaje de éxito
                    JOptionPane.showMessageDialog(null, "Administrador guardado exitosamente");

                    // Cerrar ventana posteriormente
                    dispose();

                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(null, "RU o CI deben ser valores válidos");
                } catch (CredencialesInvalidas ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                } catch (SQLException sqle) {
                    String msg = sqle.getMessage();
                    if (msg.contains("Duplicate entry")) {
                        JOptionPane.showMessageDialog(null, "El RU, CI o correo ya están registrado.");
                    } else {
                        JOptionPane.showMessageDialog(null, "Error al guardar el administrador:\n" + msg);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error del sistema");
                }
            }
        });

    }
}
