package com.laboratoriosdrogavet.core.rrhh.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import com.laboratoriosdrogavet.core.rrhh.enums.EstadoVacaciones;
import com.laboratoriosdrogavet.core.rrhh.enums.TipoVacacion;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import lombok.*;

@Entity
@Table(
    name = "detalle_vacaciones",
    indexes = {
        @Index(name = "idx_detalle_periodo", columnList = "periodo_id"),
        @Index(name = "idx_detalle_estado", columnList = "estado"),
        @Index(name = "idx_detalle_tipo", columnList = "tipo")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleVacaciones {

    // =========================
    // IDENTIFICACIÓN
    // =========================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =========================
    // FECHAS
    // =========================

    @NotNull
    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @NotNull
    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    // =========================
    // DÍAS TOMADOS (calculado)
    // =========================

    @Min(1)
    @Column(name = "dias_tomados", nullable = false)
    private int diasTomados;

    // =========================
    // TIPO
    // =========================
    
    @Builder.Default
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoVacacion tipo = TipoVacacion.NORMAL;

    // =========================
    // ESTADO
    // =========================
    
    @Builder.Default
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoVacaciones estado = EstadoVacaciones.PENDIENTE;

    // =========================
    // OBSERVACIÓN
    // =========================

    @Size(max = 255)
    @Column(length = 255)
    private String observacion;

    // =========================
    // RELACIÓN
    // =========================

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "periodo_id", nullable = false)
    private PeriodoVacaciones periodo;

    // =========================
    // VALIDACIÓN DE FECHAS
    // =========================

    @AssertTrue(message = "La fecha fin debe ser posterior o igual a la fecha inicio")
    private boolean isRangoFechasValido() {
        if (fechaInicio == null || fechaFin == null) return true;
        return !fechaFin.isBefore(fechaInicio);
    }

    // =========================
    // CÁLCULO AUTOMÁTICO
    // =========================

    @PrePersist
    @PreUpdate
    private void calcularDiasTomados() {
        if (fechaInicio != null && fechaFin != null) {
            this.diasTomados =
                (int) ChronoUnit.DAYS.between(fechaInicio, fechaFin) + 1;
        }
    }

    // ⚠️ No permitir modificación manual
    public void setDiasTomados(int diasTomados) {
        this.diasTomados = diasTomados;
    }
}
