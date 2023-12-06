package jp.ac.meijou.android.mobilea_eteam;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private List<DataRoom> data = new ArrayList<>();
    private SimpleDateFormat dateFormat;
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(long itemId);
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView textView;
        public long itemId;

        public ViewHolder(@NonNull View view) {
            super(view);
            textView = view.findViewById(R.id.textView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // アイテムの位置（position）を取得
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION  && onItemClickListener != null) {
                        long itemId = data.get(position).getId();
                        // 確認ダイアログを表示
                        showConfirmationDialog(itemView.getContext(), itemId);

                        }
                    }
            });
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
        viewHolder.itemId = data.getId();
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
    // 確認ダイアログ
    private void showConfirmationDialog(Context context, final long itemId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("データを削除しますか");
        builder.setPositiveButton("はい", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(itemId);
                }
            }
        });
        builder.setNegativeButton("いいえ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
}
