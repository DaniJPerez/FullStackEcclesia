package com.proyectoBase.gestionEcclesia.modele;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Entity
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name= "recursos_fisicos", uniqueConstraints = @UniqueConstraint(name= "uk_recurso_fisico_direccion_categoria", columnNames = {"id_direccion", "nombre_categoria"}))
public class RecursoFisico extends Recurso{

    @Column(name = "costo")
    @Positive
    private BigDecimal costo;

    @ManyToOne
    @JoinColumn(name = "id_direccion")
    private Direccion direccion;

    @Column(name = "nombre_categoria", length = 30)
    private String categoria;

    @Column(name = "cantidad", scale = 0, precision = 10)
    @Positive
    private int cantidad;
}
