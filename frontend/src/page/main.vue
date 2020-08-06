<template>
<div>
    <b-carousel
      id="carousel-1" 
      v-model="slide"
      :interval="4000"
      controls
      indicators
      background="#ababab"
      img-width="100%"
      img-height="100%"
      style="text-shadow: 1px 1px 2px #333;"
      @sliding-start="onSlideStart"
      @sliding-end="onSlideEnd"
      class="d-none d-md-block test"
    >
      <!-- Text slides with image -->
      <!-- <b-carousel-slide 
            width="100%"
            height="100%"
        caption="First slide"
        text="Nulla vitae elit libero, a pharetra augue mollis interdum."
        img-src="../../assets/img/main.jpg">
      ></b-carousel-slide> -->

      <!-- Slides with custom text -->
      <b-carousel-slide 
            width="100%"
            height="100%" img-src="https://picsum.photos/1024/480/?image=1055">
        <h1>스터디 메이트</h1>
      </b-carousel-slide>

      <!-- Slides with img slot -->
      <!-- Note the classes .d-block and .img-fluid to prevent browser default image alignment -->
      <!-- <b-carousel-slide>
        <template v-slot:img>
          <img class="img-fluid d-block" src="../assets/img/main.jpg" alt="image slot">
        </template>
      </b-carousel-slide> -->

      <!-- Slide with blank fluid image to maintain slide aspect ratio -->
      <!-- <b-carousel-slide caption="Blank Image" img-blank img-alt="Blank image">
        <p>
          Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse eros felis, tincidunt
          a tincidunt eget, convallis vel est. Ut pellentesque ut lacus vel interdum.
        </p>
      </b-carousel-slide> -->
    </b-carousel>
    

    <div class="logins ">
      <div class="login d-flex align-items-center py-5">
        <div class="container">
          <div class="row">
            <div class="col-md-9 col-lg-8 mx-auto">
              <form>
                <div class="form-label-group">
                  <input type="email" id="inputEmail" class="form-control" placeholder="Email address" required autofocus>
                  <label for="inputEmail">Email address</label>
                </div>

                <div class="form-label-group">
                  <input type="password" id="inputPassword" class="form-control" placeholder="Password" required>
                  <label for="inputPassword">Password</label>
                </div>
                <button class="btn btn-lg btn-warning btn-block btn-login text-uppercase font-weight-bold mb-2" type="submit">로그인</button>
                <div class="text-center">
                  <a class="small" href="#">Forgot password?</a></div>
              </form>
            </div>
          </div>
        </div>
 
  </div>
</div>

</div>
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
.test {
  position: relative;
  bottom: 60px;
}
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
.logins{
  z-index:1;
  position: absolute;
  width:30%;
  height:400px;
  top: 0%;
  left:70%; 
}

/* */

:root {
  --input-padding-x: 1.5rem;
  --input-padding-y: 0.75rem;
}


.login-heading {
  font-weight: 300;
}

.btn-login {
  font-size: 0.9rem;
  letter-spacing: 0.05rem;
  padding: 0.75rem 1rem;
  border-radius: 2rem;
}
.btn-warnning{
    /* background-color :#ffc107; */
      background-color : lightgray;

}

.form-label-group {
  position: relative;
  margin-bottom: 1rem;
}

.form-label-group>input,
.form-label-group>label {
  padding: var(--input-padding-y) var(--input-padding-x);
  height: auto;
  border-radius: 2rem;
}

.form-label-group>label {
  position: absolute;
  top: 0;
  left: 0;
  display: block;
  width: 100%;
  margin-bottom: 0;
  /* Override default `<label>` margin */
  line-height: 1.5;
  color: #495057;
  cursor: text;
  /* Match the input under the label */
  border: 1px solid transparent;
  border-radius: .25rem;
  transition: all .1s ease-in-out;
}

.form-label-group input::-webkit-input-placeholder {
  color: transparent;
}

.form-label-group input:-ms-input-placeholder {
  color: transparent;
}

.form-label-group input::-ms-input-placeholder {
  color: transparent;
}

.form-label-group input::-moz-placeholder {
  color: transparent;
}

.form-label-group input::placeholder {
  color: transparent;
}

.form-label-group input:not(:placeholder-shown) {
  padding-top: calc(var(--input-padding-y) + var(--input-padding-y) * (2 / 3));
  padding-bottom: calc(var(--input-padding-y) / 3);
}

.form-label-group input:not(:placeholder-shown)~label {
  padding-top: calc(var(--input-padding-y) / 3);
  padding-bottom: calc(var(--input-padding-y) / 3);
  font-size: 12px;
  color: #777;
}

/* Fallback for Edge
-------------------------------------------------- */

@supports (-ms-ime-align: auto) {
  .form-label-group>label {
    display: none;
  }
  .form-label-group input::-ms-input-placeholder {
    color: #777;
  }
}

/* Fallback for IE
-------------------------------------------------- */

@media all and (-ms-high-contrast: none),
(-ms-high-contrast: active) {
  .form-label-group>label {
    display: none;
  }
  .form-label-group input:-ms-input-placeholder {
    color: #777;
  }
}


/*
*{
  margin-top:0 !important;
} */
</style>
