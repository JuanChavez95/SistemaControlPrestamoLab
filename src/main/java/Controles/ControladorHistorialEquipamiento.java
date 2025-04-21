/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controles;

import Clases.HistorialEquipamiento;
import DataBase.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador para operaciones CRUD sobre la tabla historial_equipamiento.
 * Autor: Equipo Soldados Caídos
 */
public class ControladorHistorialEquipamiento {

    /**
     * Inserta un nuevo registro en la tabla historial_equipamiento
     * @param historialEquipamiento Objeto HistorialEquipamiento a insertar
     * @return true si la inserción fue exitosa, false en caso contrario
     * @throws SQLException Si ocurre un error en la base de datos
     */
    public boolean insertar(HistorialEquipamiento historialEquipamiento) throws SQLException {
        String sql = "INSERT INTO historial_equipamiento (id_historial, id_equipamiento) VALUES (?, ?)";
        
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, historialEquipamiento.getIdHistorial());
            stmt.setInt(2, historialEquipamiento.getIdEquipamiento());
            
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
        }
    }

    /**
     * Lista todos los registros de historial_equipamiento
     * @return Lista de objetos HistorialEquipamiento
     * @throws SQLException Si ocurre un error en la base de datos
     */
    public List<HistorialEquipamiento> listar() throws SQLException {
        List<HistorialEquipamiento> lista = new ArrayList<>();
        String sql = "SELECT * FROM historial_equipamiento";
        
        try (Connection conn = ConexionBD.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                lista.add(new HistorialEquipamiento(
                    rs.getInt("id_historial"),
                    rs.getInt("id_equipamiento")
                ));
            }
        }
        return lista;
    }

    /**
     * Busca un registro de historial_equipamiento por su ID de historial
     * @param idHistorial ID del historial a buscar
     * @return Objeto HistorialEquipamiento si existe, null en caso contrario
     * @throws SQLException Si ocurre un error en la base de datos
     */
    public HistorialEquipamiento buscarPorIdHistorial(int idHistorial) throws SQLException {
        String sql = "SELECT * FROM historial_equipamiento WHERE id_historial = ?";
        
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idHistorial);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new HistorialEquipamiento(
                        rs.getInt("id_historial"),
                        rs.getInt("id_equipamiento")
                    );
                }
            }
        }
        return null;
    }

    /**
     * Busca registros de historial_equipamiento por ID de equipamiento
     * @param idEquipamiento ID del equipamiento a buscar
     * @return Lista de objetos HistorialEquipamiento
     * @throws SQLException Si ocurre un error en la base de datos
     */
    public List<HistorialEquipamiento> buscarPorIdEquipamiento(int idEquipamiento) throws SQLException {
        List<HistorialEquipamiento> lista = new ArrayList<>();
        String sql = "SELECT * FROM historial_equipamiento WHERE id_equipamiento = ?";
        
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idEquipamiento);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(new HistorialEquipamiento(
                        rs.getInt("id_historial"),
                        rs.getInt("id_equipamiento")
                    ));
                }
            }
        }
        return lista;
    }

    /**
     * Busca el historial completo de un equipamiento específico, incluyendo los detalles del historial general
     * @param idEquipamiento ID del equipamiento a buscar
     * @return Lista de arrays de objetos con datos del historial general y el equipamiento
     * @throws SQLException Si ocurre un error en la base de datos
     */
    public List<Object[]> buscarHistorialCompletoEquipamiento(int idEquipamiento) throws SQLException {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT hg.id_historial, hg.ru_administrador, hg.fecha, hg.categoria, hg.descripcion, " +
                     "e.id_equipamiento, e.nombre_equipamiento, e.marca, e.modelo " +
                     "FROM historial_general hg " +
                     "JOIN historial_equipamiento he ON hg.id_historial = he.id_historial " +
                     "JOIN equipamiento e ON he.id_equipamiento = e.id_equipamiento " +
                     "WHERE e.id_equipamiento = ? " +
                     "ORDER BY hg.fecha DESC";
        
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idEquipamiento);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Object[] fila = new Object[9];
                    fila[0] = rs.getInt("id_historial");
                    fila[1] = rs.getInt("ru_administrador");
                    fila[2] = rs.getDate("fecha");
                    fila[3] = rs.getString("categoria");
                    fila[4] = rs.getString("descripcion");
                    fila[5] = rs.getInt("id_equipamiento");
                    fila[6] = rs.getString("nombre_equipamiento");
                    fila[7] = rs.getString("marca");
                    fila[8] = rs.getString("modelo");
                    lista.add(fila);
                }
            }
        }
        return lista;
    }

    /**
     * Elimina un registro de historial_equipamiento por su ID de historial
     * @param idHistorial ID del historial a eliminar
     * @return true si la eliminación fue exitosa, false en caso contrario
     * @throws SQLException Si ocurre un error en la base de datos
     */
    public boolean eliminar(int idHistorial) throws SQLException {
        String sql = "DELETE FROM historial_equipamiento WHERE id_historial = ?";
        
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idHistorial);
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
        }
    }

    /**
     * Actualiza un registro de historial_equipamiento
     * @param historialEquipamiento Objeto HistorialEquipamiento con los datos actualizados
     * @return true si la actualización fue exitosa, false en caso contrario
     * @throws SQLException Si ocurre un error en la base de datos
     */
    public boolean actualizar(HistorialEquipamiento historialEquipamiento) throws SQLException {
        String sql = "UPDATE historial_equipamiento SET id_equipamiento = ? WHERE id_historial = ?";
        
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, historialEquipamiento.getIdEquipamiento());
            stmt.setInt(2, historialEquipamiento.getIdHistorial());
            
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
        }
    }

    /**
     * Método para crear un nuevo registro completo de historial de equipamiento
     * (Inserta en historial_general y luego en historial_equipamiento)
     * @param ruAdministrador RU del administrador
     * @param fecha Fecha del historial
     * @param categoria Categoría del historial
     * @param descripcion Descripción del historial
     * @param idEquipamiento ID del equipamiento asociado
     * @return ID del historial creado si es exitoso, -1 en caso contrario
     * @throws SQLException Si ocurre un error en la base de datos
     */
    public int crearHistorialEquipamiento(int ruAdministrador, Date fecha, String categoria, 
                                         String descripcion, int idEquipamiento) throws SQLException {
        Connection conn = null;
        try {
            conn = ConexionBD.conectar();
            conn.setAutoCommit(false);
            
            // Crear historial general
            String sqlHistorial = "INSERT INTO historial_general (ru_administrador, fecha, categoria, descripcion) VALUES (?, ?, ?, ?)";
            int idHistorial;
            
            try (PreparedStatement stmt = conn.prepareStatement(sqlHistorial, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, ruAdministrador);
                stmt.setDate(2, new java.sql.Date(fecha.getTime()));
                stmt.setString(3, categoria);
                stmt.setString(4, descripcion);
                
                stmt.executeUpdate();
                
                // Obtener el ID generado
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        idHistorial = generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("No se pudo obtener el ID generado para el historial");
                    }
                }
            }
            
            // Crear historial equipamiento
            String sqlHistorialEquipamiento = "INSERT INTO historial_equipamiento (id_historial, id_equipamiento) VALUES (?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sqlHistorialEquipamiento)) {
                stmt.setInt(1, idHistorial);
                stmt.setInt(2, idEquipamiento);
                stmt.executeUpdate();
            }
            
            conn.commit();
            return idHistorial;
            
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    throw new SQLException("Error al hacer rollback de la transacción", ex);
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    // Ignorar
                }
            }
        }
    }
}