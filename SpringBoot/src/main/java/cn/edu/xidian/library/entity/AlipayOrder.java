package cn.edu.xidian.library.entity;

import lombok.Data;

@Data
public class AlipayOrder {
    //订单编号
    private String traceNo;
    //商品金额
    private double totalAmount;
    //商品名称
    private String subject;
    //订单追踪号，商户自己生成，可已不使用
    private String alipayTraceNo;
}
