package org.hse.lab2.model;

import org.jetbrains.annotations.NotNull;

public class Teacher {
    private Integer id;
    private String fio;

    public Teacher(int id, String fio) {
        this.id = id;
        this.fio = fio;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    @NotNull
    @Override
    public String toString() {
        return getFio();
    }
}
