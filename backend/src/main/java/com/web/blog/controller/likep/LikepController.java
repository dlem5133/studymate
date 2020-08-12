package com.web.blog.controller.likep;

import java.util.List;

import javax.validation.Valid;

import com.web.blog.dao.likep.StudylikepDao;
import com.web.blog.dao.study.StudyDao;
import com.web.blog.model.BasicResponse;

import com.web.blog.model.likep.Studylikep;
import com.web.blog.model.likep.StudylikepRequest;

import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@ApiResponses(value = { @ApiResponse(code = 401, message = "Unauthorized", response = BasicResponse.class),
        @ApiResponse(code = 403, message = "Forbidden", response = BasicResponse.class),
        @ApiResponse(code = 404, message = "Not Found", response = BasicResponse.class),
        @ApiResponse(code = 500, message = "Failure", response = BasicResponse.class) })

        @CrossOrigin(origins = { "http://i3b205.p.ssafy.io" })
@RestController
public class LikepController {

    @Autowired
    StudyDao studyDao;

    @Autowired
    StudylikepDao studylikeDao;

    @PostMapping("/likep/likep")
    @ApiOperation(value = "좋아요 기능")
    public Object likeon(@Valid @RequestBody final StudylikepRequest studylikepRequest) {

        ResponseEntity<Object> response = null;
        final BasicResponse result = new BasicResponse();

        Studylikep likep = new Studylikep();
        likep = studylikeDao.findStudylikepByUidAndPid(studylikepRequest.getUid(), studylikepRequest.getPid());

        if (likep == null) {
            final Studylikep likepp = new Studylikep();
            likepp.setPid(studylikepRequest.getPid());
            likepp.setUid(studylikepRequest.getUid());
            final Studylikep saveLikep = this.studylikeDao.save(likepp);
            result.status = true;
            result.data = "좋아요 Up";
            result.object = saveLikep;
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            studylikeDao.delete(likep);
            result.status = true;
            result.data = "좋아요 취소";
            response = new ResponseEntity<>(result, HttpStatus.OK);
        }
        return response;
    }

    @GetMapping("/likep/list")
    @ApiOperation(value = "좋아요 전체 목록")
    // 전체 글 가져오기
    public Object likeplist(@RequestParam(required = true) int pid) {
        List<Studylikep> likelist = studylikeDao.findStudylikepByPid(pid);
        ResponseEntity<Object> response = null;
        if (likelist != null) {

            BasicResponse result = new BasicResponse();
            result.status = true;
            result.data = "좋아요 전체 목록 조회 완료";
            result.object = likelist;
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        return response;
    }

}