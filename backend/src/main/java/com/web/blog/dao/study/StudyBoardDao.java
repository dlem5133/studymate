package com.web.blog.dao.study;

import java.util.List;

import com.web.blog.model.study.Board;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyBoardDao extends JpaRepository<Board, String> {

	Board findBoardByBid(int bid);

	List<Board> findBoardByPid(int pid);

	List<Board> findBoardByTitleLikeOrderByDate(String string);

	List<Board> findBoardByBodyLikeOrderByDate(String string);

	List<Board> findBoardByTitleLikeOrBodyLikeOrderByDate(String string, String string2);

}