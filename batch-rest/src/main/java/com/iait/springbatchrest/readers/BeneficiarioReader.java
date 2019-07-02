package com.iait.springbatchrest.readers;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.FixedLengthTokenizer;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import com.iait.springbatchrest.items.inputs.BeneficiarioInput;

@Component
@StepScope
public class BeneficiarioReader extends FlatFileItemReader<BeneficiarioInput> {

    public BeneficiarioReader(
            @Value("#{jobParameters['resourceLocation']}") String resourceLocation) {

        FileSystemResource resource = new FileSystemResource(resourceLocation);
        setResource(resource);

        FixedLengthTokenizer tokenizer = new FixedLengthTokenizer();
        tokenizer.setNames("nombreCompleto", "cuil", "fechaNacimiento");
        tokenizer.setColumns(new Range(1, 19), new Range(20, 30), new Range(31, 38));

        BeanWrapperFieldSetMapper<BeneficiarioInput> fieldSetMapper = 
                new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(BeneficiarioInput.class);

        DefaultLineMapper<BeneficiarioInput> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        setLineMapper(lineMapper);
    }
}
