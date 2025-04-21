/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

/*
 * Autor: Equipo Soldados Caídos
 */
public class Insumo {
    private int idInsumo; //Para lectura y edición
    private String nombreInsumo;
    private int cantidad;
    private String categoria;
    private Integer idLaboratorio; // Puede ser null
    private String disponibilidad;

    // Constructor sin ID (para inserción de datos)
    public Insumo(String nombreInsumo, int cantidad, String categoria, Integer idLaboratorio, String disponibilidad) {
        this.nombreInsumo = nombreInsumo;
        this.cantidad = cantidad;
        this.categoria = categoria;
        this.idLaboratorio = idLaboratorio;
        this.disponibilidad = disponibilidad;
    }

    // Constructor con ID (para lectura y actualización)
    public Insumo(int idInsumo, String nombreInsumo, int cantidad, String categoria, 
                  Integer idLaboratorio, String disponibilidad) {
        this.idInsumo = idInsumo;
        this.nombreInsumo = nombreInsumo;
        this.cantidad = cantidad;
        this.categoria = categoria;
        this.idLaboratorio = idLaboratorio;
        this.disponibilidad = disponibilidad;
    }

    // Getters y setters
    public int getIdInsumo() {
        return idInsumo;
    }

    public String getNombreInsumo() {
        return nombreInsumo;
    }

    public void setNombreInsumo(String nombreInsumo) {
        this.nombreInsumo = nombreInsumo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Integer getIdLaboratorio() {
        return idLaboratorio;
    }

    public void setIdLaboratorio(Integer idLaboratorio) {
        this.idLaboratorio = idLaboratorio;
    }

    public String getDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(String disponibilidad) {
        this.disponibilidad = disponibilidad;
    }
}