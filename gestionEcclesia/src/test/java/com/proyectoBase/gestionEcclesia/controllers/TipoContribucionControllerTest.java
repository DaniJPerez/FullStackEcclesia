package com.proyectoBase.gestionEcclesia.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyectoBase.gestionEcclesia.DTOS.TipoContribucionDTO;
import com.proyectoBase.gestionEcclesia.modele.TipoContribucion;
import com.proyectoBase.gestionEcclesia.services.TipoContribucionServices;
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

@WebMvcTest(TipoContribucionController.class)
class TipoContribucionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TipoContribucionServices tipoContribucionServices;

    @MockitoBean
    private UsuarioServices usuarioServices;

    private TipoContribucionDTO buildDTO() {
        return new TipoContribucionDTO(1L, "Diezmo", "Contribución del 10%");
    }

    private TipoContribucion buildEntidad() {
        TipoContribucion t = new TipoContribucion();
        t.setId(1L);
        t.setNombre("Diezmo");
        return t;
    }

    @Test
    @WithMockUser
    void getAllTipoContribuciones_retornaListaYStatus200() throws Exception {
        when(tipoContribucionServices.getAllTipoContribuciones()).thenReturn(List.of(buildEntidad()));
        when(tipoContribucionServices.convertToDto(any())).thenReturn(buildDTO());

        mockMvc.perform(get("/api/tipocontribuciones/mostratodos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombreTipoContribucion").value("Diezmo"));
    }

    @Test
    @WithMockUser
    void getTipoContribucionById_retornaDtoYStatus200() throws Exception {
        when(tipoContribucionServices.getTipoContribucionById(1L)).thenReturn(buildEntidad());
        when(tipoContribucionServices.convertToDto(any())).thenReturn(buildDTO());

        mockMvc.perform(get("/api/tipocontribuciones/buscar/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idTipoContribucion").value(1));
    }

    @Test
    @WithMockUser
    void createTipoContribucion_retornaCreadoYStatus201() throws Exception {
        TipoContribucionDTO dto = buildDTO();
        when(tipoContribucionServices.createTipoContribucion(any())).thenReturn(buildEntidad());
        when(tipoContribucionServices.convertToDto(any())).thenReturn(dto);

        mockMvc.perform(post("/api/tipocontribuciones/guardartipocontribucion")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombreTipoContribucion").value("Diezmo"));
    }

    @Test
    @WithMockUser
    void updateTipoContribucion_retornaActualizadoYStatus200() throws Exception {
        TipoContribucionDTO dto = buildDTO();
        when(tipoContribucionServices.updateTipoContribucion(eq(1L), any())).thenReturn(buildEntidad());
        when(tipoContribucionServices.convertToDto(any())).thenReturn(dto);

        mockMvc.perform(put("/api/tipocontribuciones/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descripcionTipoContribucion").value("Contribución del 10%"));
    }

    @Test
    @WithMockUser
    void deleteTipoContribucion_retornaStatus204() throws Exception {
        doNothing().when(tipoContribucionServices).deleteTipoContribucion(1L);

        mockMvc.perform(delete("/api/tipocontribuciones/1").with(csrf()))
                .andExpect(status().isNoContent());

        verify(tipoContribucionServices).deleteTipoContribucion(1L);
    }
}
