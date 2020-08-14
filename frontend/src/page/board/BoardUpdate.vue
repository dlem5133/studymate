<template>
  <div style="margin-top:6rem;" class="mb-5 container">
    <div class="container">
      <img :src="images" class="logo w-25" alt />
      <div class="text-left ml-5" v-if="postDetailData.uid==profileInfo.uid">
        <b-form-checkbox class="ml-4" v-model="boardData.isNotice" value="1" unchecked-value="0">
          <small style="font-family:'Do Hyeon', sans-serif;">공지</small>
        </b-form-checkbox>
      </div>
      <b-row class="px-5">
        <b-col sm="12" class="px-1">
          <small class="formtitle ml-3 float-left">제목</small>
          <b-form-input v-model="boardData.title" placeholder="제목" type="text"></b-form-input>
        </b-col>
        <b-col sm="12" class="px-1">
          <small class="formtitle ml-3 float-left">내용</small>
          <b-form-textarea
            style="resize:none;"
            class="border-bottom my-auto"
            v-model="boardData.body"
            placeholder="내용을 입력해주세요"
            rows="8"
          ></b-form-textarea>
        </b-col>
      </b-row>
      <div class="p-3">
        <b-button
          class="bubut"
          variant="warning"
          @click="updateBoard(boardData.bid)"
          style="border:1px solid orange;font-family:'Do Hyeon',sans-serif;"
        >SUBMIT</b-button>
        <b-button
          class="ml-2"
          variant="outline-secondary"
          style="font-family:'Do Hyeon',sans-serif;"
          @click="goBoardDetail(boardData.bid)"
        >CANCEL</b-button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import constants from "../../lib/constants";

const SERVER_URL = constants.ServerUrl;

export default {
  name: "BoardUpdate",
  data: () => {
    return {
      images: require("../../assets/img/logo.png"),
      profileInfo: [],
      boardData: [],
      postDetailData: [],
      isNotice: 0,
    };
  },
  created() {
    this.addprofileInfo();
    this.getboardData();
    this.getpostDetailData();
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
    getboardData() {
      const board_id = this.$route.params.board_id;
      axios
        .get(SERVER_URL + "/study/board/details", { params: { bid: board_id } })
        .then((res) => {
          this.boardData = res.data.object;
        })
        .catch((err) => {
          console.log(err);
        });
    },
    getpostDetailData() {
      const post_id = this.$route.params.post_id;
      console.log(post_id);
      axios
        .get(SERVER_URL + "/study/details", { params: { pid: post_id } })
        .then((res) => {
          this.postDetailData = res.data.object[0];
          console.log(this.postDetailData);
        })
        .catch((err) => {
          console.log(err);
        });
    },
    updateBoard(board_id) {
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
    goBoardDetail(board_id) {
      this.$router.push({
        name: constants.URL_TYPE.BOARD.BOARDDETAIL,
        params: { post_id: this.$route.params.post_id, board_id: board_id },
      });
    },
  },
};
</script>

<style scoped>
.bubut {
  color: white;
}
.bubut:hover {
  color: white;
}
.clickstudy {
  cursor: pointer;
}
label {
  position: relative;
  right: 40%;
  top: 5px;
  font-size: small;
}
.submit-btn {
  padding: 7px 35px;
  border-radius: 60px;
  display: inline-block;
  background-color: #4b8cfb;
  color: white;
  font-size: 18px;
  cursor: pointer;
}
.formtitle {
  padding: 0 4px;
  transform: translateY(5px);
  background-color: white;
  font-weight: bolder;
}
.formtitle1 {
  padding: 0 4px;
  transform: translateX(30px) translateY(5px);
  background-color: white;
  font-weight: bolder;
}
.logo {
  width: 7vw;
}
</style>