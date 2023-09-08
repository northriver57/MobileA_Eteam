package jp.ac.meijou.android.mobilea_eteam;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;

import java.util.Optional;

public class AddMoneyMemoActivity extends AppCompatActivity {

    private ActivityAddMoneyMemoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddMoneyMemo.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.AddCompleteButton.setOnClickListener(view -> {
            var intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
    }
}