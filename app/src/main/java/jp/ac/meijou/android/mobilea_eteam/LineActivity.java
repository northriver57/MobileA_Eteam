package jp.ac.meijou.android.mobilea_eteam;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.ValueFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jp.ac.meijou.android.mobilea_eteam.databinding.ActivityLineBinding;

public class LineActivity extends AppCompatActivity {

    private ActivityLineBinding binding;
    private ButtonClickListener buttonClickListener;
    private LineChart lineChart;
    private Spinner spinner;
    private RecordViewModel recordViewModel;
    private LiveData<List<DataRoom>> allData; // 新たに追加

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLineBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        spinner = findViewById(R.id.spinner);

        // RecordViewModelを初期化
        recordViewModel = new ViewModelProvider(this).get(RecordViewModel.class);

        // LiveDataを取得
        allData = recordViewModel.getAllData();

        // LiveDataの変更を監視
        allData.observe(this, newData -> {
            // データが変更されたときの処理
            updateLineChart(newData);
        });

        // LineChartの初期化spinner
        lineChart = findViewById(R.id.lineChartExample);

        // スピナーの選択が変更されたときのリスナーを設定
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // 選択されたカテゴリー名を取得
                String selectedCategory = parentView.getItemAtPosition(position).toString();

                // グラフデータを更新
                updateLineChartForCategory(allData.getValue(), selectedCategory); // allDataから値を取得
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // 何も選択されていない場合の処理
            }
        });

        // グラフデータの更新
        buttonClickListener = new ButtonClickListener(this);
        binding.includedLayout.button.setOnClickListener(view -> buttonClickListener.onButtonClick(MainActivity.class));
        binding.includedLayout.button4.setOnClickListener(view -> buttonClickListener.onButtonClick(totalActivity.class));
        binding.includedLayout.button2.setOnClickListener(view -> buttonClickListener.onButtonClick(PieActivity.class));
    }

    public class CategoryAxisValueFormatter extends ValueFormatter {

        @Override
        public String getAxisLabel(float value, AxisBase axis) {
            // X軸の値(value)をカテゴリーの名前に変換して表示
            return String.valueOf((int) value);
        }
    }

    private void updateLineChart(List<DataRoom> newData) {
        // カテゴリーごとの合計を算出
        Map<String, Float> categoryTotal = new HashMap<>();

        for (DataRoom data : newData) {
            String category = data.getClassification(); // カテゴリー名を取得

            if (!categoryTotal.containsKey(category)) {
                categoryTotal.put(category, 0f);
            }

            float total = categoryTotal.get(category);
            total += data.getPrice();
            categoryTotal.put(category, total);
        }

        // グラフデータの作成
        List<Entry> entryList = new ArrayList<>();

        // カテゴリーごとにデータを抽出
        for (Map.Entry<String, Float> entry : categoryTotal.entrySet()) {
            entryList.add(new Entry(entryList.size(), entry.getValue()));
        }

        List<ILineDataSet> lineDataSets = new ArrayList<>();
        LineDataSet lineDataSet = new LineDataSet(entryList, "Data");
        lineDataSet.setColor(Color.BLUE);
        lineDataSet.setLineWidth(2f);
        lineDataSets.add(lineDataSet);

        LineData lineData = new LineData(lineDataSets);

        // LineChartの設定
        lineChart.setData(lineData);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setEnabled(true);
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        // X軸にカテゴリー名を表示するための設定
        List<String> categories = new ArrayList<>(categoryTotal.keySet());
        xAxis.setValueFormatter(new IndexAxisValueFormatter(categories));
        xAxis.setGranularity(1f);
        lineChart.getDescription().setEnabled(false);

        // Y軸の設定
        YAxis leftYAxis = lineChart.getAxisLeft();
        leftYAxis.setDrawGridLines(false);
        leftYAxis.setAxisMinimum(0f);
        leftYAxis.setAxisMaximum(100000f);
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getAxisRight().setDrawGridLines(false);
        lineChart.getAxisRight().setEnabled(false);

        lineChart.invalidate();
    }

    private void updateLineChartForCategory(List<DataRoom> newData, String selectedCategory) {
        // 選択されたカテゴリーにフィルタリング
        List<DataRoom> filteredData = new ArrayList<>();
        for (DataRoom data : newData) {
            if (data.getClassification().equals(selectedCategory)) {
                filteredData.add(data);
            }
        }

        // グラフデータを更新
        updateLineChart(filteredData);
    }
}







