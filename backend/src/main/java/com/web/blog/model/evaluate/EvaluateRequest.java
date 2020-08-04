package com.web.blog.model.evaluate;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;

@Valid
@Data
public class EvaluateRequest {

    @ApiModelProperty(required = true)
    int eid;

    @ApiModelProperty(required = true)
    int pid;
    
    @ApiModelProperty(required = true)
    int writer_uid;

    @ApiModelProperty(required = true)
    int target_uid;

    @ApiModelProperty(required = true)
    double score;

    @ApiModelProperty(required = true)
    String sentence;

    
}
