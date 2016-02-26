package chenls.orderdishes.content;

import java.util.List;
import java.util.Map;

import chenls.orderdishes.bean.DishBean;

public class AckOrderContent {

    public static List<AckOrderItem> createItem(Map<Integer, DishBean> dishBeanMap) {
        return null;
    }

    public static class AckOrderItem {
        public final String iv_dish;
        public final String tv_dish_name;
        public final String tv_dish_num;
        public final String tv_dish_price;

        public AckOrderItem(String iv_dish, String tv_dish_name, String tv_dish_num, String tv_dish_price) {
            this.iv_dish = iv_dish;
            this.tv_dish_name = tv_dish_name;
            this.tv_dish_num = tv_dish_num;
            this.tv_dish_price = tv_dish_price;
        }
    }
}
