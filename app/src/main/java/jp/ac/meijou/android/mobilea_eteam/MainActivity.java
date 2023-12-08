package jp.ac.meijou.android.mobilea_eteam;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import jp.ac.meijou.android.mobilea_eteam.databinding.ActivityMainBinding;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;



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

        Spinner yearSpinner = findViewById(R.id.yearSpinner);
        Spinner monthSpinner = findViewById(R.id.monthSpinner);

        // SpinnerにAdapterを設定して選択肢を表示
        ArrayAdapter<CharSequence> yearAdapter = ArrayAdapter.createFromResource(this, R.array.years, android.R.layout.simple_spinner_item);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);
        ArrayAdapter<CharSequence> monthAdapter = ArrayAdapter.createFromResource(this, R.array.months, android.R.layout.simple_spinner_item);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(monthAdapter);

        // Spinnerの選択が変更されたときのリスナーを設定
        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // 選択された年月に対応するデータを取得して表示する処理を実装
                updateData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // 何もしない
            }
        });
        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // 選択された年月に対応するデータを取得して表示する処理を実装
                updateData();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // 何もしない
            }
        });
        // 初期値を今日の年月に設定
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1; // 月は0から始まるため+1する

// 年Spinnerの初期値を設定
        int yearPosition = yearAdapter.getPosition(String.valueOf(currentYear));
        yearSpinner.setSelection(yearPosition);

// 月Spinnerの初期値を設定
        int monthPosition = monthAdapter.getPosition(String.valueOf(currentMonth));
        monthSpinner.setSelection(monthPosition);

        buttonClickListener = new ButtonClickListener(this);

        binding.includedLayout.button4.setOnClickListener(view -> buttonClickListener.onButtonClick(totalActivity.class));
        binding.includedLayout.button3.setOnClickListener(view -> buttonClickListener.onButtonClick(LineActivity.class));
        binding.includedLayout.button2.setOnClickListener(view -> buttonClickListener.onButtonClick(GraphActivity.class));
        binding.button6.setOnClickListener(view -> buttonClickListener.onButtonClick(RecordActivity.class));
    }

    //通貨表示に変更する
    public String changeMoneyFormat(int money){

        // 日本円のフォーマットを取得
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(Locale.JAPAN);
        //通貨シンボルを削除

        return numberFormat.format(money).replace(numberFormat.getCurrency().getSymbol(), "");
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



        binding.incometext.setText(String.valueOf(changeMoneyFormat(totalIncome)));
        binding.expensetext.setText(String.valueOf(changeMoneyFormat(totalExpense)));
        binding.sumtext.setText(String.valueOf(changeMoneyFormat(totalIncome - totalExpense)));
    }

    private void updateData() {
        // 選択された年月に対応するデータを取得
        String selectedYear = ((Spinner) findViewById(R.id.yearSpinner)).getSelectedItem().toString();
        String selectedMonth = ((Spinner) findViewById(R.id.monthSpinner)).getSelectedItem().toString();
        String yearMonth = selectedYear + "-" + selectedMonth;

        // LiveDataを取得
        LiveData<List<DataRoom>> dataByYearMonth = recordViewModel.getDataByYearMonth(yearMonth);

        // LiveDataの変更を監視
        dataByYearMonth.observe(this, newData -> {
            // データが変更されたときの処理
            updateMoney(newData);
            itemAdapter.setData(newData);
        });
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