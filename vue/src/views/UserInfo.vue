<template>
  <div>
    <el-card style="width: 40%; margin-left: 120px; margin-top: 40px" >
        <h2 style="padding: 30px">Personal Information</h2>
      <el-form :model="userForm" ref="form" label-width="80px">
        <el-form-item label="Username">
          <el-input style="width: 80%" v-model="userForm.username"></el-input>
        </el-form-item>
        <el-form-item label="Gender">
          <div>
            <el-radio v-model="userForm.sex" label="Male">Male</el-radio>
            <el-radio v-model="userForm.sex" label="Female">Female</el-radio>
          </div>
        </el-form-item>
        <el-form-item label="Role">
            <span v-if="userForm.role==1" style="margin:5px">Administrator</span>
            <span v-if="userForm.role==2" style="margin:5px">Reader</span>
        </el-form-item>
        <el-form-item label="Phone">
          <el-input style="width: 40%" v-model="userForm.phone"></el-input>
        </el-form-item>
        <el-form-item label="Fine">
          <span :style="userForm.debt>0?{color:'red'}:{}" v-text="'$'+this.userForm.debt"></span>
          <span>
            <el-button type="danger" @click="payDebt" :disabled="userForm.debt==0" style="margin-left: 20px">Pay</el-button>
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
import moment, { duration } from "moment";

export default {
  name: "userInfo",
  props:["user","loaded"],
  data(){
    return{
      userForm:{}
    }
  },
  created() {
    this.loaded.then(()=>
        this.userForm=JSON.parse(JSON.stringify(this.user))
    )
  },
  methods: {
    update() {
      request.put("/user", this.userForm).then(res => {
        console.log(res)
        if (res.code == '0') {
          ElMessage.success("Update successful")
          // Trigger Layout to update user information
          sessionStorage.setItem("user",JSON.stringify(this.userForm))
          this.$emit("userInfoChange")
        } else {
          ElMessage.error(res.msg)
        }
      })
    },
    payDebt() {
      window.open("http://127.0.0.1:8080/alipay/pay?subject=Fine" +
          "&traceNo="+this.user.id+"-"+moment().format('YYYY-MM-DD-HH-mm-ss')+
          "&totalAmount="+this.user.debt,'_self')
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
