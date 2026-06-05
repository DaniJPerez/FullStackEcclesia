package com.proyectoBase.gestionEcclesia.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyectoBase.gestionEcclesia.DTOS.NacionalidadDTO;
import com.proyectoBase.gestionEcclesia.DTOS.PaisDto;
import com.proyectoBase.gestionEcclesia.modele.Nacionalidad;
import com.proyectoBase.gestionEcclesia.services.NacionalidadService;
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

@WebMvcTest(NacionalidadController.class)
class NacionalidadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private NacionalidadService nacionalidadService;

    @MockitoBean
    private UsuarioServices usuarioServices;

    private NacionalidadDTO buildDto() {
        PaisDto pais = new PaisDto(1L, "Chile", null, null);
        return new NacionalidadDTO(1L, "Chilena", "Nacionalidad de Chile", pais, null);
    }

    private Nacionalidad buildNacionalidad() {
        Nacionalidad n = new Nacionalidad();
        n.setIdNacionalidad(1L);
        n.setNombreNacionalidad("Chilena");
        return n;
    }

    @Test
    @WithMockUser
    void getAll_retornaListaYStatus200() throws Exception {
        when(nacionalidadService.getAll()).thenReturn(List.of(buildNacionalidad()));
        when(nacionalidadService.convertToDto(any(Nacionalidad.class))).thenReturn(buildDto());

        mockMvc.perform(get("/api/nacionalidades"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombreNacionalidad").value("Chilena"));
    }

    @Test
    @WithMockUser
    void getById_retornaNacionalidadYStatus200() throws Exception {
        when(nacionalidadService.findById(1L)).thenReturn(buildNacionalidad());
        when(nacionalidadService.convertToDto(any(Nacionalidad.class))).thenReturn(buildDto());

        mockMvc.perform(get("/api/nacionalidades/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idNacionalidad").value(1));
    }

    @Test
    @WithMockUser
    void create_retornaCreadaYStatus201() throws Exception {
        NacionalidadDTO dto = buildDto();
        when(nacionalidadService.save(any(NacionalidadDTO.class))).thenReturn(buildNacionalidad());
        when(nacionalidadService.convertToDto(any(Nacionalidad.class))).thenReturn(dto);

        mockMvc.perform(post("/api/nacionalidades")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombreNacionalidad").value("Chilena"));
    }

    @Test
    @WithMockUser
    void update_retornaActualizadaYStatus200() throws Exception {
        NacionalidadDTO dto = buildDto();
        when(nacionalidadService.update(eq(1L), any(NacionalidadDTO.class))).thenReturn(buildNacionalidad());
        when(nacionalidadService.convertToDto(any(Nacionalidad.class))).thenReturn(dto);

        mockMvc.perform(put("/api/nacionalidades/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idNacionalidad").value(1));
    }

    @Test
    @WithMockUser
    void delete_retornaStatus204() throws Exception {
        doNothing().when(nacionalidadService).delete(1L);

        mockMvc.perform(delete("/api/nacionalidades/1").with(csrf()))
                .andExpect(status().isNoContent());

        verify(nacionalidadService).delete(1L);
    }
}
