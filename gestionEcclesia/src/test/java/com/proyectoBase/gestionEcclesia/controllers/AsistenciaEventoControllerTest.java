package com.proyectoBase.gestionEcclesia.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyectoBase.gestionEcclesia.DTOS.AsistenciaEventoDto;
import com.proyectoBase.gestionEcclesia.DTOS.EventoDTO;
import com.proyectoBase.gestionEcclesia.DTOS.MiembroDTO;
import com.proyectoBase.gestionEcclesia.modele.AsistenciaEvento;
import com.proyectoBase.gestionEcclesia.services.AsistenciaEventoService;
import com.proyectoBase.gestionEcclesia.services.UsuarioServices;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AsistenciaEventoController.class)
class AsistenciaEventoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AsistenciaEventoService asistenciaEventoService;

    @MockitoBean
    private UsuarioServices usuarioServices;

    private AsistenciaEventoDto buildAsistenciaDto() {
        EventoDTO evento = new EventoDTO();
        evento.setId(1L);
        MiembroDTO miembro = new MiembroDTO();
        miembro.setId(1L);
        return new AsistenciaEventoDto(1L, evento, miembro, LocalDate.of(2025, 4, 20), "PRESENTE");
    }

    private AsistenciaEvento buildAsistencia() {
        AsistenciaEvento a = new AsistenciaEvento();
        a.setIdAsistenciaEvento(1L);
        return a;
    }

    @Test
    @WithMockUser
    void getAllAsistencias_retornaListaYStatus200() throws Exception {
        when(asistenciaEventoService.getAllAsistencias()).thenReturn(List.of(buildAsistencia()));
        when(asistenciaEventoService.convertToDto(any(AsistenciaEvento.class))).thenReturn(buildAsistenciaDto());

        mockMvc.perform(get("/api/asistencias/mostrartodas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].estadoAsistencia").value("PRESENTE"));
    }

    @Test
    @WithMockUser
    void getAsistenciaById_retornaAsistenciaYStatus200() throws Exception {
        when(asistenciaEventoService.getAsistenciaById(1L)).thenReturn(buildAsistencia());
        when(asistenciaEventoService.convertToDto(any(AsistenciaEvento.class))).thenReturn(buildAsistenciaDto());

        mockMvc.perform(get("/api/asistencias/buscarporid/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idAsistencia").value(1));
    }

    @Test
    @WithMockUser
    void agregarAsistencia_retornaCreadaYStatus201() throws Exception {
        AsistenciaEventoDto dto = buildAsistenciaDto();
        when(asistenciaEventoService.save(any(AsistenciaEventoDto.class))).thenReturn(buildAsistencia());
        when(asistenciaEventoService.convertToDto(any(AsistenciaEvento.class))).thenReturn(dto);

        mockMvc.perform(post("/api/asistencias/agregarasistencia")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.estadoAsistencia").value("PRESENTE"));
    }

    @Test
    @WithMockUser
    void updateAsistencia_retornaActualizadaYStatus200() throws Exception {
        AsistenciaEventoDto dto = buildAsistenciaDto();
        when(asistenciaEventoService.update(eq(1L), any(AsistenciaEventoDto.class))).thenReturn(buildAsistencia());
        when(asistenciaEventoService.convertToDto(any(AsistenciaEvento.class))).thenReturn(dto);

        mockMvc.perform(put("/api/asistencias/modificarasistencia/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idAsistencia").value(1));
    }
}
