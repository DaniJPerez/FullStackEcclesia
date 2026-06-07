package com.proyectoBase.gestionEcclesia.modele;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name="usuarios", uniqueConstraints = @UniqueConstraint(name = "uk_usuario_nombre_email", columnNames = {"nombre_usuario", "email"}))
public class Usuario {

    @Id
    @Column(name = "id_usuario", precision = 12)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuario_seq")
    @SequenceGenerator(name = "usuario_seq", sequenceName = "SEC_USUARIO", allocationSize = 1)
    private Long idUsuario;

    @Column(name = "nombre_usuario", length = 35, nullable = false, unique = true)
    private String nombreUsuario;

    @Column(name = "email", length = 50, nullable = false, unique = true)
    @Email
    private String email;

    @Column(name = "contrasena", length = 60, nullable = false)
    @Size(min = 8, max = 60, message = "La contraseña debe tener al menos 8 caracteres")
    private String contrasenia;

    @Column(name = "fecha_registro")
    private LocalDate fechaRegistro;

    @Column(name = "fecha_modificacion")
    private LocalDate fechaModificacion;

    @Column(name = "activo")
    @ColumnDefault("1")
    private Boolean activo;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol_sistema", length = 20)
    private RolSistema rolSistema;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_administrador", foreignKey = @ForeignKey(name = "fk_usuario_administrador"))
    @ToString.Exclude
    private Usuario usuarioAdministrador;

    public static RolSistema convertirRolService(String rol) {
        try {
            return RolSistema.valueOf(rol.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Rol no válido: " + rol);
        }
    }
}