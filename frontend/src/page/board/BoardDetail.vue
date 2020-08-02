<template>
    <div class="container">
      <div class="card shadow p-3">
        <div class="d-flex my-2">
          <div>
            <h2 class="font-weight-bol                            d text-left pl-3 p-0 m-0">{{ boardDetailData.title }}</h2>
            <p class="pt-1 pl-3 p-0 m-0"> {{boardDetailData.user.nickname}}, 
                {{boardDetailData.date.substring(0,10)}}</p>
          </div>
          <b-button class="my-auto ml-auto" variant="border-0" @click="goBoardMain(boardDetailData.pid, boardDetailData.bid)"><b-icon icon="backspace"></b-icon></b-button>
        </div>
        <hr>
        <p style="height:80vh;" class=" border rounded card-text text-left m-2 p-4">{{boardDetailData.body}}</p>

        <div v-if="profileInfo.uid==boardDetailData.uid">
          <button type="button" @click="boardUpdate(boardDetailData.bid)" class="m-2 btn btn-outline-primary btn-rounded waves-effect">수정</button>
          <button type="button" @click="boardDelete(boardDetailData.bid)" class="m-2 btn btn-outline-danger btn-rounded waves-effect">삭제</button>
        </div>
      </div>
    </div>
</template>

<script>
import axios from "axios";
import constants from "../../lib/constants";

const SERVER_URL = "http://localhost:8080";

export default {
  name: 'BoardDetail',
    data: () => {
    return {
      profileInfo: [],
      boardDetailData: [],
    };
  },
  created() {
    this.getboardDetailData()
    this.addprofileInfo();
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
    goBoardMain(post_id, board_id){
      this.$router.push({
        name: constants.URL_TYPE.BOARD.BOARDMAIN,
        params: { post_id: post_id, board_id:board_id },
      });
    },
    boardUpdate(board_id) {
      this.$router.push({
        name: constants.URL_TYPE.BOARD.BOARDUPDATE,
        params: { post_id: this.$route.params.post_id, board_id: board_id }
      })
    },
    boardDelete(board_id) {
      const deleteData = {
        bid: board_id,
      }
      axios
        .post(SERVER_URL + "/study/board/delete", deleteData)
        .then((res) => {
          this.$bvToast.toast('삭제되었습니다.', {title: `SUCCESS`, toaster:'b-toaster-top-center', variant: 'success', autoHideDelay: 1000, solid: true})
          this.$router.push({
            name: constants.URL_TYPE.BOARD.BOARDMAIN,
            params: { post_id: this.$route.params.post_id },
          });
        })
        .catch((err) => {
          console.log(err.data)
        });
    },
  }
}
</script>

<style>

</style>