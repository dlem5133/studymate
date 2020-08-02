<template>
  <div id="app">
    <div class="book-container">
    
    <div v-if="!isLoggedIn" class="d-flex inline justify-content-center">
        <b-nav-item @click="openModal(loginmodal)" class="login-btn  mx-3">Login</b-nav-item>
        <LoginModal v-if="loginmodal" @close="closeModal" />
        <b-nav-item @click="openList()" class="login-btn  mx-3">둘러보기</b-nav-item>
    </div>
    
    </div>
  </div>
</template>

<script src="https://use.fontawesome.com/releases/v5.2.0/js/all.js"></script>  
<script>
import "../assets/css/post.scss";
import axios from "axios";
import { VueperSlides, VueperSlide } from "vueperslides";
import "vueperslides/dist/vueperslides.css";
import LoginModal from "../modal/LoginModal";
import constants from "../lib/constants";
import Header from "../components/common/Header.vue";
import Footer from "../components/common/Footer.vue";
export default {
  name: "App",
  components: {
    Header,
    Footer,
    LoginModal,
  },
  created() {
    let url = this.$route.name;

    this.checkUrl(url);
  },
  watch: {
    $route(to) {
      this.checkUrl(to.name);
    }
  },
  methods: {
    openList() {
      this.$router.push({
        name: constants.URL_TYPE.POST.MAIN,
      });
    },
     openModal() {
      this.loginmodal = true;
    },
    closeModal() {
      this.loginmodal = false;
    },
    checkUrl(url) {
      // let array = [constants.URL_TYPE.USER.LOGIN, constants.URL_TYPE.USER.JOIN];
      let array = [
        constants.URL_TYPE.USER.LOGIN,
        constants.URL_TYPE.USER.JOIN,
        constants.URL_TYPE.USER.UPDATE,
        constants.URL_TYPE.USER.DELETE,
        constants.URL_TYPE.USER.FINDPASSWORD,
        constants.URL_TYPE.ERROR.PAGENOTFOUND
      ];

      let isHeader = falseq;
      let isFooter = true;
      array.map(path => {
        if (url === path) {
          isHeader = false;
          isFooter = false;
        }
      });
      this.isHeader = isHeader;
      this.isFooter = isFooter;
    }
  },
  data: function() {
    return {
      isHeader: false,
      isFooter:true,
      constants,
      isLoggedIn: false,
      loginmodal: false,
    };
  }
};
</script>

<style>
body {
  background-image: url('../assets/img/15944.jpg');
   background-repeat: no-repeat;
  background-attachment: fixed;
  background-size: cover;
}
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
