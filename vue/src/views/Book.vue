<style scoped>
@media screen {
  .barcode{
    text-align: center;
    margin-top: 20px;
  }
}
</style>

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
          <el-input style="width: 80%" v-model="book.isbn" disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="Book Barcode" v-if="entityMode==='Edit'">
          <el-input style="width: 80%" v-model="book.barcode"></el-input>
        </el-form-item>
        <el-form-item label="Location">
          <el-input style="width: 80%" v-model="book.location"></el-input>
        </el-form-item>
        <el-form-item label="Add Number" v-if="entityMode==='Add'">
          <el-input-number style="width: 120px" v-model="addNum" @change="generateBarcode"></el-input-number>
        </el-form-item>
      </el-form>
      <div class="barcode" v-if="entityMode==='Add'&&barcodes.length>0">
        Auto Generated Barcodes
      </div>
      <div class="print-area">
        <div class="barcode" v-for="barcode in barcodes">
          <img :src="`https://barcode.tec-it.com/barcode.ashx?data=${barcode}&code=Code11`"/>
        </div>
      </div>
      <div class="barcode" v-if="barcodes.length>0">
        <a href='https://www.tec-it.com' title='Barcode Software by TEC-IT' target='_blank'>
          TEC-IT Barcode Generator<br/>
        </a>
      </div>
      <div class="barcode" v-if="barcodes.length>0">
        <el-button type="success" @click="printBarcode">Pirnt Barcode</el-button>
      </div>
      <template #footer>
      <span class="dialog-footer">
        <el-button @click="entityDialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="submitEntity()">Submit</el-button>
      </span>
      </template>
    </el-dialog>

    <el-dialog
        v-model="warnDialogVisible"
        title="Overdue and Fine"
        width="500px"
    >
      <span>
        You have <span :style="overdueNum>0?{color:'red'}:{}">{{overdueNum}}</span> records past the return time,<br/>
        and have a fine of <span :style="user.debt>0?{color:'red'}:{}">{{user.debt}}</span> dollars.<br/>
        please return books and pay fine off as soon as possible.<br/>
        You cannot borrow any book before that.
      </span>
      <template #footer>
        <span class="dialog-footer">
          <el-button type="danger" @click="$router.push('/borrowingBook')">Go to return</el-button>
          <el-button type="danger" @click="$router.push('/userInfo')">Go to pay</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import request from "../utils/request";
import {ElMessage, ElMessageBox} from "element-plus";
import qs from "qs";
export default {
  props:["user","loaded"],
  name: 'Book',
  created(){
    this.loaded.then(()=>this.load())
  },
  methods: {
    load(){
      if(this.user.role === 2){
        request.get("/borrowRecord",{
          params:{
            borrowerId: this.user.id,
            status:"borrowing"
          }
        }).then(res =>{
          console.log(res)
          let records = res.data.records
          let nowDate = new Date();
          this.overdueNum=records
              .filter(r=> new Date(r.deadTime)<nowDate)
              .map(()=>1)
              .reduce((s,i)=>s+i,0)
          console.log(this.overdueNum)
          if (this.overdueNum>0 || this.user.debt > 0) {
            this.warnDialogVisible=true
            // ElMessageBox.alert(`
            // You have ${overdueNum} records past the return time,<br/>
            // and have a debt of ${debt} dollars.<br/>
            // please return books and pay debt off as soon as possible.<br/>
            // You cannot borrow any book before that.`,
            //     'Warning', {
            //       confirmButtonText: 'Go to return and pay',
            //       dangerouslyUseHTMLString: true
            //     })
            //     .then(()=>{
            //       if(overdueNum>0)
            //         this.$router.push("/borrowingBook")
            //       else
            //         this.$router.push("/userInfo")
            //     })
          }
        })
      }
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
        let requests = this.barcodes.map(barcode=>{
          this.book.barcode=barcode
          return request.post("/book",this.book).then(res =>{
            if(res.code!='0')
              ElMessage.error(res.msg)
          })
        })
        Promise.all(requests).then(()=>{
          ElMessage.success('Add successfully')
          this.entityDialogVisible = false
          this.load()
        })
      }
    },
    addEntity(booInfo){
      this.book ={}
      this.book.isbn=booInfo.isbn
      this.book.status='in library'
      this.book.location='not on shelf'
      this.addNum=0
      this.barcodes=[]
      this.entityDialogVisible= true
      this.entityMode="Add"
    },
    editEntity(row){
      this.book = JSON.parse(JSON.stringify(row))
      this.addNum=0
      this.barcodes=[]
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
    generateBarcode(){
      request.get("book/barcode/max" ).then(res =>{
        console.log(res)
        if(res.code != '0' ){
          ElMessage.error(res.msg)
          return
        }
        let start=Number(res.data)
        this.barcodes=[]
        for(let i=1;i<=this.addNum;i++){
          this.barcodes.push(String(start+i).padStart(8,'0'))
        }
      })
    },
    printBarcode(){
      window.print();
    }
  },
  data() {
    return {
      bookInfoList:[],
      bookInfo: {},
      book:{},
      isbn:null,
      name:null,
      author:null,
      total:10,
      pageNum:1,
      pageSize: 10,
      overdueNum:0,
      records:[],
      outDateBook:[],
      numOfOutDataBook: 0,
      ids:[],
      infoDialogVisible: false,
      entityDialogVisible: false,
      warnDialogVisible : false,
      infoMode: 'Add',
      originIsbn:null,
      entityMode: 'Add',
      originBarcode:null,
      addNum:0,
      barcodes:[],
    }
  },
}
</script>
