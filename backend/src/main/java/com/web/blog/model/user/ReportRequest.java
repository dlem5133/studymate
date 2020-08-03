package com.web.blog.model.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;

@Valid
@Data
public class ReportRequest {

    @ApiModelProperty(required = true)
    int pid;

    @ApiModelProperty(required = true)
    int target;

    @ApiModelProperty(required = true)
    String reason;}
