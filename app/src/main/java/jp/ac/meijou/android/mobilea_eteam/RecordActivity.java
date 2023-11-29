package jp.ac.meijou.android.mobilea_eteam;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.util.Calendar;
import jp.ac.meijou.android.mobilea_eteam.databinding.ActivityRecordBinding;

public class RecordActivity extends AppCompatActivity {

    private ActivityRecordBinding binding;
    private RecordViewModel recordViewModel;

    private DaoClass dao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // RecordViewModel を初期化
        recordViewModel = new ViewModelProvider(this).get(RecordViewModel.class);
        dao = AppData.getDatabase(this).daoClass();

        // OKボタンがクリックされたときの処理
        binding.OKbutton.setOnClickListener(view -> {
            String classification = binding.classificationtext.getText().toString();
            int asset = Integer.parseInt(binding.assettext.getText().toString());
            int price = Integer.parseInt(binding.pricetext.getText().toString());
            String content = binding.contenttext.getText().toString();

            // 現在の日付を取得
            long date = Calendar.getInstance().getTimeInMillis();

            // ViewModelを通じてデータベースにデータを挿入
            recordViewModel.insertData(classification, asset, price, content);

            // 登録後の処理を追加（例: MainActivityに戻るなど）

            Intent intent = new Intent(RecordActivity.this, MainActivity.class);
            startActivity(intent);
            finish();

        });

        // 「back」ボタンがクリックされたときの処理
        binding.buckbutton.setOnClickListener(view -> {
            Intent intent = new Intent(RecordActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

}
