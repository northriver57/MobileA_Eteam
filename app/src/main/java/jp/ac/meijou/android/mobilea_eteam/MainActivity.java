package jp.ac.meijou.android.mobilea_eteam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import java.text.SimpleDateFormat;
import android.os.Bundle;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import jp.ac.meijou.android.mobilea_eteam.databinding.ActivityMainBinding;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private RecordViewModel recordViewModel;
    private ButtonClickListener buttonClickListener;
    private RecyclerView recyclerView;
    private ItemAdapter itemAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // RecyclerViewの設定
        recyclerView = findViewById(R.id.listView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemAdapter = new ItemAdapter();
        recyclerView.setAdapter(itemAdapter);

        // RecordViewModelを初期化
        recordViewModel = new ViewModelProvider(this).get(RecordViewModel.class);

        // LiveDataを取得
        LiveData<List<DataRoom>> allData = recordViewModel.getAllData();

        // LiveDataの変更を監視
        allData.observe(this, newData -> {
            // データが変更されたときの処理
            updateTextView12(newData);
            itemAdapter.setData(newData);
        });



        buttonClickListener = new ButtonClickListener(this);

        binding.includedLayout.button4.setOnClickListener(view -> buttonClickListener.onButtonClick(totalActivity.class));
        binding.includedLayout.button3.setOnClickListener(view -> buttonClickListener.onButtonClick(LineActivity.class));
        binding.includedLayout.button2.setOnClickListener(view -> buttonClickListener.onButtonClick(GraphActivity.class));
        binding.button6.setOnClickListener(view -> buttonClickListener.onButtonClick(RecordActivity.class));
    }



    private void updateTextView12(List<DataRoom> newData) {
        // newDataを使用してtextView12にデータをセットする処理を実装
        StringBuilder dataText = new StringBuilder();
        int totalIncome = 0;
        int totalExpense = 0;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());


        for (DataRoom data : newData) {
            String formattedDate = dateFormat.format(new Date(data.getDate()));
            dataText.append(formattedDate).append("  ");
            dataText.append(data.getClassification()).append("  ");
            dataText.append(data.getPrice()).append("  ");
            dataText.append(data.getAsset()).append("  ");
            dataText.append(data.getContent()).append("\n");

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

}