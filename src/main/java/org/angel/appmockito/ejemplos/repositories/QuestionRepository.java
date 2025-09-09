package org.angel.appmockito.ejemplos.repositories;

import java.util.List;

public interface QuestionRepository {
    void saveQuestions(List<String> questions);
    List<String> findQuestionsByExamId(Long examId);
}
