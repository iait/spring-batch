package com.iait.springbatchrest.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.iait.springbatchrest.enums.EtapaBatch;
import com.iait.springbatchrest.services.BatchErrorService;

@Component
@StepScope
public class CustomItemReadListener implements ItemReadListener<Object> {

    private static Logger LOG = LoggerFactory.getLogger(CustomItemReadListener.class);

    @Value("#{stepExecution}")
    private StepExecution execution;

    @Autowired
    private BatchErrorService batchErrorService;

    @Override
    public void beforeRead() {}

    @Override
    public void afterRead(Object item) {}

    @Override
    public void onReadError(Exception e) {
        LOG.debug("Error leyendo...");
        Long jobExecutionId = execution.getJobExecutionId();
        int readCount = execution.getReadCount();
        batchErrorService.registrarError(jobExecutionId, EtapaBatch.READ, readCount + 1, null, e);
    }
}
