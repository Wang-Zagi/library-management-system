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
      // console.log("Switch borrow data to: ", type);
      // 创建三个对象分别存储按月份、星期几和具体日期的数据
      var aggregatedByMonth = {
        Jan: 0,
        Feb: 0,
        Mar: 0,
        Apr: 0,
        May: 0,
        Jun: 0,
        Jul: 0,
        Aug: 0,
        Sep: 0,
        Oct: 0,
        Nov: 0,
        Dec: 0,
      };
      var aggregatedByDayOfWeek = {
        Sun: 0,
        Mon: 0,
        Tue: 0,
        Wed: 0,
        Thu: 0,
        Fri: 0,
        Sat: 0,
      };
      var aggregatedByDate = {};
      var months = [
        "Jan",
        "Feb",
        "Mar",
        "Apr",
        "May",
        "Jun",
        "Jul",
        "Aug",
        "Sep",
        "Oct",
        "Nov",
        "Dec",
      ];
      var daysOfWeek = ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"];
      // console.log(this.borrowData);
      // 遍历原始数据
      var data = this.borrowData;
      for (var dateStr in data) {
        var date = new Date(dateStr); // 解析日期字符串为 Date 对象
        var month = date.getMonth(); // 获取月份
        var dayOfWeek = date.getDay(); // 获取周几

        // 按月份聚合
        aggregatedByMonth[months[month]] += data[dateStr];

        aggregatedByDayOfWeek[daysOfWeek[dayOfWeek]] += data[dateStr];

        // 按具体日期聚合
        if (!aggregatedByDate[dateStr]) {
          aggregatedByDate[dateStr] = 0;
        }
        aggregatedByDate[dateStr] += data[dateStr];
      }
      var dates;
      var values;
      switch (type) {
        case "day":
          dates = Object.keys(aggregatedByDate);
          values = Object.values(aggregatedByDate);
          break;
        case "week":
          dates = Object.keys(aggregatedByDayOfWeek);
          values = Object.values(aggregatedByDayOfWeek);
          break;
        case "month":
          dates = Object.keys(aggregatedByMonth);
          values = Object.values(aggregatedByMonth);
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
