/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Ventanas;

import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import Clases.Administrador;
import Controles.ControlAdministrador;
import exceptions.CredencialesInvalidas;

/**
 * Ventana para registrar un nuevo administrador en el sistema
 * @author DOC
 */
public class NuevoAdministrador extends JFrame {
    private JTextField cajaNombre, cajaAPP, cajaAPM, cajacorreo, cajaContra, cajaRU, cajaCI, cajaFI, cajaNIT, cajasalario;
    private JComboBox<String> cajarol;

    public NuevoAdministrador() {
        inicializarComponentes();
        setLocationRelativeTo(null); // Centra la ventana en la pantalla
        setVisible(true);
    }

    private void inicializarComponentes() {
        setTitle("Sistema de Control de Préstamos - Nuevo Administrador");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(900, 600)); // Tamaño más compacto
        setResizable(true); // Permitimos redimensionar la ventana

        // Fondo con degradado
        JPanel fondoPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gradient = new GradientPaint(0, 0, new Color(30, 60, 120), 0, getHeight(), new Color(60, 120, 180));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        fondoPanel.setLayout(new GridBagLayout());
        setContentPane(fondoPanel);

        // Panel principal del formulario
        JPanel formPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getBackground());
                g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 40, 40);
            }
        };
        formPanel.setBackground(Color.WHITE);
        formPanel.setOpaque(false);
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        formPanel.setLayout(null);
        formPanel.setPreferredSize(new Dimension(600, 520));

        // Añadir el panel al fondoPanel usando GridBagConstraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.NONE;
        fondoPanel.add(formPanel, gbc);

        // Título
        JLabel titulo = new JLabel("INGRESAR NUEVO ADMINISTRADOR", SwingConstants.CENTER);
        titulo.setBounds(0, 10, 600, 30);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setForeground(new Color(30, 60, 120));
        formPanel.add(titulo);

        // Subtítulo
        JLabel subtitulo = new JLabel("Complete los datos para crear un nuevo administrador", SwingConstants.CENTER);
        subtitulo.setBounds(0, 40, 600, 20);
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitulo.setForeground(Color.GRAY);
        formPanel.add(subtitulo);

        // Campos del formulario (dos columnas)
        int padding = 30;
        int labelWidth = 120;
        int fieldWidth = 150;
        int fieldHeight = 30;
        int yStart = 70;
        int yGap = 40;

        // Columna izquierda
        // Nombre
        JLabel nombreLabel = new JLabel("Nombre:");
        nombreLabel.setBounds(padding, yStart, labelWidth, 20);
        nombreLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        nombreLabel.setForeground(Color.DARK_GRAY);
        formPanel.add(nombreLabel);

        cajaNombre = new JTextField();
        cajaNombre.setBounds(padding + labelWidth, yStart, fieldWidth, fieldHeight);
        cajaNombre.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cajaNombre.setForeground(Color.BLACK);
        cajaNombre.setBackground(new Color(245, 245, 245));
        cajaNombre.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        formPanel.add(cajaNombre);

        // Apellido Paterno
        JLabel appLabel = new JLabel("Apellido Paterno:");
        appLabel.setBounds(padding, yStart + yGap, labelWidth, 20);
        appLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        appLabel.setForeground(Color.DARK_GRAY);
        formPanel.add(appLabel);

        cajaAPP = new JTextField();
        cajaAPP.setBounds(padding + labelWidth, yStart + yGap, fieldWidth, fieldHeight);
        cajaAPP.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cajaAPP.setForeground(Color.BLACK);
        cajaAPP.setBackground(new Color(245, 245, 245));
        cajaAPP.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        formPanel.add(cajaAPP);

        // Apellido Materno
        JLabel apmLabel = new JLabel("Apellido Materno:");
        apmLabel.setBounds(padding, yStart + 2 * yGap, labelWidth, 20);
        apmLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        apmLabel.setForeground(Color.DARK_GRAY);
        formPanel.add(apmLabel);

        cajaAPM = new JTextField();
        cajaAPM.setBounds(padding + labelWidth, yStart + 2 * yGap, fieldWidth, fieldHeight);
        cajaAPM.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cajaAPM.setForeground(Color.BLACK);
        cajaAPM.setBackground(new Color(245, 245, 245));
        cajaAPM.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        formPanel.add(cajaAPM);

        // Correo Electrónico
        JLabel correoLabel = new JLabel("Correo Email:");
        correoLabel.setBounds(padding, yStart + 3 * yGap, labelWidth, 20);
        correoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        correoLabel.setForeground(Color.DARK_GRAY);
        formPanel.add(correoLabel);

        cajacorreo = new JTextField();
        cajacorreo.setBounds(padding + labelWidth, yStart + 3 * yGap, fieldWidth, fieldHeight);
        cajacorreo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cajacorreo.setForeground(Color.BLACK);
        cajacorreo.setBackground(new Color(245, 245, 245));
        cajacorreo.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        formPanel.add(cajacorreo);

        // Fecha de Inicio
        JLabel fechaLabel = new JLabel("Fecha de Inicio:");
        fechaLabel.setBounds(padding, yStart + 4 * yGap, labelWidth, 20);
        fechaLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        fechaLabel.setForeground(Color.DARK_GRAY);
        formPanel.add(fechaLabel);

        cajaFI = new JTextField();
        cajaFI.setBounds(padding + labelWidth, yStart + 4 * yGap, fieldWidth, fieldHeight);
        cajaFI.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cajaFI.setForeground(Color.BLACK);
        cajaFI.setBackground(new Color(245, 245, 245));
        cajaFI.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        cajaFI.setToolTipText("Formato: YYYY-MM-DD");
        formPanel.add(cajaFI);

        // NIT
        JLabel nitLabel = new JLabel("Nro. Título:");
        nitLabel.setBounds(padding, yStart + 5 * yGap, labelWidth, 20);
        nitLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        nitLabel.setForeground(Color.DARK_GRAY);
        formPanel.add(nitLabel);

        cajaNIT = new JTextField();
        cajaNIT.setBounds(padding + labelWidth, yStart + 5 * yGap, fieldWidth, fieldHeight);
        cajaNIT.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cajaNIT.setForeground(Color.BLACK);
        cajaNIT.setBackground(new Color(245, 245, 245));
        cajaNIT.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        formPanel.add(cajaNIT);

        // Columna derecha
        int rightColumnX = padding + labelWidth + fieldWidth + 20; // Espacio entre columnas

        // Contraseña
        JLabel contraLabel = new JLabel("Contraseña:");
        contraLabel.setBounds(rightColumnX, yStart, labelWidth, 20);
        contraLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        contraLabel.setForeground(Color.DARK_GRAY);
        formPanel.add(contraLabel);

        cajaContra = new JTextField();
        cajaContra.setBounds(rightColumnX + labelWidth, yStart, fieldWidth, fieldHeight);
        cajaContra.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cajaContra.setForeground(Color.BLACK);
        cajaContra.setBackground(new Color(245, 245, 245));
        cajaContra.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        formPanel.add(cajaContra);

        // RU
        JLabel ruLabel = new JLabel("R.U.:");
        ruLabel.setBounds(rightColumnX, yStart + yGap, labelWidth, 20);
        ruLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        ruLabel.setForeground(Color.DARK_GRAY);
        formPanel.add(ruLabel);

        cajaRU = new JTextField();
        cajaRU.setBounds(rightColumnX + labelWidth, yStart + yGap, fieldWidth, fieldHeight);
        cajaRU.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cajaRU.setForeground(Color.BLACK);
        cajaRU.setBackground(new Color(245, 245, 245));
        cajaRU.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        formPanel.add(cajaRU);

        // Rol
        JLabel rolLabel = new JLabel("Rol:");
        rolLabel.setBounds(rightColumnX, yStart + 2 * yGap, labelWidth, 20);
        rolLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        rolLabel.setForeground(Color.DARK_GRAY);
        formPanel.add(rolLabel);

        cajarol = new JComboBox<>(new String[]{"Administrador"});
        cajarol.setBounds(rightColumnX + labelWidth, yStart + 2 * yGap, fieldWidth, fieldHeight);
        cajarol.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cajarol.setBackground(new Color(245, 245, 245));
        cajarol.setForeground(Color.BLACK);
        formPanel.add(cajarol);

        // CI
        JLabel ciLabel = new JLabel("C.I.:");
        ciLabel.setBounds(rightColumnX, yStart + 3 * yGap, labelWidth, 20);
        ciLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        ciLabel.setForeground(Color.DARK_GRAY);
        formPanel.add(ciLabel);

        cajaCI = new JTextField();
        cajaCI.setBounds(rightColumnX + labelWidth, yStart + 3 * yGap, fieldWidth, fieldHeight);
        cajaCI.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cajaCI.setForeground(Color.BLACK);
        cajaCI.setBackground(new Color(245, 245, 245));
        cajaCI.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        formPanel.add(cajaCI);

        // Salario
        JLabel salarioLabel = new JLabel("Salario:");
        salarioLabel.setBounds(rightColumnX, yStart + 4 * yGap, labelWidth, 20);
        salarioLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        salarioLabel.setForeground(Color.DARK_GRAY);
        formPanel.add(salarioLabel);

        cajasalario = new JTextField();
        cajasalario.setBounds(rightColumnX + labelWidth, yStart + 4 * yGap, fieldWidth, fieldHeight);
        cajasalario.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cajasalario.setForeground(Color.BLACK);
        cajasalario.setBackground(new Color(245, 245, 245));
        cajasalario.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        formPanel.add(cajasalario);

        // Botones
        int buttonWidth = 120;
        int buttonHeight = 30;
        int buttonY = yStart + 6 * yGap + 10; // Ajustamos la posición

        // Botón "Guardar"
        JButton botonGuardar = new JButton("GUARDAR");
        botonGuardar.setBounds(padding, buttonY, buttonWidth, buttonHeight);
        botonGuardar.setBackground(new Color(82, 169, 41));
        botonGuardar.setForeground(Color.WHITE);
        botonGuardar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        botonGuardar.setFocusPainted(false);
        botonGuardar.setBorderPainted(false);
        botonGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botonGuardar.setBorder(new LineBorder(new Color(82, 169, 41), 1, true));
        // Efecto hover
        botonGuardar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                botonGuardar.setBackground(new Color(102, 189, 61));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                botonGuardar.setBackground(new Color(82, 169, 41));
            }
        });
        // Acción del botón "Guardar" - Implementado según CÓDIGO 2
        botonGuardar.addActionListener(new ActionListener() {
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
        formPanel.add(botonGuardar);

        // Botón "Cancelar"
        JButton botonCancelar = new JButton("Cancelar");
        botonCancelar.setBounds(padding + labelWidth + fieldWidth + 20 + labelWidth + fieldWidth - buttonWidth, buttonY, buttonWidth, buttonHeight);
        botonCancelar.setBackground(new Color(180, 70, 70));
        botonCancelar.setForeground(Color.WHITE);
        botonCancelar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        botonCancelar.setFocusPainted(false);
        botonCancelar.setBorderPainted(false);
        botonCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botonCancelar.setBorder(new LineBorder(new Color(180, 70, 70), 1, true));
        botonCancelar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                botonCancelar.setBackground(new Color(200, 90, 90));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                botonCancelar.setBackground(new Color(180, 70, 70));
            }
        });
        botonCancelar.addActionListener(e -> dispose());
        formPanel.add(botonCancelar);
    }
}