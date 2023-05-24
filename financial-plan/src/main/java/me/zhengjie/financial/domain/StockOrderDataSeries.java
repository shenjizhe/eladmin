package me.zhengjie.financial.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class StockOrderDataSeries {
    private String name;
    private String type = "line";
    private String stack = "Total";
    private Boolean connectNulls = true;
    private List<BigDecimal> data;

    public StockOrderDataSeries(String name) {
        this.name = name;
        this.data = new ArrayList<>();
    }

    public void setData(List<Date> dates, Map<Date, BigDecimal> map) {

        for (Date date : dates) {
            if (map.containsKey(date)) {
                data.add(map.get(date));
            } else {
                data.add(null);
            }
        }
    }
}
