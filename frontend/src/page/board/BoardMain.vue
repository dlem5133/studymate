<template>
  <div class="container">
    <div class="my-5 d-flex">
       <!-- style="color:#FFB74D;" -->
      <h2 class="font-weight-bold ml-3">{{ postData.title }}</h2>
      <b-button class="my-auto" variant="border-0" @click="goStudyMain(postData.pid)"><b-icon icon="house"></b-icon></b-button>
      <b-button class="my-auto ml-auto" variant="border-0" @click="newBoard(postData.pid)"><b-icon icon="pen"></b-icon> WRITE</b-button>
    </div>
    <div class="card border-0">
      <div class="table-responsive">
        <table class="table align-items-center table-flush">
          <thead style="">
            <tr>
              <th scope="col">번호</th>
              <th scope="col">제목</th>
              <th scope="col">날짜</th>
              <th scope="col">작성자</th>
            </tr>
          </thead>
          <tbody>
            <tr :class="isNotice(boardItem.isnotice)" style="cursor:pointer;" @click="goBoardDetail(boardItem.bid)" v-for="(boardItem, i) in pageOfItems" :key="boardItem.bid">
              <th scope="row" v-if="boardItem.isnotice==1"><b-badge variant="warning" class=" blinking">공지</b-badge></th>
              <th scope="row" v-else>{{i+1}}</th>
              <td>{{boardItem.title}}</td>
              <td>{{boardItem.date.substring(0, 10)}}</td>
              <td>{{boardItem.user.nickname}}</td>
            </tr>
          </tbody>
        </table>
      </div>
      <jw-pagination class="mx-auto " :items="boardItems" @changePage="onChangePage"></jw-pagination>
        
      <div class="input-group flex-nowrap mx-auto my-3">
        <div class="d-flex mx-auto border rounded w-75">
          <input v-model="searchText" type="text" class="border-0 form-control" placeholder="Search">
          <div class="input-group-prepend" @click="boardSearch">
            <b-button style="cursor: pointer;" variant="border-0"><b-icon icon="search"></b-icon></b-button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import constants from "../../lib/constants";

const SERVER_URL = "http://localhost:8080";

export default {
  name: "BoardMain",
  data: () => {
    return {
      profileInfo: [],
      studyLists: [],
      postData: [],
      boardItems: [],
      pageOfItems: [],
      searchText: "",
    };
  },
  mounted() {
    },
  created() {
    this.addprofileInfo();
    this.getBoardItems();
    this.getDetail();
  },
  methods: {
    isNotice(notice){
      if (notice==1) {
        return "boardtablenotice"
      } else {
        return "boardtable"
      }
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
    addStudyList() {
      axios
        .post(SERVER_URL + "/account/studylist", {
          email: this.profileInfo.email,
          nickname: this.profileInfo.nickname,
          password: "1234qwer",
        })
        .then((res) => {
          this.studyLists = res.data.object;
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
    getDetail() {
      const post_id = this.$route.params.post_id;
      axios
        .get(SERVER_URL + "/study/details", { params: { pid: post_id } })
        .then((res) => {
          this.postData = res.data.object[0];
        })
        .catch((err) => console.log(err.data));
    },
    getBoardItems() {
      const post_id = this.$route.params.post_id;
      axios
      .get(SERVER_URL + "/study/board/list", { params: { pid: post_id } })
      .then((res) => {
        this.boardItems = res.data.object
        this.boardItems.sort((a,b)=>{return a.date>b.date ? -1 : a.date<b.date ? 1 : 0;})
        this.boardItems.sort((a,b)=>{return a.isnotice>b.isnotice ? -1 : a.isnotice<b.isnotice ? 1 : 0;})
      })
      .catch((err) => {
        console.log(err);
      });
    },
    newBoard(post_id) {
      this.$router.push({
        name: constants.URL_TYPE.BOARD.BOARDCREATE,
        params: { post_id: post_id },
      });
    },
    goBoardDetail(board_id) {
      this.$router.push({
        name: constants.URL_TYPE.BOARD.BOARDDETAIL,
        params: { post_id: this.$route.params.post_id, board_id: board_id },
      });
    },
    onChangePage(pageOfItems) {
      this.pageOfItems = pageOfItems;
    },
    boardSearch() {
      axios.get(SERVER_URL + "/study/board/search", { params: { text: this.searchText} })
      .then(res => {
        this.boardItems = res.data.object
      })
      .catch(err => {
        console.log(err);
      })
    }
  },
};
</script>

<style scoped>
.boardtable:hover {
  background-color: #ddd;
}
.boardtablenotice {
  background-color:#FFF3E0;
}
.boardtablenotice:hover {
  background-color: #FFE0B2;
}
.blinking{
	-webkit-animation:blink 0.5s ease-in-out infinite alternate;
    -moz-animation:blink 0.5s ease-in-out infinite alternate;
    animation:blink 0.5s ease-in-out infinite alternate;
}
@-webkit-keyframes blink{
    0% {opacity:0;}
    100% {opacity:1;}
}
@-moz-keyframes blink{
    0% {opacity:0;}
    100% {opacity:1;}
}
@keyframes blink{
    0% {opacity:0;}
    100% {opacity:1;}
}
</style>