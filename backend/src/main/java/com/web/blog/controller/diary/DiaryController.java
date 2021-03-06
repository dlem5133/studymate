package com.web.blog.controller.diary;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import java.time.temporal.ChronoUnit;

import com.web.blog.dao.diary.DiaryDao;
import com.web.blog.dao.diary.ExpectdoDao;
import com.web.blog.dao.mileage.MileageDao;
import com.web.blog.dao.post.ReplyDao;
import com.web.blog.dao.study.StudyDao;
import com.web.blog.dao.user.UserDao;
import com.web.blog.model.BasicResponse;
import com.web.blog.model.diary.Diary;
import com.web.blog.model.diary.DiaryRequest;
import com.web.blog.model.diary.Expectdo;
import com.web.blog.model.diary.ExpectdoRequest;
import com.web.blog.model.mileage.Mileage;
import com.web.blog.model.user.User;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
public class DiaryController {

    @Autowired
    DiaryDao diaryDao;

    @Autowired
    StudyDao studyDao;

    @Autowired
    ReplyDao replyDao;

    @Autowired
    UserDao userDao;

    @Autowired
    ExpectdoDao expectdoDao;

    @Autowired
    MileageDao mileageDao;

    @GetMapping("/diary/list")
    @ApiOperation(value = "?????? ???????????? ?????? ?????? ??????")
    // ?????? ?????? ???????????? Did ??? ???????????? ???????????????, ????????? Date ??? ??????????????????
    public Object lists(@RequestParam(required = true) int pid, @RequestParam(required = true) int tmp) {

        List<Diary> diarylist = diaryDao.findDiaryByPidAndTmp(pid, tmp);
        ResponseEntity<Object> response = null;
        List<Expectdo> expectdo = expectdoDao.findByPid(pid);
        // ????????? ????????? ??? ?????? ?????? ????????? ?????? ??????
        long dday = 1000;
        Expectdo latestExpectdo = new Expectdo();

        for (int i = 0; i < expectdo.size(); i++) {
            long tmp_dday = LocalDate.now().until(expectdo.get(i).getDodate(), ChronoUnit.DAYS);
            if (dday >= tmp_dday && tmp_dday >= 0) {
                dday = tmp_dday;
                latestExpectdo = expectdo.get(i);
            }
        }
        ArrayList<Object> diaryLstAndExpect = new ArrayList<>();
        diaryLstAndExpect.add(diarylist);
        diaryLstAndExpect.add(latestExpectdo);

        if (diarylist != null) {
            BasicResponse result = new BasicResponse();
            result.status = true;
            result.data = "?????? ????????? ?????? ?????? ?????? ??? ????????? ????????? ?????? ??????";
            result.object = diaryLstAndExpect;
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return response;
    }

    @GetMapping("/diary/details")
    @ApiOperation(value = "?????? ????????????")
    // ??????, ?????????, ????????????, ??????, ?????????, ????????????, ??????, ???????????????.
    public Object details(@RequestParam(required = true) final int did) {
        Diary diary = diaryDao.findDiaryByDid(did);
        ResponseEntity<Object> response = null;
        System.out.println(diary.getPosttime());
        if (diary != null) {
            BasicResponse result = new BasicResponse();
            result.status = true;
            result.data = "?????? ???????????? ?????? ??????";
            result.object = diary;
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        return response;
    }

    @PostMapping("/diary/write")
    @ApiOperation(value = "?????? ??????")
    public Object signup(@Valid @RequestBody DiaryRequest request) {

        ResponseEntity<Object> response = null;
        Diary diary = new Diary();
        User user = userDao.findUserByUid(request.getUid());
        diary.setTitle(request.getTitle());
        diary.setBody(request.getBody());
        diary.setWriter(user.getNickname());
        diary.setPid(request.getPid());
        diary.setUid(request.getUid());
        diary.setTmp(request.getTmp());
        diary.setPosttime(request.getPosttime());
        Diary savedDiary = this.diaryDao.save(diary);
        BasicResponse result = new BasicResponse();
        Mileage mileage = mileageDao.findByUid(user.getUid());

        mileage.setTotal(mileage.getTotal() + 10);
        mileage.setDiarypoint(mileage.getDiarypoint() + 10);
        mileageDao.save(mileage);

        userDao.save(user);
        result.status = true;
        result.data = "?????? ?????? ??????";
        result.object = savedDiary;

        response = new ResponseEntity<>(result, HttpStatus.OK);

        return response;
    }

    @PostMapping("/diary/update")
    @ApiOperation(value = "?????? ??????")
    public Object update(@Valid @RequestBody DiaryRequest request) {

        Diary diary = diaryDao.findByDidAndUid(request.getDid(), request.getUid());
        // ???????????? id ??????
        ResponseEntity<Object> response = null;
        // ????????? ????????? ??????
        diary.setTitle(request.getTitle());
        diary.setBody(request.getBody());
        diary.setPid(request.getPid());
        diary.setUid(request.getUid());
        diary.setTmp(request.getTmp());
        diary.setPosttime(request.getPosttime());

        this.diaryDao.save(diary);

        BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = "?????? ?????? ??????";
        response = new ResponseEntity<>(result, HttpStatus.OK);

        return response;
    }

    @PostMapping("/diary/delete")
    @ApiOperation(value = "?????? ??????")
    public Object delete(@Valid @RequestBody DiaryRequest request) {
        // ?????? ?????? ??????
        Diary find_pid = diaryDao.findByDidAndUid(request.getDid(), request.getUid());
        ResponseEntity<Object> response = null;
        BasicResponse result = new BasicResponse();

        diaryDao.delete(find_pid);

        result.status = true;
        result.data = "?????? ?????? ??????";
        response = new ResponseEntity<>(result, HttpStatus.OK);
        return response;
    }

    @PostMapping("/upcoming/create")
    @ApiOperation(value = "????????? ????????? ??????")
    public Object upcomingCreate(@Valid @RequestBody ExpectdoRequest request) {

        Expectdo expectdo = new Expectdo();

        expectdo.setEid(request.getEid());
        expectdo.setPid(request.getPid());
        expectdo.setUid(request.getUid());

        expectdo.setDoplace(request.getDoplace());
        expectdo.setDodate(request.getDodate());
        expectdo.setAssignment(request.getAssignment());
        expectdo.setAssignment_file(request.getAssignment_file());
        expectdoDao.save(expectdo);

        ResponseEntity<Object> response = null;
        BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = "????????? ????????? ?????? ??????";
        result.object = expectdo;
        response = new ResponseEntity<>(result, HttpStatus.OK);

        return response;
    }

    @PostMapping("/upcoming/update")
    @ApiOperation(value = "????????? ????????? ??????")
    public Object upcomingUpdate(@Valid @RequestBody ExpectdoRequest request) {

        Expectdo expectdo = new Expectdo();

        expectdo.setEid(request.getEid());
        expectdo.setPid(request.getPid());
        expectdo.setUid(request.getUid());

        expectdo.setDoplace(request.getDoplace());
        expectdo.setDodate(request.getDodate());
        expectdo.setAssignment(request.getAssignment());
        expectdo.setAssignment_file(request.getAssignment_file());

        Expectdo saved = expectdoDao.save(expectdo);
        ResponseEntity<Object> response = null;
        BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = "????????? ????????? ?????? ??????";
        result.object = saved;
        response = new ResponseEntity<>(result, HttpStatus.OK);

        return response;
    }

    @PostMapping("/upcoming/delete")
    @ApiOperation(value = "????????? ????????? ??????")
    public Object upcomingDelete(@Valid @RequestBody ExpectdoRequest request) {

        Expectdo find_expectdo = expectdoDao.findByEidAndUid(request.getEid(), request.getUid());
        ResponseEntity<Object> response = null;
        BasicResponse result = new BasicResponse();
        expectdoDao.delete(find_expectdo);

        result.status = true;
        result.data = "????????? ????????? ??????";
        response = new ResponseEntity<>(result, HttpStatus.OK);
        return response;
    }

}