package com.proyectoBase.gestionEcclesia.modele;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name="usuarios", uniqueConstraints = @UniqueConstraint(name = "uk_usuario_nombre_email", columnNames = {"nombre_usuario", "email"}))
public class Usuario {

    @Id
    @Column(name= "id_usuario", precision = 12)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuario_seq")
    @SequenceGenerator(
            name = "usuario_seq",
            sequenceName = "SEC_USUARIO", // Nombre de la secuencia en la BD (Convención: SEC_NOMBRE_TABLA)
            allocationSize = 1 // Determina cuántos IDs Hibernate pide a la vez (1 es seguro)
    )
    private Long idUsuario;

    @Column(name="nombre_usuario", length = 35, nullable = false, unique = true)
    private String nombreUsuario;

    @Column(name="email", length = 50, nullable = false, unique = true)
    @Email
    private String email;

    @Column(name="contrasena", length = 60, nullable = false)
    @Size(min = 8, max = 60, message = "La contraseña debe tener al menos 8 caracteres")
    private String contrasenia;

    @Column(name="fecha_registro")
    @CreatedDate
    private LocalDate fechaRegistro;

    @Column(name="fecha_modificacion")
    @CreatedDate
    private LocalDate fechaModificacion;

    @Column(name="activo")
    @ColumnDefault("1")
    private Boolean activo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_administrador", foreignKey = @ForeignKey(name = "fk_usuario_administrador"))
    private Usuario usuarioAdministrador;

}
