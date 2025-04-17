/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

public class Equipos {
    //Atributos Privados del Sistema
    private String idEquipos;
    private String procesador;
    private String ram;
    private String dispositivo;
    private String monitor;
    private String teclado;
    private String mouse;
    private String estado; // Nuevo atributo para el estado
    private int idLaboratorio;

    public Equipos() {}

    public Equipos(String idEquipos, String procesador, String ram, String dispositivo,
                   String monitor, String teclado, String mouse, String estado, int idLaboratorio) {
        this.idEquipos = idEquipos;
        this.procesador = procesador;
        this.ram = ram;
        this.dispositivo = dispositivo;
        this.monitor = monitor;
        this.teclado = teclado;
        this.mouse = mouse;
        this.estado = estado;
        this.idLaboratorio = idLaboratorio;
    }

    // Getters y Setters
    public String getIdEquipos() { 
        return idEquipos; 
    }
    public void setIdEquipos(String idEquipos) {
        this.idEquipos = idEquipos; 
    }

    public String getProcesador() {
        return procesador; 
    }
    public void setProcesador(String procesador) {
        this.procesador = procesador; 
    }

    public String getRam() { 
        return ram; 
    }
    public void setRam(String ram) {
        this.ram = ram; 
    }

    public String getDispositivo() {
        return dispositivo; 
    }
    public void setDispositivo(String dispositivo) {
        this.dispositivo = dispositivo; 
    }

    public String getMonitor() {
        return monitor; 
    }
    public void setMonitor(String monitor) {
        this.monitor = monitor; 
    }

    public String getTeclado() {
        return teclado; 
    }
    public void setTeclado(String teclado) {
        this.teclado = teclado; 
    }

    public String getMouse() {
        return mouse; 
    }
    public void setMouse(String mouse) {
        this.mouse = mouse; 
    }
    
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getIdLaboratorio() {
        return idLaboratorio; 
    }
    public void setIdLaboratorio(int idLaboratorio) {
        this.idLaboratorio = idLaboratorio; 
    }
}