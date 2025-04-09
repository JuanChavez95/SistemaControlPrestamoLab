/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

/**
 *
 * @author DOC
 */
public class EquipoPrestamo {
    private Long id;
    private Equipo equipo;
    private String estadoInicial;
    private String estadoFinal;
    private String observaciones;
    
    // Constructor
    public EquipoPrestamo(Long id, Equipo equipo, String estadoInicial) {
        this.id = id;
        this.equipo = equipo;
        this.estadoInicial = estadoInicial;
    }
    
    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Equipo getEquipo() { return equipo; }
    public void setEquipo(Equipo equipo) { this.equipo = equipo; }
    
    public String getEstadoInicial() { return estadoInicial; }
    public void setEstadoInicial(String estadoInicial) { this.estadoInicial = estadoInicial; }
    
    public String getEstadoFinal() { return estadoFinal; }
    public void setEstadoFinal(String estadoFinal) { this.estadoFinal = estadoFinal; }
    
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}
