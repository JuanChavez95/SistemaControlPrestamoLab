/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controles;

import Clases.DetallePrestamoInsumo;
import DataBase.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador para operaciones CRUD sobre la tabla detalle_prestamo_insumo.
 * Autor: Equipo Soldados Caídos
 */
public class ControladorDetallePrestamoInsumo {

    // Insertar un detalle de préstamo de insumo (sin ID)
    public void insertar(DetallePrestamoInsumo detalle) throws SQLException {
        String sql = "INSERT INTO detalle_prestamo_insumo (id_prestamo, id_insumo, cantidad_inicial, cantidad_final) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, detalle.getIdPrestamo());
            stmt.setInt(2, detalle.getIdInsumo());
            stmt.setInt(3, detalle.getCantidadInicial());
            
            // Manejo de valores que pueden ser NULL
            if (detalle.getCantidadFinal() != null) {
                stmt.setInt(4, detalle.getCantidadFinal());
            } else {
                stmt.setNull(4, java.sql.Types.INTEGER);
            }
            
            stmt.executeUpdate();
        }
    }

    // Listar todos los detalles de préstamo de insumo
    public List<DetallePrestamoInsumo> listar() throws SQLException {
        List<DetallePrestamoInsumo> lista = new ArrayList<>();
        String sql = "SELECT * FROM detalle_prestamo_insumo";
        try (Connection conn = ConexionBD.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new DetallePrestamoInsumo(
                    rs.getInt("id_detalle_insumo"),
                    rs.getInt("id_prestamo"),
                    rs.getInt("id_insumo"),
                    rs.getInt("cantidad_inicial"),
                    rs.getObject("cantidad_final") != null ? rs.getInt("cantidad_final") : null
                ));
            }
        }
        return lista;
    }

    // Actualizar un detalle de préstamo de insumo por ID
    public void actualizar(DetallePrestamoInsumo detalle) throws SQLException {
        String sql = "UPDATE detalle_prestamo_insumo SET id_prestamo = ?, id_insumo = ?, " +
                    "cantidad_inicial = ?, cantidad_final = ? WHERE id_detalle_insumo = ?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, detalle.getIdPrestamo());
            stmt.setInt(2, detalle.getIdInsumo());
            stmt.setInt(3, detalle.getCantidadInicial());
            
            // Manejo de valores que pueden ser NULL
            if (detalle.getCantidadFinal() != null) {
                stmt.setInt(4, detalle.getCantidadFinal());
            } else {
                stmt.setNull(4, java.sql.Types.INTEGER);
            }
            
            stmt.setInt(5, detalle.getIdDetalleInsumo());
            stmt.executeUpdate();
        }
    }

    // Eliminar un detalle de préstamo de insumo por ID
    public void eliminar(int idDetalleInsumo) throws SQLException {
        String sql = "DELETE FROM detalle_prestamo_insumo WHERE id_detalle_insumo = ?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idDetalleInsumo);
            stmt.executeUpdate();
        }
    }
    
    // Buscar un detalle de préstamo de insumo por ID
    public DetallePrestamoInsumo buscarPorId(int idDetalleInsumo) throws SQLException {
        String sql = "SELECT * FROM detalle_prestamo_insumo WHERE id_detalle_insumo = ?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idDetalleInsumo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new DetallePrestamoInsumo(
                        rs.getInt("id_detalle_insumo"),
                        rs.getInt("id_prestamo"),
                        rs.getInt("id_insumo"),
                        rs.getInt("cantidad_inicial"),
                        rs.getObject("cantidad_final") != null ? rs.getInt("cantidad_final") : null
                    );
                }
            }
        }
        return null;
    }
    
    // Listar detalles por ID de préstamo
    public List<DetallePrestamoInsumo> listarPorPrestamo(int idPrestamo) throws SQLException {
        List<DetallePrestamoInsumo> lista = new ArrayList<>();
        String sql = "SELECT * FROM detalle_prestamo_insumo WHERE id_prestamo = ?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPrestamo);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(new DetallePrestamoInsumo(
                        rs.getInt("id_detalle_insumo"),
                        rs.getInt("id_prestamo"),
                        rs.getInt("id_insumo"),
                        rs.getInt("cantidad_inicial"),
                        rs.getObject("cantidad_final") != null ? rs.getInt("cantidad_final") : null
                    ));
                }
            }
        }
        return lista;
    }
    
    // Listar detalles por ID de insumo
    public List<DetallePrestamoInsumo> listarPorInsumo(int idInsumo) throws SQLException {
        List<DetallePrestamoInsumo> lista = new ArrayList<>();
        String sql = "SELECT * FROM detalle_prestamo_insumo WHERE id_insumo = ?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idInsumo);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(new DetallePrestamoInsumo(
                        rs.getInt("id_detalle_insumo"),
                        rs.getInt("id_prestamo"),
                        rs.getInt("id_insumo"),
                        rs.getInt("cantidad_inicial"),
                        rs.getObject("cantidad_final") != null ? rs.getInt("cantidad_final") : null
                    ));
                }
            }
        }
        return lista;
    }
    
    // Actualizar la cantidad final de un insumo prestado
    public void actualizarCantidadFinal(int idDetalleInsumo, int cantidadFinal) throws SQLException {
        String sql = "UPDATE detalle_prestamo_insumo SET cantidad_final = ? WHERE id_detalle_insumo = ?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cantidadFinal);
            stmt.setInt(2, idDetalleInsumo);
            stmt.executeUpdate();
        }
    }
    
    // Obtener el total de insumos prestados por tipo
    public int contarInsumoPrestado(int idInsumo) throws SQLException {
        String sql = "SELECT SUM(cantidad_inicial) FROM detalle_prestamo_insumo dpi " +
                     "JOIN prestamo p ON dpi.id_prestamo = p.id_prestamo " +
                     "WHERE dpi.id_insumo = ? AND p.estado_prestamo = 'En curso' AND dpi.cantidad_final IS NULL";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idInsumo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next() && rs.getObject(1) != null) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }
    
    // Verificar si un insumo está disponible en la cantidad solicitada
    public boolean verificarDisponibilidadInsumo(int idInsumo, int cantidadSolicitada) throws SQLException {
        // Primero obtenemos la cantidad total disponible del insumo
        String sqlCantidadTotal = "SELECT cantidad FROM insumos WHERE id_insumo = ?";
        
        // Luego obtenemos la cantidad actualmente prestada
        String sqlCantidadPrestada = "SELECT SUM(cantidad_inicial) FROM detalle_prestamo_insumo dpi " +
                                    "JOIN prestamo p ON dpi.id_prestamo = p.id_prestamo " +
                                    "WHERE dpi.id_insumo = ? AND p.estado_prestamo = 'En curso' AND dpi.cantidad_final IS NULL";
        
        try (Connection conn = ConexionBD.conectar()) {
            int cantidadTotal = 0;
            int cantidadPrestada = 0;
            
            // Obtener cantidad total
            try (PreparedStatement stmt = conn.prepareStatement(sqlCantidadTotal)) {
                stmt.setInt(1, idInsumo);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        cantidadTotal = rs.getInt("cantidad");
                    }
                }
            }
            
            // Obtener cantidad prestada
            try (PreparedStatement stmt = conn.prepareStatement(sqlCantidadPrestada)) {
                stmt.setInt(1, idInsumo);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next() && rs.getObject(1) != null) {
                        cantidadPrestada = rs.getInt(1);
                    }
                }
            }
            
            // Verificar si hay suficiente disponibilidad
            int cantidadDisponible = cantidadTotal - cantidadPrestada;
            return cantidadDisponible >= cantidadSolicitada;
        }
    }
    
    // Finalizar préstamo de insumo y actualizar inventario
    public void finalizarPrestamoInsumo(int idDetalleInsumo, int cantidadFinal) throws SQLException {
        Connection conn = null;
        try {
            conn = ConexionBD.conectar();
            conn.setAutoCommit(false);
            
            // Obtener información del detalle del préstamo
            DetallePrestamoInsumo detalle = buscarPorId(idDetalleInsumo);
            if (detalle == null) {
                throw new SQLException("No se encontró el detalle de préstamo de insumo");
            }
            
            // Actualizar la cantidad final en el detalle de préstamo
            String sqlDetalle = "UPDATE detalle_prestamo_insumo SET cantidad_final = ? WHERE id_detalle_insumo = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlDetalle)) {
                stmt.setInt(1, cantidadFinal);
                stmt.setInt(2, idDetalleInsumo);
                stmt.executeUpdate();
            }
            
            // Calcular la diferencia para actualizar el inventario
            int diferencia = detalle.getCantidadInicial() - cantidadFinal;
            
            // Actualizar la cantidad en el inventario de insumos
            String sqlInsumo = "UPDATE insumos SET cantidad = cantidad - ? WHERE id_insumo = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlInsumo)) {
                stmt.setInt(1, diferencia);
                stmt.setInt(2, detalle.getIdInsumo());
                stmt.executeUpdate();
            }
            
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    throw new SQLException("Error al realizar rollback: " + ex.getMessage());
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    throw new SQLException("Error al cerrar la conexión: " + e.getMessage());
                }
            }
        }
    }
}