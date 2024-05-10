<template>
  <div>
    <div style="margin-top: 10px;margin-bottom: 30px">
      <el-input v-model="barcode" placeholder="Please scan the barcode of book or enter"
                clearable style="width: 400px" size="large" ref="barcodeInput">
      </el-input>
      <el-button type="primary" style="margin-left: 20px" @click="add" size="medium">Add</el-button>
      <el-button type="success" style="margin-left: 20px" @click="borrow" size="medium">Borrow All</el-button>
    </div>

    <el-table :data="bookList" stripe :border="true"
              :max-height="508"
              :row-style="{height:'6.4vh'}"
              :cell-style="{padding:0}">
      <el-table-column prop="barcode" label="Book Barcode" min-width="10"/>
      <el-table-column prop="isbn" label="Book ISBN" min-width="10"/>
      <el-table-column prop="name" label="Book Name" min-width="15"/>
      <el-table-column label="Operation" min-width="5">
        <template v-slot="scope">
          <el-button type="danger" size="mini" @click="remove(scope.row.barcode)">Remove</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import {ElMessage} from "element-plus";
import moment from "moment/moment";
import request from "@/utils/request";
export default {
  data() {
    return{
      bookList:[],
      barcode:null,
      user:{},
    }
  },
  methods:{
    add(){
      if(this.barcode == null || this.barcode === ""){
        ElMessage.warning("Please enter the barcode of the book.")
        return;
      }
      for(let i in this.bookList){
        if(this.bookList[i].barcode === this.barcode){
          ElMessage.warning("The book has been added.")
          this.barcode = null
          return;
        }
      }
      request.get("/book/"+this.barcode).then(res =>{
        if(res.code == '0'){
          if(res.data.status!=='in library')
            ElMessage.warning("The book has been borrowed.")
          else
            this.bookList.push(res.data)
          this.barcode = null
        }
        else
          ElMessage.error(res.msg)
      })
    },
    remove(barcode){
      this.bookList=this.bookList.filter(item => item.barcode !== barcode)
    },
    borrow(){
      if(this.bookList.length===0){
        ElMessage.warning("Please add a book first.")
        return;
      }
      if(this.bookList.length+this.user.borrowingTotal>5){
        ElMessage.warning("The total number of books you borrow at the same time cannot exceed 5.")
        return;
      }
      let borrowRecord={
        borrowerId: this.user.id,
        borrowerName: this.user.username,
        status:"borrowing"
      }
      let nowTime=new Date();
      borrowRecord.borrowTime = moment(nowTime).format("yyyy-MM-DD HH:mm:ss");
      nowTime.setDate(nowTime.getDate()+30);
      borrowRecord.deadTime = moment(nowTime).format("yyyy-MM-DD HH:mm:ss");

      let success=true
      this.bookList.forEach(book =>{
        borrowRecord.bookBarcode = book.barcode;
        borrowRecord.bookName = book.name;
        request.post("/borrowRecord",borrowRecord).then(res =>{
          console.log(res)
          if(res.code != '0' ){
            ElMessage.error(res.msg)
            success=false
          }
        })
      })
      if(success) {
        ElMessage.success("Borrow successfully")
        this.bookList=[]
        this.getBorrowingTotal()
      }
    },
    getBorrowingTotal(){
      request.get("/borrowRecord/total",{
        params:{
          borrowerId: this.user.id,
        }
      }).then(res =>{
        console.log(res)
        this.user.borrowingTotal = res.data
      })
    }
  },
  created(){
    let userJson = sessionStorage.getItem("user")
    if(userJson)
      this.user = JSON.parse(userJson)
    this.getBorrowingTotal()
  },
  mounted() {
    this.$refs.barcodeInput.input.focus()
    this.$refs.barcodeInput.input.addEventListener('keypress', event=> {
      if (event.keyCode === 13) {  // 检测键码是否为13（回车键）
        this.add();
      }
    });
  }
}
</script>