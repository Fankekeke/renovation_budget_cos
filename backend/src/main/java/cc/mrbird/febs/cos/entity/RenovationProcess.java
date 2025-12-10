package cc.mrbird.febs.cos.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 装修进度
 */
@Data
public class RenovationProcess {

    private Integer id;
    private String time;
    private String title;
    private String description;
    private Integer workHours;
    private BigDecimal itemPrice;
    private Integer actualWorkHours;
    private BigDecimal actualItemPrice;
    private String currentStatus;

    private String overFlag;
}
