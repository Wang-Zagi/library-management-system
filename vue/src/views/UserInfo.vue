<template>
  <div>
    <el-card style="width: 40%; margin-left: 120px; margin-top: 40px" >
        <h2 style="padding: 30px">Personal Information</h2>
      <el-form :model="user" ref="form" label-width="80px">
        <el-form-item label="Username">
          <el-input style="width: 80%" v-model="user.username"></el-input>
        </el-form-item>
        <el-form-item label="Gender">
          <div>
            <el-radio v-model="user.sex" label="Male">Male</el-radio>
            <el-radio v-model="user.sex" label="Female">Female</el-radio>
          </div>
        </el-form-item>
        <el-form-item label="Role">
            <span v-if="user.role==1" style="margin:5px">Administrator</span>
            <span v-if="user.role==2" style="margin:5px">Reader</span>
        </el-form-item>
        <el-form-item label="Phone">
          <el-input style="width: 40%" v-model="user.phone"></el-input>
        </el-form-item>
        <el-form-item label="Debt">
          <span v-text="this.user.debt"></span>
          <span>
            <button @click.prevent="payDebt" style="margin-left: 10px">Pay</button>
          </span>
        </el-form-item>
      </el-form>
      <div style="text-align: center">
        <el-button type="primary" @click="update">Save</el-button>
      </div>
    </el-card>
  </div>
</template>

<script>
import request from "@/utils/request";
import {ElMessage} from "element-plus";
import router from "@/router";
import { time } from "echarts";
import { duration } from "moment";

export default {
  name: "Person",
  data() {
    return {
      user: {}
    }
  },
  created() {
    let userJson = sessionStorage.getItem("user")
    if(!userJson) {
      router.push("/login")
      return
    }
    this.user = JSON.parse(userJson)
  },
  methods: {
    update() {
      request.put("/user", this.user).then(res => {
        console.log(res)
        if (res.code == '0') {
          ElMessage.success("Update successful")
          console.log(this.user)
          // Trigger Layout to update user information
          this.$emit("userInfoChange")
        } else {
          ElMessage.error(res.msg)
        }
      })
    },
    payDebt() {
      console.log("pay debt")
      const new_user = this.user
      new_user.debt = 0
      this.user.debt = 0
      const userJson = JSON.stringify(this.user)
      sessionStorage.setItem("user",userJson)
      request.put("/user", new_user).then(res => {
        console.log(res)
        if (res.code == '0') {
          ElMessage.success("Pay successful")
          console.log(this.user)
          // Trigger Layout to update user information
          this.$emit("userInfoChange")
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  }
}
</script>

<style>
.avatar-uploader  {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader:hover {
  border-color: #409EFF;
}

.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
.box-card {
  width: 60%;
  margin: auto;
  padding: 20px;
}
</style>
