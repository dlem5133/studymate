package com.web.blog.controller.reply;

import javax.validation.Valid;

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

@CrossOrigin(origins = { "http://localhost:3000" })
@RestController
public class ReplyController {

    @Autowired
    StudyDao studyDao;

    @Autowired
    ReplyDao replyDao;

    @Autowired
    UserDao userDao;

    @PostMapping("/reply/write")
    @ApiOperation(value = "댓글 생성")
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

        final Reply saveReply = this.replyDao.save(reply);
        final BasicResponse result = new BasicResponse();

        result.status = true;
        result.data = "댓글 생성 완료";
        result.object = saveReply;

        response = new ResponseEntity<>(result, HttpStatus.OK);

        return response;
    }

    // 로그인 아이디와 작성자 아이디 동일한 지 확인 후 수정 가능하도록 해야함.
    // 또한, 현재는 원래 댓글에 수정되는 것이 아닌 새로 댓글이 생성됨.. 프론트쪽과 auto increment확인해봐야할듯
    @PostMapping("/reply/update")
    @ApiOperation(value = "댓글 수정")
    public Object replyUpdate(@Valid @RequestBody final ReplyRequest replyRequest) {

        Reply find_Reply = replyDao.findReplyByRid(replyRequest.getRid());
        // 이메일로 id 확인
        ResponseEntity<Object> response = null;
        final BasicResponse result = new BasicResponse();

        User user = userDao.findUserByUid(replyRequest.getUid());

        find_Reply.setReply_content(replyRequest.getReply_content());
        find_Reply.setStudy(studyDao.findStudyByPid(replyRequest.getPid()));
        find_Reply.setReply_writer(user.getNickname());
        find_Reply.setPid(replyRequest.getPid());

        this.replyDao.save(find_Reply);

        result.status = true;
        result.data = "댓글 수정 완료";
        response = new ResponseEntity<>(result, HttpStatus.OK);
        return response;
    }

    @PostMapping("/reply/delete")
    @ApiOperation(value = "댓글 삭제")
    public Object replyDelete(@Valid @RequestBody final ReplyRequest replyRequest) {
        // 회원 정보 삭제
        Reply reply_rid = replyDao.findReplyByRidAndUidAndPid(replyRequest.getRid(), replyRequest.getUid(),
                replyRequest.getPid());
        // Diary find_pid = diaryDao.findByDidAndUid(request.getDid(),request.getUid());
        ResponseEntity<Object> response = null;

        if (reply_rid.getReplyparent() != 0) {
            final BasicResponse result = new BasicResponse();
            //첫 댓이 살아있지 않으면 첫댓 아예 DB에서 지워요
            if ((replyDao.findReplyByRid(reply_rid.getReplyparent())).getReply_writer().equals("")) {
                replyDao.delete(replyDao.findReplyByRid(reply_rid.getReplyparent()));
            }
            replyDao.delete(reply_rid);
            result.status = true;
            result.data = "댓글 삭제 완료";
            response = new ResponseEntity<>(result, HttpStatus.OK);

        } else {
            // 첫댓글이고
            // 해당 댓글에 답댓글이 있을 때
            if (replyDao.countByReplyparent(reply_rid.getRid()) != 0) {
                final BasicResponse result = new BasicResponse();
                reply_rid.setReply_content("삭제된 댓글입니다.");
                reply_rid.setReply_writer("");
                replyDao.save(reply_rid);
                result.status = true;
                result.data = "댓글 삭제 완료";
                response = new ResponseEntity<>(result, HttpStatus.OK);
            }

            // 답댓이 없을 때
            else {
                final BasicResponse result = new BasicResponse();
                replyDao.delete(reply_rid);
                result.status = true;
                result.data = "댓글 삭제 완료";
                response = new ResponseEntity<>(result, HttpStatus.OK);
            }

        }
        return response;
    }

}