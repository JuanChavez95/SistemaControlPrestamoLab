/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servicios;

/**
 *
 * @author DOC
 */
public class PrestamoService{
    
}
/*
import Clases.Prestamo;
import Clases.EquipoPrestamo;
import Clases.Equipo;
import Clases.Usuario;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class PrestamoService {
    
    private final EquipoService equipoService;
    private final UsuarioService usuarioService;
    
    // Constructor con inyección de dependencias
    public PrestamoService(EquipoService equipoService, UsuarioService usuarioService) {
        this.equipoService = equipoService;
        this.usuarioService = usuarioService;
    }
    
    // Método para solicitar un préstamo
    public Prestamo solicitarPrestamo(Long solicitanteId, Date fechaInicio, Date fechaFin, List<Long> equiposIds) {
        // Validar fechas
        Date ahora = new Date();
        if (fechaInicio.before(ahora)) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser anterior a la fecha actual");
        }
        
        if (fechaFin.before(fechaInicio)) {
            throw new IllegalArgumentException("La fecha fin no puede ser anterior a la fecha inicio");
        }
        
        // Obtener solicitante
        Usuario solicitante = usuarioService.buscarPorId(solicitanteId)
            .orElseThrow(() -> new IllegalArgumentException("Solicitante no encontrado"));
            
        // Verificar que el solicitante tenga permisos para solicitar préstamos
        if (!solicitante.isActivo() || !usuarioService.tienePermiso(solicitanteId, "SOLICITAR_PRESTAMO")) {
            throw new IllegalStateException("El usuario no tiene permisos para solicitar préstamos");
        }
        
        // Crear préstamo
        Prestamo prestamo = new Prestamo(generarId(), solicitante, fechaInicio, fechaFin);
        
        // Añadir equipos al préstamo
        for (Long equipoId : equiposIds) {
            Equipo equipo = equipoService.buscarPorId(equipoId)
                .orElseThrow(() -> new IllegalArgumentException("Equipo no encontrado: " + equipoId));
                
            // Verificar disponibilidad del equipo
            if (!equipo.estaDisponible()) {
                throw new IllegalStateException("El equipo no está disponible: " + equipo.getNombre());
            }
            
            // Verificar tiempo máximo de préstamo
            int horasPrestamo = calcularHorasPrestamo(fechaInicio, fechaFin);
            if (horasPrestamo > equipo.getCategoria().getTiempoMaximoPrestamo()) {
                throw new IllegalArgumentException(
                    "El tiempo de préstamo excede el máximo permitido para: " + equipo.getNombre());
            }
            
            // Reservar el equipo
            equipo.reservar();
            equipoService.actualizar(equipo);
            
            // Añadir al préstamo
            EquipoPrestamo equipoPrestamo = new EquipoPrestamo(generarId(), equipo, "BUENO");
            prestamo.addEquipo(equipoPrestamo);
        }
        
        // Guardar préstamo
        // En una implementación real, se guardaría en la base de datos
        return prestamo;
    }
    
    // Método para aprobar un préstamo
    public Prestamo aprobarPrestamo(Long prestamoId, Long autorizadorId) {
        // Obtener préstamo
        Prestamo prestamo = buscarPorId(prestamoId)
            .orElseThrow(() -> new IllegalArgumentException("Préstamo no encontrado"));
            
        // Verificar que esté en estado SOLICITADO
        if (prestamo.getEstado() != Prestamo.EstadoPrestamo.SOLICITADO) {
            throw new IllegalStateException("El préstamo no está en estado SOLICITADO");
        }
        
        // Obtener autorizador
        Usuario autorizador = usuarioService.buscarPorId(autorizadorId)
            .orElseThrow(() -> new IllegalArgumentException("Autorizador no encontrado"));
            
        // Verificar que el autorizador tenga permisos
        if (!autorizador.isActivo() || !usuarioService.tienePermiso(autorizadorId, "APROBAR_PRESTAMO")) {
            throw new IllegalStateException("El usuario no tiene permisos para aprobar préstamos");
        }
        
        // Aprobar préstamo
        prestamo.aprobar(autorizador);
        
        // Guardar cambios
        // En una implementación real, se actualizaría en la base de datos
        return prestamo;
    }
    
    // Método para iniciar un préstamo (entrega de equipos)
    public Prestamo iniciarPrestamo(Long prestamoId) {
        // Obtener préstamo
        Prestamo prestamo = buscarPorId(prestamoId)
            .orElseThrow(() -> new IllegalArgumentException("Préstamo no encontrado"));
            
        // Verificar que esté en estado APROBADO
        if (prestamo.getEstado() != Prestamo.EstadoPrestamo.APROBADO) {
            throw new IllegalStateException("El préstamo no está en estado APROBADO");
        }
        
        // Verificar fecha
        Date ahora = new Date();
        if (prestamo.getFechaInicioPrestamo().after(ahora)) {
            throw new IllegalStateException("Aún no es la fecha de inicio del préstamo");
        }
        
        // Cambiar estado de los equipos a PRESTADO
        for (EquipoPrestamo equipoPrestamo : prestamo.getEquipos()) {
            Equipo equipo = equipoPrestamo.getEquipo();
            equipo.prestar();
            equipoService.actualizar(equipo);
        }
        
        // Iniciar préstamo
        prestamo.iniciar();
        
        // Guardar cambios
        // En una implementación real, se actualizaría en la base de datos
        return prestamo;
    }
    
    // Método para devolver un préstamo
    public Prestamo devolverPrestamo(Long prestamoId, List<EquipoDevolucion> equiposDevoluciones) {
        // Obtener préstamo
        Prestamo prestamo = buscarPorId(prestamoId)
            .orElseThrow(() -> new IllegalArgumentException("Préstamo no encontrado"));
            
        // Verificar que esté en estado EN_CURSO
        if (prestamo.getEstado() != Prestamo.EstadoPrestamo.EN_CURSO) {
            throw new IllegalStateException("El préstamo no está en curso");
        }
        
        // Actualizar estado de los equipos
        StringBuilder observaciones = new StringBuilder();
        
        for (EquipoDevolucion devolucion : equiposDevoluciones) {
            // Buscar el equipo en el préstamo
            EquipoPrestamo equipoPrestamo = null;
            for (EquipoPrestamo ep : prestamo.getEquipos()) {
                if (ep.getEquipo().getId().equals(devolucion.getEquipoId())) {
                    equipoPrestamo = ep;
                    break;
                }
            }
            
            if (equipoPrestamo == null) {
                throw new IllegalArgumentException("El equipo no forma parte del préstamo: " + devolucion.getEquipoId());
            }
            
            // Actualizar estado final y observaciones
            equipoPrestamo.setEstadoFinal(devolucion.getEstadoFinal());
            equipoPrestamo.setObservaciones(devolucion.getObservaciones());
            
            // Cambiar estado del equipo según el estado final
            Equipo equipo = equipoPrestamo.getEquipo();
            if ("DAÑADO".equals(devolucion.getEstadoFinal()) || "DEFECTUOSO".equals(devolucion.getEstadoFinal())) {
                equipo.mantenimiento();
                observaciones.append("Equipo ").append(equipo.getNombre())
                    .append(" devuelto en estado ").append(devolucion.getEstadoFinal()).append(". ");
            } else {
                equipo.devolver();
            }
            
            equipoService.actualizar(equipo);
        }
        
        // Registrar devolución
        prestamo.devolver(observaciones.toString());
        
        // Guardar cambios
        // En una implementación real, se actualizaría en la base de datos
        return prestamo;
    }
    
    // Clase auxiliar para la devolución de equipos
    public static class EquipoDevolucion {
        private Long equipoId;
        private String estadoFinal;
        private String observaciones;
        
        public EquipoDevolucion(Long equipoId, String estadoFinal, String observaciones) {
            this.equipoId = equipoId;
            this.estadoFinal = estadoFinal;
            this.observaciones = observaciones;
        }
        
        public Long getEquipoId() { return equipoId; }
        public String getEstadoFinal() { return estadoFinal; }
        public String getObservaciones() { return observaciones; }
    }
    
    // Método para buscar préstamo por ID
    public Optional<Prestamo> buscarPorId(Long id) {
        // Simulación de búsqueda en base de datos
        return Optional.empty();
    }
    
    // Método para listar préstamos por solicitante
    public List<Prestamo> listarPorSolicitante(Long solicitanteId) {
        // En una implementación real, consultaría al repositorio
        return List.of();
    }
    
    // Método para listar préstamos por estado
    public List<Prestamo> listarPorEstado(Prestamo.EstadoPrestamo estado) {
        // En una implementación real, consultaría al repositorio
        return List.of();
    }
    
    // Método para calcular horas entre dos fechas
    private int calcularHorasPrestamo(Date inicio, Date fin) {
        long diff = fin.getTime() - inicio.getTime();
        return (int) (diff / (60 * 60 * 1000));
    }
    
    // Generador simple de IDs
    private Long generarId() {
        // En una implementación real, esto sería manejado por la base de datos
        return System.currentTimeMillis();
    }
}
*/