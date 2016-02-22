package chenls.orderdishes.content;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class DishContent {
    private static final int COUNT = 60;
    public static final int[] title_position = new int[]{1, 4, 6, 9, 12, 14, 19, 20,
            23, 25, 29, 32, 39, 40, 43, 47, 58};
    public static final String[] category_names = new String[]{"招牌菜", "推荐", "新品", "热菜", "凉菜", "饮料", "米饭", "山珍", "海味",
            "招牌菜", "推荐", "新品", "热菜", "凉菜", "饮料", "米饭", "山珍", "海味"};
    public static final List<DishItem> ITEMS = new ArrayList<>();

    static {
        int t = 0;
        for (int i = 0; i < COUNT; i++) {
            if (t < title_position.length) {
                if (i == title_position[t]) {
                    t++;
                    continue;
                }
            }
            ITEMS.add(createDummyItem(i, t));
        }
        for (int index : title_position) {
            ITEMS.add(index, createDummyItem(-1, -1));
        }
    }

    private static DishItem createDummyItem(int position, int type) {
        return new DishItem(type, String.valueOf(position),
                "http://pic.nipic.com/2007-11-09/200711912230489_2.jpg", "菜名" + position,
                "菜的概述菜的概述菜的概述菜的概述菜的概述菜的菜的概述菜的概述菜的概述概述菜的概述" + position,
                position, position > 2 ? View.VISIBLE : View.GONE, position, 2 * position, 3 * position);
    }

    public static class DishItem implements Parcelable {
        public final int type;
        public final String id;
        public final String iv_dish;
        public final String tv_dish_name;
        public final String tv_dish_summarize;
        public final float ratingBar;
        public final int tv_signboard;
        public final int tv_comment;
        public final int tv_sell_num;
        public final int tv_price;

        /**
         * @param id                id
         * @param iv_dish           菜的图片
         * @param tv_dish_name      菜名
         * @param tv_dish_summarize 菜的概述
         * @param ratingBar         菜的评价
         * @param tv_signboard      是否 招牌菜
         * @param tv_comment        评论人数
         * @param tv_sell_num       销售数量
         * @param tv_price          价格
         */
        public DishItem(int type, String id, String iv_dish, String tv_dish_name, String tv_dish_summarize,
                        float ratingBar, int tv_signboard, int tv_comment, int tv_sell_num,
                        int tv_price) {
            this.type = type;
            this.id = id;
            this.iv_dish = iv_dish;
            this.tv_dish_name = tv_dish_name;
            this.tv_dish_summarize = tv_dish_summarize;
            this.ratingBar = ratingBar;
            this.tv_signboard = tv_signboard;
            this.tv_comment = tv_comment;
            this.tv_sell_num = tv_sell_num;
            this.tv_price = tv_price;
        }

        protected DishItem(Parcel in) {
            type = in.readInt();
            id = in.readString();
            iv_dish = in.readString();
            tv_dish_name = in.readString();
            tv_dish_summarize = in.readString();
            ratingBar = in.readFloat();
            tv_signboard = in.readInt();
            tv_comment = in.readInt();
            tv_sell_num = in.readInt();
            tv_price = in.readInt();
        }

        public static final Creator<DishItem> CREATOR = new Creator<DishItem>() {
            @Override
            public DishItem createFromParcel(Parcel in) {
                return new DishItem(in);
            }

            @Override
            public DishItem[] newArray(int size) {
                return new DishItem[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(type);
            dest.writeString(id);
            dest.writeString(iv_dish);
            dest.writeString(tv_dish_name);
            dest.writeString(tv_dish_summarize);
            dest.writeFloat(ratingBar);
            dest.writeInt(tv_signboard);
            dest.writeInt(tv_comment);
            dest.writeInt(tv_sell_num);
            dest.writeInt(tv_price);
        }

        @Override
        public String toString() {
            return "DishItem{" +
                    "type=" + type +
                    ", id='" + id + '\'' +
                    ", iv_dish='" + iv_dish + '\'' +
                    ", tv_dish_name='" + tv_dish_name + '\'' +
                    ", tv_dish_summarize='" + tv_dish_summarize + '\'' +
                    ", ratingBar=" + ratingBar +
                    ", tv_signboard=" + tv_signboard +
                    ", tv_comment=" + tv_comment +
                    ", tv_sell_num=" + tv_sell_num +
                    ", tv_price=" + tv_price +
                    '}';
        }
    }
}
