<template>
  <Header :user="user" ref="header"/>
  <!--    主体-->
  <div style="display: flex">
    <!--      侧边栏-->
    <Aside :user="user" :loaded="load.promise"/>
    <!--      内容区域-->
    <el-main style="padding-bottom: 0px">
      <router-view :user="user" :loaded="load.promise" v-on:userInfoChange="handleInfoChange" style="flex:1"/>
    </el-main>
  </div>
</template>

<script>

import Header from "../components/Header";
import Aside from "../components/Aside";
import router from "@/router";
import request from "@/utils/request";
import {ElMessage} from "element-plus";
export default {
  name: "Layout",
  components:{
    Header,
    Aside
  },
  data(){
    let resolve, reject;
    // 创建一个新的 Promise 对象，并将 resolve 和 reject 方法保存到外部变量
    const promise = new Promise((res, rej) => {
      resolve = res;
      reject = rej;
    });
    return{
      user: {role:0},
      load: {
        promise,
        resolve,
        reject
      }
    }
  },
  methods:{
    handleInfoChange(){
      console.log("handleInfoChange")
      this.updateUserInfo()
    },
    updateUserInfo(){
      let userJson = sessionStorage.getItem("user")
      this.user = JSON.parse(userJson)
      console.log("new user:"+this.user)
    }
  },
  created() {
    let userJson = sessionStorage.getItem("user")
    if(!userJson) {
      this.load.resolve()
      return
    }
    let user = JSON.parse(userJson)
    request.post("user/login", user).then(res => {
      if (res.code == '0') {
        console.log(res.data)
        this.user=res.data
        this.load.resolve()
      } else{
        ElMessage.error(res.msg)
        this.$router.push("/login")
      }
    })
  }
}
</script>

<style>

</style>