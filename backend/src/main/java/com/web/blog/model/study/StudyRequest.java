package com.web.blog.model.study;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Date;
import java.util.ArrayList;

import javax.validation.Valid;

@Valid
@Data
public class StudyRequest {

    @ApiModelProperty(required = true)
    int pid;

    @ApiModelProperty(required = true)
    String title;

    @ApiModelProperty(required = true)
    int uid;

    @ApiModelProperty(required = true)
    String category;
    @ApiModelProperty(required = true)
    String days;

    @ApiModelProperty(required = true)
    String data;

    @ApiModelProperty(required = true)
    String background_image;

    @ApiModelProperty(required = true)
    int tmp;

    @ApiModelProperty(required = true)
    private ArrayList<String> tag;

    @ApiModelProperty(required = true)
    private Integer limitp;

    @ApiModelProperty(required = true)
    private Integer bindo;

    @ApiModelProperty(required = true)
    private Integer sido_code;

    @ApiModelProperty(required = true)
    private Integer sigungu_code;

    @ApiModelProperty(required = true)
    private Date start_date;

    @ApiModelProperty(required = true)
    private Date end_date;

    @ApiModelProperty(required = true)
    private Integer likep;
}
