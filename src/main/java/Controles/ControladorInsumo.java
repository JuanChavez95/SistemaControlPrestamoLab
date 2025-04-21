/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controles;

import Clases.Insumo;
import DataBase.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador para operaciones CRUD sobre la tabla insumos.
 * Autor: Equipo Soldados Caídos
 */
public class ControladorInsumo {

    // Insertar un insumo (sin ID)
    public void insertar(Insumo insumo) throws SQLException {
        String sql = "INSERT INTO insumos (nombre_insumo, cantidad, categoria, id_laboratorio, disponibilidad) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, insumo.getNombreInsumo());
            stmt.setInt(2, insumo.getCantidad());
            stmt.setString(3, insumo.getCategoria());
            
            // Manejar id_laboratorio que puede ser null
            if (insumo.getIdLaboratorio() != null) {
                stmt.setInt(4, insumo.getIdLaboratorio());
            } else {
                stmt.setNull(4, java.sql.Types.INTEGER);
            }
            
            stmt.setString(5, insumo.getDisponibilidad());
            stmt.executeUpdate();
        }
    }

    // Listar todos los insumos
    public List<Insumo> listar() throws SQLException {
        List<Insumo> lista = new ArrayList<>();
        String sql = "SELECT * FROM insumos";
        try (Connection conn = ConexionBD.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Integer idLab = rs.getObject("id_laboratorio", Integer.class); // Maneja null
                
                lista.add(new Insumo(
                    rs.getInt("id_insumo"),
                    rs.getString("nombre_insumo"),
                    rs.getInt("cantidad"),
                    rs.getString("categoria"),
                    idLab,
                    rs.getString("disponibilidad")
                ));
            }
        }
        return lista;
    }
    
    // Buscar un insumo por ID
    public Insumo buscarPorId(int idInsumo) throws SQLException {
        String sql = "SELECT * FROM insumos WHERE id_insumo = ?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idInsumo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Integer idLab = rs.getObject("id_laboratorio", Integer.class); // Maneja null
                    
                    return new Insumo(
                        rs.getInt("id_insumo"),
                        rs.getString("nombre_insumo"),
                        rs.getInt("cantidad"),
                        rs.getString("categoria"),
                        idLab,
                        rs.getString("disponibilidad")
                    );
                }
            }
        }
        return null;
    }
    
    // Listar insumos por laboratorio
    public List<Insumo> listarPorLaboratorio(int idLaboratorio) throws SQLException {
        List<Insumo> lista = new ArrayList<>();
        String sql = "SELECT * FROM insumos WHERE id_laboratorio = ?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idLaboratorio);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Insumo(
                        rs.getInt("id_insumo"),
                        rs.getString("nombre_insumo"),
                        rs.getInt("cantidad"),
                        rs.getString("categoria"),
                        rs.getInt("id_laboratorio"),
                        rs.getString("disponibilidad")
                    ));
                }
            }
        }
        return lista;
    }

    // Actualizar un insumo por ID
    public void actualizar(Insumo insumo) throws SQLException {
        String sql = "UPDATE insumos SET nombre_insumo = ?, cantidad = ?, categoria = ?, id_laboratorio = ?, disponibilidad = ? WHERE id_insumo = ?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, insumo.getNombreInsumo());
            stmt.setInt(2, insumo.getCantidad());
            stmt.setString(3, insumo.getCategoria());
            
            // Manejar id_laboratorio que puede ser null
            if (insumo.getIdLaboratorio() != null) {
                stmt.setInt(4, insumo.getIdLaboratorio());
            } else {
                stmt.setNull(4, java.sql.Types.INTEGER);
            }
            
            stmt.setString(5, insumo.getDisponibilidad());
            stmt.setInt(6, insumo.getIdInsumo());
            stmt.executeUpdate();
        }
    }

    // Eliminar un insumo por ID
    public void eliminar(int idInsumo) throws SQLException {
        String sql = "DELETE FROM insumos WHERE id_insumo = ?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idInsumo);
            stmt.executeUpdate();
        }
    }
    
    // Actualizar cantidad
    public void actualizarCantidad(int idInsumo, int nuevaCantidad) throws SQLException {
        String sql = "UPDATE insumos SET cantidad = ? WHERE id_insumo = ?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, nuevaCantidad);
            stmt.setInt(2, idInsumo);
            stmt.executeUpdate();
        }
    }
    
    // Actualizar disponibilidad
    public void actualizarDisponibilidad(int idInsumo, String disponibilidad) throws SQLException {
        String sql = "UPDATE insumos SET disponibilidad = ? WHERE id_insumo = ?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, disponibilidad);
            stmt.setInt(2, idInsumo);
            stmt.executeUpdate();
        }
    }
}