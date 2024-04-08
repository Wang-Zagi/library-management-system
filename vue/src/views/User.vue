<template>
  <div class="home" style="padding: 10px">
    <!-- Button -->
    <!-- Search -->
    <div style="margin: 5px 0;">
      <el-form inline="true" size="small">
        <el-form-item label="Reader ID">
          <el-input v-model="id" placeholder="Please enter the reader ID" clearable>
            <template #prefix><el-icon class="el-input__icon"><search/></el-icon></template>
          </el-input>
        </el-form-item>
        <el-form-item label="Username">
          <el-input v-model="name" placeholder="Please enter username" clearable>
            <template #prefix><el-icon class="el-input__icon"><search /></el-icon></template>
          </el-input>
        </el-form-item>
        <el-form-item label="Phone Number">
          <el-input v-model="phone" placeholder="Please enter phone number" clearable>
            <template #prefix><el-icon class="el-input__icon"><search /></el-icon></template>
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" style="margin-left: 1%" @click="load" size="mini">Search</el-button>
        </el-form-item>
        <el-form-item>
          <el-button size="mini" type="danger" @click="clear">Reset</el-button>
        </el-form-item>
        <el-form-item style="margin-left: 40px">
          <el-popconfirm title="Confirm deletion?" @confirm="deleteBatch" v-if="user.role == 1">
            <template #reference>
              <el-button type="danger" size="mini">Batch delete</el-button>
            </template>
          </el-popconfirm>
        </el-form-item>
      </el-form>
    </div>

    <!-- Data fields -->
    <el-table :data="userList" stripe border="true" @selection-change="handleSelectionChange">
      <el-table-column v-if="user.role ==1"
                       type="selection"
                       width="55">
      </el-table-column>
      <el-table-column prop="id" label="Reader ID" min-width="10"/>
      <el-table-column prop="username" label="Username" min-width="10"/>
      <el-table-column prop="sex" label="Gender" min-width="5"/>
      <el-table-column prop="phone" label="Phone Number" min-width="10"/>
      <el-table-column fixed="right" label="Operation" min-width="10">
        <template v-slot="scope">
          <el-button size="mini" @click="handleEdit(scope.row)">Edit</el-button>
          <el-popconfirm title="Confirm deletion?" @confirm="handleDelete(scope.row.id)">
            <template #reference>
              <el-button type="danger" size="mini" style="margin-top: 10px; margin-left: 20px">Delete user</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <!-- Pagination -->
    <div style="margin: 10px 0">
      <el-pagination
          v-model:currentPage="currentPage"
          :page-sizes="[5, 10, 20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="load"
          @current-change="load">
      </el-pagination>

      <el-dialog v-model="dialogVisible" title="Edit reader information" width="30%">
        <el-form :model="form" label-width="120px">
          <el-form-item label="Username">
            <el-input style="width: 80%" v-model="form.username"></el-input>
          </el-form-item>
          <el-form-item label="Gender">
            <div>
              <el-radio v-model="form.sex" label="Male">Male</el-radio>
              <el-radio v-model="form.sex" label="Female">Female</el-radio>
            </div>
          </el-form-item>
          <el-form-item label="Phone Number">
            <el-input style="width: 80%" v-model="form.phone"></el-input>
          </el-form-item>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="dialogVisible = false">Cancel</el-button>
            <el-button type="primary" @click="save">Confirm</el-button>
          </span>
        </template>
      </el-dialog>
    </div>
  </div>
</template>

<script>
// @ is an alias to /src
import request from "../utils/request";
import {ElMessage} from "element-plus";
import router from "@/router";
import qs from "qs";

export default {
  created(){
    let userJson = sessionStorage.getItem("user")
    if(!userJson) {
      router.push("/login")
      return
    }
    this.user = JSON.parse(userJson)
    this.load()
  },
  name: 'User',
  methods: {
    load(){
      request.get("/user",{
        params:{
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          id: this.id,
          name: this.name,
          phone: this.phone,
        }
      }).then(res =>{
        console.log(res)
        this.userList = res.data.records
        this.total = res.data.total
      })
    },
    save(){
      request.put("/user",this.form).then(res =>{
        console.log(res)
        if(res.code == '0'){
          ElMessage({
            message: 'update successfully',
            type: 'success',
          })
        }
        else {
          ElMessage.error(res.msg)
        }
        this.load() //不知道为啥，更新必须要放在这里面
        this.dialogVisible = false
      })
    },
    handleDelete(id){
      request.delete("user/" + id ).then(res =>{
        console.log(res)
        if(res.code == '0' ){
          ElMessage.success("delete successfully")
        }
        else
          ElMessage.error(res.msg)
        this.load()
      })
    },
    deleteBatch(){
      if (!this.ids.length) {
        ElMessage.warning("Please select the data to be deleted")
        return
      }
      request.delete("/user",{
        params:{ids:this.ids},
        paramsSerializer: params => {
          return qs.stringify(params, { indices: false })
        }
      }).then(res =>{
        if(res.code == '0'){
          ElMessage.success("delete successfully")
          this.load()
        }
        else {
          ElMessage.error(res.msg)
        }
      })
    },
    clear(){
      this.id = null
      this.name = null
      this.phone = null
      this.load()
    },
    handleSelectionChange(val){
      this.ids = val.map(v => v.id)
    },
    handleEdit(row){
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogVisible = true
    },
  },
  data() {
    return {
      form: {},
      userList: [],
      id:null,
      name:null,
      phone:null,
      total:10,
      currentPage:1,
      pageSize: 10,
      user:{},
      ids:[],
      dialogVisible : false,
    }
  },
}
</script>
