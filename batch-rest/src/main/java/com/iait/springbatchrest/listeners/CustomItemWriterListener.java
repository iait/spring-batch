package com.iait.springbatchrest.listeners;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.iait.springbatchrest.enums.EtapaBatch;
import com.iait.springbatchrest.services.BatchErrorService;

@Component
@StepScope
public class CustomItemWriterListener implements ItemWriteListener<Object> {

    private static Logger LOG = LoggerFactory.getLogger(CustomItemWriterListener.class);

    @Value("#{stepExecution}")
    private StepExecution execution;

    @Autowired
    private BatchErrorService batchErrorService;

    @Override
    public void beforeWrite(List<? extends Object> items) {}

    @Override
    public void afterWrite(List<? extends Object> items) {}

    @Override
    public void onWriteError(Exception e, List<? extends Object> items) {
        LOG.debug("Error escribiendo...");
        Long jobExecutionId = execution.getJobExecutionId();
        int writeCount = execution.getWriteCount() + 1;
        batchErrorService.registrarError(jobExecutionId, EtapaBatch.WRITE, writeCount, items, e);
    }

}
