package com.proyectoBase.gestionEcclesia.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyectoBase.gestionEcclesia.DTOS.MiembroDTO;
import com.proyectoBase.gestionEcclesia.DTOS.RolDTO;
import com.proyectoBase.gestionEcclesia.modele.Persona;
import com.proyectoBase.gestionEcclesia.services.AsistenciaEventoService;
import com.proyectoBase.gestionEcclesia.services.ContribucionService;
import com.proyectoBase.gestionEcclesia.services.MiembroService;
import com.proyectoBase.gestionEcclesia.services.UsuarioServices;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MiembroController.class)
class MiembroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private MiembroService miembroService;

    @MockitoBean
    private AsistenciaEventoService asistenciaEventoService;

    @MockitoBean
    private ContribucionService contribucionService;

    @MockitoBean
    private UsuarioServices usuarioServices;

    private MiembroDTO buildMiembroDTO() {
        MiembroDTO dto = new MiembroDTO();
        dto.setId(1L);
        dto.setNombre("Juan");
        dto.setApellido("Pérez");
        dto.setFechaNacimiento(LocalDate.of(1990, 5, 15));
        dto.setEstadoCivil("SOLTERO");
        dto.setSexo("MASCULINO");
        dto.setRolDTO(new RolDTO(1L, "ROLE_USER", "Usuario estándar"));
        return dto;
    }

    private Persona buildPersona() {
        Persona p = new Persona();
        p.setNumeroIdentificacion(1L);
        p.setPrimerNombre("Juan");
        p.setPrimerApellido("Pérez");
        return p;
    }

    @Test
    @WithMockUser
    void getAllMiembros_retornaListaYStatus200() throws Exception {
        when(miembroService.findAll()).thenReturn(List.of(buildPersona()));
        when(miembroService.convertToDTO(any(Persona.class))).thenReturn(buildMiembroDTO());
        when(asistenciaEventoService.agregarAsistenciaAMiembroDto(any())).thenReturn(buildMiembroDTO());
        when(contribucionService.agregarContribucionAMiembroDto(any())).thenReturn(buildMiembroDTO());

        mockMvc.perform(get("/api/miembros/mostratodos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Juan"));
    }

    @Test
    @WithMockUser
    void getMiembroById_retornaMiembroYStatus200() throws Exception {
        Persona persona = buildPersona();
        MiembroDTO dto = buildMiembroDTO();
        dto.setAsistenciaEvento(Collections.emptyList());
        dto.setContribuciones(Collections.emptyList());

        when(miembroService.findById(1L)).thenReturn(persona);
        when(asistenciaEventoService.agregarAsistenciaMiembro(any(Persona.class))).thenReturn(persona);
        when(miembroService.convertToDTO(any(Persona.class))).thenReturn(dto);
        when(asistenciaEventoService.agregarAsistenciaAMiembroDto(any())).thenReturn(buildMiembroDTO());
        when(contribucionService.findByMiembro(1L)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/miembros/buscar/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.apellido").value("Pérez"));
    }

    @Test
    @WithMockUser
    void getMiembrosByNombre_retornaListaYStatus200() throws Exception {
        when(miembroService.findByPrimerNombre("Juan")).thenReturn(List.of(buildPersona()));
        when(miembroService.convertToDTO(any(Persona.class))).thenReturn(buildMiembroDTO());
        when(asistenciaEventoService.agregarAsistenciaAMiembroDto(any())).thenReturn(buildMiembroDTO());
        when(contribucionService.agregarContribucionAMiembroDto(any())).thenReturn(buildMiembroDTO());

        mockMvc.perform(get("/api/miembros/buscar/nombre").param("nombre", "Juan"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Juan"));
    }

    @Test
    @WithMockUser
    void createMiembro_retornaCreadoYStatus201() throws Exception {
        MiembroDTO dto = buildMiembroDTO();
        when(miembroService.save(any(MiembroDTO.class))).thenReturn(buildPersona());
        when(miembroService.convertToDTO(any(Persona.class))).thenReturn(dto);

        mockMvc.perform(post("/api/miembros/guardarmiembro")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Juan"));
    }

    @Test
    @WithMockUser
    void updateMiembro_retornaActualizadoYStatus200() throws Exception {
        MiembroDTO dto = buildMiembroDTO();
        when(miembroService.update(eq(1L), any(MiembroDTO.class))).thenReturn(buildPersona());
        when(miembroService.convertToDTO(any(Persona.class))).thenReturn(dto);

        mockMvc.perform(put("/api/miembros/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.apellido").value("Pérez"));
    }

    @Test
    @WithMockUser
    void deleteMiembro_retornaStatus204() throws Exception {
        doNothing().when(miembroService).delete(1L);

        mockMvc.perform(delete("/api/miembros/1").with(csrf()))
                .andExpect(status().isNoContent());

        verify(miembroService).delete(1L);
    }
}
