package modelo;

import java.time.LocalTime;

/**
 * Clase que representa un horario disponible para citas
 * Historia de Usuario: HU-02 Consultar disponibilidad en tiempo real
 */
public class Horario {
    
    private LocalTime hora;
    private boolean disponible;
    private String personal;
    
    public Horario(LocalTime hora, boolean disponible) {
        this.hora = hora;
        this.disponible = disponible;
    }
    
    public Horario(LocalTime hora, boolean disponible, String personal) {
        this.hora = hora;
        this.disponible = disponible;
        this.personal = personal;
    }
    
    /**
     * Marca el horario como ocupado
     */
    public void ocupar() {
        this.disponible = false;
    }
    
    /**
     * Libera el horario
     */
    public void liberar() {
        this.disponible = true;
    }
    
    /**
     * Obtiene el horario en formato String HH:mm
     */
    public String getHoraFormateada() {
        return String.format("%02d:%02d", hora.getHour(), hora.getMinute());
    }
    
    // Getters y Setters
    public LocalTime getHora() {
        return hora;
    }
    
    public void setHora(LocalTime hora) {
        this.hora = hora;
    }
    
    public boolean isDisponible() {
        return disponible;
    }
    
    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }
    
    public String getPersonal() {
        return personal;
    }
    
    public void setPersonal(String personal) {
        this.personal = personal;
    }
    
    @Override
    public String toString() {
        return "Horario{" +
                "hora=" + getHoraFormateada() +
                ", disponible=" + disponible +
                ", personal='" + personal + '\'' +
                '}';
    }
}