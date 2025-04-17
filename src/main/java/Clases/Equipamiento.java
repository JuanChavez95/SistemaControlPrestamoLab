/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

/**
 * Clase que representa un equipamiento de laboratorio.
 * Autor: Equipo Soldados Caídos
 */
public class Equipamiento {
    private int idEquipamiento; // Para lectura y edición
    private String nombreEquipamiento;
    private String marca;
    private String modelo;
    private String numeroSerie;
    private String estado;
    private int idLaboratorio; // Referencia al laboratorio donde se encuentra

    // Constructor sin ID (para inserción de datos)
    public Equipamiento(String nombreEquipamiento, String marca, String modelo, String numeroSerie, 
                       String estado, int idLaboratorio) {
        this.nombreEquipamiento = nombreEquipamiento;
        this.marca = marca;
        this.modelo = modelo;
        this.numeroSerie = numeroSerie;
        this.estado = estado;
        this.idLaboratorio = idLaboratorio;
    }

    // Constructor con ID (para la lectura y actualización de datos)
    public Equipamiento(int idEquipamiento, String nombreEquipamiento, String marca, String modelo, 
                       String numeroSerie, String estado, int idLaboratorio) {
        this.idEquipamiento = idEquipamiento;
        this.nombreEquipamiento = nombreEquipamiento;
        this.marca = marca;
        this.modelo = modelo;
        this.numeroSerie = numeroSerie;
        this.estado = estado;
        this.idLaboratorio = idLaboratorio;
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

    public int getIdLaboratorio() {
        return idLaboratorio;
    }

    public void setIdLaboratorio(int idLaboratorio) {
        this.idLaboratorio = idLaboratorio;
    }
}
