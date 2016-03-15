package chenls.orderdishes.utils;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class MyScrollView extends ScrollView {
    private Context context;

    public MyScrollView(Context context) {
        super(context);
        this.context = context;
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }

    @Override
    protected void onScrollChanged(int l, int t, int old_l, int old_t) {
        super.onScrollChanged(l, t, old_l, old_t);
        ((ScrollChangedInterface) context).onScrollChanged();
    }

    public interface ScrollChangedInterface {
        void onScrollChanged();
    }
}
