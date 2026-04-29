package com.proyectoBase.gestionEcclesia.config;

import com.proyectoBase.gestionEcclesia.modele.Usuario;
import com.proyectoBase.gestionEcclesia.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UsuarioContextUtil {

    private final UsuarioRepository usuarioRepository;

    public Usuario getUsuarioAutenticado() {
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        Usuario usuario = usuarioRepository.findByNombreUsuario(username);
        if (usuario == null) throw new UsernameNotFoundException("Usuario no encontrado: " + username);
        return usuario;
    }
}