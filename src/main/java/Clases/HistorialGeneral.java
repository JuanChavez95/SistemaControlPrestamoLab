/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import java.util.Date;

public class HistorialGeneral {
    // Atributos Privados
    private int idHistorial;
    private int ruAdministrador;
    private Date fecha;
    private String categoria;
    private String descripcion;

    // Constructor vacío
    public HistorialGeneral() {}

    // Constructor completo
    public HistorialGeneral(int idHistorial, int ruAdministrador, Date fecha, String categoria, String descripcion) {
        this.idHistorial = idHistorial;
        this.ruAdministrador = ruAdministrador;
        this.fecha = fecha;
        this.categoria = categoria;
        this.descripcion = descripcion;
    }
    
    // Constructor sin ID (para inserciones donde el ID es auto-incremental)
    public HistorialGeneral(int ruAdministrador, Date fecha, String categoria, String descripcion) {
        this.ruAdministrador = ruAdministrador;
        this.fecha = fecha;
        this.categoria = categoria;
        this.descripcion = descripcion;
    }

    // Getters y Setters
    public int getIdHistorial() {
        return idHistorial;
    }
    public void setIdHistorial(int idHistorial) {
        this.idHistorial = idHistorial;
    }

    public int getRuAdministrador() {
        return ruAdministrador;
    }
    public void setRuAdministrador(int ruAdministrador) {
        this.ruAdministrador = ruAdministrador;
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
