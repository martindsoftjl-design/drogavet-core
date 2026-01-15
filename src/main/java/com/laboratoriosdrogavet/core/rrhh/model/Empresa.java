package com.laboratoriosdrogavet.core.rrhh.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import lombok.*;

@Entity
@Table(name = "empresas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Empresa {

    // =========================
    // IDENTIFICACIÓN
    // =========================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =========================
    // DATOS LEGALES
    // =========================

    @NotBlank(message = "El RUC es obligatorio")
    @Pattern(regexp = "\\d{11}", message = "El RUC debe tener exactamente 11 dígitos")
    @Column(nullable = false, length = 11, unique = true)
    private String ruc;

    @NotBlank(message = "La razón social es obligatoria")
    @Size(min = 3, max = 200)
    @Column(nullable = false, length = 200)
    private String razonSocial;

    @Size(max = 200)
    @Column(length = 200)
    private String nombreComercial;

    @NotBlank(message = "La dirección es obligatoria")
    @Size(max = 250)
    @Column(nullable = false, length = 250)
    private String direccion;

    // =========================
    // ESTADO
    // =========================
    
    @Builder.Default
    @Column(nullable = false)
    private boolean activo = true;
}
