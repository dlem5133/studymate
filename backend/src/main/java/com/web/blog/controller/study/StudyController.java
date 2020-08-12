package com.web.blog.controller.study;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import com.web.blog.dao.likep.StudylikepDao;
import com.web.blog.dao.mileage.MileageDao;
import com.web.blog.dao.post.ReplyDao;
import com.web.blog.dao.study.GugunDao;
import com.web.blog.dao.study.IndvstudylstDao;
import com.web.blog.dao.study.SidoDao;
import com.web.blog.dao.study.StudyDao;
import com.web.blog.dao.study.StudyTagDao;
import com.web.blog.dao.user.UserDao;
import com.web.blog.model.BasicResponse;

import com.web.blog.model.post.Reply;
import com.web.blog.model.likep.Studylikep;
import com.web.blog.model.mileage.Mileage;
import com.web.blog.model.study.EmpId;
import com.web.blog.model.study.Gugun;
import com.web.blog.model.study.Study;
import com.web.blog.model.study.StudyRequest;
import com.web.blog.model.study.Studytag;
import com.web.blog.model.study.StudytagRequest;
import com.web.blog.model.study.Indvstudylst;
import com.web.blog.model.study.IndvstudylstRequest;
import com.web.blog.model.study.Sido;
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
public class StudyController {

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

    @Autowired
    MileageDao mileageDao;

    @GetMapping("/study/details")
    @ApiOperation(value = "스터디 상세정보")
    // 제목, 작성자, 작성일시, 내용, 좋아요, 카테고리, 목차, 댓글리스트.
    public Object details(@RequestParam(required = true) int pid) {
        Study study = studyDao.findStudyByPid(pid);
        ResponseEntity<Object> response = null;
        List<Reply> reply = replyDao.findReplyByPid(pid);
        List<Studytag> tag = studyTagDao.findStudytagByPid(pid);
        List<Studylikep> studylikep = studylikepDao.findStudylikepByPid(pid);
        User user = userDao.findUserByUid(study.getUid());
        ArrayList<Object> studyAndreply = new ArrayList<>();
        studyAndreply.add(study);
        studyAndreply.add(reply);
        studyAndreply.add(tag);
        studyAndreply.add(studylikep);
        studyAndreply.add(user);
        if (study != null) {
            BasicResponse result = new BasicResponse();
            result.status = true;
            result.data = "스터디 상세 정보 조회 완료";
            result.object = studyAndreply; // tmp가 0이 아니면 댓글도 가져온다

            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        return response;
    }

    @GetMapping("/study/list")
    @ApiOperation(value = "모집 중인 스터디 전체 목록")
    // 전체 글 가져오기
    public Object list() {
        List<Study> studylist = studyDao.findByTmpOrderByPosttimeAsc(1);
        ArrayList<Object> ret = new ArrayList<>();
        for (int i = 0; i < studylist.size(); i++) {
            // 태그 매칭
            studylist.get(i).setLikep(studylikepDao.findStudylikepByPid(studylist.get(i).getPid()).size());
            studyDao.save(studylist.get(i));
            ArrayList<Object> tmp = new ArrayList<>();
            tmp.add(studylist.get(i));
            tmp.add(studyTagDao.findStudytagByPid(studylist.get(i).getPid()));
            ret.add(tmp);
        }
        ResponseEntity<Object> response = null;
        if (studylist != null) {

            BasicResponse result = new BasicResponse();
            result.status = true;
            result.data = "모집 중인 스터디 전체 목록 조회 완료";
            result.object = ret;
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        return response;
    }

    @PostMapping("/study/search")
    @ApiOperation(value = "스터디 카테고리별 검색, 태그 및 타이틀 검색")
    // 포스트 검색기능
    // 닉네임으로 검색시 title null값으로 받음
    // title로 검색시 닉네임 null값
    public Object search(@Valid @RequestBody StudyRequest request) {
        ResponseEntity<Object> response = null;
        BasicResponse result = new BasicResponse();

        if (request.getTmp() == 1) {
            // 타이틀
            List<Study> studylist = studyDao.findStudyByTitleLikeAndCategoryLikeAndTmpOrderByPosttimeAsc(
                    "%" + request.getTitle() + "%", request.getCategory(), 1);
            result.status = true;
            result.data = "스터디명 검색";
            result.object = studylist;

        } else if (request.getTmp() == 2) {
            // 지역
            List<Sido> sido = sidoCodeDao.findBySidonameLike("%" + request.getTitle() + "%");
            ArrayList<Study> studylist = new ArrayList<Study>();

            for (int i = 0; i < sido.size(); i++) {
                List<Study> tmp = studyDao.findStudyBySidocodeOrderByPosttimeAsc(sido.get(i).getSidocode());
                studylist.addAll(tmp);
            }

            List<Gugun> gugun = gugunCodeDao.findByGugunnameLike("%" + request.getTitle() + "%");
            for (int i = 0; i < gugun.size(); i++) {
                List<Study> tmp = studyDao.findStudyBySigungucodeOrderByPosttimeAsc(gugun.get(i).getGuguncode());
                studylist.addAll(tmp);
            }

            result.status = true;
            result.data = "지역 검색";
            result.object = studylist;
        } else if (request.getTmp() == 3) {
            // 태그

            List<Studytag> taglist = studyTagDao.findStudytagByTagnameLike("%" + request.getTitle() + "%");

            ArrayList<Study> responses = new ArrayList<>();
            for (int i = 0; i < taglist.size(); i++) {
                responses.add(studyDao.findStudyByPid(taglist.get(i).getPid()));
            }
            result.status = true;
            result.data = "태그 검색 완료";
            result.object = responses;
        } else {
            List<Study> studylist = studyDao.findStudyByCategoryLikeAndTmpOrderByPosttimeAsc(request.getCategory(), 1);

            result.status = true;
            result.data = "검색할 키워드가 없어 모두 검색 완료";
            result.object = studylist;
        }

        response = new ResponseEntity<>(result, HttpStatus.OK);

        return response;
    }

    @PostMapping("/study/write")
    @ApiOperation(value = "스터디 생성")
    public Object signup(@Valid @RequestBody StudyRequest request) {
        ResponseEntity<Object> response = null;

        List<Study> check_study = studyDao.findStudyByUid(request.getUid());
        int cnt = 0;
        boolean flag = false;
        BasicResponse result = new BasicResponse();

        for (int i = 0; i < check_study.size(); i++) {
            if (check_study.get(i).getPosttime().getMonthValue() == LocalDate.now().getMonthValue()) {
                cnt++;
                if (cnt >= 3) {
                    flag = true;
                }
            }
        }
        if (!flag) {

            Study study = new Study();

            study.setCategory(request.getCategory());
            study.setData(request.getData());
            study.setTitle(request.getTitle());
            study.setUid(request.getUid());
            study.setBackground_image(request.getBackground_image());
            study.setTmp(request.getTmp());
            study.setBindo(request.getBindo());
            study.setStart_date(request.getStart_date());
            study.setEnd_date(request.getEnd_date());
            study.setLikep(0);
            study.setLimitp(request.getLimitp());
            study.setSidocode(request.getSido_code());
            study.setSigungucode(request.getSigungu_code());
            study.setSido(sidoCodeDao.findBySidocode(request.getSido_code()));
            study.setGugun(gugunCodeDao.findByGuguncode(request.getSigungu_code()));
            study.setEvalcount(0);
            Study savedStudy = this.studyDao.save(study);
            int pid = savedStudy.getPid();

            for (int i = 0; i < request.getTag().size(); i++) {
                Studytag tag = new Studytag();
                tag.setPid(pid);
                tag.setStudy(savedStudy);
                tag.setTagname(request.getTag().get(i));
                studyTagDao.save(tag);
            }

            result.status = true;
            result.data = "스터디 생성 완료";
            result.object = savedStudy;

            Indvstudylst Indv = new Indvstudylst();
            User user = userDao.findUserByUid(request.getUid());
            Indv.setIsjoin(1);
            Indv.setIsleader(1);
            Indv.setUid(user.getUid());
            Indv.setPid(savedStudy.getPid());
            Indv.setEmpId(new EmpId(user, savedStudy));
            indvstudylstDao.save(Indv);

            // Studylikep studylikep = new Studylikep();
            // studylikep.setPid(pid);
            // studylikep.
            // studylikepDao.save();

        } else {
            result.status = false;
            result.data = "해당월에 스터디를 더 이상 생성 할 수 없습니다.";
            result.object = null;
        }
        response = new ResponseEntity<>(result, HttpStatus.OK);

        return response;
    }

    @PostMapping("/study/update")
    @ApiOperation(value = "스터디 수정")
    public Object update(@Valid @RequestBody StudyRequest request) {
        // Study find_pid = studyDao.findStudyByPid(request.getPid());
        // 이메일로 id 확인
        ResponseEntity<Object> response = null;
        Study study = new Study();
        // 이미지 업로드 부분

        study.setPid(request.getPid());
        study.setCategory(request.getCategory());
        study.setData(request.getData());
        study.setTitle(request.getTitle());
        study.setUid(request.getUid());
        study.setBackground_image(request.getBackground_image());
        study.setTmp(request.getTmp());
        study.setBindo(request.getBindo());
        study.setStart_date(request.getStart_date());
        study.setEnd_date(request.getEnd_date());
        study.setLimitp(request.getLimitp());
        study.setSidocode(request.getSido_code());
        study.setSigungucode(request.getSigungu_code());
        Study savedStudy = this.studyDao.save(study);
        int pid = savedStudy.getPid();
        List<Studytag> taglist = studyTagDao.findStudytagByPid(pid);
        for (int i = 0; i < taglist.size(); i++) {
            studyTagDao.delete(taglist.get(i));
        }
        if (request.getTag() != null) {

            for (int i = 0; i < request.getTag().size(); i++) {
                Studytag tmptag = new Studytag();
                tmptag.setTagname(request.getTag().get(i));
                tmptag.setPid(pid);
                tmptag.setStudy(savedStudy);
                studyTagDao.save(tmptag);
            }
        }
        BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = "스터디 수정 완료";
        result.object = savedStudy;
        response = new ResponseEntity<>(result, HttpStatus.OK);
        return response;
    }

    @PostMapping("/study/daysupdate")
    @ApiOperation(value = "스터디 요일 수정")
    public Object daysupdate(@Valid @RequestBody StudytagRequest request) {

        ResponseEntity<Object> response = null;
        Study study = new Study();
        study = request.getStudy();
        Study savedStudy = this.studyDao.save(study);
        BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = "스터디 요일 수정 완료";
        result.object = savedStudy;
        response = new ResponseEntity<>(result, HttpStatus.OK);
        return response;
    }

    @PostMapping("/study/delete")
    @ApiOperation(value = "스터디 삭제")
    public Object delete(@Valid @RequestBody StudyRequest request) {
        // 회원 정보 삭제
        Study find_pid = studyDao.findStudyByPid(request.getPid());
        ResponseEntity<Object> response = null;
        BasicResponse result = new BasicResponse();
        studyDao.delete(find_pid);

        result.status = true;
        result.data = "스터디 삭제 완료";
        response = new ResponseEntity<>(result, HttpStatus.OK);

        return response;
    }

    @PostMapping("/study/Srequest")
    @ApiOperation(value = "스터디 신청")
    public Object Srequest(@Valid @RequestBody StudyRequest request) {
        // 닉네임과 pid 만 받아오면됨
        ResponseEntity<Object> response = null;

        Study study = studyDao.findStudyByPid(request.getPid());
        BasicResponse result = new BasicResponse();

        Indvstudylst Indv = new Indvstudylst();
        User user = userDao.findUserByUid(request.getUid());
        Indv.setIsjoin(0);
        Indv.setIsleader(0);
        Indv.setUid(user.getUid());
        Indv.setPid(study.getPid());
        Indv.setEmpId(new EmpId(user, study));
        Indvstudylst saveIndv = indvstudylstDao.save(Indv);

        result.status = true;
        result.data = "스터디 신청 완료";
        result.object = saveIndv;

        response = new ResponseEntity<>(result, HttpStatus.OK);

        return response;
    }

    @PostMapping("/study/approval")
    @ApiOperation(value = "스터디원 승인")
    // 스터디원 승인
    // pid and uid 승인
    public Object approval(@Valid @RequestBody IndvstudylstRequest request) {

        ResponseEntity<Object> response = null;

        Study study = studyDao.findStudyByPid(request.getPid());
        User user = userDao.findUserByUid(request.getUid());
        BasicResponse result = new BasicResponse();
        study.setMemnum(study.getMemnum() + 1);
        studyDao.save(study);
        Indvstudylst Indv = new Indvstudylst();
        Indv.setIsjoin(1);
        Indv.setIsleader(0);
        Indv.setUid(request.getUid());
        Indv.setPid(request.getPid());
        Indv.setEmpId(new EmpId(user, study));
        Indvstudylst saveIndv = indvstudylstDao.save(Indv);

        result.status = true;
        result.data = "스터디원 승인 완료";
        result.object = saveIndv;

        response = new ResponseEntity<>(result, HttpStatus.OK);

        return response;
    }
    // 댓글기능

    @PostMapping("/study/cancel")
    @ApiOperation(value = "스터디 신청 취소")
    // 스터디 신청 취소
    // pid and uid 취소
    public Object cancel(@Valid @RequestBody IndvstudylstRequest request) {

        ResponseEntity<Object> response = null;

        BasicResponse result = new BasicResponse();
        Indvstudylst Indv = indvstudylstDao.findByPidAndUid(request.getPid(), request.getUid());
        indvstudylstDao.delete(Indv);

        result.status = true;
        result.data = "스터디 신청 취소 완료";

        response = new ResponseEntity<>(result, HttpStatus.OK);

        return response;
    }

    @PostMapping("/study/requestlist")
    @ApiOperation(value = "스터디 신청 리스트")
    // 스터디 신청 취소
    // pid and uid 취소
    public Object requestlist(@Valid @RequestBody IndvstudylstRequest request) {

        ResponseEntity<Object> response = null;
        BasicResponse result = new BasicResponse();
        List<Indvstudylst> Indv = indvstudylstDao.findByPidAndIsjoin(request.getPid(), 0);
        result.status = true;
        result.data = "스터디 신청 리스트 조회 완료";
        result.object = Indv;
        response = new ResponseEntity<>(result, HttpStatus.OK);

        return response;
    }

    @PostMapping("/study/memberlist")
    @ApiOperation(value = "스터디 멤버 리스트")
    // 스터디 신청 취소
    // pid and uid 취소
    public Object memberlist(@Valid @RequestBody IndvstudylstRequest request) {

        ResponseEntity<Object> response = null;
        BasicResponse result = new BasicResponse();
        List<Indvstudylst> Indv = indvstudylstDao.findByPidAndIsjoin(request.getPid(), 1);
        List<Indvstudylst> Indv2 = indvstudylstDao.findByPidAndIsjoin(request.getPid(), 2);
        Indv.addAll(Indv2);
        result.status = true;
        result.data = "스터디 멤버 리스트 조회 완료";
        result.object = Indv;

        response = new ResponseEntity<>(result, HttpStatus.OK);

        return response;
    }

    @PostMapping("/study/getsidocode")
    @ApiOperation(value = "시도코드데이터 얻기")
    public Object getSido() {

        List<Sido> sido = sidoCodeDao.findAll();

        ResponseEntity<Object> response = null;
        BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = "시도코드데이터 얻기 완료";
        result.object = sido;

        response = new ResponseEntity<>(result, HttpStatus.OK);

        return response;
    }

    @GetMapping("/study/getguguncode")
    @ApiOperation(value = "구군코드데이터 얻기")
    public Object getGugun(@RequestParam(required = true) String sidocode) {
        List<Gugun> gugun = gugunCodeDao.findBySidocode(sidocode);

        ResponseEntity<Object> response = null;
        BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = "구군코드데이터 얻기 완료";
        result.object = gugun;

        response = new ResponseEntity<>(result, HttpStatus.OK);

        return response;
    }

    @GetMapping("/study/recommend")
    @ApiOperation(value = "추천 스터디 목록")
    // 전체 글 가져오기
    public Object recommend() {
        List<Study> studylist = studyDao.findTop10ByTmpOrderByLikepDesc(1);
        ArrayList<Object> ret = new ArrayList<>();

        for (int i = 0; i < studylist.size(); i++) {
            // 태그 매칭
            studylist.get(i).setLikep(studylikepDao.findStudylikepByPid(studylist.get(i).getPid()).size());
            studyDao.save(studylist.get(i));
            ArrayList<Object> tmp = new ArrayList<>();
            tmp.add(studylist.get(i));
            tmp.add(studyTagDao.findStudytagByPid(studylist.get(i).getPid()));
            ret.add(tmp);
        }
        ResponseEntity<Object> response = null;
        if (studylist != null) {
            BasicResponse result = new BasicResponse();
            result.status = true;
            result.data = "추천 스터디 목록 조회 완료";
            result.object = ret;
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        return response;
    }

    @GetMapping("/study/recruitstart")
    @ApiOperation(value = "모집 시작")
    // 전체 글 가져오기
    public Object recruitstart(@RequestParam(required = true) int pid) {
        Study study = studyDao.findStudyByPid(pid);
        study.setTmp(1);
        Study savedstudy = studyDao.save(study);
        ResponseEntity<Object> response = null;
        if (study != null) {
            BasicResponse result = new BasicResponse();
            result.status = true;
            result.data = "모집 시작 완료";
            result.object = savedstudy;
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        return response;
    }

    @GetMapping("/study/recruitstop")
    @ApiOperation(value = "모집 중지")

    public Object recruitstop(@RequestParam(required = true) int pid) {
        Study study = studyDao.findStudyByPid(pid);
        study.setTmp(0);
        Study savedstudy = studyDao.save(study);
        ResponseEntity<Object> response = null;
        if (study != null) {
            BasicResponse result = new BasicResponse();
            result.status = true;
            result.data = "모집 중지 완료";
            result.object = savedstudy;
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        return response;
    }

    @GetMapping("/study/finish")
    @ApiOperation(value = "스터디 종료")
    public Object finish(@RequestParam(required = true) int pid) {
        Study study = studyDao.findStudyByPid(pid);
        study.setTmp(2);
        Study savedstudy = studyDao.save(study);
        List<Indvstudylst> indvstudylsts = indvstudylstDao.findByPid(pid);
        for (int i = 0; i < indvstudylsts.size(); i++) {
            User user = indvstudylsts.get(i).getEmpId().getUser();
            Mileage mileage = mileageDao.findByUid(indvstudylsts.get(i).getUid());

            mileage.setTotal(mileage.getTotal() + 200);
            mileage.setEndpoint(mileage.getEndpoint() + 1);
            mileageDao.save(mileage);
            userDao.save(user);
        }
        ResponseEntity<Object> response = null;
        if (study != null) {
            BasicResponse result = new BasicResponse();
            result.status = true;
            result.data = "스터디 종료";
            result.object = savedstudy;
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        return response;
    }

}