package com.web.blog.dao.diary;

import com.web.blog.model.diary.Diary;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryDao extends JpaRepository<Diary, String> {

	public Diary findDiaryByDid(int did);

	public List<Diary> findDiaryByPid(int pid);

	public Diary findByPid(int pid);

	public Diary findByPidAndUid(int pid, int uid);

	public Diary findByDidAndUid(int did, int uid);

	public List<Diary> findDiaryByPidAndTmp(int pid, int i);

}