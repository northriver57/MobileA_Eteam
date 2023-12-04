package jp.ac.meijou.android.mobilea_eteam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.ac.meijou.android.mobilea_eteam.databinding.ActivityGraphBinding;

public class GraphActivity extends AppCompatActivity {

    private ActivityGraphBinding binding;

    private RecordViewModel recordViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGraphBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        // RecordViewModelを初期化
        recordViewModel = new ViewModelProvider(this).get(RecordViewModel.class);

        // LiveDataを取得
        LiveData<List<DataRoom>> allData = recordViewModel.getAllData();

        // LiveDataの変更を監視
        allData.observe(this, newData -> {
            // データが変更されたときの処理

            createPieChart(newData);
        });


    }


    private int updateSumPrice(List<DataRoom> newData){
        int sum = 0;

        for (DataRoom data : newData) {
            sum += data.getPrice();
        }

        return sum;
    }

    private void createPieChart(List<DataRoom> newData) {

        binding.pieChart.setDrawHoleEnabled(true);//穴あけ
        binding.pieChart.setHoleRadius(30f);//穴サイズ％
        binding.pieChart.setTransparentCircleRadius(10f);
        binding.pieChart.setRotationEnabled(false);
        binding.pieChart.getLegend().setEnabled(true);
        binding.pieChart.getDescription().setEnabled(false);//Description Labelの表示を消す
        binding.pieChart.setData(ChartData(newData));
        binding.pieChart.getData().setValueFormatter(new AmountValueFormatter());

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
        colors.add(ColorTemplate.COLORFUL_COLORS[0]);
        colors.add(ColorTemplate.COLORFUL_COLORS[1]);
        colors.add(ColorTemplate.COLORFUL_COLORS[2]);
        dataSet.setColors(colors);
        dataSet.setDrawValues(true);


        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setDrawValues(true);


        // テキストの設定
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.WHITE);
        return data;


    }

    public class AmountValueFormatter extends ValueFormatter {
        private final DecimalFormat format;

        public AmountValueFormatter() {
            // 金額のフォーマットを指定
            format = new DecimalFormat("#,###");
        }

        @Override
        public String getFormattedValue(float value) {
            // カンマで区切った金額を返す
            return format.format(value);
        }

        @Override
        public String getPieLabel(float value, PieEntry pieEntry) {
            // カンマで区切った金額を返す
            return format.format(value);
        }
    }


}