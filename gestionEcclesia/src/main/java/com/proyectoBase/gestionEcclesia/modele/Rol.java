package com.proyectoBase.gestionEcclesia.modele;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="roles", uniqueConstraints = @UniqueConstraint(name = "uk_nombre_rol", columnNames = {"nombre_rol"}))
public class Rol {
    @Id
    @Column(name = "id_rol")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rol_seq")
    @SequenceGenerator(
            name = "rol_seq",
            sequenceName = "SEC_ROL", // Nombre de la secuencia en la BD (Convención: SEC_NOMBRE_TABLA)
            allocationSize = 1 // Determina cuántos IDs Hibernate pide a la vez (1 es seguro)
    )
    private Long idRol;

    @Column(name= "nombre_rol", length = 15, nullable = true)
    private String nombreRol;

    @Column(name= "descripcion_rol", length = 35)
    private String descripcionRol;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_registro", foreignKey = @ForeignKey(name = "fk_rol_usuario_registro"))
    private Usuario usuarioRegirstro;
}
