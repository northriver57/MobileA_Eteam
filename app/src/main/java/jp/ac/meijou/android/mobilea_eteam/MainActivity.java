package jp.ac.meijou.android.mobilea_eteam;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import java.util.List;
import jp.ac.meijou.android.mobilea_eteam.databinding.ActivityMainBinding;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private RecordViewModel recordViewModel;
    private ButtonClickListener buttonClickListener;
    private ItemAdapter itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // RecyclerViewの設定
        RecyclerView recyclerView = findViewById(R.id.listView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemAdapter = new ItemAdapter();
        // アイテムがクリックされたときの処理
        itemAdapter.setOnItemClickListener(this::showConfirmationDialog);
        recyclerView.setAdapter(itemAdapter);

        // RecordViewModelを初期化
        recordViewModel = new ViewModelProvider(this).get(RecordViewModel.class);

        // LiveDataを取得
        LiveData<List<DataRoom>> allData = recordViewModel.getAllData();

        // LiveDataの変更を監視
        allData.observe(this, newData -> {
            // データが変更されたときの処理
            updateMoney(newData);
            itemAdapter.setData(newData);
        });


        buttonClickListener = new ButtonClickListener(this);

        binding.includedLayout.button4.setOnClickListener(view -> buttonClickListener.onButtonClick(totalActivity.class));
        binding.includedLayout.button3.setOnClickListener(view -> buttonClickListener.onButtonClick(LineActivity.class));
        binding.includedLayout.button2.setOnClickListener(view -> buttonClickListener.onButtonClick(GraphActivity.class));
        binding.button6.setOnClickListener(view -> buttonClickListener.onButtonClick(RecordActivity.class));
    }
    private void deleteData(long itemId) {
        // itemIdを使用してデータベースからデータを削除する処理を実装
        recordViewModel.deleteData(itemId);
    }


    private void updateMoney(List<DataRoom> newData) {

        int totalIncome = 0;
        int totalExpense = 0;

        for (DataRoom data : newData) {
            // "type"が1の場合のみincomeTextに追加
            if (data.getType() == 1) {
                totalIncome += data.getPrice();
            }else if (data.getType() == 2) {
                totalExpense += data.getPrice();
            }
        }

        binding.incometext.setText(String.valueOf(totalIncome));
        binding.expensetext.setText(String.valueOf(totalExpense));
        binding.sumtext.setText(String.valueOf(totalIncome - totalExpense));
    }
    // 確認ダイアログ
    private void showConfirmationDialog(final long itemId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("データを削除しますか？");
        builder.setPositiveButton("はい", (dialog, which) -> {
            // はいボタンが押されたときの処理
            deleteData(itemId);
        });
        builder.setNegativeButton("いいえ", (dialog, which) -> {
            // いいえボタンが押されたときの処理
            dialog.dismiss(); // ダイアログを閉じる
        });
        builder.show();
    }

}