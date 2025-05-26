/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Ventanas;

import exceptions.CredencialesInvalidas;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import Clases.Usuario;
import Controles.ControlUsuario;

import org.mindrot.jbcrypt.BCrypt;
/**
 * Ventana para registrar un nuevo usuario en el sistema
 * @author DOC
 */
public class NuevoUsuario extends JFrame {
    private JTextField cajaNombre, cajaAPP, cajaAPM, cajaCorreo, cajaContra, cajaRU, cajaCI;
    private JComboBox<String> cajarol;

    public NuevoUsuario() {
        inicializarComponentes();
        setLocationRelativeTo(null); // Centra la ventana en la pantalla
        setVisible(true);
    }

    private void inicializarComponentes() {
        setTitle("Sistema de Control de Préstamos - Nuevo Usuario");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(600, 600)); // Reducimos la altura de la ventana
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
        formPanel.setPreferredSize(new Dimension(500, 500)); // Reducimos la altura del panel

        // Añadir el panel al fondoPanel usando GridBagConstraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.NONE;
        fondoPanel.add(formPanel, gbc);

        // Título
        JLabel titulo = new JLabel("Registrar Nuevo Usuario", SwingConstants.CENTER);
        titulo.setBounds(0, 10, 500, 30); // Reducimos el tamaño y ajustamos la posición
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20)); // Reducimos el tamaño de la fuente
        titulo.setForeground(new Color(30, 60, 120));
        formPanel.add(titulo);

        // Subtítulo
        JLabel subtitulo = new JLabel("Complete los datos para crear un nuevo usuario", SwingConstants.CENTER);
        subtitulo.setBounds(0, 40, 500, 20); // Reducimos el tamaño y ajustamos la posición
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12)); // Reducimos el tamaño de la fuente
        subtitulo.setForeground(Color.GRAY);
        formPanel.add(subtitulo);

        // Campos del formulario
        int padding = 30;
        int labelWidth = 150;
        int fieldWidth = 300;
        int fieldHeight = 30; // Reducimos la altura de los campos
        int yStart = 70; // Reducimos el yStart para ganar espacio
        int yGap = 40; // Reducimos el espaciado entre campos

        // Nombre
        JLabel nombreLabel = new JLabel("Nombre:");
        nombreLabel.setBounds(padding, yStart, labelWidth, 20); // Reducimos la altura
        nombreLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12)); // Reducimos la fuente
        nombreLabel.setForeground(Color.DARK_GRAY);
        formPanel.add(nombreLabel);

        cajaNombre = new JTextField();
        cajaNombre.setBounds(padding + labelWidth, yStart, fieldWidth, fieldHeight);
        cajaNombre.setFont(new Font("Segoe UI", Font.PLAIN, 12)); // Reducimos la fuente
        cajaNombre.setForeground(Color.BLACK);
        cajaNombre.setBackground(new Color(245, 245, 245));
        cajaNombre.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        cajaNombre.setText("Ingrese el nombre");
        cajaNombre.setForeground(Color.GRAY);
        cajaNombre.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (cajaNombre.getText().equals("Ingrese el nombre")) {
                    cajaNombre.setText("");
                    cajaNombre.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (cajaNombre.getText().isEmpty()) {
                    cajaNombre.setText("Ingrese el nombre");
                    cajaNombre.setForeground(Color.GRAY);
                }
            }
        });
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
        cajaAPP.setText("Ingrese el apellido paterno");
        cajaAPP.setForeground(Color.GRAY);
        cajaAPP.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (cajaAPP.getText().equals("Ingrese el apellido paterno")) {
                    cajaAPP.setText("");
                    cajaAPP.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (cajaAPP.getText().isEmpty()) {
                    cajaAPP.setText("Ingrese el apellido paterno");
                    cajaAPP.setForeground(Color.GRAY);
                }
            }
        });
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
        cajaAPM.setText("Ingrese el apellido materno");
        cajaAPM.setForeground(Color.GRAY);
        cajaAPM.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (cajaAPM.getText().equals("Ingrese el apellido materno")) {
                    cajaAPM.setText("");
                    cajaAPM.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (cajaAPM.getText().isEmpty()) {
                    cajaAPM.setText("Ingrese el apellido materno");
                    cajaAPM.setForeground(Color.GRAY);
                }
            }
        });
        formPanel.add(cajaAPM);

        // Correo
        JLabel correoLabel = new JLabel("Correo Electrónico:");
        correoLabel.setBounds(padding, yStart + 3 * yGap, labelWidth, 20);
        correoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        correoLabel.setForeground(Color.DARK_GRAY);
        formPanel.add(correoLabel);

        cajaCorreo = new JTextField();
        cajaCorreo.setBounds(padding + labelWidth, yStart + 3 * yGap, fieldWidth, fieldHeight);
        cajaCorreo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cajaCorreo.setForeground(Color.BLACK);
        cajaCorreo.setBackground(new Color(245, 245, 245));
        cajaCorreo.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        cajaCorreo.setText("ejemplo@correo.com");
        cajaCorreo.setForeground(Color.GRAY);
        cajaCorreo.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (cajaCorreo.getText().equals("ejemplo@correo.com")) {
                    cajaCorreo.setText("");
                    cajaCorreo.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (cajaCorreo.getText().isEmpty()) {
                    cajaCorreo.setText("ejemplo@correo.com");
                    cajaCorreo.setForeground(Color.GRAY);
                }
            }
        });
        formPanel.add(cajaCorreo);

        // Contraseña
        JLabel contraLabel = new JLabel("Contraseña:");
        contraLabel.setBounds(padding, yStart + 4 * yGap, labelWidth, 20);
        contraLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        contraLabel.setForeground(Color.DARK_GRAY);
        formPanel.add(contraLabel);

        cajaContra = new JTextField();
        cajaContra.setBounds(padding + labelWidth, yStart + 4 * yGap, fieldWidth, fieldHeight);
        cajaContra.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cajaContra.setForeground(Color.BLACK);
        cajaContra.setBackground(new Color(245, 245, 245));
        cajaContra.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        cajaContra.setText("Ingrese su contraseña");
        cajaContra.setForeground(Color.GRAY);
        cajaContra.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (cajaContra.getText().equals("Ingrese su contraseña")) {
                    cajaContra.setText("");
                    cajaContra.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (cajaContra.getText().isEmpty()) {
                    cajaContra.setText("Ingrese su contraseña");
                    cajaContra.setForeground(Color.GRAY);
                }
            }
        });
        formPanel.add(cajaContra);

        // RU
        JLabel ruLabel = new JLabel("R.U.:");
        ruLabel.setBounds(padding, yStart + 5 * yGap, labelWidth, 20);
        ruLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        ruLabel.setForeground(Color.DARK_GRAY);
        formPanel.add(ruLabel);

        cajaRU = new JTextField();
        cajaRU.setBounds(padding + labelWidth, yStart + 5 * yGap, fieldWidth, fieldHeight);
        cajaRU.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cajaRU.setForeground(Color.BLACK);
        cajaRU.setBackground(new Color(245, 245, 245));
        cajaRU.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        cajaRU.setText("Ingrese el R.U.");
        cajaRU.setForeground(Color.GRAY);
        cajaRU.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (cajaRU.getText().equals("Ingrese el R.U.")) {
                    cajaRU.setText("");
                    cajaRU.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (cajaRU.getText().isEmpty()) {
                    cajaRU.setText("Ingrese el R.U.");
                    cajaRU.setForeground(Color.GRAY);
                }
            }
        });
        formPanel.add(cajaRU);

        // Rol
        JLabel rolLabel = new JLabel("Rol:");
        rolLabel.setBounds(padding, yStart + 6 * yGap, labelWidth, 20);
        rolLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        rolLabel.setForeground(Color.DARK_GRAY);
        formPanel.add(rolLabel);

        cajarol = new JComboBox<>(new String[]{"Docente", "Estudiante"});
        cajarol.setBounds(padding + labelWidth, yStart + 6 * yGap, fieldWidth, fieldHeight);
        cajarol.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cajarol.setBackground(new Color(245, 245, 245));
        cajarol.setForeground(Color.BLACK);
        formPanel.add(cajarol);

        // CI
        JLabel ciLabel = new JLabel("C.I.:");
        ciLabel.setBounds(padding, yStart + 7 * yGap, labelWidth, 20);
        ciLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        ciLabel.setForeground(Color.DARK_GRAY);
        formPanel.add(ciLabel);

        cajaCI = new JTextField();
        cajaCI.setBounds(padding + labelWidth, yStart + 7 * yGap, fieldWidth, fieldHeight);
        cajaCI.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cajaCI.setForeground(Color.BLACK);
        cajaCI.setBackground(new Color(245, 245, 245));
        cajaCI.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        cajaCI.setText("Ingrese el C.I.");
        cajaCI.setForeground(Color.GRAY);
        cajaCI.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (cajaCI.getText().equals("Ingrese el C.I.")) {
                    cajaCI.setText("");
                    cajaCI.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (cajaCI.getText().isEmpty()) {
                    cajaCI.setText("Ingrese el C.I.");
                    cajaCI.setForeground(Color.GRAY);
                }
            }
        });
        formPanel.add(cajaCI);

        // Botones
        int buttonWidth = 120; // Reducimos el ancho de los botones
        int buttonHeight = 30; // Reducimos la altura de los botones
        int buttonY = yStart + 8 * yGap + 10; // Ajustamos la posición para que esté dentro del panel

        // Botón "Guardar"
        JButton botonGuardar = new JButton("Guardar");
        botonGuardar.setBounds(padding, buttonY, buttonWidth, buttonHeight);
        botonGuardar.setBackground(new Color(30, 120, 60));
        botonGuardar.setForeground(Color.WHITE);
        botonGuardar.setFont(new Font("Segoe UI", Font.BOLD, 14)); // Reducimos la fuente
        botonGuardar.setFocusPainted(false);
        botonGuardar.setBorderPainted(false);
        botonGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botonGuardar.setBorder(new LineBorder(new Color(30, 120, 60), 1, true));
        // Efecto hover
        botonGuardar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                botonGuardar.setBackground(new Color(50, 140, 80));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                botonGuardar.setBackground(new Color(30, 120, 60));
            }
        });
        // Acción del botón "Guardar"
        botonGuardar.addActionListener(e -> guardarUsuario());
        formPanel.add(botonGuardar);

        // Botón "Cancelar"
        JButton botonCancelar = new JButton("Cancelar");
        botonCancelar.setBounds(padding + labelWidth + fieldWidth - buttonWidth, buttonY, buttonWidth, buttonHeight);
        botonCancelar.setBackground(new Color(180, 70, 70));
        botonCancelar.setForeground(Color.WHITE);
        botonCancelar.setFont(new Font("Segoe UI", Font.BOLD, 14)); // Reducimos la fuente
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

    private void guardarUsuario() {
    try {
        // Recoger los datos de los campos
        String nombre = cajaNombre.getText().trim();
        String APP = cajaAPP.getText().trim();
        String APM = cajaAPM.getText().trim();
        String correo = cajaCorreo.getText().trim();
        String password = cajaContra.getText().trim();
        String ruText = cajaRU.getText().trim();
        String ciText = cajaCI.getText().trim();
        String role = (String) cajarol.getSelectedItem();

        // Validar campos vacíos y placeholders
        if (nombre.isEmpty() || nombre.equals("Ingrese el nombre") ||
            APP.isEmpty() || APP.equals("Ingrese el apellido paterno") ||
            APM.isEmpty() || APM.equals("Ingrese el apellido materno") ||
            correo.isEmpty() || correo.equals("ejemplo@correo.com") ||
            password.isEmpty() || password.equals("Ingrese su contraseña") ||
            ruText.isEmpty() || ruText.equals("Ingrese el R.U.") ||
            ciText.isEmpty() || ciText.equals("Ingrese el C.I.")) {
            throw new CredencialesInvalidas("Por favor, complete todos los campos");
        }

      

        // Convertir RU y CI a números
        int ru = Integer.parseInt(ruText);
        int ci = Integer.parseInt(ciText);

        // ENCRIPTAR LA CONTRASEÑA CON BCRYPT
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        // Crear una nueva instancia de Usuario con la contraseña encriptada
        Usuario nuevoUsuario = new Usuario(ru, nombre, APP, APM, hashedPassword, ci, role, correo);
        ControlUsuario controlUsuario = new ControlUsuario();
        controlUsuario.insertar(nuevoUsuario);

        // Mostrar mensaje de éxito
        JOptionPane.showMessageDialog(this, "Usuario guardado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        dispose();

    } catch (NumberFormatException nfe) {
        mostrarMensajeError("RU o CI deben ser números válidos");
    } catch (CredencialesInvalidas ex) {
        mostrarMensajeError(ex.getMessage());
    } catch (SQLException sqle) {
        String msg = sqle.getMessage();
        if (msg.contains("Duplicate entry")) {
            mostrarMensajeError("El RU, CI o correo ya está registrado");
        } else {
            mostrarMensajeError("Error al guardar el usuario:\n" + msg);
        }
    } catch (Exception ex) {
        mostrarMensajeError("Error del sistema: " + ex.getMessage());
    }
}


    private void mostrarMensajeError(String mensaje) {
        JOptionPane optionPane = new JOptionPane(
            mensaje,
            JOptionPane.ERROR_MESSAGE,
            JOptionPane.DEFAULT_OPTION,
            null,
            new Object[] {"Aceptar"},
            "Aceptar"
        );
        JDialog dialog = optionPane.createDialog(this, "Error");
        dialog.setVisible(true);
    }
    
}