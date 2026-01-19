package com.proyectoBase.gestionEcclesia.DTOS;

import com.proyectoBase.gestionEcclesia.modele.AsistenciaEvento;
import com.proyectoBase.gestionEcclesia.modele.Sexo;
import com.proyectoBase.gestionEcclesia.modele.EstadoCivil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.proyectoBase.gestionEcclesia.modele.Sexo;
import org.springframework.format.annotation.DateTimeFormat;


import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class MiembroDTO {
    private Long id;
    
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
    
    private String nombre2;
    
    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;
    
    private String apellido2;
    
    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe ser en el pasado")
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDate fechaNacimiento;
    
    @NotNull(message = "El estado civil es obligatorio")
    private String estadoCivil;
    
    @Email(message = "El correo debe tener un formato válido")
    private String correo;
    
    private String telefono;
    
    @Valid
    private DireccionDTO direccion;
    
    @NotNull(message = "El sexo es obligatorio")
    private String sexo;

    @NotNull
    private RolDTO rolDTO;

    private List<AsistenciaEventoDto> asistenciaEvento;

    private List<ContribucionDTO> contribuciones;

    private UsuarioDto usuarioCreador;

}
