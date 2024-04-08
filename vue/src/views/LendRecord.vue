<template>
  <div class="home" style ="padding: 10px">

    <!-- 搜索-->
    <div style="margin: 5px 0;">
      <el-form inline="true" size="small">
        <el-form-item label="图书编号" >
          <el-input v-model="bookId" placeholder="请输入图书编号"  clearable>
            <template #prefix><el-icon class="el-input__icon"><search /></el-icon></template>
          </el-input>
        </el-form-item >
        <el-form-item label="图书名称" >
          <el-input v-model="bookName" placeholder="请输入图书名称"  clearable>
            <template #prefix><el-icon class="el-input__icon"><search /></el-icon></template>
          </el-input>
        </el-form-item >
        <el-form-item label="读者用户名" v-if="user.role === 1">
          <el-input v-model="borrowerName" placeholder="请输入读者用户名"  clearable>
            <template #prefix><el-icon class="el-input__icon"><search /></el-icon></template>
          </el-input>
        </el-form-item >
        <el-form-item>
          <el-button type="primary" style="margin-left: 1%" @click="load" size="mini">查询</el-button>
        </el-form-item>
        <el-form-item>
          <el-button size="mini"  type="danger" @click="clear">重置</el-button>
        </el-form-item>
        <el-form-item style="margin-left: 100px" v-if="user.role == 1">
          <el-popconfirm title="确认删除?" @confirm="deleteBatch" v-if="user.role == 1">
            <template #reference>
              <el-button type="danger">批量删除</el-button>
            </template>
          </el-popconfirm>
        </el-form-item>
      </el-form>
    </div>

    <el-table :data="recordList" stripe :border="true" @selection-change="handleSelectionChange">
      <el-table-column v-if="user.role ==1"
                       type="selection"
                       width="55">
      </el-table-column>
      <el-table-column prop="bookId" label="图书编号"/>
      <el-table-column prop="bookName" label="图书名称" />
      <el-table-column prop="borrowerId" label="读者编号" v-if="user.role ==1"/>
      <el-table-column prop="borrowerName" label="读者用户名" v-if="user.role ==1"/>
      <el-table-column prop="lendTime" label="借阅时间"/>
      <el-table-column prop="returnTime" label="归还时间"/>
      <el-table-column prop="status" label="状态" >
        <template v-slot="scope">
          <el-tag :type="scope.row.status=='已归还'?'success':'warning'">{{scope.row.status}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column v-if="user.role === 1" label="操作" width="230px">
        <template v-slot="scope">
          <el-popconfirm title="确认代还?" @confirm="handleReturn(scope.row)">
            <template #reference>
              <el-button type="primary" size="mini" :disabled="scope.row.status=='已归还'">代还</el-button>
            </template>
          </el-popconfirm>
          <el-popconfirm title="确认删除?" @confirm="handleDelete(scope.row)">
            <template #reference>
              <el-button type="danger" size="mini" >删除记录</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <!--    分页-->
    <div style="margin: 10px 0">
      <el-pagination
          v-model:currentPage="currentPage"
          :page-sizes="[5, 10, 20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="load()"
          @current-change="load()"
      >
      </el-pagination>

    </div>
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

  created(){
    let userJson = sessionStorage.getItem("user")
    if(!userJson) {
      router.push("/login")
      return
    }
    this.user = JSON.parse(userJson)
    this.load()
  },
  name: 'lendRecord',
  methods: {
    load(){
      request.get("/lendRecord",{
        params:{
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          borrowerId: this.user.role==2?this.user.id:null,
          borrowerName: this.borrowerName,
          bookId: this.bookId,
          bookName: this.bookName,
        }
      }).then(res =>{
        console.log(res)
        this.recordList = res.data.records
        this.total = res.data.total
      })
    },
    handleReturn(row){
      let record = JSON.parse(JSON.stringify(row))
      record.status = "已归还"
      record.returnTime = moment(new Date()).format("yyyy-MM-DD HH:mm:ss")
      request.put("lendRecord",record).then(res =>{
        console.log(res)
        if(res.code == '0' ){
          ElMessage.success("归还成功")
        }
        else
          ElMessage.error(res.msg)
        this.load()
      })
    },
    handleDelete(row){
      request.delete("lendRecord/"+row.id).then(res =>{
        console.log(res)
        if(res.code == '0' ){
          ElMessage.success("删除成功")
        }
        else
          ElMessage.error(res.msg)
        this.load()
      })
    },
    deleteBatch(){
      if(!this.ids.length){
        ElMessage.warning("请选择至少一条数据！")
        return
      }
      console.log(this.ids)
      request.delete("/lendRecord", {
        params:{ids:this.ids},
        paramsSerializer: params => {
          return qs.stringify(params, { indices: false })
        }
      }).then(res =>{
        if(res.code == '0'){
          ElMessage.success("批量删除成功")
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
      this.bookId = null
      this.bookName = null
      this.borrowerName = null
      this.load()
    },
    handleEdit(row){
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogVisible = true
    },
  },

  setup() {
    const state = reactive({
      shortcuts: [
        {
          text: 'Today',
          value: new Date(),
        },
        {
          text: 'Yesterday',
          value: () => {
            const date = new Date()
            date.setTime(date.getTime() - 3600 * 1000 * 24)
            return date
          },
        },
        {
          text: 'A week ago',
          value: () => {
            const date = new Date()
            date.setTime(date.getTime() - 3600 * 1000 * 24 * 7)
            return date
          },
        },
      ],
      value1: '',
      value2: '',
      value3: '',
      defaultTime: new Date(2000, 1, 1, 12, 0, 0), // '12:00:00'
    })

    return toRefs(state)
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
      ids:[],
      dialogVisible : false,
    }
  },

})
</script>
