package com.grupo1.cursosvulcano.service;

import com.grupo1.cursosvulcano.dto.request.ModuleRequest;
import com.grupo1.cursosvulcano.dto.response.ModuleResponse;
import com.grupo1.cursosvulcano.model.entity.Course;
import com.grupo1.cursosvulcano.model.entity.Module;
import com.grupo1.cursosvulcano.model.entity.User;
import com.grupo1.cursosvulcano.model.enums.UserRole;
import com.grupo1.cursosvulcano.model.embeddable.Content;
import com.grupo1.cursosvulcano.repository.CourseRepository;
import com.grupo1.cursosvulcano.repository.ModuleRepository;
import com.grupo1.cursosvulcano.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModuleService {
    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    public List<ModuleResponse> getAllModules() {
        return moduleRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<ModuleResponse> getModulesByCourseId(Long courseId) {
        return moduleRepository.findByCourseIdOrderByOrderIndexAsc(courseId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ModuleResponse getModuleById(Long id) {
        Module module = moduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Módulo no encontrado"));
        return mapToResponse(module);
    }

    private void validateAdminRole(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("El ID del usuario es obligatorio para esta acción.");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));
        
        if (user.getRole() != UserRole.ADMIN) {
            throw new SecurityException("¡Acceso Denegado! Solo los administradores pueden gestionar módulos.");
        }
    }

    public ModuleResponse createModule(ModuleRequest request, Long courseId, Long userId) {
        validateAdminRole(userId);
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

        Module module = new Module();
        module.setCourse(course);
        
        Content content = new Content();
        content.setName(request.getName());
        content.setDescription(request.getDescription());
        module.setContent(content);
        
        module.setVideoUrl(request.getVideoUrl());
        module.setDurationInMinutes(request.getDurationInMinutes());
        module.setMarkdownUrl(request.getMarkdownUrl());
        module.setInteractiveGameUrl(request.getInteractiveGameUrl());
        module.setOrderIndex(request.getOrderIndex());
        module.setStatus(request.getStatus());

        Module saved = moduleRepository.save(module);
        return mapToResponse(saved);
    }

    public ModuleResponse updateModule(Long id, ModuleRequest request, Long userId) {
        validateAdminRole(userId);
        Module module = moduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Módulo no encontrado"));
        
        if (module.getContent() == null) {
            module.setContent(new Content());
        }
        module.getContent().setName(request.getName());
        module.getContent().setDescription(request.getDescription());
        
        module.setVideoUrl(request.getVideoUrl());
        module.setDurationInMinutes(request.getDurationInMinutes());
        module.setMarkdownUrl(request.getMarkdownUrl());
        module.setInteractiveGameUrl(request.getInteractiveGameUrl());
        module.setOrderIndex(request.getOrderIndex());
        module.setStatus(request.getStatus());

        Module saved = moduleRepository.save(module);
        return mapToResponse(saved);
    }

    public void deleteModule(Long id, Long userId) {
        validateAdminRole(userId);
        moduleRepository.deleteById(id);
    }

    private ModuleResponse mapToResponse(Module module) {
        ModuleResponse response = new ModuleResponse();
        response.setId(module.getId());
        response.setCreatedAt(module.getCreatedAt());
        response.setUpdatedAt(module.getUpdatedAt());
        response.setCourseId(module.getCourse().getId());
        response.setName(module.getContent() != null ? module.getContent().getName() : null);
        response.setDescription(module.getContent() != null ? module.getContent().getDescription() : null);
        response.setVideoUrl(module.getVideoUrl());
        response.setDurationInMinutes(module.getDurationInMinutes());
        response.setMarkdownUrl(module.getMarkdownUrl());
        response.setInteractiveGameUrl(module.getInteractiveGameUrl());
        response.setOrderIndex(module.getOrderIndex());
        response.setStatus(module.getStatus());
        return response;
    }

}

