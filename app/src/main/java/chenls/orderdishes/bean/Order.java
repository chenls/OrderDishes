package chenls.orderdishes.bean;

import java.util.Map;

import cn.bmob.v3.BmobObject;

public class Order extends BmobObject {
    private String username;
    private int state;
    private String consigneeMessage;
    private String mark;
    private Double price;
    private Map<Integer, Dish> dishMap;

    public Order() {
    }

    public Order(String username, int state, String consigneeMessage, String mark, Double price, Map<Integer, Dish> dishMap) {
        this.username = username;
        this.state = state;
        this.consigneeMessage = consigneeMessage;
        this.mark = mark;
        this.price = price;
        this.dishMap = dishMap;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int isState() {
        return state;
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
