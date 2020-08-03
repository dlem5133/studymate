package com.web.blog.model.study;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StudyResponse {
    
    Study study;
    String sido;
    String gugun;
}