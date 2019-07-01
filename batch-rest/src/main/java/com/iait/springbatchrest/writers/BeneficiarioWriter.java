package com.iait.springbatchrest.writers;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.iait.springbatchrest.entities.BeneficiarioEntity;
import com.iait.springbatchrest.services.BeneficiarioService;

@Component
public class BeneficiarioWriter implements ItemWriter<BeneficiarioEntity> {

    @Autowired
    private BeneficiarioService beneficiarioService;

    @Override
    public void write(List<? extends BeneficiarioEntity> items) throws Exception {
        beneficiarioService.alta(items);
    }
}
