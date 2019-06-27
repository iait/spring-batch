package com.iait.springbatch.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class RecordFieldSetMapper implements FieldSetMapper<Transaction> {

    @Override
    public Transaction mapFieldSet(FieldSet fieldSet) throws BindException {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Transaction transaction = new Transaction();
        transaction.setUsername(fieldSet.readString("username"));
        transaction.setUserId(fieldSet.readInt("userid"));
        transaction.setAmount(fieldSet.readDouble(3));
        String dateStr = fieldSet.readString(2);
        Date date;
        try {
            date = dateFormat.parse(dateStr);
            transaction.setTransactionDate(date);
        } catch (ParseException e) {
//            throw new BindException(transaction, "transactiondate");
            e.printStackTrace();
        }
        return transaction;
    }

}
