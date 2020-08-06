package com.web.blog.dao.evaluate;

import java.util.ArrayList;

import com.web.blog.model.evaluate.Evaluate;


import org.springframework.data.jpa.repository.JpaRepository;

public interface EvaluateDao extends JpaRepository<Evaluate, String> {

	ArrayList<Evaluate> findByPidAndWriteruidAndCount(int pid, int writer_uid, int count);

	ArrayList<Evaluate> findByWriteruidAndSentenceNotNull(int writer_uid);



	// public Diary findDiaryByDid(int did);

	// public List<Diary> findDiaryByPid(int pid);

	// public Diary findByPid(int pid);

	// public Diary findByPidAndUid(int pid, int uid);

	// public Diary findByDidAndUid(int did, int uid);

	// public List<Diary> findDiaryByPidAndTmp(int pid, int i);

}