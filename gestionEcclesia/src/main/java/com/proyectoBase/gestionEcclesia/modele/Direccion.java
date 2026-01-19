package com.proyectoBase.gestionEcclesia.modele;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "direcciones", uniqueConstraints = @UniqueConstraint(name="uk_direccion_carrera_calle_numero_barrio", columnNames = {"carrera", "calle", "numero", "id_barrio"}))
public class Direccion {
    @Id
    @Column(name = "id_direccion", precision = 10, scale = 0)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "direccion_seq")
    @SequenceGenerator(
            name = "direccion_seq",
            sequenceName = "SEC_DIRECCION", // Nombre de la secuencia en la BD (Convención: SEC_NOMBRE_TABLA)
            allocationSize = 1 // Determina cuántos IDs Hibernate pide a la vez (1 es seguro)
    )
    private Long id;

    @Column(name = "carrera", length = 35, nullable = false)
    private String carrera;

    @Column(name = "calle", length = 35, nullable = false)
    private String calle;

    @Column(name = "numero", length = 4, nullable = false)
    private String numero;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_barrio")
    private Barrio barrio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_registro", foreignKey = @ForeignKey(name = "fk_direccion_usuario_registro"))
    private Usuario usuarioRegirstro;



}
