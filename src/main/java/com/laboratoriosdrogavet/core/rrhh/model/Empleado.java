package com.laboratoriosdrogavet.core.rrhh.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.laboratoriosdrogavet.core.rrhh.enums.Afp;
import com.laboratoriosdrogavet.core.rrhh.enums.Banco;
import com.laboratoriosdrogavet.core.rrhh.enums.Cargo;
import com.laboratoriosdrogavet.core.rrhh.enums.RegimenLaboral;
import com.laboratoriosdrogavet.core.rrhh.enums.SistemaPension;
import com.laboratoriosdrogavet.core.rrhh.enums.SituacionLaboral;
import com.laboratoriosdrogavet.core.rrhh.enums.TipoContrato;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import lombok.*;

@Entity
@Table(
	    name = "empleados",
	    indexes = {
	        @Index(name = "idx_empleado_dni", columnList = "dni"),
	        @Index(name = "idx_empleado_empresa", columnList = "empresa_id"),
	        @Index(name = "idx_empleado_area", columnList = "area_id")
	    }
	)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Empleado {

    // =========================
    // IDENTIFICACIÓN
    // =========================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =========================
    // DATOS PERSONALES
    // =========================

    @NotBlank(message = "El DNI es obligatorio")
    @Column(nullable = false, length = 15, unique = true)
    private String dni;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false)
    private String nombres;

    @NotBlank(message = "El apellido es obligatorio")
    @Column(nullable = false)
    private String apellidos;

    @Past(message = "La fecha de nacimiento debe ser anterior a hoy")
    private LocalDate fechaNacimiento;

    @Size(max = 250, message = "La dirección no puede superar los 250 caracteres")
    @Column(length = 250)
    private String direccion;


    @Pattern(regexp = "\\d{9}", message = "El teléfono debe tener 9 dígitos")
    private String telefono;

    @Email(message = "Correo inválido")
    private String email;

    // =========================
    // DATOS LABORALES
    // =========================
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @Column(nullable = false)
    private LocalDate fechaIngreso;

    @PastOrPresent(message = "La fecha de cese no puede ser futura")
    private LocalDate fechaCese;


    @Size(max = 200, message = "El motivo de cese no puede superar los 200 caracteres")
    @Column(length = 200)
    private String motivoCese;


    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(length = 30, nullable = false)
    private Cargo cargo;


    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(length = 30, nullable = false)
    private TipoContrato tipoContrato;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(length = 30, nullable = false)
    private RegimenLaboral regimenLaboral;

    
    @Builder.Default
    @Column(nullable = false)
    private boolean cesado = false;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private SituacionLaboral situacion;

    
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_id", nullable = false)
    private Area area;

    // =========================
    // DATOS ADMINISTRATIVOS
    // =========================
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(length = 100, nullable = false)
    private Banco banco;

    @Size(max = 30)
    @Column(length = 30)
    private String numeroCuenta;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private SistemaPension sistemaPension;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private Afp afp;
    
    

    @Size(max = 50)
    @Column(length = 50)
    private String tipoComisionAfp;

    @Pattern(
        regexp = "\\d{10,15}",
        message = "El CUSPP debe tener entre 10 y 15 dígitos"
    )
    @Column(length = 15)
    private String cuspp;


    // =========================
    // CONTACTO DE EMERGENCIA
    // =========================

    @Size(max = 150)
    @Column(length = 150)
    private String contactoEmergenciaNombre;

    @Size(max = 50)
    @Column(length = 50)
    private String contactoEmergenciaParentesco;

    @Pattern(
        regexp = "\\d{9}",
        message = "El teléfono de emergencia debe tener 9 dígitos"
    )
    @Column(length = 9)
    private String contactoEmergenciaTelefono;

    // =========================
    // OTROS
    // =========================

    @Size(max = 500)
    @Column(length = 500)
    private String observaciones;
    
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime fechaCreacion;

    @UpdateTimestamp
    private LocalDateTime fechaActualizacion;


}

