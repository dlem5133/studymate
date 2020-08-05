package com.web.blog.dao.study;

import com.web.blog.model.study.Gugun;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GugunDao extends JpaRepository<Gugun, String> {

    List<Gugun> findBySidocode(String sidocode);

	List<Gugun> findByGugunnameLike(String string);

	Gugun findByGuguncode(Integer sigungu_code);
}