<template>
  <div class="container">
    <h1>ADMIN PAGE</h1>
    <hr>
    <div>
      <b-tabs content-class="mt-3">
        <b-tab title="유저">
          <table class="table">
            <thead class="thead-dark">
              <tr>
                <th scope="col">#</th>
                <th scope="col">Nickname</th>
                <th scope="col">Email</th>
                <th scope="col">mileage</th>
                <th scope="col">penalty</th>
                <th scope="col"></th>
              </tr>
            </thead>
            <tbody v-for="(user, ui) in users" :key="user.uid">
              <tr>
                <th scope="row">{{ui+1}}</th>
                <td>{{user.nickname}}</td>
                <td>{{user.email}}</td>
                <td>{{user.mileage}}</td>
                <td>{{user.penalty}}</td>
                <td><i class="fas fa-user-slash" @click="userDelete(user)"></i></td>
              </tr>
            </tbody>
          </table>
        </b-tab>
        <b-tab @click="allStudy" title="스터디">
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
            <tbody v-for="(study, si) in stuies" :key="study.pid">
              <tr>
                <th scope="row">{{si+1}}</th>
                <td>{{study.title}}</td>
                <td>{{study.data}}</td>
                <td>{{study.category}}</td>
                <td>{{study.posttime}}</td>
                <td><i class="fas fa-user-slash" @click="studyDelete(study)"></i></td>
              </tr>
            </tbody>
          </table>
        </b-tab>
        <b-tab @click="allReport" title="신고">
          <table class="table">
            <thead class="thead-dark">
              <tr>
                <th scope="col">#</th>
                <th scope="col">Nickname</th>
                <th scope="col">Email</th>
                <th scope="col">mileage</th>
                <th scope="col">penalty</th>
                <th scope="col"></th>
              </tr>
            </thead>
            <tbody v-for="(report, ri) in reports" :key="report.uid">
              <tr>
                <th scope="row">{{ri+1}}</th>
                <td>{{report.nickname}}</td>
                <td>{{report.email}}</td>
                <td>{{report.mileage}}</td>
                <td>{{report.penalty}}</td>
                <td><i class="fas fa-user-slash"></i></td>
              </tr>
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
      stuies: {},
      reports: {},
      banUsers: {},
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
        this.stuies = res.data.object
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
        console.log(this.reports);
      })
    },
    givePenalty() {
      axios.post(SERVER_URL + "/admin/givepenalty")
      .then(res => {
      })
    },
  },
}
</script>

<style>

</style>