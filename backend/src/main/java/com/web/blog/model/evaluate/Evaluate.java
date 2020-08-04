
package com.web.blog.model.evaluate;

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
public class Evaluate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int eid;

    @ManyToOne(optional = false)
    @JoinColumn(name = "pid")
    private Study Study;

    @ManyToOne(optional = false)
    @JoinColumn(name = "writer_uid")
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "target_uid")
    private User user2;

    @Column(insertable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int pid;

    @Column(insertable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int writer_uid;

    @Column(insertable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int target_uid;

    private double score;

    private String sentence;

    @Column(insertable = false, updatable = false)
    private LocalDateTime eva_date;

}
