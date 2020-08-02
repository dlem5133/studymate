
package com.web.blog.model.study;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Indvstudylst {

    @Id
    @EmbeddedId
    private EmpId empId;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer isleader;

    private Integer isjoin;

    @Column(insertable = false, updatable = false)
    private int uid;

    @Column(insertable = false, updatable = false)
    private int pid;

    @Column(name = "join_date")
    private LocalDateTime join_date;

    // indv 어떤 스터디에 누가 들어가있는지, 지원은 되었는지,
}