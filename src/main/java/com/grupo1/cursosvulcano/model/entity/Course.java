package com.grupo1.cursosvulcano.model.entity;

import com.grupo1.cursosvulcano.model.enums.Status;

import com.grupo1.cursosvulcano.model.enums.CourseLevel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@Table (name = "courses")
@NoArgsConstructor
@AllArgsConstructor

public class Course extends BaseEntity {

    @Column (nullable = false, length = 50)
    private String name;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Enumerated (EnumType.STRING)
    @Column (length = 20)
    private CourseLevel courseLevel;

    @Column(name = "is_published")
    private Boolean isPublished = false;

    @Enumerated (EnumType.ORDINAL)
    private Status status;

}

