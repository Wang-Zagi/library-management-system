<template>
  <div>
    <div id="myTimer" style="margin-left: 40px; font-weight: 550;font-size: 32px;margin-bottom: 20px"></div>
    <el-row :gutter="20">
      <el-col :span="6" v-for="item in cards" :key="item.title">
        <el-card class="box-card">
          <div class="clearfix">{{ item.title }}</div>
          <div class="text item">
            <svg class="icon" aria-hidden="true">
              <use :xlink:href="item.icon" style="width: 100px"></use>
            </svg>
            <span class="text">{{ item.data }}</span>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-select
        v-model="mode"
        placeholder="Day"
        style="width: 200px;margin: 10px 0px 20px 40px"
        @change="switchMode"
    >
      <template #prefix>
        <span style="color: black;padding: 10px">Count By: </span>
      </template>
      <el-option label="Day" value="day"/>
      <el-option label="Week" value="week"/>
      <el-option label="Month" value="month"/>
    </el-select>
    <!-- Prepare a DOM element with a size (width and height) for ECharts -->
    <div class="chart-container">
      <div id="chart1" class="chart"></div>
      <div style="display: inline-block;width: 50px"></div>
      <div id="chart2" class="chart"></div>
    </div>
  </div>
</template>

<script>
import * as echarts from "echarts";
import { ElMessage } from "element-plus";
import request from "../utils/request";
import router from "@/router";

export default {
  data() {
    return {
      cards: [
        { title: "Books in Library", data: 100, icon: "#iconbook-pro" },
        { title: "Number of Users", data: 1000, icon: "#iconpopulation" },
        { title: "Total borrowing", data: 100, icon: "#iconvisit" },
        { title: "Total Income", data: 100, icon: "#iconlend-record-pro" },
      ],
      data: {},
      borrowData: {},
      incomeData: {},
      mode:'day'
    };
  },
  created() {
    let userJson = sessionStorage.getItem("user");
    if (!userJson) {
      router.push("/login");
    }
  },
  mounted() {
    console.log("Mounted hook executed"); // Add this line for debug

    this.circleTimer();

    request.get("/dashboard").then((res) => {
      console.log(res);
      this.cards[0].data = res.data.bookCount;
      this.cards[1].data = res.data.userCount;
      this.cards[2].data = res.data.borrowRecordCount;
      this.cards[3].data = "$"+res.data.revenue;
    });

    request.get("/dashboard/borrowRecord").then((res) => {
      console.log(res);
      this.borrowData = res.data;

      this.switchBorrowData("day");
    });

    request.get("/dashboard/income").then((res) => {
      console.log(res);
      this.incomeData = res.data;

      this.switchIncome("day");
    });
  },

  methods: {
    circleTimer() {
      this.getTimer();
      setInterval(() => {
        this.getTimer();
      }, 1000);
    },
    getTimer() {
      var d = new Date();
      var t = d.toLocaleString();
      document.getElementById("myTimer").innerHTML = t;
    },
    switchMode(type){
      console.log(type)
      this.switchBorrowData(type)
      this.switchIncome(type)
    },
    switchBorrowData(type) {
      var today = new Date();
      today.setHours(24, 0, 0, 0); // 设置时间为当天的开始
      var oneDayMillis = 24 * 60 * 60 * 1000;
      var oneWeekMillis = 7 * oneDayMillis;

      var filteredByDate = {};
      var filteredByWeek = {};
      var filteredByMonth = {};

      // 解析this.borrowData并填充filteredByDate, filteredByWeek, filteredByMonth
      console.log(this.borrowData)

      var startDate = new Date(today.getTime() - 6 * oneDayMillis);
      for (
        var d = new Date(startDate);
        d <= today;
        d.setDate(d.getDate() + 1)
      ) {
        var dateString = d.toISOString().split("T")[0];
        filteredByDate[dateString] = this.borrowData[dateString] || 0;
      }

      var startWeekDate = new Date(today.getTime() - 3 * oneWeekMillis);
      for (
        var wd = new Date(startWeekDate);
        wd <= today;
        wd.setDate(wd.getDate() + 7)
      ) {
        var weekString = `${wd.getFullYear()}-W${this.getWeekNumber(wd)}`;
        filteredByWeek[weekString] = 0;
      }
      for (var date in this.borrowData) {
        var borrowDate = new Date(date);
        if (borrowDate >= startWeekDate && borrowDate <= today) {
          var weekString = `${borrowDate.getFullYear()}-W${this.getWeekNumber(
            borrowDate
          )}`;
          filteredByWeek[weekString] += this.borrowData[date];
        }
      }

      var startMonthDate = new Date(today);
      startMonthDate.setMonth(startMonthDate.getMonth() - 2);
      startMonthDate.setDate(1);
      for (
        var md = new Date(startMonthDate);
        md <= today;
        md.setMonth(md.getMonth() + 1)
      ) {
        var monthString = `${md.getFullYear()}-${md.getMonth() + 1}`;
        filteredByMonth[monthString] = 0;
      }
      for (var date in this.borrowData) {
        var borrowDate = new Date(date);
        if (borrowDate >= startMonthDate && borrowDate <= today) {
          var monthString = `${borrowDate.getFullYear()}-${
            borrowDate.getMonth() + 1
          }`;
          filteredByMonth[monthString] += this.borrowData[date];
        }
      }

      var dates, values;
      switch (type) {
        case "day":
          dates = Object.keys(filteredByDate);
          values = Object.values(filteredByDate);
          break;
        case "week":
          dates = Object.keys(filteredByWeek);
          values = Object.values(filteredByWeek);
          break;
        case "month":
          dates = Object.keys(filteredByMonth);
          values = Object.values(filteredByMonth);
          break;
      }

      var options = {
        title: {
          text: "Borrow Record",
        },
        tooltip: {},
        xAxis: {
          type: "category",
          data: dates,
        },
        yAxis: {
          type: "value",
        },
        series: [
          {
            name: "Borrow Count",
            type: "line",
            data: values,
            label: {
              show: true,
              position: 'top'
            }
          },
        ],
      };

      var borrowRecordChart = echarts.init(document.getElementById("chart1"));
      borrowRecordChart.setOption(options);
    },
    switchIncome(type) {
      var today = new Date();
      today.setHours(24, 0, 0, 0); // 设置时间为当天的开始
      var oneDayMillis = 24 * 60 * 60 * 1000;
      var oneWeekMillis = 7 * oneDayMillis;

      var filteredByDate = {};
      var filteredByWeek = {};
      var filteredByMonth = {};
      console.log(this.incomeData)
      // 解析this.borrowData并填充filteredByDate, filteredByWeek, filteredByMonth
      // for (var dateStr in this.incomeData) {
      //   var borrowDate = new Date(dateStr);
      //   if (!isNaN(borrowDate)) {
      //     var dateKey = borrowDate.toISOString().split("T")[0];
      //     if (!filteredByDate[dateKey]) filteredByDate[dateKey] = 0;
      //     filteredByDate[dateKey] += this.revenueData[dateStr];
      //   }
      // }

      var startDate = new Date(today.getTime() - 6 * oneDayMillis);
      for (
        var d = new Date(startDate);
        d <= today;
        d.setDate(d.getDate() + 1)
      ) {
        var dateString = d.toISOString().split("T")[0];
        filteredByDate[dateString] = this.incomeData[dateString] || 0;
      }

      var startWeekDate = new Date(today.getTime() - 3 * oneWeekMillis);
      for (
        var wd = new Date(startWeekDate);
        wd <= today;
        wd.setDate(wd.getDate() + 7)
      ) {
        var weekString = `${wd.getFullYear()}-W${this.getWeekNumber(wd)}`;
        filteredByWeek[weekString] = 0;
      }
      for (var date in this.incomeData) {
        var borrowDate = new Date(date);
        if (borrowDate >= startWeekDate && borrowDate <= today) {
          var weekString = `${borrowDate.getFullYear()}-W${this.getWeekNumber(
            borrowDate
          )}`;
          filteredByWeek[weekString] += this.incomeData[date];
        }
      }

      var startMonthDate = new Date(today);
      startMonthDate.setMonth(startMonthDate.getMonth() - 2);
      startMonthDate.setDate(1);
      for (
        var md = new Date(startMonthDate);
        md <= today;
        md.setMonth(md.getMonth() + 1)
      ) {
        var monthString = `${md.getFullYear()}-${md.getMonth() + 1}`;
        filteredByMonth[monthString] = 0;
      }
      for (var date in this.incomeData) {
        var borrowDate = new Date(date);
        if (borrowDate >= startMonthDate && borrowDate <= today) {
          var monthString = `${borrowDate.getFullYear()}-${
            borrowDate.getMonth() + 1
          }`;
          filteredByMonth[monthString] += this.incomeData[date];
        }
      }

      var dates, values;
      switch (type) {
        case "day":
          dates = Object.keys(filteredByDate);
          values = Object.values(filteredByDate);
          break;
        case "week":
          dates = Object.keys(filteredByWeek);
          values = Object.values(filteredByWeek);
          break;
        case "month":
          dates = Object.keys(filteredByMonth);
          values = Object.values(filteredByMonth);
          break;
      }

      var options = {
        title: {
          text: "Income Count",
        },
        tooltip: {},
        xAxis: {
          type: "category",
          data: dates,
        },
        yAxis: {
          type: "value",
        },
        series: [
          {
            name: "Income Count",
            type: "bar",
            data: values,
            label: {
              show: true,
              position: 'top'
            }
          },
        ],
      };

      var borrowRecordChart = echarts.init(document.getElementById("chart2"));
      borrowRecordChart.setOption(options);
    },
    getWeekNumber(d) {
      d = new Date(Date.UTC(d.getFullYear(), d.getMonth(), d.getDate()));
      d.setUTCDate(d.getUTCDate() + 4 - (d.getUTCDay() || 7));
      var yearStart = new Date(Date.UTC(d.getUTCFullYear(), 0, 1));
      var weekNo = Math.ceil(((d - yearStart) / 86400000 + 1) / 7);
      return weekNo;
    },
  },
};
</script>

<style scoped>
.box-card {
  width: 200px;
  height: 120px;
  margin-bottom: 25px;
  margin-left: auto;
  margin-right: auto;
}

.clearfix {
  text-align: center;
  font-size: 15px;
}

.text {
  text-align: center;
  font-size: 24px;
  font-weight: 700;
  vertical-align: super;
}

#main {
  width: 100%;
  height: 450px;
  margin-top: 20px;
}

.icon {
  width: 50px;
  height: 50px;
  padding-top: 5px;
  padding-right: 10px;
}

.chart-container {
  text-align: center;
}
.chart {
  width: 45%;
  height: 380px;
  display: inline-block;
}
.buttons {
  margin-bottom: 20px;
}
.buttons button {
  margin-right: 10px;
}
</style>
