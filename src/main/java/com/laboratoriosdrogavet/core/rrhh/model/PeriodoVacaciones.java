package com.laboratoriosdrogavet.core.rrhh.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.laboratoriosdrogavet.core.rrhh.enums.EstadoPeriodo;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import lombok.*;

@Entity
@Table(
    name = "periodos_vacaciones",
    indexes = {
        @Index(name = "idx_periodo_empleado", columnList = "empleado_id"),
        @Index(name = "idx_periodo_estado", columnList = "estado")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PeriodoVacaciones {

    // =========================
    // IDENTIFICACIÓN
    // =========================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =========================
    // PERIODO REAL
    // =========================

    @NotNull
    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @NotNull
    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    // =========================
    // DÍAS LEGALES
    // =========================
    
    @Builder.Default
    @Min(0)
    @Column(name = "dias_ganados", nullable = false)
    private int diasGanados = 30;
    
    @Builder.Default
    @Min(0)
    @Column(name = "dias_tomados", nullable = false)
    private int diasTomados = 0;
    
    @Builder.Default
    @Min(0)
    @Column(nullable = false)
    private int saldo = 30;

    // =========================
    // CONTROL DE SALIDAS
    // =========================
    
    @Builder.Default
    @Min(0)
    @Column(name = "salidas_max", nullable = false)
    private int salidasMax = 2;
    
    @Builder.Default
    @Min(0)
    @Column(name = "salidas_usadas", nullable = false)
    private int salidasUsadas = 0;

    // =========================
    // ESTADO
    // =========================
    
    @Builder.Default
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoPeriodo estado = EstadoPeriodo.ACTIVO;

    // =========================
    // RELACIONES
    // =========================

    @ManyToOne(optional = false)
    @JoinColumn(name = "empleado_id", nullable = false)
    private Empleado empleado;

    @Builder.Default
    @OneToMany(mappedBy = "periodo", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<DetalleVacaciones> detalles = new ArrayList<>();

    // =========================
    // VALIDACIONES
    // =========================

    @AssertTrue(message = "La fecha fin debe ser posterior o igual a la fecha inicio")
    private boolean isPeriodoValido() {
        if (fechaInicio == null || fechaFin == null) return true;
        return !fechaFin.isBefore(fechaInicio);
    }

    // =========================
    // CÁLCULO AUTOMÁTICO
    // =========================

    @PrePersist
    @PreUpdate
    private void calcularSaldo() {
        this.saldo = this.diasGanados - this.diasTomados;
    }


    // ⚠️ Saldo NO debe modificarse manualmente
    protected void setSaldo(int saldo) {
        this.saldo = saldo;
    }
    

}
