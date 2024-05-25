package cn.edu.xidian.library.mapper;

import cn.edu.xidian.library.entity.Book;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface BookMapper extends BaseMapper<Book> {
    Book get(String barcode);

    @Select("select MAX(barcode) from book")
    String getMaxBarcode();
}
