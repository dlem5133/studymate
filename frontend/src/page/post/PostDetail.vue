<template>
  <div>
    <div class="container">
      <!-- <div class="card shadow p-3"> -->
      <!-- <div class="card-body"> -->
        <div class="col-12 col-sm-12 col-md-12">
          <div class="blog-header">
            <div class="blog-cover">
              <img class="" :src="postData.background_image" width="100%" height="240px"/>
            </div>
          </div>
          <div class="d-flex justify-content-end">
            <span class="btn" @click="test" id="kakao-link">
              <b-icon icon="share"></b-icon>
            </span>
          </div>
          <div class="d-flex align-items-end flex-column bd-highlight mb-3">
            <small class="ml-1">{{changeDate(postData.posttime)}}</small>
            <b-badge variant="warning" class="ml-1" size="sm" v-if="postData.tmp==1">모집중</b-badge>
            <b-badge variant="warning" class="ml-1" size="sm" v-if="postData.tmp==0">진행중</b-badge>
          </div>
        </div>
      <hr />
      <div class="card-body row">
        <h4>{{ postData.title }}</h4>
        <div class="ml-auto col-3 text-right mt-2">
          <b-icon
            class="mr-2 font-weight-bold"
            variant="outline-secondary"
            @click="goStudyMain(postData.pid)"
            icon="house-door"
          ></b-icon>
          <b-icon v-if="!islike" :icon="changeLike" @click="likePost(islike=true)"></b-icon>
          <b-icon variant="danger" v-else :icon="changeLike" @click="likePost(islike=false)"></b-icon>
          <small class="ml-1">{{likeListData.length}}</small>
        </div>
        <div class="d-flex flex-column col-12 p-0">
          <div class="float-left">
            <p class="float-left font-weight-bold pr-3">카테고리</p>
            <p class="float-left pr-3">{{ postData.category }}</p>
          </div>

          <div class="float-left">
            <p class="float-left font-weight-bold pr-3">기간</p>
            <p
              class="float-left pr-3"
            >{{changeDate(postData.start_date)}} ~ {{changeDate(postData.end_date)}}</p>
          </div>
          <div class="d-flex float-left align-items-center">
            <p class="float-left font-weight-bold pr-3">인원</p>
            <p class="float-left pr-3">{{ postData.limitp }}명</p>
          </div>
          <div class="d-flex float-left align-items-center">
            <p class="float-left font-weight-bold pr-3">빈도</p>
            <p class="float-left pr-3">주 {{ postData.bindo }}회</p>
          </div>
          <div class="float-left">
            <p class="float-left font-weight-bold pr-3">설명</p>
            <p class="float-left text-left pr-3">{{postData.data}}</p>
          </div>
          <div class="blog-tags">
              <a
                class="float-left mr-1 mt-1"
                variant="warning"
                v-for="tag in tagData"
                :key="tag.tid"
              >#{{tag.tagname}}</a>
          </div>
        </div>
      </div>
      <div class="d-flex inline justify-content-center">
        <div v-if="profileInfo.uid!=postData.uid&&postData.tmp==1">
          <b-button v-if="!isRequest&&!isMember" @click="studyRequest(isRequest=true)">신청</b-button>
        </div>
      </div>
      <!-- </div> -->
      <!-- </div> -->
      <br />
      <div v-if="profileInfo.uid==postData.uid||isRequest" class="card">
        <div class="card-header font-weight-bold">
          <h5 class="float-left font-weight-bold m-2">승인 대기</h5>
        </div>
        <div class="card-body text-left p-0">
          <div v-for="per in requestListData" :key="per.uid">
            <div v-if="!per.isjoin" class="p-3 d-flex inline">
              <p class="px-2 m-0 col-xs-4 col-sm-7">{{ per.empId.user.nickname }}</p>
              <b-button
                v-if="per.uid==profileInfo.uid"
                @click="studyCancel(isRequest=false)"
                variant="outline-danger"
                class="btn-sm ml-auto"
              >취소</b-button>
              <b-button
                v-if="profileInfo.uid==postData.uid"
                @click="approvalStudy(per.uid)"
                variant="outline-success"
                class="btn-sm ml-auto"
              >승인</b-button>
            </div>
            <hr class="m-0" />
          </div>
        </div>
      </div>
      <br />
      <div class="card shadow mb-5">
        <div class="card-header font-weight-bold">
          <h5 class="float-left font-weight-bold m-2">COMMENTS</h5>
        </div>
        <div class="card-body text-left p-0" v-for="comment in replyData" :key="comment.rid">
          <div>
            <div class="p-3 d-flex inline">
              <p class="px-2 m-0 col-xs-4 col-sm-7">{{ comment.reply_content }}</p>
              <!-- <b-icon icon="person"></b-icon> -->
              <small class="ml-auto text-muted">{{ comment.reply_writer }}</small>
            </div>
            <div class="pt-0 px-4 text-secondary d-flex inline card-body text-left">
              <small class="mt-auto">
                {{changeDatedata(comment.reply_time)}}
                <!-- 수정 : {{changeDatedata(comment.updated_at)}} -->
              </small>
              <div class="ml-auto">
              <b-button
                size="sm"
                variant="outline-success"
                @click="reReply(comment.rid)">
              <small>답댓</small></b-button>
              <b-button
                size="sm"
                variant="outline-danger"
                @click="replyDelete(comment)"
                v-if="profileInfo.uid==comment.uid&&comment.reply_content!='삭제된 댓글입니다.'"
              >
                <small>Delete</small>
              </b-button>
              </div>
            </div>
            <div v-for="reReply in reReplyData" :key="reReply.id">
              <div v-if="comment.rid === reReply.replyparent">
                <div class="d-flex">
                  <p class="px-4 py-2 m-0 col-xs-4 col-sm-7"><b-icon icon="reply" flip-v></b-icon> {{ reReply.reply_content }}</p>
                  <small class="ml-auto text-muted">{{ reReply.reply_writer }}</small>

                  <b-button @click="replyDelete(reReply)" class="ml-auto my-auto mr-4" size="sm" variant="outline-danger"><b-icon icon="trash"></b-icon></b-button>
                </div>
              </div>
            </div>
            <hr class="m-0" />
          </div>
          
        </div>
        <div class="p-3 pb-4">
          <p v-if="replyparent==0" class="align-self-center m-1 text-left font-weight-bold">Comments</p>
          <p v-else class="align-self-center m-1 text-left font-weight-bold">{{replyparent}}'s Comments</p>
          <div class="d-flex inline">
            <textarea
              type="text"
              v-model="newReply.reply_content"
              id="content"
              class="border w-100"
            />
            <b-button
              class="ml-3"
              variant="outline-secondary"
              @click="replyCreate(replyparent)"
            >작성</b-button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import constants from "../../lib/constants";
import "../../assets/css/postdetail.scss";

const SERVER_URL = constants.ServerUrl;

export default {
  name: "PostDetail",
  data: () => {
    return {
      profileInfo: [],

      postData: {},
      replyData: {},
      reReplyData:{},
      tagData: {},
      userData: {},
      newReply: {
      },
      requestListData: {},
      approvalData: {
        pid: null,
        uid: null,
      },
      requestData: {
        pid: null,
        uid: null,
      },
      cancelData: {
        pid: null,
        uid: null,
      },
      requestLists: {},
      isRequest: false,
      memberListData: {},
      islike: false,
      likeData: {},
      likeListData:{},
      likeClickData: {
        pid: null,
        uid: null,
      },
      isMember: false,
      replyparent:0,
    };
  },
  created() {
    this.userCheck();
    // 카카오톡 공유 관련
    Kakao.init("d5e6e1f1140f3ded1483ff360cb5a153");
  },
  computed: {
    changeLike() {
      if (this.islike) {
        return "heart-fill";
      } else {
        return "heart";
      }
    },
  },
  mounted() {
    this.getDetail();
  },
  methods: {
    reReply(replyparent){
      console.log(replyparent)
      this.replyparent=replyparent
    },


    likeList(){
      axios.get(SERVER_URL+'/likep/list',{params:{pid:this.$route.params.post_id}})
      .then(res=>{
        this.likeListData = res.data.object
      })
      .catch(err=>console.log(err))
    },

    likePost() {
      this.likeClickData.pid = this.$route.params.post_id;
      this.likeClickData.uid = this.profileInfo.uid;
      axios
        .post(SERVER_URL + "/likep/likep", this.likeClickData)
        .then((res) => {
          this.likeList()
        })
        .catch((err) => console.log(err));
    },
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
            this.profileInfo = res.data.object;
            this.getDetail();
            this.requestPeopleList();
            this.memberList();
            this.likeList()
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
          this.postData = res.data.object[0];
          // this.replyData = res.data.object[1];
          this.tagData = res.data.object[2];
          this.likeData = res.data.object[3];
          this.userData = res.data.object[4];
          const datas = res.data.object[1]
          console.log(res.data.object);
          this.replyData = datas.filter(datas => datas.replyparent==0)
          this.reReplyData = datas.filter(datas => datas.replyparent!=0)

          for (var i = 0; i < this.likeData.length; i++) {
            if (this.likeData[i].uid == this.profileInfo.uid) {
              this.islike = true;
              break;
            } else {
              this.islike = false;
            }
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
    replyCreate(replyparent) {
      this.newReply.pid = this.postData.pid;
      this.newReply.uid = this.profileInfo.uid;
      this.newReply.reply_parent = replyparent;
      console.log(this.newReply)
      axios
        .post(SERVER_URL + "/reply/write", this.newReply)
        .then(()=> {
          this.getDetail()
          this.newReply.reply_content = ""
        })
        .catch((err) => console.log(err));
    },
    replyDelete(reply) {
      axios
        .post(SERVER_URL + "/reply/delete", reply)
        .then(() => {
          this.getDetail()
        })
        .catch((err) => console.log(err));
    },
    requestPeopleList() {
      axios
        .post(SERVER_URL + "/study/requestlist", {
          pid: this.$route.params.post_id,
        })
        .then((res) => {
          this.requestListData = res.data.object;
          for (var i = 0; i < this.requestListData.length; i++) {
            if (this.requestListData[i].uid == this.profileInfo.uid) {
              this.isRequest = true;
              break;
            } else {
              this.isRequest = false;
            }
          }
        })
        .catch((err) => console.log(err));
    },
    memberList() {
      axios
        .post(SERVER_URL + "/study/memberlist", {
          pid: this.$route.params.post_id,
        })
        .then((res) => {
          this.memberListData = res.data.object;
          for (var i = 0; i < this.memberListData.length; i++) {
            if (this.memberListData[i].uid == this.profileInfo.uid) {
              this.isMember = true;
              break;
            } else {
              this.isMember = false;
            }
          }
        })
        .catch((err) => console.log(err));
    },
    approvalStudy(uid) {
      this.approvalData.pid = this.$route.params.post_id;
      this.approvalData.uid = uid;
      axios
        .post(SERVER_URL + "/study/approval", this.approvalData)
        .then(() => {
          this.requestPeopleList();
        })
        .catch((err) => console.log(err));
    },
    studyRequest() {
      this.requestData.pid = this.postData.pid;
      this.requestData.uid = this.profileInfo.uid;
      axios
        .post(SERVER_URL + "/study/Srequest", this.requestData)
        .then((res) => {
          this.requestLists = res.data.object;
          this.requestPeopleList();
        })
        .catch((err) => console.log(err));
    },
    studyCancel() {
      this.cancelData.pid = this.postData.pid;
      this.cancelData.uid = this.profileInfo.uid;
      axios
        .post(SERVER_URL + "/study/cancel", this.cancelData)
        .then(() => {
          this.requestPeopleList();
        })
        .catch((err) => console.log(err));
    },
    goStudyMain(post_id) {
      this.$router.push({
        name: constants.URL_TYPE.STUDY.STUDYMAIN,
        params: { post_id: post_id },
      });
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