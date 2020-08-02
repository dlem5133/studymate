package com.web.blog.dao.diary;

import java.util.List;

import com.web.blog.model.diary.Expectdo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpectdoDao extends JpaRepository<Expectdo, String> {

    List<Expectdo> findByPid(int pid);

    Expectdo findByEidAndUid(int eid, int uid);
}