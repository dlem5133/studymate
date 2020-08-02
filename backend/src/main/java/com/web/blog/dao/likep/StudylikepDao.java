package com.web.blog.dao.likep;

import java.util.List;

import com.web.blog.model.likep.Studylikep;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StudylikepDao extends JpaRepository<Studylikep, String> {

    public List<Studylikep> findStudylikepByUid(int uid);

    public Studylikep findStudylikepByuid(int uid);

    public Studylikep findStudylikepByUidAndPid(int uid, int pid);

    public Studylikep countByPid(int pid);

    public List<Studylikep> findStudylikepByPid(Integer pid);
}