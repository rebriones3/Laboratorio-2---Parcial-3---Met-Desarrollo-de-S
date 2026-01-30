package controlador;

import modelo.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de integración para CitaController
 * Valida la interacción entre controlador, modelo y repositorio
 */
class CitaControllerTest {
    
    private CitaController controller;
    private RepositorioCitas repositorio;
    
    @BeforeEach
    void setUp() {
        controller = new CitaController();
        repositorio = RepositorioCitas.obtenerInstancia();
        repositorio.limpiar();
    }
    
    @AfterEach
    void tearDown() {
        repositorio.limpiar();
    }
    
    @Test
    void agendarCita_debe_crear_cita_exitosamente() {
        LocalDate fecha = LocalDate.now().plusDays(1);
        LocalTime hora = LocalTime.of(10, 0);
        
        CitaController.ResultadoOperacion resultado = controller.agendarCita(
            "Consulta General", "Dr. Juan Pérez", fecha, hora, "Chequeo"
        );
        
        assertTrue(resultado.isExitoso());
        assertNotNull(resultado.getCodigo());
        assertTrue(resultado.getMensaje().contains("exitosamente"));
    }
    
    @Test
    void agendarCita_debe_fallar_con_campos_incompletos() {
        LocalDate fecha = LocalDate.now().plusDays(1);
        LocalTime hora = LocalTime.of(10, 0);
        
        CitaController.ResultadoOperacion resultado = controller.agendarCita(
            null, "Dr. Juan Pérez", fecha, hora, "Chequeo"
        );
        
        assertFalse(resultado.isExitoso());
        assertNull(resultado.getCodigo());
        assertTrue(resultado.getMensaje().contains("campos obligatorios"));
    }
    
    @Test
    void agendarCita_debe_fallar_con_fecha_pasada() {
        LocalDate fechaPasada = LocalDate.now().minusDays(1);
        LocalTime hora = LocalTime.of(10, 0);
        
        CitaController.ResultadoOperacion resultado = controller.agendarCita(
            "Consulta General", "Dr. Juan Pérez", fechaPasada, hora, "Chequeo"
        );
        
        assertFalse(resultado.isExitoso());
        assertTrue(resultado.getMensaje().contains("pasadas"));
    }
    
    @Test
    void agendarCita_debe_fallar_con_horario_ocupado() {
        LocalDate fecha = LocalDate.now().plusDays(1);
        LocalTime hora = LocalTime.of(10, 0);
        
        // Primera cita
        controller.agendarCita("Consulta", "Dr. Juan Pérez", fecha, hora, "Test 1");
        
        // Segunda cita (debe fallar)
        CitaController.ResultadoOperacion resultado = controller.agendarCita(
            "Consulta", "Dr. Juan Pérez", fecha, hora, "Test 2"
        );
        
        assertFalse(resultado.isExitoso());
        assertTrue(resultado.getMensaje().contains("ocupado"));
    }
    
    @Test
    void verificarDisponibilidad_debe_retornar_true_con_horario_libre() {
        LocalDate fecha = LocalDate.now().plusDays(1);
        LocalTime hora = LocalTime.of(10, 0);
        
        boolean disponible = controller.verificarDisponibilidad("Dr. Juan Pérez", fecha, hora);
        
        assertTrue(disponible);
    }
    
    @Test
    void verificarDisponibilidad_debe_retornar_false_con_horario_ocupado() {
        LocalDate fecha = LocalDate.now().plusDays(1);
        LocalTime hora = LocalTime.of(10, 0);
        
        controller.agendarCita("Consulta", "Dr. Juan Pérez", fecha, hora, "Test");
        
        boolean disponible = controller.verificarDisponibilidad("Dr. Juan Pérez", fecha, hora);
        
        assertFalse(disponible);
    }
    
    @Test
    void obtenerHorariosOcupados_debe_retornar_lista_correcta() {
        LocalDate fecha = LocalDate.now().plusDays(1);
        controller.agendarCita("Consulta", "Dr. Juan Pérez", fecha, LocalTime.of(10, 0), "Test 1");
        controller.agendarCita("Consulta", "Dr. Juan Pérez", fecha, LocalTime.of(14, 0), "Test 2");
        
        List<LocalTime> ocupados = controller.obtenerHorariosOcupados("Dr. Juan Pérez", fecha);
        
        assertEquals(2, ocupados.size());
        assertTrue(ocupados.contains(LocalTime.of(10, 0)));
        assertTrue(ocupados.contains(LocalTime.of(14, 0)));
    }
    
    @Test
    void cancelarCita_debe_funcionar_con_codigo_valido() {
        LocalDate fecha = LocalDate.now().plusDays(1);
        LocalTime hora = LocalTime.of(10, 0);
        
        CitaController.ResultadoOperacion resultadoAgendar = controller.agendarCita(
            "Consulta", "Dr. Juan Pérez", fecha, hora, "Test"
        );
        
        CitaController.ResultadoOperacion resultadoCancelar = 
            controller.cancelarCita(resultadoAgendar.getCodigo());
        
        assertTrue(resultadoCancelar.isExitoso());
    }
    
    @Test
    void cancelarCita_debe_fallar_con_codigo_inexistente() {
        CitaController.ResultadoOperacion resultado = controller.cancelarCita("CITA-NOEXISTE");
        
        assertFalse(resultado.isExitoso());
        assertTrue(resultado.getMensaje().contains("no se encontró"));
    }
    
    @Test
    void buscarCitaPorCodigo_debe_retornar_cita_correcta() {
        LocalDate fecha = LocalDate.now().plusDays(1);
        LocalTime hora = LocalTime.of(10, 0);
        
        CitaController.ResultadoOperacion resultado = controller.agendarCita(
            "Consulta General", "Dr. Juan Pérez", fecha, hora, "Test"
        );
        
        Cita cita = controller.buscarCitaPorCodigo(resultado.getCodigo());
        
        assertNotNull(cita);
        assertEquals("Consulta General", cita.getServicio());
        assertEquals("Dr. Juan Pérez", cita.getPersonal());
    }
    
    @Test
    void buscarCitaPorCodigo_debe_retornar_null_con_codigo_inexistente() {
        Cita cita = controller.buscarCitaPorCodigo("CITA-NOEXISTE");
        assertNull(cita);
    }
    
    @Test
    void obtenerCitasPendientes_debe_retornar_solo_pendientes() {
        LocalDate fecha = LocalDate.now().plusDays(1);
        
        controller.agendarCita("Consulta", "Dr. Juan Pérez", fecha, LocalTime.of(10, 0), "Test 1");
        controller.agendarCita("Consulta", "Dra. María González", fecha, LocalTime.of(14, 0), "Test 2");
        
        List<Cita> pendientes = controller.obtenerCitasPendientes();
        
        assertTrue(pendientes.size() >= 2);
        for (Cita cita : pendientes) {
            assertEquals(Cita.EstadoCita.PENDIENTE, cita.getEstado());
        }
    }
    
    @Test
    void obtenerEstadisticas_debe_calcular_correctamente() {
        repositorio.limpiar();
        
        LocalDate fecha = LocalDate.now().plusDays(1);
        controller.agendarCita("Consulta", "Dr. Juan Pérez", fecha, LocalTime.of(10, 0), "Test 1");
        controller.agendarCita("Consulta", "Dr. Juan Pérez", fecha, LocalTime.of(14, 0), "Test 2");
        
        CitaController.EstadisticasCitas stats = controller.obtenerEstadisticas();
        
        assertEquals(2, stats.getPendientes());
        assertEquals(2, stats.getTotal());
    }
    
    @Test
    void obtenerTodasLasCitas_debe_retornar_lista_completa() {
        repositorio.limpiar();
        
        LocalDate fecha = LocalDate.now().plusDays(1);
        controller.agendarCita("Consulta", "Dr. Juan Pérez", fecha, LocalTime.of(10, 0), "Test 1");
        controller.agendarCita("Consulta", "Dra. María González", fecha, LocalTime.of(14, 0), "Test 2");
        
        List<Cita> todas = controller.obtenerTodasLasCitas();
        
        assertEquals(2, todas.size());
    }
}