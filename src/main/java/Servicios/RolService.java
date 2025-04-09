/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servicios;

/**
 *
 * @author DOC
 */
import Clases.Rol;
import Clases.Permiso;
import java.util.List;
import java.util.Optional;

public class RolService {
    
    // Método para crear un nuevo rol
    public Rol crearRol(Rol rol) {
        // Validar datos del rol
        if (rol.getNombre() == null || rol.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El nombre del rol es obligatorio");
        }
        
        // Verificar si ya existe un rol con ese nombre
        if (buscarPorNombre(rol.getNombre()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un rol con ese nombre");
        }
        
        // Guardar rol
        // En una implementación real, se guardaría en la base de datos
        return rol;
    }
    
    // Método para buscar rol por nombre
    public Optional<Rol> buscarPorNombre(String nombre) {
        // Simulación de búsqueda en base de datos
        return Optional.empty();
    }
    
    // Método para listar todos los roles
    public List<Rol> listarTodos() {
        // En una implementación real, consultaría al repositorio
        return List.of();
    }
    
    // Método para asignar un permiso a un rol
    public Rol asignarPermiso(Long rolId, Long permisoId) {
        Rol rol = buscarPorId(rolId)
            .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado"));
            
        Permiso permiso = buscarPermisoPorId(permisoId)
            .orElseThrow(() -> new IllegalArgumentException("Permiso no encontrado"));
            
        rol.addPermiso(permiso);
        
        // Guardar cambios
        return actualizar(rol);
    }
    
    // Método para buscar rol por ID
    public Optional<Rol> buscarPorId(Long id) {
        // Simulación de búsqueda en base de datos
        return Optional.empty();
    }
    
    // Método para buscar permiso por ID
    public Optional<Permiso> buscarPermisoPorId(Long id) {
        // Simulación de búsqueda en base de datos
        return Optional.empty();
    }
    
    // Método para actualizar un rol
    public Rol actualizar(Rol rol) {
        // Validar que el rol existe
        if (rol.getId() == null || !buscarPorId(rol.getId()).isPresent()) {
            throw new IllegalArgumentException("Rol no encontrado");
        }
        
        // En una implementación real, guardaría los cambios en la base de datos
        return rol;
    }
    
    // Método para eliminar un rol
    public void eliminar(Long id) {
        // Validar que el rol existe y no está asignado a ningún usuario
        Rol rol = buscarPorId(id)
            .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado"));
            
        // Verificar que no esté en uso (esto requeriría consultar usuarios)
        if (estaEnUso(rol)) {
            throw new IllegalStateException("No se puede eliminar un rol que está asignado a usuarios");
        }
        
        // En una implementación real, eliminaría el rol de la base de datos
    }
    
    // Método para verificar si un rol está en uso
    private boolean estaEnUso(Rol rol) {
        // En una implementación real, consultaría si hay usuarios con este rol
        return false;
    }
}
