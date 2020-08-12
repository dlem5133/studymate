package com.web.blog.controller.study;

import java.util.List;

import javax.validation.Valid;

import com.web.blog.dao.post.ReplyDao;
import com.web.blog.dao.study.IndvstudylstDao;
import com.web.blog.dao.study.StudyBoardDao;
import com.web.blog.dao.study.StudyDao;
import com.web.blog.dao.user.UserDao;
import com.web.blog.model.BasicResponse;
import com.web.blog.model.study.Board;
import com.web.blog.model.study.BoardRequest;
import com.web.blog.model.study.Study;
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

@CrossOrigin(origins = { "http://localhost:3000" })
@RestController
public class StudyBoardController {

    @Autowired
    StudyBoardDao studyBoardDao;

    @Autowired
    StudyDao studyDao;

    @Autowired
    ReplyDao replyDao;

    @Autowired
    UserDao userDao;

    @Autowired
    IndvstudylstDao indvstudylstDao;

    @GetMapping("/study/board/list")
    @ApiOperation(value = "스터디 게시판 전체 목록")
    // 전체 글 가져오기
    public Object list(@RequestParam(required = true) final int pid) {
        List<Board> board = studyBoardDao.findBoardByPid(pid);
        ResponseEntity<Object> response = null;

        if (board != null) {
            final BasicResponse result = new BasicResponse();
            result.status = true;
            result.data = "스터디 게시판 전체 목록 조회 완료";
            result.object = board;
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        return response;
    }

    @GetMapping("/study/board/details")
    @ApiOperation(value = "스터디 게시판 상세정보")
    // 제목, 작성자, 작성일시, 내용, 좋아요, 카테고리, 목차, 댓글리스트.
    public Object details(@RequestParam(required = true) final int bid) {
        final Board board = studyBoardDao.findBoardByBid(bid);
        ResponseEntity<Object> response = null;

        if (board != null) {
            final BasicResponse result = new BasicResponse();
            result.status = true;
            result.data = "스터디 게시판 상세정보 조회 완료";
            result.object = board;
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return response;
    }

    @PostMapping("/study/board/write")
    @ApiOperation(value = "포스트 생성")
    public Object signup(@Valid @RequestBody final BoardRequest request) {

        ResponseEntity<Object> response = null;
        final Board board = new Board();
        final Study study = studyDao.findStudyByPid(request.getPid());
        final User user = userDao.findUserByUid(request.getUid());

        board.setBody(request.getBody());
        board.setIsnotice(request.getIsnotice());
        board.setPid(request.getPid());
        board.setTitle(request.getTitle());
        board.setUid(request.getUid());
        board.setStudy(study);
        board.setUser(user);

        final Board savedBoard = this.studyBoardDao.save(board);
        final BasicResponse result = new BasicResponse();

        result.status = true;
        result.data = "success";
        result.object = savedBoard;

        response = new ResponseEntity<>(result, HttpStatus.OK);

        return response;
    }

    @PostMapping("/study/board/update")
    @ApiOperation(value = "스터디 게시판 글 수정")
    public Object update(@Valid @RequestBody final BoardRequest request) {

        ResponseEntity<Object> response = null;
        final Board board = new Board();
        final User user = userDao.findUserByUid(request.getUid());
        final Study study = studyDao.findStudyByPid(request.getPid());
        board.setUser(user);
        board.setStudy(study);
        board.setBid(request.getBid());
        board.setBody(request.getBody());
        board.setIsnotice(request.getIsnotice());
        board.setPid(request.getPid());
        board.setTitle(request.getTitle());
        board.setUid(request.getUid());

        Board updateBoard = this.studyBoardDao.save(board);

        final BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = "스터디 게시판 글 수정 완료";
        result.object = updateBoard;
        response = new ResponseEntity<>(result, HttpStatus.OK);

        return response;
    }

    @PostMapping("/study/board/delete")
    @ApiOperation(value = "스터디 게시판 글 삭제")
    public Object delete(@Valid @RequestBody final BoardRequest request) {
        // 회원 정보 삭제
        final Board find_bid = studyBoardDao.findBoardByBid(request.getBid());
        ResponseEntity<Object> response = null;
        final BasicResponse result = new BasicResponse();

        studyBoardDao.delete(find_bid);
        result.status = true;
        result.data = "스터디 게시판 글 삭제 완료";
        response = new ResponseEntity<>(result, HttpStatus.OK);

        return response;
    }

    @GetMapping("/study/board/search")
    @ApiOperation(value = "스터디 게시판 내용 및 타이틀 검색")
    // 보드 검색, 제목 및 내용으로 검색
    public Object search(@RequestParam(required = true) final String text) {
        ResponseEntity<Object> response = null;
        final BasicResponse result = new BasicResponse();

        if (text != null) {
            final List<Board> titlelist = studyBoardDao.findBoardByTitleLikeOrBodyLikeOrderByDate("%" + text + "%",
                    "%" + text + "%");
            result.status = true;
            result.data = "스터디 게시판 내용 및 타이틀 검색";
            result.object = titlelist;
        } else {
            final List<Board> boardlist = studyBoardDao.findAll();
            result.status = true;
            result.data = "스터디 게시판 내용 및 타이틀 검색 중 검색할 키워드가 없어 전체 검색 실행 완료";
            result.object = boardlist;
        }

        response = new ResponseEntity<>(result, HttpStatus.OK);

        return response;
    }

}
