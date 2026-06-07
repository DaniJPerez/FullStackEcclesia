package com.proyectoBase.gestionEcclesia.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyectoBase.gestionEcclesia.DTOS.RolDTO;
import com.proyectoBase.gestionEcclesia.modele.Rol;
import com.proyectoBase.gestionEcclesia.services.RolService;
import com.proyectoBase.gestionEcclesia.config.JwtUtil;
import com.proyectoBase.gestionEcclesia.services.UsuarioServices;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import com.proyectoBase.gestionEcclesia.config.SecurityConfig;
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

@WebMvcTest(RolController.class)
@Import(SecurityConfig.class)
class RolControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RolService rolService;

    @MockitoBean
    private UsuarioServices usuarioServices;

    @MockitoBean
    private JwtUtil jwtUtil;

    private RolDTO buildRolDTO() {
        return new RolDTO(1L, "ROLE_ADMIN", "Administrador del sistema");
    }

    private Rol buildRol() {
        Rol rol = new Rol();
        rol.setIdRol(1L);
        rol.setNombreRol("ROLE_ADMIN");
        rol.setDescripcionRol("Administrador del sistema");
        return rol;
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllRoles_retornaListaYStatus200() throws Exception {
        RolDTO dto = buildRolDTO();
        when(rolService.findAll()).thenReturn(List.of(buildRol()));
        when(rolService.convertToDTO(any(Rol.class))).thenReturn(dto);

        mockMvc.perform(get("/api/roles/mostrarTodos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombreRol").value("ROLE_ADMIN"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getRolById_retornaRolYStatus200() throws Exception {
        RolDTO dto = buildRolDTO();
        when(rolService.findById(1L)).thenReturn(buildRol());
        when(rolService.convertToDTO(any(Rol.class))).thenReturn(dto);

        mockMvc.perform(get("/api/roles/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createRol_retornaRolCreadoYStatus201() throws Exception {
        RolDTO dto = buildRolDTO();
        when(rolService.save(any(RolDTO.class))).thenReturn(buildRol());
        when(rolService.convertToDTO(any(Rol.class))).thenReturn(dto);

        mockMvc.perform(post("/api/roles/agregar")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombreRol").value("ROLE_ADMIN"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateRol_retornaRolActualizadoYStatus200() throws Exception {
        RolDTO dto = buildRolDTO();
        when(rolService.update(eq(1L), any(RolDTO.class))).thenReturn(buildRol());
        when(rolService.convertToDTO(any(Rol.class))).thenReturn(dto);

        mockMvc.perform(put("/api/roles/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descripcionRol").value("Administrador del sistema"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteRol_retornaStatus204() throws Exception {
        doNothing().when(rolService).delete(1L);

        mockMvc.perform(delete("/api/roles/1").with(csrf()))
                .andExpect(status().isNoContent());

        verify(rolService).delete(1L);
    }

    @Test
    @WithMockUser(roles = "USER")
    void getAllRoles_sinPermisoAdmin_retorna403() throws Exception {
        mockMvc.perform(get("/api/roles/mostrarTodos"))
                .andExpect(status().isForbidden());
    }
}
