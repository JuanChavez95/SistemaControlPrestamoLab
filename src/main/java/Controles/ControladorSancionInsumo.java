/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controles;

import Clases.SancionInsumo;
import DataBase.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador para operaciones CRUD sobre la tabla sancion_insumo.
 * Autor: Equipo Soldados Caídos
 */
public class ControladorSancionInsumo {

    // Insertar una relación de sanción con insumo
    public void insertar(SancionInsumo sancionInsumo) throws SQLException {
        String sql = "INSERT INTO sancion_insumo (id_sancion, id_insumo, cantidad_afectada) VALUES (?, ?, ?)";
        
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, sancionInsumo.getIdSancion());
            stmt.setInt(2, sancionInsumo.getIdInsumo());
            stmt.setInt(3, sancionInsumo.getCantidadAfectada());
            stmt.executeUpdate();
        }
    }

    // Listar todas las sanciones relacionadas con un insumo específico
    public List<SancionInsumo> listarPorInsumo(int idInsumo) throws SQLException {
        List<SancionInsumo> lista = new ArrayList<>();
        String sql = "SELECT * FROM sancion_insumo WHERE id_insumo = ?";
        
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idInsumo);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(new SancionInsumo(
                        rs.getInt("id_sancion"),
                        rs.getInt("id_insumo"),
                        rs.getInt("cantidad_afectada")
                    ));
                }
            }
        }
        return lista;
    }

    // Listar todas las sanciones relacionadas con insumos
    public List<SancionInsumo> listar() throws SQLException {
        List<SancionInsumo> lista = new ArrayList<>();
        String sql = "SELECT * FROM sancion_insumo";
        
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(new SancionInsumo(
                    rs.getInt("id_sancion"),
                    rs.getInt("id_insumo"),
                    rs.getInt("cantidad_afectada")
                ));
            }
        }
        return lista;
    }

    // Buscar por ID de sanción
    public SancionInsumo buscarPorIdSancion(int idSancion) throws SQLException {
        String sql = "SELECT * FROM sancion_insumo WHERE id_sancion = ?";
        
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idSancion);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new SancionInsumo(
                        rs.getInt("id_sancion"),
                        rs.getInt("id_insumo"),
                        rs.getInt("cantidad_afectada")
                    );
                }
            }
        }
        return null;
    }

    // Actualizar una relación de sanción con insumo
    public void actualizar(SancionInsumo sancionInsumo) throws SQLException {
        String sql = "UPDATE sancion_insumo SET id_insumo = ?, cantidad_afectada = ? WHERE id_sancion = ?";
        
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, sancionInsumo.getIdInsumo());
            stmt.setInt(2, sancionInsumo.getCantidadAfectada());
            stmt.setInt(3, sancionInsumo.getIdSancion());
            stmt.executeUpdate();
        }
    }

    // Eliminar una relación de sanción con insumo por ID de sanción
    public void eliminar(int idSancion) throws SQLException {
        String sql = "DELETE FROM sancion_insumo WHERE id_sancion = ?";
        
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idSancion);
            stmt.executeUpdate();
        }
    }
}
