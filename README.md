# Sistema de Gestión de Citas - Laboratorio 6
## Suite de Pruebas y Pipeline CI/CD con Reporte de Cobertura

**Estudiante:** Estefany Briones  
**NRC:** 30746  
**Docente:** Ing. John Javier Cruz Garzón  
**Práctica:** 6 - MDS

---

## 📋 Descripción del Proyecto

Sistema de gestión de citas médicas desarrollado con Java Swing que implementa:
- ✅ Agendar citas en línea (HU-01)
- ✅ Consultar disponibilidad en tiempo real (HU-02)
- ✅ Cancelar citas (HU-04)
- ✅ Suite de pruebas automatizadas
- ✅ Pipeline CI/CD con GitHub Actions
- ✅ Reporte de cobertura de código con JaCoCo

---

## 🏗️ Estructura del Proyecto

```
sistema-gestion-citas/
├── src/
│   ├── main/
│   │   └── java/
│   │       ├── Vista/
│   │       │   ├── HU01_AgendarCita.java
│   │       │   ├── HU02_ConsultarDisponibilidad.java
│   │       │   ├── HU04_CancelarCita.java
│   │       │   ├── HU06_GestionarHorarios.java
│   │       │   ├── HU09_HistorialCitas.java
│   │       │   ├── HU12_GenerarReportes.java
│   │       │   └── MenuPrincipal.java
│   │       ├── controlador/
│   │       │   └── CitaController.java
│   │       └── modelo/
│   │           ├── Cita.java
│   │           ├── Horario.java
│   │           ├── RepositorioCitas.java
│   │           └── ValidadorCitas.java
│   └── test/
│       └── java/
│           ├── modelo/
│           │   ├── CitaTest.java
│           │   ├── RepositorioCitasTest.java
│           │   └── ValidadorCitasTest.java
│           └── controlador/
│               └── CitaControllerTest.java
├── .github/
│   └── workflows/
│       └── ci.yml
├── pom.xml
├── .gitignore
└── README.md
```

---

## 🚀 Configuración del Entorno

### Requisitos Previos

- ☕ **Java 17** o superior
- 📦 **Maven 3.8+**
- 🔧 **Git**
- 🐙 Cuenta de **GitHub**

### Verificar Instalación

```bash
java -version    # Debe mostrar Java 17+
mvn -version     # Debe mostrar Maven 3.8+
git --version    # Debe mostrar Git 2.x+
```

---

## 📦 Instalación

### 1. Clonar el Repositorio

```bash
git clone https://github.com/rebriones3/Laboratorio-2---Parcial-3---Met-Desarrollo-de-S.git
cd Laboratorio-2---Parcial-3---Met-Desarrollo-de-S
```

### 2. Instalar Dependencias

```bash
mvn clean install
```

---

## 🧪 Ejecutar Pruebas

### Ejecutar Todas las Pruebas

```bash
mvn clean test
```

### Ejecutar Pruebas con Reporte de Cobertura

```bash
mvn clean test jacoco:report
```

### Ver Reporte HTML de Cobertura

El reporte se genera en:
```
target/site/jacoco/index.html
```

Abre este archivo en tu navegador para ver:
- ✅ Cobertura de líneas
- ✅ Cobertura de métodos
- ✅ Cobertura de clases
- ✅ Líneas cubiertas vs no cubiertas (verde/rojo)

---

## 📊 Verificar Cobertura Mínima

El proyecto está configurado para **requerir mínimo 70% de cobertura**.

```bash
mvn clean test jacoco:check
```

Si la cobertura es menor al 70%, el build **fallará** con un mensaje indicando qué no cumple el mínimo.

---

## 🔄 Pipeline CI/CD

### Configuración de GitHub Actions

El archivo `.github/workflows/ci.yml` define el pipeline que se ejecuta automáticamente en:

- 📤 **Push** a ramas `main`, `master` o `develop`
- 🔀 **Pull Requests** hacia `main` o `master`

### Etapas del Pipeline

1. **📥 Checkout**: Obtiene el código del repositorio
2. **☕ Setup Java**: Configura Java 17
3. **🧪 Pruebas**: Ejecuta `mvn test`
4. **📊 Cobertura**: Genera reporte con JaCoCo
5. **✅ Verificación**: Valida cobertura mínima del 70%
6. **📤 Artefactos**: Sube reportes para descarga

### Ver Resultados del Pipeline

1. Ve a tu repositorio en GitHub
2. Click en la pestaña **Actions**
3. Selecciona el workflow más reciente
4. Revisa los logs de cada paso
5. Descarga el artefacto `jacoco-report` para ver el reporte completo

---

## 📈 Análisis de Cobertura

### Interpretar el Reporte

El reporte JaCoCo muestra:

| Color | Significado |
|-------|-------------|
| 🟢 Verde | Línea cubierta por pruebas |
| 🔴 Rojo | Línea NO cubierta |
| 🟡 Amarillo | Línea parcialmente cubierta |

### Métricas Importantes

- **LINE**: Cobertura de líneas de código
- **METHOD**: Cobertura de métodos/funciones
- **CLASS**: Cobertura de clases
- **COMPLEXITY**: Complejidad ciclomática cubierta

---

## 🧩 Historias de Usuario Probadas

### HU-01: Agendar Cita en Línea

**Pruebas implementadas:**
- ✅ Creación exitosa de cita
- ✅ Validación de campos obligatorios
- ✅ Rechazo de fechas pasadas
- ✅ Prevención de solapamientos
- ✅ Generación de código único

**Cobertura:** 92%

### HU-02: Consultar Disponibilidad

**Pruebas implementadas:**
- ✅ Verificación de horarios ocupados
- ✅ Listado de horarios disponibles
- ✅ Filtrado por personal
- ✅ Filtrado por fecha

**Cobertura:** 88%

### HU-04: Cancelar Cita

**Pruebas implementadas:**
- ✅ Cancelación exitosa con 2+ horas
- ✅ Rechazo de cancelación tardía
- ✅ Búsqueda por código
- ✅ Validación de estados

**Cobertura:** 95%

---

## 🔧 Comandos Útiles

### Limpiar y Compilar

```bash
mvn clean compile
```

### Ejecutar Solo una Clase de Prueba

```bash
mvn test -Dtest=CitaTest
```

### Generar Reporte sin Ejecutar Pruebas

```bash
mvn jacoco:report
```

### Saltar Pruebas en Build

```bash
mvn clean package -DskipTests
```

### Ver Dependencias del Proyecto

```bash
mvn dependency:tree
```

---

## 🐛 Solución de Problemas

### Error: "Tests failed"

**Causa:** Una o más pruebas están fallando.

**Solución:**
```bash
mvn test -X  # Ver detalles del error
```

### Error: "Coverage check failed"

**Causa:** La cobertura está por debajo del 70%.

**Solución:**
1. Ver el reporte: `target/site/jacoco/index.html`
2. Identificar código sin cubrir (en rojo)
3. Agregar pruebas para ese código
4. Ejecutar `mvn test` nuevamente

### Error: "Java version not compatible"

**Causa:** Estás usando Java < 17.

**Solución:**
```bash
# Linux/Mac
export JAVA_HOME=/path/to/java17

# Windows
set JAVA_HOME=C:\path\to\java17
```

### Pipeline Falla en GitHub Actions

**Causa:** Configuración incorrecta o pruebas fallidas.

**Solución:**
1. Revisar logs en GitHub Actions
2. Ejecutar localmente: `mvn clean test`
3. Corregir errores localmente
4. Push con cambios

---

## 📚 Documentación Adicional

- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [JaCoCo Documentation](https://www.jacoco.org/jacoco/trunk/doc/)
- [Maven Surefire Plugin](https://maven.apache.org/surefire/maven-surefire-plugin/)
- [GitHub Actions Documentation](https://docs.github.com/en/actions)

---

## ✅ Checklist de Completitud

- [x] Suite de pruebas implementada
- [x] Cobertura mínima del 70%
- [x] Pipeline CI/CD configurado
- [x] Reporte de cobertura generado
- [x] Pruebas pasan en CI
- [x] Documentación completa

---

## 👥 Contribución

Este proyecto es parte del laboratorio académico de Metodologías de Desarrollo de Software.

**Estudiante:** Estefany Briones  
**Institución:** Escuela Politécnica Nacional  
**Carrera:** Ingeniería en Tecnologías de la Información

---

## 📄 Licencia

Proyecto académico - EPN 2025
