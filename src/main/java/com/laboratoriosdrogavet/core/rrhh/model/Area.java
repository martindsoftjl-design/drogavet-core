package com.laboratoriosdrogavet.core.rrhh.model;

import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import lombok.*;

@Entity
@Table(name = "areas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Area {

    // =========================
    // IDENTIFICACIÓN
    // =========================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =========================
    // DATOS DEL ÁREA
    // =========================

    @NotBlank(message = "El nombre del área es obligatorio")
    @Column(nullable = false, unique = true, length = 100)
    private String nombre;

    // =========================
    // RELACIONES
    // =========================

    @OneToMany(mappedBy = "area", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Empleado> empleados;
}
