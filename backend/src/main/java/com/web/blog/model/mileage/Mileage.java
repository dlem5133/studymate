package com.web.blog.model.mileage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.web.blog.model.user.User;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Mileage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer mid;


    @Column(insertable = false, updatable = false)
    private Integer uid;

    @ManyToOne(optional = false)
    @JoinColumn(name = "uid")
    private User user;
    private Integer diarypoint;
    private Integer evalpoint;
    private Integer endpoint;
    private Integer total;
}
