/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

/**
 *
 * @author DOC
 */
public class CategoriaEquipo {
    private Long id;
    private String nombre;
    private String descripcion;
    private int tiempoMaximoPrestamo; // en horas
    
    // Constructor
    public CategoriaEquipo(Long id, String nombre, String descripcion, int tiempoMaximoPrestamo) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tiempoMaximoPrestamo = tiempoMaximoPrestamo;
    }
    
    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    public int getTiempoMaximoPrestamo() { return tiempoMaximoPrestamo; }
    public void setTiempoMaximoPrestamo(int tiempoMaximoPrestamo) { this.tiempoMaximoPrestamo = tiempoMaximoPrestamo; }
}