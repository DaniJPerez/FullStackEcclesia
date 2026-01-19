package com.proyectoBase.gestionEcclesia.modele;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "personas", uniqueConstraints = @UniqueConstraint(name = "uk_persona_identificacion", columnNames = {"identificacion"}))
public class Persona {
    //identidficador unico
    @Id
    @Column(name = "identificacion", precision = 12, scale = 0,nullable = false, unique = true)
    private Long numeroIdentificacion;

    @Column(name = "primer_nombre", length = 15, nullable = false)
    private String primerNombre;

    @Column(name = "segundo_nombre", length = 15)
    private String segundoNombre;

    @Column(name = "primer_apellido",length = 15, nullable = false)
    private String primerApellido;

    @Column(name = "segundo_apellido", length = 15)
    private String segundoApellido;

    @Column(name = "telefono", length = 10)
    private String telefono;

    @Column(name = "correo", length = 55)
    private String correo;

    @Column(name="contrasena", length = 30,nullable = false)
    @Size(min = 8, max = 30, message = "La contraseña debe tener al menos 8 caracteres")
    private String contrasenia;

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_nacionalidad", foreignKey =@ForeignKey(name = "FK_NACIONALIDAD_PERSONA"))
    private Nacionalidad nacionalidad;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_direccion", foreignKey =@ForeignKey(name = "FK_DIRECCION_PERSONA"))
    private Direccion direccion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_civil", length = 25)
    private EstadoCivil estadoCivil;

    @Enumerated(EnumType.STRING)
    @Column(name = "sexo", length = 25, nullable = false)
    private Sexo sexo;

    @ManyToOne
    @JoinColumn(name = "id_rol", foreignKey = @ForeignKey(name = "FK_ROL_PERSONA"))
    private Rol rol;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "congregacion_id_iglesia")
    private Iglesia congregacion;

    @Column(name = "activo_persona")
    @ColumnDefault("1")
    private boolean activo;

    @OneToMany(mappedBy = "persona")
    private List<AsistenciaEvento> asistenciasEventos;

    @OneToMany(mappedBy = "persona")
    private List<Contribucion> contribuciones;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_registro", foreignKey =@ForeignKey(name = "FK_USUARIO_PERSONA"))
    private Usuario usuarioRegistro;

    public void agregarAsistencia(AsistenciaEvento asistenciaEvento){
        this.asistenciasEventos.add(asistenciaEvento);
        asistenciaEvento.setPersona(this);
    }

    public void agregarContribucion(Contribucion nuevaContribucion){
        this.contribuciones.add(nuevaContribucion);
        nuevaContribucion.setPersona(this);
    }

    public static Sexo convertirStringASexo(String sexo) {
        try{
            return Sexo.valueOf(sexo.toUpperCase().trim());
        }catch (Exception e){
            throw new IllegalArgumentException("Valor de sexo no válido: " + sexo);
        }
    }

    public static EstadoCivil convertirStringAEstadoCivil(String estadiCivil) {
        try{
            return EstadoCivil.valueOf(estadiCivil.toUpperCase().trim());
        }catch (Exception exception){
            throw new IllegalArgumentException("Valor de estado civil no válido: " + estadiCivil);
        }
    }
}
