package controlador;

import modelo.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Controlador principal para la gestión de citas
 * Coordina las operaciones entre la vista y el modelo
 */
public class CitaController {
    
    private RepositorioCitas repositorio;
    
    public CitaController() {
        this.repositorio = RepositorioCitas.obtenerInstancia();
    }
    
    /**
     * Agenda una nueva cita
     * HU-01: Agendar cita en línea
     * 
     * @return ResultadoOperacion con el código de la cita si fue exitoso
     */
    public ResultadoOperacion agendarCita(String servicio, String personal, 
                                         LocalDate fecha, LocalTime hora, 
                                         String motivo) {
        
        // 1. Validar campos obligatorios
        ValidadorCitas.TipoError error = ValidadorCitas.validarCitaCompleta(
            servicio, personal, fecha, hora, repositorio.obtenerTodas()
        );
        
        if (error != ValidadorCitas.TipoError.NINGUNO) {
            return new ResultadoOperacion(
                false, 
                null, 
                ValidadorCitas.obtenerMensajeError(error)
            );
        }
        
        // 2. Crear la cita
        Cita nuevaCita = new Cita(servicio, personal, fecha, hora, motivo);
        
        // 3. Guardar en el repositorio
        boolean guardado = repositorio.guardar(nuevaCita);
        
        if (guardado) {
            return new ResultadoOperacion(
                true,
                nuevaCita.getCodigo(),
                "Cita agendada exitosamente. Código: " + nuevaCita.getCodigo()
            );
        } else {
            return new ResultadoOperacion(
                false,
                null,
                "Error al guardar la cita. El horario podría estar ocupado."
            );
        }
    }
    
    /**
     * Obtiene los horarios ocupados para un personal en una fecha
     * HU-02: Consultar disponibilidad en tiempo real
     */
    public List<LocalTime> obtenerHorariosOcupados(String personal, LocalDate fecha) {
        return repositorio.obtenerHorariosOcupados(personal, fecha);
    }
    
    /**
     * Verifica si un horario específico está disponible
     * HU-02: Consultar disponibilidad en tiempo real
     */
    public boolean verificarDisponibilidad(String personal, LocalDate fecha, LocalTime hora) {
        return !repositorio.estaOcupado(personal, fecha, hora);
    }
    
    /**
     * Cancela una cita por código
     * HU-04: Cancelar cita
     */
    public ResultadoOperacion cancelarCita(String codigo) {
        // 1. Buscar la cita
        Cita cita = repositorio.buscarPorCodigo(codigo);
        
        if (cita == null) {
            return new ResultadoOperacion(
                false,
                null,
                "No se encontró la cita con el código: " + codigo
            );
        }
        
        // 2. Validar si puede cancelarse
        if (!cita.puedeCancelar()) {
            return new ResultadoOperacion(
                false,
                null,
                "No puede cancelar la cita. Debe hacerlo con al menos 2 horas de anticipación."
            );
        }
        
        // 3. Cancelar
        boolean cancelado = cita.cancelar();
        
        if (cancelado) {
            return new ResultadoOperacion(
                true,
                codigo,
                "Cita cancelada exitosamente."
            );
        } else {
            return new ResultadoOperacion(
                false,
                null,
                "Error al cancelar la cita."
            );
        }
    }
    
    /**
     * Obtiene todas las citas cancelables del sistema
     * HU-04: Cancelar cita
     */
    public List<Cita> obtenerCitasCancelables() {
        return repositorio.obtenerCancelables();
    }
    
    /**
     * Obtiene todas las citas pendientes
     */
    public List<Cita> obtenerCitasPendientes() {
        return repositorio.obtenerPendientes();
    }
    
    /**
     * Obtiene todas las citas
     */
    public List<Cita> obtenerTodasLasCitas() {
        return repositorio.obtenerTodas();
    }
    
    /**
     * Busca una cita por código
     */
    public Cita buscarCitaPorCodigo(String codigo) {
        return repositorio.buscarPorCodigo(codigo);
    }
    
    /**
     * Obtiene estadísticas de citas
     */
    public EstadisticasCitas obtenerEstadisticas() {
        long pendientes = repositorio.contarPorEstado(Cita.EstadoCita.PENDIENTE);
        long atendidas = repositorio.contarPorEstado(Cita.EstadoCita.ATENDIDA);
        long canceladas = repositorio.contarPorEstado(Cita.EstadoCita.CANCELADA);
        long noAsistio = repositorio.contarPorEstado(Cita.EstadoCita.NO_ASISTIO);
        
        return new EstadisticasCitas(pendientes, atendidas, canceladas, noAsistio);
    }
    
    /**
     * Clase interna para el resultado de operaciones
     */
    public static class ResultadoOperacion {
        private boolean exitoso;
        private String codigo;
        private String mensaje;
        
        public ResultadoOperacion(boolean exitoso, String codigo, String mensaje) {
            this.exitoso = exitoso;
            this.codigo = codigo;
            this.mensaje = mensaje;
        }
        
        public boolean isExitoso() {
            return exitoso;
        }
        
        public String getCodigo() {
            return codigo;
        }
        
        public String getMensaje() {
            return mensaje;
        }
    }
    
    /**
     * Clase interna para estadísticas
     */
    public static class EstadisticasCitas {
        private long pendientes;
        private long atendidas;
        private long canceladas;
        private long noAsistio;
        
        public EstadisticasCitas(long pendientes, long atendidas, long canceladas, long noAsistio) {
            this.pendientes = pendientes;
            this.atendidas = atendidas;
            this.canceladas = canceladas;
            this.noAsistio = noAsistio;
        }
        
        public long getPendientes() {
            return pendientes;
        }
        
        public long getAtendidas() {
            return atendidas;
        }
        
        public long getCanceladas() {
            return canceladas;
        }
        
        public long getNoAsistio() {
            return noAsistio;
        }
        
        public long getTotal() {
            return pendientes + atendidas + canceladas + noAsistio;
        }
    }
}