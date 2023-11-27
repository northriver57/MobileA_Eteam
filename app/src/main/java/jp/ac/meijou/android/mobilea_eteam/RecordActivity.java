package jp.ac.meijou.android.mobilea_eteam;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

import jp.ac.meijou.android.mobilea_eteam.databinding.ActivityRecordBinding;

public class RecordActivity extends AppCompatActivity {

    private ActivityRecordBinding binding;

    private DaoClass dao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dao = AppData.getDatabase(this).daoClass();

        // OKボタンがクリックされたときの処理
        binding.OKbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 入力されたデータを取得
                String classification = binding.classificationtextView.getText().toString();
                int asset = Integer.parseInt(binding.assettext.getText().toString());
                int price = Integer.parseInt(binding.pricetextView.getText().toString());
                String content = binding.contenttextView.getText().toString();

                // 現在の日付を取得
                long date = Calendar.getInstance().getTimeInMillis();

                // データベースに登録
                DataRoom data = new DataRoom(classification, asset, price, content, date);
                dao.insert(data);

                // 登録後の処理を追加（例: MainActivityに戻るなど）

                Intent intent = new Intent(RecordActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        });

        // 「back」ボタンがクリックされたときの処理
        binding.buckbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecordActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
