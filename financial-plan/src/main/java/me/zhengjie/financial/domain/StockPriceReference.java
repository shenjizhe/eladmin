package me.zhengjie.financial.domain;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import me.zhengjie.financial.define.StockFactors;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.Map;

/**
 * 价格参考
 */
public class StockPriceReference {
    private int holdDays;
    private Date beginDate;
    private BigDecimal priceRefer;
    private BigDecimal priceWorth;
    private BigDecimal priceGoal;

    private BigDecimal pricePerDay;
    private Map<String, Object> factors;

    public StockPriceReference(Map<String, Object> factors) {
        this.factors = factors;
    }

    public void setDate(StockStatics statics) {
        long between = DateUtil.between(statics.getHoldDateAvg(), new Date(), DateUnit.DAY);
        calcWorth(statics, between);
        calcRefer(statics, between);

        BigDecimal rate = new BigDecimal(1.0);
        if (between <= 7) {
            rate = new BigDecimal(factors.get(StockFactors.MultipleKeys.DAY).toString());
        } else if (between <= 30) {
            rate = new BigDecimal(factors.get(StockFactors.MultipleKeys.WEEK).toString());
        } else {
            rate = new BigDecimal(factors.get(StockFactors.MultipleKeys.MONTH).toString());
        }
        priceGoal = pricePerDay.multiply(rate).setScale(4, RoundingMode.HALF_UP);
    }

    private void calcRefer(StockStatics statics, long between) {
        BigDecimal priceHigh = statics.getPriceHigh();
        BigDecimal priceLow = statics.getPriceLow();
        BigDecimal distance = priceHigh.subtract(priceLow);
        pricePerDay = distance.divide(new BigDecimal(statics.getStock().getCycleSmall()));
        priceRefer = pricePerDay.multiply(new BigDecimal(between)).setScale(4, RoundingMode.HALF_UP);
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
