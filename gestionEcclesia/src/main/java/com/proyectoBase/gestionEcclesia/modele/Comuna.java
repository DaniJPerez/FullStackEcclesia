package com.proyectoBase.gestionEcclesia.modele;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Entity
@Data
@Component
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comunas", uniqueConstraints = @UniqueConstraint(name="uk_comuna_nombre_ciudad", columnNames = {"nombre_barrio", "id_ciudad"}))
public class Comuna {
    @Id
    @Column(name = "id_comuna", precision = 6)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comuna_seq")
    @SequenceGenerator(
            name = "comuna_seq",
            sequenceName = "SEC_COMUNA", // Nombre de la secuencia en la BD (Convención: SEC_NOMBRE_TABLA)
            allocationSize = 1 // Determina cuántos IDs Hibernate pide a la vez (1 es seguro)
    )
    private Long id;

    @Column(name = "nombre_barrio", length = 35, nullable = false)
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ciudad", foreignKey = @ForeignKey(name="FK_COMUNA_CIUDAD"))
    private Ciudad ciudad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_registro", foreignKey = @ForeignKey(name = "fk_comunas_usuario_registro"))
    private Usuario usuarioRegirstro;



}
