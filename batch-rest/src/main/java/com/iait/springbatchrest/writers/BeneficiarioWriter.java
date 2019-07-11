package com.iait.springbatchrest.writers;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.iait.springbatchrest.entities.BeneficiarioEntity;
import com.iait.springbatchrest.services.BeneficiarioService;

@Component
public class BeneficiarioWriter implements ItemWriter<BeneficiarioEntity> {

    private static Logger LOG = LoggerFactory.getLogger(BeneficiarioWriter.class);

    @Autowired
    private BeneficiarioService beneficiarioService;

    @Override
    public void write(List<? extends BeneficiarioEntity> items) throws Exception {
        LOG.debug("Escribiendo beneficiarios...");
        TimeUnit.SECONDS.sleep(2L);
        beneficiarioService.alta(items);
    }
}
