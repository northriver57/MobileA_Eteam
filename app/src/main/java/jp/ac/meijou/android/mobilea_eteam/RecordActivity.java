package jp.ac.meijou.android.mobilea_eteam;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.atomic.AtomicInteger;
import jp.ac.meijou.android.mobilea_eteam.databinding.ActivityRecordBinding;

public class RecordActivity extends AppCompatActivity {
    private ActivityRecordBinding binding;
    private RecordViewModel recordViewModel;
    private DaoClass dao;
    private long date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // RecordViewModel を初期化
        recordViewModel = new ViewModelProvider(this).get(RecordViewModel.class);
        dao = AppData.getDatabase(this).daoClass();

        // 今日の日付を取得
        Calendar today = Calendar.getInstance();
        date = today.getTimeInMillis();

        CalendarView calendarView = findViewById(R.id.calendarView2);

        // 日付が選択されたときのリスナーを設定
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            // カレンダーインスタンスを作成して日付を設定
            Calendar selectedDate = new GregorianCalendar(year, month, dayOfMonth);

            // date変数に選択された日付を設定
            date = selectedDate.getTimeInMillis();
        });

        AtomicInteger type = new AtomicInteger(-1);
        RadioGroup group = findViewById(R.id.radioGroup);
        Spinner spinnerClass = findViewById(R.id.spinnerclass);

        group.setOnCheckedChangeListener((view, id) -> {
            if (id == R.id.RadioIncome) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.ListIncome));
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerClass.setAdapter(adapter);

                type.set(1);
            } else if (id == R.id.RadioExpense) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.ListClass));
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerClass.setAdapter(adapter);
                type.set(2);
            }
        });

        // OKボタンがクリックされたときの処理
        binding.OKbutton.setOnClickListener(view -> {
            String classification = binding.spinnerclass.getSelectedItem().toString();
            String asset = binding.spinnerasset.getSelectedItem().toString();
            int price = Integer.parseInt(binding.pricetext.getText().toString());
            String content = binding.contenttext.getText().toString();

            if (type.get() != -1) {

                    // ViewModelを通じてデータベースにデータを挿入
                    recordViewModel.insertData(classification, asset, price, content, date, type.get());

                    // 登録後の処理を追加（例: MainActivityに戻るなど）
                    Intent intent = new Intent(RecordActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // タグがnullの場合の処理（デフォルト値のまま）
                    //Toast
                }
            });

        // 「back」ボタンがクリックされたときの処理
        binding.buckbutton.setOnClickListener(view -> {
            Intent intent = new Intent(RecordActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

}
