package chenls.orderdishes.utils.serializable;

import java.io.Serializable;
import java.util.List;

import chenls.orderdishes.bean.Dish;

public class SerializableDishList implements Serializable {
    private List<Dish> dish;

    public List<Dish> getDish() {
        return dish;
    }

    public SerializableDishList(List<Dish> dish) {
        this.dish = dish;
    }
}
