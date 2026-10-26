# Documentación SPI Mixer VR

## Descripción General

Esta carpeta contiene la documentación técnica y funcional del sistema **SPI Mixer VR** (Visor Remoto Mixer), una aplicación Android para la gestión automatizada de alimentación de ganado mediante mixers inteligentes.

## Documentos Disponibles

### 📋 [¿Qué es un Agente?](./QUE_ES_UN_AGENTE.md)
Explicación completa sobre el concepto de "agente" en el sistema, incluyendo roles, responsabilidades y flujo de trabajo típico del operario.

### 👥 [Roles de Usuario](./ROLES_DE_USUARIO.md)
Descripción detallada de todos los roles de usuario en el sistema, desde Operarios hasta Super Administradores, incluyendo permisos y restricciones.

### 📖 [Glosario de Términos](./GLOSARIO.md)
Definiciones de todos los términos técnicos y conceptos utilizados en el sistema SPI Mixer VR.

## Sistema SPI Mixer VR

### ¿Qué es?
SPI Mixer VR es un sistema integral de gestión de alimentación para ganado que combina:
- **Hardware:** Mixers (mezcladores) con balanzas integradas
- **Conectividad:** Bluetooth, WiFi, GPS, RFID
- **Software:** Aplicación Android para tablets
- **Gestión:** Sistema centralizado con sincronización en la nube

### Componentes Principales:

1. **Mixers Inteligentes**
   - Balanzas integradas de alta precisión
   - Conectividad Bluetooth
   - Sistema de calibración automática

2. **Tablet Android**
   - Aplicación SPI Mixer VR
   - Interfaz táctil resistente
   - Conectividad múltiple (WiFi, Bluetooth, GPS)

3. **Sistema de Gestión**
   - Base de datos local y remota
   - Sincronización automática
   - Reportes y análisis

### Flujo Operativo General:

```
Planificación Nutricional → Configuración de Rondas → Ejecución por Agente
                      ↓
Sincronización de Datos ← Registro en Tiempo Real ← Operación de Mixer
```

## Usuarios del Sistema

### Agentes (Operarios)
Son los usuarios principales que ejecutan las operaciones diarias de alimentación del ganado. Operan los mixers, ejecutan rondas y registran datos en tiempo real.

### Administradores
Gestionan la configuración del sistema, usuarios, establecimientos y equipos.

### Supervisores
Supervisan las operaciones y tienen acceso a reportes y estadísticas.

### Nutricionistas
Especializados en la creación de dietas y formulación nutricional.

## Características Técnicas

### Tecnologías Utilizadas:
- **Android SDK** - Desarrollo nativo para tablets
- **Kotlin** - Lenguaje de programación principal
- **Room Database** - Base de datos local SQLite
- **Bluetooth SDK** - Comunicación con mixers
- **GPS/Location Services** - Geolocalización de corrales
- **RFID** - Identificación automática

### Arquitectura:
- **MVVM Pattern** - Model-View-ViewModel
- **Repository Pattern** - Abstracción de datos
- **LiveData/Flow** - Programación reactiva
- **Coroutines** - Programación asíncrona

## Instalación y Configuración

### Requisitos del Sistema:
- Android 7.0 (API 24) o superior
- Bluetooth 4.0+ con BLE support
- GPS habilitado
- Conexión WiFi o móvil para sincronización
- Mínimo 2GB RAM, 16GB almacenamiento

### Configuración Inicial:
1. Instalación de la aplicación APK
2. Configuración de URL del servidor
3. Registro/login de usuario
4. Vinculación de mixers via Bluetooth
5. Sincronización inicial de datos

## Soporte y Mantenimiento

### Logs y Debugging:
- La aplicación genera logs detallados
- Sistema de reportes de errores
- Monitoreo de conectividad y sincronización

### Backup y Recuperación:
- Sincronización automática con servidor
- Exportación de datos locales
- Restauración desde respaldos remotos

### Actualizaciones:
- Sistema de versionado de aplicación
- Actualización de datos desde servidor
- Migración de base de datos automática

## Contacto y Desarrollo

Para más información técnica o soporte, consultar:
- Código fuente en el repositorio Git
- Documentación de API en el servidor
- Logs de la aplicación para debugging

---

**Versión:** SPI Mixer VR v1.0  
**Última actualización:** 2024  
**Desarrollado por:** Básculas Magris