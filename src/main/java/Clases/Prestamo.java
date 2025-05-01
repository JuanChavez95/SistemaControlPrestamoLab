/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import java.sql.Date;

/**
 * Clase que representa un préstamo en el sistema.
 * Autor: Equipo Soldados Caídos
 */
public class Prestamo {
    private int idPrestamo;
    private int ruUsuario;
    private Integer ruAdministrador; // Puede ser nulo
    private Integer idHorario;       // Puede ser nulo
    private Date fechaPrestamo;
    private String horaPrestamo;
    private String estadoPrestamo;
    private String observaciones;    // Puede ser nulo

    // Constructor sin ID (para inserción de datos)
    public Prestamo(int ruUsuario, Integer ruAdministrador, Integer idHorario, 
                   Date fechaPrestamo, String horaPrestamo, String estadoPrestamo, 
                   String observaciones) {
        this.ruUsuario = ruUsuario;
        this.ruAdministrador = ruAdministrador;
        this.idHorario = idHorario;
        this.fechaPrestamo = fechaPrestamo;
        this.horaPrestamo = horaPrestamo;
        this.estadoPrestamo = estadoPrestamo;
        this.observaciones = observaciones;
    }

    // Constructor con ID (para la lectura y actualización de datos)
    public Prestamo(int idPrestamo, int ruUsuario, Integer ruAdministrador, Integer idHorario, 
                java.sql.Date fechaPrestamo, String horaPrestamo, String estadoPrestamo, String observaciones) {
    this.idPrestamo = idPrestamo;
    this.ruUsuario = ruUsuario;
    this.ruAdministrador = ruAdministrador;
    this.idHorario = idHorario;
    this.fechaPrestamo = fechaPrestamo;
    this.horaPrestamo = horaPrestamo;
    this.estadoPrestamo = estadoPrestamo;
    this.observaciones = observaciones;
}

    // Getters y setters
    public int getIdPrestamo() {
        return idPrestamo;
    }

    public void setIdPrestamo(int idPrestamo) {
        this.idPrestamo = idPrestamo;
    }

    public int getRuUsuario() {
        return ruUsuario;
    }

    public void setRuUsuario(int ruUsuario) {
        this.ruUsuario = ruUsuario;
    }

    public Integer getRuAdministrador() {
        return ruAdministrador;
    }

    public void setRuAdministrador(Integer ruAdministrador) {
        this.ruAdministrador = ruAdministrador;
    }

    public Integer getIdHorario() {
        return idHorario;
    }

    public void setIdHorario(Integer idHorario) {
        this.idHorario = idHorario;
    }

    public Date getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(Date fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public String getHoraPrestamo() {
        return horaPrestamo;
    }

    public void setHoraPrestamo(String horaPrestamo) {
        this.horaPrestamo = horaPrestamo;
    }

    public String getEstadoPrestamo() {
        return estadoPrestamo;
    }

    public void setEstadoPrestamo(String estadoPrestamo) {
        this.estadoPrestamo = estadoPrestamo;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}