package com.grupo1.cursosvulcano.controller;

import java.util.List;
import java.util.Map;
import com.grupo1.cursosvulcano.dto.request.ModuleRequest;
import com.grupo1.cursosvulcano.dto.response.ModuleResponse;
import com.grupo1.cursosvulcano.service.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;

@RestController
@CrossOrigin
@RequestMapping("/api/modules")
public class ModuleController {
    @Autowired
    private ModuleService moduleService;

    @GetMapping
    public List<ModuleResponse> getAllModules() {
        return moduleService.getAllModules();   
    }

    @GetMapping("/{id}")
    public ModuleResponse getModuleById(@PathVariable Long id) {
        return moduleService.getModuleById(id);
    }

    @GetMapping("/course/{courseId}")
    public List<ModuleResponse> getModulesByCourseId(@PathVariable Long courseId) {
        return moduleService.getModulesByCourseId(courseId);
    }

    @PostMapping("/course/{courseId}")
    public ResponseEntity<?> createModule(
            @PathVariable Long courseId,
            @RequestBody ModuleRequest request,
            @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        try {
            ModuleResponse created = moduleService.createModule(request, courseId, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (SecurityException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", ex.getMessage()));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateModule(
            @PathVariable Long id,
            @RequestBody ModuleRequest request,
            @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        try {
            ModuleResponse updated = moduleService.updateModule(id, request, userId);
            return ResponseEntity.ok(updated);
        } catch (SecurityException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", ex.getMessage()));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteModule(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        try {
            moduleService.deleteModule(id, userId);
            return ResponseEntity.noContent().build();
        } catch (SecurityException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", ex.getMessage()));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
        }
    }

}

