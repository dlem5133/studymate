<template>
  <div style="margin-top:6rem;" class="container">
    <div class="container mb-5">
      <h1 class="mb-5" style="font-family:'Do Hyeon',sans-serif;">ADMIN</h1>
      <div>
        <b-card no-body>
          <b-tabs card >
            <b-tab title="유저">
              <div class="input-group flex-nowrap mx-auto my-3" @keypress.enter="searchUser(findUser)">
                <div class="d-flex mx-auto w-100 border" style="border-radius:30px;">
                  <input
                    v-model="findUser"
                    type="text"
                    class="border-0 form-control"
                    style="background-color: transparent;"
                    placeholder="Search"
                  />
                  <div class="input-group-prepend" @click="searchUser(findUser)">
                    <b-button style="cursor: pointer;" variant="border-0">
                      <b-icon icon="search"></b-icon>
                    </b-button>
                  </div>
                </div>
              </div>
              <table class="table">
                <thead class="thead" style="background-color:rgba(255,165,0,0.5);">
                  <tr>
                    <th class="nodisplay" scope="col">#</th>
                    <th scope="col">Nickname</th>
                    <th class="nodisplay" scope="col">Email</th>
                    <th class="nodisplay" scope="col">mileage</th>
                    <th class="nodisplay" scope="col">score</th>
                    <th scope="col">penalty</th>
                    <th scope="col"></th>
                  </tr>
                </thead>
                <tbody v-for="(user, ui) in searchuser" :key="user.uid">
                  <tr v-if="ui < userPage">
                    <th class="nodisplay" scope="row">{{ui+1}}</th>
                    <td>{{user.nickname}}</td>
                    <td class="nodisplay">{{user.email}}</td>
                    <td class="nodisplay">{{user.mileage}}</td>
                    <td class="nodisplay" >{{((user.score1 + user.score2 + user.score3) / 3).toFixed(1)}}</td>
                    <td>{{user.penalty}}</td>
                    <td>
                      <i class="fas fa-user-slash" @click="userDelete(user)"></i>
                    </td>
                  </tr>
                </tbody>
                <b-button class="d-flex plusBnt" @click="plusUserPage" variant="link">
                  <b-spinner small class="mr-1" variant="warning"></b-spinner>
                  <span style="font-family:'Do Hyeon',sans-serif;color:orange;">더보기</span>
                </b-button>
              </table>
            </b-tab>
            <b-tab @click="allStudy" title="스터디">
              <div class="input-group flex-nowrap mx-auto my-3" @keypress.enter="searchStudy(findStudy)">
                <div class="d-flex mx-auto w-100 border" style="border-radius:30px;">
                  <input
                    v-model="findUser"
                    type="text"
                    class="border-0 form-control"
                    style="background-color: transparent;"
                    placeholder="Search"
                  />
                  <div class="input-group-prepend" @click="searchStudy(findStudy)">
                    <b-button style="cursor: pointer;" variant="border-0">
                      <b-icon icon="search"></b-icon>
                    </b-button>
                  </div>
                </div>
              </div>
              <table class="table">
                <thead class="thead" style="background-color:rgba(255,165,0,0.5);">
                  <tr>
                    <th scope="col">#</th>
                    <th scope="col">Title</th>
                    <th class="nodisplay" scope="col">Content</th>
                    <th class="nodisplay" scope="col">Category</th>
                    <th class="nodisplay" scope="col">Posttime</th>
                    <th scope="col"></th>
                  </tr>
                </thead>
                <tbody v-for="(study, si) in searchstudies" :key="study.pid">
                  <tr v-if="si < studyPage">
                    <th scope="row">{{si+1}}</th>
                    <td>{{study.title}}</td>
                    <td class="nodisplay">{{study.data}}</td>
                    <td class="nodisplay">{{study.category}}</td>
                    <td class="nodisplay">{{study.posttime}}</td>
                    <td>
                      <i class="fas fa-trash-alt" @click="studyDelete(study)"></i>
                    </td>
                  </tr>
                </tbody>
              </table>
              <b-button class="d-flex plusBnt" @click="plusStudyPage" variant="link">
                <b-spinner variant="warning" small class="mr-1"></b-spinner>
                <span style="font-family:'Do Hyeon',sans-serif;color:orange;">더보기</span>
              </b-button>
            </b-tab>
            <b-tab @click="allReport" title="신고">
              <table class="table">
                <thead class="thead" style="background-color:rgba(255,165,0,0.5);">
                  <tr>
                    <th class="nodisplay" scope="col">#</th>
                    <th scope="col">Nickname</th>
                    <th class="nodisplay" scope="col">Email</th>
                    <th class="nodisplay" scope="col">mileage</th>
                    <th class="nodisplay" scope="col">score</th>
                    <th scope="col">penalty</th>
                  </tr>
                </thead>

                <tbody v-for="(reportUser, ri) in reports" :key="reportUser.uid">
                  <tr @click="$bvModal.show(`bv-modal-example_${ri}`)">
                    <th class="nodisplay" scope="row">{{ri+1}}</th>
                    <td>{{reportUser[1].nickname}}</td>
                    <td class="nodisplay">{{reportUser[1].email}}</td>
                    <td class="nodisplay">{{reportUser[1].mileage}}</td>
                    <td class="nodisplay">{{((reportUser[1].score1 + reportUser[1].score2 + reportUser[1].score3) / 3).toFixed(1)}}</td>
                    <td>{{reportUser[1].penalty}}</td>
                  </tr>
                  <b-modal :id="`bv-modal-example_${ri}`" hide-footer>
                    <template v-slot:modal-title>신고내용</template>
                    <div class="d-block text-center">
                      <h5 class="pb-3">스터디명 : {{reportUser[2].title}}</h5>
                      <b-list-group
                        class="m-2 text-left"
                        v-for="content in reportUser[0]"
                        :key="content.no"
                      >
                        <b-list-group-item>{{content.reason}}</b-list-group-item>
                      </b-list-group>
                      <div>
                        <i class="fas fa-user-slash mr-2" @click="givePenalty(reportUser[0][0])">
                          <small>페널티 부여</small>
                        </i>
                        <i class="fas fa-undo" @click="noPenalty(reportUser[0][0])">
                          <small>반려</small>
                        </i>
                      </div>
                    </div>
                  </b-modal>
                </tbody>
              </table>
            </b-tab>
            <b-tab title="차단유저">
              <table class="table">
                <thead class="thead"  style="background-color:rgba(255,165,0,0.5);">
                  <tr>
                    <th class="nodisplay" scope="col">#</th>
                    <th scope="col">Nickname</th>
                    <th class="nodisplay" scope="col">Email</th>
                    <th class="nodisplay" scope="col">mileage</th>
                    <th class="nodisplay" scope="col">score</th>
                    <th scope="col">penalty</th>
                    <th scope="col"></th>
                  </tr>
                </thead>
                <tbody v-for="(banUser, bi) in banUsers" :key="banUser.uid">
                  <tr>
                    <th class="nodisplay" scope="row">{{bi+1}}</th>
                    <td>{{banUser.nickname}}</td>
                    <td class="nodisplay">{{banUser.email}}</td>
                    <td class="nodisplay">{{banUser.mileage}}</td>
                    <td class="nodisplay">{{((banUser.score1 + banUser.score2 + banUser.score3) / 3).toFixed(1)}}</td>
                    <td>{{banUser.penalty}}</td>
                    <td>
                      <i class="fas fa-user-slash"></i>
                    </td>
                  </tr>
                </tbody>
              </table>
            </b-tab>
          </b-tabs>
        </b-card>
      </div>
    </div>
  </div>
</template>

<script>
import constants from "../lib/constants";
import axios from "axios";

const SERVER_URL = constants.ServerUrl;

export default {
  name: "AdminPage",
  data: () => {
    return {
      users: {},
      searchuser: {},
      studies: {},
      searchstudies: {},
      reports: {},
      banUsers: {},
      findUser: "",
      findStudy: "",
      userPage: 5,
      studyPage: 5,
    };
  },
  created() {
    this.allUser();
    this.allStudy()
  },
  methods: {
    allUser() {
      axios.post(SERVER_URL + "/admin/allUser").then((res) => {
        const allUsers = res.data.object;
        this.users = res.data.object.filter((allUsers) => allUsers.penalty < 3);
        this.searchuser = this.users
        this.banUsers = res.data.object.filter(
          (allUsers) => allUsers.penalty >= 3
        );
      });
    },
    userDelete(user) {
      const deleteData = {
        email: user.email,
        password: user.password,
      };
      axios
        .post(SERVER_URL + "/account/delete", deleteData)
        .then(() => {
          alert("회원을 삭제했습니다.");
          this.allUser();
        })
        .catch((err) => {
          console.log(err);
        });
    },
    allStudy() {
      axios.post(SERVER_URL + "/admin/allStudy").then((res) => {
        this.studies = res.data.object;
      });
      this.searchstudies = this.studies
    },
    studyDelete(study) {
      axios
        .post(SERVER_URL + "/study/delete", study)
        .then(() => {
          alert("스터디를 삭제했습니다.");
          this.allStudy();
        })
        .catch((err) => {
          console.log(err);
        });
    },
    allReport() {
      axios.post(SERVER_URL + "/admin/report").then((res) => {
        this.reports = res.data.object;
      });
    },
    givePenalty(user) {
      axios.post(SERVER_URL + "/admin/givepenalty", user).then(() => {
        this.allReport();
        this.allUser();
      });
    },
    noPenalty(user) {
      axios.post(SERVER_URL + "/admin/nopenalty", user).then(() => {
        this.allReport();
      });
    },
    searchUser(tmp) {
      const newUsers = this.users.filter((user) =>
        user.nickname.includes(tmp)
      );
      this.searchuser = newUsers;
    },
    searchStudy(tmp) {
      const newStudies = this.studies.filter((study) =>
        study.title.includes(tmp)
      );
      this.searchstudies = newStudies;
    },
    plusUserPage() {
      this.userPage += 5;
    },
    plusStudyPage() {
      this.studyPage += 5;
    },
  },
};
</script>

<style scoped>
tbody :hover {
  background-color: #eee;
}
i {
  cursor: pointer;
}
.plusBnt {
  position: relative;
  left: 35vw;
}
@media (max-width: 760px) {
  .nodisplay {
    display: none;
  }
}
</style>