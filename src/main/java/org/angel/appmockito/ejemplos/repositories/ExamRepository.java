package org.angel.appmockito.ejemplos.repositories;

import org.angel.appmockito.ejemplos.models.Exam;

import java.util.List;

public interface ExamRepository {
    Exam save(Exam exam);
    List<Exam> findAll();
}
