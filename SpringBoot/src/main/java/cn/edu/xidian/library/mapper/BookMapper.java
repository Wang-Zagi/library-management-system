package cn.edu.xidian.library.mapper;

import cn.edu.xidian.library.entity.Book;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BookMapper extends BaseMapper<Book> {
    Book get(String barcode);
}
