package com.example.demo.service.serviceimple;

 
 

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.repository.LessonRepository;
import com.example.demo.repository.QuizRepository;
import com.example.demo.models.Quiz;
import com.example.demo.models.Lesson;

import com.example.demo.service.QuizService;
@Service
public class QuizServiceImpl implements QuizService {

    private final QuizRepository quizRepository;
    
    private final LessonRepository lessonRepository;

    public QuizServiceImpl(QuizRepository quizRepository, LessonRepository lessonRepository) {
        this.quizRepository = quizRepository;
        this.lessonRepository = lessonRepository;
    }

    @Override
    public Quiz addQuiz(Quiz Quiz, String teacherEmail) {
        Lesson lesson = lessonRepository.findById(Quiz.getLessonId())
                .orElseThrow(() -> new RuntimeException("Lesson not found"));
        
        if (!lesson.getCourse().getTeacher().getEmail().equals(teacherEmail)) {
            throw new RuntimeException("You can only add quizzes to your own courses");
        }

        Quiz quiz = new Quiz();
        quiz.setQuestion(Quiz.getQuestion());
        quiz.setOptionA(Quiz.getOptionA());
        quiz.setOptionB(Quiz.getOptionB());
        quiz.setOptionC(Quiz.getOptionC());
        quiz.setOptionD(Quiz.getOptionD());
        quiz.setCorrectAnswer(Quiz.getCorrectAnswer());
        quiz.setMarks(Quiz.getMarks());
        quiz.setLesson(lesson);
        
        Quiz savedQuiz = quizRepository.save(quiz);
        return savedQuiz;
    }

    @Override
    public Quiz updateQuiz(Long id, Quiz Quiz, String teacherEmail) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));
        
        if (!quiz.getLesson().getCourse().getTeacher().getEmail().equals(teacherEmail)) {
            throw new RuntimeException("You can only update quizzes in your own courses");
        }

        quiz.setQuestion(Quiz.getQuestion());
        quiz.setOptionA(Quiz.getOptionA());
        quiz.setOptionB(Quiz.getOptionB());
        quiz.setOptionC(Quiz.getOptionC());
        quiz.setOptionD(Quiz.getOptionD());
        quiz.setCorrectAnswer(Quiz.getCorrectAnswer());
        quiz.setMarks(Quiz.getMarks());
        
        Quiz updatedQuiz = quizRepository.save(quiz);
        return updatedQuiz;
    }

    @Override
    public void deleteQuiz(Long id, String teacherEmail) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));
        
        if (!quiz.getLesson().getCourse().getTeacher().getEmail().equals(teacherEmail)) {
            throw new RuntimeException("You can only delete quizzes in your own courses");
        }
        
        quizRepository.delete(quiz);
    }

    @Override
    public List<Quiz> getQuizzesByLessonId(Long lessonId) {
        return quizRepository.findByLessonLessonId(lessonId).stream()
                
                .collect(Collectors.toList());
    }

    @Override
    public String submitQuiz(Long id, String answer) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));
                
        if (quiz.getCorrectAnswer().equalsIgnoreCase(answer)) {
            return "Correct Answer!";
        } else {
            return "Incorrect Answer!";
        }
    }
}


