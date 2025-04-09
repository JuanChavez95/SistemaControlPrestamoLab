/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

/**
 *
 * @author Soldado Caído: Juan Carlos Chavez Machaca
 */

import java.util.Date;

public class Administrador extends Usuario {
    private double salario;
    private Date fechaInicio;
    private String nroTitulo;

    public Administrador(int ru, String nombre, String apellidoPaterno, String apellidoMaterno,
                         String contrasena, int ci, String rol, String correo,
                         double salario, Date fechaInicio, String nroTitulo) {
        super(ru, nombre, apellidoPaterno, apellidoMaterno, contrasena, ci, rol, correo);
        this.salario = salario;
        this.fechaInicio = fechaInicio;
        this.nroTitulo = nroTitulo;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getNroTitulo() {
        return nroTitulo;
    }

    public void setNroTitulo(String nroTitulo) {
        this.nroTitulo = nroTitulo;
    }
}
