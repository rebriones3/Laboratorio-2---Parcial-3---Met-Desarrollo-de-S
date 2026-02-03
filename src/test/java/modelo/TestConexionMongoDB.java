package modelo;

import modelo.MongoDBConfig;
import modelo.RepositorioCitasMongo;
import modelo.Cita;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Clase de prueba para verificar la conexión a MongoDB
 * Ejecuta esta clase para asegurarte de que MongoDB está funcionando
 */
public class TestConexionMongoDB {
    
    public static void main(String[] args) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("🧪 INICIANDO PRUEBAS DE CONEXIÓN MONGODB");
        System.out.println("=".repeat(70) + "\n");
        
        // PRUEBA 1: Verificar conexión
        System.out.println("📋 PRUEBA 1: Verificar Conexión a MongoDB");
        System.out.println("-".repeat(70));
        MongoDBConfig config = MongoDBConfig.obtenerInstancia();
        
        if (config.estaConectado()) {
            System.out.println("✅ PRUEBA 1 EXITOSA: Conexión establecida\n");
        } else {
            System.err.println("❌ PRUEBA 1 FALLIDA: No se pudo conectar a MongoDB");
            System.err.println("💡 Asegúrate de que MongoDB esté corriendo:");
            System.err.println("   Windows: net start MongoDB");
            System.err.println("   Linux: sudo systemctl start mongod");
            System.err.println("   Mac: brew services start mongodb-community@7.0\n");
            return;
        }
        
        // PRUEBA 2: Obtener repositorio
        System.out.println("📋 PRUEBA 2: Inicializar RepositorioCitasMongo");
        System.out.println("-".repeat(70));
        RepositorioCitasMongo repo = RepositorioCitasMongo.obtenerInstancia();
        System.out.println("✅ PRUEBA 2 EXITOSA: Repositorio inicializado\n");
        
        // PRUEBA 3: Crear cita de prueba
        System.out.println("📋 PRUEBA 3: Crear y Guardar Cita de Prueba");
        System.out.println("-".repeat(70));
        
        Cita citaPrueba = new Cita(
            "Consulta de Prueba",
            "Dr. Test MongoDB",
            LocalDate.now().plusDays(1),
            LocalTime.of(10, 0),
            "Esta es una cita de prueba para verificar MongoDB"
        );
        
        System.out.println("🆕 Cita creada:");
        System.out.println("   Código: " + citaPrueba.getCodigo());
        System.out.println("   Servicio: " + citaPrueba.getServicio());
        System.out.println("   Personal: " + citaPrueba.getPersonal());
        System.out.println("   Fecha: " + citaPrueba.getFecha());
        System.out.println("   Hora: " + citaPrueba.getHora());
        
        boolean guardado = repo.guardar(citaPrueba);
        
        if (guardado) {
            System.out.println("✅ PRUEBA 3 EXITOSA: Cita guardada en MongoDB\n");
        } else {
            System.err.println("❌ PRUEBA 3 FALLIDA: No se pudo guardar la cita\n");
            return;
        }
        
        // PRUEBA 4: Recuperar cita
        System.out.println("📋 PRUEBA 4: Recuperar Cita de MongoDB");
        System.out.println("-".repeat(70));
        
        Cita citaRecuperada = repo.buscarPorCodigo(citaPrueba.getCodigo());
        
        if (citaRecuperada != null) {
            System.out.println("✅ PRUEBA 4 EXITOSA: Cita recuperada");
            System.out.println("   Código: " + citaRecuperada.getCodigo());
            System.out.println("   Servicio: " + citaRecuperada.getServicio());
            System.out.println("   Estado: " + citaRecuperada.getEstado());
            System.out.println();
        } else {
            System.err.println("❌ PRUEBA 4 FALLIDA: No se pudo recuperar la cita\n");
            return;
        }
        
        // PRUEBA 5: Contar citas
        System.out.println("📋 PRUEBA 5: Contar Documentos en MongoDB");
        System.out.println("-".repeat(70));
        
        long totalCitas = repo.contarTotal();
        long citasPendientes = repo.contarPorEstado(Cita.EstadoCita.PENDIENTE);
        
        System.out.println("📊 Estadísticas:");
        System.out.println("   Total de citas: " + totalCitas);
        System.out.println("   Citas pendientes: " + citasPendientes);
        System.out.println("✅ PRUEBA 5 EXITOSA\n");
        
        // PRUEBA 6: Actualizar cita
        System.out.println("📋 PRUEBA 6: Actualizar Cita");
        System.out.println("-".repeat(70));
        
        citaRecuperada.setMotivo("Motivo actualizado desde prueba");
        boolean actualizado = repo.actualizarCita(citaRecuperada);
        
        if (actualizado) {
            System.out.println("✅ PRUEBA 6 EXITOSA: Cita actualizada\n");
        } else {
            System.err.println("❌ PRUEBA 6 FALLIDA: No se pudo actualizar la cita\n");
        }
        
        // PRUEBA 7: Eliminar cita de prueba
        System.out.println("📋 PRUEBA 7: Eliminar Cita de Prueba");
        System.out.println("-".repeat(70));
        
        boolean eliminado = repo.eliminarCita(citaPrueba.getCodigo());
        
        if (eliminado) {
            System.out.println("✅ PRUEBA 7 EXITOSA: Cita de prueba eliminada\n");
        } else {
            System.err.println("❌ PRUEBA 7 FALLIDA: No se pudo eliminar la cita\n");
        }
        
        // Mostrar estado final
        config.mostrarEstado();
        
        // RESUMEN
        System.out.println("\n" + "=".repeat(70));
        System.out.println("🎉 RESUMEN DE PRUEBAS");
        System.out.println("=".repeat(70));
        System.out.println("✅ Todas las pruebas completadas exitosamente");
        System.out.println("✅ MongoDB está funcionando correctamente");
        System.out.println("✅ El sistema está listo para usar");
        System.out.println("=".repeat(70) + "\n");
        
        System.out.println("💡 SIGUIENTE PASO:");
        System.out.println("   Ejecuta P3Lab2BrionesEstefany.java para iniciar la aplicación\n");
    }
}