package cn.edu.xidian.library.mapper;

import cn.edu.xidian.library.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Update("update user set debt=0 where id=#{id}")
    void clearFine(Integer id);
}
