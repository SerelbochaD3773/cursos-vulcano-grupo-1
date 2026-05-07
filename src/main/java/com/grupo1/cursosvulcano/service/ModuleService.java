package com.grupo1.cursosvulcano.service;

import com.grupo1.cursosvulcano.dto.response.ModuleResponse;
import com.grupo1.cursosvulcano.model.entity.Course;
import com.grupo1.cursosvulcano.model.entity.Module;
import com.grupo1.cursosvulcano.repository.CourseRepository;
import com.grupo1.cursosvulcano.repository.ModuleRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModuleService {
    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private CourseRepository courseRepository;

    public List<Module> getAllModules() {
        return moduleRepository.findAll();
      
    public List<Module> getModulesByCourseId(Long courseId) {
        return moduleRepository.findByCourseIdOrderByOrderIndexAsc(courseId);
    }


    public Module getModuleById(Long id) {
        return moduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Módulo no encontrado"));
    }

    public Module createModule(Module module, Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

        module.setCourse(course);
        return moduleRepository.save(module);
    }

    public Module updateModule(Long id, Module module) {
        module.setId(id);
        return moduleRepository.save(module);
    }

    public void deleteModule(Long id) {
        moduleRepository.deleteById(id);
    }

    private ModuleResponse mapToResponse(Module module) {
        ModuleResponse response = new ModuleResponse();
        response.setId(module.getId());
        response.setCreatedAt(module.getCreatedAt());
        response.setUpdatedAt(module.getUpdatedAt());
        response.setCourseId(module.getCourse().getId());
        response.setName(module.getContent().getName());
        response.setDescription(module.getContent().getDescription());
        response.setVideoUrl(module.getVideoUrl());
        response.setDurationInMinutes(module.getDurationInMinutes());
        response.setMarkdownUrl(module.getMarkdownUrl());
        response.setInteractiveGameUrl(module.getInteractiveGameUrl());
        response.setOrderIndex(module.getOrderIndex());
        response.setStatus(module.getStatus());
        return response;
    }

}
