package me.zhengjie.financial.domain;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import lombok.Data;
import me.zhengjie.financial.define.StockFactors;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.Map;

/**
 * 价格参考
 */
@Data
public class StockPriceReference {
    private int holdDays;
    private Date beginDate;
    private BigDecimal priceRefer;      // 按照日期平均参考值
    private BigDecimal priceWorth;      // 按照参考率
    private BigDecimal priceGoal;       // 按照短期倍数

    private BigDecimal pricePerDay;     // 最大最小值和循环周期，每天参考增加值
    private Map<String, Object> factors;

    public StockPriceReference(Map<String, Object> factors) {
        this.factors = factors;
    }

    public void setDate(StockStatics statics) {
        holdDays = (int)DateUtil.between(statics.getHoldDateAvg(), new Date(), DateUnit.DAY);
        calcWorth(statics, holdDays);
        calcRefer(statics, holdDays);

        BigDecimal rate = new BigDecimal(1.0);
        if (holdDays <= 7) {
            rate = new BigDecimal(factors.get(StockFactors.MultipleKeys.DAY).toString());
        } else if (holdDays <= 30) {
            rate = new BigDecimal(factors.get(StockFactors.MultipleKeys.WEEK).toString());
        } else {
            rate = new BigDecimal(factors.get(StockFactors.MultipleKeys.MONTH).toString());
        }
        BigDecimal up = pricePerDay.multiply(rate).setScale(4, RoundingMode.HALF_UP);
        priceGoal = statics.getPriceCost().add(up);
    }

    private void calcRefer(StockStatics statics, long between) {
        BigDecimal priceHigh = statics.getPriceHigh();
        BigDecimal priceLow = statics.getPriceLow();
        BigDecimal distance = priceHigh.subtract(priceLow);
        BigDecimal cycleSmallDays = new BigDecimal(statics.getStock().getCycleSmall());
        pricePerDay = distance.divide(cycleSmallDays,10,RoundingMode.HALF_UP);
        BigDecimal up = pricePerDay.multiply(new BigDecimal(between)).setScale(4, RoundingMode.HALF_UP);
        priceRefer = statics.getPriceCost().add(up);
    }

    private void calcWorth(StockStatics statics, long between) {
        BigDecimal rate = new BigDecimal(1.0);
        if (between <= 7) {
            rate = new BigDecimal(factors.get(StockFactors.ProfitKeys.WEEK).toString());
        } else if (between <= 90) {
            rate = new BigDecimal(factors.get(StockFactors.ProfitKeys.QUARTER).toString());
        } else if (between <= 180) {
            rate = new BigDecimal(factors.get(StockFactors.ProfitKeys.HARF_YEAR).toString());
        } else if (between <= 270) {
            rate = new BigDecimal(factors.get(StockFactors.ProfitKeys.QUARTER3).toString());
        } else if (between <= 360) {
            rate = new BigDecimal(factors.get(StockFactors.ProfitKeys.YEAR).toString());
        }
        priceWorth = statics.getHoldPriceAvg().multiply(rate).setScale(4, RoundingMode.HALF_UP);
    }
}
