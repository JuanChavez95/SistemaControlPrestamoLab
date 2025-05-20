/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controles;

import Clases.Equipamiento;
import DataBase.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador para operaciones CRUD sobre la tabla equipamiento.
 * Autor: Equipo Soldados Caídos
 */
public class ControladorEquipamiento {

    // Insertar un equipamiento (sin ID)
    public void insertar(Equipamiento equipo) throws SQLException {
        String sql = "INSERT INTO equipamiento (nombre_equipamiento, marca, modelo, numero_serie, estado, id_laboratorio, disponibilidad) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, equipo.getNombreEquipamiento());
            stmt.setString(2, equipo.getMarca());
            stmt.setString(3, equipo.getModelo());
            stmt.setString(4, equipo.getNumeroSerie());
            stmt.setString(5, equipo.getEstado());
            
            // Manejar id_laboratorio que puede ser null
            if (equipo.getIdLaboratorio() != null) {
                stmt.setInt(6, equipo.getIdLaboratorio());
            } else {
                stmt.setNull(6, java.sql.Types.INTEGER);
            }
            
            stmt.setString(7, equipo.getDisponibilidad());
            stmt.executeUpdate();
        }
    }

    // Listar todos los equipamientos
    public List<Equipamiento> listar() throws SQLException {
        List<Equipamiento> lista = new ArrayList<>();
        String sql = "SELECT * FROM equipamiento";
        try (Connection conn = ConexionBD.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Integer idLab = rs.getObject("id_laboratorio", Integer.class); // Maneja null
                
                lista.add(new Equipamiento(
                    rs.getInt("id_equipamiento"),
                    rs.getString("nombre_equipamiento"),
                    rs.getString("marca"),
                    rs.getString("modelo"),
                    rs.getString("numero_serie"),
                    rs.getString("estado"),
                    idLab,
                    rs.getString("disponibilidad")
                ));
            }
        }
        return lista;
    }
    
    // Buscar un equipamiento por ID
    public Equipamiento buscarPorId(int idEquipamiento) throws SQLException {
        String sql = "SELECT * FROM equipamiento WHERE id_equipamiento = ?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idEquipamiento);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Integer idLab = rs.getObject("id_laboratorio", Integer.class); // Maneja null
                    
                    return new Equipamiento(
                        rs.getInt("id_equipamiento"),
                        rs.getString("nombre_equipamiento"),
                        rs.getString("marca"),
                        rs.getString("modelo"),
                        rs.getString("numero_serie"),
                        rs.getString("estado"),
                        idLab,
                        rs.getString("disponibilidad")
                    );
                }
            }
        }
        return null;
    }
    
    // Listar equipamiento por laboratorio
    public List<Equipamiento> listarPorLaboratorio(int idLaboratorio) throws SQLException {
        List<Equipamiento> lista = new ArrayList<>();
        String sql = "SELECT * FROM equipamiento WHERE id_laboratorio = ?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idLaboratorio);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Equipamiento(
                        rs.getInt("id_equipamiento"),
                        rs.getString("nombre_equipamiento"),
                        rs.getString("marca"),
                        rs.getString("modelo"),
                        rs.getString("numero_serie"),
                        rs.getString("estado"),
                        rs.getInt("id_laboratorio"),
                        rs.getString("disponibilidad")
                    ));
                }
            }
        }
        return lista;
    }

    // Actualizar un equipamiento por ID
    public void actualizar(Equipamiento equipo) throws SQLException {
        String sql = "UPDATE equipamiento SET nombre_equipamiento = ?, marca = ?, modelo = ?, numero_serie = ?, estado = ?, id_laboratorio = ?, disponibilidad = ? WHERE id_equipamiento = ?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, equipo.getNombreEquipamiento());
            stmt.setString(2, equipo.getMarca());
            stmt.setString(3, equipo.getModelo());
            stmt.setString(4, equipo.getNumeroSerie());
            stmt.setString(5, equipo.getEstado());
            
            // Manejar id_laboratorio que puede ser null
            if (equipo.getIdLaboratorio() != null) {
                stmt.setInt(6, equipo.getIdLaboratorio());
            } else {
                stmt.setNull(6, java.sql.Types.INTEGER);
            }
            
            stmt.setString(7, equipo.getDisponibilidad());
            stmt.setInt(8, equipo.getIdEquipamiento());
            stmt.executeUpdate();
        }
    }

    // Eliminar un equipamiento por ID
    public void eliminar(int idEquipamiento) throws SQLException {
        String sql = "DELETE FROM equipamiento WHERE id_equipamiento = ?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idEquipamiento);
            stmt.executeUpdate();
        }
    }
    
    // Actualizar disponibilidad
    public void actualizarDisponibilidad(int idEquipamiento, String disponibilidad) throws SQLException {
        String sql = "UPDATE equipamiento SET disponibilidad = ? WHERE id_equipamiento = ?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, disponibilidad);
            stmt.setInt(2, idEquipamiento);
            stmt.executeUpdate();
        }
    }
    
    /**
    * Cuenta el número de equipamientos con un determinado estado.
    * 
    * @param estado Estado de los equipamientos a contar
    * @return Número de equipamientos con el estado especificado
    * @throws SQLException Si ocurre un error al ejecutar la consulta
    */
    public int contarEquipamientosPorDisponibilidad(String disponibilidad) throws SQLException {
    String sql = "SELECT COUNT(*) FROM equipamiento WHERE disponibilidad = ?";
    try (Connection conn = ConexionBD.conectar();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, disponibilidad);
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
    }
    return 0;
    }


   /**
    * Cuenta el número de equipamientos asignados a un laboratorio específico.
    * 
    * @param idLaboratorio ID del laboratorio
    * @return Número de equipamientos en el laboratorio especificado
    * @throws SQLException Si ocurre un error al ejecutar la consulta
    */
    public int contarEquipamientosPorLaboratorio(int idLaboratorio) throws SQLException {
        String sql = "SELECT COUNT(*) FROM equipamiento WHERE id_laboratorio = ?";
        try (Connection conn = ConexionBD.conectar();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idLaboratorio);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                   return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    
}