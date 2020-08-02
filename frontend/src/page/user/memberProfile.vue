<template>
  <div class="container">
    <h2>Member Profile</h2>
    <div class="row row-cols-2 m-5">
      <div class="mb-5 py-5 col-12 col-md-3 card">
        <div v-if="!profileInfo.profile_image">
          <img class="border rounded-circle" src="../../assets/img/defualt_image.png" />
        </div>
        <div v-else>
          <img class="border rounded-circle" :src="profileInfo.profile_image" />
        </div>
        <br />
        <div class="px-2">
          <p>{{ profileInfo.nickname }}님</p>
          <!-- <p>email : {{ email }}</p> -->
          <!-- <small>소개 : {{ profileInfo.intro }}</small> -->
        </div>
        <div class="mt-3">
          <b-badge @click="goUpdate" class="clickbtn btn-sm mr-2" variant="info">수정</b-badge>
          <b-badge @click="goDelete" class="clickbtn btn-sm" variant="danger">탈퇴</b-badge>
        </div>
        <hr />
        <div class>
          <p class="pt-3 px-3 text-info">마일리지 log</p>
          <small class="px-3">10점</small>
          <small class="px-3">10점</small>
          <small class="px-3">10점</small>
        </div>
      </div>
      <div class="text-left pt-3 col-12 col-md-8">
          <h4>평가</h4>
        <!-- <div class="pt-3 mb-5">
          <p class="text-info">내가 만든 스터디 list</p>
          <div v-for="studylist of studyLists" :key="studylist.id">
            <div
              v-if="studylist.isleader&&studylist.empId.study.tmp==1"
              class="tmplist card my-2 px-3 p-2 w-100"
            >
              <div @click="goStudy(studylist.pid)" class="d-flex inline">
                <small>{{ studylist.empId.study.title }}</small>
                <b-badge class="ml-auto" variant="danger">팀장</b-badge>
              </div>
            </div>
            <div v-if="!studylist.isleader" class="tmplist card my-2 px-3 p-2 w-100">
              <div @click="goStudyMain(studylist.pid)" class="d-flex inline">
                <small>{{ studylist.empId.study.title }}</small>
                <b-badge class="ml-auto" variant="success">진행중</b-badge>
              </div>
            </div>
          </div>
          <div v-for="studylist of readyLists" :key="studylist.id">
            <div v-if="!studylist.isleader" class="tmplist card my-2 px-3 p-2 w-100">
              <div @click="goStudy(studylist.pid)" class="d-flex inline">
                <small>{{ studylist.empId.study.title }}</small>
                <b-badge class="ml-auto" variant="warning">대기중</b-badge>
              </div>
            </div>
          </div>
        </div> -->

        <!-- <div class="pt-3 mb-5">
          <p class="text-info">임시저장 list</p>
          <div v-for="tmplist of tmplists" :key="tmplist.pid">
            <div @click="goTmp(tmplist.pid)" class="tmplist card my-2 px-3 p-2 w-100">
              <div class="d-flex inline">
                <small v-if="tmplist.title">{{ tmplist.title }}</small>
                <small v-else>제목 없음</small>
                <small class="text-muted ml-auto">{{changeDatedata(tmplist.posttime)}}</small>
              </div>
            </div>
          </div>
        </div> -->

      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import constants from "../../lib/constants";

const SERVER_URL = "http://localhost:8080";

export default {
  name: "memberprofile",
  data: () => {
    return {
      profileInfo:{},
      studyLists: {},
      readyLists: {},
      tmplists: {},
    };
  },
  methods: {
    changeDatedata(time) {
      return time.substring(5, 10) + " " + time.substring(11, 19);
    },
    addprofileInfo() {
      const token = this.$cookies.get("Auth-Token");
      axios.get(SERVER_URL + "/account/profile", {params: {Token: token,},})
      .then((res) => {
        console.log(res.data.object)
        this.profileInfo = res.data.object
        
        this.addTmpList()
        this.addReadyList() 
        this.addStudyList()
      })
      .catch((err) => {
        this.$router.push({
          name: constants.URL_TYPE.ERROR.ERRORPAGE,
          params: { code: err.response.data },
        });
      });
    },
    addTmpList() {
      axios.post(SERVER_URL + "/account/tmplist", { nickname: this.profileInfo.nickname })
      .then((res) => {console.log(res.data.object);this.tmplists = res.data.object})
      .catch((err) => {console.log(err)})
    },
    addStudyList() {
      axios
        .post(SERVER_URL + "/account/studylist", this.profileInfo)
        .then((res) => {
          this.studyLists = res.data.object;
          console.log(this.studyLists)
        })
        .catch((err) => {
          console.log(err);
        });
    },
    addReadyList() {
      axios
        .post(SERVER_URL + "/account/readylist", this.profileInfo)
        .then((res) => {
          this.readyLists = res.data.object;
          console.log(res.data.object);
        })
        .catch((err) => {
          console.log(err);
        });
    },
    goStudy(post_id) {
      this.$router.push({name: constants.URL_TYPE.POST.POSTDETAIL, params: { post_id: post_id },});
    },
    goStudyMain(post_id) {
      this.$router.push({name: constants.URL_TYPE.STUDY.STUDYMAIN, params: { post_id: post_id },});
    },
    goTmp(post_id) {
      this.$router.push({name: constants.URL_TYPE.POST.POSTUPDATE,params: { post_id: post_id },});
    },
    goUpdate() {
      this.$router.push({ name: constants.URL_TYPE.USER.UPDATE });
    },
    goDelete() {
      this.$router.push({ name: constants.URL_TYPE.USER.DELETE });
    },
  },
  created() {
    this.addprofileInfo();
  },
};
</script>

<style scoped>
.clickbtn,
.tmplist {
  cursor: pointer;
}
</style>