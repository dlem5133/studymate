<template>
  <div class="container">
      <div class="p-5">
        <label>제목</label>
        <b-form-input class="my-2" v-model="boardData.title" placeholder="제목"></b-form-input>
        <label>내용</label>
        <b-form-textarea class="my-2" v-model="boardData.body" placeholder="게시할 내용을 입력해주세요" rows="8"></b-form-textarea>
        <b-form-checkbox v-model="boardData.isnotice" value="1" unchecked-value="0">Top</b-form-checkbox>
        <div class="p-3">
          <div @click="updateBoard(boardData.bid)" class="btn btn-primary btn-sm">작성</div>
        </div>
      </div>
    </div>
</template>

<script>
import axios from "axios";
import constants from "../../lib/constants";

const SERVER_URL =  constants.ServerUrl;

export default {
  name: "BoardUpdate",
  data: () => {
    return {
      profileInfo: [],
      boardData: [],
      isNotice:0,
    };
  },
  created() {
    this.addprofileInfo();
    this.getboardData();
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
          })
          .catch((err) => {
            this.$router.push({
              name: constants.URL_TYPE.ERROR.ERRORPAGE,
              params: { code: err.response.data },
            });
          });
      }
    },
    goStudyMain(post_id) {
      this.$router.push({
        name: constants.URL_TYPE.STUDY.STUDYMAIN,
        params: { post_id: post_id },
      });
    },
    getboardData() {
      const board_id = this.$route.params.board_id
      axios.get(SERVER_URL + "/study/board/details", { params: { bid: board_id }})
      .then(res => {
        this.boardData = res.data.object
      })
      .catch(err => {
        console.log(err);
      })
    },
    updateBoard(board_id) {
      console.log(this.boardData)
      axios
      .post(SERVER_URL + "/study/board/update", this.boardData)
      .then((res) => {
        this.$router.push({
          name: constants.URL_TYPE.BOARD.BOARDDETAIL,
          params: { post_id: this.$route.params.post_id, board_id: board_id },
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