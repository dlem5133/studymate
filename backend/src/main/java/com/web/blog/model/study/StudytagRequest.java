package com.web.blog.model.study;

import lombok.Data;

import java.util.List;

import javax.validation.Valid;

@Valid
@Data
public class StudytagRequest {
    
    Study study;
    
    List<Studytag> tag;
}