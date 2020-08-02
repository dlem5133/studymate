<template>
  <div id="header" v-if="isHeader">
    <b-navbar id="navbar" toggleable="lg" type="light" variant="light" class="px-auto sticky-top row shadow">
      <b-navbar-brand tag="router-link" :to="{name:constants.URL_TYPE.POST.MAIN}" class="ml-3 font-weight-bold text-monospace">StudyMate</b-navbar-brand>
        <b-navbar-nav class="d-flex ml-auto">
          <div v-if="!isLoggedIn" class="d-flex inline justify-content-center">
            <b-nav-item @click="openModal" class="login-btn  mx-3">Login</b-nav-item>
            <LoginModal v-if="loginmodal" @close="closeModal" />
            <b-nav-item @click="goSignup" class="login-btn mx-3 ">Signup</b-nav-item>
          </div>
          <div v-if="isLoggedIn" class="d-flex inline justify-content-center">
            <b-nav-item tag="router-link" to="/user/profile" class="login-btn mx-3">프로필</b-nav-item>
            <b-nav-item tag="router-link" to="/post/create" class="login-btn mx-3">모집생성</b-nav-item>
            <!-- <b-nav-item tag="router-link" :to="{name:constants.URL_TYPE.USER.UPDATE}" class="login-btn mx-3">회원수정</b-nav-item>
            <b-nav-item tag="router-link" :to="{name:constants.URL_TYPE.USER.DELETE}" class="login-btn mx-3">회원탈퇴</b-nav-item> -->
            <b-nav-item @click="logout" class="mx-3 login-btn">Logout</b-nav-item>
          </div>
        </b-navbar-nav>
    </b-navbar>
  </div>
</template>   

<script>
import constants from "../../lib/constants";
import LoginModal from "../../modal/LoginModal";
import axios from "axios";

const SERVER_URL = 'http://localhost:8080'

export default {
  name: "Header",
  components: {
    LoginModal,
  },
  props: ["isHeader"],
  computed: {},
  watch: {},
  created() {
    this.tokencheck();
  },
  methods: {
    logout() {
      this.$cookies.remove("Auth-Token");
      this.$router.push("/");
      this.$router.go();
      this.tokencheck();
    },
    tokencheck() {
      this.isLoggedIn = this.$cookies.isKey("Auth-Token");
    },
    openModal() {
      this.loginmodal = true;
    },
    closeModal() {
      this.loginmodal = false;
    },
    goSignup() {
      this.$router.push({name: constants.URL_TYPE.USER.SIGNUP})
    }
  },
  data: function() {
    return {
      constants,
      loginmodal: false,
      isLoggedIn: false
    };
  }
};
</script>

<style scoped>
#navbar {
  margin: 0;
  padding: 10px;
  /* height:60px; */
  }
</style>