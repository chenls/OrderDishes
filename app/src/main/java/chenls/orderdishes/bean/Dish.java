package chenls.orderdishes.bean;

import android.os.Parcel;
import android.os.Parcelable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class Dish extends BmobObject implements Parcelable {
    private BmobFile pic;
    private String name, star, commentNumber, sellNumber, price,
            summarize, allComment, category, categoryName;
    private int number;

    public Dish(String category, String name) {
        this.category = category;
        this.name = name;
    }

    public Dish(int number, String price, String name, BmobFile pic) {
        this.pic = pic;
        this.name = name;
        this.price = price;
        this.number = number;
    }

    public Dish(String star, String commentNumber, String price, String allComment, String category, String name, String sellNumber, String summarize, String categoryName) {
        this.star = star;
        this.commentNumber = commentNumber;
        this.price = price;
        this.allComment = allComment;
        this.category = category;
        this.name = name;
        this.sellNumber = sellNumber;
        this.summarize = summarize;
        this.categoryName = categoryName;
    }

    public BmobFile getPic() {
        return pic;
    }

    public int getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public String getStar() {
        return star;
    }

    public String getCommentNumber() {
        return commentNumber;
    }

    public String getSellNumber() {
        return sellNumber;
    }

    public String getPrice() {
        return price;
    }

    public String getSummarize() {
        return summarize;
    }

    public String getAllComment() {
        return allComment;
    }

    public String getCategory() {
        return category;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public static Creator<Dish> getCREATOR() {
        return CREATOR;
    }

    protected Dish(Parcel in) {
        pic = (BmobFile) in.readSerializable();
        name = in.readString();
        star = in.readString();
        commentNumber = in.readString();
        sellNumber = in.readString();
        price = in.readString();
        summarize = in.readString();
        allComment = in.readString();
        category = in.readString();
        categoryName = in.readString();
        number = in.readInt();
    }

    public static final Creator<Dish> CREATOR = new Creator<Dish>() {
        @Override
        public Dish createFromParcel(Parcel in) {
            return new Dish(in);
        }

        @Override
        public Dish[] newArray(int size) {
            return new Dish[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(pic);
        dest.writeString(name);
        dest.writeString(star);
        dest.writeString(commentNumber);
        dest.writeString(sellNumber);
        dest.writeString(price);
        dest.writeString(summarize);
        dest.writeString(allComment);
        dest.writeString(category);
        dest.writeString(categoryName);
        dest.writeInt(number);
    }
}
