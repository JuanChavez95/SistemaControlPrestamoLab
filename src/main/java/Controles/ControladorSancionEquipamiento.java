/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controles;

import Clases.SancionEquipamiento;
import DataBase.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador para operaciones CRUD sobre la tabla sancion_equipamiento.
 * Autor: Equipo Soldados Caídos
 */
public class ControladorSancionEquipamiento {

    // Insertar una relación de sanción con equipamiento
    public void insertar(SancionEquipamiento sancionEquipamiento) throws SQLException {
        String sql = "INSERT INTO sancion_equipamiento (id_sancion, id_equipamiento) VALUES (?, ?)";
        
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, sancionEquipamiento.getIdSancion());
            stmt.setInt(2, sancionEquipamiento.getIdEquipamiento());
            stmt.executeUpdate();
        }
    }

    // Listar todas las sanciones relacionadas con un equipamiento específico
    public List<SancionEquipamiento> listarPorEquipamiento(int idEquipamiento) throws SQLException {
        List<SancionEquipamiento> lista = new ArrayList<>();
        String sql = "SELECT * FROM sancion_equipamiento WHERE id_equipamiento = ?";
        
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idEquipamiento);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(new SancionEquipamiento(
                        rs.getInt("id_sancion"),
                        rs.getInt("id_equipamiento")
                    ));
                }
            }
        }
        return lista;
    }

    // Listar todas las sanciones relacionadas con equipamientos
    public List<SancionEquipamiento> listar() throws SQLException {
        List<SancionEquipamiento> lista = new ArrayList<>();
        String sql = "SELECT * FROM sancion_equipamiento";
        
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(new SancionEquipamiento(
                    rs.getInt("id_sancion"),
                    rs.getInt("id_equipamiento")
                ));
            }
        }
        return lista;
    }

    // Buscar por ID de sanción
    public SancionEquipamiento buscarPorIdSancion(int idSancion) throws SQLException {
        String sql = "SELECT * FROM sancion_equipamiento WHERE id_sancion = ?";
        
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idSancion);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new SancionEquipamiento(
                        rs.getInt("id_sancion"),
                        rs.getInt("id_equipamiento")
                    );
                }
            }
        }
        return null;
    }

    // Actualizar una relación de sanción con equipamiento
    public void actualizar(SancionEquipamiento sancionEquipamiento) throws SQLException {
        String sql = "UPDATE sancion_equipamiento SET id_equipamiento = ? WHERE id_sancion = ?";
        
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, sancionEquipamiento.getIdEquipamiento());
            stmt.setInt(2, sancionEquipamiento.getIdSancion());
            stmt.executeUpdate();
        }
    }

    // Eliminar una relación de sanción con equipamiento por ID de sanción
    public void eliminar(int idSancion) throws SQLException {
        String sql = "DELETE FROM sancion_equipamiento WHERE id_sancion = ?";
        
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idSancion);
            stmt.executeUpdate();
        }
    }
}
