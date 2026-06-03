package com.proyectoBase.gestionEcclesia.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyectoBase.gestionEcclesia.DTOS.BarrioDTO;
import com.proyectoBase.gestionEcclesia.modele.Barrio;
import com.proyectoBase.gestionEcclesia.services.BarrioService;
import com.proyectoBase.gestionEcclesia.services.UsuarioServices;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BarrioController.class)
class BarrioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BarrioService barrioService;

    @MockitoBean
    private UsuarioServices usuarioServices;

    private BarrioDTO buildBarrioDTO() {
        return new BarrioDTO(1L, "Barrio Centro", null, null);
    }

    private Barrio buildBarrio() {
        Barrio b = new Barrio();
        b.setId(1L);
        b.setNombre("Barrio Centro");
        return b;
    }

    @Test
    @WithMockUser
    void getAllBarrios_retornaListaYStatus200() throws Exception {
        when(barrioService.findAll()).thenReturn(List.of(buildBarrio()));
        when(barrioService.convertToDTO(any(Barrio.class))).thenReturn(buildBarrioDTO());

        mockMvc.perform(get("/api/barrios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombreBarrio").value("Barrio Centro"));
    }

    @Test
    @WithMockUser
    void getBarrioById_retornaBarrioYStatus200() throws Exception {
        when(barrioService.findByid(1L)).thenReturn(buildBarrio());
        when(barrioService.convertToDTO(any(Barrio.class))).thenReturn(buildBarrioDTO());

        mockMvc.perform(get("/api/barrios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idBarrio").value(1));
    }

    @Test
    @WithMockUser
    void createBarrio_retornaCreadoYStatus201() throws Exception {
        BarrioDTO dto = buildBarrioDTO();
        when(barrioService.save(any(BarrioDTO.class))).thenReturn(buildBarrio());
        when(barrioService.convertToDTO(any(Barrio.class))).thenReturn(dto);

        mockMvc.perform(post("/api/barrios")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombreBarrio").value("Barrio Centro"));
    }

    @Test
    @WithMockUser
    void updateBarrio_retornaActualizadoYStatus200() throws Exception {
        BarrioDTO dto = buildBarrioDTO();
        when(barrioService.update(eq(1L), any(BarrioDTO.class))).thenReturn(buildBarrio());
        when(barrioService.convertToDTO(any(Barrio.class))).thenReturn(dto);

        mockMvc.perform(put("/api/barrios/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreBarrio").value("Barrio Centro"));
    }

    @Test
    @WithMockUser
    void deleteBarrio_retornaStatus204() throws Exception {
        doNothing().when(barrioService).delete(1L);

        mockMvc.perform(delete("/api/barrios/1").with(csrf()))
                .andExpect(status().isNoContent());

        verify(barrioService).delete(1L);
    }
}