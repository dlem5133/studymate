
package com.web.blog.model.diary;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Expectdo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer eid;

    private Integer pid;

    private Integer uid;

    private String doplace;

    private String assignment;

    private String assignment_file;

    private LocalDateTime dodate;

}
