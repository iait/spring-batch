package com.iait.springbatchrest.entities;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.iait.springbatchrest.enums.EtapaBatch;

@Entity
@Table(name = "batch_errors")
public class BatchErrorEntity {

    @EmbeddedId
    private BatchErrorPkEntity pk;

    @Column(name = "job_execution_id", nullable = false, updatable = false, insertable = false)
    private Long jobExecutionId;

    @Column(name = "error_id", nullable = false, updatable = false, insertable = false)
    private Integer errorId;

    @Enumerated(EnumType.STRING)
    @Column(name = "etapa")
    private EtapaBatch etapa;

    @Column(name = "elemento")
    private Integer elemento;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "linea")
    private Integer linea;

    @Column(name = "input")
    private String input;

    @Column(name = "entidad")
    private String entidad;

    @Column(name = "stack_trace")
    private String stackTrace;

    public BatchErrorEntity() {
        pk = new BatchErrorPkEntity();
    }

    public Long getJobExecutionId() {
        return jobExecutionId;
    }

    public void setJobExecutionId(Long jobExecutionId) {
        this.jobExecutionId = jobExecutionId;
        pk.setJobExecutionId(jobExecutionId);
    }

    public Integer getErrorId() {
        return errorId;
    }

    public void setErrorId(Integer errorId) {
        this.errorId = errorId;
        pk.setErrorId(errorId);
    }

    public EtapaBatch getEtapa() {
        return etapa;
    }

    public void setEtapa(EtapaBatch etapa) {
        this.etapa = etapa;
    }

    public Integer getElemento() {
        return elemento;
    }

    public void setElemento(Integer elemento) {
        this.elemento = elemento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getLinea() {
        return linea;
    }

    public void setLinea(Integer linea) {
        this.linea = linea;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getEntidad() {
        return entidad;
    }

    public void setEntidad(String entidad) {
        this.entidad = entidad;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }
}
