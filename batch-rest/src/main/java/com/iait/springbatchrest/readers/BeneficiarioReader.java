package com.iait.springbatchrest.readers;

import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.FixedLengthTokenizer;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.stereotype.Component;

import com.iait.springbatchrest.items.inputs.BeneficiarioInput;

@Component
public class BeneficiarioReader extends FlatFileItemReader<BeneficiarioInput> {

    public BeneficiarioReader() {
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
