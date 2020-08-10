<template>
  <div>
    <div id="mainimg">
      <div class="main-text">
        <h4 style="font-family: 'Do Hyeon', sans-serif;color:rgba(255,255,255,0.6);">안녕하세요 스터디메이트에 방문하신걸 환영합니다 :)</h4>
        <h4 style="font-family: 'Do Hyeon', sans-serif;color:rgba(0,255,255,0.6);">지금 페이지마다 헤더높이 안되있어서 위에 붙어있을거야....고칠거야...</h4>        
        <h4>
        <b-icon icon="mouse" variant="warning" class="mt-4" @click="moveBottom"></b-icon>
        </h4>
      </div>
    </div>
    <div class="container ">
    <div id="maintext" class="row row-cols-2">
      <div id="maintable" class="main-table mt-5 col-12 col-md-12">

        <!-- ===========================================================================================  -->
          <div class="d-flex justify-content-center">
            <div v-for="category in categoryList" :key="category">
              <input
                type="radio"
                v-model="selected.category"
                :value="`${category}`"
                :id="`${category}`"
                name="rb"
                @click="searchData"
              />
              <label :for="`${category}`">{{ category }}</label>
            </div>
          </div>

          <!-- =======================================아래 수정====================================================  -->
          <form class="input-groups flex-nowrap py-4 d-flex justify-content-center">
            <div class="serach-selectbox">            
              <select v-model="selected.what" @click="searchData" class="form-control">
                <option value="all" > 전체 </option>
                <option value="title"> 스터디명 </option>
                <option value="area"> 지역</option>
                <option value="tag"> 태그</option>
              </select>
            </div>

            <div class="search">
              <div class="input-group md-form form-sm form-2 pl-0">
                <input @keyup="searchData" v-model="selected.keyword" class="form-control my-0 py-1 amber-border" type="text" placeholder="검색어를 입력해주세요" aria-label="Search">
                  <button class="input-group-text" id="addon-wrapping">
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
            :key="list[0].pid"
          >
            <li
              v-if="i < `${scrolled}`"
              class="booking-card"
              :style="{ backgroundImage: `url(${list[0].background_image})` }"
            >
              <div class="book-container">
                <div class="content">
                  <button
                    class="btn"
                    @click="goDetail(list[0].pid)"
                    :key="list[0].pid"
                  >
                    Detail
                  </button>
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
                      <img
                        class="infopplbindo"
                        src="../../assets/img/person.png"
                      />
                      2 / {{ list[0].limitp }} 명<br />
                      <img
                        class="infopplbindo"
                        src="../../assets/img/calendar.png"
                      />
                      주 {{ list[0].bindo }} 회 <br />
                      <img
                        class="infopplbindo"
                        src="../../assets/img/navi.png"
                      />
                      <span v-if="list[0].sido.sidoname=='온라인'">
                      {{list[0].sido.sidoname}}</span>
                      <span v-if="list[0].sido.sidoname!='온라인' && list[0].gugun.gugunname != '선택안함'">
                      {{ list[0].sido.sidoname }} {{list[0].gugun.gugunname}}</span>
                      <span v-if="list[0].sido.sidoname!='온라인' && list[0].gugun.gugunname == '선택안함'">
                      {{ list[0].sido.sidoname }} (미정) </span>
                    </div>
                  </div>
                </div>
              </div>
            </li>
          </ul>
        </div>
        <button class="topButton" @click="moveTop">
          <i class="fas fa-arrow-alt-circle-up"></i>
        </button>
      </div>
    </div>
  </div></div>
</template>

<script>
import "../../assets/css/post.scss";
import axios from "axios";
import { VueperSlides, VueperSlide } from "vueperslides";
import "vueperslides/dist/vueperslides.css";
import LoginModal from "../../modal/LoginModal";
import constants from "../../lib/constants";
import { mdbIcon } from 'mdbvue';

const SERVER_URL = constants.ServerUrl;

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
      allStusyList: [],
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
      selected: {
        category: "",
        what: "all",
        keyword: "",
      },
      searchList: [],
      scrolled: 3,
      readyLists: [],
      leaderLists: [],
      unleaderLists: [],
      leaderListsTmp1: [],
      leaderListsTmp0: [],
    };
  },
  created() {
    this.addprofileInfo();
    this.allStusy();
    window.addEventListener("scroll", this.handleScroll);
  },
  beforeDestroy: function() {
    window.removeEventListener("scroll", this.handleScroll);
  },
  methods: {
    allStusy() {
      axios.get(SERVER_URL + "/study/list")
      .then(res => {
        this.allStusyList = res.data.object
        this.searchList = this.allStusyList
      })
      .catch(err => {console.log(err);})
    },
    searchData() {
      var tmpSearch = this.selected
      setTimeout(() => {
        var tmpData = this.allStusyList
        if (tmpSearch.category) {
          tmpData = tmpData.filter(da => da[0].category == tmpSearch.category)
        }
        const word = tmpSearch.keyword
        var tmpData2 = []
        if (tmpSearch.what === "all") {
          for (let i = 0; i < tmpData.length; i++) {
            if (tmpData[i][0].title.includes(word) || tmpData[i][0].gugun.gugunname.includes(word) || tmpData[i][0].sido.sidoname.includes(word)) {
              tmpData2.push(tmpData[i])
            } else {
              for (let j = 0; j < tmpData[i][1].length; j++) {
                if (tmpData[i][1][j].tagname.includes(word)) {
                  tmpData2.push(tmpData[i])
                  break
                }
              }
            }
          }
        } else if (tmpSearch.what === "title") {
          for (let i = 0; i < tmpData.length; i++) {
            if (tmpData[i][0].title.includes(word)) {
              tmpData2.push(tmpData[i])
            }
          }
        } else if (tmpSearch.what === "area") {
          for (let i = 0; i < tmpData.length; i++) {
            if (tmpData[i][0].gugun.gugunname.includes(word) || tmpData[i][0].sido.sidoname.includes(word)) {
              tmpData2.push(tmpData[i])
            }
          }
        } else if (tmpSearch.what === "tag") {
          for (let i = 0; i < tmpData.length; i++) {
            for (let j = 0; j < tmpData[i][1].length; j++) {
              if (tmpData[i][1][j].tagname.includes(word)) {
                tmpData2.push(tmpData[i])
                break
              }
            }
          }
        }
        tmpData = tmpData2
        this.searchList = tmpData
      }, 100);
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
      var offset = d.scrollTop + window.innerHeight + 40;
      var height = d.offsetHeight;

      if (offset >= height) {
        this.scrolled += 3;
      }
    },
    moveTop() {
      var location = document.querySelector("#mainimg").offsetTop;
      window.scrollTo({top:location, behavior:'smooth'});
    },
    moveBottom() {
      var location = document.querySelector("#maintext").offsetTop;
      window.scrollTo({top:location-71.8, behavior:'smooth'});
    },
    
  },
};
</script>

<style scoped>
#mainimg{
  position: absolute;
  margin-top: 0;
  top:0 !important;
  min-width: 100%;
  right:0;
  height:100vh !important;
  line-height: 100vh;
  background-image: url('../../assets/img/main.jpg');
  background-size:cover; 
  z-index: 6;
}
#maintext{
  position: relative;
  top:100vh;
}
.main-text{
  padding-top:40vh;
  background-color: #000000;
  background-color: rgba(0,0,0,0.8);
  height:100vh;
}
input[type="radio"] {
  position: absolute;
  opacity: 0;
  /* z-index: -1; */
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
