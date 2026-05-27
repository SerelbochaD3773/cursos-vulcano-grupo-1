package com.grupo1.cursosvulcano.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.grupo1.cursosvulcano.dto.response.CourseProgressResponse;
import com.grupo1.cursosvulcano.model.entity.Module;
import com.grupo1.cursosvulcano.model.entity.Progress;
import com.grupo1.cursosvulcano.service.ProgressService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/progress")
@RequiredArgsConstructor
public class ProgressController {

    private final ProgressService progressService;

    @PostMapping("/game-complete/{moduleId}")
    public ResponseEntity<Progress> completeGame(
            @PathVariable Long moduleId,
            @RequestParam Long userId) {
        Progress progress = progressService.completeGame(userId, moduleId);
        return ResponseEntity.ok(progress);
    }

    @PostMapping("/module-complete/{moduleId}")
    public ResponseEntity<Progress> completeModule(
            @PathVariable Long moduleId,
            @RequestParam Long userId) {
        Progress progress = progressService.completeModule(userId, moduleId);
        return ResponseEntity.ok(progress);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<CourseProgressResponse> getCourseProgress(
            @PathVariable Long courseId,
            @RequestParam Long userId) {
        CourseProgressResponse progress = progressService.getCourseProgress(userId, courseId);
        return ResponseEntity.ok(progress);
    }

    @GetMapping("/next-module/{courseId}")
    public ResponseEntity<Module> getNextModule(
            @PathVariable Long courseId,
            @RequestParam Long userId) {
        Module nextModule = progressService.getNextUnlockedModule(userId, courseId);
        if (nextModule == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(nextModule);
    }

    @GetMapping("/module/{moduleId}")
    public ResponseEntity<Progress> getModuleProgress(
            @PathVariable Long moduleId,
            @RequestParam Long userId) {
        Progress progress = progressService.getProgressByUserAndModule(userId, moduleId);
        if (progress == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(progress);
    }
}