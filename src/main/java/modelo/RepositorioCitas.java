package modelo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Repositorio simulado para gestionar citas en memoria
 * En un proyecto real, esto se conectaría a una base de datos
 */
public class RepositorioCitas {
    
    private static RepositorioCitas instancia;
    private List<Cita> citas;
    
    // Singleton pattern
    private RepositorioCitas() {
        this.citas = new ArrayList<>();
        inicializarDatosPrueba();
    }
    
    /**
     * Obtiene la instancia única del repositorio
     */
    public static RepositorioCitas obtenerInstancia() {
        if (instancia == null) {
            instancia = new RepositorioCitas();
        }
        return instancia;
    }
    
    /**
     * Inicializa datos de prueba para simular citas existentes
     */
    private void inicializarDatosPrueba() {
        // Simular algunas citas existentes
        LocalDate hoy = LocalDate.now();
        
        Cita cita1 = new Cita("Consulta General", "Dr. Juan Pérez", 
                             hoy, LocalTime.of(8, 0), "Chequeo general");
        cita1.setCodigo("CITA-A1B2C3D4");
        
        Cita cita2 = new Cita("Consulta Especializada", "Dra. María González", 
                             hoy, LocalTime.of(9, 30), "Seguimiento");
        cita2.setCodigo("CITA-E5F6G7H8");
        
        Cita cita3 = new Cita("Exámenes de Laboratorio", "Dr. Carlos Ramírez", 
                             hoy, LocalTime.of(11, 0), "Exámenes de rutina");
        cita3.setCodigo("CITA-I9J0K1L2");
        
        // Agregar citas de prueba
        citas.add(cita1);
        citas.add(cita2);
        citas.add(cita3);
    }
    
    /**
     * Guarda una nueva cita
     */
    public boolean guardar(Cita cita) {
        if (cita == null) {
            return false;
        }
        
        // Validar que no exista solapamiento antes de guardar
        if (!ValidadorCitas.validarNoSolapamiento(
                cita.getPersonal(), 
                cita.getFecha(), 
                cita.getHora(), 
                this.citas)) {
            return false;
        }
        
        return citas.add(cita);
    }
    
    /**
     * Busca una cita por código
     */
    public Cita buscarPorCodigo(String codigo) {
        return citas.stream()
                   .filter(c -> c.getCodigo().equals(codigo))
                   .findFirst()
                   .orElse(null);
    }
    
    /**
     * Obtiene todas las citas
     */
    public List<Cita> obtenerTodas() {
        return new ArrayList<>(citas);
    }
    
    /**
     * Obtiene citas por personal
     */
    public List<Cita> obtenerPorPersonal(String personal) {
        return citas.stream()
                   .filter(c -> c.getPersonal().equals(personal))
                   .collect(Collectors.toList());
    }
    
    /**
     * Obtiene citas por fecha
     */
    public List<Cita> obtenerPorFecha(LocalDate fecha) {
        return citas.stream()
                   .filter(c -> c.getFecha().equals(fecha))
                   .collect(Collectors.toList());
    }
    
    /**
     * Obtiene citas pendientes
     */
    public List<Cita> obtenerPendientes() {
        return citas.stream()
                   .filter(c -> c.getEstado() == Cita.EstadoCita.PENDIENTE)
                   .collect(Collectors.toList());
    }
    
    /**
     * Obtiene citas cancelables (pendientes con más de 2 horas de anticipación)
     */
    public List<Cita> obtenerCancelables() {
        return citas.stream()
                   .filter(Cita::puedeCancelar)
                   .collect(Collectors.toList());
    }
    
    /**
     * Cancela una cita por código
     */
    public boolean cancelarCita(String codigo) {
        Cita cita = buscarPorCodigo(codigo);
        if (cita == null) {
            return false;
        }
        
        return cita.cancelar();
    }
    
    /**
     * Verifica si un horario está ocupado
     */
    public boolean estaOcupado(String personal, LocalDate fecha, LocalTime hora) {
        return citas.stream()
                   .anyMatch(c -> 
                       c.getEstado() == Cita.EstadoCita.PENDIENTE &&
                       c.getPersonal().equals(personal) &&
                       c.getFecha().equals(fecha) &&
                       c.getHora().equals(hora)
                   );
    }
    
    /**
     * Obtiene horarios ocupados para un personal en una fecha
     */
    public List<LocalTime> obtenerHorariosOcupados(String personal, LocalDate fecha) {
        return citas.stream()
                   .filter(c -> 
                       c.getEstado() == Cita.EstadoCita.PENDIENTE &&
                       c.getPersonal().equals(personal) &&
                       c.getFecha().equals(fecha)
                   )
                   .map(Cita::getHora)
                   .collect(Collectors.toList());
    }
    
    /**
     * Cuenta citas por estado
     */
    public long contarPorEstado(Cita.EstadoCita estado) {
        return citas.stream()
                   .filter(c -> c.getEstado() == estado)
                   .count();
    }
    
    /**
     * Elimina todas las citas (útil para pruebas)
     */
    public void limpiar() {
        citas.clear();
    }
    
    /**
     * Obtiene la cantidad total de citas
     */
    public int contarTotal() {
        return citas.size();
    }
}