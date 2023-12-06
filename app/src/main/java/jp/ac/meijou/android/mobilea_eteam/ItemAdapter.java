package jp.ac.meijou.android.mobilea_eteam;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private List<DataRoom> data = new ArrayList<>();
    private SimpleDateFormat dateFormat;
    private AdapterView.OnItemClickListener onItemClickListener;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView textView;

        public ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.textView);
        }
    }
    public ItemAdapter() {
        this.dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
    }
    public void setData(List<DataRoom> newData) {
        data.clear();
        data.addAll(newData);
        notifyDataSetChanged();
    }
    public DataRoom getItemAtPosition(int position) {
        return data.get(position);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_item_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        viewHolder.textView.setTextSize(16);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        DataRoom data = getItemAtPosition(position);
        String formattedDate = dateFormat.format(new Date(data.getDate()));
        viewHolder.textView.setText(formattedDate + "  " +
                data.getClassification() + "  " +
                data.getPrice() + "  " +
                data.getAsset() + "  " +
                data.getContent());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
