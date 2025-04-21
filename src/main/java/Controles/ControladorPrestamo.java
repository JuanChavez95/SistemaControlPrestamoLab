/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controles;

import Clases.Prestamo;
import DataBase.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador para operaciones CRUD sobre la tabla prestamo.
 * Autor: Equipo Soldados Caídos
 */
public class ControladorPrestamo {

    // Insertar un préstamo (sin ID)
    public int insertar(Prestamo prestamo) throws SQLException {
        String sql = "INSERT INTO prestamo (ru_usuario, ru_administrador, id_horario, fecha_prestamo, " +
                    "hora_prestamo, estado_prestamo, observaciones) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, prestamo.getRuUsuario());
            
            // Manejo de valores que pueden ser NULL
            if (prestamo.getRuAdministrador() != null) {
                stmt.setInt(2, prestamo.getRuAdministrador());
            } else {
                stmt.setNull(2, java.sql.Types.INTEGER);
            }
            
            if (prestamo.getIdHorario() != null) {
                stmt.setInt(3, prestamo.getIdHorario());
            } else {
                stmt.setNull(3, java.sql.Types.INTEGER);
            }
            
            stmt.setDate(4, prestamo.getFechaPrestamo());
            stmt.setString(5, prestamo.getHoraPrestamo());
            stmt.setString(6, prestamo.getEstadoPrestamo());
            stmt.setString(7, prestamo.getObservaciones());
            
            stmt.executeUpdate();
            
            // Obtener el ID generado
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return -1;
    }

    // Listar todos los préstamos
    public List<Prestamo> listar() throws SQLException {
        List<Prestamo> lista = new ArrayList<>();
        String sql = "SELECT * FROM prestamo";
        try (Connection conn = ConexionBD.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Prestamo(
                    rs.getInt("id_prestamo"),
                    rs.getInt("ru_usuario"),
                    rs.getObject("ru_administrador") != null ? rs.getInt("ru_administrador") : null,
                    rs.getObject("id_horario") != null ? rs.getInt("id_horario") : null,
                    rs.getDate("fecha_prestamo"),
                    rs.getString("hora_prestamo"),
                    rs.getString("estado_prestamo"),
                    rs.getString("observaciones")
                ));
            }
        }
        return lista;
    }

    // Actualizar un préstamo por ID
    public void actualizar(Prestamo prestamo) throws SQLException {
        String sql = "UPDATE prestamo SET ru_usuario = ?, ru_administrador = ?, id_horario = ?, " +
                    "fecha_prestamo = ?, hora_prestamo = ?, estado_prestamo = ?, observaciones = ? " +
                    "WHERE id_prestamo = ?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, prestamo.getRuUsuario());
            
            // Manejo de valores que pueden ser NULL
            if (prestamo.getRuAdministrador() != null) {
                stmt.setInt(2, prestamo.getRuAdministrador());
            } else {
                stmt.setNull(2, java.sql.Types.INTEGER);
            }
            
            if (prestamo.getIdHorario() != null) {
                stmt.setInt(3, prestamo.getIdHorario());
            } else {
                stmt.setNull(3, java.sql.Types.INTEGER);
            }
            
            stmt.setDate(4, prestamo.getFechaPrestamo());
            stmt.setString(5, prestamo.getHoraPrestamo());
            stmt.setString(6, prestamo.getEstadoPrestamo());
            stmt.setString(7, prestamo.getObservaciones());
            stmt.setInt(8, prestamo.getIdPrestamo());
            
            stmt.executeUpdate();
        }
    }

    // Eliminar un préstamo por ID
    public void eliminar(int idPrestamo) throws SQLException {
        String sql = "DELETE FROM prestamo WHERE id_prestamo = ?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPrestamo);
            stmt.executeUpdate();
        }
    }
    
    // Buscar un préstamo por ID
    public Prestamo buscarPorId(int idPrestamo) throws SQLException {
        String sql = "SELECT * FROM prestamo WHERE id_prestamo = ?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPrestamo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Prestamo(
                        rs.getInt("id_prestamo"),
                        rs.getInt("ru_usuario"),
                        rs.getObject("ru_administrador") != null ? rs.getInt("ru_administrador") : null,
                        rs.getObject("id_horario") != null ? rs.getInt("id_horario") : null,
                        rs.getDate("fecha_prestamo"),
                        rs.getString("hora_prestamo"),
                        rs.getString("estado_prestamo"),
                        rs.getString("observaciones")
                    );
                }
            }
        }
        return null;
    }
    
    // Listar préstamos por usuario
    public List<Prestamo> listarPorUsuario(int ruUsuario) throws SQLException {
        List<Prestamo> lista = new ArrayList<>();
        String sql = "SELECT * FROM prestamo WHERE ru_usuario = ?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, ruUsuario);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Prestamo(
                        rs.getInt("id_prestamo"),
                        rs.getInt("ru_usuario"),
                        rs.getObject("ru_administrador") != null ? rs.getInt("ru_administrador") : null,
                        rs.getObject("id_horario") != null ? rs.getInt("id_horario") : null,
                        rs.getDate("fecha_prestamo"),
                        rs.getString("hora_prestamo"),
                        rs.getString("estado_prestamo"),
                        rs.getString("observaciones")
                    ));
                }
            }
        }
        return lista;
    }
    
    // Listar préstamos activos
    public List<Prestamo> listarActivos() throws SQLException {
        List<Prestamo> lista = new ArrayList<>();
        String sql = "SELECT * FROM prestamo WHERE estado_prestamo = 'En curso'";
        try (Connection conn = ConexionBD.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Prestamo(
                    rs.getInt("id_prestamo"),
                    rs.getInt("ru_usuario"),
                    rs.getObject("ru_administrador") != null ? rs.getInt("ru_administrador") : null,
                    rs.getObject("id_horario") != null ? rs.getInt("id_horario") : null,
                    rs.getDate("fecha_prestamo"),
                    rs.getString("hora_prestamo"),
                    rs.getString("estado_prestamo"),
                    rs.getString("observaciones")
                ));
            }
        }
        return lista;
    }
}