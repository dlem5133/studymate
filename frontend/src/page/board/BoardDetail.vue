<template>
    <div style="margin-top:6rem;" class="container">
      <div class="container">
        <div class="card">
          <div class="p-4">
            <div class="d-flex">
              <h5 class="font-weight-bold text-left p-0 m-0">{{ boardDetailData.title }}</h5>
              <div class="d-flex ml-auto">
                <div class="d-flex my-auto" v-if="profileInfo.uid==boardDetailData.uid">
                  <button style="font-family:'Do Hyeon',sans-serif;" type="button" @click="boardUpdate(boardDetailData.bid)" class="mr-2 btn btn-outline-primary btn-sm btn-rounded waves-effect">수정</button>
                  <button style="font-family:'Do Hyeon',sans-serif;" type="button" @click="boardDelete(boardDetailData.bid)" class="mr-2 btn btn-outline-danger btn-sm btn-rounded waves-effect">삭제</button>
                </div>
                <b-icon class="my-auto" @click="goBoardMain(boardDetailData.pid, boardDetailData.bid)" icon="house-door"></b-icon>
              </div>
            </div>
              <small class="float-left mt-1"> {{boardDetailData.user.nickname}}, 
                {{boardDetailData.date.substring(0,10)}}
              </small>
          </div>
          <hr class="m-0">
          <p style="min-height:58vh;" class="card-text text-left m-2 p-4">{{boardDetailData.body}}</p>
        </div>      
      </div>
    </div>
</template>

<script>
import axios from "axios";
import constants from "../../lib/constants";

const SERVER_URL =  constants.ServerUrl;

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

<style scoped>

</style>