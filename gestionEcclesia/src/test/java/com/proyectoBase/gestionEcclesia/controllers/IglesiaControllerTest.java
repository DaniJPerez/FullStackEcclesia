package com.proyectoBase.gestionEcclesia.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyectoBase.gestionEcclesia.DTOS.IglesiaDTO;
import com.proyectoBase.gestionEcclesia.modele.Iglesia;
import com.proyectoBase.gestionEcclesia.services.IglesiaServices;
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

@WebMvcTest(IglesiaController.class)
class IglesiaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private IglesiaServices iglesiaService;

    @MockitoBean
    private UsuarioServices usuarioServices;

    private IglesiaDTO buildIglesiaDTO() {
        IglesiaDTO dto = new IglesiaDTO();
        dto.setIdIglesia(1L);
        dto.setNombreIglesia("Iglesia Central");
        dto.setTelefonoIglesia("123456789");
        dto.setEmailIglesia("iglesia@test.com");
        return dto;
    }

    private Iglesia buildIglesia() {
        Iglesia iglesia = new Iglesia();
        iglesia.setIdIglesia(1L);
        iglesia.setNombreIglesia("Iglesia Central");
        return iglesia;
    }

    @Test
    @WithMockUser
    void getAllIglesias_retornaListaYStatus200() throws Exception {
        when(iglesiaService.findAll()).thenReturn(List.of(buildIglesia()));
        when(iglesiaService.convertToDTO(any(Iglesia.class))).thenReturn(buildIglesiaDTO());

        mockMvc.perform(get("/api/iglesias/mostraTodos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombreIglesia").value("Iglesia Central"));
    }

    @Test
    @WithMockUser
    void getIglesiaById_retornaIglesiaYStatus200() throws Exception {
        when(iglesiaService.findById(1L)).thenReturn(buildIglesia());
        when(iglesiaService.convertToDTO(any(Iglesia.class))).thenReturn(buildIglesiaDTO());

        mockMvc.perform(get("/api/iglesias/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idIglesia").value(1));
    }

    @Test
    @WithMockUser
    void createIglesia_retornaCreadaYStatus201() throws Exception {
        IglesiaDTO dto = buildIglesiaDTO();
        when(iglesiaService.saveIglesia(any(IglesiaDTO.class))).thenReturn(buildIglesia());
        when(iglesiaService.convertToDTO(any(Iglesia.class))).thenReturn(dto);

        mockMvc.perform(post("/api/iglesias/crear")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombreIglesia").value("Iglesia Central"));
    }

    @Test
    @WithMockUser
    void updateIglesia_retornaActualizadaYStatus200() throws Exception {
        IglesiaDTO dto = buildIglesiaDTO();
        when(iglesiaService.updateIglesia(eq(1L), any(IglesiaDTO.class))).thenReturn(buildIglesia());
        when(iglesiaService.convertToDTO(any(Iglesia.class))).thenReturn(dto);

        mockMvc.perform(put("/api/iglesias/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.telefonoIglesia").value("123456789"));
    }

    @Test
    @WithMockUser
    void deleteIglesia_retornaStatus204() throws Exception {
        doNothing().when(iglesiaService).deleteIglesia(1L);

        mockMvc.perform(delete("/api/iglesias/1").with(csrf()))
                .andExpect(status().isNoContent());

        verify(iglesiaService).deleteIglesia(1L);
    }
}
