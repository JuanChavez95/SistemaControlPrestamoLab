/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

/*
 * Autor: Equipo Soldados Caídos
 */
public class Laboratorio {
    private int idLaboratorio; //Para lectura y edición
    private String ubicacion;
    private String descripcion;
    private int capacidad;

    // Constructor sin ID (para inserción de datos)
    public Laboratorio(String ubicacion, String descripcion, int capacidad) {
        this.ubicacion = ubicacion;
        this.descripcion = descripcion;
        this.capacidad = capacidad;
    }

    // Constructor con ID (para la lectura y actualización de datos)
    public Laboratorio(int idLaboratorio, String ubicacion, String descripcion, int capacidad) {
        this.idLaboratorio = idLaboratorio;
        this.ubicacion = ubicacion;
        this.descripcion = descripcion;
        this.capacidad = capacidad;
    }

    public int getIdLaboratorio() {
        return idLaboratorio;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }
}
