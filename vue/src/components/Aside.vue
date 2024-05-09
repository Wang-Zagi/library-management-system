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
    <el-menu-item index="/bookwithuser" v-if="user.role == 2">
      <el-icon><grid /></el-icon>
      <span>Books Borrowed</span>
    </el-menu-item>
    <el-menu-item index="/borrowrecord" v-if="user.role !=0">
      <svg class="icon" aria-hidden="true">
        <use xlink:href="#iconlend-record "></use>
      </svg>
      <span>Borrowing Records</span>
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
      <el-menu-item index="/person" style="font-color: white">
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
    <el-menu-item index="/dashboard" v-if="user.role == 1">
      <svg class="icon" aria-hidden="true">
        <use xlink:href="#icondashboard "></use>
      </svg>
      <span>Data Statistics</span>
    </el-menu-item>
  </el-menu>

</div>
</template>

<script>



export default {
  name: "Aside",
  components:{},
  created(){
    let userStr = sessionStorage.getItem("user")
    if(userStr) this.user = JSON.parse(userStr)
  },
  data(){
    return {
      user:{
        role:0
      },
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
