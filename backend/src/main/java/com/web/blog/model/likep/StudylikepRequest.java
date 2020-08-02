package com.web.blog.model.likep;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;

@Valid
@Data
public class StudylikepRequest {
    @ApiModelProperty(required = true)
    int no;

    @ApiModelProperty(required = true)
    int pid;

    @ApiModelProperty(required = true)
    int uid;

}
