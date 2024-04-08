package cn.edu.xidian.library.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("user")
@Data
public class User {
    @TableId (type = IdType.AUTO)
    private Integer id;
    private String username;
    private String password;
    private String sex;
    private String phone;
    @TableField(exist = false)
    private String token;
    private Integer role;
}
