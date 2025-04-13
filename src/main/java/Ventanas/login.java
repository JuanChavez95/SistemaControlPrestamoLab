/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Ventanas;

import DataBase.ConexionBD;
import exceptions.CredencialesInvalidas;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 * Ventana de inicio de sesión para el Sistema de Control de Préstamos de Laboratorio
 * @author DOC
 */
public class login extends JFrame {
    private String rol;
    private JTextField cajaNombre;
    private JPasswordField cajaPassword;
    private JButton togglePasswordButton;
    private boolean isPasswordVisible = false;

    public login() {
        inicializarComponentes();
        setLocationRelativeTo(null); // Centra la ventana en la pantalla
        setVisible(true);
    }

    private void inicializarComponentes() {
        setTitle("LOGIN DEL SISTEMA");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(600, 400)); // Tamaño mínimo para evitar que sea demasiado pequeño
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximizar la ventana al inicio
        setResizable(true); // Hacer la ventana redimensionable

        // Fondo con imagen
        JPanel fondoPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Cargar la imagen de fondo - Usar la ruta del MODELO 2 si está disponible
                ImageIcon backgroundImage = new ImageIcon("C:\\Users\\DOC\\Desktop\\PROYECTO V\\fondo.jpg");
                if (backgroundImage.getIconWidth() == -1) {
                    System.err.println("No se pudo cargar la imagen de fondo.");
                    // Fondo de respaldo (degradado) si la imagen no se carga
                    GradientPaint gradient = new GradientPaint(0, 0, new Color(81, 0, 255), 0, getHeight(), new Color(120, 60, 255));
                    g2d.setPaint(gradient);
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                    return;
                }

                // Dibujar la imagen de fondo escalada
                Image img = backgroundImage.getImage();
                g2d.drawImage(img, 0, 0, getWidth(), getHeight(), this);

                // Agregar una capa semitransparente para mejorar la legibilidad
                g2d.setColor(new Color(0, 0, 0, 100));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        fondoPanel.setLayout(new GridBagLayout()); // Usar GridBagLayout para un diseño adaptable
        setContentPane(fondoPanel);

        // Panel de login (centrado, con bordes más redondeados)
        JPanel loginPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getBackground());
                g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 40, 40);
            }

            @Override
            public Dimension getPreferredSize() {
                // Ajustar el tamaño del panel según el tamaño de la ventana
                Dimension screenSize = getParent().getSize();
                int width = Math.min(400, (int) (screenSize.width * 0.4));
                int height = Math.min(440, (int) (screenSize.height * 0.7));
                return new Dimension(width, height);
            }
        };
        loginPanel.setBackground(Color.WHITE);
        loginPanel.setLayout(null);
        loginPanel.setOpaque(false);
        loginPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        loginPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Añadir el panel de login al fondoPanel usando GridBagConstraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.NONE;
        fondoPanel.add(loginPanel, gbc);

        // Listener para ajustar el diseño al redimensionar
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                ajustarComponentes(loginPanel);
            }
        });

        // Campos de login
        crearCamposLogin(loginPanel);
        ajustarComponentes(loginPanel); // Ajustar inicialmente
    }

    private void crearCamposLogin(JPanel panel) {
        // Título del sistema
        JLabel sistemaLabel = new JLabel("Sistema de Préstamos", SwingConstants.CENTER);
        sistemaLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        sistemaLabel.setForeground(new Color(30, 60, 120));
        panel.add(sistemaLabel);

        // RU
        JLabel ruLabel = new JLabel("Registro Universitario (RU):");
        ruLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        ruLabel.setForeground(Color.DARK_GRAY);
        panel.add(ruLabel);

        cajaNombre = new JTextField();
        cajaNombre.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cajaNombre.setForeground(Color.BLACK);
        cajaNombre.setBackground(new Color(245, 245, 245));
        cajaNombre.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        // Placeholder
        cajaNombre.setText("Ingrese su RU");
        cajaNombre.setForeground(Color.GRAY);
        cajaNombre.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (cajaNombre.getText().equals("Ingrese su RU")) {
                    cajaNombre.setText("");
                    cajaNombre.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (cajaNombre.getText().isEmpty()) {
                    cajaNombre.setText("Ingrese su RU");
                    cajaNombre.setForeground(Color.GRAY);
                }
            }
        });
        panel.add(cajaNombre);

        // Contraseña
        JLabel passwordLabel = new JLabel("Contraseña:");
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordLabel.setForeground(Color.DARK_GRAY);
        panel.add(passwordLabel);

        cajaPassword = new JPasswordField();
        cajaPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cajaPassword.setForeground(Color.BLACK);
        cajaPassword.setBackground(new Color(245, 245, 245));
        cajaPassword.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        // Placeholder
        cajaPassword.setText("••••••••");
        cajaPassword.setForeground(Color.GRAY);
        cajaPassword.setEchoChar((char) 0);
        cajaPassword.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (new String(cajaPassword.getPassword()).equals("••••••••")) {
                    cajaPassword.setText("");
                    cajaPassword.setForeground(Color.BLACK);
                    cajaPassword.setEchoChar('•');
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (new String(cajaPassword.getPassword()).isEmpty()) {
                    cajaPassword.setText("••••••••");
                    cajaPassword.setForeground(Color.GRAY);
                    cajaPassword.setEchoChar((char) 0);
                }
            }
        });
        panel.add(cajaPassword);

        // Botón para mostrar/ocultar contraseña (usando emojis)
        togglePasswordButton = new JButton("👁️");
        togglePasswordButton.setBackground(new Color(245, 245, 245));
        togglePasswordButton.setBorder(new LineBorder(new Color(200, 200, 200), 1, true));
        togglePasswordButton.setFocusPainted(false);
        togglePasswordButton.setToolTipText("Mostrar contraseña");
        togglePasswordButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        togglePasswordButton.addActionListener(e -> togglePasswordVisibility());
        togglePasswordButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                togglePasswordButton.setBackground(new Color(230, 230, 230));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                togglePasswordButton.setBackground(new Color(245, 245, 245));
            }
        });
        panel.add(togglePasswordButton);

        // Botón de Ingresar
        JButton botonIngresar = new JButton("Iniciar Sesión");
        botonIngresar.setBackground(new Color(30, 120, 60));
        botonIngresar.setForeground(Color.WHITE);
        botonIngresar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        botonIngresar.setFocusPainted(false);
        botonIngresar.setBorderPainted(false);
        botonIngresar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botonIngresar.setBorder(new LineBorder(new Color(30, 120, 60), 1, true));
        botonIngresar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                botonIngresar.setBackground(new Color(50, 140, 80));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                botonIngresar.setBackground(new Color(30, 120, 60));
            }
        });
        botonIngresar.addActionListener(e -> intentarLogin());
        panel.add(botonIngresar);

        // Botón para nuevo usuario
        JButton botonNuevo = new JButton("Registrarse como Nuevo Usuario");
        botonNuevo.setBackground(Color.WHITE);
        botonNuevo.setForeground(new Color(30, 60, 120));
        botonNuevo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        botonNuevo.setBorderPainted(false);
        botonNuevo.setFocusPainted(false);
        botonNuevo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botonNuevo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                botonNuevo.setForeground(new Color(50, 80, 140));
                botonNuevo.setText("<html><u>Registrarse como Nuevo Usuario</u></html>");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                botonNuevo.setForeground(new Color(30, 60, 120));
                botonNuevo.setText("Registrarse como Nuevo Usuario");
            }
        });
        botonNuevo.addActionListener(e -> abrirFormularioRegistro());
        panel.add(botonNuevo);
    }

    private void ajustarComponentes(JPanel panel) {
        Dimension panelSize = panel.getSize();

        // Ajustar posiciones y tamaños según el tamaño del panel
        int padding = 30;
        int fieldHeight = 40;
        int fieldWidth = panelSize.width - 2 * padding;
        int buttonWidth = fieldWidth;
        int toggleButtonWidth = 30;

        // Título - Ahora solo hay un componente de título
        panel.getComponent(0).setBounds(0, 20, panelSize.width, 40); // sistemaLabel

        // RU
        panel.getComponent(1).setBounds(padding, 110, fieldWidth, 25); // ruLabel
        panel.getComponent(2).setBounds(padding, 140, fieldWidth, fieldHeight); // cajaNombre

        // Contraseña
        panel.getComponent(3).setBounds(padding, 190, fieldWidth, 25); // passwordLabel
        panel.getComponent(4).setBounds(padding, 220, fieldWidth - toggleButtonWidth - 10, fieldHeight); // cajaPassword
        panel.getComponent(5).setBounds(padding + fieldWidth - toggleButtonWidth, 220, toggleButtonWidth, fieldHeight); // togglePasswordButton

        // Botones - Uso exactamente las posiciones del CÓDIGO 1
        panel.getComponent(6).setBounds(padding, 290, buttonWidth, fieldHeight); // botonIngresar
        panel.getComponent(7).setBounds(padding, 350, buttonWidth, 30); // botonNuevo
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            cajaPassword.setEchoChar('•');
            togglePasswordButton.setText("👁️");
            togglePasswordButton.setToolTipText("Mostrar contraseña");
            isPasswordVisible = false;
        } else {
            cajaPassword.setEchoChar((char) 0);
            togglePasswordButton.setText("👁️‍🗨️");
            togglePasswordButton.setToolTipText("Ocultar contraseña");
            isPasswordVisible = true;
        }
    }

    private void intentarLogin() {
        try {
            String ruText = cajaNombre.getText().trim();
            String password = new String(cajaPassword.getPassword());

            if (ruText.isEmpty() || ruText.equals("Ingrese su RU") || 
                password.isEmpty() || password.equals("••••••••")) {
                throw new CredencialesInvalidas("Por favor, complete todos los campos");
            }

            try {
                int ru = Integer.parseInt(ruText);
                if (!validarEnBD(ru, password)) {
                    throw new CredencialesInvalidas("Usuario o contraseña incorrectos");
                }

                // Si pasa la validación, redireccionar según el rol
                if (rol.equals("Administrador")) {
                    new Principal().setVisible(true);
                } else if (rol.equals("Estudiante")) {
                    new Principal3().setVisible(true);
                } else {
                    new Principal2().setVisible(true);
                }
                
                // Mantenemos la lógica del MODELO 2 de no cerrar la ventana de login
                
            } catch (NumberFormatException ex) {
                throw new CredencialesInvalidas("El RU debe ser un número válido");
            }
        } catch (CredencialesInvalidas ex) {
            mostrarMensajeError(ex.getMessage());
        } catch (SQLException ex) {
            mostrarMensajeError("Error al conectar con la base de datos: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void abrirFormularioRegistro() {
        try {
            String ruText = cajaNombre.getText().trim();
            String password = new String(cajaPassword.getPassword());
            
            // Verifica si se están usando credenciales de administrador
            if (!ruText.isEmpty() && !ruText.equals("Ingrese su RU") && 
                !password.isEmpty() && !password.equals("••••••••")) {
                try {
                    int ru = Integer.parseInt(ruText);
                    if (ru == 100 && password.equals("alfha phils")) {
                        new NuevoAdministrador().setVisible(true);
                        return;
                    }
                } catch (NumberFormatException ex) {
                    // Si no es un número, simplemente continuamos al registro normal
                }
            }
            
            // Si no son credenciales de admin o hay error, abre registro normal
            new NuevoUsuario().setVisible(true);
        } catch (Exception ex) {
            mostrarMensajeError("Error al abrir el formulario: " + ex.getMessage());
        }
    }

    private boolean validarEnBD(int ru, String password) throws SQLException {
        String sql = "SELECT * FROM usuario WHERE ru = ? AND contra = ?";
        
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, ru);
            stmt.setString(2, password);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    rol = rs.getString("rol");
                    System.out.println("Rol identificado: " + rol);
                    return true;
                }
                return false;
            }
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