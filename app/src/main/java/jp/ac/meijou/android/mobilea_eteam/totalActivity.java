package jp.ac.meijou.android.mobilea_eteam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import jp.ac.meijou.android.mobilea_eteam.databinding.ActivityTotalBinding;

public class totalActivity extends AppCompatActivity {
    private ActivityTotalBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTotalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.button5.setOnClickListener(view -> {
            var intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
    }
}