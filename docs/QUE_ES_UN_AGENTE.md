# ¿Qué es un Agente?

## Introducción

En el contexto del sistema **SPI Mixer VR** (Visor Remoto Mixer), un **agente** es un tipo de usuario que desempeña un rol específico dentro del sistema de gestión de mezclas de alimentos para ganado. Este documento explica en detalle qué es un agente y su función dentro del ecosistema de la aplicación.

## Definición de Agente

Un **agente** en el sistema SPI Mixer VR es un usuario operativo que tiene la responsabilidad de:

1. **Ejecutar las rondas de alimentación** programadas para el ganado
2. **Operar los mixers** (mezcladores) de alimentos
3. **Supervisar el proceso de carga y descarga** de alimentos
4. **Registrar datos** en tiempo real durante las operaciones
5. **Sincronizar información** con el servidor central

## Roles de Usuario en el Sistema

El sistema SPI Mixer VR maneja diferentes tipos de usuarios con distintos niveles de acceso:

### 1. **Administrador** (Código: 1)
- Control total del sistema
- Gestión de usuarios, mixers, dietas y establecimientos
- Configuración global del sistema

### 2. **Operario** (Código: 2) - **AGENTE PRINCIPAL**
- Usuario que opera directamente los mixers
- Ejecuta las rondas de alimentación
- Registra datos de pesaje y mezcla
- Interactúa con la balanza y sistemas Bluetooth

### 3. **Supervisor** (Código: 3)
- Supervisa las operaciones de los operarios
- Acceso a reportes y estadísticas
- Puede ejecutar rondas cuando sea necesario

### 4. **Super Admin** (Código: 4)
- Nivel más alto de acceso
- Gestión completa del sistema y configuraciones avanzadas

### 5. **Nutricionista** (Código: 5)
- Especializado en la creación y gestión de dietas
- No puede ejecutar rondas directamente
- Enfocado en la composición nutricional

### 6. **API** (Código: 6)
- Usuario técnico para integraciones de sistema

### 7. **Técnico** (Código: 7)
- Soporte técnico y mantenimiento del sistema

## Características del Agente (Operario)

### Responsabilidades Principales:

1. **Gestión de Rondas:**
   - Iniciar y finalizar rondas de alimentación
   - Seguir las secuencias programadas de productos
   - Registrar pesos y cantidades reales vs. planificadas

2. **Operación de Mixers:**
   - Conectar y calibrar mixers via Bluetooth
   - Controlar el proceso de carga de productos
   - Gestionar la descarga en corrales específicos

3. **Control de Calidad:**
   - Verificar pesajes y mediciones
   - Asegurar la correcta proporción de mezcla
   - Reportar incidencias o anomalías

4. **Registro de Datos:**
   - Capturar información en tiempo real
   - Sincronizar datos con el servidor
   - Mantener trazabilidad de las operaciones

### Flujo de Trabajo Típico del Agente:

```
1. Inicio de Sesión → 2. Selección de Mixer → 3. Calibración de Balanza
                 ↓
4. Selección de Ronda → 5. Carga de Productos → 6. Proceso de Mezcla
                 ↓
7. Descarga en Corrales → 8. Registro de Datos → 9. Finalización de Ronda
```

## Herramientas del Agente

### Dispositivos y Conectividad:
- **Tablet Android** con la aplicación SPI Mixer VR
- **Conexión Bluetooth** con el mixer y balanzas
- **GPS** para localización de corrales
- **RFID** para identificación automática
- **Conectividad WiFi/Internet** para sincronización

### Funcionalidades de la Aplicación:
- **Visor en Tiempo Real** de pesos y mezclas
- **Configuración de Rondas** personalizadas o programadas
- **Reportes de Producción** y estadísticas
- **Sincronización Automática** con el servidor central
- **Modo Offline** para trabajar sin conectividad

## Importancia del Agente en el Sistema

El agente es el **usuario final crítico** que conecta la planificación nutricional con la ejecución práctica en el campo. Su trabajo asegura:

- **Precisión en la alimentación** del ganado
- **Trazabilidad completa** del proceso productivo
- **Optimización de recursos** y costos
- **Cumplimiento de protocolos** nutricionales
- **Calidad y consistencia** en la producción

## Capacitación del Agente

Para ser efectivo, un agente debe estar capacitado en:

1. **Uso de la aplicación móvil** SPI Mixer VR
2. **Operación de mixers** y balanzas
3. **Protocolos de seguridad** alimentaria
4. **Gestión de datos** y reportes
5. **Mantenimiento básico** de equipos
6. **Procedimientos de emergencia** y contingencias

## Conclusión

El **agente** en el sistema SPI Mixer VR es fundamentalmente el **operario especializado** que ejecuta las operaciones diarias de alimentación del ganado. Es el puente entre la tecnología y la producción, asegurando que los planes nutricionales se traduzcan en alimentación efectiva y registrada del ganado. Su rol es esencial para el éxito del sistema de gestión integral de alimentación animal.