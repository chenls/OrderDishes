package chenls.orderdishes.utils.serializable;

import java.io.Serializable;
import java.util.List;

import chenls.orderdishes.bean.Dish;

public class SerializableList implements Serializable {
    private List<Dish> dish;

    public List<Dish> getDish() {
        return dish;
    }

    public SerializableList(List<Dish> dish) {
        this.dish = dish;
    }
}
