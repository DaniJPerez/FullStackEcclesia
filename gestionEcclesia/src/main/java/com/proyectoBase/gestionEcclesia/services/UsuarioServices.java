package com.proyectoBase.gestionEcclesia.services;

import com.proyectoBase.gestionEcclesia.DTOS.LoginUserDTO;
import com.proyectoBase.gestionEcclesia.DTOS.UsuarioDto;
import com.proyectoBase.gestionEcclesia.modele.Usuario;
import com.proyectoBase.gestionEcclesia.repositories.UsuarioRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioServices {
    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    public Usuario createUsuario(UsuarioDto usuarioDto) {
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario(usuarioDto.getUserName());
        usuario.setContrasenia(passwordEncoder.encode(usuarioDto.getPassword()));
        usuario.setEmail(usuarioDto.getCorreo());
        return repository.save(usuario);
    }

    public Usuario validacionIngresoUsuario(LoginUserDTO login) {
        Usuario usuario = repository.findByNombreUsuario(login.getUserName());
        if(usuario!= null && passwordEncoder.matches(login.getContrasenia(), usuario.getContrasenia())){
            return usuario;
        }
        return null;
    }

    public Usuario updatePassword(LoginUserDTO usuario, String newPassword) {
        Usuario usuarioValidador = repository.findByNombreUsuario(usuario.getUserName());
        if(usuarioValidador!=null && passwordEncoder.matches(usuario.getContrasenia(), usuarioValidador.getContrasenia())){
            usuarioValidador.setContrasenia(passwordEncoder.encode(newPassword));
            return usuarioValidador;
        }
        return null;
    }


}
