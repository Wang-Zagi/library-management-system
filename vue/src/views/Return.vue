<template>
  <div>
    <div style="margin-top: 10px;margin-bottom: 30px">
      <el-input v-model="barcode" placeholder="Please scan the barcode of book or enter"
                clearable style="width: 400px" size="large" ref="barcodeInput">
      </el-input>
      <el-button type="primary" style="margin-left: 20px" @click="add" size="medium">Add</el-button>
      <el-button type="success" style="margin-left: 20px" @click="return_" size="medium">Return All</el-button>
    </div>

    <el-table :data="recordList" stripe :border="true"
              :max-height="508"
              :row-style="{height:'6.4vh'}"
              :cell-style="{padding:0}">
      <el-table-column prop="bookBarcode" label="Book Barcode" min-width="10"/>
      <el-table-column prop="bookName" label="Book Name" min-width="15"/>
      <el-table-column prop="borrowTime" label="Borrow Time" min-width="10"/>
      <el-table-column prop="deadTime" label="Latest Return Time" min-width="10"/>
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
      recordList:[],
      barcode:null,
      user:{},
    }
  },
  methods: {
    add() {
      if (this.barcode == null || this.barcode === "") {
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
      request.get("/borrowRecord", {
        params: {
          bookBarcode: this.barcode,
          borrowerId: this.user.id,
          status: "borrowing"
        }
      }).then(res => {
        if (res.code == '0') {
          if (res.data.records.length === 0)
            ElMessage.warning("You hasn't borrow this book.")
          else
            this.recordList.push(res.data.records[0])
          this.barcode = null
        } else
          ElMessage.error(res.msg)
      })
    },
    remove(barcode) {
      this.recordList = this.recordList.filter(item => item.barcode !== barcode)
    },
    return_() {
      if (this.recordList.length === 0) {
        ElMessage.warning("Please add a book first.")
        return;
      }
      let success = true
      this.recordList.forEach(record => {
        record.status = "confirming"
        record.returnTime = moment(new Date()).format("yyyy-MM-DD HH:mm:ss")
        request.put("borrowRecord", record).then(res => {
          console.log(res)
          if (res.code != '0') {
            success = false
            ElMessage.error(res.msg)
          }
        })
      })
      if (success) {
        ElMessage.success("Return successfully")
        this.recordList = []
        this.getBorrowingTotal()
      }
    },
  },
  created(){
    let userJson = sessionStorage.getItem("user")
    if(userJson)
      this.user = JSON.parse(userJson)
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