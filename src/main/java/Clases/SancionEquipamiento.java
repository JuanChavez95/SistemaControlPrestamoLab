/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

/**
 * Clase que representa una sanción relacionada con equipamiento.
 * Autor: Equipo Soldados Caídos
 */
public class SancionEquipamiento {
    private int idSancion;
    private int idEquipamiento;

    // Constructor para inserción y lectura
    public SancionEquipamiento(int idSancion, int idEquipamiento) {
        this.idSancion = idSancion;
        this.idEquipamiento = idEquipamiento;
    }

    public int getIdSancion() {
        return idSancion;
    }

    public void setIdSancion(int idSancion) {
        this.idSancion = idSancion;
    }

    public int getIdEquipamiento() {
        return idEquipamiento;
    }

    public void setIdEquipamiento(int idEquipamiento) {
        this.idEquipamiento = idEquipamiento;
    }
}