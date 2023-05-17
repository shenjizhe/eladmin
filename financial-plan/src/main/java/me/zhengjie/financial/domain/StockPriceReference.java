package me.zhengjie.financial.domain;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 价格参考
 */
public class StockPriceReference {
    private int holdDays;
    private Date beginDate;
    private BigDecimal priceRefer;
    private BigDecimal priceWorth;
    public void setDate(BigDecimal cost, Date date){
        long between = DateUtil.between(date,new Date(), DateUnit.DAY);

    }
}
