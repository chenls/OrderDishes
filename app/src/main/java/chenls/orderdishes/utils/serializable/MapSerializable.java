package chenls.orderdishes.utils.serializable;

import java.io.Serializable;
import java.util.Map;

import chenls.orderdishes.bean.Dish;

public class MapSerializable implements Serializable {
    private Map<Integer, Dish> map;

    public MapSerializable(Map<Integer, Dish> map) {
        this.map = map;
    }

    public Map<Integer, Dish> getMap() {
        return map;
    }
}
