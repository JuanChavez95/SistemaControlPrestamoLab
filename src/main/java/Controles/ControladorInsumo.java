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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    
    public String obtenerNombreInsumo(int idInsumo) throws SQLException {
    String sql = "SELECT nombre_insumo FROM insumos WHERE id_insumo = ?";
    try (Connection conn = ConexionBD.conectar();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, idInsumo);
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getString("nombre_insumo");
            }
        }
    }
    return "Desconocido";   
    }
    
    /**
    * Obtiene un mapa que contiene la cantidad de insumos agrupados por rangos.
    * Los rangos son: "0-10", "11-50", "51-100", "101+"
    * 
    * @return Mapa con los rangos como claves y la cantidad de insumos en ese rango como valores
    * @throws SQLException Si ocurre un error al ejecutar la consulta
    */
    public Map<String, Integer> obtenerInsumosPorRangoCantidad() throws SQLException {
        Map<String, Integer> mapaRangos = new HashMap<>();

        // Inicializar los rangos
        mapaRangos.put("0-10", 0);
        mapaRangos.put("11-50", 0);
        mapaRangos.put("51-100", 0);
        mapaRangos.put("101+", 0);

        // Contar insumos por rango
        mapaRangos.put("0-10", contarInsumosPorRango(0, 10));
        mapaRangos.put("11-50", contarInsumosPorRango(11, 50));
        mapaRangos.put("51-100", contarInsumosPorRango(51, 100));

        // Para el rango "101+" usamos un valor máximo grande
        String sql = "SELECT COUNT(*) FROM insumos WHERE cantidad > 100";
        try (Connection conn = ConexionBD.conectar();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
               mapaRangos.put("101+", rs.getInt(1));
            }
        }

        return mapaRangos;
    }

   /**
    * Cuenta la cantidad de insumos que tienen una cantidad entre el mínimo y máximo especificados.
    * 
    * @param min Valor mínimo del rango
    * @param max Valor máximo del rango
    * @return Número de insumos que tienen una cantidad dentro del rango especificado
    * @throws SQLException Si ocurre un error al ejecutar la consulta
    */
    public int contarInsumosPorRango(int min, int max) throws SQLException {
        String sql = "SELECT COUNT(*) FROM insumos WHERE cantidad BETWEEN ? AND ?";
        try (Connection conn = ConexionBD.conectar();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, min);
            stmt.setInt(2, max);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                   return rs.getInt(1);
                }
            }
        }
        return 0;
    }


    

}