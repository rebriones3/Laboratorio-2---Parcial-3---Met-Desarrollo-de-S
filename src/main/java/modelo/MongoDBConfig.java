package modelo;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

/**
 * Configuración de conexión a MongoDB
 * Gestiona la conexión y proporciona acceso a la base de datos
 * MEJORADO: Mejor manejo de errores y mensajes informativos
 */
public class MongoDBConfig {
    
    private static MongoDBConfig instancia;
    private MongoClient mongoClient;
    private MongoDatabase database;
    private boolean conectado = false;
    
    // Configuración de conexión
    private static final String CONNECTION_STRING = "mongodb://localhost:27017";
    private static final String DATABASE_NAME = "sistema_citas";
    
    /**
     * Constructor privado (Singleton)
     */
    private MongoDBConfig() {
        try {
            System.out.println("\n" + "=".repeat(60));
            System.out.println("🔌 INICIANDO CONEXIÓN A MONGODB");
            System.out.println("=".repeat(60));
            
            // Configurar el codec para trabajar con POJOs (Plain Old Java Objects)
            CodecRegistry pojoCodecRegistry = fromProviders(
                PojoCodecProvider.builder().automatic(true).build()
            );
            
            CodecRegistry codecRegistry = fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                pojoCodecRegistry
            );
            
            // Configurar settings de MongoDB
            MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(CONNECTION_STRING))
                .codecRegistry(codecRegistry)
                .build();
            
            System.out.println("⚙️  Configuración establecida");
            System.out.println("   URL: " + CONNECTION_STRING);
            System.out.println("   Base de datos: " + DATABASE_NAME);
            
            // Crear cliente MongoDB
            mongoClient = MongoClients.create(settings);
            
            // Obtener base de datos
            database = mongoClient.getDatabase(DATABASE_NAME);
            
            // Verificar conexión
            database.listCollectionNames().first();
            conectado = true;
            
            System.out.println("✅ CONEXIÓN EXITOSA");
            System.out.println("📁 Base de datos activa: " + DATABASE_NAME);
            System.out.println("=".repeat(60) + "\n");
            
        } catch (Exception e) {
            conectado = false;
            System.err.println("\n" + "=".repeat(60));
            System.err.println("❌ ERROR AL CONECTAR CON MONGODB");
            System.err.println("=".repeat(60));
            System.err.println("Mensaje: " + e.getMessage());
            System.err.println("\n💡 POSIBLES SOLUCIONES:");
            System.err.println("   1. Verifica que MongoDB esté ejecutándose");
            System.err.println("   2. En Windows: net start MongoDB");
            System.err.println("   3. En Linux/Mac: sudo systemctl start mongod");
            System.err.println("   4. Verifica que el puerto 27017 esté disponible");
            System.err.println("=".repeat(60) + "\n");
            e.printStackTrace();
        }
    }
    
    /**
     * Obtiene la instancia única de la configuración (Singleton)
     */
    public static MongoDBConfig obtenerInstancia() {
        if (instancia == null) {
            synchronized (MongoDBConfig.class) {
                if (instancia == null) {
                    instancia = new MongoDBConfig();
                }
            }
        }
        return instancia;
    }
    
    /**
     * Obtiene la base de datos de MongoDB
     */
    public MongoDatabase getDatabase() {
        if (!conectado) {
            System.err.println("⚠️ WARNING: Intentando usar base de datos sin conexión activa");
        }
        return database;
    }
    
    /**
     * Obtiene el cliente de MongoDB
     */
    public MongoClient getMongoClient() {
        return mongoClient;
    }
    
    /**
     * Cierra la conexión a MongoDB
     */
    public void cerrarConexion() {
        if (mongoClient != null) {
            mongoClient.close();
            conectado = false;
            System.out.println("🔌 Conexión a MongoDB cerrada");
        }
    }
    
    /**
     * Verifica si la conexión está activa
     */
    public boolean estaConectado() {
        try {
            if (mongoClient != null && database != null) {
                // Intentar listar colecciones para verificar conexión
                database.listCollectionNames().first();
                conectado = true;
                return true;
            }
        } catch (Exception e) {
            conectado = false;
            System.err.println("⚠️ Error al verificar conexión: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Muestra el estado de la conexión
     */
    public void mostrarEstado() {
        System.out.println("\n" + "-".repeat(60));
        System.out.println("📊 ESTADO DE CONEXIÓN MONGODB");
        System.out.println("-".repeat(60));
        System.out.println("Estado: " + (conectado ? "✅ CONECTADO" : "❌ DESCONECTADO"));
        System.out.println("URL: " + CONNECTION_STRING);
        System.out.println("Base de datos: " + DATABASE_NAME);
        
        if (conectado && database != null) {
            try {
                long count = database.getCollection("citas", CitaDTO.class).countDocuments();
                System.out.println("Documentos en colección 'citas': " + count);
            } catch (Exception e) {
                System.err.println("Error al contar documentos: " + e.getMessage());
            }
        }
        System.out.println("-".repeat(60) + "\n");
    }
}