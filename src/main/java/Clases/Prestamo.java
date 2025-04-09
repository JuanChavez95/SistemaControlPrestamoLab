/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

/**
 *
 * @author DOC
 */
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class Prestamo {
    private Long id;
    private Usuario solicitante;
    private Usuario autorizador;
    private Date fechaSolicitud;
    private Date fechaInicioPrestamo;
    private Date fechaFinPrestamo;
    private Date fechaDevolucion;
    private EstadoPrestamo estado;
    private String observaciones;
    private List<EquipoPrestamo> equipos = new ArrayList<>();
    
    // Enum para el estado del préstamo
    public enum EstadoPrestamo {
        SOLICITADO,
        APROBADO,
        RECHAZADO,
        EN_CURSO,
        DEVUELTO,
        DEVUELTO_CON_RETRASO,
        DEVUELTO_CON_OBSERVACIONES,
        CANCELADO
    }
    
    // Constructor
    public Prestamo(Long id, Usuario solicitante, Date fechaInicioPrestamo, Date fechaFinPrestamo) {
        this.id = id;
        this.solicitante = solicitante;
        this.fechaSolicitud = new Date();
        this.fechaInicioPrestamo = fechaInicioPrestamo;
        this.fechaFinPrestamo = fechaFinPrestamo;
        this.estado = EstadoPrestamo.SOLICITADO;
    }
    
    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Usuario getSolicitante() { return solicitante; }
    public void setSolicitante(Usuario solicitante) { this.solicitante = solicitante; }
    
    public Usuario getAutorizador() { return autorizador; }
    public void setAutorizador(Usuario autorizador) { this.autorizador = autorizador; }
    
    public Date getFechaSolicitud() { return fechaSolicitud; }
    public void setFechaSolicitud(Date fechaSolicitud) { this.fechaSolicitud = fechaSolicitud; }
    
    public Date getFechaInicioPrestamo() { return fechaInicioPrestamo; }
    public void setFechaInicioPrestamo(Date fechaInicioPrestamo) { this.fechaInicioPrestamo = fechaInicioPrestamo; }
    
    public Date getFechaFinPrestamo() { return fechaFinPrestamo; }
    public void setFechaFinPrestamo(Date fechaFinPrestamo) { this.fechaFinPrestamo = fechaFinPrestamo; }
    
    public Date getFechaDevolucion() { return fechaDevolucion; }
    public void setFechaDevolucion(Date fechaDevolucion) { this.fechaDevolucion = fechaDevolucion; }
    
    public EstadoPrestamo getEstado() { return estado; }
    public void setEstado(EstadoPrestamo estado) { this.estado = estado; }
    
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
    
    public List<EquipoPrestamo> getEquipos() { return equipos; }
    public void setEquipos(List<EquipoPrestamo> equipos) { this.equipos = equipos; }
    
    public void addEquipo(EquipoPrestamo equipo) {
        this.equipos.add(equipo);
    }
    
    public void removeEquipo(EquipoPrestamo equipo) {
        this.equipos.remove(equipo);
    }
    
    // Método para aprobar un préstamo
    public void aprobar(Usuario autorizador) {
        this.autorizador = autorizador;
        this.estado = EstadoPrestamo.APROBADO;
    }
    
    // Método para rechazar un préstamo
    public void rechazar(Usuario autorizador, String motivo) {
        this.autorizador = autorizador;
        this.estado = EstadoPrestamo.RECHAZADO;
        this.observaciones = motivo;
    }
    
    // Método para iniciar un préstamo
    public void iniciar() {
        this.estado = EstadoPrestamo.EN_CURSO;
    }
    
    // Método para devolver un préstamo
    public void devolver(String observaciones) {
        this.fechaDevolucion = new Date();
        this.observaciones = observaciones;
        
        if (this.fechaDevolucion.after(this.fechaFinPrestamo)) {
            this.estado = EstadoPrestamo.DEVUELTO_CON_RETRASO;
        } else if (observaciones != null && !observaciones.isEmpty()) {
            this.estado = EstadoPrestamo.DEVUELTO_CON_OBSERVACIONES;
        } else {
            this.estado = EstadoPrestamo.DEVUELTO;
        }
    }
    
    // Método para cancelar un préstamo
    public void cancelar(String motivo) {
        this.estado = EstadoPrestamo.CANCELADO;
        this.observaciones = motivo;
    }
}
