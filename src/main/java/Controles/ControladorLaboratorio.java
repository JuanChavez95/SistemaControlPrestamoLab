/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controles;

import Clases.Laboratorio;
import DataBase.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador para operaciones CRUD sobre la tabla laboratorio.
 * Autor: Equipo Soldados Caídos
 */
public class ControladorLaboratorio {

    // Insertar un laboratorio (sin ID)
    public void insertar(Laboratorio lab) throws SQLException {
        String sql = "INSERT INTO laboratorio (ubicacion, descripcion, capacidad) VALUES (?, ?, ?)";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, lab.getUbicacion());
            stmt.setString(2, lab.getDescripcion());
            stmt.setInt(3, lab.getCapacidad());
            stmt.executeUpdate();
        }
    }

    // Listar todos los laboratorios
    public List<Laboratorio> listar() throws SQLException {
        List<Laboratorio> lista = new ArrayList<>();
        String sql = "SELECT * FROM laboratorio";
        try (Connection conn = ConexionBD.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Laboratorio(
                    rs.getInt("id_laboratorio"),
                    rs.getString("ubicacion"),
                    rs.getString("descripcion"),
                    rs.getInt("capacidad")
                ));
            }
        }
        return lista;
    }

    // Actualizar un laboratorio por ID
    public void actualizar(Laboratorio lab) throws SQLException {
        String sql = "UPDATE laboratorio SET ubicacion = ?, descripcion = ?, capacidad = ? WHERE id_laboratorio = ?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, lab.getUbicacion());
            stmt.setString(2, lab.getDescripcion());
            stmt.setInt(3, lab.getCapacidad());
            stmt.setInt(4, lab.getIdLaboratorio());
            stmt.executeUpdate();
        }
    }

    // Eliminar un laboratorio por ID
    public void eliminar(int idLaboratorio) throws SQLException {
        String sql = "DELETE FROM laboratorio WHERE id_laboratorio = ?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idLaboratorio);
            stmt.executeUpdate();
        }
    }
}
