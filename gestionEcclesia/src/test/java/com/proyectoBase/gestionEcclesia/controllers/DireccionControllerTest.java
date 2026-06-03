package com.proyectoBase.gestionEcclesia.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyectoBase.gestionEcclesia.DTOS.DireccionDTO;
import com.proyectoBase.gestionEcclesia.modele.Direccion;
import com.proyectoBase.gestionEcclesia.services.DireccionService;
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

@WebMvcTest(DireccionController.class)
class DireccionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private DireccionService direccionService;

    @MockitoBean
    private UsuarioServices usuarioServices;

    private DireccionDTO buildDireccionDTO() {
        return new DireccionDTO(1L, "Av. Providencia", "1234", null, null);
    }

    private Direccion buildDireccion() {
        Direccion d = new Direccion();
        d.setId(1L);
        d.setCalle("Av. Providencia");
        d.setNumero("1234");
        return d;
    }

    @Test
    @WithMockUser
    void getAllDirecciones_retornaListaYStatus200() throws Exception {
        when(direccionService.findAll()).thenReturn(List.of(buildDireccion()));
        when(direccionService.converToDTO(any(Direccion.class))).thenReturn(buildDireccionDTO());

        mockMvc.perform(get("/api/direcciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].calle").value("Av. Providencia"));
    }

    @Test
    @WithMockUser
    void getDireccionById_retornaDireccionYStatus200() throws Exception {
        when(direccionService.findById(1L)).thenReturn(buildDireccion());
        when(direccionService.converToDTO(any(Direccion.class))).thenReturn(buildDireccionDTO());

        mockMvc.perform(get("/api/direcciones/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idDireccion").value(1));
    }

    @Test
    @WithMockUser
    void createDireccion_retornaCreadaYStatus201() throws Exception {
        DireccionDTO dto = buildDireccionDTO();
        when(direccionService.save(any(DireccionDTO.class))).thenReturn(buildDireccion());
        when(direccionService.converToDTO(any(Direccion.class))).thenReturn(dto);

        mockMvc.perform(post("/api/direcciones")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.calle").value("Av. Providencia"));
    }

    @Test
    @WithMockUser
    void updateDireccion_retornaActualizadaYStatus200() throws Exception {
        DireccionDTO dto = buildDireccionDTO();
        when(direccionService.update(eq(1L), any(DireccionDTO.class))).thenReturn(buildDireccion());
        when(direccionService.converToDTO(any(Direccion.class))).thenReturn(dto);

        mockMvc.perform(put("/api/direcciones/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numero").value("1234"));
    }

    @Test
    @WithMockUser
    void deleteDireccion_retornaStatus204() throws Exception {
        doNothing().when(direccionService).delete(1L);

        mockMvc.perform(delete("/api/direcciones/1").with(csrf()))
                .andExpect(status().isNoContent());

        verify(direccionService).delete(1L);
    }
}
