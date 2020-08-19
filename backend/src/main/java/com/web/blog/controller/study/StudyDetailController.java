package com.web.blog.controller.study;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.web.blog.dao.likep.StudylikepDao;
import com.web.blog.dao.post.ReplyDao;
import com.web.blog.dao.study.GugunDao;
import com.web.blog.dao.study.IndvstudylstDao;
import com.web.blog.dao.study.SidoDao;
import com.web.blog.dao.study.StudyDao;
import com.web.blog.dao.study.StudyTagDao;
import com.web.blog.dao.user.UserDao;
import com.web.blog.model.BasicResponse;

import com.web.blog.model.study.Study;
import com.web.blog.model.study.Indvstudylst;
import com.web.blog.model.study.IndvstudylstRequest;
import com.web.blog.model.user.DelegationRequest;
import com.web.blog.model.user.User;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.beans.factory.annotation.Autowired;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@ApiResponses(value = {
    @ApiResponse(code = 401, message = "Unauthorized", response = BasicResponse.class),
    @ApiResponse(code = 403, message = "Forbidden", response = BasicResponse.class),
    @ApiResponse(code = 404, message = "Not Found", response = BasicResponse.class),
    @ApiResponse(code = 500, message = "Failure", response = BasicResponse.class)
})

//@CrossOrigin(origins = { "http://i3b205.p.ssafy.io" })
@CrossOrigin(origins = {
    "*"
})

@RestController
public class StudyDetailController {

    @Autowired
    StudyDao studyDao;

    @Autowired
    ReplyDao replyDao;


    @Autowired
    UserDao userDao;

    @Autowired
    StudylikepDao studylikepDao;

    @Autowired
    IndvstudylstDao indvstudylstDao;

    @Autowired
    StudyTagDao studyTagDao;

    @Autowired
    SidoDao sidoCodeDao;

    @Autowired
    GugunDao gugunCodeDao;

    @PostMapping("/study/detail/delete_request")
    @ApiOperation(value = "스터디 탈퇴 신청")
    public Object delete_request(@Valid @RequestBody IndvstudylstRequest request) {
        Indvstudylst indv = indvstudylstDao.findByPidAndUid(request.getPid(), request.getUid());
        indv.setIsjoin(2);
        Indvstudylst saved_indv = indvstudylstDao.save(indv);
        ResponseEntity < Object > response = null;

        if (saved_indv != null) {
            BasicResponse result = new BasicResponse();
            result.status = true;
            result.data = "스터디 탈퇴 신청 완료";
            result.object = saved_indv;
            response = new ResponseEntity < > (result, HttpStatus.OK);
        } else {
            response = new ResponseEntity < > (null, HttpStatus.NOT_FOUND);
        }

        return response;
    }

    @PostMapping("/study/detail/delete_cancel")
    @ApiOperation(value = "스터디 탈퇴 취소")
    public Object delete_cancel(@Valid @RequestBody IndvstudylstRequest request) {
        Indvstudylst indv = indvstudylstDao.findByPidAndUid(request.getPid(), request.getUid());
        indv.setIsjoin(1);
        Indvstudylst saved_indv = indvstudylstDao.save(indv);
        ResponseEntity < Object > response = null;

        if (saved_indv != null) {
            BasicResponse result = new BasicResponse();
            result.status = true;
            result.data = "스터디 탈퇴 취소";
            result.object = saved_indv;
            response = new ResponseEntity < > (result, HttpStatus.OK);
        } else {
            response = new ResponseEntity < > (null, HttpStatus.NOT_FOUND);
        }

        return response;
    }

    @GetMapping("/study/detail/deleteList")
    @ApiOperation(value = "회원탈퇴 대기 목록")
    public Object deleteList(@RequestParam(required = true) int pid) {
        List < Indvstudylst > indv_list = indvstudylstDao.findByPidAndIsjoin(pid, 2);
        ArrayList < User > user_list = new ArrayList < > ();

        for (int i = 0; i < indv_list.size(); i++) {
            // 탈퇴 대기중인 유저 목록
            user_list.add(indv_list.get(i).getEmpId().getUser());
        }
        ResponseEntity < Object > response = null;

        BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = "회원탈퇴 대기 목록 조회 완료";
        result.object = user_list;
        response = new ResponseEntity < > (result, HttpStatus.OK);

        return response;
    }

    @PostMapping("/study/detail/delete_apply")
    @ApiOperation(value = "스터디 탈퇴 승인")
    public Object delete_apply(@Valid @RequestBody IndvstudylstRequest request) {
        Indvstudylst indv = new Indvstudylst();
        indv = indvstudylstDao.findByPidAndUid(request.getPid(), request.getUid());

        ResponseEntity < Object > response = null;

        BasicResponse result = new BasicResponse();
        if (indv.getIsjoin() == 2) {
            Study study = indv.getEmpId().getStudy();
            study.setMemnum(study.getMemnum() - 1);
            indvstudylstDao.delete(indv);

            result.status = true;
            result.data = "스터디 탈퇴 승인";
            response = new ResponseEntity < > (result, HttpStatus.OK);
        } else {
            result.status = false;
            result.data = "탈퇴 신청하지 않은 사용자 입니다";
            response = new ResponseEntity < > (result, HttpStatus.OK);
        }

        return response;
    }

    @PostMapping("/study/detail/delete_companion")
    @ApiOperation(value = "스터디 탈퇴 반려")
    public Object delete_companion(@Valid @RequestBody IndvstudylstRequest request) {
        Indvstudylst indv = indvstudylstDao.findByPidAndUid(request.getPid(), request.getUid());
        indv.setIsjoin(1);
        Indvstudylst saved_indv = indvstudylstDao.save(indv);
        ResponseEntity < Object > response = null;

        if (saved_indv != null) {
            BasicResponse result = new BasicResponse();
            result.status = true;
            result.data = "스터디 탈퇴 반려";
            result.object = saved_indv;
            response = new ResponseEntity < > (result, HttpStatus.OK);
        } else {
            response = new ResponseEntity < > (null, HttpStatus.NOT_FOUND);
        }

        return response;
    }

    @PostMapping("/study/detail/delegation")
    @ApiOperation(value = "스터디 위임")
    public Object delegation(@Valid @RequestBody DelegationRequest request) {
        Indvstudylst leader_indv = indvstudylstDao.findByPidAndUid(request.getPid(), request.getLeader());
        Indvstudylst member_indv = indvstudylstDao.findByPidAndUid(request.getPid(), request.getMember());
        Study study = studyDao.findStudyByPid(request.getPid());

        leader_indv.setIsleader(0);
        member_indv.setIsleader(1);
        study.setUid(request.getMember());
        study.setUser(userDao.findUserByUid(request.getMember()));
        indvstudylstDao.save(leader_indv);
        indvstudylstDao.save(member_indv);
        studyDao.save(study);

        ResponseEntity < Object > response = null;
        if (member_indv != null) {
            BasicResponse result = new BasicResponse();
            result.status = true;
            result.data = "스터디 위임";
            result.object = member_indv.getEmpId().getUser().getNickname();
            response = new ResponseEntity < > (result, HttpStatus.OK);
        } else {
            response = new ResponseEntity < > (null, HttpStatus.NOT_FOUND);
        }
        return response;
    }

    @PostMapping("/study/detail/fileupload")
    @ResponseBody
    @RequestMapping(value = "/study/detail/fileupload", headers = "Content-Type= multipart/form-data", method = RequestMethod.POST) @ApiOperation(value = "파일업로드")
    public Object fileupload(
        HttpServletRequest request, @RequestParam(value = "pid", required = true) Integer pid, @RequestParam(value = "file", required = true) MultipartFile file)
    throws IllegalStateException, IOException {
        FileOutputStream fos;
        byte fileData[] = file.getBytes();
        File ff = new File("frontend\\src\\assets\\uploadfile\\" + pid+file.getOriginalFilename());
        if (!ff.getParentFile().exists())
            ff.getParentFile().mkdirs();
        // System.out.println(ff.getParentFile().getAbsolutePath());
        String mainpath = ff.getPath();
        // System.out.println(mainpath);
        String path = file.getOriginalFilename();
        fos = new FileOutputStream("frontend\\src\\assets\\uploadfile\\" + pid+file.getOriginalFilename());
        fos.write(fileData);
        //        file.transferTo(ff);
        // System.out.println("file is ");
        // System.out.println("name is " + file.getName());
        fos.close();
        return file.getName();}



    //     // FileOutputStream fos;
    //     // byte fileData[] = file.getBytes();
    //     // String path = "../src/main/resources/img/";
    //     // String root_path = request.getContextPath();
    //     // System.out.println(root_path);
    //     // System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
    //     // String uploadPath =root_path + "resources\\img\\";
    //     // fos = new FileOutputStream(uploadPath + file.getOriginalFilename());
    //     // fos.write(fileData);


    //     // ResponseEntity < Object > response = null;
    //     // BasicResponse result = new BasicResponse();


    //     // //file.transferTo(new File("c:/test/" + file.getOriginalFilename()));
    //     // result.status = true;
    //     // result.data = "파일업로드";
    //     // result.object = file;




    //     // response = new ResponseEntity < > (result, HttpStatus.OK);

    //     // return response;
    // }

    // @GetMapping("/study/detail/urlload")
    // @ApiOperation(value = "url")
    // public Object urlLoad() {
    //     List < Image > image_list = imageDao.findAll();
    //     ResponseEntity < Object > response = null;
    //     BasicResponse result = new BasicResponse();
    //     result.status = true;
    //     result.data = "url 목록";
    //     result.object = image_list;
    //     response = new ResponseEntity < > (result, HttpStatus.OK);

    //     return response;
    // }
}