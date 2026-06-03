package com.proyectoBase.gestionEcclesia.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyectoBase.gestionEcclesia.DTOS.TipoEventoDTO;
import com.proyectoBase.gestionEcclesia.modele.TipoEvento;
import com.proyectoBase.gestionEcclesia.services.TipoEventoServices;
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

@WebMvcTest(TipoEventoController.class)
class TipoEventoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TipoEventoServices tipoEventoServices;

    @MockitoBean
    private UsuarioServices usuarioServices;

    private TipoEventoDTO buildDTO() {
        return new TipoEventoDTO(1L, "Culto", "Servicio dominical");
    }

    private TipoEvento buildEntidad() {
        TipoEvento t = new TipoEvento();
        t.setId(1L);
        t.setNombre("Culto");
        return t;
    }

    @Test
    @WithMockUser
    void getAllTipoEventos_retornaListaYStatus200() throws Exception {
        when(tipoEventoServices.findAll()).thenReturn(List.of(buildEntidad()));
        when(tipoEventoServices.convertToDto(any())).thenReturn(buildDTO());

        mockMvc.perform(get("/api/tipoeventos/mostratodos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombreTipoEvento").value("Culto"));
    }

    @Test
    @WithMockUser
    void getTipoEventoById_retornaDtoYStatus200() throws Exception {
        when(tipoEventoServices.findById(1L)).thenReturn(buildEntidad());
        when(tipoEventoServices.convertToDto(any())).thenReturn(buildDTO());

        mockMvc.perform(get("/api/tipoeventos/buscar/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idTipoEvento").value(1));
    }

    @Test
    @WithMockUser
    void createTipoEvento_retornaCreadoYStatus201() throws Exception {
        TipoEventoDTO dto = buildDTO();
        when(tipoEventoServices.create(any())).thenReturn(buildEntidad());
        when(tipoEventoServices.convertToDto(any())).thenReturn(dto);

        mockMvc.perform(post("/api/tipoeventos/guardartipoevento")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombreTipoEvento").value("Culto"));
    }

    @Test
    @WithMockUser
    void updateTipoEvento_retornaActualizadoYStatus200() throws Exception {
        TipoEventoDTO dto = buildDTO();
        when(tipoEventoServices.update(eq(1L), any())).thenReturn(buildEntidad());
        when(tipoEventoServices.convertToDto(any())).thenReturn(dto);

        mockMvc.perform(put("/api/tipoeventos/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descripcionTipoEvento").value("Servicio dominical"));
    }

    @Test
    @WithMockUser
    void deleteTipoEvento_retornaStatus204() throws Exception {
        doNothing().when(tipoEventoServices).deleteById(1L);

        mockMvc.perform(delete("/api/tipoeventos/1").with(csrf()))
                .andExpect(status().isNoContent());

        verify(tipoEventoServices).deleteById(1L);
    }
}
