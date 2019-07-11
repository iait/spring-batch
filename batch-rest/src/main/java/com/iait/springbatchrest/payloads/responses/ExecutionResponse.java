package com.iait.springbatchrest.payloads.responses;

import java.time.LocalDateTime;
import java.util.List;

public class ExecutionResponse {

    private String nombreArchivo;
    private String nombreTarea;
    private String estado;
    private LocalDateTime fechaInicio;
    private LocalDateTime ultimaActualizacion;
    private LocalDateTime fechaFinalizacion;
    private List<ExecutionErrorResponse> errores;

    public ExecutionResponse() {}

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public String getNombreTarea() {
        return nombreTarea;
    }

    public void setNombreTarea(String nombreTarea) {
        this.nombreTarea = nombreTarea;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDateTime getUltimaActualizacion() {
        return ultimaActualizacion;
    }

    public void setUltimaActualizacion(LocalDateTime ultimaActualizacion) {
        this.ultimaActualizacion = ultimaActualizacion;
    }

    public LocalDateTime getFechaFinalizacion() {
        return fechaFinalizacion;
    }

    public void setFechaFinalizacion(LocalDateTime fechaFinalizacion) {
        this.fechaFinalizacion = fechaFinalizacion;
    }

    public List<ExecutionErrorResponse> getErrores() {
        return errores;
    }

    public void setErrores(List<ExecutionErrorResponse> errores) {
        this.errores = errores;
    }
}
