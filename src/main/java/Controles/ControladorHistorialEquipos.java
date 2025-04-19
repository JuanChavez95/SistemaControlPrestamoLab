/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controles;

import Clases.HistorialEquipos;
import Clases.HistorialGeneral;
import Clases.Equipos;
import DataBase.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

/**
 * Controlador para operaciones CRUD sobre la tabla historial_equipos.
 * Autor: Equipo Soldados Caídos
 */
public class ControladorHistorialEquipos {
    
    public ControladorHistorialGeneral controlHistorialGeneral;
    public ControladorEquipo controlEquipo;
    
    public ControladorHistorialEquipos() {
        controlHistorialGeneral = new ControladorHistorialGeneral();
        controlEquipo = new ControladorEquipo();
    }

    /**
     * Inserta un nuevo registro en ambas tablas: historial_general e historial_equipos
     * @param ruAdministrador RU del administrador que realiza la acción
     * @param fecha Fecha del evento
     * @param categoria Categoría del evento (Mantenimiento, Baja, etc.)
     * @param descripcion Descripción detallada del evento
     * @param idEquipos ID del equipo relacionado
     * @return ID generado para el historial
     * @throws SQLException Si ocurre un error en la base de datos
     */
    public int registrarHistorialEquipo(int ruAdministrador, Date fecha, String categoria, 
                                       String descripcion, String idEquipos) throws SQLException {
        
        // Primero insertamos en historial_general y obtenemos el ID generado
        HistorialGeneral historialGeneral = new HistorialGeneral(ruAdministrador, fecha, categoria, descripcion);
        int idHistorial = controlHistorialGeneral.insertar(historialGeneral);
        
        // Luego insertamos en historial_equipos con el ID obtenido
        String sql = "INSERT INTO historial_equipos (id_historial, id_equipos) VALUES (?, ?)";
        
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idHistorial);
            stmt.setString(2, idEquipos);
            
            stmt.executeUpdate();
        }
        
        return idHistorial;
    }

    /**
     * Lista todos los registros de historial_equipos con información detallada
     * @return Lista de registros combinados entre historial_general e historial_equipos
     * @throws SQLException Si ocurre un error en la base de datos
     */
    public List<Object[]> listarHistorialConDetalle() throws SQLException {
        List<Object[]> listaRegistros = new ArrayList<>();
        
        String sql = "SELECT hg.id_historial, hg.ru_administrador, hg.fecha, hg.categoria, " +
                     "hg.descripcion, he.id_equipos " +
                     "FROM historial_general hg " +
                     "JOIN historial_equipos he ON hg.id_historial = he.id_historial " +
                     "ORDER BY hg.fecha DESC";
        
        try (Connection conn = ConexionBD.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Object[] registro = new Object[6];
                registro[0] = rs.getInt("id_historial");
                registro[1] = rs.getInt("ru_administrador");
                registro[2] = rs.getDate("fecha");
                registro[3] = rs.getString("categoria");
                registro[4] = rs.getString("descripcion");
                registro[5] = rs.getString("id_equipos");
                
                listaRegistros.add(registro);
            }
        }
        
        return listaRegistros;
    }

    /**
     * Busca historial por ID de equipo
     * @param idEquipos ID del equipo a buscar
     * @return Lista de registros combinados entre historial_general e historial_equipos
     * @throws SQLException Si ocurre un error en la base de datos
     */
    public List<Object[]> buscarHistorialPorEquipo(String idEquipos) throws SQLException {
        List<Object[]> listaRegistros = new ArrayList<>();
        
        String sql = "SELECT hg.id_historial, hg.ru_administrador, hg.fecha, hg.categoria, " +
                     "hg.descripcion, he.id_equipos " +
                     "FROM historial_general hg " +
                     "JOIN historial_equipos he ON hg.id_historial = he.id_historial " +
                     "WHERE he.id_equipos = ? " +
                     "ORDER BY hg.fecha DESC";
        
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, idEquipos);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Object[] registro = new Object[6];
                    registro[0] = rs.getInt("id_historial");
                    registro[1] = rs.getInt("ru_administrador");
                    registro[2] = rs.getDate("fecha");
                    registro[3] = rs.getString("categoria");
                    registro[4] = rs.getString("descripcion");
                    registro[5] = rs.getString("id_equipos");
                    
                    listaRegistros.add(registro);
                }
            }
        }
        
        return listaRegistros;
    }

    /**
     * Obtiene el registro de historial_equipos por ID de historial
     * @param idHistorial ID del historial a buscar
     * @return Objeto HistorialEquipos si existe, null en caso contrario
     * @throws SQLException Si ocurre un error en la base de datos
     */
    public HistorialEquipos buscarPorIdHistorial(int idHistorial) throws SQLException {
        String sql = "SELECT * FROM historial_equipos WHERE id_historial = ?";
        
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idHistorial);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new HistorialEquipos(
                        rs.getInt("id_historial"),
                        rs.getString("id_equipos")
                    );
                }
            }
        }
        
        return null;
    }

    /**
     * Actualiza el registro de historial_equipos por ID de historial
     * @param historialEquipos Objeto HistorialEquipos con los datos actualizados
     * @throws SQLException Si ocurre un error en la base de datos
     */
    public void actualizar(HistorialEquipos historialEquipos) throws SQLException {
        String sql = "UPDATE historial_equipos SET id_equipos = ? WHERE id_historial = ?";
        
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, historialEquipos.getIdEquipos());
            stmt.setInt(2, historialEquipos.getIdHistorial());
            
            stmt.executeUpdate();
        }
    }

    /**
     * Busca historial por categoría y equipo
     * @param categoria Categoría del evento
     * @param idEquipos ID del equipo
     * @return Lista de registros combinados entre historial_general e historial_equipos
     * @throws SQLException Si ocurre un error en la base de datos
     */
    public List<Object[]> buscarPorCategoriaYEquipo(String categoria, String idEquipos) throws SQLException {
        List<Object[]> listaRegistros = new ArrayList<>();
        
        String sql = "SELECT hg.id_historial, hg.ru_administrador, hg.fecha, hg.categoria, " +
                     "hg.descripcion, he.id_equipos " +
                     "FROM historial_general hg " +
                     "JOIN historial_equipos he ON hg.id_historial = he.id_historial " +
                     "WHERE hg.categoria = ? AND he.id_equipos = ? " +
                     "ORDER BY hg.fecha DESC";
        
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, categoria);
            stmt.setString(2, idEquipos);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Object[] registro = new Object[6];
                    registro[0] = rs.getInt("id_historial");
                    registro[1] = rs.getInt("ru_administrador");
                    registro[2] = rs.getDate("fecha");
                    registro[3] = rs.getString("categoria");
                    registro[4] = rs.getString("descripcion");
                    registro[5] = rs.getString("id_equipos");
                    
                    listaRegistros.add(registro);
                }
            }
        }
        
        return listaRegistros;
    }

    /**
     * Elimina un registro de historial_equipos por ID de historial
     * (La eliminación en cascada maneja también la eliminación en historial_general)
     * @param idHistorial ID del historial a eliminar
     * @throws SQLException Si ocurre un error en la base de datos
     */
    public void eliminar(int idHistorial) throws SQLException {
        // Al tener configurado ON DELETE CASCADE en la clave foránea,
        // solo necesitamos eliminar de la tabla principal (historial_general)
        controlHistorialGeneral.eliminar(idHistorial);
    }
}