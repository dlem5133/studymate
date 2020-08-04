package com.web.blog.dao.evaluate;

import com.web.blog.model.evaluate.Evaluate;


import org.springframework.data.jpa.repository.JpaRepository;

public interface EvaluateDao extends JpaRepository<Evaluate, String> {



	// public Diary findDiaryByDid(int did);

	// public List<Diary> findDiaryByPid(int pid);

	// public Diary findByPid(int pid);

	// public Diary findByPidAndUid(int pid, int uid);

	// public Diary findByDidAndUid(int did, int uid);

	// public List<Diary> findDiaryByPidAndTmp(int pid, int i);

}