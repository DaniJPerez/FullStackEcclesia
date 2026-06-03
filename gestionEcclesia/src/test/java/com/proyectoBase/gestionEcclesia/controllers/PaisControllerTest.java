package com.proyectoBase.gestionEcclesia.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyectoBase.gestionEcclesia.DTOS.PaisDto;
import com.proyectoBase.gestionEcclesia.modele.Pais;
import com.proyectoBase.gestionEcclesia.services.PaisServices;
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

@WebMvcTest(PaisController.class)
class PaisControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PaisServices paisService;

    @MockitoBean
    private UsuarioServices usuarioServices;

    private PaisDto buildPaisDto() {
        return new PaisDto(1L, "Chile", null, null);
    }

    private Pais buildPais() {
        Pais pais = new Pais();
        pais.setId(1L);
        pais.setNombrePais("Chile");
        return pais;
    }

    @Test
    @WithMockUser
    void getAllPaises_retornaListaYStatus200() throws Exception {
        when(paisService.getAllPaises()).thenReturn(List.of(buildPais()));
        when(paisService.convertToDto(any(Pais.class))).thenReturn(buildPaisDto());

        mockMvc.perform(get("/api/paises/mostratodos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombrePais").value("Chile"));
    }

    @Test
    @WithMockUser
    void getPaisById_retornaPaisYStatus200() throws Exception {
        when(paisService.findByIdPais(1L)).thenReturn(buildPais());
        when(paisService.convertToDto(any(Pais.class))).thenReturn(buildPaisDto());

        mockMvc.perform(get("/api/paises/buscar/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idPais").value(1));
    }

    @Test
    @WithMockUser
    void getPaisByNombre_retornaPaisYStatus200() throws Exception {
        when(paisService.findByNombrePais("Chile")).thenReturn(buildPais());
        when(paisService.convertToDto(any(Pais.class))).thenReturn(buildPaisDto());

        mockMvc.perform(get("/api/paises/buscar/nombre").param("nombre", "Chile"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombrePais").value("Chile"));
    }

    @Test
    @WithMockUser
    void createPais_retornaCreadoYStatus201() throws Exception {
        PaisDto dto = buildPaisDto();
        when(paisService.savePais(any(PaisDto.class))).thenReturn(buildPais());
        when(paisService.convertToDto(any(Pais.class))).thenReturn(dto);

        mockMvc.perform(post("/api/paises/guardarpais")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombrePais").value("Chile"));
    }

    @Test
    @WithMockUser
    void updatePais_retornaActualizadoYStatus200() throws Exception {
        PaisDto dto = buildPaisDto();
        when(paisService.updatePais(eq(1L), any(PaisDto.class))).thenReturn(buildPais());
        when(paisService.convertToDto(any(Pais.class))).thenReturn(dto);

        mockMvc.perform(put("/api/paises/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombrePais").value("Chile"));
    }

    @Test
    @WithMockUser
    void deletePais_retornaStatus204() throws Exception {
        doNothing().when(paisService).deletePais(1L);

        mockMvc.perform(delete("/api/paises/1").with(csrf()))
                .andExpect(status().isNoContent());

        verify(paisService).deletePais(1L);
    }
}
