<template>
  <div class="user" id="findPassword">
    <div class="wrapC table">
      <div class="middle">
        <h1>비밀번호 찾기</h1>
        <div class="form-wrap">
          <div class="input-wrap">
            <input
              v-model="findpasswordData.nickname"
              id="nickname"
              placeholder="가입 시 작성한 닉네임을 입력해주세요"
              type="text"
            />
          </div>

          <div class="input-wrap">
            <input
              v-model="findpasswordData.email"
              id="email"
              placeholder="가입 시 작성한 이메일을 입력해주세요"
              type="text"
            />
          </div>
        </div>
        <button class="btn" @click="doFind">
          <span>찾기</span>
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import "../../assets/css/user.scss";
import axios from "axios";
import constants from "../../lib/constants";

const SERVER_URL = "http://localhost:8080";

export default {
  name: "findPassword",
  components: {},
  created() {},
  watch: {},
  methods: {
    doFind() {
      axios
        .get(SERVER_URL + "/account/findPassword",{
          params: {
            email: this.findpasswordData.email,
            nickname: this.findpasswordData.nickname
          }
        })
        .then(res => {
          console.log(res);
          alert("비밀번호가 입력하신 메일주소로 전송되었습니다.");
          this.$router.push("/");
        })
        .catch(err => {
          this.$router.push({name: constants.URL_TYPE.ERROR.ERRORPAGE,params:{'code':err.response.data}});
          console.log(err.response);
          alert("메일 및 닉네임을 다시 확인해주세요.");
        });
    }
  },
  data: () => {
    return {
      findpasswordData: {
        email: "",
        nickname: ""
      }
    };
  }
};
</script>
