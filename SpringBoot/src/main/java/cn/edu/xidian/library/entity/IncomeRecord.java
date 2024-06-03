package cn.edu.xidian.library.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@TableName("income_record")
@Data
public class IncomeRecord {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer payerId;
    private Integer amount;
    @JsonFormat(locale="zh",timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date payTime;
}
