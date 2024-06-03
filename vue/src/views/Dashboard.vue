<template>
  <div>
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
    <div id="myTimer" style="margin-left: 15px; font-weight: 550"></div>
    <!-- Prepare a DOM element with a size (width and height) for ECharts -->
    <div class="chart-container">
      <div id="chart1" class="chart"></div>
      <button @click="switchBorrowData('day')">按日统计</button>
      <button @click="switchBorrowData('week')">按周统计</button>
      <button @click="switchBorrowData('month')">按月统计</button>
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
        { title: "Books in library", data: 100, icon: "#iconbook-pro" },
        { title: "Number of users", data: 1000, icon: "#iconpopulation" },
        { title: "Total borrowing", data: 100, icon: "#iconlend-record-pro" },
        { title: "Revenue", data: 100, icon: "#iconvisit" },
      ],
      data: {},
      borrowData: {},
      revenueData: {},
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
      this.cards[3].data = res.data.revenue;
    });

    request.get("/dashboard/borrowRecord").then((res) => {
      console.log(res);
      this.borrowData = res.data;
      const responseData = res.data;
      const dates = Object.keys(responseData);
      const values = Object.values(responseData);
      console.log(dates);
      console.log(values);

      var option = {
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
            type: "bar",
            data: values,
          },
        ],
      };

      var borrowRecordChart = echarts.init(document.getElementById("chart1"));
      borrowRecordChart.setOption(option);
      console.log("Borrow Record Chart Element: ", borrowRecordChart); // Add this line for debug
    });

    console.log("End of mounted hook"); // Add this line for debug
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
    switchBorrowData(type) {
      var today = new Date();
      today.setHours(0, 0, 0, 0); // 设置时间为当天的开始
      var oneDayMillis = 24 * 60 * 60 * 1000;
      var oneWeekMillis = 7 * oneDayMillis;

      var filteredByDate = {};
      var filteredByWeek = {};
      var filteredByMonth = {};

      // 解析this.borrowData并填充filteredByDate, filteredByWeek, filteredByMonth
      for (var dateStr in this.borrowData) {
        var borrowDate = new Date(dateStr);
        if (!isNaN(borrowDate)) {
          var dateKey = borrowDate.toISOString().split("T")[0];
          if (!filteredByDate[dateKey]) filteredByDate[dateKey] = 0;
          filteredByDate[dateKey] += this.borrowData[dateStr];
        }
      }

      var startDate = new Date(today.getTime() - 6 * oneDayMillis);
      for (
        var d = new Date(startDate);
        d <= today;
        d.setDate(d.getDate() + 1)
      ) {
        var dateString = d.toISOString().split("T")[0];
        filteredByDate[dateString] = filteredByDate[dateString] || 0;
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
      for (var date in filteredByDate) {
        var borrowDate = new Date(date);
        if (borrowDate >= startWeekDate && borrowDate <= today) {
          var weekString = `${borrowDate.getFullYear()}-W${this.getWeekNumber(
            borrowDate
          )}`;
          filteredByWeek[weekString] += filteredByDate[date];
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
      for (var date in filteredByDate) {
        var borrowDate = new Date(date);
        if (borrowDate >= startMonthDate && borrowDate <= today) {
          var monthString = `${borrowDate.getFullYear()}-${
            borrowDate.getMonth() + 1
          }`;
          filteredByMonth[monthString] += filteredByDate[date];
        }
      }

      var dates, values;
      switch (type) {
        case "day":
          dates = Object.keys(filteredByDate).reverse();
          values = Object.values(filteredByDate).reverse();
          break;
        case "week":
          dates = Object.keys(filteredByWeek).reverse();
          values = Object.values(filteredByWeek).reverse();
          break;
        case "month":
          dates = Object.keys(filteredByMonth).reverse();
          values = Object.values(filteredByMonth).reverse();
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
            type: "bar",
            data: values,
          },
        ],
      };

      var borrowRecordChart = echarts.init(document.getElementById("chart1"));
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
  display: flex;
  flex-direction: column;
  align-items: center;
}
.chart {
  width: 80%;
  height: 400px;
}
.buttons {
  margin-bottom: 20px;
}
.buttons button {
  margin-right: 10px;
}
</style>
