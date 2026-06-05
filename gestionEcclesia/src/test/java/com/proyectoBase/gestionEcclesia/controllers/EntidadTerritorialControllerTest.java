package com.proyectoBase.gestionEcclesia.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyectoBase.gestionEcclesia.DTOS.EntidadTerritorialDto;
import com.proyectoBase.gestionEcclesia.DTOS.PaisDto;
import com.proyectoBase.gestionEcclesia.modele.EntidadTerritorial;
import com.proyectoBase.gestionEcclesia.services.EntidadTerritorialServices;
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

@WebMvcTest(EntidadTerritorialController.class)
class EntidadTerritorialControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private EntidadTerritorialServices entidadTerritorialService;

    @MockitoBean
    private UsuarioServices usuarioServices;

    private EntidadTerritorialDto buildDto() {
        PaisDto pais = new PaisDto(1L, "Chile", null, null);
        return new EntidadTerritorialDto(1L, "Región Metropolitana", "Capital del país", pais, null);
    }

    private EntidadTerritorial buildEntidad() {
        EntidadTerritorial e = new EntidadTerritorial();
        e.setIdEntidadTerritorial(1L);
        e.setNombreEntidadTerritorial("Región Metropolitana");
        return e;
    }

    @Test
    @WithMockUser
    void getAll_retornaListaYStatus200() throws Exception {
        when(entidadTerritorialService.getAllEntidadTerritorials()).thenReturn(List.of(buildEntidad()));
        when(entidadTerritorialService.convertToDto(any(EntidadTerritorial.class))).thenReturn(buildDto());

        mockMvc.perform(get("/api/entidadesterritoriales"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombreEntidad").value("Región Metropolitana"));
    }

    @Test
    @WithMockUser
    void getById_retornaEntidadYStatus200() throws Exception {
        when(entidadTerritorialService.findByIdEntidadTerritorial(1L)).thenReturn(buildEntidad());
        when(entidadTerritorialService.convertToDto(any(EntidadTerritorial.class))).thenReturn(buildDto());

        mockMvc.perform(get("/api/entidadesterritoriales/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idEntidad").value(1));
    }

    @Test
    @WithMockUser
    void create_retornaCreadaYStatus201() throws Exception {
        EntidadTerritorialDto dto = buildDto();
        when(entidadTerritorialService.save(any(EntidadTerritorialDto.class))).thenReturn(buildEntidad());
        when(entidadTerritorialService.convertToDto(any(EntidadTerritorial.class))).thenReturn(dto);

        mockMvc.perform(post("/api/entidadesterritoriales")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombreEntidad").value("Región Metropolitana"));
    }

    @Test
    @WithMockUser
    void update_retornaActualizadaYStatus200() throws Exception {
        EntidadTerritorialDto dto = buildDto();
        when(entidadTerritorialService.update(eq(1L), any(EntidadTerritorialDto.class))).thenReturn(buildEntidad());
        when(entidadTerritorialService.convertToDto(any(EntidadTerritorial.class))).thenReturn(dto);

        mockMvc.perform(put("/api/entidadesterritoriales/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idEntidad").value(1));
    }

    @Test
    @WithMockUser
    void delete_retornaStatus204() throws Exception {
        doNothing().when(entidadTerritorialService).delete(1L);

        mockMvc.perform(delete("/api/entidadesterritoriales/1").with(csrf()))
                .andExpect(status().isNoContent());

        verify(entidadTerritorialService).delete(1L);
    }
}
