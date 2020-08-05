<template>
    <b-carousel
      id="carousel-1"
      v-model="slide"
      :interval="4000"
      controls
      indicators
      background="#ababab"
      img-width="100vw"
      img-height="100vh"
      style="text-shadow: 1px 1px 2px #333;"
      @sliding-start="onSlideStart"
      @sliding-end="onSlideEnd"
    >
      <!-- Text slides with image -->
      <b-carousel-slide 
            width="100%"
            height="100%"
        caption="First slide"
        text="Nulla vitae elit libero, a pharetra augue mollis interdum."
        img-src="https://picsum.photos/1024/480/?image=52"
      ></b-carousel-slide>

      <!-- Slides with custom text -->
      <b-carousel-slide 
            width="100%"
            height="100%" img-src="https://picsum.photos/1024/480/?image=54">
        <h1>Hello world!</h1>
      </b-carousel-slide>

      <!-- Slides with image only -->
      <b-carousel-slide
            width="100%"
            height="100%" img-src="https://picsum.photos/1024/480/?image=58"></b-carousel-slide>

      <!-- Slides with img slot -->
      <!-- Note the classes .d-block and .img-fluid to prevent browser default image alignment -->
      <b-carousel-slide>
        <template v-slot:img>
          <img
            class="d-block img-fluid"
            width="100%"
            height="100%"
            src="https://picsum.photos/1024/480/?image=55"
            alt="image slot"
          >
        </template>
      </b-carousel-slide>

      <!-- Slide with blank fluid image to maintain slide aspect ratio -->
      <b-carousel-slide caption="Blank Image" img-blank img-alt="Blank image">
        <p>
          Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse eros felis, tincidunt
          a tincidunt eget, convallis vel est. Ut pellentesque ut lacus vel interdum.
        </p>
      </b-carousel-slide>
    </b-carousel>
    <!-- <div class="book-container">
    
    <div v-if="!isLoggedIn" class="d-flex inline justify-content-center">
        <b-nav-item @click="openModal(loginmodal)" class="login-btn  mx-3">Login</b-nav-item>
        <LoginModal v-if="loginmodal" @close="closeModal" />
        <b-nav-item @click="openList()" class="login-btn  mx-3">둘러보기</b-nav-item>
    </div>
    
    </div> -->
</template>

<script>
import "../assets/css/post.scss";
import axios from "axios";
// import { VueperSlides, VueperSlide } from "vueperslides";
// import "vueperslides/dist/vueperslides.css";
import LoginModal from "../modal/LoginModal";
import constants from "../lib/constants";

export default {
  name: "App",
  components: {
    LoginModal,
  },
  created() {},
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
    onSlideStart(slide) {
      this.sliding = true
    },
    onSlideEnd(slide) {
      this.sliding = false
    }
  },
  data() {
    return {
      slide: 0,
      sliding: null,
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
}

/* *{
  margin-top:0 !important;
} */
</style>
