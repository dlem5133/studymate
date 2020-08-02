
package com.web.blog.model.study;

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

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor

@AllArgsConstructor

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JoinColumn(name = "bid")
    private Integer bid;

    @ManyToOne(optional = false)
    @JoinColumn(name = "uid")
    private User User;
    
    @Column(insertable = false, updatable = false)
    private int uid;

    @ManyToOne(optional = false)
    @JoinColumn(name = "pid")
    private Study Study;

    @Column(insertable = false, updatable = false)
    private int pid;

    private String title;
    
    private String body;
    
    @Column(insertable = false, updatable = false)
    private LocalDateTime date;
    
    private Integer isnotice;

}
