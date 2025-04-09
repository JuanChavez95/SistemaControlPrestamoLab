/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

/**
 *
 * @author DOC
 */
import java.util.List;
import java.util.ArrayList;

public class Laboratorio {
    private Long id;
    private String nombre;
    private String ubicacion;
    private String descripcion;
    private int capacidad;
    private boolean activo;
    private List<Equipo> equipos = new ArrayList<>();
    private List<Horario> horarios = new ArrayList<>();
    
    // Constructor
    public Laboratorio(Long id, String nombre, String ubicacion, String descripcion, int capacidad) {
        this.id = id;
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.descripcion = descripcion;
        this.capacidad = capacidad;
        this.activo = true;
    }
    
    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    public int getCapacidad() { return capacidad; }
    public void setCapacidad(int capacidad) { this.capacidad = capacidad; }
    
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
    
    public List<Equipo> getEquipos() { return equipos; }
    public void setEquipos(List<Equipo> equipos) { this.equipos = equipos; }
    
    public List<Horario> getHorarios() { return horarios; }
    public void setHorarios(List<Horario> horarios) { this.horarios = horarios; }
    
    public void addEquipo(Equipo equipo) {
        this.equipos.add(equipo);
        equipo.setLaboratorio(this);
    }
    
    public void removeEquipo(Equipo equipo) {
        this.equipos.remove(equipo);
        equipo.setLaboratorio(null);
    }
    
    public void addHorario(Horario horario) {
        this.horarios.add(horario);
        horario.setLaboratorio(this);
    }
    
    public void removeHorario(Horario horario) {
        this.horarios.remove(horario);
        horario.setLaboratorio(null);
    }
}