package me.zhengjie.financial.domain;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

@Data
public class StockPriceLevel {
    private BigDecimal levelBuy;
    private BigDecimal levelBuy1;
    private BigDecimal levelBuy2;
    private BigDecimal levelBuy3;

    private BigDecimal levelSell;
    private BigDecimal levelSell1;
    private BigDecimal levelSell2;
    private BigDecimal levelSell3;
    private BigDecimal oneLevel;

    public StockPriceLevel(BigDecimal low,BigDecimal high) {
        oneLevel = high.subtract(low).multiply(new BigDecimal(0.1)).setScale(4, RoundingMode.HALF_UP);
        levelBuy = high.multiply(new BigDecimal(0.3)).add(low.multiply(new BigDecimal(0.7))).setScale(4, RoundingMode.HALF_UP);
        levelSell = high.multiply(new BigDecimal(0.7)).add(low.multiply(new BigDecimal(0.3))).setScale(4, RoundingMode.HALF_UP);

        levelBuy1 = levelBuy.multiply(new BigDecimal(0.95)).setScale(4, RoundingMode.HALF_UP);
        levelBuy2 = levelBuy.multiply(new BigDecimal(0.90)).setScale(4, RoundingMode.HALF_UP);
        levelBuy3 = levelBuy.multiply(new BigDecimal(0.85)).setScale(4, RoundingMode.HALF_UP);

        levelSell1 = levelSell.multiply(new BigDecimal(1.05)).setScale(4, RoundingMode.HALF_UP);
        levelSell2 = levelSell1.multiply(new BigDecimal(1.05)).setScale(4, RoundingMode.HALF_UP);
        levelSell3 = levelSell2.multiply(new BigDecimal(1.05)).setScale(4, RoundingMode.HALF_UP);
    }

    private int holdDays;
    private Date beginDate;
    private BigDecimal priceRefer;
    private BigDecimal priceWorth;
    public void setDate(BigDecimal cost, Date date){
        long between = DateUtil.between(date,new Date(), DateUnit.DAY);

    }
}
