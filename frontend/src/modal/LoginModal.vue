<template>
  <transition name="modal" appear>
    <div @keypress.enter="doLogin" class="modal modal-overlay" @click.self="$emit('close')">
      <div class="modal-window w-50">
        <p class="font-weight-bold h4 text-center my-4">LOGIN</p>

        <div class="modal-content">
          <div class="input-wrap mx-3">
            <input
              class="p-3 border-bottom"
              v-model="email"
              id="email"
              placeholder="이메일을 입력해주세요"
              type="text"
            />
          </div>
          <div class="input-wrap mx-3">
            <input
              class="p-3 border-bottom"
              v-model="password"
              type="password"
              id="password"
              placeholder="영문 + 숫자 8자 이상"
            />
          </div>
          <b-button class="btn btn-dark m-3 w-25 mx-auto" @click="doLogin">Login</b-button>
          <div class="add-option"></div>
        </div>
        <footer class="modal-footer">
          <slot name="footer">
            <p>비밀번호를 잊어버렸나요?</p>

            <b-button v-b-modal.modal-1>비밀번호 찾기</b-button>
            <b-modal @ok="handleOk" id="modal-1" title="비밀번호 찾기">
              <div>
                <small>닉네임</small>
                <b-form-input v-model="findpasswordData.nickname" placeholder="가입 시 작성한 닉네임을 입력해주세요"></b-form-input>
              </div>
              <div>
                <small>이메일</small>
                <b-form-input v-model="findpasswordData.email" placeholder="가입 시 작성한 이메일을 입력해주세요"></b-form-input>
              </div>
            </b-modal>

            <button class="btn btn-primary text-bold" @click="$emit('close')">Close</button>
          </slot>
        </footer>
      </div>
    </div>
  </transition>
</template>

<script>
import "../assets/css/user.css";
import constants from "../lib/constants";
import axios from "axios";

const SERVER_URL = "http://localhost:8080";
export default {
  methods: {
    doLogin() {
      axios
        .get(SERVER_URL + "/account/login", {
          params: {
            email: this.email,
            password: this.password
          }
        })
        .then(res => {
          console.log(res);
          this.token = res.data.object
          if (res.status == 200) {
            this.$cookies.set('Auth-Token', this.token);
            this.$emit("close");
            this.$router.go();
          }
        })
        .catch(err => {
          console.log(err);
          alert("아이디 및 비밀번호를 확인해주세요.");
          this.email = "";
          this.password = "";
        });
    },
    handleOk() {
      axios
        .get(SERVER_URL + "/account/findPassword",{
          params: {
            email: this.findpasswordData.email,
            nickname: this.findpasswordData.nickname
          }
        })
        .then(res => {
          alert("비밀번호가 입력하신 메일주소로 전송되었습니다.");
          this.$router.push("/");
        })
        .catch(err => {
          console.log(err.response);
          alert("메일 및 닉네임을 다시 확인해주세요.");
        });
    },
  },
  data: () => {
    return {
      constants,
      email: "",
      password: "",
      token: "",
      findpasswordData: {
        email: "",
        nickname: ""
      }
    };
  }
};
</script>


<style scoped>
.modal-overlay {
  display: flex;
  align-items: center;
  justify-content: center;
  position: fixed;
  z-index: 30;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
}

.modal-window {
  background: #fff;
  border-radius: 4px;
  overflow: hidden;
}

.modal-content {
  padding: 10px 20px;
}

.modal-footer {
  background: #ccc;
  padding: 10px;
  text-align: right;
}
</style>