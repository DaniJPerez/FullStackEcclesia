package com.proyectoBase.gestionEcclesia.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyectoBase.gestionEcclesia.DTOS.ComunaDTO;
import com.proyectoBase.gestionEcclesia.modele.Comuna;
import com.proyectoBase.gestionEcclesia.services.ComunaService;
import com.proyectoBase.gestionEcclesia.services.UsuarioServices;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ComunaController.class)
class ComunaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ComunaService comunaService;

    @MockitoBean
    private UsuarioServices usuarioServices;

    private ComunaDTO buildComunaDTO() {
        return new ComunaDTO(1L, "Las Condes", "Descripción Las Condes", null, null);
    }

    private Comuna buildComuna() {
        Comuna c = new Comuna();
        c.setId(1L);
        c.setNombre("Las Condes");
        return c;
    }

    @Test
    @WithMockUser
    void getAllComunas_retornaListaYStatus200() throws Exception {
        when(comunaService.findAll()).thenReturn(List.of(buildComuna()));
        when(comunaService.convertToDTO(any(Comuna.class))).thenReturn(buildComunaDTO());

        mockMvc.perform(get("/api/comunas/mostraTodos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombreComuna").value("Las Condes"));
    }

    @Test
    @WithMockUser
    void getComunaById_retornaComunaYStatus200() throws Exception {
        when(comunaService.findById(1L)).thenReturn(buildComuna());
        when(comunaService.convertToDTO(any(Comuna.class))).thenReturn(buildComunaDTO());

        mockMvc.perform(get("/api/comunas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idComuna").value(1));
    }

    @Test
    @WithMockUser
    void createComuna_retornaCreadaYStatus201() throws Exception {
        ComunaDTO dto = buildComunaDTO();
        when(comunaService.save(any(ComunaDTO.class))).thenReturn(buildComuna());
        when(comunaService.convertToDTO(any(Comuna.class))).thenReturn(dto);

        mockMvc.perform(post("/api/comunas/crear")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombreComuna").value("Las Condes"));
    }

    @Test
    @WithMockUser
    void updateComuna_retornaActualizadaYStatus200() throws Exception {
        ComunaDTO dto = buildComunaDTO();
        when(comunaService.update(eq(1L), any(ComunaDTO.class))).thenReturn(buildComuna());
        when(comunaService.convertToDTO(any(Comuna.class))).thenReturn(dto);

        mockMvc.perform(put("/api/comunas/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreComuna").value("Las Condes"));
    }

    @Test
    @WithMockUser
    void deleteComuna_retornaStatus204() throws Exception {
        doNothing().when(comunaService).delete(1L);

        mockMvc.perform(delete("/api/comunas/1").with(csrf()))
                .andExpect(status().isNoContent());

        verify(comunaService).delete(1L);
    }
}
