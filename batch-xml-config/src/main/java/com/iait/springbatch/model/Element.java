package com.iait.springbatch.model;

import java.io.Serializable;

public class Element implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String text;

    public Element() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Element [id=" + id + ", text=" + text + "]";
    }
}
