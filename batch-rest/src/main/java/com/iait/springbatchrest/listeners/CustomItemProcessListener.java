package com.iait.springbatchrest.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemProcessListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.iait.springbatchrest.enums.EtapaBatch;
import com.iait.springbatchrest.services.BatchErrorService;

@Component
@StepScope
public class CustomItemProcessListener implements ItemProcessListener<Object, Object> {

    private static Logger LOG = LoggerFactory.getLogger(CustomItemProcessListener.class);

    @Value("#{stepExecution}")
    private StepExecution execution;

    @Autowired
    private BatchErrorService batchErrorService;

    @Override
    public void beforeProcess(Object item) {}

    @Override
    public void afterProcess(Object item, Object result) {}

    @Override
    public void onProcessError(Object item, Exception e) {
        LOG.debug("Error procesando...");
        Long jobExecutionId = execution.getJobExecutionId();
        int processCount = execution.getReadCount() + execution.getWriteCount();
        batchErrorService.registrarError(jobExecutionId, EtapaBatch.PROCESS, processCount, item, e);
    }

}
