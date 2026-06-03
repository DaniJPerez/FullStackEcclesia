package com.proyectoBase.gestionEcclesia.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyectoBase.gestionEcclesia.DTOS.FinanzaDTO;
import com.proyectoBase.gestionEcclesia.modele.Finanza;
import com.proyectoBase.gestionEcclesia.services.FinanzaServices;
import com.proyectoBase.gestionEcclesia.services.GastoServices;
import com.proyectoBase.gestionEcclesia.services.ContribucionService;
import com.proyectoBase.gestionEcclesia.services.UsuarioServices;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.context.annotation.Import;
import com.proyectoBase.gestionEcclesia.config.SecurityConfig;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {FinanzaController.class, ContribucionController.class, GastoController.class})
@Import(SecurityConfig.class)
class FinanzaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private FinanzaServices finanzaServices;

    @MockitoBean
    private ContribucionService contribucionService;

    @MockitoBean
    private GastoServices gastoServices;

    @MockitoBean
    private UsuarioServices usuarioServices;

    private FinanzaDTO buildFinanzaDTO() {
        FinanzaDTO dto = new FinanzaDTO();
        dto.setId(1L);
        dto.setFechaInicioReporte("2025-01-01");
        dto.setFechaFinalReporte("2025-12-31");
        dto.setDescripcionReporte("Reporte anual");
        dto.setIngresosTotales("500000");
        dto.setGastosTotales("200000");
        dto.setSaldoActual("300000");
        dto.setListaGastos(Collections.emptyList());
        dto.setListaEntradas(Collections.emptyList());
        return dto;
    }

    private Finanza buildFinanza() {
        Finanza f = new Finanza();
        f.setId(1L);
        return f;
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllFinanzas_retornaListaYStatus200() throws Exception {
        when(finanzaServices.findAll()).thenReturn(List.of(buildFinanza()));
        when(finanzaServices.convertToDTO(any(Finanza.class))).thenReturn(buildFinanzaDTO());

        mockMvc.perform(get("/api/finanzas/mostrarTodos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].descripcionReporte").value("Reporte anual"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getFinanzaById_retornaFinanzaYStatus200() throws Exception {
        when(finanzaServices.findById(1L)).thenReturn(buildFinanza());
        when(finanzaServices.convertToDTO(any(Finanza.class))).thenReturn(buildFinanzaDTO());

        mockMvc.perform(get("/api/finanzas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getFinanzasPorFechas_retornaListaYStatus200() throws Exception {
        when(finanzaServices.findByRangoFechas(any(), any())).thenReturn(List.of(buildFinanza()));
        when(finanzaServices.convertToDTO(any(Finanza.class))).thenReturn(buildFinanzaDTO());

        mockMvc.perform(get("/api/finanzas/reporte")
                        .param("fechaInicio", "2025-01-01")
                        .param("fechaFin", "2025-12-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].ingresosTotales").value("500000"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void generarReporteEntreFechas_retornaFinanzaCalculadaYStatus200() throws Exception {
        when(gastoServices.findByFechaRegistroBetween(any(), any())).thenReturn(Collections.emptyList());
        when(contribucionService.findByFechas(any(), any())).thenReturn(Collections.emptyList());
        when(gastoServices.convertToDto(any())).thenReturn(null);
        when(contribucionService.convertToDTO(any())).thenReturn(null);
        when(finanzaServices.updateFinanzaFromDTO(any(Finanza.class), any(FinanzaDTO.class))).thenReturn(buildFinanza());
        when(finanzaServices.convertToDTO(any(Finanza.class))).thenReturn(buildFinanzaDTO());

        mockMvc.perform(get("/api/finanzas/reporteEntreFechas")
                        .param("fechaInicio", "2025-01-01")
                        .param("fechaFin", "2025-12-31"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createFinanza_retornaCreadaYStatus201() throws Exception {
        FinanzaDTO dto = buildFinanzaDTO();
        when(finanzaServices.saveFinanza(any(FinanzaDTO.class))).thenReturn(buildFinanza());
        when(finanzaServices.convertToDTO(any(Finanza.class))).thenReturn(dto);

        mockMvc.perform(post("/api/finanzas/crear")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.descripcionReporte").value("Reporte anual"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateFinanza_retornaActualizadaYStatus200() throws Exception {
        FinanzaDTO dto = buildFinanzaDTO();
        when(finanzaServices.updateFinanza(eq(1L), any(FinanzaDTO.class))).thenReturn(buildFinanza());
        when(finanzaServices.convertToDTO(any(Finanza.class))).thenReturn(dto);

        mockMvc.perform(put("/api/finanzas/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.saldoActual").value("300000"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteFinanza_retornaStatus204() throws Exception {
        doNothing().when(finanzaServices).deleteFinanza(1L);

        mockMvc.perform(delete("/api/finanzas/1").with(csrf()))
                .andExpect(status().isNoContent());

        verify(finanzaServices).deleteFinanza(1L);
    }

    @Test
    @WithMockUser(roles = "USER")
    void getAllFinanzas_sinPermisoAdmin_retorna403() throws Exception {
        mockMvc.perform(get("/api/finanzas/mostrarTodos"))
                .andExpect(status().isForbidden());
    }
}