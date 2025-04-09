/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servicios;

/**
 *
 * @author DOC
 */
public class HorarioService{
    
}
/*
import Clases.Horario;
import Clases.Laboratorio;
import Clases.Usuario;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public class HorarioService {
    
    private final LaboratorioService laboratorioService;
    private final UsuarioService usuarioService;
    
    // Constructor con inyección de dependencias
    public HorarioService(LaboratorioService laboratorioService, UsuarioService usuarioService) {
        this.laboratorioService = laboratorioService;
        this.usuarioService = usuarioService;
    }
    
    // Método para crear un nuevo horario
    public Horario crearHorario(Long laboratorioId, DayOfWeek dia, LocalTime horaInicio, 
                             LocalTime horaFin, String materia, String paralelo, Long docenteId) {
        // Validar datos
        if (horaInicio.isAfter(horaFin) || horaInicio.equals(horaFin)) {
            throw new IllegalArgumentException("La hora de inicio debe ser anterior a la hora de fin");
        }
        
        // Obtener laboratorio
        Laboratorio laboratorio = laboratorioService.buscarPorId(laboratorioId)
            .orElseThrow(() -> new IllegalArgumentException("Laboratorio no encontrado"));
            
        // Verificar que el laboratorio esté activo
        if (!laboratorio.isActivo()) {
            throw new IllegalStateException("El laboratorio no está activo");
        }
        
        // Obtener docente
        Usuario docente = usuarioService.buscarPorId(docenteId)
            .orElseThrow(() -> new IllegalArgumentException("Docente no encontrado"));
            
        // Verificar que el docente tenga rol de docente
        if (!docente.hasRol("DOCENTE")) {
            throw new IllegalArgumentException("El usuario no tiene rol de docente");
        }
        
        // Crear horario
        Horario horario = new Horario(generarId(), laboratorio, dia, horaInicio, horaFin, 
                                    materia, paralelo, docente);
        
        // Verificar colisiones
        for (Horario existente : laboratorio.getHorarios()) {
            if (existente.isActivo() && existente.getDia() == dia && horario.colisionaCon(existente)) {
                throw new IllegalStateException("El horario colisiona con otro existente");
            }
        }
        
        // Añadir al laboratorio y guardar
        laboratorio.addHorario(horario);
        laboratorioService.actualizar(laboratorio);
        
        return horario;
    }
    
    // Método para buscar horarios por laboratorio
    public List<Horario> buscarPorLaboratorio(Long laboratorioId) {
        // En una implementación real, consultaría al repositorio
        return List.of();
    }
    
    // Método para buscar horarios por docente
    public List<Horario> buscarPorDocente(Long docenteId) {
        // En una implementación real, consultaría al repositorio
        return List.of();
    }
    
    // Método para buscar horarios por día
    public List<Horario> buscarPorDia(DayOfWeek dia) {
        // En una implementación real, consultaría al repositorio
        return List.of();
    }
    
    // Método para verificar disponibilidad de laboratorio
    public boolean laboratorioDisponible(Long laboratorioId, DayOfWeek dia, 
                                        LocalTime horaInicio, LocalTime horaFin) {
        // Obtener laboratorio
        Optional<Laboratorio> labOpt = laboratorioService.buscarPorId(laboratorioId);
        
        if (!labOpt.isPresent() || !labOpt.get().isActivo()) {
            return false;
        }
        
        // Verificar colisiones con horarios existentes
        Laboratorio lab = labOpt.get();
        for (Horario horario : lab.getHorarios()) {
            if (horario.isActivo() && horario.getDia() == dia) {
                if (horaInicio.isBefore(horario.getHoraFin()) && 
                    horaFin.isAfter(horario.getHoraInicio())) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    // Método para cancelar un horario
    public Horario cancelarHorario(Long horarioId) {
        // Buscar horario
        Horario horario = buscarPorId(horarioId)
            .orElseThrow(() -> new IllegalArgumentException("Horario no encontrado"));
            
        // Desactivar
        horario.setActivo(false);
        
        // Actualizar laboratorio
        Laboratorio laboratorio = horario.getLaboratorio();
        laboratorioService.actualizar(laboratorio);
        
        return horario;
    }
    
    // Método para buscar horario por ID
    public Optional<Horario> buscarPorId(Long id) {
        // Simulación de búsqueda en base de datos
        return Optional.empty();
    }
    
    // Generador simple de IDs
    private Long generarId() {
        // En una implementación real, esto sería manejado por la base de datos
        return System.currentTimeMillis();
    }
}
*/