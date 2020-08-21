<template>
  <div style="margin-top:6rem;" class="container">
    <div class="container">
      <h4 style="font-family:'Do Hyeon',sans-serif;">
        RANK
        <i class="fas fa-medal"></i>
      </h4>
      <div class="d-flex mx-4 my-5 justify-content-center">
        <div
          class="card mx-2 my-auto"
          style="position:relative;width:14rem;box-shadow:0px 0px 5px #C0C0C0;"
        >
          <div class="cardprofile">
            <div style="margin:0.4rem;" v-if="!RankUser2.profile_image">
              <img
                class="profileimg rounded-circle border"
                src="../../assets/img/defualt_image.png"
              />
              <i
                style="color:#C0C0C0;position:absolute;top:2%;left:2%;font-size:1.5rem;text-shadow: 2px 2px 4px gray;"
                class="fas fa-trophy"
              ></i>
            </div>
            <div style="margin:0.4rem;" v-else>
              <img class="profileimg rounded-circle border" :src="RankUser2.profile_image" />
              <i
                style="color:#C0C0C0;position:absolute;top:2%;left:2%;font-size:1.5rem;text-shadow: 2px 2px 4px gray;"
                class="fas fa-trophy"
              ></i>
            </div>
            <p style="font-family:'Do Hyeon',sans-serif;">{{RankUser2.nickname}}</p>
            <p
              style="font-size:1.2rem;color:orange;font-family:'Do Hyeon',sans-serif;"
            >{{RankData2.total}}점</p>
          </div>
          <div class="mileage w-100">
            <div
              class="row border-top w-100"
              style="background-color:rgba(255,165,0,0.2);margin:0;"
            >
              <div class="col p-1 border-right">
                <small style="font-family:'Do Hyeon',sans-serif;">일지</small>
                <br />
                <small style="font-family:'Do Hyeon',sans-serif;">{{RankData2.diarypoint}}</small>
              </div>
              <div class="col p-1 border-right">
                <small style="font-family:'Do Hyeon',sans-serif;">평가</small>
                <br />
                <small style="font-family:'Do Hyeon',sans-serif;">{{RankData2.evalpoint}}</small>
              </div>
              <div class="col p-1">
                <small style="font-family:'Do Hyeon',sans-serif;">스터디</small>
                <br />
                <small style="font-family:'Do Hyeon',sans-serif;">{{RankData2.endpoint}}</small>
              </div>
            </div>
          </div>
        </div>

        <div
          class="card border-0 mx-2 my-auto"
          style="position:relative;width:14rem;box-shadow:0px 0px 5px gold;"
        >
          <div class="cardprofile">
            <div style="margin:0.4rem;" v-if="!RankUser1.profile_image">
              <img
                class="profileimg rounded-circle border"
                src="../../assets/img/defualt_image.png"
              />
              <i
                style="color:gold;position:absolute;top:2%;left:2%;font-size:1.5rem;text-shadow: 2px 2px 4px gray;"
                class="fas fa-trophy"
              ></i>
            </div>
            <div style="margin:0.4rem;" v-else>
              <img class="profileimg rounded-circle border" :src="RankUser1.profile_image" />
              <i
                style="color:gold;position:absolute;top:2%;left:2%;font-size:1.5rem;text-shadow: 2px 2px 4px gray;"
                class="fas fa-trophy"
              ></i>
            </div>
            <p style="font-family:'Do Hyeon',sans-serif;">{{RankUser1.nickname}}</p>
            <p
              style="font-size:1.2rem;color:orange;font-family:'Do Hyeon',sans-serif;"
            >{{RankData1.total}}점</p>
          </div>
          <div class="mileage w-100">
            <div
              class="row border-top w-100"
              style="background-color:rgba(255,165,0,0.2);margin:0;"
            >
              <div class="col p-1 border-right">
                <small style="font-family:'Do Hyeon',sans-serif;">일지</small>
                <br />
                <small style="font-family:'Do Hyeon',sans-serif;">{{RankData1.diarypoint}}</small>
              </div>
              <div class="col p-1 border-right">
                <small style="font-family:'Do Hyeon',sans-serif;">평가</small>
                <br />
                <small style="font-family:'Do Hyeon',sans-serif;">{{RankData1.evalpoint}}</small>
              </div>
              <div class="col p-1">
                <small style="font-family:'Do Hyeon',sans-serif;">스터디</small>
                <br />
                <small style="font-family:'Do Hyeon',sans-serif;">{{RankData1.endpoint}}</small>
              </div>
            </div>
          </div>
        </div>

        <div
          class="card border-0 mx-2 my-auto"
          style="position:relative;width:14rem;box-shadow:0px 0px 5px #cd7f32;"
        >
          <div class="cardprofile">
            <div style="margin:0.4rem;" v-if="!RankUser3.profile_image">
              <img
                class="profileimg rounded-circle border"
                src="../../assets/img/defualt_image.png"
              />
              <i
                style="color: #cd7f32;position:absolute;top:2%;left:2%;font-size:1.5rem;text-shadow: 2px 2px 4px gray;"
                class="fas fa-trophy"
              ></i>
            </div>
            <div style="margin:0.4rem;" v-else>
              <img class="profileimg rounded-circle border" :src="RankUser3.profile_image" />
              <i
                style="color: #cd7f32;position:absolute;top:2%;left:2%;font-size:1.5rem;text-shadow: 2px 2px 4px gray;"
                class="fas fa-trophy"
              ></i>
            </div>
            <p style="font-family:'Do Hyeon',sans-serif;">{{RankUser3.nickname}}</p>
            <p
              style="font-size:1.2rem;color:orange;font-family:'Do Hyeon',sans-serif;"
            >{{RankData3.total}}점</p>
          </div>
          <div class="mileage w-100">
            <div
              class="row border-top w-100"
              style="background-color:rgba(255,165,0,0.2);margin:0;"
            >
              <div class="col p-1 border-right">
                <small style="font-family:'Do Hyeon',sans-serif;">일지</small>
                <br />
                <small style="font-family:'Do Hyeon',sans-serif;">{{RankData3.diarypoint}}</small>
              </div>
              <div class="col p-1 border-right">
                <small style="font-family:'Do Hyeon',sans-serif;">평가</small>
                <br />
                <small style="font-family:'Do Hyeon',sans-serif;">{{RankData3.evalpoint}}</small>
              </div>
              <div class="col p-1">
                <small style="font-family:'Do Hyeon',sans-serif;">스터디</small>
                <br />
                <small style="font-family:'Do Hyeon',sans-serif;">{{RankData3.endpoint}}</small>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="card border-0">
        <table class="table border-0 align-items-center table-flush">
          <thead>
            <tr>
              <th style="font-family:'Do Hyeon',sans-serif;" scope="col">순위</th>
              <th style="font-family:'Do Hyeon',sans-serif;" scope="col">유저</th>
              <th style="font-family:'Do Hyeon',sans-serif;" class="mileage" scope="col">일지</th>
              <th style="font-family:'Do Hyeon',sans-serif;" class="mileage" scope="col">평가</th>
              <th style="font-family:'Do Hyeon',sans-serif;" class="mileage" scope="col">스터디 완료</th>
              <th style="font-family:'Do Hyeon',sans-serif;" scope="col">총합</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(list, i) in RankData" :key="list.id">
              <td class="ranking">{{i+4}}</td>
              <td class="ranking">{{list.user.nickname}}</td>
              <td class="ranking mileage">{{list.diarypoint}}</td>
              <td class="ranking mileage">{{list.evalpoint}}</td>
              <td class="ranking mileage">{{list.endpoint}}</td>
              <td class="ranking" style="font-weight:bold;">{{list.total}}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import constants from "../../lib/constants";

const SERVER_URL = constants.ServerUrl;

export default {
  name: "Ranking",
  data: () => {
    return {
      RankData1: [],
      RankUser1: [],
      RankData2: [],
      RankUser2: [],
      RankData3: [],
      RankUser3: [],
      RankData: [],
    };
  },
  components: {
    constants,
  },

  created() {
    this.addRankData();
  },
  methods: {
    addRankData() {
      axios
        .get(SERVER_URL + "/mileage/list")
        .then((res) => {
          this.RankData1 = res.data.object
          for (var i = 2; i < res.data.object.length; i++) {
            if (i < 3) {
              this.RankData1 = res.data.object[0]
              this.RankUser1 = res.data.object[0].user
              this.RankData2 = res.data.object[1]
              this.RankUser2 = res.data.object[1].user
              this.RankData3 = res.data.object[2]
              this.RankUser3 = res.data.object[2].user
            } else {
              this.RankData.push(res.data.object[i]);
            }
          }
        })
        .catch((err) => {
          console.log(err);
        });
    },
  },
};
</script>
<style scoped>
.ranking {
  font-size: 0.8rem;
  font-family: "Noto Sans KR", sans-serif;
}
.cardprofile {
  margin: 1.3rem 0;
}
.profileimg {
  width: 6rem;
  height: 6rem;
}
@media (max-width: 480px) {
  .profileimg {
    width: 3rem;
    height: 3rem;
  }
  .mileage {
    display: none;
  }
  .cardprofile {
    margin: 0.8rem 0;
  }
}
.boardtable:hover {
  background-color: #ddd;
}

.boardtablenotice {
  background-color: #fff3e0;
}

.boardtablenotice:hover {
  background-color: #ffe0b2;
}

.blinking {
  -webkit-animation: blink 0.5s ease-in-out infinite alternate;
  -moz-animation: blink 0.5s ease-in-out infinite alternate;
  animation: blink 0.5s ease-in-out infinite alternate;
}

@-webkit-keyframes blink {
  0% {
    opacity: 0;
  }

  100% {
    opacity: 1;
  }
}

@-moz-keyframes blink {
  0% {
    opacity: 0;
  }

  100% {
    opacity: 1;
  }
}

@keyframes blink {
  0% {
    opacity: 0;
  }

  100% {
    opacity: 1;
  }
}
</style>