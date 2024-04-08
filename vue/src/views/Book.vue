<template>
  <div class="home" style ="padding: 10px">

    <!-- 搜索-->
    <div style="margin: 5px 0;">
      <el-form :inline="true" size="small">
        <el-form-item label="图书编号" >
          <el-input v-model="isbn" placeholder="请输入图书编号" clearable>
            <template #prefix><el-icon class="el-input__icon"><search/></el-icon></template>
          </el-input>
        </el-form-item >
        <el-form-item label="图书名称" >
          <el-input v-model="name" placeholder="请输入图书名称" clearable>
            <template #prefix><el-icon class="el-input__icon"><search /></el-icon></template>
          </el-input>
        </el-form-item >
        <el-form-item label="作者" >
          <el-input v-model="author" placeholder="请输入作者" clearable>
            <template #prefix><el-icon class="el-input__icon"><search /></el-icon></template>
          </el-input>
        </el-form-item >
        <el-form-item>
          <el-button type="primary" style="margin-left: 1%" @click="load" size="mini" >
            <svg-icon iconClass="search"/>查询</el-button>
        </el-form-item>
        <el-form-item>
          <el-button size="mini"  type="danger" @click="clear">重置</el-button>
        </el-form-item>
        <el-form-item style="margin-left: 100px" v-if="user.role === 1">
          <el-button type="primary" @click = "add">上架</el-button>
          <el-popconfirm title="确认下架?" @confirm="deleteBatch" v-if="user.role === 1">
            <template #reference>
              <el-button type="danger">批量下架</el-button>
            </template>
          </el-popconfirm>
        </el-form-item>
        <el-form-item style="float: right" v-if="numOfOutDataBook!==0">
          <el-popconfirm
              confirm-button-text="查看"
              cancel-button-text="取消"
              :icon="InfoFilled"
              icon-color="red"
              title="您有图书已逾期，请尽快归还"
              @confirm="toLook"
          >
            <template #reference>
              <el-button  type="warning">逾期通知</el-button>
            </template>
          </el-popconfirm>
        </el-form-item>
      </el-form>
    </div>

    <!-- 数据字段-->
    <el-table :data="tableData" stripe :border="true" @selection-change="handleSelectionChange">
      <el-table-column v-if="user.role ===1"
                       type="selection"
                       width="55">
      </el-table-column>
      <el-table-column prop="isbn" label="图书编号" min-width="10"/>
      <el-table-column prop="name" label="图书名称" min-width="18"/>
      <el-table-column prop="price" label="价格" min-width="5"/>
      <el-table-column prop="author" label="作者" min-width="10"/>
      <el-table-column prop="publisher" label="出版社" min-width="10"/>
      <el-table-column prop="publishTime" label="出版时间" min-width="8"/>
      <el-table-column prop="borrowNum" label="总借阅次数" min-width="8"/>
      <el-table-column prop="status" label="状态" min-width="6">
        <template v-slot="scope">
          <el-tag :type="scope.row.status==='在库中'?'success':'warning'">{{scope.row.status}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column fixed="right" label="操作" min-width="12">
        <template v-slot="scope">
          <el-button  size="mini" @click ="handleEdit(scope.row)" v-if="user.role === 1">编辑</el-button>
          <el-popconfirm title="确认下架?" @confirm="handleDelete(scope.row.isbn)" v-if="user.role === 1">
            <template #reference>
              <el-button type="danger" size="mini" >下架</el-button>
            </template>
          </el-popconfirm>
          <el-button size="mini" @click ="lend(scope.row.isbn,scope.row.name)" v-if="user.role === 2" :disabled="scope.row.status === '已借出'">借阅</el-button>
        </template>
      </el-table-column>
    </el-table>
<!--测试,通知对话框-->
    <el-dialog
        v-model="dialogVisible3"
        v-if="numOfOutDataBook!==0"
        title="逾期详情"
        width="50%"
    >
        <el-table :data="outDateBook" style="width: 100%">
          <el-table-column prop="isbn" label="图书编号" />
          <el-table-column prop="bookName" label="书名" />
          <el-table-column prop="lendTime" label="借阅日期" />
          <el-table-column prop="deadTime" label="截至日期" />
        </el-table>

      <template #footer>
      <span class="dialog-footer">
        <el-button type="primary" @click="dialogVisible3 = false"
        >确认</el-button>
      </span>
      </template>
    </el-dialog>
    <!--    分页-->
    <div style="margin: 10px 0">
      <el-pagination
          v-model:currentPage="currentPage"
          :page-sizes="[5, 10, 12]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="load"
          @current-change="load"
      >
      </el-pagination>

      <el-dialog v-model="dialogVisible" title="上架书籍" width="30%">
        <el-form :model="book" label-width="120px">

          <el-form-item label="图书编号">
            <el-input style="width: 80%" v-model="book.isbn"></el-input>
          </el-form-item>
          <el-form-item label="图书名称">
            <el-input style="width: 80%" v-model="book.name"></el-input>
          </el-form-item>
          <el-form-item label="价格">
            <el-input style="width: 80%" v-model="book.price"></el-input>
          </el-form-item>
          <el-form-item label="作者">
            <el-input style="width: 80%" v-model="book.author"></el-input>
          </el-form-item>
          <el-form-item label="出版社">
            <el-input style="width: 80%" v-model="book.publisher"></el-input>
          </el-form-item>
          <el-form-item label="出版时间">
            <div>
              <el-date-picker value-format="YYYY-MM-DD" type="date" style="width: 80%" clearable v-model="book.publishTime" ></el-date-picker>
            </div>
          </el-form-item>
        </el-form>
        <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="save('上架')">确 定</el-button>
      </span>
        </template>
      </el-dialog>

      <el-dialog v-model="dialogVisible2" title="编辑书籍信息" width="30%">
        <el-form :model="book" label-width="120px">

          <el-form-item label="图书编号" >
            <el-input style="width: 80%" v-model="book.isbn" disabled></el-input>
          </el-form-item>
          <el-form-item label="图书名称">
            <el-input style="width: 80%" v-model="book.name"></el-input>
          </el-form-item>
          <el-form-item label="价格">
            <el-input style="width: 80%" v-model="book.price"></el-input>
          </el-form-item>
          <el-form-item label="作者">
            <el-input style="width: 80%" v-model="book.author"></el-input>
          </el-form-item>
          <el-form-item label="出版社">
            <el-input style="width: 80%" v-model="book.publisher"></el-input>
          </el-form-item>
          <el-form-item label="出版时间">
            <div>
              <el-date-picker value-format="YYYY-MM-DD" type="date" style="width: 80%" clearable v-model="book.publishTime" ></el-date-picker>
            </div>
          </el-form-item>
        </el-form>
        <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible2 = false">取 消</el-button>
        <el-button type="primary" @click="save('编辑')">确 定</el-button>
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
import moment from "moment";
import router from "@/router";
import {InfoFilled} from "@element-plus/icons";
import qs from "qs";
export default {
  computed: {
    InfoFilled() {
      return InfoFilled
    }
  },
  created(){
    let userJson = sessionStorage.getItem("user")
    if(!userJson) {
      router.push("/login")
      return
    }
    this.user = JSON.parse(userJson)
    this.load()
  },
  name: 'Book',
  methods: {
    load(){
      this.numOfOutDataBook =0;
      this.outDateBook =[];
      request.get("/book",{
        params:{
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          isbn: this.isbn,
          name: this.name,
          author: this.author,
        }
      }).then(res =>{
        console.log(res)
        this.tableData = res.data.records
        this.total = res.data.total

        if(this.user.role === 2){
          request.get("/lendRecord",{
            params:{
              pageNum: 1,
              pageSize: this.total,
              borrowerId: this.user.id,
              status:"借阅中"
            }
          }).then(res =>{
            console.log(res)
            this.bookList = res.data.records
            this.number = this.bookList.length;
            var nowDate = new Date();
            for(let i=0; i< this.number; i++){
              let dDate = new Date(this.bookList[i].deadTime);
              if(dDate < nowDate){
                this.outDateBook[this.numOfOutDataBook] = {
                  isbn:this.bookList[i].isbn,
                  bookName : this.bookList[i].bookName,
                  deadTime : this.bookList[i].deadTime,
                  lendTime : this.bookList[i].lendTime,
                };
                this.numOfOutDataBook = this.numOfOutDataBook + 1;
              }
            }
          })
        }
      })
    },
    save(mode){
      if( mode === "编辑" ){
        request.put("/book",this.book).then(res =>{
          console.log(res)
          if(res.code == '0'){
            ElMessage({
              message: '编辑书籍信息成功',
              type: 'success',
            })
          }
          else {
            ElMessage.error(res.msg)
          }
          this.load()
          this.dialogVisible2 = false
        })
      }
      else {
        this.book.borrowNum = 0
        this.book.status = "在库中"
        request.post("/book",this.book).then(res =>{
          console.log(res)
          if(res.code == '0'){
            ElMessage.success('上架书籍成功')
          }
          else {
            ElMessage.error(res.msg)
          }
          this.load()
          this.dialogVisible = false
        })
      }
    },
    lend(bookId,bookName){
      console.log(this.user)
      if (this.user.phone == null||this.user.phone === ""){
        ElMessage.error("借阅失败! 请先将个人信息补充完整")
        this.$router.push("/person")//跳转个人信息界面
        return;
      }
      if(this.number ===5){
        ElMessage.warning("您同时最多只能借阅5本书")
        return;
      }
      if(this.numOfOutDataBook !==0){
        ElMessage.warning("在您归还逾期书籍前不能再借阅书籍")
        return;
      }
      let lendRecord={}
      lendRecord.bookId = bookId
      lendRecord.bookName = bookName
      lendRecord.borrowerId = this.user.id
      lendRecord.borrowerName = this.user.username
      let nowTime=new Date();
      lendRecord.lendTime = moment(nowTime).format("yyyy-MM-DD HH:mm:ss");
      nowTime.setDate(nowTime.getDate()+30);
      lendRecord.deadTime = moment(nowTime).format("yyyy-MM-DD HH:mm:ss");
      lendRecord.status="借阅中"
      request.post("/lendRecord",lendRecord).then(res =>{
        console.log(res)
        if(res.code == '0' ){
          ElMessage.success("借阅成功")
        }
        else
          ElMessage.error(res.msg)
        this.load();
      })
    },
    handleDelete(id){
      request.delete("book/" + id ).then(res =>{
        console.log(res)
        if(res.code == '0' ){
          ElMessage.success("下架成功")
        }
        else
          ElMessage.error(res.msg)
        this.load()
      })
    },
    deleteBatch(){
      if (!this.ids.length) {
        ElMessage.warning("请选择至少一条数据！")
        return
      }
      //  一个小优化，直接发送这个数组，而不是一个一个的提交下架
      // request.post("/book/deleteBatch",this.ids).then(res =>{
      request.delete("/book",{
        params:{ids:this.ids},
        paramsSerializer: params => {
          return qs.stringify(params, { indices: false })
        }
      }).then(res =>{
        if(res.code == '0'){
          ElMessage.success("批量下架成功")
          this.load()
        }
        else {
          ElMessage.error(res.msg)
        }
      })
    },
    clear(){
      this.isbn = null
      this.name = null
      this.author = null
      this.load()
    },
    add(){
      this.dialogVisible= true
      this.book ={}
    },
    handleSelectionChange(val){
      this.ids = val.map(v =>v.isbn)
    },
    handleEdit(row){
      this.book = JSON.parse(JSON.stringify(row))
      this.dialogVisible2 = true
    },
    toLook(){
      this.dialogVisible3 =true;
    },
  },
  data() {
    return {
      book: {},
      isbn:null,
      name:null,
      author:null,
      total:10,
      currentPage:1,
      pageSize: 10,
      tableData: [],
      user:{},
      number:0,
      bookList:[],
      outDateBook:[],
      numOfOutDataBook: 0,
      ids:[],
      dialogVisible: false,
      dialogVisible2: false,
      dialogVisible3 : true,
    }
  },
}
</script>
