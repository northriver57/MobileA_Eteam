package jp.ac.meijou.android.mobilea_eteam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import jp.ac.meijou.android.mobilea_eteam.databinding.ActivityTotalBinding;

public class totalActivity extends AppCompatActivity {
    private ActivityTotalBinding binding;
    private ButtonClickListener buttonClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTotalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        buttonClickListener = new ButtonClickListener(this);

        binding.includedLayout.button.setOnClickListener(view -> buttonClickListener.onButtonClick(MainActivity.class));
        binding.includedLayout.button3.setOnClickListener(view -> buttonClickListener.onButtonClick(LineActivity.class));
        binding.includedLayout.button2.setOnClickListener(view -> buttonClickListener.onButtonClick(PieActivity.class));

    }
}