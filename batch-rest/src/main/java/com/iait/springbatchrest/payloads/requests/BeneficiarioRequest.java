package com.iait.springbatchrest.payloads.requests;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;

import com.iait.springbatchrest.dtos.BeneficiarioDto;

public class BeneficiarioRequest implements BeneficiarioDto {

    @NotBlank
    private String nombre;

    @NotBlank
    private String apellido;

    private String cuil;

    private LocalDate fechaNacimiento;

    public BeneficiarioRequest() {}

    @Override
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    @Override
    public String getCuil() {
        return cuil;
    }

    public void setCuil(String cuil) {
        this.cuil = cuil;
    }

    @Override
    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }
}
