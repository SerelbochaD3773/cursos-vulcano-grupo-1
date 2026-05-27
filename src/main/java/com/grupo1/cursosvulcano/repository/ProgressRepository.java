package com.grupo1.cursosvulcano.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.grupo1.cursosvulcano.model.entity.Progress;

@Repository
public interface ProgressRepository extends JpaRepository<Progress, Long> {

    Optional<Progress> findByUserIdAndModuleId(Long userId, Long moduleId);

    List<Progress> findByUserIdAndModuleCourseId(Long userId, Long courseId);

    @Query("SELECT COUNT(m) FROM Module m WHERE m.course.id = :courseId")
    int countTotalModulesByCourse(@Param("courseId") Long courseId);

    @Query("SELECT COUNT(p) FROM Progress p WHERE p.userId = :userId AND p.module.course.id = :courseId AND p.moduleCompleted = true")
    int countCompletedModules(@Param("userId") Long userId, @Param("courseId") Long courseId);

    @Query("SELECT p FROM Progress p WHERE p.userId = :userId AND p.module.course.id = :courseId AND p.moduleCompleted = true ORDER BY p.module.orderIndex DESC")
    List<Progress> findLastCompletedModule(@Param("userId") Long userId, @Param("courseId") Long courseId);

    List<Progress> findByUserIdAndModuleCourseIdOrderByModuleOrderIndexAsc(Long userId, Long courseId);
}