package chenls.orderdishes;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import chenls.orderdishes.bean.Dish;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void list_isCorrect() throws Exception {
        List<Dish> object = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            if (i < 4)
                object.add(new Dish(pic, star, commentNumber, price, allComment, "1", "菜名" + i, sellNumber, summarize, categoryName));
            else if (i < 6)
                object.add(new Dish(pic, star, commentNumber, price, allComment, "2", "菜名" + i, sellNumber, summarize, categoryName));
            else if (i < 7)
                object.add(new Dish(pic, star, commentNumber, price, allComment, "3", "菜名" + i, sellNumber, summarize, categoryName));
            else
                object.add(new Dish(pic, star, commentNumber, price, allComment, "4", "菜名" + i, sellNumber, summarize, categoryName));
        }
        System.out.println("test" + object.toString());

        String oldCategory = null;
        for (int i = 0; i < object.size(); i++) {
            Dish dish = object.get(i);
            String newCategory = dish.getCategory();
            if (oldCategory != null) {
                if (!newCategory.equals(oldCategory)) {
                    object.add(i, new Dish(pic, star, commentNumber, price, allComment, "---------", "title" + i, sellNumber, summarize, categoryName));
                }
            } else {
                object.add(i, new Dish(pic, star, commentNumber, price, allComment, "---------", "title" + i, sellNumber, summarize, categoryName));
            }
            oldCategory = newCategory;
        }
        System.out.print("test" + object.toString());
    }
}