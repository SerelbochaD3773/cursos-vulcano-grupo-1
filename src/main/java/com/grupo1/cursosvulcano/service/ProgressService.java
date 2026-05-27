package com.grupo1.cursosvulcano.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.grupo1.cursosvulcano.dto.response.CourseProgressResponse;
import com.grupo1.cursosvulcano.dto.response.ProgressResponse;
import com.grupo1.cursosvulcano.model.entity.Course;
import com.grupo1.cursosvulcano.model.entity.Module;
import com.grupo1.cursosvulcano.model.entity.Progress;
import com.grupo1.cursosvulcano.repository.CourseRepository;
import com.grupo1.cursosvulcano.repository.ModuleRepository;
import com.grupo1.cursosvulcano.repository.ProgressRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProgressService {

    private final ProgressRepository progressRepository;
    private final ModuleRepository moduleRepository;
    private final CourseRepository courseRepository;

    @Transactional
    public Progress completeGame(Long userId, Long moduleId) {
        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new RuntimeException("Módulo no encontrado"));

        Progress progress = progressRepository.findByUserIdAndModuleId(userId, moduleId)
                .orElse(new Progress());

        progress.setUserId(userId);
        progress.setModule(module);
        progress.setGameCompleted(true);
        progress.setGameCompletedAt(LocalDateTime.now());

        return progressRepository.save(progress);
    }

    @Transactional
    public Progress completeModule(Long userId, Long moduleId) {
        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new RuntimeException("Módulo no encontrado"));

        Progress progress = progressRepository.findByUserIdAndModuleId(userId, moduleId)
                .orElse(new Progress());

        progress.setUserId(userId);
        progress.setModule(module);
        progress.setModuleCompleted(true);
        progress.setModuleCompletedAt(LocalDateTime.now());

        if (progress.getGameCompleted() == null) {
            progress.setGameCompleted(true);
            progress.setGameCompletedAt(LocalDateTime.now());
        }

        return progressRepository.save(progress);
    }

    public CourseProgressResponse getCourseProgress(Long userId, Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

        List<Module> allModules = moduleRepository.findByCourseIdOrderByOrderIndexAsc(courseId);
        List<Progress> userProgress = progressRepository.findByUserIdAndModuleCourseIdOrderByModuleOrderIndexAsc(userId, courseId);

        List<ProgressResponse> progressResponses = allModules.stream()
                .map(module -> {
                    Progress p = userProgress.stream()
                            .filter(up -> up.getModule().getId().equals(module.getId()))
                            .findFirst()
                            .orElse(null);

                    boolean gameCompleted = p != null && Boolean.TRUE.equals(p.getGameCompleted());
                    boolean moduleCompleted = p != null && Boolean.TRUE.equals(p.getModuleCompleted());

                    ProgressResponse pr = new ProgressResponse();
                    pr.setModuleId(module.getId());
                    pr.setModuleName(module.getContent() != null ? module.getContent().getName() : "Sin título");
                    pr.setOrderIndex(module.getOrderIndex());
                    pr.setGameCompleted(gameCompleted);
                    pr.setModuleCompleted(moduleCompleted);
                    if (p != null) {
                        pr.setGameCompletedAt(p.getGameCompletedAt());
                        pr.setModuleCompletedAt(p.getModuleCompletedAt());
                    }
                    return pr;
                })
                .collect(Collectors.toList());

        int totalModules = allModules.size();
        int completedModules = (int) progressResponses.stream()
                .filter(ProgressResponse::getModuleCompleted)
                .count();
        int percentage = totalModules > 0 ? (completedModules * 100) / totalModules : 0;
        boolean eligible = totalModules > 0 && completedModules == totalModules;

        Module nextModule = getNextUnlockedModule(userId, courseId);

        CourseProgressResponse response = new CourseProgressResponse();
        response.setCourseId(courseId);
        response.setCourseName(course.getName());
        response.setUserId(userId);
        response.setTotalModules(totalModules);
        response.setCompletedModules(completedModules);
        response.setCompletionPercentage(percentage);
        response.setEligibleForCertificate(eligible);
        response.setModulesProgress(progressResponses);

        if (nextModule != null) {
            response.setNextModuleId(nextModule.getId());
            response.setNextModuleName(nextModule.getContent() != null ? nextModule.getContent().getName() : "Sin título");
        }

        return response;
    }

    public Module getNextUnlockedModule(Long userId, Long courseId) {
        List<Progress> userProgress = progressRepository.findByUserIdAndModuleCourseIdOrderByModuleOrderIndexAsc(userId, courseId);

        List<Module> allModules = moduleRepository.findByCourseIdOrderByOrderIndexAsc(courseId);

        for (Module module : allModules) {
            boolean isCompleted = userProgress.stream()
                    .anyMatch(p -> p.getModule().getId().equals(module.getId()) && Boolean.TRUE.equals(p.getModuleCompleted()));

            if (!isCompleted) {
                return module;
            }
        }
        return null;
    }

    public Progress getProgressByUserAndModule(Long userId, Long moduleId) {
        return progressRepository.findByUserIdAndModuleId(userId, moduleId)
                .orElse(null);
    }
}