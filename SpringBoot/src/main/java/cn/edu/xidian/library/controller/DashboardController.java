package cn.edu.xidian.library.controller;

import cn.edu.xidian.library.LoginUser;
import cn.edu.xidian.library.commom.Result;
import cn.edu.xidian.library.entity.Book;
import cn.edu.xidian.library.entity.BorrowRecord;
import cn.edu.xidian.library.entity.IncomeRecord;
import cn.edu.xidian.library.entity.User;
import cn.edu.xidian.library.mapper.BookMapper;
import cn.edu.xidian.library.mapper.BorrowRecordMapper;
import cn.edu.xidian.library.mapper.IncomeMapper;
import cn.edu.xidian.library.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {
    @Resource
    private UserMapper userMapper;
    @Resource
    private BorrowRecordMapper borrowRecordMapper;
    @Resource
    private BookMapper bookMapper;
    @Resource
    private IncomeMapper incomeMapper;
    @GetMapping
    public Result<?> dashboardrecords(){
        int revenue = userMapper.selectDebt();
        QueryWrapper<User> queryWrapper1=new QueryWrapper<>();
        int userCount = Math.toIntExact(userMapper.selectCount(queryWrapper1));
        QueryWrapper<BorrowRecord> queryWrapper2=new QueryWrapper<BorrowRecord>();
        int borrowRecordCount = Math.toIntExact(borrowRecordMapper.selectCount(queryWrapper2));
        QueryWrapper<Book> queryWrapper3= new QueryWrapper<>();
        int bookCount = Math.toIntExact(bookMapper.selectCount(queryWrapper3));
        Map<String, Object> map = new HashMap<>();//键值对形式
        map.put("revenue", revenue);//放置visitCount到map中
        map.put("userCount", userCount);
        map.put("borrowRecordCount", borrowRecordCount);
        map.put("bookCount", bookCount);
        return Result.success(map);
    }
    @GetMapping("/borrowRecord")
    public Result<?> borrowRecord(){
        // 统计每天的借阅数量
        List<BorrowRecord> records = borrowRecordMapper.selectList(null);
        // 统计每天的借阅数量
        Map<String, Integer> borrowRecordMap = new HashMap<>();
        for (BorrowRecord record : records) {
            String date = record.getBorrowTime().toString();
            if (borrowRecordMap.containsKey(date)) {
                borrowRecordMap.put(date, borrowRecordMap.get(date) + 1);
            } else {
                borrowRecordMap.put(date, 1);
            }
        }
        return Result.success(borrowRecordMap);
    }
    @GetMapping("/revenue")
    public Result<?> debtRecord() {
        List<IncomeRecord> records = incomeMapper.selectList(null);
        Map<String, Integer> debtRecordMap = new HashMap<>();
        for (IncomeRecord record : records) {
            String date = record.getPayTime().toString();
            if (debtRecordMap.containsKey(date)) {
                debtRecordMap.put(date, debtRecordMap.get(date) + record.getAmount());
            } else {
                debtRecordMap.put(date, record.getAmount());
            }
        }
        return Result.success(debtRecordMap);
    }


}
