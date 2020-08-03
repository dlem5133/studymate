package com.web.blog.controller.account;

import java.util.List;

import javax.validation.Valid;

import com.web.blog.dao.study.IndvstudylstDao;
import com.web.blog.dao.study.StudyDao;
import com.web.blog.dao.user.ReportDao;
import com.web.blog.dao.user.UserDao;
import com.web.blog.mail.MailHandler;
import com.web.blog.model.BasicResponse;
import com.web.blog.model.study.Study;
import com.web.blog.model.study.StudyRequest;
import com.web.blog.model.study.Indvstudylst;
import com.web.blog.model.user.Report;
import com.web.blog.model.user.ReportRequest;
import com.web.blog.model.user.SignupRequest;
import com.web.blog.model.user.User;
import com.web.blog.service.JwtService;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
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

@CrossOrigin(origins = { "http://localhost:3000" })
@RestController
public class AccountController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    UserDao userDao;
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    StudyDao studyDao;

    @Autowired
    IndvstudylstDao indvstudylstDao;

    @Autowired
    ReportDao reportDao;

    @GetMapping("/account/login")
    @ApiOperation(value = "로그인")
    public Object login(@RequestParam(required = true) final String email,
            @RequestParam(required = true) final String password) {
        User userOpt = userDao.findUserByEmailAndPassword(email, password);
        ResponseEntity<Object> response = null;
        String token = jwtService.createLoginToken(userOpt);

        if (userOpt.getPassword().equals(password)) {
            final BasicResponse result = new BasicResponse();
            result.status = true;
            result.data = "로그인 성공";
            result.object = token;
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return response;
    }

    @PostMapping("/account/signup")
    @ApiOperation(value = "가입하기")
    public Object signup(@Valid @RequestBody final SignupRequest request) {
        // 이메일, 닉네임 중복처리 필수
        // 회원가입단을 생성해 보세요.
        final User email_test = userDao.getUserByEmail(request.getEmail());
        final User nickname_test = userDao.getUserByNickname(request.getNickname());
        ResponseEntity<Object> response = null;

        if (email_test != null) {
            final BasicResponse result = new BasicResponse();
            result.status = false;
            result.data = "이메일 중복";
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else if (nickname_test != null) {
            final BasicResponse result = new BasicResponse();
            result.status = false;
            result.data = "닉네임 중복";
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            final User user = new User();
            user.setEmail(request.getEmail());
            user.setPassword(request.getPassword());
            user.setNickname(request.getNickname());
            user.setIntro(request.getIntro());
            user.setProfile_image(request.getProfile_image());
            
            this.userDao.save(user);

            final BasicResponse result = new BasicResponse();
            result.status = true;
            result.data = "회원가입 완료";
            
            try {
                final String memberMail = request.getEmail();
                final MailHandler mail = new MailHandler(mailSender);
                mail.setFrom("ssafystudy@gmail.com", "스터디메이트");
                mail.setTo(memberMail);
                mail.setSubject("STUDYMATE 회원가입 인증 메일");
                mail.setText(new StringBuffer().append("<h1>STUDYMATE 회원가입 하신걸 환영합니다!!</h1>").toString());
                mail.send();
                result.data = "메일 전송 완료";
            } catch (final Exception e) {
                e.printStackTrace();
                result.data = "메일 전송 실패";
            }
            response = new ResponseEntity<>(result, HttpStatus.OK);
        }

        return response;
    }

    @PostMapping("/account/update")
    @ApiOperation(value = "수정")
    public Object update(@Valid @RequestBody final SignupRequest request) {
        // 회원 정보 수정
        User user = userDao.findByEmailAndUid(request.getEmail(), request.getUid());
        // 이메일로 id 확인
        ResponseEntity<Object> response = null;
        // final User user = new User();
        // user.setUid(request.getUid());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setNickname(request.getNickname());
        user.setIntro(request.getIntro());
        user.setProfile_image(request.getProfile_image());

        // 이미지 업로드 부분
        this.userDao.save(user);
        String token = jwtService.createLoginToken(user);

        final BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = "회원 수정 완료";
        result.object = token;
        response = new ResponseEntity<>(result, HttpStatus.OK);
        return response;
    }
    
    @PostMapping("/account/delete")
    @ApiOperation(value = "삭제")
    public Object delete(@Valid @RequestBody final SignupRequest request) {
        // 회원 정보 삭제
        // 이메일로 삭제
        User user = userDao.findUserByEmailAndPassword(request.getEmail(), request.getPassword());
        ResponseEntity<Object> response = null;
        final BasicResponse result = new BasicResponse();

        userDao.delete(user);
        result.status = true;
        result.data = "회원 탈퇴 완료";

        response = new ResponseEntity<>(result, HttpStatus.OK);
        return response;
    }

    @GetMapping("/account/findPassword")
    @ApiOperation(value = "비밀번호 찾기")
    public Object findPassword(@RequestParam(required = true) final String email,
            @RequestParam(required = true) final String nickname) {

        final User userforPassword = userDao.findUserByEmailAndNickname(email, nickname);
        ResponseEntity<?> response = null;

        if (!userforPassword.getPassword().equals("")) {
            final BasicResponse result = new BasicResponse();
            result.status = true;
            result.data = "success";
            try {
                final String memberMail = userforPassword.getEmail();
                final MailHandler mail = new MailHandler(mailSender);
                mail.setFrom("ssafystudy@gmail.com", "스터디메이트");
                mail.setTo(memberMail);
                mail.setSubject("STUDYMATE 비밀번호 인증 메일");
                mail.setText(new StringBuffer().append("<h1> 찾으시는 비밀번호는 \"").append(userforPassword.getPassword())
                        .append("\"입니다 <h1>").toString());
                mail.send();
                result.data = "비밀번호 찾기 메일 전송 완료";
            } catch (final Exception e) {
                e.printStackTrace();
                result.data = "비밀번호 찾기 메일 전송 실패";
            }
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            response = new ResponseEntity<>("null", HttpStatus.NOT_FOUND);
        }

        return response;
    }

    @GetMapping("/account/profile")
    @ApiOperation(value = "회원 프로필")
    public Object profile(@RequestParam(required = true) final String Token) { // 회원 정보 조회
        // 이메일로 조회
        User user = (jwtService.getUser(Token));

        ResponseEntity<Object> response = null;
        final BasicResponse result = new BasicResponse();

        result.status = true;
        result.data = "회원 프로필 조회 완료";
        result.object = user;

        response = new ResponseEntity<>(result, HttpStatus.OK);
        return response;
    }

    @PostMapping("/account/tmplist")
    @ApiOperation(value = "임시저장된 스터디 전체 목록")
    // 닉네임으로 임시저장 리스트 불러오기
    public Object tmplist(@Valid @RequestBody final StudyRequest request) {
        final List<Study> studylist = studyDao.findByTmpAndUid(0, request.getUid());
        ResponseEntity<Object> response = null;

        if (studylist != null) {
            final BasicResponse result = new BasicResponse();
            result.status = true;
            result.data = "임시저장된 스터디 전체 조회 완료";
            result.object = studylist;
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        return response;
    }

    @PostMapping("/account/studylist")
    @ApiOperation(value = "가입된 스터디 전체 목록")
    // uid로 studylist 가져오기
    public Object studylist(@Valid @RequestBody final SignupRequest request) {
        final User user = userDao.findUserByEmailAndNickname(request.getEmail(), request.getNickname());
        final List<Indvstudylst> indvlist = indvstudylstDao.findByUidAndIsjoin(user.getUid(), 1);
        ResponseEntity<Object> response = null;

        if (indvlist != null) {
            final BasicResponse result = new BasicResponse();
            result.status = true;
            result.data = "가입된 스터디 전체 조회 완료";
            result.object = indvlist;
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        return response;
    }

    @PostMapping("/account/readylist")
    @ApiOperation(value = "가입신청 된 스터디 전체 목록")
    // uid와 joindata 들고오기
    public Object readylist(@Valid @RequestBody final SignupRequest request) {
        final User user = userDao.findUserByEmailAndNickname(request.getEmail(), request.getNickname());
        final List<Indvstudylst> indvlist = indvstudylstDao.findByUidAndIsjoin(user.getUid(), 0);
        ResponseEntity<Object> response = null;

        if (indvlist != null) {
            final BasicResponse result = new BasicResponse();
            result.status = true;
            result.data = "가입신청 된 스터디 전체 목록 조회 완료";
            result.object = indvlist;
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        return response;
    }

    @PostMapping("/account/report")
    @ApiOperation(value = "팀원 신고")
    public Object report(@Valid @RequestBody final ReportRequest request) {

        Report report = new Report();
        report.setPid(request.getPid());
        report.setTarget(request.getTarget());
        report.setReason(request.getReason());

        reportDao.save(report);
        ResponseEntity<Object> response = null;

        final BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = "유저 신고완료"; 
        response = new ResponseEntity<>(result, HttpStatus.OK);

        return response;
    }

}