<script>
import {ElMessage} from "element-plus";
import moment from "moment/moment";
import request from "@/utils/request";
export default {
  methods:{
    borrow(bookId,bookName){
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
      let borrowRecord={}
      borrowRecord.bookId = bookId
      borrowRecord.bookName = bookName
      borrowRecord.borrowerId = this.user.id
      borrowRecord.borrowerName = this.user.username
      let nowTime=new Date();
      borrowRecord.borrowTime = moment(nowTime).format("yyyy-MM-DD HH:mm:ss");
      nowTime.setDate(nowTime.getDate()+30);
      borrowRecord.deadTime = moment(nowTime).format("yyyy-MM-DD HH:mm:ss");
      borrowRecord.status="borrowed"
      request.post("/borrowRecord",borrowRecord).then(res =>{
        console.log(res)
        if(res.code == '0' ){
          ElMessage.success("Borrow successfully")
        }
        else
          ElMessage.error(res.msg)
        this.load();
      })
    }
  }
}

</script>

<template>

</template>

<style scoped>

</style>