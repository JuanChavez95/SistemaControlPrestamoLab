/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import java.util.Date;

/**
 * Clase que representa una sanción en el sistema.
 * Autor: Equipo Soldados Caídos
 */
public class Sancion {
    private int idSancion;
    private int ruUsuario;
    private Integer idPrestamo; // Puede ser null
    private String tipoSancion;
    private String descripcion;
    private Date fechaSancion;
    private String estadoSancion;
    private Date fechaInicio;
    private Date fechaFin;
    private int diasSuspension;

    // Constructor sin ID (para inserción)
    public Sancion(int ruUsuario, Integer idPrestamo, String tipoSancion, String descripcion, 
                  Date fechaSancion, String estadoSancion, Date fechaInicio, Date fechaFin, 
                  int diasSuspension) {
        this.ruUsuario = ruUsuario;
        this.idPrestamo = idPrestamo;
        this.tipoSancion = tipoSancion;
        this.descripcion = descripcion;
        this.fechaSancion = fechaSancion;
        this.estadoSancion = estadoSancion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.diasSuspension = diasSuspension;
    }

    // Constructor con ID (para lectura y actualización)
    public Sancion(int idSancion, int ruUsuario, Integer idPrestamo, String tipoSancion, 
                  String descripcion, Date fechaSancion, String estadoSancion, 
                  Date fechaInicio, Date fechaFin, int diasSuspension) {
        this.idSancion = idSancion;
        this.ruUsuario = ruUsuario;
        this.idPrestamo = idPrestamo;
        this.tipoSancion = tipoSancion;
        this.descripcion = descripcion;
        this.fechaSancion = fechaSancion;
        this.estadoSancion = estadoSancion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.diasSuspension = diasSuspension;
    }

    public int getIdSancion() {
        return idSancion;
    }

    public int getRuUsuario() {
        return ruUsuario;
    }

    public void setRuUsuario(int ruUsuario) {
        this.ruUsuario = ruUsuario;
    }

    public Integer getIdPrestamo() {
        return idPrestamo;
    }

    public void setIdPrestamo(Integer idPrestamo) {
        this.idPrestamo = idPrestamo;
    }

    public String getTipoSancion() {
        return tipoSancion;
    }

    public void setTipoSancion(String tipoSancion) {
        this.tipoSancion = tipoSancion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFechaSancion() {
        return fechaSancion;
    }

    public void setFechaSancion(Date fechaSancion) {
        this.fechaSancion = fechaSancion;
    }

    public String getEstadoSancion() {
        return estadoSancion;
    }

    public void setEstadoSancion(String estadoSancion) {
        this.estadoSancion = estadoSancion;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public int getDiasSuspension() {
        return diasSuspension;
    }

    public void setDiasSuspension(int diasSuspension) {
        this.diasSuspension = diasSuspension;
    }
}