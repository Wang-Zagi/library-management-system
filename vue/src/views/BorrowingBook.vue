<template>
    <!-- Search -->
    <div style="margin: 5px 0;">
      <el-form :inline="true" size="small">
        <el-form-item label="Book ISBN">
          <el-input v-model="bookId" placeholder="Please enter" clearable style="width: 200px">
            <template #prefix><el-icon class="el-input__icon"><search /></el-icon></template>
          </el-input>
        </el-form-item>
        <el-form-item label="Book Name">
          <el-input v-model="bookName" placeholder="Please enter" clearable style="width: 200px">
            <template #prefix><el-icon class="el-input__icon"><search /></el-icon></template>
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" style="margin-left: 1%" @click="load" size="mini">Search</el-button>
        </el-form-item>
        <el-form-item>
          <el-button size="mini" type="danger" @click="clear">Reset</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- Data Fields -->
    <el-table :data="recordList" stripe :border="true">
      <el-table-column prop="bookId" label="Book ISBN" sortable />
      <el-table-column prop="bookName" label="Book Name" />
      <el-table-column prop="borrowTime" label="Borrow Time" />
      <el-table-column prop="deadTime" label="Latest Return Date" />
      <el-table-column fixed="right" label="Operation">
        <template v-slot="scope">
          <el-button size="mini" @click="handleEdit(scope.row)" v-if="user.role === 1">Alter</el-button>
          <el-popconfirm title="Confirm renewal (extending for 30 days)?" @confirm="handlereBorrow(scope.row)" v-if="user.role === 2" :disabled="scope.row.prolong == 0">
            <template #reference>
              <el-button type="danger" size="mini">Renewal</el-button>
            </template>
          </el-popconfirm>
          <el-popconfirm title="Confirm return?" @confirm="handleReturn(scope.row) ">
            <template #reference>
              <el-button type="primary" size="mini">Return</el-button>
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
        @current-change="load"
      >
      </el-pagination>
    </div>
</template>

<script>
import request from "../utils/request";
import {ElMessage} from "element-plus";
import moment from "moment";
import router from "@/router";
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
  name: 'borrowingBook',
  methods: {
    load(){
      request.get("/borrowRecord",{
        params:{
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          bookId: this.bookId,
          bookName: this.bookName,
          borrowerName: this.borrowerName,
          borrowerId: this.user.id,
          status: "borrowed",
        }
      }).then(res =>{
        console.log(res)
        this.recordList = res.data.records
        this.total = res.data.total
      })
    },
    handlereBorrow(row){
      let record = JSON.parse(JSON.stringify(row))
      let deadtTime = new Date(row.deadTime);
      deadtTime.setDate(deadtTime.getDate()+30);
      record.deadTime = moment(deadtTime).format("yyyy-MM-DD HH:mm:ss");
      request.put("/borrowRecord",record).then(res =>{
        console.log(res)
        if(res.code == '0'){
          ElMessage({
            message: 'Renewal successfully',
            type: 'success',
          })
        }
        else {
          ElMessage.error(res.msg)
        }
        this.load()
        this.dialogVisible2 = false
      })
    },
    handleReturn(row){
      let record = JSON.parse(JSON.stringify(row))
      record.status = "returned"
      record.returnTime = moment(new Date()).format("yyyy-MM-DD HH:mm:ss")
      request.put("borrowRecord",record).then(res =>{
        console.log(res)
        if(res.code == '0' ){
          ElMessage.success("Return successfully")
        }
        else
          ElMessage.error(res.msg)
        this.load()
      })
    },
    clear(){
      this.bookId = null
      this.bookName = null
      this.borrowerName = null
      this.load()
    },
    handleEdit(row){
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogVisible2 = true
    },
  },
  data() {
    return {
      bookId:null,
      bookName:null,
      borrowerName:null,
      total:10,
      currentPage:1,
      pageSize: 10,
      recordList: [],
      user:{},
      dialogVisible: false,
      dialogVisible2: false,
    }
  },
}
</script>
