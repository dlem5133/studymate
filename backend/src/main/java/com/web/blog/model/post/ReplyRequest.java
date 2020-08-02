package com.web.blog.model.post;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;

@Valid
@Data
public class ReplyRequest {

    @ApiModelProperty(required = true)
    int pid;

    @ApiModelProperty(required = true)
    int rid;

    @ApiModelProperty(required = true)
    String reply_writer;

    @ApiModelProperty(required = true)
    String reply_content;

    @ApiModelProperty(required = true)
    int reply_parent;

    @ApiModelProperty(required = true)
    int uid;
}
