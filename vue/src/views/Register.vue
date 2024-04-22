<template>
<div  class="login-container"  >
    <el-form ref="form" :model="form"    :rules="rules" class="login-page">
      <h2 class="title" style="margin-bottom: 20px;text-align: center">User Register</h2>
      <el-form-item prop="username" >
        <el-input v-model="form.username" placeholder="Please enter your username." clearable >
          <template #prefix>
            <el-icon class="el-input__icon"><User/></el-icon>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input v-model="form.password"  placeholder="Please enter your password." clearable show-password>
          <template #prefix>
            <el-icon class="el-input__icon"><Lock /></el-icon>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item prop="confirm">
        <el-input v-model="form.confirm" placeholder="Please confirm your password again." clearable show-password>
          <template #prefix>
            <el-icon class="el-input__icon"><Lock /></el-icon>
          </template>
        </el-input>
      </el-form-item>
<!--      <el-form-item prop="role">-->
<!--        <el-radio v-model="form.role" label="2">reader</el-radio>-->
<!--        <el-radio v-model="form.role" label="1">admin</el-radio>-->
<!--      </el-form-item>-->
      <el-form-item prop="authorize" v-if="form.role==1">
        <el-input v-model="form.authorize" placeholder="Please enter the administrator registration code." clearable show-password>
          <template #prefix>
            <el-icon class="el-input__icon"><Lock /></el-icon>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item >
        <el-button type="primary" style=" width: 100%;font-size: 20px"  @click="register">register</el-button>
      </el-form-item>
      <el-form-item><el-button type="text" style="font-size: 16px" @click="$router.push('/login')">login>> </el-button></el-form-item>
    </el-form>
</div>

</template>

<script>
import request from "../utils/request";
import {ElMessage} from "element-plus";
export default {
  name: "Login",
  data(){
    return{
      form:{
        role:"2"
      },
      validCode: '',
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
      confirm:[
        {
          required:true,
          message:"Please confirm your password.",
          trigger:"blur"
        }
      ],
        authorize:[
          {
            required:true,
            message:"Please enter the registration code.",
            trigger:"blur"
          }
        ],
      }
    }
    },

  methods:{
    register(){
      this.$refs['form'].validate((valid) => {
        if (valid) {
          if(this.form.password != this.form.confirm)
          {
            ElMessage.error("The two password inputs do not match.")
            return
          }
          if(this.form.role == 1 && this.form.authorize != "2236")
          {
            ElMessage.error("Please enter the correct registration code.")
            return
          }
          request.post("user/register",this.form).then(res=>{
            if(res.code == '0')
            {
              ElMessage.success("Registration successful")
              this.$router.push("/login")
            }
            else {ElMessage.error(res.msg)}
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