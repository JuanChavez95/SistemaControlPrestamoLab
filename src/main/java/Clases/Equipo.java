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

public class Equipo {
    private Long id;
    private String nombre;
    private String descripcion;
    private String numeroSerie;
    private String codigoInventario;
    private String marca;
    private String modelo;
    private Date fechaAdquisicion;
    private Double valorAdquisicion;
    private EstadoEquipo estado;
    private CategoriaEquipo categoria;
    private Laboratorio laboratorio;
    
    // Enum para el estado del equipo
    public enum EstadoEquipo {
        DISPONIBLE,
        PRESTADO,
        EN_MANTENIMIENTO,
        DADO_DE_BAJA,
        RESERVADO
    }
    
    // Constructor
    public Equipo(Long id, String nombre, String descripcion, String numeroSerie, String codigoInventario,
                 String marca, String modelo, Date fechaAdquisicion, Double valorAdquisicion,
                 CategoriaEquipo categoria, Laboratorio laboratorio) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.numeroSerie = numeroSerie;
        this.codigoInventario = codigoInventario;
        this.marca = marca;
        this.modelo = modelo;
        this.fechaAdquisicion = fechaAdquisicion;
        this.valorAdquisicion = valorAdquisicion;
        this.estado = EstadoEquipo.DISPONIBLE;
        this.categoria = categoria;
        this.laboratorio = laboratorio;
    }
    
    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    public String getNumeroSerie() { return numeroSerie; }
    public void setNumeroSerie(String numeroSerie) { this.numeroSerie = numeroSerie; }
    
    public String getCodigoInventario() { return codigoInventario; }
    public void setCodigoInventario(String codigoInventario) { this.codigoInventario = codigoInventario; }
    
    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }
    
    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }
    
    public Date getFechaAdquisicion() { return fechaAdquisicion; }
    public void setFechaAdquisicion(Date fechaAdquisicion) { this.fechaAdquisicion = fechaAdquisicion; }
    
    public Double getValorAdquisicion() { return valorAdquisicion; }
    public void setValorAdquisicion(Double valorAdquisicion) { this.valorAdquisicion = valorAdquisicion; }
    
    public EstadoEquipo getEstado() { return estado; }
    public void setEstado(EstadoEquipo estado) { this.estado = estado; }
    
    public CategoriaEquipo getCategoria() { return categoria; }
    public void setCategoria(CategoriaEquipo categoria) { this.categoria = categoria; }
    
    public Laboratorio getLaboratorio() { return laboratorio; }
    public void setLaboratorio(Laboratorio laboratorio) { this.laboratorio = laboratorio; }
    
    // Método para comprobar disponibilidad
    public boolean estaDisponible() {
        return this.estado == EstadoEquipo.DISPONIBLE;
    }
    
    // Método para cambiar estado a prestado
    public void prestar() {
        if (this.estado == EstadoEquipo.DISPONIBLE || this.estado == EstadoEquipo.RESERVADO) {
            this.estado = EstadoEquipo.PRESTADO;
        } else {
            throw new IllegalStateException("El equipo no está disponible para préstamo");
        }
    }
    
    // Método para cambiar estado a disponible
    public void devolver() {
        if (this.estado == EstadoEquipo.PRESTADO) {
            this.estado = EstadoEquipo.DISPONIBLE;
        } else {
            throw new IllegalStateException("El equipo no está en estado prestado");
        }
    }
    
    // Método para reservar un equipo
    public void reservar() {
        if (this.estado == EstadoEquipo.DISPONIBLE) {
            this.estado = EstadoEquipo.RESERVADO;
        } else {
            throw new IllegalStateException("El equipo no está disponible para reserva");
        }
    }
    
    // Método para mantenimiento
    public void mantenimiento() {
        this.estado = EstadoEquipo.EN_MANTENIMIENTO;
    }
    
    // Método para dar de baja
    public void darDeBaja() {
        this.estado = EstadoEquipo.DADO_DE_BAJA;
    }
}
