package jp.ac.meijou.android.mobilea_eteam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import jp.ac.meijou.android.mobilea_eteam.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.button4.setOnClickListener(view -> {
            var intent = new Intent(this, totalActivity.class);
            startActivity(intent);
        });

//AddMoneyMemoに遷移
//        binding.button6.setOnClickListener(view -> {
//            var intent = new Intent(this, totalActivity.class);
//            startActivity(intent);
//        });

    }

}