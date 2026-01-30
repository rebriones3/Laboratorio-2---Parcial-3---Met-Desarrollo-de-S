package modelo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para RepositorioCitas
 * Valida las operaciones de persistencia en memoria
 */
class RepositorioCitasTest {
    
    private RepositorioCitas repositorio;
    
    @BeforeEach
    void setUp() {
        repositorio = RepositorioCitas.obtenerInstancia();
        repositorio.limpiar();
    }
    
    @AfterEach
    void tearDown() {
        repositorio.limpiar();
    }
    
    @Test
    void obtenerInstancia_debe_retornar_singleton() {
        RepositorioCitas instancia1 = RepositorioCitas.obtenerInstancia();
        RepositorioCitas instancia2 = RepositorioCitas.obtenerInstancia();
        
        assertSame(instancia1, instancia2);
    }
    
    @Test
    void guardar_debe_agregar_cita_al_repositorio() {
        LocalDate fecha = LocalDate.now().plusDays(1);
        LocalTime hora = LocalTime.of(10, 0);
        Cita cita = new Cita("Consulta", "Dr. Test", fecha, hora, "Test");
        
        boolean resultado = repositorio.guardar(cita);
        
        assertTrue(resultado);
        assertEquals(1, repositorio.contarTotal());
    }
    
    @Test
    void guardar_debe_fallar_con_cita_nula() {
        boolean resultado = repositorio.guardar(null);
        assertFalse(resultado);
    }
    
    @Test
    void guardar_debe_fallar_con_horario_duplicado() {
        LocalDate fecha = LocalDate.now().plusDays(1);
        LocalTime hora = LocalTime.of(10, 0);
        
        Cita cita1 = new Cita("Consulta", "Dr. Test", fecha, hora, "Test 1");
        Cita cita2 = new Cita("Consulta", "Dr. Test", fecha, hora, "Test 2");
        
        repositorio.guardar(cita1);
        boolean resultado = repositorio.guardar(cita2);
        
        assertFalse(resultado);
        assertEquals(1, repositorio.contarTotal());
    }
    
    @Test
    void buscarPorCodigo_debe_retornar_cita_correcta() {
        LocalDate fecha = LocalDate.now().plusDays(1);
        LocalTime hora = LocalTime.of(10, 0);
        Cita cita = new Cita("Consulta", "Dr. Test", fecha, hora, "Test");
        repositorio.guardar(cita);
        
        Cita encontrada = repositorio.buscarPorCodigo(cita.getCodigo());
        
        assertNotNull(encontrada);
        assertEquals(cita.getCodigo(), encontrada.getCodigo());
    }
    
    @Test
    void buscarPorCodigo_debe_retornar_null_con_codigo_inexistente() {
        Cita encontrada = repositorio.buscarPorCodigo("CITA-NOEXISTE");
        assertNull(encontrada);
    }
    
    @Test
    void obtenerTodas_debe_retornar_lista_completa() {
        LocalDate fecha = LocalDate.now().plusDays(1);
        repositorio.guardar(new Cita("Consulta", "Dr. Test", fecha, LocalTime.of(10, 0), "Test 1"));
        repositorio.guardar(new Cita("Consulta", "Dr. Test", fecha, LocalTime.of(14, 0), "Test 2"));
        
        List<Cita> todas = repositorio.obtenerTodas();
        
        assertEquals(2, todas.size());
    }
    
    @Test
    void obtenerPorPersonal_debe_filtrar_correctamente() {
        LocalDate fecha = LocalDate.now().plusDays(1);
        repositorio.guardar(new Cita("Consulta", "Dr. Juan Pérez", fecha, LocalTime.of(10, 0), "Test"));
        repositorio.guardar(new Cita("Consulta", "Dra. María González", fecha, LocalTime.of(14, 0), "Test"));
        repositorio.guardar(new Cita("Consulta", "Dr. Juan Pérez", fecha, LocalTime.of(16, 0), "Test"));
        
        List<Cita> citasJuan = repositorio.obtenerPorPersonal("Dr. Juan Pérez");
        
        assertEquals(2, citasJuan.size());
        for (Cita cita : citasJuan) {
            assertEquals("Dr. Juan Pérez", cita.getPersonal());
        }
    }
    
    @Test
    void obtenerPorFecha_debe_filtrar_correctamente() {
        LocalDate fecha1 = LocalDate.now().plusDays(1);
        LocalDate fecha2 = LocalDate.now().plusDays(2);
        
        repositorio.guardar(new Cita("Consulta", "Dr. Test", fecha1, LocalTime.of(10, 0), "Test"));
        repositorio.guardar(new Cita("Consulta", "Dr. Test", fecha1, LocalTime.of(14, 0), "Test"));
        repositorio.guardar(new Cita("Consulta", "Dr. Test", fecha2, LocalTime.of(10, 0), "Test"));
        
        List<Cita> citasFecha1 = repositorio.obtenerPorFecha(fecha1);
        
        assertEquals(2, citasFecha1.size());
        for (Cita cita : citasFecha1) {
            assertEquals(fecha1, cita.getFecha());
        }
    }
    
    @Test
    void obtenerPendientes_debe_retornar_solo_pendientes() {
        LocalDate fecha = LocalDate.now().plusDays(1);
        Cita cita1 = new Cita("Consulta", "Dr. Test", fecha, LocalTime.of(10, 0), "Test");
        Cita cita2 = new Cita("Consulta", "Dr. Test", fecha, LocalTime.of(14, 0), "Test");
        
        cita2.marcarAtendida();
        
        repositorio.guardar(cita1);
        repositorio.guardar(cita2);
        
        List<Cita> pendientes = repositorio.obtenerPendientes();
        
        assertEquals(1, pendientes.size());
        assertEquals(Cita.EstadoCita.PENDIENTE, pendientes.get(0).getEstado());
    }
    
    @Test
    void estaOcupado_debe_retornar_true_con_horario_ocupado() {
        LocalDate fecha = LocalDate.now().plusDays(1);
        LocalTime hora = LocalTime.of(10, 0);
        
        repositorio.guardar(new Cita("Consulta", "Dr. Test", fecha, hora, "Test"));
        
        boolean ocupado = repositorio.estaOcupado("Dr. Test", fecha, hora);
        
        assertTrue(ocupado);
    }
    
    @Test
    void estaOcupado_debe_retornar_false_con_horario_libre() {
        LocalDate fecha = LocalDate.now().plusDays(1);
        LocalTime hora = LocalTime.of(10, 0);
        
        boolean ocupado = repositorio.estaOcupado("Dr. Test", fecha, hora);
        
        assertFalse(ocupado);
    }
    
    @Test
    void obtenerHorariosOcupados_debe_retornar_lista_correcta() {
        LocalDate fecha = LocalDate.now().plusDays(1);
        repositorio.guardar(new Cita("Consulta", "Dr. Test", fecha, LocalTime.of(10, 0), "Test"));
        repositorio.guardar(new Cita("Consulta", "Dr. Test", fecha, LocalTime.of(14, 0), "Test"));
        
        List<LocalTime> ocupados = repositorio.obtenerHorariosOcupados("Dr. Test", fecha);
        
        assertEquals(2, ocupados.size());
        assertTrue(ocupados.contains(LocalTime.of(10, 0)));
        assertTrue(ocupados.contains(LocalTime.of(14, 0)));
    }
    
    @Test
    void contarPorEstado_debe_contar_correctamente() {
        LocalDate fecha = LocalDate.now().plusDays(1);
        Cita cita1 = new Cita("Consulta", "Dr. Test", fecha, LocalTime.of(10, 0), "Test");
        Cita cita2 = new Cita("Consulta", "Dr. Test", fecha, LocalTime.of(14, 0), "Test");
        Cita cita3 = new Cita("Consulta", "Dr. Test", fecha, LocalTime.of(16, 0), "Test");
        
        cita2.marcarAtendida();
        cita3.cancelar();
        
        repositorio.guardar(cita1);
        repositorio.guardar(cita2);
        repositorio.guardar(cita3);
        
        assertEquals(1, repositorio.contarPorEstado(Cita.EstadoCita.PENDIENTE));
        assertEquals(1, repositorio.contarPorEstado(Cita.EstadoCita.ATENDIDA));
        assertEquals(1, repositorio.contarPorEstado(Cita.EstadoCita.CANCELADA));
    }
    
    @Test
    void limpiar_debe_eliminar_todas_las_citas() {
        LocalDate fecha = LocalDate.now().plusDays(1);
        repositorio.guardar(new Cita("Consulta", "Dr. Test", fecha, LocalTime.of(10, 0), "Test"));
        repositorio.guardar(new Cita("Consulta", "Dr. Test", fecha, LocalTime.of(14, 0), "Test"));
        
        repositorio.limpiar();
        
        assertEquals(0, repositorio.contarTotal());
    }
}