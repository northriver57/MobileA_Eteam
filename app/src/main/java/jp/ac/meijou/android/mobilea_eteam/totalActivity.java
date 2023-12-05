package jp.ac.meijou.android.mobilea_eteam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import java.util.List;

import jp.ac.meijou.android.mobilea_eteam.databinding.ActivityTotalBinding;

public class totalActivity extends AppCompatActivity {
    private ActivityTotalBinding binding;
    private RecordViewModel recordViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTotalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        // RecordViewModelを初期化
        recordViewModel = new ViewModelProvider(this).get(RecordViewModel.class);

        // LiveDataを取得
        LiveData<List<DataRoom>> allData = recordViewModel.getAllData();

        // LiveDataの変更を監視
        allData.observe(this, newData -> {
            // データが変更されたときの処理

            createData(newData);
        });

    }

    private void createData(List<DataRoom> newData) {
        int totalCash = 0;
        int totalEMoney = 0;
        int totalBank = 0;
        int totalEtc = 0;
        int total;

        for (DataRoom data : newData) {

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

        total = totalCash + totalEMoney + totalBank + totalEtc;
        binding.cashview.setText(String.valueOf(totalCash));
        binding.emoneyview.setText(String.valueOf(totalEMoney));
        binding.bankview.setText(String.valueOf(totalBank));
        binding.etcview.setText(String.valueOf(totalEtc));
        binding.totalview.setText(String.valueOf(total));
    }
}