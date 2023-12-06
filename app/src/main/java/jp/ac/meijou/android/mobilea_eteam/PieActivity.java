package jp.ac.meijou.android.mobilea_eteam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import jp.ac.meijou.android.mobilea_eteam.databinding.ActivityPieBinding;
import jp.ac.meijou.android.mobilea_eteam.databinding.ActivityTotalBinding;

public class PieActivity extends AppCompatActivity {

    private ActivityPieBinding binding;
    private ButtonClickListener buttonClickListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPieBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        buttonClickListener = new ButtonClickListener(this);

        binding.includedLayout.button.setOnClickListener(view -> buttonClickListener.onButtonClick(MainActivity.class));
        binding.includedLayout.button4.setOnClickListener(view -> buttonClickListener.onButtonClick(totalActivity.class));
        binding.includedLayout.button3.setOnClickListener(view -> buttonClickListener.onButtonClick(LineActivity.class));
    }
}