<template>
  <div class="container">
    <div class="row d-flex justify-content-center">
      <div class="center">
        <div class="profile">
          <div class="image">
            <div class="circle-1"></div>
            <div class="circle-2"></div>

            <div v-if="!profileInfo.profile_image">
              <img src="../../assets/img/defualt_image.png" width="70" height="70" />
            </div>
            <div v-else>
              <img :src="profileInfo.profile_image" width="70" height="70" />
            </div>
          </div>

          <div class="name">{{ profileInfo.nickname }}</div>
          <div class="job">{{ profileInfo.email }}</div>

          <div class="actions">
            <b-button class="btn" v-b-modal.modal-1>수정</b-button>
            <b-modal @ok="userUpdate" id="modal-1" title="회원정보 수정">
              <small>닉네임</small>
              <b-form-input v-model="updateData.nickname"></b-form-input>
              <small>이메일</small>
              <b-form-input v-model="updateData.email"></b-form-input>
              <small>비밀번호</small>
              <b-form-input type="password" v-model="updateData.password"></b-form-input>
              <small>자기소개</small>
              <b-form-textarea v-model="updateData.intro" rows="3" max-rows="6"></b-form-textarea>
              <small>프로필</small>
              <div v-if="!updateData.profile_image">
                <h2>Select an image</h2>
                <input type="file" @change="onChangeImages" />
              </div>
              <div v-else>
                <img :src="updateData.profile_image" style="width: 100%;" />
                <button @click="removeImage">Remove image</button>
              </div>
            </b-modal>

            <b-button class="btn" v-b-modal.modal-2>탈퇴</b-button>
            <b-modal @ok="userDelete" id="modal-2" title="회원 탈퇴">
              <div>
                <small>이메일</small>
                <b-form-input v-model="deleteData.email" placeholder="가입 시 작성한 이메일을 입력해주세요"></b-form-input>
              </div>
              <div>
                <small>비밀번호</small>
                <b-form-input
                  type="password"
                  v-model="deleteData.password"
                  placeholder="가입 시 작성한 비밀번호를 입력해주세요"
                ></b-form-input>
              </div>
            </b-modal>
          </div>
        </div>

        <div class="stats">
          <div class="box">
            <span class="value">5</span>
            <span class="parameter">가입중인 스터디</span>
          </div>
          <div class="box">
            <span class="value">1387</span>
            <span class="parameter">마일리지</span>
          </div>
          <div class="box">
            <span class="value">4.6</span>
            <span class="parameter">랭크</span>
          </div>
        </div>
      </div>
      <div class="side">
        <div class="text-left pt-3 col-12">
          <small class="text-success font-weight-bold text-left pl-3">진행중 스터디</small>
          <div
            v-for="list in readyLists"
            :key="list.id"
            class="card m-2 px-2 p-2"
            @click="goStudyMain(list.pid)"
          >
            <div class="d-flex inline">
              <small class="text-left">{{ list.empId.study.title }}</small>
              <b-badge class="ml-auto my-auto" variant="secondary">승인대기중</b-badge>
            </div>
          </div>

          <div
            v-for="list in leaderListsTmp0"
            :key="list.id"
            class="card m-2 px-2 p-2"
            @click="goStudyMain(list.pid)"
          >
            <div class="d-flex inline">
              <small class="text-left">{{ list.empId.study.title }}</small>
              <b-badge class="ml-auto my-auto" variant="danger">진행중&팀장</b-badge>
            </div>
          </div>

          <div
            v-for="list in unleaderLists"
            :key="list.id"
            class="card m-2 px-2 p-2"
            @click="goStudyMain(list.pid)"
          >
            <div class="d-flex inline">
              <small class="text-left">{{ list.empId.study.title }}</small>
              <b-badge class="ml-auto my-auto" variant="success">신청완료</b-badge>
            </div>
          </div>
          <hr />

          <small class="text-warning font-weight-bold text-left pl-3 pb-2">모집중 스터디</small>
          <div
            v-for="list in leaderListsTmp1"
            :key="list.id"
            class="card m-2 px-2 p-2"
            @click="goStudyMain(list.pid)"
          >
            <div class="d-flex inline">
              <small class="text-left">{{ list.empId.study.title }}</small>
              <b-badge class="ml-auto my-auto" variant="warning">모집중&팀장</b-badge>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import "../../assets/css/profile.scss";
import axios from "axios";
import constants from "../../lib/constants";

const SERVER_URL = "http://localhost:8080";

export default {
  name: "profile",
  data: () => {
    return {
      profileInfo: {},
      studyLists: {},
      tmplists: {},
      updateData: {
        email: "",
        nickname: "",
        password: "",
        intro: "",
        profile_image: "",
        uid: "",
      },
      deleteData: {
        email: "",
        password: "",
      },
      readyLists: [],
      leaderLists: [],
      unleaderLists: [],
      leaderListsTmp1: [],
      leaderListsTmp0: [],
    };
  },
  methods: {
    changeDatedata(time) {
      return time.substring(5, 10) + " " + time.substring(11, 19);
    },
    addprofileInfo() {
      const token = this.$cookies.get("Auth-Token");
      axios
        .get(SERVER_URL + "/account/profile", { params: { Token: token } })
        .then((res) => {
          this.profileInfo = res.data.object;
          console.log("pro", this.profileInfo);
          this.updateData.email = res.data.object.email;
          this.updateData.nickname = res.data.object.nickname;
          this.updateData.password = res.data.object.password;
          this.updateData.uid = res.data.object.uid;
          this.updateData.intro = res.data.object.intro;
          this.updateData.profile_image = res.data.object.profile_image;
          this.addTmpList();
          this.addReadyList();
          this.addStudyList();
        })
        .catch((err) => {
          this.$router.push({
            name: constants.URL_TYPE.ERROR.ERRORPAGE,
            params: { code: err.response.data },
          });
        });
    },
    addTmpList() {
      axios
        .post(SERVER_URL + "/account/tmplist", {
          nickname: this.profileInfo.nickname,
        })
        .then((res) => {
          console.log("tmplist", res.data.object);
          this.tmplists = res.data.object;
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
          console.log("studylist", this.leaderLists);
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
          console.log("readylist", res.data.object);
        })
        .catch((err) => {
          console.log(err);
        });
    },

    goStudy(post_id) {
      this.$router.push({
        name: constants.URL_TYPE.POST.POSTDETAIL,
        params: { post_id: post_id },
      });
    },
    goStudyMain(post_id) {
      this.$router.push({
        name: constants.URL_TYPE.STUDY.STUDYMAIN,
        params: { post_id: post_id },
      });
    },
    goTmp(post_id) {
      this.$router.push({
        name: constants.URL_TYPE.POST.POSTUPDATE,
        params: { post_id: post_id },
      });
    },
    onChangeImages(e) {
      const selectedImage = e.target.files[0];
      this.createBase64Image(selectedImage);
    },
    createBase64Image(fileObject) {
      this.updateData.profile_image = new Image();
      const reader = new FileReader();
      reader.onload = (e) => {
        this.updateData.profile_image = e.target.result;
      };
      reader.readAsDataURL(fileObject);

      // const file = e.target.files[0];
      // this.updateData.profile_image = URL.createObjectURL(file);
    },
    removeImage: function (e) {
      this.updateData.profile_image = "";
    },
    userUpdate() {
      axios
        .post(SERVER_URL + "/account/update", this.updateData)
        .then((res) => {
          this.$cookies.remove("Auth-Token");
          const token = res.data.object;
          this.$cookies.set("Auth-Token", token);
          alert("수정되었습니다.");
          this.$router.go();
        })
        .catch((err) => {
          // this.$router.push({name: constants.URL_TYPE.ERROR.ERRORPAGE,params:{'code':err.response.data}});
          console.log(err.response.data.errors[0]);
          alert(err.response.data.errors[0].field + "를 확인해 주세요");
        });
    },
    userDelete() {
      axios
        .post(SERVER_URL + "/account/delete", this.deleteData)
        .then((res) => {
          this.$cookies.remove("Auth-Token");
          alert("회원탈퇴되었습니다.");
          this.$router.push("/");
          // this.$router.go();
        })
        .catch((err) => {
          alert("입력정보를 확인해주세요.");
          console.log(err);
        });
    },
  },
  created() {
    this.addprofileInfo();
  },
};
</script>

<style scoped>
.clickbtn,
.tmplist {
  cursor: pointer;
}
</style>