package com.web.blog.dao.study;

import java.util.List;

import com.web.blog.model.study.Image;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageDao extends JpaRepository<Image, String> {

}