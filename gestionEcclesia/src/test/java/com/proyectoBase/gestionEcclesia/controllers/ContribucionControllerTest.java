package com.proyectoBase.gestionEcclesia.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyectoBase.gestionEcclesia.DTOS.ContribucionDTO;
import com.proyectoBase.gestionEcclesia.DTOS.MiembroDTO;
import com.proyectoBase.gestionEcclesia.DTOS.TipoContribucionDTO;
import com.proyectoBase.gestionEcclesia.modele.Contribucion;
import com.proyectoBase.gestionEcclesia.services.ContribucionService;
import com.proyectoBase.gestionEcclesia.services.UsuarioServices;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ContribucionController.class)
class ContribucionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ContribucionService contribucionService;

    @MockitoBean
    private UsuarioServices usuarioServices;

    private ContribucionDTO buildContribucionDTO() {
        MiembroDTO miembro = new MiembroDTO();
        miembro.setId(1L);
        TipoContribucionDTO tipo = new TipoContribucionDTO(1L, "Diezmo", "10%");
        return new ContribucionDTO(1L, LocalDate.of(2025, 3, 10),
                new BigDecimal("10000"), "Ninguna", miembro, tipo, null);
    }

    private Contribucion buildContribucion() {
        Contribucion c = new Contribucion();
        c.setId(1L);
        c.setMonto(new BigDecimal("10000"));
        return c;
    }

    @Test
    @WithMockUser
    void getAllContribuciones_retornaListaYStatus200() throws Exception {
        when(contribucionService.findAll()).thenReturn(List.of(buildContribucion()));
        when(contribucionService.convertToDTO(any(Contribucion.class))).thenReturn(buildContribucionDTO());

        mockMvc.perform(get("/api/contribuciones/mostrarTodos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].monto").value(10000));
    }

    @Test
    @WithMockUser
    void getContribucionById_retornaContribucionYStatus200() throws Exception {
        when(contribucionService.findById(1L)).thenReturn(buildContribucion());
        when(contribucionService.convertToDTO(any(Contribucion.class))).thenReturn(buildContribucionDTO());

        mockMvc.perform(get("/api/contribuciones/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser
    void getContribucionesByMiembro_retornaListaYStatus200() throws Exception {
        when(contribucionService.findByMiembro(1L)).thenReturn(List.of(buildContribucion()));
        when(contribucionService.convertToDTO(any(Contribucion.class))).thenReturn(buildContribucionDTO());

        mockMvc.perform(get("/api/contribuciones/miembro/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].observaciones").value("Ninguna"));
    }

    @Test
    @WithMockUser
    void getContribucionesEntreFechas_retornaListaYStatus200() throws Exception {
        when(contribucionService.findByFechas(any(), any())).thenReturn(List.of(buildContribucion()));
        when(contribucionService.convertToDTO(any(Contribucion.class))).thenReturn(buildContribucionDTO());

        mockMvc.perform(get("/api/contribuciones/entre-fechas")
                        .param("inicio", "2025-01-01")
                        .param("fin", "2025-12-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].monto").value(10000));
    }

    @Test
    @WithMockUser
    void createContribucion_retornaCreadaYStatus201() throws Exception {
        ContribucionDTO dto = buildContribucionDTO();
        when(contribucionService.save(any(ContribucionDTO.class))).thenReturn(buildContribucion());
        when(contribucionService.convertToDTO(any(Contribucion.class))).thenReturn(dto);

        mockMvc.perform(post("/api/contribuciones")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.monto").value(10000));
    }

    @Test
    @WithMockUser
    void updateContribucion_retornaActualizadaYStatus200() throws Exception {
        ContribucionDTO dto = buildContribucionDTO();
        when(contribucionService.update(eq(1L), any(ContribucionDTO.class))).thenReturn(buildContribucion());
        when(contribucionService.convertToDTO(any(Contribucion.class))).thenReturn(dto);

        mockMvc.perform(put("/api/contribuciones/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser
    void deleteContribucion_retornaStatus204() throws Exception {
        doNothing().when(contribucionService).delete(1L);

        mockMvc.perform(delete("/api/contribuciones/1").with(csrf()))
                .andExpect(status().isNoContent());

        verify(contribucionService).delete(1L);
    }
}
