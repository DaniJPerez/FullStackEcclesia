package com.proyectoBase.gestionEcclesia.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyectoBase.gestionEcclesia.DTOS.CiudadDTO;
import com.proyectoBase.gestionEcclesia.modele.Ciudad;
import com.proyectoBase.gestionEcclesia.services.CiudadService;
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

@WebMvcTest(CiudadController.class)
class CiudadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CiudadService ciudadService;

    @MockitoBean
    private UsuarioServices usuarioServices;

    private CiudadDTO buildCiudadDTO() {
        return new CiudadDTO(1L, "Santiago", null, null);
    }

    private Ciudad buildCiudad() {
        Ciudad c = new Ciudad();
        c.setIdCiudad(1L);
        c.setNombreCiudad("Santiago");
        return c;
    }

    @Test
    @WithMockUser
    void getAllCiudades_retornaListaYStatus200() throws Exception {
        when(ciudadService.getAllCiudades()).thenReturn(List.of(buildCiudad()));
        when(ciudadService.convertToDTO(any(Ciudad.class))).thenReturn(buildCiudadDTO());

        mockMvc.perform(get("/api/ciudades/mostratodos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombreCiudad").value("Santiago"));
    }

    @Test
    @WithMockUser
    void getCiudadById_retornaCiudadYStatus200() throws Exception {
        when(ciudadService.findByid(1L)).thenReturn(buildCiudad());
        when(ciudadService.convertToDTO(any(Ciudad.class))).thenReturn(buildCiudadDTO());

        mockMvc.perform(get("/api/ciudades/buscar/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCiudad").value(1));
    }

    @Test
    @WithMockUser
    void createCiudad_retornaCreadaYStatus201() throws Exception {
        CiudadDTO dto = buildCiudadDTO();
        when(ciudadService.save(any(CiudadDTO.class))).thenReturn(buildCiudad());
        when(ciudadService.convertToDTO(any(Ciudad.class))).thenReturn(dto);

        mockMvc.perform(post("/api/ciudades/guardarciudad")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombreCiudad").value("Santiago"));
    }

    @Test
    @WithMockUser
    void updateCiudad_retornaActualizadaYStatus200() throws Exception {
        CiudadDTO dto = buildCiudadDTO();
        when(ciudadService.update(eq(1L), any(CiudadDTO.class))).thenReturn(buildCiudad());
        when(ciudadService.convertToDTO(any(Ciudad.class))).thenReturn(dto);

        mockMvc.perform(put("/api/ciudades/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreCiudad").value("Santiago"));
    }

    @Test
    @WithMockUser
    void deleteCiudad_retornaStatus204() throws Exception {
        doNothing().when(ciudadService).dalete(1L);

        mockMvc.perform(delete("/api/ciudades/1").with(csrf()))
                .andExpect(status().isNoContent());

        verify(ciudadService).dalete(1L);
    }
}