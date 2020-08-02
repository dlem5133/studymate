package com.web.blog.controller.admin;

import java.util.List;

import com.web.blog.dao.study.StudyDao;
import com.web.blog.dao.user.UserDao;
import com.web.blog.model.BasicResponse;
import com.web.blog.model.study.Study;
import com.web.blog.model.user.User;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
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

}