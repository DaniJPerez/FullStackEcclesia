package com.proyectoBase.gestionEcclesia.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyectoBase.gestionEcclesia.DTOS.ZonaAdministrativaDto;
import com.proyectoBase.gestionEcclesia.modele.ZonaAdministrativa;
import com.proyectoBase.gestionEcclesia.services.UsuarioServices;
import com.proyectoBase.gestionEcclesia.services.ZonaAdministrativaServices;
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

@WebMvcTest(ZonaAdministrativaController.class)
class ZonaAdministrativaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ZonaAdministrativaServices zonaAdministrativaServices;

    @MockitoBean
    private UsuarioServices usuarioServices;

    private ZonaAdministrativaDto buildDto() {
        return new ZonaAdministrativaDto(1L, "Zona Norte", "Descripción zona norte", null, null);
    }

    private ZonaAdministrativa buildEntidad() {
        ZonaAdministrativa z = new ZonaAdministrativa();
        z.setIdZona(1L);
        z.setNombreZona("Zona Norte");
        return z;
    }

    @Test
    @WithMockUser
    void getAllZonas_retornaListaYStatus200() throws Exception {
        when(zonaAdministrativaServices.getAllZonasAdministrativas()).thenReturn(List.of(buildEntidad()));
        when(zonaAdministrativaServices.convertToDto(any())).thenReturn(buildDto());

        mockMvc.perform(get("/api/zonasadministrativas/mostratodos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombreZona").value("Zona Norte"));
    }

    @Test
    @WithMockUser
    void getZonaById_retornaDtoYStatus200() throws Exception {
        when(zonaAdministrativaServices.getZonaAdministrativaById(1L)).thenReturn(buildEntidad());
        when(zonaAdministrativaServices.convertToDto(any())).thenReturn(buildDto());

        mockMvc.perform(get("/api/zonasadministrativas/buscar/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idZona").value(1));
    }

    @Test
    @WithMockUser
    void createZona_retornaCreadaYStatus201() throws Exception {
        ZonaAdministrativaDto dto = buildDto();
        when(zonaAdministrativaServices.createZonaAdministrativa(any())).thenReturn(buildEntidad());
        when(zonaAdministrativaServices.convertToDto(any())).thenReturn(dto);

        mockMvc.perform(post("/api/zonasadministrativas/guardarzonaadministrativa")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombreZona").value("Zona Norte"));
    }

    @Test
    @WithMockUser
    void updateZona_retornaActualizadaYStatus200() throws Exception {
        ZonaAdministrativaDto dto = buildDto();
        when(zonaAdministrativaServices.updateZonaAdministrativa(eq(1L), any())).thenReturn(buildEntidad());
        when(zonaAdministrativaServices.convertToDto(any())).thenReturn(dto);

        mockMvc.perform(put("/api/zonasadministrativas/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreZona").value("Zona Norte"));
    }

    @Test
    @WithMockUser
    void deleteZona_retornaStatus204() throws Exception {
        doNothing().when(zonaAdministrativaServices).deleteZonaAdministrativa(1L);

        mockMvc.perform(delete("/api/zonasadministrativas/1").with(csrf()))
                .andExpect(status().isNoContent());

        verify(zonaAdministrativaServices).deleteZonaAdministrativa(1L);
    }
}

