package org.angel.appmockito.ejemplos.services;

import org.angel.appmockito.ejemplos.models.Exam;
import org.angel.appmockito.ejemplos.repositories.ExamRepository;
import org.angel.appmockito.ejemplos.repositories.QuestionRepository;

import java.util.List;
import java.util.Optional;

public class ExamServiceImpl implements ExamService{

    private final ExamRepository examRepository;
    private final QuestionRepository questionRepository;

    public ExamServiceImpl(ExamRepository examRepository, QuestionRepository questionRepository) {
        this.examRepository = examRepository;
        this.questionRepository = questionRepository;
    }

    @Override
    public Exam save(Exam exam) {
        if(!exam.getQuestions().isEmpty()) questionRepository.saveQuestions(exam.getQuestions());
        return examRepository.save(exam);
    }

    @Override
    public Optional<Exam> findExamByName(String name) {
        return examRepository.findAll().stream().filter(exam -> exam.getName().contains(name)).findFirst();
    }

    @Override
    public Exam findExamByNameWithQuestions(String name) {
        Optional<Exam> examOptional = findExamByName(name);
        Exam exam = null;

        if (examOptional.isPresent()) {
            exam = examOptional.orElseThrow();
            List<String> questions = questionRepository.findQuestionsByExamId(exam.getId());
            exam.setQuestions(questions);
        }
        return exam;
    }

}
