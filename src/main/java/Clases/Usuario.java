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
import java.util.Set;
import java.util.HashSet;

public class Usuario {
    private int ru;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String contrasena;
    private int ci;
    private String rol;
    private String correo;

    public Usuario(int ru, String nombre, String apellidoPaterno, String apellifoMaterno, String contrasena, int ci, String rol, String correo) {
        this.ru = ru;
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellifoMaterno;
        this.contrasena = contrasena;
        this.ci = ci;
        this.rol = rol;
        this.correo = correo;
    }

    public int getRu() {
        return ru;
    }

    public void setRu(int ru) {
        this.ru = ru;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellifoMaterno) {
        this.apellidoMaterno = apellifoMaterno;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public int getCi() {
        return ci;
    }

    public void setCi(int ci) {
        this.ci = ci;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
   
    
}
