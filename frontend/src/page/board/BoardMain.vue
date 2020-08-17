<template>
  <div style="margin-top:6rem;" class="mb-5 container">
    <div class="mt-5 my-3 d-flex">
      <!-- style="color:#FFB74D;" -->
      <div class="d-flex">
        <h5 class="font-weight-bold ml-3">{{ postData.title }}</h5>
        <b-icon
          style="cursor:pointer;"
          class="mt-2 ml-2"
          icon="house"
          @click="goStudyMain(postData.pid)"
        ></b-icon>
      </div>
      <b-button class="my-auto ml-auto" variant="border-0" @click="newBoard(postData.pid)">
        <small style="font-family:'Do Hyeon', sans-serif;">
          <b-icon class="mr-1" icon="pen"></b-icon>WRITE
        </small>
      </b-button>
    </div>
    <div class="container p-0">
      <div class="table">
        <table class="table border align-items-center table-flush">
          <thead>
            <tr>
              <th scope="col">번호</th>
              <th scope="col">제목</th>
              <th class="did" scope="col">날짜</th>
              <th scope="col">작성자</th>
            </tr>
          </thead>
          <tbody>
            <tr
              :class="isNotice(boardItem.isnotice)"
              style="cursor:pointer;"
              @click="goBoardDetail(boardItem.bid)"
              v-for="(boardItem, i) in pageOfItems"
              :key="boardItem.bid"
            >
              <th scope="row" v-if="boardItem.isnotice==1">
                <b-badge variant="warning" class="blinking">공지</b-badge>
              </th>
              <th scope="row" v-else>{{i+1}}</th>
              <td>{{boardItem.title}}</td>
              <td class="did">{{boardItem.date.substring(0, 10)}}</td>
              <td>{{boardItem.user.nickname}}</td>
            </tr>
          </tbody>
        </table>
      </div>
      <jw-pagination
        class="mx-auto"
        :styles="customStyles"
        :labels="customLabels"
        :items="boardItems"
        @changePage="onChangePage"
      ></jw-pagination>

      <div class="input-group flex-nowrap mx-auto my-3">
        <div class="d-flex mx-auto w-75 border" style="border-radius:30px;">
          <input
            v-model="searchText"
            type="text"
            class="border-0 form-control"
            style="background-color: transparent;"
            placeholder="Search"
          />
          <div class="input-group-prepend" @click="boardSearch">
            <b-button style="cursor: pointer;" variant="border-0">
              <b-icon icon="search"></b-icon>
            </b-button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import constants from "../../lib/constants";

const SERVER_URL = constants.ServerUrl;
const customStyles = {
  ul: {
    // border: '2px solid black'
  },
  li: {
    display: "inline-block",
    // border-radius: '50%'
  },
  a: {
    // 'background-color':'orange',
  },
};
const customLabels = {
  first: "<<",
  last: ">>",
  previous: "<",
  next: ">",
};
export default {
  name: "BoardMain",
  data: () => {
    return {
      profileInfo: [],
      studyLists: [],
      postData: [],
      boardItems: [],
      pageOfItems: [],
      searchText: "",

      customStyles,
      customLabels,
    };
  },
  mounted() {},
  created() {
    this.addprofileInfo();
    this.getBoardItems();
    this.getDetail();
  },
  methods: {
    isNotice(notice) {
      if (notice == 1) {
        return "boardtablenotice";
      } else {
        return "boardtable";
      }
    },
    addprofileInfo() {
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
            this.addStudyList();
          })
          .catch((err) => {
            this.$router.push({
              name: constants.URL_TYPE.ERROR.ERRORPAGE,
              params: { code: err.response.data },
            });
          });
      }
    },
    addStudyList() {
      axios
        .post(SERVER_URL + "/account/studylist", {
          email: this.profileInfo.email,
          nickname: this.profileInfo.nickname,
          password: "1234qwer",
        })
        .then((res) => {
          this.studyLists = res.data.object;
        })
        .catch((err) => {
          console.log(err);
        });
    },
    goStudyMain(post_id) {
      this.$router.push({
        name: constants.URL_TYPE.STUDY.STUDYMAIN,
        params: { post_id: post_id },
      });
    },
    getDetail() {
      const post_id = this.$route.params.post_id;
      axios
        .get(SERVER_URL + "/study/details", { params: { pid: post_id } })
        .then((res) => {
          this.postData = res.data.object[0];
        })
        .catch((err) => console.log(err.data));
    },
    getBoardItems() {
      const post_id = this.$route.params.post_id;
      axios
        .get(SERVER_URL + "/study/board/list", { params: { pid: post_id } })
        .then((res) => {
          this.boardItems = res.data.object;
          this.boardItems.sort((a, b) => {
            return a.date > b.date ? -1 : a.date < b.date ? 1 : 0;
          });
          this.boardItems.sort((a, b) => {
            return a.isnotice > b.isnotice
              ? -1
              : a.isnotice < b.isnotice
              ? 1
              : 0;
          });
        })
        .catch((err) => {
          console.log(err);
        });
    },
    newBoard(post_id) {
      this.$router.push({
        name: constants.URL_TYPE.BOARD.BOARDCREATE,
        params: { post_id: post_id },
      });
    },
    goBoardDetail(board_id) {
      this.$router.push({
        name: constants.URL_TYPE.BOARD.BOARDDETAIL,
        params: { post_id: this.$route.params.post_id, board_id: board_id },
      });
    },
    onChangePage(pageOfItems) {
      this.pageOfItems = pageOfItems;
    },
    boardSearch() {
      axios
        .get(SERVER_URL + "/study/board/search", {
          params: { text: this.searchText },
        })
        .then((res) => {
          this.boardItems = res.data.object;
        })
        .catch((err) => {
          console.log(err);
        });
    },
  },
};
</script>

<style scoped>
@media (max-width: 480px) {
  .did {
    display: none;
  }
}
input::placeholder {
  font-size: 0.8rem;
  font-family: "Do Hyeon", sans-serif;
}
input {
  font-size: 0.9rem;
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