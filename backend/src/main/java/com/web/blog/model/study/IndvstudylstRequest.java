package com.web.blog.model.study;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;

@Valid
@Data
public class IndvstudylstRequest {

    @ApiModelProperty(required = true)
    int pid;

    @ApiModelProperty(required = true)
    int uid;

    @ApiModelProperty(required = true)
    int isJoin;

    @ApiModelProperty(required = true)
    int isLeader;

}
