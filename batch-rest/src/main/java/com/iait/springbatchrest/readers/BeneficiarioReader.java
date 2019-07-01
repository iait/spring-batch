package com.iait.springbatchrest.readers;

import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.core.io.Resource;

import com.iait.springbatchrest.elements.BeneficiarioElement;

public class BeneficiarioReader extends FlatFileItemReader<BeneficiarioElement> {

    public BeneficiarioReader(Resource resource) {

        setResource(resource);
    }

}
