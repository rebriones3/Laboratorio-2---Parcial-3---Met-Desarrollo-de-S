package modelo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.time.LocalDate;
import java.time.LocalTime;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase Cita
 * Valida el comportamiento del modelo de datos
 * HU-01: Agendar cita en línea
 * HU-04: Cancelar cita
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
        assertEquals(13, cita.getCodigo().length());
    }
    
    @Test
    void constructor_debe_asignar_fecha_creacion() {
        assertNotNull(cita.getFechaCreacion());
        assertTrue(cita.getFechaCreacion().isBefore(java.time.LocalDateTime.now().plusSeconds(1)));
    }
    
    @Test
    void getFechaHora_debe_combinar_fecha_y_hora() {
        assertEquals(fechaFutura.atTime(horaFutura), cita.getFechaHora());
    }
    
    @Test
    void getFechaHora_debe_retornar_null_sin_fecha() {
        Cita citaSinFecha = new Cita();
        assertNull(citaSinFecha.getFechaHora());
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
    void puedeCancelar_debe_retornar_false_sin_fecha_hora() {
        Cita citaSinFecha = new Cita();
        assertFalse(citaSinFecha.puedeCancelar());
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
    
    @Test
    void setCodigo_debe_actualizar_codigo() {
        String nuevoCodigo = "CITA-TEST123";
        cita.setCodigo(nuevoCodigo);
        assertEquals(nuevoCodigo, cita.getCodigo());
    }
    
    @Test
    void setFecha_debe_actualizar_fecha() {
        LocalDate nuevaFecha = LocalDate.now().plusDays(2);
        cita.setFecha(nuevaFecha);
        assertEquals(nuevaFecha, cita.getFecha());
    }
    
    @Test
    void setHora_debe_actualizar_hora() {
        LocalTime nuevaHora = LocalTime.of(14, 0);
        cita.setHora(nuevaHora);
        assertEquals(nuevaHora, cita.getHora());
    }
    
    @Test
    void setEstado_debe_actualizar_estado() {
        cita.setEstado(Cita.EstadoCita.ATENDIDA);
        assertEquals(Cita.EstadoCita.ATENDIDA, cita.getEstado());
    }
    
    @Test
    void toString_debe_incluir_informacion_basica() {
        String resultado = cita.toString();
        
        assertTrue(resultado.contains("Cita"));
        assertTrue(resultado.contains(cita.getCodigo()));
        assertTrue(resultado.contains("Consulta General"));
        assertTrue(resultado.contains("Dr. Juan Pérez"));
    }
    
    @Test
    void estado_descripcion_debe_retornar_texto_correcto() {
        assertEquals("Pendiente", Cita.EstadoCita.PENDIENTE.getDescripcion());
        assertEquals("Atendida", Cita.EstadoCita.ATENDIDA.getDescripcion());
        assertEquals("Cancelada", Cita.EstadoCita.CANCELADA.getDescripcion());
        assertEquals("No Asistió", Cita.EstadoCita.NO_ASISTIO.getDescripcion());
    }
    
    @Test
    void constructor_vacio_debe_crear_cita_con_valores_por_defecto() {
        Cita citaVacia = new Cita();
        
        assertEquals(Cita.EstadoCita.PENDIENTE, citaVacia.getEstado());
        assertNotNull(citaVacia.getFechaCreacion());
    }
    
    @Test
    void dos_citas_deben_tener_codigos_diferentes() {
        Cita cita1 = new Cita("Consulta", "Dr. Test", fechaFutura, horaFutura, "Test1");
        Cita cita2 = new Cita("Consulta", "Dr. Test", fechaFutura, horaFutura, "Test2");
        
        assertNotEquals(cita1.getCodigo(), cita2.getCodigo());
    }
}