package jp.ac.meijou.android.mobilea_eteam;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import jp.ac.meijou.android.mobilea_eteam.databinding.ActivityGraphBinding;

public class GraphActivity extends AppCompatActivity {

    private ActivityGraphBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGraphBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        createPieChart();
    }

    private void createPieChart() {

        binding.pieChart.setDrawHoleEnabled(true);//穴あけ
        binding.pieChart.setHoleRadius(50f);//穴サイズ％
        binding.pieChart.setTransparentCircleRadius(55f);
        binding.pieChart.setRotationAngle(270);//回転開始位置
        binding.pieChart.setRotationEnabled(true);
        binding.pieChart.getLegend().setEnabled(true);
    //    binding.pieChart.setDescription("PieChart");
        binding.pieChart.setData(ChartData());

        binding.pieChart.invalidate();//更新
        binding.pieChart.animateXY(2000,2000);//表示アニメーション
    }

    private PieData ChartData() {
        ArrayList<PieEntry> yVals = new ArrayList<>();
        ArrayList<String> xVals = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();

        xVals.add("A");
        xVals.add("B");
        xVals.add("C");

        yVals.add(new PieEntry(20, 0));
        yVals.add(new PieEntry(30, 1));
        yVals.add(new PieEntry(50, 2));

        PieDataSet dataSet = new PieDataSet(yVals, "Data");
        dataSet.setSliceSpace(5f);
        dataSet.setSelectionShift(1f);

        // 色の設定
        colors.add(ColorTemplate.COLORFUL_COLORS[0]);
        colors.add(ColorTemplate.COLORFUL_COLORS[1]);
        colors.add(ColorTemplate.COLORFUL_COLORS[2]);
        dataSet.setColors(colors);
        dataSet.setDrawValues(true);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());

        // テキストの設定
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.WHITE);
        return data;
    }

}