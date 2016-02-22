package chenls.orderdishes.content;

import java.util.ArrayList;
import java.util.List;

public class AckOrderContent {

    public static final List<AckOrderItem> ITEMS = new ArrayList<>();

    static {
        for (int i = 1; i <= 20; i++) {
            addItem(createDummyItem(i));
        }
    }

    private static void addItem(AckOrderItem item) {
        ITEMS.add(item);
    }

    private static AckOrderItem createDummyItem(int position) {
        return new AckOrderItem("http://img4.3lian.com/sucai/img6/230/29.jpg", "名字" + position, "数量" + position, "价格" + position);
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
