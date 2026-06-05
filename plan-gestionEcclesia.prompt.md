# Plan: Corrección y mejora de contribuciones y sincronía de módulos

## TL;DR
Revisar y documentar las inconsistencias y errores detectados (anotaciones, fechas, relaciones bidireccionales, DTOs faltantes, secuencias), proponer cambios concretos en archivos clave (`ContribucionDTO`, `ContribucionService`, `Evento`, repositorios y DTOs relacionados) y entregar una lista de tareas para implementar y refactorizar (mappings, validaciones, utilidades). Esto garantiza que la persistencia y la lógica de enlace entre entidades funcionen correctamente.

## Problemas Identificados

### 1. Inconsistencias de tipo de fecha
- **Evento.java**: usa `LocalDate` para `fechaInicio` y `fechaFin`
- **EventoRepository.java**: métodos de consulta usan `LocalDateTime` (incompatible)
- **EventoDTO.java**: usa `LocalDate` pero `@DateTimeFormat(pattern = "yyyy-mm-dd")` está mal (debe ser `yyyy-MM-dd`)
- **ContribucionDTO.java**: idem, patrón incorrecto

### 2. DTOs incompletos e inconsistentes
- **ContribucionDTO.java**: falta campo `eventoId` o `eventoDTO`; falta `tipoContribucionId` o `tipoContribucionDTO`
- **EventoDTO.java**: campo `asistentesIds` es `List<AsistenciaEventoDto>` pero no se sincroniza con `Evento.asistentes`
- Métodos de conversión DTO↔Entity repiten patrones de validación y lógica (ternarios anidados complejos)

### 3. Relaciones bidireccionales sin sincronía completa
- **Evento.java**: tiene método `agregarContribucion()` que sincroniza `contribuciones` y `Contribucion.evento`
- **ContribucionService.updateContribucionFromDTO()**: no llama a `evento.agregarContribucion()`, solo asigna directamente; falta integración con `EventoService`
- **Persona.java**: tiene método `agregarContribucion()` pero no es usado en `ContribucionService`

### 4. Errores lógicos en servicios
- **CiudadService.updateCiudadFromDTO()**: línea 55 usa `!ciudad.getNombreCiudad().isBlank()` sin null-check previo
- **EventoService.updateEventoFromDTO()**: 
  - Línea 61: asigna `fechaInicio` usando `eventoDTO.getFechaInicio()` pero valida la variable local `fechaInicio` (puede haber desfase)
  - Línea 67: valida `!estadoEvento.isEmpty()` cuando `estadoEvento` es `null` (NPE potencial)
  - Línea 77: verifica `accesivilidad==null` pero NO sincroniza cambios en `evento`
- **ContribucionService.updateContribucionFromDTO()**: no maneja `tipoContribucion`, no enlaza con `evento` llamando `evento.agregarContribucion()`

### 5. Métodos generales de conversión duplicados
- Todos los servicios (`BarrioService`, `CiudadService`, `ContribucionService`, etc.) usan patrones idénticos:
  ```java
  
  
  var nombre = dto.getNombre() != null ? dto.getNombre() : entidad.getNombre();
  if(nombre==null) throw else entidad.setNombre(nombre);
  
  ```
- Oportunidad de refactorización: crear utilidad genérica

### 6. Anotaciones y dependencias correctas
- `@RequiredArgsConstructor` está bien escrito en todos los servicios (no hay `@RequiredArsConstructor`)
- `lombok` está correctamente configurado en `build.gradle` (v1.18.30)

### 7. Secuencias y generación de IDs
- **Evento.java**: `@SequenceGenerator(sequenceName = "SEC_ROL")` **ERROR** - debería ser `SEC_EVENTO`
- **Contribucion.java**: `@SequenceGenerator(sequenceName = "SEC_CONTRIBUCION")` - correcto
- Inconsistencia: `Evento` reutiliza secuencia de `Rol`

## Steps de Implementación

### 1. Normalizar tipos de fecha y formato
**Archivos a cambiar:**
- `modele/Evento.java`: confirmar uso de `LocalDate` (solo fecha) o cambiar a `LocalDateTime` (fecha+hora)
- `repositories/EventoRepository.java`: alinear método de consulta con tipo elegido
- `DTOS/EventoDTO.java`: corregir `@DateTimeFormat(pattern = "yyyy-MM-dd")` (patrón correcto)
- `DTOS/ContribucionDTO.java`: corregir `@DateTimeFormat(pattern = "yyyy-MM-dd")`
- `services/EventoService.java`: alinear comparaciones de fecha

**Recomendación:** usar `LocalDate` si solo necesitas fecha (sin hora); cambiar `EventoRepository` para que use `LocalDate` en lugar de `LocalDateTime`.

### 2. Corregir secuencias de generación de IDs
**Archivos a cambiar:**
- `modele/Evento.java`: cambiar `sequenceName = "SEC_ROL"` a `sequenceName = "SEC_EVENTO"`

### 3. Completar y sincronizar DTOs de contribuciones
**Archivos a cambiar:**
- `DTOS/ContribucionDTO.java`: añadir campos:
  - `Long eventoId` (o `EventoDTO eventoDTO` si necesitas todo el evento)
  - `Long tipoContribucionId` (o `TipoContribucionDTO tipoContribucionDTO`)
  - Añadir anotaciones de validación: `@NotNull` para `eventoId` y `tipoContribucionId`

### 4. Mejorar `ContribucionService.updateContribucionFromDTO()`
**Archivo a cambiar:**
- `services/ContribucionService.java`:
  - Añadir parámetro `eventoService` (inyección de dependencia)
  - Añadir parámetro `tipoContribucionService` (inyección de dependencia)
  - En `updateContribucionFromDTO()`:
    - Si `contribucionDTO.getEventoId()` no es null, obtener evento y llamar `evento.agregarContribucion(contribucion)`
    - Si `contribucionDTO.getTipoContribucionId()` no es null, obtener tipo contribución y asignar a `contribucion`
    - Usar `Persona.agregarContribucion()` para sincronizar bidireccional con persona
  - En `convertToDTO()`: asignar `eventoId` y `tipoContribucionId` desde la entidad

### 5. Refactorizar helpers de DTO
**Archivos a crear/cambiar:**
- Crear `utils/DtoMapperUtils.java` con métodos estáticos:
  ```java
  
  public static <T> T requireNonNullElse(T dtoValue, T entityValue, Supplier<T> defaultValue){return ;}
  public static String normalizeString(String dtoValue, String entityValue){return '';}
  public static void assignIfNotNull(Consumer<T> setter, T value, String fieldName){return;}
  ````
- Aplicar a:
  - `services/BarrioService.java`: simplificar `updateBarrioFromDTO()`
  - `services/CiudadService.java`: simplificar `updateCiudadFromDTO()`
  - `services/ContribucionService.java`: simplificar `updateContribucionFromDTO()`

### 6. Corregir errores lógicos en `EventoService`
**Archivo a cambiar:**
- `services/EventoService.java`:
  - Línea 55: cambiar inicialización de `id` para usar la variable `id` en asignación (no reasignar)
  - Línea 61: cambiar `evento.setFechaInicio(eventoDTO.getFechaInicio())` a `evento.setFechaInicio(fechaInicio)` (usar variable local validada)
  - Línea 64-67: refactorizar validación de `fechaFin`; cambiar `evento.setFechaFin(eventoDTO.getFechaFinalizacion())` a `evento.setFechaFin(fechaFin)`
  - Línea 77: cambiar lógica de asignación de `estadoEvento`; usar null-check seguro
  - Línea 88-91: cambiar asignación de `descripcion` para usar variable validada
  - Método `convertToDTO()` línea 158: cambiar lógica de `estadoEvento` (actualmente hace `null` cuando es `true`)

### 7. Corregir errores lógicos en `CiudadService`
**Archivo a cambiar:**
- `services/CiudadService.java`:
  - Línea 55: cambiar `!ciudad.getNombreCiudad().isBlank()` a `ciudad.getNombreCiudad() != null && !ciudad.getNombreCiudad().isBlank()`
  - Línea 44: cambiar método `dalete()` a `delete()` (typo)

### 8. Sincronización de asistentes en Evento
**Archivos a cambiar:**
- `DTOS/EventoDTO.java`: cambiar `asistentesIds` a `List<AsistenciaEventoDto> asistentes` o crear método en `EventoService` para poblar asistentes desde BD
- `services/EventoService.java`: en `convertToDTO()`, agregar lógica para asignar asistentes desde `evento.getAsistentes()`

## Consideraciones Adicionales

### A. Preferencia de tiempo
**Decisión necesaria:** ¿usar `LocalDate` (solo fecha) o `LocalDateTime` (fecha+hora) para `Evento`?
- **LocalDate:** si los eventos solo necesitan saber qué día ocurren (sin importar hora específica)
- **LocalDateTime:** si necesitas precisión de hora (p.ej. eventos que ocurren a cierta hora del día)

**Recomendación:** usa `LocalDate` para simplificar lógica de comparación y BD.

### B. Mappeo automático
Considera agregar **MapStruct** o **ModelMapper** para generar conversiones DTO↔Entity automáticamente:
```gradle
// build.gradle
implementation 'org.mapstruct:mapstruct:1.5.5.Final'
annotationProcessor 'org.mapstruct:mapstruct-processor:1.5.5.Final'
```

### C. Mejoras prácticas futuras
1. Crear `@ControllerAdvice` para manejo centralizado de excepciones (reemplazar throws con respuestas HTTP estándar)
2. Agregar tests unitarios para servicios (p.ej. `ContribucionServiceTest.java`)
3. Usar migraciones DB (Flyway/Liquibase) para cambios de schema (secuencias, columnas)
4. Standardizar nombres: renombrar `dalete` → `delete`, `findByid` → `findById`, `dalete` → `delete` (Case sensitivity)

## Orden de Implementación Recomendado

1. **Fase 1 (Crítica):** Corregir secuencias de IDs (`Evento.java`), tipos de fecha (`Evento.java`, `EventoRepository.java`, DTOs), patrones de formato (@DateTimeFormat)
2. **Fase 2 (Alta):** Completar `ContribucionDTO` con `eventoId` y `tipoContribucionId`; refactorizar `ContribucionService.updateContribucionFromDTO()` con inyección de dependencias
3. **Fase 3 (Media):** Corregir errores lógicos en `EventoService`, `CiudadService`; sincronizar `Persona.agregarContribucion()` en servicios
4. **Fase 4 (Optimización):** Crear `DtoMapperUtils.java`; refactorizar servicios para usar utilidades; añadir tests
5. **Fase 5 (Futuro):** Integrar MapStruct; añadir `@ControllerAdvice`; setup Flyway

## Archivos a Modificar (Resumen)

| Archivo | Cambios | Prioridad |
|---------|---------|-----------|
| `modele/Evento.java` | Secuencia SEC_EVENTO, validar LocalDate vs LocalDateTime | Crítica |
| `repositories/EventoRepository.java` | Alinear tipos de fecha | Crítica |
| `DTOS/EventoDTO.java` | @DateTimeFormat(pattern = "yyyy-MM-dd"), sincronizar asistentes | Media |
| `DTOS/ContribucionDTO.java` | Añadir eventoId, tipoContribucionId, corregir @DateTimeFormat | Alta |
| `services/ContribucionService.java` | Inyectar eventoService, tipoContribucionService; refactorizar updateContribucionFromDTO() | Alta |
| `services/EventoService.java` | Corregir lógica de fechas, estadoEvento, convertToDTO() | Media |
| `services/CiudadService.java` | Corregir null-check línea 55, renombrar dalete → delete | Baja |
| `services/BarrioService.java` | Refactorizar con DtoMapperUtils (Fase 4) | Baja |
| `utils/DtoMapperUtils.java` | Crear (Fase 4) | Optimización |

## Preguntas de Confirmación

1. ¿Usar `LocalDate` (solo fecha) o `LocalDateTime` (fecha+hora) para `Evento`, `Contribucion`, etc.?
2. ¿Modificar `ContribucionDTO` para incluir `eventoId` y `tipoContribucionId`? (recomendado: sí)
3. ¿Implementar MapStruct ahora (Fase 5) o dejar mapeos manuales por ahora?
4. ¿Crear `@ControllerAdvice` centralizado para excepciones? (recomendado: sí para Fase 4+)

