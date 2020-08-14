<style>
  input[type="file"] {
    position: absolute;
    top: -500px;
  }

  div.file-listing {
    width: 200px;
  }

  span.remove-file {
    color: red;
    cursor: pointer;
    float: right;
  }
</style>

<template>
  <div class="container">
    <div class="mt-5">
      <div class="large-12 medium-12 small-12 cell">
        <label>Files
          <input type="file" id="files" ref="files" multiple v-on:change="handleFilesUpload()" />
        </label>
      </div>
      <div class="large-12 medium-12 small-12 cell">
        <div v-for="(file, key) in files" :key="file.id" class="file-listing">{{ file.name }} <span class="remove-file"
            v-on:click="removeFile( key )">Remove</span></div>
      </div>
      <br>
      <div class="large-12 medium-12 small-12 cell">
        <button v-on:click="addFiles()">Add Files</button>
      </div>
      <br>
      <div class="large-12 medium-12 small-12 cell">
        <button v-on:click="submitFiles()">Submit</button>
      </div>
    </div>
    <div class="row mb-5">
            <div
              class="col-12 col-sm-6 col-lg-4 col-xl-3 d-flex justify-content-center"
              v-for="(list) in searchList"
              :key="list.id"
            >
              <p>{{list.url}}</p>
              <img :src="require(`@/assets/uploadfile/${list.url}`)" />
              <hr>
              <!-- <img src="../../assets/uploadfile/codeit.png" alt="">
              <img src="@/assets/uploadfile/main.jpg" alt=""> -->
            
            </div>
          </div>

  </div>
</template>

<script>
  import axios from "axios";
  import constants from "../../lib/constants";



  const SERVER_URL = constants.ServerUrl;
  export default {
    /*
      Defines the data used by the component
    */
    data() {
      return {
        files: [],
        searchList:[],
        test: [
          {
            url: "codeit.png"
          }
        ]
      }
    },
    /*
      Defines the method used by the component
    */
    created(){
      this.urlLoad()
    },
    mounted(){
      this.urlLoad()
    },
    computed: {
      },
    methods: {
      getImg(url) {
        console.log("url", url)
        var tt = "../../assets/uploadfile/" + url
        var t = require(tt)
        return t
      },
      /*
        Adds a file
      */
      addFiles() {
        this.$refs.files.click();
        console.log(this.files)
      },
      /*
        Submits files to the server
      */  
      urlLoad() {
        axios.get(SERVER_URL + "/study/detail/urlload",
       
          ).then((res) => {

            this.searchList = res.data.object
            console.log('SUCCESS!!');
          })
          .catch(function () {
            console.log('FAILURE!!');
          });
      },
      submitFiles() {
        /*
          Initialize the form data
        */
        const formData = new FormData();
        var ff = new FormData();
        /*
          Iteate over any file sent over appending the files
          to the form data.
        */
        ff.append("tst","teststst")
       console.log("ff")
       console.log(ff)
       
        for (var i = 0; i < this.files.length; i++) {
          let file = this.files[i]
          formData.append('file', file)
        }
        /*
          Make the request to the POST /select-files URL
        */
        
        console.log(formData)
        for(var key of formData.values())
       {
         console.log(key)
       }
        axios.post(SERVER_URL + "/study/detail/fileupload",
            formData, {
              headers: {
                'Content-Type': 'multipart/form-data'
              }
            }
          ).then((res) => {
            console.log(res)
            this.urlLoad()
            console.log('SUCCESS!!');
          })
          .catch(function () {
            console.log('FAILURE!!');
          });
      },
      /*
        Handles the uploading of files
      */
      handleFilesUpload() {
        let uploadedFiles = this.$refs.files.files;
        /*
          Adds the uploaded file to the files array
        */
        for (var i = 0; i < uploadedFiles.length; i++) {
          this.files.push(uploadedFiles[i]);
        }
      },
      /*
        Removes a select file the user has uploaded
      */
      removeFile(key) {
        this.files.splice(key, 1);
      }
    }
  }
</script>