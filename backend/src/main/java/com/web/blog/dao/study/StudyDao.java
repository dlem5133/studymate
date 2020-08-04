package com.web.blog.dao.study;

import com.web.blog.model.study.Study;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyDao extends JpaRepository<Study, String> {

	public Study findStudyByPid(int pid);

	public void findStudyByTitle(String title);

	public List<Study> findStudyByTitleLike(String title);

	public List<Study> findStudyByCategoryLike(String category);

	public List<Study> findStudyByCategory(String category);

	public List<Study> findStudyByTitleLikeAndCategoryLike(String string, String category);

	public Study findPostByPid(int pid);

	public void findPostByTitle(String title);

	public List<Study> findByTmp(int i);

	public List<Study> findStudyByTitleLikeAndTmp(String title, int i);

	public List<Study> findByTmpLessThan(int i);

	public List<Study> findStudyByCategoryLikeAndTmp(String category, int i);

	public List<Study> findStudyByTitleLikeAndCategoryLikeAndTmp(String string, String category, int i);


	public List<Study> findByTmpAndUid(int i, int uid);

	public List<Study> findByTmpOrderByPosttimeAsc(int i);

	public List<Study> findStudyByTitleLikeAndCategoryLikeAndTmpOrderByPosttimeAsc(String string, String category,
			int i);


	public List<Study> findStudyByCategoryLikeAndTmpOrderByPosttimeAsc(String category, int i);

	public List<Study> findTop10ByTmpOrderByLikepDesc(int i);

	public List<Study> findStudyBySidocodeOrderByPosttimeAsc(Integer sidocode);


	public List<Study> findStudyBySigungucodeOrderByPosttimeAsc(Integer integer);

	public List<Study> findStudyByUid(Integer uid);

}