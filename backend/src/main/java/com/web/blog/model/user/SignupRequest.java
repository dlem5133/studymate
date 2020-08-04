package com.web.blog.model.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Valid
@Data
public class SignupRequest {

    @ApiModelProperty(required = true)
    @NotNull
    @Email
    String email;

    @ApiModelProperty(required = true)
    @NotNull
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d$@$!%*#?&]{8,}$")
    String password;

    @ApiModelProperty(required = true)

    String nickname;
    @ApiModelProperty(required = true)
    String intro;

    @ApiModelProperty(required = true)
    String profile_image;

    @ApiModelProperty(required = true)
    int uid;

}
