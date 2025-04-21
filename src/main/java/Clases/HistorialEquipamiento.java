/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

public class HistorialEquipamiento {
    // Atributos Privados
    private int idHistorial;
    private int idEquipamiento;

    // Constructor vacío
    public HistorialEquipamiento() {}

    // Constructor completo
    public HistorialEquipamiento(int idHistorial, int idEquipamiento) {
        this.idHistorial = idHistorial;
        this.idEquipamiento = idEquipamiento;
    }

    // Getters y Setters
    public int getIdHistorial() {
        return idHistorial;
    }
    public void setIdHistorial(int idHistorial) {
        this.idHistorial = idHistorial;
    }

    public int getIdEquipamiento() {
        return idEquipamiento;
    }
    public void setIdEquipamiento(int idEquipamiento) {
        this.idEquipamiento = idEquipamiento;
    }
}
