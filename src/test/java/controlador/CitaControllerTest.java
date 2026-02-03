package controlador;

import modelo.Cita;
import modelo.RepositorioCitasMongo;
import org.junit.jupiter.api.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests para CitaController
 * Limpia la base de datos antes y después de cada test
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CitaControllerTest {
    
    private CitaController controller;
    private RepositorioCitasMongo repositorio;
    
    @BeforeEach
    void setUp() {
        controller = new CitaController();
        repositorio = RepositorioCitasMongo.obtenerInstancia();
        repositorio.eliminarTodas();
        System.out.println("\n🧹 Base de datos limpiada para el test\n");
    }
    
    @Test
    @Order(1)
    void agendarCita_debe_crear_cita_exitosamente() {
        String servicio = "Consulta General";
        String personal = "Dr. Juan Pérez";
        LocalDate fecha = LocalDate.now().plusDays(1);
        LocalTime hora = LocalTime.of(10, 0);
        String motivo = "Chequeo general";
        
        CitaController.ResultadoOperacion resultado = 
            controller.agendarCita(servicio, personal, fecha, hora, motivo);
        
        assertTrue(resultado.isExitoso(), "La cita debería agendarse exitosamente");
        assertNotNull(resultado.getCodigo(), "Debe generar un código de cita");
        assertTrue(resultado.getCodigo().startsWith("CITA-"), "El código debe tener formato CITA-XXXXXXXX");
    }
    
    @Test
    @Order(2)
    void agendarCita_debe_rechazar_campos_vacios() {
        LocalDate fecha = LocalDate.now().plusDays(1);
        LocalTime hora = LocalTime.of(10, 0);
        
        CitaController.ResultadoOperacion resultado = 
            controller.agendarCita(null, "Dr. Juan Pérez", fecha, hora, "");
        
        assertFalse(resultado.isExitoso(), "No debe agendar con campos vacíos");
        assertTrue(resultado.getMensaje().contains("campos obligatorios"), 
                  "El mensaje debe indicar campos obligatorios");
    }
    
    @Test
    @Order(3)
    void agendarCita_debe_rechazar_fechas_pasadas() {
        String servicio = "Consulta General";
        String personal = "Dr. Juan Pérez";
        LocalDate fechaPasada = LocalDate.now().minusDays(1);
        LocalTime hora = LocalTime.of(10, 0);
        
        CitaController.ResultadoOperacion resultado = 
            controller.agendarCita(servicio, personal, fechaPasada, hora, "");
        
        assertFalse(resultado.isExitoso(), "No debe agendar citas en fechas pasadas");
        assertTrue(resultado.getMensaje().contains("pasada"), 
                  "El mensaje debe mencionar fecha pasada");
    }
    
    @Test
    @Order(4)
    void agendarCita_debe_prevenir_solapamiento() {
        String servicio = "Consulta";
        String personal = "Dr. Juan Pérez";
        LocalDate fecha = LocalDate.now().plusDays(1);
        LocalTime hora = LocalTime.of(10, 0);
        
        CitaController.ResultadoOperacion primera = 
            controller.agendarCita(servicio, personal, fecha, hora, "Primera");
        assertTrue(primera.isExitoso(), "Primera cita debe agendarse");
        
        CitaController.ResultadoOperacion segunda = 
            controller.agendarCita(servicio, personal, fecha, hora, "Segunda");
        
        assertFalse(segunda.isExitoso(), "No debe permitir solapamiento");
        assertTrue(segunda.getMensaje().contains("ocupado"), 
                  "El mensaje debe mencionar horario ocupado");
    }
    
    @Test
    @Order(5)
    void verificarDisponibilidad_debe_retornar_false_con_horario_ocupado() {
        String personal = "Dr. Juan Pérez";
        LocalDate fecha = LocalDate.now().plusDays(1);
        LocalTime hora = LocalTime.of(10, 0);
        
        controller.agendarCita("Consulta", personal, fecha, hora, "Test");
        
        boolean disponible = controller.verificarDisponibilidad(personal, fecha, hora);
        
        assertFalse(disponible, "El horario ocupado debe retornar false");
    }
    
    @Test
    @Order(6)
    void verificarDisponibilidad_debe_retornar_true_con_horario_libre() {
        String personal = "Dr. Juan Pérez";
        LocalDate fecha = LocalDate.now().plusDays(1);
        LocalTime horaOcupada = LocalTime.of(10, 0);
        LocalTime horaLibre = LocalTime.of(14, 0);
        
        controller.agendarCita("Consulta", personal, fecha, horaOcupada, "Test");
        
        boolean disponible = controller.verificarDisponibilidad(personal, fecha, horaLibre);
        
        assertTrue(disponible, "El horario libre debe retornar true");
    }
    
    @Test
    @Order(7)
    void obtenerHorariosOcupados_debe_listar_correctamente() {
        String personal = "Dr. Juan Pérez";
        LocalDate fecha = LocalDate.now().plusDays(1);
        
        controller.agendarCita("Consulta", personal, fecha, LocalTime.of(10, 0), "Test 1");
        controller.agendarCita("Consulta", personal, fecha, LocalTime.of(14, 0), "Test 2");
        
        List<LocalTime> ocupados = controller.obtenerHorariosOcupados(personal, fecha);
        
        assertEquals(2, ocupados.size(), "Debe haber 2 horarios ocupados");
        assertTrue(ocupados.contains(LocalTime.of(10, 0)), "Debe incluir 10:00");
        assertTrue(ocupados.contains(LocalTime.of(14, 0)), "Debe incluir 14:00");
    }
    
    @Test
    @Order(8)
    void cancelarCita_debe_rechazar_codigo_invalido() {
        CitaController.ResultadoOperacion resultado = 
            controller.cancelarCita("CITA-NOEXISTE");
        
        assertFalse(resultado.isExitoso(), "Debe rechazar código inválido");
        // toLowerCase() porque el mensaje empieza con "No" mayúscula pero contains() es case-sensitive
        assertTrue(resultado.getMensaje().toLowerCase().contains("no se encontr"), 
                  "El mensaje debe indicar que no se encontró");
    }
    
    @Test
    @Order(9)
    void cancelarCita_debe_funcionar_con_codigo_valido() {
        LocalDate fecha = LocalDate.now().plusDays(1);
        LocalTime hora = LocalTime.of(10, 0);
        
        CitaController.ResultadoOperacion agendada = 
            controller.agendarCita("Consulta", "Dr. Juan Pérez", fecha, hora, "Test");
        
        assertTrue(agendada.isExitoso(), "La cita debe agendarse");
        String codigo = agendada.getCodigo();
        
        CitaController.ResultadoOperacion resultado = controller.cancelarCita(codigo);
        
        assertTrue(resultado.isExitoso(), "La cita debe cancelarse exitosamente");
        assertEquals(codigo, resultado.getCodigo(), "Debe retornar el código correcto");
    }
    
    @Test
    @Order(10)
    void buscarCitaPorCodigo_debe_retornar_null_con_codigo_invalido() {
        Cita cita = controller.buscarCitaPorCodigo("CITA-NOEXISTE");
        
        assertNull(cita, "Debe retornar null para código inválido");
    }
    
    @Test
    @Order(11)
    void buscarCitaPorCodigo_debe_retornar_cita_correcta() {
        LocalDate fecha = LocalDate.now().plusDays(1);
        CitaController.ResultadoOperacion agendada = 
            controller.agendarCita("Consulta", "Dr. Juan Pérez", fecha, LocalTime.of(10, 0), "Test");
        
        String codigo = agendada.getCodigo();
        
        Cita cita = controller.buscarCitaPorCodigo(codigo);
        
        assertNotNull(cita, "Debe encontrar la cita");
        assertEquals(codigo, cita.getCodigo(), "El código debe coincidir");
        assertEquals("Consulta", cita.getServicio(), "El servicio debe coincidir");
    }
    
    @Test
    @Order(12)
    void obtenerEstadisticas_debe_calcular_correctamente() {
        LocalDate fecha = LocalDate.now().plusDays(1);
        controller.agendarCita("Consulta", "Dr. Juan Pérez", fecha, LocalTime.of(10, 0), "Test 1");
        controller.agendarCita("Consulta", "Dra. María González", fecha, LocalTime.of(14, 0), "Test 2");
        
        CitaController.EstadisticasCitas stats = controller.obtenerEstadisticas();
        
        assertEquals(2, stats.getPendientes(), "Debe haber 2 citas pendientes");
        assertEquals(0, stats.getAtendidas(), "No debe haber citas atendidas");
        assertEquals(0, stats.getCanceladas(), "No debe haber citas canceladas");
        assertEquals(2, stats.getTotal(), "El total debe ser 2");
    }
    
    @Test
    @Order(13)
    void obtenerTodasLasCitas_debe_retornar_lista_completa() {
        LocalDate fecha = LocalDate.now().plusDays(1);
        controller.agendarCita("Consulta", "Dr. Juan Pérez", fecha, LocalTime.of(10, 0), "Test 1");
        controller.agendarCita("Consulta", "Dra. María González", fecha, LocalTime.of(14, 0), "Test 2");
        
        List<Cita> citas = controller.obtenerTodasLasCitas();
        
        assertEquals(2, citas.size(), "Debe haber 2 citas en total");
    }
    
    @Test
    @Order(14)
    void obtenerCitasPendientes_debe_filtrar_correctamente() {
        LocalDate fecha = LocalDate.now().plusDays(1);
        
        controller.agendarCita("Consulta", "Dr. Juan Pérez", fecha, LocalTime.of(10, 0), "Test 1");
        controller.agendarCita("Consulta", "Dra. María González", fecha, LocalTime.of(14, 0), "Test 2");
        
        List<Cita> pendientes = controller.obtenerCitasPendientes();
        
        assertEquals(2, pendientes.size(), "Debe haber 2 citas pendientes");
        assertTrue(pendientes.stream().allMatch(c -> c.getEstado() == Cita.EstadoCita.PENDIENTE),
                  "Todas deben estar en estado PENDIENTE");
    }
    
    @AfterEach
    void tearDown() {
        if (repositorio != null) {
            repositorio.eliminarTodas();
            System.out.println("🧹 Base de datos limpiada después del test\n");
        }
    }
}