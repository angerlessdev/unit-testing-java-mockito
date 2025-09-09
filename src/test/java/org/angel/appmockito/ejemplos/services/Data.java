package org.angel.appmockito.ejemplos.services;

import org.angel.appmockito.ejemplos.models.Exam;

import java.util.Arrays;
import java.util.List;

public class Data {
    public final static List<Exam> EXAMS = Arrays.asList(
            new Exam(5L, "Mate"),
            new Exam(6L, "Language"),
            new Exam(7L, "History"));

    public final static List<String> QUESTIONS = Arrays.asList(
            "Arithmetic", "Integrals", "Derivatives", "Geometry", "Trigonometry");

    public final static Exam EXAM = new Exam(8L, "Physics");
}
