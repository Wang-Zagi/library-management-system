package cn.edu.xidian.library.controller;

import cn.edu.xidian.library.LoginUser;
import cn.edu.xidian.library.commom.Result;
import cn.edu.xidian.library.entity.LendRecord;
import cn.edu.xidian.library.entity.User;
import cn.edu.xidian.library.mapper.LendRecordMapper;
import cn.edu.xidian.library.mapper.UserMapper;
import cn.edu.xidian.library.utils.TokenUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserMapper userMapper;
    @Resource
    private LendRecordMapper lendRecordMapper;

    @GetMapping()
    public Result<?> findPage2(@RequestParam(defaultValue = "1") Integer pageNum,
                               @RequestParam(defaultValue = "10") Integer pageSize,
                               @RequestParam(defaultValue = "") Long Id,
                               @RequestParam(defaultValue = "") String name,
                               @RequestParam(defaultValue = "") String phone){
        LambdaQueryWrapper<User> wrappers = Wrappers.lambdaQuery();
        if(Id!=null)
            wrappers.eq(User::getId, Id);
        if(StringUtils.isNotBlank(name))
            wrappers.like(User::getUsername, name);
        if(StringUtils.isNotBlank(phone))
            wrappers.like(User::getPhone, phone);
        wrappers.like(User::getRole,2);
        wrappers.orderByAsc(User::getId);   //按编号排序
        Page<User> userPage =userMapper.selectPage(new Page<>(pageNum,pageSize), wrappers);
        return Result.success(userPage);
    }
    @PostMapping("/register")
    public Result<?> register(@RequestBody User user){
        User res = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername,user.getUsername()));
        if(res != null)
        {
            return Result.error("-1","用户名已被占用");
        }
        userMapper.insert(user);
        return Result.success();
    }
    @PostMapping("/login")
    public Result<?> login(@RequestBody User user){
        User res = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername,user.getUsername()).eq(User::getPassword,user.getPassword()));
        if(res == null)
        {
            return Result.error("-1","用户名或密码错误");
        }
        String token = TokenUtils.genToken(res);
        res.setToken(token);
        LoginUser.addVisitCount();
        return Result.success(res);
    }

    @PutMapping("/password")
    public  Result<?> update( @RequestParam Integer id,
                              @RequestParam String password){
        User user = userMapper.selectById(id);
        user.setPassword(password);
        userMapper.updateById(user);
        return Result.success();
    }

    @PutMapping("/forget")
    public Result<?> forget(@RequestBody User user){
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(User::getUsername, user.getUsername());
        User user1 = userMapper.selectOne(wrapper);
        user.setId(user1.getId());
        userMapper.updateById(user);
        return Result.success();
    }

    @PutMapping
    public Result<?> update(@RequestBody User user){
        userMapper.updateById(user);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id){
        LambdaQueryWrapper<LendRecord> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(LendRecord::getBorrowerId,id)
                .eq(LendRecord::getStatus,"on loan");
        if (lendRecordMapper.selectOne(wrapper) != null)
            return Result.error("-1","用户有书籍正在借阅中，无法删除");
        userMapper.deleteById(id);
        return Result.success();
    }

    @PostMapping
    @Transactional
    public  Result<?> deleteBatch(@RequestParam List<Integer> ids){
        for (Integer id:ids){
            LambdaQueryWrapper<LendRecord> wrapper = Wrappers.lambdaQuery();
            wrapper.eq(LendRecord::getBorrowerId,id)
                    .eq(LendRecord::getStatus,"on loan");
            if (lendRecordMapper.selectOne(wrapper) != null)
                return Result.error("-1","用户有书籍正在借阅中，无法删除");
        }
        userMapper.deleteBatchIds(ids);
        return Result.success();
    }
}
