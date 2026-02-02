package com.proyectoBase.gestionEcclesia.modele;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="iglesias", uniqueConstraints = @UniqueConstraint(name = "uk_nombre_iglesia", columnNames = "nombre_iglesia"))
@Data
public class Iglesia {
    @Id
    @Column(name = "id_iglesia")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "iglesia_seq")
    @SequenceGenerator(
            name = "iglesia_seq",
            sequenceName = "SEC_IGLESIA", // Nombre de la secuencia en la BD (Convención: SEC_NOMBRE_TABLA)
            allocationSize = 1 // Determina cuántos IDs Hibernate pide a la vez (1 es seguro)
    )
    private Long idIglesia;

    @Column(name = "nombre_iglesia", length = 35)
    private String nombreIglesia;
    @Column(name = "numero_iglesia_zona")
    private short numeroIglesiaZona;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_pastor", foreignKey = @ForeignKey(name = "fk_iglesia_persona"))
    private Persona pastor;

    @Column(name= "telefono_contacto", length = 10)
    private String telefonoContacto;

    @Column(name = "correo_electronico", length = 50)
    private String correoElectronico;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_direccion", foreignKey = @ForeignKey(name = "fk_iglesia_direccion"))
    private Direccion direccion;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_zona_administrativa", foreignKey = @ForeignKey(name = "fk_iglesia_zona_administrativa"))
    private ZonaAdministrativa zonaAdministrativa;

    @OneToMany(mappedBy = "congregacion", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Persona> miembros;

    @OneToMany(mappedBy = "iglesia", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Recurso> recursos;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_registro", foreignKey = @ForeignKey(name = "fk_iglesia_usuario_registro"))
    private Usuario usuarioRegistro;

}
