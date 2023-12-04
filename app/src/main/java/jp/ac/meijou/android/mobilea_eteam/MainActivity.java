package jp.ac.meijou.android.mobilea_eteam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import java.text.SimpleDateFormat;
import android.content.Intent;
import android.os.Bundle;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import jp.ac.meijou.android.mobilea_eteam.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
private ActivityMainBinding binding;
    private RecordViewModel recordViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // RecordViewModelを初期化
        recordViewModel = new ViewModelProvider(this).get(RecordViewModel.class);

        // LiveDataを取得
        LiveData<List<DataRoom>> allData = recordViewModel.getAllData();

        // LiveDataの変更を監視
        allData.observe(this, newData -> {
            // データが変更されたときの処理
            updateTextView12(newData);
        });



        binding.button4.setOnClickListener(view -> {
            var intent = new Intent(this, totalActivity.class);
            startActivity(intent);
        });

        binding.button3.setOnClickListener(view -> {
            var intent = new Intent(this, LineActivity.class);
            startActivity(intent);
        });

        binding.button2.setOnClickListener(view -> {
            var intent = new Intent(this, GraphActivity.class);
            startActivity(intent);
        });

        binding.button6.setOnClickListener(view -> {
            var intent = new Intent(this, RecordActivity.class);
            startActivity(intent);
        });
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
        binding.textView12.setText(dataText.toString());
        binding.incometext.setText(String.valueOf(totalIncome));
        binding.expensetext.setText(String.valueOf(totalExpense));
        binding.sumtext.setText(String.valueOf(totalIncome - totalExpense));
    }

}