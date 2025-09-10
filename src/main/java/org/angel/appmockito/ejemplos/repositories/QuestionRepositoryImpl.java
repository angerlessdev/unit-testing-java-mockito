package org.angel.appmockito.ejemplos.repositories;

import org.angel.appmockito.ejemplos.Data;

import java.util.List;

public class QuestionRepositoryImpl implements QuestionRepository {
    @Override
    public void saveQuestions(List<String> questions) {
        System.out.println("QuestionRepositoryImpl.saveQuestions");
    }

    @Override
    public List<String> findQuestionsByExamId(Long examId) {
        System.out.println("QuestionRepositoryImpl.findQuestionsByExamId");
        return Data.QUESTIONS;
    }
}
