package com.iait.springbatchrest.ctrls;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.iait.springbatchrest.entities.BeneficiarioEntity;
import com.iait.springbatchrest.payloads.requests.BeneficiarioRequest;
import com.iait.springbatchrest.payloads.responses.BeneficiarioResponse;
import com.iait.springbatchrest.payloads.responses.ExecutionErrorResponse;
import com.iait.springbatchrest.payloads.responses.ExecutionResponse;
import com.iait.springbatchrest.services.BeneficiarioService;

@Controller
public class BeneficiarioCtrl {

    @Autowired
    private BeneficiarioService beneficiarioService;

    @Autowired
    @Qualifier("beneficiarioJob")
    private Job beneficiarioJob;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired 
    private JobExplorer jobExplorer;

    @GetMapping(path = "/api/beneficiarios/padron/{id}")
    public ResponseEntity<ExecutionResponse> padronEstado(
            @PathVariable(name = "id") Long id) {

        JobExecution execution = jobExplorer.getJobExecution(id);
        ExecutionResponse response = new ExecutionResponse();
        String nombreArchivo = execution.getJobParameters().getString("originalFileName");
        response.setNombreArchivo(nombreArchivo);
        response.setNombreTarea(execution.getJobInstance().getJobName());
        response.setEstado(execution.getStatus().name());

        LocalDateTime fechaInicio = execution.getStartTime().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDateTime();
        response.setFechaInicio(fechaInicio);
        LocalDateTime ultimaActualizacion = execution.getLastUpdated().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDateTime();
        response.setUltimaActualizacion(ultimaActualizacion);
        LocalDateTime fechaFinalizacion = execution.getEndTime() == null 
                ? null 
                : execution.getEndTime().toInstant().atZone(ZoneId.systemDefault())
                        .toLocalDateTime();
        response.setFechaFinalizacion(fechaFinalizacion);

        List<ExecutionErrorResponse> errores = execution.getAllFailureExceptions().stream()
                .map(t -> {
                    StringWriter sw = new StringWriter();
                    t.printStackTrace(new PrintWriter(sw));
                    ExecutionErrorResponse errorResponse = new ExecutionErrorResponse();
                    errorResponse.setStackTrace(sw.toString());
                    return errorResponse;
                }).collect(Collectors.toList());
        response.setErrores(errores);

        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/api/beneficiarios")
    public ResponseEntity<List<BeneficiarioResponse>> buscarTodos() {
        List<BeneficiarioResponse> response = beneficiarioService.buscarTodos().stream()
                .map(entity -> new BeneficiarioResponse(entity))
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/api/beneficiarios/{id}")
    public ResponseEntity<BeneficiarioResponse> buscar(@PathVariable(name = "id") Long id) {
        Optional<BeneficiarioEntity> entityOpt = beneficiarioService.buscarPorId(id);
        if (!entityOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        BeneficiarioResponse response = new BeneficiarioResponse(entityOpt.get());
        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/api/beneficiarios")
    public ResponseEntity<?> alta(@RequestBody BeneficiarioRequest request) 
            throws URISyntaxException {
        BeneficiarioEntity entity = beneficiarioService.alta(request);
        URI location = new URI("/api/beneficiarios/" + entity.getId());
        return ResponseEntity.created(location).build();
    }

    @PostMapping(path = "/api/beneficiarios/padron")
    public ResponseEntity<Long> alta(@RequestParam(name = "padron") MultipartFile padron) 
            throws Exception {

        String contentType = padron.getContentType();
        MimeType mimeType = MimeTypeUtils.parseMimeType(contentType);
        if (!mimeType.isCompatibleWith(MimeTypeUtils.TEXT_PLAIN)) {
            return ResponseEntity.badRequest().build();
        }

        String fileName = padron.getOriginalFilename();
        String prefix = fileName.substring(0, fileName.lastIndexOf("."));
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        File tmpFile = File.createTempFile(prefix, suffix);

        try (FileOutputStream tmpFileStream = new FileOutputStream(tmpFile)) {
            tmpFileStream.write(padron.getBytes());
        }

        JobParameters params = new JobParametersBuilder()
                .addString("resourceLocation", tmpFile.getAbsolutePath())
                .addString("originalFileName", fileName)
                .toJobParameters(); 
        JobExecution execution = jobLauncher.run(beneficiarioJob, params);

        return ResponseEntity.ok(execution.getId());
    }
}
