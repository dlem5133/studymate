package com.web.blog.controller.reply;

import javax.validation.Valid;
import java.time.LocalDateTime;

import com.web.blog.dao.post.ReplyDao;
import com.web.blog.dao.study.StudyDao;
import com.web.blog.dao.user.UserDao;
import com.web.blog.model.BasicResponse;
import com.web.blog.model.post.Reply;
import com.web.blog.model.post.ReplyRequest;
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
public class ReplyController {

    @Autowired
    StudyDao studyDao;

    @Autowired
    ReplyDao replyDao;

    @Autowired
    UserDao userDao;

    @PostMapping("/reply/write")
    @ApiOperation(value = "λκΈ μμ±")
    public Object replyWrtie(@Valid @RequestBody final ReplyRequest replyRequest) {

        ResponseEntity<Object> response = null;
        User user = userDao.findUserByUid(replyRequest.getUid());

        final Reply reply = new Reply();

        reply.setUser(user);
        reply.setReply_content(replyRequest.getReply_content());
        reply.setReply_writer(user.getNickname());
        reply.setPid(replyRequest.getPid());
        reply.setStudy(studyDao.findStudyByPid(replyRequest.getPid()));
        reply.setReplyparent(replyRequest.getReply_parent());
        reply.setReply_time(LocalDateTime.now());


        final Reply saveReply = this.replyDao.save(reply);
        final BasicResponse result = new BasicResponse();

        result.status = true;
        result.data = "λκΈ μμ± μλ£";
        result.object = saveReply;

        response = new ResponseEntity<>(result, HttpStatus.OK);

        return response;
    }

    // λ‘κ·ΈμΈ μμ΄λμ μμ±μ μμ΄λ λμΌν μ§ νμΈ ν μμ  κ°λ₯νλλ‘ ν΄μΌν¨.
    // λν, νμ¬λ μλ λκΈμ μμ λλ κ²μ΄ μλ μλ‘ λκΈμ΄ μμ±λ¨.. νλ‘ νΈμͺ½κ³Ό auto incrementνμΈν΄λ΄μΌν λ―
    @PostMapping("/reply/update")
    @ApiOperation(value = "λκΈ μμ ")
    public Object replyUpdate(@Valid @RequestBody final ReplyRequest replyRequest) {

        Reply find_Reply = replyDao.findReplyByRid(replyRequest.getRid());
        // μ΄λ©μΌλ‘ id νμΈ
        ResponseEntity<Object> response = null;
        final BasicResponse result = new BasicResponse();

        User user = userDao.findUserByUid(replyRequest.getUid());

        find_Reply.setReply_content(replyRequest.getReply_content());
        find_Reply.setStudy(studyDao.findStudyByPid(replyRequest.getPid()));
        find_Reply.setReply_writer(user.getNickname());
        find_Reply.setPid(replyRequest.getPid());

        this.replyDao.save(find_Reply);

        result.status = true;
        result.data = "λκΈ μμ  μλ£";
        response = new ResponseEntity<>(result, HttpStatus.OK);
        return response;
    }

    @PostMapping("/reply/delete")
    @ApiOperation(value = "λκΈ μ­μ ")
    public Object replyDelete(@Valid @RequestBody final ReplyRequest replyRequest) {
        // νμ μ λ³΄ μ­μ 
        Reply reply_rid = replyDao.findReplyByRidAndUidAndPid(replyRequest.getRid(), replyRequest.getUid(),
                replyRequest.getPid());
        // Diary find_pid = diaryDao.findByDidAndUid(request.getDid(),request.getUid());
        ResponseEntity<Object> response = null;
        BasicResponse result = new BasicResponse();

        // μ²«λκΈμ΄ μλ λ
        if (reply_rid.getReplyparent() != 0) {
            if (replyDao.countByReplyparent(reply_rid.getReplyparent()) <= 1) {
                // μ²« λμ΄ μ΄μμμ§ μμΌλ©΄ μ²«λ μμ DBμμ μ§μμ
                if ((replyDao.findReplyByRid(reply_rid.getReplyparent())).getReply_writer().equals("")) {
                    replyDao.delete(replyDao.findReplyByRid(reply_rid.getReplyparent()));
                }
            }

            replyDao.delete(reply_rid);
            result.status = true;
            result.data = "λκΈ μ­μ  μλ£";
            response = new ResponseEntity<>(result, HttpStatus.OK);

        } else {
            // μ²«λκΈμ΄κ³ 
            // ν΄λΉ λκΈμ λ΅λκΈμ΄ μμ λ
            if (replyDao.countByReplyparent(reply_rid.getRid()) != 0) {
                reply_rid.setReply_content("μ­μ λ λκΈμλλ€.");
                reply_rid.setReply_writer("");
                replyDao.save(reply_rid);
                result.status = true;
                result.data = "λκΈ μ­μ  μλ£";
                response = new ResponseEntity<>(result, HttpStatus.OK);
            }

            // λ΅λμ΄ μμ λ
            else {
                result = new BasicResponse();
                replyDao.delete(reply_rid);
                result.status = true;
                result.data = "λκΈ μ­μ  μλ£";
                response = new ResponseEntity<>(result, HttpStatus.OK);
            }

        }
        return response;
    }

}