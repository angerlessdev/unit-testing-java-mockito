package org.angel.appmockito.ejemplos.repositories;

import org.angel.appmockito.ejemplos.Data;
import org.angel.appmockito.ejemplos.models.Exam;

import java.util.List;

public class ExamRepositoryImpl implements ExamRepository {
    @Override
    public Exam save(Exam exam) {
        System.out.println("ExamRepositoryImpl.save");
        return Data.EXAM;
    }

    @Override
    public List<Exam> findAll() {
        System.out.println("ExamRepositoryImpl.findAll");
        return Data.EXAMS;
    }
}
