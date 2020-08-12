package com.web.blog.controller.mileage;

import java.util.List;

import com.web.blog.dao.evaluate.EvaluateDao;
import com.web.blog.dao.mileage.MileageDao;
import com.web.blog.dao.study.StudyDao;
import com.web.blog.dao.user.UserDao;
import com.web.blog.model.BasicResponse;
import com.web.blog.model.mileage.Mileage;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiOperation;

import org.springframework.web.bind.annotation.RequestParam;

@ApiResponses(value = { @ApiResponse(code = 401, message = "Unauthorized", response = BasicResponse.class),
        @ApiResponse(code = 403, message = "Forbidden", response = BasicResponse.class),
        @ApiResponse(code = 404, message = "Not Found", response = BasicResponse.class),
        @ApiResponse(code = 500, message = "Failure", response = BasicResponse.class) })

        @CrossOrigin(origins = { "http://i3b205.p.ssafy.io" })
@RestController
public class MileageController {

    @Autowired
    StudyDao studyDao;

    @Autowired
    EvaluateDao evaluateDao;

    @Autowired
    MileageDao mileageDao;

    @Autowired
    UserDao userDao;

    @GetMapping("/mileage/user")
    @ApiOperation(value = "개인 마일리지 조회")
    public Object user(@RequestParam(required = true) Integer uid) {

        ResponseEntity<Object> response = null;
        Mileage mileage = mileageDao.findByUid(uid);

        final BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = "개인 마일리지 조회";
        result.object = mileage;
        response = new ResponseEntity<>(result, HttpStatus.OK);

        return response;
    }
    @GetMapping("/mileage/list")
    @ApiOperation(value = "랭킹 조회")
    public Object list() {

        ResponseEntity<Object> response = null;
        List<Mileage> mileage = mileageDao.findByOrderByTotalDesc();

        final BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = "개인 마일리지 조회";
        result.object = mileage;
        response = new ResponseEntity<>(result, HttpStatus.OK);

        return response;
    }

}