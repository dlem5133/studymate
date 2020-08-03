<template>
  <div class="container">
    <div class="main-table">
      <div class="container p-2">
        <div class="ml-auto text-right p-1 " v-if="profileInfo.uid==postData.uid">
                <b-button size="sm" class="mr-2 font-weight-bold" @click="postUpdate(postData.pid)"
                   variant="outline-primary">수정</b-button>
                <b-button size="sm" class="font-weight-bold" @click="postDelete"
                  variant="outline-danger">삭제</b-button>
              </div>
        <div class="card shadow p-3">
          
          <div class="card-body">
            <div class="row">

              <div class="col-8 col-md-6 col-sm-4">
                
                <div>

                  
                  <b-modal id="modal-6" title="팀원 보기">
                    <form ref="form">
                      <b-form-group>
                        <div v-for="per in memberListData" :key="per.uid">
                          <div @click="goStudyMain(postData.pid)" v-if="per.isjoin" class="p-3 d-flex inline">
                            <p class="px-2 m-0 col-xs-4 col-sm-7">{{ per.empId.user.nickname }} 
                              <b-button class="btn-sm mr-2 float-right " variant="primary" @click="delegation(per.uid)"> 위임 </b-button>
                            </p>
                            <div v-if="per.empId.user.uid == postData.uid">팀장</div>
                            <div v-else> 팀원 </div>

                          </div>
                          <hr class="m-0" />
                        </div>
                      </b-form-group>
                    </form>
                  </b-modal>

                </div>
                <h3 class="text-left font-weight-bold">{{ postData.title }} 팀장페이지</h3>
                
                <small class="float-left">{{ userData.nickname }},</small><br>
                <small class="d-flex inline">{{postData.start_date}} ~ {{postData.end_date}}</small>
                <br>
                <b-button class="btn-sm float-left" variant="primary" v-b-modal.modal-5> 탈퇴 대기 </b-button>
                <b-modal id="modal-5" title="스터디 탈퇴 목록">
                    <form ref="form">
                      <b-form-group>
                        <div v-for="per in DeleteMemberListData" :key="per.uid">
                          <div @click="goStudyMain(postData.pid)" v-if="per.isjoin" class="p-3 d-flex inline">
                            <p class="px-2 m-0 col-xs-4 col-sm-7">{{ per.empId.user.nickname }} 
                              <b-button class="btn-sm mr-2 float-right " variant="success" @click="DeleteApply(per.uid)"> 승인 </b-button>
                              <b-button class="btn-sm mr-2 float-right " variant="danger" @click="DeleteCancel(per.uid)"> 취소 </b-button>
                            </p>
                            <div v-if="per.empId.user.uid == postData.uid">팀장</div>
                            <div v-else> 팀원 </div>

                          </div>
                          <hr class="m-0" />
                        </div>
                      </b-form-group>
                    </form>
                  </b-modal>

                <b-button class="btn-sm float-left" variant="success" v-b-modal.modal-6> 팀장 위임 </b-button>

              </div>

              

            </div>
          </div>
        </div>
        <div class="ml-auto text-right p-1" v-if="profileInfo.uid==postData.uid">

          <b-button class="btn-sm mr-2 " variant="warning" v-b-modal.modal-3>생성 </b-button>
          <b-modal @ok="expectOk" id="modal-3" title="일정 추가">
            <form ref="form">
              <b-form-group id="expectdoWrite">
                <div class="input-wrap mx-3">
                  <input class="p-3 border-bottom" v-model="expectData.doplace" id="doplace" placeholder="장소 입력"
                    type="text" />
                </div>
                <div class="input-wrap mx-3">
                  <input class="p-3 border-bottom" v-model="expectData.dodate" type="datetime-local" id="dodate"
                    placeholder="날짜 입력" />
                </div>
                <div class="input-wrap mx-3">
                  <input class="p-3 border-bottom" v-model="expectData.assignment" type="text" id="assignment"
                    placeholder="할일 입력" />
                </div>
              </b-form-group>
            </form>
          </b-modal>
          <b-button class="btn-sm mr-2 " variant="primary" v-b-modal.modal-4 >수정</b-button>
          <b-modal @ok="expectUpdate" id="modal-4" title="일정 업데이트">
            <form ref="form">
              <b-form-group id="expectdoUpdate">
                <div class="input-wrap mx-3">
                  <input class="p-3 border-bottom" v-model="expectData.doplace" id="doplace" placeholder="장소 입력"
                    type="text" />
                </div>
                <div class="input-wrap mx-3">
                  <input class="p-3 border-bottom" v-model="expectData.dodate" type="datetime-local" id="dodate"
                    placeholder="날짜 입력" />
                </div>
                <div class="input-wrap mx-3">
                  <input class="p-3 border-bottom" v-model="expectData.assignment" type="text" id="assignment"
                    placeholder="할일 입력" />
                </div>
              </b-form-group>
            </form>
          </b-modal>
          <b-button class="btn-sm" variant="success" @click="expectDelete">삭제</b-button>

        </div>
        
        <div class="card my-1">
          <div class="card-body text-left">
            <p class="badge badge-pill badge-danger">D-{{decimalDay}}


            </p>

            <p>장소 : {{expectTodo.doplace}}</p>
            <p>일시 : {{expectTodo.dodate}}</p>
            <p>과제 : {{expectTodo.assignment}}</p>
          </div>
        </div>
        <div class="ml-auto text-right p-1" v-if="profileInfo.uid==postData.uid">
          <b-button class="btn-sm" variant="warning" v-b-modal.modal-1 >요일 선택 </b-button>
          <b-modal @ok="handleOk" id="modal-1" title="일지 작성가능한 요일을 정하세요.">
            <form ref="form">
              <b-form-group>
                <b-form-checkbox-group v-model="selectedDay" v-for="day in week" :key="day">
                  <b-form-checkbox :value="day">{{day}}</b-form-checkbox>
                </b-form-checkbox-group>
              </b-form-group>
            </form>
          </b-modal>
          
        </div>



        <div @click="getDaily">
          <b-calendar :date-info-fn="dateClass" v-model="value" block locale="ko-kr"></b-calendar>
        </div>

        <hr />
        <div class="card" style="width: 100%;">
          <div class="card-header">
            {{value}}
            <i @click="goDailyCreate" class="fas fa-pen-square createpoint"> 새글 작성</i>
          </div>
          <ul v-for="(dailyList, i) in dailyLists" :key="dailyList.did" class="list-group text-left list-group-flush">
            <li @click="goDailyDetail(dailyList.did)" class="list-group-item dailyc;-ss">
              {{i+1}}. {{dailyList.title}}
              <b-badge variant="success">{{dailyList.writer}}</b-badge>

            </li>
          </ul>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import axios from "axios";
  import constants from "../../lib/constants";

  const SERVER_URL = "http://localhost:8080";

  export default {
    name: "PostDetail",
    data: () => {
      return {
        profileInfo: [],
        memberListData: [],
        DeleteMemberListData: [],
        postData: [],
        tagData: [],
        userData: [],
        studyLists: [],
        value: "날짜를 선택해주세요.",
        allDailyLists: [],
        dailyLists: [],
        checkPost: [],
        expectTodo: [],
        expectData: {
          dodate: null,
          doplace: "",
          assignment: "",
          uid: "",
          pid: "",
          eid: 0
        },
        decimalDay: "",
        delegationData: {
          leader: "",
          member: "",
          pid: 0
        },
        indvData:{
          uid : 0,
          pid : 0,
          isJoin : 0,
          isLeader : 0
        },
        updateData: {
          study: [],
          tag: []

        },


        expectDataList: [],
        selectedDay: [],
        week: ['월요일', '화요일', '수요일', '목요일', '금요일', '토요일', '일요일'],
        days: [],

        readyLists: [],
        leaderLists: [],
        unleaderLists: [],

        leaderListsTmp1: [],

        leaderListsTmp0: [],

      };
    },
    created() {
      this.addprofileInfo();
      this.memberList()
      this.getDetail()
      this.DeleteMemberList()
    },
    mounted() {
      this.getPostTime()
    },
    methods: {
      handleOk() {
        let dayString = []
        for (var i = 0; i < this.selectedDay.length; i++) {
          dayString += this.selectedDay[i]
        }
        this.postData.days = dayString
        this.updateData.study = this.postData
        this.updateData.tag = this.tagData
        console.log(this.updateData)
        axios
          .post(SERVER_URL + "/study/update", this.updateData)
          .then(res => {
            console.log(res.data.object)
          })
          .catch(err => {
            console.log(err)
          })
      },
      addStudyList() {
        axios
          .post(SERVER_URL + "/account/studylist", this.profileInfo)
          .then((res) => {
            this.leaderLists = res.data.object.filter(item => item.isleader != 0)
            this.leaderListsTmp0 = this.leaderLists.filter(item => item.empId.study.tmp == 0)
            this.leaderListsTmp1 = this.leaderLists.filter(item => item.empId.study.tmp == 1)
            this.unleaderLists = res.data.object.filter(item => item.isleader == 0)
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
      addprofileInfo() {
        if (this.$cookies.isKey("Auth-Token")) {
          const token = this.$cookies.get("Auth-Token");
          axios.get(SERVER_URL + "/account/profile", {
              params: {
                Token: token,
              },
            })
            .then((res) => {
              this.profileInfo = res.data.object;
              this.addStudyList();
              this.addReadyList()
            })
            .catch((err) => {
              this.$router.push({
                name: constants.URL_TYPE.ERROR.ERRORPAGE,
                params: {
                  code: err.response.data
                },
              });
            });
        }
      },
      goStudyMain(post_id) {
        this.$router.push({
          name: constants.URL_TYPE.STUDY.STUDYMAIN,
          params: {
            post_id: post_id
          },
        });
        this.$router.go()
      },
      postUpdate(post_id) {
        this.$router.push({
          name: constants.URL_TYPE.POST.POSTUPDATE,
          params: {
            post_id: post_id
          },
        });
      },
      postDelete() {
        axios
          .post(SERVER_URL + "/study/delete", this.postData)
          .then((res) => {
            this.$router.push({
              name: constants.URL_TYPE.POST.MAIN
            });
          })
          .catch((err) => console.log(err));
      },
      memberList() {
        axios.post(SERVER_URL + '/study/memberlist', {
            pid: this.$route.params.post_id
          })
          .then(res => {
            this.memberListData = res.data.object
          })
      },
      DeleteMemberList(){
        axios.post(SERVER_URL + '/study/detail/deleteList', {
            pid: this.$route.params.post_id
          })
          .then(res => {
            console.log(res.data.object)
            this.DeleteMemberListData = res.data.object
          })
      },
      
      getDetail() {
        const post_id = this.$route.params.post_id;
        axios
          .get(SERVER_URL + "/study/details", {
            params: {
              pid: post_id
            }
          })
          .then((res) => {
            
            console.log(res);
            console.log(res.data)
            console.log(res.data.object)
            this.postData = res.data.object[0];
            this.tagData = res.data.object[2]
            this.userData = res.data.object[4];
            for (var i = 0; i < this.postData.days.length;) {
              this.days.push(this.postData.days.slice(i, i + 3))
              i += 3
            }
            
          })
          .catch((err) => console.log(err.data));
      },
      getDaily() {
        axios.get(SERVER_URL + "/diary/list", {
            params: {
              pid: this.$route.params.post_id,
              tmp: 1
            }
          })
          .then(res => {
            const tmpData = res.data.object[0]
            this.dailyLists = tmpData.filter(tmpData => tmpData.posttime === this.value)
          })
          .catch(err => {
            console.log(err)
          })
      },
      goDailyCreate() {
        var date = new Date();
        var year = date.getFullYear();
        var month = date.getMonth() + 1
        var day = date.getDate();
        if (month < 10) {
          month = "0" + month;
        }
        if (day < 10) {
          day = "0" + day;
        }
        var today = year + "-" + month + "-" + day;
        let flag = false

        for (var i = 0; i < this.allDailyLists.length; i++) {
          if (this.allDailyLists[i].posttime === today && this.allDailyLists[i].uid === this.profileInfo.uid) {
            alert('일지는 하루에 한번만 작성가능 합니다.')
            flag = true
            break
          }
        }
        if (!flag) {
          this.$router.push({
            name: constants.URL_TYPE.STUDY.DAILYCREATE,
            params: {
              post_id: this.$route.params.post_id
            },
          });
        }
      },
      goDailyDetail(daily_id) {
        this.$router.push({
          name: constants.URL_TYPE.STUDY.DAILYDETAIL,
          params: {
            post_id: this.$route.params.post_id,
            daily_id: daily_id
          },
        });
      },
      getPostTime() {
        axios.get(SERVER_URL + "/diary/list", {
            params: {
              pid: this.$route.params.post_id,
              tmp: 1
            }
          })
          .then(res => {
            this.expectTodo = res.data.object[1]
            console.log(res.data.object);
            console.log(this.expectTodo);
            var today = new Date()
            var count = new Date(res.data.object[1].dodate)
            var dday = Math.floor((count - today) / 1000 / 24 / 60 / 60)
            if (dday <= 0) {
              this.decimalDay = 'day'
            } else {
              this.decimalDay = dday
            }
            this.allDailyLists = res.data.object[0]
            var i = 0;
            while (i < this.allDailyLists.length) {
              this.checkPost.push(this.allDailyLists[i].posttime)
              i++;
            }
          })
          .catch(err => {
            console.log(err)
          })
      },
      dateClass(ymd, date) {
        var d = this.week[date.getDay()]
        if (this.checkPost.indexOf(ymd) !== -1) {
          return 'table-success'
        }

        if (this.days.indexOf(d) !== -1) {
          return 'table-secondary'
        }
      },
      goBoard(post_id) {
        this.$router.push({
          name: constants.URL_TYPE.BOARD.BOARDMAIN,
          params: {
            post_id: post_id
          },
        });
      },
      expectOk() {
        console.log(this.expectData);
        this.expectData.uid = this.profileInfo.uid
        this.expectData.pid = this.postData.pid
        axios.post(SERVER_URL + "/upcoming/create", this.expectData)
          .then((res) => {
            this.expectUpdate()
            this.getPostTime()
          })
          .catch((err) => console.log(err));
      },
      expectUpdate() {
        console.log(this.expectTodo);
        this.expectData.eid = this.expectTodo.eid;
        this.expectData.uid = this.profileInfo.uid
        this.expectData.pid = this.postData.pid
        console.log(this.expectData)
        axios.post(SERVER_URL + "/upcoming/update", this.expectData)
          .then((res) => {

            this.expectTodo = res.data.object
            this.getPostTime()
          })

          .catch((err) => console.log(err));
      },
      expectDelete() {
        this.expectData.eid = this.expectTodo.eid;
        this.expectData.uid = this.profileInfo.uid
        this.expectData.pid = this.postData.pid
        console.log(this.expectData)
        axios.post(SERVER_URL + "/upcoming/delete", this.expectData)
          .then((res) => {

            this.expectTodo = res.data.object
            this.getPostTime()
          })

          .catch((err) => console.log(err));
      },
      delegation(memberid){
        this.delegationData.pid = this.postData.pid;
        this.delegationData.leader = this.postData.uid;
        this.delegationData.member = memberid;
        console.log(this.delegationData)
        axios.post(SERVER_URL + "/study/detail/delegation", this.delegationData)
          .then((res) => {
            console.log(res);
          })

          .catch((err) => console.log(err));
      },
      deleteApply(memberid){
        this.indvData.uid = memberid;
        this.indvData.pid = this.postData.pid;
        this.indvData.isLeader = 0;
        this.indvData.isJoin = 1;
        console.log(this.indvData)
        axios.post(SERVER_URL + "/study/detail/delete_apply", this.indvData)
          .then((res) => {
            console.log(res);
          })

          .catch((err) => console.log(err));
      },
      deleteCancel(memberid){
          this.indvData.uid = memberid;
        this.indvData.pid = this.postData.pid;
        this.indvData.isLeader = 0;
        this.indvData.isJoin = 1;
        console.log(this.delegationData)
        axios.post(SERVER_URL + "/study/detail/delete_companion", this.indvData)
          .then((res) => {
            console.log(res);
          })

          .catch((err) => console.log(err));
      }
    },
  };
</script>

<style>
  .clickstudy {
    cursor: pointer;
  }

  .createpoint {
    position: absolute;
    right: 2%;
    cursor: pointer;
    font-size: large;
  }

  .dailycss:hover {
    cursor: pointer;
    background-color: #eee;
  }

  /* .calendar {
  background-color: ;
} */
</style>