package chenls.orderdishes.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class Dish extends BmobObject {
    private BmobFile pic;
    private String name, star, commentNumber, sellNumber, price, summarize, allComment, category;

    public Dish(String name, String star
            , String commentNumber, String sellNumber, String price
            , String summarize, String allComment, String category) {
        super("Dish");
        this.name = name;
        this.star = star;
        this.commentNumber = commentNumber;
        this.sellNumber = sellNumber;
        this.price = price;
        this.summarize = summarize;
        this.allComment = allComment;
        this.category = category;
    }

    public BmobFile getPic() {
        return pic;
    }

    public void setPic(BmobFile pic) {
        this.pic = pic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getCommentNumber() {
        return commentNumber;
    }

    public void setCommentNumber(String commentNumber) {
        this.commentNumber = commentNumber;
    }

    public String getSellNumber() {
        return sellNumber;
    }

    public void setSellNumber(String sellNumber) {
        this.sellNumber = sellNumber;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSummarize() {
        return summarize;
    }

    public void setSummarize(String summarize) {
        this.summarize = summarize;
    }

    public String getAllComment() {
        return allComment;
    }

    public void setAllComment(String allComment) {
        this.allComment = allComment;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
