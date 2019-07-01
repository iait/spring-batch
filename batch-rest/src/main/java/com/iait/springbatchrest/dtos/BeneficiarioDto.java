package com.iait.springbatchrest.dtos;

import java.time.LocalDate;

public interface BeneficiarioDto {

    String getNombre();

    String getApellido();

    String getCuil();

    LocalDate getFechaNacimiento();
}
