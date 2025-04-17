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
        String sql = "INSERT INTO equipamiento (nombre_equipamiento, marca, modelo, numero_serie, estado, id_laboratorio) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, equipo.getNombreEquipamiento());
            stmt.setString(2, equipo.getMarca());
            stmt.setString(3, equipo.getModelo());
            stmt.setString(4, equipo.getNumeroSerie());
            stmt.setString(5, equipo.getEstado());
            
            if (equipo.getIdLaboratorio() == 0) {
                stmt.setNull(6, java.sql.Types.INTEGER); // Usar NULL para el id_laboratorio
            } else {
                stmt.setInt(6, equipo.getIdLaboratorio());
            }
            //stmt.setInt(6, equipo.getIdLaboratorio());
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
                lista.add(new Equipamiento(
                    rs.getInt("id_equipamiento"),
                    rs.getString("nombre_equipamiento"),
                    rs.getString("marca"),
                    rs.getString("modelo"),
                    rs.getString("numero_serie"),
                    rs.getString("estado"),
                    rs.getInt("id_laboratorio")
                ));
            }
        }
        return lista;
    }

    // Actualizar un equipamiento por ID
    public void actualizar(Equipamiento equipo) throws SQLException {
        String sql = "UPDATE equipamiento SET nombre_equipamiento = ?, marca = ?, modelo = ?, numero_serie = ?, estado = ?, id_laboratorio = ? WHERE id_equipamiento = ?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, equipo.getNombreEquipamiento());
            stmt.setString(2, equipo.getMarca());
            stmt.setString(3, equipo.getModelo());
            stmt.setString(4, equipo.getNumeroSerie());
            stmt.setString(5, equipo.getEstado());
            stmt.setInt(6, equipo.getIdLaboratorio());
            stmt.setInt(7, equipo.getIdEquipamiento());
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
    
    // Listar equipamientos por laboratorio
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
                        rs.getInt("id_laboratorio")
                    ));
                }
            }
        }
        return lista;
    }
    
    // Buscar equipamiento por nombre
    public List<Equipamiento> buscarPorNombre(String nombre) throws SQLException {
        List<Equipamiento> lista = new ArrayList<>();
        String sql = "SELECT * FROM equipamiento WHERE nombre_equipamiento LIKE ?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + nombre + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Equipamiento(
                        rs.getInt("id_equipamiento"),
                        rs.getString("nombre_equipamiento"),
                        rs.getString("marca"),
                        rs.getString("modelo"),
                        rs.getString("numero_serie"),
                        rs.getString("estado"),
                        rs.getInt("id_laboratorio")
                    ));
                }
            }
        }
        return lista;
    }
}