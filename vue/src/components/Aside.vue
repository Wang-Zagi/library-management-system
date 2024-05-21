<template>
<div>

  <el-menu
      style="width: 250px; min-height: calc(100vh - 50px);"
      :default-active="$route.path"
      class="el-menu-vertical-demo"
      router
      background-color="#30333c" text-color="#fff"
  >
    <el-menu-item index="/book" v-if="user.role == 1" >
      <svg class="icon" aria-hidden="true">
        <use xlink:href="#iconbook "></use>
      </svg>
      <span>Book Management</span>
    </el-menu-item>
    <el-menu-item index="/book" v-if="user.role != 1">
      <svg class="icon" aria-hidden="true">
        <use xlink:href="#iconbook "></use>
      </svg>
      <span>Book Query</span>
    </el-menu-item>
    <el-menu-item index="/borrow" v-if="user.role == 2 && user.debt == 0 && overdueNum == 0">
      <svg class="icon" aria-hidden="true">
        <use xlink:href="#iconbook "></use>
      </svg>
      <span>Borrow</span>
    </el-menu-item>
    <el-menu-item index="/return" v-if="user.role == 2">
      <svg class="icon" aria-hidden="true">
        <use xlink:href="#iconbook "></use>
      </svg>
      <span>Return</span>
    </el-menu-item>
    <el-menu-item index="/borrowingBook" v-if="user.role == 2">
      <el-icon><grid /></el-icon>
      <span>Borrowing Books</span>
    </el-menu-item>
    <el-menu-item index="/borrowRecord" v-if="user.role !=0">
      <svg class="icon" aria-hidden="true">
        <use xlink:href="#iconlend-record "></use>
      </svg>
      <span>Borrow Records</span>
    </el-menu-item>
    <el-menu-item index="/user" v-if="user.role == 1">
      <svg class="icon" aria-hidden="true">
        <use xlink:href="#iconreader "></use>
      </svg>
      <span>Reader Management</span>
    </el-menu-item>
    <el-sub-menu index="2" text-color="#fff" v-if="user.role!=0">
      <template #title>
        <svg class="icon" aria-hidden="true">
          <use xlink:href="#icon-mingpian "></use>
        </svg>
        <span>Personal Information</span>
      </template>
      <el-menu-item index="/userInfo" style="font-color: white">
        <svg class="icon" aria-hidden="true">
          <use xlink:href="#icon-a-bianji1 "></use>
        </svg>
        <span>Edit Information</span>
      </el-menu-item>
      <el-menu-item index="/password">
        <svg class="icon" aria-hidden="true">
          <use xlink:href="#icon-mima "></use>
        </svg>
        <span>Change Password</span>
      </el-menu-item>
    </el-sub-menu>
<!--    <el-menu-item index="/dashboard" v-if="user.role == 1">-->
<!--      <svg class="icon" aria-hidden="true">-->
<!--        <use xlink:href="#icondashboard "></use>-->
<!--      </svg>-->
<!--      <span>Data Statistics</span>-->
<!--    </el-menu-item>-->
  </el-menu>

</div>
</template>

<script>



import request from "@/utils/request";
import {ElMessage, ElMessageBox} from "element-plus";

export default {
  name: "Aside",
  components:{},
  created(){
    let userStr = sessionStorage.getItem("user")
    if(userStr) {
      this.user = JSON.parse(userStr)
      request.post("user/login", this.user).then(res => {
        if (res.code == '0') {
          console.log(res.data)
          this.user=res.data
          sessionStorage.setItem("user",JSON.stringify(this.user))//缓存用户信息
        } else
          ElMessage.error(res.msg)
      })
      if(this.user.role === 2){
        request.get("/borrowRecord",{
          params:{
            borrowerId: this.user.id,
            status:"borrowing"
          }
        }).then(res => {
          console.log(res)
          let records = res.data.records
          let nowDate = new Date();
          this.overdueNum = records
              .filter(r => new Date(r.deadTime) < nowDate)
              .map(() => 1)
              .reduce((s, i) => s + i, 0)
        })
      }
    }
  },
  data(){
    return {
      user:{
        role:0
      },
      overdueNum:0
    }
  }
}
</script>

<style scoped>
.icon {
  width: 30px;
  height: 30px;
  padding-top: 5px;
  padding-right: 10px;
}
</style>
