package com.iait.springbatchrest.cfg;

import java.io.FileNotFoundException;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.builder.SimpleStepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.iait.springbatchrest.entities.BeneficiarioEntity;
import com.iait.springbatchrest.items.inputs.BeneficiarioInput;
import com.iait.springbatchrest.listeners.CustomItemProcessListener;
import com.iait.springbatchrest.listeners.CustomItemReadListener;
import com.iait.springbatchrest.listeners.CustomItemWriterListener;
import com.iait.springbatchrest.processors.BeneficiarioProcessor;
import com.iait.springbatchrest.readers.BeneficiarioReader;
import com.iait.springbatchrest.writers.BeneficiarioWriter;

@Configuration
public class JobConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private BeneficiarioReader beneficiarioReader;

    @Autowired
    private CustomItemReadListener readListener;

    @Autowired
    private BeneficiarioProcessor beneficiarioProcessor;

    @Autowired
    private CustomItemProcessListener processListener;

    @Autowired
    private BeneficiarioWriter beneficiarioWriter;

    @Autowired
    private CustomItemWriterListener writerListener;

    @Bean
    @Qualifier("beneficiarioJob")
    public Job getBeneficiarioJob() {
        SimpleStepBuilder<BeneficiarioInput, BeneficiarioEntity> stepBuilder = 
                stepBuilderFactory.get("step1").chunk(2);
        Step step = stepBuilder
                .reader(beneficiarioReader)
                    .listener(readListener)
                .processor(beneficiarioProcessor)
                    .listener(processListener)
                .writer(beneficiarioWriter)
                    .listener(writerListener)
                .faultTolerant()
                    .skipLimit(10)
                    .skip(Exception.class)
                    .noSkip(FileNotFoundException.class)
                .build();
        Job job = jobBuilderFactory.get("beneficiarioJob")
                .preventRestart()
                .start(step)
                .build();
        return job;
    }
}
