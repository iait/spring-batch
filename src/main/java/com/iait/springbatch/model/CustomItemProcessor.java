package com.iait.springbatch.model;

import org.springframework.batch.item.ItemProcessor;

public class CustomItemProcessor implements ItemProcessor<Transaction, Transaction> {

    @Override
    public Transaction process(Transaction item) throws Exception {
        System.out.println("Processing... " + item);
        return item;
    }

}
