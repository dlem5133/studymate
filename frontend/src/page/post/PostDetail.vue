<template>
  <div class="detail container">
    <div id="postwidth" class="container">
      <div class="card shadow border px-0">
        <div class="cardimg">
          <img style="position:relative;" :src="postData.background_image" alt height="270rem" width="60%" />
          <div style="position:absolute;top:5px;left:10px;">
            <b-badge variant="warning" class="my-auto" v-if="postData.tmp == 1">모집중</b-badge>
            <b-badge variant="success" class="my-auto" v-if="postData.tmp == 0">진행중</b-badge>
            <b-badge variant="info" class="my-auto" v-if="postData.tmp == 3">추가모집중</b-badge>
          </div>
          <div style="position:absolute;top:13.5rem;right:1rem;" v-if="
              profileInfo.uid != postData.uid && isLoggedIn && (postData.tmp==3 || postData.tmp == 1)
            ">
            <b-button variant="danger" style="font-family: 'Do Hyeon', sans-serif;border:1px solid white"
              v-if="!isRequest && !isMember" @click="studyRequest((isRequest = true))">신청</b-button>
            <b-button variant="danger" style="font-family: 'Do Hyeon', sans-serif;border:1px solid white"
              v-if="isRequest && !isMember" @click="studyCancel((isRequest = false))">취소</b-button>
          </div>
          <div style="position:absolute;top:13.5rem;right:1rem;" v-if="profileInfo.uid == postData.uid && isLoggedIn">
            <b-button variant="warning" style="font-family: 'Do Hyeon', sans-serif;border:1px solid white"
              v-if="postData.tmp==1||postData.tmp==3" @click="studyStart(postData.pid)">시작</b-button>
            <b-button variant="warning" style="font-family: 'Do Hyeon', sans-serif;border:1px solid white"
              v-if="postData.tmp==0" @click="studyMemberPlus(postData.pid)">추가모집</b-button>
          </div>
        </div>
        <div class="p-3">
          <div class="d-flex">
            <small class="float-left">[{{ postData.category }}]</small>

            <div class="d-flex ml-auto">
              <div v-if="userData.uid != profileInfo.uid" class="pb-1">
                <small @click="goLeaderProfile(userData.uid)" id="leader">
                  <b-icon icon="person-badge"></b-icon>
                  팀장 {{ userData.nickname }}
                </small>
              </div>
              <div class="d-flex" v-else>
                <b-dropdown right size="sm" v-if="profileInfo.uid==postData.uid" variant="link"
                  toggle-class="text-decoration-none" no-caret>
                  <template v-slot:button-content class>
                    <b-icon icon="gear" class="mr-1" variant="dark"></b-icon>
                    <small style="color:black;">SETTINGS</small>
                  </template>
                  <b-dropdown-item @click="postUpdate(postData.pid)">수정</b-dropdown-item>
                  <b-dropdown-item @click="postDelete">삭제</b-dropdown-item>
                </b-dropdown>
                <!-- 팀원 모달 -->
                <b-modal id="modal-1" title="팀원 승인대기" hide-footer>
                  <div v-for="per in requestListData" :key="per.uid" class="list-group">
                    <div v-if="!per.isjoin" class="px-3 d-flex list-group-item">
                      <div class="d-flex" style="cursor:pointer;" @click="goMemberProfile(per.uid)">
                        <p class="m-0 p-0 my-auto">{{ per.empId.user.nickname }}</p>
                        <b-icon variant="warning" class="ml-1 my-auto" icon="person"></b-icon>
                      </div>
                      <div class="ml-auto">
                        <b-button style="font-family: 'Do Hyeon', sans-serif;" v-if="profileInfo.uid == postData.uid"
                          @click="approvalStudy(per.uid)" variant="outline-success" class="btn-sm">승인</b-button>
                      </div>
                    </div>
                  </div>
                </b-modal>
                <b-button style="color:orange;" v-if="requestListData.length > 0" v-b-modal.modal-1 size="sm"
                  class="p-0 border-0" variant="link">
                  <b-icon class="ml-2" icon="person"></b-icon>
                  <small>승인대기</small>
                </b-button>
              </div>
            </div>
          </div>
          <div class="d-flex">
            <h5 class="mb-0 text-left">{{ postData.title }}</h5>
            <b-button style="color:orange;" v-if="isLoggedIn && (postData.tmp==0 || postData.tmp==3)"
              @click="goStudyMain(postData.pid)" size="sm" class="p-0 border-0" variant="link">
              <b-icon class="mx-1" icon="house-door"></b-icon>
              <small>진행페이지</small>
            </b-button>
            <div class="ml-auto">
              <b-icon v-if="likeData.length==0" class="mr-1" variant="danger" icon="heart" @click="likePost"></b-icon>
              <b-icon v-else class="mr-1" variant="danger" :icon="icon" @click="likePost"></b-icon>
              <small class="mr-2 my-auto">{{ likeData.length }}</small>
              <b-icon class="mr-1" @click="test" id="kakao-link" icon="share-fill" size="sm"></b-icon>
            </div>
          </div>
          <div class="text-left d-flex text-secondary"></div>
          <hr />
          <div class="text-left d-flex row justify-content-between">
            <div class="detailboard col-12 col-md-3 mb-2">
              <div class="py-3 my-auto">
                <div style="color:orange;" class="text-center mb-2">
                  <b-icon font-scale="2" icon="person"></b-icon>
                </div>
                <div class="my-auto text-center">
                  <p class="m-0 p-0">{{postData.memnum}} / {{ postData.limitp }} 명</p>
                </div>
              </div>
            </div>
            <div class="detailboard col-12 col-md-3 mb-2">
              <div class="py-3 my-auto">
                <div style="color:orange;" class="text-center mb-2">
                  <b-icon font-scale="2" icon="calendar2-date"></b-icon>
                </div>
                <div class="my-auto text-center">
                  <p class="m-0 p-0">
                    {{ changeDate(postData.start_date) }} ~
                    {{ changeDate(postData.end_date) }}
                  </p>
                </div>
              </div>
            </div>
            <div class="detailboard col-12 col-md-3 mb-2">
              <div class="py-3 my-auto">
                <div style="color:orange;" class="text-center mb-2">
                  <b-icon class="my-auto" font-scale="2" icon="calendar2-range-fill"></b-icon>
                </div>
                <div class="my-auto text-center">
                  <p class="m-0 p-0">주 {{ postData.bindo }} 회</p>
                </div>
              </div>
            </div>
          </div>
          <hr class="mb-0" />

          <div class="float-left w-100 mt-4">
            <small class="float-left text-left px-3">{{ postData.data }}</small>
          </div>
          <br />
          <div class="my-5 px-3">
            <b-button class="float-left mr-2 my-1 px-1 py-0" variant="outline-secondary" size="sm"
              v-for="tag in tagData" :key="tag.tid">
              <small># {{ tag.tagname }}</small>
            </b-button>
          </div>
        </div>
      </div>

      <br />

      <!-- 댓글 -->
      <div class="card shadow mb-5 mb-1">
        <p class="text-left m-0 px-3 py-3">
          <b-icon icon="chat-dots" class="mr-1 my-auto" flip-h></b-icon>댓글
        </p>
        <div v-if="isLoggedIn" class="mx-3 mb-3">
          <hr class="mt-0" />
          <div class="d-flex inline">
            <textarea style="resize:none;" type="text" v-model="newReply.reply_content" id="content"
              class="p-2 border rounded-lg w-100" />
            <div class="buttonitems border rounded-lg px-1 ml-2 my-auto">
              <b-icon icon="pencil" @click="replyCreate(replyparent)"></b-icon>
            </div>
          </div>
        </div>
        <hr v-if="isLoggedIn&&replyData.length>0" class="mb-4" />

        <div
          class="card-body w-100 text-left px-3 pt-0"
          v-for="comment in replyData"
          :key="comment.rid"
        >
          <div style="position:relative;" class="d-flex mb-1 w-100">
            <div style="min-height:4rem;" class="p-2 w-100 border rounded-lg">
              <div class="d-flex w-100">
                <div class="d-flex mb-2">
                  <div v-if="!comment.user.profile_image">
                    <img
                      class="border rounded-circle profileImg"
                      src="../../assets/img/defualt_image.png"
                    />
                  </div>
                  <div v-else>
                    <img class="rounded-circle profileImg" :src="comment.user.profile_image" />
                  </div>
                  <small class="mt-1 px-1">{{ comment.reply_writer }}</small>
                </div>
                <div class="ml-auto">
                  <div class="d-flex ml-auto">
                    <div
                      v-if="isLoggedIn"
                      title="댓글"
                      class="buttonitems border rounded-circle px-1 mr-1"
                    >
                      <b-icon
                        style="color:orange;"
                        shift-v="2"
                        icon="chat-dots"
                        flip-h
                        @click="reReply(comment.rid)"
                      ></b-icon>
                    </div>
                    <div
                      title="수정"
                      v-if="
                        comment.uid == profileInfo.uid &&
                          comment.reply_content != '삭제된 댓글입니다.'
                      "
                      class="buttonitems border rounded-circle px-1 mr-1"
                    >
                      <b-icon
                        variant="info"
                        icon="pencil"
                        @click="isReplyUpdate(comment.rid, comment.reply_content)"
                      ></b-icon>
                    </div>

                    <div
                      title="삭제"
                      v-if="
                        profileInfo.uid == comment.uid &&
                          comment.reply_content != '삭제된 댓글입니다.'
                      "
                      class="buttonitems border rounded-circle px-1"
                    >
                      <b-icon variant="danger" icon="trash" @click="replyDelete(comment)"></b-icon>
                    </div>
                  </div>
                </div>
              </div>
              <div class="d-flex w-100">
                <div class="replywidth px-2">
                  <small>{{ comment.reply_content }}</small>
                </div>
                <div class="ml-auto">
                  <small class="text-secondary">{{ changeDatedata(comment.reply_time) }}</small>
                </div>
              </div>
            </div>
          </div>

          <!-- 댓글 수정 -->
          <div class="d-flex w-100" v-if="isUpdate == true && comment.rid == replyId">
            <textarea
              style="font-size:0.8rem;resize:none;background-color:rgba(0,0,0,0.1)"
              type="text"
              v-model="updateReply.reply_content"
              id="content"
              class="ml-3 my-1 p-2 w-100 border rounded-lg"
            />
            <div class="buttonitems mt-auto border rounded-lg px-1 ml-2 my-1">
              <b-icon icon="pencil" @click="replyUpdate(comment.rid)"></b-icon>
            </div>
          </div>

          <!-- 대댓글 달기 -->
          <div class="d-flex w-100" v-if="isReply == true && replyparent == comment.rid">
            <b-icon class="ml-1 my-auto" icon="reply" flip-v></b-icon>
            <textarea
              style="resize:none;font-size:0.8rem;background-color:rgba(255,165,0,0.07);"
              type="text"
              placeholder="댓글을 작성해 주세요."
              v-model="newreReply.reply_content"
              id="content"
              class="ml-3 my-1 p-2 w-100 border rounded-lg"
            />
            <div class="buttonitems mt-auto border rounded-lg px-1 ml-2 my-1">
              <b-icon icon="pencil" @click="reReplyCreate(replyparent)"></b-icon>
            </div>
          </div>

          <!-- 대댓글 보기 -->
          <div class="w-100" v-for="reReply in reReplyData" :key="reReply.id">
            <div
              style="position:relative;"
              class="w-100"
              v-if="comment.rid === reReply.replyparent"
            >
              <div class="d-flex my-2 w-100">
                <b-icon icon="reply" class="ml-1 mr-3 my-auto" flip-v></b-icon>

                <div
                  style="background-color:rgba(255,165,0,0.07);"
                  class="p-2 w-100 border rounded-lg"
                >
                  <div class="d-flex">
                    <div class="d-flex mb-2">
                      <div v-if="!reReply.user.profile_image">
                        <img
                          class="border rounded-circle profileImg"
                          src="../../assets/img/defualt_image.png"
                        />
                      </div>
                      <div v-else>
                        <img class="rounded-circle profileImg" :src="reReply.user.profile_image" />
                      </div>
                      <small class="my-auto px-1">
                        {{
                        reReply.reply_writer
                        }}
                      </small>
                    </div>

                    <div class="ml-auto">
                      <div
                        title="삭제"
                        v-if="profileInfo.uid == reReply.uid"
                        class="buttonitems border rounded-circle px-1 mr-1"
                      >
                        <b-icon variant="danger" icon="trash" @click="replyDelete(reReply)"></b-icon>
                      </div>
                    </div>
                  </div>
                  <div class="d-flex w-100">
                    <div class="px-2 replywidth2">
                      <small>{{ reReply.reply_content }}</small>
                    </div>
                    <div class="ml-auto">
                      <small class="text-secondary">{{ changeDatedata(reReply.reply_time) }}</small>
                    </div>
                  </div>
                </div>
              </div>
            </div>
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
import VueSweetalert2 from "vue-sweetalert2";
import "sweetalert2/dist/sweetalert2.min.css";
import swal from "sweetalert";

const options = {
  confirmButtonColor: "orange",
  cancelButtonColor: "lightgray",
};

const SERVER_URL = constants.ServerUrl;

export default {
  name: "PostDetail",
  data: () => {
    return {
      profileInfo: [],
      isLoggedIn: false,

      postData: {},
      tagData: {},
      likeData: {},
      userData: {},
      replyData: {},
      reReplyData: {},
      islike: true,
      icon: "",
      requestListData: {},
      isRequest: false,

      memberListData: {},
      isMember: false,

      replyparent: 0,
      isReply: false,
      replyId: 0,
      isUpdate: false,

      newReply: {},
      newreReply: {},
      updateReply: {},

      approvalData: {},
      requestData: {},
      cancelData: {},
      requestLists: {},

      likeClickData: {},
    };
  },
  created() {
    window.scroll({ top: 0, left: 0, behavior: "smooth" });
    this.getDetail();
    this.userCheck();
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
    // window.scroll({top:0,left:0,behavior:'smooth'});
    this.getDetail();
  },
  methods: {
    changeDate(time) {
      if (time) {
        return (
          time.split("-")[0].substring(2, 4) +
          "/" +
          time.split("-")[1] +
          "/" +
          time.split("-")[2].substring(0, 2)
        );
      }
    },
     changeDatedata(time) {
      return time.substring(5, 10)
    },
    userCheck() {
      if (this.$cookies.isKey("Auth-Token")) {
        this.isLoggedIn = true;
        const token = this.$cookies.get("Auth-Token");
        axios
          .get(SERVER_URL + "/account/profile", {
            params: {
              Token: token,
            },
          })
          .then((res) => {
            this.profileInfo = res.data.object;
            this.requestPeopleList();
            this.memberList();
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
          var tmp = res.data.object[0];
          var t = require("@/assets/uploadfile/" + tmp.background_image);
          tmp.background_image = t;
          this.postData = tmp;
          this.tagData = res.data.object[2];
          this.likeData = res.data.object[3];
          this.userData = res.data.object[4];
          const datas = res.data.object[1];
          this.replyData = datas.filter((datas) => datas.replyparent == 0);
          this.reReplyData = datas.filter((datas) => datas.replyparent != 0);

          for (var i = 0; i < this.likeData.length; i++) {
            if (this.likeData[i].uid == this.profileInfo.uid) {
              this.islike = true;
              this.icon = "heart-fill";
              break;
            } else {
              this.islike = false;
              this.icon = "heart";
            }
          }
        })
        .catch((err) => {
          this.$router.go()
          this.$router.push({
            name: constants.URL_TYPE.POST.MAIN,
          });
        });
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
    postUpdate(post_id) {
      this.$router.push({
        name: constants.URL_TYPE.POST.POSTUPDATE,
        params: {
          post_id: post_id,
        },
      });
    },
    postDelete() {
      axios
        .post(SERVER_URL + "/study/delete", this.postData)
        .then((res) => {
          this.$router.push({
            name: constants.URL_TYPE.POST.MAIN,
          });
        })
        .catch((err) => console.log(err));
    },
    reReply(replyparent) {
      if (this.isReply == true) {
        this.isReply = false;
        this.replyparent = "";
      } else {
        this.isReply = true;
        this.isUpdate = false;
        this.replyparent = replyparent;
      }
    },
    isReplyUpdate(rid, comment) {
      if (this.isUpdate == true) {
        this.isUpdate = false;
        this.replyId = "";
        this.replyparent = "";
      } else {
        this.isUpdate = true;
        this.isReply = false;
        this.replyId = rid;
        this.updateReply.reply_content = comment;
        this.replyparent = "";
      }
    },
    replyCreate(replyparent) {
      if (
        this.newReply.reply_content == null ||
        this.newReply.reply_content == ""
      ) {
        swal("댓글 내용을 입력해주세요", { buttons: false, timer: 1200 });
      } else {
        this.newReply.pid = this.postData.pid;
        this.newReply.uid = this.profileInfo.uid;
        this.newReply.reply_parent = 0;
        axios
          .post(SERVER_URL + "/reply/write", this.newReply)
          .then(() => {
            this.getDetail();
            this.newReply.reply_content = "";
          })
          .catch((err) => console.log(err));
      }
    },
    reReplyCreate(replyparent) {
      if (
        this.newreReply.reply_content == null ||
        this.newreReply.reply_content == ""
      ) {
        swal("댓글 내용을 입력해주세요", { buttons: false, timer: 1200 });
      } else {
        this.newreReply.pid = this.postData.pid;
        this.newreReply.uid = this.profileInfo.uid;
        this.newreReply.reply_parent = replyparent;
        axios
          .post(SERVER_URL + "/reply/write", this.newreReply)
          .then(() => {
            this.getDetail();
            this.newreReply.reply_content = "";
            this.isReply = false;
            this.replyparent = "";
          })
          .catch((err) => console.log(err));
      }
    },
    replyUpdate(rid) {
      if (
        this.updateReply.reply_content == null ||
        this.updateReply.reply_content == ""
      ) {
        swal("댓글 내용을 입력해주세요", { buttons: false, timer: 1200 });
      } else {
        this.updateReply.pid = this.postData.pid;
        this.updateReply.uid = this.profileInfo.uid;
        this.updateReply.rid = rid;
        axios
          .post(SERVER_URL + "/reply/update", this.updateReply)
          .then(() => {
            this.getDetail();
            this.updateReply.reply_content = "";
            this.isUpdate = false;
          })
          .catch((err) => console.log(err));
      }
    },
    replyDelete(reply) {
      swal({
        text: "댓글을 삭제하시겠습니까?",
        dangerMode: true,
        buttons: true,
      }).then((willDelete) => {
        if (willDelete) {
          axios
            .post(SERVER_URL + "/reply/delete", reply)
            .then(() => {
              this.getDetail();
            })
            .catch((err) => console.log(err));
        }
      });
    },
    approvalStudy(uid) {
      this.approvalData.pid = this.$route.params.post_id;
      this.approvalData.uid = uid;
      if(this.postData.memnum<this.postData.limitp)
      {
              axios
        .post(SERVER_URL + "/study/approval", this.approvalData)
        .then(() => {
          this.requestPeopleList();
        })
        .catch((err) => console.log(err));
      }
      else{
        alert("인원이 가득찼습니다.")
      }
    },
    studyRequest() {
      this.requestData.pid = this.postData.pid;
      this.requestData.uid = this.profileInfo.uid;
      axios
        .post(SERVER_URL + "/study/Srequest", this.requestData)
        .then((res) => {
          this.requestLists = res.data.object;
          this.requestPeopleList();
          this.getDetail();
          this.$router.go();
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
          this.$router.go();
        })
        .catch((err) => console.log(err));
    },
    studyMemberPlus(post_id) {
      axios
        .get(SERVER_URL + "/study/recruitstart", { params: { pid: post_id } })
        .then(() => {
          this.getDetail();
          this.$router.go();
        })
        .catch((err) => console.log(err));
    },
    studyStart(post_id) {
      axios
        .get(SERVER_URL + "/study/recruitstop", { params: { pid: post_id } })
        .then(() => {
          this.getDetail();
          this.$router.go();
        })
        .catch((err) => console.log(err));
    },
    likePost() {
      if (this.isLoggedIn) {
        this.likeClickData.pid = this.$route.params.post_id;
        this.likeClickData.uid = this.profileInfo.uid;
        axios
          .post(SERVER_URL + "/likep/likep", this.likeClickData)
          .then((res) => {
            if (this.islike) {
              this.islike = false;
              this.icon = "heart";
            } else {
              this.islike = true;
              this.icon = "heart-fill";
            }
            this.getDetail();
          })
          .catch((err) => console.log(err));
      } else {
        this.islike = false;
        swal("로그인이 필요합니다.", { buttons: false, timer: 1200 });
      }
    },
    goStudyMain(post_id) {
      this.$router.push({
        name: constants.URL_TYPE.STUDY.STUDYMAIN,
        params: { post_id: post_id },
      });
    },
    goLeaderProfile(uid) {
      this.$router.push({
        name: constants.URL_TYPE.USER.MEMBERPROFILE,
        params: { user_id: uid },
      });
    },
    goMemberProfile(user_id) {
      this.$router.push({
        name: constants.URL_TYPE.USER.MEMBERPROFILE,
        params: {
          user_id: user_id,
        },
      });
    },
    // 카카오톡 공유
    test() {
      this.newReply.pid = this.postData.pid;
      Kakao.Link.createDefaultButton({
        objectType: "feed",
        container: "#kakao-link",
        content: {
          title: '친구가 스터디 "' + this.postData.title + '"를 공유했습니다 !',
          //이미지와 버튼 링크 다르게 생성 가능
          //프로필 버튼 누르면 메인으로 가게끔 설정해놓음
          description: "",
          imageUrl:
            "https://user-images.githubusercontent.com/29462979/90760146-687f8b00-e31c-11ea-9277-938a206c1748.png",
        link: {
            mobileWebUrl:
              "http://i3b205.p.ssafy.io:8081/#/post/" + this.postData.pid,
            webUrl: "http://i3b205.p.ssafy.io:8081/#/post/" + this.postData.pid,
          },
        },
        buttons: [
          {
            title: "바로가기",
            link: {
              mobileWebUrl:
                "http://i3b205.p.ssafy.io:8081/#/post/" + this.postData.pid,
              webUrl:
                "http://i3b205.p.ssafy.io:8081/#/post/" + this.postData.pid,
            },
          },
        ],
      });
    },
  },
};
</script>

<style scoped>
* {
  font-family: "Noto Sans KR", sans-serif;
}
.replywidth,
.replywidth2 {
  width: 40rem;
}
#postwidth{
  padding:0 10rem;
}
@media (max-width:480px){
  #postwidth{
    padding:0 1rem;
  }
}
@media (max-width: 760px) {
  #postwidth{
    padding:0 1rem;
  }
  .replywidth {
    width: 12rem;
  }
  .replywidth2 {
    width: 9rem;
  }
}
.detail {
  margin-top: 6rem;
}
.detailboard {
  position: relative;
}
#detailboardtext {
  position: absolute;
  top: -13%;
  background-color: white;
  left: 47%;
}
.buttonitems:hover {
  background-color: rgba(0, 0, 0, 0.1);
}
.profileImg {
  width: 1.5rem;
  height: 1.5rem;
}
.profileImg1 {
  width: 1.2rem;
  height: 1.2rem;
}
#leader {
  color: gray;
}
#leader:hover {
  color: black;
  cursor: pointer;
}

.images {
  overflow: hidden;
}
.imagefile {
  max-width: 100%;
  max-height: 30em;
}
</style>