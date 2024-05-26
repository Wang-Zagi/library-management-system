<template>
  <div style="height: 50px; line-height:50px; border-bottom: 1px solid #ccc; display: flex">

   <div style="width: 600px; padding-left:30px; font-weight: bold; color:dodgerblue">
     <img :src="imgUrl" class="icon" >
     <span style="font-size:30px">Library Management System</span>
   </div>
   <div style="flex: 1"></div>
   <div style="width: 100px">
     <el-button type="primary" round @click="$router.push('/login')" v-if="user.role===0">Login >></el-button>
     <el-dropdown v-if="user.role!==0">
      <span class="el-dropdown-link">
        {{user.username}} <el-icon class="el-icon--right">
          <arrow-down />
          </el-icon>
      </span>
       <template #dropdown>
         <el-dropdown-menu>
           <el-dropdown-item @click="exit">Logout</el-dropdown-item>
         </el-dropdown-menu>
       </template>
     </el-dropdown>
   </div>
  </div>
</template>

<script>
import {ElMessage} from "element-plus";

export default {
  name: "Header",
  props: ['user'],
  data(){
    return{
      imgUrl:require("../assets/icon/login.png")
    }
  },
  methods:{
    exit(){
      sessionStorage.removeItem("user")
      this.$router.push("/login")
      ElMessage.success("Exit system successfully")
    }
  }
}
</script>

<style scoped>
.icon {
  width: 40px;
  height: 40px;
  padding-top: 5px;
  padding-right: 10px;
}
</style>
