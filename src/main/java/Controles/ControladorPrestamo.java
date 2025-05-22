/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controles;

import Clases.Prestamo;
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
import java.util.logging.Logger;

/**
 * Controlador para operaciones CRUD sobre la tabla prestamo.
 * Autor: Equipo Soldados Caídos
 */
public class ControladorPrestamo {
    private static final Logger LOGGER = Logger.getLogger(ControladorPrestamo.class.getName());
    private final ControladorInsumo controladorInsumo = new ControladorInsumo();
    private final ControladorEquipamiento controladorEquipamiento = new ControladorEquipamiento();

    // Verificar si un usuario existe
    public boolean existeUsuario(int ru) throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuario WHERE ru = ?";
        System.out.println("Verificando existencia de usuario con RU: " + ru);
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, ru);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                System.out.println("Resultado de existeUsuario para RU " + ru + ": " + (count > 0));
                return count > 0;
            }
            System.out.println("No se encontraron resultados para RU: " + ru);
            return false;
        } catch (SQLException ex) {
            System.err.println("Error en existeUsuario para RU " + ru + ": " + ex.getMessage());
            throw ex;
        }
    }

    // Insertar un préstamo (sin ID)
    public int insertar(Prestamo prestamo) throws SQLException {
        String sql = "INSERT INTO prestamo (ru_usuario, ru_administrador, id_horario, fecha_prestamo, " +
                    "hora_prestamo, estado_prestamo, observaciones) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, prestamo.getRuUsuario());
            
            // Manejo de valores que pueden ser NULL
            if (prestamo.getRuAdministrador() != null) {
                stmt.setInt(2, prestamo.getRuAdministrador());
            } else {
                stmt.setNull(2, java.sql.Types.INTEGER);
            }
            
            if (prestamo.getIdHorario() != null) {
                stmt.setInt(3, prestamo.getIdHorario());
            } else {
                stmt.setNull(3, java.sql.Types.INTEGER);
            }
            
            stmt.setDate(4, prestamo.getFechaPrestamo());
            stmt.setString(5, prestamo.getHoraPrestamo());
            stmt.setString(6, prestamo.getEstadoPrestamo());
            stmt.setString(7, prestamo.getObservaciones());
            
            stmt.executeUpdate();
            
            // Obtener el ID generado
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return -1;
    }

    // Listar todos los préstamos
    public List<Prestamo> listar() throws SQLException {
        List<Prestamo> lista = new ArrayList<>();
        String sql = "SELECT * FROM prestamo";
        try (Connection conn = ConexionBD.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Prestamo(
                    rs.getInt("id_prestamo"),
                    rs.getInt("ru_usuario"),
                    rs.getObject("ru_administrador") != null ? rs.getInt("ru_administrador") : null,
                    rs.getObject("id_horario") != null ? rs.getInt("id_horario") : null,
                    rs.getDate("fecha_prestamo"),
                    rs.getString("hora_prestamo"),
                    rs.getString("estado_prestamo"),
                    rs.getString("observaciones")
                ));
            }
        }
        return lista;
    }

    // Actualizar un préstamo por ID
    public void actualizar(Prestamo prestamo) throws SQLException {
        String sql = "UPDATE prestamo SET ru_usuario = ?, ru_administrador = ?, id_horario = ?, " +
                    "fecha_prestamo = ?, hora_prestamo = ?, estado_prestamo = ?, observaciones = ? " +
                    "WHERE id_prestamo = ?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, prestamo.getRuUsuario());
            
            // Manejo de valores que pueden ser NULL
            if (prestamo.getRuAdministrador() != null) {
                stmt.setInt(2, prestamo.getRuAdministrador());
            } else {
                stmt.setNull(2, java.sql.Types.INTEGER);
            }
            
            if (prestamo.getIdHorario() != null) {
                stmt.setInt(3, prestamo.getIdHorario());
            } else {
                stmt.setNull(3, java.sql.Types.INTEGER);
            }
            
            stmt.setDate(4, prestamo.getFechaPrestamo());
            stmt.setString(5, prestamo.getHoraPrestamo());
            stmt.setString(6, prestamo.getEstadoPrestamo());
            stmt.setString(7, prestamo.getObservaciones());
            stmt.setInt(8, prestamo.getIdPrestamo());
            
            stmt.executeUpdate();
        }
    }

    // Eliminar un préstamo por ID
    public void eliminar(int idPrestamo) throws SQLException {
        String sql = "DELETE FROM prestamo WHERE id_prestamo = ?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPrestamo);
            stmt.executeUpdate();
        }
    }
    
    // Buscar un préstamo por ID
    public Prestamo buscarPorId(int idPrestamo) throws SQLException {
        String sql = "SELECT * FROM prestamo WHERE id_prestamo = ?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPrestamo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Prestamo(
                        rs.getInt("id_prestamo"),
                        rs.getInt("ru_usuario"),
                        rs.getObject("ru_administrador") != null ? rs.getInt("ru_administrador") : null,
                        rs.getObject("id_horario") != null ? rs.getInt("id_horario") : null,
                        rs.getDate("fecha_prestamo"),
                        rs.getString("hora_prestamo"),
                        rs.getString("estado_prestamo"),
                        rs.getString("observaciones")
                    );
                }
            }
        }
        return null;
    }
    
    // Listar préstamos por usuario
    public List<Prestamo> listarPorUsuario(int ruUsuario) throws SQLException {
        List<Prestamo> lista = new ArrayList<>();
        String sql = "SELECT * FROM prestamo WHERE ru_usuario = ?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, ruUsuario);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Prestamo(
                        rs.getInt("id_prestamo"),
                        rs.getInt("ru_usuario"),
                        rs.getObject("ru_administrador") != null ? rs.getInt("ru_administrador") : null,
                        rs.getObject("id_horario") != null ? rs.getInt("id_horario") : null,
                        rs.getDate("fecha_prestamo"),
                        rs.getString("hora_prestamo"),
                        rs.getString("estado_prestamo"),
                        rs.getString("observaciones")
                    ));
                }
            }
        }
        return lista;
    }
    
    // Listar préstamos activos
    public List<Prestamo> listarActivos() throws SQLException {
        List<Prestamo> lista = new ArrayList<>();
        String sql = "SELECT * FROM prestamo WHERE estado_prestamo = 'En curso'";
        try (Connection conn = ConexionBD.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Prestamo(
                    rs.getInt("id_prestamo"),
                    rs.getInt("ru_usuario"),
                    rs.getObject("ru_administrador") != null ? rs.getInt("ru_administrador") : null,
                    rs.getObject("id_horario") != null ? rs.getInt("id_horario") : null,
                    rs.getDate("fecha_prestamo"),
                    rs.getString("hora_prestamo"),
                    rs.getString("estado_prestamo"),
                    rs.getString("observaciones")
                ));
            }
        }
        return lista;
    }

    // Obtener equipamientos de un préstamo
    public List<Integer> obtenerEquipamientosPrestamo(int idPrestamo) throws SQLException {
        List<Integer> equipamientoIds = new ArrayList<>();
        String sql = "SELECT id_equipamiento FROM detalle_prestamo_equipamiento WHERE id_prestamo = ?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPrestamo);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    equipamientoIds.add(rs.getInt("id_equipamiento"));
                }
            }
        }
        return equipamientoIds;
    }

    // Obtener insumos y cantidades de un préstamo
    public Map<Integer, Integer> obtenerInsumosPrestamoConCantidades(int idPrestamo) throws SQLException {
        Map<Integer, Integer> insumoCantidades = new HashMap<>();
        String sql = "SELECT id_insumo, cantidad_inicial FROM detalle_prestamo_insumo WHERE id_prestamo = ?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPrestamo);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    insumoCantidades.put(rs.getInt("id_insumo"), rs.getInt("cantidad_inicial"));
                }
            }
        }
        return insumoCantidades;
    }

    // Insertar detalle de equipamiento en un préstamo
    public void insertarDetalleEquipamiento(int idPrestamo, int idEquipamiento) throws SQLException {
        String sql = "INSERT INTO detalle_prestamo_equipamiento (id_prestamo, id_equipamiento) VALUES (?, ?)";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPrestamo);
            stmt.setInt(2, idEquipamiento);
            stmt.executeUpdate();
        }
    }

    // Insertar detalle de insumo en un préstamo  
    public void insertarDetalleInsumo(int idPrestamo, int idInsumo, int cantidad) throws SQLException {
        String sql = "INSERT INTO detalle_prestamo_insumo (id_prestamo, id_insumo, cantidad_inicial) VALUES (?, ?, ?)";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPrestamo);
            stmt.setInt(2, idInsumo);
            stmt.setInt(3, cantidad);
            stmt.executeUpdate();
        }
    }

    // Obtener el nombre de un usuario por RU
    public String obtenerNombreUsuario(int ruUsuario) throws SQLException {
        String sql = "SELECT nombre FROM usuario WHERE ru = ?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, ruUsuario);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("nombre");
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("Error al obtener nombre de usuario con RU " + ruUsuario + ": " + e.getMessage());
            throw e;
        }
        return null;
    }

    // Aceptar un préstamo
    public void aceptarPrestamo(int idPrestamo, Integer idHorario, int ruAdministrador, String observaciones) throws SQLException {
        // Actualizar el estado del préstamo
        String sql = "UPDATE prestamo SET estado_prestamo = ?, ru_administrador = ?, observaciones = ? WHERE id_prestamo = ?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "Aceptado");
            stmt.setInt(2, ruAdministrador);
            stmt.setString(3, observaciones);
            stmt.setInt(4, idPrestamo);
            int filas = stmt.executeUpdate();
            if (filas == 0) {
                throw new SQLException("No se encontró el préstamo con ID " + idPrestamo);
            }
        } catch (SQLException e) {
            LOGGER.severe("Error al aceptar préstamo ID " + idPrestamo + ": " + e.getMessage());
            throw e;
        }

        // Actualizar disponibilidad de equipamientos
        List<Integer> equipamientoIds = obtenerEquipamientosPrestamo(idPrestamo);
        for (Integer idEquipamiento : equipamientoIds) {
            controladorEquipamiento.actualizarDisponibilidad(idEquipamiento, "Prestado");
        }

        // Reducir la cantidad de insumos
        Map<Integer, Integer> insumoCantidades = obtenerInsumosPrestamoConCantidades(idPrestamo);
        for (Map.Entry<Integer, Integer> entry : insumoCantidades.entrySet()) {
            int idInsumo = entry.getKey();
            int cantidadPrestada = entry.getValue();
            Clases.Insumo insumo = controladorInsumo.buscarPorId(idInsumo);
            if (insumo != null) {
                int nuevaCantidad = insumo.getCantidad() - cantidadPrestada;
                controladorInsumo.actualizarCantidad(idInsumo, nuevaCantidad);
                controladorInsumo.actualizarDisponibilidad(idInsumo, nuevaCantidad > 0 ? "Disponible" : "No Disponible");
            }
        }
    }

    // Rechazar un préstamo
    public void rechazarPrestamo(int idPrestamo) throws SQLException {
        String sql = "UPDATE prestamo SET estado_prestamo = ? WHERE id_prestamo = ?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "Rechazado");
            stmt.setInt(2, idPrestamo);
            int filas = stmt.executeUpdate();
            if (filas == 0) {
                throw new SQLException("No se encontró el préstamo con ID " + idPrestamo);
            }
        } catch (SQLException e) {
            LOGGER.severe("Error al rechazar préstamo ID " + idPrestamo + ": " + e.getMessage());
            throw e;
        }

        // Restaurar disponibilidad de equipamientos
        List<Integer> equipamientoIds = obtenerEquipamientosPrestamo(idPrestamo);
        for (Integer idEquipamiento : equipamientoIds) {
            controladorEquipamiento.actualizarDisponibilidad(idEquipamiento, "Disponible");
        }
    }

    // Terminar un préstamo
   public void terminarPrestamo(int idPrestamo, List<Integer> insumoIds, List<Integer> cantidadesDevueltas) throws SQLException {
    String sql = "UPDATE prestamo SET estado_prestamo = ? WHERE id_prestamo = ?";
    try (Connection conn = ConexionBD.conectar();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, "Terminado");
        stmt.setInt(2, idPrestamo);
        int filas = stmt.executeUpdate();
        if (filas == 0) {
            throw new SQLException("No se encontró el préstamo con ID " + idPrestamo);
        }
    } catch (SQLException e) {
        LOGGER.severe("Error al terminar préstamo ID " + idPrestamo + ": " + e.getMessage());
        throw e;
    }

    // Registrar cantidades devueltas y actualizar cantidades de insumos
    for (int i = 0; i < insumoIds.size(); i++) {
        int idInsumo = insumoIds.get(i);
        int cantidadDevuelta = cantidadesDevueltas.get(i);

        // Actualizar la cantidad devuelta en detalle_prestamo_insumo
        String sqlDetalle = "UPDATE detalle_prestamo_insumo SET cantidad_final = ? WHERE id_prestamo = ? AND id_insumo = ?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sqlDetalle)) {
            stmt.setInt(1, cantidadDevuelta);
            stmt.setInt(2, idPrestamo);
            stmt.setInt(3, idInsumo);
            stmt.executeUpdate();
        }

        // Obtener la cantidad inicial prestada para este insumo
        String sqlCantidadInicial = "SELECT cantidad_inicial FROM detalle_prestamo_insumo WHERE id_prestamo = ? AND id_insumo = ?";
        int cantidadInicial = 0;
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sqlCantidadInicial)) {
            stmt.setInt(1, idPrestamo);
            stmt.setInt(2, idInsumo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    cantidadInicial = rs.getInt("cantidad_inicial");
                }
            }
        }

        // Actualizar la cantidad en la tabla insumos
        Clases.Insumo insumo = controladorInsumo.buscarPorId(idInsumo);
        if (insumo != null) {
            // La nueva cantidad debe ser la actual más lo que se devuelve
            int nuevaCantidad = insumo.getCantidad() + cantidadDevuelta;
            controladorInsumo.actualizarCantidad(idInsumo, nuevaCantidad);
            controladorInsumo.actualizarDisponibilidad(idInsumo, nuevaCantidad > 0 ? "Disponible" : "No Disponible");
            System.out.println("Insumo ID " + idInsumo + ": Cantidad actual " + insumo.getCantidad() + 
                              " + Devuelto " + cantidadDevuelta + " = Nueva cantidad " + nuevaCantidad);
        }
    }

    // Restaurar disponibilidad de equipamientos
    List<Integer> equipamientoIds = obtenerEquipamientosPrestamo(idPrestamo);
    for (Integer idEquipamiento : equipamientoIds) {
        controladorEquipamiento.actualizarDisponibilidad(idEquipamiento, "Disponible");
    }
}

    
    // Método faltante en ControladorPrestamo
    public Integer obtenerHorarioPrestamo(int idPrestamo) throws SQLException {
        String sql = "SELECT id_horario FROM prestamo WHERE id_prestamo = ?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPrestamo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getObject("id_horario") != null ? rs.getInt("id_horario") : null;
                }
            }
        }
        return null;
    }
    
    
    /**
    * Cuenta el número de préstamos con un determinado estado.
    * 
    * @param estado Estado de los préstamos a contar
    * @return Número de préstamos con el estado especificado
    * @throws SQLException Si ocurre un error al ejecutar la consulta
    */
    public int contarPrestamosPorEstado(String estado) throws SQLException {
        String sql = "SELECT COUNT(*) FROM prestamo WHERE estado_prestamo = ?";
        try (Connection conn = ConexionBD.conectar();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, estado);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }
    
    /**
    * Obtiene el email del usuario por su RU.
    * @param ruUsuario RU del usuario
    * @return Email del usuario o null si no se encuentra
    * @throws SQLException Si ocurre un error en la base de datos
    */
    public String obtenerEmailUsuario(int ruUsuario) throws SQLException {
        String sql = "SELECT email FROM usuario WHERE ru = ?";

        // Aquí se obtiene una conexión desde ConexionBD
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, ruUsuario);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("email");
                }
            }
        }
        return null;
    }

}