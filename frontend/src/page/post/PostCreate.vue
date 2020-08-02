<template>
  <div class="container w-50">
  <div class="card shadow my-5">
    <div class="card-text">
    <h1><img src="https://image.flaticon.com/icons/svg/2231/2231677.svg" class="m-5 w-25" alt=""></h1>
    <b-row class="m-3">
      <b-col sm="12" class="px-1">
        <small class="formtitle ml-3 float-left">제목</small>
        <b-form-input v-model="postCreateDate.title" placeholder="제목" type="text"></b-form-input>
      </b-col>


      <b-col sm="12" class="px-1">
        <label>제목</label>
        <b-form-input v-model="postCreateDate.title" placeholder="제목" type="text"></b-form-input>
      </b-col>
      <b-col sm="6" class="px-1">
        <label>일정</label>
        <b-form-input v-model="postCreateDate.bindo" placeholder="주 _회" type="text"></b-form-input>
      </b-col>
      <b-col sm="6" class="px-1">
        <label>인원수 (3명 이상)</label>
        <b-form-spinbutton id="demo-sb" v-model="postCreateDate.limitp" min="3" max="100"></b-form-spinbutton>
      </b-col>
      <b-col sm="6" class="px-1">
        <label>시/도</label>
        <select @click="getgungu" v-model="postCreateDate.sido_code" class="custom-select">
          <option v-for="sido in allSidoCode" :key="sido.sidocode" :value="sido.sidocode">{{sido.sidoname}}</option>
        </select>
      </b-col>
      <b-col sm="6" class="px-1">
        <label>시/군/구</label>
        <select v-model="postCreateDate.sigungu_code" class="custom-select">
          <option v-for="sido in allGugunCode" :key="sido.guguncode" :value="sido.guguncode">{{sido.gugunname}}</option>
        </select>
      </b-col>
      <b-col sm="6" class="px-1">
        <label>시작일</label>
        <b-form-input v-model="postCreateDate.start_date" type="date"></b-form-input>
      </b-col>
      <b-col sm="6" class="px-1">
        <label>종료일</label>
        <b-form-input v-model="postCreateDate.end_date" type="date"></b-form-input>
      </b-col>
      <b-col sm="6" class="px-1">
        <label>카테고리</label>
        <b-form-select v-model="postCreateDate.category" :options="options"></b-form-select>
      </b-col>
      <b-col sm="6" class="px-1">
        <label>태그</label>
        <!-- <b-form-input v-model="postCreateDate.tag" placeholder="태그" type="text"></b-form-input> -->
        <b-form-tags input-id="tags-basic" v-model="postCreateDate.tag" placeholder="태그" class="mb-2"></b-form-tags>
      </b-col>
      <b-col sm="12" class="px-1">
        <label>내용</label>
        <b-form-textarea v-model="postCreateDate.data" placeholder="내용" rows="3"></b-form-textarea>
      </b-col>
    </b-row>
    <div v-if="!postCreateDate.background_image">
      <h2>Select an image</h2>
      <input type="file" @change="onFileChange" />
    </div>
    <div v-else>
      <img :src="postCreateDate.background_image" />
      <button @click="removeImage">Remove image</button>
    </div>
    <div class="d-flex inline justify-content-center">
      <div class="p-3">
        <div @click="postCreate(postCreateDate.tmp=1)" class="btn btn-warning btn-sm">SUBMIT</div>
      </div>
    </div>
  </div>
  </div>
  </div>
</template>

<script>
import axios from "axios";
import constants from '../../lib/constants'

const SERVER_URL = "http://localhost:8080";

export default {
  name: "PostCreate",
  data: () => {
    return {
      profileInfo: [],
      options: [
        { value: null, text: '카테고리' },
        { value: '알고리즘', text: '알고리즘' },
        { value: '공모전', text: '공모전' },
        { value: '자격증', text: '자격증' },
        { value: '취업', text: '취업' },
        { value: '웹', text: '웹' },
        { value: '기타', text: '기타' },
      ],
      allSidoCode: [],
      allGugunCode: [],
      postCreateDate: {
        title: "",
        data: "",
        background_image: "",
        category: null,
        uid: "",
        tag: [],
        sido_code: "",
        sigungu_code: "",
        start_date: "",
        end_date: "",
        limitp: 3,
        bindo: "",
        tmp:0,
      }
    };
  },
  created(){
    this.user_check()
    this.getSidoCode()
  },
  methods: {
    user_check(){
      if (this.$cookies.isKey('Auth-Token')){
        const token = this.$cookies.get("Auth-Token");
        axios
          .get(SERVER_URL + "/account/profile", {
            params: {
              Token: token,
            },
          })
          .then((res) => {
            this.profileInfo = res.data.object
          })
          .catch((err) => {
            this.$router.push({
              name: constants.URL_TYPE.ERROR.ERRORPAGE,
              params: { code: err.response.data },
            });
          });
      } else (
        alert("로그인후 이용해주세요."),
        this.$router.push('/')
      )
    },
    postTmp(){
      this.postCreateDate.uid = this.profileInfo.uid
      console.log(this.postCreateDate)
      axios
        .post(SERVER_URL + "/study/write", this.postCreateDate)
        .then(res => {
          console.log(res)
          this.$router.push({name:constants.URL_TYPE.POST.MAIN})
        })
        .catch(err => {
          console.log(err)
          alert("입력정보를 확인해주세요!")
        });
    },
    postCreate() {
      this.postCreateDate.uid = this.profileInfo.uid
      console.log(this.postCreateDate)
      axios
        .post(SERVER_URL + "/study/write", this.postCreateDate)
        .then(res => {
          console.log(res)
          this.$router.push({name:constants.URL_TYPE.POST.POSTDETAIL, params: { post_id: res.data.object.pid }})
        })
        .catch(err => {
          console.log(err)
          alert("입력정보를 확인해주세요!")
        })
    },
    onFileChange(e) {
      var files = e.target.files || e.dataTransfer.files;
      if (!files.length) return;
      this.createImage(files[0]);
    },
    createImage(file) {
      this.postCreateDate.background_image = new Image();
      var reader = new FileReader();

      reader.onload = e => {
        this.postCreateDate.background_image = e.target.result;
      };
      reader.readAsDataURL(file);
    },
    removeImage: function(e) {
      this.postCreateDate.background_image = "";
    },
    getSidoCode () {
      axios.post(SERVER_URL + "/study/getsidocode")
      .then(res => {
        this.allSidoCode = res.data.object
      })
      .catch(err => {
        console.log(err);
      })
    },
    getgungu() {
      const code = this.postCreateDate.sido_code
      console.log(code);
      axios.get(SERVER_URL + "/study/getguguncode", { params: { sidocode: code } })
      .then(res => {
        this.allGugunCode = res.data.object
      })
      .catch(err => {
        console.log(err);
      })
    }
  }
};
</script>

<style>

label{
  position: relative;
  right: 40%;
  top: 5px;
  font-size: small;
}
.submit-btn {
    float: center;
    padding: 7px 35px;
    border-radius: 60px;
    display: inline-block;
    background-color: #4b8cfb;
    color: white;
    font-size: 18px;
    cursor: pointer;
}

.formtitle{
  transform:translateY(10px);
  background-color:white;
}
</style>