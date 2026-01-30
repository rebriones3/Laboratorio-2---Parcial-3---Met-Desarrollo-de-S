package modelo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.time.LocalDate;
import java.time.LocalTime;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase Cita
 * Valida el comportamiento del modelo de datos
 */
class CitaTest {
    
    private Cita cita;
    private LocalDate fechaFutura;
    private LocalTime horaFutura;
    
    @BeforeEach
    void setUp() {
        fechaFutura = LocalDate.now().plusDays(1);
        horaFutura = LocalTime.of(10, 0);
        cita = new Cita("Consulta General", "Dr. Juan Pérez", 
                       fechaFutura, horaFutura, "Chequeo");
    }
    
    @Test
    void constructor_debe_crear_cita_con_estado_pendiente() {
        assertEquals(Cita.EstadoCita.PENDIENTE, cita.getEstado());
    }
    
    @Test
    void constructor_debe_generar_codigo_unico() {
        assertNotNull(cita.getCodigo());
        assertTrue(cita.getCodigo().startsWith("CITA-"));
        assertEquals(13, cita.getCodigo().length()); // CITA- + 8 caracteres
    }
    
    @Test
    void getFechaHora_debe_combinar_fecha_y_hora() {
        assertEquals(fechaFutura.atTime(horaFutura), cita.getFechaHora());
    }
    
    @Test
    void puedeCancelar_debe_retornar_true_con_mas_de_2_horas() {
        LocalDate fechaFutura = LocalDate.now().plusDays(1);
        LocalTime horaFutura = LocalTime.of(10, 0);
        Cita citaFutura = new Cita("Consulta", "Dr. Test", 
                                   fechaFutura, horaFutura, "Test");
        
        assertTrue(citaFutura.puedeCancelar());
    }
    
    @Test
    void puedeCancelar_debe_retornar_false_con_estado_no_pendiente() {
        cita.marcarAtendida();
        assertFalse(cita.puedeCancelar());
    }
    
    @Test
    void cancelar_debe_cambiar_estado_a_cancelada() {
        LocalDate fechaFutura = LocalDate.now().plusDays(1);
        LocalTime horaFutura = LocalTime.of(10, 0);
        Cita citaCancelable = new Cita("Consulta", "Dr. Test", 
                                       fechaFutura, horaFutura, "Test");
        
        boolean resultado = citaCancelable.cancelar();
        
        assertTrue(resultado);
        assertEquals(Cita.EstadoCita.CANCELADA, citaCancelable.getEstado());
    }
    
    @Test
    void cancelar_debe_fallar_si_no_cumple_reglas() {
        cita.marcarAtendida();
        
        boolean resultado = cita.cancelar();
        
        assertFalse(resultado);
        assertEquals(Cita.EstadoCita.ATENDIDA, cita.getEstado());
    }
    
    @Test
    void marcarAtendida_debe_cambiar_estado() {
        cita.marcarAtendida();
        assertEquals(Cita.EstadoCita.ATENDIDA, cita.getEstado());
    }
    
    @Test
    void marcarNoAsistio_debe_cambiar_estado() {
        cita.marcarNoAsistio();
        assertEquals(Cita.EstadoCita.NO_ASISTIO, cita.getEstado());
    }
    
    @Test
    void getters_deben_retornar_valores_correctos() {
        assertEquals("Consulta General", cita.getServicio());
        assertEquals("Dr. Juan Pérez", cita.getPersonal());
        assertEquals(fechaFutura, cita.getFecha());
        assertEquals(horaFutura, cita.getHora());
        assertEquals("Chequeo", cita.getMotivo());
    }
    
    @Test
    void setters_deben_actualizar_valores() {
        cita.setServicio("Consulta Especializada");
        cita.setPersonal("Dra. María González");
        cita.setMotivo("Control");
        
        assertEquals("Consulta Especializada", cita.getServicio());
        assertEquals("Dra. María González", cita.getPersonal());
        assertEquals("Control", cita.getMotivo());
    }
}