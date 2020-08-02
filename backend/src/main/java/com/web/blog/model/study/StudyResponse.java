package com.web.blog.model.study;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StudyResponse {
    
    Study study;
    String sido;
    String gugun;
}