package com.web.blog.dao.post;

import java.util.List;

import com.web.blog.model.post.Reply;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyDao extends JpaRepository<Reply, String> {

	public List<Reply> findReplyByPid(int pid);

	public Reply findReplyByRid(int rid);

	public Reply findReplyByRidAndUid(int rid, int uid);

	public Reply findReplyByRidAndUidAndPid(int rid, int uid, int pid);

	public int countByReplyparent(int rid);

	int countByRid(int rid);

}