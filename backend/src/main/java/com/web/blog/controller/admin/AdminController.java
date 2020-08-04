package com.web.blog.controller.admin;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;

import com.web.blog.dao.study.IndvstudylstDao;
import com.web.blog.dao.study.StudyDao;
import com.web.blog.dao.user.ReportDao;
import com.web.blog.dao.user.UserDao;
import com.web.blog.model.BasicResponse;
import com.web.blog.model.study.Indvstudylst;
import com.web.blog.model.study.Study;
import com.web.blog.model.user.Report;
import com.web.blog.model.user.ReportRequest;
import com.web.blog.model.user.User;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@ApiResponses(value = { @ApiResponse(code = 401, message = "Unauthorized", response = BasicResponse.class),
        @ApiResponse(code = 403, message = "Forbidden", response = BasicResponse.class),
        @ApiResponse(code = 404, message = "Not Found", response = BasicResponse.class),
        @ApiResponse(code = 500, message = "Failure", response = BasicResponse.class) })

@CrossOrigin(origins = { "http://localhost:3000" })
@RestController
public class AdminController {

    @Autowired
    StudyDao studyDao;

    @Autowired
    UserDao userDao;

    @Autowired
    ReportDao reportDao;

    @Autowired
    IndvstudylstDao indvStudylstDao;

    @PostMapping("/admin/allUser")
    @ApiOperation(value = "모든 회원 조회")
    public Object findAllUser() {
        List<User> user = userDao.findAll();
        
        ResponseEntity<Object> response = null;
        BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = "모든 회원 조회 완료";
        result.object = user;

        response = new ResponseEntity<>(result, HttpStatus.OK);

        return response;

    }

    @PostMapping("/admin/allStudy")
    @ApiOperation(value = "모든 스터디 조회")
    public Object findAllStudy() {
        List<Study> study = studyDao.findAll();

        ResponseEntity<Object> response = null;
        BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = "모든 회원 조회 완료";
        result.object = study;

        response = new ResponseEntity<>(result, HttpStatus.OK);

        return response;

    }

    @PostMapping("/admin/report")
    @ApiOperation(value = "3회 이상 신고받은 유저 리스트")
    public Object searchReportUser() {
        ResponseEntity<Object> response = null;
        List<Indvstudylst> check = indvStudylstDao.findByIsjoin(1);

        BasicResponse result = new BasicResponse();
        ArrayList<List<Report>> myset = new ArrayList<>();

        // HashMap<Integer, Integer> myset = new HashMap<>();

        for (int i = 0; i < check.size(); i++) {
            int reportuser = reportDao.countByPidAndTarget(check.get(i).getPid(), check.get(i).getUid());
            if (reportuser >= (int) Math.round(indvStudylstDao.countByPid(check.get(i).getPid()))/(double) 2) {
                myset.add(reportDao.findByPidAndTarget(check.get(i).getPid(), check.get(i).getUid()));
            }
        }

        result.status = true;
        result.data = "모든 회원 조회 완료";
        result.object = myset;

        response = new ResponseEntity<>(result, HttpStatus.OK);

        return response;
    }

    @Transactional
    @PostMapping("/admin/givepenalty")
    @ApiOperation(value = "패널티 부여")
    public Object givePenalty(@Valid @RequestBody ReportRequest request) {

        ResponseEntity<Object> response = null;

        User user = userDao.findUserByUid(request.getTarget());
        user.setPenalty(user.getPenalty() + 1);
        userDao.save(user);

        // 패널티 부여 시, 해당 신고내역은 삭제
        reportDao.deleteByPidAndTarget(request.getPid(), request.getTarget());

        // 패널티 부여 시, 해당 유저는 해당 스터디에서 추방
        indvStudylstDao.deleteByPidAndUid(request.getPid(), request.getTarget());

        BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = "패널티 부여 및 해당 스터디의 target 신고 내역 완료";
        result.object = user;

        response = new ResponseEntity<>(result, HttpStatus.OK);

        return response;

    }

    @PostMapping("/admin/banlist")
    @ApiOperation(value = "벤 목록 조회")
    public Object searchban() {
        ResponseEntity<Object> response = null;
        List<User> user = userDao.findByPenaltyGreaterThan(3);

        BasicResponse result = new BasicResponse();

        result.status = true;
        result.data = "벤 목록 조회 완료";
        result.object = user;

        response = new ResponseEntity<>(result, HttpStatus.OK);
        return response;

    }

}