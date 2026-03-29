package com.grupo1.cursosvulcano.model.entity;

import com.grupo1.cursosvulcano.model.enums.Experience;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "reviews")

public class Review extends BaseEntity {

    @Column(columnDefinition = "TEXT", nullable = false)
    private String comment;

    private int rating; // Calificación del 1 al 5

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Enumerated (EnumType.STRING)
    @Column (length = 20)
    private Experience experience;
    
    //@ManyToOne
    //@JoinColumn(name = "user_id", nullable = false)
    //private User user;

}
