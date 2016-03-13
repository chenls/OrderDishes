package chenls.orderdishes.utils.serializable;

import java.io.Serializable;
import java.util.List;

import chenls.orderdishes.bean.Category;

public class CategoryListSerializable implements Serializable {
    private List<Category> categoryList;

    public CategoryListSerializable(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }
}
