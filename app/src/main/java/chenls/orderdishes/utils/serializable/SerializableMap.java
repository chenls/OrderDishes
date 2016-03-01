package chenls.orderdishes.utils.serializable;

import java.io.Serializable;
import java.util.Map;

import chenls.orderdishes.bean.DishBean;

public class SerializableMap implements Serializable {
    private Map<Integer, DishBean> map;

    public SerializableMap(Map map) {
        this.map = map;
    }

    public Map<Integer, DishBean> getMap() {
        return map;
    }
}
