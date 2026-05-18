package com.grupo1.cursosvulcano.controller;

import com.grupo1.cursosvulcano.dto.request.ClassScheduleRequest;
import com.grupo1.cursosvulcano.dto.response.AvailableScheduleGroupResponse;
import com.grupo1.cursosvulcano.dto.response.ClassScheduleResponse;
import com.grupo1.cursosvulcano.service.ClassScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedules")
@CrossOrigin("*") // IMPORTANTE: Permite que el frontend React se conecte
@RequiredArgsConstructor
public class ClassScheduleController {

    private final ClassScheduleService classScheduleService;

    // --- NUEVO: Obtener TODAS las clases (soluciona el 404 al cargar ClassManagement)
    @GetMapping
    public ResponseEntity<List<ClassScheduleResponse>> getAllSchedules() {
        return ResponseEntity.ok(classScheduleService.getAllSchedules());
    }

    // --- NUEVO: Crear disponibilidad (soluciona el error al guardar la nueva clase)
    @PostMapping
    public ResponseEntity<ClassScheduleResponse> createAvailability(@RequestBody ClassScheduleRequest request) {
        return ResponseEntity.ok(classScheduleService.createAvailability(request));
    }

    // --- NUEVO: Cambiar el estado (publicar/despublicar) de un horario sin validar studentId
    @PatchMapping("/{scheduleId}/status")
    public ResponseEntity<ClassScheduleResponse> updateStatus(
            @PathVariable Long scheduleId,
            @RequestParam String status) {
        return ResponseEntity.ok(classScheduleService.updateStatus(scheduleId, status));
    }

    // --- EXISTENTES ---

    // Obtener horarios disponibles de un profesor
    @GetMapping("/available/{expertId}")
    public ResponseEntity<List<AvailableScheduleGroupResponse>> getAvailableSchedules(@PathVariable Long expertId) {
        return ResponseEntity.ok(classScheduleService.getAvailableSchedules(expertId));
    }
    // Obtener horarios agendados de un estudiante
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<ClassScheduleResponse>> getStudentSchedules(@PathVariable Long studentId) {
        return ResponseEntity.ok(classScheduleService.getStudentSchedules(studentId));
    }

    @GetMapping("/expert/{expertId}")
    public ResponseEntity<List<ClassScheduleResponse>> getExpertSchedules(@PathVariable Long expertId) {
        return ResponseEntity.ok(classScheduleService.getExpertSchedules(expertId));
    }

    // El alumno agenda una clase
    @PostMapping("/student/{studentId}")
    public ResponseEntity<ClassScheduleResponse> scheduleClass(
            @PathVariable Long studentId,
            @RequestBody ClassScheduleRequest request) {
        return ResponseEntity.ok(classScheduleService.scheduleClass(studentId, request));
    }

    // Modificar una clase agendada
    @PutMapping("/{scheduleId}/student/{studentId}")
    public ResponseEntity<ClassScheduleResponse> modifySchedule(
            @PathVariable Long scheduleId,
            @PathVariable Long studentId,
            @RequestBody ClassScheduleRequest request) {
        return ResponseEntity.ok(classScheduleService.modifySchedule(scheduleId, studentId, request));
    }

    // Cancelar/Eliminar una clase agendada (requiere studentId - para alumnos)
    @DeleteMapping("/{scheduleId}/student/{studentId}")
    public ResponseEntity<Void> cancelSchedule(
            @PathVariable Long scheduleId,
            @PathVariable Long studentId) {
        classScheduleService.cancelSchedule(scheduleId, studentId);
        return ResponseEntity.noContent().build();
    }

    // Eliminar disponibilidad (admin, sin validar studentId)
    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<Void> deleteAvailability(@PathVariable Long scheduleId) {
        classScheduleService.deleteAvailability(scheduleId);
        return ResponseEntity.noContent().build();
    }
}
