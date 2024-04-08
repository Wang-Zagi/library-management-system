<template>
  <div class="login-container">
    <el-form ref="form" :model="form" :rules="rules" class="login-page">
      <h2 class="title" style="margin-bottom: 20px">Reset Password</h2>
      <el-form-item prop="username">
        <el-input v-model="form.username" placeholder="Please enter your username." clearable>
        </el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input v-model="form.password" placeholder="Please enter a new password." clearable show-password>
        </el-input>
      </el-form-item>
      <el-form-item prop="confirm">
        <el-input v-model="form.confirm" placeholder="Please confirm your password again." clearable show-password>
        </el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" style="width: 100%; font-size: 18px" @click="register">Reset Password</el-button>
      </el-form-item>
      <el-form-item>
        <el-button type="text" style="font-size: 16px" @click="$router.push('/login')">Proceed to Log In >> </el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import request from "../utils/request";
import {ElMessage} from "element-plus";
export default {
  name: "Forget",
  data() {
    return {
      isDisabled: false,
      time: 60,
      form: {},
      rules: {
        username: [
          {
            required: true,
            message: 'Please enter your username.',
            trigger: 'blur',
          },
          {
            min: 2,
            max: 13,
            message: 'The length must be between 2 and 13 characters.',
            trigger: 'blur',
          },
        ],
        password: [
          {
            required: true,
            message: 'Please enter your password.',
            trigger: 'blur',
          }
        ],
        confirm: [
          {
            required: true,
            message: "Please confirm your password.",
            trigger: "blur"
          }
        ],
      }
    }
  },
  methods: {
    register() {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          if (!this.form.password) {
            ElMessage.error("Please fill in your new password.")
            return
          }
          if (this.form.password != this.form.confirm) {
            ElMessage.error("The two password inputs do not match.")
            return
          }
          request.put("forget", this.form).then(res => {
            if (res.code == '0') {
              ElMessage.success("Password successfully updated")
              this.$router.push("/login")
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
  height: 100%;
  background: url('../img/bg.svg');
  background-size: contain;
}
.login-page {
  border-radius: 5px;
  margin: 180px auto;
  width: 350px;
  padding: 35px 35px 15px;
  background: #fff;
  border: 1px solid #eaeaea;
  box-shadow: 0 0 25px #cac6c6;
}
</style>
