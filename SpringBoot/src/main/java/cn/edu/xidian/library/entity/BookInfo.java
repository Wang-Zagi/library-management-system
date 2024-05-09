package cn.edu.xidian.library.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@TableName("book_info")
@Data
public class BookInfo {
    @TableId
    private String isbn;
    private String name;
    private BigDecimal price;
    private String author;
    private String publisher;
    @JsonFormat(locale="zh",timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date publishTime;
    @TableField(exist = false)
    private List<Book> books;
}
