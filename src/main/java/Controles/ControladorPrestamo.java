/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controles;

/**
 *
 * @author DOC
 */
public class ControladorPrestamo {
    /*
    CLASE PRÉSTAMO:
    package Clases;

import java.sql.Date;

public class Prestamo {
    private int idPrestamo;
    private int ruUsuario;
    private Integer ruAdministrador; // Puede ser null
    private Integer idHorario;       // Puede ser null
    private Date fechaPrestamo;
    private String horaPrestamo;
    private String observaciones;

    // Constructor sin ID (para inserciones)
    public Prestamo(int ruUsuario, Integer ruAdministrador, Integer idHorario, Date fechaPrestamo, String horaPrestamo, String observaciones) {
        this.ruUsuario = ruUsuario;
        this.ruAdministrador = ruAdministrador;
        this.idHorario = idHorario;
        this.fechaPrestamo = fechaPrestamo;
        this.horaPrestamo = horaPrestamo;
        this.observaciones = observaciones;
    }

    // Constructor con ID (para lectura y edición)
    public Prestamo(int idPrestamo, int ruUsuario, Integer ruAdministrador, Integer idHorario, Date fechaPrestamo, String horaPrestamo, String observaciones) {
        this.idPrestamo = idPrestamo;
        this.ruUsuario = ruUsuario;
        this.ruAdministrador = ruAdministrador;
        this.idHorario = idHorario;
        this.fechaPrestamo = fechaPrestamo;
        this.horaPrestamo = horaPrestamo;
        this.observaciones = observaciones;
    }

    // Getters y Setters
    public int getIdPrestamo() { return idPrestamo; }
    public int getRuUsuario() { return ruUsuario; }
    public void setRuUsuario(int ruUsuario) { this.ruUsuario = ruUsuario; }

    public Integer getRuAdministrador() { return ruAdministrador; }
    public void setRuAdministrador(Integer ruAdministrador) { this.ruAdministrador = ruAdministrador; }

    public Integer getIdHorario() { return idHorario; }
    public void setIdHorario(Integer idHorario) { this.idHorario = idHorario; }

    public Date getFechaPrestamo() { return fechaPrestamo; }
    public void setFechaPrestamo(Date fechaPrestamo) { this.fechaPrestamo = fechaPrestamo; }

    public String getHoraPrestamo() { return horaPrestamo; }
    public void setHoraPrestamo(String horaPrestamo) { this.horaPrestamo = horaPrestamo; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}

    CONTROLADOR PRÉSTAMO:
    package Controles;

import Clases.Prestamo;
import DataBase.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ControladorPrestamo {

    // Insertar un préstamo (administrador y horario pueden ser null)
    public void insertar(Prestamo p) throws SQLException {
        String sql = "INSERT INTO prestamo (ru_usuario, ru_administrador, id_horario, fecha_prestamo, hora_prestamo, observaciones) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, p.getRuUsuario());
            if (p.getRuAdministrador() != null) {
                stmt.setInt(2, p.getRuAdministrador());
            } else {
                stmt.setNull(2, Types.INTEGER);
            }
            if (p.getIdHorario() != null) {
                stmt.setInt(3, p.getIdHorario());
            } else {
                stmt.setNull(3, Types.INTEGER);
            }
            stmt.setDate(4, p.getFechaPrestamo());
            stmt.setString(5, p.getHoraPrestamo());
            stmt.setString(6, p.getObservaciones());
            stmt.executeUpdate();
        }
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
                    (Integer) rs.getObject("ru_administrador"),
                    (Integer) rs.getObject("id_horario"),
                    rs.getDate("fecha_prestamo"),
                    rs.getString("hora_prestamo"),
                    rs.getString("observaciones")
                ));
            }
        }
        return lista;
    }

    // Actualizar un préstamo
    public void actualizar(Prestamo p) throws SQLException {
        String sql = "UPDATE prestamo SET ru_usuario = ?, ru_administrador = ?, id_horario = ?, fecha_prestamo = ?, hora_prestamo = ?, observaciones = ? WHERE id_prestamo = ?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, p.getRuUsuario());
            if (p.getRuAdministrador() != null) {
                stmt.setInt(2, p.getRuAdministrador());
            } else {
                stmt.setNull(2, Types.INTEGER);
            }
            if (p.getIdHorario() != null) {
                stmt.setInt(3, p.getIdHorario());
            } else {
                stmt.setNull(3, Types.INTEGER);
            }
            stmt.setDate(4, p.getFechaPrestamo());
            stmt.setString(5, p.getHoraPrestamo());
            stmt.setString(6, p.getObservaciones());
            stmt.setInt(7, p.getIdPrestamo());
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
}

    */
}
