package chenls.orderdishes.utils;

import java.io.Serializable;
import java.util.Map;

public class SerializableMap implements Serializable {
    private Map map;

    public SerializableMap(Map map) {
        this.map = map;
    }

    public Map getMap() {
        return map;
    }
}
