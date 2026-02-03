package modelo;

/**
 * Data Transfer Object (DTO) para Cita
 * Se usa para el mapeo con MongoDB ya que LocalDate y LocalTime
 * no son directamente serializables por el driver de MongoDB
 */
public class CitaDTO {
    
    private String codigo;
    private String servicio;
    private String personal;
    private String fecha;        // String en formato ISO (yyyy-MM-dd)
    private String hora;         // String en formato ISO (HH:mm)
    private String motivo;
    private String estado;       // String del enum EstadoCita
    private String fechaCreacion; // String del LocalDateTime
    
    // Constructor vacío requerido por MongoDB
    public CitaDTO() {
    }
    
    // Constructor completo
    public CitaDTO(String codigo, String servicio, String personal, 
                   String fecha, String hora, String motivo, 
                   String estado, String fechaCreacion) {
        this.codigo = codigo;
        this.servicio = servicio;
        this.personal = personal;
        this.fecha = fecha;
        this.hora = hora;
        this.motivo = motivo;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
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
    
    public String getFecha() {
        return fecha;
    }
    
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
    
    public String getHora() {
        return hora;
    }
    
    public void setHora(String hora) {
        this.hora = hora;
    }
    
    public String getMotivo() {
        return motivo;
    }
    
    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
    
    public String getEstado() {
        return estado;
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    public String getFechaCreacion() {
        return fechaCreacion;
    }
    
    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    @Override
    public String toString() {
        return "CitaDTO{" +
                "codigo='" + codigo + '\'' +
                ", servicio='" + servicio + '\'' +
                ", personal='" + personal + '\'' +
                ", fecha='" + fecha + '\'' +
                ", hora='" + hora + '\'' +
                ", estado='" + estado + '\'' +
                '}';
    }
}