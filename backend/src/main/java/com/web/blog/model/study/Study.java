
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

import com.fasterxml.jackson.annotation.JsonInclude;

import java.sql.Date;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Study {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JoinColumn(name = "pid")
    private Integer pid;

    private Integer likep;

    private String title;
    private String category;    
    private String days;    
    private Integer uid;

    private String data;

    @Column(name = "background_image")
    private String background_image;

    @Column(insertable = false, updatable = false)
    private LocalDateTime posttime;

    private Integer tmp;

  
    private Integer limitp;

    private Integer bindo;

    @Column(name = "sido_code")
    private Integer sidocode;

    @Column(name = "sigungu_code")
    private Integer sigungucode;

    @Column(name = "start_date")
    private Date start_date;

    @Column(name = "end_date")
    private Date end_date;

}
