<template>
  <div class="container">
    <img src="../../assets/img/owl_logo.png" alt="로고" style="width: 250px; margin-top: 50px;">
    <div class="d-flex justify-content-center">
      <b-card title="회원가입" style="width: 500px;" class="my-5">
        <div v-if="page == 0">
          <b-card-text>
            <b-form-group label="이메일" label-for="input-email">
              <b-card class="baseInput" no-body id="input-email">{{$route.query.email}}</b-card>
            </b-form-group>
            <b-form-group label="비밀번호" label-for="input-password">
              <b-form-input type="password" v-model="signupData.password" id="input-password"></b-form-input>
            </b-form-group>
            <b-form-group label="비밀번호 확인" label-for="input-passwordConfirm">
              <b-form-input type="password" v-model="signupData.passwordConfirm" id="input-passwordConfirm"></b-form-input>
            </b-form-group>
          </b-card-text>
        </div>
        <div v-if="page == 1">
          <b-card-text>
            <b-form-group label="닉네임" label-for="input-nickname">
              <b-card class="baseInput" no-body id="input-nickname">{{$route.query.nickname}}</b-card>
            </b-form-group>
            <b-form-group label="자기소개" label-for="input-intro">
              <b-form-textarea id="input-intro" v-model="signupData.intro" rows="3" max-rows="6"></b-form-textarea>
            </b-form-group>
          </b-card-text>
        </div>
        <div v-if="page == 2">
          <b-card-text>
            <div v-if="!signupData.profile_image">
              <h2>Select an image</h2>
              <input type="file" @change="onChangeImages">
            </div>
            <div v-else>
              <img :src="signupData.profile_image" style="width: 100%;"/>
              <button @click="removeImage">Remove image</button>
            </div>
          </b-card-text>
        </div>

        <div class="d-flex justify-content-between">
          <b-button v-if="0 < page && page < 3" @click="pageSwitch(-1)" pill>이전</b-button>
          <b-button v-if="page < 2" @click="pageSwitch(1)" pill variant="info">다음</b-button>
          <b-button v-if="page == 2" @click="doSign" pill variant="success">제출</b-button>
        </div>
      </b-card>

    </div>
  </div>
</template>

<script>
// import constants from "../lib/constants";
import axios from "axios";

const SERVER_URL = 'http://localhost:8080'

export default {
  name: "Signup",
  data: () => {
    return {
      page: 0,
      signupData: {
        email: "",
        nickname: "",
        password: "",
        passwordConfirm: "",
        intro: "",
        profile_image: "",
      },
    };
  },
  methods: {
    pageSwitch(n) {
      this.page = this.page + n
    },
    onChangeImages(e) {
      const selectedImage = e.target.files[0];
      this.createBase64Image(selectedImage);
    },
    createBase64Image(fileObject) {
      this.signupData.profile_image = new Image();
      const reader = new FileReader();
      reader.onload = (e) => {
        this.signupData.profile_image = e.target.result;
      };
      reader.readAsDataURL(fileObject);
    },
    removeImage: function (e) {
      this.signupData.profile_image = '';
    },
    doSign() {
      this.signupData.email = this.$route.query.email
      this.signupData.nickname = this.$route.query.nickname
      axios
        .post(SERVER_URL + "/account/signup", this.signupData)
        .then(res => {
          alert("회원가입되었습니다.");
          console.log(this.signupData)
          this.$router.push("/user/join/confirm");
        })
        .catch(err => {
          console.log(err.response);
        });
    },
  },
}
</script>

<style scope>
.baseInput {
  text-align: left;
  height: 38px;
  padding: 5px;
  padding-left: 15px;
}
</style>