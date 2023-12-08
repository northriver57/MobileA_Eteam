package jp.ac.meijou.android.mobilea_eteam;




import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import java.util.Calendar;
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
    private Spinner yearSpinner;
    private int selectedYear;
    private RecordViewModel recordViewModel;
    private LiveData<List<DataRoom>> allData; // 新たに追加

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLineBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        spinner = findViewById(R.id.spinner);
        yearSpinner = findViewById(R.id.yearSpinner);
        // RecordViewModelを初期化
        recordViewModel = new ViewModelProvider(this).get(RecordViewModel.class);

        // LiveDataを取得
        allData = recordViewModel.getAllData();

        // LiveDataの変更を監視
        allData.observe(this, newData -> {
            // データが変更されたときの処理
            updateLineChart(newData);
            setInitialYearMonth();
        });
        // LineChartの初期化spinner
        lineChart = findViewById(R.id.lineChartExample);
        setupYearSpinner();
        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Object selectedItem = parentView.getItemAtPosition(position);
                if (selectedItem instanceof String) {
                    // 文字列を整数に変換
                    try {
                        selectedYear = Integer.parseInt((String) selectedItem);
                        updateLineChartForCategoryAndYear(allData.getValue(), selectedYear);
                    } catch (NumberFormatException e) {
                        // 整数に変換できない場合のエラー処理
                        e.printStackTrace();
                    }
                } else {
                    // 予期しない型の場合のエラー処理
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // 何も選択されていない場合の処理
            }

        });
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
        binding.includedLayout.button2.setOnClickListener(view -> buttonClickListener.onButtonClick(GraphActivity.class));

    }

    public class MonthAxisValueFormatter extends ValueFormatter {
        @Override
        public String getAxisLabel(float value, AxisBase axis) {
            // 常に1から12までの月を表示
            int month = ((int) value % 12) + 1;
            return month + "月";
        }
    }
    private int getIndex(Spinner spinner, String value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(value)) {
                return i;
            }
        }
        return 0; // マッチしなかった場合は最初の要素を選択
    }

    private void setInitialYearMonth() {
        // 現在の年を取得
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);

        // 年の Spinner に初期値をセット
        String yearString = String.valueOf(currentYear);
        yearSpinner.setSelection(getIndex(yearSpinner, yearString));
    }




    private void updateLineChart(List<DataRoom> newData) {
        // カテゴリーごとの月ごとの合計を算出
        Map<String, Map<Integer, Float>> categoryMonthTotal = new HashMap<>();

        for (DataRoom data : newData) {
            String category = data.getClassification(); // カテゴリー名を取得
            int month = data.getMonth(); // 月を取得

            if (!categoryMonthTotal.containsKey(category)) {
                categoryMonthTotal.put(category, new HashMap<>());
            }

            Map<Integer, Float> monthTotal = categoryMonthTotal.get(category);

            if (!monthTotal.containsKey(month)) {
                monthTotal.put(month, 0f);
            }

            float total = monthTotal.get(month);
            total += data.getPrice();
            monthTotal.put(month, total);
        }

        // グラフデータの作成
        List<Entry> entryList = new ArrayList<>();

        // カテゴリーごとにデータを抽出
        for (Map<Integer, Float> monthTotal : categoryMonthTotal.values()) {
            for (Map.Entry<Integer, Float> entry : monthTotal.entrySet()) {
                entryList.add(new Entry(entry.getKey() - 1, entry.getValue()));
            }
        }

        List<ILineDataSet> lineDataSets = new ArrayList<>();
        LineDataSet lineDataSet = new LineDataSet(entryList, "Data");
        lineDataSet.setColor(Color.BLUE);
        lineDataSet.setLineWidth(2f);
        lineDataSets.add(lineDataSet);

        LineData lineData = new LineData(lineDataSets);

        // 以前のデータをクリア
        lineChart.clear();

        // LineChartの設定
        lineChart.setData(lineData);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setEnabled(true);
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        // X軸に月の名前を表示するための設定
        xAxis.setValueFormatter(new MonthAxisValueFormatter());
        xAxis.setGranularity(1f);
        lineChart.getDescription().setEnabled(false);

        // Y軸の設定
        YAxis leftYAxis = lineChart.getAxisLeft();
        leftYAxis.setDrawGridLines(false);
        leftYAxis.setAxisMinimum(0f);
        leftYAxis.setAxisMaximum(50000f);
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getAxisRight().setDrawGridLines(false);
        lineChart.getAxisRight().setEnabled(false);

        lineChart.invalidate();
    }


    private List<String> generateYearList() {
        List<String> years = new ArrayList<>();
        // ここで年のリストを生成するロジックを実装
        // 例: 現在の年から過去10年分をリストに追加
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 0; i < 10; i++) {
            years.add(String.valueOf(currentYear - i));
        }
        return years;
    }
    private void updateLineChartForCategoryAndYear(List<DataRoom> newData, int selectedYear) {
        // 選択された年にフィルタリング
        List<DataRoom> filteredData = new ArrayList<>();
        for (DataRoom data : newData) {
            if (data.getYear() == selectedYear) {
                filteredData.add(data);
            }
        }

        // グラフデータを更新
        updateLineChart(filteredData);
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



    private void setupYearSpinner() {
        List<String> years = generateYearList();

        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, years);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);
    }
}