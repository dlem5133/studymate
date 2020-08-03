
package com.web.blog.dao.user;

import com.web.blog.model.user.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportDao extends JpaRepository<Report, String> {
 
}
