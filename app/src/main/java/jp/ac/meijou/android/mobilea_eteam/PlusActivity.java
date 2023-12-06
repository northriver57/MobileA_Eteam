package jp.ac.meijou.android.mobilea_eteam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import jp.ac.meijou.android.mobilea_eteam.databinding.ActivityPlusBinding;
import jp.ac.meijou.android.mobilea_eteam.databinding.ActivityTotalBinding;

public class PlusActivity extends AppCompatActivity {

    private ActivityPlusBinding binding;
    private ButtonClickListener buttonClickListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlusBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        buttonClickListener = new ButtonClickListener(this);

        binding.includedLayout.button.setOnClickListener(view -> buttonClickListener.onButtonClick(MainActivity.class));
        binding.includedLayout.button4.setOnClickListener(view -> buttonClickListener.onButtonClick(totalActivity.class));
        binding.includedLayout.button3.setOnClickListener(view -> buttonClickListener.onButtonClick(LineActivity.class));
        binding.includedLayout.button2.setOnClickListener(view -> buttonClickListener.onButtonClick(PieActivity.class));



    }
}