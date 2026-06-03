package com.proyectoBase.gestionEcclesia.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.proyectoBase.gestionEcclesia.DTOS.DireccionDTO;
import com.proyectoBase.gestionEcclesia.DTOS.EventoDTO;
import com.proyectoBase.gestionEcclesia.modele.Evento;
import com.proyectoBase.gestionEcclesia.services.AsistenciaEventoService;
import com.proyectoBase.gestionEcclesia.services.ContribucionService;
import com.proyectoBase.gestionEcclesia.services.EventoService;
import com.proyectoBase.gestionEcclesia.services.UsuarioServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EventoController.class)
class   EventoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private EventoService eventoService;

    @MockitoBean
    private AsistenciaEventoService asistenciaEventoService;

    @MockitoBean
    private ContribucionService contribucionService;

    @MockitoBean
    private UsuarioServices usuarioServices;

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    private EventoDTO buildEventoDTO() {
        EventoDTO dto = new EventoDTO();
        dto.setId(1L);
        dto.setNombre("Culto Dominical");
        dto.setDescripcion("Servicio semanal");
        dto.setFechaInicio(LocalDate.of(2025, 6, 1));
        dto.setEstadoEvento("ACTIVO");
        dto.setDireccionDTO(new DireccionDTO());
        dto.setAsistentesIds(Collections.emptyList());
        dto.setContribuciones(Collections.emptyList());
        return dto;
    }

    private Evento buildEvento() {
        Evento evento = new Evento();
        evento.setId(1L);
        evento.setNombre("Culto Dominical");
        return evento;
    }

    @Test
    @WithMockUser
    void getAllEventos_retornaListaYStatus200() throws Exception {
        when(eventoService.findAll()).thenReturn(List.of(buildEvento()));
        when(eventoService.convertToDTO(any(Evento.class))).thenReturn(buildEventoDTO());
        when(asistenciaEventoService.agregarAsistenciaAEventoDto(any())).thenReturn(buildEventoDTO());
        when(contribucionService.agregarContribucionAEventoDto(any())).thenReturn(buildEventoDTO());

        mockMvc.perform(get("/api/eventos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Culto Dominical"));
    }

    @Test
    @WithMockUser
    void getEventoById_retornaEventoYStatus200() throws Exception {
        when(eventoService.findById(1L)).thenReturn(buildEvento());
        when(eventoService.convertToDTO(any(Evento.class))).thenReturn(buildEventoDTO());
        when(asistenciaEventoService.agregarAsistenciaAEventoDto(any())).thenReturn(buildEventoDTO());
        when(contribucionService.findByEvento(1L)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/eventos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Culto Dominical"));
    }

    @Test
    @WithMockUser
    void getEventosFuturos_retornaListaYStatus200() throws Exception {
        when(eventoService.findEventosFuturos()).thenReturn(List.of(buildEvento()));
        when(eventoService.convertToDTO(any(Evento.class))).thenReturn(buildEventoDTO());

        mockMvc.perform(get("/api/eventos/futuros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].estadoEvento").value("ACTIVO"));
    }

    @Test
    @WithMockUser
    void getEventosEntreFechas_retornaListaYStatus200() throws Exception {
        when(eventoService.findEventosEntreFechas(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(buildEvento()));
        when(eventoService.convertToDTO(any(Evento.class))).thenReturn(buildEventoDTO());
        when(asistenciaEventoService.agregarAsistenciaAEventoDto(any())).thenReturn(buildEventoDTO());
        when(contribucionService.agregarContribucionAEventoDto(any())).thenReturn(buildEventoDTO());

        mockMvc.perform(get("/api/eventos/entre-fechas")
                        .param("inicio", "2025-01-01T00:00:00")
                        .param("fin", "2025-12-31T23:59:59"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Culto Dominical"));
    }

    @Test
    @WithMockUser
    void createEvento_retornaCreadoYStatus201() throws Exception {
        EventoDTO dto = buildEventoDTO();
        when(eventoService.save(any(EventoDTO.class))).thenReturn(buildEvento());
        when(eventoService.convertToDTO(any(Evento.class))).thenReturn(dto);

        mockMvc.perform(post("/api/eventos")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Culto Dominical"));
    }

    @Test
    @WithMockUser
    void updateEvento_retornaActualizadoYStatus200() throws Exception {
        EventoDTO dto = buildEventoDTO();
        when(eventoService.update(any(EventoDTO.class))).thenReturn(buildEvento());
        when(eventoService.convertToDTO(any(Evento.class))).thenReturn(dto);

        mockMvc.perform(put("/api/eventos/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estadoEvento").value("ACTIVO"));
    }

    @Test
    @WithMockUser
    void deleteEvento_retornaStatus204() throws Exception {
        doNothing().when(eventoService).delete(1L);

        mockMvc.perform(delete("/api/eventos/1").with(csrf()))
                .andExpect(status().isNoContent());

        verify(eventoService).delete(1L);
    }
}