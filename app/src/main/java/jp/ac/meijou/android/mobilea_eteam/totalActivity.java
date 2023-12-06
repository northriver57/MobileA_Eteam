package jp.ac.meijou.android.mobilea_eteam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.RadioGroup;

import java.util.Calendar;
import java.util.List;

import jp.ac.meijou.android.mobilea_eteam.databinding.ActivityTotalBinding;

public class totalActivity extends AppCompatActivity {
    private ActivityTotalBinding binding;
    private RecordViewModel recordViewModel;

    int check = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTotalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        // RecordViewModelを初期化
        recordViewModel = new ViewModelProvider(this).get(RecordViewModel.class);

        // LiveDataを取得
        LiveData<List<DataRoom>> allData = recordViewModel.getAllData();

        // データが変更されたときの処理
        allData.observe(this, this::createData);


        RadioGroup group = findViewById(R.id.radioGroup);
        group.setOnCheckedChangeListener((view, id) -> {
            if (id == R.id.RadioCurrent) {
                check = 1;
                allData.observe(this, this::createData);
            } else if (id == R.id.RadioFuture) {
                check = 2;
                allData.observe(this, this::createData);
            }
        });


    }

    private void createData(List<DataRoom> newData) {
        int totalCash = 0;
        int totalEMoney = 0;
        int totalBank = 0;
        int totalEtc = 0;
        int total;
        long todayDate;

        // 今日の日付を取得
        Calendar today = Calendar.getInstance();
        todayDate = today.getTimeInMillis();

        for (DataRoom data : newData) {

            if(check == 1 && data.getDate() <= todayDate){
                if ("現金".equals(data.getAsset())) {
                    if(data.getType() == 1){
                        totalCash += data.getPrice();
                    } else if (data.getType() == 2) {
                        totalCash -= data.getPrice();
                    }

                }else if ("電子マネー".equals(data.getAsset())) {
                    if(data.getType() == 1){
                        totalEMoney += data.getPrice();
                    } else if (data.getType() == 2) {
                        totalEMoney -= data.getPrice();
                    }
                }
                else if ("銀行".equals(data.getAsset())) {
                    if(data.getType() == 1){
                        totalBank += data.getPrice();
                    } else if (data.getType() == 2) {
                        totalBank -= data.getPrice();
                    }
                }
                else if ("その他".equals(data.getAsset())) {
                    if(data.getType() == 1){
                        totalEtc += data.getPrice();
                    } else if (data.getType() == 2) {
                        totalEtc -= data.getPrice();
                    }
                }
            }
            else if (check == 2) {
                if ("現金".equals(data.getAsset())) {
                    if(data.getType() == 1){
                        totalCash += data.getPrice();
                    } else if (data.getType() == 2) {
                        totalCash -= data.getPrice();
                    }

                }else if ("電子マネー".equals(data.getAsset())) {
                    if(data.getType() == 1){
                        totalEMoney += data.getPrice();
                    } else if (data.getType() == 2) {
                        totalEMoney -= data.getPrice();
                    }
                }
                else if ("銀行".equals(data.getAsset())) {
                    if(data.getType() == 1){
                        totalBank += data.getPrice();
                    } else if (data.getType() == 2) {
                        totalBank -= data.getPrice();
                    }
                }
                else if ("その他".equals(data.getAsset())) {
                    if(data.getType() == 1){
                        totalEtc += data.getPrice();
                    } else if (data.getType() == 2) {
                        totalEtc -= data.getPrice();
                    }
                }
            }

        }

        total = totalCash + totalEMoney + totalBank + totalEtc;
        binding.cashview.setText(String.valueOf(totalCash));
        binding.emoneyview.setText(String.valueOf(totalEMoney));
        binding.bankview.setText(String.valueOf(totalBank));
        binding.etcview.setText(String.valueOf(totalEtc));
        binding.totalview.setText(String.valueOf(total));
    }
}