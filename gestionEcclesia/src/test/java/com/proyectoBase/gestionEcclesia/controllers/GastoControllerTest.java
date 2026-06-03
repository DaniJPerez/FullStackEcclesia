package com.proyectoBase.gestionEcclesia.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyectoBase.gestionEcclesia.DTOS.GastoDTO;
import com.proyectoBase.gestionEcclesia.modele.Gasto;
import com.proyectoBase.gestionEcclesia.services.GastoServices;
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

@WebMvcTest(GastoController.class)
class GastoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private GastoServices gastoServices;

    @MockitoBean
    private UsuarioServices usuarioServices;

    private GastoDTO buildGastoDTO() {
        return new GastoDTO(1L, "Compra de sillas", "5000", "2025-01-15", null);
    }

    private Gasto buildGasto() {
        Gasto gasto = new Gasto();
        gasto.setId(1L);
        return gasto;
    }

    @Test
    @WithMockUser
    void getAllGastos_retornaListaYStatus200() throws Exception {
        GastoDTO dto = buildGastoDTO();
        when(gastoServices.findAll()).thenReturn(List.of(buildGasto()));
        when(gastoServices.convertToDto(any(Gasto.class))).thenReturn(dto);

        mockMvc.perform(get("/api/gastos/mostratodos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].descripcionGasto").value("Compra de sillas"));
    }

    @Test
    @WithMockUser
    void getGastoById_retornaGastoYStatus200() throws Exception {
        GastoDTO dto = buildGastoDTO();
        when(gastoServices.findById(1L)).thenReturn(buildGasto());
        when(gastoServices.convertToDto(any(Gasto.class))).thenReturn(dto);

        mockMvc.perform(get("/api/gastos/buscar/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.montoGasto").value("5000"));
    }

    @Test
    @WithMockUser
    void getGastosEntreFechas_retornaListaYStatus200() throws Exception {
        GastoDTO dto = buildGastoDTO();
        when(gastoServices.findByFechaRegistroBetween(any(), any())).thenReturn(List.of(buildGasto()));
        when(gastoServices.convertToDto(any(Gasto.class))).thenReturn(dto);

        mockMvc.perform(get("/api/gastos/buscarEntreFechas")
                        .param("fechaInicio", "2025-01-01")
                        .param("fechaFin", "2025-12-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idGasto").value(1));
    }

    @Test
    @WithMockUser
    void createGasto_retornaGastoCreadoYStatus201() throws Exception {
        GastoDTO dto = buildGastoDTO();
        when(gastoServices.saveGasto(any(GastoDTO.class))).thenReturn(buildGasto());
        when(gastoServices.convertToDto(any(Gasto.class))).thenReturn(dto);

        mockMvc.perform(post("/api/gastos/guardargasto")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.descripcionGasto").value("Compra de sillas"));
    }

    @Test
    @WithMockUser
    void updateGasto_retornaGastoActualizadoYStatus200() throws Exception {
        GastoDTO dto = buildGastoDTO();
        when(gastoServices.updateGasto(eq(1L), any(GastoDTO.class))).thenReturn(buildGasto());
        when(gastoServices.convertToDto(any(Gasto.class))).thenReturn(dto);

        mockMvc.perform(put("/api/gastos/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.montoGasto").value("5000"));
    }

    @Test
    @WithMockUser
    void deleteGasto_retornaStatus204() throws Exception {
        doNothing().when(gastoServices).deleteGasto(1L);

        mockMvc.perform(delete("/api/gastos/1").with(csrf()))
                .andExpect(status().isNoContent());

        verify(gastoServices).deleteGasto(1L);
    }
}
