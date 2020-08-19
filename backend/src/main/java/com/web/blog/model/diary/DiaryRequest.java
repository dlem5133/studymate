package com.web.blog.model.diary;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Date;

import javax.validation.Valid;

@Valid
@Data
public class DiaryRequest {

    @ApiModelProperty(required = true)
    private Integer did;

    @ApiModelProperty(required = true)
    private Integer pid;

    @ApiModelProperty(required = true)
    private Integer uid;

    @ApiModelProperty(required = true)
    private String title;

    @ApiModelProperty(required = true)
    private String body;

    @ApiModelProperty(required = true)
    private String writer;

    @ApiModelProperty(required = true)
    private String posttime;

    @ApiModelProperty(required = true)
    private Integer tmp;
}
