<template>
  <b-container style="max-width: 750px;">
    <h1>Edit</h1>
    <b-row class="m-3">
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
        <div @click="postUpdate(postCreateDate.tmp=1)" class="btn btn-warning btn-sm">SUBMIT</div>
      </div>
      <div class="p-3">
        <div @click="postTmp(postCreateDate.tmp=0)" class="btn btn-primary btn-sm">임시저장</div>
      </div>
    </div>
  </b-container>
</template>


<script>
import axios from "axios"
import constants from '../../lib/constants'

const SERVER_URL = 'http://localhost:8080'

export default {
  name: 'PostUpdate',
  // beforeMount() {
  //   this.getDetail()
  //   this.user_check()
  //   this.getSidoCode()
  // },
  created(){
    this.getDetail()
    this.user_check()
    this.getSidoCode()
  },
  data: () => {
    return {
      postCreateDate:{
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
      },
      allSidoCode: [],
      allGugunCode: [],
      options: [
        { value: null, text: '카테고리' },
        { value: '알고리즘', text: '알고리즘' },
        { value: '공모전', text: '공모전' },
        { value: '자격증', text: '자격증' },
        { value: '취업', text: '취업' },
        { value: '웹', text: '웹' },
        { value: '기타', text: '기타' },
      ],
      totalData : {},
      profileInfo:{},
    }
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
    getDetail() {
      const post_id = this.$route.params.post_id
      axios.get(SERVER_URL + '/study/details', { params: {pid: post_id}})
      .then(res => {
        if (res.data.object.length >= 1){
          this.postCreateDate = res.data.object[0]
          const tmptag = res.data.object[2]
          let tags = []
          for (var i=0;i<tmptag.length;i++){
            tags.push(tmptag[i].tagname)
          }
          this.postCreateDate.tag = tags
          console.log(this.postCreateDate)
        } else {
          this.postCreateDate = res.data.object
        }
          })
      .catch(err => console.log(err.data))
    },
    postTmp(){
      this.postCreateDate.uid = this.profileInfo.uid
      axios
        .post(SERVER_URL + "/study/write", this.postCreateDate)
        .then(res => {
          this.$router.push({name:constants.URL_TYPE.POST.MAIN})
        })
        .catch(err => {
          console.log(err)
          alert("입력정보를 확인해주세요!")
        });
    },
    postUpdate () {
      const post_id = this.$route.params.post_id
      
      this.postCreateDate.uid = this.profileInfo.uid
      axios
        .post(SERVER_URL + "/study/update", this.postCreateDate)
        .then(res => {
          this.$router.push({name:constants.URL_TYPE.POST.POSTDETAIL, params: { post_id: post_id }});
        })
        .catch(err => {
          console.log(err)
        })
    },
    onFileChange(e) {
      var files = e.target.files || e.dataTransfer.files;
      if (!files.length)
        return;
      this.createImage(files[0]);
    },
    createImage(file) {
      this.postData.background_image = new Image();
      var reader = new FileReader();

      reader.onload = (e) => {
        this.postData.background_image = e.target.result;
      };
      reader.readAsDataURL(file);
    },
    removeImage: function (e) {
      this.postData.background_image = '';
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
  },
}
</script>

<style>

</style>