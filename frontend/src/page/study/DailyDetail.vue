<template>
  <div class="main-table border">
    <div class="container p-5">
      <div class="card text-left">
        <div class="card-header">
          <h5>{{dailyDetailData.title}}</h5><br />
           {{dailyDetailData.writer}} <small>{{dailyDetailData.posttime}}</small>
        </div>
        <div class="card-body">
          <Viewer v-if="dailyDetailData.body != null" :initialValue="dailyDetailData.body" />
        </div>
      </div>
      <div v-if="profileInfo.uid === dailyDetailData.uid">
        <button type="button" @click="dailyUpdate(dailyDetailData.did)"
          class="m-2 btn btn-outline-primary btn-rounded waves-effect">수정</button>
        <button type="button" @click="dailyDelete(dailyDetailData.did)"
          class="m-2 btn btn-outline-danger btn-rounded waves-effect">삭제</button>
      </div>
    </div>
  </div>
</template>

<script>
  import axios from "axios";
  import constants from "../../lib/constants";
  import "codemirror/lib/codemirror.css";
  import "@toast-ui/editor/dist/toastui-editor.css";
  import {
    Viewer
  } from "@toast-ui/vue-editor";

  const SERVER_URL = constants.ServerUrl;

  export default {
    name: 'dailydetail',
    components: {
    Viewer
  },
    data: () => {
      return {

        profileInfo: [],
        studyLists: [],
        dailyDetailData: [],
      };
    },
    mounted() {
      this.detailData()
    },
    created() {
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
              this.addStudyList();
            })
            .catch((err) => {
              this.$router.push({
                name: constants.URL_TYPE.ERROR.ERRORPAGE,
                params: {
                  code: err.response.data
                },
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
          params: {
            post_id: post_id
          },
        });
      },
      detailData() {
        axios
          .get(SERVER_URL + "/diary/details", {
            params: {
              did: this.$route.params.daily_id
            }
          })
          .then((res) => {
            this.dailyDetailData = res.data.object
          })
          .catch((err) => console.log(err.data));
      },
      dailyUpdate(daily_id) {
        this.$router.push({
          name: constants.URL_TYPE.STUDY.DAILYUPDATE,
          params: {
            post_id: this.$route.params.post_id,
            daily_id: daily_id
          }
        })
      },
      dailyDelete(daily_id) {
        const deleteData = {
          did: this.$route.params.daily_id,
          uid: this.profileInfo.uid
        }
        axios
          .post(SERVER_URL + "/diary/delete", deleteData)
          .then((res) => {
            console.log(res)
            alert("삭제되었습니다.")
            this.$router.push({
              name: constants.URL_TYPE.STUDY.STUDYMAIN,
              params: {
                post_id: this.$route.params.post_id
              },
            });
          })
          .catch((err) => {
            console.log(err.data)
          });
      },
    },
  }
</script>

<style>
  .clickstudy {
    cursor: pointer;
  }
</style>