<template>
  <div class="home" style ="padding: 10px">
    <div style="max-width: 800px;margin: 5px auto 15px" v-if="user.role === 0">
      <el-alert center="true" type="warning" :closable="false" show-icon="true" style="font-size: 18px"
                title="If you want to borrow books, Please login"/>
    </div>
    <!-- Search -->
    <div style="margin: 5px 0;">
      <el-form :inline="true" size="small">
        <el-form-item label="Book ISBN" >
          <el-input v-model="isbn" placeholder="Please input book ISBN" clearable>
            <template #prefix><el-icon class="el-input__icon"><search/></el-icon></template>
          </el-input>
        </el-form-item >
        <el-form-item label="Book Name" >
          <el-input v-model="name" placeholder="Please input book name" clearable>
            <template #prefix><el-icon class="el-input__icon"><search /></el-icon></template>
          </el-input>
        </el-form-item >
        <el-form-item label="Author" >
          <el-input v-model="author" placeholder="Please input book author" clearable>
            <template #prefix><el-icon class="el-input__icon"><search /></el-icon></template>
          </el-input>
        </el-form-item >
        <el-form-item>
          <el-button type="primary" style="margin-left: 1%" @click="load" size="mini" >
            <svg-icon iconClass="search"/>Search</el-button>
        </el-form-item>
        <el-form-item>
          <el-button size="mini"  type="danger" @click="clear">Clear</el-button>
        </el-form-item>
        <el-form-item style="margin-left: 20px" v-if="user.role === 1">
          <el-button type="primary" @click = "add">Add</el-button>
          <el-popconfirm title="Confirm to delete?" @confirm="deleteBatch" v-if="user.role === 1">
            <template #reference>
              <el-button type="danger">Batch delete</el-button>
            </template>
          </el-popconfirm>
        </el-form-item>
        <el-form-item style="float: right" v-if="numOfOutDataBook!==0">
          <el-popconfirm
              confirm-button-text="Look up"
              cancel-button-text="Cancel"
              :icon="InfoFilled"
              icon-color="red"
              title="Our books are overdue. Please return them as soon as possible."
              @confirm="toLook"
          >
            <template #reference>
              <el-button  type="warning">Overdue notice</el-button>
            </template>
          </el-popconfirm>
        </el-form-item>
      </el-form>
    </div>

    <!-- Data Fields -->
    <el-table :data="tableData" stripe :border="true" @selection-change="handleSelectionChange">
      <el-table-column v-if="user.role ===1"
                       type="selection"
                       width="55">
      </el-table-column>
      <el-table-column prop="isbn" label="Book ISBN" min-width="10"/>
      <el-table-column prop="name" label="Book Name" min-width="16"/>
      <el-table-column prop="price" label="Price" min-width="4"/>
      <el-table-column prop="author" label="Author" min-width="10"/>
      <el-table-column prop="publisher" label="Publisher" min-width="6"/>
      <el-table-column prop="publishTime" label="Publish Time" min-width="8"/>
      <el-table-column prop="borrowNum" label="Borrow Times" min-width="8"/>
      <el-table-column prop="status" label="Status" min-width="6">
        <template v-slot="scope">
          <el-tag :type="scope.row.status==='in library'?'success':'warning'">{{scope.row.status}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column fixed="right" label="Operation" min-width="12">
        <template v-slot="scope">
          <el-button  size="mini" @click ="handleEdit(scope.row)" v-if="user.role === 1">Edit</el-button>
          <el-popconfirm title="Confirm to delete?" @confirm="handleDelete(scope.row.isbn)" v-if="user.role === 1">
            <template #reference>
              <el-button type="danger" size="mini" >Delete</el-button>
            </template>
          </el-popconfirm>
          <el-button size="mini" @click ="lend(scope.row.isbn,scope.row.name)" v-if="user.role === 2" :disabled="scope.row.status === 'borrowed'">Borrow</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- Testing, Notification Dialog -->
    <el-dialog
        v-model="dialogVisible3"
        v-if="numOfOutDataBook!==0"
        title="Overdue Details"
        width="50%"
    >
        <el-table :data="outDateBook" style="width: 100%">
          <el-table-column prop="isbn" label="Book ISBN" />
          <el-table-column prop="bookName" label="Book Name" />
          <el-table-column prop="lendTime" label="Borrowing Date" />
          <el-table-column prop="deadTime" label="Deadline" />
        </el-table>

      <template #footer>
      <span class="dialog-footer">
        <el-button type="primary" @click="dialogVisible3 = false"
        >Confirm</el-button>
      </span>
      </template>
    </el-dialog>

    <!-- Pagination -->
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

      <el-dialog v-model="dialogVisible" title="Shelve Books" width="30%">
        <el-form :model="book" label-width="120px">

          <el-form-item label="Book ISBN">
            <el-input style="width: 80%" v-model="book.isbn"></el-input>
          </el-form-item>
          <el-form-item label="Book Name">
            <el-input style="width: 80%" v-model="book.name"></el-input>
          </el-form-item>
          <el-form-item label="Price">
            <el-input style="width: 80%" v-model="book.price"></el-input>
          </el-form-item>
          <el-form-item label="Author">
            <el-input style="width: 80%" v-model="book.author"></el-input>
          </el-form-item>
          <el-form-item label="Publisher">
            <el-input style="width: 80%" v-model="book.publisher"></el-input>
          </el-form-item>
          <el-form-item label="Publish Time">
            <div>
              <el-date-picker value-format="YYYY-MM-DD" type="date" style="width: 80%" clearable v-model="book.publishTime" ></el-date-picker>
            </div>
          </el-form-item>
        </el-form>
        <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="save('Shelve')">Confirm</el-button>
      </span>
        </template>
      </el-dialog>

      <el-dialog v-model="dialogVisible2" title="Edit Book Info" width="30%">
        <el-form :model="book" label-width="120px">

          <el-form-item label="Book ISBN" >
            <el-input style="width: 80%" v-model="book.isbn" disabled></el-input>
          </el-form-item>
          <el-form-item label="Book Name">
            <el-input style="width: 80%" v-model="book.name"></el-input>
          </el-form-item>
          <el-form-item label="Price">
            <el-input style="width: 80%" v-model="book.price"></el-input>
          </el-form-item>
          <el-form-item label="Author">
            <el-input style="width: 80%" v-model="book.author"></el-input>
          </el-form-item>
          <el-form-item label="Publisher">
            <el-input style="width: 80%" v-model="book.publisher"></el-input>
          </el-form-item>
          <el-form-item label="Publish Time">
            <div>
              <el-date-picker value-format="YYYY-MM-DD" type="date" style="width: 80%" clearable v-model="book.publishTime" ></el-date-picker>
            </div>
          </el-form-item>
        </el-form>
        <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible2 = false">Cancel</el-button>
        <el-button type="primary" @click="save('Edit')">Confirm</el-button>
      </span>
        </template>
      </el-dialog>
    </div>
  </div>
</template>

<script>
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
    if(userJson)
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
              status:"on loan"
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
      if( mode === "Edit" ){
        request.put("/book",this.book).then(res =>{
          console.log(res)
          if(res.code == '0'){
            ElMessage({
              message: 'Edit successfully',
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
        this.book.status = "in library"
        request.post("/book",this.book).then(res =>{
          console.log(res)
          if(res.code == '0'){
            ElMessage.success('Add successfully')
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
        ElMessage.error("Borrowing failed! Please complete your personal information first.")
        this.$router.push("/person")//跳转个人信息界面
        return;
      }
      if(this.number ===5){
        ElMessage.warning("You can borrow up to 5 books at a time.")
        return;
      }
      if(this.numOfOutDataBook !==0){
        ElMessage.warning("You cannot borrow books again until you return the overdue ones.")
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
      lendRecord.status="on loan"
      request.post("/lendRecord",lendRecord).then(res =>{
        console.log(res)
        if(res.code == '0' ){
          ElMessage.success("Borrow successfully")
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
          ElMessage.success("Delete successfully")
        }
        else
          ElMessage.error(res.msg)
        this.load()
      })
    },
    deleteBatch(){
      if (!this.ids.length) {
        ElMessage.warning("Please select at least one item.")
        return
      }
      request.delete("/book",{
        params:{ids:this.ids},
        paramsSerializer: params => {
          return qs.stringify(params, { indices: false })
        }
      }).then(res =>{
        if(res.code == '0'){
          ElMessage.success("Batch removal successful")
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
      user:{role:0},
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
