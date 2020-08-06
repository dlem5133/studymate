<template>
  <div class="container" v-if="isHeader">
    <b-navbar id="navbar" toggleable="lg" class="px-auto sticky-top row mb-5">
      <b-navbar-brand id="brandname" tag="router-link" :to="{name:constants.URL_TYPE.POST.MAIN}"> 
      <!-- <img  src="../../assets/img/textlogo.png" width="120px" height="30px"/> -->
        <h4 id="brandname">스터디메이트</h4>
        <button @click="kakao">로긘</button>
      </b-navbar-brand>
        <b-navbar-nav class="d-flex ml-auto">

        </b-navbar-nav>

        <b-dropdown size="lg" dropleft variant="link" toggle-class="text-decoration-none" no-caret>
          <template v-slot:button-content>
            <b-icon icon="person-circle" style="color:#feb74d"></b-icon>
            <!-- <i class="far fa-user-circle iconcss"></i> -->
          </template>

          <div class="text-center">
            <div v-if="isLoggedIn">
              <div v-if="!profileInfo.profile_image">
                <img class="border rounded-circle" src="../../assets/img/defualt_image.png" width="70" height="70"/>
              </div>
              <div v-else>
                <img class="border rounded-circle" :src="profileInfo.profile_image" width="70" height="70"/>
              </div>
              <div class="nameandemail">{{ profileInfo.nickname }}<br>
              <small  class="email">{{ profileInfo.email }}</small>
              </div>
              <b-list-group-item class="listitem" tag="router-link" to="/user/profile">프로필</b-list-group-item>
              <b-list-group-item class="listitem" @click="logout">로그아웃</b-list-group-item>
            </div>
            <div v-if="!isLoggedIn">
              <!-- <img class="border rounded-circle" src="../../assets/img/defualt_image.png" width="70" height="70"/> -->
              <b-list-group-item class="listitem" variant="warning" @click="openModal">로그인</b-list-group-item>
              <LoginModal v-if="loginmodal" @close="closeModal" />
              <b-list-group-item class="listitem" variant="warning" @click="goSignup">회원가입</b-list-group-item>
            </div>
          </div>

        </b-dropdown>


        <b-dropdown v-if="isLoggedIn" size="lg" dropleft variant="link" toggle-class="text-decoration-none" no-caret>
          <template v-slot:button-content>
            <b-icon icon="book" style="color:#feb74d"></b-icon>
            <!-- <i class="fas fa-book-reader iconcss"></i> -->
          </template>

          <div class="text-center text-nowrap" style="width: auto;">
            
            <div class="text-left">
              <small class="text-success font-weight-bold pl-3">진행중 스터디</small>
              <div
                v-for="list in readyLists"
                :key="list.id"
                class="card m-2 px-2"
                @click="goStudyMain(list.pid)"
              >
                <div class="">
                  <small class="text-left">{{ list.empId.study.title }}</small>
                  <b-badge class="ml-auto my-auto" variant="secondary">승인대기중</b-badge>
                </div>
              </div>

              <div
                v-for="list in leaderListsTmp0"
                :key="list.id"
                class="card m-2 px-2 p-2"
                @click="goStudyMain(list.pid)"
              >
                <div class="d-flex inline">
                  <small class="text-left">{{ list.empId.study.title }}</small>
                  <b-badge class="ml-auto my-auto" variant="danger">진행중&팀장</b-badge>
                </div>
              </div>

              <div
                v-for="list in unleaderLists"
                :key="list.id"
                class="card m-2 px-2 p-2"
                @click="goStudyMain(list.pid)"
              >
                <div class="d-flex inline">
                  <small class="text-left">{{ list.empId.study.title }}</small>
                  <b-badge class="ml-auto my-auto" variant="success">신청완료</b-badge>
                </div>
              </div>
              <hr />

              <small class="text-warning font-weight-bold text-left pl-3 pb-2">모집중 스터디</small>
              <div
                v-for="list in leaderListsTmp1"
                :key="list.id"
                class="card m-2 px-2 p-2"
                @click="goStudyMain(list.pid)"
              >
                <div class="d-flex inline">
                  <small class="text-left">{{ list.empId.study.title }}</small>
                  <b-badge class="ml-auto my-auto" variant="warning">모집중&팀장</b-badge>
                </div>
              </div>
            </div>
            <hr>
            <b-list-group-item class="listitem" tag="router-link" to="/post/create"><i class="fas fa-plus plusstudy" style="color: white"></i> <i class="fas fa-book-open plusstudy" style="color: white"></i></b-list-group-item>
          </div>

        </b-dropdown>

    </b-navbar>
  </div>
</template>   

<script>
import constants from "../../lib/constants";
import LoginModal from "../../modal/LoginModal";
import axios from "axios";

const SERVER_URL = 'http://localhost:8080'

export default {
  name: "Header",
  components: {
    LoginModal,
  },
  props: ["isHeader"],
  computed: {},
  watch: {},
  created() {
    this.tokencheck();
    this.addprofileInfo();
  },
  methods: {

    kakao(){
      axios.get(SERVER_URL+"/k/klogin").then((res)=> {
        this.locate = res.data.object;
        location.href=this.locate;
    })
    },


    logout() {
      this.$cookies.remove("Auth-Token");
      this.$router.push("/");
      this.$router.go();
      this.tokencheck();
    },
    tokencheck() {
      this.isLoggedIn = this.$cookies.isKey("Auth-Token");
    },
    openModal() {
      this.loginmodal = true;
    },
    closeModal() {
      this.loginmodal = false;
    },
    goSignup() {
      this.$router.push({name: constants.URL_TYPE.USER.SIGNUP})
    },
    addprofileInfo() {
      if (this.$cookies.isKey("Auth-Token")) {
        const token = this.$cookies.get("Auth-Token");
        axios.get(SERVER_URL + "/account/profile", {params: {Token: token}})
        .then((res) => {
          this.profileInfo = res.data.object
          console.log("pro", this.profileInfo)
          this.addReadyList() 
          this.addStudyList()
        })
        .catch((err) => {
          console.log(err)
          // this.$router.push({
          //   name: constants.URL_TYPE.ERROR.ERRORPAGE,
          //   params: { code: err.response.data },
          // });
        });
      }
    },
    addStudyList() {
      axios
        .post(SERVER_URL + "/account/studylist", this.profileInfo)
        .then((res) => {
          this.leaderLists = res.data.object.filter(
            (item) => item.isleader != 0
          );
          this.leaderListsTmp0 = this.leaderLists.filter(
            (item) => item.empId.study.tmp == 0
          );
          this.leaderListsTmp1 = this.leaderLists.filter(
            (item) => item.empId.study.tmp == 1
          );

          this.unleaderLists = res.data.object.filter(
            (item) => item.isleader == 0
          );
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
        })
        .catch((err) => {
          console.log(err);
        });
    },
    goStudyMain(post_id) {
      this.$router.push({
        name: constants.URL_TYPE.STUDY.STUDYMAIN,
        params: { post_id: post_id },
      });
    },
  },
  data: function() {
    return {
      constants,
      profileInfo: {},
      loginmodal: false,
      isLoggedIn: false,
      readyLists: [],
      leaderLists: [],
      unleaderLists: [],
      leaderListsTmp1: [],
      leaderListsTmp0: [],
    };
  }
};
</script>

<style scoped>
*{
  font-family:'Do Hyeon', sans-serif;
}
#brandname{
  /* font-family: 'Black Han Sans', sans-serif; */
  font-family: 'Do Hyeon', sans-serif;
  font-weight: bold;
  font-size:x-large;
  /* font-family: 'Jua', sans-serif; */
}

#navbar {
  margin: 0;
  padding: 10px;
  /* height:60px; */
}
.iconcss {
  font-size: 30px;
  color: orange;
}
.listitem {
  background-color: #ffb74d;
}

.listitem:hover{
  background-color: lightgray;
}
.nameandemail{
  margin-top:5px;
  margin-bottom:5px;
}

</style>