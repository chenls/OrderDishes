package chenls.orderdishes.bean;

import cn.bmob.v3.BmobUser;

public class MyUser extends BmobUser {

    private static final long serialVersionUID = 1L;

    private String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
