package org.angel.appmockito.ejemplos.services;

import org.angel.appmockito.ejemplos.models.Exam;

import java.util.Optional;

public interface ExamService {
    Exam save(Exam exam);
    Optional<Exam> findExamByName(String name);
    Exam findExamByNameWithQuestions(String name);
}
