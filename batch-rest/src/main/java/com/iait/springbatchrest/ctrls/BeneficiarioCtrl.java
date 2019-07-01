package com.iait.springbatchrest.ctrls;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.step.builder.SimpleStepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SyncTaskExecutor;
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

import com.iait.springbatchrest.cfg.BatchConfig;
import com.iait.springbatchrest.entities.BeneficiarioEntity;
import com.iait.springbatchrest.items.inputs.BeneficiarioInput;
import com.iait.springbatchrest.payloads.requests.BeneficiarioRequest;
import com.iait.springbatchrest.payloads.responses.BeneficiarioResponse;
import com.iait.springbatchrest.processors.BeneficiarioProcessor;
import com.iait.springbatchrest.readers.BeneficiarioReader;
import com.iait.springbatchrest.services.BeneficiarioService;
import com.iait.springbatchrest.writers.BeneficiarioWriter;

@Controller
public class BeneficiarioCtrl {

    @Autowired
    private BeneficiarioService beneficiarioService;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private BeneficiarioReader beneficiarioReader;

    @Autowired
    private BeneficiarioProcessor beneficiarioProcessor;

    @Autowired
    private BeneficiarioWriter beneficiarioWriter;

    @Autowired
    private BatchConfig batchCon;

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
    public ResponseEntity<String> alta(@RequestParam(name = "padron") MultipartFile padron) 
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

        FileSystemResource resource = new FileSystemResource(tmpFile);
        beneficiarioReader.setResource(resource);

        SimpleStepBuilder<BeneficiarioInput, BeneficiarioEntity> stepBuilder = 
                stepBuilderFactory.get("step").chunk(2);
        Step step = stepBuilder
                .reader(beneficiarioReader)
                .processor(beneficiarioProcessor)
                .writer(beneficiarioWriter)
                .build();
        Job job = jobBuilderFactory.get("beneficiarioJob").start(step).build();

        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(batchCon.getJobRepository());
        jobLauncher.setTaskExecutor(new SyncTaskExecutor());
        JobExecution execution = jobLauncher.run(job, new JobParameters());
        ExitStatus status = execution.getExitStatus();

        return ResponseEntity.ok(status.getExitDescription());
    }
}
