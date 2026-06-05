# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**gestionEcclesia** is a Spring Boot 3.4.4 REST API backend for a church management system (EcclesiaDigital). It manages members, events, contributions, finances, and church resources.

- **Java 17**, Gradle, Oracle Database (XE), Spring Security (HTTP Basic, stateless), Lombok, JPA/Hibernate
- Main class: `gestionEcclesia/src/main/java/com/proyectoBase/gestionEcclesia/EcclesiaDigitalApplication.java`
- Server runs on port **8080**

## Commands

All commands run from `gestionEcclesia/` (the inner directory with `build.gradle`):

```powershell
# Build
.\gradlew.bat build

# Run
.\gradlew.bat bootRun

# Test
.\gradlew.bat test

# Clean build
.\gradlew.bat clean build
```

## Architecture

Strict 5-layer architecture. All source code is under:
`src/main/java/com/proyectoBase/gestionEcclesia/`

| Layer | Package | Role |
|-------|---------|------|
| Controllers | `controllers/` | REST endpoints — receive DTOs, delegate to services, return `ResponseEntity<?>` |
| Services | `services/` | Business logic, DTO↔Entity conversion, `@Transactional` |
| Repositories | `repositories/` | `JpaRepository` extensions, custom queries |
| Entities | `modele/` | JPA entities with Oracle sequences (`@SequenceGenerator`) |
| DTOs | `DTOS/` | Request/response objects with Jakarta validation constraints |

**Config** (`config/`): `SecurityConfig`, `GraphQLConfig`, `PasswordEnconderConfig`, `UsuarioContextUtil` (gets authenticated user from security context in services).

### Request flow
```
HTTP client → Controller (DTO in) → Service (entity ops) → Repository → Oracle DB
                                ↑
                        UsuarioContextUtil (auth context)
```

## Security

- HTTP Basic authentication, stateless sessions (no JWT)
- `ROLE_ADMIN` required for `/api/roles/**`, `/api/usuarios/**`, `/api/finanzas/**`
- `/api/auth/register` is public
- All other endpoints require any authenticated user
- Passwords encoded with BCrypt via `PasswordEnconderConfig`

## Database

Oracle XE at `localhost:1521/XEPDB1`, schema user `usuario_prueba`. Hibernate DDL is set to `update` (auto-creates/migrates tables on startup). All entities use Oracle sequences named `SEC_<ENTITY>` (e.g., `SEC_USUARIO`, `SEC_PERSONA`, `SEC_EVENTO`).

## Domain Model

Core entities and their key relationships:

- **Usuario** → has roles (`Rol`), linked to a `Persona`; used for authentication
- **Persona** — church member; belongs to an `Iglesia`; has attendances and contributions
- **Iglesia** — church location; has a pastor (`Persona`), address, and `ZonaAdministrativa`
- **Evento** — church event; has contributions (`Contribucion`) and attendance records (`AsistenciaEvento`)
- **Contribucion** — monetary contribution by a `Persona` to an `Evento`, typed via `TipoContribucion`
- **Finanza** — financial report; aggregates `Contribucion` (income) and `Gasto` (expenses) via many-to-many
- **Gasto** — expense entry
- **Recurso** (abstract) → `RecursoEconomico`, `RecursoFisico` — church resources
- **Geographic hierarchy**: `Pais` → `Ciudad` → `Comuna` → `Barrio` → `Direccion` → `EntidadTerritorial` → `ZonaAdministrativa`

## API Endpoints

| Controller | Base Path |
|-----------|-----------|
| AuthController | `/api/auth` |
| MiembroController | `/api/miembros` |
| IglesiaController | `/api/iglesias` |
| EventoController | `/api/eventos` |
| ContribucionController | `/api/contribuciones` |
| AsistenciaEventoController | `/api/asistencias` |
| FinanzaController | `/api/finanzas` (ADMIN only) |
| GastoController | `/api/gastos` |
| RolController | `/api/roles` (ADMIN only) |
| RecursoEconomicoController | `/api/recursos-economicos` |
| RecursoFisicoController | `/api/recursos-fisicos` |
| TipoContribucionController | `/api/tipocontribuciones` |
| TipoEventoController | `/api/tipoeventos` |
| Geographic controllers | `/api/paises`, `/api/ciudades`, `/api/comunas`, `/api/barrios`, `/api/direcciones`, `/api/zonasadministrativas`, `/api/entidadesterritoriales` |

## Conventions

- Services use `@RequiredArgsConstructor` (Lombok) for dependency injection — no `@Autowired`
- Entity IDs use `@SequenceGenerator` with `allocationSize=1`; never use `GenerationType.IDENTITY` with Oracle
- Services access the current logged-in user via `UsuarioContextUtil.getUsuarioActual()`
- DTOs live in `DTOS/` (note the all-caps package name)
- Entity package is named `modele/` (not `model`)