package com.grupo1.cursosvulcano.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProgressResponse {

    private Long moduleId;
    private String moduleName;
    private Integer orderIndex;
    private Boolean gameCompleted;
    private Boolean moduleCompleted;
    private LocalDateTime gameCompletedAt;
    private LocalDateTime moduleCompletedAt;
}