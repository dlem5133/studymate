<template>
  <div id="container" class="fixed-top" v-if="isHeader">
    <b-navbar id="navbar" toggleable="lg" class="px-5 px-auto row">
      <div class="container pt-2">
        <b-navbar-brand id="brandname" tag="router-link" :to="{name:constants.URL_TYPE.POST.MAIN}">
          <h4 id="brandname" class="mb-0">스터디메이트</h4>
        </b-navbar-brand>
        <b-navbar-nav class="d-flex ml-auto"></b-navbar-nav>

        <b-dropdown size="lg" right variant="link" toggle-class="text-decoration-none" no-caret>
          <template v-slot:button-content>
            <b-icon icon="person-circle" style="color:orange"></b-icon>
          </template>
          <div class="text-center">
            <div v-if="isLoggedIn">
              <div v-if="!profileInfo.profile_image">
                <img
                  class="border rounded-circle profileImg"
                  src="../../assets/img/defualt_image.png"
                />
              </div>
              <div v-else>
                <img class="border rounded-circle profileImg" :src="profileInfo.profile_image" />
              </div>
              <div class="nameandemail">
                <h6
                  class="mt-2 mb-0"
                  style="font-family: 'Do Hyeon', sans-serif;"
                >{{ profileInfo.nickname }}</h6>
                <small class="email">{{ profileInfo.email }}</small>
                <br />
                <small class="email">마일리지: {{ total_mileage }} | 평점: {{ total_score }}</small>
              </div>
              <b-list-group-item class="listitem" tag="router-link" to="/user/profile">프로필</b-list-group-item>
              <b-list-group-item class="listitem" @click="logout">로그아웃</b-list-group-item>
            </div>
            <div v-if="!isLoggedIn">
              <b-list-group-item
                class="listitem"
                style="font-family: 'Do Hyeon', sans-serif;"
                @click="openModal"
              >로그인</b-list-group-item>
              <LoginModal v-if="loginmodal" @close="closeModal" />
              <b-list-group-item
                class="listitem"
                style="font-family: 'Do Hyeon', sans-serif;"
                @click="kakao"
              >회원가입</b-list-group-item>
            </div>
          </div>
        </b-dropdown>

        <b-dropdown
          v-if="isLoggedIn"
          size="lg"
          right
          variant="link"
          toggle-class="text-decoration-none"
          no-caret
        >
          <template v-slot:button-content>
            <b-icon icon="book" style="color:orange"></b-icon>
          </template>

          <div class="text-center text-nowrap" style="width: auto;">
            <div class="text-left">
              <div class="my-2">
                <small class="text-success font-weight-bold text-left pl-3 pb-2">진행중 스터디</small>
              </div>
              <div
                v-for="list in ingLists"
                :key="list.id"
                class="card m-2 px-2 p-2"
                @click="goStudyMain(list.pid)"
              >
                <div class="d-flex inline">
                  <small class="text-left mr-2">{{ list.empId.study.title }}</small>
                  <b-badge class="ml-auto my-auto" variant="success">진행중</b-badge>
                </div>
              </div>

              <div
                v-for="list in plusUnleaderLists"
                :key="list.id"
                class="card m-2 px-2 p-2"
                @click="goStudyMain(list.pid)"
              >
                <div class="d-flex inline">
                  <small class="text-left mr-2">{{ list.empId.study.title }}</small>
                  <b-badge class="ml-auto my-auto" variant="success">진행중</b-badge>
                </div>
              </div>

              <hr class="mb-0" />

              <div class="my-2">
                <small class="text-warning font-weight-bold text-left pl-3 pb-2">모집중 스터디</small>
              </div>
              <div
                v-for="list in readyLists"
                :key="list.id"
                class="card m-2 px-2 p-2"
                @click="goPostMain(list.pid)"
              >
                <div class="d-flex inline">
                  <small class="text-left mr-2">{{ list.empId.study.title }}</small>
                  <b-badge class="ml-auto my-auto" variant="secondary">승인대기중</b-badge>
                </div>
              </div>

              <div
                v-for="list in comLists"
                :key="list.id"
                class="card m-2 px-2 p-2"
                @click="goPostMain(list.pid)"
              >
                <div class="d-flex inline">
                  <small class="text-left mr-2">{{ list.empId.study.title }}</small>
                  <b-badge class="ml-auto my-auto" variant="warning">모집중</b-badge>
                </div>
              </div>

              <div
                v-for="list in plusLeaderLists"
                :key="list.id"
                class="card m-2 px-2 p-2"
                @click="goPostMain(list.pid)"
              >
                <div class="d-flex inline">
                  <small class="text-left mr-2">{{ list.empId.study.title }}</small>
                  <b-badge class="ml-auto my-auto" variant="warning">추가모집중</b-badge>
                </div>
              </div>
            </div>
            <b-list-group-item
              class="listitem border-0"
              tag="router-link"
              :to="{name:constants.URL_TYPE.POST.POSTCREATE}"
            >스터디 생성</b-list-group-item>
          </div>
        </b-dropdown>
      </div>
    </b-navbar>
  </div>
</template>   

<script>
import constants from "../../lib/constants";
import LoginModal from "../../modal/LoginModal";
import axios from "axios";

const SERVER_URL = constants.ServerUrl;

export default {
  name: "Header",
  components: {
    constants,
    LoginModal,
  },
  props: ["isHeader"],
  computed: {
    total_score() {
      return (
        (this.profileInfo.score1 +
          this.profileInfo.score2 +
          this.profileInfo.score3) /
        3
      ).toFixed(1);
    },
    total_mileage() {
      if ("+this.mileageTotal".length > 3) {
        return this.mileageTotal / 1000 + "K";
      } else {
        return this.mileageTotal;
      }
    },
  },
  created() {
    this.tokencheck();
    this.addprofileInfo();
    window.addEventListener("scroll", this.handleScroll);
  },
  methods: {
    handleScroll() {
      if (
        document.body.scrollTop > 80 ||
        document.documentElement.scrollTop > 80
      ) {
        document.getElementById("container").style.backgroundColor = "white";
        // document.getElementById("container").style.borderBottom = "1px solid orange";
      } else {
        document.getElementById("container").style.backgroundColor =
          "rgba(255,255,255,0)";
        // document.getElementById("navbar").style.margin = "1.5rem 1.5rem"
      }
    },
    openModal() {
      this.loginmodal = true;
    },
    closeModal() {
      this.loginmodal = false;
    },
    kakao() {
      axios.get(SERVER_URL + "/k/klogin").then((res) => {
        this.locate = res.data.object;
        location.href = this.locate;
      });
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
    addprofileInfo() {
      if (this.$cookies.isKey("Auth-Token")) {
        const token = this.$cookies.get("Auth-Token");
        axios
          .get(SERVER_URL + "/account/profile", { params: { Token: token } })
          .then((res) => {
            this.profileInfo = res.data.object;
            this.addReadyList();
            this.addStudyList();
            this.mileageList();
          })
          .catch((err) => {
            console.log(err);
          });
      }
    },
    addStudyList() {
      axios
        .post(SERVER_URL + "/account/studylist", this.profileInfo)
        .then((res) => {
          this.ingLists = res.data.object.filter(
            (item) => item.empId.study.tmp == 0
          );
          this.comLists = res.data.object.filter(
            (item) => item.empId.study.tmp == 1
          );
          this.endLists = res.data.object.filter(
            (item) => item.empId.study.tmp == 2
          );
          this.plusLists = res.data.object.filter(
            (item) => item.empId.study.tmp == 3
          );
          this.plusLeaderLists = this.plusLists.filter(
            (item) => item.isleader == 1
          );
          this.plusUnleaderLists = this.plusLists.filter(
            (item) => item.isleader == 0
          );
          console.log(res.data.object)
        })
        .catch((err) => {
          console.log(err);
        });
    },
    addReadyList() {
      axios
        .post(SERVER_URL + "/account/readylist", this.profileInfo)
        .then((res) => {console.log(res.data.object)
          this.readyLists = res.data.object;
        })
        .catch((err) => {
          console.log(err);
        });
    },
    mileageList() {
      axios
        .get(SERVER_URL + "/mileage/user", {
          params: { uid: this.profileInfo.uid },
        })
        .then((res) => {
          this.mileageTotal = res.data.object.total;
        })
        .catch((err) => console.log(err));
    },
    goStudyMain(post_id) {
      this.$router.push({
        name: constants.URL_TYPE.STUDY.STUDYMAIN,
        params: { post_id: post_id },
      });
    },
    goPostMain(post_id) {
      this.$router.push({
        name: constants.URL_TYPE.POST.POSTDETAIL,
        params: { post_id: post_id },
      });
    },
  },
  data: function () {
    return {
      constants,
      profileInfo: {},
      loginmodal: false,
      isLoggedIn: false,

      mileageTotal: null,

      // 리스트 7 종류
      readyLists: [],
      comLists: [],
      ingLists: [],
      endLists: [],
      plusLists: [],
      plusLeaderLists: [],
      plusUnleaderLists: [],
    };
  },
};
</script>

<style scoped>
* {
  font-family: "Noto Sans KR", sans-serif;
}

#brandname {
  font-family: "Do Hyeon", sans-serif;
  font-size: x-large;
  color: orange;
}
.dropdown-menu {
  font-size: 100px !important;
}

.profileImg {
  width: 70px;
  height: 70px;
}

.iconcss {
  font-size: 30px;
  color: orange;
}

.listitem {
  font-family: "Do Hyeon", sans-serif;
  background-color: orange;
  color: white;
  /* border-radius: 15px; */
}

.listitem:hover {
  font-family: "Do Hyeon", sans-serif;
  color: white;
  background-color: rgb(255, 145, 0);
}
.nameandemail {
  margin-top: 5px;
  margin-bottom: 5px;
}
</style>