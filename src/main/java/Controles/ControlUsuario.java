/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controles;

/**
 *
 * @author Equipo Soldados Caídos
 */

import java.sql.Connection;
import java.sql.SQLException;
import Clases.Usuario;
import DataBase.ConexionBD;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ControlUsuario {
    
    // Método para insertar un usuario
    public void insertar(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuario (ru, contra, nombre, apellido_paterno, apellido_materno, ci, rol, email) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionBD.conectar();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, usuario.getRu());
            stmt.setString(2, usuario.getContrasena());
            stmt.setString(3, usuario.getNombre());
            stmt.setString(4, usuario.getApellidoPaterno());
            stmt.setString(5, usuario.getApellidoMaterno());
            stmt.setInt(6, usuario.getCi());
            stmt.setString(7, usuario.getRol());
            stmt.setString(8, usuario.getCorreo());
            stmt.executeUpdate();
        }
    }
    
    // Método para listar los usuarios
    public List<Usuario> listar() throws SQLException {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuario";
        try (Connection conn = ConexionBD.conectar();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                usuarios.add(new Usuario(
                    rs.getInt("ru"),
                    rs.getString("nombre"),
                    rs.getString("apellido_paterno"),
                    rs.getString("apellido_materno"),
                    rs.getString("contra"),
                    rs.getInt("ci"),
                    rs.getString("rol"),
                    rs.getString("email")
                ));
            }
        }
        return usuarios;
    }

    // Método para actualizar un usuario
    public void actualizar(Usuario usuario) throws SQLException {
        String sql = "UPDATE usuario SET contra = ?, nombre = ?, apellido_paterno = ?, apellido_materno = ?, ci = ?, rol = ?, email = ? WHERE ru = ?";
        try (Connection conn = ConexionBD.conectar();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usuario.getContrasena());
            stmt.setString(2, usuario.getNombre());
            stmt.setString(3, usuario.getApellidoPaterno());
            stmt.setString(4, usuario.getApellidoMaterno());
            stmt.setInt(5, usuario.getCi());
            stmt.setString(6, usuario.getRol());
            stmt.setString(7, usuario.getCorreo());
            stmt.setInt(8, usuario.getRu());
            stmt.executeUpdate();
        }
    }

    // Método para eliminar un usuario
    public void eliminar(int ru) throws SQLException {
        String sql = "DELETE FROM usuario WHERE ru = ?";
        try (Connection conn = ConexionBD.conectar();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, ru);
            stmt.executeUpdate();
        }
    }
}
