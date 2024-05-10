package cn.edu.xidian.library.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("book")
@Data
public class Book {
    @TableId
    private String barcode;
    private String isbn;
    @TableField(exist = false)
    private String name;
    private String location;
    private String status;
}
