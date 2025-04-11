/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controles;

import Clases.HistorialEquipos;
import DataBase.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ControladorHistorialEquipos {

    // Insertar nuevo historial
    public void insertar(HistorialEquipos historial) throws SQLException {
        String sql = "INSERT INTO historial_equipos (id_equipos, fecha, categoria, descripcion) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, historial.getIdEquipos());
            stmt.setDate(2, historial.getFecha());
            stmt.setString(3, historial.getCategoria());
            stmt.setString(4, historial.getDescripcion());
            stmt.executeUpdate();
        }
    }

    // Listar historial completo
    public List<HistorialEquipos> listar() throws SQLException {
        List<HistorialEquipos> lista = new ArrayList<>();
        String sql = "SELECT * FROM historial_equipos";
        try (Connection conn = ConexionBD.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new HistorialEquipos(
                    rs.getInt("id_historial_equipos"),
                    rs.getString("id_equipos"),
                    rs.getDate("fecha"),
                    rs.getString("categoria"),
                    rs.getString("descripcion")
                ));
            }
        }
        return lista;
    }

    // Actualizar un registro del historial
    public void actualizar(HistorialEquipos historial) throws SQLException {
        String sql = "UPDATE historial_equipos SET id_equipos = ?, fecha = ?, categoria = ?, descripcion = ? WHERE id_historial_equipos = ?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, historial.getIdEquipos());
            stmt.setDate(2, historial.getFecha());
            stmt.setString(3, historial.getCategoria());
            stmt.setString(4, historial.getDescripcion());
            stmt.setInt(5, historial.getIdHistorialEquipos());
            stmt.executeUpdate();
        }
    }

    // Eliminar un registro por ID
    public void eliminar(int idHistorial) throws SQLException {
        String sql = "DELETE FROM historial_equipos WHERE id_historial_equipos = ?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idHistorial);
            stmt.executeUpdate();
        }
    }
}
