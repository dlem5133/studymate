package com.web.blog.dao.study;

import com.web.blog.model.study.Studytag;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyTagDao extends JpaRepository<Studytag, String> {

	void deleteByPid(int pid);

	List<Studytag> findStudytagByPid(int pid);

	List<Studytag> findStudytagByTagnameLike(String title);

	void deleteAllByPid(int pid);

	List<Integer> findPidByTagnameLike(String string);

}