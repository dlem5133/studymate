<template>
  <div id="main" class="container">
    <div class="row row-cols-2">
      <div class="main-table col-12 col-md-12">
        <div class="container logoimg">
          <img class="main-table col-12 col-md-6" src="../../assets/img/owl_logo.png" />
        </div>

        <!-- ===========================================================================================  -->
          <div class="d-flex justify-content-center">
            <div v-for="category in categoryList"
              :key="category"
            >
              <input
                type="radio"
                @click="addCategory(category)"
                :id="`${category}`"
                name="rb"
              />
              <label :for="`${category}`">{{ category }}</label>
            </div>
          </div>

          <!-- =======================================아래 수정====================================================  -->
          <form class="input-groups flex-nowrap py-4 d-flex justify-content-center">
            <div class="serach-selectbox">            
              <select
                v-model="selected"
                class="form-control" @click="sendOption">
                <option value="all" > 전체 </option>
                <option value="title"> 스터디명 </option>
                <option value="area"> 지역</option>
                <option value="tag"> 태그</option>
               
              </select>
            </div>

            
            <div class="search">
              <!--<div class="serach-input">
                <input
                  v-model="selectCategory.optionstext"
                  class="form-control"
                  type="text"
                  placeholder="검색어를 입력해주세요"
                />
              </div>-->
              <!-- -->
              <div class="input-group md-form form-sm form-2 pl-0">
                <input class="form-control my-0 py-1 amber-border" v-model="selectCategory.optionstext" type="text" placeholder="검색어를 입력해주세요" aria-label="Search">
                  <button class="input-group-text" id="addon-wrapping"  @click="categorySubmit">
                    <mdbIcon icon="search"/>
                  </button>
              </div>
            </div>
          </form>
        <!-- ======================================= 카드====================================================  -->

        <div class="row">
          <ul
            class="col-12 col-sm-6 col-lg-4 col-xl-3 d-flex justify-content-center"
            v-for="(list, i) in searchList"
            :key="list.pid"
          >
            <li
              v-if="i < `${scrolled}`"
              class="booking-card"
              :style="{ backgroundImage: `url(${list.background_image})` }"
            >
              <div class="book-container">
                <div class="content">
                  <button
                    class="btn"
                    @click="goDetail(list.pid)"
                    :key="list.pid"
                  >
                    Detail
                  </button>
                </div>
              </div>
              <div class="informations-container">
                <div class="categoryandtitle">
                  <h3 class="title">{{ list.title }}</h3>
                  <small class="mycategory">{{ list.category }}</small>
                </div>
                <div class="more-information">
                  <div class="info-container">
                    <div class="box inform">
                      <img
                        class="infopplbindo"
                        src="../../assets/img/person.png"
                      />
                      2 / {{ list.limitp }} 명<br />
                      <img
                        class="infopplbindo"
                        src="../../assets/img/calendar.png"
                      />
                      주 {{ list.bindo }} 회 <br />
                      <img
                        class="infopplbindo"
                        src="../../assets/img/navi.png"
                      />
                      <span v-if="list.sido.sidoname=='온라인'">
                      {{list.sido.sidoname}}</span>
                      <span v-if="list.sido.sidoname!='온라인' && list.gugun.gugunname != '선택안함'">
                      {{ list.sido.sidoname }} {{list.gugun.gugunname}}</span>
                      <span v-if="list.sido.sidoname!='온라인' && list.gugun.gugunname == '선택안함'">
                      {{ list.sido.sidoname }} (미정) </span>
                    </div>
                  </div>
                </div>
                <!-- <div class= "tags">
                    <b-badge class=" mr-1 mt-2 " variant="warning"  v-for="tag in list.tag_list" 
                      :key="tag.tid"
                      >#{{ tag.tagname }}</b-badge>
                </div>-->
              </div>
            </li>
          </ul>
        </div>
        <button class="topButton" @click="moveTop">
          <i class="fas fa-arrow-alt-circle-up"></i>
        </button>
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
import { mdbIcon } from 'mdbvue';

const SERVER_URL = "http://localhost:8080";

export default {
  name: "",
  components: {
    mdbIcon,
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
        options: null,
        optionstext: null,
        title: null,
        sido_code: null,
        tag: null,
        tmp: 0,
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
      whatSearch: ["제목", "지역", "태그"],
      option: "all",
      searchList: [],
      scrolled: 3,
      timer: null,
      selected:"all" ,
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
    sendOption() {
      // console.log(event.target.value);
      const opt = this.studyLists;
      // console.log(opt);
      //this.categorySubmit();
      if (opt == "all") {
        this.selectCategory.options = "%%";
      } else {
        this.selectCategory.options = opt;
      }
      //this.categorySubmit();
    },

    addCategory(catego) {
      if (catego === "전체") {
        this.selectCategory.category = "%%";
      } else {
        this.selectCategory.category = catego;
      } //카테고리 정해줌
      // console.log("애드");
      // console.log(this.selectCategory);
      this.categorySubmit();
    },

    categorySubmit() {
      this.selectCategory.title = this.selectCategory.optionstext;
      if (this.selected == "title") {
        this.selectCategory.tmp = 1
        this.selectCategory.sido_code = null;
        this.selectCategory.tag = null;
      } else if (this.selected == "area") {
        this.selectCategory.tmp = 2
      } else if (this.selected == "tag") {
        this.selectCategory.tmp = 3
      }

      console.log(this.selectCategory);

      axios
        .post(SERVER_URL + "/study/search", this.selectCategory)
        .then((res) => {
          this.searchList = res.data.object;
          console.log(this.searchList);
        })
        .catch((err) => {
          console.log(err);
        });
    },
    ////////////////////////////////////////////////////////////////////////////////////////

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
      var offset = d.scrollTop + window.innerHeight + 40;
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

h1 {
  margin: 0 0 1em;
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
  border: 2px solid #ffb74d;
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
  background: #f7bd68;
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

/* */
.row {
  align-items: center;
}
.group {
  align-items: center;
  margin-bottom: 2em;
}

.input-group {
  padding-bottom: 20px;
}
.input-groups {
  border-bottom: 2px solid #ffb74d;
}
.search {
  width: 50%;
}

.serach-input {
  width: 80%;
  display: inline-block;
}

.input-group-prepend {
  width: auto;
  float: right;
  align-items: center;
}

#addon-wrapping {
  background: #ffb74d;
  border: #ffb74d;
}
.input-group.md-form.form-sm.form-2 input {
      border: 1px solid #bdbdbd;
      /* border-bottom: 1px solid #bdbdbd; */

      /* border-top: none; */
      border-top-left-radius: 0.25rem;
      border-bottom-left-radius: 0.25rem;
  }
.input-group.md-form.form-sm.form-2 input.amber-border  {
  /* border-bottom: 1px solid #ffca28; */
    border: 1px solid #ffca28;

}

select.form-control{
  border: 1px solid #ffca28;
}


</style>
