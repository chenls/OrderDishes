package chenls.orderdishes.bean;

import cn.bmob.v3.BmobObject;

public class MyComment extends BmobObject {
    String dishObjectId, name, comment, start;

    public MyComment(String dishObjectId, String name, String comment, String start) {
        this.dishObjectId = dishObjectId;
        this.name = name;
        this.comment = comment;
        this.start = start;
    }

    public String getDishObjectId() {
        return dishObjectId;
    }

    public String getName() {
        return name;
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
