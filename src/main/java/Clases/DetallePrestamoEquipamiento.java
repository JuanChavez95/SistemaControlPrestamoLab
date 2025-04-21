/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

/**
 * Clase que representa el detalle de préstamo de equipamiento.
 * Autor: Equipo Soldados Caídos
 */
public class DetallePrestamoEquipamiento {
    private int idDetalle;
    private int idPrestamo;
    private int idEquipamiento;
    
    // Constructor sin ID (para inserción de datos)
    public DetallePrestamoEquipamiento(int idPrestamo, int idEquipamiento) {
        this.idPrestamo = idPrestamo;
        this.idEquipamiento = idEquipamiento;
    }
    
    // Constructor con ID (para la lectura y actualización de datos)
    public DetallePrestamoEquipamiento(int idDetalle, int idPrestamo, int idEquipamiento) {
        this.idDetalle = idDetalle;
        this.idPrestamo = idPrestamo;
        this.idEquipamiento = idEquipamiento;
    }
    
    // Getters y setters
    public int getIdDetalle() {
        return idDetalle;
    }
    
    public void setIdDetalle(int idDetalle) {
        this.idDetalle = idDetalle;
    }
    
    public int getIdPrestamo() {
        return idPrestamo;
    }
    
    public void setIdPrestamo(int idPrestamo) {
        this.idPrestamo = idPrestamo;
    }
    
    public int getIdEquipamiento() {
        return idEquipamiento;
    }
    
    public void setIdEquipamiento(int idEquipamiento) {
        this.idEquipamiento = idEquipamiento;
    }
}