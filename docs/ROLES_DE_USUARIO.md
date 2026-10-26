# Roles de Usuario en SPI Mixer VR

## Descripción General

El sistema SPI Mixer VR (Visor Remoto Mixer) implementa un sistema de roles jerárquico que define los permisos y responsabilidades de cada tipo de usuario. Estos roles están codificados en la aplicación y determinan qué funcionalidades puede acceder cada usuario.

## Códigos de Roles Definidos

### ADMINISTRADOR (Código: 1)
**Descripción:** Usuario con control administrativo completo del sistema.

**Responsabilidades:**
- Gestión completa de usuarios (crear, editar, eliminar)
- Configuración de mixers y equipos
- Administración de establecimientos y corrales
- Gestión de productos y dietas
- Configuración global del sistema
- Acceso a todos los reportes y estadísticas

**Permisos:**
- ✅ Crear/editar/eliminar usuarios
- ✅ Configurar mixers
- ✅ Gestionar establecimientos
- ✅ Ejecutar rondas
- ✅ Acceso a reportes completos
- ✅ Configuración del sistema

---

### OPERARIO (Código: 2) - **AGENTE**
**Descripción:** Usuario operativo que ejecuta las tareas diarias de alimentación.

**Responsabilidades:**
- Operar mixers y balanzas
- Ejecutar rondas de alimentación
- Registrar datos de producción
- Calibrar equipos
- Sincronizar información

**Permisos:**
- ✅ Ejecutar rondas
- ✅ Operar mixers
- ✅ Registrar datos de pesaje
- ✅ Calibrar balanzas
- ✅ Sincronizar datos
- ❌ Gestionar usuarios
- ❌ Configuración del sistema

---

### SUPERVISOR (Código: 3)
**Descripción:** Usuario con capacidades de supervisión y control operativo.

**Responsabilidades:**
- Supervisar operaciones de operarios
- Revisar y validar reportes
- Ejecutar rondas cuando sea necesario
- Controlar calidad de datos

**Permisos:**
- ✅ Ejecutar rondas
- ✅ Acceso a reportes
- ✅ Supervisar operaciones
- ✅ Validar datos
- ⚠️ Gestión limitada de usuarios
- ❌ Configuración global

---

### SUPER ADMIN (Código: 4)
**Descripción:** Nivel máximo de acceso con permisos especiales del sistema.

**Responsabilidades:**
- Control total del sistema
- Configuraciones avanzadas
- Mantenimiento de base de datos
- Gestión de integraciones
- Soporte técnico avanzado

**Permisos:**
- ✅ Todos los permisos de Administrador
- ✅ Configuraciones avanzadas
- ✅ Mantenimiento de BD
- ✅ Borrar datos del sistema
- ✅ Configurar URLs de servidor
- ✅ Exportar/importar datos

---

### NUTRICIONISTA (Código: 5)
**Descripción:** Especialista en nutrición animal y formulación de dietas.

**Responsabilidades:**
- Crear y gestionar dietas
- Definir composición de productos
- Análisis nutricional
- Optimización de fórmulas

**Permisos:**
- ✅ Gestionar dietas
- ✅ Configurar productos
- ✅ Acceso a reportes nutricionales
- ✅ Análisis de composición
- ❌ Ejecutar rondas
- ❌ Operar mixers

**Nota Especial:** Los nutricionistas NO pueden ejecutar rondas directamente, ya que su rol está enfocado en la planificación nutricional.

---

### API (Código: 6)
**Descripción:** Usuario técnico para integraciones y servicios automatizados.

**Responsabilidades:**
- Integraciones de sistemas
- Sincronización automática
- Servicios web
- Procesamiento batch

**Permisos:**
- ✅ Acceso a API endpoints
- ✅ Sincronización de datos
- ✅ Servicios automatizados
- ❌ Interfaz de usuario
- ❌ Operaciones manuales

---

### TÉCNICO (Código: 7)
**Descripción:** Personal de soporte técnico y mantenimiento.

**Responsabilidades:**
- Mantenimiento de equipos
- Soporte técnico
- Calibración de sistemas
- Diagnóstico de problemas

**Permisos:**
- ✅ Configurar equipos
- ✅ Calibrar sistemas
- ✅ Diagnósticos
- ✅ Mantenimiento
- ⚠️ Acceso limitado a datos operativos
- ❌ Gestión de usuarios

## Jerarquía de Permisos

```
Super Admin (4)
    ↓
Administrador (1)
    ↓
Supervisor (3)
    ↓
Operario/Agente (2)
    ↓
Técnico (7) / Nutricionista (5) / API (6)
```

## Consideraciones Especiales

### Restricciones por Rol:
- **Nutricionistas** no pueden ejecutar rondas
- **Operarios** no pueden gestionar usuarios
- **API** solo para acceso programático
- **Técnicos** acceso limitado a datos de producción

### Seguridad:
- Cada rol tiene permisos específicos codificados
- Autenticación requerida para todas las operaciones
- Logs de auditoría por usuario y acción
- Sesiones con timeout automático

### Flexibilidad:
- Los roles pueden combinarse según necesidades
- Permisos temporales para casos especiales
- Configuración por establecimiento si es necesario

## Implementación en el Código

Los códigos de rol se encuentran definidos en:
- `Constants.kt` - Definiciones de constantes
- `UserAdapter.kt` - Mapeo de roles para UI
- `MainActivity.kt` - Control de acceso por rol
- Base de datos local y remota

```kotlin
const val USER_ADMIN = 1
const val USER_OPERATOR = 2
const val USER_SUPERVISOR = 3
const val USER_SUPER_ADMIN = 4
const val USER_NUTRICIONIST = 5
const val USER_API = 6
const val USER_TECHNICIAN = 7
```