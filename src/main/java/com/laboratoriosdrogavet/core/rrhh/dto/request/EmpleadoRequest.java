package com.laboratoriosdrogavet.core.rrhh.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmpleadoRequest {

    @NotBlank
    private String dni;

    @NotBlank
    private String nombres;

    @NotBlank
    private String apellidos;

    @NotBlank
    private String cargo;

    @NotNull
    private Long empresaId;

    @NotNull
    private Long areaId;
}

