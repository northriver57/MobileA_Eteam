package jp.ac.meijou.android.mobilea_eteam;


import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import java.text.NumberFormat;

import jp.ac.meijou.android.mobilea_eteam.databinding.ActivityGraphBinding;

public class GraphActivity extends AppCompatActivity {

    private ActivityGraphBinding binding;

    private RecordViewModel recordViewModel;
    private ButtonClickListener buttonClickListener;
    private Spinner spinnerYear;
    private Spinner spinnerMonth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGraphBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        buttonClickListener = new ButtonClickListener(this);

        binding.includedLayout.button.setOnClickListener(view -> buttonClickListener.onButtonClick(MainActivity.class));
        binding.includedLayout.button4.setOnClickListener(view -> buttonClickListener.onButtonClick(totalActivity.class));
        binding.includedLayout.button3.setOnClickListener(view -> buttonClickListener.onButtonClick(LineActivity.class));

        // Spinnerを取得
        spinnerYear = findViewById(R.id.yearSpinner);
        spinnerMonth = findViewById(R.id.monthSpinner);

        // 年のデータをセット
        ArrayAdapter<CharSequence> yearAdapter = ArrayAdapter.createFromResource(
                this, R.array.years, android.R.layout.simple_spinner_item);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYear.setAdapter(yearAdapter);

        // 月のデータをセット
        ArrayAdapter<CharSequence> monthAdapter = ArrayAdapter.createFromResource(
                this, R.array.months, android.R.layout.simple_spinner_item);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonth.setAdapter(monthAdapter);

        // 初期値を現在の年月にする
        setInitialYearMonth();

        // Spinnerのイベントリスナーをセット
        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Pie(); // プルダウンが選択されたら円グラフのテキスト更新メソッドを呼ぶ
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // 何もしない
            }
        });

        spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Pie(); // プルダウンが選択されたら円グラフのテキスト更新メソッドを呼ぶ
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // 何もしない
            }
        });



    }

    private void Pie(){
        // RecordViewModelを初期化
        recordViewModel = new ViewModelProvider(this).get(RecordViewModel.class);

        String selectedYear = (String) spinnerYear.getSelectedItem();
        String selectedMonth = (String) spinnerMonth.getSelectedItem();
        // 特定の年月のデータを取得
        String targetYearMonth = String.format("%s-%s", selectedYear, selectedMonth); // 取得したい年月を指定
        LiveData<List<DataRoom>> dataByYearMonth = recordViewModel.getDataByYearMonth(targetYearMonth);

        // LiveDataの変更を監視
        dataByYearMonth.observe(this, newData -> {
            // データが変更されたときの処理

            createPieChart(newData, selectedYear, selectedMonth);

        });
    }


    private void setInitialYearMonth() {
        // 現在の年月を取得
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1; // 0-indexedなので+1する

        // 年と月の Spinner に初期値をセット
        String yearString = String.valueOf(currentYear);
        String monthString = String.format(Locale.getDefault(), "%02d", currentMonth);
        spinnerYear.setSelection(getIndex(spinnerYear, yearString));
        spinnerMonth.setSelection(getIndex(spinnerMonth, monthString));
    }

    private int getIndex(Spinner spinner, String value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(value)) {
                return i;
            }
        }
        return 0; // マッチしなかった場合は最初の要素を選択
    }

    //取得したデータリストから数値の合計を返す
    private int updateSumPrice(List<DataRoom> newData){
        int sum = 0;

        for (DataRoom data : newData) {
            sum += data.getPrice();
        }

        return sum;
    }

    //円グラフを作成する
    private void createPieChart(List<DataRoom> newData, String selectedYear, String selectedMonth) {

        binding.pieChart.setDrawHoleEnabled(true);//穴あけ
        binding.pieChart.setHoleRadius(30f);//穴サイズ％
        binding.pieChart.setTransparentCircleRadius(10f);
        binding.pieChart.setRotationEnabled(false);
        binding.pieChart.getLegend().setEnabled(true);
        binding.pieChart.getDescription().setEnabled(false);//Description Labelの表示を消す
        binding.pieChart.setCenterTextColor(Color.BLACK);
        binding.pieChart.setCenterTextSize(14f);
        binding.pieChart.setDrawEntryLabels(false);// エントリーラベルを非表示にする

        int sumprice = updateSumPrice(newData);

        // 日本円のフォーマットを取得
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(Locale.JAPAN);

        // 通貨シンボルを削除
        String formattedAmount = numberFormat.format(sumprice).replace(numberFormat.getCurrency().getSymbol(), "");

        String centerText = String.format("%s年%s月\n総支出\n%s円", selectedYear, selectedMonth, formattedAmount);// グラフの中央に表示するテキスト
        binding.pieChart.setCenterText(centerText);// グラフの中央にテキストを設定

        binding.pieChart.setData(ChartData(newData));


        binding.pieChart.invalidate();//更新
    }

    private PieData ChartData(List<DataRoom> newData) {
        ArrayList<PieEntry> rate = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();

        //updateTextTest(newData);
        Map<String, List<DataRoom>> categorizedData = new HashMap<>();

        // 初期化
        for (String genre : getResources().getStringArray(R.array.ListClass)) {
            categorizedData.put(genre, new ArrayList<>());
        }

        // ジャンルごとにデータを分類
        for (DataRoom data : newData) {
            String genre = data.getClassification(); // ここでジャンル名を取得（getName()がジャンル名に対応すると仮定）

            if (categorizedData.containsKey(genre)) {
                categorizedData.get(genre).add(data);
            }
        }

        //資産の別でリストに格納
        List<DataRoom> LivingExpenses = categorizedData.get("生活費");
        List<DataRoom> foodExpensesData = categorizedData.get("食費");
        List<DataRoom> ClothingData = categorizedData.get("衣類");
        List<DataRoom> TransportationExpensesData = categorizedData.get("交通費");
        List<DataRoom> MedicalExpensesData = categorizedData.get("医療費");
        List<DataRoom> SocialExpensesData = categorizedData.get("交際費");
        List<DataRoom> EntertainmentData = categorizedData.get("娯楽");
        List<DataRoom> CommunicationExpensesData = categorizedData.get("通信費");
        List<DataRoom> MiscellaneousExpensesData = categorizedData.get("雑費");
        List<DataRoom> otherData = categorizedData.get("その他");

        Log.v("tag", "" + TransportationExpensesData + TransportationExpensesData.size());

        //割合とそのラベルを直接入力
        if(LivingExpenses.size() > 0) rate.add(new PieEntry(updateSumPrice(LivingExpenses), "生活費"));
        if(foodExpensesData.size() > 0) rate.add(new PieEntry((float) updateSumPrice(foodExpensesData), "食費"));
        if(ClothingData.size() > 0) rate.add(new PieEntry((float) updateSumPrice(ClothingData), "衣類"));
        if(TransportationExpensesData.size() > 0) rate.add(new PieEntry((float) updateSumPrice(TransportationExpensesData), "交通費"));
        if(MedicalExpensesData.size() > 0) rate.add(new PieEntry((float) updateSumPrice(MedicalExpensesData), "医療費"));
        if(SocialExpensesData.size() > 0) rate.add(new PieEntry((float) updateSumPrice(SocialExpensesData), "交際費"));
        if(EntertainmentData.size() > 0) rate.add(new PieEntry((float) updateSumPrice(EntertainmentData), "娯楽費"));
        if(CommunicationExpensesData.size() > 0) rate.add(new PieEntry((float) updateSumPrice(CommunicationExpensesData), "通信費"));
        if(MiscellaneousExpensesData.size() > 0) rate.add(new PieEntry((float) updateSumPrice(MiscellaneousExpensesData), "雑費"));
        if(otherData.size() > 0) rate.add(new PieEntry((float) updateSumPrice(otherData), "その他"));


        PieDataSet dataSet = new PieDataSet(rate, "Data");
        dataSet.setSliceSpace(5f);
        dataSet.setSelectionShift(0f);

        // 色の設定
        colors.add(Color.parseColor("#1f77b4"));
        colors.add(Color.parseColor("#ff7f0e"));
        colors.add(Color.parseColor("#2ca02c"));
        colors.add(Color.parseColor("#d62728"));
        colors.add(Color.parseColor("#9467bd"));
        colors.add(Color.parseColor("#17becf"));
        colors.add(Color.parseColor("#e377c2"));
        colors.add(Color.parseColor("#bcbd22"));
        colors.add(Color.parseColor("#7f7f7f"));
        colors.add(Color.parseColor("#8c564b"));

        dataSet.setColors(colors);

        //データラベルの設定
        dataSet.setDrawValues(false);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setDrawValues(false);


        return data;

    }

}