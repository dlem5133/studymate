<template>
  <div style="margin-top:6rem;" class="container">
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
        </div>

        <div class="stats">
          <div class="box" @click="page=0">
            <span class="value">{{studyList.length}}</span>
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

      <div v-if="page===0">
        <div class="side">
          <p class="milelog px-auto p-0 m-5">리스트는 확인 하실수 없습니다.</p>
        </div>
      </div>

      <div v-if="page===1">
        <div class="side">
          <div class="d-flex justify-content-between">
            <p class="milelog">마일리지 LOG</p>
            <button @click="goRank" class="rankbnt">랭킹 보러가기</button>
          </div>
          <div class="row d-flex justify-content-center">
            <div class="card col-4 col-lg-3 my-1 m-lg-1 p-0 py-5">
              <b-icon class="mileicon" icon="calendar4-week"></b-icon>
              <small>일지</small>
              <div>
                <span class="milepoint">{{ mileageData.diarypoint }}</span>
                <small>점</small>
              </div>
            </div>
            <div class="card col-4 col-lg-3 my-1 m-lg-1 p-0 py-5">
              <b-icon class="mileicon" icon="award"></b-icon>
              <small>평가</small>
              <div>
                <span class="milepoint">{{ mileageData.evalpoint }}</span>
                <small>점</small>
              </div>
            </div>
            <div class="card col-4 col-lg-3 my-1 m-lg-1 p-0 py-5">
              <b-icon class="mileicon" icon="book-half"></b-icon>
              <small>스터디 완료</small>
              <div>
                <span class="milepoint">{{ mileageData.endpoint * 200 }}</span>
                <small>점</small>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div v-if="page===2">
        <div class="side">
          <p class="milelog">상호평가 LOG</p>
          <div id="ratingbox" class="text-left row mx-auto d-flex justify-content-center">
            <div class="card col-4 col-lg-3 my-1 m-lg-1 p-0 py-5">
              <div class="text-center border-info">
                <b-form-rating
                  v-model="profileInfo.score1"
                  readonly
                  no-border
                  inline
                  color="#feb74d"
                  size="sm"
                ></b-form-rating>
                <p>{{profileInfo.score1}} / 5</p>
              </div>
              <div class="text-center">
                <small class="text-dark font-weight-bold">성실도</small>
              </div>
            </div>
            <div class="card col-4 col-lg-3 my-1 m-lg-1 p-0 py-5">
              <div class="text-center">
                <b-form-rating
                  v-model="profileInfo.score2"
                  readonly
                  no-border
                  inline
                  color="#feb74d"
                  size="sm"
                ></b-form-rating>
                <p>{{profileInfo.score2}} / 5</p>
              </div>
              <div class="text-center">
                <small class="text-dark font-weight-bold">참여도</small>
              </div>
            </div>
            <div class="card col-4 col-lg-3 my-1 m-lg-1 p-0 py-5">
              <div class="text-center">
                <b-form-rating
                  v-model="profileInfo.score3"
                  readonly
                  no-border
                  inline
                  color="#feb74d"
                  size="sm"
                ></b-form-rating>
                <p>{{profileInfo.score2}} / 5</p>
              </div>
              <div class="text-center">
                <small class="text-dark font-weight-bold">인싸력</small>
              </div>
            </div>
          </div>
          <div>
            <hr />
            <small class="text-secondary font-weight-bold text-left pl-3 pb-2">한줄평</small>

            <div v-for="(list1, n) in evalistdata" :key="list1.id" class="card p-1 px-2 m-1 mx-5">
              <div v-if="n < showEvalist" class="d-flex inline">
                <small class="text-left text-truncate">{{ list1.sentence }}</small>
                <b-badge class="ml-auto my-auto" variant="success">{{list1.study.category}}</b-badge>
              </div>
            </div>
            <button @click="showEvalist += 3" variant="link" class="small">...더보기</button>
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
  name: "memberProfile",
  data: () => {
    return {
      profileInfo: {},
      studyList: {},
      page: 1,
      showEvalist: 3,

      evalistdata: {},
      mileageData: {},
    };
  },
  computed: {
    total_score() {
      return (
        (this.profileInfo.score1 +
          this.profileInfo.score2 +
          this.profileInfo.score3) /
        3
      ).toFixed(1);
    },
    total_mileage() {
      if ("+this.mileageData.total".length > 3) {
        return this.mileageData.total / 1000 + "K";
      } else {
        return this.mileageData.total;
      }
    },
  },
  methods: {
    evaList() {
      const targetid = this.profileInfo.uid;
      axios
        .post(SERVER_URL + "/eva/userlist", { target_uid: targetid })
        .then((res) => {
          this.evalistdata = res.data.object;
        })
        .catch((err) => console.log(err));
    },
    mileageList() {
      axios
        .get(SERVER_URL + "/mileage/user", {
          params: { uid: this.profileInfo.uid },
        })
        .then((res) => {
          this.mileageData = res.data.object;
        })
        .catch((err) => console.log(err));
    },
    addprofileInfo() {
      const userId = this.$route.params.user_id;
      axios
        .get(SERVER_URL + "/account/memprofile", { params: { uid: userId } })
        .then((res) => {
          this.profileInfo = res.data.object[0];
          this.studyList = res.data.object[1];
          this.evaList();
          this.mileageList();
        })
        .catch(() => {});
    },
    goRank() {
      this.$router.push({
        name: constants.URL_TYPE.RANK.RANKING,
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
.form-control {
  background-color: rgba(0, 0, 0, 0);
}
.b-rating .b-rating-star,
.b-rating .b-rating-value {
  padding: 0 !important;
}
.milelog {
  margin: 5px;
  padding-left: 5vw;
  font-size: 25px;
  font-weight: bolder;
  text-align: left;
}
.mileicon {
  font-size: 70px;
  margin-left: auto;
  margin-right: auto;
}
.milepoint {
  font-size: 40px;
  font-weight: bolder;
  color: orange;
}
.rankbnt {
  margin-right: 5vw;
  margin-top: auto;
  height: 25px;
  font-size: small;
}
</style>