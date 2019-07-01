package com.iait.springbatchrest.ctrls;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.iait.springbatchrest.entities.BeneficiarioEntity;
import com.iait.springbatchrest.payloads.requests.BeneficiarioRequest;
import com.iait.springbatchrest.payloads.responses.BeneficiarioResponse;
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
}
