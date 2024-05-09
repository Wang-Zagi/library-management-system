package cn.edu.xidian.library.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("book")
@Data
public class Book {
    @TableId
    private String barcode;
    private String isbn;
    private String location;
    private String status;
}
