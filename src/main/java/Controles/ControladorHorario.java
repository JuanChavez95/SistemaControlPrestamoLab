/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controles;

import Clases.Horario;
import DataBase.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ControladorHorario {

    // Insertar un horario
    public void insertar(Horario h) throws SQLException {
        String sql = "INSERT INTO horario (materia, paralelo, semestre, carrera, hora, dia, id_laboratorio, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, h.getMateria());
            stmt.setInt(2, h.getParalelo());
            stmt.setString(3, h.getSemestre());
            stmt.setString(4, h.getCarrera());
            stmt.setString(5, h.getHora());
            stmt.setString(6, h.getDia());
            stmt.setInt(7, h.getIdLaboratorio());
            stmt.setString(8, h.getEstado());
            stmt.executeUpdate();
        }
    }

    // Listar todos los horarios
    public List<Horario> listar() throws SQLException {
        List<Horario> lista = new ArrayList<>();
        String sql = "SELECT * FROM horario";
        try (Connection conn = ConexionBD.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Horario(
                    rs.getInt("id_horario"),
                    rs.getString("materia"),
                    rs.getInt("paralelo"),
                    rs.getString("semestre"),
                    rs.getString("carrera"),
                    rs.getString("hora"),
                    rs.getString("dia"),
                    rs.getInt("id_laboratorio"),
                    rs.getString("estado")
                ));
            }
        }
        return lista;
    }

    // Listar horarios por laboratorio
    public List<Horario> listarPorLaboratorio(int idLaboratorio) throws SQLException {
        List<Horario> lista = new ArrayList<>();
        String sql = "SELECT * FROM horario WHERE id_laboratorio = ?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idLaboratorio);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Horario(
                        rs.getInt("id_horario"),
                        rs.getString("materia"),
                        rs.getInt("paralelo"),
                        rs.getString("semestre"),
                        rs.getString("carrera"),
                        rs.getString("hora"),
                        rs.getString("dia"),
                        rs.getInt("id_laboratorio"),
                        rs.getString("estado")
                    ));
                }
            }
        }
        return lista;
    }

    // Actualizar un horario
    public void actualizar(Horario h) throws SQLException {
        String sql = "UPDATE horario SET materia = ?, paralelo = ?, semestre = ?, carrera = ?, hora = ?, dia = ?, id_laboratorio = ?, estado = ? WHERE id_horario = ?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, h.getMateria());
            stmt.setInt(2, h.getParalelo());
            stmt.setString(3, h.getSemestre());
            stmt.setString(4, h.getCarrera());
            stmt.setString(5, h.getHora());
            stmt.setString(6, h.getDia());
            stmt.setInt(7, h.getIdLaboratorio());
            stmt.setString(8, h.getEstado());
            stmt.setInt(9, h.getIdHorario());
            stmt.executeUpdate();
        }
    }

    // Eliminar un horario por ID
    public void eliminar(int idHorario) throws SQLException {
        String sql = "DELETE FROM horario WHERE id_horario = ?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idHorario);
            stmt.executeUpdate();
        }
    }
    
    public Horario buscarPorId(int idHorario) throws SQLException {
    String sql = "SELECT * FROM horario WHERE id_horario = ?";
    try (Connection conn = ConexionBD.conectar();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, idHorario);
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return new Horario(
                    rs.getInt("id_horario"),
                    rs.getString("materia"),
                    rs.getInt("paralelo"),
                    rs.getString("semestre"),
                    rs.getString("carrera"),
                    rs.getString("hora"),
                    rs.getString("dia"),
                    rs.getInt("id_laboratorio"),
                    rs.getString("estado")
                );
            }
        }
    }
    return null;
    }
}