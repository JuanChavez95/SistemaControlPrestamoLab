/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

/**
 * Clase que representa el detalle de préstamo de insumos.
 * Autor: Equipo Soldados Caídos
 */
public class DetallePrestamoInsumo {
    private int idDetalleInsumo;
    private int idPrestamo;
    private int idInsumo;
    private int cantidadInicial;
    private Integer cantidadFinal; // Puede ser nulo hasta finalizar el préstamo
    
    // Constructor sin ID (para inserción de datos)
    public DetallePrestamoInsumo(int idPrestamo, int idInsumo, int cantidadInicial, Integer cantidadFinal) {
        this.idPrestamo = idPrestamo;
        this.idInsumo = idInsumo;
        this.cantidadInicial = cantidadInicial;
        this.cantidadFinal = cantidadFinal;
    }
    
    // Constructor con ID (para la lectura y actualización de datos)
    public DetallePrestamoInsumo(int idDetalleInsumo, int idPrestamo, int idInsumo, 
                                 int cantidadInicial, Integer cantidadFinal) {
        this.idDetalleInsumo = idDetalleInsumo;
        this.idPrestamo = idPrestamo;
        this.idInsumo = idInsumo;
        this.cantidadInicial = cantidadInicial;
        this.cantidadFinal = cantidadFinal;
    }
    
    // Getters y setters
    public int getIdDetalleInsumo() {
        return idDetalleInsumo;
    }
    
    public void setIdDetalleInsumo(int idDetalleInsumo) {
        this.idDetalleInsumo = idDetalleInsumo;
    }
    
    public int getIdPrestamo() {
        return idPrestamo;
    }
    
    public void setIdPrestamo(int idPrestamo) {
        this.idPrestamo = idPrestamo;
    }
    
    public int getIdInsumo() {
        return idInsumo;
    }
    
    public void setIdInsumo(int idInsumo) {
        this.idInsumo = idInsumo;
    }
    
    public int getCantidadInicial() {
        return cantidadInicial;
    }
    
    public void setCantidadInicial(int cantidadInicial) {
        this.cantidadInicial = cantidadInicial;
    }
    
    public Integer getCantidadFinal() {
        return cantidadFinal;
    }
    
    public void setCantidadFinal(Integer cantidadFinal) {
        this.cantidadFinal = cantidadFinal;
    }
}