/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Paneles;

import Clases.Usuario;
import Controles.ControlUsuario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

/**
 * Panel para la gestión de usuarios: buscar, modificar y eliminar
 * @author DOC
 */
public class PanelEditar extends JPanel {
    // Componentes de la UI
    JTextField txtBuscarRU;
    JButton btnBuscar;
    JButton btnModificar;
    JButton btnEliminar;
    JButton btnLimpiar;
    
    // Campos para mostrar/editar datos
    private JTextField txtRU;
    private JTextField txtNombre;
    private JTextField txtApellidoPaterno;
    private JTextField txtApellidoMaterno;
    private JTextField txtCI;
    private JTextField txtCorreo;
    private JTextField txtRol;
    private JPasswordField txtContrasena;
    
    // Control para operaciones de base de datos
    private ControlUsuario controlUsuario;
    
    // Usuario actual seleccionado
    private Usuario usuarioActual;
    
    public PanelEditar() {
        // Inicializar el control
        controlUsuario = new ControlUsuario();
        
        // Configuración del panel
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        
        // Panel de título
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(new Color(65, 105, 225)); // Azul real
        
        JLabel titleLabel = new JLabel("Ajustes de Usuario");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        titlePanel.add(titleLabel);
        
        add(titlePanel, BorderLayout.NORTH);
        
        // Panel de búsqueda
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(new EmptyBorder(10, 20, 10, 20));
        
        JLabel lblBuscar = new JLabel("Buscar por RU:");
        txtBuscarRU = new JTextField(10);
        btnBuscar = new JButton("Buscar");
        btnBuscar.setBackground(new Color(0, 123, 255));
        btnBuscar.setForeground(Color.WHITE);
        
        searchPanel.add(lblBuscar);
        searchPanel.add(txtBuscarRU);
        searchPanel.add(btnBuscar);
        
        // Panel de formulario
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Primera columna - Labels
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("RU:"), gbc);
        
        gbc.gridy = 1;
        formPanel.add(new JLabel("Nombre:"), gbc);
        
        gbc.gridy = 2;
        formPanel.add(new JLabel("Apellido Paterno:"), gbc);
        
        gbc.gridy = 3;
        formPanel.add(new JLabel("Apellido Materno:"), gbc);
        
        gbc.gridy = 4;
        formPanel.add(new JLabel("CI:"), gbc);
        
        // Segunda columna - TextFields
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        txtRU = new JTextField(20);
        txtRU.setEditable(false); // No se puede cambiar el RU
        formPanel.add(txtRU, gbc);
        
        gbc.gridy = 1;
        txtNombre = new JTextField(20);
        formPanel.add(txtNombre, gbc);
        
        gbc.gridy = 2;
        txtApellidoPaterno = new JTextField(20);
        formPanel.add(txtApellidoPaterno, gbc);
        
        gbc.gridy = 3;
        txtApellidoMaterno = new JTextField(20);
        formPanel.add(txtApellidoMaterno, gbc);
        
        gbc.gridy = 4;
        txtCI = new JTextField(20);
        formPanel.add(txtCI, gbc);
        
        // Tercera columna - Labels
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Rol:"), gbc);
        
        gbc.gridy = 1;
        formPanel.add(new JLabel("Correo:"), gbc);
        
        gbc.gridy = 2;
        formPanel.add(new JLabel("Contraseña:"), gbc);
        
        // Cuarta columna - TextFields
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        txtRol = new JTextField(20);
        formPanel.add(txtRol, gbc);
        
        gbc.gridy = 1;
        txtCorreo = new JTextField(20);
        formPanel.add(txtCorreo, gbc);
        
        gbc.gridy = 2;
        txtContrasena = new JPasswordField(20);
        formPanel.add(txtContrasena, gbc);
        
        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(new EmptyBorder(20, 0, 20, 0));
        
        btnModificar = new JButton("Modificar");
        btnModificar.setBackground(new Color(40, 167, 69)); // Verde
        btnModificar.setForeground(Color.WHITE);
        
        btnEliminar = new JButton("Eliminar");
        btnEliminar.setBackground(new Color(220, 53, 69)); // Rojo
        btnEliminar.setForeground(Color.WHITE);
        
        btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setBackground(new Color(108, 117, 125)); // Gris
        btnLimpiar.setForeground(Color.WHITE);
        
        buttonPanel.add(btnModificar);
        buttonPanel.add(btnEliminar);
        buttonPanel.add(btnLimpiar);
        
        // Panel contenedor principal
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.add(searchPanel, BorderLayout.NORTH);
        contentPanel.add(formPanel, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(contentPanel, BorderLayout.CENTER);
        
        // Configurar los listeners de los botones
        configurarListeners();
        
        // Inicialmente deshabilitar botones de modificación y eliminación
        btnModificar.setEnabled(false);
        btnEliminar.setEnabled(false);
    }
    
    private void configurarListeners() {
        // Botón de búsqueda
        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarUsuario();
            }
        });
        
        // Botón de modificación
        btnModificar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modificarUsuario();
            }
        });
        
        // Botón de eliminación
        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarUsuario();
            }
        });
        
        // Botón de limpiar
        btnLimpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarCampos();
            }
        });
    }
    
    private void buscarUsuario() {
        try {
            String ruText = txtBuscarRU.getText().trim();
            if (ruText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, ingrese un RU para buscar.", 
                        "Campo vacío", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int ru;
            try {
                ru = Integer.parseInt(ruText);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "El RU debe ser un número entero.", 
                        "Formato incorrecto", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Buscar el usuario en la base de datos
            List<Usuario> usuarios = controlUsuario.listar();
            usuarioActual = null;
            
            for (Usuario usuario : usuarios) {
                if (usuario.getRu() == ru) {
                    usuarioActual = usuario;
                    break;
                }
            }
            
            if (usuarioActual != null) {
                // Mostrar datos del usuario
                mostrarDatosUsuario();
                // Habilitar botones de modificación y eliminación
                btnModificar.setEnabled(true);
                btnEliminar.setEnabled(true);
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró ningún usuario con el RU especificado.", 
                        "Usuario no encontrado", JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
            }
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al buscar el usuario: " + ex.getMessage(), 
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private void mostrarDatosUsuario() {
        if (usuarioActual != null) {
            txtRU.setText(String.valueOf(usuarioActual.getRu()));
            txtNombre.setText(usuarioActual.getNombre());
            txtApellidoPaterno.setText(usuarioActual.getApellidoPaterno());
            txtApellidoMaterno.setText(usuarioActual.getApellidoMaterno());
            txtCI.setText(String.valueOf(usuarioActual.getCi()));
            txtRol.setText(usuarioActual.getRol());
            txtCorreo.setText(usuarioActual.getCorreo());
            txtContrasena.setText(usuarioActual.getContrasena());
        }
    }
    
    private void modificarUsuario() {
        try {
            // Verificar que se haya seleccionado un usuario
            if (usuarioActual == null) {
                JOptionPane.showMessageDialog(this, "Debe buscar un usuario primero.", 
                        "Usuario no seleccionado", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Validar campos
            if (txtNombre.getText().trim().isEmpty() || 
                txtApellidoPaterno.getText().trim().isEmpty() || 
                txtCI.getText().trim().isEmpty() || 
                txtRol.getText().trim().isEmpty() || 
                txtCorreo.getText().trim().isEmpty() || 
                new String(txtContrasena.getPassword()).trim().isEmpty()) {
                
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", 
                        "Campos vacíos", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Actualizar datos del usuario
            usuarioActual.setNombre(txtNombre.getText().trim());
            usuarioActual.setApellidoPaterno(txtApellidoPaterno.getText().trim());
            usuarioActual.setApellidoMaterno(txtApellidoMaterno.getText().trim());
            
            try {
                usuarioActual.setCi(Integer.parseInt(txtCI.getText().trim()));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "El CI debe ser un número entero.", 
                        "Formato incorrecto", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            usuarioActual.setRol(txtRol.getText().trim());
            usuarioActual.setCorreo(txtCorreo.getText().trim());
            usuarioActual.setContrasena(new String(txtContrasena.getPassword()));
            
            // Guardar cambios en la base de datos
            controlUsuario.actualizar(usuarioActual);
            
            JOptionPane.showMessageDialog(this, "Usuario actualizado correctamente.", 
                    "Actualización exitosa", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al actualizar el usuario: " + ex.getMessage(), 
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private void eliminarUsuario() {
        try {
            // Verificar que se haya seleccionado un usuario
            if (usuarioActual == null) {
                JOptionPane.showMessageDialog(this, "Debe buscar un usuario primero.", 
                        "Usuario no seleccionado", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Confirmar eliminación
            int opcion = JOptionPane.showConfirmDialog(this, 
                    "¿Está seguro de que desea eliminar al usuario con RU " + usuarioActual.getRu() + "?", 
                    "Confirmar eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            
            if (opcion == JOptionPane.YES_OPTION) {
                // Eliminar usuario
                controlUsuario.eliminar(usuarioActual.getRu());
                
                JOptionPane.showMessageDialog(this, "Usuario eliminado correctamente.", 
                        "Eliminación exitosa", JOptionPane.INFORMATION_MESSAGE);
                
                // Limpiar campos después de eliminar
                limpiarCampos();
            }
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al eliminar el usuario: " + ex.getMessage(), 
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private void limpiarCampos() {
        txtBuscarRU.setText("");
        txtRU.setText("");
        txtNombre.setText("");
        txtApellidoPaterno.setText("");
        txtApellidoMaterno.setText("");
        txtCI.setText("");
        txtRol.setText("");
        txtCorreo.setText("");
        txtContrasena.setText("");
        
        usuarioActual = null;
        btnModificar.setEnabled(false);
        btnEliminar.setEnabled(false);
    }
}