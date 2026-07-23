package com.example.demo.service.serviceimple;

 


import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.models.StudyMaterial;
import com.example.demo.repository.StudyMaterialRepository;
import com.example.demo.service.StudyMaterialService;
@Service
public class StudyMaterialServiceImpl implements StudyMaterialService {

    private final StudyMaterialRepository repository;

    public StudyMaterialServiceImpl(StudyMaterialRepository repository) {
        this.repository = repository;
    }

    @Override
    public StudyMaterial addMaterial(StudyMaterial material) {
        return repository.save(material);
    }

    @Override
    public List<StudyMaterial> getAllMaterials() {
        return repository.findAll();
    }

    @Override
    public StudyMaterial getMaterialById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Study Material not found"));
    }

    @Override
    public List<StudyMaterial> getMaterialsByCourse(Long courseId) {
        return repository.findByCourseCourseId(courseId);
    }

    @Override
    public List<StudyMaterial> getMaterialsByLesson(Long lessonId) {
        return repository.findByLessonLessonId(lessonId);
    }

    @Override
    public StudyMaterial updateMaterial(Long id, StudyMaterial material) {

        StudyMaterial existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Study Material not found"));

        existing.setTitle(material.getTitle());
        existing.setDescription(material.getDescription());
        existing.setMaterialType(material.getMaterialType());
        existing.setFileUrl(material.getFileUrl());

        return repository.save(existing);
    }

    @Override
    public void deleteMaterial(Long id) {

        StudyMaterial material = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Study Material not found"));

        repository.delete(material);
    }
}


