/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servicios;

/**
 *
 * @author DOC
 */
import Clases.Laboratorio;
import java.util.List;
import java.util.Optional;

public class LaboratorioService {
    
    // Método para crear un nuevo laboratorio
    public Laboratorio crearLaboratorio(Laboratorio laboratorio) {
        // Validar datos del laboratorio
        if (laboratorio.getNombre() == null || laboratorio.getUbicacion() == null) {
            throw new IllegalArgumentException("Nombre y ubicación son obligatorios");
        }
        
        // Verificar que no exista otro laboratorio con el mismo nombre
        if (buscarPorNombre(laboratorio.getNombre()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un laboratorio con ese nombre");
        }
        
        // Guardar laboratorio
        // En una implementación real, se guardaría en la base de datos
        return laboratorio;
    }
    
    // Método para buscar por nombre
    public Optional<Laboratorio> buscarPorNombre(String nombre) {
        // Simulación de búsqueda en base de datos
        return Optional.empty();
    }
    
    // Método para buscar por ID
    public Optional<Laboratorio> buscarPorId(Long id) {
        // Simulación de búsqueda en base de datos
        return Optional.empty();
    }
    
    // Método para listar todos los laboratorios
    public List<Laboratorio> listarTodos() {
        // En una implementación real, consultaría al repositorio
        return List.of();
    }
    
    // Método para listar laboratorios activos
    public List<Laboratorio> listarActivos() {
        // En una implementación real, consultaría al repositorio
        return List.of();
    }
    
    // Método para actualizar un laboratorio
    public Laboratorio actualizar(Laboratorio laboratorio) {
        // Validar que el laboratorio existe
        if (laboratorio.getId() == null || !buscarPorId(laboratorio.getId()).isPresent()) {
            throw new IllegalArgumentException("Laboratorio no encontrado");
        }
        
        // En una implementación real, guardaría los cambios en la base de datos
        return laboratorio;
    }
    
    // Método para cambiar estado de un laboratorio
    public Laboratorio cambiarEstado(Long id, boolean activo) {
        // Buscar laboratorio
        Laboratorio laboratorio = buscarPorId(id)
            .orElseThrow(() -> new IllegalArgumentException("Laboratorio no encontrado"));
            
        // Cambiar estado
        laboratorio.setActivo(activo);
        
        // Guardar cambios
        return actualizar(laboratorio);
    }
}
