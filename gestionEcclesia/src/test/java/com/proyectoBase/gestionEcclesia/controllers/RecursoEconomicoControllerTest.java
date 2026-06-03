package com.proyectoBase.gestionEcclesia.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.proyectoBase.gestionEcclesia.DTOS.RecursoEconomicoDTO;
import com.proyectoBase.gestionEcclesia.modele.RecursoEconomico;
import com.proyectoBase.gestionEcclesia.services.RecursoEconomicoService;
import com.proyectoBase.gestionEcclesia.services.UsuarioServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RecursoEconomicoController.class)
class RecursoEconomicoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RecursoEconomicoService recursoEconomicoService;

    @MockitoBean
    private UsuarioServices usuarioServices;

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    private RecursoEconomicoDTO buildDTO() {
        return new RecursoEconomicoDTO(1L, "Ofrenda especial", LocalDate.of(2025, 3, 1),
                new BigDecimal("50000"), null, "Donación voluntaria", null);
    }

    private RecursoEconomico buildEntidad() {
        RecursoEconomico r = new RecursoEconomico();
        r.setIdRecurso(1L);
        r.setDescripcionRecurso("Ofrenda especial");
        r.setValorRecurso(new BigDecimal("50000"));
        return r;
    }

    @Test
    @WithMockUser
    void getAllRecursosEconomicos_retornaListaYStatus200() throws Exception {
        when(recursoEconomicoService.findAll()).thenReturn(List.of(buildEntidad()));
        when(recursoEconomicoService.convertToDTO(any(RecursoEconomico.class))).thenReturn(buildDTO());

        mockMvc.perform(get("/api/recursos-economicos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].descripcion").value("Ofrenda especial"));
    }

    @Test
    @WithMockUser
    void getRecursoEconomicoById_retornaDtoYStatus200() throws Exception {
        when(recursoEconomicoService.findById(1L)).thenReturn(buildEntidad());
        when(recursoEconomicoService.convertToDTO(any(RecursoEconomico.class))).thenReturn(buildDTO());

        mockMvc.perform(get("/api/recursos-economicos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser
    void createRecursoEconomico_retornaCreadoYStatus201() throws Exception {
        RecursoEconomicoDTO dto = buildDTO();
        when(recursoEconomicoService.save(any(RecursoEconomicoDTO.class))).thenReturn(buildEntidad());
        when(recursoEconomicoService.convertToDTO(any(RecursoEconomico.class))).thenReturn(dto);

        mockMvc.perform(post("/api/recursos-economicos")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.descripcion").value("Ofrenda especial"));
    }

    @Test
    @WithMockUser
    void updateRecursoEconomico_retornaActualizadoYStatus200() throws Exception {
        RecursoEconomicoDTO dto = buildDTO();
        when(recursoEconomicoService.update(eq(1L), any(RecursoEconomicoDTO.class))).thenReturn(buildEntidad());
        when(recursoEconomicoService.convertToDTO(any(RecursoEconomico.class))).thenReturn(dto);

        mockMvc.perform(put("/api/recursos-economicos/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.monto").value(50000));
    }

    @Test
    @WithMockUser
    void deleteRecursoEconomico_retornaStatus204() throws Exception {
        doNothing().when(recursoEconomicoService).delete(1L);

        mockMvc.perform(delete("/api/recursos-economicos/1").with(csrf()))
                .andExpect(status().isNoContent());

        verify(recursoEconomicoService).delete(1L);
    }
}