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

    // Insertar un nuevo horario a la base de datos
    public void insertar(Horario horario) throws SQLException {
        String sql = "INSERT INTO horario (materia, paralelo, semestre, carrera, hora, dia, id_laboratorio) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, horario.getMateria());
            stmt.setInt(2, horario.getParalelo());
            stmt.setString(3, horario.getSemestre());
            stmt.setString(4, horario.getCarrera());
            stmt.setString(5, horario.getHora());
            stmt.setString(6, horario.getDia());
            stmt.setInt(7, horario.getIdLaboratorio());
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
                    rs.getInt("id_laboratorio")
                ));
            }
        }
        return lista;
    }

    // Actualizar un horario por ID
    public void actualizar(Horario horario) throws SQLException {
        String sql = "UPDATE horario SET materia = ?, paralelo = ?, semestre = ?, carrera = ?, hora = ?, dia = ?, id_laboratorio = ? WHERE id_horario = ?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, horario.getMateria());
            stmt.setInt(2, horario.getParalelo());
            stmt.setString(3, horario.getSemestre());
            stmt.setString(4, horario.getCarrera());
            stmt.setString(5, horario.getHora());
            stmt.setString(6, horario.getDia());
            stmt.setInt(7, horario.getIdLaboratorio());
            stmt.setInt(8, horario.getIdHorario());
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
}

