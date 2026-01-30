package modelo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Clase que representa una cita en el sistema
 * Historia de Usuario: HU-01 Agendar cita en línea
 */
public class Cita {
    
    private String codigo;
    private String servicio;
    private String personal;
    private LocalDate fecha;
    private LocalTime hora;
    private String motivo;
    private EstadoCita estado;
    private LocalDateTime fechaCreacion;
    
    // Constructor vacío
    public Cita() {
        this.estado = EstadoCita.PENDIENTE;
        this.fechaCreacion = LocalDateTime.now();
    }
    
    // Constructor completo
    public Cita(String servicio, String personal, LocalDate fecha, LocalTime hora, String motivo) {
        this.codigo = generarCodigo();
        this.servicio = servicio;
        this.personal = personal;
        this.fecha = fecha;
        this.hora = hora;
        this.motivo = motivo;
        this.estado = EstadoCita.PENDIENTE;
        this.fechaCreacion = LocalDateTime.now();
    }
    
    /**
     * Genera un código único para la cita
     * Formato: CITA-XXXXXXXX
     */
    private String generarCodigo() {
        return "CITA-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
    /**
     * Obtiene el datetime combinado de fecha y hora
     */
    public LocalDateTime getFechaHora() {
        if (fecha != null && hora != null) {
            return LocalDateTime.of(fecha, hora);
        }
        return null;
    }
    
    /**
     * Verifica si la cita puede ser cancelada
     * Regla: Al menos 2 horas de anticipación
     */
    public boolean puedeCancelar() {
        if (estado != EstadoCita.PENDIENTE) {
            return false;
        }
        
        LocalDateTime fechaHoraCita = getFechaHora();
        if (fechaHoraCita == null) {
            return false;
        }
        
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime limiteCancelacion = fechaHoraCita.minusHours(2);
        
        return ahora.isBefore(limiteCancelacion);
    }
    
    /**
     * Cancela la cita si cumple las reglas
     */
    public boolean cancelar() {
        if (puedeCancelar()) {
            this.estado = EstadoCita.CANCELADA;
            return true;
        }
        return false;
    }
    
    /**
     * Marca la cita como atendida
     */
    public void marcarAtendida() {
        this.estado = EstadoCita.ATENDIDA;
    }
    
    /**
     * Marca la cita como no asistió
     */
    public void marcarNoAsistio() {
        this.estado = EstadoCita.NO_ASISTIO;
    }
    
    // Getters y Setters
    public String getCodigo() {
        return codigo;
    }
    
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    
    public String getServicio() {
        return servicio;
    }
    
    public void setServicio(String servicio) {
        this.servicio = servicio;
    }
    
    public String getPersonal() {
        return personal;
    }
    
    public void setPersonal(String personal) {
        this.personal = personal;
    }
    
    public LocalDate getFecha() {
        return fecha;
    }
    
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
    
    public LocalTime getHora() {
        return hora;
    }
    
    public void setHora(LocalTime hora) {
        this.hora = hora;
    }
    
    public String getMotivo() {
        return motivo;
    }
    
    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
    
    public EstadoCita getEstado() {
        return estado;
    }
    
    public void setEstado(EstadoCita estado) {
        this.estado = estado;
    }
    
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    
    @Override
    public String toString() {
        return "Cita{" +
                "codigo='" + codigo + '\'' +
                ", servicio='" + servicio + '\'' +
                ", personal='" + personal + '\'' +
                ", fecha=" + fecha +
                ", hora=" + hora +
                ", estado=" + estado +
                '}';
    }
    
    /**
     * Enum para los estados posibles de una cita
     */
    public enum EstadoCita {
        PENDIENTE("Pendiente"),
        ATENDIDA("Atendida"),
        CANCELADA("Cancelada"),
        NO_ASISTIO("No Asistió");
        
        private final String descripcion;
        
        EstadoCita(String descripcion) {
            this.descripcion = descripcion;
        }
        
        public String getDescripcion() {
            return descripcion;
        }
    }
}