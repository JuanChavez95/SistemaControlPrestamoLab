/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

/**
 *
 * @author DOC
 */
import java.time.DayOfWeek;
import java.time.LocalTime;

public class Horario {
    private Long id;
    private Laboratorio laboratorio;
    private DayOfWeek dia;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private String materia;
    private String paralelo;
    private Usuario docente;
    private boolean activo;
    
    // Constructor
    public Horario(Long id, Laboratorio laboratorio, DayOfWeek dia, LocalTime horaInicio, 
                  LocalTime horaFin, String materia, String paralelo, Usuario docente) {
        this.id = id;
        this.laboratorio = laboratorio;
        this.dia = dia;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.materia = materia;
        this.paralelo = paralelo;
        this.docente = docente;
        this.activo = true;
    }
    
    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Laboratorio getLaboratorio() { return laboratorio; }
    public void setLaboratorio(Laboratorio laboratorio) { this.laboratorio = laboratorio; }
    
    public DayOfWeek getDia() { return dia; }
    public void setDia(DayOfWeek dia) { this.dia = dia; }
    
    public LocalTime getHoraInicio() { return horaInicio; }
    public void setHoraInicio(LocalTime horaInicio) { this.horaInicio = horaInicio; }
    
    public LocalTime getHoraFin() { return horaFin; }
    public void setHoraFin(LocalTime horaFin) { this.horaFin = horaFin; }
    
    public String getMateria() { return materia; }
    public void setMateria(String materia) { this.materia = materia; }
    
    public String getParalelo() { return paralelo; }
    public void setParalelo(String paralelo) { this.paralelo = paralelo; }
    
    public Usuario getDocente() { return docente; }
    public void setDocente(Usuario docente) { this.docente = docente; }
    
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
    
    // Método para verificar si hay colisión con otro horario
    public boolean colisionaCon(Horario otro) {
        if (!this.dia.equals(otro.dia)) {
            return false;
        }
        
        return (this.horaInicio.isBefore(otro.horaFin) && this.horaFin.isAfter(otro.horaInicio));
    }
}