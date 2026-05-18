package com.grupo1.cursosvulcano.service;

import com.grupo1.cursosvulcano.dto.request.ClassScheduleRequest;
import com.grupo1.cursosvulcano.dto.response.AvailableScheduleGroupResponse;
import com.grupo1.cursosvulcano.dto.response.ClassScheduleResponse;
import com.grupo1.cursosvulcano.model.entity.ClassSchedule;
import com.grupo1.cursosvulcano.repository.ClassScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClassScheduleService {

    private final ClassScheduleRepository classScheduleRepository;

    // Obtener todos los horarios (para la tabla de Gestión de Clases)
    public List<ClassScheduleResponse> getAllSchedules() {
        List<ClassSchedule> schedules = classScheduleRepository.findAll();
        return schedules.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    // Crear disponibilidad (para el Profesor/Admin)
    public ClassScheduleResponse createAvailability(ClassScheduleRequest request) {
        ClassSchedule schedule = new ClassSchedule();

        schedule.setStudentId(null);
        schedule.setExpertId(request.getExpertId());
        schedule.setCourseId(request.getCourseId());
        schedule.setStartTime(request.getStartTime());
        schedule.setNotes(request.getNotes() != null ? request.getNotes() : "Disponible");
        schedule.setStatus("AVAILABLE");

        ClassSchedule saved = classScheduleRepository.save(schedule);
        return mapToResponse(saved);
    }

    // Obtener horarios disponibles filtrando por experto y estado AVAILABLE
    public List<AvailableScheduleGroupResponse> getAvailableSchedules(Long expertId) {
        List<ClassSchedule> availableSchedules = classScheduleRepository.findByExpertIdAndStatus(expertId, "AVAILABLE");

        Map<String, List<String>> grouped = new HashMap<>();
        for (ClassSchedule schedule : availableSchedules) {
            LocalDateTime dateTime = schedule.getStartTime();
            if (dateTime == null)
                continue;

            String date = dateTime.toLocalDate().toString();
            // Formato 24h (HH:mm) para que el frontend pueda agendar correctamente
            String time = String.format("%02d:%02d", dateTime.getHour(), dateTime.getMinute());

            grouped.computeIfAbsent(date, k -> new ArrayList<>()).add(time);
        }

        return grouped.entrySet().stream()
                .map(entry -> {
                    AvailableScheduleGroupResponse response = new AvailableScheduleGroupResponse();
                    response.setDate(entry.getKey());
                    response.setTimes(entry.getValue());
                    return response;
                })
                .collect(Collectors.toList());
    }

    public List<ClassScheduleResponse> getStudentSchedules(Long studentId) {
        List<ClassSchedule> schedules = classScheduleRepository.findByStudentId(studentId);
        return schedules.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public List<ClassScheduleResponse> getExpertSchedules(Long expertId) {
        List<ClassSchedule> schedules = classScheduleRepository.findByExpertId(expertId);
        return schedules.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public ClassScheduleResponse scheduleClass(ClassScheduleRequest request) {
        ClassSchedule schedule = new ClassSchedule();
        schedule.setStudentId(request.getStudentId());
        schedule.setExpertId(request.getExpertId());
        schedule.setCourseId(request.getCourseId());
        schedule.setStartTime(request.getStartTime());
        schedule.setEndTime(request.getEndTime());
        schedule.setNotes(request.getNotes());
        schedule.setStatus(request.getStudentId() != null ? "SCHEDULED" : "AVAILABLE");
        ClassSchedule saved = classScheduleRepository.save(schedule);
        return mapToResponse(saved);
    }

    public ClassScheduleResponse scheduleClass(Long studentId, ClassScheduleRequest request) {
        request.setStudentId(studentId);
        return scheduleClass(request);
    }

    public ClassScheduleResponse updateStatus(Long scheduleId, String status) {
        ClassSchedule schedule = classScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));
        schedule.setStatus(status);
        ClassSchedule saved = classScheduleRepository.save(schedule);
        return mapToResponse(saved);
    }

    public ClassScheduleResponse modifySchedule(Long scheduleId, Long studentId, ClassScheduleRequest request) {
        ClassSchedule schedule = classScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));
        if (schedule.getStudentId() != null && !schedule.getStudentId().equals(studentId)) {
            throw new RuntimeException("Unauthorized");
        }
        schedule.setStartTime(request.getStartTime());
        schedule.setEndTime(request.getEndTime());
        schedule.setNotes(request.getNotes());
        schedule.setExpertId(request.getExpertId());
        schedule.setCourseId(request.getCourseId());
        ClassSchedule saved = classScheduleRepository.save(schedule);
        return mapToResponse(saved);
    }

    public void deleteAvailability(Long scheduleId) {
        ClassSchedule schedule = classScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));
        classScheduleRepository.delete(schedule);
    }

    public void cancelSchedule(Long scheduleId, Long studentId) {
        ClassSchedule schedule = classScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));
        if (!schedule.getStudentId().equals(studentId)) {
            throw new RuntimeException("Unauthorized");
        }
        classScheduleRepository.delete(schedule);
    }

    private ClassScheduleResponse mapToResponse(ClassSchedule schedule) {
        ClassScheduleResponse response = new ClassScheduleResponse();
        response.setId(schedule.getId());
        response.setStudentId(schedule.getStudentId());
        response.setExpertId(schedule.getExpertId());
        response.setCourseId(schedule.getCourseId());
        response.setStartTime(schedule.getStartTime());
        response.setEndTime(schedule.getEndTime());
        response.setStatus(schedule.getStatus());
        response.setNotes(schedule.getNotes());
        return response;
    }
}
