package cn.edu.xidian.library.mapper;

import cn.edu.xidian.library.entity.BookInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BookInfoMapper extends BaseMapper<BookInfo>{

    List<BookInfo> getList(Integer pageNum, Integer pageSize, String isbn, String name, String author);

    Integer getTotal(String isbn, String name, String author);
}
