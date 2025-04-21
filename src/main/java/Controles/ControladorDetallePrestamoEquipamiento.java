/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controles;

import Clases.DetallePrestamoEquipamiento;
import DataBase.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador para operaciones CRUD sobre la tabla detalle_prestamo_equipamiento.
 * Autor: Equipo Soldados Caídos
 */
public class ControladorDetallePrestamoEquipamiento {

    // Insertar un detalle de préstamo de equipamiento (sin ID)
    public void insertar(DetallePrestamoEquipamiento detalle) throws SQLException {
        String sql = "INSERT INTO detalle_prestamo_equipamiento (id_prestamo, id_equipamiento) VALUES (?, ?)";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, detalle.getIdPrestamo());
            stmt.setInt(2, detalle.getIdEquipamiento());
            stmt.executeUpdate();
        }
    }

    // Listar todos los detalles de préstamo de equipamiento
    public List<DetallePrestamoEquipamiento> listar() throws SQLException {
        List<DetallePrestamoEquipamiento> lista = new ArrayList<>();
        String sql = "SELECT * FROM detalle_prestamo_equipamiento";
        try (Connection conn = ConexionBD.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new DetallePrestamoEquipamiento(
                    rs.getInt("id_detalle"),
                    rs.getInt("id_prestamo"),
                    rs.getInt("id_equipamiento")
                ));
            }
        }
        return lista;
    }

    // Actualizar un detalle de préstamo de equipamiento por ID
    public void actualizar(DetallePrestamoEquipamiento detalle) throws SQLException {
        String sql = "UPDATE detalle_prestamo_equipamiento SET id_prestamo = ?, id_equipamiento = ? WHERE id_detalle = ?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, detalle.getIdPrestamo());
            stmt.setInt(2, detalle.getIdEquipamiento());
            stmt.setInt(3, detalle.getIdDetalle());
            stmt.executeUpdate();
        }
    }

    // Eliminar un detalle de préstamo de equipamiento por ID
    public void eliminar(int idDetalle) throws SQLException {
        String sql = "DELETE FROM detalle_prestamo_equipamiento WHERE id_detalle = ?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idDetalle);
            stmt.executeUpdate();
        }
    }
    
    // Buscar un detalle de préstamo de equipamiento por ID
    public DetallePrestamoEquipamiento buscarPorId(int idDetalle) throws SQLException {
        String sql = "SELECT * FROM detalle_prestamo_equipamiento WHERE id_detalle = ?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idDetalle);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new DetallePrestamoEquipamiento(
                        rs.getInt("id_detalle"),
                        rs.getInt("id_prestamo"),
                        rs.getInt("id_equipamiento")
                    );
                }
            }
        }
        return null;
    }
    
    // Listar detalles por ID de préstamo
    public List<DetallePrestamoEquipamiento> listarPorPrestamo(int idPrestamo) throws SQLException {
        List<DetallePrestamoEquipamiento> lista = new ArrayList<>();
        String sql = "SELECT * FROM detalle_prestamo_equipamiento WHERE id_prestamo = ?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPrestamo);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(new DetallePrestamoEquipamiento(
                        rs.getInt("id_detalle"),
                        rs.getInt("id_prestamo"),
                        rs.getInt("id_equipamiento")
                    ));
                }
            }
        }
        return lista;
    }
    
    // Listar detalles por ID de equipamiento
    public List<DetallePrestamoEquipamiento> listarPorEquipamiento(int idEquipamiento) throws SQLException {
        List<DetallePrestamoEquipamiento> lista = new ArrayList<>();
        String sql = "SELECT * FROM detalle_prestamo_equipamiento WHERE id_equipamiento = ?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idEquipamiento);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(new DetallePrestamoEquipamiento(
                        rs.getInt("id_detalle"),
                        rs.getInt("id_prestamo"),
                        rs.getInt("id_equipamiento")
                    ));
                }
            }
        }
        return lista;
    }
    
    // Verificar si un equipamiento está en uso (prestado)
    public boolean equipamientoEnUso(int idEquipamiento) throws SQLException {
        String sql = "SELECT COUNT(*) FROM detalle_prestamo_equipamiento dpe " +
                     "JOIN prestamo p ON dpe.id_prestamo = p.id_prestamo " +
                     "WHERE dpe.id_equipamiento = ? AND p.estado_prestamo = 'En curso'";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idEquipamiento);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
}