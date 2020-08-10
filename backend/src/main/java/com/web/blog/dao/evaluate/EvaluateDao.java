package com.web.blog.dao.evaluate;

import java.sql.Date;
import java.util.ArrayList;

import com.web.blog.model.evaluate.Evaluate;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EvaluateDao extends JpaRepository<Evaluate, String> {

	ArrayList<Evaluate> findByPidAndWriteruidAndCount(int pid, int writer_uid, int count);

	ArrayList<Evaluate> findByWriteruidAndSentenceNotNull(int writer_uid);

	ArrayList<Evaluate> findByTargetuidAndSentenceNotNull(int target_uid);

	ArrayList<Evaluate> findByPidAndWriteruidAndEvadate(int pid, int writer_uid, Date eva_date);

}