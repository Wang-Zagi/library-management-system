<template>
  <!-- Search -->
  <div style="margin: 5px 0;">
    <el-form :inline="true" size="small">
      <el-form-item label="Book Barcode">
        <el-input v-model="bookBarcode" placeholder="Please enter" clearable style="width: 200px">
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
    <el-table-column prop="bookBarcode" label="Book Barcode"/>
    <el-table-column prop="bookName" label="Book Name" />
    <el-table-column prop="borrowTime" label="Borrow Time" />
    <el-table-column prop="deadTime" label="Latest Return Time" />
    <el-table-column label="Overdue Days">
      <template v-slot="scope">
        <span style="color: red">{{overdueDays(scope.row.deadTime)}}</span>
      </template>
    </el-table-column>
    <el-table-column fixed="right" label="Operation" v-if="user.role === 1">
      <template v-slot="scope">
        <el-button size="mini" @click="handleEdit(scope.row)">Alter</el-button>
      </template>
    </el-table-column>
  </el-table>
  <!-- Pagination -->
  <div style="margin: 10px 0" v-if="user.role==1">
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
          bookBarcode: this.bookBarcode,
          bookName: this.bookName,
          borrowerName: this.borrowerName,
          borrowerId: this.user.id,
          status: "borrowing",
        }
      }).then(res =>{
        console.log(res)
        this.recordList = res.data.records
        this.total = res.data.total
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
      this.bookBarcode= null
      this.bookName = null
      this.borrowerName = null
      this.load()
    },
    handleEdit(row){
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogVisible2 = true
    },
    overdueDays(deadTime){
      let timeDiff = new Date().getTime() - new Date(deadTime).getTime();
      let dayDiff = timeDiff / (1000 * 3600 * 24);
      return dayDiff>0?Math.ceil(dayDiff):""
    }
  },
  data() {
    return {
      bookBarcode:null,
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
