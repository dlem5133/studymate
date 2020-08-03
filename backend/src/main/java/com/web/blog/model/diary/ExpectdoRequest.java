package com.web.blog.model.diary;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

import javax.validation.Valid;

@Valid
@Data
public class ExpectdoRequest {

    @ApiModelProperty(required = true)
    private Integer eid;

    @ApiModelProperty(required = true)
    private Integer pid;

    @ApiModelProperty(required = true)
    private Integer uid;

    @ApiModelProperty(required = true)
    private String doplace;

    @ApiModelProperty(required = true)
    private String assignment;

    @ApiModelProperty(required = true)
    private String assignment_file;

    @ApiModelProperty(required = true)
    private LocalDateTime dodate;

}
