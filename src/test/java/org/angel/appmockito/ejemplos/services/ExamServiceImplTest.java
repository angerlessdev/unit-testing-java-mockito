package org.angel.appmockito.ejemplos.services;

import org.angel.appmockito.ejemplos.Data;
import org.angel.appmockito.ejemplos.models.Exam;
import org.angel.appmockito.ejemplos.repositories.ExamRepository;
import org.angel.appmockito.ejemplos.repositories.ExamRepositoryImpl;
import org.angel.appmockito.ejemplos.repositories.QuestionRepository;
import org.angel.appmockito.ejemplos.repositories.QuestionRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExamServiceImplTest {
    @Mock
    ExamRepositoryImpl examRepository;
    @Mock
    QuestionRepositoryImpl questionRepository;
    @InjectMocks
    ExamServiceImpl service;
    @Captor
    ArgumentCaptor<Long> captor;

    @BeforeEach
    void setUp() {
        // repository = mock(ExamRepository.class);
        // questionRepository = mock(QuestionRepository.class);
        // service = new ExamServiceImpl(repository, questionRepository);
    }

    @Test
    void findExamByName() {
        when(examRepository.findAll()).thenReturn(Data.EXAMS);
        Optional<Exam> exam = service.findExamByName("Mate");

        assertTrue(exam.isPresent());
        assertEquals("Mate", exam.get().getName());
        assertEquals(5L, exam.orElseThrow().getId());

    }

    @Test
    void findExamByNameEmpty() {
        List<Exam> exams = Collections.emptyList();

        when(examRepository.findAll()).thenReturn(exams);
        Optional<Exam> exam = service.findExamByName("Mate");

        assertTrue(exam.isEmpty());
    }

    @Test
    void testQuestionsExam() {
        when(examRepository.findAll()).thenReturn(Data.EXAMS);
        when(questionRepository.findQuestionsByExamId(5L)).thenReturn(Data.QUESTIONS);

        Exam exam = service.findExamByNameWithQuestions("Mate");
        assertEquals(5, exam.getQuestions().size());
        assertTrue(exam.getQuestions().contains("Trigonometry"));
    }

    @Test
    void testQuestionsExamVerify() {
        when(examRepository.findAll()).thenReturn(Data.EXAMS);
        when(questionRepository.findQuestionsByExamId(5L)).thenReturn(Data.QUESTIONS);

        Exam exam = service.findExamByNameWithQuestions("Mate");
        assertEquals(5, exam.getQuestions().size());
        assertTrue(exam.getQuestions().contains("Trigonometry"));

        // Verificación de interacciones
        verify(examRepository).findAll();                     // se llamó al repo de exámenes
        verify(questionRepository).findQuestionsByExamId(5L); // se pidió la lista de preguntas del examen con id=5
                                                              // o usar anyLong()
    }

    @Test
    void testSaveExam() {
        // BDD
        // Given
        Exam newExam = Data.EXAM;
        newExam.setQuestions(Data.QUESTIONS);

        when(examRepository.save(any(Exam.class))).then(new Answer<Exam>() {
            Long sequence = 8L;
            @Override
            public Exam answer(InvocationOnMock invocationOnMock) throws Throwable {
                Exam exam = invocationOnMock.getArgument(0);
                exam.setId(sequence++);
                return exam;
            }
        });
        // Exam exam = service.save(new Exam(8L, "Physics"));
        // When
        Exam exam = service.save(newExam);

        // Then
        assertNotNull(exam.getId());
        assertEquals("Physics", exam.getName());
        assertEquals(8L, exam.getId());
        verify(examRepository).save(any(Exam.class));
        verify(questionRepository).saveQuestions(anyList());
    }

    @Test
    void testHandlingException() {
        when(examRepository.findAll()).thenReturn(Data.EXAMS);
        when(questionRepository.findQuestionsByExamId(anyLong())).thenThrow(IllegalArgumentException.class);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> service.findExamByNameWithQuestions("Mate"));

        assertEquals(IllegalArgumentException.class, exception.getClass());
        verify(examRepository).findAll();
        verify(questionRepository).findQuestionsByExamId(anyLong());
    }

    @Test
    void testHandlingExceptionNullId() {
        when(examRepository.findAll()).thenReturn(Data.EXAMS_ID_NULL);
        // Any argument that is null will throw IllegalArgumentException
        when(questionRepository.findQuestionsByExamId(isNull())).thenThrow(IllegalArgumentException.class);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> service.findExamByNameWithQuestions("Mate"));

        assertEquals(IllegalArgumentException.class, exception.getClass());
    }

    @Test
    void testArgumentMatchers(){
        when(examRepository.findAll()).thenReturn(Data.EXAMS);
        when(questionRepository.findQuestionsByExamId(anyLong())).thenReturn(Data.QUESTIONS);
        service.findExamByNameWithQuestions("Mate");

        verify(examRepository).findAll();
        verify(questionRepository).findQuestionsByExamId(argThat(arg -> arg.equals(5L)));
    }

    @Test
    void testArgumentCaptor() {
        when(examRepository.findAll()).thenReturn(Data.EXAMS);
        //when(questionRepository.findQuestionsByExamId(anyLong())).thenReturn(Data.QUESTIONS);

        service.findExamByNameWithQuestions("Mate");

        verify(questionRepository).findQuestionsByExamId(captor.capture());

        assertEquals(5L, captor.getValue());
    }

    @Test
    void testDoThrow() {
        Exam exam = Data.EXAM;
        exam.setQuestions(Data.QUESTIONS);
        // For void methods
        doThrow(IllegalArgumentException.class).when(questionRepository).saveQuestions(anyList());

        assertThrows(IllegalArgumentException.class, () -> service.save(exam));
    }

    @Test
    void testDoAnswer() {
        when(examRepository.findAll()).thenReturn(Data.EXAMS);
        // when(questionRepository.findQuestionsByExamId(anyLong())).thenReturn(Data.QUESTIONS);
        doAnswer(invocation -> {
            Long id = invocation.getArgument(0); // PARAMETER POSITION
            return id == 5L? Data.QUESTIONS : null;
        }).when(questionRepository).findQuestionsByExamId(anyLong());

        Exam exam = service.findExamByNameWithQuestions("Language");

        assertEquals(5, exam.getQuestions().size());
        assertEquals(5L, exam.getId());
        assertEquals("Mate", exam.getName());

        verify(questionRepository).findQuestionsByExamId(anyLong());
    }

    @Test
    void testDoAnswerSaveExam() {
        // BDD
        // Given
        Exam newExam = Data.EXAM;
        newExam.setQuestions(Data.QUESTIONS);

        doAnswer(new Answer<Exam>() {
            Long sequence = 8L;
            @Override
            public Exam answer(InvocationOnMock invocationOnMock) throws Throwable {
                Exam exam = invocationOnMock.getArgument(0);
                exam.setId(sequence++);
                return exam;
            }
        }).when(examRepository).save(any(Exam.class));
        // Exam exam = service.save(new Exam(8L, "Physics"));
        // When
        Exam exam = service.save(newExam);

        // Then
        assertNotNull(exam.getId());
        assertEquals("Physics", exam.getName());
        assertEquals(8L, exam.getId());
        verify(examRepository).save(any(Exam.class));
        verify(questionRepository).saveQuestions(anyList());
    }

    @Test
    void testDoCallRealMethod() {
        when(examRepository.findAll()).thenReturn(Data.EXAMS);
        //when(questionRepository.findQuestionsByExamId(anyLong())).thenReturn(Data.QUESTIONS);
        doCallRealMethod().when(questionRepository).findQuestionsByExamId(anyLong());

        Exam exam = service.findExamByNameWithQuestions("Mate");
        assertEquals(5L, exam.getQuestions().size());
        assertEquals("Mate", exam.getName());
    }
}












