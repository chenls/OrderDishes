package chenls.orderdishes.bean;

import cn.bmob.v3.BmobObject;

public class MyComment extends BmobObject {
    String dishObjectId, name, comment, start;

    public String getDishObjectId() {
        return dishObjectId;
    }

    public void setDishObjectId(String dishObjectId) {
        this.dishObjectId = dishObjectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }
}
