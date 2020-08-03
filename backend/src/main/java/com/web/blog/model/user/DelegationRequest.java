
package com.web.blog.model.user;

import lombok.Data;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModelProperty;

@Valid
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DelegationRequest {
    
    @ApiModelProperty(required = true)
    @NotNull
    private Integer leader;
    @ApiModelProperty(required = true)
    @NotNull
    private Integer member;
    @ApiModelProperty(required = true)
    @NotNull
    private Integer pid;

}
