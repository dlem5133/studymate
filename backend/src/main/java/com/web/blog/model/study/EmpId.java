package com.web.blog.model.study;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.web.blog.model.user.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Data
public class EmpId implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @ManyToOne(optional = false)
    @JoinColumn(name = "uid")
    private User User;

    @ManyToOne(optional = false)
    @JoinColumn(name = "pid")
    private Study Study;
}