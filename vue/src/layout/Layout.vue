<template>
  <Header ref="header"/>
  <!--    主体-->
  <div style="display: flex">
    <!--      侧边栏-->
    <Aside/>
    <!--      内容区域-->
    <router-view v-on:userInfoChange="handleInfoChange" style="flex:1"/>
  </div>
</template>

<script>

import Header from "../components/Header";
import Aside from "../components/Aside";
export default {
  name: "Layout",
  components:{
    Header,
    Aside
  },
  methods:{
    handleInfoChange(){
      console.log("handleInfoChange")
      this.$refs.header.loadUserInfo()
    }
  }
}
</script>

<style scoped>

</style>