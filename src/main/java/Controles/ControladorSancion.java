/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controles;

import Clases.Sancion;
import DataBase.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;

/**
 * Controlador para operaciones CRUD sobre la tabla sancion.
 * Autor: Equipo Soldados Caídos
 */
public class ControladorSancion {

    // Insertar una sanción y retornar el ID generado
    public int insertar(Sancion sancion) throws SQLException {
        String sql = "INSERT INTO sancion (ru_usuario, id_prestamo, tipo_sancion, descripcion, fecha_sancion, estado_sancion, fecha_inicio, fecha_fin, dias_suspension) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        int idGenerado = -1;
        
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, sancion.getRuUsuario());
            
            if (sancion.getIdPrestamo() != null) {
                stmt.setInt(2, sancion.getIdPrestamo());
            } else {
                stmt.setNull(2, java.sql.Types.INTEGER);
            }
            
            stmt.setString(3, sancion.getTipoSancion());
            stmt.setString(4, sancion.getDescripcion());
            stmt.setDate(5, new Date(sancion.getFechaSancion().getTime()));
            stmt.setString(6, sancion.getEstadoSancion());
            stmt.setDate(7, new Date(sancion.getFechaInicio().getTime()));
            
            if (sancion.getFechaFin() != null) {
                stmt.setDate(8, new Date(sancion.getFechaFin().getTime()));
            } else {
                stmt.setNull(8, java.sql.Types.DATE);
            }
            
            stmt.setInt(9, sancion.getDiasSuspension());
            stmt.executeUpdate();
            
            // Obtener el ID generado
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                idGenerado = generatedKeys.getInt(1);
            }
        }
        
        return idGenerado;
    }

    // Listar todas las sanciones
    public List<Sancion> listar() throws SQLException {
        List<Sancion> lista = new ArrayList<>();
        String sql = "SELECT * FROM sancion";
        
        try (Connection conn = ConexionBD.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Integer idPrestamo = rs.getInt("id_prestamo");
                if (rs.wasNull()) {
                    idPrestamo = null;
                }
                
                Date fechaFin = rs.getDate("fecha_fin");
                
                lista.add(new Sancion(
                    rs.getInt("id_sancion"),
                    rs.getInt("ru_usuario"),
                    idPrestamo,
                    rs.getString("tipo_sancion"),
                    rs.getString("descripcion"),
                    rs.getDate("fecha_sancion"),
                    rs.getString("estado_sancion"),
                    rs.getDate("fecha_inicio"),
                    fechaFin,
                    rs.getInt("dias_suspension")
                ));
            }
        }
        return lista;
    }

    // Buscar sanciones por usuario
    public List<Sancion> buscarPorUsuario(int ruUsuario) throws SQLException {
        List<Sancion> lista = new ArrayList<>();
        String sql = "SELECT * FROM sancion WHERE ru_usuario = ?";
        
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, ruUsuario);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Integer idPrestamo = rs.getInt("id_prestamo");
                    if (rs.wasNull()) {
                        idPrestamo = null;
                    }
                    
                    Date fechaFin = rs.getDate("fecha_fin");
                    
                    lista.add(new Sancion(
                        rs.getInt("id_sancion"),
                        rs.getInt("ru_usuario"),
                        idPrestamo,
                        rs.getString("tipo_sancion"),
                        rs.getString("descripcion"),
                        rs.getDate("fecha_sancion"),
                        rs.getString("estado_sancion"),
                        rs.getDate("fecha_inicio"),
                        fechaFin,
                        rs.getInt("dias_suspension")
                    ));
                }
            }
        }
        return lista;
    }

    // Actualizar una sanción
    public void actualizar(Sancion sancion) throws SQLException {
        String sql = "UPDATE sancion SET ru_usuario = ?, id_prestamo = ?, tipo_sancion = ?, descripcion = ?, fecha_sancion = ?, estado_sancion = ?, fecha_inicio = ?, fecha_fin = ?, dias_suspension = ? WHERE id_sancion = ?";
        
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, sancion.getRuUsuario());
            
            if (sancion.getIdPrestamo() != null) {
                stmt.setInt(2, sancion.getIdPrestamo());
            } else {
                stmt.setNull(2, java.sql.Types.INTEGER);
            }
            
            stmt.setString(3, sancion.getTipoSancion());
            stmt.setString(4, sancion.getDescripcion());
            stmt.setDate(5, new Date(sancion.getFechaSancion().getTime()));
            stmt.setString(6, sancion.getEstadoSancion());
            stmt.setDate(7, new Date(sancion.getFechaInicio().getTime()));
            
            if (sancion.getFechaFin() != null) {
                stmt.setDate(8, new Date(sancion.getFechaFin().getTime()));
            } else {
                stmt.setNull(8, java.sql.Types.DATE);
            }
            
            stmt.setInt(9, sancion.getDiasSuspension());
            stmt.setInt(10, sancion.getIdSancion());
            stmt.executeUpdate();
        }
    }

    // Eliminar una sanción por ID
    public void eliminar(int idSancion) throws SQLException {
        String sql = "DELETE FROM sancion WHERE id_sancion = ?";
        
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idSancion);
            stmt.executeUpdate();
        }
    }
    
    // Buscar sanción por ID
    public Sancion buscarPorId(int idSancion) throws SQLException {
        String sql = "SELECT * FROM sancion WHERE id_sancion = ?";
        
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idSancion);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Integer idPrestamo = rs.getInt("id_prestamo");
                    if (rs.wasNull()) {
                        idPrestamo = null;
                    }
                    
                    Date fechaFin = rs.getDate("fecha_fin");
                    
                    return new Sancion(
                        rs.getInt("id_sancion"),
                        rs.getInt("ru_usuario"),
                        idPrestamo,
                        rs.getString("tipo_sancion"),
                        rs.getString("descripcion"),
                        rs.getDate("fecha_sancion"),
                        rs.getString("estado_sancion"),
                        rs.getDate("fecha_inicio"),
                        fechaFin,
                        rs.getInt("dias_suspension")
                    );
                }
            }
        }
        return null;
    }
}