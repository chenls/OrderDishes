package chenls.orderdishes.content;

import java.util.ArrayList;
import java.util.List;

public class CategoryContent {

    public static final List<CategoryItem> ITEMS = new ArrayList<>();

    static {
        for (int i = 1; i <= DishContent.category_names.length; i++) {
            addItem(createDummyItem(i));
        }
    }

    private static void addItem(CategoryItem item) {
        ITEMS.add(item);
    }

    private static CategoryItem createDummyItem(int position) {
        return new CategoryItem(DishContent.category_names[position - 1]);
    }

    public static class CategoryItem {
        public final String category_name;

        public CategoryItem(String category_name) {
            this.category_name = category_name;
        }
    }
}
