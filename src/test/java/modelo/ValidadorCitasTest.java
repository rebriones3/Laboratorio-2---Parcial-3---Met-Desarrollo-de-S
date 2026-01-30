package modelo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para ValidadorCitas
 * Valida las reglas de negocio del sistema
 */
class ValidadorCitasTest {
    
    private List<Cita> citasExistentes;
    private LocalDate fechaFutura;
    private LocalTime horaFutura;
    
    @BeforeEach
    void setUp() {
        citasExistentes = new ArrayList<>();
        fechaFutura = LocalDate.now().plusDays(1);
        horaFutura = LocalTime.of(10, 0);
    }
    
    @Test
    void validarCamposObligatorios_debe_retornar_true_con_campos_completos() {
        boolean resultado = ValidadorCitas.validarCamposObligatorios(
            "Consulta", "Dr. Test", fechaFutura, horaFutura
        );
        assertTrue(resultado);
    }
    
    @Test
    void validarCamposObligatorios_debe_retornar_false_con_servicio_nulo() {
        boolean resultado = ValidadorCitas.validarCamposObligatorios(
            null, "Dr. Test", fechaFutura, horaFutura
        );
        assertFalse(resultado);
    }
    
    @Test
    void validarCamposObligatorios_debe_retornar_false_con_servicio_vacio() {
        boolean resultado = ValidadorCitas.validarCamposObligatorios(
            "   ", "Dr. Test", fechaFutura, horaFutura
        );
        assertFalse(resultado);
    }
    
    @Test
    void validarCamposObligatorios_debe_retornar_false_con_personal_nulo() {
        boolean resultado = ValidadorCitas.validarCamposObligatorios(
            "Consulta", null, fechaFutura, horaFutura
        );
        assertFalse(resultado);
    }
    
    @Test
    void validarCamposObligatorios_debe_retornar_false_con_fecha_nula() {
        boolean resultado = ValidadorCitas.validarCamposObligatorios(
            "Consulta", "Dr. Test", null, horaFutura
        );
        assertFalse(resultado);
    }
    
    @Test
    void validarCamposObligatorios_debe_retornar_false_con_hora_nula() {
        boolean resultado = ValidadorCitas.validarCamposObligatorios(
            "Consulta", "Dr. Test", fechaFutura, null
        );
        assertFalse(resultado);
    }
    
    @Test
    void validarFechaNoEnPasado_debe_retornar_true_con_fecha_futura() {
        assertTrue(ValidadorCitas.validarFechaNoEnPasado(fechaFutura));
    }
    
    @Test
    void validarFechaNoEnPasado_debe_retornar_true_con_fecha_hoy() {
        assertTrue(ValidadorCitas.validarFechaNoEnPasado(LocalDate.now()));
    }
    
    @Test
    void validarFechaNoEnPasado_debe_retornar_false_con_fecha_pasada() {
        LocalDate fechaPasada = LocalDate.now().minusDays(1);
        assertFalse(ValidadorCitas.validarFechaNoEnPasado(fechaPasada));
    }
    
    @Test
    void validarFechaNoEnPasado_debe_retornar_false_con_fecha_nula() {
        assertFalse(ValidadorCitas.validarFechaNoEnPasado(null));
    }
    
    @Test
    void validarHorarioNoEnPasado_debe_retornar_true_con_horario_futuro() {
        assertTrue(ValidadorCitas.validarHorarioNoEnPasado(fechaFutura, horaFutura));
    }
    
    @Test
    void validarHorarioNoEnPasado_debe_retornar_false_con_horario_pasado() {
        LocalDate hoy = LocalDate.now();
        LocalTime horaPasada = LocalTime.now().minusHours(1);
        assertFalse(ValidadorCitas.validarHorarioNoEnPasado(hoy, horaPasada));
    }
    
    @Test
    void validarNoSolapamiento_debe_retornar_true_sin_conflictos() {
        boolean resultado = ValidadorCitas.validarNoSolapamiento(
            "Dr. Test", fechaFutura, horaFutura, citasExistentes
        );
        assertTrue(resultado);
    }
    
    @Test
    void validarNoSolapamiento_debe_retornar_false_con_horario_ocupado() {
        Cita citaExistente = new Cita("Consulta", "Dr. Test", 
                                      fechaFutura, horaFutura, "Test");
        citasExistentes.add(citaExistente);
        
        boolean resultado = ValidadorCitas.validarNoSolapamiento(
            "Dr. Test", fechaFutura, horaFutura, citasExistentes
        );
        assertFalse(resultado);
    }
    
    @Test
    void validarNoSolapamiento_debe_retornar_true_con_personal_diferente() {
        Cita citaExistente = new Cita("Consulta", "Dr. Otro", 
                                      fechaFutura, horaFutura, "Test");
        citasExistentes.add(citaExistente);
        
        boolean resultado = ValidadorCitas.validarNoSolapamiento(
            "Dr. Test", fechaFutura, horaFutura, citasExistentes
        );
        assertTrue(resultado);
    }
    
    @Test
    void validarNoSolapamiento_debe_retornar_true_con_cita_cancelada() {
        Cita citaCancelada = new Cita("Consulta", "Dr. Test", 
                                      fechaFutura, horaFutura, "Test");
        citaCancelada.cancelar();
        citasExistentes.add(citaCancelada);
        
        boolean resultado = ValidadorCitas.validarNoSolapamiento(
            "Dr. Test", fechaFutura, horaFutura, citasExistentes
        );
        assertTrue(resultado);
    }
    
    @Test
    void validarPoliticaCancelacion_debe_retornar_true_con_mas_de_2_horas() {
        LocalDateTime fechaHoraCita = LocalDateTime.now().plusHours(3);
        assertTrue(ValidadorCitas.validarPoliticaCancelacion(fechaHoraCita));
    }
    
    @Test
    void validarPoliticaCancelacion_debe_retornar_false_con_menos_de_2_horas() {
        LocalDateTime fechaHoraCita = LocalDateTime.now().plusHours(1);
        assertFalse(ValidadorCitas.validarPoliticaCancelacion(fechaHoraCita));
    }
    
    @Test
    void validarCitaCompleta_debe_retornar_NINGUNO_con_datos_validos() {
        ValidadorCitas.TipoError error = ValidadorCitas.validarCitaCompleta(
            "Consulta", "Dr. Test", fechaFutura, horaFutura, citasExistentes
        );
        assertEquals(ValidadorCitas.TipoError.NINGUNO, error);
    }
    
    @Test
    void validarCitaCompleta_debe_retornar_CAMPOS_INCOMPLETOS() {
        ValidadorCitas.TipoError error = ValidadorCitas.validarCitaCompleta(
            null, "Dr. Test", fechaFutura, horaFutura, citasExistentes
        );
        assertEquals(ValidadorCitas.TipoError.CAMPOS_INCOMPLETOS, error);
    }
    
    @Test
    void validarCitaCompleta_debe_retornar_FECHA_PASADA() {
        LocalDate fechaPasada = LocalDate.now().minusDays(1);
        ValidadorCitas.TipoError error = ValidadorCitas.validarCitaCompleta(
            "Consulta", "Dr. Test", fechaPasada, horaFutura, citasExistentes
        );
        assertEquals(ValidadorCitas.TipoError.FECHA_PASADA, error);
    }
    
    @Test
    void validarCitaCompleta_debe_retornar_HORARIO_OCUPADO() {
        Cita citaExistente = new Cita("Consulta", "Dr. Test", 
                                      fechaFutura, horaFutura, "Test");
        citasExistentes.add(citaExistente);
        
        ValidadorCitas.TipoError error = ValidadorCitas.validarCitaCompleta(
            "Consulta", "Dr. Test", fechaFutura, horaFutura, citasExistentes
        );
        assertEquals(ValidadorCitas.TipoError.HORARIO_OCUPADO, error);
    }
    
    @Test
    void obtenerMensajeError_debe_retornar_mensaje_correcto() {
        String mensaje = ValidadorCitas.obtenerMensajeError(
            ValidadorCitas.TipoError.CAMPOS_INCOMPLETOS
        );
        assertNotNull(mensaje);
        assertTrue(mensaje.contains("campos obligatorios"));
    }
}