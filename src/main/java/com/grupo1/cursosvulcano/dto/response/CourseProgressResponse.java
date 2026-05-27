package com.grupo1.cursosvulcano.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseProgressResponse {

    private Long courseId;
    private String courseName;
    private Long userId;
    private Integer totalModules;
    private Integer completedModules;
    private Integer completionPercentage;
    private Boolean eligibleForCertificate;
    private List<ProgressResponse> modulesProgress;
    private Long nextModuleId;
    private String nextModuleName;
}