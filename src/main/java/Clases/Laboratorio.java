/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

/*
 * Clase que representa la entidad Laboratorio.
 * Se usa para mapear objetos a registros de la base de datos.
 * Autor: Soldados Caídos
 */
public class Laboratorio {

    // Atributos privados que representan las columnas de la tabla laboratorio
    private int idLaboratorio;     // ID del laboratorio (clave primaria, generado automáticamente)
    private String ubicacion;      // Ubicación física del laboratorio (ej. "Bloque A")
    private String descripcion;    // Descripción general del laboratorio
    private int capacidad;         // Número máximo de personas o equipos que puede alojar

    /**
     * Constructor utilizado al crear un nuevo laboratorio (sin ID, ya que lo asigna la base de datos).
     * @param ubicacion Ubicación del laboratorio.
     * @param descripcion Descripción del laboratorio.
     * @param capacidad Capacidad máxima del laboratorio.
     */
    public Laboratorio(String ubicacion, String descripcion, int capacidad) {
        this.ubicacion = ubicacion;
        this.descripcion = descripcion;
        this.capacidad = capacidad;
    }

    /**
     * Constructor utilizado al cargar o actualizar un laboratorio existente.
     * @param idLaboratorio ID del laboratorio.
     * @param ubicacion Ubicación del laboratorio.
     * @param descripcion Descripción del laboratorio.
     * @param capacidad Capacidad del laboratorio.
     */
    public Laboratorio(int idLaboratorio, String ubicacion, String descripcion, int capacidad) {
        this.idLaboratorio = idLaboratorio;
        this.ubicacion = ubicacion;
        this.descripcion = descripcion;
        this.capacidad = capacidad;
    }

    // Métodos getter y setter para acceder y modificar los atributos

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
