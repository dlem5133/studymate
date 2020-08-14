<template>
  <div style="margin-top:6rem;" class="container">
    <h3>error</h3>
    <hr />
    <div v-if="code.status!=200">
      {{code.status}}
      {{code.error}}
      <br />
      <a href="/" class="text-info">홈페이지</a>로 이동하여 다시 시도해보세요.
    </div>
  </div>
</template>

<script>
export default {
  props: ["error"],
  data() {
    return {
      code: this.$route.params.code,
    };
  },
  created() {
    // this.tokenDelete()
  },
  methods: {
    // tokenDelete() {
    //   this.$cookies.remove("Auth-Token");
    // }
  },
};
</script>

<style scoped>
</style>