package chenls.orderdishes.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Category implements Parcelable {
    int position;
    String categoryName;

    public Category(int position, String categoryName) {
        this.position = position;
        this.categoryName = categoryName;
    }

    protected Category(Parcel in) {
        position = in.readInt();
        categoryName = in.readString();
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    public int getPosition() {
        return position;
    }

    public String getCategoryName() {
        return categoryName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(position);
        dest.writeString(categoryName);
    }
}
