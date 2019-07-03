package com.iait.springbatchrest.cfg;

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
    private BeneficiarioProcessor beneficiarioProcessor;

    @Autowired
    private BeneficiarioWriter beneficiarioWriter;

    @Bean
    @Qualifier("beneficiarioJob")
    public Job getBeneficiarioJob() {
        SimpleStepBuilder<BeneficiarioInput, BeneficiarioEntity> stepBuilder = 
                stepBuilderFactory.get("step1").chunk(2);
        Step step = stepBuilder
                .reader(beneficiarioReader)
                .processor(beneficiarioProcessor)
                .writer(beneficiarioWriter)
                .build();
        Job job = jobBuilderFactory.get("beneficiarioJob")
                .preventRestart()
                .start(step)
                .build();
        return job;
    }
}
