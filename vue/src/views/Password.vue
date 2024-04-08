<template xmlns="http://www.w3.org/1999/html">
  <div>
    <el-card style="width: 40%; margin-left: 120px; margin-top: 40px" >
      <el-form
          ref="form"
          :model="form"
          status-icon
          :rules="rules"
          label-width="100px"
          class="demo-ruleForm"
      >
        <el-form-item label="new password" prop="password">
          <el-input
              v-model="form2.password"
              type="password"
              autocomplete="off"
          ></el-input>
        </el-form-item>
        <el-form-item label="confirm new password" prop="checkpassword">
          <el-input
              v-model="form.checkpassword"
              type="password"
              autocomplete="off"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm" style="text-align: center;margin-left: 120px">submit</el-button>
          <el-button @click="resetForm('form')" style="text-align: center;margin-left: 20px">reset</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import request from "../utils/request";
import {ElMessage} from "element-plus";
import router from "@/router";

export default {
  name: "Password",
  data() {

    const validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('Please enter a new password.'))
      } else {
        callback()
      }
    }
    const validatePass3 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('Please enter a new password again.'))
      } else if (value !== this.form2.password) {
        callback(new Error("The passwords entered twice do not match."))
      } else {
        callback()
      }
    }
    return {

      isDisabled: false,
      time: 60,
      form: {
        checkpassword: '',
      },
      form2:{
        password:'',
        id:0
      },
      rules: {
        password: [{ validator: validatePass, trigger: 'blur' ,required: true}],
        checkpassword: [{ validator: validatePass3, trigger: 'blur',required: true,}],
      },
    }
  },
  created() {
    let userJson = sessionStorage.getItem("user")
    if(!userJson) {
      router.push("/login")
      return
    }
    this.form2.id = JSON.parse(userJson).id
  },
  methods: {

    submitForm() {
      this.$refs['form'].validate((valid) => {
        if (valid) {

          request.put("/user", this.form2).then(res => {
            if (res.code == '0') {
              ElMessage.success("Password changed successfully. Please log in again.")
              sessionStorage.removeItem("user")//清空缓存的用户信息
              this.$router.push("/login")//跳转登录界面
            } else {
              ElMessage.error(res.msg)
            }
          })
        }
      })
    },
    resetForm(formName) {
      this.$refs[formName].resetFields()
    },
  },
}
</script>

<style scoped>

</style>
