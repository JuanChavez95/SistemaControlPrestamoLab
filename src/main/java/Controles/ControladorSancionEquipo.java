/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controles;

import Clases.SancionEquipo;
import DataBase.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador para operaciones CRUD sobre la tabla sancion_equipo.
 * Autor: Equipo Soldados Caídos
 */
public class ControladorSancionEquipo {

    // Insertar una relación de sanción con equipo
    public void insertar(SancionEquipo sancionEquipo) throws SQLException {
        String sql = "INSERT INTO sancion_equipo (id_sancion, id_equipos) VALUES (?, ?)";
        
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, sancionEquipo.getIdSancion());
            stmt.setString(2, sancionEquipo.getIdEquipos());
            stmt.executeUpdate();
        }
    }

    // Listar todas las sanciones relacionadas con un equipo específico
    public List<SancionEquipo> listarPorEquipo(String idEquipos) throws SQLException {
        List<SancionEquipo> lista = new ArrayList<>();
        String sql = "SELECT * FROM sancion_equipo WHERE id_equipos = ?";
        
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, idEquipos);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(new SancionEquipo(
                        rs.getInt("id_sancion"),
                        rs.getString("id_equipos")
                    ));
                }
            }
        }
        return lista;
    }

    // Listar todas las sanciones relacionadas con equipos
    public List<SancionEquipo> listar() throws SQLException {
        List<SancionEquipo> lista = new ArrayList<>();
        String sql = "SELECT * FROM sancion_equipo";
        
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(new SancionEquipo(
                    rs.getInt("id_sancion"),
                    rs.getString("id_equipos")
                ));
            }
        }
        return lista;
    }

    // Buscar por ID de sanción
    public SancionEquipo buscarPorIdSancion(int idSancion) throws SQLException {
        String sql = "SELECT * FROM sancion_equipo WHERE id_sancion = ?";
        
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idSancion);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new SancionEquipo(
                        rs.getInt("id_sancion"),
                        rs.getString("id_equipos")
                    );
                }
            }
        }
        return null;
    }

    // Actualizar una relación de sanción con equipo
    public void actualizar(SancionEquipo sancionEquipo) throws SQLException {
        String sql = "UPDATE sancion_equipo SET id_equipos = ? WHERE id_sancion = ?";
        
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, sancionEquipo.getIdEquipos());
            stmt.setInt(2, sancionEquipo.getIdSancion());
            stmt.executeUpdate();
        }
    }

    // Eliminar una relación de sanción con equipo por ID de sanción
    public void eliminar(int idSancion) throws SQLException {
        String sql = "DELETE FROM sancion_equipo WHERE id_sancion = ?";
        
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idSancion);
            stmt.executeUpdate();
        }
    }
}