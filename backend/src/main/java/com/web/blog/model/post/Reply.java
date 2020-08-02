
package com.web.blog.model.post;

import lombok.AllArgsConstructor;
import lombok.*;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.web.blog.model.study.Study;
import com.web.blog.model.user.User;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int rid;

    @ManyToOne(optional = false)
    @JoinColumn(name = "pid")
    private Study Study;

    @ManyToOne(optional = false)
    @JoinColumn(name = "uid")
    private User user;

    @Column(insertable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int pid;

    @Column(insertable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int uid;

    private String reply_writer;
    
    private String reply_content;
    
    private int reply_parent;

    @Column(insertable = false, updatable = false)
    private LocalDateTime reply_time;

}
