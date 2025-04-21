/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

/*
 * Autor: Equipo Soldados Caídos
 */
public class Equipamiento {
    private int idEquipamiento; //Para lectura y edición
    private String nombreEquipamiento;
    private String marca;
    private String modelo;
    private String numeroSerie;
    private String estado;
    private Integer idLaboratorio; // Puede ser null
    private String disponibilidad;

    // Constructor sin ID (para inserción de datos)
    public Equipamiento(String nombreEquipamiento, String marca, String modelo, String numeroSerie, 
                        String estado, Integer idLaboratorio, String disponibilidad) {
        this.nombreEquipamiento = nombreEquipamiento;
        this.marca = marca;
        this.modelo = modelo;
        this.numeroSerie = numeroSerie;
        this.estado = estado;
        this.idLaboratorio = idLaboratorio;
        this.disponibilidad = disponibilidad;
    }

    // Constructor con ID (para lectura y actualización)
    public Equipamiento(int idEquipamiento, String nombreEquipamiento, String marca, String modelo,
                        String numeroSerie, String estado, Integer idLaboratorio, String disponibilidad) {
        this.idEquipamiento = idEquipamiento;
        this.nombreEquipamiento = nombreEquipamiento;
        this.marca = marca;
        this.modelo = modelo;
        this.numeroSerie = numeroSerie;
        this.estado = estado;
        this.idLaboratorio = idLaboratorio;
        this.disponibilidad = disponibilidad;
    }

    // Getters y setters
    public int getIdEquipamiento() {
        return idEquipamiento;
    }

    public String getNombreEquipamiento() {
        return nombreEquipamiento;
    }

    public void setNombreEquipamiento(String nombreEquipamiento) {
        this.nombreEquipamiento = nombreEquipamiento;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getNumeroSerie() {
        return numeroSerie;
    }

    public void setNumeroSerie(String numeroSerie) {
        this.numeroSerie = numeroSerie;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getIdLaboratorio() {
        return idLaboratorio;
    }

    public void setIdLaboratorio(Integer idLaboratorio) {
        this.idLaboratorio = idLaboratorio;
    }

    public String getDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(String disponibilidad) {
        this.disponibilidad = disponibilidad;
    }
}