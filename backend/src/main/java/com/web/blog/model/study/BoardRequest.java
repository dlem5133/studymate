package com.web.blog.model.study;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

import javax.validation.Valid;

@Valid
@Data
public class BoardRequest {
    @ApiModelProperty(required = true)
    int bid;

    @ApiModelProperty(required = true)
    int pid;

    @ApiModelProperty(required = true)
    int uid;

    @ApiModelProperty(required = true)
    String title;

    @ApiModelProperty(required = true)
    LocalDateTime date;

    @ApiModelProperty(required = true)
    String body;

    int isnotice;

}
