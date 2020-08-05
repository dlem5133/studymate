<template>
  <div>
    <h1>kakao</h1>
    <button @click="test">kakaka</button>
    <a href="https://kauth.kakao.com/oauth/authorize?client_id=a61b27fc4e535f7a22983d0d0da6eb9d&redirect_uri=http://localhost:8080/account/kakaologin&response_type=code">123</a>
    <!-- https://kauth.kakao.com/oauth/authorize?client_id=a61b27fc4e535f7a22983d0d0da6eb9d&redirect_uri=http://localhost:8080/account/loginn&response_type=code -->

    <p>{{$route.query}}</p>
  </div>
</template>

<script>
export default {
  methods: {
    test() {
      console.log(123);
    }
  }
}
</script>

<style>

</style>