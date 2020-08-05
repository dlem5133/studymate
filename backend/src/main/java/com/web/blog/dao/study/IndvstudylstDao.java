package com.web.blog.dao.study;

import com.web.blog.model.study.EmpId;
import com.web.blog.model.study.Indvstudylst;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IndvstudylstDao extends JpaRepository<Indvstudylst, EmpId> {

	public List<Indvstudylst> findByUid(Integer integer);

	public List<Indvstudylst> findByUidAndIsjoin(Integer uid, int i);

	public Indvstudylst findByPidAndUid(int pid, int uid);

	public List<Indvstudylst> findByPid(int pid);

	public List<Indvstudylst> findByPidAndIsjoin(int pid, int i);

	public List<Integer> countByPidAndIsjoin(int pid, int i);

	Integer countByPid(int pid);
	
	List<Indvstudylst> findByIsjoin(int i);

	public void deleteByUid(Integer uid);

	// public List<Indvstudylst> findByPidOrderByJoinDate(Integer pid);

	// public List<Indvstudylst> findByPidOrderByJoin_date(Integer pid);

	public void deleteByPidAndUid(int pid, int target);

	public void deleteAllByUid(Integer uid);
}