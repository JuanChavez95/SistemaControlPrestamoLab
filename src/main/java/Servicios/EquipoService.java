/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servicios;

/**
 *
 * @author DOC
 */
import Clases.Equipo;
import Clases.CategoriaEquipo;
import Clases.Laboratorio;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class EquipoService {
    
    // Método para registrar un nuevo equipo
    public Equipo registrarEquipo(Equipo equipo) {
        // Validar datos del equipo
        if (equipo.getNombre() == null || equipo.getCodigoInventario() == null) {
            throw new IllegalArgumentException("Nombre y código de inventario son obligatorios");
        }
        
        // Verificar que el código de inventario sea único
        if (buscarPorCodigoInventario(equipo.getCodigoInventario()).isPresent()) {
            throw new IllegalArgumentException("El código de inventario ya existe");
        }
        
        // Guardar equipo
        // En una implementación real, se guardaría en la base de datos
        return equipo;
    }
    
    // Método para buscar por código de inventario
    public Optional<Equipo> buscarPorCodigoInventario(String codigoInventario) {
        // Simulación de búsqueda en base de datos
        return Optional.empty();
    }
    
    // Método para buscar por ID
    public Optional<Equipo> buscarPorId(Long id) {
        // Simulación de búsqueda en base de datos
        return Optional.empty();
    }
    
    // Método para listar equipos por laboratorio
    public List<Equipo> listarPorLaboratorio(Long laboratorioId) {
        // En una implementación real, consultaría al repositorio
        return List.of();
    }
    
    // Método para listar equipos por categoría
    public List<Equipo> listarPorCategoria(Long categoriaId) {
        // En una implementación real, consultaría al repositorio
        return List.of();
    }
    
    // Método para listar equipos por estado
    public List<Equipo> listarPorEstado(Equipo.EstadoEquipo estado) {
        // En una implementación real, consultaría al repositorio
        return List.of();
    }
    
    // Método para actualizar un equipo
    public Equipo actualizar(Equipo equipo) {
        // Validar que el equipo existe
        if (equipo.getId() == null || !buscarPorId(equipo.getId()).isPresent()) {
            throw new IllegalArgumentException("Equipo no encontrado");
        }
        
        // En una implementación real, guardaría los cambios en la base de datos
        return equipo;
    }
    
    // Método para dar de baja un equipo
    public Equipo darDeBaja(Long equipoId, String motivo) {
        // Buscar equipo
        Equipo equipo = buscarPorId(equipoId)
            .orElseThrow(() -> new IllegalArgumentException("Equipo no encontrado"));
            
        // Verificar que no esté prestado
        if (equipo.getEstado() == Equipo.EstadoEquipo.PRESTADO) {
            throw new IllegalStateException("No se puede dar de baja un equipo prestado");
        }
        
        // Dar de baja
        equipo.darDeBaja();
        
        // Guardar cambios
        return actualizar(equipo);
    }
    
    // Método para realizar mantenimiento de un equipo
    public Equipo enviarAMantenimiento(Long equipoId) {
        // Buscar equipo
        Equipo equipo = buscarPorId(equipoId)
            .orElseThrow(() -> new IllegalArgumentException("Equipo no encontrado"));
            
        // Verificar que no esté prestado
        if (equipo.getEstado() == Equipo.EstadoEquipo.PRESTADO) {
            throw new IllegalStateException("No se puede enviar a mantenimiento un equipo prestado");
        }
        
        // Cambiar estado
        equipo.mantenimiento();
        
        // Guardar cambios
        return actualizar(equipo);
    }
    
    // Método para finalizar mantenimiento
    public Equipo finalizarMantenimiento(Long equipoId) {
        // Buscar equipo
        Equipo equipo = buscarPorId(equipoId)
            .orElseThrow(() -> new IllegalArgumentException("Equipo no encontrado"));
            
        // Verificar que esté en mantenimiento
        if (equipo.getEstado() != Equipo.EstadoEquipo.EN_MANTENIMIENTO) {
            throw new IllegalStateException("El equipo no está en mantenimiento");
        }
        
        // Cambiar estado
        equipo.setEstado(Equipo.EstadoEquipo.DISPONIBLE);
        
        // Guardar cambios
        return actualizar(equipo);
    }
}