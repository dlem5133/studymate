<template>
  <div class="container p-5">
    <b-form-input
      class="my-2"
      v-model="preData.title"
      placeholder="제목"
    ></b-form-input>
    <editor v-if="preData.body != null" :initialValue="preData.body" ref="toastuiEditor" />
    <div class="d-flex inline justify-content-center">
      <div class="p-3">
        <div @click="submitDaily(1)" class="btn btn-warning btn-sm">
          작성하기
        </div>
      </div>
      <div class="p-3">
        <div class="btn btn-primary btn-sm">
          <span @click="submitDaily(0)">임시저장 </span>
          <b-badge variant="light" id="show-btn" @click="$bvModal.show('bv-modal-example')">{{tmpDailyData.length}}</b-badge>
        </div>
      </div>
      <b-modal id="bv-modal-example" hide-footer>
        <template v-slot:modal-title>
          임시저장중인 리스트
        </template>
        <div class="card" v-for="tmpdaily in tmpDailyData" :key="tmpdaily.did">
          <div v-if="tmpdaily.did != $route.params.daily_id" class="card-body d-flex justify-content-between">
            <p>{{ tmpdaily.title }}</p>
            <p @click="$bvModal.hide('bv-modal-example')">
              {{ tmpdaily.posttime }}
              <b-button @click="continueWrite(tmpdaily.pid, tmpdaily.did)" variant="outline-success">작성</b-button>
            </p>
          </div>
        </div>
        <b-button class="mt-3" block @click="$bvModal.hide('bv-modal-example')">닫기</b-button>
      </b-modal>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import constants from "../../lib/constants";
import "codemirror/lib/codemirror.css";
import "@toast-ui/editor/dist/toastui-editor.css";
import codeSyntaxHighlight from "@toast-ui/editor-plugin-code-syntax-highlight";
import 'highlight.js/styles/github.css';
import hljs from 'highlight.js';
import { Editor } from "@toast-ui/vue-editor";
const SERVER_URL = constants.ServerUrl;
const baekjoon = "/백준";
const swea = "/swea";
const git = "/깃";
export default {
  name: "DailyUpdate",
  components: {
    editor: Editor,
  },
  data: () => {
    return {
        editorOptions: {
          hideModeSwitch: true,
          placeholder: '내용을 입력해주세요.',
          initialEditType:"markdown",
          previewStyle:"vertical",
          plugins: [[codeSyntaxHighlight, { hljs }]],
                  extendedAutolinks: (content) => {
          //현재 문제 약 20000번 까지 있음
          //백준1800 입력하면 1800번으로 감
          for (let index = 0; index < 20000; index++) {
            var newone = baekjoon;
            newone += index;
            const matched = content.match(newone);
            if (content === newone) {
              return matched.map((m) => ({
                text: newone.substring(1,3)+ " " + newone.substring(3)+"번",
                url: "https://www.acmicpc.net/problem/" + index,
                range: [0, 8],
              }));
            }
            // /백준
            else if (content === baekjoon) {
              const matched = content.match(baekjoon);
              if (matched) {
                return matched.map((m) => ({
                  text: "백준",
                  url: "https://www.acmicpc.net",
                  range: [0, 3],
                }));
              }
            }
            // /SWEA
            else if (content === swea) {
              const matched = content.match(swea);
              if (matched) {
                return matched.map((m) => ({
                  text: "SWEA",
                  url: "https://swexpertacademy.com/main/main.do",
                  range: [0, 4],
                }));
              }
            }
            else if (content === git) {
              const matched = content.match(git);
              if (matched) {
                return matched.map((m) => ({
                  text: "깃허브",
                  url: "https://github.com/",
                  range: [0, 4],
                }));
              }
            }
          }
          return null;
        },
        },
      profileInfo: [],
      studyLists: [],
      tmpDailyData: [],
      preData: [],
      body: '',
    };
  },
  created() {
    this.getPreData();
    this.addprofileInfo();
    this.checkUser();
    this.getTmpDaily();
  },
  methods: {
    checkUser() {
      setTimeout(() => {
        if (this.preData.uid && this.profileInfo.uid) {
          if (this.preData.uid !== this.profileInfo.uid) {
            alert("작성자만 수정 할 수 있습니다.");
            this.$router.push({
              name: constants.URL_TYPE.STUDY.DAILYDETAIL,
              params: {
                post_id: this.$route.params.post_id,
                daily_id: this.$route.params.daily_id,
              },
            });
          }
        } else this.checkUser();
      }, 100);
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
    getPreData() {
      axios
        .get(SERVER_URL + "/diary/details", {
          params: { did: this.$route.params.daily_id },
        })
        .then((res) => {
          this.preData = res.data.object;
          console.log(this.preData)
        })
        .catch((err) => console.log(err.data));
    },
    submitDaily(tmpN) {
      if (this.text == "") {
        alert("제목을 입력해주세요.");
      } else if (this.$refs.toastuiEditor.invoke("getMarkdown") == ""  ||this.$refs.toastuiEditor.invoke("getMarkdown") == initcontent) {
        alert("새로운 내용을 입력해주세요.");
      } else {
        const dailydData = {
          title: this.preData.title,
          body: this.$refs.toastuiEditor.invoke("getMarkdown"),
          pid: this.$route.params.post_id,
          did: this.$route.params.daily_id,
          uid: this.profileInfo.uid,
          tmp: tmpN,
        };
        axios
          .post(SERVER_URL + "/diary/update", dailydData)
          .then((res) => {
            this.$router.push({
              name: constants.URL_TYPE.STUDY.DAILYDETAIL,
              params: {
                post_id: this.$route.params.post_id,
              },
            });
          })
          .catch((err) => {
            console.log(err);
          });
      }
    },
    getTmpDaily() {
      axios
        .get(SERVER_URL + "/diary/list", {
          params: { pid: this.$route.params.post_id, tmp: 0 },
        })
        .then((res) => {
          const data = res.data.object[0];
          this.tmpDailyData = data.filter(
            (data) => data.uid === this.profileInfo.uid
          );
        })
        .catch((err) => {
          console.log(err);
        });
    },
    continueWrite(post_id, daily_id) {
      this.$router.push({
        name: constants.URL_TYPE.STUDY.DAILYUPDATE,
        params: { post_id: post_id, daily_id: daily_id },
      });
      this.getPreData();
      this.addprofileInfo();
      this.checkUser();
      this.getTmpDaily();
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
