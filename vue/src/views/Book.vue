<template>
  <div style="max-width: 800px;margin: 5px auto 15px" v-if="user.role === 0">
    <el-alert center="true" type="warning" :closable="false" show-icon="true" style="font-size: 18px"
              title="If you want to borrow books, Please login"/>
  </div>
  <!-- Search -->
  <div style="margin: 5px 0;">
    <el-form :inline="true" size="small">
      <el-form-item label="Book ISBN" >
        <el-input v-model="isbn" placeholder="Please enter" clearable style="width: 200px">
          <template #prefix><el-icon class="el-input__icon"><search/></el-icon></template>
        </el-input>
      </el-form-item >
      <el-form-item label="Book Name" >
        <el-input v-model="name" placeholder="Please enter" clearable style="width: 200px">
          <template #prefix><el-icon class="el-input__icon"><search /></el-icon></template>
        </el-input>
      </el-form-item >
      <el-form-item label="Author" >
        <el-input v-model="author" placeholder="Please enter" clearable style="width: 200px">
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
        <el-button type="primary" @click="addInfo">Add</el-button>
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
  <el-table :data="bookInfoList" stripe :border="true"
            :max-height="user.role==0?464:user.role==1?517:508"
            :row-style="{height:user.role==0?'5.8vh':'6.4vh'}"
            :cell-style="{padding:0}"
            @selection-change="handleSelectionChange">
    <el-table-column type="selection" width="40" v-if="user.role ===1"/>
    <el-table-column prop="isbn" label="Book ISBN" min-width="140px" fixed="left"/>
    <el-table-column prop="name" label="Book Name" min-width="200px"/>
    <el-table-column prop="price" label="Price" min-width="60px"/>
    <el-table-column prop="author" label="Author" min-width="140px"/>
    <el-table-column prop="publisher" label="Publisher" min-width="100px"/>
    <el-table-column prop="publishTime" label="Publish Time" min-width="100px"/>
    <el-table-column label="Entities" type="expand" width="80px" fixed="right">
      <template #default="props">
        <el-table :data="props.row.books"
                  :row-style="{height:'5vh'}"
                  style="width:100%;"
                  border>
          <el-table-column label="Index" align="center" min-width="5">
            <template #default="scope" >
              <span>{{scope.$index+1}}</span>
            </template>
          </el-table-column>
          <el-table-column prop="barcode" label="Barcode" min-width="10"></el-table-column>
          <el-table-column prop="location" label="Location" min-width="15"></el-table-column>
          <el-table-column prop="status" label="Status" align="center" min-width="5">
            <template v-slot="scope">
              <el-tag :type="scope.row.status==='in library'?'success':'warning'">{{scope.row.status}}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="Operation" min-width="10" v-if="user.role === 1">
            <template v-slot="scope">
              <el-button type="info" size="mini" @click ="editEntity(scope.row)">Edit</el-button>
              <el-popconfirm title="Confirm to delete?" @confirm="deleteEntity(scope.row.barcode)">
                <template #reference>
                  <el-button type="danger" size="mini" >Delete</el-button>
                </template>
              </el-popconfirm>
            </template>
          </el-table-column>
        </el-table>
      </template>
    </el-table-column>
    <el-table-column label="Operation" min-width="250px" fixed="right" v-if="user.role === 1">
      <template v-slot="scope">
        <el-button type="info" size="mini" @click ="editInfo(scope.row)">Edit</el-button>
        <el-popconfirm title="Confirm to delete?" @confirm="deleteInfo(scope.row.isbn)">
          <template #reference>
            <el-button type="danger" size="mini" >Delete</el-button>
          </template>
        </el-popconfirm>
        <el-button type="primary" size="mini" @click ="addEntity(scope.row)">Add Entity</el-button>
      </template>
    </el-table-column>
  </el-table>

  <!-- Pagination -->
  <div style="margin: 10px 0">
    <el-pagination
        v-model:currentPage="pageNum"
        v-model:page-size="pageSize"
        :page-sizes="[5, 10, 15]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="load"
        @current-change="load"
    >
    </el-pagination>

    <el-dialog v-model="infoDialogVisible" :title="infoMode+'BookInfo'" width="30%">
      <el-form :model="bookInfo" label-width="120px">
        <el-form-item label="Book ISBN">
          <el-input style="width: 80%" v-model="bookInfo.isbn"></el-input>
          <el-button type="primary" @click="getInfo">Auto fill in information from isbn api</el-button>
        </el-form-item>
        <el-form-item label="Book Name">
          <el-input style="width: 80%" v-model="bookInfo.name"></el-input>
        </el-form-item>
        <el-form-item label="Price">
          <el-input style="width: 80%" v-model="bookInfo.price"></el-input>
        </el-form-item>
        <el-form-item label="Author">
          <el-input style="width: 80%" v-model="bookInfo.author"></el-input>
        </el-form-item>
        <el-form-item label="Publisher">
          <el-input style="width: 80%" v-model="bookInfo.publisher"></el-input>
        </el-form-item>
        <el-form-item label="Publish Time">
          <div>
            <el-date-picker value-format="YYYY-MM-DD" type="date" style="width: 80%" clearable v-model="bookInfo.publishTime" ></el-date-picker>
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
      <span class="dialog-footer">
        <el-button @click="infoDialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="submitInfo()">Submit</el-button>
      </span>
      </template>
    </el-dialog>

    <el-dialog v-model="entityDialogVisible" :title="entityMode+'Book'" width="30%">
      <el-form :model="book" label-width="120px">
        <el-form-item label="Book ISBN">
          <el-input style="width: 80%" v-model="book.isbn"></el-input>
        </el-form-item>
        <el-form-item label="Book Barcode">
          <el-input style="width: 80%" v-model="book.barcode"></el-input>
        </el-form-item>
        <el-form-item label="Location">
          <el-input style="width: 80%" v-model="book.location"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
      <span class="dialog-footer">
        <el-button @click="entityDialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="submitEntity()">Submit</el-button>
      </span>
      </template>
    </el-dialog>

    <el-dialog
        v-model="dialogVisible3"
        v-if="numOfOutDataBook!==0"
        title="Overdue Details"
        width="50%"
    >
      <el-table :data="outDateBook" style="width: 100%">
        <el-table-column prop="isbn" label="Book ISBN" />
        <el-table-column prop="bookName" label="Book Name" />
        <el-table-column prop="borrowTime" label="Borrowing Date" />
        <el-table-column prop="deadTime" label="Deadline" />
      </el-table>

      <template #footer>
      <span class="dialog-footer">
        <el-button type="primary" @click="dialogVisible3 = false"
        >Confirm</el-button>
      </span>
      </template>
    </el-dialog>
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
      request.get("/bookInfo/total",{
        params:{
          isbn: this.isbn,
          name: this.name,
          author: this.author,
        }
      }).then(res => {
        console.log(res)
        this.total = res.data
      })
      request.get("/bookInfo",{
        params:{
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          isbn: this.isbn,
          name: this.name,
          author: this.author,
        }
      }).then(res =>{
        console.log(res)
        this.bookInfoList = res.data

        // if(this.user.role === 2){
        //   request.get("/borrowRecord",{
        //     params:{
        //       pageNum: 1,
        //       pageSize: this.total,
        //       borrowerId: this.user.id,
        //       status:"borrowed"
        //     }
        //   }).then(res =>{
        //     console.log(res)
        //     this.records = res.data.records
        //     this.number = this.records.length;
        //     var nowDate = new Date();
        //     for(let i=0; i< this.number; i++){
        //       let dDate = new Date(this.records[i].deadTime);
        //       if(dDate < nowDate){
        //         this.outDateBook[this.numOfOutDataBook] = {
        //           isbn:this.records[i].isbn,
        //           bookName : this.records[i].bookName,
        //           deadTime : this.records[i].deadTime,
        //           borrowTime : this.records[i].borrowTime,
        //         };
        //         this.numOfOutDataBook = this.numOfOutDataBook + 1;
        //       }
        //     }
        //   })
        // }
      })
    },
    clear(){
      this.isbn = null
      this.name = null
      this.author = null
      this.load()
    },
    submitInfo(){
      if( this.infoMode === "Edit" ){
        request.put("/bookInfo/"+this.originIsbn,this.bookInfo).then(res =>{
          console.log(res)
          if(res.code == '0'){
            ElMessage.success('Edit successfully')
            this.load()
            this.infoDialogVisible = false
          }
          else
            ElMessage.error(res.msg)
        })
      }
      else {
        request.post("/bookInfo",this.bookInfo).then(res =>{
          console.log(res)
          if(res.code == '0'){
            ElMessage.success('Add successfully')
            this.load()
            this.infoDialogVisible = false
          }
          else
            ElMessage.error(res.msg)
        })
      }
    },
    deleteInfo(id){
      request.delete("bookInfo/" + id ).then(res =>{
        console.log(res)
        if(res.code == '0' )
          ElMessage.success("Delete successfully")
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
      request.delete("/bookInfo",{
        params:{ids:this.ids},
        paramsSerializer: params => {
          return qs.stringify(params, { indices: false })
        }
      }).then(res =>{
        if(res.code == '0'){
          ElMessage.success("Batch Delete successful")
          this.load()
        }
        else
          ElMessage.error(res.msg)
      })
    },
    addInfo(){
      this.bookInfo ={}
      this.infoDialogVisible= true
      this.infoMode="Add"
    },
    editInfo(row){
      this.bookInfo = JSON.parse(JSON.stringify(row))
      this.originIsbn = row.isbn
      this.infoDialogVisible = true
      this.infoMode="Edit"
    },
    getInfo(){
      //TODO 调用ISBN API接口自动填充信息（待更换稳定接口）
      request.get('http://data.isbn.work/openApi/getInfoByIsbn?appKey=ae1718d4587744b0b79f940fbef69e77&isbn='+this.bookInfo.isbn)
          .then(resp=>{
            console.log(resp.data)
            if(resp.code!='0'){
              ElMessage.error(resp.msg)
              return
            }
            this.bookInfo.name = resp.data.bookName
            this.bookInfo.author= resp.data.author
            this.bookInfo.price= resp.data.price/100
            this.bookInfo.publisher=resp.data.press
            this.bookInfo.publishTime=resp.data.pressDate+"-01"
          });
    },
    submitEntity(){
      if( this.entityMode === "Edit" ){
        request.put("/book/"+this.originBarcode,this.book).then(res =>{
          console.log(res)
          if(res.code == '0'){
            ElMessage.success('Edit successfully')
            this.load()
            this.entityDialogVisible = false
          }
          else
            ElMessage.error(res.msg)
        })
      }
      else {
        request.post("/book",this.book).then(res =>{
          console.log(res)
          if(res.code == '0'){
            ElMessage.success('Add successfully')
            this.load()
            this.entityDialogVisible = false
          }
          else
            ElMessage.error(res.msg)
        })
      }
    },
    addEntity(booInfo){
      this.book ={}
      this.book.isbn=booInfo.isbn
      this.book.status='in library'
      this.entityDialogVisible= true
      this.entityMode="Add"
    },
    editEntity(row){
      this.book = JSON.parse(JSON.stringify(row))
      this.originBarcode = row.barcode
      this.entityDialogVisible = true
      this.entityMode="Edit"
    },
    deleteEntity(id){
      request.delete("book/" + id ).then(res =>{
        console.log(res)
        if(res.code == '0' )
          ElMessage.success("Delete successfully")
        else
          ElMessage.error(res.msg)
        this.load()
      })
    },
    handleSelectionChange(val){
      this.ids = val.map(v =>v.isbn)
    },
    toLook(){
      this.dialogVisible3 =true;
    },
  },
  data() {
    return {
      bookInfoList:[],
      bookInfo: {},
      book: {},
      isbn:null,
      name:null,
      author:null,
      total:10,
      pageNum:1,
      pageSize: 10,
      user:{role:0},
      number:0,
      records:[],
      outDateBook:[],
      numOfOutDataBook: 0,
      ids:[],
      infoDialogVisible: false,
      entityDialogVisible: false,
      dialogVisible3 : true,
      infoMode: 'Add',
      originIsbn:null,
      entityMode: 'Add',
      originBarcode:null,
    }
  },
}
</script>
