package com.proyectoBase.gestionEcclesia.modele;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "finanzas", uniqueConstraints = @UniqueConstraint(name= "uk_finanza_fecha_actualizacion", columnNames = {"fecha_actualizacion"}))
public class Finanza {
    @Id
    @Column(name = "id_finanzas", length = 12)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "finanza_seq")
    @SequenceGenerator(
            name = "finanza_seq",
            sequenceName = "SEC_FINANZA", // Nombre de la secuencia en la BD (Convención: SEC_NOMBRE_TABLA)
            allocationSize = 1 // Determina cuántos IDs Hibernate pide a la vez (1 es seguro)
    )
    private Long id;

    @ManyToMany
    @JoinTable(
            name = "finanza_contribuciones",
            joinColumns = @JoinColumn(name = "id_finanza"),
            inverseJoinColumns = @JoinColumn(name = "id_contribucion")
    )
    @ToString.Exclude
    private List<Contribucion> listaEntradas;

    @ManyToMany
    @JoinTable(
            name = "finanza_gastos",
            joinColumns = @JoinColumn(name = "id_finanza"),
            inverseJoinColumns = @JoinColumn(name = "id_gasto")
    )
    @ToString.Exclude
    private List<Gasto> listaGastos;

    @Column(name = "total_contribuciones", precision = 10, scale = 2)
    private BigDecimal gastosTotales;

    @Column(name= "ingresos_totales", precision = 10, scale = 2)
    private BigDecimal ingresosTotales;

    @Column(name= "egresos_totales", precision = 10, scale = 2)
    private BigDecimal egresosTotales;

    @Column(name = "saldo_actual", precision = 10, scale = 2)
    private BigDecimal saldoActual;

    @Column(name= "saldo_anterior", precision = 10, scale = 2)
    private BigDecimal saldoAnterior;

    @Column(name = "activo_total", precision = 10, scale = 2)
    private BigDecimal activoTotal;

    @Column(name="fecha_inicial_reporte")
    private LocalDate fechaInicialReporte;

    @Column(name="fecha_final_reporte")
    private LocalDate fechaFinalReporte;

    @Column(name="observaciones", length = 155)
    private String observaciones;

    @Column(name = "fecha_actualizacion")
    private LocalDate fechaActualizacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_registro", foreignKey = @ForeignKey(name = "fk_finanzas_usuario_registro"))
    private Usuario usuarioRegistro;



}
