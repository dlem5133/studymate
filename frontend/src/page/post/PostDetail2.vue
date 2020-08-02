<template>

<div class="blog-container">
  
  <div class="blog-header">
    <div class="blog-cover">
      <div class="blog-author">
        <h3>Russ Beye</h3>
      </div>
    </div>
  </div>

  <div class="blog-body">
    <div class="blog-title">
      <h1><a href="#">I Like To Make Cool Things</a></h1>
    </div>
    <div class="blog-summary">
      <p>I love working on fresh designs that <a href="https://www.youtube.com/watch?v=hANtM1vJvOo">breathe</a>. To that end, I need to freshen up my portfolio here because it does exactly the opposite. For the next month I will be working almost exclusively on my portfolio. Sounds like a lot of fun!</p>
    </div>
    <div class="blog-tags">
      <ul>
        <li><a href="#">css</a></li>
        <li><a href="#">web design</a></li>
        <li><a href="#">codepen</a></li>
        <li><a href="https://twitter.com/russbeye">twitter</a></li>
      </ul>
    </div>
  </div>
  
  <div class="blog-footer">
    <ul>
      <li class="published-date">2 days ago</li>
      <li class="comments"><a href="#"><svg class="icon-bubble"><use xlink:href="#icon-bubble"></use></svg><span class="numero">4</span></a></li>
      <li class="shares"><a href="#"><svg class="icon-star"><use xlink:href="#icon-star"></use></svg><span class="numero">1</span></a></li>
    </ul>
  </div>

</div>






</template>

<script>
import axios from "axios";
import constants from "../../lib/constants";
import "../../assets/css/postdetail.scss";


const SERVER_URL = "http://localhost:8080";

export default {
  name: "PostDetail",
  data: () => {
    return {
      profileInfo:[],

      postData: {},
      replyData: {},
      tagData: {},
      userData:{},
      newReply: {
        pid: null,
        uid: null,
        reply_content: null,
      },
      requestListData:{},
      approvalData:{
        pid:null,
        uid:null,
      },
      requestData: { 
        pid: null, 
        uid: null, 
      },
      cancelData:{
        pid:null,
        uid:null,
      },
      requestLists: {},
      isRequest:false,
      memberListData:{},
      islike:false,
      likeData:{},
      likeClickData:{
        pid:null,
        uid:null,
      },
      isMember : false,
    };
  },
  created() {
    this.userCheck();
    // 카카오톡 공유 관련
    Kakao.init("d5e6e1f1140f3ded1483ff360cb5a153");
  },
  computed: {
    changeLike(){
      if (this.islike) {
        return "heart-fill"
      } else{
        return "heart"
      }
    }
  },
  mounted() {
    this.getDetail();
  },
  methods: {
    changeDate(time) {
      if (time) {
        return (
          time.split("-")[0] +
          "/" +
          time.split("-")[1] +
          "/" +
          time.split("-")[2].substring(0, 2)
        );
      }
    },
    changeDatedata(time) {
      return time.substring(5, 10) + " " + time.substring(11, 19);
    },
    userCheck() {
      if (this.$cookies.isKey("Auth-Token")) {
        const token = this.$cookies.get("Auth-Token");
        axios
        .get(SERVER_URL + "/account/profile", {
          params: {
            Token: token,
          },
        })
        .then((res) => {
          this.profileInfo = res.data.object
          this.getDetail();
          this.requestPeopleList()
          this.memberList()
        })
        .catch((err) => {
          this.$router.push({
            name: constants.URL_TYPE.ERROR.ERRORPAGE,
            params: { code: err.response.data },
          });
        });
      }
    },
    getDetail() {
      const post_id = this.$route.params.post_id;
      axios
        .get(SERVER_URL + "/study/details", { params: { pid: post_id } })
        .then((res) => {
          console.log(res.data.object);
          this.postData = res.data.object[0];
          this.replyData = res.data.object[1];
          this.tagData = res.data.object[2];
          this.likeData = res.data.object[3];
          this.userData = res.data.object[4];
          for (var i=0;i<this.likeData.length;i++){
            if (this.likeData[i].uid == this.profileInfo.uid){
              this.islike = true
              break;
            } else { this.islike = false }
          }
        })
        .catch((err) => console.log(err.data));
    },
    // postUpdate(post_id) {
    //   this.$router.push({
    //     name: constants.URL_TYPE.POST.POSTUPDATE,
    //     params: { post_id: post_id },
    //   });
    // },
    // postDelete() {
    //   axios
    //     .post(SERVER_URL + "/study/delete", this.postData)
    //     .then((res) => {
    //       this.$router.push({ name: constants.URL_TYPE.POST.MAIN });
    //     })
    //     .catch((err) => console.log(err));
    // },
    replyCreate(user) {
      this.newReply.pid = this.postData.pid;
      this.newReply.uid = user;
      axios.post(SERVER_URL + "/reply/write", this.newReply)
      .then((res) => this.$router.go())
      .catch((err) => console.log(err));
    },
    replyDelete(reply) {
      axios
        .post(SERVER_URL + "/reply/delete", reply)
        .then((res) => this.$router.go())
        .catch((err) => console.log(err));
    },
    requestPeopleList(){
      axios.post(SERVER_URL + "/study/requestlist", { pid: this.$route.params.post_id })
      .then(res=>{
        this.requestListData = res.data.object
        for (var i=0;i<this.requestListData.length;i++){
          if (this.requestListData[i].uid == this.profileInfo.uid){
            this.isRequest = true
            break;
          } else { this.isRequest = false }
        }
      })
      .catch(err=>console.log(err))
    },
    memberList(){
      axios.post(SERVER_URL+'/study/memberlist',{ pid: this.$route.params.post_id })
      .then(res=>{
        this.memberListData = res.data.object
        for (var i=0;i<this.memberListData.length;i++){
          if (this.memberListData[i].uid == this.profileInfo.uid){
            this.isMember = true
            break;
          } else { this.isMember = false }
        }
      }).catch(err=>console.log(err))
    },
    approvalStudy(uid){
      this.approvalData.pid = this.$route.params.post_id
      this.approvalData.uid = uid;
      axios.post(SERVER_URL + "/study/approval", this.approvalData)
      .then((res) => {
        this.$router.go()
      })
      .catch((err) => console.log(err))
    },
    studyRequest() {
      this.requestData.pid = this.postData.pid;
      this.requestData.uid = this.profileInfo.uid;
      axios
        .post(SERVER_URL + "/study/Srequest", this.requestData)
        .then((res) => {
          this.requestLists = res.data.object;
          this.requestPeopleList()
        })
        .catch((err) => console.log(err));
    },
    studyCancel(){
      this.cancelData.pid = this.postData.pid;
      this.cancelData.uid = this.profileInfo.uid
      axios.post(SERVER_URL + '/study/cancel',this.cancelData)
      .then(()=>{
        this.requestPeopleList()
      })
      .catch(err=>console.log(err))
    },


    likePost(){
      this.likeClickData.pid = this.$route.params.post_id 
      this.likeClickData.uid = this.profileInfo.uid
      console.log(this.likeClickData)
      axios.post(SERVER_URL+'/likep/likep',this.likeClickData)
      .then(res=>{
        this.$router.go()
      })
      .catch(err=>console.log(err))
    },
    goStudyMain(post_id) {
      this.$router.push({name: constants.URL_TYPE.STUDY.STUDYMAIN, params: { post_id: post_id },});
    },
    
    
    
    
    

    // 카카오톡 공유 관련
    test() {
      this.newReply.pid = this.postData.pid;
      // Kakao.Link.sendCustom({
      //   templateId: 33033,
      // });
      Kakao.Link.createDefaultButton({
        objectType: "feed",
        container: "#kakao-link",
        content: {
          title: '친구가 스터디 "' + this.postData.title + '"를 공유했습니다 !',

          //이미지와 버튼 링크 다르게 생성 가능
          //프로필 버튼 누르면 메인으로 가게끔 설정해놓음
          description: "",
          imageUrl:
            "https://cdn.a1news.co.kr/news/photo/202003/5196_6089_1728.png",
          link: {
            mobileWebUrl: "http://localhost:3000/#/post/" + this.postData.pid,
            webUrl: "http://localhost:3000/#/post/" + this.postData.pid,
          },
        },
        buttons: [
          {
            title: "바로가기",
            link: {
              mobileWebUrl: "http://localhost:3000/#/post/" + this.postData.pid,
              webUrl: "http://localhost:3000/#/post/" + this.postData.pid,
            },
          },
        ],
      });
    },
  },
};
</script>

<style>
</style>