package cn.edu.xidian.library.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@TableName("book")
@Data
public class Book {
    @TableId
    private String isbn;
    private String name;
    private BigDecimal price;
    private String author;
    private String publisher;
    @JsonFormat(locale="zh",timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date publishTime;
    private String status;
    private Integer borrowNum;
}
