<template>
  <!-- 搜索-->
  <div style="margin: 5px 0;">
    <el-form inline="true" size="small">
      <el-form-item label="Book Barcode" >
        <el-input v-model="bookBarcode" placeholder="Please enter" clearable style="width: 200px">
          <template #prefix><el-icon class="el-input__icon"><search /></el-icon></template>
        </el-input>
      </el-form-item >
      <el-form-item label="Book Name" >
        <el-input v-model="bookName" placeholder="Please enter" clearable style="width: 200px">
          <template #prefix><el-icon class="el-input__icon"><search /></el-icon></template>
        </el-input>
      </el-form-item >
      <el-form-item label="Borrower Name" v-if="user.role === 1">
        <el-input v-model="borrowerName" placeholder="Please enter" clearable style="width: 200px">
          <template #prefix><el-icon class="el-input__icon"><search /></el-icon></template>
        </el-input>
      </el-form-item >
      <el-form-item>
        <el-button type="primary" style="margin-left: 1%" @click="load" size="mini">Search</el-button>
      </el-form-item>
      <el-form-item>
        <el-button size="mini"  type="danger" @click="clear">Reset</el-button>
      </el-form-item>
<!--      <el-form-item style="margin-left: 30px" v-if="user.role == 1">-->
<!--        <el-popconfirm title="confirm delete?" @confirm="deleteBatch" v-if="user.role == 1">-->
<!--          <template #reference>-->
<!--            <el-button type="danger">Batch delete</el-button>-->
<!--          </template>-->
<!--        </el-popconfirm>-->
<!--      </el-form-item>-->
    </el-form>
  </div>

  <el-table :data="recordList" stripe :border="true"
            @selection-change="handleSelectionChange"
            :max-height="user.role==1?517:508"
            :row-style="{height:'6.4vh'}"
            :cell-style="{padding:0}">
    <el-table-column prop="bookBarcode" label="Book Barcode" min-width="110px" fixed="left"/>
    <el-table-column prop="bookName" label="Book Name" min-width="200px"/>
    <el-table-column prop="borrowerId" label="Borrower Id" min-width="100px" v-if="user.role ==1"/>
    <el-table-column prop="borrowerName" label="Borrower Name" min-width="120px" v-if="user.role ==1"/>
    <el-table-column prop="borrowTime" label="Borrow Time" min-width="140px"/>
    <el-table-column prop="returnTime" label="Return Time" min-width="140px"/>
    <el-table-column prop="status" label="Status"  min-width="100px" fixed="right">
      <template v-slot="scope">
        <el-tag :type="scope.row.status === 'returned'?'success':
                scope.row.status === 'confirming'?'primary':'warning'">{{scope.row.status}}</el-tag>
      </template>
    </el-table-column>
    <el-table-column v-if="user.role === 1" label="operation" width="180px" fixed="right">
      <template v-slot="scope">
        <el-popconfirm title="confirm return?" @confirm="handleConfirm(scope.row)" v-if="scope.row.status=='confirming'">
          <template #reference>
            <el-button type="primary" size="mini">Confirm</el-button>
          </template>
        </el-popconfirm>
        <el-popconfirm title="confirm reject?" @confirm="handleReject(scope.row)" v-if="scope.row.status=='confirming'">
          <template #reference>
            <el-button type="danger" size="mini">Reject</el-button>
          </template>
        </el-popconfirm>
      </template>
    </el-table-column>
  </el-table>

  <!--    分页-->
  <div style="margin: 10px 0">
    <el-pagination
        v-model:currentPage="currentPage"
        v-model:page-size="pageSize"
        :page-sizes="[5, 10, 20]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="load()"
        @current-change="load()"
    >
    </el-pagination>

  </div>
</template>

<script >

import request from "../utils/request";
import {ElMessage} from "element-plus";
import { defineComponent, reactive, toRefs } from 'vue'
import router from "@/router";
import moment from "moment/moment";
import qs from "qs";

export default defineComponent({
  props:["user"],
  name: 'borrowRecord',
  created(){
    this.load()
  },
  methods: {
    load(){
      request.get("/borrowRecord",{
        params:{
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          borrowerId: this.user.role==2?this.user.id:null,
          borrowerName: this.borrowerName,
          bookBarcode: this.bookBarcode,
          bookName: this.bookName,
        }
      }).then(res =>{
        console.log(res)
        this.recordList = res.data.records
        this.total = res.data.total
      })
    },
    handleConfirm(row){
      let record = JSON.parse(JSON.stringify(row))
      record.status = "returned"
      request.put("borrowRecord",record).then(res =>{
        console.log(res)
        if(res.code == '0' )
          ElMessage.success("Confirm successfully")
        else
          ElMessage.error(res.msg)
        this.load()
      })
    },
    handleReject(row){
      let record = JSON.parse(JSON.stringify(row))
      record.status = "borrowing"
      request.put("borrowRecord",record).then(res =>{
        console.log(res)
        if(res.code == '0' )
          ElMessage.success("Reject successfully")
        else
          ElMessage.error(res.msg)
        this.load()
      })
    },
    handleDelete(row){
      request.delete("borrowRecord/"+row.id).then(res =>{
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
      if(!this.ids.length){
        ElMessage.warning("Please select the data to be deleted")
        return
      }
      console.log(this.ids)
      request.delete("/borrowRecord", {
        params:{ids:this.ids},
        paramsSerializer: params => {
          return qs.stringify(params, { indices: false })
        }
      }).then(res =>{
        if(res.code == '0'){
          ElMessage.success("")
          this.load()
        }
        else {
          ElMessage.error(res.msg)
        }
      })
    },
    handleSelectionChange(val){
      this.ids = val.map(v =>v.id)
    },
    clear(){
      this.bookBarcode = null
      this.bookName = null
      this.borrowerName = null
      this.load()
    },
    handleEdit(row){
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogVisible = true
    },
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
      ids:[],
      dialogVisible : false,
    }
  },

})
</script>
