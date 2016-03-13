package chenls.orderdishes.bean;

import java.util.Map;

import cn.bmob.v3.BmobObject;

public class Order extends BmobObject {
    private boolean isPay;
    private String consigneeMessage;
    private String mark;
    private Double price;
    private Map<Integer, Dish> dishMap;

    public Order() {
    }

    public Order(boolean isPay, String consigneeMessage, String mark, Double price, Map<Integer, Dish> dishMap) {
        this.isPay = isPay;
        this.consigneeMessage = consigneeMessage;
        this.mark = mark;
        this.price = price;
        this.dishMap = dishMap;
    }

    public void setPay(boolean pay) {
        isPay = pay;
    }

    public boolean isPay() {
        return isPay;
    }

    public String getConsigneeMessage() {
        return consigneeMessage;
    }

    public String getMark() {
        return mark;
    }

    public Double getPrice() {
        return price;
    }

    public Map<Integer, Dish> getDishMap() {
        return dishMap;
    }
}
