import Vue from "vue";
import Router from "vue-router";

import constants from "../lib/constants";
import AdminPage from "../page/AdminPage.vue";
// 유저
import Profile from "../page/user/Profile.vue";
import Signup from "../page/user/Signup.vue";
import MemberProfile from '../page/user/memberProfile'

// 포스트
import List from "../page/post/List.vue";
import PostCreate from "../page/post/PostCreate.vue";
import PostDetail from "../page/post/PostDetail.vue";
import PostUpdate from "../page/post/PostUpdate.vue";

// 진행
import StudyMain from "../page/study/StudyMain";
import StudyLeaderMain from "../page/study/StudyLeaderMain";
import DailyDetail from "../page/study/DailyDetail";
import DailyCreate from "../page/study/DailyCreate";
import DailyUpdate from "../page/study/DailyUpdate";

// 게시판
import BoardMain from "../page/board/BoardMain.vue"
import BoardCreate from "../page/board/BoardCreate.vue"
import BoardDetail from "../page/board/BoardDetail.vue"
import BoardUpdate from "../page/board/BoardUpdate.vue"

// 랭킹

import Ranking from "../page/rank/Ranking.vue"

// 404 오류
import PageNotFound from "../page/error/PageNotFound.vue";
import ErrorPage from "../page/error/ErrorPage.vue";
import Duplicate from "../page/error/Duplicate.vue";

Vue.use(Router);

export default new Router({
  routes: [
    {
      path: "/admin",
      name: constants.URL_TYPE.ADMIN,
      component: AdminPage,
    },
    // 로그인/가입
    {
      path: "/user/profile",
      name: constants.URL_TYPE.USER.PROFILE,
      component: Profile,
    },
    {
      path: "/user/signup",
      name: constants.URL_TYPE.USER.SIGNUP,
      component: Signup,
    },
    {
      path: "/user/:user_id",
      name: constants.URL_TYPE.USER.MEMBERPROFILE,
      component: MemberProfile,
    },
    // 포스트
    {
      path: "/",
      name: constants.URL_TYPE.POST.MAIN,
      component: List,
    },
    {
      path: "/post/create",
      name: constants.URL_TYPE.POST.POSTCREATE,
      component: PostCreate,
    },
    {
      path: "/post/:post_id",
      name: constants.URL_TYPE.POST.POSTDETAIL,
      component: PostDetail,
    },
    {
      path: "/post/update/:post_id",
      name: constants.URL_TYPE.POST.POSTUPDATE,
      component: PostUpdate,
    },
    // 진행
    {
      path: "/study/:post_id",
      name: constants.URL_TYPE.STUDY.STUDYMAIN,
      component: StudyMain,
    },
    {
      path: "/study/leader/:post_id",
      name: constants.URL_TYPE.STUDY.STUDYLEADERMAIN,
      component: StudyLeaderMain,
    },
    {
      path: "/study/:post_id/:daily_id/detail",
      name: constants.URL_TYPE.STUDY.DAILYDETAIL,
      component: DailyDetail,
    },
    {
      path: "/study/:post_id/create",
      name: constants.URL_TYPE.STUDY.DAILYCREATE,
      component: DailyCreate,
    },
    {
      path: "/study/:post_id/:daily_id/update",
      name: constants.URL_TYPE.STUDY.DAILYUPDATE,
      component: DailyUpdate,
    },
    // 게시판
    {
      path: "/study/:post_id/board",
      name: constants.URL_TYPE.BOARD.BOARDMAIN,
      component: BoardMain,
    },
    {
      path: "/study/:post_id/board/create",
      name: constants.URL_TYPE.BOARD.BOARDCREATE,
      component: BoardCreate,
    },
    {
      path: "/study/:post_id/board/:board_id/detail",
      name: constants.URL_TYPE.BOARD.BOARDDETAIL,
      component: BoardDetail,
    },
    {
      path: "/study/:post_id/board/:board_id/update",
      name: constants.URL_TYPE.BOARD.BOARDUPDATE,
      component: BoardUpdate,
    },
    //랭킹
    {
      path: "/rank/ranking",
      name: constants.URL_TYPE.RANK.RANKING,
      component: Ranking,
    },
    //page not found
    {
      path: "*",
      name: constants.URL_TYPE.ERROR.PAGENOTFOUND,
      component: PageNotFound,
    },
    {
      path: "/error",
      name: constants.URL_TYPE.ERROR.ERRORPAGE,
      component: ErrorPage,
      props: true,
    },
    {
      path: "/duplicate",
      name: constants.URL_TYPE.ERROR.DUPLICATE,
      component: Duplicate,
    },
  ],
});
