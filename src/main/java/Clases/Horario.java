/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

public class Horario {
    private int idHorario;
    private String materia;
    private int paralelo;
    private String semestre;
    private String carrera;
    private String hora;
    private String dia;
    private int idLaboratorio;
    private String estado;

    // Constructor sin ID (para inserciones)
    public Horario(String materia, int paralelo, String semestre, String carrera, String hora, String dia, int idLaboratorio, String estado) {
        this.materia = materia;
        this.paralelo = paralelo;
        this.semestre = semestre;
        this.carrera = carrera;
        this.hora = hora;
        this.dia = dia;
        this.idLaboratorio = idLaboratorio;
        this.estado = estado;
    }

    // Constructor con ID (para lectura y edición de valores en la interfaz)
    public Horario(int idHorario, String materia, int paralelo, String semestre, String carrera, String hora, String dia, int idLaboratorio, String estado) {
        this.idHorario = idHorario;
        this.materia = materia;
        this.paralelo = paralelo;
        this.semestre = semestre;
        this.carrera = carrera;
        this.hora = hora;
        this.dia = dia;
        this.idLaboratorio = idLaboratorio;
        this.estado = estado;
    }

    // Getters y Setters
    public int getIdHorario() {
        return idHorario;
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    public int getParalelo() {
        return paralelo;
    }

    public void setParalelo(int paralelo) {
        this.paralelo = paralelo;
    }

    public String getSemestre() {
        return semestre;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public int getIdLaboratorio() {
        return idLaboratorio;
    }

    public void setIdLaboratorio(int idLaboratorio) {
        this.idLaboratorio = idLaboratorio;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
