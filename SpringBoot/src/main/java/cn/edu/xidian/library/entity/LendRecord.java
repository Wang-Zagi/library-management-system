package cn.edu.xidian.library.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@TableName("lend_record")
@Data
public class LendRecord {
    @TableId (type = IdType.AUTO)
    private Integer id;
    private String bookId;
    private String bookName;
    private Integer borrowerId;
    private Integer borrowerName;
    @JsonFormat(locale="zh",timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date lendTime;
    @JsonFormat(locale="zh",timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date DeadTime;
    @JsonFormat(locale="zh",timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date returnTime;
    private String status;
}
