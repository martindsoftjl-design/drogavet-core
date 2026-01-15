package com.laboratoriosdrogavet.core.rrhh.dto.response;

import lombok.*;

@Getter
@AllArgsConstructor
@Builder
public class EmpleadoResponse {

    private Long id;
    private String dni;
    private String nombres;
    private String apellidos;
    private String cargo;
    private boolean cesado;
}

