package com.iait.springbatchrest.payloads.responses;

import java.time.LocalDate;

import com.iait.springbatchrest.entities.BeneficiarioEntity;

public class BeneficiarioResponse {

    private Long id;

    private String nombre;

    private String apellido;

    private String cuil;

    private LocalDate fechaNacimiento;

    public BeneficiarioResponse(BeneficiarioEntity entity) {
        id = entity.getId();
        nombre = entity.getNombre();
        apellido = entity.getApellido();
        cuil = entity.getCuil();
        fechaNacimiento = entity.getFechaNacimiento();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCuil() {
        return cuil;
    }

    public void setCuil(String cuil) {
        this.cuil = cuil;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }
}
