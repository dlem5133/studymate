package com.web.blog.controller.account;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.validation.Valid;

import com.web.blog.dao.mileage.MileageDao;
import com.web.blog.dao.study.IndvstudylstDao;
import com.web.blog.dao.study.StudyDao;
import com.web.blog.dao.user.ReportDao;
import com.web.blog.dao.user.UserDao;
import com.web.blog.mail.MailHandler;
import com.web.blog.model.BasicResponse;
import com.web.blog.model.mileage.Mileage;
import com.web.blog.model.study.Study;
import com.web.blog.model.study.StudyRequest;
import com.web.blog.model.study.Indvstudylst;
import com.web.blog.model.user.Report;
import com.web.blog.model.user.ReportRequest;
import com.web.blog.model.user.SignupRequest;
import com.web.blog.model.user.User;
import com.web.blog.service.JwtService;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
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

@ApiResponses(value = {
    @ApiResponse(code = 401, message = "Unauthorized", response = BasicResponse.class),
    @ApiResponse(code = 403, message = "Forbidden", response = BasicResponse.class),
    @ApiResponse(code = 404, message = "Not Found", response = BasicResponse.class),
    @ApiResponse(code = 500, message = "Failure", response = BasicResponse.class)
})

@CrossOrigin(origins = {
    "*"
})
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

    @Autowired
    MileageDao mileageDao;

    @Autowired
    private KakaoAPI kakao;

    @GetMapping(value = "/k/klogin")
    public Object lgin() {

        String URL = "https://kauth.kakao.com/oauth/authorize?client_id=a61b27fc4e535f7a22983d0d0da6eb9d&redirect_uri=http://i3b205.p.ssafy.io:8080/account/kakaologin&response_type=code";
        ResponseEntity < Object > response = null;
        final BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = "success";
        result.object = URL;
        response = new ResponseEntity < > (result, HttpStatus.OK);
        return response;
    }

    @GetMapping(value = "/account/kakaologin")
    public Object kakaologin(@RequestParam("code") final String code) {

        final String access_Token = kakao.getAccessToken(code);

        // accesstoken?????? ??????????????? ????????? ??? ??????.
        final HashMap < String, String > userInfo = kakao.getUserInfo(access_Token);

        User userOpt;

        if (userInfo.get("email") == null) {
            // ????????? ?????? ????????? ?????? ??? ??????
            // front?????? alert????????? ??????????????? ???????????? ????????????

            return new RedirectView("http://i3b205.p.ssafy.io:8081/#/duplicate");
        }

        // ??? ?????? ???????????? ??????.
        // ?????????, DB??? ?????? ????????? ?????? ??? ????????? ????????? ????????? ???????????? ??????.
        else {
            userOpt = userDao.findUserByNicknameAndEmail(userInfo.get("nickname").toString(),
                userInfo.get("email").toString());
            // ????????? ??????????????? ?????? ?????? <a> ???????????? ???????????? ??????????????? ????????????
            /*
             * https://kauth.kakao.com/oauth/authorize?client_id=
             * a61b27fc4e535f7a22983d0d0da6eb9d&redirect_uri=http://localhost:8080/account/
             * kakaologin&response_type=code
             */

            String user_email = userInfo.get("email").toString();
            String user_name = userInfo.get("nickname");

            if (userOpt != null) {
                // ?????? ?????? ?????????, ???????????? ????????????, front?????? alert????????? ??????????????? ????????? ????????????
                return new RedirectView("http://i3b205.p.ssafy.io:8081/#/duplicate");
            } else {

                // ???????????? ????????? ??????
                String namee = URLEncoder.encode(user_name, StandardCharsets.UTF_8);

                String url = "http://i3b205.p.ssafy.io:8081/#/user/signup?email=" + user_email + "&nickname=" + namee + "&pass=" +
                    "A!Hvcidfndkl@RDUWCanklcn3$!nvidh893bqtejfdA*Rdwasc";
                return new RedirectView(url);

            }
        }
    }

    @GetMapping("/account/login")
    @ApiOperation(value = "?????????")
    public Object login(@RequestParam(required = true) final String email,
        @RequestParam(required = true) final String password) {
        User userOpt = new User();
        userOpt = userDao.findUserByEmailAndPassword(email, password);
        // https://kauth.kakao.com/oauth/authorize?client_id=a61b27fc4e535f7a22983d0d0da6eb9d&redirect_uri=http://localhost:8080/account/loginn&response_type=code
        // ????????? ??????????????? ?????? ?????? <a> ???????????? ???????????? ??????????????? ????????????
        ResponseEntity < Object > response = null;

        String token = jwtService.createLoginToken(userOpt);

        if (userOpt.getPassword().equals(password)) {
            final BasicResponse result = new BasicResponse();
            result.status = true;
            result.data = "????????? ??????";
            result.object = token;
            response = new ResponseEntity < > (result, HttpStatus.OK);
        } else {
            response = new ResponseEntity < > (null, HttpStatus.NOT_FOUND);
        }
        return response;
    }

    @PostMapping("/account/signup")
    @ApiOperation(value = "????????????")
    public Object signup(@Valid @RequestBody final SignupRequest request) {
        // ?????????, ????????? ???????????? ??????
        // ?????????????????? ????????? ?????????.
        final User email_test = userDao.getUserByEmail(request.getEmail());
        final User nickname_test = userDao.getUserByNickname(request.getNickname());
        ResponseEntity < Object > response = null;
        if (email_test != null) {
            final BasicResponse result = new BasicResponse();
            result.status = false;
            result.data = "????????? ??????";
            response = new ResponseEntity < > (result, HttpStatus.OK);
        } else if (nickname_test != null) {
            final BasicResponse result = new BasicResponse();
            result.status = false;
            result.data = "????????? ??????";
            response = new ResponseEntity < > (result, HttpStatus.OK);
        } else {
            final User user = new User();
            user.setEmail(request.getEmail());
            user.setPassword(request.getPassword());
            user.setNickname(request.getNickname());
            user.setIntro(request.getIntro());
            user.setProfile_image(request.getProfile_image());
            user.setMileage(1000);
            user.setPenalty(0);
            user.setTotalscoreper(0);
            User saveduser = this.userDao.save(user);

            Mileage mileage = new Mileage();
            mileage.setUid(saveduser.getUid());
            mileage.setUser(saveduser);
            mileage.setDiarypoint(0);
            mileage.setEndpoint(0);
            mileage.setEvalpoint(0);
            
            mileage.setTotal(1000);
            mileageDao.save(mileage);

            final BasicResponse result = new BasicResponse();
            result.status = true;
            result.data = "???????????? ??????";

            try {
                final String memberMail = request.getEmail();
                final MailHandler mail = new MailHandler(mailSender);
                mail.setFrom("ssafystudy@gmail.com", "??????????????????");
                mail.setTo(memberMail);
                mail.setSubject("STUDYMATE ???????????? ?????? ??????");
                mail.setText(new StringBuffer().append("<h1>STUDYMATE ???????????? ????????? ???????????????!!</h1>").toString());
                mail.send();
                result.data = "?????? ?????? ??????";
            } catch (final Exception e) {
                e.printStackTrace();
                result.data = "?????? ?????? ??????";
            }

            response = new ResponseEntity < > (result, HttpStatus.OK);
        }
        return response;
    }

    @PostMapping("/account/update")
    @ApiOperation(value = "??????")
    public Object update(@Valid @RequestBody final SignupRequest request) {
        // ?????? ?????? ??????
        User user = userDao.findByEmailAndUid(request.getEmail(), request.getUid());
        // ???????????? id ??????
        ResponseEntity < Object > response = null;
        // final User user = new User();
        // user.setUid(request.getUid());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setNickname(request.getNickname());
        user.setIntro(request.getIntro());
        user.setProfile_image(request.getProfile_image());

        // ????????? ????????? ??????
        this.userDao.save(user);
        String token = jwtService.createLoginToken(user);

        final BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = "?????? ?????? ??????";
        result.object = token;
        response = new ResponseEntity < > (result, HttpStatus.OK);
        return response;
    }

    @PostMapping("/account/delete")
    @ApiOperation(value = "?????? ??? ??????")
    public Object delete(@Valid @RequestBody final SignupRequest request) {
        // ?????? ?????? ??????
        // ???????????? ??????
        User user = userDao.findUserByEmailAndPassword(request.getEmail(), request.getPassword());

        ResponseEntity < Object > response = null;
        final BasicResponse result = new BasicResponse();
        List < Study > studylist = studyDao.findStudyByUid(user.getUid());

        // ????????? ?????? ??????
        for (int i = 0; i < studylist.size(); i++) {
            List < Indvstudylst > indvstudylsts = indvstudylstDao.findByPidAndIsjoin(studylist.get(i).getPid(),1);
            if (indvstudylsts.size() == 1) {
                System.out.println(studylist.get(i));
                studyDao.delete(studylist.get(i));
            } else {
                // indv ?????? ??????
                for (int j = 0; j < indvstudylsts.size(); j++) {
                    Indvstudylst tmp = indvstudylsts.get(j);
                    if(tmp.getUid()!=user.getUid())
                    {
                        tmp.setIsleader(1);
                        indvstudylstDao.save(tmp);
                        studylist.get(i).setMemnum(studylist.get(i).getMemnum() - 1);
                        studylist.get(i).setUid(tmp.getUid());
                        studylist.get(i).setUser(tmp.getEmpId().getUser());
                        studyDao.save(studylist.get(i));
                        break;
                    }
                }
                // study uid ??????
            }
        }
        userDao.delete(user);
        result.status = true;
        result.data = "?????? ?????? ??????";

        response = new ResponseEntity < > (result, HttpStatus.OK);
        return response;
    }

    @GetMapping("/account/findPassword")
    @ApiOperation(value = "???????????? ??????")
    public Object findPassword(@RequestParam(required = true) final String email,
        @RequestParam(required = true) final String nickname) {

        final User userforPassword = userDao.findUserByEmailAndNickname(email, nickname);
        ResponseEntity < ? > response = null;

        if (!userforPassword.getPassword().equals("")) {
            final BasicResponse result = new BasicResponse();
            result.status = true;
            result.data = "success";
            try {
                final String memberMail = userforPassword.getEmail();
                final MailHandler mail = new MailHandler(mailSender);
                mail.setFrom("ssafystudy@gmail.com", "??????????????????");
                mail.setTo(memberMail);
                mail.setSubject("STUDYMATE ???????????? ?????? ??????");
                mail.setText(new StringBuffer().append("<h1> ???????????? ??????????????? \"").append(userforPassword.getPassword())
                    .append("\"????????? <h1>").toString());
                mail.send();
                result.data = "???????????? ?????? ?????? ?????? ??????";
            } catch (final Exception e) {
                e.printStackTrace();
                result.data = "???????????? ?????? ?????? ?????? ??????";
            }
            response = new ResponseEntity < > (result, HttpStatus.OK);
        } else {
            response = new ResponseEntity < > ("null", HttpStatus.NOT_FOUND);
        }

        return response;
    }

    @GetMapping("/account/profile")
    @ApiOperation(value = "?????? ?????????")
    public Object profile(@RequestParam(required = true) final String Token) { // ?????? ?????? ??????
        // ???????????? ??????
        ArrayList < Object > userAndStudy = new ArrayList < > ();
        User user2 = (jwtService.getUser(Token));
        User user = userDao.findUserByEmailAndPassword(user2.getEmail(), user2.getPassword());
        userAndStudy.add(user);
        userAndStudy.add(studyDao.findAll());

        ResponseEntity < Object > response = null;
        final BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = "?????? ????????? ?????? ??????";
        result.object = user;

        response = new ResponseEntity < > (result, HttpStatus.OK);
        return response;
    }

    @GetMapping("/account/memprofile")
    @ApiOperation(value = "?????? ?????????")
    public Object memprofile(@RequestParam(required = true) final Integer uid) {
        // ?????? ?????? ??????
        User user = userDao.findUserByUid(uid);
        ResponseEntity < Object > response = null;
        final BasicResponse result = new BasicResponse();

        result.status = true;
        result.data = "?????? ????????? ?????? ??????";
        result.object = user;

        response = new ResponseEntity < > (result, HttpStatus.OK);
        return response;
    }

    @PostMapping("/account/tmplist")
    @ApiOperation(value = "??????????????? ????????? ?????? ??????")
    // ??????????????? ???????????? ????????? ????????????
    public Object tmplist(@Valid @RequestBody final StudyRequest request) {
        final List < Study > studylist = studyDao.findByTmpAndUid(0, request.getUid());
        ResponseEntity < Object > response = null;

        if (studylist != null) {
            final BasicResponse result = new BasicResponse();
            result.status = true;
            result.data = "??????????????? ????????? ?????? ?????? ??????";
            result.object = studylist;
            response = new ResponseEntity < > (result, HttpStatus.OK);
        } else {
            response = new ResponseEntity < > (null, HttpStatus.NOT_FOUND);
        }

        return response;
    }

    @PostMapping("/account/studylist")
    @ApiOperation(value = "????????? ????????? ?????? ??????")
    // uid??? studylist ????????????
    public Object studylist(@Valid @RequestBody final SignupRequest request) {
        final User user = userDao.findUserByEmailAndNickname(request.getEmail(), request.getNickname());
        final List < Indvstudylst > indvlist = indvstudylstDao.findByUidAndIsjoin(user.getUid(), 1);
        ResponseEntity < Object > response = null;

        if (indvlist != null) {
            final BasicResponse result = new BasicResponse();
            result.status = true;
            result.data = "????????? ????????? ?????? ?????? ??????";
            result.object = indvlist;
            response = new ResponseEntity < > (result, HttpStatus.OK);
        } else {
            response = new ResponseEntity < > (null, HttpStatus.NOT_FOUND);
        }

        return response;
    }

    @PostMapping("/account/readylist")
    @ApiOperation(value = "???????????? ??? ????????? ?????? ??????")
    // uid??? joindata ????????????
    public Object readylist(@Valid @RequestBody final SignupRequest request) {
        final User user = userDao.findUserByEmailAndNickname(request.getEmail(), request.getNickname());
        final List < Indvstudylst > indvlist = indvstudylstDao.findByUidAndIsjoin(user.getUid(), 0);
        ResponseEntity < Object > response = null;

        if (indvlist != null) {
            final BasicResponse result = new BasicResponse();
            result.status = true;
            result.data = "???????????? ??? ????????? ?????? ?????? ?????? ??????";
            result.object = indvlist;
            response = new ResponseEntity < > (result, HttpStatus.OK);
        } else {
            response = new ResponseEntity < > (null, HttpStatus.NOT_FOUND);
        }

        return response;
    }

    @PostMapping("/account/report")
    @ApiOperation(value = "?????? ??????")
    public Object report(@Valid @RequestBody final ReportRequest request) {

        Report report_check = reportDao.findReportByPidAndReporterAndTarget(request.getPid(), request.getReporter(),
            request.getTarget());
        ResponseEntity < Object > response = null;
        final BasicResponse result = new BasicResponse();

        if (report_check != null) {
            result.status = false;
            result.data = "?????? ????????? ?????? ?????????.";
            response = new ResponseEntity < > (result, HttpStatus.OK);
        } else {
            Report report = new Report();
            report.setPid(request.getPid());
            report.setTarget(request.getTarget());
            report.setReason(request.getReason());
            report.setReporter(request.getReporter());

            reportDao.save(report);

            result.status = true;
            result.data = "?????? ????????????";
            response = new ResponseEntity < > (result, HttpStatus.OK);
        }

        return response;
    }

    @PostMapping("/account/reportcheck")
    @ApiOperation(value = "?????? ??????")
    public Object reportcheck(@Valid @RequestBody final ReportRequest request) {

        Report report_check = reportDao.findReportByPidAndReporterAndTarget(request.getPid(), request.getReporter(),
            request.getTarget());
        ResponseEntity < Object > response = null;
        final BasicResponse result = new BasicResponse();

        if (report_check != null) {
            result.status = false;
            result.data = "?????? ????????? ?????? ?????????.";
            response = new ResponseEntity < > (result, HttpStatus.OK);
        } else {
            result.status = true;
            result.data = "?????? ?????? ??????";
            response = new ResponseEntity < > (result, HttpStatus.OK);
        }

        return response;
    }
    
    
    @PostMapping("/account/reportlist")
    @ApiOperation(value = "?????? ?????? ??????")
    public Object reportlist(@RequestParam(required = true) int pid) {

        ResponseEntity<Object> response = null;

        final List<Report> reportList = reportDao.findByPid(pid);

        final BasicResponse result = new BasicResponse();

        result.status = true;
        result.data = "???????????? ??????";
        result.object = reportList;

        response = new ResponseEntity<>(result, HttpStatus.OK);

        return response;
    }

}