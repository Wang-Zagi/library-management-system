<template>
<div  class="login-container"  >
    <el-form ref="form" :model="form"   :rules="rules" class="login-page">
      <h2 class="title" style="margin-bottom: 20px;text-align: center">Library Borrowing and Management System Login</h2>
      <el-form-item prop="username" >
        <el-input v-model="form.username"  placeholder="user name" clearable>
          <template #prefix>
            <el-icon class="el-input__icon"><User /></el-icon>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input v-model="form.password" placeholder="password" clearable show-password>
          <template #prefix>
            <el-icon class="el-input__icon"><Lock /></el-icon>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary"  style=" width: 100%;height: 130%;font-size: 16px" @click="login">login</el-button>
      </el-form-item>
      <el-form-item style="" text-align: center>
        <el-button type="text" style="font-size: 16px" @click="$router.push('/register')">No account? Go to register.</el-button>
      </el-form-item>
    </el-form>
</div>

</template>

<script>
import request from "../utils/request";
import {ElMessage} from "element-plus";

export default {
  name: "Login",
  data() {
    return {
      form: {},
      rules: {
        username: [
          {
            required: true,
            message: 'Please enter your username.',
            trigger: 'blur',
          }
        ],
        password: [
          {
            required: true,
            message: 'Please enter your password.',
            trigger: 'blur',
          }
        ]

      }

    }
  },
  methods: {
    login(){
      this.$refs['form'].validate((valid) => {
        if (valid) {
          request.post("user/login", this.form).then(res => {
            if (res.code == '0') {
              ElMessage.success("Login successful")
              sessionStorage.setItem("user",JSON.stringify(res.data))//缓存用户信息
              this.$router.push("/book")
            } else {
              ElMessage.error(res.msg)
            }
          })
        }
      })
    }
  }
}

</script>

<style scoped>
.login-container {
  position: fixed;
  width: 100%;
  height: 100vh;
  background: url('../img/bg.svg');
  background-size: contain;
  overflow: hidden;
}
.login-page {
  border-radius: 5px;
  margin: 300px auto;
  width: 420px;
  padding: 35px 35px 15px;
  background: #fff;
  border: 1px solid #eaeaea;
  box-shadow: 0 0 25px #cac6c6;
}
.el-input__inner{
  height: 40px;
}
</style>
