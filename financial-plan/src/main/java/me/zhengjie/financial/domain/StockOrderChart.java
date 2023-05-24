package me.zhengjie.financial.domain;

import lombok.Data;
import me.zhengjie.financial.service.dto.StockOrderDto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class StockOrderChart {
    List<StockOrderDataSeries> list = new ArrayList<>();

    public StockOrderChart() {
    }

    public void setOrders(List<StockOrderDto> orders) {

        for (StockOrderDto order : orders) {
            if (order.getTradeType() == 1) {

            } else if (order.getTradeType() == 2) {

            }
        }
    }

    private List<Date> setDate(List<StockOrderDto> orders) {
        List<Date> dates = new ArrayList<>(orders.size());
        Date first = orders.get(0).getTradeTime();
        Date last = orders.get(orders.size()-1).getTradeTime();
        return dates;
    }
}
