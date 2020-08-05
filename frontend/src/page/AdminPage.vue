<template>
  <div class="container">
    <h1>ADMIN PAGE</h1>
    <hr>
    <div>
      <b-tabs content-class="mt-3">
        <b-tab title="유저">
          <div class="input-group m-1" @keypress.enter="searchUser(findUser)">
            <input class="form-control" v-model="findUser" type="text" placeholder="닉네임을 입력해주세요">
            <button class="input-group-text" @click="searchUser(findUser)">
              <i class="fas fa-search"></i>
            </button>
          </div>
          <table class="table">
            <thead class="thead-dark">
              <tr>
                <th scope="col">#</th>
                <th scope="col">Nickname</th>
                <th scope="col">Email</th>
                <th scope="col">mileage</th>
                <th scope="col">score</th>
                <th scope="col">penalty</th>
                <th scope="col"></th>
              </tr>
            </thead>
            <tbody v-for="(user, ui) in users" :key="user.uid">
              <tr v-if="ui < userPage">
                <th scope="row">{{ui+1}}</th>
                <td>{{user.nickname}}</td>
                <td>{{user.email}}</td>
                <td>{{user.mileage}}</td>
                <td>{{((user.score1 + user.score2 + user.score3) / 3).toFixed(1)}}</td>
                <td>{{user.penalty}}</td>
                <td><i class="fas fa-user-slash" @click="userDelete(user)"></i></td>
              </tr>
            </tbody>
            <b-button class="d-flex plusBnt" @click="plusUserPage" variant="link">
              <b-spinner small></b-spinner>
              <span>더보기</span>
            </b-button>
          </table>
        </b-tab>
        <b-tab @click="allStudy" title="스터디">
          <div class="input-group m-1" @keypress.enter="searchStudy(findStudy)">
            <input class="form-control" v-model="findStudy" type="text" placeholder="타이들을 입력해주세요">
            <button class="input-group-text" @click="searchStudy(findStudy)">
              <i class="fas fa-search"></i>
            </button>
          </div>
          <table class="table">
            <thead class="thead-dark">
              <tr>
                <th scope="col">#</th>
                <th scope="col">Title</th>
                <th scope="col">Content</th>
                <th scope="col">Category</th>
                <th scope="col">Posttime</th>
                <th scope="col"></th>
              </tr>
            </thead>
            <tbody v-for="(study, si) in studies" :key="study.pid">
              <tr v-if="si < studyPage">
                <th scope="row">{{si+1}}</th>
                <td>{{study.title}}</td>
                <td>{{study.data}}</td>
                <td>{{study.category}}</td>
                <td>{{study.posttime}}</td>
                <td><i class="fas fa-trash-alt" @click="studyDelete(study)"></i></td>
              </tr>
            </tbody>
          </table>
          <b-button class="d-flex plusBnt" @click="plusStudyPage" variant="link">
            <b-spinner small></b-spinner>
            <span>더보기</span>
          </b-button>
        </b-tab>
        <b-tab @click="allReport" title="신고">
          <table class="table">
            <thead class="thead-dark">
              <tr>
                <th scope="col">#</th>
                <th scope="col">Nickname</th>
                <th scope="col">Email</th>
                <th scope="col">mileage</th>
                <th scope="col">score</th>
                <th scope="col">penalty</th>
              </tr>
            </thead>

            <tbody v-for="(reportUser, ri) in reports" :key="reportUser.uid">
              <tr @click="$bvModal.show(`bv-modal-example_${ri}`)">
                <th scope="row">{{ri+1}}</th>
                <td>{{reportUser[1].nickname}}</td>
                <td>{{reportUser[1].email}}</td>
                <td>{{reportUser[1].mileage}}</td>
                <td>{{((reportUser[1].score1 + reportUser[1].score2 + reportUser[1].score3) / 3).toFixed(1)}}</td>
                <td>{{reportUser[1].penalty}}</td>
              </tr>
              <b-modal :id="`bv-modal-example_${ri}`" hide-footer>
                <template v-slot:modal-title>
                  신고내용
                </template>
                <div class="d-block text-center">
                  <h5 class="pb-3">스터디명 : {{reportUser[2].title}}</h5>
                  <b-list-group class="m-2 text-left" v-for="content in reportUser[0]" :key="content.no">
                    <b-list-group-item>{{content.reason}}</b-list-group-item>
                  </b-list-group>
                  <div>
                    <i class="fas fa-user-slash mr-2" @click="givePenalty(reportUser[0][0])"><small> 페널티 부여</small></i>
                    <i class="fas fa-undo" @click="noPenalty(reportUser[0][0])"><small> 반려</small></i>
                  </div>
                </div>
              </b-modal>
            </tbody>
          </table>
        </b-tab>
        <b-tab title="차단유저">
          <table class="table">
            <thead class="thead-dark">
              <tr>
                <th scope="col">#</th>
                <th scope="col">Nickname</th>
                <th scope="col">Email</th>
                <th scope="col">mileage</th>
                <th scope="col">score</th>
                <th scope="col">penalty</th>
                <th scope="col"></th>
              </tr>
            </thead>
            <tbody v-for="(banUser, bi) in banUsers" :key="banUser.uid">
              <tr>
                <th scope="row">{{bi+1}}</th>
                <td>{{banUser.nickname}}</td>
                <td>{{banUser.email}}</td>
                <td>{{banUser.mileage}}</td>
                <td>{{((banUser.score1 + banUser.score2 + banUser.score3) / 3).toFixed(1)}}</td>
                <td>{{banUser.penalty}}</td>
                <td><i class="fas fa-user-slash"></i></td>
              </tr>
            </tbody>
          </table>
        </b-tab>
      </b-tabs>
    </div>
  </div>
</template>

<script>
import axios from "axios";

const SERVER_URL = "http://localhost:8080";

export default {
  name: "AdminPage",
  data: () => {
    return {
      users: {},
      studies: {},
      reports: {},
      banUsers: {},
      findUser: "",
      findStudy: "",
      userPage: 5,
      studyPage: 5,
    }
  },
  created () {
    this.allUser()
  },
  methods: {
    allUser() {
      axios.post(SERVER_URL + "/admin/allUser")
      .then(res => {
        const allUsers = res.data.object
        this.users = res.data.object.filter(allUsers => allUsers.penalty < 3)
        this.banUsers = res.data.object.filter(allUsers => allUsers.penalty >= 3)
      })
    },
    userDelete(user) {
      const deleteData = {
        email: user.email,
        password: user.password,
      }
      axios
      .post(SERVER_URL + "/account/delete", deleteData)
      .then(() => {
        alert("회원을 삭제했습니다.")
        this.allUser()
      })
      .catch((err) => {
        console.log(err);
      });
    },
    allStudy() {
      axios.post(SERVER_URL + "/admin/allStudy")
      .then(res => {
        this.studies = res.data.object
      })
    },
    studyDelete(study) {
      axios
      .post(SERVER_URL + "/study/delete", study)
      .then(() => {
        alert("스터디를 삭제했습니다.");
        this.allStudy()
      })
      .catch((err) => {
        console.log(err);
      });
    },
    allReport() {
      axios.post(SERVER_URL + "/admin/report")
      .then(res => {
        this.reports = res.data.object
      })
    },
    givePenalty(user) {
      axios.post(SERVER_URL + "/admin/givepenalty", user)
      .then(() => {
        this.allReport()
      })
    },
    noPenalty(user) {
      axios.post(SERVER_URL + "/admin/nopenalty", user)
      .then(() => {
        this.allReport()
      })
    },
    searchUser(tmp) {
      this.allUser()
      setTimeout(() => {
        const newUsers = this.users.filter(user => user.nickname.includes(tmp))
        this.users = newUsers
      }, 100);
    },
    searchStudy(tmp) {
      this.allStudy()
      setTimeout(() => {
        const newStudies = this.studies.filter(study => study.title.includes(tmp))
        this.studies = newStudies
      }, 100);
    },
    plusUserPage() {
      this.userPage += 5
    },
    plusStudyPage() {
      this.studyPage += 5
    },
  },
}
</script>

<style scope>
tbody :hover{
  background-color: #eee;
}
i {
  cursor: pointer;
}
.plusBnt {
  position: relative;
  left: 35vw;
}
</style>