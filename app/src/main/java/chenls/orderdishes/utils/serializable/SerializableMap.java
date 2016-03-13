package chenls.orderdishes.utils.serializable;

import java.io.Serializable;
import java.util.Map;

import chenls.orderdishes.bean.Dish;

public class SerializableMap implements Serializable {
    private Map<Integer, Dish> map;

    public SerializableMap(Map map) {
        this.map = map;
    }

    public Map<Integer, Dish> getMap() {
        return map;
    }
}
