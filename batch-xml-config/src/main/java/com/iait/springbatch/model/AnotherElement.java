package com.iait.springbatch.model;

import java.io.Serializable;

public class AnotherElement implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    public AnotherElement() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "AnotherElement [id=" + id + "]";
    }
}
