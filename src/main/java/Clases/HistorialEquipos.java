/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import java.sql.Date;

public class HistorialEquipos {

    private int idHistorialEquipos;
    private String idEquipos;
    private Date fecha;
    private String categoria;
    private String descripcion;

    public HistorialEquipos() {}

    public HistorialEquipos(int idHistorialEquipos, String idEquipos, Date fecha, String categoria, String descripcion) {
        this.idHistorialEquipos = idHistorialEquipos;
        this.idEquipos = idEquipos;
        this.fecha = fecha;
        this.categoria = categoria;
        this.descripcion = descripcion;
    }

    // Getters y Setters

    public int getIdHistorialEquipos() {
        return idHistorialEquipos;
    }

    public void setIdHistorialEquipos(int idHistorialEquipos) {
        this.idHistorialEquipos = idHistorialEquipos;
    }

    public String getIdEquipos() {
        return idEquipos;
    }

    public void setIdEquipos(String idEquipos) {
        this.idEquipos = idEquipos;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
