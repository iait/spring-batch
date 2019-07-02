package com.iait.springbatchrest.processors;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.iait.springbatchrest.entities.BeneficiarioEntity;
import com.iait.springbatchrest.items.inputs.BeneficiarioInput;

@Component
public class BeneficiarioProcessor 
            implements ItemProcessor<BeneficiarioInput, BeneficiarioEntity> {

    private static Logger LOG = LoggerFactory.getLogger(BeneficiarioProcessor.class);

    @Override
    public BeneficiarioEntity process(BeneficiarioInput item) throws Exception {
        LOG.debug("Procesando beneficiarios...");
        TimeUnit.SECONDS.sleep(1L);

        BeneficiarioEntity entity = new BeneficiarioEntity();

        String[] apellidoNombre = item.getNombreCompleto().split(",");
        String apellido = apellidoNombre[0].trim();
        String nombre = apellidoNombre[1].trim();
        entity.setNombre(nombre);
        entity.setApellido(apellido);

        entity.setCuil(item.getCuil());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate fechaNacimiento = LocalDate.parse(item.getFechaNacimiento(), formatter);
        entity.setFechaNacimiento(fechaNacimiento);

        return entity;
    }

}
