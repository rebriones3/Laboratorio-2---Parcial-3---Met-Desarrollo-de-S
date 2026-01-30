package modelo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Clase que contiene las reglas de negocio para validación de citas
 * Implementa los criterios de aceptación de las historias de usuario
 */
public class ValidadorCitas {
    
    /**
     * Valida que todos los campos obligatorios estén completos
     * Criterio HU-01: Validación de campos obligatorios
     */
    public static boolean validarCamposObligatorios(String servicio, String personal, 
                                                     LocalDate fecha, LocalTime hora) {
        return servicio != null && !servicio.trim().isEmpty() &&
               personal != null && !personal.trim().isEmpty() &&
               fecha != null &&
               hora != null;
    }
    
    /**
     * Valida que la fecha no sea pasada
     * Criterio HU-01: No permite seleccionar horarios pasados
     */
    public static boolean validarFechaNoEnPasado(LocalDate fecha) {
        if (fecha == null) {
            return false;
        }
        return !fecha.isBefore(LocalDate.now());
    }
    
    /**
     * Valida que el horario no esté en el pasado
     * Criterio HU-01: No permite seleccionar horarios pasados
     */
    public static boolean validarHorarioNoEnPasado(LocalDate fecha, LocalTime hora) {
        if (fecha == null || hora == null) {
            return false;
        }
        
        LocalDateTime fechaHoraCita = LocalDateTime.of(fecha, hora);
        LocalDateTime ahora = LocalDateTime.now();
        
        return fechaHoraCita.isAfter(ahora);
    }
    
    /**
     * Valida que no haya solapamiento de horarios
     * Criterio HU-11: Evitar solapamiento de citas
     */
    public static boolean validarNoSolapamiento(String personal, LocalDate fecha, 
                                                LocalTime hora, List<Cita> citasExistentes) {
        if (citasExistentes == null || citasExistentes.isEmpty()) {
            return true;
        }
        
        for (Cita cita : citasExistentes) {
            // Solo verificar citas pendientes del mismo personal
            if (cita.getEstado() == Cita.EstadoCita.PENDIENTE &&
                cita.getPersonal().equals(personal) &&
                cita.getFecha().equals(fecha) &&
                cita.getHora().equals(hora)) {
                return false; // Hay solapamiento
            }
        }
        
        return true; // No hay solapamiento
    }
    
    /**
     * Valida que se pueda cancelar una cita
     * Criterio HU-04: Permite cancelar con al menos 2 horas de anticipación
     */
    public static boolean validarPuedeCancelar(Cita cita) {
        if (cita == null) {
            return false;
        }
        
        return cita.puedeCancelar();
    }
    
    /**
     * Valida que la política de cancelación se cumpla
     * Criterio HU-04: Al menos 2 horas de anticipación
     */
    public static boolean validarPoliticaCancelacion(LocalDateTime fechaHoraCita) {
        if (fechaHoraCita == null) {
            return false;
        }
        
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime limiteCancelacion = fechaHoraCita.minusHours(2);
        
        return ahora.isBefore(limiteCancelacion);
    }
    
    /**
     * Obtiene mensaje de error según la validación
     */
    public static String obtenerMensajeError(TipoError tipoError) {
        switch (tipoError) {
            case CAMPOS_INCOMPLETOS:
                return "Por favor complete todos los campos obligatorios.";
            case FECHA_PASADA:
                return "No puede agendar citas en fechas pasadas.";
            case HORARIO_PASADO:
                return "No puede agendar citas en horarios pasados.";
            case HORARIO_OCUPADO:
                return "El horario seleccionado ya está ocupado. Por favor seleccione otro.";
            case NO_PUEDE_CANCELAR:
                return "No puede cancelar la cita. Debe hacerlo con al menos 2 horas de anticipación.";
            case CITA_YA_ATENDIDA:
                return "No puede cancelar una cita que ya fue atendida.";
            default:
                return "Error desconocido en la validación.";
        }
    }
    
    /**
     * Enum de tipos de errores de validación
     */
    public enum TipoError {
        CAMPOS_INCOMPLETOS,
        FECHA_PASADA,
        HORARIO_PASADO,
        HORARIO_OCUPADO,
        NO_PUEDE_CANCELAR,
        CITA_YA_ATENDIDA,
        NINGUNO
    }
    
    /**
     * Realiza validación completa de una cita nueva
     * Retorna el tipo de error encontrado o NINGUNO si es válida
     */
    public static TipoError validarCitaCompleta(String servicio, String personal,
                                                LocalDate fecha, LocalTime hora,
                                                List<Cita> citasExistentes) {
        // 1. Validar campos obligatorios
        if (!validarCamposObligatorios(servicio, personal, fecha, hora)) {
            return TipoError.CAMPOS_INCOMPLETOS;
        }
        
        // 2. Validar fecha no en pasado
        if (!validarFechaNoEnPasado(fecha)) {
            return TipoError.FECHA_PASADA;
        }
        
        // 3. Validar horario no en pasado
        if (!validarHorarioNoEnPasado(fecha, hora)) {
            return TipoError.HORARIO_PASADO;
        }
        
        // 4. Validar no solapamiento
        if (!validarNoSolapamiento(personal, fecha, hora, citasExistentes)) {
            return TipoError.HORARIO_OCUPADO;
        }
        
        return TipoError.NINGUNO;
    }
}