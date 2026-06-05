package com.proyectoBase.gestionEcclesia.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyectoBase.gestionEcclesia.DTOS.LoginUserDTO;
import com.proyectoBase.gestionEcclesia.DTOS.UsuarioDto;
import com.proyectoBase.gestionEcclesia.config.SecurityConfig;
import com.proyectoBase.gestionEcclesia.modele.Usuario;
import com.proyectoBase.gestionEcclesia.services.UsuarioServices;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
@Import(SecurityConfig.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UsuarioServices usuarioServices;

    private UsuarioDto buildDto() {
        return new UsuarioDto(null, "admin", "pass123", LocalDate.of(2024, 1, 1), true, "admin@test.com", "ROLE_ADMIN");
    }

    private Usuario buildUsuario() {
        Usuario u = new Usuario();
        u.setIdUsuario(1L);
        u.setNombreUsuario("admin");
        u.setEmail("admin@test.com");
        return u;
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void create_retornaUsuarioCreadoYStatus201() throws Exception {
        when(usuarioServices.createUsuario(any(UsuarioDto.class))).thenReturn(buildUsuario());

        mockMvc.perform(post("/api/usuarios")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildDto())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombreUsuario").value("admin"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updatePassword_conCredencialesValidas_retornaUsuarioYStatus200() throws Exception {
        LoginUserDTO loginDto = new LoginUserDTO("admin", "pass123");
        when(usuarioServices.updatePassword(any(LoginUserDTO.class), eq("newPass123"))).thenReturn(buildUsuario());

        mockMvc.perform(put("/api/usuarios/password")
                        .with(csrf())
                        .param("newPassword", "newPass123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreUsuario").value("admin"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updatePassword_conCredencialesInvalidas_retorna401() throws Exception {
        LoginUserDTO loginDto = new LoginUserDTO("admin", "wrongpass");
        when(usuarioServices.updatePassword(any(LoginUserDTO.class), eq("newPass123"))).thenReturn(null);

        mockMvc.perform(put("/api/usuarios/password")
                        .with(csrf())
                        .param("newPassword", "newPass123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    void create_sinPermisoAdmin_retorna403() throws Exception {
        mockMvc.perform(post("/api/usuarios")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildDto())))
                .andExpect(status().isForbidden());
    }
}
