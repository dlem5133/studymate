package com.web.blog.model.evaluate;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Date;

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
    double score1;

    @ApiModelProperty(required = true)
    double score2;

    @ApiModelProperty(required = true)
    double score3;

    @ApiModelProperty(required = true)
    String sentence;

    @ApiModelProperty(required = true)
    Date eva_date;
    
    @ApiModelProperty(required = true)
    int count;
    
}
