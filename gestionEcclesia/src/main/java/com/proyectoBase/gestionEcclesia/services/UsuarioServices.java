package com.proyectoBase.gestionEcclesia.services;

import com.proyectoBase.gestionEcclesia.DTOS.LoginUserDTO;
import com.proyectoBase.gestionEcclesia.DTOS.UsuarioDto;
import com.proyectoBase.gestionEcclesia.modele.Persona;
import com.proyectoBase.gestionEcclesia.modele.RolSistema;
import com.proyectoBase.gestionEcclesia.modele.Usuario;
import com.proyectoBase.gestionEcclesia.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class UsuarioServices implements UserDetailsService{

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    public Usuario createUsuario(UsuarioDto usuarioDto) {
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario(usuarioDto.getUserName());
        // Guardar contraseña ya codificada
        usuario.setContrasenia(passwordEncoder.encode(usuarioDto.getPassword()));
        usuario.setEmail(usuarioDto.getCorreo());
        var fechaRegistro = LocalDate.now();
        usuario.setFechaRegistro(fechaRegistro);

        var modoUsuario = usuarioDto.getModo() != null && !usuarioDto.getModo().isBlank()
                ? Usuario.convertirRolService(usuarioDto.getModo())
                : RolSistema.ROLE_USER; // default si no envían modo
        usuario.setRolSistema(modoUsuario);

        // Si hay sesión activa, registrar quién lo creó
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            usuario.setUsuarioAdministrador(repository.findByNombreUsuario(auth.getName()));
        }

        return repository.save(usuario);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = repository.findByNombreUsuario(username);
        if (usuario == null) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + username);
        }
        // Retornar UserDetails con contraseña ya codificada (BCrypt)
        return User.withUsername(usuario.getNombreUsuario())
                .password(usuario.getContrasenia())
                .roles(usuario.getRolSistema() != null
                        ? usuario.getRolSistema().name().replace("ROLE_", "")
                        : "USER")
                .build();
    }

    // (opcional) método de validación programática si lo necesitas:
    public Usuario validacionIngresoUsuario(LoginUserDTO login) {
        Usuario usuario = repository.findByNombreUsuario(login.getUserName());
        if (usuario != null && passwordEncoder.matches(login.getContrasenia(), usuario.getContrasenia())) {
            return usuario;
        }
        throw new BadCredentialsException("Usuario o contraseña incorrectos");
    }

    public Usuario updatePassword(LoginUserDTO usuario, String newPassword) {
        Usuario usuarioValidador = repository.findByNombreUsuario(usuario.getUserName());
        if (usuarioValidador != null && passwordEncoder.matches(usuario.getContrasenia(), usuarioValidador.getContrasenia())) {
            usuarioValidador.setContrasenia(passwordEncoder.encode(newPassword));
            return usuarioValidador;
        }
        return null;
    }



}
