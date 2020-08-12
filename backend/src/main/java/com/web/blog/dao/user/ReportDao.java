
package com.web.blog.dao.user;

import java.util.List;

import com.web.blog.model.user.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportDao extends JpaRepository<Report, String> {
   Report findReportByPidAndReporterAndTarget(int pid, int reporter, int target);

   int countByPidAndTarget(int pid, int target);

   List<Report> findByPidAndTarget(int pid, int target);

   void deleteByPidAndTarget(int pid, int target);
}
