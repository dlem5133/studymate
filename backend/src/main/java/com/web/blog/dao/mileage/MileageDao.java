package com.web.blog.dao.mileage;

import com.web.blog.model.mileage.Mileage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MileageDao extends JpaRepository<Mileage, String> {

	Mileage findByUid(Integer uid);

   
}