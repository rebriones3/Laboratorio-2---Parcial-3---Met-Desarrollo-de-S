package controlador;

import modelo.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Controlador principal para la gestión de citas
 * Coordina las operaciones entre la vista y el modelo
 * MODIFICADO: Ahora usa RepositorioCitasMongo en lugar de RepositorioCitas
 */
public class CitaController {
    
    private RepositorioCitasMongo repositorio;
    
    public CitaController() {
        // CAMBIO IMPORTANTE: Usar MongoDB en lugar de repositorio en memoria
        this.repositorio = RepositorioCitasMongo.obtenerInstancia();
        System.out.println("✅ CitaController inicializado con MongoDB");
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
        
        System.out.println("📝 Intentando agendar cita:");
        System.out.println("   - Servicio: " + servicio);
        System.out.println("   - Personal: " + personal);
        System.out.println("   - Fecha: " + fecha);
        System.out.println("   - Hora: " + hora);
        
        // 1. Validar campos obligatorios
        ValidadorCitas.TipoError error = ValidadorCitas.validarCitaCompleta(
            servicio, personal, fecha, hora, repositorio.obtenerTodas()
        );
        
        if (error != ValidadorCitas.TipoError.NINGUNO) {
            String mensaje = ValidadorCitas.obtenerMensajeError(error);
            System.err.println("❌ Validación fallida: " + mensaje);
            return new ResultadoOperacion(false, null, mensaje);
        }
        
        // 2. Crear la cita
        Cita nuevaCita = new Cita(servicio, personal, fecha, hora, motivo);
        System.out.println("🆕 Cita creada con código: " + nuevaCita.getCodigo());
        
        // 3. Guardar en MongoDB
        boolean guardado = repositorio.guardar(nuevaCita);
        
        if (guardado) {
            System.out.println("✅ Cita guardada exitosamente en MongoDB");
            return new ResultadoOperacion(
                true,
                nuevaCita.getCodigo(),
                "Cita agendada exitosamente. Código: " + nuevaCita.getCodigo()
            );
        } else {
            System.err.println("❌ Error al guardar en MongoDB");
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
        List<LocalTime> horarios = repositorio.obtenerHorariosOcupados(personal, fecha);
        System.out.println("🕐 Horarios ocupados para " + personal + " el " + fecha + ": " + horarios.size());
        return horarios;
    }
    
    /**
     * Verifica si un horario específico está disponible
     * HU-02: Consultar disponibilidad en tiempo real
     */
    public boolean verificarDisponibilidad(String personal, LocalDate fecha, LocalTime hora) {
        boolean disponible = !repositorio.estaOcupado(personal, fecha, hora);
        System.out.println("🔍 Disponibilidad " + personal + " " + fecha + " " + hora + ": " + 
                          (disponible ? "DISPONIBLE" : "OCUPADO"));
        return disponible;
    }
    
    /**
     * Cancela una cita por código
     * HU-04: Cancelar cita
     */
    public ResultadoOperacion cancelarCita(String codigo) {
        System.out.println("🗑️ Intentando cancelar cita: " + codigo);
        
        // 1. Buscar la cita
        Cita cita = repositorio.buscarPorCodigo(codigo);
        
        if (cita == null) {
            System.err.println("❌ Cita no encontrada: " + codigo);
            return new ResultadoOperacion(
                false,
                null,
                "No se encontró la cita con el código: " + codigo
            );
        }
        
        // 2. Validar si puede cancelarse
        if (!cita.puedeCancelar()) {
            System.err.println("❌ Cita no puede cancelarse (menos de 2 horas)");
            return new ResultadoOperacion(
                false,
                null,
                "No puede cancelar la cita. Debe hacerlo con al menos 2 horas de anticipación."
            );
        }
        
        // 3. Cancelar usando el repositorio MongoDB
        boolean cancelado = repositorio.cancelarCita(codigo);
        
        if (cancelado) {
            System.out.println("✅ Cita cancelada exitosamente");
            return new ResultadoOperacion(
                true,
                codigo,
                "Cita cancelada exitosamente."
            );
        } else {
            System.err.println("❌ Error al cancelar la cita");
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
        List<Cita> cancelables = repositorio.obtenerCancelables();
        System.out.println("📋 Citas cancelables: " + cancelables.size());
        return cancelables;
    }
    
    /**
     * Obtiene todas las citas pendientes
     */
    public List<Cita> obtenerCitasPendientes() {
        List<Cita> pendientes = repositorio.obtenerPendientes();
        System.out.println("⏳ Citas pendientes: " + pendientes.size());
        return pendientes;
    }
    
    /**
     * Obtiene todas las citas
     */
    public List<Cita> obtenerTodasLasCitas() {
        List<Cita> todas = repositorio.obtenerTodas();
        System.out.println("📊 Total de citas: " + todas.size());
        return todas;
    }
    
    /**
     * Busca una cita por código
     */
    public Cita buscarCitaPorCodigo(String codigo) {
        Cita cita = repositorio.buscarPorCodigo(codigo);
        System.out.println("🔎 Búsqueda por código " + codigo + ": " + 
                          (cita != null ? "ENCONTRADA" : "NO ENCONTRADA"));
        return cita;
    }
    
    /**
     * Obtiene estadísticas de citas
     */
    public EstadisticasCitas obtenerEstadisticas() {
        long pendientes = repositorio.contarPorEstado(Cita.EstadoCita.PENDIENTE);
        long atendidas = repositorio.contarPorEstado(Cita.EstadoCita.ATENDIDA);
        long canceladas = repositorio.contarPorEstado(Cita.EstadoCita.CANCELADA);
        long noAsistio = repositorio.contarPorEstado(Cita.EstadoCita.NO_ASISTIO);
        
        System.out.println("📈 Estadísticas - P:" + pendientes + " A:" + atendidas + 
                          " C:" + canceladas + " N:" + noAsistio);
        
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