<template>
  <div>
    <div id="mainimg">
      <div class="main-text">
        <div class="d-flex justify-content-center">

        <h3 class="mb-3"
          style="font-family: 'Do Hyeon', sans-serif;color:rgba(255,255,255,0.7);"
        >안녕하세요 스터디메이트에 방문하신걸 환영합니다.</h3>
        </div>
        <h4 style="font-size:1em;font-family: 'IBMPlexSansKR-Text';color:rgba(255,255,255,0.5);">원하는 스터디를 쉽게 모집하고, 신뢰도 있는 팀원을 모집해보세요.</h4>
        <h4 style="font-size:1em;font-family: 'IBMPlexSansKR-Text';color:rgba(255,255,255,0.5);">IT에 특화된 에디터를 사용하여 쉽고 빠르게 스터디를 기록해보세요</h4>
        
        <h6 class="mt-4" style="color:rgba(255,255,255,0.8);">
          <b-button v-b-modal.modal-1 size="sm" class="about rounded-lg" variant="outline-light" >About US</b-button> 
          
        </h6>
        <h4>
          <b-icon
            animation="fade"
            icon="mouse"
            variant="warning"
            class="mt-4"
            style="cursor:pointer;"
            @click="moveBottom"
          ></b-icon>
        </h4>
      </div>
    </div>

    <b-modal id="modal-1" hide-header ok-only>
      <!-- 2.회원가입 3.스터디생성 4.일지 5.평가 6.신고 -->
        <div class="d-flex">
          <h4 class="pt-2 pl-2 pr-0 text-warning" style="font-family: 'Do Hyeon', sans-serif;">스터디메이트</h4><h5 class="pt-2  px-1 mt-1" style="font-family: 'Do Hyeon', sans-serif;">의 규칙</h5>
          <div class="ml-auto my-auto" @click="$bvModal.hide('modal-1')">
            <b-icon icon="x"></b-icon>
          </div>
        </div>
        <hr />
      <!-- <b-card no-body> -->
        <b-tabs active-nav-item-class="bg-warning" class="p-2" pills vertical>
          <b-tab title="회원가입" active>
            <b-card-text>
              <small class="font-weight-bold">1. 회원가입을 눌러주세요.</small>
              <img class="w-100 mt-3 mb-4" src="@/assets/img/main_rule/signup_1.png" alt="">
              <small class="font-weight-bold">2. 카카오 회원가입을 진행해주세요.</small>
              <img class="border rounded-lg w-100 mt-3 mb-4" src="@/assets/img/main_rule/signup_2.jpg" alt="">
            </b-card-text>
          </b-tab>
          <b-tab title="스터디">
            <b-card-text>
              <small class="font-weight-bold">1. 로그인 후 책 모양 아이콘을 눌러주세요.</small>
              <img class="w-100 mt-3 mb-4" src="@/assets/img/main_rule/study_1.jpg" alt="">
              <small class="font-weight-bold">2. 스터디를 생성해주세요.</small>
              <img class="border rounded-lg w-100 mt-3 mb-4" src="@/assets/img/main_rule/study_2.jpg" alt="">
            </b-card-text>
          </b-tab>
          <b-tab title="일지">
            <b-card-text>
              <small class="font-weight-bold">1. 로그인 후 진행중인 스터디를 눌러주세요.</small>
              <img class="w-100 mt-3 mb-4" src="@/assets/img/main_rule/eval_1.jpg" alt="">
              <small class="font-weight-bold">2. 스터디 일지를 작성해 보세요.</small>
              <img class="border rounded-lg w-100 mt-3 mb-4" src="@/assets/img/main_rule/일지_2.jpg" alt="">
              <small class="font-weight-bold">3. 스터디 팀원들이 작성한 일지를 확인할 수 있습니다.</small>
              <img class="w-100 mt-3 mb-4" src="@/assets/img/main_rule/일지_3.jpg" alt="">
            </b-card-text>
          </b-tab>
          <b-tab title="평가">
            <b-card-text>
              <small class="font-weight-bold">1. 로그인 후 진행중인 스터디를 눌러주세요.</small>
              <img class="w-100 mt-3 mb-4" src="@/assets/img/main_rule/eval_1.jpg" alt="">
              <small class="font-weight-bold">2. EVALUATE(평가)를 눌러주세요.</small>
              <img class="border rounded-lg w-100 mt-3 mb-4" src="@/assets/img/main_rule/eval_2.jpg" alt="">
              <small class="font-weight-bold">3. 스터디 팀원들에 대한 평가를 진행할 수 있습니다.</small>
              <img class="w-100 mt-3 mb-4" src="@/assets/img/main_rule/eval_3.jpg" alt="">
              <img class="border rounded-lg w-100 mt-3 mb-4" src="@/assets/img/main_rule/eval_4.jpg" alt="">
            </b-card-text>
          </b-tab>
          <b-tab title="마일리지">
            <b-card-text>
              <small class="font-weight-bold">1. 로그인 후 프로필을 눌러주세요.</small>
              <img class="w-100 mt-3 mb-4" src="@/assets/img/main_rule/마일리지_1.jpg" alt="">
              <small class="font-weight-bold">2. 스터디 목록을 확인할 수 있습니다.</small>
              <img class="border rounded-lg w-100 mt-3 mb-4" src="@/assets/img/main_rule/마일리지_2.jpg" alt="">
              <small class="font-weight-bold">3. 마일리지 현황을 확인할 수 있습니다.</small>
              <img class="w-100 mt-3 mb-4" src="@/assets/img/main_rule/마일리지_3.jpg" alt="">
              <small class="font-weight-bold">4. 마일리지 랭킹을 확인할 수 있습니다.</small>
              <img class="border rounded-lg w-100 mt-3 mb-4" src="@/assets/img/main_rule/마일리지_4.jpg" alt="">
              <img class="border rounded-lg w-100 mt-3 mb-4" src="@/assets/img/main_rule/마일리지_5.jpg" alt="">
            </b-card-text>
          </b-tab>
          <b-tab title="신고">
            <b-card-text>
              <small class="font-weight-bold">1. 로그인 후 진행중인 스터디를 눌러주세요.</small>
              <img class="w-100 mt-3 mb-4" src="@/assets/img/main_rule/eval_1.jpg" alt="">
              <small class="font-weight-bold">2. MEMBER(팀원)를 눌러주세요.</small>
              <img class="border rounded-lg w-100 mt-3 mb-4" src="@/assets/img/main_rule/신고_2.jpg" alt="">
              <small class="font-weight-bold">3. 팀장 위임 기능과 팀원 신고 기능을 사용할 수 있습니다.</small>
              <img class="w-100 mt-3 mb-4" src="@/assets/img/main_rule/신고_3.jpg" alt="">
              <img class="border rounded-lg w-100 mt-3 mb-4" src="@/assets/img/main_rule/신고_4.jpg" alt="">
            </b-card-text>
          </b-tab>
        </b-tabs>
      <!-- </b-card> -->
    </b-modal>

    <div class="container">
      <div id="maintext" class="row row-cols-2">
        <div id="maintable" class="main-table mt-5 col-12 col-md-12">
          <!-- ===================== 카테고리 ======================  -->
          <div class="row">
            <div class="col-12 offset-lg-2 col-lg-4 p-0 d-flex">
              <div class="mx-auto" v-for="category in categoryList1" :key="category">
                <input
                  class
                  type="radio"
                  v-model="selected.category"
                  :value="`${category}`"
                  :id="`${category}`"
                  name="rb"
                  @click="searchData"
                />
                <label class="radiolabel" :for="`${category}`">{{ category }}</label>
              </div>
            </div>
            <div class="col-12 col-lg-5 p-0 d-flex">
              <div class="mx-auto" v-for="category in categoryList2" :key="category">
                <input
                  class
                  type="radio"
                  v-model="selected.category"
                  :value="`${category}`"
                  :id="`${category}`"
                  name="rb"
                  @click="searchData"
                />
                <label class="radiolabel" :for="`${category}`">{{ category }}</label>
              </div>
            </div>
          </div>

          <!-- ======================== 검색 ========================  -->
          <form class="input-groups flex-nowrap py-4 d-flex justify-content-center">
            <div class="searchinput d-flex w-100">
              <div class="serach-selectbox border-0">
                <select
                  style="background-color:rgba(255,255,255,0)"
                  v-model="selected.what"
                  class="form-control border-0"
                  @click="searchData"
                >
                  <option value="all">전체</option>
                  <option value="title">스터디명</option>
                  <option value="area">지역</option>
                  <option value="tag">태그</option>
                </select>
              </div>

              <div class="search mx-auto w-100">
                <div class="input-group pb-0 md-form form-sm form-2 pl-0">
                  <input
                    class="form-control my-0 py-1 border-0"
                    @keyup="searchData"
                    v-model="selected.keyword"
                    type="text"
                    placeholder="검색어를 입력해주세요"
                    aria-label="Search"
                  />
                  <b-icon icon="search" style="color:orange;cursor:pointer;" class="mx-3 my-auto"></b-icon>
                </div>
              </div>
            </div>
          </form>
          <!-- ======================== 카드=========================  -->

          <div class="row mb-5">
            <ul
              class="col-12 col-sm-6 col-lg-4 col-xl-3 d-flex justify-content-center"
              v-for="(list, i) in searchList"
              :key="list[0].pid"
            >
              <li
                v-if="i < `${scrolled}`"
                class="booking-card"
                :style="{ backgroundImage: `url(${list[0].background_image})` }"
              >
                <div class="book-container">
                  <div class="content">
                    <button class="btn" @click="goDetail(list[0].pid)" :key="list[0].pid">자세히 보기</button>
                  </div>
                </div>
                <div class="informations-container">
                  <div class="categoryandtitle">
                    <h3 class="title">{{ list[0].title }}</h3>
                    <small class="mycategory">{{ list[0].category }}</small>
                  </div>
                  <div class="more-information">
                    <div class="info-container">
                      <div class="box inform">
                        <img class="infopplbindo" src="../../assets/img/person.png" />
                        {{list[0].memnum}} / {{ list[0].limitp }} 명
                        <br />
                        <img class="infopplbindo" src="../../assets/img/calendar.png" />
                        주 {{ list[0].bindo }} 회
                        <br />
                        <img class="infopplbindo" src="../../assets/img/navi.png" />
                        <span v-if="list[0].sido.sidoname=='온라인'">{{list[0].sido.sidoname}}</span>
                        <span
                          v-if="list[0].sido.sidoname!='온라인' && list[0].gugun.gugunname != '선택안함'"
                        >{{ list[0].sido.sidoname }} {{list[0].gugun.gugunname}}</span>
                        <span
                          v-if="list[0].sido.sidoname!='온라인' && list[0].gugun.gugunname == '선택안함'"
                        >{{ list[0].sido.sidoname }} (미정)</span>
                      </div>
                    </div>
                  </div>
                </div>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import "../../assets/css/post.scss";
import axios from "axios";
import constants from "../../lib/constants";

const SERVER_URL = constants.ServerUrl;

export default {
  name: "",
  components: {
    constants,
  },
  data: () => {
    return {
      profileInfo: [],
      studyLists: [],
      allStudyList: [],
      searchList: [],
      categoryList1: ["전체", "공모전", "알고리즘"],
      categoryList2: ["취업", "자격증", "웹", "기타"],
      whatSearch: ["제목", "지역", "태그"],
      selected: {
        category: "",
        what: "all",
        keyword: "",
      },
      scrolled: 8,
    };
  },
  created() {
    this.addprofileInfo();
    this.allStudy();
    window.addEventListener("scroll", this.handleScroll);
  },
  beforeDestroy: function () {
    window.removeEventListener("scroll", this.handleScroll);
  },
  methods: {
    linkClass(idx){
      if (this.tabindex === idx){
        return ['bg-warning', 'text-light']
      } else{
        return ['bg-warning','text-light']
      }
    },
    allStudy() {
      axios
        .get(SERVER_URL + "/study/list")
        .then((res) => {
          this.allStudyList = res.data.object;
          var tmp = this.allStudyList;
          for (let i = 0; i < tmp.length; i++) {
            if (tmp[i][0].background_image != "") {
              var t = require("@/assets/uploadfile/" +
                tmp[i][0].background_image);
              tmp[i][0].background_image = t;
            }
          }
          this.searchList = tmp;
        })
        .catch((err) => {
          console.log(err);
        });
    },
    searchData() {
      var tmpSearch = this.selected;
      setTimeout(() => {
        var tmpData = this.allStudyList;
        if (tmpSearch.category != "" && tmpSearch.category != "전체") {
          tmpData = tmpData.filter(
            (da) => da[0].category == tmpSearch.category
          );
        }
        const word = tmpSearch.keyword;
        var tmpData2 = [];
        if (tmpSearch.what === "all") {
          for (let i = 0; i < tmpData.length; i++) {
            if (
              tmpData[i][0].title.includes(word) ||
              tmpData[i][0].gugun.gugunname.includes(word) ||
              tmpData[i][0].sido.sidoname.includes(word)
            ) {
              tmpData2.push(tmpData[i]);
            } else {
              for (let j = 0; j < tmpData[i][1].length; j++) {
                if (tmpData[i][1][j].tagname.includes(word)) {
                  tmpData2.push(tmpData[i]);
                  break;
                }
              }
            }
          }
        } else if (tmpSearch.what === "title") {
          for (let i = 0; i < tmpData.length; i++) {
            if (tmpData[i][0].title.includes(word)) {
              tmpData2.push(tmpData[i]);
            }
          }
        } else if (tmpSearch.what === "area") {
          for (let i = 0; i < tmpData.length; i++) {
            if (
              tmpData[i][0].gugun.gugunname.includes(word) ||
              tmpData[i][0].sido.sidoname.includes(word)
            ) {
              tmpData2.push(tmpData[i]);
            }
          }
        } else if (tmpSearch.what === "tag") {
          for (let i = 0; i < tmpData.length; i++) {
            for (let j = 0; j < tmpData[i][1].length; j++) {
              if (tmpData[i][1][j].tagname.includes(word)) {
                tmpData2.push(tmpData[i]);
                break;
              }
            }
          }
        }
        tmpData = tmpData2;
        this.searchList = tmpData;
      }, 100);
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
          })
          .catch((err) => {
            this.$router.push({
              name: constants.URL_TYPE.ERROR.ERRORPAGE,
              params: { code: err.response.data },
            });
          });
      }
    },
    goDetail(post_id) {
      this.$router.push({
        name: constants.URL_TYPE.POST.POSTDETAIL,
        params: { post_id: post_id },
      });
    },
    handleScroll() {
      var d = document.documentElement;
      var offset = d.scrollTop + window.innerHeight;
      var height = d.offsetHeight + 685;
      if (offset >= height) {
        this.scrolled += 3;
      }
    },
    moveTop() {
      var location = document.querySelector("#mainimg").offsetTop;
      window.scrollTo({ top: location, behavior: "smooth" });
    },
    moveBottom() {
      var location = document.querySelector("#maintext").offsetTop;
      window.scrollTo({ top: location - 71.8, behavior: "smooth" });
    },
  },
};
</script>

<style scoped>
* {
  font-family: "IBMPlexSansKR-Text";
}
.about{
  border:1px solid rgba(255,255,255,0.4);
  background-color:rgba(255,255,255,0.2);
  color:rgba(255,165,0,0.7);
  font-family: 'Do Hyeon',sans-serif;
}
.about:hover{
  background-color:rgba(255,255,255,0.8);
  font-family: 'Do Hyeon',sans-serif;
  color:rgba(0,0,0,0.5)
}
#mainimg {
  position: absolute;
  top: 0 !important;
  min-width: 100%;
  width: 100%;
  right: 0;
  height: 100vh !important;
  line-height: 100vh;
  background-image: url("../../assets/img/main.jpg");
  background-size: cover;
  z-index: 6;
}
#maintext {
  position: relative;
  top: 100vh;
}
.main-text {
  padding-top: 40vh;
  background-color: #000000;
  background-color: rgba(0, 0, 0, 0.8);
  height: 100vh;
}
input[type="radio"] {
  position: absolute;
}
.radiolabel {
  position: relative;
  /* margin-right: 1em; */
  padding-left: 3em;
  padding-right: 1em;
  line-height: 2;
  cursor: pointer;
}
.radiolabel::before {
  box-sizing: border-box;
  content: " ";
  position: absolute;
  top: 0.3em;
  left: 1em;
  display: block;
  width: 1.4em;
  height: 1.4em;
  border: 2px solid orange;
  border-radius: 0.25em;
  z-index: -1;
}
input[type="radio"] + .radiolabel::before {
  border-radius: 1em;
}
/* Checked */
input[type="radio"]:checked + .radiolabel {
  /* padding-left: 1em; */
  transform: translateX(-13%);
  color: black;
}
input[type="radio"]:checked + .radiolabel::before {
  top: 0;
  left: 1.7em;
  width: 80%;
  height: 2em;
  background: orange;
}
/* Transition */
.radiolabel,
.radiolabel::before {
  -webkit-transition: 0.25s all ease;
  -o-transition: 0.25s all ease;
  transition: 0.25s all ease;
}
.input-group.md-form.form-sm.form-2 input {
  border: 1px solid #bdbdbd;
  border-top-left-radius: 0.25rem;
  border-bottom-left-radius: 0.25rem;
}
.input-group.md-form.form-sm.form-2 input.amber-border {
  border: 1px solid orange;
}
select.form-control {
  border-right: 1px solid orange;
}
.searchinput {
  border: 2px solid orange;
  border-radius: 100px;
}
input::placeholder {
  font-size: small;
}
</style>