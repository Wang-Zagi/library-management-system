<template>
  <div class="home" style ="padding: 10px">
    <!-- 搜索-->
    <div style="margin: 5px 0;">

      <el-form :inline="true" size="small" >
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
        <el-form-item>
          <el-button type="primary" style="margin-left: 1%" @click="load" size="mini">查询</el-button>
        </el-form-item>
        <el-form-item>
          <el-button size="mini"  type="danger" @click="clear">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 数据字段-->
    <el-table :data="recordList" stripe :border="true">
      <el-table-column prop="bookId" label="图书编号" sortable />
      <el-table-column prop="bookName" label="图书名称" />
      <el-table-column prop="lendTime" label="借阅时间" />
      <el-table-column prop="deadTime" label="最迟归还日期" />
      <el-table-column fixed="right" label="操作" >
        <template v-slot="scope">
          <el-button  size="mini" @click ="handleEdit(scope.row)" v-if="user.role === 1">修改</el-button>
          <el-popconfirm title="确认续借(续借一次延长30天)?" @confirm="handlereBorrow(scope.row)" v-if="user.role === 2" :disabled="scope.row.prolong == 0">
            <template #reference>
              <el-button type="danger" size="mini">续借</el-button>
            </template>
          </el-popconfirm>
          <el-popconfirm title="确认归还?" @confirm="handleReturn(scope.row) ">
            <template #reference>
              <el-button type="primary" size="mini" >归还</el-button>
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
          @size-change="load"
          @current-change="load"
      >
      </el-pagination>

    </div>
  </div>
</template>

<script>
// @ is an alias to /src
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
      request.get("/lendRecord",{
        params:{
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          bookId: this.bookId,
          bookName: this.bookName,
          borrowerName: this.borrowerName,
          borrowerId: this.user.id,
          status: "借阅中",
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
      request.put("/lendRecord",record).then(res =>{
        console.log(res)
        if(res.code == '0'){
          ElMessage({
            message: '续借成功',
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
