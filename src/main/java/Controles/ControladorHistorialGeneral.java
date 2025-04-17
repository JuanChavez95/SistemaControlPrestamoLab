/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controles;

import Clases.HistorialGeneral;
import DataBase.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador para operaciones CRUD sobre la tabla historial_general.
 * Autor: Equipo Soldados Caídos
 */
public class ControladorHistorialGeneral {

    /**
     * Inserta un nuevo registro en la tabla historial_general
     * @param historial Objeto HistorialGeneral a insertar
     * @return ID generado automáticamente por la BD
     * @throws SQLException Si ocurre un error en la base de datos
     */
    public int insertar(HistorialGeneral historial) throws SQLException {
        String sql = "INSERT INTO historial_general (ru_administrador, fecha, categoria, descripcion) VALUES (?, ?, ?, ?)";
        int idGenerado = 0;
        
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, historial.getRuAdministrador());
            stmt.setDate(2, new java.sql.Date(historial.getFecha().getTime()));
            stmt.setString(3, historial.getCategoria());
            stmt.setString(4, historial.getDescripcion());
            
            stmt.executeUpdate();
            
            // Obtener el ID generado automáticamente
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    idGenerado = generatedKeys.getInt(1);
                }
            }
        }
        return idGenerado;
    }

    /**
     * Lista todos los registros de historial_general
     * @return Lista de objetos HistorialGeneral
     * @throws SQLException Si ocurre un error en la base de datos
     */
    public List<HistorialGeneral> listar() throws SQLException {
        List<HistorialGeneral> lista = new ArrayList<>();
        String sql = "SELECT * FROM historial_general ORDER BY fecha DESC";
        
        try (Connection conn = ConexionBD.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                lista.add(new HistorialGeneral(
                    rs.getInt("id_historial"),
                    rs.getInt("ru_administrador"),
                    rs.getDate("fecha"),
                    rs.getString("categoria"),
                    rs.getString("descripcion")
                ));
            }
        }
        return lista;
    }

    /**
     * Busca un registro de historial por su ID
     * @param idHistorial ID del historial a buscar
     * @return Objeto HistorialGeneral si existe, null en caso contrario
     * @throws SQLException Si ocurre un error en la base de datos
     */
    public HistorialGeneral buscarPorId(int idHistorial) throws SQLException {
        String sql = "SELECT * FROM historial_general WHERE id_historial = ?";
        
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idHistorial);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new HistorialGeneral(
                        rs.getInt("id_historial"),
                        rs.getInt("ru_administrador"),
                        rs.getDate("fecha"),
                        rs.getString("categoria"),
                        rs.getString("descripcion")
                    );
                }
            }
        }
        return null;
    }

    /**
     * Busca registros de historial por RU del administrador
     * @param ruAdministrador RU del administrador
     * @return Lista de objetos HistorialGeneral
     * @throws SQLException Si ocurre un error en la base de datos
     */
    public List<HistorialGeneral> buscarPorAdministrador(int ruAdministrador) throws SQLException {
        List<HistorialGeneral> lista = new ArrayList<>();
        String sql = "SELECT * FROM historial_general WHERE ru_administrador = ? ORDER BY fecha DESC";
        
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, ruAdministrador);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(new HistorialGeneral(
                        rs.getInt("id_historial"),
                        rs.getInt("ru_administrador"),
                        rs.getDate("fecha"),
                        rs.getString("categoria"),
                        rs.getString("descripcion")
                    ));
                }
            }
        }
        return lista;
    }

    /**
     * Busca registros de historial por categoría
     * @param categoria Categoría a buscar
     * @return Lista de objetos HistorialGeneral
     * @throws SQLException Si ocurre un error en la base de datos
     */
    public List<HistorialGeneral> buscarPorCategoria(String categoria) throws SQLException {
        List<HistorialGeneral> lista = new ArrayList<>();
        String sql = "SELECT * FROM historial_general WHERE categoria = ? ORDER BY fecha DESC";
        
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, categoria);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(new HistorialGeneral(
                        rs.getInt("id_historial"),
                        rs.getInt("ru_administrador"),
                        rs.getDate("fecha"),
                        rs.getString("categoria"),
                        rs.getString("descripcion")
                    ));
                }
            }
        }
        return lista;
    }

    /**
     * Busca registros de historial entre dos fechas
     * @param fechaInicio Fecha de inicio del rango
     * @param fechaFin Fecha de fin del rango
     * @return Lista de objetos HistorialGeneral
     * @throws SQLException Si ocurre un error en la base de datos
     */
    public List<HistorialGeneral> buscarPorRangoFechas(Date fechaInicio, Date fechaFin) throws SQLException {
        List<HistorialGeneral> lista = new ArrayList<>();
        String sql = "SELECT * FROM historial_general WHERE fecha BETWEEN ? AND ? ORDER BY fecha DESC";
        
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, fechaInicio);
            stmt.setDate(2, fechaFin);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(new HistorialGeneral(
                        rs.getInt("id_historial"),
                        rs.getInt("ru_administrador"),
                        rs.getDate("fecha"),
                        rs.getString("categoria"),
                        rs.getString("descripcion")
                    ));
                }
            }
        }
        return lista;
    }

    /**
     * Actualiza un registro de historial por su ID
     * @param historial Objeto HistorialGeneral con los datos actualizados
     * @throws SQLException Si ocurre un error en la base de datos
     */
    public void actualizar(HistorialGeneral historial) throws SQLException {
        String sql = "UPDATE historial_general SET ru_administrador = ?, fecha = ?, categoria = ?, descripcion = ? WHERE id_historial = ?";
        
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, historial.getRuAdministrador());
            stmt.setDate(2, new java.sql.Date(historial.getFecha().getTime()));
            stmt.setString(3, historial.getCategoria());
            stmt.setString(4, historial.getDescripcion());
            stmt.setInt(5, historial.getIdHistorial());
            
            stmt.executeUpdate();
        }
    }

    /**
     * Elimina un registro de historial por su ID
     * @param idHistorial ID del historial a eliminar
     * @throws SQLException Si ocurre un error en la base de datos
     */
    public void eliminar(int idHistorial) throws SQLException {
        String sql = "DELETE FROM historial_general WHERE id_historial = ?";
        
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idHistorial);
            stmt.executeUpdate();
        }
    }
}