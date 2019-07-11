
CREATE TABLE batch_errors(
    job_execution_id bigint NOT NULL,
    error_id int NOT NULL,
    etapa varchar(10) NOT NULL,
    elemento int NOT NULL,
    descripcion varchar(255) NOT NULL,
    linea bigint NULL,
    input varchar(255) NULL,
    entidad varchar(255) NULL,
    stack_trace varchar(max) NOT NULL
);

ALTER TABLE batch_errors
ADD CONSTRAINT PK_batch_errors PRIMARY KEY (job_execution_id, error_id);