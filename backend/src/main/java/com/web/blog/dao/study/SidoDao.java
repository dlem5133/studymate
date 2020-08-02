package com.web.blog.dao.study;

import java.util.List;

import com.web.blog.model.study.Sido;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SidoDao extends JpaRepository<Sido, String> {

	List<Sido> findBySidonameLike(String string);

}