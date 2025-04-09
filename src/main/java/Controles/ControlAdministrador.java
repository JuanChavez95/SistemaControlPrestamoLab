/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controles;

/**
 *
 * @author Soldado Caído: Juan Carlos Chavez Machaca
 */

import Clases.Administrador;
import Clases.Usuario;
import DataBase.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ControlAdministrador {

    public void insertarAdministrador(Administrador admin) throws SQLException {
        Connection conn = null;
        PreparedStatement stmtUsuario = null;
        PreparedStatement stmtAdmin = null;

        String sqlUsuario = "INSERT INTO usuario (ru, contra, nombre, apellido_paterno, apellido_materno, ci, rol, email) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlAdmin = "INSERT INTO administrador (ru, salario, fecha_inicio, nro_titulo) VALUES (?, ?, ?, ?)";

        try {
            conn = ConexionBD.conectar();
            conn.setAutoCommit(false); // Iniciar transacción

            // Insertar en la tabla usuario
            stmtUsuario = conn.prepareStatement(sqlUsuario);
            stmtUsuario.setInt(1, admin.getRu());
            stmtUsuario.setString(2, admin.getContrasena());
            stmtUsuario.setString(3, admin.getNombre());
            stmtUsuario.setString(4, admin.getApellidoPaterno());
            stmtUsuario.setString(5, admin.getApellidoMaterno());
            stmtUsuario.setInt(6, admin.getCi());
            stmtUsuario.setString(7, admin.getRol());
            stmtUsuario.setString(8, admin.getCorreo());
            stmtUsuario.executeUpdate();

            // Insertar en la tabla administrador
            stmtAdmin = conn.prepareStatement(sqlAdmin);
            stmtAdmin.setInt(1, admin.getRu());
            stmtAdmin.setDouble(2, admin.getSalario());
            stmtAdmin.setDate(3, new java.sql.Date(admin.getFechaInicio().getTime()));
            stmtAdmin.setString(4, admin.getNroTitulo());
            stmtAdmin.executeUpdate();

            conn.commit(); // Confirmar transacción
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback(); // Revertir cambios si hay error
            }
            throw e;
        } finally {
            if (stmtUsuario != null) stmtUsuario.close();
            if (stmtAdmin != null) stmtAdmin.close();
            if (conn != null) conn.close();
        }
    }
}
