# Glosario de Términos - SPI Mixer VR

## A

**AGENTE**  
Usuario operativo principal del sistema, específicamente el **Operario** (código 2) que ejecuta las rondas de alimentación del ganado y opera los mixers en campo. Es el puente entre la planificación nutricional y la ejecución práctica.

**ANDROID**  
Sistema operativo móvil utilizado por las tablets que ejecutan la aplicación SPI Mixer VR.

**API**  
Interfaz de programación de aplicaciones. En el contexto del sistema, es un tipo de usuario (código 6) utilizado para integraciones automáticas.

## B

**BLUETOOTH**  
Tecnología de comunicación inalámbrica utilizada para conectar la tablet con los mixers y balanzas.

**BALANZA**  
Dispositivo de pesaje integrado en los mixers para medir con precisión los ingredientes y mezclas.

## C

**CALIBRACIÓN**  
Proceso de ajuste de la balanza para asegurar mediciones precisas. Incluye tara y peso conocido.

**CORRAL**  
Área donde se encuentra el ganado que recibirá la alimentación. Cada corral puede tener coordenadas GPS y RFID asociados.

## D

**DIETA**  
Formulación nutricional que especifica qué productos y en qué cantidades deben mezclarse para alimentar al ganado.

## E

**ESTABLECIMIENTO**  
Instalación ganadera que contiene múltiples corrales. Unidad organizacional principal del sistema.

## G

**GPS**  
Sistema de posicionamiento global utilizado para localizar corrales automáticamente durante las operaciones.

## M

**MIXER**  
Equipo mezclador de alimentos para ganado equipado con balanza y conectividad Bluetooth. El dispositivo principal que opera el agente.

## N

**NUTRICIONISTA**  
Tipo de usuario (código 5) especializado en la creación de dietas y formulación nutricional. NO puede ejecutar rondas.

## O

**OPERARIO**  
Sinónimo de **AGENTE**. Usuario que opera los mixers y ejecuta las rondas de alimentación (código de rol 2).

## P

**PRODUCTO**  
Ingrediente individual utilizado en las dietas (ej: maíz, soja, vitaminas, etc.).

## R

**RFID**  
Tecnología de identificación por radiofrecuencia utilizada para identificar automáticamente corrales.

**RONDA**  
Secuencia completa de alimentación que incluye la carga de productos, mezcla y descarga en corrales específicos.

**ROL**  
Tipo de usuario con permisos específicos en el sistema (Administrador, Operario, Supervisor, etc.).

## S

**SINCRONIZACIÓN**  
Proceso de envío y recepción de datos entre la tablet local y el servidor central.

**SPI MIXER VR**  
Nombre completo del sistema: "Visor Remoto Mixer" - aplicación Android para gestión de alimentación ganadera.

## T

**TABLET**  
Dispositivo Android portátil utilizado por los agentes para operar el sistema en campo.

**TARA**  
Operación de calibración que establece el peso cero de la balanza (peso del mixer vacío).

## U

**USUARIO**  
Persona autorizada para usar el sistema, clasificada según su rol (Administrador, Operario/Agente, Supervisor, etc.).

**URL**  
Dirección del servidor central donde se almacenan y sincronizan los datos del sistema.

## V

**VISOR REMOTO**  
Interfaz de la tablet que permite visualizar en tiempo real el peso y estado del mixer durante las operaciones.

---

## Códigos de Rol Resumidos

| Código | Rol | Descripción Corta |
|--------|-----|-------------------|
| 1 | Administrador | Gestión completa del sistema |
| 2 | **Operario/AGENTE** | **Usuario operativo principal** |
| 3 | Supervisor | Supervisión de operaciones |
| 4 | Super Admin | Control total del sistema |
| 5 | Nutricionista | Gestión de dietas únicamente |
| 6 | API | Usuario técnico para integraciones |
| 7 | Técnico | Soporte y mantenimiento |

## Estados de Ronda

- **SIN EJECUCIONES PREVIAS** - Ronda nueva, nunca ejecutada
- **CARGA INCOMPLETA** - Ronda en proceso de carga de productos
- **CARGA COMPLETA** - Todos los productos cargados correctamente
- **DESCARGA INCOMPLETA** - Ronda en proceso de descarga en corrales
- **DESCARGA COMPLETA** - Ronda finalizada exitosamente
- **FINALIZADA** - Ronda completada y registrada
- **INCOMPLETA** - Ronda terminada con problemas o faltantes