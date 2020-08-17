<template>
  <div style="margin-top:6rem;" class="container pt-0">
    <div class="card border-0 w-75 mx-auto">
      <div class="card-text">
        <img :src="images" class="logo w-25" alt />
        <b-row class>
          <b-col sm="12" md="8" class="px-1">
            <small class="formtitle ml-3 float-left">제목</small>
            <b-form-input v-model="postCreateDate.title" placeholder="제목" type="text"></b-form-input>
          </b-col>
          <b-col sm="12" md="4" class="px-1 pt-4" style="height: 57px;">
            <div>
              <input type="file" id="files" ref="files" multiple @change="handleFilesUpload()" />
              <div v-for="(file, key) in files" :key="file.id">
                {{ file.name }}
                <span class="remove-file" @click="removeFile( key )">Remove</span>
              </div>
            </div>
            <div v-if="!files[0]">
              <button @click="addFiles()">Add Files</button>
            </div>
          </b-col>
          <b-col sm="12" md="6" class="px-1">
            <small class="formtitle ml-3 float-left">일정 (주 _회)</small>
            <b-form-spinbutton v-model="postCreateDate.bindo" min="1" max="7"></b-form-spinbutton>
          </b-col>
          <b-col sm="12" md="6" class="px-1">
            <small class="formtitle1 ml-3 float-left">인원수 (3명 이상)</small>
            <b-form-spinbutton v-model="postCreateDate.limitp" min="3" max="100"></b-form-spinbutton>
          </b-col>
          <b-col sm="12" md="6" class="px-1">
            <small class="formtitle ml-3 float-left">시/도</small>
            <select @click="getgungu" v-model="postCreateDate.sido_code" class="custom-select">
              <option
                v-for="sido in allSidoCode"
                :key="sido.sidocode"
                :value="sido.sidocode"
              >{{ sido.sidoname }}</option>
            </select>
          </b-col>
          <b-col sm="12" md="6" class="px-1">
            <small class="formtitle ml-3 float-left">시/군/구</small>
            <select v-model="postCreateDate.sigungu_code" class="custom-select">
              <option
                v-for="sido in allGugunCode"
                :key="sido.guguncode"
                :value="sido.guguncode"
              >{{ sido.gugunname }}</option>
            </select>
          </b-col>
          <b-col sm="12" md="6" class="px-1">
            <small class="formtitle ml-3 float-left">시작일</small>
            <b-form-input v-model="postCreateDate.start_date" type="date"></b-form-input>
          </b-col>
          <b-col sm="12" md="6" class="px-1">
            <small class="formtitle ml-3 float-left">종료일</small>
            <b-form-input v-model="postCreateDate.end_date" type="date"></b-form-input>
          </b-col>
          <b-col sm="12" md="6" class="px-1">
            <small class="formtitle ml-3 float-left">카테고리</small>
            <b-form-select value="f02c" v-model="postCreateDate.category" :options="options"></b-form-select>
          </b-col>
          <b-col sm="12" md="6" class="px-1">
            <b-input-group size="sm" class="taggroup mb-2">
              <b-icon icon="tags-fill" variant="warning" class="mx-2 mt-2"></b-icon>
              <b-form-tags
                input-id="tags-basic"
                tag-variant="warning"
                v-model="postCreateDate.tag"
                placeholder="태그"
                class="rounded mb-2 p-2"
              ></b-form-tags>
            </b-input-group>
          </b-col>
          <b-col sm="12" class="px-1">
            <small class="formtitle ml-3 float-left">내용</small>
            <b-form-textarea
              v-model="postCreateDate.data"
              placeholder="내용"
              rows="3"
              style="resize:none;"
            ></b-form-textarea>
          </b-col>
        </b-row>

        <div class="d-flex inline justify-content-center mb-5">
          <div class="p-3">
            <b-button
              @click="postCreate((postCreateDate.tmp = 1))"
              style="border:1.5px solid orange;font-family:'Do Hyeon',sans-serif;"
              variant="outline-warning"
              class="createbtn"
            >스터디 생성</b-button>
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

export default {
  name: "PostCreate",
  data: () => {
    return {
      images: require("../../assets/img/logo.png"),
      profileInfo: [],
      files: [],
      options: [
        { value: null, text: "카테고리" },
        { value: "알고리즘", text: "알고리즘" },
        { value: "공모전", text: "공모전" },
        { value: "자격증", text: "자격증" },
        { value: "취업", text: "취업" },
        { value: "웹", text: "웹" },
        { value: "기타", text: "기타" },
      ],
      allSidoCode: [],
      allGugunCode: [],
      postCreateDate: {
        title: "",
        data: "",
        background_image: "",
        category: null,
        uid: "",
        tag: [],
        sido_code: "",
        sigungu_code: "",
        start_date: "",
        end_date: "",
        limitp: 3,
        bindo: 1,
        tmp: 0,
      },
    };
  },
  created() {
    this.user_check();
    this.getSidoCode();
  },
  methods: {
    user_check() {
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
          })
          .catch((err) => {
            this.$router.push({
              name: constants.URL_TYPE.ERROR.ERRORPAGE,
              params: { code: err.response.data },
            });
          });
      } else alert("로그인후 이용해주세요."), this.$router.push("/");
    },
    postTmp() {
      this.postCreateDate.uid = this.profileInfo.uid;
      axios
        .post(SERVER_URL + "/study/write", this.postCreateDate)
        .then((res) => {
          this.$router.push({ name: constants.URL_TYPE.POST.MAIN });
        })
        .catch((err) => {
          console.log(err);
          this.$swal("생성 실패", "입력정보를 확인해주세요", "error");
        });
    },
    postCreate() {
      this.postCreateDate.uid = this.profileInfo.uid;
      var date = new Date();
      if (this.postCreateDate.title == "") {
        this.$swal(
          "생성 실패",
          "스터디 제목이 입력되지 않았습니다. 확인해주세요!",
          "error"
        );
      } else if (this.postCreateDate.bindo == "") {
        this.$swal(
          "가입 실패",
          "일정이 입력되지 않았습니다. 확인해주세요!",
          "error"
        );
      } else if (parseInt(this.postCreateDate.bindo) >= 7) {
        this.$swal(
          "가입 실패",
          "스터디는 주 7회를 넘을 수 없습니다. ",
          "error"
        );
      } else if (this.postCreateDate.sido_code == "") {
        this.$swal(
          "가입 실패",
          "시/도가 입력되지 않았습니다.확인해주세요!",
          "error"
        );
      } else if (this.postCreateDate.sigungu_code == "") {
        this.$swal(
          "가입 실패",
          "시/군/구가 입력되지 않았습니다. 확인해주세요!",
          "error"
        );
      } else if (this.postCreateDate.start_date == "") {
        this.$swal(
          "가입 실패",
          "시작일이 입력되지 않았습니다. 확인해주세요!",
          "error"
        );
      } else if (
        !(
          parseInt(this.postCreateDate.start_date.substring(0, 4)) >=
            date.getFullYear() ||
          parseInt(this.postCreateDate.start_date.substring(5, 7)) >=
            date.getMonth() + 1 ||
          parseInt(this.postCreateDate.start_date.substring(8, 10)) >
            date.getDate()
        )
      ) {
        this.$swal(
          "가입 실패",
          "시작일은 오늘 이후로 설정해주세요. <br> 오늘은 " +
            (date.getMonth() + 1) +
            "월 " +
            date.getDate() +
            "일 입니다.",
          "error"
        );
      } else if (this.postCreateDate.end_date == "") {
        this.$swal(
          "가입 실패",
          "종료일이 입력되지 않았습니다. 확인해주세요!",
          "error"
        );
      } else if (
        !(
          parseInt(this.postCreateDate.end_date.substring(0, 4)) >=
            this.postCreateDate.start_date.substring(0, 4) ||
          parseInt(this.postCreateDate.end_date.substring(5, 7)) >=
            this.postCreateDate.start_date.substring(5, 7) ||
          parseInt(this.postCreateDate.end_date.substring(8, 10)) >
            this.postCreateDate.start_date.substring(8, 10)
        )
      ) {
        this.$swal(
          "가입 실패",
          "종료일은 시작일보다 뒤에 있어야합니다 <br> 스터디 시작일은 " +
            this.postCreateDate.start_date.substring(5, 7) +
            "월 " +
            this.postCreateDate.start_date.substring(8, 10) +
            "일 입니다.",
          "error"
        );
      } else if (this.postCreateDate.category == null) {
        this.$swal(
          "가입 실패",
          "카테고리가 입력되지 않았습니다. 확인해주세요!",
          "error"
        );
      } else if (this.postCreateDate.tag.length == 0) {
        this.$swal(
          "가입 실패",
          "태그가 입력되지 않았습니다. 확인해주세요!",
          "error"
        );
      } else if (this.postCreateDate.data == "") {
        this.$swal(
          "가입 실패",
          "내용이 입력되지 않았습니다. 확인해주세요!",
          "error"
        );
      } else {
        if (this.files[0]) {
          this.postCreateDate.background_image = this.files[0].name;
        }
        else {
          this.postCreateDate.background_image = null;
        }
        axios
          .post(SERVER_URL + "/study/write", this.postCreateDate)
          .then((res) => {
            swal("스터디가 생성되었습니다.", { buttons: false, timer: 1200 });
            if (this.files[0]) {
              this.submitFiles(res.data.object.pid);
            }
            this.$router.push({
              name: constants.URL_TYPE.POST.POSTDETAIL,
              params: { post_id: res.data.object.pid },
            });
          })
          .catch((err) => {
            console.log(err.response);
            this.$swal("스터디 생성 실패", "입력정보를 확인해주세요", "error");
          });
      }
    },
    // 이미지 업로드
    handleFilesUpload() {
      let uploadedFiles = this.$refs.files.files;
      for (var i = 0; i < uploadedFiles.length; i++) {
        this.files.push(uploadedFiles[i]);
      }
    },
    addFiles() {
      this.$refs.files.click();
    },
    removeFile(key) {
      this.files.splice(key, 1);
    },
    submitFiles(pid) {
      const formData = new FormData();
      let file = this.files[0];
      formData.append("file", file);
      formData.append("pid", pid);
      axios
        .post(SERVER_URL + "/study/detail/fileupload", formData, {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        })
        .then(() => {})
        .catch(function () {
          console.log("FAILURE!!");
        });
    },
    getSidoCode() {
      axios
        .post(SERVER_URL + "/study/getsidocode")
        .then((res) => {
          this.allSidoCode = res.data.object;
        })
        .catch((err) => {
          console.log(err);
        });
    },
    getgungu() {
      const code = this.postCreateDate.sido_code;
      axios
        .get(SERVER_URL + "/study/getguguncode", { params: { sidocode: code } })
        .then((res) => {
          this.allGugunCode = res.data.object;
        })
        .catch((err) => {
          console.log(err);
        });
    },
  },
};
</script>

<style scoped>
.createbtn {
  color: orange;
}
.createbtn:hover {
  color: white;
  background-color: orange;
}
label {
  position: relative;
  right: 40%;
  top: 5px;
  font-size: small;
}
.submit-btn {
  padding: 7px 35px;
  border-radius: 60px;
  display: inline-block;
  background-color: #4b8cfb;
  color: white;
  font-size: 18px;
  cursor: pointer;
}
.formtitle {
  padding: 0 4px;
  transform: translateY(5px);
  background-color: white;
  font-weight: bolder;
}
.formtitle1 {
  padding: 0 4px;
  transform: translateX(30px) translateY(5px);
  background-color: white;
  font-weight: bolder;
}
.logo {
  width: 7vw;
}
.taggroup {
  margin-top: 20px;
}
</style>
