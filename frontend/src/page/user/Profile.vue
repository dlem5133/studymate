<template>
  <div class="container">
    <div class="row d-flex justify-content-center">
      <div class="center">
        <div class="profile">
          <div class="image">
            <div class="circle-1"></div>
            <div class="circle-2"></div>

            <div v-if="!profileInfo.profile_image">
              <img src="../../assets/img/defualt_image.png" width="70" height="70" />
            </div>
            <div v-else>
              <img :src="profileInfo.profile_image" width="70" height="70" />
            </div>
          </div>

          <div class="name">{{ profileInfo.nickname }}</div>
          <div class="job">{{ profileInfo.email }}</div>

          <div class="actions">
            <b-button class="btn" v-b-modal.modal-1>수정</b-button>
            <b-modal @ok="userUpdate" id="modal-1" title="회원정보 수정">
              <small>닉네임</small>
              <b-card class="baseInput" no-body id="input-email">{{updateData.nickname}}</b-card>
              <small>이메일</small>
              <b-card class="baseInput" no-body id="input-email">{{updateData.email}}</b-card>
              <small>비밀번호</small>
              <b-form-input type="password" v-model="updateData.password"></b-form-input>
              <small>자기소개</small>
              <b-form-textarea v-model="updateData.intro" rows="3" max-rows="6"></b-form-textarea>
              <small>프로필</small>
              <div v-if="!updateData.profile_image">
                <h2>Select an image</h2>
                <input type="file" @change="onChangeImages" />
              </div>
              <div v-else>
                <img :src="updateData.profile_image" style="width: 100%;" />
                <button @click="removeImage">Remove image</button>
              </div>
            </b-modal>

            <b-button class="btn" v-b-modal.modal-2>탈퇴</b-button>
            <b-modal @ok="userDelete" id="modal-2" title="회원 탈퇴">
              <div>
                <small>이메일</small>
                <b-form-input v-model="deleteData.email" placeholder="가입 시 작성한 이메일을 입력해주세요"></b-form-input>
              </div>
              <div>
                <small>비밀번호</small>
                <b-form-input
                  type="password"
                  v-model="deleteData.password"
                  placeholder="가입 시 작성한 비밀번호를 입력해주세요"
                ></b-form-input>
              </div>
            </b-modal>
          </div>
        </div>

        <div class="stats">
          <div class="box" @click="page=0">
            <span class="value">{{studyLists.length + readyLists.length}}</span>
            <span class="parameter">가입중인 스터디</span>
          </div>
          <div class="box" @click="page=1">
            <span class="value">{{total_mileage}}</span>
            <span class="parameter">마일리지</span>
          </div>
          <div class="box" @click="page=2">
            <span class="value">{{total_score}} / 5</span>
            <span class="parameter">평점</span>
          </div>
        </div>
      </div>

      <div v-if="page === 0">
        <div class="side">
          <div class="text-left pt-3 col-12">
            <small class="text-success font-weight-bold text-left pl-3">진행중 스터디</small>
            <div
              v-for="list in ingStudy"
              :key="list.id"
              class="card m-2 px-2 p-2"
              @click="goStudyMain(list.pid)"
            >
              <div class="d-flex inline">
                <small class="text-left">{{ list.empId.study.title }}</small>
                <b-badge class="ml-auto my-auto" variant="success">진행중</b-badge>
              </div>
            </div>

            <hr />

            <small class="text-warning font-weight-bold text-left pl-3 pb-2">모집중 스터디</small>
            <div
              v-for="list in readyLists"
              :key="list.id"
              class="card m-2 px-2 p-2"
              @click="goPostMain(list.pid)"
            >
              <div class="d-flex inline">
                <small class="text-left">{{ list.empId.study.title }}</small>
                <b-badge class="ml-auto my-auto" variant="warning">승인대기중</b-badge>
              </div>
            </div>
            <div
              v-for="list in comStudy"
              :key="list.id"
              class="card m-2 px-2 p-2"
              @click="goPostMain(list.pid)"
            >
              <div class="d-flex inline">
                <small class="text-left">{{ list.empId.study.title }}</small>
                <b-badge class="ml-auto my-auto" variant="danger">모집중</b-badge>
              </div>
            </div>

            
            <hr />

            <small class="text-secondary font-weight-bold text-left pl-3 pb-2">완료된 스터디</small>
            
            <div
              v-for="list in endStudy"
              :key="list.id"
              class="card m-2 px-2 p-2"
              v-b-modal.modal-3
            >
              <div class="d-flex inline">
                <small class="text-left">{{ list.empId.study.title }}</small>
                <b-badge class="ml-auto my-auto" variant="secondary">완료</b-badge>
              </div>
            <!-- <b-modal title="한줄평" id="modal-3" hide-footer>

              <div 
              v-show="list.pid==list1.pid" v-for="list1 in evalistdata" :key="list1.id"
              class="m-2 px-2 p-2"
            >
              <div class="d-flex inline">
                <small class="text-left">{{ list1.sentence }}</small>
                <b-badge class="ml-auto my-auto" variant="success">{{list1.user.nickname}}</b-badge>
              </div>
            </div>
            </b-modal> -->

            </div>

          </div>
        </div>
      </div>


      <div v-if="page===1">
        <div class="side">
          <div class="text-left pt-3 col-12">
            <small class="text-dark font-weight-bold text-left pl-3">마일리지 LOG</small>
            <div>
              <small class="text-success font-weight-bold text-left pl-3">일지</small>
              <small>{{ mileageData.diarypoint }}</small>
            </div>              
            <div>
              <small class="text-success font-weight-bold text-left pl-3">평가</small>
              <small>{{ mileageData.evalpoint }}</small>
            </div>
            <div>
              <small class="text-success font-weight-bold text-left pl-3">스터디 완료</small>
              <small>{{ mileageData.endpoint * 200 }}</small>
            </div>
          </div>
        </div>
      </div>

      <div v-if="page===2">
        <div class="side" >
          <div class="p-3">
           <p> {{total_score}}총점인데 이따가 꾸밀게 뭘 해도 못생겼어</p>
          <hr>
          <div id="ratingbox" class="text-left row mx-auto">
            <div class="col-4 py-5">
              <div class="text-center">
                <b-form-rating 
                  v-model="profileInfo.score1"
                  readonly
                  no-border 
                  inline
                  color="#feb74d"
                  size="sm"
                ></b-form-rating>
              </div>
              <div class="text-center pt-5">
                <small class="text-dark font-weight-bold">성실도</small>
              </div>
            </div>
            <div class="col-4 py-5">
              <div class="text-center">
                <b-form-rating
                  v-model="profileInfo.score2"
                  readonly
                  no-border 
                  inline
                  color="#feb74d"
                  size="sm"
                ></b-form-rating>
              </div>
              <div class="text-center pt-5">
                <small class="text-dark font-weight-bold">참여도</small>
              </div>
            </div>
            <div class="col-4 py-5">
              <div class="text-center">
                <b-form-rating
                  v-model="profileInfo.score3"
                  readonly
                  no-border 
                  inline
                  color="#feb74d"
                  size="sm"
                ></b-form-rating>
              </div>
              <div class="text-center pt-5">
                <small class="text-dark font-weight-bold">인싸력</small>
              </div>
            </div>

            </div>
            <div>
            <hr>
            <small class="text-secondary font-weight-bold text-left pl-3 pb-2">한줄평</small>

            <div 
              v-for="list1 in evalistdata" :key="list1.id"
              class="m-2 px-2 p-2"
            >
              <div class="d-flex inline">
                <small class="text-left">{{ list1.sentence }}</small>
                <b-badge class="ml-auto my-auto" variant="success">{{list1.user.nickname}}</b-badge>
              </div>
            </div></div>
          </div>
        </div>
      </div>


    </div>
  </div>
</template>

<script>
import "../../assets/css/profile.scss";
import axios from "axios";
import constants from "../../lib/constants";

const SERVER_URL = constants.ServerUrl;

export default {
  name: "profile",
  data: () => {
    return {
      profileInfo: {},
      updateData: {
        email: "",
        nickname: "",
        password: "",
        intro: "",
        profile_image: "",
        uid: "",
      },
      deleteData: {
        email: "",
        password: "",
      },
      page: 0,

      studyLists: [],
      comStudy:[],
      ingStudy:[],
      endStudy:[],
      readyLists: [],
      evalistdata:{},
      mileageData:{},
    };
  },
  computed:{
    total_score(){
      return ((this.profileInfo.score1 + this.profileInfo.score2 + this.profileInfo.score3) / 3).toFixed(1)
    },    
    total_mileage(){
      
      if ("+this.mileageData.total".length > 3){
        return this.mileageData.total / 1000 + "K"
      } else {
        return this.mileageData.total
      }
    }
  },
  methods: {
    evaList(){
      const targetid = this.profileInfo.uid
      axios.post(SERVER_URL+'/eva/targetlist', {target_uid:targetid})
      .then(res=>{
        this.evalistdata = res.data.object
      })
      .catch(err=>console.log(err))
    },
    mileageList(){
      axios.get(SERVER_URL+"/mileage/user", {params:{uid:this.profileInfo.uid}})
      .then(res=>{
        this.mileageData = res.data.object
      })
      .catch(err=>console.log(err))
    },
    changeDatedata(time) {
      return time.substring(5, 10) + " " + time.substring(11, 19);
    },
    addprofileInfo() {
      const token = this.$cookies.get("Auth-Token");
      axios
        .get(SERVER_URL + "/account/profile", { params: { Token: token } })
        .then((res) => {
          this.profileInfo = res.data.object;
          this.updateData.email = res.data.object.email;
          this.updateData.nickname = res.data.object.nickname;
          this.updateData.password = res.data.object.password;
          this.updateData.uid = res.data.object.uid;
          this.updateData.intro = res.data.object.intro;
          this.updateData.profile_image = res.data.object.profile_image;
          this.addReadyList();
          this.addStudyList();
          this.mileageList()
          this.evaList()
        })
        .catch((err) => {
          this.$router.push({
            name: constants.URL_TYPE.ERROR.ERRORPAGE,
            params: { code: err.response.data },
          });
        });
    },
    addStudyList() {
      axios
        .post(SERVER_URL + "/account/studylist", this.profileInfo)
        .then((res) => {
          this.studyLists = res.data.object
          this.comStudy = res.data.object.filter((item)=>item.empId.study.tmp===0)
          this.ingStudy = res.data.object.filter((item)=>item.empId.study.tmp===1)
          this.endStudy = res.data.object.filter((item)=>item.empId.study.tmp===2)
        })
        .catch((err) => {
          console.log(err);
        });
    },
    addReadyList() {
      axios
        .post(SERVER_URL + "/account/readylist", this.profileInfo)
        .then((res) => {
          this.readyLists = res.data.object;
        })
        .catch((err) => {
          console.log(err);
        });
    },
    goPostMain(post_id) {
      this.$router.push({
        name: constants.URL_TYPE.POST.POSTDETAIL,
        params: { post_id: post_id },
      });
    },
    goStudyMain(post_id) {
      this.$router.push({
        name: constants.URL_TYPE.STUDY.STUDYMAIN,
        params: { post_id: post_id },
      });
    },
    onChangeImages(e) {
      const selectedImage = e.target.files[0];
      this.createBase64Image(selectedImage);
    },
    createBase64Image(fileObject) {
      this.updateData.profile_image = new Image();
      const reader = new FileReader();
      reader.onload = (e) => {
        this.updateData.profile_image = e.target.result;
      };
      reader.readAsDataURL(fileObject);
    },
    removeImage: function (e) {
      this.updateData.profile_image = "";
    },
    userUpdate() {
      axios
        .post(SERVER_URL + "/account/update", this.updateData)
        .then((res) => {
          this.$cookies.remove("Auth-Token");
          const token = res.data.object;
          this.$cookies.set("Auth-Token", token);
          alert("수정되었습니다.");
          this.addprofileInfo()
        })
        .catch((err) => {
          // this.$router.push({name: constants.URL_TYPE.ERROR.ERRORPAGE,params:{'code':err.response.data}});
          console.log(err.response.data.errors[0]);
          alert(err.response.data.errors[0].field + "를 확인해 주세요");
        });
    },
    userDelete() {
      axios
        .post(SERVER_URL + "/account/delete", this.deleteData)
        .then((res) => {
          this.$cookies.remove("Auth-Token");
          alert("회원탈퇴되었습니다.");
          this.$router.push("/");
          // this.$router.go();
        })
        .catch((err) => {
          alert("입력정보를 확인해주세요.");
          console.log(err);
        });
    },
  },
  created() {
    this.addprofileInfo();
  },
};
</script>

<style scoped>
.clickbtn,
.tmplist {
  cursor: pointer;
}
.form-control{
 background-color: rgba(0,0,0,0);
}
.b-rating .b-rating-star, .b-rating .b-rating-value{
  padding:0 !important;
}
</style>