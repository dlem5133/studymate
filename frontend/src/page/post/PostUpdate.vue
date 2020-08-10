<template>
  <div class="container pt-0 mt-0">
  <div class="card border-0 w-75 mx-auto mx-5">
    <div class="card-text mx-5">
    <img :src="images" class="logo w-25"  alt="">
    <b-row class="m-3">
      <b-col sm="12" md="8" class="px-1">
        <small class="formtitle ml-3 float-left">제목</small>
        <b-form-input v-model="postCreateDate.title" placeholder="제목" type="text"></b-form-input>
      </b-col>
      <b-col sm="12" md="4" class="px-1 mb-4">
        <div style="transform: translateY(21px);" v-if="!postCreateDate.background_image">
          <b-form-file placeholder=" " browse-text="Image" @change="onFileChange" ></b-form-file>
        </div>
        <div class="d-flex" style="transform: translateY(20px);" v-else>
          <img :src="postCreateDate.background_image" class="w-25" style="height:30px;"/>
          <b-button class="ml-3" @click="removeImage" variant="border-0"><b-icon icon="trash"></b-icon></b-button>
        </div>
        <!-- <b-form-input v-model="postCreateDate.title" placeholder="제목" type="text"></b-form-input> -->
      </b-col>
      <b-col sm="12" md="6" class="px-1">
        <small class="formtitle ml-3 float-left">일정</small>
        <b-form-input v-model="postCreateDate.bindo" placeholder="주 _회" type="text"></b-form-input>
      </b-col>
      <b-col sm="12" md="6" class="px-1">
        <small class="formtitle1 ml-3 float-left">인원수 (3명 이상)</small>
        <b-form-spinbutton id="demo-sb" v-model="postCreateDate.limitp" min="3" max="100"></b-form-spinbutton>
      </b-col>
      <b-col sm="12" md="6" class="px-1">
        <small class="formtitle ml-3 float-left">시/도</small>
        <select @click="getgungu" v-model="postCreateDate.sido_code" class="custom-select">
          <option v-for="sido in allSidoCode" :key="sido.sidocode" :value="sido.sidocode">{{sido.sidoname}}</option>
        </select>
      </b-col>
      <b-col sm="12" md="6" class="px-1">
        <small class="formtitle ml-3 float-left">시/군/구</small>
        <select v-model="postCreateDate.sigungu_code" class="custom-select">
          <option v-for="sido in allGugunCode" :key="sido.guguncode" :value="sido.guguncode">{{sido.gugunname}}</option>
        </select>
      </b-col>
      <b-col sm="12" md="6" class="px-1">
        <small class="formtitle ml-3 float-left">시작일</small>
        <b-form-input v-model="postCreateDate.start_date" type="date"></b-form-input>
      </b-col>
      <b-col sm="12" md="6" class="px-1">
        <small class="formtitle ml-3 float-left">종료일</small>
        <b-form-input v-model="postCreateDate.end_date" type="date"></b-form-input>
      </b-col>
      <b-col sm="12" md="6" class="px-1">
        <small class="formtitle ml-3 float-left">카테고리</small>
        <b-form-select value="f02c" v-model="postCreateDate.category" :options="options"></b-form-select>
      </b-col>
      <b-col sm="12" md="6" class="px-1">
        <b-input-group size="sm" class="taggroup mb-2">
          <b-icon icon="tags-fill" variant="warning" class="mx-2 mt-2"></b-icon>
        <!-- <b-form-input v-model="postCreateDate.tag" placeholder="태그" type="text"></b-form-input> -->
          <b-form-tags input-id="tags-basic" 
        tag-variant="warning" v-model="postCreateDate.tag" placeholder="태그" class="rounded mb-2 p-2"></b-form-tags>
        </b-input-group>
      </b-col>
      <b-col sm="12" class="px-1">
        <small class="formtitle ml-3 float-left">내용</small>
        <b-form-textarea v-model="postCreateDate.data" placeholder="내용" rows="3"></b-form-textarea>
      </b-col>
    <b-col>
    <!-- <div>
      <div v-if="!postCreateDate.background_image">
        <small class="formtitle  ml-3 float-left">Background Image</small>
        <input type="file" @change="onFileChange" />
      </div>
      <div v-else>
        <img :src="postCreateDate.background_image" class="w-25"/>
        <button @click="removeImage">Remove image</button>
      </div>
    </div> -->
    <div class="d-flex inline justify-content-center mt-2 mb-5">
      <div class="p-3">
        <div @click="postUpdate(postCreateDate.tmp=1)" class="btn btn-warning btn-sm">SUBMIT</div>
      </div>
    </div>
    </b-col>
    </b-row>
  </div>
  </div>
  </div>
</template>


<script>
import axios from "axios"
import constants from '../../lib/constants'

const SERVER_URL = constants.ServerUrl;

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
      images: require('../../assets/img/logo.png'),
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
           var date = new Date();
      // console.log(date.getFullYear());
      if (this.postCreateDate.title == "") {
        alert("스터디 제목이 입력되지 않았습니다. 확인해주세요!");
      } else if (this.postCreateDate.bindo == "") {
        alert("일정이 입력되지 않았습니다. 확인해주세요!");
      } else if (parseInt(this.postCreateDate.bindo) >= 7) {
        alert("스터디는 주 7회를 넘을 수 없습니다. ");
      } else if (this.postCreateDate.sido_code == "") {
        alert("시/도가 입력되지 않았습니다. 확인해주세요!");
      } else if (this.postCreateDate.sigungu_code == "") {
        alert("시/군/구가 입력되지 않았습니다. 확인해주세요!");
      } else if (this.postCreateDate.start_date == "") {
        alert("시작일이 입력되지 않았습니다. 확인해주세요!");
      } else if (
        !(
          parseInt(this.postCreateDate.start_date.substring(0, 4)) >=
            date.getFullYear() &&
          parseInt(this.postCreateDate.start_date.substring(5, 7)) >=
            date.getMonth() + 1 &&
          parseInt(this.postCreateDate.start_date.substring(8, 10)) >
            date.getDate()
        )
      ) {
        alert(
          "시작일은 현재 날짜 뒤에 있어야합니다 ㅠ^ㅠ \n오늘은 " +
            (date.getMonth() + 1) +
            "월 " +
            date.getDate() +
            "일 입니다."
        );
      } else if (this.postCreateDate.end_date == "") {
        alert("종료일이 입력되지 않았습니다. 확인해주세요!");
      } else if (
        !(
          parseInt(this.postCreateDate.end_date.substring(0, 4)) >=
            this.postCreateDate.start_date.substring(0, 4) &&
          parseInt(this.postCreateDate.end_date.substring(5, 7)) >=
            this.postCreateDate.start_date.substring(5, 7) &&
          parseInt(this.postCreateDate.end_date.substring(8, 10)) >
            this.postCreateDate.start_date.substring(8, 10)
        )
      ) {
        alert(
          "종료일은 시작일보다 뒤에 있어야합니다 ㅠ^ㅠ \n스터디 시작일은 " +
            this.postCreateDate.start_date.substring(5, 7) +
            "월 " +
            this.postCreateDate.start_date.substring(8, 10) +
            "일 입니다."
        );
      } else if (this.postCreateDate.category == null) {
        alert("카테고리가 입력되지 않았습니다. 확인해주세요!");
      } else if (this.postCreateDate.tag.length == 0) {
        alert("태그가 입력되지 않았습니다. 확인해주세요!");
      } else if (this.postCreateDate.data == "") {
        alert("내용이 입력되지 않았습니다. 스터디를 소개해주세요!");
      } else {
      axios
        .post(SERVER_URL + "/study/update", this.postCreateDate)
        .then(res => {
          this.$router.push({name:constants.URL_TYPE.POST.POSTDETAIL, params: { post_id: post_id }});
        })
        .catch(err => {
          console.log(err)
        })
      }
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