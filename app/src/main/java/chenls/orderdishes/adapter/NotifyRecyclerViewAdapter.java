package chenls.orderdishes.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import chenls.orderdishes.R;
import chenls.orderdishes.bean.Notify;


public class NotifyRecyclerViewAdapter extends
        RecyclerView.Adapter<NotifyRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private final List<Notify> notifyList;

    public NotifyRecyclerViewAdapter(Context context, List<Notify> notifyList) {
        this.context = context;
        this.notifyList = notifyList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_notify_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Notify notify = notifyList.get(position);
        holder.tv_title.setText(notify.getTitle());
        holder.tv_content.setText(notify.getContent());
        holder.tv_date.setText(notify.getCreatedAt());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, notify.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return notifyList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView tv_title, tv_content, tv_date;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            tv_content = (TextView) view.findViewById(R.id.tv_content);
            tv_date = (TextView) view.findViewById(R.id.tv_date);
        }
    }
}
