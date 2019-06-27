package com.iait.springbatch.processors;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.iait.springbatch.model.AnotherElement;
import com.iait.springbatch.model.Element;

@Component
public class ElementProcessor implements ItemProcessor<Element, AnotherElement> {

    @Override
    public AnotherElement process(Element item) throws Exception {
        String anotherElementId = item.getId() + "::" + item.getText();
        AnotherElement anotherElement = new AnotherElement();
        anotherElement.setId(anotherElementId);
        return anotherElement;
    }

}
