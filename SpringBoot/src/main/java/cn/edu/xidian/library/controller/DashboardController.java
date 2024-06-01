package cn.edu.xidian.library.controller;

import cn.edu.xidian.library.LoginUser;
import cn.edu.xidian.library.commom.Result;
import cn.edu.xidian.library.entity.Book;
import cn.edu.xidian.library.entity.BorrowRecord;
import cn.edu.xidian.library.entity.User;
import cn.edu.xidian.library.mapper.BookMapper;
import cn.edu.xidian.library.mapper.BorrowRecordMapper;
import cn.edu.xidian.library.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
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



}
