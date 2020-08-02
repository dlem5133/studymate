<template>
  <div class="container">
    <div class="container p-5">
      <h1 class="mb-5">BOARD</h1>
      <!-- v-if="studyLists.empId.study.uid==profileInfo.uid" -->
      <div >
        <b-form-checkbox class="ml-4" v-model="isNotice" value="1" unchecked-value="0">공지</b-form-checkbox>
      </div>
      <div class="d-flex my-4">
        <p class="h5 m-0 p-0 my-auto ">제목</p>
        <b-form-input class="border-bottom my-auto ml-4" v-model="text" placeholder="제목을 입력해주세요"></b-form-input>
      </div>
      <div class="d-flex my-4">
        <p class="h5 m-0 p-0">내용</p>
        <b-form-textarea class="border-bottom my-auto ml-4" v-model="content" placeholder="내용을 입력해주세요" rows="8"></b-form-textarea>
      </div>
      <div class="p-3">
        <b-button variant="outline-success" @click="submitBoard">SUBMIT</b-button>
        <!-- <b-button class="ml-2" variant="outline-secondary" @click="goBack(boardDetailData.pid)">취소</b-button> -->
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import constants from "../../lib/constants";

const SERVER_URL = "http://localhost:8080";

export default {
  name: "boardCreate",
  data: () => {
    return {
      profileInfo: [],
      boardDetailData: [],
      text: "",
      content: "",
      isNotice: 0,
    };
  },
  created() {
    this.addprofileInfo();
    this.getboardDetailData()
  },
  methods: {
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
            console.log(this.profileInfo)
          })
          .catch((err) => {
            this.$router.push({
              name: constants.URL_TYPE.ERROR.ERRORPAGE,
              params: { code: err.response.data },
            });
          });
      }
    },
    getboardDetailData() {
      const board_id = this.$route.params.board_id
      axios.get(SERVER_URL + "/study/board/details", { params: { bid: board_id }})
      .then(res => {
        this.boardDetailData = res.data.object
      })
      .catch(err => {
        console.log(err);
      })
    },
    goBack(post_id) {
      this.$router.push({
        name: constants.URL_TYPE.BOARD.BOARDMAIN,
        params: { post_id: post_id },
      });
    },
    submitBoard() {
      const boardData = {
        title: this.text,
        body: this.content,
        uid: this.profileInfo.uid,
        pid: this.$route.params.post_id,
        isnotice: this.isNotice,
      };
      axios
        .post(SERVER_URL + "/study/board/write", boardData)
        .then((res) => {
          this.$router.push({
            name: constants.URL_TYPE.BOARD.BOARDMAIN,
            params: { post_id: this.$route.params.post_id },
          });
        })
        .catch((err) => {
          console.log(err);
        });
    },
  },
};
</script>

<style scoped>
.clickstudy {
  cursor: pointer;
}
label {
  font-size: 20px;
}
</style>