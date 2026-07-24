package com.example.demo.service.serviceimple;

 

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.TeacherRepository;
import com.example.demo.models.Course;

import com.example.demo.models.Teacher;
import com.example.demo.service.CourseService;
@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    
    private final TeacherRepository teacherRepository;

    public CourseServiceImpl(CourseRepository courseRepository, TeacherRepository teacherRepository) {
        this.courseRepository = courseRepository;
        this.teacherRepository = teacherRepository;
    }

    @Override
    public Course createCourse(Course Course, String teacherEmail) {
        Teacher teacher = teacherRepository.findByEmail(teacherEmail)
                .orElseThrow(() -> new RuntimeException("Teacher not found with email: " + teacherEmail));
        
        if (courseRepository.existsByCourseCode(Course.getCourseCode())) {
            throw new RuntimeException("Course Code already exists");
        }

        Course course = new Course();
        course.setCourseName(Course.getCourseName());
        course.setCourseCode(Course.getCourseCode());
        course.setDescription(Course.getDescription());
        course.setThumbnail(Course.getThumbnail());
        course.setDuration(Course.getDuration());
        course.setTeacher(teacher);
        
        Course savedCourse = courseRepository.save(course);
        return savedCourse;
    }

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAll().stream()
                
                .collect(Collectors.toList());
    }

    @Override
    public Course getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));
        return course;
    }

    @Override
    public Course updateCourse(Long id, Course Course, String teacherEmail) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));
        
        if (!course.getTeacher().getEmail().equals(teacherEmail)) {
            throw new RuntimeException("You can only update your own courses.");
        }

        course.setCourseName(Course.getCourseName());
        if (!course.getCourseCode().equals(Course.getCourseCode())) {
            if (courseRepository.existsByCourseCode(Course.getCourseCode())) {
                throw new RuntimeException("Course Code already exists");
            }
            course.setCourseCode(Course.getCourseCode());
        }
        course.setDescription(Course.getDescription());
        course.setThumbnail(Course.getThumbnail());
        course.setDuration(Course.getDuration());
        
        Course updatedCourse = courseRepository.save(course);
        return updatedCourse;
    }

    @Override
    public void deleteCourse(Long id, String teacherEmail) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));
        
        if (!course.getTeacher().getEmail().equals(teacherEmail)) {
            throw new RuntimeException("You can only delete your own courses.");
        }
        
        courseRepository.delete(course);
    }

    @Override
    public List<Course> getCoursesByTeacherEmail(String teacherEmail) {
        return courseRepository.findByTeacherEmail(teacherEmail).stream()
                
                .collect(Collectors.toList());
    }


}


