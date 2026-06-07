package com.proyectoBase.gestionEcclesia.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyectoBase.gestionEcclesia.DTOS.LoginResponseDTO;
import com.proyectoBase.gestionEcclesia.DTOS.LoginUserDTO;
import com.proyectoBase.gestionEcclesia.DTOS.UsuarioDto;
import com.proyectoBase.gestionEcclesia.modele.Usuario;
import com.proyectoBase.gestionEcclesia.config.JwtUtil;
import com.proyectoBase.gestionEcclesia.services.UsuarioServices;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import com.proyectoBase.gestionEcclesia.config.SecurityConfig;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UsuarioServices usuarioServices;

    @MockitoBean
    private JwtUtil jwtUtil;

    private UsuarioDto buildUsuarioDto() {
        return new UsuarioDto(null, "usuario1", "pass123", LocalDate.now(), true, "u@test.com", "ADMIN");
    }

    @Test
    void register_creaUsuarioYRetorna201() throws Exception {
        UsuarioDto dto = buildUsuarioDto();
        Usuario respuesta = new Usuario();
        respuesta.setIdUsuario(1L);
        respuesta.setNombreUsuario("usuario1");
        respuesta.setEmail("u@test.com");

        when(usuarioServices.createUsuario(any(UsuarioDto.class))).thenReturn(respuesta);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombreUsuario").value("usuario1"));
    }

    @Test
    void register_conCuerpoVacio_retorna400() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isCreated());
    }

    @Test
    void login_conCredencialesValidas_retornaTokenYStatus200() throws Exception {
        LoginUserDTO loginDto = new LoginUserDTO("usuario1", "pass123");
        LoginResponseDTO respuesta = new LoginResponseDTO("jwt-token-de-ejemplo", "Bearer", "usuario1", "u@test.com", "ROLE_USER");

        when(usuarioServices.login(any(LoginUserDTO.class))).thenReturn(respuesta);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreUsuario").value("usuario1"))
                .andExpect(jsonPath("$.tipo").value("Bearer"))
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    void login_conCredencialesInvalidas_retorna401() throws Exception {
        LoginUserDTO loginDto = new LoginUserDTO("usuario1", "wrongpass");

        when(usuarioServices.login(any(LoginUserDTO.class)))
                .thenThrow(new BadCredentialsException("Credenciales inválidas"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isUnauthorized());
    }
}