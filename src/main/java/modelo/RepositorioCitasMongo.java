package modelo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.conversions.Bson;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.*;

/**
 * Repositorio de Citas con persistencia en MongoDB
 * Implementa operaciones CRUD completas
 * 
 * CRUD:
 * - CREATE: guardar()
 * - READ: buscarPorCodigo(), obtenerTodas(), obtenerPorPersonal(), etc.
 * - UPDATE: actualizarCita(), cancelarCita()
 * - DELETE: eliminarCita()
 */
public class RepositorioCitasMongo {
    
    private static RepositorioCitasMongo instancia;
    private MongoCollection<CitaDTO> coleccionCitas;
    private MongoDatabase database;
    
    private static final String COLECCION_CITAS = "citas";
    
    /**
     * Constructor privado (Singleton)
     */
    private RepositorioCitasMongo() {
        try {
            MongoDBConfig config = MongoDBConfig.obtenerInstancia();
            database = config.getDatabase();
            coleccionCitas = database.getCollection(COLECCION_CITAS, CitaDTO.class);
            
            System.out.println("✅ RepositorioCitasMongo inicializado");
            System.out.println("📁 Colección: " + COLECCION_CITAS);
            
        } catch (Exception e) {
            System.err.println("❌ Error al inicializar repositorio: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Obtiene la instancia única del repositorio (Singleton)
     */
    public static RepositorioCitasMongo obtenerInstancia() {
        if (instancia == null) {
            synchronized (RepositorioCitasMongo.class) {
                if (instancia == null) {
                    instancia = new RepositorioCitasMongo();
                }
            }
        }
        return instancia;
    }
    
    // ==================== CREATE ====================
    
    /**
     * Guarda una nueva cita en MongoDB
     * 
     * @param cita La cita a guardar
     * @return true si se guardó exitosamente, false en caso contrario
     */
    public boolean guardar(Cita cita) {
        try {
            if (cita == null) {
                System.err.println("⚠️ No se puede guardar una cita nula");
                return false;
            }
            
            // Validar que no exista solapamiento
            if (!ValidadorCitas.validarNoSolapamiento(
                    cita.getPersonal(), 
                    cita.getFecha(), 
                    cita.getHora(), 
                    obtenerTodas())) {
                System.err.println("⚠️ El horario ya está ocupado");
                return false;
            }
            
            // Convertir Cita a CitaDTO para MongoDB
            CitaDTO citaDTO = convertirACitaDTO(cita);
            
            // Insertar en MongoDB
            coleccionCitas.insertOne(citaDTO);
            
            System.out.println("✅ Cita guardada: " + cita.getCodigo());
            return true;
            
        } catch (Exception e) {
            System.err.println("❌ Error al guardar cita: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // ==================== READ ====================
    
    /**
     * Busca una cita por su código
     * 
     * @param codigo Código único de la cita
     * @return La cita encontrada o null
     */
    public Cita buscarPorCodigo(String codigo) {
        try {
            CitaDTO citaDTO = coleccionCitas.find(eq("codigo", codigo)).first();
            
            if (citaDTO != null) {
                return convertirACita(citaDTO);
            }
            
            return null;
            
        } catch (Exception e) {
            System.err.println("❌ Error al buscar cita: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Obtiene todas las citas de la base de datos
     * 
     * @return Lista de todas las citas
     */
    public List<Cita> obtenerTodas() {
        List<Cita> citas = new ArrayList<>();
        
        try {
            coleccionCitas.find().into(new ArrayList<>())
                .forEach(citaDTO -> citas.add(convertirACita(citaDTO)));
                
        } catch (Exception e) {
            System.err.println("❌ Error al obtener citas: " + e.getMessage());
        }
        
        return citas;
    }
    
    /**
     * Obtiene citas por personal médico
     * 
     * @param personal Nombre del personal
     * @return Lista de citas del personal especificado
     */
    public List<Cita> obtenerPorPersonal(String personal) {
        List<Cita> citas = new ArrayList<>();
        
        try {
            coleccionCitas.find(eq("personal", personal)).into(new ArrayList<>())
                .forEach(citaDTO -> citas.add(convertirACita(citaDTO)));
                
        } catch (Exception e) {
            System.err.println("❌ Error al obtener citas por personal: " + e.getMessage());
        }
        
        return citas;
    }
    
    /**
     * Obtiene citas por fecha
     * 
     * @param fecha Fecha a buscar
     * @return Lista de citas en esa fecha
     */
    public List<Cita> obtenerPorFecha(LocalDate fecha) {
        List<Cita> citas = new ArrayList<>();
        
        try {
            String fechaStr = fecha.toString();
            coleccionCitas.find(eq("fecha", fechaStr)).into(new ArrayList<>())
                .forEach(citaDTO -> citas.add(convertirACita(citaDTO)));
                
        } catch (Exception e) {
            System.err.println("❌ Error al obtener citas por fecha: " + e.getMessage());
        }
        
        return citas;
    }
    
    /**
     * Obtiene citas pendientes
     * 
     * @return Lista de citas con estado PENDIENTE
     */
    public List<Cita> obtenerPendientes() {
        return obtenerTodas().stream()
            .filter(c -> c.getEstado() == Cita.EstadoCita.PENDIENTE)
            .collect(Collectors.toList());
    }
    
    /**
     * Obtiene citas cancelables (pendientes con más de 2 horas de anticipación)
     * 
     * @return Lista de citas que pueden ser canceladas
     */
    public List<Cita> obtenerCancelables() {
        return obtenerTodas().stream()
            .filter(Cita::puedeCancelar)
            .collect(Collectors.toList());
    }
    
    /**
     * Verifica si un horario está ocupado
     * 
     * @param personal Nombre del personal
     * @param fecha Fecha de la cita
     * @param hora Hora de la cita
     * @return true si está ocupado, false si está libre
     */
    public boolean estaOcupado(String personal, LocalDate fecha, LocalTime hora) {
        try {
            Bson filtro = and(
                eq("personal", personal),
                eq("fecha", fecha.toString()),
                eq("hora", hora.toString()),
                eq("estado", "PENDIENTE")
            );
            
            return coleccionCitas.find(filtro).first() != null;
            
        } catch (Exception e) {
            System.err.println("❌ Error al verificar disponibilidad: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Obtiene horarios ocupados para un personal en una fecha
     * 
     * @param personal Nombre del personal
     * @param fecha Fecha a consultar
     * @return Lista de horarios ocupados
     */
    public List<LocalTime> obtenerHorariosOcupados(String personal, LocalDate fecha) {
        List<LocalTime> horarios = new ArrayList<>();
        
        try {
            Bson filtro = and(
                eq("personal", personal),
                eq("fecha", fecha.toString()),
                eq("estado", "PENDIENTE")
            );
            
            coleccionCitas.find(filtro).into(new ArrayList<>())
                .forEach(citaDTO -> horarios.add(LocalTime.parse(citaDTO.getHora())));
                
        } catch (Exception e) {
            System.err.println("❌ Error al obtener horarios ocupados: " + e.getMessage());
        }
        
        return horarios;
    }
    
    /**
     * Cuenta citas por estado
     * 
     * @param estado Estado de la cita
     * @return Número de citas en ese estado
     */
    public long contarPorEstado(Cita.EstadoCita estado) {
        try {
            return coleccionCitas.countDocuments(eq("estado", estado.name()));
        } catch (Exception e) {
            System.err.println("❌ Error al contar por estado: " + e.getMessage());
            return 0;
        }
    }
    
    /**
     * Obtiene la cantidad total de citas
     * 
     * @return Total de citas en la base de datos
     */
    public long contarTotal() {
        try {
            return coleccionCitas.countDocuments();
        } catch (Exception e) {
            System.err.println("❌ Error al contar total: " + e.getMessage());
            return 0;
        }
    }
    
    // ==================== UPDATE ====================
    
    /**
     * Actualiza una cita existente
     * 
     * @param cita Cita con los datos actualizados
     * @return true si se actualizó exitosamente
     */
    public boolean actualizarCita(Cita cita) {
        try {
            if (cita == null || cita.getCodigo() == null) {
                return false;
            }
            
            CitaDTO citaDTO = convertirACitaDTO(cita);
            
            Bson filtro = eq("codigo", cita.getCodigo());
            Bson actualizacion = Updates.combine(
                Updates.set("servicio", citaDTO.getServicio()),
                Updates.set("personal", citaDTO.getPersonal()),
                Updates.set("fecha", citaDTO.getFecha()),
                Updates.set("hora", citaDTO.getHora()),
                Updates.set("motivo", citaDTO.getMotivo()),
                Updates.set("estado", citaDTO.getEstado())
            );
            
            coleccionCitas.updateOne(filtro, actualizacion);
            
            System.out.println("✅ Cita actualizada: " + cita.getCodigo());
            return true;
            
        } catch (Exception e) {
            System.err.println("❌ Error al actualizar cita: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Cancela una cita por código
     * 
     * @param codigo Código de la cita a cancelar
     * @return true si se canceló exitosamente
     */
    public boolean cancelarCita(String codigo) {
        try {
            Cita cita = buscarPorCodigo(codigo);
            
            if (cita == null) {
                System.err.println("⚠️ Cita no encontrada: " + codigo);
                return false;
            }
            
            if (!cita.puedeCancelar()) {
                System.err.println("⚠️ La cita no puede ser cancelada");
                return false;
            }
            
            boolean cancelado = cita.cancelar();
            
            if (cancelado) {
                return actualizarCita(cita);
            }
            
            return false;
            
        } catch (Exception e) {
            System.err.println("❌ Error al cancelar cita: " + e.getMessage());
            return false;
        }
    }
    
    // ==================== DELETE ====================
    
    /**
     * Elimina una cita por código
     * 
     * @param codigo Código de la cita a eliminar
     * @return true si se eliminó exitosamente
     */
    public boolean eliminarCita(String codigo) {
        try {
            if (codigo == null || codigo.trim().isEmpty()) {
                return false;
            }
            
            Bson filtro = eq("codigo", codigo);
            boolean resultado = coleccionCitas.deleteOne(filtro).getDeletedCount() > 0;
            
            if (resultado) {
                System.out.println("✅ Cita eliminada: " + codigo);
            } else {
                System.err.println("⚠️ No se encontró la cita: " + codigo);
            }
            
            return resultado;
            
        } catch (Exception e) {
            System.err.println("❌ Error al eliminar cita: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Elimina todas las citas de la colección
     * CUIDADO: Esta operación es irreversible
     * 
     * @return true si se eliminaron todas las citas
     */
    public boolean eliminarTodas() {
        try {
            coleccionCitas.deleteMany(new org.bson.Document());
            System.out.println("⚠️ Todas las citas han sido eliminadas");
            return true;
        } catch (Exception e) {
            System.err.println("❌ Error al eliminar todas las citas: " + e.getMessage());
            return false;
        }
    }
    
    // ==================== MÉTODOS DE CONVERSIÓN ====================
    
    /**
     * Convierte una Cita a CitaDTO para MongoDB
     */
    private CitaDTO convertirACitaDTO(Cita cita) {
        CitaDTO dto = new CitaDTO();
        dto.setCodigo(cita.getCodigo());
        dto.setServicio(cita.getServicio());
        dto.setPersonal(cita.getPersonal());
        dto.setFecha(cita.getFecha().toString());
        dto.setHora(cita.getHora().toString());
        dto.setMotivo(cita.getMotivo());
        dto.setEstado(cita.getEstado().name());
        dto.setFechaCreacion(cita.getFechaCreacion().toString());
        return dto;
    }
    
    /**
     * Convierte un CitaDTO de MongoDB a Cita
     */
    private Cita convertirACita(CitaDTO dto) {
        Cita cita = new Cita();
        cita.setCodigo(dto.getCodigo());
        cita.setServicio(dto.getServicio());
        cita.setPersonal(dto.getPersonal());
        cita.setFecha(LocalDate.parse(dto.getFecha()));
        cita.setHora(LocalTime.parse(dto.getHora()));
        cita.setMotivo(dto.getMotivo());
        cita.setEstado(Cita.EstadoCita.valueOf(dto.getEstado()));
        return cita;
    }
}