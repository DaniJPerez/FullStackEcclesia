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
@Table(name="recursos_economicos", uniqueConstraints = @UniqueConstraint(name = "uk_recurso_economico_valor_recurso", columnNames = {"valor_recurso"}))
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("ECONOMICO")
public class RecursoEconomico extends Recurso{
    @Column(name= "valor_recurso")
    @Positive
    private BigDecimal valorRecurso;
}
