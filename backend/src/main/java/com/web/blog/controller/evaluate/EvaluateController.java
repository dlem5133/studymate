package com.web.blog.controller.evaluate;

import java.util.ArrayList;

import javax.validation.Valid;

import com.web.blog.dao.evaluate.EvaluateDao;
import com.web.blog.dao.mileage.MileageDao;
import com.web.blog.dao.study.StudyDao;
import com.web.blog.dao.user.UserDao;
import com.web.blog.model.BasicResponse;
import com.web.blog.model.evaluate.Evaluate;
import com.web.blog.model.evaluate.EvaluateRequest;
import com.web.blog.model.mileage.Mileage;
import com.web.blog.model.study.Study;
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

        @CrossOrigin(origins = { "*" })
@RestController
public class EvaluateController {

    @Autowired
    StudyDao studyDao;

    @Autowired
    EvaluateDao evaluateDao;

    @Autowired
    UserDao userDao;

    @Autowired
    MileageDao mileageDao;

    @PostMapping("/eva/score")
    @ApiOperation(value = "평점 넘기기")
    public Object evaScore1(@Valid @RequestBody final EvaluateRequest evaluateRequest) {
        ResponseEntity<Object> response = null;
        User writer = userDao.findUserByUid(evaluateRequest.getWriter_uid());
        User target = userDao.findUserByUid(evaluateRequest.getTarget_uid());
        Study study = studyDao.findStudyByPid(evaluateRequest.getPid());
        Evaluate eva = new Evaluate();
        eva.setPid(evaluateRequest.getPid());
        eva.setWriteruid(writer.getUid());
        eva.setTargetuid(target.getUid());
        eva.setScore1(evaluateRequest.getScore1());
        eva.setScore2(evaluateRequest.getScore2());
        eva.setScore3(evaluateRequest.getScore3());
        eva.setSentence(evaluateRequest.getSentence());
        eva.setCount(evaluateRequest.getCount());
        eva.setStudy(study);
        eva.setUser(writer);
        eva.setUser2(target);
        if (target.getScore1() == 0) {
            target.setScore1(evaluateRequest.getScore1());
        } else {
            // score 1,2,3 세팅
            double a = target.getScore1() * target.getTotalscoreper();
            double b = evaluateRequest.getScore1();
            double c = (a + b) / (target.getTotalscoreper() + 1);
            target.setScore1(c);

            a = target.getScore2() * target.getTotalscoreper();
            b = evaluateRequest.getScore2();
            c = (a + b) / (target.getTotalscoreper() + 1);
            target.setScore2(c);

            a = target.getScore3() * target.getTotalscoreper();
            b = evaluateRequest.getScore3();
            c = (a + b) / (target.getTotalscoreper() + 1);
            target.setScore3(c);

            target.setTotalscoreper(target.getTotalscoreper() + 1);
        }
        userDao.save(target);
        // 마일리지
        Mileage mileage = mileageDao.findByUid(writer.getUid());
        mileage.setTotal(mileage.getTotal() + 10);
        mileage.setEndpoint(mileage.getEvalpoint() + 1);
        mileageDao.save(mileage);
        userDao.save(writer);
        final Evaluate saveEva = this.evaluateDao.save(eva);
        final BasicResponse result = new BasicResponse();

        result.status = true;
        result.data = "평점 넘기기 완료";
        result.object = saveEva;

        response = new ResponseEntity<>(result, HttpStatus.OK);

        return response;
    }

    @PostMapping("/eva/write")
    @ApiOperation(value = "한줄 평 남기기")
    public Object evaWrtie(@Valid @RequestBody final EvaluateRequest evaluateRequest) {

        ResponseEntity<Object> response = null;
        final Evaluate eva = new Evaluate();
        User writer = userDao.findUserByUid(evaluateRequest.getWriter_uid());
        User target = userDao.findUserByUid(evaluateRequest.getTarget_uid());
        Study study = studyDao.findStudyByPid(evaluateRequest.getPid());

        eva.setPid(evaluateRequest.getPid());
        eva.setWriteruid(writer.getUid());
        eva.setTargetuid(target.getUid());
        eva.setScore1(evaluateRequest.getScore1());
        eva.setScore2(evaluateRequest.getScore2());
        eva.setScore3(evaluateRequest.getScore3());
        eva.setSentence(evaluateRequest.getSentence());
        eva.setCount(evaluateRequest.getCount());
        eva.setStudy(study);
        eva.setUser(writer);
        eva.setUser2(target);
        if (target.getScore1() == 0) {
            target.setScore1(evaluateRequest.getScore1());
        } else {
            // score 1,2,3 세팅
            double a = target.getScore1() * target.getTotalscoreper();
            double b = evaluateRequest.getScore1();
            double c = (a + b) / (target.getTotalscoreper() + 1);
            target.setScore1(c);

            a = target.getScore2() * target.getTotalscoreper();
            b = evaluateRequest.getScore2();
            c = (a + b) / (target.getTotalscoreper() + 1);
            target.setScore2(c);

            a = target.getScore3() * target.getTotalscoreper();
            b = evaluateRequest.getScore3();
            c = (a + b) / (target.getTotalscoreper() + 1);
            target.setScore3(c);

            target.setTotalscoreper(target.getTotalscoreper() + 1);
        }
        userDao.save(target);
        Mileage mileage = mileageDao.findByUid(writer.getUid());
        mileage.setTotal(mileage.getTotal() + 200);
        mileage.setEndpoint(mileage.getEndpoint() + 200);
        mileageDao.save(mileage);
        userDao.save(writer);
        final Evaluate saveEva = this.evaluateDao.save(eva);
        final BasicResponse result = new BasicResponse();

        result.status = true;
        result.data = "한줄평 남기고 평점 넘기기 완료";
        result.object = saveEva;

        response = new ResponseEntity<>(result, HttpStatus.OK);

        return response;
    }

    @PostMapping("/eva/list")
    @ApiOperation(value = "평가 목록 조회")
    public Object evalist(@Valid @RequestBody final EvaluateRequest Request) {

        ResponseEntity<Object> response = null;

        final ArrayList<Evaluate> evaList = evaluateDao.findByPidAndWriteruidAndEvadate(Request.getPid(),
                Request.getWriter_uid(), Request.getEva_date());

        final BasicResponse result = new BasicResponse();

        result.status = true;
        result.data = "평가목록 조회";
        result.object = evaList;

        response = new ResponseEntity<>(result, HttpStatus.OK);

        return response;
    }

    @PostMapping("/eva/finallist")
    @ApiOperation(value = "마지막 평가 목록 조회")
    public Object finalevalist(@Valid @RequestBody final EvaluateRequest Request) {

        ResponseEntity<Object> response = null;

        final ArrayList<Evaluate> evaList = evaluateDao.findByPidAndWriteruidAndCount(Request.getPid(),
                Request.getWriter_uid(), 1);

        final BasicResponse result = new BasicResponse();

        result.status = true;
        result.data = "마지막평가목록 조회";
        result.object = evaList;

        response = new ResponseEntity<>(result, HttpStatus.OK);

        return response;
    }

    @PostMapping("/eva/userlist")
    @ApiOperation(value = "유저 한줄평")
    public Object userlist(@Valid @RequestBody final EvaluateRequest Request) {

        ResponseEntity<Object> response = null;

        final ArrayList<Evaluate> evaList = evaluateDao.findByTargetuidAndSentenceNotNull(Request.getTarget_uid());

        final BasicResponse result = new BasicResponse();

        result.status = true;
        result.data = "유저 한줄평 조회";
        result.object = evaList;

        response = new ResponseEntity<>(result, HttpStatus.OK);

        return response;
    }
}