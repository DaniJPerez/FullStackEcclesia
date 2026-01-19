package com.proyectoBase.gestionEcclesia.modele;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tipo_eventos", uniqueConstraints = @UniqueConstraint(name = "uk_tipo_evento_nombre", columnNames = {"nombre_tipo_evento"}))
public  class TipoEvento {
    @Id
    @Column(name = "id_tipo_evento")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipo_evento_seq")
    @SequenceGenerator(
            name = "tipo_evento_seq",
            sequenceName = "SEC_TIPO_EVENTO", // Nombre de la secuencia en la BD (Convención: SEC_NOMBRE_TABLA)
            allocationSize = 1 // Determina cuántos IDs Hibernate pide a la vez (1 es seguro)
    )
    private Long id;

    @Column(name = "nombre_tipo_evento", length = 30)
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_registro", foreignKey = @ForeignKey(name = "fk_tipo_evento_usuario_registro"))
    private Usuario usuarioRegirstro;

}
