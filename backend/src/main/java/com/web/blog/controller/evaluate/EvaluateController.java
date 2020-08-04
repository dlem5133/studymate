package com.web.blog.controller.evaluate;

import javax.validation.Valid;

import com.web.blog.dao.evaluate.EvaluateDao;
import com.web.blog.dao.study.StudyDao;
import com.web.blog.dao.user.UserDao;
import com.web.blog.model.BasicResponse;
import com.web.blog.model.evaluate.Evaluate;
import com.web.blog.model.evaluate.EvaluateRequest;
import com.web.blog.model.user.User;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.beans.factory.annotation.Autowired;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@ApiResponses(value = { @ApiResponse(code = 401, message = "Unauthorized", response = BasicResponse.class),
        @ApiResponse(code = 403, message = "Forbidden", response = BasicResponse.class),
        @ApiResponse(code = 404, message = "Not Found", response = BasicResponse.class),
        @ApiResponse(code = 500, message = "Failure", response = BasicResponse.class) })

@CrossOrigin(origins = { "http://localhost:3000" })
@RestController
public class EvaluateController {

    @Autowired
    StudyDao studyDao;

    @Autowired
    EvaluateDao evaluateDao;

    @Autowired
    UserDao userDao;

    @PostMapping("/eva/score")
    @ApiOperation(value = "평점 넘기기")
    public Object evaScore(@Valid @RequestBody final EvaluateRequest evaluateRequest) {

        ResponseEntity<Object> response = null;
        User writer = userDao.findUserByUid(evaluateRequest.getWriter_uid());

        User target = userDao.findUserByUid(evaluateRequest.getTarget_uid());

        final Evaluate eva = new Evaluate();

        eva.setPid(evaluateRequest.getPid());
        eva.setWriter_uid(writer.getUid());
        eva.setTarget_uid(target.getUid());
        eva.setScore(evaluateRequest.getScore());
        
        final Evaluate saveEva = this.evaluateDao.save(eva);
        final BasicResponse result = new BasicResponse();

        result.status = true;
        result.data = "평점 넘기기 완료";
        result.object = saveEva;

        response = new ResponseEntity<>(result, HttpStatus.OK);

        return response;
    }



    @PostMapping("/eva/write")
    @ApiOperation(value = "평점 넘기고 한줄 평 남기기")
    public Object evaWrtie(@Valid @RequestBody final EvaluateRequest evaluateRequest) {
        
        ResponseEntity<Object> response = null;
        User writer = userDao.findUserByUid(evaluateRequest.getWriter_uid());

        User target = userDao.findUserByUid(evaluateRequest.getTarget_uid());

        final Evaluate eva = new Evaluate();

        eva.setPid(evaluateRequest.getPid());
        eva.setWriter_uid(writer.getUid());
        eva.setTarget_uid(target.getUid());
        eva.setSentence(evaluateRequest.getSentence());
        eva.setScore(evaluateRequest.getScore());
       

        final Evaluate saveEva = this.evaluateDao.save(eva);
        final BasicResponse result = new BasicResponse();

        result.status = true;
        result.data = "한줄평 남기고 평점 넘기기 완료";
        result.object = saveEva;

        response = new ResponseEntity<>(result, HttpStatus.OK);

        return response;
    }

}