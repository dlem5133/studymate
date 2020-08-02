<template>
  <div id="main">
    <div class="row row-cols-2">
      <div class="main-table pr-0 d-none d-md-block col-md-3 border bg-light">
        <div class="m-4">
          <div class="card">
            <div class="card-header mb-3">
              <div class="mt-4" v-if="!profileInfo.profile_image">
                <img
                  class="rounded-circle"
                  src="../../assets/img/defualt_image.png"
                />
              </div>
              <div class="mt-4" v-else>
                <img class="rounded-circle" :src="profileInfo.profile_image" />
              </div>
              <div class="mb-3">
                <small>{{ profileInfo.nickname }}님의 정보</small>
              </div>
            </div>
            <small class="text-success font-weight-bold text-left pl-3"
              >진행중 스터디</small
            >
            <div
              v-for="list in readyLists"
              :key="list.id"
              class=" card m-2 px-2 p-2"
              @click="goStudyMain(list.pid)"
            >
              <div class="d-flex inline">
                <small class="text-left">{{ list.empId.study.title }}</small>
                <b-badge class="ml-auto my-auto" variant="secondary"
                  >승인대기중</b-badge
                >
              </div>
            </div>

            <div
              v-for="list in leaderListsTmp0"
              :key="list.id"
              class=" card m-2 px-2 p-2"
              @click="goStudyMain(list.pid)"
            >
              <div class="d-flex inline">
                <small class=" text-left">{{ list.empId.study.title }}</small>
                <b-badge class="ml-auto my-auto" variant="danger">팀장</b-badge>
              </div>
            </div>

            <div
              v-for="list in unleaderLists"
              :key="list.id"
              class=" card m-2 px-2 p-2"
              @click="goStudyMain(list.pid)"
            >
              <div class="d-flex inline">
                <small class=" text-left">{{ list.empId.study.title }}</small>
                <!-- <b-badge class="ml-auto" variant="success">신청완료</b-badge> -->
              </div>
            </div>
            <hr />

            <small class="text-warning font-weight-bold text-left pl-3 pb-2"
              >모집중 스터디</small
            >
            <div
              v-for="list in leaderListsTmp1"
              :key="list.id"
              class=" card m-2 px-2 p-2"
              @click="goStudyMain(list.pid)"
            >
              <div class="d-flex inline">
                <small class=" text-left">{{ list.empId.study.title }}</small>
                <!-- <b-badge class="ml-auto" variant="warning">모집중&팀장</b-badge> -->
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="main-table col-12 col-md-9 border">
        <div class="container">
          <img src="../../assets/img/logo.png" />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import "../../assets/css/post.scss";
import axios from "axios";
import { VueperSlides, VueperSlide } from "vueperslides";
import "vueperslides/dist/vueperslides.css";
import LoginModal from "../../modal/LoginModal";
import constants from "../../lib/constants";

const SERVER_URL = "http://localhost:8080";

export default {
  name: "",
  components: {
    constants,
    LoginModal,
    VueperSlides,
    VueperSlide,
  },
  data: () => {
    return {
      loginmodal: false,
      profileInfo: [],
      studyLists: [],
      slide: 0,
      sliding: null,

      selectCategory: {
        category: "%%",
        title: null,
      },
      categoryList: [
        "알고리즘",
        "공모전",
        "자격증",
        "취업",
        "웹",
        "기타",
        "전체",
      ],

      searchList: [],
      scrolled: 3,
      timer: null,
      readyLists: [],
      leaderLists: [],
      unleaderLists: [],
      leaderListsTmp1: [],
      leaderListsTmp0: [],
    };
  },
  created() {
    this.addprofileInfo();
    this.categorySubmit();
    window.addEventListener("scroll", this.handleScroll);
  },
  beforeDestroy: function() {
    window.removeEventListener("scroll", this.handleScroll);
  },
  methods: {
    addCategory(cate) {
      if (cate === "전체") {
        this.selectCategory.category = "%%";
      } else {
        this.selectCategory.category = cate;
      }
      this.categorySubmit();
    },
    categorySubmit() {
      axios
        .post(SERVER_URL + "/study/search", this.selectCategory)
        .then((res) => {
          this.searchList = res.data.object;
        })
        .catch((err) => {
          console.log(err);
        });
    },
    changeDatedata(time) {
      if (time) {
        return (
          time.split("-")[0] +
          "/" +
          time.split("-")[1] +
          "/" +
          time.split("-")[2].substring(0, 2)
        );
      }
    },
    getArticles(id) {
      this.$router.push({
        name: constants.URL_TYPE.POST.POSTDETAIL,
        params: { post_id: id },
      });
    },
    addStudyList() {
      axios
        .post(SERVER_URL + "/account/studylist", {
          email: this.profileInfo.email,
          nickname: this.profileInfo.nickname,
          password: this.profileInfo.password,
        })
        .then((res) => {
          this.studyLists = res.data.object;
        })
        .catch((err) => {
          console.log(err);
        });
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
    addprofileInfo() {
      if (this.$cookies.isKey("Auth-Token")) {
        const token = this.$cookies.get("Auth-Token");
        axios
          .get(SERVER_URL + "/account/profile", {
            params: {
              Token: token,
            },
          })
          .then((res) => {
            this.profileInfo = res.data.object;

            this.addStudyList();
            this.addReadyList();
          })
          .catch((err) => {
            this.$router.push({
              name: constants.URL_TYPE.ERROR.ERRORPAGE,
              params: { code: err.response.data },
            });
          });
      }
    },

    openModal() {
      this.loginmodal = true;
    },
    closeModal() {
      this.loginmodal = false;
    },

    goStudyMain(post_id) {
      this.$router.push({
        name: constants.URL_TYPE.STUDY.STUDYMAIN,
        params: { post_id: post_id },
      });
    },
    goDetail(post_id) {
      this.post_id = post_id;
      this.$router.push({
        name: constants.URL_TYPE.POST.POSTDETAIL,
        params: { post_id: post_id },
      });
    },
    handleScroll() {
      var d = document.documentElement;
      var offset = d.scrollTop + window.innerHeight - 16;
      var height = d.offsetHeight;

      if (offset >= height) {
        this.scrolled += 3;
      }
    },
    moveTop() {
      window.scrollTo(0, 0);
    },
  },
};
</script>

<style scoped>
#main {
  padding: 0;
}
.clickstudy {
  cursor: pointer;
}
h1 {
  margin: 0 0 1em;
}
.group {
  display: flex;
  align-items: center;
  margin-bottom: 2em;
}
input[type="radio"] {
  position: absolute;
  opacity: 0;
  z-index: -1;
}
label {
  position: relative;
  margin-right: 1em;
  padding-left: 2em;
  padding-right: 1em;
  line-height: 2;
  cursor: pointer;
}
label::before {
  box-sizing: border-box;
  content: " ";
  position: absolute;
  top: 0.3em;
  left: 0;
  display: block;
  width: 1.4em;
  height: 1.4em;
  border: 2px solid black;
  border-radius: 0.25em;
  z-index: -1;
}
input[type="radio"] + label::before {
  border-radius: 1em;
}
/* Checked */
input[type="radio"]:checked + label {
  padding-left: 1em;
  color: black;
}
input[type="radio"]:checked + label::before {
  top: 0;
  width: 100%;
  height: 2em;
  background: white;
}

/* Transition */
label,
label::before {
  -webkit-transition: 0.25s all ease;
  -o-transition: 0.25s all ease;
  transition: 0.25s all ease;
}
.topButton {
  position: fixed;
  bottom: 50px;
  right: 50px;
  font-size: 30px;
  text-shadow: 0px 0px 10px white;
}
</style>
