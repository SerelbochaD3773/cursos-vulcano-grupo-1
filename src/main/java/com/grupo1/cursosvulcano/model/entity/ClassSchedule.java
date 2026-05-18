package com.grupo1.cursosvulcano.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "class_schedules")
public class ClassSchedule extends BaseEntity {

    @Column(name = "student_id", nullable = true)
    private Long studentId;

    @Column(name = "expert_id", nullable = true)
    private Long expertId;

    @Column(name = "course_id", nullable = false)
    private Long courseId;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column
    private LocalDateTime endTime;

    @Column
    private String status;

    @Column
    private String notes;
}