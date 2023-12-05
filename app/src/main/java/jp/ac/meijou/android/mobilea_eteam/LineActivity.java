package jp.ac.meijou.android.mobilea_eteam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import jp.ac.meijou.android.mobilea_eteam.databinding.ActivityLineBinding;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LineActivity extends AppCompatActivity {

    private ActivityLineBinding binding;
    private LineChart lineChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLineBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.button5.setOnClickListener(view -> {
            var intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        List<Float> x = new ArrayList<>(Arrays.asList(1f, 2f, 3f, 5f, 8f, 13f, 21f, 34f)); // X軸データ
        List<Float> y = new ArrayList<>();
        for (Float value : x) {
            y.add(value * value); // Y軸データ（X軸の2乗）
        }

        // Entryにデータ格納
        List<Entry> entryList = new ArrayList<>();
        for (int i = 0; i < x.size(); i++) {
            entryList.add(new Entry(x.get(i), y.get(i)));
        }

        // LineDataSetのList
        List<ILineDataSet> lineDataSets = new ArrayList<>();

        // DataSetにデータ格納
        LineDataSet lineDataSet = new LineDataSet(entryList, "square");

        // DataSetにフォーマット指定（3章で詳説）
        lineDataSet.setColor(Color.BLUE);

        // リストに格納
        lineDataSets.add(lineDataSet);

        // LineDataにLineDataSet格納
        LineData lineData = new LineData(lineDataSets);

        // LineChartにLineData格納
        lineChart = findViewById(R.id.lineChartExample); // LineChartのIDを指定
        lineChart.setData(lineData);

        // Chartのフォーマット指定（3章で詳説）

        // X軸の設定
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setEnabled(true);
        xAxis.setTextColor(Color.BLACK);

        // LineChart更新
        lineChart.invalidate();
    }
}