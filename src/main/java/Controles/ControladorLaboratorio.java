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
 * Encargado de interactuar con la base de datos.
 * Autor:Soldados Caídos
 */
public class ControladorLaboratorio {

    /**
     * Inserta un nuevo laboratorio en la base de datos.
     * No se especifica el ID porque es autoincremental en la base de datos.
     * 
     * @param lab Objeto Laboratorio con los datos a guardar.
     * @throws SQLException Si ocurre un error al ejecutar la consulta.
     */
    public void insertar(Laboratorio lab) throws SQLException {
        String sql = "INSERT INTO laboratorio (ubicacion, descripcion, capacidad) VALUES (?, ?, ?)";
        try (Connection conn = ConexionBD.conectar();  // Conexión automática con try-with-resources
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, lab.getUbicacion());     // Establece ubicación
            stmt.setString(2, lab.getDescripcion());   // Establece descripción
            stmt.setInt(3, lab.getCapacidad());        // Establece capacidad
            stmt.executeUpdate();                      // Ejecuta la inserción
        }
    }

    /**
     * Obtiene la lista completa de laboratorios desde la base de datos.
     * 
     * @return Lista de objetos Laboratorio.
     * @throws SQLException Si ocurre un error en la consulta.
     */
    public List<Laboratorio> listar() throws SQLException {
        List<Laboratorio> lista = new ArrayList<>();
        String sql = "SELECT * FROM laboratorio";
        try (Connection conn = ConexionBD.conectar();        // Conecta a la BD
             Statement stmt = conn.createStatement();        // Crea una sentencia simple
             ResultSet rs = stmt.executeQuery(sql)) {        // Ejecuta la consulta
             
            while (rs.next()) {
                // Crea un nuevo objeto Laboratorio con los datos de cada fila
                lista.add(new Laboratorio(
                    rs.getInt("id_laboratorio"),
                    rs.getString("ubicacion"),
                    rs.getString("descripcion"),
                    rs.getInt("capacidad")
                ));
            }
        }
        return lista; // Devuelve la lista completa
    }

    /**
     * Actualiza los datos de un laboratorio existente basado en su ID.
     * 
     * @param lab Objeto Laboratorio con los datos actualizados.
     * @throws SQLException Si ocurre un error al actualizar.
     */
    public void actualizar(Laboratorio lab) throws SQLException {
        String sql = "UPDATE laboratorio SET ubicacion = ?, descripcion = ?, capacidad = ? WHERE id_laboratorio = ?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, lab.getUbicacion());
            stmt.setString(2, lab.getDescripcion());
            stmt.setInt(3, lab.getCapacidad());
            stmt.setInt(4, lab.getIdLaboratorio()); // Se actualiza por ID
            stmt.executeUpdate(); // Ejecuta la actualización
        }
    }

    /**
     * Elimina un laboratorio de la base de datos usando su ID.
     * 
     * @param idLaboratorio ID del laboratorio a eliminar.
     * @throws SQLException Si ocurre un error durante la eliminación.
     */
    public void eliminar(int idLaboratorio) throws SQLException {
        String sql = "DELETE FROM laboratorio WHERE id_laboratorio = ?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idLaboratorio); // Establece el ID para eliminar
            stmt.executeUpdate(); // Ejecuta la eliminación
        }
    }
}

