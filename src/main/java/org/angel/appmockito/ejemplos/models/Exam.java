package org.angel.appmockito.ejemplos.models;

import java.util.ArrayList;
import java.util.List;

public class Exam {
    private Long id;
    private String name;
    private List<String> questions;


    public Exam(Long id, String name) {
        this.id = id;
        this.questions = new ArrayList<String>();
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<String> getQuestions() {
        return questions;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setQuestions(List<String> questions) {
        this.questions = questions;
    }

    public void setName(String name) {
        this.name = name;
    }
}
