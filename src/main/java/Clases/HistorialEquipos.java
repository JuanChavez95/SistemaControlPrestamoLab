/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

public class HistorialEquipos {
    // Atributos Privados
    private int idHistorial;
    private String idEquipos;

    // Constructor vacío
    public HistorialEquipos() {}

    // Constructor completo
    public HistorialEquipos(int idHistorial, String idEquipos) {
        this.idHistorial = idHistorial;
        this.idEquipos = idEquipos;
    }

    // Getters y Setters
    public int getIdHistorial() {
        return idHistorial;
    }
    public void setIdHistorial(int idHistorial) {
        this.idHistorial = idHistorial;
    }

    public String getIdEquipos() {
        return idEquipos;
    }
    public void setIdEquipos(String idEquipos) {
        this.idEquipos = idEquipos;
    }
}