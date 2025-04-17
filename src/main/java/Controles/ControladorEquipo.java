/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controles;

import Clases.Equipos;
import DataBase.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador para operaciones CRUD sobre la tabla equipos.
 * Autor: Equipo Soldados Caídos
 */
public class ControladorEquipo {

    // Insertar nuevo equipo a la tabla
    public void insertar(Equipos equipo) throws SQLException {
        String sql = "INSERT INTO equipos (id_equipos, procesador, ram, dispositivo, monitor, teclado, mouse, estado, id_laboratorio) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, equipo.getIdEquipos());
            stmt.setString(2, equipo.getProcesador());
            stmt.setString(3, equipo.getRam());
            stmt.setString(4, equipo.getDispositivo());
            stmt.setString(5, equipo.getMonitor());
            stmt.setString(6, equipo.getTeclado());
            stmt.setString(7, equipo.getMouse());
            stmt.setString(8, equipo.getEstado());
            stmt.setInt(9, equipo.getIdLaboratorio());
            stmt.executeUpdate();
        }
    }

    // Listar todos los equipos disponibles
    public List<Equipos> listar() throws SQLException {
        List<Equipos> lista = new ArrayList<>();
        String sql = "SELECT * FROM equipos";
        try (Connection conn = ConexionBD.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Equipos(
                    rs.getString("id_equipos"),
                    rs.getString("procesador"),
                    rs.getString("ram"),
                    rs.getString("dispositivo"),
                    rs.getString("monitor"),
                    rs.getString("teclado"),
                    rs.getString("mouse"),
                    rs.getString("estado"),
                    rs.getInt("id_laboratorio")
                ));
            }
        }
        return lista;
    }

    // Actualizar un equipo por su ID
    public void actualizar(Equipos equipo) throws SQLException {
        String sql = "UPDATE equipos SET procesador = ?, ram = ?, dispositivo = ?, monitor = ?, teclado = ?, mouse = ?, estado = ?, id_laboratorio = ? WHERE id_equipos = ?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, equipo.getProcesador());
            stmt.setString(2, equipo.getRam());
            stmt.setString(3, equipo.getDispositivo());
            stmt.setString(4, equipo.getMonitor());
            stmt.setString(5, equipo.getTeclado());
            stmt.setString(6, equipo.getMouse());
            stmt.setString(7, equipo.getEstado());
            stmt.setInt(8, equipo.getIdLaboratorio());
            stmt.setString(9, equipo.getIdEquipos());
            stmt.executeUpdate();
        }
    }

    // Eliminar un equipo por ID
    public void eliminar(String idEquipos) throws SQLException {
        String sql = "DELETE FROM equipos WHERE id_equipos = ?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, idEquipos);
            stmt.executeUpdate();
        }
    }
}