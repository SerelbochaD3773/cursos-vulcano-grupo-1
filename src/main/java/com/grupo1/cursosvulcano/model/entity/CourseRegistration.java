package com.grupo1.cursosvulcano.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "course_registrations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseRegistration extends BaseEntity {


    @ManyToOne
    private UserProfile userProfile;

    @ManyToOne
    private Course course;


    
}
