package com.example.demo.service.serviceimple;

import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.repository.StudentRepository;
import com.example.demo.repository.TeacherRepository;
 
 
import com.example.demo.models.Student;
import com.example.demo.service.StudentService;
@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    private final TeacherRepository teacherRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    public StudentServiceImpl(StudentRepository studentRepository, TeacherRepository teacherRepository, BCryptPasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Student registerStudent(Student student) {

        if (studentRepository.existsByEmail(student.getEmail()) || teacherRepository.existsByEmail(student.getEmail())) {
            throw new RuntimeException("Email already exists.");
        }

        // Encrypt password before saving
        student.setPassword(passwordEncoder.encode(student.getPassword()));

        // Default role
        student.setRole("STUDENT");

        return studentRepository.save(student);
    }

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public Student getStudentById(Long studentId) {

        return studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found."));
    }

    @Override
    public Student getStudentByEmail(String email) {

        return studentRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Student not found."));
    }

    @Override
    public Student updateStudent(Long studentId, Student student) {

        Student existingStudent = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found."));

        existingStudent.setFullName(student.getFullName());
        existingStudent.setPhone(student.getPhone());
        existingStudent.setBatch(student.getBatch());
        existingStudent.setProfileImage(student.getProfileImage());

        // Update password only if a new one is provided
        if (student.getPassword() != null && !student.getPassword().isEmpty()) {
            existingStudent.setPassword(passwordEncoder.encode(student.getPassword()));
        }

        existingStudent.setRole("STUDENT");

        return studentRepository.save(existingStudent);
    }

    @Override
    public void deleteStudent(Long studentId) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found."));

        studentRepository.delete(student);
    }
}


