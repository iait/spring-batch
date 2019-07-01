package com.iait.springbatchrest.ctrls;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
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

import com.iait.springbatchrest.elements.BeneficiarioElement;
import com.iait.springbatchrest.entities.BeneficiarioEntity;
import com.iait.springbatchrest.payloads.requests.BeneficiarioRequest;
import com.iait.springbatchrest.payloads.responses.BeneficiarioResponse;
import com.iait.springbatchrest.readers.BeneficiarioReader;
import com.iait.springbatchrest.services.BeneficiarioService;

@Controller
public class BeneficiarioCtrl {

    @Autowired
    private BeneficiarioService beneficiarioService;

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
    public ResponseEntity<?> alta(@RequestParam(name = "padron") MultipartFile padron) 
            throws IOException {

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
        ItemReader<BeneficiarioElement> reader = new BeneficiarioReader(resource);

        return ResponseEntity.ok().build();
    }
}
