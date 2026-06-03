package com.proyectoBase.gestionEcclesia.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyectoBase.gestionEcclesia.DTOS.RecursoFisicoDTO;
import com.proyectoBase.gestionEcclesia.modele.RecursoFisico;
import com.proyectoBase.gestionEcclesia.services.RecursoFisicoService;
import com.proyectoBase.gestionEcclesia.services.UsuarioServices;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RecursoFisicoController.class)
class RecursoFisicoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RecursoFisicoService recursoFisicoService;

    @MockitoBean
    private UsuarioServices usuarioServices;

    private RecursoFisicoDTO buildDTO() {
        return new RecursoFisicoDTO(1L, "Sillas plegables", 50,
                new BigDecimal("15000"), null, "Buen estado", null, null);
    }

    private RecursoFisico buildEntidad() {
        RecursoFisico r = new RecursoFisico();
        r.setIdRecurso(1L);
        r.setDescripcionRecurso("Sillas plegables");
        r.setCantidad(50);
        return r;
    }

    @Test
    @WithMockUser
    void getAllRecursosFisicos_retornaListaYStatus200() throws Exception {
        when(recursoFisicoService.findAll()).thenReturn(List.of(buildEntidad()));
        when(recursoFisicoService.convertToDTO(any(RecursoFisico.class))).thenReturn(buildDTO());

        mockMvc.perform(get("/api/recursos-fisicos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].descripcion").value("Sillas plegables"));
    }

    @Test
    @WithMockUser
    void getRecursoFisicoById_retornaDtoYStatus200() throws Exception {
        when(recursoFisicoService.findById(1L)).thenReturn(buildEntidad());
        when(recursoFisicoService.convertToDTO(any(RecursoFisico.class))).thenReturn(buildDTO());

        mockMvc.perform(get("/api/recursos-fisicos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cantidad").value(50));
    }

    @Test
    @WithMockUser
    void getRecursosFisicosByDescripcion_retornaListaYStatus200() throws Exception {
        when(recursoFisicoService.findByDescripcion("Sillas")).thenReturn(List.of(buildEntidad()));
        when(recursoFisicoService.convertToDTO(any(RecursoFisico.class))).thenReturn(buildDTO());

        mockMvc.perform(get("/api/recursos-fisicos/buscar").param("descripcion", "Sillas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].descripcion").value("Sillas plegables"));
    }

    @Test
    @WithMockUser
    void createRecursoFisico_retornaCreadoYStatus201() throws Exception {
        RecursoFisicoDTO dto = buildDTO();
        when(recursoFisicoService.save(any(RecursoFisicoDTO.class))).thenReturn(buildEntidad());
        when(recursoFisicoService.convertToDTO(any(RecursoFisico.class))).thenReturn(dto);

        mockMvc.perform(post("/api/recursos-fisicos")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.descripcion").value("Sillas plegables"));
    }

    @Test
    @WithMockUser
    void updateRecursoFisico_retornaActualizadoYStatus200() throws Exception {
        RecursoFisicoDTO dto = buildDTO();
        when(recursoFisicoService.update(eq(1L), any(RecursoFisicoDTO.class))).thenReturn(buildEntidad());
        when(recursoFisicoService.convertToDTO(any(RecursoFisico.class))).thenReturn(dto);

        mockMvc.perform(put("/api/recursos-fisicos/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cantidad").value(50));
    }

    @Test
    @WithMockUser
    void deleteRecursoFisico_retornaStatus204() throws Exception {
        doNothing().when(recursoFisicoService).delete(1L);

        mockMvc.perform(delete("/api/recursos-fisicos/1").with(csrf()))
                .andExpect(status().isNoContent());

        verify(recursoFisicoService).delete(1L);
    }
}