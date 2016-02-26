package chenls.orderdishes.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class DishBean implements Parcelable{
    int num;
    int price;
    String name;
    String image;

    public DishBean(int num, int price, String name, String image) {
        this.num = num;
        this.price = price;
        this.name = name;
        this.image = image;
    }

    protected DishBean(Parcel in) {
        num = in.readInt();
        price = in.readInt();
        name = in.readString();
        image = in.readString();
    }

    public static final Creator<DishBean> CREATOR = new Creator<DishBean>() {
        @Override
        public DishBean createFromParcel(Parcel in) {
            return new DishBean(in);
        }

        @Override
        public DishBean[] newArray(int size) {
            return new DishBean[size];
        }
    };

    public int getNum() {
        return num;
    }

    public int getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(num);
        dest.writeInt(price);
        dest.writeString(name);
        dest.writeString(image);
    }

    @Override
    public String toString() {
        return "DishBean{" +
                "num=" + num +
                ", price=" + price +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
