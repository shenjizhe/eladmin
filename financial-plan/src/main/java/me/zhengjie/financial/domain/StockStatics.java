package me.zhengjie.financial.domain;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import lombok.Data;
import me.zhengjie.financial.service.dto.StockAnalysisDto;
import me.zhengjie.financial.service.dto.StockDto;
import me.zhengjie.financial.service.dto.StockOrderDto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class StockStatics implements Serializable {
    private StockDto stock;

    public StockStatics(StockDto stock) {
        this.stock = stock;
    }

    private List<StockOrderDto> buyOrders = new ArrayList<>();
    private List<StockOrderDto> saleOrders = new ArrayList<>();
    private List<StockOrderDto> holdOrders = new ArrayList<>();

    private Integer holdCount;
    private BigDecimal holdPriceTotal;
    private BigDecimal holdPriceAvg;
    private Date holdDateAvg;           // 算数平均持仓日期

    public void calcOrders(List<StockOrderDto> orders) {
        buyOrders.clear();
        saleOrders.clear();
        holdOrders.clear();
        int buyCount = 0;
        int saleCount = 0;

        orders.sort(StockOrderDto::compareTo);
        for (StockOrderDto order : orders) {
            switch (order.getTradeType()) {
                case 1:
                    buyOrders.add(order);
                    buyCount += order.getTradeCount();
                    break;
                case -1:
                    saleOrders.add(order);
                    saleCount += order.getTradeCount();
                    break;
                default:
                    break;
            }
        }

        int leftCount = buyCount - saleCount;

        Date beginDate = null;
        for (int i = buyOrders.size() - 1; i > 0; i--) {
            StockOrderDto order = buyOrders.get(i);
            if (leftCount > order.getTradeCount()) {
                holdOrders.add(order);
                leftCount -= order.getTradeCount();
            } else {
                order.setTradeCount(leftCount);
                holdOrders.add(order);
                beginDate = order.getTradeTime();
                break;
            }
        }


        holdCount = 0;
        holdPriceTotal = BigDecimal.ZERO;
        long sumTotal = 0;

        for (StockOrderDto holdOrder : holdOrders) {
            Integer count = holdOrder.getTradeCount();
            BigDecimal multiply = holdOrder.getTradePrice().multiply(new BigDecimal(count));
            holdCount += count;
            long between = DateUtil.between(beginDate, holdOrder.getTradeTime(), DateUnit.DAY) + 1;
            sumTotal += between * count;
            holdPriceTotal = holdPriceTotal.add(multiply);
        }

        holdPriceAvg = holdPriceTotal.divide(new BigDecimal(holdCount), 4, RoundingMode.HALF_UP);
        int days = (int) sumTotal / holdCount;
        holdDateAvg = DateUtil.offsetDay(beginDate, days);
    }

    private StockAnalysisDto analysis;
    private StockPriceLevel level;
    private BigDecimal priceCost;
    private BigDecimal priceAvg;
    private BigDecimal priceHigh;
    private BigDecimal priceLow;
    private boolean beWorthAmplitude;  // 波动够大
    private BigDecimal costWaterLevel; // 成本够低

    public void calcAnylsis(StockAnalysisDto stockAnalysis, BigDecimal rateLimit) {
        this.analysis = stockAnalysis;
        this.priceCost = stockAnalysis.getMainHoldCost();
        this.priceAvg = stockAnalysis.getPriceAvg();
        this.priceHigh = stockAnalysis.getPriceHigh90().multiply(new BigDecimal(2.0)).subtract(stockAnalysis.getPriceHigh70());
        this.priceLow = stockAnalysis.getPriceLow90().multiply(new BigDecimal(2.0)).subtract(stockAnalysis.getPriceLow70());

        BigDecimal rate = stockAnalysis.getPriceHigh90()
                .divide(stockAnalysis.getPriceLow90(), 4, RoundingMode.HALF_UP)
                .subtract(new BigDecimal(1.0));
        beWorthAmplitude = rate.compareTo(rateLimit) >= 0;
        costWaterLevel = (priceCost.subtract(priceLow))
                .divide(priceHigh.subtract(priceLow), 4, RoundingMode.HALF_UP);
    }

    public void calcLevel() {
        level = new StockPriceLevel(this.priceLow, this.priceHigh);
    }

    private StockPriceReference priceReference;

    public void calcReference(Map<String, Object> map) {
        priceReference = new StockPriceReference(map);
        priceReference.setDate(this);
    }
}
